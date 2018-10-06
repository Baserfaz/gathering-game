package com.ui;

import com.enumerations.HorizontalAlign;

import java.awt.*;

public class VPanel extends Panel {

    private int margin = 15;
    private HorizontalAlign horAlign;

    public VPanel(int x, int y, int w, int h, Color bgColor, boolean isTransparent, boolean borders, HorizontalAlign align) {
        super(x, y, w, h, bgColor, isTransparent, borders);
        this.horAlign = align;
    }

    @Override
    public void updatePanelItems() {

        int currentHeight = 0;

        // we want to put the items inside the panel under each other
        // the items are in the order they were added
        for(int i = 0; i < this.getElements().size(); i++) {

            // get current element
            GuiElement element = this.getElements().get(i);

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

        this.shrink(currentHeight);
    }

    public void shrink(int height) {

        // calculate height if its not yet calculated
        if(height <= 0) {
            height = margin;
            for(int i = 0; i < this.getElements().size(); i++) {
                GuiElement element = this.getElements().get(i);
                height += element.getHeight() + margin;
            }
        }

        // add bottom margin
        height += margin;

        // shrink the bottom of the panel to fit the content
        this.setHeight(height);
    }

    @Override
    public void addElement(GuiElement e) {
        super.addElement(e);
        this.updatePanelItems();
    }
}
