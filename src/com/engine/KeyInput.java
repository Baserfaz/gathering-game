package com.engine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import com.enumerations.GameState;
import com.gameobjects.Actor;

public class KeyInput extends KeyAdapter {

    private Map<Integer, Command> buttons = new HashMap<Integer, Command>();
    private Map<Integer, Command> keyBinds = new HashMap<Integer, Command>();
    
    public enum Command { MOVE_LEFT, MOVE_RIGHT, MOVE_DOWN, MOVE_UP, ACTION }
    
    public KeyInput() {
        
        // bind keys
        this.keyBinds.put(KeyEvent.VK_A, Command.MOVE_LEFT);
        this.keyBinds.put(KeyEvent.VK_LEFT, Command.MOVE_LEFT);
        
        this.keyBinds.put(KeyEvent.VK_D, Command.MOVE_RIGHT);
        this.keyBinds.put(KeyEvent.VK_RIGHT, Command.MOVE_RIGHT);
        
        this.keyBinds.put(KeyEvent.VK_S, Command.MOVE_DOWN);
        this.keyBinds.put(KeyEvent.VK_DOWN, Command.MOVE_DOWN);
        
        this.keyBinds.put(KeyEvent.VK_W, Command.MOVE_UP);
        this.keyBinds.put(KeyEvent.VK_UP, Command.MOVE_UP);
        
        this.keyBinds.put(KeyEvent.VK_SPACE, Command.ACTION);
        this.keyBinds.put(KeyEvent.VK_SPACE, Command.ACTION);
        
    }

    public void keyPressed(KeyEvent e) {

        // get the pressed key 
        int key = e.getKeyCode();
        
        if(buttons.containsKey(key) == false) {
            buttons.put(key, this.keyBinds.get(key));
        }

        // -------------- HANDLE INPUTS ------------------

        if(Game.instance.getGamestate() == GameState.MAINMENU) this.handleKeysInMenu(e);
        else if(Game.instance.getGamestate() == GameState.INGAME) this.handleKeysInGame(e);
        else if(Game.instance.getGamestate() == GameState.PAUSEMENU) this.handleKeysInPauseMenu(e);
        
    }

    public void keyReleased(KeyEvent e) {
        
        // get the pressed key 
        int key = e.getKeyCode();

        Command cmd = this.keyBinds.get(key);
        
        buttons.remove(key);
    }

    private void handleKeysInGame(KeyEvent e) {
        
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_F1) {
            Game.drawDebugInfo = !Game.drawDebugInfo;
        } else if(key == KeyEvent.VK_F2) {
            Game.drawCameraRect = !Game.drawCameraRect;
        } else if(key == KeyEvent.VK_F3) {
            Game.isPaused = !Game.isPaused;
        } else if(key == KeyEvent.VK_ESCAPE) {
            Game.instance.setGamestate(GameState.PAUSEMENU);
            Game.isPaused = true;
        }
    }

    private void handleKeysInPauseMenu(KeyEvent e) {
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_ESCAPE) {
            Game.instance.setGamestate(GameState.INGAME);
            Game.isPaused = false;
        }
    }
    
    private void handleKeysInMenu(KeyEvent e) {
        int key = e.getKeyCode();   
        
        if(key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    
    public Map<Integer, KeyInput.Command> getButtons() { return this.buttons; }
}
