package com.mattp.lpdmexpanded.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mattp.lpdmexpanded.Ability;

@Database(entities = {Ability.class}, version = 1)
public abstract class AbilityDatabase extends RoomDatabase {
    public static final String ABILITY_DB_NAME = "ABILITY_DATABASE";
    public static final String ABILITY_TABLE = "ABILITY_TABLE";

    public abstract AbilityDAO getAbilityDAO();
}
