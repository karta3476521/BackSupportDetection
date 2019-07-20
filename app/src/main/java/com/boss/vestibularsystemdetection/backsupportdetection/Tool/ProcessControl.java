package com.boss.vestibularsystemdetection.backsupportdetection.Tool;

public class ProcessControl {
    private static String PROCESS = "";

    public static void setProcess(String process){
        PROCESS = process;
    }

    public static String getProcessStatus(){
        return PROCESS;
    }

}
