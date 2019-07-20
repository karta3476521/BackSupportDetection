package com.boss.vestibularsystemdetection.backsupportdetection.measure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.boss.vestibularsystemdetection.backsupportdetection.Tool.Arith;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.IOTool;

import java.util.ArrayList;
import java.util.List;

public class RotateView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    SurfaceHolder mSurfaceHolder;
    Canvas mCanvas;
    private int mWidth;
    private int mHeight;
    private String path;
    private String rotateType;

    public RotateView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView();
    }

    private void initView(){
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);//獲取焦點
        setFocusableInTouchMode(true);//透過點擊獲取焦點
        this.setKeepScreenOn(true);//保持屏幕长亮
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    public void setPath(String path){
        this.path = path;
    }

    public void setRotateType(String rotateType){
        this.rotateType = rotateType;
    }

    private List<Double> angle_X, angle_Y, angle_Z;
    private void getdata(){
//        IOTool mIOTool = new IOTool("BackDetectionData/2019年07月20號 下午04:23:45");
        IOTool mIOTool = new IOTool(path);
        float ave_X = Float.parseFloat(mIOTool.readFile("average_X.xml").get(0));
        float ave_Y = Float.parseFloat(mIOTool.readFile("average_Y.xml").get(0));
        float ave_Z = Float.parseFloat(mIOTool.readFile("average_Z.xml").get(0));
        List<String> axis_X = mIOTool.readFile("axis_X.xml");
        List<String> axis_Y = mIOTool.readFile("axis_Y.xml");
        List<String> axis_Z = mIOTool.readFile("axis_Z.xml");

        angle_X = calculateAngle(axis_X, ave_X);
        angle_Y = calculateAngle(axis_Y, ave_Y);
        angle_Z = calculateAngle(axis_Z, ave_Z);
    }

    public List<List<Double>> getAngle(){
        List<List<Double>> angle = new ArrayList<List<Double>>();
        angle.add(angle_X);
        angle.add(angle_Y);
        angle.add(angle_Z);

        return angle;
    }

    private List<Double> calculateAngle(List<String> list, float ave_angle){
        List<Double> angle = new ArrayList<Double>();

        for(int i=0; i<list.size(); i++){
            float sensor_angle = Float.parseFloat(list.get(i));
            double sub_angle = Arith.sub(sensor_angle, ave_angle);
            angle.add(Math.toDegrees(sub_angle));
        }
        return angle;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        getdata();
        drawSomething();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {

    }

    //繪圖邏輯
    private synchronized void drawSomething() {
        try {
            //獲得canvas
            mCanvas = mSurfaceHolder.lockCanvas();
//            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            //畫筆
            Paint mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(2);
            mPaint.setColor(Color.GRAY);

            //draw data
            DrawData(mCanvas, mPaint);

            //View 的中央
            mPaint.setColor(Color.RED); //顏色
            mPaint.setStrokeWidth(20);
            mCanvas.drawPoint(mWidth/2, mHeight/2, mPaint);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }finally {
            if (mCanvas != null){
                //釋放canvas並提交畫布
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void DrawData(Canvas mCanvas, Paint mPaint) throws Exception {
        Path mPath = new Path();

        if(rotateType == "xy") {
            mPath.moveTo(mWidth / 2 + angle_X.get(0).floatValue(), mHeight / 2 + angle_Y.get(0).floatValue());

            for (int i = 0; i < angle_X.size(); i++) {
                float pointX = mWidth / 2 + angle_X.get(i).floatValue();
                float pointY = mHeight / 2 + angle_Y.get(i).floatValue();
                mPath.lineTo(pointX, pointY);
            }
        }else if(rotateType == "xz"){

            mPath.moveTo(mWidth / 2 + angle_X.get(0).floatValue(), mHeight / 2 + angle_Z.get(0).floatValue());

            for (int i = 0; i < angle_X.size(); i++) {
                float pointX = mWidth / 2 + angle_X.get(i).floatValue();
                float pointY = mHeight / 2 + angle_Z.get(i).floatValue();
                mPath.lineTo(pointX, pointY);
            }
        }else if(rotateType == "yz"){
            mPath.moveTo(mWidth / 2 + angle_Y.get(0).floatValue(), mHeight / 2 + angle_Z.get(0).floatValue());

            for (int i = 0; i < angle_Y.size(); i++) {
                float pointX = mWidth / 2 + angle_Y.get(i).floatValue();
                float pointY = mHeight / 2 + angle_Z.get(i).floatValue();
                mPath.lineTo(pointX, pointY);
            }
        }else
            throw new Exception("send error Rotatetype");

        mCanvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (wSpecMode == MeasureSpec.AT_MOST && hSpecMode == MeasureSpec.AT_MOST) {
            System.err.println("1");
//            setMeasuredDimension(wSpecSize*4/5, hSpecSize*2/5);
            setMeasuredDimension(wSpecSize, hSpecSize);
        } else if (wSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wSpecSize, hSpecSize);
        } else if (hSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, hSpecSize);
        }
    }
}
