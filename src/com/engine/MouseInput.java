package com.engine;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import com.enumerations.GameState;
import com.enumerations.InteractAction;
import com.enumerations.SoundEffect;
import com.ui.GuiElement;
import com.ui.InteractableGuiElement;
import com.ui.Panel;
import com.utilities.Util;

public class MouseInput implements MouseMotionListener, MouseListener {
    
    private GuiElement lastElementHovered;
    
    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
        
        Point mousePos = Game.instance.getMousePos();
        GameState state = Game.instance.getGamestate();
        List<Panel> panels = Util.getPanelsInGamestate(state);
        
        if(panels.isEmpty() || mousePos == null) return;
        
        for(Panel panel : panels) {

            // panels elements
            for(GuiElement el : panel.getElements()) {

                if(el.getBounds().contains(mousePos) == false) {
                    // we didn't click this element!
                    // -> call unfocus
                    if(el instanceof InteractableGuiElement) {
                        InteractableGuiElement iel = (InteractableGuiElement) el;
                        iel.onUnfocus();
                    }
                    continue;
                }

                if(el.isEnabled() == false) continue;
                if(el instanceof InteractableGuiElement == false) continue;

                InteractableGuiElement iel = (InteractableGuiElement) el;

                if(Game.isMuted == false && panel.isMuted() == false && el.isMuted() == false) {
                    Game.instance.getSoundManager().playSound(SoundEffect.SELECT);
                }

                iel.onClick();
                break;
            }
        }
    }
    


    // hover effects on gui elements.
    public void mouseMoved(MouseEvent e) {
        Game.instance.setMousePos(e.getPoint());
        
        if(Game.instance.getGuiElementManager() == null) return;
        List<Panel> panels = Util.getPanelsInGamestate(Game.instance.getGamestate());
        if(panels.isEmpty()) return;
        
        boolean hoveredOnSomething = false;
        
        for(Panel panel : panels) {

            // if(panel.isEnabled() == false ) { continue; }
            // if(panel.getBounds().contains(e.getPoint()) == false) { continue; }

            // panels elements
            for(GuiElement el : panel.getElements()) {

                if(el instanceof InteractableGuiElement == false) continue;
                if(el.isEnabled() == false) { continue; }
                if(el.getBounds().contains(e.getPoint()) == false) { continue; }

                InteractableGuiElement iel = (InteractableGuiElement) el;

                hoveredOnSomething = true;

                if(this.lastElementHovered != el && Game.isMuted == false && panel.isMuted() == false && el.isMuted() == false) {
                    Game.instance.getSoundManager().playSound(SoundEffect.HOVER);
                }

                iel.setHovering(true);
                iel.onHover();
                this.lastElementHovered = el;
                break;
            }
        }

        if(hoveredOnSomething == false && this.lastElementHovered != null) {
            if(this.lastElementHovered instanceof InteractableGuiElement) {
                InteractableGuiElement iel = (InteractableGuiElement) this.lastElementHovered;
                iel.setHovering(false);
            }
            this.lastElementHovered = null;
        }
    }

    public void mouseDragged(MouseEvent e) { Game.instance.setMousePos(e.getPoint()); }

    // not used
    public void mouseEntered(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
