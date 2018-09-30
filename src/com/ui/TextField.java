package com.ui;

import com.engine.Game;

import java.awt.*;

public class TextField extends GuiElement {

    private final int borderMargin = 2;
    private final Color fontColor = Colors.BLACK;

    private String value = "Lollero";
    private boolean isSelected = false;
    private boolean isEditable;
    private Panel parent;
    private int textMargin;
    private int fontSize;
    private Font font;
    private FontMetrics fontMetrics;
    private int borderThickness;

    public TextField(Panel panel, int w, int h,
                     int textMargin, int fontSize,
                     boolean isEditable) {
        super(panel.x, panel.y, w, h);

        this.isEditable = isEditable;
        this.parent = panel;
        this.textMargin = textMargin;
        this.fontSize = fontSize;

        this.borderThickness = borderMargin * 2;

        this.font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, fontSize);
        this.fontMetrics = new Canvas().getFontMetrics(font);
    }

    public void onSelect() {


    }

    public void onDeselect() {



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
    public void tick() {

    }

    @Override
    public void onHover() {}

    @Override
    public void onClick() {
        if(!isSelected) isSelected = true;
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

    public int getBorderMargin() {
        return borderMargin;
    }

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
}
