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

import java.util.*;

/**
 * <p>JBotSim's {@link Color} representation.</p>
 *
 * <p>Note: This class is strongly inspired from Java AWT's. Indeed, it is a remnant from JBotSim's pre-1.0.0 tight
 * coupling to Java AWT/Spring.</p>
 */
public class Color {
    public static final Color white = new Color(255, 255, 255, 255);
    public static final Color WHITE = white;
    public static final Color lightGray = new Color(192, 192, 192);
    public static final Color LIGHT_GRAY = lightGray;
    public static final Color gray = new Color(128, 128, 128);
    public static final Color GRAY = gray;
    public static final Color darkGray = new Color(64, 64, 64);
    public static final Color DARK_GRAY = darkGray;
    public static final Color black = new Color(0, 0, 0);
    public static final Color BLACK = black;
    public static final Color red = new Color(255, 0, 0);
    public static final Color RED = red;
    public static final Color pink = new Color(255, 175, 175);
    public static final Color PINK = pink;
    public static final Color orange = new Color(255, 200, 0);
    public static final Color ORANGE = orange;
    public static final Color yellow = new Color(255, 255, 0);
    public static final Color YELLOW = yellow;
    public static final Color green = new Color(0, 255, 0);
    public static final Color GREEN = green;
    public static final Color magenta = new Color(255, 0, 255);
    public static final Color MAGENTA = magenta;
    public static final Color cyan = new Color(0, 255, 255);
    public static final Color CYAN = cyan;
    public static final Color blue = new Color(0, 0, 255);
    public static final Color BLUE = blue;

    static ArrayList<Color> indexedColors = new ArrayList<>(Arrays.asList(
            Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.PINK,
            Color.BLACK, Color.WHITE, Color.GRAY, Color.ORANGE, Color.CYAN,
            Color.MAGENTA, Color.LIGHT_GRAY, Color.DARK_GRAY));

    private int value;
    private double r, g, b, a;
    static final private double FACTOR = 0.7;

    static final int ALPHA_MASK = 255 << 24;

    // We expect the indexedColors array to always be the same Colors. The associated Random thus uses a constant seed.
    static private Random intColorsRandom = new Random(0);

    /**
     * <p>Creates a RGBA color from the provided integer representation.</p>
     *
     * @param r value of the red component, as a [0,255] integer.
     * @param g value of the green component, as a [0,255] integer.
     * @param b value of the blue component, as a [0,255] integer.
     * @param a value of the alpha component, as a [0,255] integer.
     */
    public Color(int r, int g, int b, int a) {
        value = ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                ((b & 0xFF) << 0);
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        testColorValueRange(r, g, b, a);
    }

    /**
     * <p>Creates an opaque RGB (no alpha component) color from the provided integer representation.</p>
     *
     * @param r value of the red component, as a [0,255] integer.
     * @param g value of the green component, as a [0,255] integer.
     * @param b value of the blue component, as a [0,255] integer.
     */
    public Color(int r, int g, int b) {
        this(r, g ,b, 255);
    }


    /**
     * <p>Creates an opaque RGB (no alpha component) color from the provided integer representation.</p>
     * @param value the combined RGB component, as an integer.
     */
    public Color(int value) {
        this(value, false);
    }

    /**
     * <p>Creates a RGB color from the provided integer representation.</p>
     * @param value the combined RGBA component, as an integer.
     * @param hasalpha <code>true</code> if the alpha component should be taken into account.
     */
    public Color(int value, boolean hasalpha) {
        if (!hasalpha)
            value |= ALPHA_MASK;

        this.value = value;
        this.a = (value >> 24) & 0xFF;
        this.r = (value >> 16) & 0xFF;
        this.g = (value >>  8) & 0xFF;
        this.b = (value >>  0) & 0xFF;
    }

    /**
     * <p>Copy constructor.</p>
     * @param color the {@link Color} to be copied.
     */
    public Color(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
        this.value = color.value;
    }

    /**
     * <p>Gets the red component in the default sRGB space.</p>
     * @return the red component, a [0,255] integer.
     */
    public int getRed() {
        return (int) r;
    }

    /**
     * <p>Gets the green component in the default sRGB space.</p>
     * @return the green component, a [0,255] integer.
     */
    public int getGreen() {
        return (int) g;
    }

