package jbotsim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * * Original code AWT
 */
public class Color {
    public static Color white = new Color(255, 255, 255, 255);
    public static Color WHITE = white;
    public static Color lightGray = new Color(192, 192, 192);
    public static Color LIGHT_GRAY = lightGray;
    public static Color gray = new Color(128, 128, 128);
    public static Color GRAY = gray;
    public static Color darkGray = new Color(64, 64, 64);
    public static Color DARK_GRAY = darkGray;
    public static Color black = new Color(0, 0, 0);
    public static Color BLACK = black;
    public static Color red = new Color(255, 0, 0);
    public static Color RED = red;
    public static Color pink = new Color(255, 175, 175);
    public static Color PINK = pink;
    public static Color orange = new Color(255, 200, 0);
    public static Color ORANGE = orange;
    public static Color yellow = new Color(255, 255, 0);
    public static Color YELLOW = yellow;
    public static Color green = new Color(0, 255, 0);
    public static Color GREEN = green;
    public static Color magenta = new Color(255, 0, 255);
    public static Color MAGENTA = magenta;
    public static Color cyan = new Color(0, 255, 255);
    public static Color CYAN = cyan;
    public static Color blue = new Color(0, 0, 255);
    public static Color BLUE = blue;
    static ArrayList<Color> basicColors = new ArrayList<>(Arrays.asList(
            Color.blue, Color.red, Color.green, Color.yellow, Color.pink,
            Color.black, Color.white, Color.gray, Color.orange, Color.cyan,
            Color.magenta, Color.lightGray, Color.darkGray));

    private float falpha = 0;
    int value = 0;
    private double r, g, b, a;
    private double FACTOR = 0.7;

    static final int ALPHA_MASK = 255 << 24;

    public Color(int r, int g, int b, int a) {
        value = ((255 & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                ((b & 0xFF) << 0);
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(int r, int g, int b) {
        value = ((255 & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                ((b & 0xFF) << 0);
        this.r = r;
        this.g = g;
        this.b = b;
    }


    public Color(int value) {
        this(value, false);
    }


    public Color(int value, boolean hasalpha) {
        if (hasalpha)
            falpha = ((value & ALPHA_MASK) >> 24) / 255f;
        else {
            value |= ALPHA_MASK;
            falpha = 1;
        }
        this.value = value;
    }

    public int getRed() {
        return (int) r;
    }

    public int getGreen() {
        return (int) g;
    }

    public int getBlue() {
        return (int) b;
    }

    public int getAlpha() {
        return (int) a;
    }

    public int getRGB() {
        return value;
    }

    public static List<Color> getBasicColors() {
        return basicColors;
    }

    public void testColorValueRange(int r, int g, int b, int a) {
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


    public void testColorValueRange(float r, float g, float b, float a) {
        boolean rangeError = false;
        String badComponentString = "";
        if (a < 0.0 || a > 1.0) {
            rangeError = true;
            badComponentString = badComponentString + " Alpha";
        }
        if (r < 0.0 || r > 1.0) {
            rangeError = true;
            badComponentString = badComponentString + " Red";
        }
        if (g < 0.0 || g > 1.0) {
            rangeError = true;
            badComponentString = badComponentString + " Green";
        }
        if (b < 0.0 || b > 1.0) {
            rangeError = true;
            badComponentString = badComponentString + " Blue";
        }
        if (rangeError == true) {
            throw new IllegalArgumentException("Color parameter outside of expected range:"
                    + badComponentString);
        }
    }


    public static Color decode(String nm) throws NumberFormatException {
        Integer intval = Integer.decode(nm);
        int i = intval.intValue();
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }


    public static Color getColor(String nm) {
        return getColor(nm, null);
    }


    public static Color getColor(String nm, Color v) {
        Integer intval = Integer.parseInt(nm);
        if (intval == null) {
            return v;
        }
        int i = intval.intValue();
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }


    public static Color getColor(String nm, int v) {
        Integer intval = Integer.parseInt(nm);
        int i = (intval != null) ? intval.intValue() : v;
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, (i >> 0) & 0xFF);
    }


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

    public Color darker() {
        return new Color(Math.max((int) (getRed() * FACTOR), 0),
                Math.max((int) (getGreen() * FACTOR), 0),
                Math.max((int) (getBlue() * FACTOR), 0),
                getAlpha());
    }

    public static Color getRandomColor() {
        return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }
}
