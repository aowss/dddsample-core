package se.citerus.dddsample.interfaces.booking.rest;

import java.time.LocalDate;

/**
 * Record for cargo registration request.
 * Using a record for immutable DTOs is a good practice in modern Java.
 */
public record CargoRegistrationRequest(
    String originUnlocode,
    String destinationUnlocode,
    LocalDate arrivalDeadline
) {} 