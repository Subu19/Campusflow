module com.campusflow.campusflow {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.campusflow.campusflow to javafx.fxml;
    exports com.campusflow.campusflow;
}