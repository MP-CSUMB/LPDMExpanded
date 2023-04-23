package com.mattp.lpdmexpanded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mattp.lpdmexpanded.db.MonsterDAO;
import com.mattp.lpdmexpanded.db.MonsterDatabase;
import com.mattp.lpdmexpanded.db.UserDAO;
import com.mattp.lpdmexpanded.db.UserDatabase;

import java.util.List;

public class CreateMonsterActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.mattp.lpdmexpanded.USER_ID_KEY";
    private static final String PREFERENCES_KEY = "com.mattp.lpdmexpanded.PREFERENCES_KEY";
    private static final String NEW_MONSTER_KEY = "com.mattp.lpdmexpanded.NEW_MONSTER_KEY";
    private Button mFire_Lizard_button;
    private Button mFlower_Dino_button;
    private Button mWeird_Turtle_button;
    private Button mElectric_Rat_button;
    private Button mBack_button;

    private EditText mMonsterNameEditText;
    private String mMonsterName;
    private MonsterDAO mMonsterDAO;
    private UserDAO mUserDAO;
    private SharedPreferences mPreferences;
    private int mUserId;
    private int mMonsterId;
    private String newMonsterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_monster);

        wireUpDisplay();
        getDatabase();

        if (mPreferences == null) {
            mPreferences = this.getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        }

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        mFire_Lizard_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMonsterName = mMonsterNameEditText.getText().toString();
                makeMonster(monsterMatch(mMonsterName), "Fire Lizard");
                jumpToAbility();
            }
        });

        mWeird_Turtle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMonsterName = mMonsterNameEditText.getText().toString();
                makeMonster(monsterMatch(mMonsterName), "Weird Turtle");

                jumpToAbility();
            }
        });

        mElectric_Rat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMonsterName = mMonsterNameEditText.getText().toString();
                makeMonster(monsterMatch(mMonsterName), "Electric Rat");

                jumpToAbility();
            }
        });

        mFlower_Dino_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMonsterName = mMonsterNameEditText.getText().toString();
                makeMonster(monsterMatch(mMonsterName), "Flower Dino");

                jumpToAbility();
            }
        });

        mBack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Jump to the LandingPage
                Intent intent = new Intent(CreateMonsterActivity.this, LandingPage.class);
                intent.putExtra(USER_ID_KEY, mUserId);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getDatabase() {
        mUserDAO = Room.databaseBuilder(this, UserDatabase.class, UserDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();

        mMonsterDAO = Room.databaseBuilder(this, MonsterDatabase.class, MonsterDatabase.MONSTER_DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getMonsterDAO();
    }

    public boolean monsterMatch(String monsterName) {
        List<Monster> monsters = mMonsterDAO.getAllMonsters();

        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i).getMonsterName().equals(mMonsterName)) {
                return true;
            }
        }
        return false;
    }

    private void wireUpDisplay() {
        mFire_Lizard_button = findViewById(R.id.fire_lizard_button);
        mWeird_Turtle_button = findViewById(R.id.weird_turtle_button);
        mElectric_Rat_button = findViewById(R.id.electric_Rat_button);
        mFlower_Dino_button = findViewById(R.id.flower_Dino_button);
        mBack_button = findViewById(R.id.back_button);
        mMonsterNameEditText = findViewById(R.id.monster_name_edit_text);
    }

    public void makeMonster(boolean monsterExists, String monsterType) {
        if (!monsterExists) {
            Monster newMonster = new Monster(mMonsterName, monsterType);
            newMonsterName = newMonster.getMonsterName();
            mMonsterDAO.insert(newMonster);
        } else {
            Toast.makeText(CreateMonsterActivity.this, "Monster name not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void jumpToAbility() {
        Intent intent = new Intent(CreateMonsterActivity.this, AbilitySelectActivity.class);
        intent.putExtra(NEW_MONSTER_KEY, newMonsterName);
        startActivity(intent);
        finish();
    }
}