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

    @Query("DELETE FROM task WHERE task_id= :id")
    void delete_tasks(int id);
}
