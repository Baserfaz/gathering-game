package com.ui;

import com.engine.Game;
import com.enumerations.InteractAction;

import java.awt.*;

public class TextField extends InteractableGuiElement {

    private final int borderMargin = 2;
    private final Color fontColor = Colors.BLACK;
    private final int labelFontSize = 30;

    private String value = "";

    private Panel parent;
    private Font font;
    private FontMetrics fontMetrics;

    private boolean isSelected = false;
    private boolean isEditable;
    private int textMargin;
    private int fontSize;
    private int maxLength;
    private int borderThickness;

    public TextField(Panel panel, int w, int h,
                     int textMargin, int fontSize, int maxlen,
                     boolean isEditable) {
        super(panel.x, panel.y, w, h, InteractAction.NONE, InteractAction.NONE);

        this.maxLength = maxlen;
        this.isEditable = isEditable;
        this.parent = panel;
        this.textMargin = textMargin;
        this.fontSize = fontSize;

        this.borderThickness = borderMargin * 2;

        this.font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, fontSize);
        this.fontMetrics = new Canvas().getFontMetrics(font);
    }

    @Override
    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        // cache default stroke
        Stroke oldStroke = g2d.getStroke();

        // render a box
        g2d.setColor(Colors.WHITE);
        g2d.fillRect(x, y, w, h);

        // set the border thickness
        g2d.setStroke(new BasicStroke(borderThickness));

        // render borders
        if(isSelected) {

            // make borders glow when selected
            g2d.setColor(Colors.YELLOW);
            g2d.drawRect(x - borderMargin, y - borderMargin, w + (borderMargin * 2), h + (borderMargin * 2));

        } else {

            g2d.setColor(Colors.GRAY);
            g2d.drawRect(x - borderMargin, y - borderMargin, w + (borderMargin * 2), h + (borderMargin * 2));

        }

        // render text
        g2d.setFont(font);
        g2d.setColor(fontColor);
        g2d.drawString(value, x + textMargin, y + textMargin + (this.fontMetrics.getHeight() / 2));

        // set the old stroke
        g2d.setStroke(oldStroke);

    }

    @Override
    public void tick() {}

    @Override
    public void onHover() {}

    @Override
    public void onClick() {
        if(!isSelected) isSelected = true;
    }

    @Override
    public void onUnfocus() {
        if(isSelected) {
            isSelected = false;
        }
    }

    // ---------

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public Panel getParent() {
        return parent;
    }

    public void setParent(Panel parent) {
        this.parent = parent;
    }

    public int getTextMargin() {
        return textMargin;
    }

    public void setTextMargin(int margin) {
        this.textMargin = margin;
    }

    public int getBorderMargin() { return borderMargin; }

    public int getBorderThickness() {
        return this.borderThickness;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public FontMetrics getFontMetrics() {
        return fontMetrics;
    }

    public void setFontMetrics(FontMetrics fontMetrics) {
        this.fontMetrics = fontMetrics;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getLabelFontSize() {
        return labelFontSize;
    }

}
