package com.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.engine.Game;
import com.enumerations.BlockType;
import com.enumerations.SpriteType;

public class Block extends GameObject {
    
    private BlockType blockType;

    private List<Actor> actors;
    private List<Item> items;
    
    public Block(Point tilePos, BlockType blockType, SpriteType type) {
        super(tilePos, type);
        this.blockType = blockType;
        this.items = new ArrayList<>();
        this.actors = new ArrayList<>();
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

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public void addActor(Actor a) { this.actors.add(a); }
    public void removeActor(Actor a) { this.actors.remove(a); }

    public List<Item> getItems() { return this.items; }
    public void addItem(Item item) { this.items.add(item); }
    public BlockType getBlocktype() { return blockType; }
    public void setBlocktype(BlockType blocktype) { this.blockType = blocktype; }
}

