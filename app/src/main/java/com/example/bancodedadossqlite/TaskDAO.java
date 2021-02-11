package com.example.bancodedadossqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO implements ITaskDAO, Serializable {

    private SQLiteDatabase write;
    private SQLiteDatabase read;
    private ContentValues cv;

    public TaskDAO(Context context) {
        DbHelper db = new DbHelper(context);

        cv = new ContentValues();
        write = db.getWritableDatabase();
        read = db.getReadableDatabase();
    }

    @Override
    public boolean newTask(Task task) {
        try{
            cv.clear();
            cv.put("task", task.getTask());
            cv.put("card_link", task.getCardLink());
            write.insert(DbHelper.TASK_TABLE, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean update(Task task) {

        try{
            String[] args = {task.getId().toString()};
            cv.clear();
            cv.put("task", task.getTask());
            cv.put("card_link", task.getCardLink());
            write.update(DbHelper.TASK_TABLE, cv,"id = ?", args );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean delete(Task task) {
        try{
            String[] args = {task.getId().toString()};
            write.delete(DbHelper.TASK_TABLE, "id = ?", args );
            cv.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<Task> taskList() {

        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks";
        Cursor cursor = read.rawQuery(sql, null);

        while(cursor.moveToNext()){

            Task task = new Task();

            task.setId(cursor.getLong(cursor.getColumnIndex("id")));
            task.setTask(cursor.getString(cursor.getColumnIndex("task")));
            task.setCardLink(cursor.getString(cursor.getColumnIndex("card_link")));

            tasks.add(task);
        }

        return tasks;
    }


}
