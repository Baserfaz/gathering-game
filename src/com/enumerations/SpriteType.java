package com.enumerations;

import com.engine.Game;

public enum SpriteType {
    NONE,
    PLAYER,
    FLOOR,
    NORTH_WALL,
    SOUTH_WALL,
    WEST_WALL,
    EAST_WALL,
    NORTH_WALL_TOP,
    NW_WALL,
    NE_WALL,
    SW_WALL,
    SE_WALL,
    APPLE,
    LADDER,
    STONE_1,
    CHEST_CLOSED,
    CHEST_OPEN,
    CHEST_LOCKED,
    BUTTON_UP,
    BUTTON_DOWN,
    GOLD,
    GUI_HEALTH(true);

    private boolean guiElement;

    SpriteType(Boolean b) { this.guiElement = b; }
    SpriteType() { this.guiElement = false; }

    public int getSizeMultiplier() {
        return this.guiElement
            ? Game.SPRITE_UI_SIZE_MULT
            : Game.SPRITE_SIZE_MULT;
    }
}
