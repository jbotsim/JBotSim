/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.jbotsim.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {
    private Color color255;


    @BeforeEach
    void setUp() {
        color255 = Color.getColorFromInteger(255);
    }

    // region constructors

    @Test
    void constructor_rgbaValues_matchesAWT() {
        int r = 1;
        int g = 2;
        int b = 3;
        int a = 4;
        Color color = new Color(r, g, b, a);
        java.awt.Color awtColor = new java.awt.Color(r, g, b, a);
        System.err.println(awtColor.getAlpha());
        checkColorWithAWT(awtColor, color);
    }
    @Test
    void constructor_rgbValues_matchesAWT() {
        int r = 1;
        int g = 2;
        int b = 3;
        Color color = new Color(r, g, b);
        java.awt.Color awtColor = new java.awt.Color(r, g, b);

        checkColorWithAWT(awtColor, color);
    }

    @Test
    void constructor_intValueWithAlpha_matchesAWT() {
        int value = 0x77AABBCC;
        Color color = new Color(value, true);
        java.awt.Color awtColor = new java.awt.Color(value, true);

        checkColorWithAWT(awtColor, color);
    }

    @Test
    void constructor_intValueWithoutAlpha_matchesAWT() {
        int value = 0x77AABBCC;
        java.awt.Color awtColor = new java.awt.Color(value);

        Color color1 = new Color(value);
        checkColorWithAWT(awtColor, color1);

        Color color2 = new Color(value, false);
        checkColorWithAWT(awtColor, color2);
    }

    private void checkColorWithAWT(java.awt.Color expectedColor, Color color) {
        assertEquals(expectedColor.getRed(), color.getRed(), "Incorrect R component");
        assertEquals(expectedColor.getGreen(), color.getGreen(), "Incorrect G component");
        assertEquals(expectedColor.getBlue(), color.getBlue(), "Incorrect B component");
        assertEquals(expectedColor.getAlpha(), color.getAlpha(), "Incorrect A component");
        assertEquals(expectedColor.getRGB(), color.getRGB(), "Incorrect RGB value");
    }

    // region incorrect parameters
    @Test
    void constructor_incorrectComponents_throwsIllegalArgumentException() {
        checkBadComponentR(-1);
        checkBadComponentG(-1);
        checkBadComponentB(-1);
        checkBadComponentA(-1);

        checkBadComponentR(256);
        checkBadComponentG(256);
        checkBadComponentB(256);
        checkBadComponentA(256);
    }

    private void checkBadComponentR(int badComponentValue) {
        int good = 1;
        assertColorConstructor4ThrowsIllegalArgumentException(badComponentValue, good, good, good);
        assertColorConstructor4ThrowsIllegalArgumentException(badComponentValue, good, good);
    }


    private void checkBadComponentG(int badComponentValue) {
        int good = 1;
        assertColorConstructor4ThrowsIllegalArgumentException(good, badComponentValue, good, good);
        assertColorConstructor4ThrowsIllegalArgumentException(good, badComponentValue, good);
    }


    private void checkBadComponentB(int badComponentValue) {
        int good = 1;
        assertColorConstructor4ThrowsIllegalArgumentException(good, good, badComponentValue, good);
        assertColorConstructor4ThrowsIllegalArgumentException(good, good, badComponentValue);
    }

    private void checkBadComponentA(int badComponentValue) {
        int good = 1;
        assertColorConstructor4ThrowsIllegalArgumentException(good, good, good, badComponentValue);
    }

    private void assertColorConstructor4ThrowsIllegalArgumentException(int r, int g, int b, int a) {
        assertThrows(IllegalArgumentException.class, () -> {
            new Color(r, g, b, a);
        });
    }
    private void assertColorConstructor4ThrowsIllegalArgumentException(int r, int g, int b) {
        assertThrows(IllegalArgumentException.class, () -> {
            new Color(r, g, b);
        });
    }

    // endregion incorrect parameters
    // endregion

    // region indexOf(Color)

    @Test
    void indexOf_constantIndexes_sameResult() {

        // Constant values
        testIndexOf(0, Color.blue);
        testIndexOf(1, Color.red);
        testIndexOf(2, Color.green);
        testIndexOf(3, Color.yellow);
        testIndexOf(4, Color.pink);
        testIndexOf(5, Color.black);
        testIndexOf(6, Color.white);
        testIndexOf(7, Color.gray);
        testIndexOf(8, Color.orange);
        testIndexOf(9, Color.cyan);
        testIndexOf(10, Color.magenta);
        testIndexOf(11, Color.lightGray);
        testIndexOf(12, Color.darkGray);

        // Generated value
        testGetColorAt(color255, 255);

    }

    @Test
    void indexOf_notPresent_returnMinus1() {

        testIndexOf( -1, new Color(2, 2, 2));

    }

    private void testIndexOf(int expectedIndex, Color color) {
        int index = Color.indexOf(color);
        assertEquals(expectedIndex, index);
    }

    // endregion

    // region getColorAt(int)

    @Test
    void getColorAt_constantIndexes_sameResult() {

        // Constant values
        testGetColorAt(Color.blue, 0);
        testGetColorAt(Color.red, 1);
        testGetColorAt(Color.green, 2);
        testGetColorAt(Color.yellow, 3);
        testGetColorAt(Color.pink, 4);
        testGetColorAt(Color.black, 5);
        testGetColorAt(Color.white, 6);
        testGetColorAt(Color.gray, 7);
        testGetColorAt(Color.orange, 8);
        testGetColorAt(Color.cyan, 9);
        testGetColorAt(Color.magenta, 10);
        testGetColorAt(Color.lightGray, 11);
        testGetColorAt(Color.darkGray, 12);

        // Generated values
        testGetColorAt(color255, 255);
        testGetColorAt(new Color(182, 139, 4), 5000);
        testGetColorAt(new Color(242, 132, 68), 6000);

    }

    private void testGetColorAt(Color expectedColor, int intColor) {
        Color color = Color.getColorAt(intColor);
        assertEquals(expectedColor, color);
    }

    // endregion

    // region getColorFromInteger(int)

    @Test
    void getColorFromInteger_constantIndexes_sameResult() {

        // Constant values
        testGetColorFromInteger(Color.blue, 0);
        testGetColorFromInteger(Color.red, 1);
        testGetColorFromInteger(Color.green, 2);
        testGetColorFromInteger(Color.yellow, 3);
        testGetColorFromInteger(Color.pink, 4);
        testGetColorFromInteger(Color.black, 5);
        testGetColorFromInteger(Color.white, 6);
        testGetColorFromInteger(Color.gray, 7);
        testGetColorFromInteger(Color.orange, 8);
        testGetColorFromInteger(Color.cyan, 9);
        testGetColorFromInteger(Color.magenta, 10);
        testGetColorFromInteger(Color.lightGray, 11);
        testGetColorFromInteger(Color.darkGray, 12);

        // Generated values
        testGetColorFromInteger(color255, 255);
        testGetColorFromInteger(new Color(182, 139, 4), 5000);
        testGetColorFromInteger(new Color(242, 132, 68), 6000);

    }

    private void testGetColorFromInteger(Color expectedColor, int intColor) {
        Color color = Color.getColorFromInteger(intColor);
        assertEquals(expectedColor, color);
    }

    // endregion

    // region getRandomColor(Random)
    @Test
    void getRandomColor_sameSeeds_sameResult() {
        Color color1 = Color.getRandomColor(new Random(1));
        Color color2 = Color.getRandomColor(new Random(1));

        assertEquals(color1, color2);
    }

    @Test
    void getRandomColor_differentSeeds_differentResult() {
        Color color1 = Color.getRandomColor(new Random(1));
        Color color2 = Color.getRandomColor(new Random(2));

        assertNotEquals(color1, color2);
    }

    @Test
    void getRandomColor_consecutiveCalls_differentResults() {
        Random random = new Random(1);
        Color color1 = Color.getRandomColor(random);
        Color color2 = Color.getRandomColor(random);

        assertNotEquals(color1, color2);
    }
    // endregion

    // region equals

    @Test
    void equals_null_notEqual() {
        Color color1 = new Color(125, 125 , 125, 0);

        assertNotEquals(color1, null);
    }
    @Test
    void equals_sameObject_equal() {
        Color color1 = new Color(125, 125 , 125, 0);

        assertEquals(color1, color1);
    }
    @Test
    void equals_sameValues_equal() {
        Color color1 = new Color(125, 125 , 125, 0);
        Color color2 = new Color(125, 125 , 125, 0);

        assertEquals(color1, color2);
    }
    @Test
    void equals_differentR_notEqual() {
        Color color1 = new Color(0, 125 , 125, 0);
        Color color2 = new Color(125, 125 , 125, 0);

        assertNotEquals(color1, color2);
    }
    @Test
    void equals_differentG_notEqual() {
        Color color1 = new Color(125, 0 , 125, 0);
        Color color2 = new Color(125, 125 , 125, 0);

        assertNotEquals(color1, color2);
    }
    @Test
    void equals_differentB_notEqual() {
        Color color1 = new Color(125, 125 , 0, 0);
        Color color2 = new Color(125, 125 , 125, 0);

        assertNotEquals(color1, color2);
    }
    @Test
    void equals_differentAlpha_notEqual() {
        Color color1 = new Color(125, 125 , 125, 12);
        Color color2 = new Color(125, 125 , 125, 0);

        assertNotEquals(color1, color2);
    }
    // endregion

    // region copy constructor
    @Test
    void copyConstructor_equal() {
        Color color1 = new Color(125, 125 , 125, 12);
        Color color2 = new Color(color1);

        assertEquals(color1, color2);
    }
    // endregion
}