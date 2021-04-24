package mrir2562;

import inventory.model.Inventory;
import inventory.model.Part;
import inventory.repository.PartRepository;
import inventory.service.PartService;
import inventory.validator.ValidationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PartServiceTest {

    private static final String partDynamicValue = "Google";
    
    static PartService partService;
    
    private String name;

    @BeforeAll
    static void setUp() {
        PartRepository partRepository = new PartRepository(new Inventory());
        partService = new PartService(partRepository);
        while (!partService.getAllParts().isEmpty()) {
            Part p = partService.getAllParts().get(0);
            partService.deletePart(p);
        }
    }
    
    @BeforeEach
    void setUpForEach() {
        name = "Surub";
    }

    @AfterEach
    void tearDown() {
        while (!partService.getAllParts().isEmpty()) {
            Part p = partService.getAllParts().get(0);
            partService.deletePart(p);
        }
    }

    @AfterAll
    static void overAllTearDown() {
        partService = null;
    }

    ///ECP tests:
    @Test
    @Order(1)
    //ecp valid
    void test_addOutsourcePart_valid_ecp() {

        double price = 15.0;
        int inStock = 2000;
        int min = 1000;
        int max = 20000;

        //act
        partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue);

        //assert
        assert (partService.getAllParts().size() == 1);
    }

    @Test
    @Order(2)
        //ecp invalid
    void test_addOutsourcePart_invalidMinStock_ECP() {
        
        double price = 15.0;
        int inStock = 1000;
        int min = 2000;
        int max = 20000;

        //act
        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));

        assert (ex.getMessage().equals("Inventory level is lower than minimum value. "));
    }

    @Test
        //ecp invalid
    void test_addOutsourcePart_invalidStock_min_ECP() {
        
        double price = 15.0;
        int inStock = 30000;
        int min = 2000;
        int max = 20000;

        //act
        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));

        assert (ex.getMessage().equals("Inventory level is higher than the maximum value. "));
    }

    @Test
        //ecp invalid
    void test_addOutsourcePart_invalidStock_max_ECP() {
        
        double price = 15.0;
        int inStock = 30000;
        int min = 2000;
        int max = 20000;

        //act 
        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));

        assert (ex.getMessage().equals("Inventory level is higher than the maximum value. "));
    }

    @Test
        // ecp invalid
    void test_addOutsourcePart_invalid_min_max_ECP() {

        double price = 15.0;
        int inStock = 1500;
        int min = 2000;
        int max = 1000;

        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));

        assert (ex.getMessage().equals("The Min value must be less than the Max value. Inventory level is lower than minimum value. Inventory level is higher than the maximum value. "));
    }
    
    ///BVA tests:
    @Test
    @Order(3)
    //bva extrema inferioara +1
    void test_addOutsourcePart_valid_bva() {

        name = "S";
        double price = 0.1;
        int inStock = 1;
        int min = 0;
        int max = 2;

        //act
        partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue);

        //assert
        assert (partService.getAllParts().size() == 1);
    }

    @Test
        //bva extrema inferioara
    void addOutsourcePart_valid_bva() {
        
        name = "S";
        double price = 1.0;
        int inStock = 1;
        int min = 0;
        int max = 2147483647;

        partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue);

        assert (partService.getAllParts().size() == 1);
    }
    
    @Test
    void addOutsourcePart_negativeMin_bva() {

        double price = 0.1;
        int inStock = 1;
        int min = -1;
        int max = 2;

        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));

        assert (ex.getMessage().equals("The min value must be positive. "));
    }
    
    @Test
    @Order(4)
    void addOutsourcePart_invalidInStock_bva() {

        double price = 1.0;
        int inStock = 0;
        int min = 0;
        int max = 2;

        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));

        assert (ex.getMessage().equals("Inventory level must be greater than 0. "));
    }

    @Disabled
    @Test
    void addOutsourcePart_valid_disabled() {

        double price = 1.0;
        int inStock = 1;
        int min = 0;
        int max = 1000;

        partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue);

        assert (partService.getAllParts().size() == 1);
    }
}
