package se.citerus.dddsample.domain.model.voyage;

import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A voyage schedule.
 * 
 */
public record Schedule(List<CarrierMovement> carrierMovements) {

  public static final Schedule EMPTY = new Schedule(Collections.emptyList());

  public Schedule {
    Objects.requireNonNull(carrierMovements);
    Validate.noNullElements(carrierMovements);
//    Validate.notEmpty(carrierMovements);
  }

  /**
   * @return Carrier movements.
   */
  public List<CarrierMovement> carrierMovements() {
    return Collections.unmodifiableList(carrierMovements);
  }

}
