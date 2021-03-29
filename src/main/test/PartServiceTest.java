import inventory.model.Inventory;
import inventory.model.Part;
import inventory.repository.PartRepository;
import inventory.service.PartService;
import inventory.validator.ValidationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class PartServiceTest {

    static PartService partService;

    @BeforeAll
    static void setUp() {
        PartRepository partRepository = new PartRepository(new Inventory());
        partService = new PartService(partRepository);
        while (!partService.getAllParts().isEmpty()) {
            Part p = partService.getAllParts().get(0);
            partService.deletePart(p);
        }
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
    //ecp valid
    void test_addOutsourcePart_valid_ecp() {

        String name = "Surub";
        double price = 15.0;
        int inStock = 2000;
        int min = 1000;
        int max = 20000;
        String partDynamicValue = "Google";

        //act
        partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue);

        //assert
        assert (partService.getAllParts().size() == 1);
    }

    @Test
        //ecp invalid
    void test_addOutsourcePart_invalidMinStock_ECP() {
        String name = "Surub";
        double price = 15.0;
        int inStock = 1000;
        int min = 2000;
        int max = 20000;
        String partDynamicValue = "Google";


        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));

        assert (ex.getMessage().equals("Inventory level is lower than minimum value. "));
    }

    @Test
        //ecp invalid
    void test_addOutsourcePart_invalidStock_min_ECP() {
        String name = "Surub";
        double price = 15.0;
        int inStock = 30000;
        int min = 2000;
        int max = 20000;
        String partDynamicValue = "Google";


        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));
        assert (ex.getMessage().equals("Inventory level is higher than the maximum value. "));
    }

    @Test
        //ecp invalid
    void test_addOutsourcePart_invalidStock_max_ECP() {
        String name = "Surub";
        double price = 15.0;
        int inStock = 30000;
        int min = 2000;
        int max = 20000;
        String partDynamicValue = "Google";


        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));
        assert (ex.getMessage().equals("Inventory level is higher than the maximum value. "));
    }

    @Test
        //ecp invalid
    void test_addOutsourcePart_invalid_min_max_ECP() {
        String name = "Surub";
        double price = 15.0;
        int inStock = 1500;
        int min = 2000;
        int max = 1000;
        String partDynamicValue = "Google";


        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));
        assert (ex.getMessage().equals("The Min value must be less than the Max value. Inventory level is lower than minimum value. Inventory level is higher than the maximum value. "));
    }


    ///BVA tests:
    @Test
    //bva extrema inferioara +1
    void test_addOutsourcePart_valid_bva() {

        String name = "S";
        double price = 0.1;
        int inStock = 1;
        int min = 0;
        int max = 2;
        String partDynamicValue = "S";

        //act
        partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue);

        //assert
        assert (partService.getAllParts().size() == 1);
    }


    @Test
//bva extrema inferioara
    void test_addOutsourcePart_invalidName() {
        String name = "";
        double price = 1.0;
        int inStock = 1;
        int min = 0;
        int max = 2;
        String partDynamicValue = "";

        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));
        System.out.println(ex.getMessage());
        assert (ex.getMessage().equals("A name has not been entered. "));
    }


    @Test
//bva extrema inferioara
    void test_addOutsourcePart_invalidPrice() {
        String name = "S";
        double price = 0.0;
        int inStock = 1;
        int min = 0;
        int max = 2;
        String partDynamicValue = "";

        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));
        assert (ex.getMessage().equals("The price must be greater than 0. "));
    }


    @Test
//bva extrema inferioara
    void test_addOutsourcePart_invalidInStock() {
        String name = "S";
        double price = 1.0;
        int inStock = 0;
        int min = 0;
        int max = 2;
        String partDynamicValue = "";

        ValidationException ex = assertThrows(ValidationException.class, () ->
                partService.addOutsourcePart(name, price, inStock, min, max, partDynamicValue));
        assert (ex.getMessage().equals("Inventory level must be greater than 0. "));
    }


}
