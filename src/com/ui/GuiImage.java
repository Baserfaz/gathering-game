package com.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.enumerations.DepthLevel;
import com.enumerations.InteractAction;

public class GuiImage extends InteractableGuiElement {

    private BufferedImage img;
    private DepthLevel deptLevel;
    
    public GuiImage(int x, int y, BufferedImage img, boolean isEnabled, 
            DepthLevel deptLevel, InteractAction onClickAction, InteractAction onHoverAction) {
        super(x, y, img.getWidth(), img.getHeight(), onClickAction, onHoverAction);
        this.img = img;
        this.deptLevel = deptLevel;
        this.isEnabled = isEnabled;
        this.isMuted = true;
    }
    
    // a shorthand constructor.
    public GuiImage(int x, int y, BufferedImage img, boolean isEnabled,
            InteractAction onClickAction, InteractAction onHoverAction) {
        super(x, y, img.getWidth(), img.getHeight(), onClickAction, onHoverAction);
        this.img = img;
        this.deptLevel = DepthLevel.FOREGROUND;
        this.isEnabled = isEnabled;
        this.isMuted = true;
    }

    public void render(Graphics g) {
        if(this.isVisible) g.drawImage(this.img, (int)this.x, (int)this.y, null);
    }
    
    public void tick() {}

    // ----- GETTERS & SETTERS -----
    public BufferedImage getImg() { return img; }
    public void setImg(BufferedImage img) { this.img = img; }
    public DepthLevel getDeptLevel() { return deptLevel; }
    public void setDeptLevel(DepthLevel deptLevel) { this.deptLevel = deptLevel; }
}
