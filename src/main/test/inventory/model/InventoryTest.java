package inventory.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private static Inventory inventory;

    @BeforeAll
    static void setup() {
        inventory = new Inventory();
    }

    @AfterAll
    static void tearDown() {
        inventory = null;
    }

    @BeforeEach
    void addParts() {
        inventory.addPart(new InhousePart(1, "Part", 10.0, 5, 0, 10, 0));
    }

    @AfterEach
    void deleteParts() {
        while (!inventory.getAllParts().isEmpty()) {
            Part p = inventory.getAllParts().get(0);
            inventory.deletePart(p);
        }
    }

    @Test
    void lookupPart_searchItemIsNull() {
        Part part = inventory.lookupPart(null);
        assertNull(part);
    }

    @Test
    void lookupPart_searchItemIsEmpty() {
        Part part = inventory.lookupPart("");
        assertNull(part);
    }

    @Test
    void lookupPart_notFound() {
        Part part = inventory.lookupPart("NotFound");
        assertNull(part);
    }

    @Test
    void lookupPart_successLookupByName() {
        Part part = inventory.lookupPart("Part");

        assertNotNull(part);
        assertEquals(1, part.getPartId());
        assertEquals("Part", part.getName());
        assertEquals(10.0, part.getPrice());
        assertEquals(5, part.getInStock());
        assertEquals(0, part.getMin());
        assertEquals(10, part.getMax());
    }

    @Test
    void lookupPart_successLookupByPartId() {
        Part part = inventory.lookupPart("1");

        assertNotNull(part);
        assertEquals(1, part.getPartId());
        assertEquals("Part", part.getName());
        assertEquals(10.0, part.getPrice());
        assertEquals(5, part.getInStock());
        assertEquals(0, part.getMin());
        assertEquals(10, part.getMax());
    }

    @Test
    void lookupPart_successLoopAll() {
        inventory.addPart(new InhousePart(2, "Part2", 10.0, 5, 0, 10, 0));
        inventory.addPart(new InhousePart(3, "Part3", 10.0, 5, 0, 10, 0));
        inventory.addPart(new InhousePart(4, "Part4", 10.0, 5, 0, 10, 0));
        inventory.addPart(new InhousePart(5, "Part5", 10.0, 5, 0, 10, 0));

        Part part = inventory.lookupPart("5");

        assertNotNull(part);
        assertEquals(5, part.getPartId());
        assertEquals("Part5", part.getName());
        assertEquals(10.0, part.getPrice());
        assertEquals(5, part.getInStock());
        assertEquals(0, part.getMin());
        assertEquals(10, part.getMax());
    }

    @Test
    void lookupPart_emptyList() {
        deleteParts();

        Part part = inventory.lookupPart("5");

        assertNull(part);
    }
}