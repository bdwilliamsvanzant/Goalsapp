package com.example.goals;

import com.example.goals.util.Constants;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = Constants.TABLE_NAME_GOAL)
public class Goal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long goal_id;

    @ColumnInfo(name = "Goal Name")
    private String goal_name;

    @ColumnInfo(name = "Description")
    private String content;

    //TODO: set appropriate data types (numeric/time) for the following attributes.
    @ColumnInfo(name = "Difficulty")
    private int difficulty;

    @ColumnInfo(name = "Start Date")
    private String start_time;

    @ColumnInfo(name = "End Date")
    private String end_time;

    @ColumnInfo(name = "Points")
    private int points;

    @ColumnInfo(name = "Time Span")
    private String timespan;

    @ColumnInfo(name = "Complete")
    private boolean complete;

    @ColumnInfo(name = "Complete Date")
    private long completeDate;

    @Ignore
    public Goal(){}

    /*
    public Goal(String content, String goal_name){
        this.goal_name = goal_name;
        this.content = content;
    }*/

    public Goal(String content, String goal_name, int difficulty, int points, String start_time, String end_time) {
        this.goal_name = goal_name;
        this.content = content;
        this.difficulty = difficulty;
        this.points = points;
        this.start_time = start_time;
        this.end_time = end_time;
        this.completeDate = 0;
    }

    public long getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(long goal_id) {
        this.goal_id = goal_id;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){ this.content = content; }

    public String getGoal_name() {
        return goal_name;
    }

    public void setGoal_name(String goal_name) {
        this.goal_name = goal_name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getStart_time() { return start_time; }

    public void setStart_time(String start_time) { this.start_time = start_time; }

    public String getEnd_time() { return end_time; }

    public void setEnd_time(String end_time) { this.end_time = end_time; }

    public int getPoints(){ return  points; }

    public void setPoints(int points){ this.points = points; }

    public String getTimespan() {
        return timespan;
    }

    public void setTimespan(String timespan) {
        this.timespan = timespan;
    }

    public void setComplete (boolean isComplete) { this.complete = isComplete; }

    public boolean getComplete (){ return complete; }

    public void setCompleteDate (long date) { this.completeDate = date; }

    public long getCompleteDate (){ return completeDate; }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Goal)) return false;

        Goal goal = (Goal) o;

        if(goal_id != goal.goal_id) return false;
        return goal_name != null ? goal_name.equals(goal.goal_name) : goal.goal_name == null;
    }

    public void completeGoal(){
        if(!complete){
            setComplete(true);
        }
    }

    @Override
    public int hashCode(){
        int result = (int) goal_id;
        result = 31 * result + (goal_name != null ? goal_name.hashCode() : 0);
        return result;
    }


    @Override
    public String toString(){
        return "Goal{" +
                "goal_id='" + goal_id + '\'' +
                ", content='" + content + '\'' +
                ", goal_name='" + goal_name + '\'' +
                ", difficulty='" + Integer.toString(difficulty) + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", points='" + Integer.toString(points) + '\'' + '}';
        //TODO: Add in start/end and point fields.
    }
}

