package com.boss.greenpower.backsupportdetection.measure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boss.greenpower.backsupportdetection.MainActivity;
import com.boss.greenpower.backsupportdetection.R;
import com.boss.greenpower.backsupportdetection.Tool.Arith;
import com.boss.greenpower.backsupportdetection.Tool.IOTool;
import com.boss.greenpower.backsupportdetection.Tool.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class AdjustActionActivity extends AppCompatActivity implements SensorEventListener {
    CountDownTimer mCountDownTimer;
    Handler mHandler;
    boolean isPause = false, isRecordStart = false, isSecondMeasure = false, isRemeasure = false, isOnPause = false, isOnFinish = false;
    SensorManager mSensorManager;
    Sensor magneticSensor, accelerometerSensor;
    IOTool mIOTool;
    TextView tv39;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_action);
        getSupportActionBar().hide();

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mHandler = new Handler();
        init();
    }

    List<Float> list_AxisX, list_AxisY, list_AxisZ;
//    List<Boolean> timesOfCheck;
    float[] values, r, gravity, geomagnetic;
    TextView tv3, tv4, tv5;
    ImageView imgView;
    String path;
    String runnable_name = "";
    float ave_X, ave_Y, ave_Z;

    private void init(){
        values = new float[3];
        gravity = new float[3];
        r = new float[9];
        geomagnetic = new float[3];
        list_AxisX = new ArrayList<Float>();
        list_AxisY = new ArrayList<Float>();
        list_AxisZ = new ArrayList<Float>();
//        timesOfCheck = new ArrayList<Boolean>();
        tv3 = (TextView)findViewById(R.id.textView3);
        tv4 = (TextView)findViewById(R.id.textView4);
        tv5 = (TextView)findViewById(R.id.textView5);
        tv39 = (TextView)findViewById(R.id.textView39);
        imgView = (ImageView) findViewById(R.id.imageView);
    }

    //sensor
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
            getValues();
        }

        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            geomagnetic = event.values;
        }

    }

    private void getValues(){
        SensorManager.getRotationMatrix(r, null, gravity, geomagnetic);
        SensorManager.getOrientation(r, values);
        tv39.setText("X："+(int)Math.toDegrees(values[1]) +
                ", Y："+(int)Math.toDegrees(values[2]) +
                ", Z："+(int)Math.toDegrees(values[0]));
        if(!isPause) {
            isPause = true;
            runnable_name = "delayToStart";
            mHandler.postDelayed(delayToStart, 3000);
//            checkBalance();
        }
        if(isRecordStart)
            addValues();
    }

    private Runnable delayToStart = new Runnable() {
        @Override
        public void run() {
            runnable_name = "";
            isRecordStart = true;
            setCountDown(3000);
        }
    };

    private void addValues(){
        list_AxisZ.add(values[0]);
        list_AxisX.add(values[1]);
        list_AxisY.add(values[2]);
    }

    //檢查角度是否多次連續小於一定角度，但因為較新的手機執行數度太快暫時不使用
