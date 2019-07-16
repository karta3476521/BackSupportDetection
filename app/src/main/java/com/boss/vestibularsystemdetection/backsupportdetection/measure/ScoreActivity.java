package com.boss.vestibularsystemdetection.backsupportdetection.measure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.boss.vestibularsystemdetection.backsupportdetection.R;

import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    TrajectoryView mTrajectoryView;
    TextView tv10;
    String path;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        getSupportActionBar().hide();

        Intent intent = this.getIntent();
        path = intent.getStringExtra("path");
        mTrajectoryView = (TrajectoryView)findViewById(R.id.trajectoryView);
        mTrajectoryView.setPath(path);

        mHandler = new Handler();
        mHandler.postDelayed(delayToCalculate, 300);
        tv10 = (TextView)findViewById(R.id.textView10);
    }

    private Runnable delayToCalculate = new Runnable() {
        @Override
        public void run() {
            List<Integer> distance = mTrajectoryView.getDistance();
            int distance_sum = calcaulateData(distance);
            tv10.setText(distance_sum+"");
        }
    };

    private int calcaulateData(List<Integer> list){
        int sum = 0;
        for(int l : list){
            sum += Math.abs(l);
        }
        return sum;
    }

    public void back_onClick(View v){
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
    }
}
