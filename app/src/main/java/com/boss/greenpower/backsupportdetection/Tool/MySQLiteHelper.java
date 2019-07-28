package com.boss.greenpower.backsupportdetection.Tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String databaseName = "Spring_db";
    private static final int version = 2;

    public MySQLiteHelper(Context context) {
        super(context, databaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS registers_tb (" +
                " email varchar(36) PRIMARY KEY NOT NULL," +
                " password varchar(36) NOT NULL," +
                " name varchar(36) NOT NULL," +
                " age integer NOT NULL" +
                ")");
    }

    //更新資料表
    //newVersion 必須大於oldVersion
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists registers_tb");
        //MyHelper的內容有改變，需要在onCreate()一次砍掉重建
        onCreate(db);
    }
}
