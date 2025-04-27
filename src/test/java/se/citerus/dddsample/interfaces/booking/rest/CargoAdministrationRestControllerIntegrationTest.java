package se.citerus.dddsample.interfaces.booking.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.PUT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
class CargoAdministrationRestControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("dddsample")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldChangeCargoDestination() {
        // Given
        String trackingId = "ABC123";
        String newDestination = "SESTO";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("unlocode", newDestination), headers);

        // When
        ResponseEntity<?> response = restTemplate.exchange(
            "/api/cargos/{trackingId}/destination",
            PUT,
            request,
            Void.class,
            trackingId
        );

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void shouldRegisterNewCargo() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> requestBody = Map.of(
            "originUnlocode", "SESTO",
            "destinationUnlocode", "FIHEL",
            "arrivalDeadline", Instant.now().plusSeconds(60 * 60 * 24).toString()
        );
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // When
        ResponseEntity<?> response = restTemplate.postForEntity("/api/cargos", request, Void.class);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void shouldGetRouteCandidates() {
        // Given
        String trackingId = "ABC123";

        // When
        ResponseEntity<?> response = restTemplate.getForEntity(
            "/api/cargos/{trackingId}/routes",
            Void.class,
            trackingId
        );

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
} 