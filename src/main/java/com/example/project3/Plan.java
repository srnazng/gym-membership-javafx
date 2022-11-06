package com.example.project3;

/**
 * The plans class includes the various subscription plans to the gym
 * @author Jackson Lee, Serena Zeng
 */
public enum Plan {
    STANDARD, FAMILY, PREMIUM;

    /**
     * Convert name of plan as a String to Plan object
     * @param plan_name     Name of plan as a String object
     * @return              Plan object corresponding to plan_name
     */
    public static Plan toPlan(String plan_name){
        if(plan_name.equalsIgnoreCase(FAMILY.name())){
            return FAMILY;
        }
        if(plan_name.equalsIgnoreCase(PREMIUM.name())){
            return PREMIUM;
        }
        return STANDARD;
    }
}
