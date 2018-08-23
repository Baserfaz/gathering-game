package com.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Mathf {

    public static float lerp(float point1, float point2, float alpha) {
        return point1 + alpha * (point2 - point1);
    }

    public static float clamp(float min, float max, float current) {
        if(current < min) return min;
        else if(current > max) return max;
        else return current;
    }

    public static double randomRange(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }
    
    // https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
}
