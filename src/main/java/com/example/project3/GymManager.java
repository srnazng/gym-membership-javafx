package com.example.project3;

import java.util.Scanner;

/**
 * The GymManager class is the user interface class that processes the
 * command line inputs and prints the corresponding outputs.
 * @author Jackson Lee, Serena Zeng
 */
public class GymManager {
    private static final String DOB_ERROR = "DOB ";
    private static final int A_COMMAND_LENGTH = 5;
    private static final int R_COMMAND_LENGTH = 4;
    private static final int C_COMMAND_LENGTH = 7;
    private static final int D_COMMAND_LENGTH = 7;
    private static final int ARG_1 = 1;
    private static final int ARG_2 = 2;
    private static final int ARG_3 = 3;
    private static final int ARG_4 = 4;
    private static final int ARG_5 = 5;
    public static final int ARG_6 = 6;

    private static MemberDatabase database;
    private static ClassSchedule schedule;

    /**
     * Return the class schedule
     * @return schedule field
     */
    public static ClassSchedule getSchedule() {
        return schedule;
    }

    /**
     * Run program
     * Reads lines from terminal and performs commands based off inputs
     * @return true
     */
    protected boolean run() {
        System.out.println("Gym Manager running...");
        Scanner sc = new Scanner(System.in);
        String s;

        database = new MemberDatabase();
        schedule = new ClassSchedule();
        
        while(sc.hasNextLine()) {
            s = sc.nextLine();
            if(s.equals("Q")){
                System.out.println("Gym Manager terminated.");
                break;
            }
            runCommand(s.split(" ")[0], s);
        }
        return true;
    }

    /**
     * Processes terminal commands
     * @param command   command indicating type of task
     * @param line      entire instruction
     */
    private void runCommand(String command, String line) {
        if(command.trim().length() == 0) { return; }
        if(command.equals("LS")) { schedule.loadSchedule(); }
        else if(command.equals("LM")) { database.loadMembers(); }
        else if(command.equals("A")) { handleAddMember(line, Plans.STANDARD); }
        else if(command.equals("AF")) { handleAddMember(line, Plans.FAMILY); }
        else if(command.equals("AP")) { handleAddMember(line, Plans.PREMIUM); }
        else if(command.equals("PF")) { database.printWithFees(); }
        else if(command.equals("C")) { handleCheckIn(line, false); }
        else if(command.equals("CG")) { handleCheckIn(line, true); }
        else if(command.equals("D")) { handleDropClass(line, false); }
        else if(command.equals("DG")) { handleDropClass(line, true); }
        else if(command.equals("R")) { handleCancelMembership(line); }
        else if(command.equals("P")) { database.printDefault(); }
        else if(command.equals("PC")) {  database.printByLocation(); }
        else if(command.equals("PN")) { database.printByName(); }
        else if(command.equals("PD")) { database.printByExpirationDate(); }
        else if(command.equals("S")) { schedule.printSchedule(); }
        else { System.out.println(command + " is an invalid command!"); }
    }

    /**
     * Handles inputs with command type A, AF, AP
     * Parses command for member information
     * Checks if member is valid and if so, adds member to member database
     * @param command           Entire line of instruction containing member information
     * @param membershipType    Standard, Family, or Premium plan of member
     * @return true if member successfully added, false otherwise
     */
    private boolean handleAddMember(String command, Plans membershipType){
        String[] parts = command.split(" ");
        if(parts.length < A_COMMAND_LENGTH) return false;

        Date dob = new Date(parts[ARG_3]);
        String error = checkBirthdayErrors(dob);
        if(error != null){
            System.out.println(error);
            return false;
        }
        Location location = Location.toLocation(parts[ARG_4]);
        if(location == null) {
            System.out.println(parts[ARG_4] + ": invalid location!");
            return false;
        }
        Member member;
        if(membershipType == Plans.PREMIUM){ member = new Premium(parts[ARG_1], parts[ARG_2], dob, location); }
        else if(membershipType == Plans.FAMILY){ member = new Family(parts[ARG_1], parts[ARG_2], dob, location); }
        else{ member = new Member(parts[ARG_1], parts[ARG_2], dob, location); }
        if(database.contains(member)) {
            System.out.println(parts[ARG_1] + " " + parts[ARG_2] + " is already in the database.");
            return false;
        }
        if(database.add(member)){
            System.out.println(parts[ARG_1] + " " + parts[ARG_2] + " added.");
        }
        return true;
    }

