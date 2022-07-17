package com.example.todolist.View.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnItemClickListener, TaskAdapter.OnItemDeleteListener {

    LinearLayout main_background;
    ExtendedFloatingActionButton addButton;

    TaskAdapter taskAdapter;
    RecyclerView itemView;
    List<Task> tasks;

    String format = "dd-MM-yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init_view();

        addButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                add_item_alert();
            }
        });


    }

    public void get_tasks() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "task_database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        TaskDao taskDao = db.taskDao();

        tasks = new ArrayList<>();
        tasks = taskDao.get_all_tasks();

        taskAdapter = new TaskAdapter(tasks, MainActivity.this);
        taskAdapter.setOnClickListener(this::OnItemClick, this::OnItemDelete);
        itemView.setAdapter(taskAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        EditText startText = addDialog.findViewById(R.id.startText);
        EditText endText = addDialog.findViewById(R.id.endText);
        TextView titleText = addDialog.findViewById(R.id.titleText);

        titleText.setText("Add item");

        AppCompatButton saveButton = addDialog.findViewById(R.id.saveButton);

        startText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pick_date(startText);
            }
        });

        endText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pick_date(endText);
            }
        });

        current_date(startText);
        current_date(endText);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                String taskName = taskNameText.getText().toString().trim();
                String startDate = startText.getText().toString().trim();
                String endDate = endText.getText().toString().trim();

                if (TextUtils.isEmpty(taskName)) {
                    FancyToast.makeText(getApplicationContext(), "Empty field", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                    try {
                        Date date1 = simpleDateFormat.parse(startDate);
                        Date date2 = simpleDateFormat.parse(endDate);

                        long duration = printDifference(date1, date2);

                        //Toast.makeText(MainActivity.this, String.valueOf(duration), Toast.LENGTH_SHORT).show();

                        insert_task(taskName, startDate, endDate, String.valueOf(duration));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    private void pick_date(EditText editText) {

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // String myFormat = "dd MMMM yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

                editText.setText(sdf.format(myCalendar.getTime()));
            }

        };
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void current_date(EditText editText) {
        String date = new SimpleDateFormat(format).format(new Date());
        editText.setText(date);
    }

    private void insert_task(String taskName, String startDate, String endDate, String duration) {

        //Toast.makeText(this, startDate+" "+endDate, Toast.LENGTH_SHORT).show();

        InsertThread insertThread = new InsertThread(taskName, startDate, endDate, duration, MainActivity.this);
        insertThread.start();

        get_tasks();
    }

    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        // different = different % daysInMilli;


        return elapsedDays;
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

        get_tasks();
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
        EditText startText = editDialog.findViewById(R.id.startText);
        EditText endText = editDialog.findViewById(R.id.endText);
        TextView titleText = editDialog.findViewById(R.id.titleText);

        titleText.setText("Edit item");
        taskNameText.setText(response.taskName);


        AppCompatButton saveButton = editDialog.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                String taskName = taskNameText.getText().toString().trim();
                String startDate = startText.getText().toString().trim();
                String endDate = endText.getText().toString().trim();

                if (TextUtils.isEmpty(taskName)) {
                    FancyToast.makeText(getApplicationContext(), "Empty field", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                    try {
                        Date date1 = simpleDateFormat.parse(startDate);
                        Date date2 = simpleDateFormat.parse(endDate);

                        long duration = printDifference(date1, date2);

                        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                                AppDatabase.class, "task_database").allowMainThreadQueries().build();
                        TaskDao taskDao = db.taskDao();
                        taskDao.update_task(response.task_id, taskName, String.valueOf(duration));

                        get_tasks();

                        editDialog.dismiss();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

    }

    @Override
    public void OnItemClick(int position) {

        Task response = tasks.get(position);

        //Toast.makeText(this, response.taskName, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Item_details_activity.class);
        intent.putExtra("task_id", String.valueOf(response.task_id));
        startActivity(intent);
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