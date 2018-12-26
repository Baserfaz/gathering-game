package com.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.data.Health;
import com.data.Inventory;
import com.engine.Game;
import com.engine.KeyInput;
import com.enumerations.Direction;
import com.enumerations.ItemType;
import com.enumerations.SpriteType;
import com.enumerations.UnitType;
import com.interfaces.ICollidable;
import com.utilities.Mathf;
import com.utilities.RenderUtils;

public class Actor extends GameObject implements ICollidable {

    // how fast velocity decreases over time
    // max friction is accelerationValue + deaccelerationValue
    private double friction = 0.44;

    // how fast acceleration decreases
    private double deaccelerationValue = 0.30;

    // how fast acceleration happens
    private double accelerationValue = 0.15;

    private double maxAcceleration = 1.0;
    private double maxVelocity = 5.0;

    // in milliseconds -> 1000ms = 1s
    private double timeBetweenAttacks = 500.0;

    // -------------------

    protected Health health;
    protected Inventory inventory;

    protected String name;
    protected UnitType unitType;
    protected Direction facingDirection = Direction.WEST;
    protected int attackDamage;

    protected KeyInput keyInput;

    protected double velocity_x, velocity_y;
    protected double acceleration_x, acceleration_y;

    private double lastAccelx, lastAccely, lastVelocityx, lastVelocityy;

    private boolean isCollisionsEnabled = true;
    private Rectangle hitbox;

    private long lastFrameTime = 0l;
    private long attackTimer = 0l;

    private boolean canAttack = false;

    public Actor(String name, Point tilePos, UnitType unitType,
                 SpriteType spriteType, int hp, int damage) {
        super(tilePos, spriteType, true);

        this.name = name;
        this.attackDamage = damage;
        this.unitType = unitType;
        this.health = new Health(hp, this);

        // create hitbox, only the size matters here, position is updated on every frame
        int size = Game.CALCULATED_SPRITE_SIZE / 2;
        this.hitbox = new Rectangle(this.worldPosition.x, this.worldPosition.y, size, size);

        if(this.unitType == UnitType.PLAYER_UNIT) {
            this.keyInput = Game.instance.getKeyInput();
            this.inventory = new Inventory();
        }
    }

    public void tick() {
        if(this.unitType == UnitType.PLAYER_UNIT) { this.handleButtons(); }
        this.move();
        this.calculateCollisions();
        this.updateHitboxPos();
        this.updateAttackTimer();
    }
    
