package com.gameobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map;

import com.data.Health;
import com.engine.Game;
import com.engine.KeyInput;
import com.enumerations.Direction;
import com.enumerations.SpriteType;
import com.enumerations.UnitType;
import com.utilities.RenderUtils;

public class Actor extends GameObject {
    
    protected String name;
    protected Health HP;
    protected UnitType unitType;

    protected Direction facingDirection = Direction.WEST;

    protected int attackDamage = 1;

    protected KeyInput keyInput;
    
    public Actor(String name, Point worldPos, UnitType unitType,
                 SpriteType spriteType, int hp, int damage) {
        super(worldPos, spriteType);
        
        this.name = name;
        this.attackDamage = damage;
        this.unitType = unitType;
        this.HP = new Health(hp, this);
        
        this.keyInput = Game.instance.getKeyInput();
    }
    
    public void tick() {
        if(this.unitType == UnitType.PLAYER_UNIT) this.handleButtons();
    }
    
    public void render(Graphics g) {
        if(this.isVisible) {
            BufferedImage img = this.defaultStaticSprite;
            if(this.facingDirection == Direction.WEST) { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null);
            } else if(this.facingDirection == Direction.EAST) { RenderUtils.renderSpriteFlippedHorizontally(img, this.worldPosition, g);
            } else { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null); }
        }
    }
    
    public void onDeath() {
        this.deactivate();
    }
    
    public void moveUp() {

    }
    
    public void moveDown() {

    }
    
    public void moveLeft() {

        this.facingDirection = Direction.WEST;
    }
    
    public void moveRight() {

        this.facingDirection = Direction.EAST;
    }
    
    public void doAction() {
        
    }
    
    private void handleButtons() {
        Map<Integer, KeyInput.Command> buttons = this.keyInput.getButtons();
        if(buttons.containsValue(KeyInput.Command.MOVE_DOWN)) { this.moveDown(); }
        if(buttons.containsValue(KeyInput.Command.MOVE_UP)) { this.moveUp(); } 
        if(buttons.containsValue(KeyInput.Command.MOVE_LEFT)) { this.moveLeft(); }
        if(buttons.containsValue(KeyInput.Command.MOVE_RIGHT)) { this.moveRight(); }
        if(buttons.containsValue(KeyInput.Command.ACTION)) { this.doAction(); }
    }

    public Health getHP() { return this.HP; }
    public String getName() { return this.name; }
    public Direction getFacingDirection() { return facingDirection; }
    public void setFacingDirection(Direction facingDirection) { this.facingDirection = facingDirection; }

}
