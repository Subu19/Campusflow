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
    requires java.mail;
    requires activation;
    requires jdk.httpserver;
    //requires mail;
    opens com.campusflow.campusflow to javafx.fxml;
    opens com.campusflow.campusflow.EntityClass to javafx.base;
    exports com.campusflow.campusflow;
}