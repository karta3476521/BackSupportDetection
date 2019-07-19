package com.boss.vestibularsystemdetection.backsupportdetection.Setting;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.boss.vestibularsystemdetection.backsupportdetection.R;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.MyUtils;

public class LoginActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获取当前焦点所在的控件；
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                // 判断点击的点是否落在当前焦点所在的 view 上；
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                    MyUtils.hideKeyBoard(this);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void back_onClick(View v){
        finish();
    }

    public void register_onClick(View v){
        intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
