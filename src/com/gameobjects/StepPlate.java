package com.gameobjects;

import com.engine.Game;
import com.enumerations.ItemType;
import com.enumerations.SpriteType;

import java.awt.*;

public class StepPlate extends Item {

    private boolean isActivated = false;

    // TODO: pass a type of activation as a parameter
    public StepPlate(Point tilePos) {
        super(tilePos, ItemType.BUTTON, SpriteType.BUTTON_UP);
    }

    public void activatePlate() {
        if(isActivated) return;
        this.isActivated = true;
        this.defaultStaticSprite = Game.instance.getSpriteStorage().getSprite(SpriteType.BUTTON_DOWN);

        // TODO: do activation thing here...
    }
}
