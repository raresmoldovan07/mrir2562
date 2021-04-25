package inventory.unit;

import inventory.model.InhousePart;
import org.junit.jupiter.api.Test;

import static inventory.unit.MockUtils.getDefaultInHousePart;
import static org.junit.jupiter.api.Assertions.*;

class InhousePartTest {

    @Test
    void getMachineId() {
        InhousePart inhousePart = getDefaultInHousePart();
        assertEquals(13, inhousePart.getMachineId());
    }

    @Test
    void setMachineId() {
        InhousePart inhousePart = getDefaultInHousePart();
        inhousePart.setMachineId(12345);
        assertEquals(12345, inhousePart.getMachineId());
    }
}
