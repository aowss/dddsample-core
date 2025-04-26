package se.citerus.dddsample.interfaces.booking.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import se.citerus.dddsample.interfaces.booking.facade.BookingServiceFacade;
import se.citerus.dddsample.interfaces.booking.facade.dto.CargoRoutingDTO;
import se.citerus.dddsample.interfaces.booking.facade.dto.LegDTO;
import se.citerus.dddsample.interfaces.booking.facade.dto.LocationDTO;
import se.citerus.dddsample.interfaces.booking.facade.dto.RouteCandidateDTO;

import java.net.URI;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

/**
 * REST API for cargo administration.
 * This is a REST facade for the booking service, providing a RESTful interface
 * for cargo administration operations.
 */
@RestController
@RequestMapping("/api")
public class CargoAdministrationRestController {

    private final BookingServiceFacade bookingServiceFacade;

    public CargoAdministrationRestController(BookingServiceFacade bookingServiceFacade) {
        this.bookingServiceFacade = bookingServiceFacade;
    }

    @GetMapping("/locations")
    public ResponseEntity<List<LocationDTO>> listLocations() {
        try {
            return ResponseEntity.ok(bookingServiceFacade.listShippingLocations());
        } catch (RemoteException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/cargos")
    public ResponseEntity<List<CargoRoutingDTO>> listCargos() {
        try {
            return ResponseEntity.ok(bookingServiceFacade.listAllCargos());
        } catch (RemoteException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/cargos")
    public ResponseEntity<CargoRoutingDTO> registerCargo(@RequestBody CargoRegistrationRequest request) {
        try {
            String trackingId = bookingServiceFacade.bookNewCargo(
                request.originUnlocode(),
                request.destinationUnlocode(),
                request.arrivalDeadline().atStartOfDay().toInstant(ZoneOffset.UTC)
            );
            
            CargoRoutingDTO cargo = bookingServiceFacade.loadCargoForRouting(trackingId);
            URI location = UriComponentsBuilder.fromPath("/api/cargos/{trackingId}")
                .buildAndExpand(trackingId)
                .toUri();
            
            return ResponseEntity.created(location).body(cargo);
        } catch (RemoteException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/cargos/{trackingId}")
    public ResponseEntity<CargoRoutingDTO> getCargo(@PathVariable String trackingId) {
        try {
            CargoRoutingDTO cargo = bookingServiceFacade.loadCargoForRouting(trackingId);
            if (cargo == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cargo);
        } catch (RemoteException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/cargos/{trackingId}/routes")
    public ResponseEntity<List<RouteCandidateDTO>> getRouteCandidates(@PathVariable String trackingId) {
        try {
            List<RouteCandidateDTO> routeCandidates = bookingServiceFacade.requestPossibleRoutesForCargo(trackingId);
            if (routeCandidates == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(routeCandidates);
        } catch (RemoteException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/cargos/{trackingId}/routes")
    public ResponseEntity<CargoRoutingDTO> assignRoute(
        @PathVariable String trackingId,
        @RequestBody List<LegDTO> legs
    ) {
        try {
            RouteCandidateDTO route = new RouteCandidateDTO(legs);
            bookingServiceFacade.assignCargoToRoute(trackingId, route);
            CargoRoutingDTO cargo = bookingServiceFacade.loadCargoForRouting(trackingId);
            return ResponseEntity.ok(cargo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RemoteException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/cargos/{trackingId}/destination")
    public ResponseEntity<CargoRoutingDTO> changeDestination(
        @PathVariable String trackingId,
        @RequestBody Map<String, String> request
    ) {
        String unlocode = request.get("unlocode");
        if (unlocode == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            bookingServiceFacade.changeDestination(trackingId, unlocode);
            CargoRoutingDTO cargo = bookingServiceFacade.loadCargoForRouting(trackingId);
            return ResponseEntity.ok(cargo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RemoteException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 