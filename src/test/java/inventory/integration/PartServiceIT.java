package inventory.integration;

import inventory.model.InhousePart;
import inventory.model.Inventory;
import inventory.model.Part;
import inventory.repository.PartRepository;
import inventory.service.PartService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class PartServiceIT {

    private final PartRepository partRepository = new PartRepository(new Inventory());
    private final PartService partService = new PartService(partRepository);

    private final String name = "partName";
    private final double price = 10.0;
    private final int partId = 1;
    private final int inStock = 10;
    private final int min = 5;
    private final int max = 15;
    private final int machineId = 1;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    public void tearDown() {
        while (!partService.getAllParts().isEmpty()) {
            Part p = partService.getAllParts().get(0);
            partService.deletePart(p);
        }
    }

    @Test
    public void lookupPart_success() {
        partService.addInhousePart(name, price, inStock, min, max, machineId);
        InhousePart searchedPart = (InhousePart) partService.lookupPart(name);
        assertAll(searchedPart);
    }

    @Test
    public void lookupPart_fail() {
        InhousePart inhousePart = (InhousePart) partService.lookupPart("InvalidName");
        assertNull(inhousePart);
    }

    private void assertAll(InhousePart inhousePart) {
        assertNotNull(inhousePart);
        assertEquals(partId, inhousePart.getPartId());
        assertEquals(name, inhousePart.getName());
        assertEquals(inStock, inhousePart.getInStock());
        assertEquals(min, inhousePart.getMin());
        assertEquals(max, inhousePart.getMax());
        assertEquals(machineId, inhousePart.getMachineId());
    }
}
