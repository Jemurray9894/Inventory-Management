package murray.software1project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import murray.software1project.Model.InHouse;
import murray.software1project.Model.Inventory;
import murray.software1project.Model.Outsourced;
import murray.software1project.Model.Part;

import java.io.IOException;
import java.util.Optional;


public class ModifyPart {
    @FXML
    private Button cancelModifyPartBT;

    @FXML
    private Button saveModifyPartBT;

    @FXML
    private TextField modifyPartID;

    @FXML
    private TextField textnameModifyPart;

    @FXML
    private TextField textinventoryModifyPart;

    @FXML
    private TextField textpriceModifyPart;

    @FXML
    private TextField textmaxModifyPart;


    @FXML
    private TextField textminModifyPart;

    @FXML
    private TextField radioTextAdd;

    @FXML
    private RadioButton inhousePartModify;

    @FXML
    private RadioButton outsourcePartModify;

    @FXML
    private Label radioLabelPart;

    private int currentIndex = 0;



    /**
     * Retrieves selected Product data from the table on the main screen.
     *
     * @param selectedIndex
     * @param part
     */
    public void sendPart(int selectedIndex, Part part) {
        currentIndex = selectedIndex;

        modifyPartID.setText(Integer.toString(part.getId()));
        textnameModifyPart.setText(part.getName());
        textinventoryModifyPart.setText(Integer.toString(part.getStock()));
        textpriceModifyPart.setText(Double.toString(part.getPrice()));
        textmaxModifyPart.setText(Integer.toString(part.getMax()));
        textminModifyPart.setText(Integer.toString(part.getMin()));


        if (part instanceof InHouse) {
            inhousePartModify.setSelected(true);
            radioLabelPart.setText("Machine ID");
            radioTextAdd.setText(String.valueOf(((InHouse) part).getMachineID()));
        } else {
            outsourcePartModify.setSelected(true);
            radioLabelPart.setText("Company Name");
            radioTextAdd.setText(((Outsourced) part).getCompanyName());
        }
    }




    /**
     * Takes the application back to the main form when the cancel button is clicked. Alerts user that unsaved entries will be cleared.
      * @param event
     * @throws IOException
     */
    @FXML
    public void handlepartCancelM(ActionEvent event) throws IOException {
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

        }
    }
    /**
     * Controls what happens when the in-house radio button is clicked. Changes the text here to "Machine ID"
     * @param event
     */
    @FXML
    private void handleinhousePartradio(ActionEvent event) {

        radioLabelPart.setText("Machine ID");
        }


    /**
     * Controls what happens when the in-house radio button is clicked. Changes the text here to "Company Name"
     * @param event
     */
   @FXML
   private void handleoutsourcePartradio(ActionEvent event) {

       radioLabelPart.setText("Company Name");
        }

    /**
     * Saves the modification and returns to the main form.
     * @param event
     */
    @FXML
    public void handleSaveModifyPart(ActionEvent event) {
        try {
            int id = Integer.parseInt(modifyPartID.getText());
            String name = textnameModifyPart.getText();
            int stock = Integer.parseInt(textinventoryModifyPart.getText());
            double price = Double.parseDouble(textpriceModifyPart.getText());
            int min = Integer.parseInt(textminModifyPart.getText());
            int max = Integer.parseInt(textmaxModifyPart.getText());

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
             * Updates part on list if in house radio button is selected
             */

            if (inhousePartModify.isSelected()) {
                machineID = Integer.parseInt(radioTextAdd.getText());
                InHouse updatedPart = new InHouse(id, name, price, stock, min, max, machineID);
                Inventory.updatePart(currentIndex, updatedPart);
            }
            /**
             * Updates part on list if outsource radio button is selected
             */
            else {
                if (outsourcePartModify.isSelected()) {
                    companyName = radioTextAdd.getText();;
                    Outsourced updatedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                    Inventory.updatePart(currentIndex, updatedPart);
                }
                /**
                 * Returns an error if either the in house or outsourced radio buttons are left unselected.
                 */
                if (!inhousePartModify.isSelected() && !outsourcePartModify.isSelected()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Part type not selected");
                    alert.setContentText("Please select either In-House or Outsourced.");
                    alert.showAndWait();
                    return;
                }
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
         */
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Addition");
            alert.setContentText("Enter valid input for all fields.");
            alert.showAndWait();
            return;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}


