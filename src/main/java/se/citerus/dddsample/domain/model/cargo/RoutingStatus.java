package se.citerus.dddsample.domain.model.cargo;

import se.citerus.dddsample.domain.shared.ValueObject;

/// Routing status indicates whether a cargo has been assigned a route.
///
/// ## Status Values
///
/// - **NOT_ROUTED** - No route has been assigned to the cargo
/// - **ROUTED** - A route has been assigned and satisfies the route specification
/// - **MISROUTED** - A route has been assigned but does not satisfy the route specification
public enum RoutingStatus implements ValueObject<RoutingStatus> {
  NOT_ROUTED, ROUTED, MISROUTED;

  @Override
  public boolean sameValueAs(final RoutingStatus other) {
    return this.equals(other);
  }
  
}
