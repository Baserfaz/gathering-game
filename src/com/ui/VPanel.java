package com.ui;

import com.enumerations.HorizontalAlign;

import java.awt.*;

public class VPanel extends Panel {

    private int margin = 10;
    private HorizontalAlign horAlign;

    public VPanel(int x, int y, int w, int h, Color bgColor, boolean isTransparent, boolean borders, HorizontalAlign align) {
        super(x, y, w, h, bgColor, isTransparent, borders);
        this.horAlign = align;
    }

    @Override
    public void updatePanelItems() {

        int count = 0;

        GuiElement lastElement = null;

        // we want to put the items inside the panel under each other
        // the items are in the order they were added
        for(GuiElement e : this.getElements()) {

            int xx;
            int yy = this.y + margin;

            if(lastElement != null) {
                yy += (count * lastElement.height) + (count * margin);
            }

            switch (horAlign) {
                case CENTER:
                    xx = (this.x + this.width / 2) - e.width / 2;
                    break;
                case LEFT:
                    xx = this.x + margin;
                    break;
                case RIGHT:
                    xx = this.x + this.width - margin - e.width;
                    break;
                default:
                    System.out.println("VPanel:updatePanelItems: not supported alignment: " + horAlign);
                    continue;
            }

            e.setX(xx);
            e.setY(yy);

            count += 1;
            lastElement = e;
        }

    }

    @Override
    public void addElement(GuiElement e) {
        super.addElement(e);
        this.updatePanelItems();
    }

}
