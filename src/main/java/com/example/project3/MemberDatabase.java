package com.example.project3;

import java.io.File;
import java.util.Scanner;

import static com.example.project3.Constants.*;

/**
 * The MemberDatabase class contains a list of all the members of the gym,
 * stored in an array of Member objects. This class also includes methods
 * to print the members based on different sorted fields.
 * @author Jackson Lee, Serena Zeng
 */
public class MemberDatabase {
    private Member[] mlist;
    private int size;
    private static final int GROWTH_FACTOR = 4;
    public static final int FNAME_INDEX = 0;
    public static final int LNAME_INDEX = 1;
    public static final int DOB_INDEX = 2;
    public static final int EXP_INDEX = 3;
    public static final int CITY_INDEX = 4;
    public static final int NUM_ARGS = 5;

    /**
     * Create new MemberDatabase object with size 0
     */
    public MemberDatabase(){
        size = 0;
        mlist = new Member[GROWTH_FACTOR];
    }

    /**
     * Finds index of a member in the member database
     * @param member Target Member object
     * @return Index of the member in member list, or -1 if member doesn't exist.
     */
    private int find(Member member) {
        for (int i = 0; i < size; i++){
            if (member.equals(mlist[i])) return i;
        }
        return NOT_FOUND;
    }

    /**
     * Grow the member list mlist by GROWTH_FACTOR
     */
    private void grow() {
        Member [] mcopy = new Member [mlist.length + GROWTH_FACTOR];
        for (int i = 0; i < mlist.length; i++) {
            mcopy[i] = mlist[i];
        }
        mlist = mcopy;
    }

    /**
     * Add a member to the database, and check if it's already in the database
     * @param member to be added
     * @return false if member already exists in database
     */
    public boolean add(Member member) {
        if (contains(member)) return false;
        mlist[size] = member;
        size++;
        if (size == mlist.length) grow();
        return true;
    }

    /**
     * Remove a member from the gym database
     * @param member Member to be removed
     * @return false if member does not exist, otherwise true
     */
    public boolean remove(Member member) {
        if (!contains(member)) return false;
        int mindex = find(member);

        for (int i = mindex; i < size; i++){
            mlist[i] = mlist[i + 1];
        }
        size--;
        return true;
    }

    /**
     * If there are no members, a message indicating database is empty is printed.
     * Otherwise, a list of members, without their fees, is returned without sorting.
     * @return string representing list of members
     */
    public String printDefault() {
        if(size < 1){
            return "Member database is empty!\n";
        }
        return "\n-list of members-\n"
                + print(false, false) + "-end of list-\n\n";
    }

    /**
     * If there are no members, a message indicating database is empty is printed.
     * Otherwise, a list of members and their first bill fees is returned without sorting.
     * @return string representing list of members with fee info
     */
    public String printWithFees() {
        if(size < 1){
            return "Member database is empty!\n";
        }
        return "\n-list of members with membership fees-\n"
                + print(true, true) + "-end of list-\n\n";
    }

    /**
     * If there are no members, a message indicating database is empty is printed.
     * Otherwise, a list of members and their second bill fees is returned without sorting.
     * @return string representing list of members with fee info
     */
    public String printWithSecondFees() {
        if(size < 1){
            return "Member database is empty!\n";
        }
        return "\n-list of members with membership fees-\n"
                + print(true, false) + "-end of list-\n\n";
    }

    /**
     * Print the members in the database (without headers)
     * @param includeFees   whether to include the fees
     * @param firstFee      whether fee is first time payment
     * @return string representing list of members
     */
    public String print(boolean includeFees, boolean firstFee){
        String returnString = "";
        for (int i = 0; i < size; i++){
            String memberInfo = mlist[i].toString();
            if(includeFees){
                if(firstFee && !(mlist[i] instanceof Premium)){
                    memberInfo = memberInfo + ", Membership fee: $" + decimalFormat.format(mlist[i].membershipFee() + ONE_TIME_FEE);
                }
                else{
                    memberInfo = memberInfo + ", Membership fee: $" + decimalFormat.format(mlist[i].membershipFee());
                }
            }
            returnString += memberInfo + "\n";
        }
        return returnString;
    }

