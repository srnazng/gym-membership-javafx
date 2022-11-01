package com.example.project3;

import java.text.DecimalFormat;

/**
 * The Constants class contains a list of public
 * constants that can be used across other classes
 * @author Jackson Lee, Serena Zeng
 */
public class Constants {
    public static final int LESS = -1;
    public static final int EQUAL = 0;
    public static final int GREATER = 1;
    public static final int NOT_FOUND = -1;
    public static final int DAYS_PER_LONG_MONTH = 31;
    public static final int DAYS_PER_SHORT_MONTH = 30;
    public static final int FEB_LEAP_DAYS = 29;
    public static final int MONTHS_PER_YEAR = 12;
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUARTERCENTENNIAL = 400;
    public static final int ADULT_YEAR = 18;
    public static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    public static final double STANDARD_FEE = 39.99 * 3;
    public static final double FAMILY_FEE = 59.99 * 3;
    public static final double PREMIUM_FEE = 59.99 * 11;
    public static final double ONE_TIME_FEE = 29.99;
    public static final int STANDARD_FAMILY_EXPIRATION = 3;
    public static final int PREMIUM_EXPIRATION = 12;
}
