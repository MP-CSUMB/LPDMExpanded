package com.mattp.lpdmexpanded.utilities;

import java.util.Random;

/**
 * @author Drew A. Clinkenbeard
 * @since 12 - October - 2022
 * A utility for generating random numbers. dice.
 */
public class Dice {

    /**
     * Takes in two ints and compares to ensure that min is smaller than max, then generates a
     * random number between min and max.
     *
     * @param min the lowest number to be returned, inclusive
     * @param max the largest number to be returned, inclusive
     * @return returns x such that min <= x <= max
     */
    public static int roll(int min, int max) {
        Random random = new Random();
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        return (random.nextInt(max - Math.abs(min)) + 1) + min;
    }

    public static int roll(int max) {
        return Dice.roll(max, 1);
    }

}
