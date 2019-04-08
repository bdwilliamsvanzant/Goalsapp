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
    //@Query("SELECT * FROM " + Constants.TABLE_NAME_GOAL + "\n WHERE `Complete` AND `End Date` = (SELECT MAX(`End Date`) FROM "+ Constants.TABLE_NAME_GOAL + ");" )
    @Query("SELECT * FROM " + Constants.TABLE_NAME_GOAL + "\n WHERE  `End Date` = (SELECT MAX(`End Date`) FROM "+ Constants.TABLE_NAME_GOAL + ");" )
    Goal getMostRecentGoal();

    @Query("SELECT * FROM " + Constants.TABLE_NAME_GOAL)
    List<Goal> getGoals();

    @Insert
    long insertGoal(Goal goal);

    @Update
    void updateGoal(Goal repos);

    @Update
    void setComplete(Goal goal);

    @Delete
    void deleteGoal(Goal goal);

}
