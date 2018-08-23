package com.utilities;

import com.engine.Camera;
import com.engine.Game;
import com.enumerations.Direction;
import com.gameobjects.GameObject;

import java.lang.Math;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Util {

    // ------------------------ RANDOMIZATION -------------------------

    public static Direction GetRandomCardinalDirection() { return Direction.values()[Util.GetRandomInteger(0, 4)]; }
    public static Direction GetRandomOrdinalDirection() { return Direction.values()[Util.GetRandomInteger(4, 8)]; }
    public static int GetRandomInteger() { return ThreadLocalRandom.current().nextInt(0, 101); }
    public static int GetRandomInteger(int min, int max) { return ThreadLocalRandom.current().nextInt(min, max); }

    // ------------------------ MATH ----------------------------------

    // http://stackoverflow.com/questions/16656651/does-java-have-a-clamp-function
    public static int clamp(int val, int min, int max) { return Math.max(min, Math.min(max, val)); }

    // http://www.java-gaming.org/index.php?topic=34706.0
    public static float lerp(float point1, float point2, float alpha) { return point1 + alpha * (point2 - point1); }

    // http://www.java-gaming.org/index.php?topic=34706.0
    public static int lerp(int point1, int point2, float alpha) { return Math.round(point1 + alpha * (point2 - point1)); }

    

    // ------------------ RESOURCE LOADING -----------------------

    public static void loadCustomFont() {

        String fullPath = "/fonts/" + Game.CUSTOMFONTFOLDER + "/" + Game.CUSTOMFONTNAME + Game.CUSTOMFONTEXTENSION;
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

    public static Point calculateCameraPos(Point target) {

        // returns a position of the target, which is centered 
        // to the camera view.
        
        int spriteSize = Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;

        int x = 0, y = 0;
        Camera cam = Game.instance.getCamera();

        if(cam == null) {
            System.out.println("Util::calculateCameraPos: Camera is null!");
            return null;
        }

        if(target != null) {
            x = (-target.x - spriteSize / 2) + Game.CAMERA_WIDTH / 2;
            y = (-target.y - spriteSize / 2) + Game.CAMERA_HEIGHT / 2;
        }

        return new Point(x, y);
    }
}
