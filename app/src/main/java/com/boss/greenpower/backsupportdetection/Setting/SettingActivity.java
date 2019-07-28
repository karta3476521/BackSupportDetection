package com.boss.greenpower.backsupportdetection.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.boss.greenpower.backsupportdetection.R;
import com.boss.greenpower.backsupportdetection.Tool.UserTool.UserManage;

public class SettingActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

    }

    public void back_onClick(View v){
        finish();
    }

    public void account_onClick(View v){
        if(UserManage.getUserEmail().length() > 0){
            intent = new Intent(SettingActivity.this, AccoutManageActivity.class);
            startActivity(intent);
        }else {
            intent = new Intent(SettingActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void history_onClick(View v){
        intent = new Intent(SettingActivity.this, HistoryActivity.class);
        startActivity(intent);
    }
}
