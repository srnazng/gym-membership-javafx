package com.example.project3;

import java.util.Calendar;

/**
 * The Date class represents a calendar date consisting of year, month, and day
 * @author Jackson Lee, Serena Zeng
 */
public class Date implements Comparable<Date> {
    private int year;
    private int month; // 1 to 12
    private int day;

    private static final int INDEX_0 = 0;
    private static final int INDEX_1 = 1;
    private static final int INDEX_2 = 2;
    private static final int DATE_NUM_PARTS = 3;

    /**
     * Get year of Date
     *
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * Get month of Date (1 - 12)
     *
     * @return month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Get day of month of Date (1 - 31)
     *
     * @return day of month
     */
    public int getDay() {
        return day;
    }

    /**
     * Creates a new Date object with today's date
     */
    public Date() {
        Calendar today = Calendar.getInstance();
        this.year = today.get(Calendar.YEAR);
        this.month = today.get(Calendar.MONTH + 1);
        this.day = today.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Creates a new Date object given a date represented by a
     * String of format “mm/dd/yyyy”
     *
     * @param date String containing year, month, and day of new Date
     */
    public Date(String date) {
        String[] dateParts = date.split("/", 0);
        if (dateParts.length < DATE_NUM_PARTS) {
            return;
        }
        this.month = Integer.parseInt(dateParts[INDEX_0]);
        this.day = Integer.parseInt(dateParts[INDEX_1]);
        this.year = Integer.parseInt(dateParts[INDEX_2]);
    }

    /**
     * Creates a new Date object given a date represented by a
     * String with specified delimiter
     *
     * @param date          Date represented as a String
     * @param delimiter     delimiter character
     */
    public Date(String date, String delimiter) {
        String[] dateParts = date.split(delimiter, 0);
        if (dateParts.length < DATE_NUM_PARTS) {
            return;
        }
        if(delimiter.equalsIgnoreCase("-")){
            this.month = Integer.parseInt(dateParts[INDEX_1]);
            this.day = Integer.parseInt(dateParts[INDEX_2]);
            this.year = Integer.parseInt(dateParts[INDEX_0]);
        }
        else{
            this.month = Integer.parseInt(dateParts[INDEX_0]);
            this.day = Integer.parseInt(dateParts[INDEX_1]);
            this.year = Integer.parseInt(dateParts[INDEX_2]);
        }
    }

    /**
     * Create new Date object given a Calendar object
     * @param calDate Calendar containing year, month, and day of new Date
     */
    public Date(Calendar calDate) {
        this.month = calDate.get(Calendar.MONTH) + 1;
        this.day = calDate.get(Calendar.DAY_OF_MONTH);
        this.year = calDate.get(Calendar.YEAR);
    }

    /**
     * Convert Date object to String with format MM/DD/YYYY
     *
     * @return Date as a String with correct formatting
     */
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    /**
     * Compare current date object with another date
     *
     * @param date the object to be compared.
     * @return -1 if this date is before, 0 if same day, 1 if after
     */
    @Override
    public int compareTo(Date date) {
        if (year < date.getYear()) {
            return Constants.LESS;
        }
        if (year > date.getYear()) {
            return Constants.GREATER;
        }

        // same year
        if (month < date.getMonth()) {
            return Constants.LESS;
        }
        if (month > date.getMonth()) {
            return Constants.GREATER;
        }

        // same month
        if (day < date.getDay()) {
            return Constants.LESS;
        }
        if (day > date.getDay()) {
            return Constants.GREATER;
        }

        // same day
        return Constants.EQUAL;
    }

    /**
     * check if a date is a valid calendar date
     * Check if the current date object is valid
     * Checks for leap years etc.
     *
     * @return true if date is valid, false otherwise
     */
    public boolean isValid() {
        // check valid year
        if (year < 0 || month < 1 || month > Constants.MONTHS_PER_YEAR || day < 1) {
            return false;
        }
        if ((month == Month.JANUARY.getValue() || month == Month.MARCH.getValue()
                || month == Month.MAY.getValue() || month == Month.JULY.getValue()
                || month == Month.AUGUST.getValue() || month == Month.OCTOBER.getValue()
                || month == Month.DECEMBER.getValue()) && day > Constants.DAYS_PER_LONG_MONTH) {
            return false;
        }
        if ((month == Month.APRIL.getValue() || month == Month.JUNE.getValue() ||
                month == Month.SEPTEMBER.getValue() || month == Month.NOVEMBER.getValue())
                && day > Constants.DAYS_PER_SHORT_MONTH) {
            return false;
        }
        if (month == Month.FEBRUARY.getValue()) {
            if (day == Constants.FEB_LEAP_DAYS && !isLeapYear()
                    || day > Constants.FEB_LEAP_DAYS) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sees if this date is eighteen years ago or earlier
     *
     * @return true if is eighteen years ago, false otherwise
     */
    public boolean isEighteen() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -Constants.ADULT_YEAR);
        Date minusEighteen = new Date(calendar);
        return this.compareTo(minusEighteen) == Constants.LESS;
    }

    /**
     * Checks if date is in the past
     *
     * @return true if date is past, false otherwise
     */
    public boolean isPast() {
        Date today = new Date(Calendar.getInstance());
        return this.compareTo(today) <= 0;
    }

    /**
     * Check whether current date is in a leap year
     *
     * @return true if is leap year, false otherwise
     */
    private boolean isLeapYear() {
        if (year % Constants.QUADRENNIAL == 0) {
            if (year % Constants.CENTENNIAL == 0) {
                if (year % Constants.QUARTERCENTENNIAL == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Testbed main to test isValid() method
     *
     * @param args default parameters
     */
    public static void main(String[] args) {
        boolean expectedOutput;
        //Test Number 1
        expectedOutput = true;
        System.out.println("January, March, May, July, August, October and December, each has 31 days");
        testResult(new Date("1/1/2023"), expectedOutput);
        testResult(new Date("3/5/2023"), expectedOutput);
        testResult(new Date("5/10/2023"), expectedOutput);
        testResult(new Date("7/15/2023"), expectedOutput);
        testResult(new Date("8/22/2023"), expectedOutput);
        testResult(new Date("10/29/2023"), expectedOutput);
        testResult(new Date("12/30/2023"), expectedOutput);

        //Test Number 2
        expectedOutput = false;
        System.out.println("January, March, May, July, August, October and December, each has 31 days");
        testResult(new Date("1/32/2003"), expectedOutput);
        testResult(new Date("3/32/2003"), expectedOutput);
        testResult(new Date("5/32/2003"), expectedOutput);
        testResult(new Date("7/32/2003"), expectedOutput);
        testResult(new Date("8/32/2003"), expectedOutput);
        testResult(new Date("10/32/2003"), expectedOutput);
        testResult(new Date("12/32/2003"), expectedOutput);

        //Test Number 3
        expectedOutput = true;
        System.out.println("April, June, September and November, each has 30 days");
        testResult(new Date("4/1/2023"), expectedOutput);
        testResult(new Date("6/18/2023"), expectedOutput);
        testResult(new Date("9/27/2023"), expectedOutput);
        testResult(new Date("11/30/2023"), expectedOutput);

        //Test Number 4
        expectedOutput = false;
        System.out.println("April, June, September and November, each has 30 days");
        testResult(new Date("4/31/2003"), expectedOutput);
        testResult(new Date("6/31/2003"), expectedOutput);
        testResult(new Date("9/31/2003"), expectedOutput);
        testResult(new Date("11/31/2003"), expectedOutput);

        //Test Number 5
        expectedOutput = false;
        System.out.println("Dates cannot have negative or 0 values");
        testResult(new Date("-1/31/2003"), expectedOutput);
        testResult(new Date("0/31/2003"), expectedOutput);
        testResult(new Date("1/-31/2003"), expectedOutput);
        testResult(new Date("1/0/2003"), expectedOutput);
        testResult(new Date("1/31/-2003"), expectedOutput);
        testResult(new Date("1/31/0"), expectedOutput);

        //Test Number 6
        expectedOutput = false;
        System.out.println("Months have to be between 1 and 12.");
        testResult(new Date("13/31/2003"), expectedOutput);

        //Test Number 7
        expectedOutput = false;
        System.out.println("Years not divisible by 4 are not leap years");
        testResult(new Date("2/29/2003"), expectedOutput);

        //Test Number 8
        expectedOutput = true;
        System.out.println("Years divisible by 4 but not divisible by 100 are leap years");
        testResult(new Date("2/29/2008"), expectedOutput);

        //Test Number 9
        expectedOutput = false;
        System.out.println("Years divisible by 100 but not divisible by 400 are leap years");
        testResult(new Date("2/29/1900"), expectedOutput);

        //Test Number 10
        expectedOutput = true;
        System.out.println("Years divisible by 400 are leap years");
        testResult(new Date("2/29/2000"), expectedOutput);
    }

    /**
     * Test isValid on given Date and compare with an expected result
     *
     * @param date           Date object to be tested
     * @param expectedOutput Expected return value of isValid
     */
    private static void testResult(Date date, boolean expectedOutput) {
        boolean receivedOutput = date.isValid();
        if (receivedOutput == expectedOutput) {
            System.out.println(date + ": passed");
        } else {
            System.out.println(date + ": failed. Please do better.");
        }
    }
}