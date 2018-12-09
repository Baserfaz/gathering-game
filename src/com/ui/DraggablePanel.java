package com.ui;

import com.engine.Game;

import java.awt.*;

public class DraggablePanel extends Panel {

    private final int headerHeight = 30;

    private boolean showHeader = true;
    private boolean isDragging = false;

    public DraggablePanel(int width, int height) {
        super(PanelAlign.MIDDLE, width, height, null, Colors.WHITE, false, true, 0);
    }

    @Override
    public void render(Graphics g) {
        if(!isVisible) return;

        Point pos = this.calculatePanelAlignmentPos();

        // draw the panel header
        if(showHeader) {
            g.setColor(Colors.BLUE);
            g.fillRect(pos.x, pos.y - headerHeight, getWidth(), headerHeight);
            // TODO: draw close button
        }

        // draw the panel
        g.setColor(Colors.WHITE);
        g.fillRect(pos.x, pos.y, getWidth(), getHeight());

        // draw border
        if(this.drawBorders) {
            g.setColor(this.borderColor);
            g.drawRect(pos.x - 1, pos.y - headerHeight - 1, getWidth() + 1, getHeight() + headerHeight + 1);
        }

        for(GuiElement element : this.elements) {
            element.render(g);
        }
    }

    @Override
    public void tick() {
        if(isDragging) {
            // TODO: get the start mouse position where we clicked.
            Point mousePos = Game.instance.getMousePos();
            this.x = mousePos.x;
            this.y = mousePos.y;
        }
    }

    @Override
    public void updatePanelItems() {
        // TODO
    }
}
