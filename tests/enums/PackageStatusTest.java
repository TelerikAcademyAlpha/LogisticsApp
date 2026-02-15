package enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PackageStatusTest {
    @Test void fourStatuses() { assertEquals(4, PackageStatus.values().length); }

    @Test void valueOf() {
        assertAll(
                () -> assertEquals(PackageStatus.UNASSIGNED, PackageStatus.valueOf("UNASSIGNED")),
                () -> assertEquals(PackageStatus.ASSIGNED, PackageStatus.valueOf("ASSIGNED")),
                () -> assertEquals(PackageStatus.IN_TRANSIT, PackageStatus.valueOf("IN_TRANSIT")),
                () -> assertEquals(PackageStatus.DELIVERED, PackageStatus.valueOf("DELIVERED"))
        );
    }
}
