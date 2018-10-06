package com.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GuiImage extends GuiElement {

    private BufferedImage img;
    
    public GuiImage(int x, int y, BufferedImage img) {
        super(x, y, img.getWidth(), img.getHeight());
        this.img = img;
        this.isMuted = true;
    }

    public void render(Graphics g) {
        if(this.isVisible) g.drawImage(this.img, this.x, this.y, null);
    }

    public void tick() {}

    // ----- GETTERS & SETTERS -----
    public BufferedImage getImg() { return img; }
    public void setImg(BufferedImage img) { this.img = img; }
}
