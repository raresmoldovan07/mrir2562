package inventory.integration;

import inventory.model.InhousePart;
import inventory.model.Inventory;
import inventory.model.Part;
import inventory.repository.PartRepository;
import inventory.service.PartService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PartServiceRepoIntegration {

    private PartRepository partRepository;
    private PartService partService;

    private final String name = "partName";
    private final double price = 10.0;
    private final int partId = 1;
    private final int inStock = 10;
    private final int min = 5;
    private final int max = 15;
    private final int machineId = 1;
    
    @Mock
    private InhousePart inhousePart;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        partRepository = new PartRepository(new Inventory());
        partRepository.addPart(inhousePart);
        partService = new PartService(partRepository);
        when(inhousePart.getPartId()).thenReturn(partId);
        when(inhousePart.getName()).thenReturn(name);
        when(inhousePart.getPrice()).thenReturn(price);
        when(inhousePart.getInStock()).thenReturn(inStock);
        when(inhousePart.getMin()).thenReturn(min);
        when(inhousePart.getMax()).thenReturn(max);
        when(inhousePart.getMachineId()).thenReturn(machineId);
    }

    @AfterEach
    public void tearDown() {
        while (!partService.getAllParts().isEmpty()) {
            Part p = partService.getAllParts().get(0);
            partService.deletePart(p);
        }
    }
    
    @Test
    public void lookupPart_repoIntegration_byPartName() {
        InhousePart part = (InhousePart) partService.lookupPart(name);
        verify(inhousePart, times(1)).getName();
        verify(inhousePart, times(0)).getPartId();
        assertAll(part);
    }
    
    @Test
    public void lookupPart_repoIntegration_byPartId() {
        InhousePart part = (InhousePart) partService.lookupPart(String.valueOf(partId));
        verify(inhousePart, times(1)).getName();
        verify(inhousePart, times(1)).getPartId();
        assertAll(part);
    }
    
    
    @Test
    public void lookupPart_repoIntegration_null() {
        InhousePart part = (InhousePart) partService.lookupPart(null);
        verify(inhousePart, times(0)).getName();
        verify(inhousePart, times(0)).getPartId();
        assertNull(part);
    }
    
    
    @Test
    public void lookupPart_repoIntegration_emptyString() {
        InhousePart part = (InhousePart) partService.lookupPart("");
        verify(inhousePart, times(0)).getName();
        verify(inhousePart, times(0)).getPartId();
        assertNull(part);
    }
    
    @Test
    public void lookupPart_repoIntegration_failed() {
        InhousePart part = (InhousePart) partService.lookupPart("InvalidName");
        verify(inhousePart, times(1)).getPartId();
        verify(inhousePart, times(1)).getName();
        assertNull(part);
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
