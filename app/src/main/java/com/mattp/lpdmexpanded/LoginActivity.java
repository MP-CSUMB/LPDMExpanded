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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mattp.lpdmexpanded.db.UserDatabase;
import com.mattp.lpdmexpanded.db.UserDAO;

import java.util.List;


public class LoginActivity extends AppCompatActivity {

    TextView mLoginDisplay;
    private static final String USER_ID_KEY = "com.example.loginactivity.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.loginactivity.PREFERENCES_KEY";
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private String mUsername;
    private String mPassword;
    private Button mLoginButton;
    private Button mCreateAccountButton;
    private User mUser;

    private int mUserId = -1;

    private UserDAO mUserDAO;

    private Menu mOptionsMenu;

    private SharedPreferences mPreferences = null;

    private void wireUpDisplay() {
        mUsernameEditText = findViewById(R.id.username_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        mLoginButton = findViewById(R.id.login_button);
        mCreateAccountButton = findViewById(R.id.create_account_button);
    }

    private void getDatabase() {
        mUserDAO = Room.databaseBuilder(this, UserDatabase.class, UserDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireUpDisplay();

        getDatabase();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = mUsernameEditText.getText().toString();
                mPassword = mPasswordEditText.getText().toString();

                mUser = mUserDAO.getUserByUsername(mUsername);

                if (mUser != null && mUser.getPassword().equals(mPassword)) {
                    SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE).edit();
                    editor.putString("username", mUser.getUsername());
                    editor.apply();
                    invalidateOptionsMenu();

                    Intent intent = new Intent(LoginActivity.this, LandingPage.class);
                    startActivity(intent);

                } else {
                    // Outputs an invalid input text response
                    Toast.makeText(LoginActivity.this, "Invalid username or mPassword", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = mUsernameEditText.getText().toString();
                mPassword = mPasswordEditText.getText().toString();
                List<User> users = mUserDAO.getAllUsers();
                boolean userMatch = false;

                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUsername().equals(mUsername)) {
                        userMatch = true;
                    }
                }
                if (!userMatch) {
                    User newUser = new User(mUsername, mPassword);
                    mUserDAO.insert(newUser);
                    Toast.makeText(LoginActivity.this, "Account created", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Username not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        if (mUser != null) {
            MenuItem item = menu.findItem(R.id.userMenuLogout);
            item.setTitle(mUser.getUsername());

            mOptionsMenu = menu;
        }

        return super.onPrepareOptionsMenu(menu);
    }


    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);

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

    private void addUserToPreference(int userId) {
        if (mPreferences == null) {
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }


}