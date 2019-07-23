package com.boss.vestibularsystemdetection.backsupportdetection.Tool.UserTool;

import com.boss.vestibularsystemdetection.backsupportdetection.Tool.IOTool;

public class LogoutSystem {

    public static void logout(){
        UserManage.setUserEmail("");
        IOTool mIOTool = new IOTool("BackDetectionData");
        mIOTool.writeFile("account.xml", "");
    }

}
