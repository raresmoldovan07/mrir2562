package inventory;

import inventory.model.Inventory;
import inventory.repository.PartRepository;
import inventory.repository.ProductRepository;
import inventory.service.PartService;
import inventory.controller.MainScreenController;
import inventory.service.ProductService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Inventory inventory = new Inventory();
        
        PartRepository partRepository = new PartRepository(inventory);
        PartService partService = new PartService(partRepository);

        ProductRepository productRepository = new ProductRepository(inventory);
        ProductService productService = new ProductService(productRepository);
        
        System.out.println(productService.getAllProducts());
        System.out.println(partService.getAllParts());
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/fxml/MainScreen.fxml"));

        Parent root=loader.load();
        MainScreenController ctrl=loader.getController();
        ctrl.setService(partService, productService);

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
