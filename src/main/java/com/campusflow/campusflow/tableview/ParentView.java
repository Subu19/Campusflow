package com.campusflow.campusflow.tableview;
import com.campusflow.campusflow.EntityClass.Parent;
import com.campusflow.campusflow.database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ParentView {
    private TableColumn<Parent, String> pAddress;
    private  TableColumn<Parent, Long> pContact;
    private  TableColumn<Parent, String> pEmail;
    private TableColumn<Parent, Integer> pId;
    private  TableColumn<Parent,String> pName;
    private TableView<Parent> tableView;
    private String search;


    ObservableList<Parent> parentData(){

        Vector<Parent> parentVector = new Vector<Parent>();
        Integer id;
        String name;
        String address;
        Integer contact;
        String email;

        try{
            String sql = "SELECT * FROM `parents`;";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                id = result.getInt("pid");
                name = result.getString("first_name")+" "+result.getString("middle_name")+" "+result.getString("last_name");
                address= result.getString("address");
                contact = Math.toIntExact(result.getLong("contact"));
                email = result.getString("email");

                Parent newParent = new Parent(id,name,address,contact,email);
                parentVector.add(newParent);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

//        Student s1 = new Student(1,"Subu","ktm",1000010, "subuacharya19@gmail.com", "BIT", "2020", 1);
//        Student s2 = new Student(2,"Subu2","ktm",1002010, "subuacharya19@gmail.com", "BIT", "2020", 1);

        return (ObservableList<Parent>) FXCollections.observableArrayList(parentVector);
    }

    public ParentView(javafx.scene.control.TableView<Parent> ParentTable, javafx.scene.control.TableColumn<Parent, Integer> pID, javafx.scene.control.TableColumn<Parent, String> pName, javafx.scene.control.TableColumn<Parent, String> pAddress, javafx.scene.control.TableColumn<Parent, Long> pContact, javafx.scene.control.TableColumn<Parent, String> pEmail) {
        this.tableView = ParentTable;
        this.pEmail= pEmail;
        this.pContact = pContact;
        this.pAddress= pAddress;
        this.pName = pName;
        this.pId = pID;


        this.pName.setCellValueFactory(new PropertyValueFactory<Parent, String>("pName"));
        this.pId.setCellValueFactory(new PropertyValueFactory<Parent, Integer>("pId"));
        this.pAddress.setCellValueFactory(new PropertyValueFactory<Parent, String>("pAddress"));
        this.pContact.setCellValueFactory(new PropertyValueFactory<Parent, Long>("pContact"));
        this.pEmail.setCellValueFactory(new PropertyValueFactory<Parent, String>("pEmail"));

        tableView.setItems(parentData());
    }

    ObservableList<Parent> parentDataSearch(){

        Vector<Parent> parentVector = new Vector<Parent>();
        Integer id;
        String name;
        String address;
        Integer contact;
        String email;

        try{
            String sql = "SELECT * FROM `parents`WHERE pid = '" + search + "' OR first_name = '" + search + "';";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                id = result.getInt("pid");
                name = result.getString("first_name")+" "+result.getString("middle_name")+" "+result.getString("last_name");
                address= result.getString("address");
                contact = Math.toIntExact(result.getLong("contact"));
                email = result.getString("email");

                Parent newParent = new Parent(id,name,address,contact,email);
                parentVector.add(newParent);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

//        Student s1 = new Student(1,"Subu","ktm",1000010, "subuacharya19@gmail.com", "BIT", "2020", 1);
//        Student s2 = new Student(2,"Subu2","ktm",1002010, "subuacharya19@gmail.com", "BIT", "2020", 1);

        return (ObservableList<Parent>) FXCollections.observableArrayList(parentVector);
    }
    public ParentView(String search,javafx.scene.control.TableView<Parent> ParentTable, javafx.scene.control.TableColumn<Parent, Integer> pID, javafx.scene.control.TableColumn<Parent, String> pName, javafx.scene.control.TableColumn<Parent, String> pAddress, javafx.scene.control.TableColumn<Parent, Long> pContact, javafx.scene.control.TableColumn<Parent, String> pEmail) {
        this.tableView = ParentTable;
        this.pEmail= pEmail;
        this.pContact = pContact;
        this.pAddress= pAddress;
        this.pName = pName;
        this.pId = pID;
        this.search= search;


        this.pName.setCellValueFactory(new PropertyValueFactory<Parent, String>("pName"));
        this.pId.setCellValueFactory(new PropertyValueFactory<Parent, Integer>("pId"));
        this.pAddress.setCellValueFactory(new PropertyValueFactory<Parent, String>("pAddress"));
        this.pContact.setCellValueFactory(new PropertyValueFactory<Parent, Long>("pContact"));
        this.pEmail.setCellValueFactory(new PropertyValueFactory<Parent, String>("pEmail"));

        tableView.setItems(parentDataSearch());
    }
}


