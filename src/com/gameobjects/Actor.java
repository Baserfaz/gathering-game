package com.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map;

import com.data.Health;
import com.data.Level;
import com.engine.Game;
import com.engine.KeyInput;
import com.enumerations.Direction;
import com.enumerations.SpriteType;
import com.enumerations.UnitType;
import com.utilities.RenderUtils;

public class Actor extends GameObject {

    final double deaccelerationValue = 0.05;
    final double accelerationValue = 0.05;

    final double maxAcceleration = 1.0;
    final double maxVelocity = 1.0;

    private float lastAccelx, lastAccely;

    protected String name;
    protected Health HP;
    protected UnitType unitType;
    protected Direction facingDirection = Direction.WEST;
    protected int attackDamage;
    protected KeyInput keyInput;
    protected Level level;

    protected float velocity_x, velocity_y;
    protected float acceleration_x, acceleration_y;

    public Actor(String name, Point tilePos, UnitType unitType,
                 SpriteType spriteType, int hp, int damage) {
        super(tilePos, spriteType);
        
        this.name = name;
        this.attackDamage = damage;
        this.unitType = unitType;
        this.HP = new Health(hp, this);
        
        this.keyInput = Game.instance.getKeyInput();
        this.level = Game.instance.getLevel();

        // register this actor the the block we spawned on
        this.level.getBlock(tilePos).addActor(this);

    }

    public void tick() {
        if(this.unitType == UnitType.PLAYER_UNIT) {
            this.handleButtons();
        }

        // velocity x & y can be positive or negative here.
        //if(velocity_x < maxVelocity) {
            velocity_x += acceleration_x;
        //} else {
        //    velocity_x = (int) maxVelocity;
        //}

        //if(velocity_y < maxVelocity) {
            velocity_y += acceleration_y;
        //} else {
        //    velocity_y = (int) maxVelocity;
        //}

        worldPosition.x += velocity_x;
        worldPosition.y += velocity_y;

        // de-accelerate x
        if(acceleration_x > 0) { acceleration_x -= deaccelerationValue; }
        else { acceleration_x += deaccelerationValue; }

        // de-accelerate y
        if(acceleration_y > 0) { acceleration_y -= deaccelerationValue; }
        else { acceleration_y += deaccelerationValue; }

        // if the actor has de-accelerated so that its going to the other direction, just stop
        if(this.keyInput.getButtons().isEmpty()) {
            if (lastAccelx > 0 && acceleration_x < 0 || lastAccelx < 0 && acceleration_x > 0) {
                acceleration_x = 0;
                velocity_x = 0;
            } else if (lastAccely > 0 && acceleration_y < 0 || lastAccely < 0 && acceleration_y > 0) {
                acceleration_y = 0;
                velocity_y = 0;
            }
        }

        lastAccelx = acceleration_x;
        lastAccely = acceleration_y;
    }
    
    public void render(Graphics g) {
        if(this.isVisible) {
            BufferedImage img = this.defaultStaticSprite;
            if(this.facingDirection == Direction.WEST) { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null); }
            else if(this.facingDirection == Direction.EAST) { RenderUtils.renderSpriteFlippedHorizontally(img, this.worldPosition, g); }
            else { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null); }
        }
    }
    
    public void onDeath() {
        this.deactivate();
    }

    private void handleButtons() {

        // current buttons that are held down
        Map<Integer, KeyInput.Command> buttons = this.keyInput.getButtons();

        if (buttons.containsValue(KeyInput.Command.MOVE_DOWN)) {

            if(acceleration_y < maxAcceleration) {
                acceleration_y += (deaccelerationValue + accelerationValue);
            }
        }

        if (buttons.containsValue(KeyInput.Command.MOVE_UP)) {

            if(acceleration_y > -maxAcceleration) {
                acceleration_y -= (deaccelerationValue + accelerationValue);
            }
        }

        if (buttons.containsValue(KeyInput.Command.MOVE_RIGHT)) {

            facingDirection = Direction.EAST;

            if(acceleration_x < maxAcceleration) {
                acceleration_x += (deaccelerationValue + accelerationValue);
            }
        }

        if (buttons.containsValue(KeyInput.Command.MOVE_LEFT)) {

            facingDirection = Direction.WEST;
            if(acceleration_x > -maxAcceleration) {
                acceleration_x -= (deaccelerationValue + accelerationValue);
            }

        }

        if (buttons.containsValue(KeyInput.Command.ACTION)) {

        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_DOWN)) {

        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_UP)) {

        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_LEFT)) {

        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_RIGHT)) {

        }

    }

    public UnitType getUnitType() {
        return unitType;
    }

    public float getVelocity_x() {
        return velocity_x;
    }

    public float getVelocity_y() {
        return velocity_y;
    }

    public float getAcceleration_x() {
        return acceleration_x;
    }

    public float getAcceleration_y() {
        return acceleration_y;
    }
}
