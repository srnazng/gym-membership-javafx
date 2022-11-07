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
}