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

    @Override
    public GuiElement addElement(GuiElement e) {
        super.addElement(e);
        this.updatePanelItems();
        return e;
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
}
