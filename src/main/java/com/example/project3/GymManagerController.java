package com.example.project3;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import static com.example.project3.Location.*;

public class GymManagerController{
    @FXML
    private TextField membership_fname, membership_lname, fitclass_fname, fitclass_lname;
    @FXML
    private DatePicker membership_dob, fitclass_dob;
    @FXML
    private ComboBox membership_loc, fitclass_classes;
    @FXML
    private CheckBox fitclass_is_guest;
    @FXML
    private TextArea messageArea;
    @FXML
    private ToggleGroup membership;
    private static MemberDatabase database;
    private static ClassSchedule schedule;
    private static String[] locationList;
    private static String[] classList;

    @FXML
    void onAddMembershipClick(ActionEvent event) {
        String fname = membership_fname.getText();
        String lname = membership_lname.getText();

        if(fname.isEmpty() || lname.isEmpty() || membership_dob.getValue() == null){
            messageArea.appendText("Field cannot be empty.\n");
            return;
        }

        if(membership_loc.getValue() == null){
            messageArea.appendText("Invalid location.\n");
            return;
        }

        Date dob = new Date(membership_dob.getValue().toString(), "-");
        Location loc = toLocation(membership_loc.getValue().toString());
        Plan membershipType = Plan.toPlan(membership.selectedToggleProperty()
                .getValue().toString().split("'")[1]);

        String error = checkBirthdayErrors(dob);
        if(error != null){
            messageArea.appendText(error);
            return;
        }

        Member member;
        if(membershipType == Plan.PREMIUM){ member = new Premium(fname, lname, dob, loc); }
        else if(membershipType == Plan.FAMILY){ member = new Family(fname, lname, dob, loc); }
        else{ member = new Member(fname, lname, dob, loc); }

        if(database.contains(member)) {
            messageArea.appendText(member.getFname() + " " + member.getLname() + " is already in the database.\n");
            return;
        }
        if(database.add(member)){
            messageArea.appendText(member.getFname() + " " + member.getLname() + " added.");
            membership_fname.setText("");
            membership_lname.setText("");
            membership_dob.setValue(null);
            membership_loc.setValue(null);
        }
    }

    @FXML
    void checkInClass(){
        String fname = fitclass_fname.getText();
        String lname = fitclass_lname.getText();
        if(fname.isEmpty() || lname.isEmpty() || fitclass_dob.getValue() == null){
            messageArea.appendText("Field cannot be empty.\n");
            return;
        }
        Date dob = new Date(fitclass_dob.getValue().toString(), "-");
        if(!dob.isValid()) {
            messageArea.appendText("DOB " + dob + ": invalid calendar date!\n");
            return;
        }
        FitnessClass fitclass = schedule.toClass(fitclass_classes.getValue().toString());
        if(fitclass == null){
            messageArea.appendText("Class does not exist\n");
            return;
        }
        Member member = database.getMember(new Member(fname, lname, dob));
        if (member == null) {
            messageArea.appendText(fname + " " + lname + " " + dob + " is not in the database.\n");
            return;
        }
        if (member.getExpire().isPast()) {
            messageArea.appendText(fname + " " + lname + " " + dob + " membership expired.");
            return;
        }
        boolean isGuest = fitclass_is_guest.isSelected();

        String message;
        if (isGuest) message = fitclass.addGuest(member);
        else message = fitclass.add(member);

        messageArea.appendText(message);

        if (fitclass.getLastAddSuccessful()){
            messageArea.appendText(fitclass.getClassMemberList());
            messageArea.appendText(fitclass.getClassGuestList());
        }
    }

    @FXML
    void loadMemberList(ActionEvent event){
        database.loadMembers();
    }

    @FXML
    void loadClassList(ActionEvent event){
        schedule.loadSchedule();
        updateClassInput();
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

    /**
     * Return error message if invalid birthday
     * @param dob   Date object of birthday
     * @return Error message if dob invalid, null otherwise
     */
    private String checkBirthdayErrors(Date dob) {
        if (!dob.isValid()) {
            return "DOB " + dob + ": invalid calendar date!\n";
        }
        if (!dob.isPast()) {
            return "DOB " + dob + ": cannot be today or a future date!\n";
        }
        if (!dob.isEighteen()) {
            return "DOB " + dob + ": must be 18 or older to join!\n";
        }
        return null;
    }

    @FXML
    void onRemoveMembershipClick(ActionEvent event){
        String fname = membership_fname.getText();
        String lname = membership_lname.getText();

        if(fname.isEmpty() || lname.isEmpty() || membership_dob.getValue() == null){
            messageArea.appendText("Field cannot be empty.\n");
            return;
        }

        Date dob = new Date(membership_dob.getValue().toString(), "-");

        String error = checkBirthdayErrors(dob);
        if(error != null){
            messageArea.appendText(error);
            return;
        }
        Member member = new Member(fname, lname, dob);
        if (member != null){
            if (database.remove(member)){
                messageArea.appendText(member.getFname() + " " + member.getLname() + " removed.\n");
                membership_fname.setText("");
                membership_lname.setText("");
                membership_dob.setValue(null);
                membership_loc.setValue(null);
            }
            else{
                messageArea.appendText(member.getFname() + " " + member.getLname() + " is not in the database.\n");
            }
        }
    }

    @FXML
    public void initialize() {
        database = new MemberDatabase();
        schedule = new ClassSchedule();

        locationList = new String[]{BRIDGEWATER.name(), EDISON.name(),
                FRANKLIN.name(), PISCATAWAY.name(), SOMERVILLE.name()};
        membership_loc.setItems(FXCollections.observableArrayList(locationList));
        updateClassInput();
    }

    public void updateClassInput(){
        FitnessClass[] classes = schedule.getClassList();
        if(classes != null){
            classList = new String[classes.length];
            for(int i=0; i<classes.length; i++){
                classList[i] = classes[i].toString();
            }
            fitclass_classes.setItems(FXCollections.observableArrayList(classList));
        }
    }


}