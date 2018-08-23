package com.gameobjects;

import java.awt.Graphics;
import java.awt.Point;

import com.enumerations.SpriteType;

public class Item extends GameObject {

    public Item(Point worldPos, SpriteType type) {
        super(worldPos, type);
        
        
        
        
    }

    @Override
    public void tick() {
        

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(defaultStaticSprite, worldPosition.x, worldPosition.y, null);
    }

}
