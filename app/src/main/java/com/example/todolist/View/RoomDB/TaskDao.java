package com.example.todolist.View.RoomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    List<Task> get_all_tasks();

    @Query("SELECT * FROM task WHERE task_id = :taskID")
    Task get_specific_tasks(int taskID);

    @Insert
    void insertTask(Task tasks);

    @Query("DELETE FROM task WHERE task_id= :id")
    void delete_tasks(int id);

    @Query("UPDATE task SET task_name = :task_name,start_date = :startDate, end_date = :endDate, task_duration= :task_duration WHERE task_id = :id")
    void update_task(int id, String task_name,String startDate, String endDate, String task_duration);
}