//    int roll_temp, roll = 0;
//    private boolean checkValue(){
//        roll_temp = roll;
//        roll = (int)Math.toDegrees(values[2]);
//        if(Math.abs(roll - roll_temp) <= 15)
//            return true;
//        else
//            return false;
//    }
//
//    private void checkBalance(){
//        timesOfCheck.add(checkValue());
//
//        int times = 0;
//        for(boolean check : timesOfCheck){
//            if(check == true)
//                times++;
//            else
//                times = 0;
//
//            if(times >= 30) {
//                isPause = true;
//                isRecordStart = true;
//                setCountDown(3500);
//            }
//        }
//    }

    MediaPlayer mMediaPlayer;
    long curTime;
    private void setCountDown(final long millisInFuture) {
        isOnFinish = false;

        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {

            @Override
            public void onFinish() {
                isRecordStart = false;
                isOnFinish = true;
                mCountDownTimer.cancel();
                if(!isSecondMeasure)
                    dismiss();
                else
                    end();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                curTime = millisUntilFinished;  //防止onPause中斷

                if(isSecondMeasure)
                    tv5.setText((int)curTime/1000+"");

                if(mMediaPlayer != null){
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }

                mMediaPlayer = MediaPlayer.create(AdjustActionActivity.this, R.raw.ding);
                mMediaPlayer.start();
            }

        }.start();
    }

    private void dismiss(){
        if(!isRemeasure) {
            tv4.setVisibility(View.VISIBLE);
            ave_X = average(list_AxisX);
            ave_Y = average(list_AxisY);
            ave_Z = average(list_AxisZ);
            path = "BackDetectionData/" + MyUtils.getDate();
            mIOTool = new IOTool(path);
            mIOTool.writeFile("average_X.xml", ave_X + "");
            mIOTool.writeFile("average_Y.xml", ave_Y + "");
            mIOTool.writeFile("average_Z.xml", ave_Z + "");
            runnable_name = "delayToTemp";
            mHandler.postDelayed(delayToTemp, 2000);
        }else {
            float ave_X_plus = average(list_AxisX);
            float ave_Y_plus = average(list_AxisY);
            float ave_Z_plus = average(list_AxisZ);

            if(isPosture_confirm(ave_X, ave_X_plus) && isPosture_confirm(ave_Y, ave_Y_plus) && isPosture_confirm(ave_Z, ave_Z_plus)){
                tv4.setVisibility(View.VISIBLE);
                path = "BackDetectionData/" + MyUtils.getDate();
                mIOTool = new IOTool(path);
                mIOTool.writeFile("average_X.xml", ave_X_plus + "");
                mIOTool.writeFile("average_Y.xml", ave_Y_plus + "");
                mIOTool.writeFile("average_Z.xml", ave_Z_plus + "");
                runnable_name = "delayToTemp";
                mHandler.postDelayed(delayToTemp, 2000);
            }else {
                list_AxisX.clear();
                list_AxisY.clear();
                list_AxisZ.clear();
                isPause = false;
            }
        }
    }

    private boolean isPosture_confirm(float previous, float now){
        if(Math.toDegrees(now) <= Arith.add(Math.toDegrees(previous), 10) && Math.toDegrees(now) >= Arith.sub(Math.toDegrees(previous), 10))
            return true;
        else
            return false;
    }

    private float average(List<Float> list){
        float sum = 0;
        for(float o : list){
            sum += o;
        }
        return sum / list.size();
    }

    private Runnable delayToTemp = new Runnable() {
        @Override
        public void run() {
            runnable_name = "";
            tv4.setVisibility(View.INVISIBLE);
            tv3.setText("保持不動，\n嗶聲後開始測量");
            runnable_name = "delayToMeasure";
            mHandler.postDelayed(delayToMeasure, 2000);
        }
    };

    private Runnable delayToMeasure = new Runnable() {
        @Override
        public void run() {
            runnable_name = "";
            imgView.setVisibility(View.INVISIBLE);
            tv5.setVisibility(View.VISIBLE);
            tv3.setText("開始測量");

            list_AxisX.clear();
            list_AxisY.clear();
            list_AxisZ.clear();
            isSecondMeasure = true;
            isRecordStart = true;
            setCountDown(5000);
        }
    };

    private void end(){
        tv3.setText("測量完畢");
        tv5.setText("完成");
        mIOTool.writeFile("axis_X.xml", list_AxisX);
        mIOTool.writeFile("axis_Y.xml", list_AxisY);
        mIOTool.writeFile("axis_Z.xml", list_AxisZ);

        if(!isRemeasure)
            setDialog();
        else
            setDialog2();
    }

    private void setDialog(){
        // get dialog.xml view
        LayoutInflater mlayoutInflater = LayoutInflater.from(this);
        View dialogView = mlayoutInflater.inflate(R.layout.first_dialog, null);

        Button bt1 = (Button)dialogView.findViewById(R.id.button5);
        Button bt2 = (Button)dialogView.findViewById(R.id.button6);
        TextView mTextView = (TextView)dialogView.findViewById(R.id.textView7);

        final AlertDialog mAlertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdjustActionActivity.this, ScoreActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProcessControl.setProcess("Second");
                remeasure();
                mAlertDialog.cancel();
//                if(UserManage.getUserEmail().length() > 0) {
//                    remeasure();
//                    mAlertDialog.cancel();
//                }else{
//                    Intent intent = new Intent(AdjustActionActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }
            }
        });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdjustActionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlertDialog.show();
    }

    private void setDialog2(){
        // get dialog.xml view
        LayoutInflater mlayoutInflater = LayoutInflater.from(this);
        View dialogView = mlayoutInflater.inflate(R.layout.second_dialog, null);

        Button bt1 = (Button)dialogView.findViewById(R.id.button7);
        Button bt2 = (Button)dialogView.findViewById(R.id.button8);
        TextView mTextView = (TextView)dialogView.findViewById(R.id.textView8);

        final AlertDialog mAlertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdjustActionActivity.this, ScoreActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdjustActionActivity.this, CompareActivity.class);
                intent.putExtra("old_path", old_path);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdjustActionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlertDialog.show();
    }

    private String old_path;
    private void remeasure(){
        isRemeasure = true;
        isSecondMeasure = false;
        isPause = false;
        list_AxisX.clear();
        list_AxisY.clear();
        list_AxisZ.clear();
        old_path = path;

//        timesOfCheck.clear();
        tv3.setText("嗨！請握著手機\n保持不動3秒");
        tv5.setVisibility(View.INVISIBLE);
        imgView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isOnPause && !isOnFinish)
            setCountDown(curTime);

        //防止Handler onPause後app停擺
        setHandlerNotYetFinished();
        mSensorManager.registerListener(this, magneticSensor, 100 * 1000);
        mSensorManager.registerListener(this, accelerometerSensor, 100 * 1000);
    }

    private void setHandlerNotYetFinished(){
        mHandler = new Handler();

        switch (runnable_name){
            case "delayToStart":
                mHandler.postDelayed(delayToStart, 2000);
                break;
            case "delayToTemp":
                mHandler.postDelayed(delayToTemp, 2000);
                break;
            case "delayToMeasure":
                mHandler.postDelayed(delayToMeasure, 2000);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCountDownTimer != null) {
            mCountDownTimer.cancel();
            isOnPause = true;
        }

        if(mHandler != null)
            mHandler.removeCallbacksAndMessages(null);

        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void back_onClick(View v){
        finish();
    }

    //sensor
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
