package com.example.todolist.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.View.RoomDB.AppDatabase;
import com.example.todolist.View.RoomDB.Task;
import com.example.todolist.View.RoomDB.TaskDao;

public class Item_details_activity extends AppCompatActivity {

    ImageView backButton;
    LinearLayout main_background;
    TextView titleText, durationText;
    String taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        init_view();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        get_item();


    }

    private void get_item() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "task_database").allowMainThreadQueries().build();
        TaskDao taskDao = db.taskDao();
        Task response = taskDao.get_specific_tasks(Integer.parseInt(taskID));

        //Toast.makeText(this, response.taskName, Toast.LENGTH_SHORT).show();
        titleText.setText(response.taskName);
        durationText.setText("Duration: "+response.taskDuration+" days");
    }

    private void init_view() {
        backButton = findViewById(R.id.backButton);
        main_background = findViewById(R.id.main_background);
        titleText = findViewById(R.id.titleText);
        durationText = findViewById(R.id.durationText);
        taskID = getIntent().getStringExtra("task_id");
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


}