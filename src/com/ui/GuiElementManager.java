package com.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.enumerations.DepthLevel;
import com.enumerations.GameState;

public class GuiElementManager {

    private List<GuiElement> mainmenuElements;
    private List<GuiElement> loadingElements;
    private List<GuiElement> ingameElements;
    private List<GuiElement> pausemenuElements;
    private List<GuiElement> gameOverElements;
    
    public GuiElementManager() {
        this.mainmenuElements = new ArrayList<GuiElement>();
        this.loadingElements = new ArrayList<GuiElement>();
        this.ingameElements = new ArrayList<GuiElement>();
        this.pausemenuElements = new ArrayList<GuiElement>();
        this.gameOverElements = new ArrayList<GuiElement>();
    }

    public void render(Graphics g, GameState state) {
        
        List<GuiElement> buttons = new ArrayList<GuiElement>();
        List<GuiElement> imagesBackground = new ArrayList<GuiElement>();
        List<GuiElement> imagesForeground = new ArrayList<GuiElement>();
        List<GuiElement> imagesMiddleground = new ArrayList<GuiElement>();
                
        List<GuiElement> elems = this.getGuiElementList(state);
        
        if(elems.isEmpty()) {
            return;
        }
        
        for(GuiElement e : elems) {
            if(e instanceof Button) buttons.add(e);
            else if(e instanceof GuiImage) {
                
                GuiImage img = (GuiImage) e;
                
                if(img.getDeptLevel() == DepthLevel.BACKGROUND) {
                    imagesBackground.add(e);
                } else if(img.getDeptLevel() == DepthLevel.FOREGROUND) {
                    imagesForeground.add(e);
                } else if(img.getDeptLevel() == DepthLevel.MIDDLEGROUND) {
                    imagesMiddleground.add(e);
                } else {
                    System.out.println("GuiElementManager::render: no depthlevel set for guiImage: " + e.toString());
                }
                
            } else {
                System.out.println("GuiElementManager::render: unsupported GuiElement type: " + e.getClass().getTypeName());
            }
        }
        
        // render in back to front order
        for(GuiElement e : imagesBackground) e.render(g);
        for(GuiElement e : imagesMiddleground) e.render(g);        
        for(GuiElement e : imagesForeground) e.render(g);
        for(GuiElement e : buttons) e.render(g);
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
    
    private List<GuiElement> getGuiElementList(GameState state) {
        
        List<GuiElement> selectedList = new ArrayList<GuiElement>();
        
        switch(state) {
        case INGAME:
            selectedList = this.ingameElements;
            break;
        case LOADING:
            selectedList = this.loadingElements;
            break;
        case MAINMENU:
            selectedList = this.mainmenuElements;
            break;
        case PAUSEMENU:
            selectedList = this.pausemenuElements;
            break;
        case GAME_OVER:
            selectedList = this.gameOverElements;
            break;
        default:
            System.out.println("GuiElementManager::render: Gamestate not supported!");
            break;
        }
        
        return selectedList;
    }
    
    // ---- GETTERS & SETTERS ----
    public void addElementToMainmenu(GuiElement element) { this.mainmenuElements.add(element); }
    public List<GuiElement> getMainmenuElements() { return mainmenuElements; }
    public void addMultipleElementsToMainmenu(List<GuiElement> es) { this.mainmenuElements.addAll(es); }
    
    public void addElementToLoading(GuiElement element) { this.loadingElements.add(element); }
    public List<GuiElement> getLoadingElements() { return loadingElements; }
    public void addMultipleElementsToLoading(List<GuiElement> loadingElements) { this.loadingElements.addAll(loadingElements); }
    
    public void addElementToIngame(GuiElement element) { this.ingameElements.add(element); }
    public List<GuiElement> getIngameElements() { return ingameElements; }
    public void addMultipleElementsToIngame(List<GuiElement> ingameElements) { this.ingameElements.addAll(ingameElements); }
    
    public void addElementToPauseMenu(GuiElement element) { this.pausemenuElements.add(element); }
    public List<GuiElement> getPausemenuElements() { return this.pausemenuElements; }
    public void addMultipleElementsToPausemenu(List<GuiElement> elements) { this.pausemenuElements.addAll(elements); }
    
    public void addElementToGameOver(GuiElement element) { this.gameOverElements.add(element); }
    public List<GuiElement> getGameOverElements() { return this.gameOverElements; }
    public void addMultipleElementsToGameOver(List<GuiElement> elements) { this.gameOverElements.addAll(elements); }
}
