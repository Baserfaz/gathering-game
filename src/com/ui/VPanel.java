package com.ui;

import com.enumerations.HorizontalAlign;

import java.awt.*;

public class VPanel extends Panel {

    private HorizontalAlign horAlign;

    public VPanel(int x, int y, int w, int h,
                  Panel parent, Color bgColor, boolean isTransparent, boolean borders, int margin, HorizontalAlign align) {
        super(x, y, w, h, parent, bgColor, isTransparent, borders, margin);
        this.horAlign = align;
    }

    @Override
    public void updatePanelItems() {

        int currentHeight = 0;

        // we want to put the items inside the panel under each other
        // the items are in the order they were added
        for(GuiElement element : this.getElements()) {

            int xx;
            int yy = this.y + currentHeight + margin;

            switch (horAlign) {
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
                    System.out.println("VPanel:updatePanelItems: not supported alignment: " + horAlign);
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
