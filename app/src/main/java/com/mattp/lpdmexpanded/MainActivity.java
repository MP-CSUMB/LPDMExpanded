package com.mattp.lpdmexpanded;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.mattp.lpdmexpanded.db.UserDAO;
import com.mattp.lpdmexpanded.db.UserDatabase;
import com.mattp.lpdmexpanded.db.MonsterDAO;
import com.mattp.lpdmexpanded.db.MonsterDatabase;
import com.mattp.lpdmexpanded.db.AbilityDAO;
import com.mattp.lpdmexpanded.db.AbilityDatabase;

import java.util.List;


/**
 * @Author: Matthew Perona
 * @Date: 6 - April - 2023
 * Explanation: Creates a login page for the app which has 2 buttons, a login and a create account.
 * Login will take a username and password login and check it against a database and allow entry or not.
 * Create account will... create a new user account.
 * There are 2 account types, standard and admin.
 * */

public class MainActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.mattp.lpdmexpanded.USER_ID_KEY";
    private static final String PREFERENCES_KEY = "com.mattp.lpdmexpanded.PREFERENCES_KEY";
    private Button mLogin_button;
    private TextView mTitle;
    private UserDAO mUserDAO;
    private MonsterDAO mMonsterDAO;
    private AbilityDAO mAbilityDAO;




    private SharedPreferences mPreferences;
    private int mUserId;


//    ActivityMainBinding mMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();
        wireUpDisplay();
        getPrefs();
        checkForUser();

        addUserToPreference(mUserId);


        // Check if the user is already logged in using sharedPreferences

        if (mUserId != -1) {

            // Since user is logged in use intent factory to jump to landing page
            Intent intent = new Intent(MainActivity.this, LandingPage.class);
            intent.putExtra(USER_ID_KEY, mUserId);
            startActivity(intent);

            finish();
        } else {
            mLogin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Jump to the LoginActivity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra(USER_ID_KEY, mUserId);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

    private void wireUpDisplay() {
        mLogin_button = findViewById(R.id.login_button);
        mTitle = findViewById(R.id.title_textView);
    }

    private void addUserToPreference(int userId) {
        if (mPreferences == null) {
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
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

        mAbilityDAO = Room.databaseBuilder(this, AbilityDatabase.class, AbilityDatabase.ABILITY_DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getAbilityDAO();
    }

    private void checkForUser() {
        // do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        // do we have a user in the preferences?
        if (mUserId != -1) {
            return;
        }

        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        if (mUserId != -1) {
            return;
        }

        if (mPreferences == null) {
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        // do we have any users at all?
        List<User> users = mUserDAO.getAllUsers();
        if(users.size() <= 0) {
            User defaultUser1 = new User("admin2", "admin2", true);
            User defaultUser2 = new User("testuser1", "testuser1", false);
            mUserDAO.insert(defaultUser1, defaultUser2);

            Monster defaultMonster1 = new Monster("Electric Rat", "Electric Rat");
            defaultMonster1.setSkillOne("Attack");
            defaultMonster1.setSkillTwo("Static Shock");
            mMonsterDAO.insert(defaultMonster1);
            Monster defaultMonster2 = new Monster("Weird Turtle", "Weird Turtle");
            defaultMonster1.setSkillOne("Attack");
            defaultMonster1.setSkillTwo("Gentle Mist");
            mMonsterDAO.insert(defaultMonster2);
            Monster defaultMonster3 = new Monster("Fire Lizard", "Fire Lizard");
            defaultMonster1.setSkillOne("Attack");
            defaultMonster1.setSkillTwo("Get Warm");
            mMonsterDAO.insert(defaultMonster3);
            Monster defaultMonster4 = new Monster("Flower Dino", "Flower Dino");
            defaultMonster1.setSkillOne("Attack");
            defaultMonster1.setSkillTwo("Leaf Me Alone");
            mMonsterDAO.insert(defaultMonster4);

            Ability defaultAbility1 = new Ability("Attack", "", 1, -1);
            Ability defaultAbility2 = new Ability("Use Those Feet", "", 1.5, 3);
            Ability defaultAbility3 = new Ability("Get Warm", "", 1.5, 3);
            Ability defaultAbility4 = new Ability("Leaf Me Alone", "", 1.5, 3);
            Ability defaultAbility5 = new Ability("Static Shock", "", 1.5, 3);
            Ability defaultAbility6 = new Ability("Gentle Mist", "", 1.5, 3);
            mAbilityDAO.insert(defaultAbility1, defaultAbility2, defaultAbility3, defaultAbility4, defaultAbility5, defaultAbility6);
        }
    }


    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);
    }

    private void logoutUser() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearUserFromIntent();
                        clearUserFromPref();
                    }
                });
        alertBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        alertBuilder.create().show();
    }

    private void clearUserFromPref() {
        addUserToPreference(-1);
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY, -1);
    }

}