package com.ui;


import com.engine.Game;

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

    protected int xrelcam = 0, yrelcam = 0;

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
    }

    public Panel(int x, int y, int width, int height,
                 Panel parent, Color bgColor, boolean isTransparent,
                 boolean borders, int margin) {
        super(x, y, width, height);

        this.panelAlign = null;
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

            // relative to camera position
            Rectangle rectangle = Game.instance.getCamera().getCameraBounds();
            int xx = x + (int) rectangle.getX();
            int yy = y + (int) rectangle.getY();

            // calculate position using PanelAlignments
            if (this.panelAlign != null) {
                switch (this.panelAlign) {
                    case NORTH:
                        yy = (int) rectangle.getY();
                        break;
                    case SOUTH:
                        yy = (int) rectangle.getY() + (int) rectangle.getHeight() - this.h;
                        break;
                    default:
                        break;
                }
            }

            // cache the calculated position relative to camera
            this.xrelcam = xx;
            this.yrelcam = yy;

            // draw rect
            if(!this.isTransparent) {
                g.setColor(backgroundColor);
                g.fillRect(xx, yy, w, h);
            }

            // draw borders
            if (drawBorders) {
                g.setColor(borderColor);
                g.drawRect(xx, yy, w, h);
            }

            // render all elements inside this panel
            for(GuiElement e : elements) {
                e.render(g);
            }
        }
    }

    public void shrink() {
        this.shrinkVertically();
        this.shrinkHorizontally();
    }

    private void shrinkHorizontally() {
//        GuiElement widestElement = null;
//
//        // calculate total element height
//        for(GuiElement element : this.getElements()) {
//
//            // get widest element
//            // if(widestElement == null) { widestElement = element; }
//            // else { if(widestElement.w < element.w) { widestElement = element; } }
//
//            if(element instanceof Panel) {
//                ((Panel) element).shrink();
//            }
//
//            height += element.getHeight() + margin;
//        }
//
//        // shrink the bottom of the panel to fit the content
//        this.setHeight(height);

        // this.setWidth(widestElement.w + margin); // TODO: breaks panel inside panel width
    }

    private void shrinkVertically() {

        // top and bottom margins
        int height = margin;

        if(this instanceof VPanel) {

            // calculate total element height
            for (GuiElement element : this.getElements()) {

                if (element instanceof Panel) {
                    ((Panel) element).shrink();
                }

                height += element.getHeight() + margin;
            }

        } else {

            Integer highestItemHeight = null;

            // HPanel
            for (GuiElement element : this.getElements()) {

                if(highestItemHeight == null) {
                    highestItemHeight = element.getHeight();
                } else {
                    if(element.getHeight() > highestItemHeight) {
                        highestItemHeight = element.getHeight();
                    }
                }

                if (element instanceof Panel) {
                    ((Panel) element).shrink();
                }
            }

            height += highestItemHeight + margin;
        }

        // shrink the bottom of the panel to fit the content
        this.setHeight(height);

    }

    public abstract void updatePanelItems();

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

    public int getXrelcam() {
        return xrelcam;
    }

    public int getYrelcam() {
        return yrelcam;
    }
}
