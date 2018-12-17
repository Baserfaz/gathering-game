package com.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.engine.Game;
import com.enumerations.SpriteType;

public abstract class GameObject {

    protected Point tilePosition;
    protected Point worldPosition;
    protected BufferedImage defaultStaticSprite;

    protected boolean isEnabled = false;
    protected boolean isVisible = false;

    public GameObject(Point tilePosition, SpriteType type) {

        this.tilePosition = tilePosition;

        // calculate world position
        int size = Game.CALCULATED_SPRITE_SIZE;
        int margin = 0;

        this.worldPosition = new Point(
                tilePosition.x * size + tilePosition.x * margin,
                tilePosition.y * size + tilePosition.y * margin);
        
        // create sprites
        this.defaultStaticSprite = Game.instance.getSpriteStorage().getSprite(type);
        
        // add to handler
        Game.instance.getHandler().AddObject(this);
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    /**
     * Calculates the hitbox size based on the given sprite.
     * Doesn't care about full alpha pixels.
     * @return
     */
    public Rectangle calculateBoundingBox() {

        int[] pixels = defaultStaticSprite.getRGB(0, 0,
                        defaultStaticSprite.getWidth(), defaultStaticSprite.getHeight(),
                        null, 0, defaultStaticSprite.getWidth());

        int x = this.worldPosition.x, y = this.worldPosition.y;
        int w = defaultStaticSprite.getWidth(), h = defaultStaticSprite.getHeight();

        int largestX = 0, smallestX = w;
        int largestY = 0, smallestY = h;

        for(int i = 0; i < pixels.length; i++) {

            int current = pixels[i];
            int alpha = (current & 0xff000000) >>> 24;

            if(alpha == 255) {

                // pixel position in the sprite convert from 1D to 2D array.
                int yy = i / w;
                int xx = i % w;

                if(yy < smallestY) { smallestY = yy; }
                else if(yy > largestY) { largestY = yy; }

                if(xx > largestX) { largestX = xx; }
                else if(xx < smallestX) { smallestX = xx; }
            }
        }

        y += smallestY;
        x += smallestX;
        w = largestX - smallestX;
        h = largestY - smallestY;

        return new Rectangle(x, y, w, h);
    }

    public String getInfo() {
        return "GameObject: " + this.toString() + " worldPos: (" +
                this.getWorldPosition().x + ", " + this.getWorldPosition().y + ")"
                + " tilePos: (" + this.getTilePosition().x + ", " + this.getTilePosition().y + ")";
    }

    public void addWorldPosition(int x, int y) {
        this.worldPosition = new Point(this.worldPosition.x + x,
                this.worldPosition.y + y);
    }

    public Point getWorldPosition() { return this.worldPosition; }
    public Point getTilePosition() { return this.tilePosition; }
    public void setTilePosition(Point p) { this.tilePosition = p; }
    public void setWorldPosition(Block block) { this.worldPosition = block.getWorldPosition(); }

    public Point getCenterPosition() {
        int size = Game.CALCULATED_SPRITE_SIZE / 2;
        int xx = this.worldPosition.x + size;
        int yy = this.worldPosition.y + size;
        return new Point(xx, yy);
    }

    public void activate() { 
        this.isVisible = true;
        this.isEnabled = true;
    }
    
    public void deactivate() {
        this.isVisible = false;
        this.isEnabled = false;
    }

}
