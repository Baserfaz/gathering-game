package com.gameobjects;

import com.enumerations.Direction;
import com.enumerations.SpriteType;
import com.interfaces.ICollidable;
import com.utilities.RenderUtils;

import java.awt.*;

public class Projectile extends GameObject implements ICollidable {

    public enum DamageType {
        PHYSICAL, FROST, FIRE, POISON, LIGHTNING
    }

    private Direction lookDirection;
    private Rectangle hitbox;
    private boolean isCollidable;
    private Point startPoint;

    private int damageAmount = 1;
    private double travelSpeed = 10.0;
    private double travelDistance = 200.0;
    private DamageType damageType = DamageType.PHYSICAL;

    public Projectile(Point worldStartPosition, Direction direction, SpriteType type) {
        super(worldStartPosition, type, false);

        this.lookDirection = direction;
        this.startPoint = (Point) worldStartPosition.clone();

        // rotate sprite: by default the sprite should be facing WEST
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

        this.hitbox = this.calculateBoundingBox();
        this.isCollidable = true;
    }

    @Override
    public void tick() {
        if(this.isEnabled) {

            double distance = this.getDistanceTravelled();
            if(distance >= this.travelDistance) {
                this.onTravelEnd();
                return;
            }

            this.move();
            this.updateHitbox();
        }
    }

    private void updateHitbox() {
        Point centerPosition = this.getCenterPosition();
        this.hitbox.x = centerPosition.x - hitbox.width / 2;
        this.hitbox.y = centerPosition.y - hitbox.height / 2;
    }

    private void move() {
        switch (this.lookDirection) {
            case SOUTH:
                this.worldPosition.y += this.travelSpeed;
                break;
            case NORTH:
                this.worldPosition.y -= this.travelSpeed;
                break;
            case WEST:
                this.worldPosition.x -= this.travelSpeed;
                break;
            case EAST:
                this.worldPosition.x += this.travelSpeed;
                break;
            default:
                System.out.println("Projectile.tick: Error: not supported look direction: " + this.lookDirection);
                break;
        }
    }

    private void onTravelEnd() {
        this.deactivate();
        this.disableCollisions();
        this.isDeleted = true;
    }

    @Override
    public void render(Graphics g) {
        if(this.isVisible) {
            g.drawImage(this.defaultStaticSprite,
                    this.worldPosition.x, this.worldPosition.y, null);
        }
    }

    private double getDistanceTravelled() {
        double xx = Math.pow(worldPosition.x - startPoint.x, 2);
        double yy = Math.pow(worldPosition.y - startPoint.y, 2);
        return Math.sqrt(xx + yy);
    }

    public int getDamageAmount() {
        return this.damageAmount;
    }

    @Override
    public void onCollision(ICollidable other) {
        this.onTravelEnd();
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
}
