package com.boss.vestibularsystemdetection.backsupportdetection.Tool;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtils {

    public static String getDate(){
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy年MM月dd號 ahh:mm:ss");
        String time = ft.format(dNow);
        return time;
    }

    public static void hideKeyBoard(Activity mActivity){
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getWindowToken(), 0);
    }

}
