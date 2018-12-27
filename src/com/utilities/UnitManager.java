package com.utilities;

import java.util.ArrayList;
import java.util.List;

import com.enumerations.UnitType;
import com.data.Level;
import com.enumerations.SpriteType;
import com.gameobjects.Block;
import com.gameobjects.Actor;
import com.gameobjects.Player;

public class UnitManager {

    private List<Actor> unitInstances;
    private Player player;
    
    public UnitManager() {
        unitInstances = new ArrayList<>();
    }

    public void createPlayerUnit(Level level) {
        if(level != null && level.getBlocks().isEmpty() == false) { 
            Block spawn = level.getRandomValidBlock();
            this.player = new Player("Player", spawn.getTilePosition(), 3, 1);
        }
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
