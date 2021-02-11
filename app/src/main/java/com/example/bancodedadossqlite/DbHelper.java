package com.example.bancodedadossqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NAME_DB = "TaskDataBase";
    public static String TASK_TABLE = "tasks";

    public DbHelper(@Nullable Context context) {
        super(context, NAME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS tasks  (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "task TEXT," +
                "card_link TEXT," +
                "concluded TEXT," +
                "date TEXT" +
                ") ";

        try{
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        String sql = "";
//
//        try{
//            db.execSQL(sql);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
