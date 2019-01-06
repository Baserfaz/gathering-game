package com.utilities;

import java.util.ArrayList;
import java.util.List;

import com.enumerations.EnemyType;
import com.data.Level;
import com.gameobjects.Block;
import com.gameobjects.Actor;
import com.gameobjects.Player;
import com.gameobjects.Slime;

public class UnitManager {

    private List<Actor> unitInstances;
    private Player player;
    
    public UnitManager() {
        unitInstances = new ArrayList<>();
    }

    public void createPlayerUnit(Level level) {
        if(!isLevelValid(level)) return;
        Block spawn = level.getRandomValidBlock();
        this.player = new Player("Player", spawn.getTilePosition(), 3, 1);
        this.unitInstances.add(player);
    }

    public void createEnemyUnit(Level level, EnemyType enemyType) {
        if(!isLevelValid(level)) return;
        Block spawn = level.getRandomValidBlock();

        switch (enemyType) {
            case SLIME_NORMAL:
                Slime slime = new Slime(spawn.getTilePosition());
                this.unitInstances.add(slime);
                break;
            default:
                System.out.println("UnitManager.createEnemyUnit: error: not supported EnemyType: " + enemyType);
                break;
        }
    }

    private boolean isLevelValid(Level level) {
        return level != null && !level.getBlocks().isEmpty();
    }

    public List<Actor> getUnitInstances() { return unitInstances; }
    public Player getPlayer() { return this.player; }
}
