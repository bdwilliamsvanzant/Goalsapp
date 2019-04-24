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
    @Query("SELECT * FROM " + Constants.TABLE_NAME_GOAL + "\n WHERE `Complete` AND `Complete Date` = (SELECT MAX(`Complete Date`) FROM "+ Constants.TABLE_NAME_GOAL + ");" )
    //@Query("SELECT * FROM " + Constants.TABLE_NAME_GOAL + "\n WHERE  `End Date` = (SELECT MAX(`End Date`) FROM "+ Constants.TABLE_NAME_GOAL + ");" )
    Goal getMostRecentGoal();

    @Query("SELECT * FROM " + Constants.TABLE_NAME_GOAL + " WHERE Complete = 0 AND \"End Date\" > :temp")
    List<Goal> getGoals(long temp);

    @Query("SELECT COUNT(*) FROM " + Constants.TABLE_NAME_GOAL + " WHERE Complete = 0")
    int getActiveGoals();

    @Query("SELECT COUNT(*) FROM " + Constants.TABLE_NAME_GOAL + " WHERE Complete = 1")
    int getCompletedGoals();

    @Query("SELECT SUM(points) FROM " + Constants.TABLE_NAME_GOAL + " WHERE Complete = 1")
    int getTotalPointsEarned();

    @Insert
    long insertGoal(Goal goal);

    @Update
    void updateGoal(Goal repos);

    @Update
    void setComplete(Goal goal);

    @Delete
    void deleteGoal(Goal goal);

}
