package com.utilities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.enumerations.UnitType;
import com.data.Level;
import com.enumerations.SpriteType;
import com.gameobjects.Block;
import com.gameobjects.Actor;

public class UnitManager {

    private List<Actor> unitInstances;
    private Actor player;
    
    public UnitManager() {
        unitInstances = new ArrayList<Actor>();
    }

    public void createPlayerUnit(Level level) {
        
        if(level != null && level.getBlocks().isEmpty() == false) { 
            Block spawn = level.getRandomValidBlock();
            this.player = this.createUnitInstance("Player", spawn, UnitType.PLAYER_UNIT, 3, 1);
        } else {
            
           Point spawnPoint = new Point(0, 0);
           this.player = this.createUnitInstance("Player", spawnPoint, UnitType.PLAYER_UNIT, 3, 1);
        }
        
        System.out.println("Units created!");
        
    }
    
    private Actor createUnitInstance(String name, Point wpos, UnitType unitType, int hp, int dmg) {
        
        Actor unit = null;
        
        switch(unitType) {
        case PLAYER_UNIT:
            unit = new Actor(name, wpos, unitType, SpriteType.PLAYER, hp, dmg);
            break;

        default:
            System.out.println("ActorManager::createEnemyInstance: unsupported enemy type: " + unitType);
            break;
        }
        
        if(unit != null) {
            this.unitInstances.add(unit);
        }
        
        return unit;
        
    }
    
    private Actor createUnitInstance(String name, Block block, UnitType unitType, int health, int damage) {
        
        Actor unit = null;
        
        switch(unitType) {
        case PLAYER_UNIT:
            unit = new Actor(name, block.getWorldPosition(), unitType, SpriteType.PLAYER, health, damage);
            break;

        default:
            System.out.println("ActorManager::createEnemyInstance: unsupported enemy type: " + unitType);
            break;
        }
        
        if(unit != null) {
            this.unitInstances.add(unit);
        }
        
        return unit;
    }
    
    public void removeUnit(Actor go) {
        for(Actor actor : unitInstances) {
            if(actor.equals(go)) {
                unitInstances.remove(go);
                break;
            }
        }
    }
    
    // ---- GETTERS & SETTERS ----
    public List<Actor> getUnitInstances() { return unitInstances; }
    public Actor getPlayer() { return this.player; }
}
