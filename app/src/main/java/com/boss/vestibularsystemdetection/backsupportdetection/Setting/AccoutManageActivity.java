package com.boss.vestibularsystemdetection.backsupportdetection.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.boss.vestibularsystemdetection.backsupportdetection.R;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.UserTool.LogoutSystem;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.UserTool.UserManage;

public class AccoutManageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accout_manage);
        getSupportActionBar().hide();

        TextView tv17 = (TextView)findViewById(R.id.textView17);
        tv17.setText(UserManage.getUserEmail());
    }

    public void logout_onClick(View v){
        LogoutSystem.logout();
        finish();
    }

    public void modify_onClick(View v){
        Intent intent = new Intent(AccoutManageActivity.this, ModifyImformationActivity.class);
        startActivity(intent);
    }

    public void back_onClick(View v){
        finish();
    }
}
