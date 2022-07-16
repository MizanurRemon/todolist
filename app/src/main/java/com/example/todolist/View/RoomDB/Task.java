package com.example.todolist.View.RoomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int task_id;

    @ColumnInfo(name = "task_name")
    public String taskName;

    @ColumnInfo(name = "task_duration")
    public String taskDuration;

    public Task(String taskName, String taskDuration) {
        this.taskName = taskName;
        this.taskDuration = taskDuration;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(String taskDuration) {
        this.taskDuration = taskDuration;
    }
}
