package com.boss.greenpower.backsupportdetection.Setting;

import android.content.ContentValues;
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
import android.widget.TextView;
import android.widget.Toast;

import com.boss.greenpower.backsupportdetection.R;
import com.boss.greenpower.backsupportdetection.Tool.MySQLiteHelper;
import com.boss.greenpower.backsupportdetection.Tool.MyUtils;
import com.boss.greenpower.backsupportdetection.Tool.UserTool.UserManage;

public class ModifyImformationActivity extends AppCompatActivity {
    EditText edt8, edt9, edt10, edt11;
    TextInputLayout edtLayout7, edtLayout8, edtLayout9, edtLayout10;
    TextView tv19;
    ImageView imgVu6, imgVu7;
    SQLiteDatabase db;
    boolean isVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_imformation);
        getSupportActionBar().hide();

        edt8 = (EditText)findViewById(R.id.editText8);
        edt9 = (EditText)findViewById(R.id.editText9);
        edt10 = (EditText)findViewById(R.id.editText10);
        edt11 = (EditText)findViewById(R.id.editText11);
        edtLayout7 = (TextInputLayout)findViewById(R.id.textInputLayout7);
        edtLayout8 = (TextInputLayout)findViewById(R.id.textInputLayout8);
        edtLayout9 = (TextInputLayout)findViewById(R.id.textInputLayout9);
        edtLayout10 = (TextInputLayout)findViewById(R.id.textInputLayout10);
        imgVu6 = (ImageView)findViewById(R.id.imageView6);
        imgVu7 = (ImageView)findViewById(R.id.imageView7);
        tv19 = (TextView)findViewById(R.id.textView19);
        tv19.setText(UserManage.getUserEmail());
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

    public void back_onClick(View v){
        finish();
    }

    public void confirm_onClick(View v){
        boolean isHaveValue = false;
        ContentValues mContentValues = new ContentValues();
        setAllErrorClear();

        if(edt8.getText().toString().length() > 0 || edt9.getText().toString().length() > 0){
            if(edt8.getText().toString().length() > 0 && edt9.getText().toString().length() > 0) {
                if (edt8.getText().toString().equals(edt9.getText().toString()))
                    mContentValues.put("password", edt8.getText().toString().replace(" ", ""));
                else {
                    //錯誤提示
                    setErrorText(edtLayout8, "兩次密碼需相同");
                }
            }else {
                if(edt8.getText().toString().length() == 0)
                    setErrorText(edtLayout7, "不可為空");
                else
                    setErrorText(edtLayout8, "不可為空");
            }
            isHaveValue = true;
        }
        if(edt10.getText().toString().length() > 0)
            mContentValues.put("name", edt10.getText().toString().trim());
        if(edt11.getText().toString().length() > 0)
            mContentValues.put("name", Integer.parseInt(edt11.getText().toString().replace(" ", "")));

        if(!isHaveValue && mContentValues.size() == 0){
            //全部都沒有輸入
            setAllError();
        }

        if(mContentValues.size() != 0){
            String qry = "email='" + UserManage.getUserEmail() + "'";
            int count = db.update("registers_tb", mContentValues, qry, null);

            if(count > 0)
                Toast.makeText(this, "資料已更新", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "更新失敗", Toast.LENGTH_SHORT).show();
        }
    }

    //顯示錯誤提示
    private void setErrorText(TextInputLayout edtLayout, String errText){
        edtLayout.setErrorEnabled(true);
        edtLayout.setError(errText);
    }

    private void setAllError(){
        setErrorText(edtLayout7, "請至少修改一個欄位");
        setErrorText(edtLayout8, "請至少修改一個欄位");
        setErrorText(edtLayout9, "請至少修改一個欄位");
        setErrorText(edtLayout10, "請至少修改一個欄位");
    }

    private void setAllErrorClear(){
        edtLayout7.setErrorEnabled(false);
        edtLayout8.setErrorEnabled(false);
        edtLayout9.setErrorEnabled(false);
        edtLayout10.setErrorEnabled(false);
    }

    //show & hide password text
    public void passwordVisible_onClick(View v){
        isVisible = !isVisible;
        if(isVisible) {
            //show password
            edt8.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edt9.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //let cursor stay the last word
            edt8.setSelection(edt8.getText().length());
            edt9.setSelection(edt9.getText().length());
            //change imageView to sleepy_eye.png
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imgVu6.setBackground(getResources().getDrawable(R.drawable.sleepy_eye));
                imgVu7.setBackground(getResources().getDrawable(R.drawable.sleepy_eye));
            }
        }else {
            //hide password
            edt8.setTransformationMethod(PasswordTransformationMethod.getInstance().getInstance());
            edt9.setTransformationMethod(PasswordTransformationMethod.getInstance().getInstance());
            edt8.setSelection(edt8.getText().length());
            edt9.setSelection(edt9.getText().length());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imgVu6.setBackground(getResources().getDrawable(R.drawable.eye));
                imgVu7.setBackground(getResources().getDrawable(R.drawable.eye));
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
