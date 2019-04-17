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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserStatsActivity  extends AppCompatActivity {

    private GoalDatabase goalDatabase;



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
                //set UI to goal values
                TextView goalTextView = rootView.findViewById(R.id.goal_info);

                goalTextView.setText(goal.getGoal_name()+"\n"
                        +goal.getContent() +"\n"
                        +goal.getDifficulty()+"\n"
                        +goal.getPoints()+"\n"
                        +goal.getStart_time()+"\n"
                        +goal.getEnd_time()
                );
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
                TextView rewardTextView = rootView.findViewById(R.id.reward_info);

                rewardTextView.setText(reward.getReward_name()+"\n"
                        +reward.getDescription()+"\n"
                        +reward.getPoints()
                );
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
            if (activityReference.get() != null)
                return activityReference.get().goalDatabase.getGoalDao().getActiveGoals();
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