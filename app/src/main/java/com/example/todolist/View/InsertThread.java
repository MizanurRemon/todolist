package com.example.todolist.View;

import android.content.Context;

import androidx.room.Room;

import com.example.todolist.View.Activity.MainActivity;
import com.example.todolist.View.RoomDB.AppDatabase;
import com.example.todolist.View.RoomDB.Task;
import com.example.todolist.View.RoomDB.TaskDao;

public class InsertThread extends Thread {

    String taskName, duration, startDate, endDate;
    Context context;
    MainActivity mainActivity;

    public InsertThread(String taskName, String startDate, String endDate, String duration, MainActivity mainActivity) {
        // store parameter for later user
        this.taskName = taskName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.mainActivity = mainActivity;
    }

    public void run() {
        AppDatabase db = Room.databaseBuilder(mainActivity.getApplicationContext(),
                AppDatabase.class, "task_database").fallbackToDestructiveMigration().build();

        TaskDao taskDao = db.taskDao();
        taskDao.insertTask(new Task(taskName, startDate, endDate, duration));

        //mainActivity.get_tasks();
        //FancyToast.makeText(getApplicationContext(), "inserted", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
    }

}
