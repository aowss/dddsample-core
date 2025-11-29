package se.citerus.dddsample.interfaces.booking.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import se.citerus.dddsample.interfaces.booking.facade.dto.CargoRoutingDTO;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class CargoAdministrationRestControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    private MediaType jsonMediaType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.
                webAppContextSetup(context).
                build();
    }

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("dddsample")
            .withUsername("test")
            .withPassword("test")
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("schema.sql"), "/docker-entrypoint-initdb.d/0-schema.sql"
            )
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("locations.sql"), "/docker-entrypoint-initdb.d/1-locations.sql"
            );;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @BeforeEach
//    void setUp() {
//        // Insert test voyage
//        jdbcTemplate.update(
//            "INSERT INTO Voyage (id, voyage_number) VALUES (?, ?)",
//            13, "V100S"
//        );
//
//        // Insert carrier movements for the test voyage
//        jdbcTemplate.update(
//            "INSERT INTO CarrierMovement (id, departure_location_id, arrival_location_id, departure_time, arrival_time, voyage_id) " +
//            "VALUES (?, ?, ?, ?, ?, ?)",
//            1, 12, 13, // From New York to Dallas
//            Instant.now().plusSeconds(3600),
//            Instant.now().plusSeconds(7200),
//            1
//        );
//
//        // Insert test cargo
//        jdbcTemplate.update(
//            "INSERT INTO Cargo (id, tracking_id, origin_id, spec_origin_id, spec_destination_id, spec_arrival_deadline, transport_status, routing_status) " +
//            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
//            1, "ABC123", 12, 12, 13, // From New York to Dallas
//            Instant.now().plusSeconds(86400),
//            "IN_PORT", "ROUTED"
//        );
//
//        // Insert leg for the test cargo
//        jdbcTemplate.update(
//            "INSERT INTO Leg (id, voyage_id, load_location_id, unload_location_id, load_time, unload_time, cargo_id) " +
//            "VALUES (?, ?, ?, ?, ?, ?, ?)",
//            1, 1, 12, 13,
//            Instant.now().plusSeconds(3600),
//            Instant.now().plusSeconds(7200),
//            1
//        );
//    }

//    @BeforeEach
    void deleteData() {
        var tables = List.of("HandlingEvent", "Leg", "Cargo", "CarrierMovement", "Voyage");
        tables.forEach(table -> System.out.println("table " + table + ":\n" + jdbcTemplate.queryForList("SELECT * FROM " + table)));
        tables.forEach(table -> {
            System.out.println("delete table " + table);
            jdbcTemplate.update("DELETE FROM " + table);
        });
//        jdbcTemplate.update("DELETE FROM HandlingEvent");
//        jdbcTemplate.update("DELETE FROM Leg");
//        jdbcTemplate.update("DELETE FROM Cargo");
//        jdbcTemplate.update("DELETE FROM CarrierMovement");
//        jdbcTemplate.update("DELETE FROM Voyage");
        tables.forEach(table -> System.out.println("table " + table + " after delete:\n" + jdbcTemplate.queryForList("SELECT * FROM " + table)));
    }

    @Test
    void shouldChangeCargoDestination() {
        // Given
        String trackingId = "ABC123";
        String newDestination = "SESTO";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("unLocode", newDestination), headers);

        // When
        ResponseEntity<CargoRoutingDTO> response = restTemplate.exchange(
            "/api/cargos/{trackingId}/destination",
            PUT,
            request,
            CargoRoutingDTO.class,
            trackingId
        );

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFinalDestination()).isEqualTo(newDestination);
    }

    @Test
//    @Sql("classpath:/locations.sql")
    void shouldRegisterNewCargo() throws Exception {
        // Given
        String body = Json.createObjectBuilder()
                .add("originUnlocode", "USNYC")
                .add("destinationUnlocode", "USDAL")
                .add("arrivalDeadline", Instant.now().plusSeconds(86400).toString())
                .build().toString();

        // When
        ResultActions result = mockMvc.
                perform(
                    post("/api/cargos")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        // Then
        result
                .andExpect(status().isCreated()).
                andExpect(content().contentType(jsonMediaType)).
                andExpect(jsonPath("$origin", is("USNYC")));
//        assertThat(response.getBody().getOrigin()).isEqualTo("USNYC");
//        assertThat(response.getBody().getFinalDestination()).isEqualTo("USDAL");
    }

    @Test
    void shouldGetRouteCandidates() {
        // Given
        String trackingId = "ABC123";

        // When
        ResponseEntity<List> response = restTemplate.getForEntity(
            "/api/cargos/{trackingId}/routes",
            List.class,
            trackingId
        );

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }
} 