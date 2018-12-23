package com.gameobjects;

import com.enumerations.ItemType;
import com.enumerations.SpriteType;

import java.awt.*;

public class Consumable extends Item {

    private int amount;

    // TODO: type of consumable: healing, mana, energy?

    public Consumable(Point tilePos, ItemType itemType, SpriteType spriteType, int amount) {
        super(tilePos, itemType, spriteType);
        this.amount = amount;
    }

    public void consume(Actor target) {
        target.getHealth().healDamage(amount);
        this.deactivate();
        this.disableCollisions();
        this.setDeleted(true);
    }
}
