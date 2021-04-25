package inventory.unit;

import inventory.model.InhousePart;

public class MockUtils {
    public static InhousePart getDefaultInHousePart() {
        return new InhousePart(1, "Part", 10.0, 5, 0, 10, 13);
    }
}
