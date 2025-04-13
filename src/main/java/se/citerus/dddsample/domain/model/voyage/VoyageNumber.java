package se.citerus.dddsample.domain.model.voyage;

import java.util.Objects;

/**
 * Identifies a voyage.
 * 
 */
public record VoyageNumber(String number) {

  public VoyageNumber {
    Objects.requireNonNull(number);
  }

}
