package com.ui;

import java.awt.*;

public class HPanel extends Panel {

    private boolean elementsEqualSize;

    public HPanel(PanelAlign panelAlign, int width, int height,
                  Panel parent, Color bgColor, Color borderColor, boolean isTransparent,
                  boolean borders, int margin, boolean elementsEqualSize) {
        super(panelAlign, width, height, parent, bgColor, borderColor, isTransparent, borders, 2, margin);

        this.elementsEqualSize = elementsEqualSize;
        this.backgroundColor = bgColor;
    }

    @Override
    public GuiElement addElement(GuiElement e) {
        super.addElement(e);

        if(this.elementsEqualSize) {
            // TODO: get highest element height and set it to all elements.
        }

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
