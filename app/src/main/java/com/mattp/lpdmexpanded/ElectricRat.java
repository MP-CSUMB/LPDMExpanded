package com.mattp.lpdmexpanded;

import utilities.Dice;


/*
 * Creates the concrete implementation of the monster class for Electric Rats.
 * Sets default values for the monster type as well as the default elemental type.
 * */

public class ElectricRat extends Monster{

    private final int DEFENSE_MIN = 5;
    private final int DEFENSE_MAX = 8;
    private final int ATTACK_MIN = 5;
    private final int ATTACK_MAX = 8;

    public ElectricRat(String name) {
        super(name, Monster.ElementalType.ELECTRIC);
        setAttackMin(ATTACK_MIN);
        setAttackMax(ATTACK_MAX);
        setDefenseMin(DEFENSE_MIN);
        setDefenseMax(DEFENSE_MAX);
    }

    @Override
    public void setAttackPoints() {
        this.attackPoints = Dice.roll(this.ATTACK_MIN, this.ATTACK_MAX);
    }

    @Override
    public void setDefensePoints() {
        this.defensePoints = Dice.roll(this.DEFENSE_MIN, this.DEFENSE_MAX);
    }
}
