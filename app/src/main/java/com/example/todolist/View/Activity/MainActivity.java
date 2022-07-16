package com.example.todolist.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.View.RoomDB.AppDatabase;
import com.example.todolist.View.RoomDB.Task;
import com.example.todolist.View.RoomDB.TaskDao;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity {

    LinearLayout main_background;
    ExtendedFloatingActionButton addButton;

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

        InsertThread insertThread = new InsertThread(taskName, duration);
        insertThread.start();
    }

    private void init_view() {
        main_background = findViewById(R.id.main_background);
        addButton = findViewById(R.id.addButton);
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

    class InsertThread extends Thread {

        final String taskName, duration;

        public InsertThread(String taskName, String duration) {
            // store parameter for later user
            this.taskName = taskName;
            this.duration = duration;
        }

        public void run() {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "task_database").build();

            TaskDao taskDao = db.taskDao();
            taskDao.insertTask(new Task(taskName, duration));

            //FancyToast.makeText(getApplicationContext(), "inserted", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

        }

    }
}