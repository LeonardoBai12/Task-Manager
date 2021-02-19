package com.example.bancodedadossqlite

import android.app.Activity
import android.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_addedit)
open class EditTaskActivity : Activity() {

    @ViewById
    lateinit var inputTask: TextInputLayout

    @ViewById
    lateinit var inputCardLink: TextInputLayout

    @Extra("Task")
    lateinit var task: Task

    @AfterViews
    fun afterViews() {
        inputTask.editText?.setText(task.task)
        inputCardLink.editText?.setText(task.cardLink)
    }

    @Click(R.id.buttonConfirm)
    fun onConfirmClick() {
        task.task = inputTask.editText?.text.toString()
        task.cardLink = inputCardLink.editText?.text.toString()
        intent.putExtra("newTask", task)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        super.onBackPressed()
    }
}