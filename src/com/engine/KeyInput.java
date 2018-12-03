package com.engine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enumerations.GameState;
import com.ui.GuiElement;
import com.ui.Panel;
import com.ui.TextField;
import com.utilities.Util;

public class KeyInput extends KeyAdapter {

    private Map<Integer, Command> buttons = new HashMap<>();
    private Map<Integer, Command> keyBinds = new HashMap<>();
    
    public enum Command {
        MOVE_LEFT, MOVE_RIGHT, MOVE_DOWN, MOVE_UP, ACTION,
        ATTACK_LEFT, ATTACK_RIGHT, ATTACK_UP, ATTACK_DOWN
    }

    public final char[] validTextFieldCharacters =
    {
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
        'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z',

        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
        'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
        'W', 'X', 'Y', 'Z',

        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',

        '-', '_'
    };

    public KeyInput() {
        
        // bind keys
        this.keyBinds.put(KeyEvent.VK_A, Command.MOVE_LEFT);
        this.keyBinds.put(KeyEvent.VK_LEFT, Command.ATTACK_LEFT);
        
        this.keyBinds.put(KeyEvent.VK_D, Command.MOVE_RIGHT);
        this.keyBinds.put(KeyEvent.VK_RIGHT, Command.ATTACK_RIGHT);
        
        this.keyBinds.put(KeyEvent.VK_S, Command.MOVE_DOWN);
        this.keyBinds.put(KeyEvent.VK_DOWN, Command.ATTACK_DOWN);
        
        this.keyBinds.put(KeyEvent.VK_W, Command.MOVE_UP);
        this.keyBinds.put(KeyEvent.VK_UP, Command.ATTACK_UP);

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
        int key = e.getKeyCode();

        // handle UI objects
        this.handleKeysInUI(e);

        if(buttons.containsKey(key)) {
            buttons.remove(key);
        }
    }

    public void resetButtons() {
        this.buttons.clear();
    }

    private void handleKeysInUI(KeyEvent e) {

        // get the panels in current UI state
        List<Panel> panels = Util.getPanelsInGamestate(Game.instance.getGamestate());
        if(panels.isEmpty()) return;

        for(Panel panel : panels) { if(this.handlePanelElements(panel, e)) break; }
    }

    private boolean handlePanelElements(Panel panel, KeyEvent e) {
        for(GuiElement el : panel.getElements()) {
            if(el instanceof Panel) { this.handlePanelElements((Panel) el, e); }
            if(handleTextFields(el, e)) return true;
        }
        return false;
    }

    private boolean handleTextFields(GuiElement el, KeyEvent e) {
        if(el instanceof TextField) {
            TextField txt = (TextField) el;
            if(txt.isSelected()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        txt.setSelected(false);
                        e.consume();
                        break;
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
                            char c = e.getKeyChar();
                            if(Util.isCharInArray(c, validTextFieldCharacters)) {
                                txt.setValue(txt.getValue() + c);
                            }
                        }
                        break;
                }
                return true;
            }
        }
        return false;
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
