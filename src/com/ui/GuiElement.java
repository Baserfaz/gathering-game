package com.ui;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GuiElement {

    protected int x, y, w, h;
    protected boolean isMuted = false, isHovering = false, isEnabled = true, isVisible = true;

    public GuiElement(Panel parent) {
        this.x = parent.x;
        this.y = parent.y;
        this.w = parent.w;
        this.h = parent.h;
    }

    public GuiElement(int w, int h) {
        this.x = 0;
        this.y = 0;
        this.w = w;
        this.h = h;
    }

    public GuiElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
    }

    public abstract void render(Graphics g);
    public abstract void tick();

    // ---- GETTERS & SETTERS ----
    public Rectangle getBounds() { return new Rectangle(this.x, this.y, this.w, this.h); }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean isEnabled) { this.isEnabled = isEnabled; }
    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean isVisible) { this.isVisible = isVisible; }
    public int getWidth() { return w; }
    public void setWidth(int width) { this.w = width; }
    public int getHeight() { return h; }
    public void setHeight(int height) { this.h = height; }
    public boolean isMuted() { return this.isMuted; }

}
