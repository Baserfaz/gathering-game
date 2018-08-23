package com.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.enumerations.GameState;

public class GuiElementManager {

    private List<Panel> mainmenuPanel;
    private List<Panel> loadingPanel;
    private List<Panel> ingamePanel;
    private List<Panel> pausemenuPanel;
    private List<Panel> gameOverPanel;
    
    public GuiElementManager() {
        this.mainmenuPanel = new ArrayList<>();
        this.loadingPanel = new ArrayList<>();
        this.ingamePanel = new ArrayList<>();
        this.pausemenuPanel = new ArrayList<>();
        this.gameOverPanel = new ArrayList<>();
    }

    public void render(Graphics g, GameState state) {
        for(GuiElement e : this.getGuiElementList(state)) e.render(g);
    }
    
    public void tick(GameState state) {
        for(GuiElement e : this.getGuiElementList(state)) e.tick();
    }
    
    public void activateGuiElementsInGameState(GameState state) {
        this.deactivateAllElements();
        for(GuiElement e : getGuiElementList(state)) { 
            e.isEnabled = true; 
            e.isVisible = true;
        }
    }
    
    private void deactivateAllElements() {
        for(GameState s : GameState.values()) {
            for(GuiElement e : this.getGuiElementList(s)) {
                e.isEnabled = false;
                e.isVisible = false;
            }
        }
    }

    public List<Panel> getMainmenuPanels() {
        return mainmenuPanel;
    }

    public List<Panel> getLoadingPanels() {
        return loadingPanel;
    }

    public List<Panel> getIngamePanels() {
        return ingamePanel;
    }

    public List<Panel> getPausemenuPanels() {
        return pausemenuPanel;
    }

    public List<Panel> getGameOverPanels() {
        return gameOverPanel;
    }

    private List<Panel> getGuiElementList(GameState state) {
        
        List<Panel> selectedList = new ArrayList<>();
        
        switch(state) {
        case INGAME:
            selectedList = this.ingamePanel;
            break;
        case LOADING:
            selectedList = this.loadingPanel;
            break;
        case MAINMENU:
            selectedList = this.mainmenuPanel;
            break;
        case PAUSEMENU:
            selectedList = this.pausemenuPanel;
            break;
        case GAME_OVER:
            selectedList = this.gameOverPanel;
            break;
        default:
            System.out.println("GuiElementManager::render: Gamestate not supported!");
            break;
        }
        
        return selectedList;
    }
    
    // ---- GETTERS & SETTERS ----
    public void addElementToMainmenu(Panel panel) { this.mainmenuPanel.add(panel); }
    public void addMultipleElementsToMainmenu(List<Panel> ps) { this.mainmenuPanel.addAll(ps); }
    
    public void addElementToLoading(Panel panel) { this.loadingPanel.add(panel); }
    public void addMultipleElementsToLoading(List<Panel> ps) { this.loadingPanel.addAll(ps); }
    
    public void addElementToIngame(Panel panel) { this.ingamePanel.add(panel); }
    public void addMultipleElementsToIngame(List<Panel> ps) { this.ingamePanel.addAll(ps); }
    
    public void addElementToPauseMenu(Panel panel) { this.pausemenuPanel.add(panel); }
    public void addMultipleElementsToPausemenu(List<Panel> ps) { this.pausemenuPanel.addAll(ps); }
    
    public void addElementToGameOver(Panel panel) { this.gameOverPanel.add(panel); }
    public void addMultipleElementsToGameOver(List<Panel> ps) { this.gameOverPanel.addAll(ps); }
}
