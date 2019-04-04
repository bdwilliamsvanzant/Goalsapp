package com.example.goals.Database;

import android.content.Context;

import com.example.goals.DAOs.GoalDao;
import com.example.goals.DAOs.RewardDao;
import com.example.goals.Goal;
import com.example.goals.Reward;
import com.example.goals.User;
import com.example.goals.util.Constants;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Goal.class, Reward.class, User.class}, version = Constants.DB_VERSION)
public abstract class GoalDatabase extends RoomDatabase {
    public abstract GoalDao getGoalDao();
    public abstract RewardDao getRewardDao();
    private static GoalDatabase goalDB;

    public static GoalDatabase getInstance(Context context){
        if(null == goalDB){
            goalDB = buildDatabaseInstance(context);
        }
        return goalDB;
    }


private static GoalDatabase buildDatabaseInstance(Context context){
    return Room.databaseBuilder(context,
            GoalDatabase.class,
            com.example.goals.util.Constants.DB_NAME).allowMainThreadQueries().build();
    }
    public void cleanUp(){
    goalDB = null;
    }
}