    /**
     * <p>Gets the blue component in the default sRGB space.</p>
     * @return the blue component, a [0,255] integer.
     */
    public int getBlue() {
        return (int) b;
    }

    /**
     * <p>Gets the alpha component.</p>
     * @return the alpha component, a [0,255] integer.
     */
    public int getAlpha() {
        return (int) a;
    }

    /**
     * <p>Gets the RGB value representing the color.</p>
     * @return the RGB value.
     */
    public int getRGB() {
        return value;
    }

    /**
     * <p>Returns the list of currently indexed {@link Color}s.</p>
     * @return the list of currently indexed {@link Color}s.
     * @see #indexOf(Color)
     * @see #getColorAt(Integer)
     */
    public static List<Color> getIndexedColors() {
        return indexedColors;
    }

    private void testColorValueRange(int r, int g, int b, int a) {
        boolean rangeError = false;
        String badComponentString = "";

        if (a < 0 || a > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Alpha";
        }
        if (r < 0 || r > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Red";
        }
        if (g < 0 || g > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Green";
        }
        if (b < 0 || b > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Blue";
        }
        if (rangeError == true) {
            throw new IllegalArgumentException("Color parameter outside of expected range:"
                    + badComponentString);
        }
    }

    /**
     * <p>Converts a {@link String} to an integer and returns the specified opaque {@link Color}.</p>
     * <p>This method handles string formats that are used to represent octal and hexadecimal numbers.</p>
     *
     * @param colorValue a {@link String} that represents an opaque color as a 24-bit integer
     * @return the new {@link Color} object.
     * @see java.lang.Integer#decode
     * @exception  NumberFormatException  if the specified string cannot be interpreted as a decimal, octal,
     * or hexadecimal integer.
     */
    public static Color decode(String colorValue) throws NumberFormatException {
        Integer intval  = Integer.decode(colorValue);
        int i = intval.intValue();
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }


    /**
     * <p>Finds a RGB color in the system properties.</p>
     * <p>The argument is treated as the name of a system property to be obtained. The string value of this property is
     * then interpreted as an integer which is then converted to a {@link Color} object.</p>
     * <p>If the specified property is not found or could not be parsed as an integer then <code>null</code>
     * is returned.</p>
     *
     * @param name the name of the color property.
     * @return the {@link Color} converted from the system property.
     * @see java.lang.System#getProperty(java.lang.String)
     * @see java.lang.Integer#getInteger(java.lang.String)
     * @see #Color(int)
     */
    public static Color getColor(String name) {
        return getColor(name, null);
    }


    /**
     * <p>Finds a RGB color in the system properties.</p>
     * <p>The first argument is treated as the name of a system property to
     * be obtained. The string value of this property is then interpreted
     * as an integer which is then converted to a {@link Color} object. </p>
     * <p>If the specified property is not found or could not be parsed as
     * an integer then the {@link Color} <code>defaultValue</code> is used instead.</p>
     *
     * @param name the name of the color property.
     * @param defaultValue the default color value, a {@link Color} object.
     * @return the {@link Color}  converted from the system property or the {@link Color}  converted from the
     * specified integer.
     * @see java.lang.System#getProperty(java.lang.String)
     * @see java.lang.Integer#getInteger(java.lang.String)
     * @see #Color(int)
     */
    public static Color getColor(String name, Color defaultValue) {
        Integer intval = Integer.parseInt(name);
        if (intval == null) {
            return defaultValue;
        }
        int i = intval.intValue();
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }

