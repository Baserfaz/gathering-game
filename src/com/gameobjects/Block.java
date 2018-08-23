package com.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map.Entry;

import com.engine.Game;
import com.enumerations.BlockType;
import com.enumerations.Direction;
import com.enumerations.SpriteType;

public class Block extends GameObject {
    
    private BlockType blockType;
    private Point gridPosition;
    
    private Item item;
    
    public Block(Point worldPos, Point gridPosition, BlockType blockType, SpriteType type) {
        super(worldPos, type);
        
        this.blockType = blockType;
        this.gridPosition = gridPosition;
        
    }

    public void tick() {}
    
    public void render(Graphics g) {
        if(this.isVisible) {
            
            // draw floor/base
            g.drawImage(defaultStaticSprite, worldPosition.x, worldPosition.y, null);
            
        }
    }
    
    public void changeBlock(BlockType bt, SpriteType st) {
        this.blockType = bt;
        this.defaultStaticSprite = Game.instance.getSpriteStorage().getSprite(st);
    }
    
    public Item getItem() {
        return this.item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
    
    // ------------- GETTERS & SETTERS ----------------
    
    public BlockType getBlocktype() { return blockType; }
    public void setBlocktype(BlockType blocktype) { this.blockType = blocktype; }
    
    public Point getGridPosition() { return gridPosition; }
    public void setGridPosition(Point gridPosition) { this.gridPosition = gridPosition; }
}