    /**
     * If there are no members, a message indicating database is empty is printed.
     * Otherwise, print the list of members sorted in place by county, then zip code.
     * @return string representing list of members sorted by location
     */
    public String printByLocation() {
        if(size < 1){
            return "Member database is empty!\n";
        }
        for (int i = 0; i < size - 1; i++){
            for (int j = 0; j < size - 1 - i; j++){
                Member mem1 = mlist[j];
                Member mem2 = mlist[j + 1];
                if (mem1.compareCounty(mem2) > 0){
                    mlist[j] = mem2;
                    mlist[j + 1] = mem1;
                }
            }
        }

        String returnString = "\n-list of members sorted by county and zipcode-\n";
        for (int i = 0; i < size; i++){
            returnString += mlist[i] + "\n";
        }
        returnString += "-end of list-\n\n";
        return returnString;
    }

    /**
     * If there are no members, a message indicating database is empty is printed.
     * Otherwise, print the list of members sorted in place by expiration date
     * @return string representing list of members sorted by epiration date
     */
    public String printByExpirationDate() {
        if(size < 1){
            return "Member database is empty!\n";
        }
        for (int i = 0; i < size - 1; i++){
            for (int j = 0; j < size - 1 - i; j++){
                Member mem1 = mlist[j];
                Member mem2 = mlist[j + 1];
                if (mem1.compareExpiration(mem2) > 0){
                    mlist[j] = mem2;
                    mlist[j + 1] = mem1;
                }
            }
        }

        String returnString = "\n-list of members sorted by membership expiration date-\n";
        for (int i = 0; i < size; i++){
            returnString += mlist[i] + "\n";
        }
        returnString += "-end of list-\n";
        return returnString;
    }

    /**
     * If there are no members, a message indicating database is empty is printed.
     * Otherwise, print the list of members sorted in place last name, then by first name
     * @return string representing list of members sorted by last name, then by first name
     */
    public String printByName() {
        if(size < 1){
            return "Member database is empty!\n";
        }
        for (int i = 0; i < size - 1; i++){
            for (int j = 0; j < size - 1 - i; j++){
                Member mem1 = mlist[j];
                Member mem2 = mlist[j + 1];
                if (mem1.compareTo(mem2) > 0){
                    mlist[j] = mem2;
                    mlist[j + 1] = mem1;
                }
            }
        }
        String returnString = "\n-list of members sorted by last name, and first name-\n";
        for (int i = 0; i < size; i++){
            returnString += mlist[i] + "\n";
        }
        returnString += "-end of list-\n";
        return returnString;
    }

    /**
     * Check whether the member database contains the given member
     * @param member Member object being checked for
     * @return true if member in database, otherwise false
     */
    public boolean contains(Member member){
        return find(member) != NOT_FOUND;
    }

    /**
     * Gets the Member object from the member database that is equivalent
     * to the given Member object (typically does not contain all fields)
     * @param member Target Member object
     * @return Member object from member database with no null fields
     */
    public Member getMember(Member member){
        int index = find(member);
        if(index == NOT_FOUND){
            return null;
        }
        return mlist[index];
    }

    /**
     * Reads members line by line from the text file
     * memberList.txt and loads the members into
     * the member database.
     */
    public void loadMembers() {
        try{
            File file = new File("src/input/memberList.txt");
            Scanner sc = new Scanner(file);

            int num_members = 0;
            while (sc.hasNextLine()){
                String s = sc.nextLine();
                if(!s.isBlank()) {
                    num_members++;
                }
            }
            sc.close();
            sc = new Scanner(file);

            for (int i = 0; i < num_members; i++){
                String memberString = sc.nextLine();
                String[] memberParts = memberString.split("\\s+");
                if(memberParts.length == NUM_ARGS){
                    Member member = new Member(memberParts[FNAME_INDEX], memberParts[LNAME_INDEX],
                            new Date(memberParts[DOB_INDEX]), new Date(memberParts[EXP_INDEX]),
                            Location.toLocation(memberParts[CITY_INDEX]));
                    add(member);
                }
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
