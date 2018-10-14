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
    
    protected String name;
    protected Health HP;
    protected UnitType unitType;
    protected Direction facingDirection = Direction.WEST;
    protected int attackDamage = 1;
    protected KeyInput keyInput;
    protected Level level;
    protected Block currentBlock;

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
        if(this.unitType == UnitType.PLAYER_UNIT) this.handleButtons();
    }
    
    public void render(Graphics g) {
        if(this.isVisible) {
            BufferedImage img = this.defaultStaticSprite;
            if(this.facingDirection == Direction.WEST) { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null);
            } else if(this.facingDirection == Direction.EAST) { RenderUtils.renderSpriteFlippedHorizontally(img, this.worldPosition, g);
            } else { g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null); }
        }
    }
    
    public void onDeath() {
        this.deactivate();
    }
    
    public void moveUp() {

        Point newPos = new Point(
                this.tilePosition.x,
                this.tilePosition.y - 1);

        this.updateTileposition(newPos);
    }
    
    public void moveDown() {
        Point newPos = new Point(
                this.tilePosition.x,
                this.tilePosition.y + 1);

        this.updateTileposition(newPos);
    }
    
    public void moveLeft() {
        Point newPos = new Point(
                this.tilePosition.x - 1,
                this.tilePosition.y);

        this.updateTileposition(newPos);
    }
    
    public void moveRight() {
        Point newPos = new Point(
                this.tilePosition.x + 1,
                this.tilePosition.y);

        this.updateTileposition(newPos);
    }

    private void updateTileposition(Point newPos) {
        Block block = level.getBlock(newPos);
        if(level.validateBlock(block)) {

            if(currentBlock != null) {
                currentBlock.removeActor(this);
            }

            this.setTilePosition(newPos);
            block.addActor(this);
            currentBlock = block;
        }
    }

    public void doAction() {
        
    }

    private void handleButtons() {
        Map<Integer, KeyInput.Command> buttons = this.keyInput.getButtons();
        if(buttons.containsValue(KeyInput.Command.MOVE_DOWN)) { this.moveDown(); }
        if(buttons.containsValue(KeyInput.Command.MOVE_UP)) { this.moveUp(); } 
        if(buttons.containsValue(KeyInput.Command.MOVE_LEFT)) { this.moveLeft(); }
        if(buttons.containsValue(KeyInput.Command.MOVE_RIGHT)) { this.moveRight(); }
        if(buttons.containsValue(KeyInput.Command.ACTION)) { this.doAction(); }
    }

    public Health getHP() { return this.HP; }
    public String getName() { return this.name; }
    public Direction getFacingDirection() { return facingDirection; }
    public void setFacingDirection(Direction facingDirection) { this.facingDirection = facingDirection; }

    public void setName(String name) {
        this.name = name;
    }

    public void setHP(Health HP) {
        this.HP = HP;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public KeyInput getKeyInput() {
        return keyInput;
    }

    public void setKeyInput(KeyInput keyInput) {
        this.keyInput = keyInput;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(Block currentBlock) {
        this.currentBlock = currentBlock;
    }
}
