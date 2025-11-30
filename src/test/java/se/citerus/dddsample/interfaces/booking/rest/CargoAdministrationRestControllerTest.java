package se.citerus.dddsample.interfaces.booking.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.citerus.dddsample.application.util.DateTestUtil;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.interfaces.booking.facade.BookingServiceFacade;
import se.citerus.dddsample.interfaces.booking.facade.dto.CargoRoutingDTO;
import se.citerus.dddsample.interfaces.booking.facade.dto.LegDTO;
import se.citerus.dddsample.interfaces.booking.facade.dto.LocationDTO;
import se.citerus.dddsample.interfaces.booking.facade.dto.RouteCandidateDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = CargoAdministrationRestControllerTest.TestConfiguration.class)
public class CargoAdministrationRestControllerTest {

    @Configuration
    static class TestConfiguration {}

    private MockMvc mockMvc;
    private BookingServiceFacade bookingServiceFacade;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        bookingServiceFacade = mock(BookingServiceFacade.class);
        CargoAdministrationRestController controller = new CargoAdministrationRestController(bookingServiceFacade);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @Test
    public void listLocations() throws Exception {
        LocationDTO location = new LocationDTO("USNYC", "New York");
        when(bookingServiceFacade.listShippingLocations()).thenReturn(List.of(location));

        mockMvc.perform(get("/api/locations"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].unLocode").value("USNYC"))
            .andExpect(jsonPath("$[0].name").value("New York"));
    }

    @Test
    public void listCargos() throws Exception {
        List<CargoRoutingDTO> cargoList = Arrays.asList(
            new CargoRoutingDTO("ABC123", "SESTO", "FIHEL", DateTestUtil.toInstant(2023, 1, 1), false)
        );
        cargoList.get(0).addLeg("V001", "SESTO", "DEHAM", DateTestUtil.toInstant(2023, 1, 1), DateTestUtil.toInstant(2023, 1, 2));
        cargoList.get(0).addLeg("V002", "DEHAM", "FIHEL", DateTestUtil.toInstant(2023, 1, 3), DateTestUtil.toInstant(2023, 1, 4));

        when(bookingServiceFacade.listAllCargos()).thenReturn(cargoList);

        mockMvc.perform(get("/api/cargos"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].trackingId").value("ABC123"))
            .andExpect(jsonPath("$[0].origin").value("SESTO"))
            .andExpect(jsonPath("$[0].finalDestination").value("FIHEL"))
            .andExpect(jsonPath("$[0].legs[0].voyageNumber").value("V001"))
            .andExpect(jsonPath("$[0].legs[0].from").value("SESTO"))
            .andExpect(jsonPath("$[0].legs[0].to").value("DEHAM"))
            .andExpect(jsonPath("$[0].legs[1].voyageNumber").value("V002"))
            .andExpect(jsonPath("$[0].legs[1].from").value("DEHAM"))
            .andExpect(jsonPath("$[0].legs[1].to").value("FIHEL"));
    }

    @Test
    public void registerCargo() throws Exception {
        CargoRoutingDTO cargo = new CargoRoutingDTO("ABC123", "SESTO", "FIHEL", DateTestUtil.toInstant(2023, 1, 1), false);
        cargo.addLeg("V001", "SESTO", "DEHAM", DateTestUtil.toInstant(2023, 1, 1), DateTestUtil.toInstant(2023, 1, 2));
        cargo.addLeg("V002", "DEHAM", "FIHEL", DateTestUtil.toInstant(2023, 1, 3), DateTestUtil.toInstant(2023, 1, 4));

        when(bookingServiceFacade.bookNewCargo(eq("SESTO"), eq("FIHEL"), any(Instant.class))).thenReturn("ABC123");
        when(bookingServiceFacade.loadCargoForRouting("ABC123")).thenReturn(cargo);

        CargoRegistrationRequest request = new CargoRegistrationRequest("SESTO", "FIHEL", LocalDate.of(2023, 1, 1));
        mockMvc.perform(post("/api/cargos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/cargos/ABC123"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.trackingId").value("ABC123"))
            .andExpect(jsonPath("$.origin").value("SESTO"))
            .andExpect(jsonPath("$.finalDestination").value("FIHEL"))
            .andExpect(jsonPath("$.legs[0].voyageNumber").value("V001"))
            .andExpect(jsonPath("$.legs[0].from").value("SESTO"))
            .andExpect(jsonPath("$.legs[0].to").value("DEHAM"))
            .andExpect(jsonPath("$.legs[1].voyageNumber").value("V002"))
            .andExpect(jsonPath("$.legs[1].from").value("DEHAM"))
            .andExpect(jsonPath("$.legs[1].to").value("FIHEL"));
    }

    @Test
    public void getCargo() throws Exception {
        CargoRoutingDTO cargo = new CargoRoutingDTO("ABC123", "SESTO", "FIHEL", DateTestUtil.toInstant(2023, 1, 1), false);
        cargo.addLeg("V001", "SESTO", "DEHAM", DateTestUtil.toInstant(2023, 1, 1), DateTestUtil.toInstant(2023, 1, 2));
        cargo.addLeg("V002", "DEHAM", "FIHEL", DateTestUtil.toInstant(2023, 1, 3), DateTestUtil.toInstant(2023, 1, 4));

        when(bookingServiceFacade.loadCargoForRouting("ABC123")).thenReturn(cargo);

        mockMvc.perform(get("/api/cargos/ABC123"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.trackingId").value("ABC123"))
            .andExpect(jsonPath("$.origin").value("SESTO"))
            .andExpect(jsonPath("$.finalDestination").value("FIHEL"))
            .andExpect(jsonPath("$.legs[0].voyageNumber").value("V001"))
            .andExpect(jsonPath("$.legs[0].from").value("SESTO"))
            .andExpect(jsonPath("$.legs[0].to").value("DEHAM"))
            .andExpect(jsonPath("$.legs[1].voyageNumber").value("V002"))
            .andExpect(jsonPath("$.legs[1].from").value("DEHAM"))
            .andExpect(jsonPath("$.legs[1].to").value("FIHEL"));
    }

    @Test
    public void getCargoNotFound() throws Exception {
        String trackingId = "NONEXISTENT";
        when(bookingServiceFacade.loadCargoForRouting(trackingId)).thenReturn(null);

        mockMvc.perform(get("/api/cargos/{trackingId}", trackingId))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getRouteCandidates() throws Exception {
        String trackingId = "ABC123";
        LegDTO leg = new LegDTO(
            "V001", 
            "USNYC", 
            "NLRTM", 
            DateTestUtil.toInstant(2024, 1, 1), 
            DateTestUtil.toInstant(2024, 1, 15)
        );
        RouteCandidateDTO route = new RouteCandidateDTO(List.of(leg));
        when(bookingServiceFacade.requestPossibleRoutesForCargo(trackingId)).thenReturn(List.of(route));

        mockMvc.perform(get("/api/cargos/{trackingId}/routes", trackingId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].legs[0].voyageNumber").value("V001"));
    }

    @Test
    public void assignRoute() throws Exception {
        CargoRoutingDTO cargo = new CargoRoutingDTO("ABC123", "SESTO", "FIHEL", DateTestUtil.toInstant(2023, 1, 1), false);
        cargo.addLeg("V001", "SESTO", "DEHAM", DateTestUtil.toInstant(2023, 1, 1), DateTestUtil.toInstant(2023, 1, 2));
        cargo.addLeg("V002", "DEHAM", "FIHEL", DateTestUtil.toInstant(2023, 1, 3), DateTestUtil.toInstant(2023, 1, 4));

        List<LegDTO> legs = Arrays.asList(
            new LegDTO("V001", "SESTO", "DEHAM", DateTestUtil.toInstant(2023, 1, 1), DateTestUtil.toInstant(2023, 1, 2)),
            new LegDTO("V002", "DEHAM", "FIHEL", DateTestUtil.toInstant(2023, 1, 3), DateTestUtil.toInstant(2023, 1, 4))
        );

        when(bookingServiceFacade.loadCargoForRouting("ABC123")).thenReturn(cargo);

        mockMvc.perform(post("/api/cargos/ABC123/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(legs)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.trackingId").value("ABC123"))
            .andExpect(jsonPath("$.origin").value("SESTO"))
            .andExpect(jsonPath("$.finalDestination").value("FIHEL"))
            .andExpect(jsonPath("$.legs[0].voyageNumber").value("V001"))
            .andExpect(jsonPath("$.legs[0].from").value("SESTO"))
            .andExpect(jsonPath("$.legs[0].to").value("DEHAM"))
            .andExpect(jsonPath("$.legs[1].voyageNumber").value("V002"))
            .andExpect(jsonPath("$.legs[1].from").value("DEHAM"))
            .andExpect(jsonPath("$.legs[1].to").value("FIHEL"));
    }

    @Test
    public void changeDestination() throws Exception {
        CargoRoutingDTO cargo = new CargoRoutingDTO("ABC123", "SESTO", "FIHEL", DateTestUtil.toInstant(2023, 1, 1), false);
        cargo.addLeg("V001", "SESTO", "DEHAM", DateTestUtil.toInstant(2023, 1, 1), DateTestUtil.toInstant(2023, 1, 2));
        cargo.addLeg("V002", "DEHAM", "FIHEL", DateTestUtil.toInstant(2023, 1, 3), DateTestUtil.toInstant(2023, 1, 4));

        when(bookingServiceFacade.loadCargoForRouting("ABC123")).thenReturn(cargo);

        mockMvc.perform(put("/api/cargos/ABC123/destination")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("unLocode", "FIHEL"))))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.trackingId").value("ABC123"))
            .andExpect(jsonPath("$.origin").value("SESTO"))
            .andExpect(jsonPath("$.finalDestination").value("FIHEL"))
            .andExpect(jsonPath("$.legs[0].voyageNumber").value("V001"))
            .andExpect(jsonPath("$.legs[0].from").value("SESTO"))
            .andExpect(jsonPath("$.legs[0].to").value("DEHAM"))
            .andExpect(jsonPath("$.legs[1].voyageNumber").value("V002"))
            .andExpect(jsonPath("$.legs[1].from").value("DEHAM"))
            .andExpect(jsonPath("$.legs[1].to").value("FIHEL"));
    }

    @Test
    public void changeDestinationBadRequest() throws Exception {
        mockMvc.perform(put("/api/cargos/ABC123/destination")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("wrongField", "SESTO"))))
            .andExpect(status().isBadRequest());
    }
} 