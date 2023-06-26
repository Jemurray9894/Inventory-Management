package murray.software1project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import murray.software1project.Model.Inventory;
import murray.software1project.Model.Part;
import murray.software1project.Model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import static murray.software1project.Model.Inventory.updateProduct;
import static murray.software1project.Model.Inventory.lookupPart;
public class ModifyProduct implements Initializable {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    @FXML
    private Button modProdCancel;
    @FXML
    private Button modProdSave;
    @FXML
    private Button modProdTableAdd;
    @FXML
    private Button modAssocTableRemove;
    @FXML
    private TextField modProdID;
    @FXML
    private TextField modProdName;
    @FXML
    private TextField modProdInventory;
    @FXML
    private TextField modProdPrice;
    @FXML
    private TextField modProdMax;
    @FXML
    private TextField modProdMin;
    @FXML
    private TextField modProdSearch;

    @FXML private TableView<Part> modProdTable;
    @FXML private TableColumn<Part, Integer> modProdTableID;
    @FXML private TableColumn<Part, String> modProdTableName;
    @FXML private TableColumn<Part, Integer> modProdTableInventory;
    @FXML private TableColumn<Part, Double> modProdTablePrice;

    @FXML private TableView<Part> modAssocTable;
    @FXML private TableColumn<Part, Integer> modAssocTableID;
    @FXML private TableColumn<Part, String> modAssocTableName;
    @FXML private TableColumn<Part, Integer> modAssocTableInventory;
    @FXML private TableColumn<Part, Double> modAssocTablePrice;

    private int currentIndex = 0;


    /**
     * Brings product data from main table to the modify form
     * @param selectedIndex
     * @param product
     */
    @FXML
    public void sendProduct(int selectedIndex, Product product) {
        currentIndex = selectedIndex;

        modProdID.setText(Integer.toString(product.getId()));
        modProdName.setText(product.getName());
        modProdInventory.setText(Integer.toString(product.getStock()));
        modProdPrice.setText(Double.toString(product.getPrice()));
        modProdMax.setText(Integer.toString(product.getMax()));
        modProdMin.setText(Integer.toString(product.getMin()));

        for (Part part: product.getAllAssociatedParts()) {
            associatedParts.add(part);
        }
    }



    /**
     * Takes you back to the main form when the Cancel button is clicked. Confirms if user wants to cancel before all unsaved entries are cleared.
     * @param event
     * @throws IOException
     */
    public void handlemodProdCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Unsaved modifications will be cleared.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainform.fxml"));
            Parent root = loader.load();
            Scene mainScene = new Scene(root);
            stage.setScene(mainScene);
            stage.show();
        } else {

        }
    }

    /**
     * Saves the Product to the table and returns to the main form when the Save button is clicked.
     * @param event
     */
    @FXML
    public void handlemodProdSave(ActionEvent event) {
        try {
            int id = Integer.parseInt(modProdID.getText());
            String name = modProdName.getText();
            int stock = Integer.parseInt(modProdInventory.getText());
            double price = Double.parseDouble(modProdPrice.getText());
            int min = Integer.parseInt(modProdMin.getText());
            int max = Integer.parseInt(modProdMax.getText());

            /**
             * Validates if the min is less than the max value are provides an error if not
             */
            if (max < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Invalid");
                alert.setHeaderText("Review Min and Max Values");
                alert.setContentText("The Max value cannot be less than the Min value");
                alert.showAndWait();
                return;
            }
            /**
             * Validates that the inventory stock number is between the max and the min values selected
             */
            else if (stock < min || stock > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Input Invalid");
                alert.setContentText("Inventory value should be between Max and Min.");
                alert.showAndWait();
            }
            /**
             * Ensures that the name field isn't left empty
             */
            else if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Input Invalid");
                alert.setContentText("Name field cannot be empty");
                alert.showAndWait();
                return;

            }
            /**
             * Updates Product on the Products table on the main form while also accounting for it's associated part.
             */
            Product updatedProduct = new Product(id, name, price, stock, min, max);
            if (updatedProduct != associatedParts) {
                Inventory.updateProduct(currentIndex, updatedProduct);
            }

            for (Part part : associatedParts) {
                if (part != associatedParts)
                    updatedProduct.addAssociatedPart(part);
            }

            /**
             * Takes you back to the main screen after save is clicked
             */
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainform.fxml"));
            Parent root = loader.load();
            Scene mainScene = new Scene(root);
            stage.setScene(mainScene);
            stage.show();
        }
        /**
         * Checks to make sure all fields have something entered before saving to the table.
         */ catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Addition");
            alert.setContentText("Enter valid input for all fields.");
            alert.showAndWait();
            return;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Allows you to search for parts via ID or name. Gives an error if part is not found via name or ID.
     * @param event
     */
    @FXML
    void handlemodProdSearch(ActionEvent event) {
        String searchField = modProdSearch.getText();
        try {
            ObservableList<Part> tempParts = (ObservableList<Part>) Inventory.lookupPart(searchField);
            if (tempParts.isEmpty()) {
                int searchID = Integer.parseInt(searchField);
                Part searchP = lookupPart(searchID);
                if (searchP != null) {
                    tempParts.add(searchP);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attention");
                    alert.setContentText("No parts found via Part ID");
                    alert.showAndWait();
                }
            }
            modProdTable.setItems(tempParts);
        } catch (NumberFormatException e) {
            Alert noParts = new Alert(Alert.AlertType.ERROR);
            noParts.setTitle("Search Error");
            noParts.setContentText("No parts found. Please try again.");
            noParts.showAndWait();
        }
    }
    /**
     * Gives functionality to the Add button under the Parts table. Takes the selected part and adds it to the Associated Parts table.
     * @param event
     */
    @FXML
    void handlemodProdTableAdd(ActionEvent event) {
        Part selectedPart = modProdTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection Detected");
            alert.setContentText("Please select a part from the table");
            alert.showAndWait();
            return;
        } else if (!associatedParts.contains(selectedPart)) {
            associatedParts.add(selectedPart);
            modAssocTable.setItems(associatedParts);
        }
    }
    /**
     * Gives functionality to the Remove Associated Part button. Removes the selected part and disassociates it from the Product.
     * @param event
     */
    @FXML
    void handlemodAssocTableRemove(ActionEvent event) {
        Part selectedPart = modAssocTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection Detected");
            alert.setContentText("Please select a part from the table");
            alert.showAndWait();
            return;
        } else if (associatedParts.contains(selectedPart)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Removal");
            alert.setContentText("Are you sure you want to remove the associated part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(selectedPart);
                modAssocTable.setItems(associatedParts);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         * Populates the tables on the Modify Product form
         */

        modProdTable.setItems(Inventory.getallParts());
        modProdTableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdTableInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        modAssocTable.setItems(associatedParts);
        modAssocTableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modAssocTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modAssocTableInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modAssocTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
