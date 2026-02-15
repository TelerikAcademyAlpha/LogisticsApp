package enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TruckModelTest {
    @Test
    void allModels() { assertEquals(3, TruckModel.values().length); }

    @Test void valueOf() {
        assertAll(
                () -> assertEquals(TruckModel.SCANIA, TruckModel.valueOf("SCANIA")),
                () -> assertEquals(TruckModel.MAN, TruckModel.valueOf("MAN")),
                () -> assertEquals(TruckModel.ACTROS, TruckModel.valueOf("ACTROS"))
        );
    }
}
