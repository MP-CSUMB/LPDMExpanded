package com.mattp.lpdmexpanded;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class CreateMonsterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_monster);



//        mCreate_Monster_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean monsterMatch = false;
//
//                mMonsterName = mMonsterNameEditText.getText().toString();
//                List<Monster> monsters = mMonsterDAO.getAllMonsters();
//
//                for (int i = 0; i < monsters.size(); i++) {
//                    if (monsters.get(i).getMonsterName().equals(mMonsterName)) {
//                        monsterMatch = true;
//                    }
//                }
//                if (!monsterMatch) {
//                    mUser.setMonsterOne(mMonsterName);
//                    mUserDAO.update(mUser);
//                    mMonster = mMonsterDAO.getMonsterByName(mMonsterName);
//                    mMonsterDAO.insert(mMonster);
//                    String displayText = mUser.getMonsterOne() + "\n" + mMonster.getMonsterType();
//                    monsterDisplay.setText(displayText);
//                } else {
//                    Toast.makeText(CreateMonsterActivity.this, "Monster name not available", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}