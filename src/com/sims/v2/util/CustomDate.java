package com.sims.v2.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDate {
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public static String serialize(Date date){
        return formatter.format(date);
    }

    public static Date deserialize(String date){
        try {
            return formatter.parse(date);
        }
        catch (ParseException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
