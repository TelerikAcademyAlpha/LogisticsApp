package enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {
    //Using @DisplayName we can name our methods
    @Test
    @DisplayName("Parse from abbreviation")
    void fromAbbreviation() {
        assertEquals(Location.SYD, Location.fromString("SYD"));
        assertEquals(Location.MEL, Location.fromString("MEL"));
        assertEquals(Location.ADL, Location.fromString("ADL"));
        assertEquals(Location.ASP, Location.fromString("ASP"));
        assertEquals(Location.BRI, Location.fromString("BRI"));
        assertEquals(Location.DAR, Location.fromString("DAR"));
        assertEquals(Location.PER, Location.fromString("PER"));
    }

    @Test
    @DisplayName("Parse from full name")
    void fromFullName() {
        assertEquals(Location.SYD, Location.fromString("Sydney"));
        assertEquals(Location.MEL, Location.fromString("Melbourne"));
        assertEquals(Location.ASP, Location.fromString("Alice Springs"));
    }

    @Test
    @DisplayName("Parse is case-insensitive")
    void caseInsensitive() {
        assertEquals(Location.SYD, Location.fromString("syd"));
        assertEquals(Location.MEL, Location.fromString("Mel"));
        assertEquals(Location.ASP, Location.fromString("alice springs"));
    }

    @Test
    @DisplayName("Throws on invalid city name")
    void invalidCity() { assertThrows(IllegalArgumentException.class, () -> Location.fromString("London")); }

    @Test
    @DisplayName("Throws on null")
    void nullInput() { assertThrows(IllegalArgumentException.class, () -> Location.fromString(null)); }

    @Test
    @DisplayName("Throws on empty string")
    void emptyInput() { assertThrows(IllegalArgumentException.class, () -> Location.fromString("")); }

    @Test
    @DisplayName("Throws on whitespace only")
    void whitespaceInput() { assertThrows(IllegalArgumentException.class, () -> Location.fromString("   ")); }

    @Test
    @DisplayName("getFullName returns correct city")
    void fullName() {
        assertEquals("Sydney", Location.SYD.getFullName());
        assertEquals("Alice Springs", Location.ASP.getFullName());
    }

    @Test
    @DisplayName("toString contains name and abbreviation")
    void toStringFormat() {
        assertTrue(Location.SYD.toString().contains("Sydney"));
        assertTrue(Location.SYD.toString().contains("SYD"));
    }

    @Test
    @DisplayName("All 7 Australian cities exist")
    void allCities() { assertEquals(7, Location.values().length); }
}
