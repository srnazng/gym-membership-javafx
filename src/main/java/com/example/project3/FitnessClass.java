package com.example.project3;

import java.util.ArrayList;

/**
 * The FitnessClass class represents the fitness classes at the gym.
 * Each FitnessClass object includes the name of the class, the name
 * of the instructor, the time of the class, and a list of members
 * who are checked in at the current time. This class also includes methods
 * to add or drop a class, and to check if a member is checked into the class.
 * @author Jackson Lee, Serena Zeng
 */
public class FitnessClass {
    private String name;
    private String instructor;
    private Time time;
    private Location location;
    private ArrayList<Member> checkedInMembers;
    private ArrayList<Member> checkedInGuests;
    private boolean lastAddSuccessful;

    /**
     * Create a new Fitness Class object with no checked in members
     * @param name          Name of class
     * @param instructor    Name of the class instructor
     * @param time          Time of the class (morning or afternoon)
     * @param location      Location of class
     */
    public FitnessClass(String name, String instructor, Time time, Location location){
        this.name = name;
        this.location = location;
        this.instructor = instructor;
        this.time = time;
        checkedInMembers = new ArrayList<>();
        checkedInGuests = new ArrayList<>();
    }

    /**
     * Create a Fitness Class object with limited information
     * @param name          Name of class
     * @param instructor    Name of the class instructor
     * @param city      Location of class
     */
    public FitnessClass(String name, String instructor, String city){
        this.name = name;
        this.location = Location.toLocation(city);
        this.instructor = instructor;
    }

    /**
     * Get whether or not the last add attempt was successful
     * @return true if the last add attempt was successful, false otherwise
     */
    public boolean getLastAddSuccessful(){
        return lastAddSuccessful;
    }

    /**
     * Get name of fitness class
     * @return class name
     */
    public String getName(){ return name; }

    /**
     * Get time of fitness class
     * @return MORNING or AFTERNOON
     */
    public Time getTime(){ return time; }

    /**
     * Get location of fitness class
     * @return Location object
     */
    public Location getLocation(){ return location; }

    /**
     * Get fitness class instructor
     * @return Name of fitness class instructor
     */
    public String getInstructor(){ return instructor; }

    /**
     * Compare two FitnessClass objects
     * @param obj   FitnessClass object to be compared
     * @return  true if same class name, instructor, and location; false otherwise
     */
    @Override
    public boolean equals(Object obj){
        FitnessClass otherClass = (FitnessClass) obj;
        if(!otherClass.getName().equalsIgnoreCase(name) ||
            !otherClass.getInstructor().equalsIgnoreCase(instructor) ||
            !otherClass.getLocation().equals(location)){
            return false;
        }
        return true;
    }


    /**
     * Converts object to a String object containing name of the class,
     * instructor name, time of the class, and a list of participants
     * @return Fitness class formatted as a String
     */
    @Override
    public String toString() {
        String toReturn = name.toUpperCase() + " - " + instructor.toUpperCase() + ", " + time.getTime()
                + ", " + location.name().toUpperCase();
        if(checkedInGuests.size() > 0 || checkedInMembers.size() > 0){
            return toReturn + getClassMemberList() + getClassGuestList();
        }
        return toReturn + "\n";
    }

    /**
     * Converts FitnessClass to String object containing name of the class,
     * instructor name, time of the class
     * @return  Fitness class formatted as a String
     */
    public String toSimpleString(){
        return name.toUpperCase() + " - " + instructor.toUpperCase() + ", " + time.getTime()
                + ", " + location.name().toUpperCase() + "\n";
    }

    /**
     * Gets a list of Members (as Strings) who are checked into the fitness class
     * @return  List of participants of the class, empty string if no participants
     */
    public String getClassMemberList(){
        String toReturn = "";
        if(checkedInMembers.size() > 0){
            toReturn = "\n- Participants -\n";
        }
        else{
            return "";
        }

        for(int i=0; i<checkedInMembers.size(); i++){
            if(checkedInMembers.get(i) != null){
                toReturn = toReturn + "\t" + checkedInMembers.get(i).toString();
            }
            if(i != checkedInMembers.size() - 1){
                toReturn = toReturn + "\n";
            }
            else if(checkedInGuests.size() < 1){
                toReturn = toReturn + "\n";
            }
        }
        return toReturn;
    }

