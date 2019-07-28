package com.boss.greenpower.backsupportdetection.measure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boss.greenpower.backsupportdetection.R;
import com.boss.greenpower.backsupportdetection.Tool.ViewPagerAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    RotateView xyView, xzView, yzView;
    TextView tv10, tv40, tv42, tv44;
    String path;
    Handler mHandler;
    DotsIndicator mDotsIndicator;
    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    private Runnable delayToCal = new Runnable() {
        @Override
        public void run() {
            List<Double> angleX = xyView.getAngle().get(0);
            List<Double> angleY = xyView.getAngle().get(1);
            List<Double> angleZ = xyView.getAngle().get(2);

            int angleX_sum = calcaulateData(angleX);
            int angleY_sum = calcaulateData(angleY);
            int angleZ_sum = calcaulateData(angleZ);
            tv10.setText(angleX_sum + angleY_sum + angleZ_sum+"");
            tv40.setText(angleX_sum + angleY_sum+"");
            tv42.setText(angleX_sum + angleZ_sum+"");
            tv44.setText(angleY_sum + angleZ_sum+"");
        }
    };

    private void setAtributes(){
        xyView.setPath(path);
        xzView.setPath(path);
        yzView.setPath(path);
        xyView.setRotateType("xy");
        xzView.setRotateType("xz");
        yzView.setRotateType("yz");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        getSupportActionBar().hide();

        Intent intent = this.getIntent();
        path = intent.getStringExtra("path");

        mDotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        //獲取LayoutInflater
        LayoutInflater mInflater = LayoutInflater.from(this);
        List<View> views = new ArrayList<View>();
        views.add(mInflater.inflate(R.layout.xy_rotate_view, null));
        views.add(mInflater.inflate(R.layout.xz_rotate_view, null));
        views.add(mInflater.inflate(R.layout.yz_rotate_view, null));
        xyView = (RotateView)views.get(0).findViewById(R.id.rotateView);
        xzView = (RotateView)views.get(1).findViewById(R.id.rotateView2);
        yzView = (RotateView)views.get(2).findViewById(R.id.rotateView3);
        tv40 = (TextView)views.get(0).findViewById(R.id.textView40);
        tv42 = (TextView)views.get(1).findViewById(R.id.textView42);
        tv44 = (TextView)views.get(2).findViewById(R.id.textView44);
        setAtributes();
        mViewPagerAdapter = new ViewPagerAdapter(views, this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mDotsIndicator.setViewPager(mViewPager);

        mHandler = new Handler();
        mHandler.postDelayed(delayToCal, 300);
        tv10 = (TextView)findViewById(R.id.textView10);
    }

    private int calcaulateData(List<Double> list){
        int sum = 0;
        for(double l : list){
            sum += Math.abs((int)l);
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
