package se.citerus.dddsample.domain.model.location;

import org.apache.commons.lang3.Validate;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * United nations location code.
 * 
 * http://www.unece.org/cefact/locode/
 * http://www.unece.org/cefact/locode/DocColumnDescription.htm#LOCODE
 */
public record UnLocode(String unlocode) {

  private static final Pattern VALID_PATTERN = Pattern.compile("[a-zA-Z]{2}[a-zA-Z2-9]{3}");

  public UnLocode(String unlocode) {
    Objects.requireNonNull(unlocode, "Country and location may not be null");
    Validate.isTrue(VALID_PATTERN.matcher(unlocode).matches(),
            unlocode + " is not a valid UN/LOCODE (does not match pattern)");

    this.unlocode = unlocode.toUpperCase();
  }

}
