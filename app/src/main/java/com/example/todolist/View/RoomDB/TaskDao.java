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

    @Query("UPDATE task SET task_name = :task_name, task_duration= :task_duration WHERE task_id = :id")
    void update_task(int id, String task_name, String task_duration);
}
