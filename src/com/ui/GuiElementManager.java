package com.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.enumerations.GameState;

public class GuiElementManager {

    private HashMap<GameState, List<Panel>> gameStateToPanelMap;

    public GuiElementManager() {
        this.gameStateToPanelMap = new HashMap<>();
        for(GameState state : GameState.values()) {
            gameStateToPanelMap.put(state, new ArrayList<>());
        }
    }

    public void render(Graphics g, GameState state) {
        for(Panel p : this.getPanels(state)) p.render(g);
    }
    
    public void tick(GameState state) {
        for(GuiElement e : this.getPanels(state)) e.tick();
    }
    
    public void activateGuiElementsInGameState(GameState state) {
        this.deactivateAllElements();
        for(GuiElement e : getPanels(state)) {
            e.isEnabled = true; 
            e.isVisible = true;
        }
    }
    
    private void deactivateAllElements() {
        for(GameState s : GameState.values()) {
            for(GuiElement e : this.getPanels(s)) {
                e.isEnabled = false;
                e.isVisible = false;
            }
        }
    }

    public List<Panel> getPanels(GameState state) { return this.gameStateToPanelMap.get(state); }
    public void addElementToMap(GameState state, Panel panel) { this.gameStateToPanelMap.get(state).add(panel); }
}