    /**
     * Handles inputs with command C and CG
     * Parses command for fitness class and member information
     * Checks in the member if
     * 1. The member is in the database
     * 2. Their membership is not expired, their
     * 3. Their date of birth is valid
     * 4. The specified class exists
     * 5. The class has no time conflict with the member's other fitness clases
     * 6. The member is not already checked in
     * Displays appropriate error messages if unable to check in member to class
     * @param command Entire line of instruction containing member and class information
     * @param isGuest true if checking in guest, false otherwise
     */
    private void handleCheckIn(String command, boolean isGuest) {
        String[] parts = command.split(" ");
        if (parts.length != C_COMMAND_LENGTH) return;
        Date dob = new Date(parts[ARG_6]);
        String fname = parts[ARG_4];
        String lname = parts[ARG_5];
        String className = parts[ARG_1];
        String instructor = parts[ARG_2];
        String location = parts[ARG_3];
        if(!dob.isValid()) {
            System.out.println(DOB_ERROR + dob + ": invalid calendar date!");
            return;
        }
        Member member = database.getMember(new Member(fname, lname, dob));
        if (member == null) {
            System.out.println(fname + " " + lname + " " + dob + " is not in the database.");
            return;
        }
        if (Location.toLocation(location) == null){
            System.out.println(location + " - invalid location.");
            return;
        }
        if (!schedule.hasClass(new FitnessClass(className, instructor, location))) {
            System.out.println(schedule.handleClassNotExist(new FitnessClass(className, instructor, location)));
            return;
        }
        if (member.getExpire().isPast()) {
            System.out.println(fname + " " + lname + " " + dob + " membership expired.");
            return;
        }
        FitnessClass fitClass = schedule.getClass(new FitnessClass(className, instructor, location));
        String message;
        if (isGuest) message = fitClass.addGuest(member);
        else message = fitClass.add(member);
        System.out.print(message);
        if (fitClass.getLastAddSuccessful()){
            System.out.print(fitClass.getClassMemberList());
            System.out.println(fitClass.getClassGuestList());
        }
    }


    /**
     * Handles inputs with command D and DG
     * Parses command for fitness class and member information
     * Drops the member from the class if
     * 1. The member is initially checked into the class
     * 2. Their date of birth is valid
     * 3. The specified class exists
     * Displays appropriate error messages if unable to drop member from class
     * @param command Entire line of instruction containing member and class information
     * @param isGuest true of dropping class for guest, false otherwise
     */
    private void handleDropClass(String command, boolean isGuest){
        String[] parts = command.split(" ");
        if(parts.length < D_COMMAND_LENGTH) return;

        Date dob = new Date(parts[ARG_6]);
        String fname = parts[ARG_4];
        String lname = parts[ARG_5];
        String className = parts[ARG_1];
        String instructor = parts[ARG_2];
        String location = parts[ARG_3];

        // check if DOB is valid
        if(!dob.isValid()) {
            System.out.println(DOB_ERROR + parts[ARG_6] + ": invalid calendar date!");
            return;
        }
        if (Location.toLocation(location) == null){
            System.out.println(location + " - invalid location.");
            return;
        }
        if (!schedule.hasClass(new FitnessClass(className, instructor, location))) {
            System.out.println(schedule.handleClassNotExist(new FitnessClass(className, instructor, location)));
            return;
        }
        // check if user is registered
        Member member = new Member(fname, lname, dob);
        if(!database.contains(member)){
            System.out.println(fname + " " + lname + " " + parts[ARG_6] + " is not in the database.");
            return;
        }
        member = database.getMember(new Member(fname, lname, dob));
        FitnessClass fitClass = schedule.getClass(new FitnessClass(className, instructor, location));
        if(isGuest) { System.out.println(fitClass.dropGuestClass(member)); }
        else{ System.out.println(fitClass.dropClass(member)); }
    }

    /**
     * Handles inputs with command R
     * Parses command for member information
     * Checks if member is in the member database and if so, removes from the member from the database
     * @param command   Entire line of instruction containing member information
     * @return true if membership cancel success, false otherwise
     */
    private boolean handleCancelMembership(String command){
        String[] parts = command.split(" ");
        if(parts.length < R_COMMAND_LENGTH) return false;
        if(database.remove(new Member(parts[ARG_1], parts[ARG_2], new Date(parts[ARG_3])))){
            System.out.println(parts[ARG_1] + " " + parts[ARG_2] + " removed.");
        }
        else{
            System.out.println(parts[ARG_1] + " " + parts[ARG_2] + " is not in the database.");
        }
        return true;
    }

    /**
     * Return error message if invalid birthday
     * @param dob   Date object of birthday
     * @return Error message if dob invalid, null otherwise
     */
    private String checkBirthdayErrors(Date dob) {
        if (!dob.isValid()) {
            return DOB_ERROR + dob + ": invalid calendar date!";
        }
        if (!dob.isPast()) {
            return DOB_ERROR + dob + ": cannot be today or a future date!";
        }
        if (!dob.isEighteen()) {
            return DOB_ERROR + dob + ": must be 18 or older to join!";
        }
        return null;
    }


    /**
     * Create test schedule
     * @return ClassSchedule object for testing
     */
    public static ClassSchedule createTestSchedule() {
        schedule = new ClassSchedule();
        return schedule;
    }
}
