package com.example.goals.DatabaseActivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.goals.Database.GoalDatabase;
import com.example.goals.Goal;
import com.example.goals.R;
import com.example.goals.Reward;
import com.example.goals.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserStatsActivity  extends AppCompatActivity {

    private GoalDatabase goalDatabase;

    private String longToString(long temp)
    {
        Date time = new Date(temp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return (Integer.toString(cal.get(Calendar.MONTH)+1) +"/" + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(cal.get(Calendar.YEAR)));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);
        initializeViews();
         BottomNavigationView nav = findViewById(R.id.navigation);
        Menu menu = nav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
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
        nav.setSelectedItemId(R.id.navigation_stats);
        display();

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayOptions(actionBar.DISPLAY_SHOW_CUSTOM);
            View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
            actionBar.setCustomView(cView);

        }
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void display() {
        //find most recently created goal
        goalDatabase = GoalDatabase.getInstance(UserStatsActivity.this);
        View rootView = getWindow().getDecorView().getRootView();

        //get # active goals
        new RetrieveActiveGoals(this, rootView).execute();
        //get # completed goals
        new RetrieveCompletedGoals(this, rootView).execute();

        //get # unearned rewards
        new RetrieveUnearnedRewards(this, rootView).execute();
        //get # Earned rewards
        new RetrieveEarnedRewards(this, rootView).execute();

        //get total current points
        new RetrieveTotalCurrentPoints(this, rootView).execute();
        //get total points spent
        new RetrieveSpentPoints(this, rootView).execute();

        //most recently completed goal
        new RetrieveGoal(this, rootView).execute();

        //most recently completed reward
        new RetrieveReward(this, rootView).execute();



    }

    private static class RetrieveGoal extends AsyncTask<Void, Void, Goal> {
        private WeakReference<UserStatsActivity> activityReference;
        private View rootView;

        private String longToString(long temp)
        {
            Date time = new Date(temp);
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            return (Integer.toString(cal.get(Calendar.MONTH)+1) +"/" + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(cal.get(Calendar.YEAR)));
        }


        public RetrieveGoal(UserStatsActivity context, View rootView){
            activityReference = new WeakReference<>(context);
            this.rootView=rootView;
        }

        @Override
        protected Goal doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().goalDatabase.getGoalDao().getMostRecentGoal();
            else
                return null;
        }

        @Override
        protected void onPostExecute(Goal goal) {

            if (goal != null ) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY", Locale.US);

                //set UI to goal values
                TextView goalTitle = rootView.findViewById(R.id.goal_name);
                goalTitle.setText(Constants.LABEL_GOAL_NAME + goal.getGoal_name());

                TextView goalDesc = rootView.findViewById(R.id.item_text);
                goalDesc.setText(Constants.LABEL_GOAL_DESCRIPTION + goal.getContent());

                TextView goalPoints = rootView.findViewById(R.id.tv_goal_points);
                goalPoints.setText(Constants.LABEL_POINTS + goal.getPoints());

                TextView goalStart = rootView.findViewById(R.id.tv_goal_startTime);
                goalStart.setText(Constants.LABEL_START_DATE + dateFormat.format(goal.getStart_time()));

                TextView goalEnd = rootView.findViewById(R.id.tv_goal_endTime);
                goalEnd.setText(Constants.LABEL_END_DATE + dateFormat.format(goal.getEnd_time()));

            }
        }
    }

    private static class RetrieveReward extends AsyncTask<Void, Void, Reward> {
        private WeakReference<UserStatsActivity> activityReference;
        private View rootView;


        public RetrieveReward(UserStatsActivity context, View rootView){
            activityReference = new WeakReference<>(context);
            this.rootView=rootView;
        }

        @Override
        protected Reward doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().goalDatabase.getRewardDao().getMostRecentReward();
            else
                return null;
        }

        @Override
        protected void onPostExecute(Reward reward) {
            if (reward != null ) {
                //set UI to reward values
                TextView rewardTitle = rootView.findViewById(R.id.reward_name);
                rewardTitle.setText(Constants.LABEL_REWARD_NAME + reward.getReward_name());

                TextView rewardDesc = rootView.findViewById(R.id.reward_text);
                rewardDesc.setText(Constants.LABEL_REWARD_DESCRIPTION + reward.getDescription());

                TextView rewardPoints = rootView.findViewById(R.id.tv_reward_points);
                rewardPoints.setText(Constants.LABEL_REWARD_POINTS + reward.getPoints());

            }
        }
    }

    private static class RetrieveActiveGoals extends AsyncTask<Void, Void, Integer> {
        private WeakReference<UserStatsActivity> activityReference;
        private View rootView;


        public RetrieveActiveGoals(UserStatsActivity context, View rootView){
            activityReference = new WeakReference<>(context);
            this.rootView=rootView;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Date temp= new Date();
            if (activityReference.get() != null)
                return activityReference.get().goalDatabase.getGoalDao().getActiveGoals(temp.getTime());
            else
                return 0;
        }

        @Override
        protected void onPostExecute(Integer numActiveGoals) {


            //set UI to goal values
            TextView numActiveGoalsTextView = rootView.findViewById(R.id.total_active_goals);

            numActiveGoalsTextView.setText( "Active Goals: "+ numActiveGoals);

        }
    }

    private static class RetrieveCompletedGoals extends AsyncTask<Void, Void, Integer> {
        private WeakReference<UserStatsActivity> activityReference;
        private View rootView;


        public RetrieveCompletedGoals(UserStatsActivity context, View rootView){
            activityReference = new WeakReference<>(context);
            this.rootView=rootView;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().goalDatabase.getGoalDao().getCompletedGoals();
            else
                return 0;
        }

        @Override
        protected void onPostExecute(Integer numCompletedGoals) {


            //set UI to goal values
            TextView numActiveGoalsTextView = rootView.findViewById(R.id.total_completed_goals);

            numActiveGoalsTextView.setText( "Completed Goals: "+ numCompletedGoals);

        }
    }

    private static class RetrieveUnearnedRewards extends AsyncTask<Void, Void, Integer> {
        private WeakReference<UserStatsActivity> activityReference;
        private View rootView;


        public RetrieveUnearnedRewards(UserStatsActivity context, View rootView){
            activityReference = new WeakReference<>(context);
            this.rootView=rootView;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().goalDatabase.getRewardDao().getUnearnedRewards();
            else
                return 0;
        }

        @Override
        protected void onPostExecute(Integer numUnearnedRewards) {


            //set UI to goal values
            TextView numActiveGoalsTextView = rootView.findViewById(R.id.total_unearned_rewards);

            numActiveGoalsTextView.setText( "Unearned Rewards: "+ numUnearnedRewards);

        }
    }

    private static class RetrieveEarnedRewards extends AsyncTask<Void, Void, Integer> {
        private WeakReference<UserStatsActivity> activityReference;
        private View rootView;


        public RetrieveEarnedRewards(UserStatsActivity context, View rootView){
            activityReference = new WeakReference<>(context);
            this.rootView=rootView;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().goalDatabase.getRewardDao().getEarnedRewards();
            else
                return 0;
        }

        @Override
        protected void onPostExecute(Integer numEarnedRewards) {


            //set UI to goal values
            TextView numActiveGoalsTextView = rootView.findViewById(R.id.total_earned_rewards);

            numActiveGoalsTextView.setText( "Earned Rewards: "+ numEarnedRewards);

        }
    }

    private static class RetrieveTotalCurrentPoints extends AsyncTask<Void, Void, Integer> {
        private WeakReference<UserStatsActivity> activityReference;
        private View rootView;


        public RetrieveTotalCurrentPoints(UserStatsActivity context, View rootView){
            activityReference = new WeakReference<>(context);
            this.rootView=rootView;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            if (activityReference.get() != null){

                Integer totalPointsEarned = activityReference.get().goalDatabase.getGoalDao().getTotalPointsEarned();
                return totalPointsEarned - activityReference.get().goalDatabase.getRewardDao().getSpentPoints();
            }
            else
                return 0;
        }

        @Override
        protected void onPostExecute(Integer currentPoints) {


            //set UI to goal values
            TextView numActiveGoalsTextView = rootView.findViewById(R.id.total_current_points);

            numActiveGoalsTextView.setText( "Total Current Points: "+ currentPoints);

        }
    }

    private static class RetrieveSpentPoints extends AsyncTask<Void, Void, Integer> {
        private WeakReference<UserStatsActivity> activityReference;
        private View rootView;


        public RetrieveSpentPoints(UserStatsActivity context, View rootView){
            activityReference = new WeakReference<>(context);
            this.rootView=rootView;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            if (activityReference.get() != null){

                return activityReference.get().goalDatabase.getRewardDao().getSpentPoints();
            }
            else
                return 0;
        }

        @Override
        protected void onPostExecute(Integer spentPoints) {


            //set UI to goal values
            TextView numActiveGoalsTextView = rootView.findViewById(R.id.total_points_spent);

            numActiveGoalsTextView.setText( "Total Spent Points: "+ spentPoints);

        }
    }




    @Override
    protected void onDestroy(){
        goalDatabase.cleanUp();
        super.onDestroy();
    }
}