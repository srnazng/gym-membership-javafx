package com.example.project3;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.example.project3.Location.*;

/**
 * The GymManagerController class is the class that
 * processes the user interactions
 * @author Jackson Lee, Serena Zeng
 */
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

    /**
     * Checks if member is valid and if so, adds member to member database.
     * Called after clicking the Add button on the Membership tab.
     */
    @FXML
    void onAddMembershipClick() {
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
        Member member = new Member(fname, lname, dob, loc);
        if(membershipType == Plan.PREMIUM){ member = new Premium(fname, lname, dob, loc); }
        else if(membershipType == Plan.FAMILY){ member = new Family(fname, lname, dob, loc); }
        if(database.contains(member)) {
            messageArea.appendText(member.getFname() + " " + member.getLname() + " is already in the database.\n");
            return;
        }
        if(database.add(member)){
            messageArea.appendText(member.getFname() + " " + member.getLname() + " added.\n");
            membership_fname.setText("");
            membership_lname.setText("");
            membership_dob.setValue(null);
            membership_loc.setValue(null);
        }
    }

    /**
     * Checks if member with specified information is in database and if so, removes member from database.
     * Called after clicking the Remove button on the Membership tab.
     */
    @FXML
    void onRemoveMembershipClick(){
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

    /**
     * Checks in member or member's guest into fitness class and shows errors if unable to do so.
     * Called after clicking the Check In button on the Fitness Class tab.
     */
    @FXML
    void onCheckInClassClick(){
        String fname = fitclass_fname.getText();
        String lname = fitclass_lname.getText();
        if(fname.isEmpty() || lname.isEmpty() || fitclass_dob.getValue() == null || fitclass_classes.getValue() == null){
            messageArea.appendText("Field cannot be empty.\n");
            return;
        }
        Date dob = new Date(fitclass_dob.getValue().toString(), "-");
        if(!dob.isValid()) {
            messageArea.appendText("DOB " + dob + ": invalid calendar date!\n");
            return;
        }
        FitnessClass fitClass = schedule.toClass(fitclass_classes.getValue().toString());
        if(fitClass == null){
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
        if (isGuest) message = fitClass.addGuest(member);
        else message = fitClass.add(member);
        messageArea.appendText(message);
        if (fitClass.getLastAddSuccessful()){
            messageArea.appendText(fitClass.getClassMemberList());
            messageArea.appendText(fitClass.getClassGuestList());
            fitclass_fname.setText("");
            fitclass_lname.setText("");
            fitclass_dob.setValue(null);
            fitclass_classes.setValue(null);
        }
    }

    /**
     * Checks if member or member's guest is checked into class and
     * if so, removes member from participants in specified fitness class.
     * Called after clicking the Drop button on the Fitness Class tab.
     */
    @FXML
    void onDropClassClick(){
        String fname = fitclass_fname.getText();
        String lname = fitclass_lname.getText();
        if(fname.isEmpty() || lname.isEmpty() || fitclass_dob.getValue() == null || fitclass_classes.getValue() == null){
            messageArea.appendText("Field cannot be empty.\n");
            return;
        }
        Date dob = new Date(fitclass_dob.getValue().toString(), "-");
        if(!dob.isValid()) {
            messageArea.appendText("DOB " + dob + ": invalid calendar date!\n");
            return;
        }
        FitnessClass fitClass = schedule.toClass(fitclass_classes.getValue().toString());
        if(fitClass == null){
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
        if(isGuest) { messageArea.appendText(fitClass.dropGuestClass(member)); }
        else{ messageArea.appendText(fitClass.dropClass(member)); }
        fitclass_fname.setText("");
        fitclass_lname.setText("");
        fitclass_dob.setValue(null);
        fitclass_classes.setValue(null);
    }

    /**
     * Load members from file into members database and print list of members
     */
    @FXML
    void loadMemberList(){
        database.loadMembers();
        messageArea.appendText("\n-list of members loaded-\n");
        messageArea.appendText(database.print(false, false));
        messageArea.appendText("-end of list-\n");
    }

    /**
     * Load fitness classes from file into schedule and print list of classes
     */
    @FXML
    void loadClassList(){
        schedule.loadSchedule();
        messageArea.appendText("\n-Fitness classes loaded-\n");
        messageArea.appendText(schedule.printClasses());
        messageArea.appendText("-end of class list.\n");
        updateClassInput();
    }

    /**
     * Print members in member database
     */
    @FXML
    void printMembers(){
        messageArea.appendText(database.printDefault());
    }

    /**
     * Print members in member database with fee information
     * of first bill based on membership type
     */
    @FXML
    void printMembersFirstFee(){
        messageArea.appendText(database.printWithFees());
    }

    /**
     * Print members in member database with fee information
     * of second bill based on membership type
     */
    @FXML
    void printMembersSecondFee(){
        messageArea.appendText(database.printWithSecondFees());
    }

    /**
     * Print members in member database sorted by gym location
     */
    @FXML
    void printMembersByLocation(){
        messageArea.appendText(database.printByLocation());
    }

    /**
     * Print members in member database sorted by last then first name
     */
    @FXML
    void printMembersByName(){
        messageArea.appendText(database.printByName());
    }

    /**
     * Print members in member database sorted by expiration date
     */
    @FXML
    void printMembersByExpiration(){
        messageArea.appendText(database.printByExpirationDate());
    }

    /**
     * Print schedule of fitness classes
     */
    @FXML
    void printClasses(){
        messageArea.appendText(schedule.printSchedule());
    }

    /**
     * Called when program starts.
     * Initializes database, schedule, and ComboBox lists.
     */
    @FXML
    public void initialize() {
        database = new MemberDatabase();
        schedule = new ClassSchedule();

        locationList = new String[]{BRIDGEWATER.name(), EDISON.name(),
                FRANKLIN.name(), PISCATAWAY.name(), SOMERVILLE.name()};
        membership_loc.setItems(FXCollections.observableArrayList(locationList));
        updateClassInput();
    }

    /**
     * Set ComboBox options to list of all fitness classes in schedule
     */
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

    /**
     * Return the class schedule
     * @return schedule field
     */
    public static ClassSchedule getSchedule() {
        return schedule;
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
}