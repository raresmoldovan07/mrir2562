package inventory.controller;

import inventory.model.Part;
import inventory.model.Product;
import inventory.service.PartService;
import inventory.service.ProductService;
import inventory.validator.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class AddProductController extends BaseController implements Initializable {
    
    // Declare fields
    private ObservableList<Part> addParts = FXCollections.observableArrayList();
    
    private PartService partService;
    private ProductService productService;
    
    @FXML
    private TextField minTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField productIdTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField inventoryTxt;

    @FXML
    private TextField priceTxt;

    @FXML
    private TextField productSearchTxt;

    @FXML
    private TableView<Part> addProductTableView;

    @FXML
    private TableColumn<Part, Integer> addProductIdCol;

    @FXML
    private TableColumn<Part, String> addProductNameCol;

    @FXML
    private TableColumn<Part, Double> addProductInventoryCol;

    @FXML
    private TableColumn<Part, Integer> addProductPriceCol;

    @FXML
    private TableView<Part> deleteProductTableView;

    @FXML
    private TableColumn<Part, Integer> deleteProductIdCol;

    @FXML
    private TableColumn<Part, String> deleteProductNameCol;

    @FXML
    private TableColumn<Part, Double> deleteProductInventoryCol;

    @FXML
    private TableColumn<Part, Integer> deleteProductPriceCol;

    public void setService(PartService service, ProductService productService){
        this.partService =service;
        this.productService = productService;
        addProductTableView.setItems(service.getAllParts());
    }

    /**
     * Initializes the controller class and populates table view.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate add product table view
        addProductIdCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        addProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        addProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    
    /**
     * Method to add values of addParts to the bottom table view of the scene.
     */
    public void updateDeleteProductTableView() {
        deleteProductTableView.setItems(addParts);
        
        deleteProductIdCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        deleteProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        deleteProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        deleteProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Ask user for confirmation before deleting selected part from current product.
     * @param event 
     */
    @FXML
    void handleDeleteProduct(ActionEvent event) {
        Part part = deleteProductTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Part Deletion!");
        alert.setContentText("Are you sure you want to delete part " + part.getName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Part deleted.");
            addParts.remove(part);
        } else {
            System.out.println("Canceled part deletion.");
        }
    }

    /**
     * Ask user for confirmation before canceling product addition
     * and switching scene to Main Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void handleCancelProduct(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Cancelation");
        alert.setContentText("Are you sure you want to cancel adding product?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Ok selected. Product addition canceled.");
            displayScene(event, "/fxml/MainScreen.fxml");
        } else {
            System.out.println("Cancel clicked.");
        }
    }
    
    /**
     * Add selected part from top table view to bottom table view in order to create
     * new product
     * @param event 
     */
    @FXML
    void handleAddProduct(ActionEvent event) {
        Part part = addProductTableView.getSelectionModel().getSelectedItem();
        addParts.add(part);
        updateDeleteProductTableView();
    }

    /**
     * Validate given product parameters.  If valid, add product to inventory,
     * else give user an error message explaining why the product is invalid.
     * @param event
     * @throws IOException
     */
    @FXML
    void handleSaveProduct(ActionEvent event) throws IOException {
        String name = nameTxt.getText();
        String price = priceTxt.getText();
        String inStock = inventoryTxt.getText();
        String min = minTxt.getText();
        String max = maxTxt.getText();
        String errorMessage = "";
        
        try {
            errorMessage = Validator.isValidProduct(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), addParts, errorMessage);
            if(errorMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Part!");
                alert.setHeaderText("Error!");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            } else {
                productService.addProduct(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), addParts);
                displayScene(event, "/fxml/MainScreen.fxml");
            }
        } catch (NumberFormatException e) {
            System.out.println("Form contains blank field.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Product!");
            alert.setHeaderText("Error!");
            alert.setContentText("Form contains blank field.");
            alert.showAndWait();
        }

    }

    /**
     * Gets search text and inputs into lookupAssociatedPart method to highlight desired part
     * @param event 
     */
    @FXML
    void handleSearchProduct(ActionEvent event) {
        String x = productSearchTxt.getText();
        addProductTableView.getSelectionModel().select(partService.lookupPart(x));
    }


    
}
