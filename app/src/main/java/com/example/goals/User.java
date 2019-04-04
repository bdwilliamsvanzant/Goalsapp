package com.example.goals;

import com.example.goals.util.Constants;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = Constants.TABLE_NAME_USER)
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long user_id;

    @ColumnInfo(name = "User" +
            " Name")
    private String user_name;

    @ColumnInfo(name = "Points")
    private int points;

    @Ignore
    public User(){}

    public User(String user_name, int points) {
        this.user_name = user_name;
        this.points = points;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getPoints(){ return  points; }

    public void setPoints(int points){ this.points = points; }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof User)) return false;

        User user = (User) o;

        if(user_id != user.user_id) return false;
        return user_name != null ? user_name.equals(user.user_name) : user.user_name == null;
    }

    @Override
    public int hashCode(){
        int result = (int) user_id;
        result = 31 * result + (user_name != null ? user_name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "User" +
                "{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", points='" + points + '\'' + '}';
    }
}

