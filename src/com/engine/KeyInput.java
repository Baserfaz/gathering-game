package com.engine;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enumerations.GameState;
import com.gameobjects.Actor;
import com.ui.GuiElement;
import com.ui.InteractableGuiElement;
import com.ui.Panel;
import com.ui.TextField;
import com.utilities.Util;

import javax.xml.soap.Text;

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

        // handle UI objects
        this.handleKeysInUI(e);

        if(Game.instance.getGamestate() == GameState.MAINMENU) this.handleKeysInMenu(e);
        else if(Game.instance.getGamestate() == GameState.INGAME) this.handleKeysInGame(e);
        else if(Game.instance.getGamestate() == GameState.PAUSEMENU) this.handleKeysInPauseMenu(e);
        
    }

    public void keyReleased(KeyEvent e) {
        
        // get the pressed key 
        int key = e.getKeyCode();
        
        buttons.remove(key);
    }

    private void handleKeysInUI(KeyEvent e) {

        int key = e.getKeyCode();
        char character = e.getKeyChar();

        // get the panels in current UI state
        List<Panel> panels = Util.getPanelsInGamestate(Game.instance.getGamestate());
        if(panels.isEmpty()) return;

        for(Panel panel : panels) {
            for(GuiElement el : panel.getElements()) {

                // handle textfields
                if(el instanceof TextField) {
                    TextField txt = (TextField) el;
                    if(txt.isSelected()) {
                        switch (key) {
                            case KeyEvent.VK_ESCAPE:
                                txt.setSelected(false);
                                e.consume();
                                break;
                            case KeyEvent.VK_BACK_SPACE:
                                if(txt.getValue().length() > 0) {
                                    txt.setValue(txt.getValue().substring(0, txt.getValue().length() - 1));
                                }
                                break;
                            default:
                                if(txt.getValue().length() < txt.getMaxLength()) {
                                    txt.setValue(txt.getValue() + character);
                                }
                                break;
                        }
                    }
                }
            }
        }
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
    
    private void handleKeysInMenu(KeyEvent e) {}
    
    public Map<Integer, KeyInput.Command> getButtons() { return this.buttons; }
}
