package com.gameobjects;

import com.data.Animation;
import com.engine.Game;
import com.enumerations.AnimationType;
import com.enumerations.DamageType;
import com.enumerations.Direction;
import com.enumerations.SpriteType;
import com.interfaces.ICollidable;
import com.utilities.AnimationFactory;
import com.utilities.RenderUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile extends GameObject implements ICollidable {

    private Animation onDestroyAnim;
    private int frameIndex = 0;  // what frame is it currently
    private int frameTime = 100; // how long one frame is shown (in ms)
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

        this.onDestroyAnim =
                Game.instance.getSpriteStorage().getAnimation(AnimationType.PLAYER_PROJECTILE_DESTROY);

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

        this.shadow = RenderUtils.tint(this.defaultStaticSprite, true, 4);

        this.hitbox = this.calculateBoundingBox();
        this.isCollidable = true;
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

            if(isOnTravelEnd) {
                this.onTravelEnd();
                return;
            }

            double distance = this.getDistanceTravelled();
            if(distance >= this.travelDistance) {
                isOnTravelEnd = true;
                return;
            }

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

    private void move() {

        // TODO: use velocity and acceleration instead

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

        // This function is run every tick if the travelEnd flag is true.
        // Meaning we can run animation logic here.
        if(onDestroyAnim != null
                && onDestroyAnim.getFrameCount() > 0
                && this.frameIndex < onDestroyAnim.getFrameCount()) {

            // timing
            long now = System.nanoTime();
            long deltaTime = now - lastTime;
            lastTime = now;

            if(this.frameTimer >= this.frameTime) {

                BufferedImage img = onDestroyAnim.getFrame(this.frameIndex);
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

                this.defaultStaticSprite = img;
                this.frameIndex += 1;
            } else {
                this.frameTimer += deltaTime;
            }

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

    @Override
    public void onCollision(ICollidable other) {
        if(other instanceof Block) {

            // hit wall
            isOnTravelEnd = true;

        } else if(other instanceof Actor && !other.equals(owner) ) {

            // hit an actor
            // -> calculate damage
            // TODO: damage type... resistances...

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
}
