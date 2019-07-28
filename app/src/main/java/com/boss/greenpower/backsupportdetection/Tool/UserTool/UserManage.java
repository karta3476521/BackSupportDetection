package com.boss.greenpower.backsupportdetection.Tool.UserTool;

public class UserManage {
    private static String USER_EMAIL = "";

    public static void setUserEmail(String email){
        USER_EMAIL = email;
    }

    public static String getUserEmail(){
        return USER_EMAIL;
    }
}
