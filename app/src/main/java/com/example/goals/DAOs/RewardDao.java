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
    int getActiveRewards();

    @Query("SELECT COUNT(*) FROM " + Constants.TABLE_NAME_REWARD + " WHERE Complete = 1")
    int getCompletedRewards();

    @Query("SELECT SUM(points) FROM " + Constants.TABLE_NAME_REWARD + " WHERE Complete = 1")
    int getSpentPoints();

    @Insert
    long insertReward(Reward reward);

    @Update
    void updateReward(Reward repos);

    @Delete
    void deleteReward(Reward reward);
}
