package se.citerus.dddsample.domain.model.handling;

import se.citerus.dddsample.domain.model.location.UnLocode;

public class UnknownLocationException extends CannotCreateHandlingEventException {

  private final UnLocode unLocode;

  public UnknownLocationException(final UnLocode unLocode) {
    this.unLocode = unLocode;
  }

  @Override
  public String getMessage() {
    return "No location with UN locode " + unLocode.idString() + " exists in the system";
  }
}
