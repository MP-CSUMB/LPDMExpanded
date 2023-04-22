package com.mattp.lpdmexpanded;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mattp.lpdmexpanded.db.UserDatabase;

import java.util.ArrayList;

@Entity(tableName = UserDatabase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mPassword;
    private String mUsername;
    private boolean isAdmin;
    private String monsterOne;


    public void setMonsterOne(String monsterName) {
        this.monsterOne = monsterName;
    }

    public String getMonsterOne() {
        return monsterOne;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public User(String username, String password, boolean isAdmin) {
        this.setUsername(username);
        this.setPassword(password);
        this.setIsAdmin(isAdmin);
        this.setMonsterOne("Pick-Up-Shoes");
    }


}
