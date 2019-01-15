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
        color255 = Color.getIntColor(255);
    }


    // region getIntColor(Color)


    @Test
    void getIntColorToIndex_constantIndexes_sameResult() {

        // Constant values
        testGetIntColor_toIndex(0, Color.blue);
        testGetIntColor_toIndex(1, Color.red);
        testGetIntColor_toIndex(2, Color.green);
        testGetIntColor_toIndex(3, Color.yellow);
        testGetIntColor_toIndex(4, Color.pink);
        testGetIntColor_toIndex(5, Color.black);
        testGetIntColor_toIndex(6, Color.white);
        testGetIntColor_toIndex(7, Color.gray);
        testGetIntColor_toIndex(8, Color.orange);
        testGetIntColor_toIndex(9, Color.cyan);
        testGetIntColor_toIndex(10, Color.magenta);
        testGetIntColor_toIndex(11, Color.lightGray);
        testGetIntColor_toIndex(12, Color.darkGray);

        // Generated value
        testGetIntColor(color255, 255);

    }

    @Test
    void getIntColorToIndex_notPresent_returnMinus1() {

        testGetIntColor_toIndex( -1, new Color(-1, -1, -1));

    }

    private void testGetIntColor_toIndex(int expectedIndex, Color color) {
        int index = color.getIntColor();
        assertEquals(expectedIndex, index);
    }

    // endregion

    // region getIntColor(int)

    @Test
    void getIntColor_constantIndexes_sameResult() {

        // Constant values
        testGetIntColor(Color.blue, 0);
        testGetIntColor(Color.red, 1);
        testGetIntColor(Color.green, 2);
        testGetIntColor(Color.yellow, 3);
        testGetIntColor(Color.pink, 4);
        testGetIntColor(Color.black, 5);
        testGetIntColor(Color.white, 6);
        testGetIntColor(Color.gray, 7);
        testGetIntColor(Color.orange, 8);
        testGetIntColor(Color.cyan, 9);
        testGetIntColor(Color.magenta, 10);
        testGetIntColor(Color.lightGray, 11);
        testGetIntColor(Color.darkGray, 12);

        // Generated values
        testGetIntColor(color255, 255);
        testGetIntColor(new Color(182, 139, 4), 5000);
        testGetIntColor(new Color(242, 132, 68), 6000);

    }

    private void testGetIntColor(Color expectedColor, int intColor) {
        Color color = Color.getIntColor(intColor);
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