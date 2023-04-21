package com.mattp.lpdmexpanded.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mattp.lpdmexpanded.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + UserDatabase.USER_TABLE + " WHERE mUsername = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + UserDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM " + UserDatabase.USER_TABLE)
    List<User> getAllUsers();


}
