package com.example.project3;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class GymManagerController {
    @FXML
    private TextField membership_fname, membership_lname;

    @FXML
    private DatePicker membership_dob;

    @FXML
    private ComboBox membership_loc;

    @FXML
    protected void onAddMembershipClick() {
        String fname = membership_fname.getText();
        String lname = membership_lname.getText();
        Date dob = new Date(membership_dob.getValue().toString());
    }
}