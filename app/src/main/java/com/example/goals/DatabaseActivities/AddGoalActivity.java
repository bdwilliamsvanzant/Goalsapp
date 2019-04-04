package com.example.goals.DatabaseActivities;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.goals.Goal;
import com.example.goals.Database.GoalDatabase;
import com.example.goals.R;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;



public class AddGoalActivity extends AppCompatActivity {

    private TextInputEditText et_title, et_content, et_startDate, et_endDate;
    private DatePickerDialog datepicker;
    private GoalDatabase goalDatabase;
    private Goal goal;
    private boolean update;
    private int points;
    private Spinner difficulty;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        et_startDate = findViewById(R.id.et_startDate);
        et_endDate = findViewById(R.id.et_endDate);
        difficulty = findViewById(R.id.difficulty_spinner);

        goalDatabase = GoalDatabase.getInstance(AddGoalActivity.this);

        Button save_butt = findViewById(R.id.but_save);
        if( (goal = (Goal) getIntent().getSerializableExtra("goal"))!=null)
        {
            update = true;
            et_title.setText(goal.getGoal_name());
            et_content.setText(goal.getContent());
            et_startDate.setText(goal.getStart_time());
            et_endDate.setText(goal.getEnd_time());
            difficulty.setSelection(goal.getDifficulty());
        }

        save_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update) {
                    if (et_content.getText() != null) {
                        goal.setContent(et_content.getText().toString());
                    }
                    if (et_title.getText() != null) {
                        goal.setGoal_name(et_title.getText().toString());
                    }
                    if (et_startDate.getText() != null) {
                        goal.setStart_time(et_startDate.getText().toString());
                    }
                    if (et_endDate.getText() != null) {
                        goal.setEnd_time(et_endDate.getText().toString());
                    }
                    goalDatabase.getGoalDao().updateGoal(goal);
                    setResult(goal, 2);
                } else {
                    points = difficulty.getSelectedItemPosition() * 100;

                    //String content, String goal_name, int difficulty, int points, String start_time, String end_time

                    if (et_content.getText() != null && et_title.getText() != null && et_startDate.getText() != null && et_endDate.getText() != null) {
                        goal = new Goal(et_content.getText().toString(),
                                et_title.getText().toString(),
                                difficulty.getSelectedItemPosition(),
                                points,
                                et_startDate.getText().toString(),
                                et_endDate.getText().toString());
                        new InsertTask(AddGoalActivity.this, goal).execute();
                    }
                }
            }

        });

        final Button startDate_butt = findViewById(R.id.startDatebutton);
        Button endDate_butt = findViewById(R.id.endDateButton);

        startDate_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                datepicker = new DatePickerDialog(AddGoalActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        et_startDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datepicker.show();
            }
        });

        endDate_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                datepicker = new DatePickerDialog(AddGoalActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        et_endDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datepicker.show();
            }
        });

        Spinner spinner = findViewById(R.id.difficulty_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulty_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }

    private void setResult(Goal goal, int flag){
        setResult(flag, new Intent().putExtra("Goal", goal));
        finish();
    }

    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<AddGoalActivity> activityReference;
        private Goal goal;

        InsertTask(AddGoalActivity context, Goal goal){
            activityReference = new WeakReference<>(context);
            this.goal = goal;
        }

        @Override
        protected Boolean doInBackground(Void... objs){
            long j = activityReference.get().goalDatabase.getGoalDao().insertGoal(goal);
            goal.setGoal_id(j);
            Log.e("ID", "doInBackground: " + j);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool){
            if(bool){
                activityReference.get().setResult(goal,1);
                activityReference.get().finish();
            }
        }
    }
}

