package com.example.goals.DAOs;

import com.example.goals.Goal;
import com.example.goals.util.Constants;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface GoalDao{
    @Query("SELECT * FROM " + Constants.TABLE_NAME_GOAL)
    List<Goal> getGoals();

    @Insert
    long insertGoal(Goal goal);

    @Update
    void updateGoal(Goal repos);

    @Delete
    void deleteGoal(Goal goal);

}