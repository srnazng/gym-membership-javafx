package com.example.project3;

import java.io.File;
import java.util.Scanner;

import static models.Constants.NOT_FOUND;
import static models.Constants.decimalFormat;

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
     * Otherwise, a list of members is printed without sorting.
     */
    public void printDefault() {
        if(size < 1){
            System.out.println("Member database is empty!");
            return;
        }
        System.out.println("\n-list of members-");
        print(false);
        System.out.println("-end of list-\n");
    }

    /**
     * If there are no members, a message indicating database is empty is printed.
     * Otherwise, a list of members and their fees is printed without sorting.
     */
    public void printWithFees() {
        if(size < 1){
            System.out.println("Member database is empty!");
            return;
        }

        System.out.println("\n-list of members with membership fees-");
        print(true);
        System.out.println("-end of list-\n");
    }

    /**
     * Print the members in the database (without headers)
     * @param includeFees   whether or not to include the fees
     */
    private void print(boolean includeFees){
        for (int i = 0; i < size; i++){
            String memberInfo = mlist[i].toString();
            if(includeFees){
                memberInfo = memberInfo + ", Membership fee: $" + decimalFormat.format(mlist[i].membershipFee());
            }
            System.out.println(memberInfo);
        }
    }

    /**
     * If there are no members, a message indicating database is empty is printed.
     * Otherwise, print the list of members sorted in place by county, then zip code.
     */
    public void printByLocation() {
        if(size < 1){
            System.out.println("Member database is empty!");
            return;
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

        System.out.println("\n-list of members sorted by county and zipcode-");
        for (int i = 0; i < size; i++){
            System.out.println(mlist[i]);
        }
        System.out.println("-end of list-\n");
    }

    /**
     * If there are no members, a message indicating database is empty is printed.
     * Otherwise, print the list of members sorted in place by expiration date of membership.
     */
    public void printByExpirationDate() {
        if(size < 1){
            System.out.println("Member database is empty!");
            return;
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
        System.out.println("\n-list of members sorted by membership expiration date-");
        for (int i = 0; i < size; i++){
            System.out.println(mlist[i]);
        }
        System.out.println("-end of list-\n");
    }

    /**
     * If there are no members, a message indicating database is empty is printed.
     * Otherwise, print the list of members sorted in place by last name, then first name.
     */
    public void printByName() {
        if(size < 1){
            System.out.println("Member database is empty!");
            return;
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
        System.out.println("\n-list of members sorted by last name, and first name-");
        for (int i = 0; i < size; i++){
            System.out.println(mlist[i]);
        }
        System.out.println("-end of list-\n");
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

            while (sc.hasNextLine()){
                String s = sc.nextLine();
                if(!s.isBlank()) {
                    size++;
                }
            }
            sc.close();
            sc = new Scanner(file);
            mlist = new Member[size + GROWTH_FACTOR];

            for (int i = 0; i < size; i++){
                String memberString = sc.nextLine();
                String[] memberParts = memberString.split("\\s+");
                if(memberParts.length == NUM_ARGS){
                    Member member = new Member(memberParts[FNAME_INDEX], memberParts[LNAME_INDEX],
                            new Date(memberParts[DOB_INDEX]), new Date(memberParts[EXP_INDEX]),
                            Location.toLocation(memberParts[CITY_INDEX]));
                    mlist[i] = member;
                }
            }
            sc.close();

            System.out.println("\n-list of members loaded-");
            print(false);
            System.out.println("-end of list-\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
