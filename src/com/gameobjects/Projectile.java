package com.gameobjects;

import com.data.Animation;
import com.engine.Game;
import com.enumerations.AnimationType;
import com.enumerations.DamageType;
import com.enumerations.Direction;
import com.enumerations.SpriteType;
import com.interfaces.ICollidable;
import com.interfaces.IPhysicsObject;
import com.sun.javafx.geom.Vec2d;
import com.utilities.AnimationFactory;
import com.utilities.RenderUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile extends GameObject implements ICollidable, IPhysicsObject {

    private final int shadowTintDepth = 4;

    // create objects here.. looks ugly in constructor
    private Vec2d acceleration = new Vec2d();
    private Vec2d velocity = new Vec2d();
    private Vec2d cachedAcceleration = new Vec2d();
    private Vec2d cachedVelocity = new Vec2d();

    private Animation onDestroyAnim;
    private int frameIndex = 0;  // what frame is it currently
    private int frameTime = 20; // how long one frame is shown
    private long frameTimer = 0l;
    private long lastTime = 0l;

    private Direction lookDirection;
    private Rectangle hitbox;
    private boolean isCollidable;
    private Point startPoint;
    private Actor owner;

    private int damageAmount = 1;
    private double travelSpeed = 10.0;
    private double travelDistance = 200.0;
    private DamageType damageType = DamageType.PHYSICAL;

    private boolean isOnTravelEnd = false;

    private BufferedImage shadow;

    public Projectile(Point worldStartPosition, Direction direction, SpriteType type, Actor owner) {
        super(worldStartPosition, type, false);

        this.lookDirection = direction;
        this.startPoint = (Point) worldStartPosition.clone();
        this.owner = owner;

        this.onDestroyAnim = Game.instance.getSpriteStorage().getAnimation(AnimationType.PLAYER_PROJECTILE_DESTROY);

        setSpriteRotation();
        this.shadow = RenderUtils.tint(this.defaultStaticSprite, true, shadowTintDepth);

        this.hitbox = this.calculateBoundingBox();
        this.isCollidable = true;
    }

    private void setSpriteRotation() {
        switch (lookDirection) {
            case EAST:
                this.defaultStaticSprite = RenderUtils.flipSpriteHorizontally(this.defaultStaticSprite);
                break;
            case NORTH:
                this.defaultStaticSprite = RenderUtils.rotateImageClockwise(this.defaultStaticSprite, 1);
                break;
            case SOUTH:
                this.defaultStaticSprite = RenderUtils.rotateImageClockwise(this.defaultStaticSprite, 3);
                break;
        }
    }

    @Override
    public void render(Graphics g) {

        // draw shadow first, a bit further down
        g.drawImage(this.shadow,
                worldPosition.x,
                worldPosition.y + Game.CALCULATED_SPRITE_SIZE / 2,
                null);

        // draw projectile on top of the shadow
        g.drawImage(this.defaultStaticSprite,
                worldPosition.x, worldPosition.y, null);

    }

    @Override
    public void tick() {
        if(this.isEnabled) {

            // if this projectile has travelled far enough

            if(isOnTravelEnd) {
                this.onTravelEnd();
                return;
            }

            double distance = this.getDistanceTravelled();
            if(distance >= this.travelDistance) {
                isOnTravelEnd = true;
                return;
            }

            // ---

            this.move();
            this.updateHitbox();
            this.calculateCollisions();
        }
    }

    private void updateHitbox() {
        Point centerPosition = this.getCenterPosition();
        this.hitbox.x = centerPosition.x - hitbox.width / 2;
        this.hitbox.y = centerPosition.y - hitbox.height / 2;
    }

    @Override
    public void move() {

        // TODO: in constructor set the velocity to some random amount and dont touch acceleration.

//        switch (this.lookDirection) {
//            case SOUTH:
//                acceleration.y += travelSpeed;
//                break;
//            case NORTH:
//                acceleration.y -= travelSpeed;
//                break;
//            case WEST:
//                acceleration.x -= travelSpeed;
//                break;
//            case EAST:
//                acceleration.x += travelSpeed;
//                break;
//            default:
//                System.out.println("Projectile.tick: Error: not supported look direction: " + this.lookDirection);
//                break;
//        }

        IPhysicsObject.super.move();
    }

    private void onTravelEnd() {

        // This function is run every tick if the travelEnd flag is true.
        // Meaning we can run animation logic here.
        if(onDestroyAnim != null
                && onDestroyAnim.getFrameCount() > 0
                && frameIndex <= onDestroyAnim.getFrameCount()) {

            // timing
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) * 0.000001;
            lastTime = now;

            // do the animation
            if(frameTimer >= frameTime) {

                BufferedImage img = onDestroyAnim.getFrame(frameIndex);

                if(img != null) {
                    switch (this.lookDirection) {
                        case NORTH:
                            img = RenderUtils.rotateImageClockwise(img, 1);
                            break;
                        case SOUTH:
                            img = RenderUtils.rotateImageClockwise(img, 3);
                            break;
                        case EAST:
                            img = RenderUtils.flipSpriteHorizontally(img);
                            break;
                    }

                    defaultStaticSprite = img;
                    shadow = RenderUtils.tint(img, true, shadowTintDepth);
                }

                frameIndex += 1;
                frameTimer = 0;

            } else {
                frameTimer += deltaTime;
            }

            // this return means we are exiting the function before calling deactivate,
            // only after the animation is over we are not returning.
            return;
        }

        this.deactivate();
        this.disableCollisions();
        this.isDeleted = true;
    }

    private double getDistanceTravelled() {
        double xx = Math.pow(worldPosition.x - startPoint.x, 2);
        double yy = Math.pow(worldPosition.y - startPoint.y, 2);
        return Math.sqrt(xx + yy);
    }

    public int getDamageAmount() {
        return this.damageAmount;
    }

    // ----

    @Override
    public void onCollision(ICollidable other) {
        if(other instanceof Block) {

            // hit wall
            isOnTravelEnd = true;

        } else if(other instanceof Enemy) {

            Actor actor = (Actor) other;
            actor.getHealth().takeDamage(this.damageAmount);

            isOnTravelEnd = true;
        }
    }

    @Override
    public boolean isActive() {
        return this.isCollidable;
    }

    @Override
    public void disableCollisions() {
        this.isCollidable = false;
    }

    @Override
    public void enableCollisions() {
        this.isCollidable = true;
    }

    @Override
    public Rectangle getHitbox() {
        return this.hitbox;
    }

    @Override
    public void setHitbox(Rectangle rectangle) {
        this.hitbox = rectangle;
    }

    // ----

    @Override
    public double getMaxAcceleration() {
        return 10d;
    }

    @Override
    public double getMaxVelocity() {
        return 10d;
    }

    @Override
    public double getDeaccelerationValue() {
        return 0.1d;
    }

    @Override
    public Vec2d getAcceleration() {
        return acceleration;
    }

    @Override
    public Vec2d getVelocity() {
        return velocity;
    }

    @Override
    public double getFriction() {
        return 0.5d;
    }

    @Override
    public Vec2d getCachedAcceleration() {
        return cachedAcceleration;
    }

    @Override
    public Vec2d getCachedVelocity() {
        return cachedVelocity;
    }

    @Override
    public void setCachedAcceleration(Vec2d a) {
        cachedAcceleration = a;
    }

    @Override
    public void setCachedVelocity(Vec2d a) {
        cachedVelocity = a;
    }

    @Override
    public void addAccelerationX(double a) {
        acceleration.x += a;
    }

    @Override
    public void addAccelerationY(double a) {
        acceleration.y += a;
    }

    @Override
    public void addVelocityX(double a) {
        velocity.x += a;
    }

    @Override
    public void addVelocityY(double a) {
        velocity.y += a;
    }

    @Override
    public void setVelocityX(double a) {
        velocity.x = a;
    }

    @Override
    public void setVelocityY(double a) {
        velocity.y = a;
    }

    @Override
    public void setAccelerationX(double a) {
        acceleration.x = a;
    }

    @Override
    public void setAccelerationY(double a) {
        acceleration.y = a;
    }

    @Override
    public void addWorldPosition(Vec2d a) {
        worldPosition.x += a.x;
        worldPosition.y += a.y;
    }
}
