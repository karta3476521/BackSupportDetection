package com.boss.vestibularsystemdetection.backsupportdetection.Tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtils {

    public static String getDate(){
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy年MM月dd號 ahh:mm:ss");
        String time = ft.format(dNow);
        return time;
    }

}
