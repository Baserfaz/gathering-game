package com.gameobjects;

import com.enumerations.ItemType;
import com.enumerations.SpriteType;

import java.awt.*;

public class Gold extends Item {

    protected int amount;

    public Gold(Point tilePos, int amount) {
        super(tilePos, ItemType.GOLD, SpriteType.GOLD);
        this.amount = amount;
    }

    public void pickup(Player target) {
        target.getInventory().addGold(amount);
        this.deactivate();
        this.disableCollisions();
        this.setDeleted(true);
    }
}
