package com.driver;

public class TimeUtils {
    public static int convertTime(String deliveryTime){
        String[] arr = deliveryTime.split(":");
        int HH = Integer.parseInt(arr[0]);
        int MM = Integer.parseInt(arr[1]);
        return HH*60 + MM;
    }
    public static String convertTime(int deliveryTime){
        int HH = deliveryTime/60;
        int MM = deliveryTime%60;
        String hh = String.valueOf(HH);
        String mm = String.valueOf(MM);

        if(hh.length() == 1){
            hh = '0' + hh;
        }
        if(mm.length() == 1){
            mm = '0' + mm;
        }
        return hh + ':' + mm;

    }
}
