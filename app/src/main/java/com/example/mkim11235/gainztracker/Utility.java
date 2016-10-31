package com.example.mkim11235.gainztracker;

import java.util.Calendar;

/**
 * Created by Michael on 10/18/2016!
 */

public class Utility {

    // Returns date in db format MM/DD/YYYY to YYYYMMDD
    public static String formatDateReadableToDB(String date) {
        String year = date.substring(6);
        String month = date.substring(0, 2);
        String newDate = date.substring(3, 5);

        return year + month + newDate;
    }

    // Returns current  date in format mm/dd/yyyy
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        return formatDateReadable(year, month, date);
    }

    // Returns date in readable format YYYYMMDD to MM/DD/YYYY
    public static String formatDateDBToReadable(String date) {
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6));
        int year = Integer.parseInt(date.substring(0, 4));
        return formatDateReadable(year, month, day);
    }

    // Returns date in format mm/dd/yyyy
    public static String formatDateReadable(int year, int month, int day) {
        StringBuilder builder = new StringBuilder();
        if (month / 10 == 0) {
            builder.append("0");
        }
        builder.append(month);
        builder.append("/");

        if (day / 10 == 0) {
            builder.append("0");
        }
        builder.append(day);
        builder.append("/");
        builder.append(year);

        return builder.toString();
    }

    /**
     * gets the year from readable date
     *
     * @param date date to get year from
     * @return int value of year
     */
    public static int getYearFromReadableDate(String date) {
        return Integer.parseInt(date.substring(6));
    }

    /**
     * Gets the month from readable date
     *
     * @param date date to get month from
     * @return int value of month
     */
    public static int getMonthFromReadableDate(String date) {
        return Integer.parseInt(date.substring(0, 2));
    }

    /**
     * Gets the day from readable date
     *
     * @param date date to get day from
     * @return int value of day
     */
    public static int getDayFromReadableDate(String date) {
        return Integer.parseInt(date.substring(3, 5));
    }
}
