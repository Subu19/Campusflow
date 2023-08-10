module com.campusflow.campusflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;
    requires javafx.swing;
    requires json.simple;
    requires java.desktop;
    requires webcam.capture;
    requires com.google.zxing;
    requires zxing.javase;
    opens com.campusflow.campusflow to javafx.fxml;
    exports com.campusflow.campusflow;
}