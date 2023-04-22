package com.mattp.lpdmexpanded.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mattp.lpdmexpanded.Monster;

import java.util.List;

@Dao
public interface MonsterDAO {

    @Insert
    void insert(Monster... monsters);

    @Update
    void update(Monster... monsters);

    @Delete
    void delete(Monster monster);

    @Query("SELECT * FROM " + MonsterDatabase.MONSTER_TABLE + " WHERE mMonsterName = :monsterName")
    Monster getMonsterByName(String monsterName);

    @Query("SELECT * FROM " + MonsterDatabase.MONSTER_TABLE + " WHERE mMonsterId = :monsterId")
    Monster getMonsterById(int monsterId);

    @Query("SELECT * FROM " + MonsterDatabase.MONSTER_TABLE)
    List<Monster> getAllMonsters();


}
