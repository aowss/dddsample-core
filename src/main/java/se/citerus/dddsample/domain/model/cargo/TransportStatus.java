package se.citerus.dddsample.domain.model.cargo;

import se.citerus.dddsample.domain.shared.ValueObject;

/// Represents the different transport statuses for a cargo.
///
/// ## Status Values
///
/// - **NOT_RECEIVED** - Cargo has not yet been received at the origin
/// - **IN_PORT** - Cargo is currently in a port location
/// - **ONBOARD_CARRIER** - Cargo is on board a carrier (voyage)
/// - **CLAIMED** - Cargo has been claimed by the customer
/// - **UNKNOWN** - Transport status cannot be determined
public enum TransportStatus implements ValueObject<TransportStatus> {
  NOT_RECEIVED, IN_PORT, ONBOARD_CARRIER, CLAIMED, UNKNOWN;

  @Override
  public boolean sameValueAs(final TransportStatus other) {
    return this.equals(other);
  }
}
