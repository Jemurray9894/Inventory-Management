package murray.software1project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Javadocs folder located within project's folder titled "Javadocs"
 */
public class MainForm extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainForm.class.getResource("mainform.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 500);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * FUTURE ENHANCEMENT: Perhaps in an updated version of this application, the user could select multiple parts and products at once to be deleted or modified.
     */
}