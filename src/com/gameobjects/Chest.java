package com.gameobjects;

import com.engine.Game;
import com.enumerations.Direction;
import com.enumerations.ItemType;
import com.enumerations.SpriteType;
import com.utilities.ItemCreator;
import com.utilities.RenderUtils;

import java.awt.*;

public class Chest extends Item {

    private boolean isOpen = false;
    private boolean isLocked;

    public Chest(Point tilePos, boolean isLocked) {
        super(tilePos, ItemType.CHEST, SpriteType.CHEST_CLOSED);
        this.isLocked = isLocked;

        if(isLocked) {
            this.defaultStaticSprite = Game.instance.getSpriteStorage().getSprite(SpriteType.CHEST_LOCKED);
        }

        this.hitbox = this.calculateBoundingBox();
    }

    public void unlock() {
        if(!this.isLocked || this.isOpen) return;
        this.isLocked = false;
        this.defaultStaticSprite = Game.instance.getSpriteStorage().getSprite(SpriteType.CHEST_CLOSED);
    }

    public void open() {
        if(isLocked || isOpen) return;
        this.isOpen = true;

        this.defaultStaticSprite = Game.instance.getSpriteStorage().getSprite(SpriteType.CHEST_OPEN);
        if(this.lookDirection == Direction.EAST) {
            this.defaultStaticSprite = RenderUtils.flipSpriteHorizontally(this.defaultStaticSprite);
        }

        // TODO: create loot here & randomize
        ItemCreator.createItem(this.tilePosition, ItemType.GOLD);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
