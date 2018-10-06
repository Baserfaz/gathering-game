package com.ui;

import com.engine.Game;

import java.awt.*;

public class PlainText extends GuiElement {

    private Panel parent;
    private int fontSize;
    private Color color;
    private String text;
    private Font font;

    public PlainText(Panel parent, String text, int size, Color color) {
        super(parent.x, parent.y, 0, 0);

        this.text = text;
        this.parent = parent;
        this.color = color;
        this.fontSize = size;
        this.font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, fontSize);

        FontMetrics fontMetric = new Canvas().getFontMetrics(font);

        this.setWidth(fontMetric.stringWidth(text));
        this.setHeight(fontMetric.getHeight());
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(color);
        g2d.setFont(font);

        int yy = y + g2d.getFontMetrics().getHeight();

        for (String line : text.split("\n")) {
            g2d.drawString(line, x, yy);
            yy += g.getFontMetrics().getHeight() + Game.TEXT_LINEHEIGHT;
        }
    }

    @Override
    public void tick() { }

}
