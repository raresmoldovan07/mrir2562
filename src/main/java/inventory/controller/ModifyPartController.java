
package inventory.controller;

import inventory.model.InhousePart;
import inventory.model.OutsourcedPart;
import inventory.model.Part;
import inventory.service.PartService;
import inventory.service.ProductService;
import inventory.validator.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static inventory.controller.MainScreenController.getModifyPartIndex;


public class ModifyPartController extends BaseController implements Initializable {
    
    // Declare field
    private Stage stage;
    private Parent scene;
    private int partIndex= getModifyPartIndex();
    private String errorMessage = new String();
    private boolean isOutsourced;
    private int partId;

    private PartService partService;
    private ProductService productService;
    
    @FXML
    private RadioButton inhouseRBtn;

    @FXML
    private RadioButton outsourcedRBtn;
    
    @FXML
    private Label modifyPartDynamicLbl;
    
    @FXML
    private TextField modifyPartDynamicTxt;

    @FXML
    private TextField partIdTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField inventoryTxt;

    @FXML
    private TextField priceTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField minTxt;

    public ModifyPartController(){}

    @Override
    public void setService(PartService service, ProductService productService){
        this.partService =service;
        this.productService = productService;
        fillWithData();
    }

    private void fillWithData(){
        if (partIndex < 0) {
            return;
        }
        Part part = partService.getAllParts().get(partIndex);

        partId = partService.getAllParts().get(partIndex).getPartId();
        partIdTxt.setText(Integer.toString(part.getPartId()));
        nameTxt.setText(part.getName());
        inventoryTxt.setText(Integer.toString(part.getInStock()));
        priceTxt.setText(Double.toString(part.getPrice()));
        maxTxt.setText(Integer.toString(part.getMax()));
        minTxt.setText(Integer.toString(part.getMin()));

        if(part instanceof InhousePart) {
            modifyPartDynamicTxt.setText(Integer.toString(((InhousePart) partService.getAllParts().get(partIndex)).getMachineId()));
            modifyPartDynamicLbl.setText("Machine ID");
            inhouseRBtn.setSelected(true);
            isOutsourced = false;
        } else {
            modifyPartDynamicTxt.setText(((OutsourcedPart) partService.getAllParts().get(partIndex)).getCompanyName());
            modifyPartDynamicLbl.setText("Company Name");
            outsourcedRBtn.setSelected(true);
            isOutsourced = true;
        }
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    /**
     * If in-house radio button is selected set isOutsourced boolean
     * to false and modify dynamic label to Machine ID
     * @param event 
     */
    @FXML
    void handleInhouseRBtn(ActionEvent event) {
        isOutsourced = false;
        modifyPartDynamicLbl.setText("Machine ID");
    }
    
    /**
     * If outsourced radio button is selected set isOutsourced boolean
     * to true and modify dynamic label to Company Name
     * @param event 
     */
    @FXML
    void handleOutsourcedRBtn(ActionEvent event) {
        isOutsourced = true;
        modifyPartDynamicLbl.setText("Company Name");
    }

    /**
     * Seek user confirmation before canceling modifications and
     * switching scene to MainScreen
     * @param event
     * @throws IOException
     */
    @FXML
    void handleModifyPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Cancellation");
        alert.setContentText("Are you sure you want to cancel modifying part " + nameTxt.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            System.out.println("Ok selected. Part modification cancelled.");
            displayScene(event, "/fxml/MainScreen.fxml");
        } else {
            System.out.println("Cancel clicked. Please complete part modification.");
        }
    }

    /**
     * Validate part attributes and save modifications to chosen
     * Part object then switch scene to MainScreen
     * @param event
     * @throws IOException
     */
    @FXML
    void handleModifyPartSave(ActionEvent event) throws IOException {
        String partId = partIdTxt.getText();
        String name = nameTxt.getText();
        String price = priceTxt.getText();
        String inStock = inventoryTxt.getText();
        String min = minTxt.getText();
        String max = maxTxt.getText();
        String partDynamicValue = modifyPartDynamicTxt.getText();
        errorMessage = "";
        
        try {
            errorMessage = Validator.isValidPart(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), errorMessage);
            if(errorMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Part!");
                alert.setHeaderText("Error!");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            } else {
                if(isOutsourced) {
                    partService.updateOutsourcedPart(partIndex, Integer.parseInt(partId), name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), partDynamicValue);
                } else {
                    partService.updateInhousePart(partIndex, Integer.parseInt(partId), name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), Integer.parseInt(partDynamicValue));
                }
                displayScene(event, "/fxml/MainScreen.fxml");
            }

        } catch (NumberFormatException e) {
            System.out.println("Blank Fields");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part!");
            alert.setHeaderText("Error");
            alert.setContentText("Form contains blank field.");
            alert.showAndWait();
        }

    }

}
