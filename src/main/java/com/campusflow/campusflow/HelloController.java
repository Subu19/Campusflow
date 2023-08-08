package com.campusflow.campusflow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import com.campusflow.campusflow.HelloApplication;
import com.campusflow.campusflow.database.Database;
public class HelloController {

    @FXML
    private PasswordField PASSWORD;

    @FXML
    private TextField USERNAME;

    @FXML
    private Button loginbutton;

    @FXML
    private Label feedback;

    @FXML
    private Button settingsbtn;

    @FXML
    private TabPane settingsTab;
    //database variables
    @FXML
    private TextField database;

    @FXML
    private TextField dbpass;

    @FXML
    private TextField dbuser;

    @FXML
    private TextField host;

    @FXML
    private TextField port;

    //database submission
    @FXML
    void onSubmitDB(ActionEvent event) {
        if(!dbpass.getText().isEmpty() && !dbuser.getText().isEmpty() && !database.getText().isEmpty() && !host.getText().isEmpty() && !port.getText().isEmpty() ){
            if(Database.postDatabaseDetails(host.getText(),database.getText(),dbuser.getText(),dbpass.getText(),port.getText())){
                System.out.println("Saved!");
            }else{
                System.out.println(("Couldnt save!"));
            }
        }else{
            System.out.println("Empty fields");
        }
    }


    @FXML
    void onSettings(ActionEvent event) {
        //show settings
        settingsTab.setVisible(true);
        //check if there is database info stored in the config file.
        if(Database.checkDatabaseDetails()){

        }else{

        }

    }

    @FXML
    void onLogin(ActionEvent event) throws InterruptedException {
        BackgroundFill greenFill = new BackgroundFill(Color.GREEN, null, null);
        Background green = new Background(greenFill);
        BackgroundFill redFIll = new BackgroundFill(Color.GREEN, null, null);
        Background red = new Background(redFIll);
        loginbutton.setVisible(false);
        Stage mainwindow =(Stage) loginbutton.getScene().getWindow();
        if(USERNAME.getText().equals("subu") && PASSWORD.getText().equals(("1234"))){
            feedback.setText("Success! Opening Dashboard...");
            feedback.setTextFill(Color.GREEN);
            HelloApplication.loggedin = true;
            Dashboard dashboard = new Dashboard(mainwindow);



        }else{
            feedback.setText("Failed! Try again!");
            feedback.setTextFill(Color.RED);
            USERNAME.setText("");
            PASSWORD.setText("");
            loginbutton.setVisible(true);
        }
    }

}
