package com.ui;

import java.awt.*;

public class Separator extends GuiElement {

    private Panel parent;
    private int amount;

    public Separator(Panel parent, int height) {
        super(parent.x, parent.y, parent.w, height);
        this.parent = parent;
        this.amount = height;
    }

    @Override
    public void render(Graphics g) {}

    @Override
    public void tick() {}
}
