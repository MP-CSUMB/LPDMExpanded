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
import android.widget.EditText;
import android.widget.Toast;

import com.mattp.lpdmexpanded.db.UserDAO;
import com.mattp.lpdmexpanded.db.UserDatabase;

import java.util.List;

public class AdminLandingPage extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.mattp.lpdmexpanded.USER_ID_KEY";
    private static final String PREFERENCES_KEY = "com.mattp.lpdmexpanded.PREFERENCES_KEY";

    private SharedPreferences mPreferences;
    private EditText mUsernameEditText;
    private String mUsername;

    private User mUser;
    private UserDAO mUserDAO;
    private int mUserId;
    private Button mRevokeButton;
    private Button mGrantButton;
    private Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing_page);

        if (mPreferences == null) {
            mPreferences = this.getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        }

        getDatabase();

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);
        mUser = mUserDAO.getUserById(mUserId);

        wireUpDisplay();

        mGrantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = mUsernameEditText.getText().toString();
                User getUser = mUserDAO.getUserByUsername(mUsername);

                if (getUser != null) {
                    getUser.setIsAdmin(true);
                    mUserDAO.update(getUser);
                    Toast.makeText(AdminLandingPage.this, "Admin Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminLandingPage.this, "Invalid Username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRevokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = mUsernameEditText.getText().toString();
                User getUser = mUserDAO.getUserByUsername(mUsername);

                List<User> users = mUserDAO.getAllUsers();
                int adminCount = 0;

                for (int i = 0; i < users.size(); i++) {

                    if (adminCount > 1) {
                        break;
                    }
                    if (users.get(i).getIsAdmin() == true) {
                        adminCount++;
                    }
                }
                if (getUser != null) {
                    if (adminCount > 1) {
                        getUser.setIsAdmin(false);
                        mUserDAO.update(getUser);
                        Toast.makeText(AdminLandingPage.this, "Admin Revoked", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminLandingPage.this, "Cannot Revoke Only Admin", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminLandingPage.this, "Invalid Username", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jump to the LoginActivity
                Intent intent = new Intent(AdminLandingPage.this, LandingPage.class);
                intent.putExtra(USER_ID_KEY, mUserId);
                startActivity(intent);
                finish();
            }
        });

    }

    private void wireUpDisplay() {

        mUsernameEditText = findViewById(R.id.username_edit_text);
        mRevokeButton = findViewById(R.id.revoke_admin_button);
        mGrantButton = findViewById(R.id.grant_admin_button);
        mBackButton = findViewById(R.id.backToLanding_button);
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
                        Intent intent = new Intent(AdminLandingPage.this, LoginActivity.class);
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