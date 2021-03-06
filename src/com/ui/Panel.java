package com.ui;


import com.engine.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Panel extends GuiElement {

    public enum PanelAlign { NORTH, MIDDLE, SOUTH, WEST, EAST }
    public enum PanelType { HEALTH, COINS, RUNTIME, PAUSE, GAMEOVER, MAINMENU }

    protected Panel parent;
    protected int margin;
    protected Color backgroundColor;
    protected Color borderColor;
    protected int borderThickness;
    protected boolean drawBorders;
    protected boolean isTransparent;

    protected PanelType panelType;
    protected PanelAlign panelAlign;

    protected int xrelcam = 0, yrelcam = 0;

    protected List<GuiElement> elements = new ArrayList<>();

    public Panel(PanelAlign panelAlign, PanelType panelType, int width, int height,
                 Panel parent, Color bgColor, Color borderColor, boolean isTransparent,
                 boolean borders, int borderThickness, int margin) {
        super(width, height);

        this.panelType = panelType;
        this.panelAlign = panelAlign;
        this.parent = parent;
        this.backgroundColor = bgColor;
        this.isTransparent = isTransparent;
        this.drawBorders = borders;
        this.margin = margin;
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
    }

    public List<GuiElement> getElements() {
        return elements;
    }

    protected Point calculatePanelAlignmentPos() {

        // relative to camera position
        Rectangle cameraBounds = Game.instance.getCamera().getCameraBounds();
        int xx = x + (int) cameraBounds.getX();
        int yy = y + (int) cameraBounds.getY();

        // calculate position using PanelAlignments
        if (this.panelAlign != null) {
            switch (this.panelAlign) {
                case NORTH:
                    yy = (int) cameraBounds.getY();
                    break;
                case SOUTH:
                    yy = (int) cameraBounds.getY() + (int) cameraBounds.getHeight() - this.h;
                    break;
                case MIDDLE:
                    yy = ((int) cameraBounds.getHeight() / 2) - (h / 2);
                    xx = ((int) cameraBounds.getWidth() / 2) - (w / 2);
                case WEST:
                    break;
                case EAST:
                    xx = ((int) cameraBounds.getWidth()) - w;
                default:
                    break;
            }
        }

        // cache the calculated position relative to camera
        this.xrelcam = xx;
        this.yrelcam = yy;

        return new Point(xx, yy);
    }

    @Override
    public void render(Graphics g) {
        if(!isVisible || this.elements.isEmpty()) return;

        Graphics2D g2d = (Graphics2D) g;
        Stroke defaultStroke = g2d.getStroke();

        Point pos = this.calculatePanelAlignmentPos();

        // draw rect
        if(!this.isTransparent) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(pos.x, pos.y, w, h);
        }

        if (drawBorders) {
            g2d.setStroke(new BasicStroke(borderThickness));
            g2d.setColor(borderColor);
            g2d.drawRect(pos.x - borderThickness, pos.y - borderThickness,
                    w + borderThickness * 2, h + borderThickness * 2);
            g2d.setStroke(defaultStroke);
        }

        // render all elements inside this panel
        for(GuiElement e : elements) {
            e.render(g);
        }
    }

    public void shrink() {
        if(this.elements.isEmpty()) return;
        this.shrinkVertically();
        this.shrinkHorizontally();
    }

    private void shrinkHorizontally() {

        // shrinks the child elements to be maximum of panel width
        for(GuiElement e : this.getElements()) {
            if(e.w > this.w) {
                int diff = e.w - this.w;
                e.w = this.w;
                e.x += diff / 2;
            }
        }
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

            // HPanel

            Integer highestItemHeight = null;
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
    public PanelType getPanelType() { return panelType; }

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
