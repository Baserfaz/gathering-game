package com.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.data.Health;
import com.engine.Game;
import com.enumerations.Direction;
import com.enumerations.ItemType;
import com.enumerations.SpriteType;
import com.interfaces.ICollidable;
import com.utilities.Mathf;
import com.utilities.RenderUtils;

public abstract class Actor extends GameObject implements ICollidable {

    // how fast velocity decreases over time
    // max friction is accelerationValue + deaccelerationValue
    protected double friction = 0.44;

    // how fast acceleration decreases
    protected double deaccelerationValue = 0.30;

    // how fast acceleration happens
    protected double accelerationValue = 0.15;

    protected double maxAcceleration = 1.0;
    protected double maxVelocity = 5.0;

    // in milliseconds -> 1000ms = 1s
    protected double timeBetweenAttacks = 500.0;
    protected double invulnerabilityDuration = 500.0;

    // -------------------

    protected Health health;

    protected String name;
    protected Direction facingDirection = Direction.WEST;
    protected int attackDamage;

    protected double velocity_x, velocity_y;
    protected double acceleration_x, acceleration_y;

    private double lastAccelx, lastAccely, lastVelocityx, lastVelocityy;

    private boolean isCollisionsEnabled = true;
    protected Rectangle hitbox;

    protected long lastFrameTime = 0l;
    protected long attackTimer = 0l;
    protected long invulnerabilityTimer = 0l;

    protected boolean canAttack = false;
    protected boolean isInvulnerable = false;

    public Actor(String name, Point tilePos, SpriteType spriteType, int hp, int damage) {
        super(tilePos, spriteType, true);

        this.name = name;
        this.attackDamage = damage;
        this.health = new Health(hp, this);

        // create hitbox, only the size matters here, position is updated on every frame
        int size = Game.CALCULATED_SPRITE_SIZE / 2;
        this.hitbox = new Rectangle(this.worldPosition.x, this.worldPosition.y, size, size);
    }

    public void tick() {
        this.move();
        this.calculateCollisions();
        this.updateHitbox(this.getCenterPosition());
        this.updateTimers();
    }

    @Override
    public void render(Graphics g) {
        if(this.isVisible) {
            BufferedImage img = this.defaultStaticSprite;
            if(this.facingDirection == Direction.EAST) { img = RenderUtils.flipSpriteHorizontally(img); }
            if(isInvulnerable) { img = RenderUtils.tintWithColor(img, Color.red); }
            g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null);
        }
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

    private void move() {

        // clamp the maximum acceleration to some values
        double axx = Mathf.clamp(-maxAcceleration, maxAcceleration, acceleration_x);
        double ayy = Mathf.clamp(-maxAcceleration, maxAcceleration, acceleration_y);

        // handle velocity in x-axis
        double absVelx = Math.abs(velocity_x);
        if(absVelx < maxVelocity) {
            velocity_x += axx;
        } else if(absVelx > maxVelocity) {
            if(velocity_x > 0) { velocity_x = maxVelocity; }
            else if(velocity_x < 0) { velocity_x = -maxVelocity; }
        }

        // handle velocity in y-axis
        double absVely = Math.abs(velocity_y);
        if(absVely < maxVelocity) {
            velocity_y += ayy;
        } else if(absVely > maxVelocity) {
            if(velocity_y > 0) { velocity_y = maxVelocity; }
            else if(velocity_y < 0) { velocity_y = -maxVelocity; }
        }

        // actually move the character using velocity
        worldPosition.x += velocity_x;
        worldPosition.y += velocity_y;

        // ---- handle acceleration and velocity after applying the forces to world position
        if(acceleration_x > 0) { acceleration_x -= deaccelerationValue; }
        else if(acceleration_x < 0) { acceleration_x += deaccelerationValue; }

        if(acceleration_y > 0) { acceleration_y -= deaccelerationValue; }
        else if(acceleration_y < 0) { acceleration_y += deaccelerationValue; }

        // when changing polarity -> stop
        if (lastAccelx > 0 && acceleration_x <= 0 || lastAccelx < 0 && acceleration_x >= 0) {
            acceleration_x = 0;
        }

        if (lastAccely > 0 && acceleration_y <= 0 || lastAccely < 0 && acceleration_y >= 0) {
            acceleration_y = 0;
        }

        //  ---- add friction to the velocity, so it decreases over time
        if(velocity_x > 0) { velocity_x -= friction; }
        else if(velocity_x < 0) { velocity_x += friction; }

        if(velocity_y > 0) { velocity_y -= friction; }
        else if(velocity_y < 0) { velocity_y += friction; }

        // when changing polarity -> stop
        if (lastVelocityx > 0 && velocity_x <= 0 || lastVelocityx < 0 && velocity_x >= 0) {
            velocity_x = 0;
            acceleration_x = 0;
        }

        if (lastVelocityy > 0 && velocity_y <= 0 || lastVelocityy < 0 && velocity_y >= 0) {
            velocity_y = 0;
            acceleration_y = 0;
        }

        // ---- cache
        lastAccelx = acceleration_x;
        lastAccely = acceleration_y;
        lastVelocityx = velocity_x;
        lastVelocityy = velocity_y;
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

    @Override
    public void onCollision(ICollidable other) {
        if(other instanceof Block ||
                (other instanceof Item && ((Item) other).getItemType() == ItemType.STONE)) {

            // TODO: problem in left & up collisions -> negative velocity

            this.velocity_x = -velocity_x;
            this.velocity_y = -velocity_y;
            this.acceleration_x = 0;
            this.acceleration_y = 0;

        } else if(other instanceof SpikeTrap) {
            SpikeTrap trap = (SpikeTrap) other;
            if (!trap.isAutomatic() && !trap.isTrapActivated()) { trap.activateTrap(); }
            this.getHealth().takeDamage(trap.getTrapDamage());
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
}
