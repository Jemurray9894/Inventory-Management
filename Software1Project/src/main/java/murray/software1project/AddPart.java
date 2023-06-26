package murray.software1project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import murray.software1project.Model.InHouse;
import murray.software1project.Model.Inventory;
import murray.software1project.Model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class AddPart implements Initializable {


    @FXML
    private Button partCancelBT;

    @FXML
    private Button savePartBT;

    @FXML
    private TextField addPartID;

    @FXML
    private TextField textnameAddPart;

    @FXML
    private TextField textinventoryAddPart;

    @FXML
    private TextField textpriceAddPart;

    @FXML
    private TextField textmaxAddPart;

    @FXML
    private TextField radioTextAdd;

    @FXML
    private TextField textminAddPart;

    @FXML
    private RadioButton inhousePartradio;

    @FXML
    private RadioButton outsourcePartradio;
    @FXML
    private Label radioLabelPart;

    /**
     * Takes you back to the main form when the Cancel button is clicked. Confirms if user wants to cancel before all unsaved entries are cleared.
     * RUNTIME ERROR  NullPointException Cannot invoke "javafx.scene.control.Button.getScene()" because "this.CancelPartBT" is null
     * * 	at murray.software1project/murray.software1project.AddPart.handleCancelPartBT(AddPart.java:21)
     * * 	I fixed this by adding the proper fx:id for the cancel button in the fxml file for the AddPart form, I also made sure to add fx:id tags to mostly everything, as the NullPointerException error came up many times forme for the same reason.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void handlepartCancel(ActionEvent event) throws IOException {
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
     * Controls what happens when the in-house radio button is clicked. Changes the text here to "Machine ID"
     *
     * @param event
     */
    @FXML
    private void handleinhousePartradio(ActionEvent event) {

            radioLabelPart.setText("Machine ID");
        }


    /**
     * Controls what clicking the outsourced radio button does. Changes the text here to "Company Name".
     *
     * @param event
     */
    @FXML
    private void handleoutsourcePartradio(ActionEvent event) {

            radioLabelPart.setText("Company Name");
        }


    /**
     * Saves the part to the table when the Save button is clicked.
     *
     * @param event
     */
    @FXML
    public void handlesavePartBT(ActionEvent event) {
        try {
            Random rand = new Random();
            int uniqueID = new Random().nextInt(1000) + 1;
            String name = textnameAddPart.getText();
            int stock = Integer.parseInt(textinventoryAddPart.getText());
            double price = Double.parseDouble(textpriceAddPart.getText());
            int min = Integer.parseInt(textminAddPart.getText());
            int max = Integer.parseInt(textmaxAddPart.getText());

            int machineID = 0;
            String companyName;

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
                return;
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
             * Adds to list if in house radio button is selected
             */

            if (inhousePartradio.isSelected()) {
                machineID = Integer.parseInt(radioTextAdd.getText());
                InHouse addPart = new InHouse(uniqueID, name, price, stock, min, max, machineID);
                Inventory.addPart(addPart);
            }
            /**
             * Adds to list if outsource radio button is selected
             */

                if (outsourcePartradio.isSelected()) {
                    companyName = radioTextAdd.getText();
                    Outsourced addPart = new Outsourced(uniqueID, name, price, stock, min, max, companyName);
                    Inventory.addPart(addPart);
                }
                /**
                 * Returns an error if either the in house or outsourced radio buttons are left unselected.
                 */
                if (!inhousePartradio.isSelected() && !outsourcePartradio.isSelected()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Part type not selected");
                    alert.setContentText("Please select either In-House or Outsourced.");
                    alert.showAndWait();
                    return;
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
     * Generates a unique ID for each part
     * @return
     */


    /**
     * Gets Part ID and sets it to the ID text field.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}



