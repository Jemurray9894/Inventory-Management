module murray.software1project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens murray.software1project to javafx.fxml;
    exports murray.software1project;
    exports murray.software1project.Model;
    opens murray.software1project.Model to javafx.fxml;


}