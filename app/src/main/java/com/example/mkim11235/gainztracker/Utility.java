package com.example.mkim11235.gainztracker;

/**
 * Created by Michael on 10/18/2016.
 */

public class Utility {
    // Returns date in format mmddyyyy
    public static String formatDate(int year, int month, int day) {
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

    // Returns date in readable format MMDDYYYY to MM/DD/YYYY
    public static String formatDateFromString(String date) {
        int month = Integer.parseInt(date.substring(0, 2));
        int day = Integer.parseInt(date.substring(2, 4));
        int year = Integer.parseInt(date.substring(4, date.length()));
        return formatDate(month, day, year);
    }
}
