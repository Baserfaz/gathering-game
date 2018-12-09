package com.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {

    public String name;
    private ArrayList<BufferedImage> frames;

    public Animation() {
        this.name = "Unnamed animation";
        this.frames = new ArrayList<>();
    }

    public Animation(String name, ArrayList<BufferedImage> frames) {
        this.name = name;
        this.frames = frames;
    }

    public BufferedImage getFrame(int index) {
        if(index < 0 || index > this.frames.size()) return null;
        return this.frames.get(index);
    }

    public String getName() {
        return name;
    }

    public ArrayList<BufferedImage> getFrames() {
        return frames;
    }

    public void addFrame(BufferedImage frame) {
        this.frames.add(frame);
    }
}
