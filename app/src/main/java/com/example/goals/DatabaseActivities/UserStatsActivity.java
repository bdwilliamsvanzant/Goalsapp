package com.example.goals.DatabaseActivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.goals.Database.GoalDatabase;
import com.example.goals.Goal;
import com.example.goals.GoalsAdapter;
import com.example.goals.R;
import com.example.goals.Reward;
import com.example.goals.UserStatsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserStatsActivity  extends AppCompatActivity {

    private TextView textViewMsg;
    private RecyclerView recyclerView;
    private GoalDatabase goalDatabase;

    private List<Goal> goals;
    private Goal mostRecentGoal;
    private List<Reward> rewards;
    private Reward mostRecentReward;

    private UserStatsAdapter userStatsAdapter;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);
        initializeViews();
        BottomNavigationView nav = findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_goals:
                        Intent intent0 = new Intent(UserStatsActivity.this, GoalListActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.navigation_rewards:
                        Intent intent1 = new Intent(UserStatsActivity.this, RewardListActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_stats:
                        break;
                }
                return false;
            }
        });
        display();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewMsg = findViewById(R.id.goal_count);

        recyclerView = findViewById(R.id.goalRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserStatsActivity.this));

        goals = new ArrayList<>();
        userStatsAdapter = new UserStatsAdapter(goals, UserStatsActivity.this);

        recyclerView.setAdapter(userStatsAdapter);


    }

    private static class RetrieveTask extends AsyncTask<Void, Void, List<Goal>> {
        private WeakReference<UserStatsActivity> activityReference;

        RetrieveTask(UserStatsActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Goal> doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().goalDatabase.getGoalDao().getGoals();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Goal> goals) {
            if (goals != null && goals.size() > 0) {
                activityReference.get().goals.clear();
                activityReference.get().goals.addAll(goals);

                activityReference.get().textViewMsg.setVisibility(View.GONE);

            }
        }
    }

    private void display() {
        //retrieve all goals
        goalDatabase = GoalDatabase.getInstance(UserStatsActivity.this);
        new UserStatsActivity.RetrieveTask(this).execute();

        //find most recently created goal


        //retrieve all rewards

        //find most recently achieved reward

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode > 0) {
            if (resultCode == 1) {
                goals.add((Goal) data.getSerializableExtra("goal"));
            } else if (resultCode == 2) {
                goals.set(pos, (Goal) data.getSerializableExtra("goal"));
            }
        }
        listVisibility();
    }

    private void listVisibility(){
        int emptyMsgVisibility = View.GONE;
        if(goals.size() == 0){
            if(textViewMsg.getVisibility() == View.GONE)
                emptyMsgVisibility = View.VISIBLE;
        }
        textViewMsg.setVisibility(emptyMsgVisibility);
        userStatsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy(){
        goalDatabase.cleanUp();
        super.onDestroy();
    }
}

