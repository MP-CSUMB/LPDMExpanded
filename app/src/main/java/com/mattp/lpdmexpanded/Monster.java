package com.mattp.lpdmexpanded;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mattp.lpdmexpanded.db.MonsterDatabase;
import com.mattp.lpdmexpanded.utilities.Dice;

/**
 * @author Matthew Perona
 * @since 12 - March - 2023
 * Description Abstract Monster class from which we will add distinct monster classes for different monster versions.
 * These will be differentiated by "elemental type" as well as base stats and will have "weakness" and "bonus" characteristics
 * based on opposing types in battle. Will also implement a battle system that uses the Dice to create a randomly generated outcome
 * for battle pairings.
 */

@Entity(tableName = MonsterDatabase.MONSTER_TABLE)
public class Monster {

    @PrimaryKey(autoGenerate = true)
    private int mMonsterId;


    private String mMonsterName = "";
    private static final double MAX_HP = 20.0;
    private int attackMin = 1;
    private boolean fainted = false;
    private int defensePoints = 10;
    private int defenseMin = 1;
    private int defenseMax = 10;
    private int attackMax = 10;
    private int attackPoints = 10;
    private Double healthPoints = MAX_HP;
    private String phrase = "";
    private String mElementType;
    private String mMonsterType;



    private String skillOne;
    private String skillTwo;



    public Monster(String monsterName, String monsterType) {
        this.mMonsterName = monsterName;
        this.mMonsterType = monsterType;
        this.setSkillOne("Attack");


        if (mMonsterType.equals("Fire Lizard")) {
            this.setAttackMin(8);
            this.setAttackMax(10);
            this.setDefenseMin(1);
            this.setDefenseMax(4);
            this.setElementType("FIRE");
            this.setSkillTwo("Get Warm");
        }
        if (mMonsterType.equals("Weird Turtle")) {
            this.setAttackMin(3);
            this.setAttackMax(8);
            this.setDefenseMin(6);
            this.setDefenseMax(8);
            this.setElementType("WATER");
            this.setSkillTwo("Gentle Mist");
        }
        if (mMonsterType.equals("Electric Rat")) {
            this.setAttackMin(5);
            this.setAttackMax(8);
            this.setDefenseMin(5);
            this.setDefenseMax(8);
            this.setElementType("ELECTRIC");
            this.setSkillTwo("Static Shock");
        }
        if (mMonsterType.equals("Flower Dino")) {
            this.setAttackMin(4);
            this.setAttackMax(8);
            this.setDefenseMin(3);
            this.setDefenseMax(6);
            this.setElementType("GRASS");
            this.setSkillTwo("Leaf Me Alone");
        }

//        setPhrase(this);
    }

    public void setElementType(String type) {
        this.mElementType = type;
    }

    public String getElementType() {
        return mElementType;
    }

    public String getMonsterType() {
        return mMonsterType;
    }

    public void setMonsterType(String monsterType) {
        this.mMonsterType = monsterType;
    }

    public int getMonsterId() {
        return mMonsterId;
    }

    public void setMonsterId(int MonsterId) {
        this.mMonsterId = MonsterId;
    }

    // Sets the speech phrases for the monsters.
//    private static Monster setPhrase(Monster monster) {
//        if (monster.getMonsterType().equals("Weird Turtle")) {
//            monster.setPhrase("'Urtle!");
//        } else if (monster.getMonsterType().equals("Fire Lizard")) {
//            monster.setPhrase("Deal with it.");
//        } else if (monster.getMonsterType().equals("Electric Rat")) {
//            monster.setPhrase("'Lectric!");
//        } else if (monster.getMonsterType().equals("Flower Dino")) {
//            monster.setPhrase("Flowah!");
//        } else {
//            monster.setPhrase("Mewtw... ahem... No phrase for me!");
//        }
//        return monster;
//    }

    // Performs the attack function for the monsters. This will also print out the details of the battle.
    public double attack(Monster monster) {
        double attackValue = 1.0;

        if (this.isFainted()) {
            System.out.println(this.getMonsterName() + " isn't conscious... it can't attack.");
            return 0.0;
        } else {
            System.out.println(this.getMonsterName() + " is attacking " + monster.getMonsterName() + ".");
            System.out.println(this.getPhrase());

            attackValue = calculateAttackPoints(this, monster.getElementType());

            System.out.println(this.getMonsterName() + " is attacking with " + attackValue);
        }

        return monster.takeDamage(attackValue);
    }

    // Calculates the damage taken by the target monster. This value is limited to 0, in that it doesn't allow for negative values.
    // Also prints out some colorful battle descriptors.
    private double takeDamage(double attackValue) {
        double defensePoints = calculateDefensePoints(this);
        double newAttackValue = attackValue - defensePoints;

        if (newAttackValue <= 0.0) {
            newAttackValue = 0.0;
        }

        if (newAttackValue > 0) {
            System.out.println(this.getMonsterName() + " is hit for " + newAttackValue + " damage!");
            this.setHealthPoints(this.getHealthPoints() - newAttackValue);
        } else if (newAttackValue == 0){

            System.out.println(this.getMonsterName() + " is nearly hit!");
        }

        if (attackValue < calculateDefensePoints(this) / 2) {
            System.out.println(this.getMonsterName() + " shrugs off the puny attack.");
        }

        if (this.getHealthPoints() <= 0) {
            System.out.println(this.getMonsterName() + " has faint--passed out. It's passed out.");
            this.setFainted(true);
        } else {
            System.out.println(this.getMonsterName() + " has " + this.getHealthPoints() + " / " + this.MAX_HP + " HP remaining");
        }

        return newAttackValue;
    }

