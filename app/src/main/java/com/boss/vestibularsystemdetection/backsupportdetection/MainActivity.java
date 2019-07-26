package com.boss.vestibularsystemdetection.backsupportdetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.boss.vestibularsystemdetection.backsupportdetection.Setting.SettingActivity;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.IOTool;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.UserTool.UserManage;
import com.boss.vestibularsystemdetection.backsupportdetection.measure.AdjustActionActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    SensorManager mSensorManager;
    Sensor magneticSensor, accelerometerSensor;
    boolean isHaveSensor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        detectSensor();
        getWritePermission();
        //創建目錄資料夾
        IOTool mIOTool = new IOTool("BackDetectionData");
        List<String> fileNames = Arrays.asList(mIOTool.getNameList());
        if(fileNames.contains("account.xml")) {
            try {
                String account = mIOTool.readFile("account.xml").get(0);
                UserManage.setUserEmail(account);
            }catch (IndexOutOfBoundsException ex){
                System.err.println("no account login");
            }
        }
    }

    private void detectSensor(){
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(magneticSensor == null)
            Toast.makeText(this, "沒有磁力儀，無法測量", Toast.LENGTH_SHORT).show();
        else if(accelerometerSensor == null)
            Toast.makeText(this, "沒有加速度儀，無法測量", Toast.LENGTH_SHORT).show();
        else
            isHaveSensor = true;
    }

    private void getWritePermission(){
        int permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }



    public void measure_onClick(View v){
        int permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "請開啟權限才可量測", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else if(isHaveSensor){
            intent = new Intent(MainActivity.this, AdjustActionActivity.class);
            startActivity(intent);
        }
    }

    public void view_onClick(View v){
        intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    //使返回鍵失效
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