    public void render(Graphics g) {
        if(this.isVisible) {
            BufferedImage img = this.defaultStaticSprite;
            if(this.facingDirection == Direction.WEST) { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null); }
            else if(this.facingDirection == Direction.EAST) { RenderUtils.renderSpriteFlippedHorizontally(img, this.worldPosition, g); }
            else { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null); }
        }
    }

    private void updateAttackTimer() {

        // always update lastFrameTime
        long now = System.nanoTime();
        long deltaTime = now - lastFrameTime;
        lastFrameTime = now;

        if(canAttack) return;

        // deltaTime is in nanoseconds
        if(attackTimer < timeBetweenAttacks) {
            attackTimer += deltaTime * 0.000001; // some weird amount of time??
        } else {
            this.onAttackTimerReset();
        }
    }

    private void updateHitboxPos() {
        Point centerPosition = this.getCenterPosition();
        this.hitbox.x = centerPosition.x - hitbox.width / 2;
        this.hitbox.y = centerPosition.y - hitbox.height / 2;
    }

    private void calculateCollisions() {

        Collection<GameObject> gos = Game.instance.getHandler().getObjects()
                .stream()
                .filter(a -> (a instanceof ICollidable))
                .filter(a -> !a.equals(this))
                .filter(a -> ((ICollidable) a).isActive())
                .filter(a -> ((ICollidable) a).getDistanceFrom(this.getHitbox()) < Game.CALCULATED_MAX_COLLISION_DISTANCE)
                .filter(a -> this.isColliding((ICollidable)a))
                .collect(Collectors.toList());

        if(!gos.isEmpty()) {
            gos.stream().forEach(g -> this.onCollision((ICollidable) g));
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

        // handle acceleration and velocity after applying the forces to world position
        this.updateDeacceleration();
        this.updateVelocityFriction();

        // cache
        lastAccelx = acceleration_x;
        lastAccely = acceleration_y;
        lastVelocityx = velocity_x;
        lastVelocityy = velocity_y;
    }

    private void updateDeacceleration() {

        // de-accelerate x
        if(acceleration_x > 0) { acceleration_x -= deaccelerationValue; }
        else if(acceleration_x < 0) { acceleration_x += deaccelerationValue; }

        // de-accelerate y
        if(acceleration_y > 0) { acceleration_y -= deaccelerationValue; }
        else if(acceleration_y < 0) { acceleration_y += deaccelerationValue; }

        // when changing polarity -> stop
        if (lastAccelx > 0 && acceleration_x <= 0 || lastAccelx < 0 && acceleration_x >= 0) {
            acceleration_x = 0;
        }

        if (lastAccely > 0 && acceleration_y <= 0 || lastAccely < 0 && acceleration_y >= 0) {
            acceleration_y = 0;
        }
    }

    private void updateVelocityFriction() {

        // add friction to the velocity, so it decreases over time
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
    }

    private void onAttackTimerReset() {
        canAttack = true;
        attackTimer = 0l;
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
            if(Math.abs(acceleration_y) < maxAcceleration) {
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
            if(Math.abs(acceleration_x) < maxAcceleration) {
                acceleration_x -= (deaccelerationValue + accelerationValue);
            }
        }

        if (buttons.containsValue(KeyInput.Command.ACTION)) {
            // TODO: drop bomb etc. ??
        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_DOWN)) {
            if(canAttack) {
                Point p = (Point) this.getWorldPosition().clone();
                p.y += this.hitbox.height;
                new Projectile(p, Direction.SOUTH, SpriteType.PROJECTILE_PLAYER_1);
                this.canAttack = false;
            }
        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_UP)) {
            if(canAttack) {
                Point p = (Point) this.getWorldPosition().clone();
                p.y -= this.hitbox.height;
                new Projectile(p, Direction.NORTH, SpriteType.PROJECTILE_PLAYER_1);
                this.canAttack = false;
            }
        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_LEFT)) {
            if(canAttack) {
                Point p = (Point) this.getWorldPosition().clone();
                p.x -= this.hitbox.width;
                new Projectile(p, Direction.WEST, SpriteType.PROJECTILE_PLAYER_1);
                this.canAttack = false;
            }
        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_RIGHT)) {
            if(canAttack) {
                Point p = (Point) this.getWorldPosition().clone();
                p.x += this.hitbox.width;
                new Projectile(p, Direction.EAST, SpriteType.PROJECTILE_PLAYER_1);
                this.canAttack = false;
            }
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

    @Override
    public void onCollision(ICollidable other) {

        // ---- GENERAL COLLISIONS ----
        if(other instanceof Block ||
                (other instanceof Item && ((Item) other).getItemType() == ItemType.STONE)) {

            // TODO: problem in left & up collisions -> negative velocity

            this.velocity_x = -velocity_x;
            this.velocity_y = -velocity_y;
            this.acceleration_x = 0;
            this.acceleration_y = 0;
        }

        // ---- SPECIAL STUFF FOR PLAYER ----
        if(this.unitType != UnitType.PLAYER_UNIT) return;

        if(other instanceof Chest) {
            Chest c = (Chest) other;
            if(c.isLocked() && this.inventory.hasKeys()) {
                c.unlock();
                this.inventory.useKey();
            }
            if(!c.isLocked() && !c.isOpen()) {
                c.open();
            }
        } else if(other instanceof StepPlate) {
            StepPlate stepPlate = (StepPlate) other;
            stepPlate.activatePlate();
        } else if(other instanceof Gold) {
            Gold gold = (Gold) other;
            gold.pickup(this);
        } else if(other instanceof Consumable) {
            Consumable consumable = (Consumable) other;
            consumable.consume(this);
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

    public Health getHealth() {
        return this.health;
    }
}
