package com.boss.vestibularsystemdetection.backsupportdetection.measure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boss.vestibularsystemdetection.backsupportdetection.R;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.ViewPagerAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class CompareActivity extends AppCompatActivity {
//    TrajectoryView mTrajectoryView, mTrajectoryView_old;
    String old_path, path;
    Handler mHandler;
    TextView tv13, tv14, tv22, tv23, tv26, tv27;
    RotateView xyView, xzView, yzView, xyView_old, xzView_old, yzView_old;
    DotsIndicator mDotsIndicator;
    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    List<View> views;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        getSupportActionBar().hide();

        Intent intent = this.getIntent();
        path = intent.getStringExtra("path");
        old_path = intent.getStringExtra("old_path");

//        mTrajectoryView = (TrajectoryView)findViewById(R.id.trajectoryView2);
//        mTrajectoryView.setPath(path);
//        mTrajectoryView_old = (TrajectoryView)findViewById(R.id.trajectoryView3);
//        mTrajectoryView_old.setPath(old_path);

        mDotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator2);
        mViewPager = (ViewPager) findViewById(R.id.viewPager2);
        //獲取LayoutInflater
        LayoutInflater mInflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(mInflater.inflate(R.layout.xy_rotate_views, null));
        views.add(mInflater.inflate(R.layout.xz_rotate_views, null));
        views.add(mInflater.inflate(R.layout.yz_rotate_views, null));

        xyView = (RotateView)views.get(0).findViewById(R.id.rotateView4);
        xyView_old = (RotateView)views.get(0).findViewById(R.id.rotateView5);
        xzView = (RotateView)views.get(1).findViewById(R.id.rotateView6);
        xzView_old = (RotateView)views.get(1).findViewById(R.id.rotateView7);
        yzView = (RotateView)views.get(2).findViewById(R.id.rotateView8);
        yzView_old = (RotateView)views.get(2).findViewById(R.id.rotateView9);

        setTvBringToFront();
        setAtributes();
        mViewPagerAdapter = new ViewPagerAdapter(views, this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mDotsIndicator.setViewPager(mViewPager);

        mHandler = new Handler();
        mHandler.postDelayed(delayToCal, 1000);
    }

    private void setTvBringToFront(){
        tv13 = (TextView)views.get(0).findViewById(R.id.textView13);
        tv13.bringToFront();
        tv14 = (TextView)views.get(0).findViewById(R.id.textView14);
        tv14.bringToFront();
        tv22 = (TextView)views.get(1).findViewById(R.id.textView22);
        tv22.bringToFront();
        tv23 = (TextView)views.get(1).findViewById(R.id.textView23);
        tv23.bringToFront();
        tv26 = (TextView)views.get(2).findViewById(R.id.textView26);
        tv26.bringToFront();
        tv27 = (TextView)views.get(2).findViewById(R.id.textView27);
        tv27.bringToFront();
    }

    private void setAtributes(){
        xyView.setPath(path);
        xyView_old.setPath(old_path);
        xzView.setPath(path);
        xzView_old.setPath(old_path);
        yzView.setPath(path);
        yzView_old.setPath(old_path);
        xyView.setRotateType("xy");
        xyView_old.setRotateType("xy");
        xzView.setRotateType("xz");
        xzView_old.setRotateType("xz");
        yzView.setRotateType("yz");
        yzView_old.setRotateType("yz");
    }

//    private Runnable delayToCalculate = new Runnable() {
//        @Override
//        public void run() {
//            List<Double> distance = mTrajectoryView.getDistance();
//            int distance_sum = calcaulateData(distance);
//            tv11.setText(distance_sum+"");
//
//            distance.clear();
//            distance = mTrajectoryView_old.getDistance();
//            distance_sum = calcaulateData(distance);
//            tv12.setText(distance_sum+"");
//        }
//    };

    private Runnable delayToCal = new Runnable() {
        @Override
        public void run() {
            List<Double> angleX = xyView.getAngle().get(0);
            List<Double> angleY = xyView.getAngle().get(1);
            List<Double> angleZ = xyView.getAngle().get(2);
            List<Double> angleX_old = xyView_old.getAngle().get(0);
            List<Double> angleY_old = xyView_old.getAngle().get(1);
            List<Double> angleZ_old = xyView_old.getAngle().get(2);

            int angleX_sum = calcaulateData(angleX);
            int angleY_sum = calcaulateData(angleY);
            int angleZ_sum = calcaulateData(angleZ);
            int angleX_sum_old = calcaulateData(angleX_old);
            int angleY_sum_old = calcaulateData(angleY_old);
            int angleZ_sum_old = calcaulateData(angleZ_old);
            int angle_sum = angleX_sum + angleY_sum + angleZ_sum;
            int angle_sum_old = angleX_sum_old + angleY_sum_old + angleZ_sum_old;
            tv13.setText(angle_sum+"");
            tv22.setText(angle_sum+"");
            tv26.setText(angle_sum+"");
            tv14.setText(angle_sum_old+"");
            tv23.setText(angle_sum_old+"");
            tv27.setText(angle_sum_old+"");
        }
    };

    private int calcaulateData(List<Double> list){
        int sum = 0;
        for(double l : list){
            sum += Math.abs((int) l);
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
