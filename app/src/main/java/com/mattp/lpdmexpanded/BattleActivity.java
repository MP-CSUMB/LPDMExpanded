package com.mattp.lpdmexpanded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mattp.lpdmexpanded.db.MonsterDAO;
import com.mattp.lpdmexpanded.db.MonsterDatabase;
import com.mattp.lpdmexpanded.db.UserDAO;
import com.mattp.lpdmexpanded.db.UserDatabase;
import com.mattp.lpdmexpanded.db.AbilityDAO;
import com.mattp.lpdmexpanded.db.AbilityDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class BattleActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.mattp.lpdmexpanded.USER_ID_KEY";
    private static final String PREFERENCES_KEY = "com.mattp.lpdmexpanded.PREFERENCES_KEY";
    private static final String MONSTER_KEY = "com.mattp.lpdmexpanded.MONSTER_KEY";

    private Button mExit_button;
    private Button mAttack_button;
    private Button mAbility_button;
    private TextView mEnemy_monster_text;
    private TextView mMy_monster_text;
    private TextView mOutcome_text;
    private TextView mAbility_Counter_text;

    private UserDAO mUserDAO;
    private MonsterDAO mMonsterDAO;
    private AbilityDAO mAbilityDAO;
    private SharedPreferences mPreferences;
    private User mUser;
    private Monster mMyMonster;
    private Monster mEnemyMonster;
    private Ability mAbility;

    private boolean endBattle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        wireUpDisplay();
        getDatabase();
        getRandomMonster();

        if (mPreferences == null) {
            mPreferences = this.getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        }
        mMyMonster = mMonsterDAO.getMonsterById(mPreferences.getInt(MONSTER_KEY, -1));
        mUser = mUserDAO.getUserById(mPreferences.getInt(USER_ID_KEY, -1));

        mAbility = mAbilityDAO.getAbilityByName(mMyMonster.getSkillTwo());
    }

    private void getRandomMonster() {
        Random random = new Random();
        int randomMonsterId = random.nextInt(mMonsterDAO.getAllMonsters().size());
        mEnemyMonster = mMonsterDAO.getMonsterById(randomMonsterId);
    }

    private void wireUpDisplay() {
        mExit_button = findViewById(R.id.exit_button);
        if (endBattle) {
            mExit_button.setVisibility(View.VISIBLE);
        } else {
            mExit_button.setVisibility(View.GONE);
        }

        mAttack_button = findViewById(R.id.attack_button);
        mAbility_button = findViewById(R.id.ability_button);
        mEnemy_monster_text = findViewById(R.id.enemy_monster_text);
        mMy_monster_text = findViewById(R.id.my_monster_text);
        mOutcome_text = findViewById(R.id.outcome_text);
        mAbility_Counter_text = findViewById(R.id.ability_counter_text);
    }

    public void getDatabase() {
        mUserDAO = Room.databaseBuilder(this, UserDatabase.class, UserDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();

        mMonsterDAO = Room.databaseBuilder(this, MonsterDatabase.class, MonsterDatabase.MONSTER_DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getMonsterDAO();

        mAbilityDAO = Room.databaseBuilder(this,AbilityDatabase.class, AbilityDatabase.ABILITY_DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getAbilityDAO();
    }
}