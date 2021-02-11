package com.example.bancodedadossqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
@OptionsMenu( R.menu.task_menu )
public class MainActivity extends AppCompatActivity {

    private static TaskAdapter.Adapter adapter;
    private static TaskDAO taskDAO;

    @ViewById
    static RecyclerView recyclerView;

    @AfterViews
    void afterViews(){
        taskDAO = new TaskDAO(this);
        createAdapter();
        createRecyclerView();
    }

    @Click(R.id.buttonAdd)
    void addClick(){
        Intent intent = new Intent(this, EditTaskActivity_.class);
        Bundle bundle = new Bundle();

        Task task = new Task();

        bundle.putSerializable("Task", task);
        intent.putExtras(bundle);

        startActivityForResult(intent,1);
    }

    @OnActivityResult(1)
    void onActivityResult( int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Task task = (Task) data.getSerializableExtra("newTask");

            if(task.getId() == null)
                taskDAO.newTask(task);
            else
                taskDAO.update(task);

            adapter.updateList(taskDAO.taskList());
        }
    }

    void createAdapter(){
        adapter = new TaskAdapter.Adapter();
        adapter.updateList(taskDAO.taskList());
    }

    void createRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.addItemDecoration( new DividerItemDecoration( getApplicationContext(), LinearLayout.VERTICAL ) );
        recyclerView.setAdapter( adapter );
    }

    public static void deleteTask(Context context, Task task){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm");
        builder.setMessage("Delete this task?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                taskDAO.delete(task);
                adapter.updateList(taskDAO.taskList());
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}