package com.boss.vestibularsystemdetection.backsupportdetection.Setting;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.boss.vestibularsystemdetection.backsupportdetection.R;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.IOTool;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.MySQLiteHelper;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.MyUtils;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.ProcessControl;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.UserTool.UserManage;

public class RegisterActivity extends AppCompatActivity{
    SQLiteDatabase db;
    EditText edt4, edt5, edt6, edt7;
    TextInputLayout edtLayout3, edtLayout4, edtLayout5, edtLayout6;
    ImageView imgVu4;
    boolean isVisible = false;
    static final String table = "registers_tb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        edt4 = (EditText)findViewById(R.id.editText4);
        edt5 = (EditText)findViewById(R.id.editText5);
        edt6 = (EditText)findViewById(R.id.editText6);
        edt7 = (EditText)findViewById(R.id.editText7);
        edtLayout3 = (TextInputLayout)findViewById(R.id.textInputLayout3);
        edtLayout4 = (TextInputLayout)findViewById(R.id.textInputLayout4);
        edtLayout5 = (TextInputLayout)findViewById(R.id.textInputLayout5);
        edtLayout6 = (TextInputLayout)findViewById(R.id.textInputLayout6);
        imgVu4 = (ImageView)findViewById(R.id.imageView4);

    }

    //open database
    private void openDatabase(){
        MySQLiteHelper dbHelper = new MySQLiteHelper(this);
        //getWritableDatabase 可以讀寫
        //getReadableDatabase 唯讀
        db = dbHelper.getReadableDatabase();
        System.err.println(" 資料庫是否開啟"+db.isOpen()+"，版本"+db.getVersion());
    }

    //close database
    private void closeDatabase(){
        db.close();
    }

    public void back_onClick(View v){
        finish();
    }

    public void register_onClick(View v){
        if(checkEditText(edt4) && checkEditText(edt5) && checkEditText(edt6) && checkEditText(edt7)){
            int age = Integer.parseInt(edt7.getText().toString().replace(" ", ""));

            //ContentValues Map集合物件
            ContentValues mContentValues = new ContentValues();
            mContentValues.put("email", edt4.getText().toString().replace(" ", ""));
            mContentValues.put("password", edt5.getText().toString().replace(" ", ""));
            mContentValues.put("name", edt6.getText().toString().trim());
            mContentValues.put("age", age);
            //插入
            long count = db.insert(table, null, mContentValues);

            if(count > 0) {
                Toast.makeText(this, "註冊成功", Toast.LENGTH_SHORT).show();
                UserManage.setUserEmail(edt4.getText().toString().replace(" ", ""));
                IOTool mIOTool = new IOTool("BackDetectionData");
                mIOTool.writeFile("account.xml", edt4.getText().toString().replace(" ", ""));

                if(ProcessControl.getProcessStatus() == "Second")
                    finish();
                else {
                    Intent intent = new Intent(RegisterActivity.this, AccoutManageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else
                Toast.makeText(this, "註冊失敗", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, "註冊失敗", Toast.LENGTH_SHORT).show();
    }

    //check EditText is not Empty
    private boolean checkEditText(EditText edt){
        if(edt.getText().toString().replace(" ", "").length() != 0) {
            switch (edt.getId()){
                case R.id.editText4:
                    //關閉錯誤提示
                    edtLayout3.setErrorEnabled(false);
                    break;
                case R.id.editText5:
                    edtLayout4.setErrorEnabled(false);
                    break;
                case R.id.editText6:
                    edtLayout5.setErrorEnabled(false);
                    break;
                case R.id.editText7:
                    edtLayout6.setErrorEnabled(false);
                    break;
                default:
                    break;
            }

            return true;
        }else {
            switch (edt.getId()){
                case R.id.editText4:
                    //顯示錯誤提示
                    edtLayout3.setErrorEnabled(true);
                    edtLayout3.setError("不可為空");
                    break;
                case R.id.editText5:
                    edtLayout4.setErrorEnabled(true);
                    edtLayout4.setError("不可為空");
                    break;
                case R.id.editText6:
                    edtLayout5.setErrorEnabled(true);
                    edtLayout5.setError("不可為空");
                    break;
                case R.id.editText7:
                    edtLayout6.setErrorEnabled(true);
                    edtLayout6.setError("不可為空");
                    break;
                default:
                    break;
            }

            return false;
        }
    }

    //show & hide password text
    public void passwordVisible_onClick(View v){
        isVisible = !isVisible;
        if(isVisible) {
            //show password
            edt5.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //let cursor stay the last word
            edt5.setSelection(edt5.getText().length());
            //change imageView to sleepy_eye.png
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imgVu4.setBackground(getResources().getDrawable(R.drawable.sleepy_eye));
            }
        }else {
            //hide password
            edt5.setTransformationMethod(PasswordTransformationMethod.getInstance().getInstance());
            edt5.setSelection(edt5.getText().length());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imgVu4.setBackground(getResources().getDrawable(R.drawable.eye));
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //獲取當前焦點所在的控件；
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                //判斷點擊的點是否落在當前焦點所在的view 上；
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                    //隱藏鍵盤
                    MyUtils.hideKeyBoard(this);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onStart() {
        super.onStart();
        openDatabase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeDatabase();
    }
}
