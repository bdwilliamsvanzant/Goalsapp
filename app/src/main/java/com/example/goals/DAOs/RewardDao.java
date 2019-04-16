package com.example.goals.DAOs;

import com.example.goals.Goal;
import com.example.goals.Reward;
import com.example.goals.util.Constants;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RewardDao {

    @Query("SELECT * FROM " + Constants.TABLE_NAME_REWARD + "\n WHERE `End Date` = (SELECT MAX(`End Date`) FROM "+ Constants.TABLE_NAME_REWARD + ");" )
    Reward getMostRecentReward();

    @Query("SELECT * FROM " + Constants.TABLE_NAME_REWARD + " WHERE Complete = 0")
    List<Reward> getRewards();

    @Query("SELECT COUNT(*) FROM " + Constants.TABLE_NAME_REWARD + " WHERE Complete = 0")
    int getEarnedRewards();

    @Query("SELECT COUNT(*) FROM " + Constants.TABLE_NAME_REWARD + " WHERE Complete = 1")
    int getUnearnedRewards();

    @Query("SELECT SUM(points) FROM " + Constants.TABLE_NAME_REWARD + " WHERE Complete = 1")
    int getSpentPoints();

    @Query("SELECT SUM(G.points - R.points) FROM "+ Constants.TABLE_NAME_GOAL + " AS G," + Constants.TABLE_NAME_REWARD + " AS R WHERE G.Complete = 1 AND R.Complete = 1")
    int getCurrentPoints();

    @Insert
    long insertReward(Reward reward);

    @Update
    void updateReward(Reward repos);

    @Delete
    void deleteReward(Reward reward);
}
