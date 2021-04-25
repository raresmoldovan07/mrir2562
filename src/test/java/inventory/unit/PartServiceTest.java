package inventory.unit;

import inventory.model.Part;
import inventory.repository.PartRepository;
import inventory.service.PartService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import static inventory.unit.MockUtils.getDefaultInHousePart;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class PartServiceTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private PartService partService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addInhousePart() {
        when(partRepository.getAutoPartId()).thenReturn(1);
        partService.addInhousePart("Part", 10.0, 5, 0, 10, 13);
        Mockito.verify(partRepository, times(1)).getAutoPartId();
        Mockito.verify(partRepository, times(1)).addPart(getDefaultInHousePart());
    }

    @Test
    void getAllParts() {
        ObservableList<Part> partObservableList = FXCollections.observableArrayList();
        partObservableList.add(getDefaultInHousePart());
        when(partRepository.getAllParts()).thenReturn(partObservableList);
        ObservableList<Part> resultList = partService.getAllParts();
        Mockito.verify(partRepository, times(1)).getAllParts();
        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertEquals(partObservableList, resultList);
    }
}
