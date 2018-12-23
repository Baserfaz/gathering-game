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

    private boolean isCollidable;
    private Rectangle hitbox;
    private ItemType itemType;
    private Direction lookDirection;

    public Item(Point tilePos, ItemType itemType, SpriteType spriteType) {
        super(tilePos, spriteType);
        this.itemType = itemType;
        hitbox = this.calculateBoundingBox();

        // randomize look direction of the item
        int r = Util.GetRandomInteger(0, 100);
        if(r < 50) this.lookDirection = Direction.WEST;
        else this.lookDirection = Direction.EAST;

        // set sprite
        if(this.lookDirection == Direction.EAST) {
            this.defaultStaticSprite = RenderUtils.flipSpriteHorizontally(this.defaultStaticSprite);
        }
    }

    @Override
    public void tick() {
        if(this.isEnabled && this.isVisible) {
            this.isCollidable = true;

            // TODO: update hitbox position if the item can move

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
    public void onCollision(ICollidable other) {
        if (other instanceof Actor) {
            Actor actor = (Actor) other;
            if(actor.getUnitType() == UnitType.PLAYER_UNIT) {

                // if player collided with this item
                // TODO switch cases for item types

                System.out.println("Player collided with " + itemType);

            }
        }
    }

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
