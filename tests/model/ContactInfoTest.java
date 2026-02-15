package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContactInfoTest {

    @Test
    void creation() {
        ContactInfo ci = new ContactInfo("Test", "Test", "123 Main St");
        assertAll(() -> assertEquals("Test", ci.getName()), () -> assertEquals("Test", ci.getLastName()), () -> assertEquals("123 Main St", ci.getAddress()));
    }

    @Test void setters() {
        ContactInfo ci = new ContactInfo("Test", "Test", "123 St");
        ci.setName("Test1"); ci.setLastName("Test1"); ci.setAddress("456 Ave");
        assertAll(() -> assertEquals("Test1", ci.getName()), () -> assertEquals("Test1", ci.getLastName()), () -> assertEquals("456 Ave", ci.getAddress()));
    }

    @Test void toStringContainsAll() {
        String s = new ContactInfo("Test", "Test", "123 St").toString();
        assertAll(() -> assertTrue(s.contains("Test")), () -> assertTrue(s.contains("Test")), () -> assertTrue(s.contains("123 St")));
    }
}
