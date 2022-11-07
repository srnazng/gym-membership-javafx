package com.example.project3.tests;

import com.example.project3.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateTest {
    @Test
    public void testMonthRange() {
        //Months must be between 1 and 12
        Date date1 = new Date("-1/1/2022");
        assertFalse(date1.isValid());

        Date date2 = new Date("0/1/2022");
        assertFalse(date2.isValid());

        Date date3 = new Date("1/1/2022");
        assertTrue(date3.isValid());

        Date date4 = new Date("13/1/2022");
        assertFalse(date4.isValid());
    }

    @Test
    public void testDayRangeInLongMonth() {
        //Dates in a long month must be between 1 and 31
        Date date1 = new Date("1/-1/2003");
        assertFalse(date1.isValid());

        Date date2 = new Date("1/0/2003");
        assertFalse(date2.isValid());

        Date date3 = new Date("1/31/2003");
        assertTrue(date3.isValid());

        Date date4 = new Date("10/32/2023");
        assertFalse(date4.isValid());
    }

    @Test
    public void testDayRangeInShortMonth() {
        //Dates in a short month must be between 1 and 30
        Date date1 = new Date("4/-1/2022");
        assertFalse(date1.isValid());

        Date date2 = new Date("4/0/2022");
        assertFalse(date2.isValid());

        Date date3 = new Date("4/30/2022");
        assertTrue(date3.isValid());

        Date date4 = new Date("6/31/2003");
        assertFalse(date4.isValid());
    }

    public void testYearRange() {
        //Year must be a positive number
        Date date1 = new Date("4/1/-1");
        assertFalse(date1.isValid());

        Date date2 = new Date("4/1/0");
        assertFalse(date2.isValid());

        Date date3 = new Date("4/1/1");
        assertTrue(date3.isValid());
    }


    @Test
    public void testYearNotMultipleOfFour() {
        //28 days in february when the year is not a multiple of 4
        Date date1 = new Date("2/28/2009");
        assertTrue(date1.isValid());

        Date date2 = new Date("2/29/2009");
        assertFalse(date2.isValid());
    }

    @Test
    public void testYearMultipleOfFourNotHundred() {
        //29 days in the February when the year is a multiple of 4
        //and not a multiple of one hundred
        Date date1 = new Date("2/29/2008");
        assertTrue(date1.isValid());

        Date date2 = new Date("2/30/2008");
        assertFalse(date2.isValid());
    }

    @Test
    public void testYearMultipleofHundredNotFourHundred() {
        //28 days in February is multiple of one hundred and
        //Not multiple of four hundred
        Date date1 = new Date("2/28/1900");
        assertTrue(date1.isValid());

        Date date2 = new Date("2/29/1900");
        assertFalse(date2.isValid());
    }

    @Test
    public void testYearMultipleOfFourHundred() {
        //29 days in February when year is multiple of four hundred
        Date date1 = new Date("2/29/2008");
        assertTrue(date1.isValid());

        Date date2 = new Date("2/30/2008");
        assertFalse(date2.isValid());
    }

}