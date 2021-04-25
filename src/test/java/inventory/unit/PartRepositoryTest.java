package inventory.unit;

import inventory.model.Inventory;
import inventory.model.Part;
import inventory.repository.PartRepository;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static inventory.unit.MockUtils.getDefaultInHousePart;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class PartRepositoryTest {

    @Mock
    private Inventory inventory;

    @InjectMocks
    private PartRepository partRepository;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addPart() {
        Part defaultPart = getDefaultInHousePart();
        when(inventory.getAllParts()).thenReturn(FXCollections.observableArrayList());
        when(inventory.getProducts()).thenReturn(FXCollections.observableArrayList());
        partRepository.addPart(defaultPart);
        Mockito.verify(inventory, times(1)).getAllParts();
        Mockito.verify(inventory, times(1)).getProducts();
        Mockito.verify(inventory, times(1)).addPart(defaultPart);
    }

    @Test
    void getAllParts() {
        partRepository.getAllParts();
        Mockito.verify(inventory, times(1)).getAllParts();
    }
}
