package murray.software1project;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import murray.software1project.Model.Inventory;
import murray.software1project.Model.Part;
import murray.software1project.Model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static murray.software1project.Model.Inventory.lookupPart;
import static murray.software1project.Model.Inventory.lookupProduct;


public class MainController implements Initializable {

    @FXML private TableView<Part> mainPartsTable;
    @FXML private TableColumn<Part, Integer> columnPartID;
    @FXML private TableColumn<Part, String> columnPartName;
    @FXML private TableColumn<Part, Integer> columnPartInventory;
    @FXML private TableColumn<Part, Double> columnPartPrice;

    @FXML private TableView<Product> mainProductsTable;
    @FXML private TableColumn<Product, Integer> columnProductID;
    @FXML private TableColumn<Product, String> columnProductName;
    @FXML private TableColumn<Product, Integer> columnProductInventory;
    @FXML private TableColumn<Product, Double> columnProductPrice;



    @FXML
    private TextField mainSearchPart;

    @FXML
    private TextField mainSearchProduct;

    @FXML
    private Button ADDPartBT;
    @FXML
    private Button MODPartBT;

    @FXML
    private Button DELPartBT;

    @FXML
    private Button ADDProdBT;

    @FXML
    private Button MODProdBT;

    @FXML
    private Button DELProdBT;

    @FXML
    private Button ExitMainBT;





    /**
     *Gives functionality to the Add button on the parts side. Opens the Add Part form.
     * @param event
     * @throws IOException
     */
    public void handleADDPartBT(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPart.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 500);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens the Modify Part form when the Modify button is clicked oin the parts side. SHows an error message if no part is selected.
     * @param event
     * @throws IOException
     */
    public void handleMODPartBT(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPart.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            ModifyPart MPController = loader.getController();
            MPController.sendPart(mainPartsTable.getSelectionModel().getSelectedIndex(), mainPartsTable.getSelectionModel().getSelectedItem());

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Return and select a Part");
            alert.showAndWait();
        }
    }

    /**
     * Deletes the selected part when the Delete button is clicked. Provides an error message when no part is selected and Confirms deletion if a part is selected.
     * @param event
     */
    @FXML
    void handleDelPart(ActionEvent event) {
        Part selectedPart = mainPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a part to delete.");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to delete this part?");
        alert.setContentText("Part ID: " + selectedPart.getId() + "\nPart Name: " + selectedPart.getName());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
        }
    }

    /**
     * Deletes a selected product. Prevents deletion if there is an associated part.
     * @param event
     */
    @FXML
    void handleDELProdBT(ActionEvent event) {
        Product selectedProduct = mainProductsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a product to delete.");
            alert.showAndWait();
            return;
        } else if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Product cannot be deleted because it has associated parts.");
            alert.showAndWait();
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you want to delete this product?");
            alert.setContentText("Product ID: " + selectedProduct.getId() + "\nProduct Name: " + selectedProduct.getName());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedProduct);
            }
        }

    }
    /**
     * Allows you to search the table for a part via ID or Name. Returns errors if no parts are found with either variable.
     * @param event
     */
    @FXML
    void handlemainSearchPart(ActionEvent event) {
        String searchField = mainSearchPart.getText();
        try {
            ObservableList<Part> tempParts = (ObservableList<Part>) Inventory.lookupPart(searchField);
            if (tempParts.isEmpty()) {
                int searchPartID = Integer.parseInt(searchField);
                Part searchPart = lookupPart(searchPartID);
                if (searchPart != null) {
                    tempParts.add(searchPart);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attention");
                    alert.setContentText("No parts found via Part ID");
                    alert.showAndWait();
                }
            }
            mainPartsTable.setItems(tempParts);
        } catch (NumberFormatException e) {
            Alert noParts = new Alert(Alert.AlertType.ERROR);
            noParts.setTitle("Search Error");
            noParts.setContentText("No parts found. Please try again.");
            noParts.showAndWait();
        }
    }

    /**
     * Allows you to search the table for a product via ID or Name. Returns errors if no parts are found with either variable.
     * @param event
     */
    @FXML
    void handlemainSearchProduct(ActionEvent event) {
        String searchField = mainSearchProduct.getText();
        try {
            ObservableList<Product> tempProducts = (ObservableList<Product>) Inventory.lookupProduct(searchField);
            if (tempProducts.isEmpty()) {
                int searchProductID = Integer.parseInt(searchField);
                Product searchProd = lookupProduct(searchProductID);
                if (searchProd != null) {
                    tempProducts.add(searchProd);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attention");
                    alert.setContentText("No products found via Part ID");
                    alert.showAndWait();
                }
            }
            mainProductsTable.setItems(tempProducts);
        } catch (NumberFormatException e) {
            Alert noParts = new Alert(Alert.AlertType.ERROR);
            noParts.setTitle("Search Error");
            noParts.setContentText("No products found. Please try again.");
            noParts.showAndWait();
        }
    }


    /**
     * Opens the Add Product form when the Add button is clicked on the products side.
     * @param event
     * @throws IOException
     */
    public void handleADDProdBT(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1050, 495);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

    }

    /**
     * Opens modify product form when the Modify button is clicked on the Products side.
     * @param event
     * @throws IOException
     */
    public void handleMODProdBT(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1019, 490);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            ModifyProduct MPController = loader.getController();
            MPController.sendProduct(mainProductsTable.getSelectionModel().getSelectedIndex(), mainProductsTable.getSelectionModel().getSelectedItem());

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Return and select a Product");
            alert.showAndWait();
        }
    }
    /**
     * Closes the application when the exit button is clicked.
     * @param event
     * @throws IOException
     */
    public void handleExitBT(ActionEvent event) throws IOException {
        Stage stage = (Stage) ExitMainBT.getScene().getWindow();
        stage.close();
    }

    /**
     * Populates the main form Parts table with the parts enters on the Add Part form.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPartsTable.setItems(Inventory.getallParts());
        columnPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        /**
         * Populates the main for Products table with the data entered on the Add Product form.
         */
        mainProductsTable.setItems(Inventory.getallProducts());
        columnProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnProductInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
