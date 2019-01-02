package com.data;

import com.enumerations.AnimationType;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

public class Animation {

    private String name;
    private AnimationType animationType;
    private ArrayList<BufferedImage> frames;

    public Animation(String name, AnimationType type) {
        this.name = name;
        this.frames = new ArrayList<>();
        this.animationType = type;
    }

    public BufferedImage getFrame(int index) {
        if(this.frames.isEmpty() || index < 0 || index > this.frames.size()) return null;
        return this.frames.get(index);
    }

    public int getFrameCount() { return this.frames.size(); }
    public String getName() {
        return name;
    }
    public ArrayList<BufferedImage> getFrames() {
        return frames;
    }
    public void addFrame(BufferedImage frame) {
        this.frames.add(frame);
    }
    public void addFrames(Collection<BufferedImage> frames) { this.frames.addAll(frames); }
}