    // Calculates the defense values for a monster for a round (method call) from a random roll within the range of defense values..
    protected double calculateDefensePoints(Monster monster) {
        int defenseValue = Dice.roll(monster.getDefenseMin(), monster.getDefenseMax());

        if (defenseValue < monster.getDefenseMax() / 2.0 && defenseValue % 2 == 0) {
            defenseValue = (defenseValue + 1) * 2;
            System.out.println(monster.getMonsterName() + " finds courage in the desperate situation!");
        }
        if (defenseValue == monster.getDefenseMin()) {
            System.out.println(monster.getMonsterName() + " is clearly not paying attention.");
        }

        return defenseValue;
    }

    // Calculates the attack values for a monster for a round (method call) from a random roll within the range of attack values.
    public double calculateAttackPoints(Monster monster, String enemyType) {
        int attackPoints = Dice.roll(monster.getAttackMin(), monster.getAttackMax());
        double modifier = 1.0;

        System.out.println(monster.getMonsterName() + " rolls a " + attackPoints);

        modifier = modifier * attackModifier(enemyType);

        if (modifier >= 2.0) {
            System.out.println("It's su-- *cough* very effective!");
        }

        return attackPoints * modifier;
    }


    /* Establishes the damage modifiers using the provided table.
     *    x                  Defending
     * Attack    Electric    Fire    Grass   Water
     * Electric  0.5         1.0     0.5     2.0
     * Fire      1.0         0.5     2.0     0.5
     * Grass     1.0         0.5     0.5     2.0
     * Water     1.0         2.0     0.5     0.5
     *
     * With any unknown types treated as no modifier (x1.0)
     * */
    protected double attackModifier(String defending) {
        int attackIndex = 4;
        int defendIndex = 4;

        // [attackIndex][defendIndex]
        double[][] attackModifierTable = { {0.5, 1.0, 0.5, 2.0, 1.0},
                {1.0, 0.5, 2.0, 0.5, 1.0},
                {1.0, 0.5, 0.5, 2.0, 1.0},
                {1.0, 2.0, 0.5, 0.5, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0} };

            if (mElementType.equals("ELECTRIC")) {
                attackIndex = 0;
            } else if (mElementType.equals("FIRE")) {
                attackIndex = 1;
            } else if (mElementType.equals("GRASS")) {
                attackIndex = 2;
            } else if (mElementType.equals("WATER")) {
                attackIndex = 3;
            } else {
                attackIndex = 4;
            }

            if (defending.equals("ELECTRIC")) {
                defendIndex = 0;
            } else if (defending.equals("FIRE")) {
                defendIndex = 1;
            } else if (defending.equals("GRASS")) {
                defendIndex = 2;
            } else if (defending.equals("WATER")) {
                defendIndex = 3;
            } else {
                defendIndex = 4;
            }

            if (attackIndex != 4 && defendIndex != 4) {
                return attackModifierTable[attackIndex][defendIndex];
            }
        return attackModifierTable[attackIndex][defendIndex];
    }


    public boolean isFainted() {
        if (this.fainted) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public String toString() {
        StringBuilder outputSB = new StringBuilder();

        if (!this.isFainted()) {
            outputSB.append(this.getMonsterName() + " has " + this.getHealthPoints() + "/" + this.MAX_HP + " hp.\n");
        } else {
            outputSB.append(this.getMonsterName() + " has fainted.\n");

        }

        outputSB.append("Elemental type: " + this.getElementType());

        return outputSB.toString();
    }


    public void setFainted(boolean fainted) {
        this.fainted = fainted;
    }


    public void setPhrase(String phrase){
        this.phrase = phrase;
    }


    public String getMonsterName() {
        return mMonsterName;
    }


    public void setMonsterName(String mMonsterName) {
        this.mMonsterName = mMonsterName;
    }


    public int getAttackMin() {
        return attackMin;
    }


    public void setAttackMin(int attackMin) {
        this.attackMin = attackMin;
    }


    public int getDefensePoints() {
        return defensePoints;
    }


    public int getDefenseMin() {
        return defenseMin;
    }


    public void setDefenseMin(int defenseMin) {
        this.defenseMin = defenseMin;
    }


    public int getDefenseMax() {
        return defenseMax;
    }


    public void setDefenseMax(int defenseMax) {
        this.defenseMax = defenseMax;
    }


    public int getAttackMax() {
        return attackMax;
    }


    public void setAttackMax(int attackMax) {
        this.attackMax = attackMax;
    }


    public Double getHealthPoints() {
        return healthPoints;
    }


    public void setHealthPoints(Double healthPoints) {
        this.healthPoints = healthPoints;
    }


    public String getPhrase() {
        return phrase + " " + phrase;
    }


    public int getAttackPoints() {
        return attackPoints;
    }


    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }


    public void setDefensePoints(int defensePoints) {
        this.defensePoints = defensePoints;
    }

    public void setAttackPoints() {
        this.attackPoints = Dice.roll(this.attackMin, this.attackMax);
    }

    public void setDefensePoints() {
        this.defensePoints = Dice.roll(this.defenseMin, this.defenseMax);
    }

    public String getSkillOne() {
        return skillOne;
    }

    public void setSkillOne(String skillOne) {
        this.skillOne = skillOne;
    }

    public String getSkillTwo() {
        return skillTwo;
    }

    public void setSkillTwo(String skillTwo) {
        this.skillTwo = skillTwo;
    }
}
