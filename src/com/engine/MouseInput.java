package com.engine;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import com.enumerations.GameState;
import com.enumerations.SoundEffect;
import com.ui.GuiElement;

public class MouseInput implements MouseMotionListener, MouseListener {
    
    private GuiElement lastElementHovered;
    
    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
        
        Point mousePos = Game.instance.getMousePos();
        GameState state = Game.instance.getGamestate();
        List<GuiElement> elements = this.getElements(state);
        
        if(elements.isEmpty() || mousePos == null) return;
        
        for(GuiElement element : elements) {
            if(element.isEnabled()) {
                if(element.getBounds().contains(mousePos)) {
                    if(Game.isMuted == false && element.isMuted() == false) {
                        Game.instance.getSoundManager().playSound(SoundEffect.SELECT);
                    }
                    element.onClick();
                    break;
                }
            }
        }
    }
    
    private List<GuiElement> getElements(GameState state) {
        List<GuiElement> elements = new ArrayList<GuiElement>();
        
        if(state == GameState.MAINMENU) {
            elements = Game.instance.getGuiElementManager().getMainmenuElements();
        } else if(state == GameState.INGAME) {
            elements = Game.instance.getGuiElementManager().getIngameElements();
        } else if(state == GameState.PAUSEMENU) {
            elements = Game.instance.getGuiElementManager().getPausemenuElements();
        }
        
        return elements;
    }

    // hover effects on gui elements.
    public void mouseMoved(MouseEvent e) {
        Game.instance.setMousePos(e.getPoint());
        
        if(Game.instance.getGuiElementManager() == null) return;
        List<GuiElement> elements = this.getElements(Game.instance.getGamestate());
        if(elements.isEmpty()) return;
        
        boolean hoveredOnSomething = false;
        
        for(GuiElement element : elements) {
            if(element.isEnabled()) {
                element.setHovering(false);
                
                if(element.getBounds().contains(e.getPoint())) {
                    hoveredOnSomething = true;
                    
                    if(this.lastElementHovered != element && Game.isMuted == false && element.isMuted() == false) {
                        Game.instance.getSoundManager().playSound(SoundEffect.HOVER);
                    }
                    
                    element.setHovering(true);
                    element.onHover();
                    this.lastElementHovered = element;
                    break;
                }
            }
        }
        
        // if we didnt hover on anything
        // then reset last element.
        if(hoveredOnSomething == false) this.lastElementHovered = null;
        
    }

    public void mouseDragged(MouseEvent e) { Game.instance.setMousePos(e.getPoint()); }

    // not used
    public void mouseEntered(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
