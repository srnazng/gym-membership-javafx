package com.example.project3;

import java.util.Calendar;

/**
 * The Member class represents a member of the gym and is comparable based on name.
 * Each Member object contains member information including first name, last name, date of
 * birth, membership expiration date, and location. A member is uniquely identified by
 * first name, last name, and date of birth. This class also offers other methods of comparison
 * including by expiration date and by county/zipcode.
 * @author Jackson Lee, Serena Zeng
 */
public class Member implements Comparable<Member>{
    private String fname;
    private String lname;
    private Date dob;
    private Date expire;
    private Location location;

    /**
     * Get the location of Member's gym
     * @return Location object
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Creates a member object with all fields completed
     * @param fname     first name
     * @param lname     last name
     * @param dob       date of birth
     * @param location  location (county, city, zipcode)
     */
    public Member(String fname, String lname, Date dob, Location location){
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.expire = calculateExpirationDate();
        this.location = location;
    }

    /**
     * Creates a member object with all fields completed
     * @param fname     first name
     * @param lname     last name
     * @param dob       date of birth
     * @param expire    date of membership expiration
     * @param location  location (county, city, zipcode)
     */
    public Member(String fname, String lname, Date dob, Date expire, Location location){
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.expire = expire;
        this.location = location;
    }

    /**
     * Creates a member object with the minimally required fields
     * @param fname     first name
     * @param lname     last name
     * @param dob       date of birth
     */
    public Member(String fname, String lname, Date dob){
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.expire = null;
        this.location = null;
    }

    /**
     * Calculate the expiration date for Standard and Family Plan
     * members based on taday's date.
     * @return  Date object that is three months from today
     */
    protected Date calculateExpirationDate(){
        Calendar threeMonthsLater = Calendar.getInstance();
        threeMonthsLater.add(Calendar.MONTH, Constants.STANDARD_FAMILY_EXPIRATION);
        return new Date(threeMonthsLater);
    }

    /**
     * Get the first name of the member
     * @return first name as a string
     */
    public String getFname(){
        return fname;
    }

    /**
     * Get the last name of the member
     * @return last name as a string
     */
    public String getLname(){
        return lname;
    }
    /**
     * Get the expiration date of the Member
     * @return expiration date as a Date object
     */
    public Date getExpire(){
        return expire;
    }

    /**
     * Get the fee of the Member
     * @return fee of member as a double
     */
    public double membershipFee() {
        return Constants.STANDARD_FEE + Constants.ONE_TIME_FEE;
    }

    /**
     * Represent current member as string
     * @return String representing member
     */
    @Override
    public String toString() {
        String memberString = fname + " " + lname + ", DOB: " + dob.toString();
        if(expire.isPast()){
            memberString = memberString + ", Membership expired ";
        }
        else{
            memberString = memberString + ", Membership expires ";
        }
        memberString = memberString + expire.toString() + ", Location: " + location;
        if(this instanceof Family){
            if(this instanceof Premium){
                memberString = memberString + ", (Premium) Guest-pass remaining: ";
            }
            else{
                memberString = memberString + ", (Family) Guest-pass remaining: ";
            }
        }
        return memberString;
    }

    /**
     * Checks if a given object is equal to the Member
     * Members are considered equal if the two first names, last names and DOBs are equal
     * @param obj   Member object being compared
     * @return true if obj is equal to member, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        Member member = (Member) obj;
        return fname.equalsIgnoreCase(member.fname)
                && lname.equalsIgnoreCase(member.lname)
                && dob.getYear() == member.dob.getYear()
                && dob.getMonth() == member.dob.getMonth()
                && dob.getDay() == member.dob.getDay();
    }

    /**
     * Compares current member with another member based on first and last name
     * @param member Other Member object being compared to
     * @return negative if this member comes before, 0 if equal, positive otherwise
     */
    @Override
    public int compareTo(Member member) {
        int last_compare = lname.compareToIgnoreCase(member.lname);
        if(last_compare != 0){
            return last_compare;
        }
        return fname.compareToIgnoreCase(member.fname);
    }

