package br.com.petshow.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class DateUtilsAndroid {

    public static Date getDate(int year,int month, int day){
        Calendar calendar =  Calendar.getInstance();
        calendar.set(year, month, day);

        Date data =calendar.getTime();

        return data;
    }
    public static String dateToString(int year,int month, int day){
        // meses o index inicia em 0 e termina em 11
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



    public static String dateTo_ddMMYYYY(Date date){


        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

}
