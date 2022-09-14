package com.unina.natour.controllers.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static final String DATE_SIMPLE_PATTERN = "dd/MM/yyyy";
    public static final String DATE_FULL_PATTERN = "yyyy-MM-dd hh:mm:ss";

    public static Calendar toCalendar(String string) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_SIMPLE_PATTERN);
        Calendar calendar = Calendar.getInstance();

        Date date = null;
        try {
            date = simpleDateFormat.parse(string);
        }
        catch (ParseException e) {
            e.printStackTrace();

            simpleDateFormat = new SimpleDateFormat(DATE_FULL_PATTERN);
            date = simpleDateFormat.parse(string);

        }

        calendar.setTime(date);

        return calendar;
    }

    public static String getDateWithoutHours(String date){
        String result = date.substring(0,8);
        return result;
    }

    public static String toSimpleString(Calendar calendar){
        Date date = new Date(calendar.getTimeInMillis());
        DateFormat dateFormat = new SimpleDateFormat(DATE_SIMPLE_PATTERN);
        String string = dateFormat.format(date);

        return string;
    }

    public static String toFullString(Calendar calendar){
        Date date = new Date(calendar.getTimeInMillis());
        DateFormat dateFormat = new SimpleDateFormat(DATE_FULL_PATTERN);
        String string = dateFormat.format(date);

        return string;
    }


    public static String toDurationString(Float duration){
        String durationString;

        BigDecimal durationInHours = new BigDecimal(duration/3600);

        int hours = durationInHours.intValue();
        Float floatMinutes = durationInHours.subtract(new BigDecimal(hours)).floatValue() * 60;
        int minutes = floatMinutes.intValue();

        durationString = hours + "h " + minutes + "m";

        return durationString;
    }


    public static String toDistanceString(Float distance){
        String distanceString;

        if(distance < 1000) {
            distanceString = distance.intValue() + "m";
        }
        else{
            Float km = distance/1000;
            distanceString = String.format("%.2f", km) + "km";
        }

        return distanceString;
    }
}
