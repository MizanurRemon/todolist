package com.example.todolist.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.View.Adapter.TaskAdapter;
import com.example.todolist.View.InsertThread;
import com.example.todolist.View.RoomDB.AppDatabase;
import com.example.todolist.View.RoomDB.Task;
import com.example.todolist.View.RoomDB.TaskDao;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnItemClickListener, TaskAdapter.OnItemDeleteListener {

    LinearLayout main_background;
    ExtendedFloatingActionButton addButton;

    TaskAdapter taskAdapter;
    RecyclerView itemView;
    List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init_view();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add_item_alert();
            }
        });


        get_tasks();
    }

    public void get_tasks() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "task_database").allowMainThreadQueries().build();
        TaskDao taskDao = db.taskDao();

        tasks = new ArrayList<>();
        tasks = taskDao.get_all_tasks();

        taskAdapter = new TaskAdapter(tasks, MainActivity.this);
        taskAdapter.setOnClickListener(this::OnItemClick, this::OnItemDelete);
        itemView.setAdapter(taskAdapter);

    }

    private void add_item_alert() {
        Dialog addDialog = new Dialog(this);
        addDialog.setContentView(R.layout.add_item_alert);
        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addDialog.show();

        Window window = addDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

        EditText taskNameText = addDialog.findViewById(R.id.taskNameText);
        EditText durationText = addDialog.findViewById(R.id.durationText);
        TextView titleText = addDialog.findViewById(R.id.titleText);

        titleText.setText("Add item");

        AppCompatButton saveButton = addDialog.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = taskNameText.getText().toString().trim();
                String duration = durationText.getText().toString().trim();

                if (TextUtils.isEmpty(taskName) || TextUtils.isEmpty(duration)) {
                    FancyToast.makeText(getApplicationContext(), "Empty field", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                } else {
                    insert_task(taskName, duration);
                }
            }
        });
    }

    private void insert_task(String taskName, String duration) {

        InsertThread insertThread = new InsertThread(taskName, duration, MainActivity.this);
        insertThread.start();

        get_tasks();
    }

    private void init_view() {
        main_background = findViewById(R.id.main_background);
        addButton = findViewById(R.id.addButton);

        itemView = findViewById(R.id.itemViewID);
        itemView.setHasFixedSize(true);
        itemView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        set_mode();
    }

    private void set_mode() {

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                set_night_mode();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                set_light_mode();
                break;
        }


    }

    private void set_light_mode() {

        //Toast.makeText(this, "light", Toast.LENGTH_SHORT).show();
        main_background.setBackgroundColor(getResources().getColor(android.R.color.background_light));
    }

    private void set_night_mode() {
        //Toast.makeText(this, "night", Toast.LENGTH_SHORT).show();
        main_background.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
    }

    public void edit_function(int position) {
        Task response = tasks.get(position);
        //Toast.makeText(this, "edit " + String.valueOf(response.task_id), Toast.LENGTH_SHORT).show();

        Dialog editDialog = new Dialog(this);
        editDialog.setContentView(R.layout.add_item_alert);
        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editDialog.show();

        Window window = editDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

        EditText taskNameText = editDialog.findViewById(R.id.taskNameText);
        EditText durationText = editDialog.findViewById(R.id.durationText);
        TextView titleText = editDialog.findViewById(R.id.titleText);

        titleText.setText("Edit item");
        taskNameText.setText(response.taskName);
        durationText.setText(response.taskDuration);

        AppCompatButton saveButton = editDialog.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = taskNameText.getText().toString().trim();
                String duration = durationText.getText().toString().trim();

                if (TextUtils.isEmpty(taskName) || TextUtils.isEmpty(duration)) {
                    FancyToast.makeText(getApplicationContext(), "Empty field", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                } else {
                    AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "task_database").allowMainThreadQueries().build();
                    TaskDao taskDao = db.taskDao();
                    taskDao.update_task(response.task_id, taskName, duration);

                    get_tasks();

                    editDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void OnItemClick(int position) {

        Task response = tasks.get(position);

        Toast.makeText(this, response.taskName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemDelete(int position) {
        Task response = tasks.get(position);

        String taskID = String.valueOf(response.task_id);

        // Toast.makeText(this, taskID, Toast.LENGTH_SHORT).show();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "task_database").allowMainThreadQueries().build();
        TaskDao taskDao = db.taskDao();
        taskDao.delete_tasks(response.task_id);
        tasks.remove(position);
        taskAdapter.notifyDataSetChanged();
        itemView.setAdapter(taskAdapter);
    }


}