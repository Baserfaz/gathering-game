package com.ui;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Panel extends GuiElement {

    private Color backgroundColor;
    private Color borderColor = new Color(90, 90, 90, 255);
    private boolean drawBorders;
    private boolean isTransparent;

    private List<GuiElement> elements = new ArrayList<>();

    public Panel(int x, int y, int width, int height, Color bgColor, boolean isTransparent, boolean borders) {
        super(x, y, width, height);
        this.backgroundColor = bgColor;
        this.drawBorders = borders;
        this.isTransparent = isTransparent;
    }

    public List<GuiElement> getElements() {
        return elements;
    }

    @Override
    public void render(Graphics g) {
        if(this.isVisible()) {

            // draw rect
            if(!this.isTransparent) {
                g.setColor(backgroundColor);
                g.fillRect(x, y, w, h);
            }

            // draw borders
            if (drawBorders) {
                g.setColor(borderColor);
                g.drawRect(x, y, w, h);
            }

            // render all elements inside this panel
            for(GuiElement e : elements) {
                e.render(g);
            }
        }
    }

    public abstract void updatePanelItems();

    public void addElement(GuiElement e) {
        this.elements.add(e);
    }

    public void removeElement(GuiElement e) {
        this.elements.remove(e);
    }

    @Override
    public void tick() {
        for(GuiElement e : elements) {
            e.tick();
        }
    }
}
