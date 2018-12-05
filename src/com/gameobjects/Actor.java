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
import com.utilities.Mathf;
import com.utilities.RenderUtils;

public class Actor extends GameObject {

    final double friction = 0.05;               // how fast velocity decreases over time
    final double deaccelerationValue = 0.03;    // how fast acceleration decreases
    final double accelerationValue = 0.15;      // how fast acceleration happens

    final double maxAcceleration = 1.0;
    final double maxVelocity = 5.0;

    protected String name;
    protected Health HP;
    protected UnitType unitType;
    protected Direction facingDirection = Direction.WEST;
    protected int attackDamage;
    protected KeyInput keyInput;
    protected Level level;

    protected double velocity_x, velocity_y;
    protected double acceleration_x, acceleration_y;

    private double lastAccelx, lastAccely, lastVelocityx, lastVelocityy;

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

        if(this.unitType == UnitType.PLAYER_UNIT) { this.handleButtons(); }
        this.move();
    }
    
    public void render(Graphics g) {
        if(this.isVisible) {
            BufferedImage img = this.defaultStaticSprite;
            if(this.facingDirection == Direction.WEST) { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null); }
            else if(this.facingDirection == Direction.EAST) { RenderUtils.renderSpriteFlippedHorizontally(img, this.worldPosition, g); }
            else { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null); }
        }
    }

    private void move() {

        // clamp the maximum acceleration to some values
        double axx = Mathf.clamp(-maxAcceleration, maxAcceleration, acceleration_x);
        double ayy = Mathf.clamp(-maxAcceleration, maxAcceleration, acceleration_y);

        // handle velocity in x-axis
        if(Math.abs(velocity_x) < maxVelocity) {
            velocity_x += axx;
        } else {
            if(velocity_x > 0) { velocity_x = maxVelocity; }
            else { velocity_x = -maxVelocity; }
        }

        // handle velocity in y-axis
        if(Math.abs(velocity_y) < maxVelocity) {
            velocity_y += ayy;
        } else {
            if(velocity_y > 0) { velocity_y = maxVelocity; }
            else { velocity_y = -maxVelocity; }
        }

        // actually move the character using velocity
        worldPosition.x += velocity_x;
        worldPosition.y += velocity_y;

        this.updateAcceleration();
        this.updateVelocity();

        // cache
        lastAccelx = acceleration_x;
        lastAccely = acceleration_y;
        lastVelocityx = velocity_x;
        lastVelocityy = velocity_y;
    }

    private void updateAcceleration() {

        // de-accelerate x
        if(acceleration_x > 0) { acceleration_x -= deaccelerationValue; }
        else { acceleration_x += deaccelerationValue; }

        // de-accelerate y
        if(acceleration_y > 0) { acceleration_y -= deaccelerationValue; }
        else { acceleration_y += deaccelerationValue; }

        // when changing polarity -> stop
        if (lastAccelx > 0 && acceleration_x < 0 || lastAccelx < 0 && acceleration_x > 0) {
            acceleration_x = 0;
        } else if (lastAccely > 0 && acceleration_y < 0 || lastAccely < 0 && acceleration_y > 0) {
            acceleration_y = 0;
        }
    }

    private void updateVelocity() {

        // add friction to the velocity, so it decreases over time
        if(velocity_x > 0) { velocity_x -= friction; }
        else { velocity_x += friction; }

        if(velocity_y > 0) { velocity_y -= friction; }
        else { velocity_y += friction; }

        // when changing polarity -> stop
        if (lastVelocityx > 0 && velocity_x < 0 || lastVelocityx < 0 && velocity_x > 0) {
            velocity_x = 0;
            acceleration_x = 0;
        } else if (lastVelocityy > 0 && velocity_y < 0 || lastVelocityy < 0 && velocity_y > 0) {
            velocity_y = 0;
            acceleration_y = 0;
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

    public double getVelocity_x() {
        return velocity_x;
    }

    public double getVelocity_y() {
        return velocity_y;
    }

    public double getAcceleration_x() {
        return acceleration_x;
    }

    public double getAcceleration_y() {
        return acceleration_y;
    }
}
