package com.mattp.lpdmexpanded.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mattp.lpdmexpanded.Ability;

import java.util.List;

@Dao
public interface AbilityDAO {

    @Insert
    void insert(Ability... abilities);

    @Update
    void update(Ability... abilities);

    @Delete
    void delete(Ability ability);

    @Query("SELECT * FROM " + AbilityDatabase.ABILITY_TABLE + " WHERE mAbilityName = :abilityName")
    Ability getAbilityByName(String abilityName);

    @Query("SELECT * FROM " + AbilityDatabase.ABILITY_TABLE + " WHERE mAbilityId = :abilityId")
    Ability getAbilityById(int abilityId);

    @Query("SELECT * FROM " + AbilityDatabase.ABILITY_TABLE)
    List<Ability> getAllAbilities();


}
