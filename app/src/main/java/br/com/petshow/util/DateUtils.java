package br.com.petshow.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by bruno on 17/02/2017.
 */

public class DateUtils {

    public static Date getDate(int year,int month, int day){
        Calendar calendar =  Calendar.getInstance();
        calendar.set(year, month, day);

        Date data =calendar.getTime();

        return data;
    }
    public static String dateToString(int year,int month, int day){
        Calendar calendar =  Calendar.getInstance();
        calendar.set(year, month, day);

        Date data =calendar.getTime();
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dt =format.format(data);
        return dt;
    }


    public static String dateToString(Date date){



        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dt =format.format(date);
        return dt;
    }



}
