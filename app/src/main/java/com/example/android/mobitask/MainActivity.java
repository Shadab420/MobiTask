package com.example.android.mobitask;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button createTask;
    private Button taskList;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        createTask = (Button) findViewById(R.id.create_task_btn);
        taskList = (Button) findViewById(R.id.task_list_btn);

        //Create and insert task

        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View createTaskView = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.create_task_dialog, null);

                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setView(createTaskView);

                alertBuilder.setCancelable(true);

                Button createBtn = (Button) createTaskView.findViewById(R.id.create_btn);
                final TextView msgTV = (TextView) createTaskView.findViewById(R.id.msg_tv);

                createBtn.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        EditText taskName = (EditText) createTaskView.findViewById(R.id.name_et);
                        EditText taskDesc = (EditText) createTaskView.findViewById(R.id.description_et);

                        Task newTask = new Task(taskName.getText().toString(), taskDesc.getText().toString());

                        if(taskName.getText().toString().isEmpty()){
                            msgTV.setTextColor(Color.RED);
                            msgTV.setText("Please provide a proper task name!");
                        }

                        else if(myDb.insertTask(newTask)){
                            msgTV.setTextColor(Color.parseColor("#217421"));
                            msgTV.setText("Task Inserted");

                        }

                        else{
                            msgTV.setTextColor(Color.RED);
                            msgTV.setText("Task not Inserted");
                        }
                    }
                });

                Dialog createTaskDialog = alertBuilder.create();
                createTaskDialog.show();

            }
        });


        //Get task list

        taskList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Task> taskListDB = myDb.getAllData();

                if(taskListDB.size() == 0){
                    Toast.makeText(MainActivity.this, "No data is available!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
