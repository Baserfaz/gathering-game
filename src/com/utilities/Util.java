package com.utilities;

import com.engine.Game;
import com.enumerations.Direction;
import com.enumerations.GameState;
import com.ui.Panel;

import java.lang.Math;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Util {

    public static boolean isCharInArray(char c, char[] arr) {
        for(int i = 0; i < arr.length; i++) {
            if(c == arr[i]) return true;
        }
        return false;
    }

    public static List<Panel> getPanelsInCurrentGamestate() {
        return Game.instance.getGuiElementManager().getPanels(Game.instance.getGamestate());
    }

    // ------------------------ RANDOMIZATION -------------------------

    public static Direction getRandomCardinalDirection() { return Direction.values()[Util.getRandomInteger(0, 4)]; }
    public static Direction getRandomOrdinalDirection() { return Direction.values()[Util.getRandomInteger(4, 8)]; }
    public static int getRandomInteger() { return ThreadLocalRandom.current().nextInt(0, 101); }
    public static int getRandomInteger(int min, int max) { return ThreadLocalRandom.current().nextInt(min, max); }

    // ------------------------ MATH ----------------------------------

    // http://stackoverflow.com/questions/16656651/does-java-have-a-clamp-function
    public static int clamp(int val, int min, int max) { return Math.max(min, Math.min(max, val)); }

    // http://www.java-gaming.org/index.php?topic=34706.0
    public static float lerp(float point1, float point2, float alpha) { return point1 + alpha * (point2 - point1); }

    // http://www.java-gaming.org/index.php?topic=34706.0
    public static int lerp(int point1, int point2, float alpha) { return Math.round(point1 + alpha * (point2 - point1)); }

    

    // ------------------ RESOURCE LOADING -----------------------

    public static void loadCustomFont() {

        String fullPath = "/fonts/" + Game.CUSTOM_FONT_FOLDER + "/" + Game.CUSTOM_FONT_NAME + Game.CUSTOM_FONT_EXTENSION;
        Font font = null;
        
        System.out.println("Trying to load font: " + fullPath);
        
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Util.class.getResourceAsStream(fullPath));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (FontFormatException | IOException e) { e.printStackTrace(); }

        // cache the font
        Game.instance.setCustomFont(font);

        System.out.println("Succesfully loaded custom font: " + font.getFontName());
    }

    // ----------------- OTHER UTILS -------------------------------

    public static String colorToString(Color color) { 
        return color.getRed() + "," + color.getGreen() + "," + color.getBlue(); 
    }

    public static String capitalize(String s) {
        String a = s.substring(0, 1).toUpperCase();
        String b = s.substring(1, s.length()).toLowerCase();
        return a + b;
    }

    public static Color changeAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
