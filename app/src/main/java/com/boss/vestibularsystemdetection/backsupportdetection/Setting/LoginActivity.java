package com.boss.vestibularsystemdetection.backsupportdetection.Setting;

import android.content.Intent;
import android.database.Cursor;
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
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.MySQLiteHelper;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.MyUtils;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.ProcessControl;
import com.boss.vestibularsystemdetection.backsupportdetection.Tool.UserTool.UserManage;

public class LoginActivity extends AppCompatActivity {
    Intent intent;
    EditText edt2, edt3;
    TextInputLayout edtLayout1, edtLayout2;
    ImageView imgVu5;
    boolean isVisible = false;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        edt2 = (EditText)findViewById(R.id.editText2);
        edt3 = (EditText)findViewById(R.id.editText3);
        edtLayout1 = (TextInputLayout)findViewById(R.id.textInputLayout);
        edtLayout2 = (TextInputLayout)findViewById(R.id.textInputLayout2);
        imgVu5 = (ImageView)findViewById(R.id.imageView5);
    }

    //open database
    private void openDatabase(){
        MySQLiteHelper dbHelper = new MySQLiteHelper(this);
        //getWritableDatabase 可以讀寫
        //getReadableDatabase 唯讀
        db = dbHelper.getReadableDatabase();
    }

    //close database
    private void closeDatabase(){
        db.close();
    }

    public void login_onClick(View v){
        if(checkEditText(edt2) && checkEditText(edt3)){
            String[] columns = {"email", "password", "name", "age"};

            //查詢database
            String qry = "email='" + edt2.getText().toString().replace(" ", "") + "'";
            Cursor mCursor = db.query("registers_tb", columns, qry, null, null, null, null);

            if(mCursor!= null && mCursor.getCount() == 0) {
                Toast.makeText(this, "未註冊帳號，請重新註冊", Toast.LENGTH_SHORT).show();
                edt3.setText("");
            }else {
                mCursor.moveToFirst();
                String password = mCursor.getString(mCursor.getColumnIndex("password"));
                if(edt3.getText().toString().replace(" ", "").equals(password)) {
                    UserManage.setUserEmail(edt2.getText().toString().replace(" ", ""));
                    Toast.makeText(this, "登入成功", Toast.LENGTH_SHORT).show();
                    if(ProcessControl.getProcessStatus() == "Second")
                        finish();
                    else {
                        intent = new Intent(LoginActivity.this, AccoutManageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Toast.makeText(this, "密碼錯誤，請重新輸入密碼", Toast.LENGTH_SHORT).show();
                    edt3.setText("");
                }
            }

        }else
            Toast.makeText(this, "登入失敗", Toast.LENGTH_SHORT).show();
    }

    //check EditText is not Empty
    private boolean checkEditText(EditText edt){
        if(edt.getText().toString().length() != 0) {
            switch (edt.getId()){
                case R.id.editText2:
                    //關閉錯誤提示
                    edtLayout1.setErrorEnabled(false);
                    break;
                case R.id.editText3:
                    edtLayout2.setErrorEnabled(false);
                    break;
                default:
                    break;
            }

            return true;
        }else {
            switch (edt.getId()){
                case R.id.editText2:
                    //顯示錯誤提示
                    edtLayout1.setErrorEnabled(true);
                    edtLayout1.setError("不可為空");
                    break;
                case R.id.editText3:
                    edtLayout2.setErrorEnabled(true);
                    edtLayout2.setError("不可為空");
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
            edt3.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //let cursor stay the last word
            edt3.setSelection(edt3.getText().length());
            //change imageView to sleepy_eye.png
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imgVu5.setBackground(getResources().getDrawable(R.drawable.sleepy_eye));
            }
        }else {
            //hide password
            edt3.setTransformationMethod(PasswordTransformationMethod.getInstance().getInstance());
            edt3.setSelection(edt3.getText().length());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imgVu5.setBackground(getResources().getDrawable(R.drawable.eye));
            }
        }
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
        finish();
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
