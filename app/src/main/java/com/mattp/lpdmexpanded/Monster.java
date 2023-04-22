package com.mattp.lpdmexpanded;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mattp.lpdmexpanded.db.MonsterDatabase;
import com.mattp.lpdmexpanded.utilities.Dice;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = MonsterDatabase.MONSTER_TABLE)
public abstract class Monster {

    /**
     * @author Matthew Perona
     * @since 12 - March - 2023
     * Description Abstract Monster class from which we will add distinct monster classes for different monster versions.
     * These will be differentiated by "elemental type" as well as base stats and will have "weakness" and "bonus" characteristics
     * based on opposing types in battle. Will also implement a battle system that uses the Dice to create a randomly generated outcome
     * for battle pairings.
     */

    @PrimaryKey(autoGenerate = true)
    private int mMonsterId;

    protected enum ElementalType {
        ELECTRIC,
        FIRE,
        GRASS,
        WATER,
    }

    private String mMonsterName = "";
    private static final double MAX_HP = 40.0;
    private int attackMin = 1;
    private boolean fainted = false;
    protected int defensePoints = 10;
    private int defenseMin = 1;
    private int defenseMax = 10;
    private int attackMax = 10;
    private Double healthPoints = MAX_HP;
    private String phrase = "";
    private List<ElementalType> elements = new ArrayList<>();
    protected int attackPoints = 10;



    public Monster(String monsterName, ElementalType type) {
        this.mMonsterName = monsterName;
        this.elements.add(type);

        setPhrase(this);
    }

    // Sets and adds additional elemental types to a monster.
    public int setType(ElementalType type) {
        for (int i = 0; i < this.getElements().size(); i++) {
            if (this.getElements().get(i).equals(type)) {
                System.out.println(type + " already set!");
                return 1;
            } else if (this.attackModifier(type) > 1.0) {
                System.out.println("Can't have conflicting types!");
                return -1;
            } else {
                System.out.println(this.getmMonsterName() + " now has " + type);
                this.elements.add(type);
                return 0;
            }
        }
        return 0;
    }

    // Sets the speech phrases for the monsters.
    private static Monster setPhrase(Monster monster) {
        if (monster.getClass().equals(WeirdTurtle.class)) {
            monster.setPhrase("'Urtle!");
        } else if (monster.getClass().equals(FireLizard.class)) {
            monster.setPhrase("Deal with it.");
        } else if (monster.getClass().equals(ElectricRat.class)) {
            monster.setPhrase("'Lectric!");
        } else if (monster.getClass().equals(FlowerDino.class)) {
            monster.setPhrase("Flowah!");
        } else {
            monster.setPhrase("Mewtw... ahem... No phrase for me!");
        }
        return monster;
    }

    // Performs the attack function for the monsters. This will also print out the details of the battle.
    public double attack(Monster monster) {
        double attackValue = 1.0;

        if (this.isFainted()) {
            System.out.println(this.getmMonsterName() + " isn't conscious... it can't attack.");
            return 0.0;
        } else {
            System.out.println(this.getmMonsterName() + " is attacking " + monster.getmMonsterName() + ".");
            System.out.println(this.getPhrase());

            attackValue = calculateAttackPoints(this, monster.getElements());

            System.out.println(this.getmMonsterName() + " is attacking with " + attackValue);
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
            System.out.println(this.getmMonsterName() + " is hit for " + newAttackValue + " damage!");
            this.setHealthPoints(this.getHealthPoints() - newAttackValue);
        } else if (newAttackValue == 0){

            System.out.println(this.getmMonsterName() + " is nearly hit!");
        }

        if (attackValue < calculateDefensePoints(this) / 2) {
            System.out.println(this.getmMonsterName() + " shrugs off the puny attack.");
        }

        if (this.getHealthPoints() <= 0) {
            System.out.println(this.getmMonsterName() + " has faint--passed out. It's passed out.");
            this.setFainted(true);
        } else {
            System.out.println(this.getmMonsterName() + " has " + this.getHealthPoints() + " / " + this.MAX_HP + " HP remaining");
        }

        return newAttackValue;
    }

