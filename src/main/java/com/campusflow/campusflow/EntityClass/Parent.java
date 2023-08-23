package com.campusflow.campusflow.EntityClass;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
public class Parent {
    private SimpleStringProperty pName;
    private SimpleStringProperty pAddress;
    private SimpleIntegerProperty pContact;
    private SimpleStringProperty pEmail;
    private SimpleIntegerProperty pId;


    public String getpName() {
        return pName.get();
    }

    public SimpleStringProperty pNameProperty() {
        return pName;
    }

    public String getpAddress() {
        return pAddress.get();
    }

    public SimpleStringProperty pAddressProperty() {
        return pAddress;
    }

    public int getpContact() {
        return pContact.get();
    }

    public SimpleIntegerProperty pContactProperty() {
        return pContact;
    }

    public String getpEmail() {
        return pEmail.get();
    }

    public SimpleStringProperty pEmailProperty() {
        return pEmail;
    }

    public int getpId() {
        return pId.get();
    }

    public SimpleIntegerProperty pIdProperty() {
        return pId;
    }

    public void setpName(String pName) {
        this.pName.set(pName);
    }

    public void setpAddress(String pAddress) {
        this.pAddress.set(pAddress);
    }

    public void setpContact(int pContact) {
        this.pContact.set(pContact);
    }

    public void setpEmail(String pEmail) {
        this.pEmail.set(pEmail);
    }

    public void setpId(int pId) {this.pId.set(pId);}

    public Parent(Integer id, String name, String address, Integer contact, String email){
        this.pAddress =new SimpleStringProperty(address);
        this.pName = new SimpleStringProperty(name);
        this.pContact = new SimpleIntegerProperty(contact);
        this.pId = new SimpleIntegerProperty(id);
        this.pEmail = new SimpleStringProperty(email);
    }
}
