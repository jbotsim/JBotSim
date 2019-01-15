package io.jbotsim.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ColorTest {
    private Color color255;


    @BeforeEach
    void setUp() {
        color255 = Color.getColorAt(255);
    }


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

        testIndexOf( -1, new Color(-1, -1, -1));

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