package com.mattp.lpdmexpanded.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mattp.lpdmexpanded.User;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public static final String DB_NAME = "USER_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";

    public abstract UserDAO getUserDAO();
}
