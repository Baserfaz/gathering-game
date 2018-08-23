package com.gameobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import com.data.Health;
import com.engine.Game;
import com.engine.KeyInput;
import com.enumerations.ActorState;
import com.enumerations.Direction;
import com.enumerations.SpriteType;
import com.enumerations.UnitType;
import com.utilities.RenderUtils;

public class Actor extends GameObject {
    
    protected String name;
    protected Health HP;
    protected UnitType unitType;
    
    protected BufferedImage frame;
    
    protected ActorState actorState = ActorState.IDLING;
    protected Direction facingDirection = Direction.WEST;
    protected Rectangle attackBox;
    
    protected float movementSpeed = 0.5f;
    protected int attackDamage = 1;
    
    protected Point2D.Float acceleration = new Point2D.Float();
    protected Point2D.Float velocity = new Point2D.Float();
    
    protected float maxVelocity = 5f;
    
    protected KeyInput keyInput;
    
    private float dampenValue = 0.1f;
    
    public Actor(String name, Point worldPos, UnitType unitType, SpriteType spriteType, int hp, int damage) {
        super(worldPos, spriteType);
        
        this.name = name;
        this.attackDamage = damage;
        this.unitType = unitType;
        this.HP = new Health(hp, this);
        
        this.keyInput = Game.instance.getKeyInput();
    }
    
    public void tick() {
        
        this.updateHitbox();
        
        if(this.unitType == UnitType.PLAYER_UNIT) this.handleButtons();
        
        // movement
        this.velocity = new Point2D.Float(
                this.velocity.x + this.acceleration.x, 
                this.velocity.y + this.acceleration.y);
        
        this.worldPosition = new Point(
                this.worldPosition.x + (int)this.velocity.x,
                this.worldPosition.y + (int)this.velocity.y);
        
        // set min/max velocity.
        if(this.velocity.x > this.maxVelocity) this.velocity.x = this.maxVelocity;
        if(this.velocity.x < -this.maxVelocity) this.velocity.x = -this.maxVelocity;
        if(this.velocity.y > this.maxVelocity) this.velocity.y = this.maxVelocity;
        if(this.velocity.y < -this.maxVelocity) this.velocity.y = -this.maxVelocity;
        
        // dampen velocity 
        if(this.velocity.x > 0f) this.velocity.x -= this.dampenValue;
        else if(this.velocity.x < 0f) this.velocity.x += this.dampenValue;
        if(this.velocity.y > 0f) this.velocity.y -= this.dampenValue;
        else if(this.velocity.y < 0f) this.velocity.y += this.dampenValue;
        
        // TODO: this is kind of a hack tbh.
        this.acceleration.x = 0f;
        this.acceleration.y = 0f;
    }
    
    public void render(Graphics g) {
        if(this.isVisible) {
            
            BufferedImage img = this.defaultStaticSprite;
            if(this.facingDirection == Direction.WEST) { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null);
            } else if(this.facingDirection == Direction.EAST) { RenderUtils.renderSpriteFlippedHorizontally(img, this.worldPosition, g);
            } else { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null); }
            
            // debug
            g.setColor(Color.red);
            g.drawRect(this.hitbox.x, this.hitbox.y, this.hitbox.width, this.hitbox.height);
            
        }
    }
    
    public void onDeath() {
        this.deactivate();
    }
    
    public void moveUp() {
        this.acceleration.y = -this.movementSpeed;
    }
    
    public void moveDown() {
        this.acceleration.y = this.movementSpeed;
    }
    
    public void moveLeft() {
        this.acceleration.x = -this.movementSpeed;
        this.facingDirection = Direction.WEST;
    }
    
    public void moveRight() {
        this.acceleration.x = this.movementSpeed;
        this.facingDirection = Direction.EAST;
    }
    
    public void doAction() {
        
    }
    
    private void handleButtons() {
        
        // keys are handled in actor class because this overrides the AWT key press handling.
        
        Map<Integer, KeyInput.Command> buttons = this.keyInput.getButtons();
        if(buttons.containsValue(KeyInput.Command.MOVE_DOWN)) { this.moveDown(); }
        if(buttons.containsValue(KeyInput.Command.MOVE_UP)) { this.moveUp(); } 
        if(buttons.containsValue(KeyInput.Command.MOVE_LEFT)) { this.moveLeft(); }
        if(buttons.containsValue(KeyInput.Command.MOVE_RIGHT)) { this.moveRight(); }
        if(buttons.containsValue(KeyInput.Command.ACTION)) { this.doAction(); }
    }
    
    // --------- GETTERS & SETTERS --------
    public Health getHP() { return this.HP; }
    public String getName() { return this.name; }
    public Direction getFacingDirection() { return facingDirection; }
    public void setFacingDirection(Direction facingDirection) { this.facingDirection = facingDirection; }
    public ActorState getActorState() { return this.actorState; }
    public Point2D.Float getVelocity() { return this.velocity; }
    public Point2D.Float getAcceleration() { return this.acceleration; }

}
