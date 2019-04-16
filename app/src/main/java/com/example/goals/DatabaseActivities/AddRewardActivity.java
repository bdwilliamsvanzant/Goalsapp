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

import com.example.goals.Database.GoalDatabase;
import com.example.goals.Reward;
import com.example.goals.R;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;


public class AddRewardActivity extends AppCompatActivity {

    private TextInputEditText et_title, et_content;
    private Spinner et_points;
    private GoalDatabase goalDatabase;
    private Reward reward;
    private boolean update;
    private int points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_reward_description);
        et_points = findViewById(R.id.difficulty_spinner);

        goalDatabase = GoalDatabase.getInstance(AddRewardActivity.this);

        Button save_butt = findViewById(R.id.but_save);
        if ((reward = (Reward) getIntent().getSerializableExtra("reward")) != null)
        {
            //getSupportActionBar().setTitle("Update Reward");
            update = true;
            //save_butt.setText(reward.getReward_name());
            et_title.setText(reward.getReward_name());
            et_content.setText(reward.getDescription());
            et_points.setSelection(reward.getPoints());
        }

        save_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update) {
                    if (et_content.getText() != null) {
                        reward.setDescription(et_content.getText().toString());
                    }
                    if (et_title.getText() != null) {
                        reward.setReward_name(et_title.getText().toString());
                    }
                    goalDatabase.getRewardDao().updateReward(reward);
                    setResult(reward, 2);
                }
                else {
                    points = (et_points.getSelectedItemPosition()+1) * 100;
                    String rewardDescription;
                    String rewardName;
                    if (et_content.getText() != null) {
                        rewardDescription = et_content.getText().toString();
                    }
                    //String content, String reward_name, int difficulty, int points, String start_time, String end_time
                    if (et_content.getText() != null && et_title.getText() != null && et_points != null) {
                        reward = new Reward(et_title.getText().toString(),
                                et_content.getText().toString(),
                                points );
                        Log.e("points", "oncreate: " + points );
                        new InsertTask(AddRewardActivity.this, reward).execute();
                    }
                }
            }

        });
        //Spinner spinner = findViewById(R.id.difficulty_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulty_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_points.setAdapter(adapter);
    }
    private void setResult(Reward reward, int flag){
        setResult(flag,new Intent().putExtra("Reward", reward));
        finish();
    }

    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<AddRewardActivity> activityReference;
        private Reward reward;

        InsertTask(AddRewardActivity context, Reward reward){
            activityReference = new WeakReference<>(context);
            this.reward = reward;
        }

        @Override
        protected Boolean doInBackground(Void... objs){
            long j = activityReference.get().goalDatabase.getRewardDao().insertReward(reward);
            reward.setReward_id(j);
            Log.e("ID", "doInBackground: " + j);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool){
            if(bool){
                activityReference.get().setResult(reward,1);
                activityReference.get().finish();
            }
        }
    }
}

