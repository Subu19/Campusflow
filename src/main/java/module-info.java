module com.campusflow.campusflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires gson;
    requires json.simple;
    opens com.campusflow.campusflow to javafx.fxml;
    exports com.campusflow.campusflow;
}