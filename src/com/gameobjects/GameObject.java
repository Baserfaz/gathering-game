package com.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.engine.Game;
import com.enumerations.SpriteType;

public abstract class GameObject {

    protected Point worldPosition;
    protected BufferedImage defaultStaticSprite;

    protected boolean isEnabled = false;
    protected boolean isVisible = false;
    
    public GameObject(Point worldPos, SpriteType type) {

        // set world position
        this.worldPosition = worldPos;
        
        // create sprites
        this.defaultStaticSprite = Game.instance.getSpriteStorage().getSprite(type);
        
        // add to handler
        Game.instance.getHandler().AddObject(this);
        
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    public String getInfo() {
        return "GameObject: " + this.toString() + " worldPos: (" +
                this.getWorldPosition().x + ", " + this.getWorldPosition().y + ")";
    }

    public void addWorldPosition(float x, float y) { 
        this.worldPosition = new Point(this.worldPosition.x + Math.round(x),
                this.worldPosition.y + Math.round(y));
    }

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
