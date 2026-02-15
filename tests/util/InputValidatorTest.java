package util;

import enums.Location;
import exception.InvalidInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InputValidatorTest {
    @Test
    void requireNonEmptyTrims() { assertEquals("hello", InputValidator.requireNonEmpty("  hello  ", "f")); }

    @Test
    void requireNonEmptyBlank() { assertThrows(InvalidInputException.class, () -> InputValidator.requireNonEmpty("   ", "f")); }

    @Test
    void requireNonEmptyNull() { assertThrows(InvalidInputException.class, () -> InputValidator.requireNonEmpty(null, "f")); }

    @Test
    void positiveDouble() { assertEquals(45.5, InputValidator.requirePositiveDouble("45.5", "w"), 0.01); }

    @Test
    void positiveDoubleZero() { assertThrows(InvalidInputException.class, () -> InputValidator.requirePositiveDouble("0", "w")); }

    @Test
    void positiveDoubleNeg() { assertThrows(InvalidInputException.class, () -> InputValidator.requirePositiveDouble("-5", "w")); }

    @Test
    void positiveDoubleNaN() { assertThrows(InvalidInputException.class, () -> InputValidator.requirePositiveDouble("abc", "w")); }

    @Test
    void positiveInt() { assertEquals(10, InputValidator.requirePositiveInt("10", "id")); }

    @Test
    void positiveIntZero() { assertThrows(InvalidInputException.class, () -> InputValidator.requirePositiveInt("0", "id")); }

    @Test
    void parseInt() { assertEquals(42, InputValidator.parseInt("42", "v")); }

    @Test
    void parseIntNaN() { assertThrows(InvalidInputException.class, () -> InputValidator.parseInt("abc", "v")); }

    @Test
    void validLocation() { assertEquals(Location.SYD, InputValidator.requireValidLocation("SYD")); }

    @Test
    void invalidLocation() { assertThrows(IllegalArgumentException.class, () -> InputValidator.requireValidLocation("XYZ")); }

}
