package com.gameobjects;

import java.awt.*;

import com.engine.Game;
import com.enumerations.ItemType;
import com.enumerations.SpriteType;
import com.interfaces.ICollidable;

public class Item extends GameObject implements ICollidable {

    private boolean isCollidable;
    private Rectangle hitbox;
    private ItemType itemType;

    public Item(Point tilePos, ItemType itemType, SpriteType spriteType) {
        super(tilePos, spriteType);

        this.itemType = itemType;

        Point pos = this.getWorldPosition();
        hitbox = new Rectangle(pos.x, pos.y,
                Game.CALCULATED_SPRITE_SIZE, Game.CALCULATED_SPRITE_SIZE);
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
            g.drawImage(defaultStaticSprite, worldPosition.x, worldPosition.y, null);
        }
    }

    @Override
    public void onCollision(ICollidable other) {

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