    // Calculates the defense values for a monster for a round (method call) from a random roll within the range of defense values..
    protected double calculateDefensePoints(Monster monster) {
        int defenseValue = Dice.roll(monster.getDefenseMin(), monster.getDefenseMax());

        if (defenseValue < monster.getDefenseMax() / 2.0 && defenseValue % 2 == 0) {
            defenseValue = (defenseValue + 1) * 2;
            System.out.println(monster.getmMonsterName() + " finds courage in the desperate situation!");
        }
        if (defenseValue == monster.getDefenseMin()) {
            System.out.println(monster.getmMonsterName() + " is clearly not paying attention.");
        }

        return defenseValue;
    }

    // Calculates the attack values for a monster for a round (method call) from a random roll within the range of attack values.
    public double calculateAttackPoints(Monster monster, List<ElementalType> enemyType) {
        int attackPoints = Dice.roll(monster.getAttackMin(), monster.getAttackMax());
        double modifier = 1.0;

        System.out.println(monster.getmMonsterName() + " rolls a " + attackPoints);

        for (int i = 0; i < enemyType.size(); i++) {
            modifier = modifier * attackModifier(enemyType.get(i));
        }

        if (modifier >= 2.0) {
            System.out.println("It's su-- *cough* very effective!");
        }

        return attackPoints * modifier;
    }


    /* Establshes the damage modifiers using the provided table.
     *    x                  Defending
     * Attack    Electric    Fire    Grass   Water
     * Electric  0.5         1.0     0.5     2.0
     * Fire      1.0         0.5     2.0     0.5
     * Grass     1.0         0.5     0.5     2.0
     * Water     1.0         2.0     0.5     0.5
     *
     * With any unknown types treated as no modifier (x1.0)
     * */
    protected double attackModifier(ElementalType defending) {
        int attackIndex = 4;
        int defendIndex = 4;

        // [attackIndex][defendIndex]
        double[][] attackModifierTable = { {0.5, 1.0, 0.5, 2.0, 1.0},
                {1.0, 0.5, 2.0, 0.5, 1.0},
                {1.0, 0.5, 0.5, 2.0, 1.0},
                {1.0, 2.0, 0.5, 0.5, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0} };

        for (ElementalType element : this.getElements()) {
            if (element.equals(ElementalType.ELECTRIC)) {
                attackIndex = 0;
            } else if (element.equals(ElementalType.FIRE)) {
                attackIndex = 1;
            } else if (element.equals(ElementalType.GRASS)) {
                attackIndex = 2;
            } else if (element.equals(ElementalType.WATER)) {
                attackIndex = 3;
            } else {
                attackIndex = 4;
            }

            if (defending.equals(ElementalType.ELECTRIC)) {
                defendIndex = 0;
            } else if (defending.equals(ElementalType.FIRE)) {
                defendIndex = 1;
            } else if (defending.equals(ElementalType.GRASS)) {
                defendIndex = 2;
            } else if (defending.equals(ElementalType.WATER)) {
                defendIndex = 3;
            } else {
                defendIndex = 4;
            }

            if (attackIndex != 4 && defendIndex != 4) {
                return attackModifierTable[attackIndex][defendIndex];
            }
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
            outputSB.append(this.getmMonsterName() + " has " + this.getHealthPoints() + "/" + this.MAX_HP + " hp.\n");
        } else {
            outputSB.append(this.getmMonsterName() + " has fainted.\n");

        }
        if (this.getElements().size() > 1) {
            outputSB.append("Elemental type: ");
            for (int i = 0; i < this.getElements().size(); i++) {
                if (i + 1 == this.getElements().size()) {
                    outputSB.append(this.getElements().get(i));
                } else {
                    outputSB.append(this.getElements().get(i) + ", ");
                }
            }
        } else {
            outputSB.append("Elemental type: " + this.getElements().get(0));
        }

        return outputSB.toString();
    }


    public void setFainted(boolean fainted) {
        this.fainted = fainted;
    }


    public void setPhrase(String phrase){
        this.phrase = phrase;
    }


    public String getmMonsterName() {
        return mMonsterName;
    }


    public void setmMonsterName(String mMonsterName) {
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


    public List<ElementalType> getElements() {
        return elements;
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

    public abstract void setAttackPoints();

    public abstract void setDefensePoints();
}
