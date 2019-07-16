package com.boss.vestibularsystemdetection.backsupportdetection.measure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.boss.vestibularsystemdetection.backsupportdetection.R;

import java.util.List;

public class CompareActivity extends AppCompatActivity {
    TrajectoryView mTrajectoryView, mTrajectoryView_old;
    String old_path, path;
    Handler mHandler;
    TextView tv11, tv12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        getSupportActionBar().hide();

        Intent intent = this.getIntent();
        path = intent.getStringExtra("path");
        mTrajectoryView = (TrajectoryView)findViewById(R.id.trajectoryView2);
        mTrajectoryView.setPath(path);

        old_path = intent.getStringExtra("old_path");
        mTrajectoryView_old = (TrajectoryView)findViewById(R.id.trajectoryView3);
        mTrajectoryView_old.setPath(old_path);

        mHandler = new Handler();
        mHandler.postDelayed(delayToCalculate, 300);
        tv11 = (TextView)findViewById(R.id.textView11);
        tv12 = (TextView)findViewById(R.id.textView12);
    }

    private Runnable delayToCalculate = new Runnable() {
        @Override
        public void run() {
            List<Integer> distance = mTrajectoryView.getDistance();
            int distance_sum = calcaulateData(distance);
            tv11.setText(distance_sum+"");

            distance.clear();
            distance = mTrajectoryView_old.getDistance();
            distance_sum = calcaulateData(distance);
            tv12.setText(distance_sum+"");
        }
    };

    private int calcaulateData(List<Integer> list){
        int sum = 0;
        for(int l : list){
            sum += Math.abs(l);
        }
        return sum;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
    }

    public void back_onClick(View v){
        finish();
    }
}
