package com.example.project3;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class GymManagerController {
    @FXML
    private TextField membership_fname, membership_lname;

    @FXML
    private DatePicker membership_dob;

    @FXML
    private ComboBox membership_loc;

    @FXML
    private TextArea messageArea;

    private static MemberDatabase database;

    private static ClassSchedule schedule;


    @FXML
    public void initialize(){
        database = new MemberDatabase();
        schedule = new ClassSchedule();
    }

    @FXML
    void onAddMembershipClick(ActionEvent event) {
        String fname = membership_fname.getText();
        String lname = membership_lname.getText();
        Date dob = new Date(membership_dob.getValue().toString());

    }

    @FXML
    void loadMemberList(ActionEvent event){
        database.loadMembers();
    }

    @FXML
    void loadClassList(ActionEvent event){
        schedule.loadSchedule();
    }

    @FXML
    void printMembers(ActionEvent event){
        messageArea.appendText(database.printDefault());
    }

    @FXML
    void printMembersWithFees(ActionEvent event){
        messageArea.appendText(database.printWithFees());
    }

    @FXML
    void printMembersByLocation(ActionEvent event){
        messageArea.appendText(database.printByLocation());
    }

    @FXML
    void printMembersByName(ActionEvent event){
        messageArea.appendText(database.printByName());
    }

    @FXML
    void printMembersByExpiration(ActionEvent event){
        messageArea.appendText(database.printByExpirationDate());
    }

    @FXML
    void printClasses(ActionEvent event){
        messageArea.appendText(schedule.printSchedule());
    }
}