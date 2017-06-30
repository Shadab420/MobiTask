package com.example.android.mobitask;

import android.app.Dialog;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class TaskListActivity extends AppCompatActivity {

    private ListView taskListView;
    private ArrayList<Task> taskArrayList;
    private ArrayAdapter<Task> taskArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        taskListView = (ListView) findViewById(R.id.task_list_view);
//        taskArrayAdapter = new ArrayAdapter<Task>(this, R.layout.task_list, taskArrayList);
        generateTaskList();
    }

    private void generateTaskList(){
        final DatabaseHelper myDb = new DatabaseHelper(this);

        taskArrayList = myDb.getAllData();



//        for(Task t : taskArrayList){
//            Toast.makeText(this, Integer.toString(t.getID()), Toast.LENGTH_SHORT).show();
//        }

        taskArrayAdapter = new TaskListAdapter(this, R.layout.task_list, taskArrayList);
        taskArrayAdapter.notifyDataSetChanged();

        taskListView.setAdapter(taskArrayAdapter);
        taskListView.setItemsCanFocus(false);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View taskInfoView = (LayoutInflater.from(TaskListActivity.this)).inflate(R.layout.task_info_display_dialog,null);

                AlertDialog.Builder taskInfoAlertDialog = new AlertDialog.Builder(TaskListActivity.this);
                taskInfoAlertDialog.setView(taskInfoView);
                taskInfoAlertDialog.setCancelable(true);

                int taskId = taskArrayAdapter.getItem(position).getID();
                Task selectedTask = myDb.getTaskInfo(taskId);

                TextView taskName = (TextView) taskInfoView.findViewById(R.id.task_name);
                TextView taskDesc = (TextView) taskInfoView.findViewById(R.id.task_desc);
                TextView createDate = (TextView) taskInfoView.findViewById(R.id.task_create_date);
                TextView updateDate = (TextView) taskInfoView.findViewById(R.id.task_update_date);

                taskName.setText("Name: " + selectedTask.getName());
                taskDesc.setText("Description: " + selectedTask.getDescription());
                createDate.setText("Entry Date: " + selectedTask.getDateCreated());
                updateDate.setText("Last Updated: " + selectedTask.getDateUpdated());

                Dialog taskInfoDialog = taskInfoAlertDialog.create();
                taskInfoDialog.show();
            }
        });


    }
}
