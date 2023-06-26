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

import static murray.software1project.Model.Inventory.addProduct;
import static murray.software1project.Model.Inventory.lookupPart;

public class AddProduct implements Initializable {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    @FXML
    private Button addProdCancel;
    @FXML
    private Button addProdSave;
    @FXML
    private Button addAssocTableRemove;
    @FXML
    private Button addProdTableAdd;
    @FXML
    private TextField addProdID;
    @FXML
    private TextField addProdName;
    @FXML
    private TextField addProdInventory;
    @FXML
    private TextField addProdPrice;
    @FXML
    private TextField addProdMax;
    @FXML
    private TextField addProdMin;
    @FXML
    private TextField addProdSearch;

    @FXML private TableView<Part> addProdTable;
    @FXML private TableColumn<Part, Integer> addProdTableID;
    @FXML private TableColumn<Part, String> addProdTableName;
    @FXML private TableColumn<Part, Integer> addProdTableInventory;
    @FXML private TableColumn<Part, Double> addProdTablePrice;

    @FXML private TableView<Part> addAssocTable;
    @FXML private TableColumn<Part, Integer> addAssocTableID;
    @FXML private TableColumn<Part, String> addAssocTableName;
    @FXML private TableColumn<Part, Integer> addAssocTableInventory;
    @FXML private TableColumn<Part, Double> addPartAssocPrice;


    /**
     * Takes you back to the main form when the Cancel button is clicked. Confirms if user wants to cancel before all unsaved entries are cleared.
     * @param event
     * @throws IOException
     */
    public void handleaddProdCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Unsaved entries will be cleared.");
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
    public void handleaddProdSave(ActionEvent event) {
        try {
            Random rand = new Random();
            int uniqueID = new Random().nextInt(1000) + 1;
            String name = addProdName.getText();
            int stock = Integer.parseInt(addProdInventory.getText());
            double price = Double.parseDouble(addProdPrice.getText());
            int min = Integer.parseInt(addProdMin.getText());
            int max = Integer.parseInt(addProdMax.getText());

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
             * Adds Product to the Products table on the main form while also accounting for it's associated part.
             */
            Product product = new Product(uniqueID, name, price, stock, min, max);

            for (Part part : associatedParts) {
                if (part != associatedParts)
                    product.addAssociatedPart(part);
            }

            Inventory.getallProducts().add(product);
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
    void handleaddProdSearch(ActionEvent event) {
        String searchField = addProdSearch.getText();
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
            addProdTable.setItems(tempParts);
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
    void handleaddProdTableAdd(ActionEvent event) {
        Part selectedPart = addProdTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection Detected");
            alert.setContentText("Please select a part from the table");
            alert.showAndWait();
            return;
        } else if (!associatedParts.contains(selectedPart)) {
            associatedParts.add(selectedPart);
            addAssocTable.setItems(associatedParts);
        }
    }

    /**
     * Gives functionality to the Remove Associated Part button. Removes the selected part and disassociates it from the Product.
     * @param event
     */
    @FXML
    void handleaddAssocTableRemove(ActionEvent event) {
        Part selectedPart = addAssocTable.getSelectionModel().getSelectedItem();

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
                addAssocTable.setItems(associatedParts);
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         * Populates the tables on the Add Product form
         */

        addProdTable.setItems(Inventory.getallParts());
    addProdTableID.setCellValueFactory(new PropertyValueFactory<>("id"));
    addProdTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
    addProdTableInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
    addProdTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    addAssocTable.setItems(associatedParts);
    addAssocTableID.setCellValueFactory(new PropertyValueFactory<>("id"));
    addAssocTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
    addAssocTableInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
    addPartAssocPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
