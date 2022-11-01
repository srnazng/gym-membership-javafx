package com.example.project3;

/**
 * The Time enum represents the two times at which
 * fitness classes are offered.
 * @author Jackson Lee, Serena Zeng
 */
public enum Time {
    MORNING("9:30"),
    AFTERNOON("14:00"),
    EVENING("18:30");

    private final String time;

    /**
     * Create a Time object given the time of day
     * @param time  Time of day as HH:MM
     */
    Time(String time){
        this.time = time;
    }

    /**
     * Formats the time as HH:MM
     * @return  the time value as a String
     */
    public String getTime() { return time; }

    /**
     * Get Time object based on String value
     * @param time  Time represented by String in hh:mm format
     * @return  corresponding Time object
     */
    public static Time toTime(String time){
        for(Time t : values()){
            if(t.name().equalsIgnoreCase(time)){
                return t;
            }
        }
        return null;
    }
}
