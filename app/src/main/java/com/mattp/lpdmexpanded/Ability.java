package com.mattp.lpdmexpanded;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mattp.lpdmexpanded.db.AbilityDatabase;

@Entity(tableName = AbilityDatabase.ABILITY_TABLE)
public class Ability {

    @PrimaryKey(autoGenerate = true)
    private int mAbilityId;

    private String mAbilityName;
    private String mElement;
    private double mAbilityMultiplier;
    private int mUseCount;

    public Ability(String abilityName, String element, double abilityMultiplier, int useCount) {
        this.setAbilityName(abilityName);
        this.setElement(element);
        this.setAbilityMultiplier(abilityMultiplier);
        this.setUseCount(useCount);
    }

    public int getAbilityId() {
        return mAbilityId;
    }

    public void setAbilityId(int abilityId) {
        this.mAbilityId = abilityId;
    }

    public String getAbilityName() {
        return mAbilityName;
    }

    public void setAbilityName(String abilityName) {
        this.mAbilityName = abilityName;
    }

    public String getElement() {
        return mElement;
    }

    public void setElement(String element) {
        this.mElement = element;
    }

    public double getAbilityMultiplier() {
        return mAbilityMultiplier;
    }

    public void setAbilityMultiplier(double abilityMultiplier) {
        this.mAbilityMultiplier = abilityMultiplier;
    }

    public int getUseCount() {
        return mUseCount;
    }

    public void setUseCount(int useCount) {
        this.mUseCount = useCount;
    }
}
