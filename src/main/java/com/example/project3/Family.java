package com.example.project3;

/**
 * The family class represents a member who has a family plan.
 * Each family object contains member information including first name, last name, date of
 * birth, membership expiration date, location, and number of remaining guest passes.
 * A member is uniquely identified by first name, last name, and date of birth.
 * The family member is instantiated with 1 guest pass.
 * @author Jackson Lee, Serena Zeng
 */
public class Family extends Member {
    protected int remainingGuestPasses;

    public Family(String fname, String lname, Date dob, Location location) {
        super(fname, lname, dob, location);
        this.remainingGuestPasses = 1;
    }

    /**
     * Get the amount to be paid at the next billing cycle for Family Plan members
     * @return dollar amount
     */
    @Override
    public double membershipFee(){
        return Constants.FAMILY_FEE;
    }

    /**
     * Decrement the number of remaining guest passes
     * @return true if a guest pass was available, false otherwise
     */
    public boolean useGuestPass(){
        if(remainingGuestPasses < 1){
            return false;
        }
        remainingGuestPasses--;
        return true;
    }

    /**
     * Increment the number of remaining guest passes
     * (typically after guest is done with class)
     */
    public void incrementGuestPass(){
        remainingGuestPasses++;
    }

    /**
     * Check if Member has any guest passes remaining
     * @return  true if guest passes available, false otherwise
     */
    public boolean hasGuestPass(){
        return remainingGuestPasses > 0;
    }

    /**
     * Format Family Plan member and information as a String
     * @return Family Plan member as String
     */
    @Override
    public String toString(){
        return super.toString() + remainingGuestPasses;
    }
}
