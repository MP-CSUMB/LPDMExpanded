package com.mattp.lpdmexpanded;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.mattp.lpdmexpanded.db.UserDAO;
import com.mattp.lpdmexpanded.db.UserDatabase;
import com.mattp.lpdmexpanded.databinding.ActivityMainBinding;

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

    private static final String USER_ID_KEY = "com.example.loginactivity.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.loginactivity.PREFERENCES_KEY";
    private Button mLogin_button;
    private UserDAO mUserDAO;

    User mUser;


    private SharedPreferences mPreferences = null;
    private int mUserId = -1;


    ActivityMainBinding mMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();

        checkForUser();
        addUserToPreference(mUserId);

        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mMainBinding.getRoot();

        setContentView(view);

        // Initialize buttons
        mLogin_button = mMainBinding.loginButton;


        // Check if the user is already logged in using sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(getString(R.string.isLoggedIn), false);

        if (isLoggedIn) {

            // Since user is logged in use intent factory to jump to landing page
            Intent intent = new Intent(MainActivity.this, LandingPage.class);
            startActivity(intent);

            finish();
        } else {

            // Since the user isn't logged in, activate visibility on the login and create account buttons
            mLogin_button.setVisibility(View.VISIBLE);

            mLogin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Jump to the LoginActivity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

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
    }

    private void checkForUser() {
        // do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        // do we have a user in the preferences?
        if (mUserId != -1) {
            return;
        }

       if (mPreferences == null) {
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if (mUserId != -1) {
            return;
        }

        // do we have any users at all?
        List<User> users = mUserDAO.getAllUsers();
        if(users.size() <= 0) {
            User defaultUser1 = new User("admin2", "admin2");
            User defaultUser2 = new User("testuser1", "testuser1");

            mUserDAO.insert(defaultUser1);
            mUserDAO.insert(defaultUser2);
        }

//        Intent intent = LoginActivity.intentFactory(this);
//        startActivity(intent);

    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
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