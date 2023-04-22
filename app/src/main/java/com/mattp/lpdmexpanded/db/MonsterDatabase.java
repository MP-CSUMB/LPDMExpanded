package com.mattp.lpdmexpanded.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mattp.lpdmexpanded.Monster;

@Database(entities = {Monster.class}, version = 1)
public abstract class MonsterDatabase extends RoomDatabase {
    public static final String MONSTER_DB_NAME = "MONSTER_DATABASE";
    public static final String MONSTER_TABLE = "MONSTER_TABLE";

    public abstract MonsterDAO getMonsterDAO();
}
