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
import com.ui.Panel;

public class MouseInput implements MouseMotionListener, MouseListener {
    
    private GuiElement lastElementHovered;
    
    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
        
        Point mousePos = Game.instance.getMousePos();
        GameState state = Game.instance.getGamestate();
        List<Panel> panels = this.getElements(state);
        
        if(panels.isEmpty() || mousePos == null) return;
        
        for(Panel panel : panels) {
            if(panel.isEnabled() == false) { continue; }

            // panels elements
            for(GuiElement el : panel.getElements()) {
                if(el.isEnabled() == false) { continue; }

                if(Game.isMuted == false && panel.isMuted() == false && el.isMuted() == false) {
                    Game.instance.getSoundManager().playSound(SoundEffect.SELECT);
                }

                el.onClick();
                break;
            }
        }
    }
    
    private List<Panel> getElements(GameState state) {
        List<Panel> elements = new ArrayList<>();
        
        if(state == GameState.MAINMENU) {
            elements = Game.instance.getGuiElementManager().getMainmenuPanels();
        } else if(state == GameState.INGAME) {
            elements = Game.instance.getGuiElementManager().getIngamePanels();
        } else if(state == GameState.PAUSEMENU) {
            elements = Game.instance.getGuiElementManager().getPausemenuPanels();
        }
        return elements;
    }

    // hover effects on gui elements.
    public void mouseMoved(MouseEvent e) {
        Game.instance.setMousePos(e.getPoint());
        
        if(Game.instance.getGuiElementManager() == null) return;
        List<Panel> panels = this.getElements(Game.instance.getGamestate());
        if(panels.isEmpty()) return;
        
        boolean hoveredOnSomething = false;
        
        for(Panel panel : panels) {

            if(panel.isEnabled() == false ) { continue; }
            if(panel.getBounds().contains(e.getPoint()) == false) { continue; }

            // panels elements
            for(GuiElement el : panel.getElements()) {

                if(el.isEnabled() == false) { continue; }
                if(el.getBounds().contains(e.getPoint()) == false) { continue; }

                hoveredOnSomething = true;

                if(this.lastElementHovered != panel && Game.isMuted == false && panel.isMuted() == false && el.isMuted() == false) {
                    Game.instance.getSoundManager().playSound(SoundEffect.HOVER);
                }

                el.setHovering(true);
                el.onHover();
                this.lastElementHovered = el;
                break;
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
