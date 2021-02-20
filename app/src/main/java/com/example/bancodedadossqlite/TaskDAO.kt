package com.example.bancodedadossqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.MutableLiveData
import java.io.Serializable
import java.util.*

class TaskDAO(context: Context?) : ITaskDAO, Serializable {

    private var taskList = MutableLiveData<List<Task>>()
    private val write: SQLiteDatabase
    private val read: SQLiteDatabase
    private val cv: ContentValues
    override fun newTask(task: Task): Boolean {
        try {
            cv.clear()
            cv.put("task", task.task)
            cv.put("card_link", task.cardLink)
            write.insert(DbHelper.TASK_TABLE, null, cv)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        loadTaskList()
        return true
    }

    override fun update(task: Task): Boolean {
        try {
            val args = arrayOf(task.id.toString())
            cv.clear()
            cv.put("task", task.task)
            cv.put("card_link", task.cardLink)
            write.update(DbHelper.TASK_TABLE, cv, "id = ?", args)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        loadTaskList()
        return true
    }

    override fun delete(task: Task): Boolean {
        try {
            val args = arrayOf(task.id.toString())
            write.delete(DbHelper.TASK_TABLE, "id = ?", args)
            cv.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        loadTaskList()
        return true
    }

    override fun loadTaskList(): MutableLiveData<List<Task>> {
        val tasks: MutableList<Task> = ArrayList()
        val sql = "SELECT * FROM tasks"
        val cursor = read.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val task = Task()
            task.id = cursor.getLong(cursor.getColumnIndex("id"))
            task.task = cursor.getString(cursor.getColumnIndex("task"))
            task.cardLink = cursor.getString(cursor.getColumnIndex("card_link"))
            tasks.add(task)
        }
        taskList.postValue(tasks)
        return taskList
    }

    init {
        val db = DbHelper(context)
        cv = ContentValues()
        write = db.writableDatabase
        read = db.readableDatabase
    }
}