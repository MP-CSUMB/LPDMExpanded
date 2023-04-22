package com.mattp.lpdmexpanded;

import com.mattp.lpdmexpanded.utilities.Dice;


/*
 * Creates the concrete implementation of the monster class for Flower Dinos.
 * Sets default values for the monster type as well as the default elemental type.
 * */

public class FlowerDino extends Monster{

    private final int DEFENSE_MIN = 4;
    private final int DEFENSE_MAX = 8;
    private final int ATTACK_MIN = 3;
    private final int ATTACK_MAX = 6;

    public FlowerDino(String name) {
        super(name, Monster.ElementalType.GRASS);
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
