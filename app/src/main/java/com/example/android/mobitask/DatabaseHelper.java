package com.example.android.mobitask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by PC on 24-Jun-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String dbName = "mobitask.db";
    private static final int dbVersion = 1;
    private static final String tableName = "tasks_table";
    private static final String id_col = "ID";
    private static final String task_name_col = "NAME";
    private static final String task_desc_col = "DESCRIPTION";
    private static final String create_date_col = "DATE_CREATED";
    private static final String update_date_col = "DATE_UPDATED";


    public DatabaseHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_tasks_query = "CREATE TABLE " + tableName + "("
                                + id_col + " INTEGER PRIMARY KEY AUTOINCREMENT, " + task_name_col + " TEXT, " + task_desc_col + " TEXT, "
                                + create_date_col  + " DATE, " + update_date_col + " DATE )";

        db.execSQL(create_tasks_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXIST" + tableName);
        onCreate(db);

    }

    public boolean insertTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(task_name_col, task.getName());
        contentValues.put(task_desc_col, task.getDescription());
        contentValues.put(create_date_col, task.getDateCreated());
        contentValues.put(update_date_col, task.getDateUpdated());

        long status = db.insert(tableName, null, contentValues);

        if(status == -1) return false;
        else return true;
    }

    public boolean deleteTask(int taskId){

        SQLiteDatabase db = this.getWritableDatabase();

        String deleteTaskQuery = "DELETE FROM "+ tableName + " WHERE ID = " + taskId;

        db.execSQL(deleteTaskQuery);

        return true;
    }

    public ArrayList<Task> getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + tableName, null);

        ArrayList taskArrayListDB = new ArrayList<Task>();

        if(result.moveToFirst()) {

            do {
                Task task = new Task();
                task.setID(Integer.parseInt(result.getString(0)));
                task.setName(result.getString(1));
                task.setDescription(result.getString(2));
                task.setDates(result.getString(3), result.getString(4));
                taskArrayListDB.add(task);
            } while (result.moveToNext());
        }

        return taskArrayListDB;
    }

    public Task getTaskInfo(int taskId){

        SQLiteDatabase db = this.getWritableDatabase();
        String selectTaskRowQuery = "SELECT * FROM " + tableName + " WHERE ID = " + taskId;
        Cursor result = db.rawQuery(selectTaskRowQuery, null);
        result.moveToFirst();

        Task task = new Task();
        task.setID(Integer.parseInt(result.getString(0)));
        task.setName(result.getString(1));
        task.setDescription(result.getString(2));
        task.setDates(result.getString(3), result.getString(4));

        return task;
    }

    public boolean updateTaskInfo(Task updatedTask) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(task_name_col, updatedTask.getName());
        contentValues.put(task_desc_col, updatedTask.getDescription());
        contentValues.put(update_date_col, updatedTask.getDateUpdated());

        int res = db.update(tableName, contentValues, id_col + " = ?", new String[] {String.valueOf(updatedTask.getID())});

        return true;
    }
}
