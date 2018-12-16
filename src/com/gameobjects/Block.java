package com.gameobjects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.engine.Game;
import com.enumerations.BlockType;
import com.enumerations.SpriteType;
import com.interfaces.ICollidable;

public class Block extends GameObject implements ICollidable {
    
    private BlockType blockType;

    private List<Actor> actors;
    private List<Item> items;

    private boolean isCollidable;
    private Rectangle hitbox;

    public Block(Point tilePos, BlockType blockType, SpriteType type) {
        super(tilePos, type);
        this.blockType = blockType;
        this.items = new ArrayList<>();
        this.actors = new ArrayList<>();

        if(blockType == BlockType.UNWALKABLE) { this.isCollidable = true; }
        else { this.isCollidable = false; }

        // create hitbox
        int size = Game.CALCULATED_SPRITE_SIZE;
        this.hitbox = new Rectangle(this.worldPosition.x, this.worldPosition.y, size, size);
    }

    public void tick() {}
    
    public void render(Graphics g) {
        if(this.isVisible) {
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

    @Override
    public boolean isActive() {
        return isCollidable;
    }

    @Override
    public void disableCollisions() {
        this.isCollidable = false;
    }

    @Override
    public void enableCollisions() {
        this.isCollidable = true;
    }

    @Override
    public Rectangle getHitbox() {
        return this.hitbox;
    }
}

