package com.example.android.mobitask;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import static android.content.Intent.getIntent;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by PC on 26-Jun-17.
 */

public class TaskListAdapter extends ArrayAdapter<Task>{

    private int layout;
    private DatabaseHelper myDb;

    //for holding the list items layouts views
    public class TaskListViewHolder{
        TextView listItemTV;
        Button editBtn;
        Button deleteBtn;
    }

    public TaskListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Task> objects) {
        super(context, resource, objects);

        layout = resource;
        myDb = new DatabaseHelper(getContext());
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TaskListViewHolder mainViewHolder = null;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            TaskListViewHolder viewHolder = new TaskListViewHolder();
            viewHolder.listItemTV = (TextView) convertView.findViewById(R.id.list_item_tv);
            viewHolder.listItemTV.setText(getItem(position).getName());
            viewHolder.editBtn = (Button) convertView.findViewById(R.id.list_item_edit_btn);
            viewHolder.deleteBtn = (Button) convertView.findViewById(R.id.list_item_delete_btn);

            viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View editTaskView = (LayoutInflater.from(getContext())).inflate(R.layout.edit_task_dialog, null);

                    AlertDialog.Builder editTaskAlertDialog = new AlertDialog.Builder(getContext());
                    editTaskAlertDialog.setView(editTaskView);
                    editTaskAlertDialog.setCancelable(true);

                    final TextView msgTV = (TextView) editTaskView.findViewById(R.id.msg_tv);
                    final TextView taskName = (TextView) editTaskView.findViewById(R.id.name_et);
                    final TextView taskDesc = (TextView) editTaskView.findViewById(R.id.description_et);
                    Button updateBtn = (Button) editTaskView.findViewById(R.id.update_btn);

                    final int taskId = getItem(position).getID();

                    Task t = myDb.getTaskInfo(taskId);
                    taskName.setText(t.getName());
                    taskDesc.setText(t.getDescription());

                    Dialog editTaskDialog = editTaskAlertDialog.create();
                    editTaskDialog.show();

                    updateBtn.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View v) {

                            Task updatedTask = new Task();
                            updatedTask.setID(taskId);
                            updatedTask.setName(taskName.getText().toString());
                            updatedTask.setDescription(taskDesc.getText().toString());
                            updatedTask.setDateUpdated();

                            if(updatedTask.getName().isEmpty()){
                                msgTV.setTextColor(Color.RED);
                                msgTV.setText("Please provide a valid task name!");
                            }
                            else if(myDb.updateTaskInfo(updatedTask)){
                                msgTV.setTextColor(Color.parseColor("#217421"));
                                msgTV.setText("Task Information Updated Successfully!");

                                //Update list adapter data because its updated in database
                                getItem(position).setName(updatedTask.getName());
                                getItem(position).setDescription(updatedTask.getDescription());
                                TaskListAdapter.this.notifyDataSetChanged();
                            }
                            else{
                                msgTV.setTextColor(Color.RED);
                                msgTV.setText("Update Failed! Please try again!");
                            }
                        }
                    });
                }
            });

            viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View deleteTaskView = (LayoutInflater.from(getContext())).inflate(R.layout.delete_task_dialog,null);

                    AlertDialog.Builder askToDeleteDialog = new AlertDialog.Builder(getContext());
                    askToDeleteDialog.setView(deleteTaskView);
                    askToDeleteDialog.setCancelable(true);

                    askToDeleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int taskId = getItem(position).getID();

//                            Toast.makeText(getContext(), taskId+"", Toast.LENGTH_SHORT).show();
                            if(myDb.deleteTask(taskId)){
                                Toast.makeText(getContext(), "Task deleted!", Toast.LENGTH_SHORT).show();
                                TaskListAdapter.this.remove(getItem(position)); //Remove current task item from list because its removed from db.
                            }
                            else{
                                Toast.makeText(getContext(), "Task couldn't be deleted!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    askToDeleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    Dialog deleteTaskDialog = askToDeleteDialog.create();
                    deleteTaskDialog.show();
                }
            });

            convertView.setTag(viewHolder);
        }
        else{
            mainViewHolder = (TaskListViewHolder) convertView.getTag();
            mainViewHolder.listItemTV.setText(getItem(position).getName());
        }

        return convertView;
    }


}

