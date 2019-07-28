package com.boss.greenpower.backsupportdetection.Tool.UserTool;

import com.boss.greenpower.backsupportdetection.Tool.IOTool;

public class LogoutSystem {

    public static void logout(){
        UserManage.setUserEmail("");
        IOTool mIOTool = new IOTool("BackDetectionData");
        mIOTool.writeFile("account.xml", "");
    }

}
