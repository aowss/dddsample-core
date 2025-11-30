package se.citerus.dddsample.domain.model.location;

import org.apache.commons.lang3.Validate;
import se.citerus.dddsample.domain.shared.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

/// United Nations location code (UN/LOCODE).
///
/// ## Format
///
/// A UN/LOCODE consists of:
///
/// - **Country code**: exactly two letters (e.g., "US", "NL")
/// - **Location code**: usually three letters, but may contain numbers 2-9 (e.g., "NYC", "RTM")
///
/// Examples: "USNYC" (New York), "NLRTM" (Rotterdam)
///
/// ## Standards
///
/// - [UN/LOCODE Standard](http://www.unece.org/cefact/locode/)
/// - [Column Description](http://www.unece.org/cefact/locode/DocColumnDescription.htm#LOCODE)
public final class UnLocode implements ValueObject<UnLocode> {

  private String unLocode;

  // Country code is exactly two letters.
  // Location code is usually three letters, but may contain the numbers 2-9 as well
  private static final Pattern VALID_PATTERN = Pattern.compile("[a-zA-Z]{2}[a-zA-Z2-9]{3}");

  /**
   * Constructor.
   *
   * @param countryAndLocation Location string.
   */
  public UnLocode(final String countryAndLocation) {
    Objects.requireNonNull(countryAndLocation, "Country and location may not be null");
    Validate.isTrue(VALID_PATTERN.matcher(countryAndLocation).matches(),
      countryAndLocation + " is not a valid UN/LOCODE (does not match pattern)");

    this.unLocode = countryAndLocation.toUpperCase();
  }

  /**
   * @return country code and location code concatenated, always upper case.
   */
  public String idString() {
    return unLocode;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UnLocode other = (UnLocode) o;

    return sameValueAs(other);
  }

  @Override
  public int hashCode() {
    return unLocode.hashCode();
  }

  @Override
  public boolean sameValueAs(UnLocode other) {
    return other != null && this.unLocode.equals(other.unLocode);
  }

  @Override
  public String toString() {
    return idString();
  }

  UnLocode() {
    // Needed by Hibernate
  }

}
