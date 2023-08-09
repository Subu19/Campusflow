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
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class HelloController {

    @FXML
    private PasswordField PASSWORD;

    @FXML
    private TextField USERNAME;

    @FXML
    private Button loginbutton;

    @FXML
    private Label feedback;

    //
    //Settings TAB Menu
    //
    @FXML
    private TabPane settingsTab;

    @FXML
    private Label databaseAlert;

    @FXML
    private Button settingsbtn;

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

    @FXML
    private Button databaseSubmitBtn;
    //database submission
    @FXML
    void onSubmitDB(ActionEvent event) {
        if(!dbpass.getText().isEmpty() && !dbuser.getText().isEmpty() && !database.getText().isEmpty() && !host.getText().isEmpty() && !port.getText().isEmpty() ){
            if(Database.postDatabaseDetails(host.getText(),database.getText(),dbuser.getText(),dbpass.getText(),port.getText())){
                databaseAlert.setText("Successfully saved database!");
                requestConnection();
            }else{
                databaseAlert.setText("Error occurred when saving file!");
            }
        }else{
            databaseAlert.setText("Fields cant be empty!");
        }
    }


    @FXML
    void onSettings(ActionEvent event) {
        studentsTab.setVisible(false);
        teachersTab.setVisible(false);
        homeTab.setVisible(false);
        //
        //TRUE
        settingsTab.setVisible(true);
        //check if there is database info stored in the config file.
        if(Database.checkDatabaseDetails()){
            JSONParser jsonParser = new JSONParser();
            try(FileReader reader = new FileReader("./src/main/resources/config/config.json")){
                Object obj = jsonParser.parse(reader);
                JSONObject json = (JSONObject) obj;

                host.setText((String) json.get("host"));
                port.setText((String) json.get("port").toString());
                database.setText((String) json.get("database"));
                dbuser.setText((String) json.get("username"));
                dbpass.setText((String) json.get("password"));
                databaseSubmitBtn.setText("Update");

                databaseAlert.setText("Above information is being used for database connection!");
                requestConnection();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }else{
            databaseAlert.setText("Something went wrong!");
        }

    }
    private void requestConnection(){
        String checkConnection = Database.getConnection(host.getText(),database.getText(),dbuser.getText(),dbpass.getText(),port.getText());
        if(Objects.equals(checkConnection, "connected")){
            databaseAlert.setText("Connected to database.");
        }else{
            databaseAlert.setText(checkConnection);
        }
    }
    //
    // Students TAB Menu
    //
    @FXML
    private TabPane studentsTab;

    @FXML
    private Button studentsbtn;

    @FXML
    void onStudents(ActionEvent event) {
        settingsTab.setVisible(false);
        teachersTab.setVisible(false);
        homeTab.setVisible(false);
        //
        //TRUE
        studentsTab.setVisible(true);

    }

    //
    //Teachers TAB Menu
    //
    @FXML
    private TabPane teachersTab;

    @FXML
    void onTeachers(ActionEvent event) {
        settingsTab.setVisible(false);
        studentsTab.setVisible(false);
        homeTab.setVisible(false);
        // TRUE
        teachersTab.setVisible(true);

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



    //////=================== Home TAB =======================//
    @FXML
    private TabPane homeTab;

    @FXML
    void onHome(ActionEvent event) throws InterruptedException {
        teachersTab.setVisible(false);
        studentsTab.setVisible(false);
        settingsTab.setVisible(false);
        homeTab.setVisible(true);
    }
}
