package com.example.bancodedadossqlite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    lateinit var taskDAO: TaskDAO

    fun loadTasks() : MutableLiveData<List<Task>>{
        return taskDAO.loadTaskList()
    }
}