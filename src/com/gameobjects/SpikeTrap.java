package com.gameobjects;

import com.engine.Game;
import com.enumerations.DamageType;
import com.enumerations.ItemType;
import com.enumerations.SpriteType;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpikeTrap extends Item {

    private int trapDamage;
    private DamageType damageType;

    private boolean activated = false;
    private boolean isAutomatic;

    private int rechargeTime = 1500;
    private long rechargeTimer = 0l;
    private long lastTime = 0l;

    // cache the original sprite
    private BufferedImage defaultImage;

    public SpikeTrap(Point tilePos, int damage, DamageType damageType, boolean isAutomatic) {
        super(tilePos, ItemType.SPIKE_TRAP, SpriteType.SPIKE_DOWN);
        this.defaultImage = defaultStaticSprite;

        this.isAutomatic = isAutomatic;
        this.damageType = damageType;
        this.trapDamage = damage;
    }

    public void tick() {

        // get delta time between ticks
        long now = System.nanoTime();
        long deltaTime = now - lastTime;
        lastTime = now;

        if(this.activated) {

            // -> trap is up
            if (rechargeTimer < rechargeTime) {
                this.rechargeTimer += deltaTime * 0.000001;
            } else {
                this.deactivateTrap();
                this.rechargeTimer = 0l;
            }

        } else {

            // -> trap is down
            if(isAutomatic) {

                if (rechargeTimer < rechargeTime) {
                    this.rechargeTimer += deltaTime * 0.000001;
                } else {
                    this.activateTrap();
                    this.rechargeTimer = 0l;
                }
            }
        }

    }

    public void activateTrap() {
        this.activated = true;
        this.defaultStaticSprite = Game.instance.getSpriteStorage().getSprite(SpriteType.SPIKE_UP);
    }

    public void deactivateTrap() {
        this.activated = false;
        this.defaultStaticSprite = this.defaultImage;
    }

    public boolean isTrapActivated() {
        return this.activated;
    }

    public int getTrapDamage() {
        return trapDamage;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public boolean isAutomatic() {
        return isAutomatic;
    }
}
