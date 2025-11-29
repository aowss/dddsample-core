package se.citerus.dddsample.interfaces.booking.rest;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record for cargo registration request.
 * Using a record for immutable DTOs is a good practice in modern Java.
 */
public record CargoRegistrationRequest(
    @JsonProperty String originUnlocode,
    @JsonProperty String destinationUnlocode,
    @JsonProperty LocalDate arrivalDeadline
) {} 