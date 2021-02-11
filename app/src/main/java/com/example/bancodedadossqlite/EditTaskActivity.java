package com.example.bancodedadossqlite;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_addedit)
public class EditTaskActivity extends Activity {

    private Toolbar toolbar;

    @ViewById
    TextInputLayout inputTask;

    @ViewById
    TextInputLayout inputCardLink;

    @Extra("Task")
    Task task;

    @AfterViews
    void afterViews(){
        inputTask.getEditText().setText(task.getTask());
        inputCardLink.getEditText().setText(task.getCardLink());
    }

    @Click(R.id.buttonConfirm)
    void onConfirmClick() {
        task.setTask( inputTask.getEditText().getText().toString() );
        task.setCardLink( inputCardLink.getEditText().getText().toString() );

        Intent intent = getIntent();
        intent.putExtra("newTask", task);
        setResult(RESULT_OK,intent);

        onBackPressed();
    }



}
