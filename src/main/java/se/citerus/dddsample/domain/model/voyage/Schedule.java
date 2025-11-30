package se.citerus.dddsample.domain.model.voyage;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import se.citerus.dddsample.domain.shared.ValueObject;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/// A voyage schedule defines the sequence of carrier movements for a voyage.
///
/// ## Structure
///
/// A schedule consists of one or more carrier movements that together
/// form the complete journey from the voyage's starting point to its destination.
///
/// ## Characteristics
///
/// - Movements are ordered sequentially
/// - Each movement's arrival location should match the next movement's departure location
/// - The schedule is immutable once created
public class Schedule implements ValueObject<Schedule> {

  private List<CarrierMovement> carrierMovements = Collections.emptyList();

  public static final Schedule EMPTY = new Schedule();

  public Schedule(final List<CarrierMovement> carrierMovements) {
    Objects.requireNonNull(carrierMovements);
    Validate.noNullElements(carrierMovements);
    Validate.notEmpty(carrierMovements);

    this.carrierMovements = carrierMovements;
  }

  /**
   * @return Carrier movements.
   */
  public List<CarrierMovement> carrierMovements() {
    return Collections.unmodifiableList(carrierMovements);
  }

  @Override
  public boolean sameValueAs(final Schedule other) {
    return other != null && this.carrierMovements.equals(other.carrierMovements);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Schedule that = (Schedule) o;

    return sameValueAs(that);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.carrierMovements).toHashCode();
  }

  Schedule() {
    // Needed by Hibernate
  }

}
