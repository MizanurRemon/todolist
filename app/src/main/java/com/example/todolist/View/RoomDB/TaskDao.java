package com.example.todolist.View.RoomDB;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface TaskDao {

    @Insert
    void insertTask(Task tasks);
}