    /**
     * Gets a list of Members (as Strings) who have checked in guests to the fitness class
     * @return  List of members with guests in the class, empty string if no guests
     */
    public String getClassGuestList(){
        String toReturn = "";
        if(checkedInGuests.size() > 0){
            toReturn = "\n- Guests -\n";
        }
        else{
            return "";
        }

        for(int i=0; i<checkedInGuests.size(); i++){
            if(checkedInGuests.get(i) != null){
                toReturn = toReturn + "\t" + checkedInGuests.get(i).toString();
            }
            toReturn = toReturn + "\n";
        }
        return toReturn;
    }

    /**
     * Add a member to the list of members in fitness class
     * @param member to be added
     * @return message to be outputted, depending on whether member was succesfully added
     */
    public String add(Member member) {
        String fname = member.getFname();
        String lname = member.getLname();
        lastAddSuccessful = false;
        if ((contains(member))) {
            return fname + " " + lname + " already checked in.\n";
        }
        if( !(member instanceof Family) && !location.equals(member.getLocation())){
            return fname + " " + lname + " checking in " + location.toString().toUpperCase()
                    + " - standard membership location restriction.\n";
        }
        if (!validTime(member)){
           return "Time conflict - " + name.toUpperCase() + " - " + instructor.toUpperCase()
                    + ", "  +  time.getTime() + ", " + location.toString().toUpperCase() + "\n";
        }
        lastAddSuccessful = true;
        checkedInMembers.add(member);
        return fname + " " + lname + " checked in " + name.toUpperCase() + " - " +
                instructor.toUpperCase() + ", " + time.getTime() + ", " + location.name() + "\n";
    }

    /**
     * Sees if there is a time conflict given a member and a fitness class
     * @param member the member trying to check in
     * @return true if no time conflict, false if there is a time conflict
     */
    private boolean validTime(Member member){
        ClassSchedule schedule = GymManagerController.getSchedule();
        if(schedule == null){
            return true;
        }
        FitnessClass[] classes = schedule.sameTimeClasses(this);
        for(int i=0; i<classes.length; i++){
            if(classes[i].contains(member)){
                return false;
            }
        }
        return true;
    }

    /**
     * Check in a guest to the fitness class and
     * increment the number of guests checked into the class
     * @param member that the guest belongs to
     * @return message to be outputted, depending on whether guest was successfully added
     */
    public String addGuest(Member member) {
        String fname = member.getFname();
        String lname = member.getLname();
        lastAddSuccessful = false;
        if(!(member instanceof Family)){
            return "Standard membership - guest check-in is not allowed.\n";
        }
        if(!((Family) member).hasGuestPass()){
            return fname + " " + lname + " ran out of guest pass.\n";
        }
        if(!location.equals(member.getLocation())){
            return fname + " " + lname + " Guest checking in " + location.toString().toUpperCase()
                    + " - guest location restriction.\n";
        }

        lastAddSuccessful = true;
        ((Family) member).useGuestPass();
        checkedInGuests.add(member);
        return fname + " " + lname + " (guest) checked in " + name.toUpperCase() + " - " +
                instructor.toUpperCase() + ", " + time.getTime() + ", " + location.name() + "\n";
    }

    /**
     * Remove the given member from the list of participants in the class and
     * decrement the number of participants checked into the class
     * @param member Member to remove from participant list
     * @return false if member is not in the class, otherwise true
     */
    public String dropClass(Member member){
        if(!contains(member)){
            return member.getFname() + " " + member.getLname() + " did not check in.\n";
        }
        checkedInMembers.remove(member);
        return member.getFname() + " " + member.getLname() + " done with the class.\n";
    }

    /**
     * Remove the given Member providing the pass to the guest and
     * @param member Member to remove from guest list
     * @return false if guest of member is not in the class, otherwise true
     */
    public String dropGuestClass(Member member){
        if(!containsGuest(member)){
            return member.getFname() + " " + member.getLname() + " Guest did not check in.\n";
        }
        checkedInGuests.remove(member);
        ((Family) member).incrementGuestPass();
        return member.getFname() + " " + member.getLname() + " Guest done with the class.\n";
    }

    /**
     * Check whether a member has already been checked into the fitness class
     * @param member    Member to find in class
     * @return  true if member is checked in to class, false otherwise
     */
    public boolean contains(Member member){
        return checkedInMembers.contains(member);
    }

    /**
     * Check whether a member has a guest already been checked into the fitness class
     * @param member    Member to find in class
     * @return  true if member has guest checked in to class, false otherwise
     */
    public boolean containsGuest(Member member){
        return checkedInGuests.contains(member);
    }
}
