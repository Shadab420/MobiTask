package com.example.android.mobitask;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Date;

/**
 * Created by PC on 23-Jun-17.
 */

public class Task {

    private int id;
    private String name;
    private String description;
    private String dateCreated;
    private String dateUpdated;

    public Task(){}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Task(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;

        setDates();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Task(String name, String description){
        this.name = name;
        this.description = description;

        setDates();
    }

    public void setID(int id){
        this .id = id;
    }

    public int getID(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDates(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(cal.getTime());

        this.dateCreated = formattedDate;
        this.dateUpdated = formattedDate;
    }

    public void setDates(String dateCreated, String dateUpdated){
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public String getDateCreated(){
        return dateCreated;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDateUpdated(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(cal.getTime());

        this.dateUpdated = formattedDate;
    }

    public String getDateUpdated(){
        return dateUpdated;
    }
}
