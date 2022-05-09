package com.example.weatherforecast.common;


import android.annotation.SuppressLint;
import android.location.Location;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Common {
    public static final String API_KEY_ID = "725389ad4485ecfccfbe8c73913ff9dc";
    public static String latitude = "21.0245", longitude = "105.8412";
    public static Location current_location = null;

    public static String convertUnixToHour (int dt){

        Date date = new Date((long)dt*1000);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return sdf.format(date);
    }
    public static String convertUnixToDayOfWeek (int dt){
        /*
        Date date = new Date(dt*1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd");
        String str = sdf.format(date);
        System.out.print(str);
        return sdf.format(date);
        */
        Date date = new Date(dt*1000L);
        Locale vn = new Locale("vi", "VN");
        DateFormat localFormat = DateFormat.getDateInstance(DateFormat.FULL, vn);
        return localFormat.format(date);

        /*
        Double d = Double.parseDouble(String.valueOf(dt));
        long unixSeconds = d.longValue();
        // convert seconds to milliseconds
        Date date = new java.util.Date(unixSeconds*1000L);

        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

         */
    }
    public static String convertUnixToDateTime (int dt){
        /*
        Date date = new Date(dt*1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, HH:mm");
        //String str = sdf.format(date);


        return sdf.format(date);

         */

        Date date = new Date(dt*1000L);
        Locale vn = new Locale("vi", "VN");
        DateFormat localFormat = DateFormat.getDateInstance(DateFormat.FULL, vn);
        return localFormat.format(date);

    }
    public static String convertDegreeToCardinalDirection(int directionInDegrees){
        String cardinalDirection;
        if( (directionInDegrees >= 348.75) && (directionInDegrees <= 360) ||
                (directionInDegrees >= 0) && (directionInDegrees <= 11.25)    ){
            cardinalDirection = "Bắc";
        } else if( (directionInDegrees >= 11.25 ) && (directionInDegrees <= 33.75)){
            cardinalDirection = "Bắc Đông Bắc";
        } else if( (directionInDegrees >= 33.75 ) &&(directionInDegrees <= 56.25)){
            cardinalDirection = "Đông Bắc";
        } else if( (directionInDegrees >= 56.25 ) && (directionInDegrees <= 78.75)){
            cardinalDirection = "Đông Đông Bắc";
        } else if( (directionInDegrees >= 78.75 ) && (directionInDegrees <= 101.25) ){
            cardinalDirection = "Đông";
        } else if( (directionInDegrees >= 101.25) && (directionInDegrees <= 123.75) ){
            cardinalDirection = "Đông Đông Nam";
        } else if( (directionInDegrees >= 123.75) && (directionInDegrees <= 146.25) ){
            cardinalDirection = "Đông Nam";
        } else if( (directionInDegrees >= 146.25) && (directionInDegrees <= 168.75) ){
            cardinalDirection = "Nam Đông Nam";
        } else if( (directionInDegrees >= 168.75) && (directionInDegrees <= 191.25) ){
            cardinalDirection = "Nam";
        } else if( (directionInDegrees >= 191.25) && (directionInDegrees <= 213.75) ){
            cardinalDirection = "Nam Tây Nam";
        } else if( (directionInDegrees >= 213.75) && (directionInDegrees <= 236.25) ){
            cardinalDirection = "Nam Tây Nam";
        } else if( (directionInDegrees >= 236.25) && (directionInDegrees <= 258.75) ){
            cardinalDirection = "Tây Tây Nam";
        } else if( (directionInDegrees >= 258.75) && (directionInDegrees <= 281.25) ){
            cardinalDirection = "Tây";
        } else if( (directionInDegrees >= 281.25) && (directionInDegrees <= 303.75) ){
            cardinalDirection = "Tây Tây Bắc";
        } else if( (directionInDegrees >= 303.75) && (directionInDegrees <= 326.25) ){
            cardinalDirection = "Tây Bắc";
        } else if( (directionInDegrees >= 326.25) && (directionInDegrees <= 348.75) ){
            cardinalDirection = "Bắc Tây Bắc";
        } else {
            cardinalDirection = "?";
        }

        return cardinalDirection;
    }
    public static void appendColoredText(TextView tv, String text, int color) {
        int start = tv.getText().length();
        tv.append(text);
        int end = tv.getText().length();

        Spannable spannableText = (Spannable) tv.getText();
        spannableText.setSpan(new ForegroundColorSpan(color), start, end, 0);
    }

}
