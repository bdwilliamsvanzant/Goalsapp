
package com.example.goals.DatabaseActivities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.example.goals.Database.GoalDatabase;
import com.example.goals.Goal;
import com.example.goals.R;
import com.example.goals.Reward;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserStatsActivity  extends AppCompatActivity {

    private GoalDatabase goalDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);
        initializeViews();
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
        new RetrieveGoal(this, rootView).execute();
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

    @Override
    protected void onDestroy(){
        goalDatabase.cleanUp();
        super.onDestroy();
    }
}