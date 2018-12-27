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

    private int rechargeTime = 1500;
    private long rechargeTimer = 0l;
    private long lastTime = 0l;

    // cache the original sprite
    private BufferedImage defaultImage;

    public SpikeTrap(Point tilePos, int damage, DamageType damageType) {
        super(tilePos, ItemType.SPIKE_TRAP, SpriteType.SPIKE_DOWN);
        this.defaultImage = defaultStaticSprite;

        this.damageType = damageType;
        this.trapDamage = damage;
    }

    public void tick() {
        if(this.isEnabled) {

            // get delta time between ticks
            long now = System.nanoTime();
            long deltaTime = now - lastTime;
            lastTime = now;

            if(!this.activated) return;

            if(rechargeTimer < rechargeTime) {
                this.rechargeTimer += deltaTime * 0.000001;
            } else {
                this.deactivateTrap();
                this.rechargeTimer = 0l;
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
}