    /**
     * Compares current member with another member based on location (sorted by county then zip code)
     * @param member    Other Member object being compared to
     * @return negative if this member comes before, 0 if same location, positive otherwise
     */
    public int compareCounty(Member member){
        // compare county
        String county1 = location.getCounty();
        String county2 = member.location.getCounty();
        int countComp = county1.compareToIgnoreCase(county2);
        if (countComp != 0) {
            return countComp;
        }
        // compare zip code
        String zip1 = location.getZipCode();
        String zip2 = member.location.getZipCode();
        return zip1.compareToIgnoreCase(zip2);
    }

    /**
     * Compare current member with another member based on the expiration date of their membership
     * @param member    Other Member object being compared to
     * @return negative if this member comes before, 0 if same expiration date, positive otherwise
     */
    public int compareExpiration(Member member){
        return expire.compareTo(member.expire);
    }

    /**
     * Testbed main to test compareTo method
     * @param args  default parameters
     */
    public static void main(String[] args){
        Member member1;
        Member member2;
        String expectedOutput;
        //Test Number 1
        System.out.println("Member with alphabetically prior last name is placed in front.");
        member1 = new Member("John", "Doe", new Date("9/2/2022"));
        member2 = new Member("Mary", "Lindsey", new Date("12/1/1989"));
        expectedOutput = "negative";
        testResult(member1, member2, expectedOutput);

        //Test Number 2
        System.out.println("Member with alphabetically later last name is placed behind.");
        member1 = new Member("Duke", "Ellington", new Date("9/2/2022"));
        member2 = new Member("Roy", "Brooks", new Date("12/1/1989"));
        expectedOutput = "positive";
        testResult(member1, member2, expectedOutput);

        //Test Number 3
        System.out.println("Member with same last name and alphabetically prior first name is placed in front.");
        member1 = new Member("Jane", "Doe", new Date("9/2/2022"));
        member2 = new Member("John", "Doe", new Date("12/1/1989"));
        expectedOutput = "negative";
        testResult(member1, member2, expectedOutput);

        //Test Number 4
        System.out.println("Member with same last name and alphabetically later first name is placed behind");
        member1 = new Member("John", "Doe", new Date("9/2/2022"));
        member2 = new Member("Jane", "Doe", new Date("12/1/1989"));
        expectedOutput = "positive";
        testResult(member1, member2, expectedOutput);

        //Test Number 5
        System.out.println("Members with same first and last names can go in either order.");
        member1 = new Member("John", "Doe", new Date("9/2/2022"));
        member2 = new Member("John", "Doe", new Date("12/1/1989"));
        expectedOutput = "zero";
        testResult(member1, member2, expectedOutput);

        //Test Number 6
        System.out.println("Cases do not matter in ordering");
        member1 = new Member("John", "Doe", new Date("9/2/2022"));
        member2 = new Member("john", "doe", new Date("12/1/1989"));
        expectedOutput = "zero";
        testResult(member1, member2, expectedOutput);

        //Test Number 7
        System.out.println("Member1 with last name that is prefix of Member2's last name comes is placed behind");
        member1 = new Member("John", "Doe", new Date("9/2/2022"));
        member2 = new Member("Jane", "Doee", new Date("12/1/1989"));
        expectedOutput = "negative";
        testResult(member1, member2, expectedOutput);
    }

    /**
     * Test compareTo on two given Member objects, and compare with an expected result
     * @param member1           First member to be compared
     * @param member2           Second member to be compared
     * @param expectedOutput    Expected return value of compareTo
     */
    private static void testResult(Member member1, Member member2, String expectedOutput){
        int receivedOutput = member1.compareTo(member2);
        boolean passed = (receivedOutput < 0 && expectedOutput.equals("negative"))
                        || (receivedOutput == 0 && expectedOutput.equals("zero"))
                        || ((receivedOutput > 0 && expectedOutput.equals("positive")));
        if (passed){
            System.out.println(member1.fname + " " + member1.lname + " compared to "
                    + member2.fname + " " + member2.lname + ": passed");
        }
        else{
            System.out.println(member1.fname + " " + member1.lname + " compared to "
                    + member2.fname + " " + member2.lname + ": failed. Please do better");
        }
    }
}
