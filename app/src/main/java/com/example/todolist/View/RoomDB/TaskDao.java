package com.example.todolist.View.RoomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    List<Task> get_all_tasks();

    @Insert
    void insertTask(Task tasks);
}
