package com.example.goals;

import com.example.goals.util.Constants;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = Constants.TABLE_NAME_REWARD)
public class Reward implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long reward_id;

    @ColumnInfo(name = "Reward" +
            " Name")
    private String reward_name;

    @ColumnInfo(name = "Description")
    private String description;

    //TODO: set appropriate data types (numeric/time) for the following attributes.
    @ColumnInfo(name = "Points")
    private int points;

    @ColumnInfo(name = "End Date")
    private long end_time;

    @ColumnInfo(name = "Complete")
    private boolean complete;
    @Ignore
    public Reward(){}

    public Reward(String reward_name, String description, int points) {
        this.reward_name = reward_name;
        this.description = description;
        this.points = points;
    }

    public long getReward_id() {
        return reward_id;
    }

    public void setReward_id(long reward_id) {
        this.reward_id = reward_id;
    }

    public String getReward_name() {
        return reward_name;
    }

    public void setReward_name(String reward_name) {
        this.reward_name = reward_name;
    }

    public void setDescription(String description) { this.description = description; }

    public String getDescription () { return description; }

    public int getPoints(){ return  points; }

    public void setPoints(int points){ this.points = points; }

    public long getEnd_time() { return end_time; }
    public void setEnd_time(long end_time) { this.end_time = end_time; }

    public void setComplete (boolean isComplete) { this.complete = isComplete; }
    public boolean getComplete (){ return complete; }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Reward)) return false;

        Reward reward = (Reward) o;

        if(reward_id != reward.reward_id) return false;
        return reward_name != null ? reward_name.equals(reward.reward_name) : reward.reward_name == null;
    }

    @Override
    public int hashCode(){
        int result = (int) reward_id;
        result = 31 * result + (reward_name != null ? reward_name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "Reward" +
                "{" +
                "reward_id='" + reward_id + '\'' +
                ", reward_name='" + reward_name + '\'' +
                ", points='" + points + '\'' + '}';
        //TODO: Add in start/end and point fields.
    }
}


