package com.ui;

import com.enumerations.HorizontalAlign;

import java.awt.*;

public class VPanel extends Panel {

    private HorizontalAlign itemHorAlign;

    public VPanel(PanelAlign panelAlign, PanelType panelType, int w, int h, Panel parent,
                  Color bgColor, Color borderColor,
                  boolean isTransparent, boolean borders, int margin,
                  HorizontalAlign itemHorAlign) {

        super(panelAlign, panelType, w, h, parent, bgColor, borderColor, isTransparent, borders, 2, margin);
        this.itemHorAlign = itemHorAlign;
    }

    @Override
    public void updatePanelItems() {

        int currentHeight = 0;

        // we want to put the items inside the panel under each other
        // the items are in the order they were added
        for(GuiElement element : this.getElements()) {

            int xx;
            int yy = this.y + currentHeight + margin;

            switch (itemHorAlign) {
                case CENTER:
                    xx = (this.x + this.w / 2) - element.w / 2;
                    break;
                case LEFT:
                    xx = this.x + margin;
                    break;
                case RIGHT:
                    xx = this.x + this.w - margin - element.w;
                    break;
                default:
                    System.out.println("VPanel:updatePanelItems: not supported alignment: " + itemHorAlign);
                    continue;
            }

            element.setX(xx);
            element.setY(yy);

            currentHeight += element.getHeight() + margin;
        }
    }

    @Override
    public GuiElement addElement(GuiElement e) {
        super.addElement(e);
        this.updatePanelItems();
        return e;
    }
}
