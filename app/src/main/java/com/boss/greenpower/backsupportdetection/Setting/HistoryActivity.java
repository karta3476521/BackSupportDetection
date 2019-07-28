package com.boss.greenpower.backsupportdetection.Setting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boss.greenpower.backsupportdetection.R;
import com.boss.greenpower.backsupportdetection.Tool.IOTool;
import com.boss.greenpower.backsupportdetection.measure.ScoreActivity;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        setScrollView();
    }


    private void setScrollView(){
        IOTool mIOTool = new IOTool("BackDetectionData/");
        List<String> names = mIOTool.getFileList();

        final LinearLayout linear = (LinearLayout)findViewById(R.id.myLinear);//取得组件
        for (String n : names){
            final Button bt = new Button(this);
            final String name = n;
            bt.setText(name);
            bt.setTextSize(18);
            bt.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                bt.setBackground(this.getResources().getDrawable(R.drawable.button_shape));
            }
            bt.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HistoryActivity.this, ScoreActivity.class);
                    intent.putExtra("path", "BackDetectionData/" + name);
                    startActivity(intent);
                }
            });

            bt.setOnLongClickListener(new Button.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    setDeleteDialog(linear, bt, name);

                    //true : longClick
                    //false : longClick + onClick
                    return true;
                }
            });

            linear.addView(bt);
        }
    }

    private void setDeleteDialog(final LinearLayout linear, final Button bt, final String name){
        // get dialog.xml view
        LayoutInflater mlayoutInflater = LayoutInflater.from(this);
        View dialogView = mlayoutInflater.inflate(R.layout.delete_dialog, null);

        Button bt1 = (Button)dialogView.findViewById(R.id.button4);
        Button bt2 = (Button)dialogView.findViewById(R.id.button10);

        final AlertDialog mAlertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IOTool mIOTool = new IOTool("BackDetectionData/" + name);
                mIOTool.deleteFile();
                linear.removeView(bt);
                mAlertDialog.cancel();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.cancel();
            }
        });

        mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlertDialog.show();
    }

    public void back_onClick(View v){
        finish();
    }
}