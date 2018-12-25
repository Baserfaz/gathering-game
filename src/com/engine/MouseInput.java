package com.engine;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import com.enumerations.GameState;
import com.enumerations.SoundEffect;
import com.ui.GuiElement;
import com.ui.InteractableGuiElement;
import com.ui.Panel;
import com.utilities.Util;

public class MouseInput implements MouseMotionListener, MouseListener {
    
    private GuiElement lastElementHovered;
    private boolean hoveredOnSomething;

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {

        if(e.getButton() != MouseEvent.BUTTON1) return;

        Point mousePos = Game.instance.getMousePos();
        List<Panel> panels = Util.getPanelsInCurrentGamestate();
        
        if(panels.isEmpty() || mousePos == null) return;
        
        for(Panel panel : panels) {
            handlePanelElements(panel, mousePos);
        }
    }

    // go through panel's child panels too recursively
    private void handlePanelElements(Panel panel, Point mousePos) {
        for(GuiElement el : panel.getElements()) {
            if(el instanceof Panel) { this.handlePanelElements((Panel) el, mousePos); }
            if(handleClick(panel, el, mousePos)) break;
        }
    }

    private boolean handleClick(Panel panel, GuiElement el, Point mousePos) {

        // tie mouse position to camera position

        Rectangle cameraBounds = Game.instance.getCamera().getCameraBounds();
        Point mousePoint = mousePos;
        mousePoint.x += cameraBounds.x;
        mousePoint.y += cameraBounds.y;

        if(el.getBounds().contains(mousePoint) == false) {
            // we didn't click this element -> call unfocus
            if(el instanceof InteractableGuiElement) {
                InteractableGuiElement iel = (InteractableGuiElement) el;
                iel.onUnfocus();
            }
            return false;
        }

        if(el.isEnabled() == false || el instanceof InteractableGuiElement == false) return false;
        InteractableGuiElement iel = (InteractableGuiElement) el;

        if(Game.isMuted == false) {
            Game.instance.getSoundManager().playSound(SoundEffect.SELECT);
        }

        iel.onClick();
        return true;
    }

    // hover effects on gui elements.
    public void mouseMoved(MouseEvent e) {
        Game.instance.setMousePos(e.getPoint());
        
        if(Game.instance.getGuiElementManager() == null) return;
        List<Panel> panels = Util.getPanelsInCurrentGamestate();
        if(panels == null || panels.isEmpty()) return;

        this.hoveredOnSomething = false;

        for(Panel panel : panels) { if(handleHoverOnPanelElements(panel, e)) break; }

        if(hoveredOnSomething == false && this.lastElementHovered != null) {
            if(this.lastElementHovered instanceof InteractableGuiElement) {
                InteractableGuiElement iel = (InteractableGuiElement) this.lastElementHovered;
                iel.setHovering(false);
            }
            this.lastElementHovered = null;
        }
    }

    private boolean handleHoverOnPanelElements(Panel panel, MouseEvent e) {
        // panels elements
        for(GuiElement el : panel.getElements()) {
            if(el instanceof Panel) { this.handleHoverOnPanelElements((Panel) el, e); }
            if(this.handleHoverOnElement(el, e)) return true;
        }
        return false;
    }

    private boolean handleHoverOnElement(GuiElement el, MouseEvent e) {
        if(el instanceof InteractableGuiElement == false || el.isEnabled() == false) { return false; }

        // tie the mouse position to camera position
        Rectangle cameraBounds = Game.instance.getCamera().getCameraBounds();
        Point mousePoint = e.getPoint();
        mousePoint.x += cameraBounds.x;
        mousePoint.y += cameraBounds.y;

        boolean hovering = el.getBounds().contains(mousePoint);
        if(!hovering) return false;

        InteractableGuiElement iel = (InteractableGuiElement) el;
        this.hoveredOnSomething = true;

        if(this.lastElementHovered != el && Game.isMuted == false) {
            Game.instance.getSoundManager().playSound(SoundEffect.HOVER);
        }

        iel.setHovering(true);
        iel.onHover();
        this.lastElementHovered = el;
        return true;
    }

    public void mouseDragged(MouseEvent e) {
        Game.instance.setMousePos(e.getPoint());

        // TODO: get panels, check if we are dragging draggable gui-element etc. etc.
    }

    // not used
    public void mouseEntered(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
