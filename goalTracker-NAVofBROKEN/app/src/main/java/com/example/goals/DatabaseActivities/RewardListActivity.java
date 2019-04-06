package com.example.goals.DatabaseActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.goals.Database.GoalDatabase;
import com.example.goals.Reward;
import com.example.goals.RewardsAdapter;
import com.example.goals.R;
import com.example.goals.DatabaseActivities.AddRewardActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RewardListActivity extends AppCompatActivity implements RewardsAdapter.OnRewardItemClick {

    private TextView textViewMsg;
    private RecyclerView recyclerView;
    private GoalDatabase GoalDatabase;
    private List<Reward> Rewards;
    private RewardsAdapter RewardsAdapter;
    private int pos;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(RewardListActivity.this, AddRewardActivity.class), 100);
        }
    };

    private void displayList() {
        GoalDatabase = GoalDatabase.getInstance(RewardListActivity.this);
        new RetrieveTask(this).execute();
    }

    private static class RetrieveTask extends AsyncTask<Void, Void, List<Reward>> {
        private WeakReference<RewardListActivity> activityReference;

        RetrieveTask(RewardListActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Reward> doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().GoalDatabase.getRewardDao().getRewards();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Reward> Rewards) {
            if (Rewards != null && Rewards.size() > 0) {
                activityReference.get().Rewards.clear();
                activityReference.get().Rewards.addAll(Rewards);

                activityReference.get().textViewMsg.setVisibility(View.GONE);
                activityReference.get().RewardsAdapter.notifyDataSetChanged();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        initializeViews();
        BottomNavigationView nav = findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_goals:
                        Intent intent0 = new Intent(RewardListActivity.this, GoalListActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.navigation_rewards:
                        break;
                    case R.id.navigation_stats:
                        Intent intent2 = new Intent(RewardListActivity.this, UserStatsActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
        displayList();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //textViewMsg = findViewById(R.id.Reward_count);

        Button add_but = findViewById(R.id.but_add);
        add_but.setOnClickListener(listener);
        recyclerView = findViewById(R.id.rewardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(RewardListActivity.this));
        Rewards = new ArrayList<>();
        RewardsAdapter = new RewardsAdapter(Rewards, RewardListActivity.this);
        recyclerView.setAdapter(RewardsAdapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode > 0) {
            if (resultCode == 1) {
                Rewards.add((Reward) data.getSerializableExtra("Reward"));
            } else if (resultCode == 2) {
                Rewards.set(pos, (Reward) data.getSerializableExtra("Reward"));
            }
            listVisibility();
            displayList();
        }
    }

    @Override
    public void onRewardClick(final int pos) {
        new AlertDialog.Builder(RewardListActivity.this)
                .setTitle("Select Options")
                .setItems(new String[]{"Delete", "Update"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                GoalDatabase.getRewardDao().deleteReward(Rewards.get(pos));
                                Rewards.remove(pos);
                                listVisibility();
                                break;
                            case 1:
                                RewardListActivity.this.pos = pos;
                                startActivityForResult(
                                        new Intent(RewardListActivity.this, AddRewardActivity.class).putExtra("Reward", Rewards.get(pos).toString()), 100);
                                break;
                        }
                    }
                }).show();
    }

    private void listVisibility(){
        int emptyMsgVisibility = View.GONE;
        if(Rewards.size() == 0){
            if(textViewMsg.getVisibility() == View.GONE)
                emptyMsgVisibility = View.VISIBLE;
        }
        textViewMsg.setVisibility(emptyMsgVisibility);
        RewardsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy(){
        GoalDatabase.cleanUp();
        super.onDestroy();
    }
}