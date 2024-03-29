
package inventory.controller;

import inventory.model.Part;
import inventory.model.Product;
import inventory.service.PartService;
import inventory.service.ProductService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainScreenController extends BaseController implements Initializable {
    
     // Declare fields
    private static Part modifyPart;
    private static Product modifyProduct;
    private static int modifyPartIndex;
    private static int modifyProductIndex;
    
    // Declare methods
    public static int getModifyPartIndex() {
        return modifyPartIndex;
    }
    
    public static int getModifyProductIndex() {
        return modifyProductIndex;
    }

    private PartService partService;
    private ProductService productService;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partsIdCol;

    @FXML
    private TableColumn<Part, String> partsNameCol;

    @FXML
    private TableColumn<Part, Integer> partsInventoryCol;

    @FXML
    private TableColumn<Part, Double> partsPriceCol;
    
    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, Integer> productsIdCol;

    @FXML
    private TableColumn<Product, String> productsNameCol;

    @FXML
    private TableColumn<Product, Integer> productsInventoryCol;

    @FXML
    private TableColumn<Product, Double> productsPriceCol;
    
    @FXML
    private TextField partsSearchTxt;
    
    @FXML
    private TextField productsSearchTxt;

    public MainScreenController(){}

    public void setService(PartService service, ProductService productService){
        this.partService =service;
        this.productService = productService;
        partsTableView.setItems(service.getAllParts());
        productsTableView.setItems(productService.getAllProducts());
    }
    
    /**
     * Initializes the controller class and populate table views.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate parts table view
        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        partsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Populate products table view
        productsIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        productsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Ask user for confirmation before deleting selected part from list of parts.
     * @param event 
     */
    @FXML
    void handleDeletePart(ActionEvent event) {
        Part part = partsTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Part Deletion?");
        alert.setContentText("Are you sure you want to delete part " + part.getName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.out.println("Part deleted.");
            partService.deletePart(part);
        } else {
            System.out.println("Canceled part deletion.");
        }
    }

    /**
     * Ask user for confirmation before deleting selected product from list of products.
     * @param event 
     */
    @FXML
    void handleDeleteProduct(ActionEvent event) {
        Product product = productsTableView.getSelectionModel().getSelectedItem();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Product Deletion?");
        alert.setContentText("Are you sure you want to delete product " + product.getName() + " from products?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            productService.deleteProduct(product);
            System.out.println("Product " + product.getName() + " was removed.");
        } else {
            System.out.println("Product " + product.getName() + " was not removed.");
        }
    }

    /**
     * Switch scene to Add Part
     * @param event
     * @throws IOException
     */
    @FXML
    void handleAddPart(ActionEvent event) throws IOException {
        displayScene(event, "/fxml/AddPart.fxml");
    }

    /**
     * Switch scene to Add Product
     * @param event
     * @throws IOException
     */
    @FXML
    void handleAddProduct(ActionEvent event) throws IOException {
        displayScene(event, "/fxml/AddProduct.fxml");
    }

    /**
     * Changes scene to Modify Part screen and passes values of selected part
     * and its index
     * @param event
     * @throws IOException
     */
    @FXML
    void handleModifyPart(ActionEvent event) throws IOException {
        modifyPart = partsTableView.getSelectionModel().getSelectedItem();
        modifyPartIndex = partService.getAllParts().indexOf(modifyPart);
        
        displayScene(event, "/fxml/ModifyPart.fxml");
    }

    /**
     * Switch scene to Modify Product
     * @param event
     * @throws IOException
     */
    @FXML
    void handleModifyProduct(ActionEvent event) throws IOException {
        modifyProduct = productsTableView.getSelectionModel().getSelectedItem();
        modifyProductIndex = productService.getAllProducts().indexOf(modifyProduct);
        
        displayScene(event, "/fxml/ModifyProduct.fxml");
    }

    /**
     * Ask user for confirmation before exiting
     * @param event 
     */
    @FXML
    void handleExit(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            System.out.println("Ok selected. Program exited");
            System.exit(0);
        } else {
            System.out.println("Cancel clicked.");
        }
    }

    /**
     * Gets search text and inputs into lookupPart method to highlight desired part
     * @param event 
     */
    @FXML
    void handlePartsSearchBtn(ActionEvent event) {
        String x = partsSearchTxt.getText();
        partsTableView.getSelectionModel().select(partService.lookupPart(x));
    }

    /**
     * Gets search text and inputs into lookupProduct method to highlight desired product
     * @param event 
     */
    @FXML
    void handleProductsSearchBtn(ActionEvent event) {
        String x = productsSearchTxt.getText();
        productsTableView.getSelectionModel().select(productService.lookupProduct(x));
    }


}
