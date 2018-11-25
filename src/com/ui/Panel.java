package com.ui;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Panel extends GuiElement {

    public enum PanelAlign { NORTH, MIDDLE, SOUTH, WEST, EAST }

    protected Panel parent;
    protected int margin;
    protected Color backgroundColor;
    protected Color borderColor = new Color(90, 90, 90, 255);
    protected boolean drawBorders;
    protected boolean isTransparent;
    protected PanelAlign panelAlign;

    protected List<GuiElement> elements = new ArrayList<>();

    public Panel(PanelAlign panelAlign, int width, int height,
                 Panel parent, Color bgColor, boolean isTransparent,
                 boolean borders, int margin) {
        super(width, height);

        this.panelAlign = panelAlign;
        this.parent = parent;
        this.backgroundColor = bgColor;
        this.isTransparent = isTransparent;
        this.drawBorders = borders;
        this.margin = margin;

        this.calculatePanelPosition();
    }

    public Panel(int x, int y, int width, int height,
                 Panel parent, Color bgColor, boolean isTransparent,
                 boolean borders, int margin) {
        super(x, y, width, height);

        this.margin = margin;
        this.backgroundColor = bgColor;
        this.drawBorders = borders;
        this.isTransparent = isTransparent;
        this.parent = parent;
    }

    public List<GuiElement> getElements() {
        return elements;
    }

    @Override
    public void render(Graphics g) {
        if(this.isVisible()) {

            if(this.elements.isEmpty()) return;

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

    public void shrink() {

        // TODO: shrink horizontally

        // top and bottom margins
        int height = margin;

        // calculate total element height
        for(GuiElement element : this.getElements()) {

            if(element instanceof Panel) {
                ((Panel) element).shrink();
            }

            height += element.getHeight() + margin;
        }

        // shrink the bottom of the panel to fit the content
        this.setHeight(height);
    }

    public abstract void updatePanelItems();

    public void calculatePanelPosition() {
        int newx = 0, newy = 0;

        switch (this.panelAlign) {
            case SOUTH:

                // TODO actually calculate stuff



                break;
            default:
                System.out.println("CalculatePanelPosition: Unsuported PanelAlignment: " + this.panelAlign);
        }

        this.x = newx;
        this.y = newy;
    }

    public GuiElement addElement(GuiElement e) {
        if(!this.elements.contains(e)) {
            this.elements.add(e);
        }
        return e;
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

    public PanelAlign getPanelAlign() {
        return panelAlign;
    }

    public void setPanelAlign(PanelAlign panelAlign) {
        this.panelAlign = panelAlign;
    }
}
