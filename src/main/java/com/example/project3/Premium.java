package com.example.project3;

import java.util.Calendar;

/**
 * The premium class represents a member with a premium membership
 * Each premium object contains fields including the first name,
 * last name, date of birth, and location of the member. Each premium
 * object is instantiated with 3 guest passes.
 * @author Jackson Lee, Serena Zeng
 */
public class Premium extends Family {
    /**
     * Create a new instance of a Premium plan member
     * @param fname     first name
     * @param lname     last name
     * @param dob       date of birth
     * @param location  location of gym
     */
    public Premium(String fname, String lname, Date dob, Location location) {
        super(fname, lname, dob, location);
        this.remainingGuestPasses = 3;
    }

    /**
     * Returns the next membership fee due for a premium member
     * @return  dollar amount of fee
     */
    @Override
    public double membershipFee(){
        return Constants.PREMIUM_FEE;
    }

    /**
     * Determine the expiration date based on today's date
     * @return  Date object of expiration date
     */
    @Override
    protected Date calculateExpirationDate(){
        Calendar threeMonthsLater = Calendar.getInstance();
        threeMonthsLater.add(Calendar.MONTH, Constants.PREMIUM_EXPIRATION);
        return new Date(threeMonthsLater);
    }
}
