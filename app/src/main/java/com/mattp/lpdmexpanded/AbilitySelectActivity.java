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

import java.util.List;

public class AbilitySelectActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.mattp.lpdmexpanded.USER_ID_KEY";
    private static final String PREFERENCES_KEY = "com.mattp.lpdmexpanded.PREFERENCES_KEY";
    private static final String NEW_MONSTER_KEY = "com.mattp.lpdmexpanded.NEW_MONSTER_KEY";

    private Button mGet_Warm_button;
    private Button mGentle_Mist_button;
    private Button mUse_Those_Feet_button;
    private Button mLeaf_Me_Alone_button;
    private Button mStatic_Shock_button;
    private Button mRandom_Ability_button;

    private int mUserId;
    private SharedPreferences mPreferences;
    private UserDAO mUserDAO;
    private MonsterDAO mMonsterDAO;
    private AbilityDAO mAbilityDAO;
    private String mMonsterName;
    private Monster mMonster;
    private int mMonsterId;
    private User mUser;

    private void wireUpDisplay() {
        mGentle_Mist_button = findViewById(R.id.gentle_Mist_button);
        mGet_Warm_button = findViewById(R.id.get_Warm_button);
        mLeaf_Me_Alone_button = findViewById(R.id.leaf_Me_Alone_button);
        mUse_Those_Feet_button = findViewById(R.id.use_Those_Feet_button);
        mStatic_Shock_button = findViewById(R.id.static_Shock_button);
    }

    private void jumpToCreate() {
        Intent intent = new Intent(AbilitySelectActivity.this, CreateMonsterActivity.class);
        intent.putExtra(USER_ID_KEY, mUserId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ability_select);

        wireUpDisplay();
        getDatabase();

        if (mPreferences == null) {
            mPreferences = this.getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        }

        mMonsterId = getIntent().getIntExtra(NEW_MONSTER_KEY, -1);
        System.out.println(mMonsterId);

        mStatic_Shock_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monsterAbilityAssigner("Static Shock");
                jumpToCreate();
            }
        });

        mGet_Warm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monsterAbilityAssigner("Get Warm");
                jumpToCreate();
            }
        });

        mLeaf_Me_Alone_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monsterAbilityAssigner("Leaf Me Alone");
                jumpToCreate();
            }
        });

        mGentle_Mist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monsterAbilityAssigner("Gentle Mist");
                jumpToCreate();
            }
        });

        mUse_Those_Feet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monsterAbilityAssigner("Use Those Feet");
                jumpToCreate();
            }
        });

    }

    public void monsterAbilityAssigner(String abilityName) {
        System.out.println(abilityName);
        mMonster.setSkillTwo(abilityName);
        mMonsterDAO.update(mMonster);
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