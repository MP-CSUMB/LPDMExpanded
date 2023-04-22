package com.mattp.lpdmexpanded;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mattp.lpdmexpanded.db.UserDAO;
import com.mattp.lpdmexpanded.db.UserDatabase;

public class LandingPage extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.mattp.lpdmexpanded.USER_ID_KEY";
    private static final String PREFERENCES_KEY = "com.mattp.lpdmexpanded.PREFERENCES_KEY";

    private SharedPreferences mPreferences;

    private User mUser;
    private UserDAO mUserDAO;
    private int mUserId;
    private Button mAdminButton;
    private Button mBattleButton;
    private Button mSetTeamButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        if (mPreferences == null) {
            mPreferences = this.getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        }

        getDatabase();


        mUserId = mPreferences.getInt(USER_ID_KEY, -1);
        mUser = mUserDAO.getUserById(mUserId);
        System.out.println(mUser.getIsAdmin());
        wireUpDisplay();

        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jump to the AdminLandingActivity
                Intent intent = new Intent(LandingPage.this, AdminLandingPage.class);
                intent.putExtra(USER_ID_KEY, mUserId);
                startActivity(intent);
                finish();
            }
        });

        mBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Jump to the BattleActivity
                Intent intent = new Intent(LandingPage.this, BattleActivity.class);
                intent.putExtra(USER_ID_KEY, mUserId);
                startActivity(intent);
                finish();
            }
        });

        mSetTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Jump to the SetTeamActivity
                Intent intent = new Intent(LandingPage.this, SetTeamActivity.class);
                intent.putExtra(USER_ID_KEY, mUserId);
                startActivity(intent);
                finish();
            }
        });


    }

    private void wireUpDisplay() {

        mAdminButton = findViewById(R.id.admin_button);
        boolean userIsAdmin = mUser.getIsAdmin();
        if (userIsAdmin) {
            mAdminButton.setVisibility(View.VISIBLE);
        } else {
            mAdminButton.setVisibility(View.GONE);
        }

        mBattleButton = findViewById(R.id.battle_button);
        mSetTeamButton = findViewById(R.id.team_button);

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
                        Intent intent = new Intent(LandingPage.this, LoginActivity.class);
                        startActivity(intent);
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

    private void addUserToPreference(int userId) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.userMenuLogout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.userMenuLogout);

        if (mUser != null) {
            item.setTitle(mUser.getUsername());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void getDatabase() {
        mUserDAO = Room.databaseBuilder(this, UserDatabase.class, UserDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }
}