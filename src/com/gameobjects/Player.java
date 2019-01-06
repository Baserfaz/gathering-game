package com.gameobjects;

import com.data.Inventory;
import com.engine.Game;
import com.engine.KeyInput;
import com.enumerations.Direction;
import com.enumerations.SpriteType;
import com.interfaces.ICollidable;

import java.awt.*;
import java.util.Map;

public class Player extends Actor {

    private KeyInput keyInput;
    private Inventory inventory;

    public Player(String name, Point tilePos, int hp, int damage) {
        super(name, tilePos, SpriteType.PLAYER, hp, damage);

        this.keyInput = Game.instance.getKeyInput();
        this.inventory = new Inventory();

        // create hitbox, only the size matters here, position is updated on every frame
        int size = Game.CALCULATED_SPRITE_SIZE / 2;
        this.hitbox = new Rectangle(this.worldPosition.x, this.worldPosition.y, size, size);
    }

    @Override
    public void tick() {
        this.handleButtons();
        super.tick();
    }

    private void handleButtons() {

        // current buttons that are held down
        Map<Integer, KeyInput.Command> buttons = this.keyInput.getButtons();

        double maxAcceleration = getMaxAcceleration();
        double ay = getAcceleration().y;
        double ax = getAcceleration().x;
        double totalAcc = getAccelerationValue() + getDeaccelerationValue();

        if (buttons.containsValue(KeyInput.Command.MOVE_DOWN)) {
            if(ay < maxAcceleration) {
                addAccelerationY(totalAcc);
            }
        }

        if (buttons.containsValue(KeyInput.Command.MOVE_UP)) {
            if(Math.abs(ay) < maxAcceleration) {
                addAccelerationY(-totalAcc);
            }
        }

        if (buttons.containsValue(KeyInput.Command.MOVE_RIGHT)) {
            facingDirection = Direction.EAST;
            if(ax < maxAcceleration) {
                addAccelerationX(totalAcc);
            }
        }

        if (buttons.containsValue(KeyInput.Command.MOVE_LEFT)) {
            facingDirection = Direction.WEST;
            if(Math.abs(ax) < maxAcceleration) {
                addAccelerationX(-totalAcc);
            }
        }

        if (buttons.containsValue(KeyInput.Command.ACTION)) {
            // TODO: drop bomb etc. ??
        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_DOWN)) {
            if(canAttack) {
                Point p = (Point) this.getWorldPosition().clone();
                p.y += this.hitbox.height;
                new Projectile(p, Direction.SOUTH, SpriteType.PROJECTILE_PLAYER_1, this);
                this.canAttack = false;
            }
        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_UP)) {
            if(canAttack) {
                Point p = (Point) this.getWorldPosition().clone();
                p.y -= this.hitbox.height;
                new Projectile(p, Direction.NORTH, SpriteType.PROJECTILE_PLAYER_1, this);
                this.canAttack = false;
            }
        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_LEFT)) {
            if(canAttack) {
                Point p = (Point) this.getWorldPosition().clone();
                p.x -= this.hitbox.width;
                new Projectile(p, Direction.WEST, SpriteType.PROJECTILE_PLAYER_1, this);
                this.canAttack = false;
            }
        }

        if(buttons.containsValue(KeyInput.Command.ATTACK_RIGHT)) {
            if(canAttack) {
                Point p = (Point) this.getWorldPosition().clone();
                p.x += this.hitbox.width;
                new Projectile(p, Direction.EAST, SpriteType.PROJECTILE_PLAYER_1, this);
                this.canAttack = false;
            }
        }
    }

    @Override
    public void onCollision(ICollidable other) {
        super.onCollision(other);
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

    public Inventory getInventory() {
        return inventory;
    }
}
