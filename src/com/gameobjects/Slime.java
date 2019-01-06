package com.gameobjects;

import com.enumerations.SpriteType;

import java.awt.*;

public class Slime extends Enemy {

    public Slime(Point tilePos) {
        super("Slime", tilePos, SpriteType.SLIME_NORMAL, 3, 1);
    }

    @Override
    public void tick() {
        super.tick();
        this.updateBehaviour();
    }

    private void updateBehaviour() {

        

    }

}
