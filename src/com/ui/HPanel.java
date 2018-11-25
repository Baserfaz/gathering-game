package com.ui;

import java.awt.*;

public class HPanel extends Panel {

    private Panel parent;
    private Color backgroundColor;
    private boolean isTransparent;
    private boolean hasBorders;
    private int margin;

    public HPanel(PanelAlign panelAlign, int width, int height,
                  Panel parent, Color bgColor, boolean isTransparent,
                  boolean borders, int margin) {
        super(panelAlign, width, height, parent, bgColor, isTransparent, borders, margin);

        this.parent = parent;
        this.backgroundColor = bgColor;
        this.isTransparent = isTransparent;
        this.hasBorders = borders;
        this.margin = margin;
    }

    public HPanel(int x, int y, int width, int height,
                  Panel parent, Color bgColor, boolean isTransparent,
                  boolean borders, int margin) {
        super(x, y, width, height, parent, bgColor, isTransparent, borders, margin);

        this.parent = parent;
        this.backgroundColor = bgColor;
        this.isTransparent = isTransparent;
        this.hasBorders = borders;
        this.margin = margin;
    }

    @Override
    public void updatePanelItems() {
        int currentWidth = 0;
        for(GuiElement element : this.getElements()) {

            int xx = this.x + currentWidth + margin;
            int yy = this.y + margin;

            element.setX(xx);
            element.setY(yy);

            currentWidth += element.getWidth() + margin;
        }
    }

    public Panel getParent() {
        return parent;
    }

    public void setParent(Panel parent) {
        this.parent = parent;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
    }

    public boolean isHasBorders() {
        return hasBorders;
    }

    public void setHasBorders(boolean hasBorders) {
        this.hasBorders = hasBorders;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
}
