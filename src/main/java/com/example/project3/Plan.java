package com.example.project3;

/**
 * The plans class includes the various subscription plans to the gym
 * @author Jackson Lee, Serena Zeng
 */
public enum Plan {
    STANDARD, FAMILY, PREMIUM;

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
