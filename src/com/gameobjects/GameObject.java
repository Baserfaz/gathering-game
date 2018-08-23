package com.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.engine.Game;
import com.enumerations.SpriteType;

public abstract class GameObject {

    protected Point worldPosition;
    
    protected BufferedImage defaultStaticSprite;
    
    protected Rectangle hitbox;
    
    protected boolean hasFocus = false;
    protected boolean isEnabled = false;
    protected boolean isVisible = false;
    
    public GameObject(Point worldPos, SpriteType type) {

        // set world position
        this.worldPosition = worldPos;
        
        // create sprites
        this.defaultStaticSprite = Game.instance.getSpriteStorage().getSprite(type);
        
        // add to handler
        Game.instance.getHandler().AddObject(this);
        
        this.recalculateBoundingBox();
        
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    
    public void recalculateBoundingBox() {
                
        int[] pixels = defaultStaticSprite.getRGB(0, 0, defaultStaticSprite.getWidth(),
                defaultStaticSprite.getHeight(), null, 0, defaultStaticSprite.getWidth());
        
        int x = this.worldPosition.x, y = this.worldPosition.y;
        int w = defaultStaticSprite.getWidth(), h = defaultStaticSprite.getHeight();
        
        int largestX = 0;
        int smallestX = w;
        int largestY = 0;
        int smallestY = h;
        
        for(int i = 0; i < pixels.length; i++) {
         
            int current = pixels[i];
            int alpha = (current & 0xff000000) >>> 24;
            
            if(alpha == 255) {
                
                // pixel position in the sprite
                int yy = i / w;
                int xx = i % w;
                
                if(yy < smallestY) {
                    smallestY = yy;
                } else if(yy > largestY) {
                    largestY = yy;
                }
                
                if(xx > largestX) {
                    largestX = xx;
                } else if(xx < smallestX) {
                    smallestX = xx;
                }
                
            }
        }
        
        y += smallestY;
        x += smallestX;
        w = largestX - smallestX;
        h = largestY - smallestY;
        
        // update hitbox 
        this.setBoundingBoxSize(x, y, w, h);
        
    }
    
    public void setBoundingBoxSize(int x, int y, int w, int h) {
        
        if(this.hitbox == null) { this.hitbox = new Rectangle(); }
        
        this.hitbox.x = x;
        this.hitbox.y = y;
        this.hitbox.width = w;
        this.hitbox.height = h;
    }
    
    public void updateHitbox() {
        this.hitbox.x += this.worldPosition.x;
        this.hitbox.y += this.worldPosition.y;
    }
    
    public String getInfo() {
        return "GameObject: " + this.toString() + " worldPos: (" +
                this.getWorldPosition().x + ", " + this.getWorldPosition().y + ")";
    }

    public void addWorldPosition(float x, float y) { 
        this.worldPosition = new Point(this.worldPosition.x + Math.round(x),
                this.worldPosition.y + Math.round(y));
    }
    
    // ---- GETTERS & SETTERS ----
    public Point getCenterPoint() { return new Point(this.hitbox.x, this.hitbox.y); }
    public Rectangle getHitbox() { return this.hitbox; }
    public void setSprite(BufferedImage i) { this.defaultStaticSprite = i; }
    public BufferedImage getSprite() { return this.defaultStaticSprite; }
    public void setWorldPosition(int x, int y) { this.worldPosition = new Point(x, y); }
    public void setWorldPosition(Point pos) { this.worldPosition = pos; }
    public Point getWorldPosition() { return this.worldPosition; }
    public boolean getIsVisible() { return this.isVisible; }
    public boolean getIsEnabled() { return this.isEnabled; }
    public void setIsVisible(boolean b) { this.isVisible = b; }
    public void setIsEnabled(boolean b) { this.isEnabled = b; }
    
    public void activate() { 
        this.isVisible = true;
        this.isEnabled = true;
    }
    
    public void deactivate() {
        this.isVisible = false;
        this.isEnabled = false;
    }

}
