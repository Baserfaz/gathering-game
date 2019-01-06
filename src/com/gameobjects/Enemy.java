package com.gameobjects;

import com.enumerations.SpriteType;

import java.awt.*;

public abstract class Enemy extends Actor {

    public Enemy(String name, Point tilePos,
                 SpriteType spriteType, int hp, int damage) {
        super(name, tilePos, spriteType, hp, damage);
    }

}
