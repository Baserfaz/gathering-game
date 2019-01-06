package com.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.data.Health;
import com.enumerations.Direction;
import com.enumerations.ItemType;
import com.enumerations.SpriteType;
import com.interfaces.ICollidable;
import com.interfaces.IPhysicsObject;
import com.sun.javafx.geom.Vec2d;
import com.utilities.RenderUtils;

public abstract class Actor extends GameObject implements ICollidable, IPhysicsObject {

    // how fast velocity decreases over time
    // max friction is accelerationValue + deaccelerationValue
    private double friction = 0.44;

    // how fast acceleration decreases
    private double deaccelerationValue = 0.30;

    // how fast acceleration happens
    private double accelerationValue = 0.15;

    private double maxAcceleration = 1.0;
    private double maxVelocity = 5.0;

    // ----

    // in milliseconds -> 1000ms = 1s
    private double timeBetweenAttacks = 500.0;
    private double invulnerabilityDuration = 500.0;

    // ----

    protected Health health;

    protected String name;
    protected Direction facingDirection = Direction.WEST;
    protected int attackDamage;

    private double velocity_x, velocity_y;
    private double acceleration_x, acceleration_y;

    private double lastAccelx, lastAccely, lastVelocityx, lastVelocityy;

    private boolean isCollisionsEnabled = true;
    protected Rectangle hitbox;

    private long lastFrameTime = 0l;
    private long attackTimer = 0l;
    private long invulnerabilityTimer = 0l;

    protected boolean canAttack = false;
    protected boolean isInvulnerable = false;

    public Actor(String name, Point tilePos, SpriteType spriteType, int hp, int damage) {
        super(tilePos, spriteType, true);

        this.name = name;
        this.attackDamage = damage;
        this.health = new Health(hp, this);

        // if a subclass has not defined hitbox, use this as default.
        if(hitbox == null) {
            hitbox = this.calculateBoundingBox();
        }
    }

    public void tick() {
        this.move();
        this.calculateCollisions();

        // moves the hitbox to the center of the sprite, not always desirable
        this.updateHitbox(this.getCenterPosition());
        this.updateTimers();
    }

    @Override
    public void render(Graphics g) {
        BufferedImage img = this.defaultStaticSprite;
        if(this.facingDirection == Direction.EAST) { img = RenderUtils.flipSpriteHorizontally(img); }
        if(isInvulnerable) { img = RenderUtils.tintWithColor(img, Color.red); }
        g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null);
    }

    private void updateTimers() {

        // calculate time between ticks in ns
        long now = System.nanoTime();
        double deltaTime = (now - lastFrameTime) * 0.000001;
        lastFrameTime = now;

        // update attack timer
        if(!canAttack) {
            if (attackTimer < timeBetweenAttacks) {
                attackTimer += deltaTime;
            } else {
                this.onAttackTimerReset();
            }
        }

        // update invulnerability timer
        if(isInvulnerable) {
            if (invulnerabilityTimer < invulnerabilityDuration) {
                invulnerabilityTimer += deltaTime;
            } else {
                this.onInvulnerabilityTimerReset();
            }
        }
    }

    private void onAttackTimerReset() {
        canAttack = true;
        attackTimer = 0l;
    }

    private void onInvulnerabilityTimerReset() {
        this.isInvulnerable = false;
        invulnerabilityTimer = 0l;
    }

    public void onDamageTaken() {
        this.isInvulnerable = true;
    }

    public void onDeath() {
        this.deactivate();
        this.isDeleted = true;
    }

    @Override
    public void onCollision(ICollidable other) {
        if(other instanceof Block
                || (other instanceof Item && ((Item) other).getItemType() == ItemType.STONE)
                || other instanceof Actor) {

            velocity_x = -velocity_x;
            velocity_y = -velocity_y;
            acceleration_x = 0;
            acceleration_y = 0;

        } else if(other instanceof SpikeTrap) {
            SpikeTrap trap = (SpikeTrap) other;
            if(!trap.isAutomatic() && !trap.isTrapActivated()) { trap.activateTrap(); }
            if(trap.isTrapActivated()) this.getHealth().takeDamage(trap.getTrapDamage());
        }
    }

    @Override
    public boolean isActive() {
        return this.isCollisionsEnabled;
    }

    @Override
    public void disableCollisions() {
        this.isCollisionsEnabled = false;
    }

    @Override
    public void enableCollisions() {
        this.isCollisionsEnabled = true;
    }

    @Override
    public Rectangle getHitbox() {
        return this.hitbox;
    }

    @Override
    public void setHitbox(Rectangle rectangle) {
        this.hitbox = rectangle;
    }

    public Health getHealth() {
        return this.health;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    @Override
    public void setAccelerationX(double acceleration_x) {
        this.acceleration_x = acceleration_x;
    }

    @Override
    public void addAccelerationX(double a) {
        this.acceleration_x += a;
    }

    @Override
    public void setAccelerationY(double acceleration_y) {
        this.acceleration_y = acceleration_y;
    }

    @Override
    public void addAccelerationY(double a) {
        this.acceleration_y += a;
    }

    @Override
    public double getMaxAcceleration() {
        return maxAcceleration;
    }

    @Override
    public double getMaxVelocity() {
        return maxVelocity;
    }

    @Override
    public double getDeaccelerationValue() {
        return deaccelerationValue;
    }

    public double getAccelerationValue() {
        return accelerationValue;
    }

    @Override
    public double getFriction() {
        return friction;
    }

    @Override
    public Vec2d getAcceleration() {
        return new Vec2d(acceleration_x, acceleration_y);
    }

    @Override
    public Vec2d getVelocity() {
        return new Vec2d(velocity_x, velocity_y);
    }

    @Override
    public Vec2d getCachedVelocity() {
        return new Vec2d(lastVelocityx, lastVelocityy);
    }

    @Override
    public Vec2d getCachedAcceleration() {
        return new Vec2d(lastAccelx, lastAccely);
    }

    @Override
    public void setCachedVelocity(Vec2d a) {
        this.lastVelocityx = a.x;
        this.lastVelocityy = a.y;
    }

    @Override
    public void setCachedAcceleration(Vec2d a) {
        this.lastAccelx = a.x;
        this.lastAccely = a.y;
    }

    @Override
    public void setVelocityX(double a) {
        this.velocity_x = a;
    }

    @Override
    public void setVelocityY(double a) {
        this.velocity_y = a;
    }

    @Override
    public void addVelocityX(double a) {
        this.velocity_x += a;
    }

    @Override
    public void addVelocityY(double a) {
        this.velocity_y += a;
    }

    @Override
    public void addWorldPosition(Vec2d a) {
        this.worldPosition.x += a.x;
        this.worldPosition.y += a.y;
    }

}