    /**
     * <p>Finds a RGB color in the system properties.</p>
     * <p>The first argument is treated as the name of a system property to be obtained. The string value of this
     * property is then interpreted as an integer which is then converted to a {@link Color} object. </p>
     * <p>If the specified property is not found or could not be parsed as an integer then the integer value
     * <code>defaultValue</code> is used instead, and is converted to a {@link Color} object.</p>
     *
     * @param name the name of the color property
     * @param defaultValue the default color value, as an integer
     * @return the {@link Color} converted from the system property or the {@link Color} converted from the
     * specified integer.
     * @see java.lang.System#getProperty(java.lang.String)
     * @see java.lang.Integer#getInteger(java.lang.String)
     * @see #Color(int)
     */
    public static Color getColor(String name, int defaultValue) {
        Integer intval = Integer.getInteger(name);
        int i = (intval != null) ? intval.intValue() : defaultValue;
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, (i >> 0) & 0xFF);
    }

    /**
     * <p>Creates a new {@link Color} that is a brighter version of this {@link Color}.</p>
     *
     * <p>This method applies an arbitrary scale factor to each of the three RGB
     * components of this {@link Color} to create a brighter version
     * of this {@link Color}.
     * The {@code alpha} value is preserved.</p>
     * <p>Although {@link #brighter()} and {@link #darker()} are inverse operations, the results of a series of
     * invocations of these two methods might be inconsistent because of rounding errors.</p>
     * @return a new {@link Color} object that is a brighter version of this {@link Color} with the same {@code alpha}
     * value.
     * @see #darker()
     */
    public Color brighter() {
        int r = getRed();
        int g = getGreen();
        int b = getBlue();
        int alpha = getAlpha();

        int i = (int) (1.0 / (1.0 - FACTOR));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if (r > 0 && r < i) r = i;
        if (g > 0 && g < i) g = i;
        if (b > 0 && b < i) b = i;

        return new Color(Math.min((int) (r / FACTOR), 255),
                Math.min((int) (g / FACTOR), 255),
                Math.min((int) (b / FACTOR), 255),
                alpha);
    }

    /**
     * <p>Creates a new {@link Color} that is a darker version of this {@link Color}.</p>
     *
     * <p>This method applies an arbitrary scale factor to each of the three RGB components of this {@link Color} to
     * create a darker version of this {@link Color}.
     * The {@code alpha} value is preserved.</p>
     * <p>Although {@link #brighter()} and {@link #darker()} are inverse operations, the results of a series of
     * invocations of these two methods might be inconsistent because of rounding errors.</p>
     * @return  a new {@link Color} object that is a darker version of this {@link Color} with the same {@code alpha} value.
     * @see #brighter()
     */
    public Color darker() {
        return new Color(Math.max((int) (getRed() * FACTOR), 0),
                Math.max((int) (getGreen() * FACTOR), 0),
                Math.max((int) (getBlue() * FACTOR), 0),
                getAlpha());
    }

    /**
     * <p>Returns the {@link Color} associated to the provided index.</p>
     * <p>The {@link Color} returned for a specific index will remain the same across instances.</p>
     * <p>This method is typically used to associate a {@link Color} to an identifier. Missing {@link Color}s are thus
     * generated (from {@code 0} to {@code intColor}) on demand, and stored for later use.</p>
     * @param colorIndex the index of the {@link Color}.
     * @return the {@link Color} associated to the provided index.
     * @see #indexOf(Color)
     * @see #getIndexedColors()
     */
    public static Color getColorAt(Integer colorIndex) {
        while (Color.indexedColors.size() <= colorIndex)
            Color.indexedColors.add(Color.getRandomColor(intColorsRandom));

        return Color.indexedColors.get(colorIndex);
    }

    /**
     * <p>Searches for the color index associated with the current {@link Color}.</p>
     * <p>Only already generated and indexed {@link Color}s can be found.</p>
     *
     * @param color the {@link Color} to be searched for.
     * @return the associated color index. {@code -1} if not found.
     * @see #getColorAt(Integer)
     * @see #getIndexedColors()
     */
    public static int indexOf(Color color) {
        return Color.indexedColors.indexOf(color);
    }

    /**
     * <p>Returns a {@link Color} object generated using 3 calls to the provided {@link Random} object.</p>
     * <p>If the provided random is null, the result of {@link #getRandomColor()} is returned.</p>
     * @param random a {@link Random} object to be used. Can be null.
     * @return the generated {@link Color} object.
     * @see #getRandomColor()
     */
    public static Color getRandomColor(Random random) {
        if(random == null)
          return getRandomColor();

        return new Color(nextRandomRGBComponent(random), nextRandomRGBComponent(random), nextRandomRGBComponent(random));
    }

    private static int nextRandomRGBComponent(Random random) {
        return (int) (random.nextDouble() * 255);
    }

    /**
     * <p>Returns a randomly generated {@link Color} object.</p>
     * <p>The {@link Color} is created by using 3 calls to {@link Math#random()}.</p>
     * @return the generated {@link Color} object.
     * @see #getRandomColor(Random)
     */
    public static Color getRandomColor() {
        return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Color))
            return false;

        return getRGB() == ((Color) obj).getRGB();
    }
}
