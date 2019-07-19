package com.boss.vestibularsystemdetection.backsupportdetection.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.boss.vestibularsystemdetection.backsupportdetection.R;

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
        intent = new Intent(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void history_onClick(View v){
        intent = new Intent(SettingActivity.this, HistoryActivity.class);
        startActivity(intent);
    }
}
