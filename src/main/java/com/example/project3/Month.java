package com.example.project3;

/**
 * The Month enum consists of all the months and their
 * corresponding integer value
 * @author Jackson Lee, Serena Zeng
 */
public enum Month {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    private final int value;

    /**
     * Create a Month object that corresponds to the given integer value
     * @param value integer value corresponding to the Month
     */
    Month(int value){
        this.value = value;
    }

    /**
     * Gets the month as an integer from 1 to 12
     * @return integer value of the month
     */
    public int getValue(){ return value; }
}
