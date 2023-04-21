package com.mattp.lpdmexpanded;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
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

    private static final String USER_ID_KEY = "com.example.loginactivity.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.loginactivity.PREFERENCES_KEY";

    private SharedPreferences mPreferences = null;

    private User mUser;
    private UserDAO mUserDAO;

    private Button adminButton;

    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        getPrefs();

        boolean userIsAdmin = mPreferences.getBoolean("isAdmin", false);

        adminButton = findViewById(R.id.admin_button);

        if (userIsAdmin) {
            adminButton.setVisibility(View.VISIBLE);
        } else {
            adminButton.setVisibility(View.GONE);
        }

    }

    private void wireupDisplay() {

        adminButton = findViewById(R.id.admin_button);

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
        if (mPreferences == null) {
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
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
        String username = mPreferences.getString("username", null);

        item.setTitle(username);

        mOptionsMenu = menu;

        return super.onPrepareOptionsMenu(menu);
    }
}