package com.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.enumerations.Direction;
import com.enumerations.ItemType;
import com.enumerations.SpriteType;
import com.enumerations.UnitType;
import com.interfaces.ICollidable;
import com.utilities.RenderUtils;
import com.utilities.Util;

public class Item extends GameObject implements ICollidable {

    protected boolean isCollidable;
    protected Rectangle hitbox;
    protected ItemType itemType;
    protected Direction lookDirection;

    public Item(Point tilePos, ItemType itemType, SpriteType spriteType) {
        super(tilePos, spriteType);
        this.itemType = itemType;

        // randomize look direction of the item
        int r = Util.GetRandomInteger(0, 100);
        if(r < 50) this.lookDirection = Direction.WEST;
        else this.lookDirection = Direction.EAST;

        // set sprite, west is already set
        if(this.lookDirection == Direction.EAST) {
            this.defaultStaticSprite = RenderUtils.flipSpriteHorizontally(this.defaultStaticSprite);
        }

        this.hitbox = this.calculateBoundingBox();
    }

    @Override
    public void tick() {
        if(this.isEnabled && this.isVisible) {
            this.isCollidable = true;
        } else {
            this.isCollidable = false;
        }
    }

    @Override
    public void render(Graphics g) {
        if(this.isVisible) {
            g.drawImage(this.defaultStaticSprite,
                    worldPosition.x, worldPosition.y, null);
        }
    }

    @Override
    public void onCollision(ICollidable other) {}

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public boolean isActive() {
        return this.isCollidable;
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
