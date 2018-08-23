package com.ui;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GuiElement {

    protected float x = 0;
    protected float y = 0;
    protected int width = 0;
    protected int height = 0;
    
    protected boolean isMuted = false;
    protected boolean isHovering = false;
    protected boolean isEnabled = true;
    protected boolean isVisible = true;
    
    public GuiElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void render(Graphics g);
    public abstract void tick();
    public abstract void onClick();
    public abstract void onHover();
    
    // ---- GETTERS & SETTERS ----
    public Rectangle getBounds() { return new Rectangle((int)this.x, (int)this.y, this.width, this.height); }
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean isEnabled) { this.isEnabled = isEnabled; }
    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean isVisible) { this.isVisible = isVisible; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public void setHovering(boolean b) { this.isHovering = b; }
    public boolean isHovering() { return this.isHovering; }
    public boolean isMuted() { return this.isMuted; }
}
