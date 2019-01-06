package com.gameobjects;

import com.engine.Game;
import com.enumerations.SpriteType;

import java.awt.*;

public class Slime extends Enemy {

    private Player player;

    public Slime(Point tilePos) {
        super("Slime", tilePos, SpriteType.SLIME_NORMAL, 3, 1);
    }

    @Override
    public void tick() {
        if(player == null) {
            player = Game.instance.getUnitManager().getPlayer();
        } else {
            super.tick();
            this.updateBehaviour();
        }
    }

    private void updateBehaviour() {



    }

}
