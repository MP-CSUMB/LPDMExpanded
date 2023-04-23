package com.mattp.lpdmexpanded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mattp.lpdmexpanded.db.MonsterDAO;
import com.mattp.lpdmexpanded.db.MonsterDatabase;
import com.mattp.lpdmexpanded.db.UserDAO;
import com.mattp.lpdmexpanded.db.UserDatabase;

import java.util.List;

public class SetTeamActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.mattp.lpdmexpanded.USER_ID_KEY";
    private static final String PREFERENCES_KEY = "com.mattp.lpdmexpanded.PREFERENCES_KEY";
    private static final String MONSTER_KEY = "com.mattp.lpdmexpanded.MONSTER_KEY";
    private Button mBack_button;
    private Button mAdd_Monster_button;
    private EditText mMonsterNameEditText;
    private TextView monsterDisplay;
    private String mMonsterName;
    private int mUserId;
    private SharedPreferences mPreferences;
    private MonsterDAO mMonsterDAO;
    private UserDAO mUserDAO;
    private User mUser;
    private Monster mMonster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_team);

        getDatabase();
        wireUpDisplay();

        if (mPreferences == null) {
            mPreferences = this.getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        }

        mUser = mUserDAO.getUserById(mPreferences.getInt(USER_ID_KEY, -1));


        mBack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Jump to the LandingPage
                Intent intent = new Intent(SetTeamActivity.this, LandingPage.class);
                intent.putExtra(USER_ID_KEY, mUserId);
                startActivity(intent);
                finish();
            }
        });

        mAdd_Monster_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMonsterName = mMonsterNameEditText.getText().toString();
                mMonster = mMonsterDAO.getMonsterByName(mMonsterName);


                if (mMonster != null) {
                    mUser.setMonsterOne(mMonsterName);
                    mUserDAO.update(mUser);
                    String displayText = mUser.getMonsterOne() + "\n" + mMonster.getMonsterType() + "\n" + "Ability:\n" + mMonster.getSkillTwo();
                    System.out.println(displayText);
                    monsterDisplay.setText(displayText);
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putInt(MONSTER_KEY, mMonster.getMonsterId());
                    editor.apply();
                } else {
                    Toast.makeText(SetTeamActivity.this, "Not a valid Monster", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void wireUpDisplay() {
        mMonsterNameEditText = findViewById(R.id.monster_name_edit_text);
        mAdd_Monster_button = findViewById(R.id.add_monster_button);
        mBack_button = findViewById(R.id.back_button);
        monsterDisplay = findViewById(R.id.team_viewer);
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
}