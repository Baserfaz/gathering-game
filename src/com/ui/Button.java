package com.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.engine.Game;
import com.enumerations.InteractAction;
import com.utilities.RenderUtils;

public class Button extends InteractableGuiElement {

    private Color fontColor;
    private Color bgColor;
    private int fontSize;
    private BufferedImage img;
    private String txt;
    
    public Button(int x, int y, int width, int height, 
            String txt, Color fontColor, Color bgColor, int fontSize,
            InteractAction onClickAction, InteractAction onHoverAction) {
        
        super(x, y, width, height, onClickAction, onHoverAction);
        this.fontColor = fontColor;
        this.bgColor = bgColor;
        this.fontSize = fontSize;
        this.txt = txt;
        
    }
    
    public Button(int x, int y, BufferedImage img, 
            InteractAction onClickAction, InteractAction onHoverAction) {
        super(x, y, img.getWidth(), img.getHeight(), onClickAction, onHoverAction);
        this.img = img;
    }
    
    public void render(Graphics g) {
        if(this.isVisible()) {
            
            if(this.img != null) {
                
                if(this.isHovering) g.drawImage(RenderUtils.tintWithColor(this.img, Color.white), (int)this.x, (int)this.y, null);
                else g.drawImage(this.img, (int)this.x, (int)this.y, null);
                
            } else {
                
                Font font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, this.fontSize);
                g.setFont(font);
                
                Rectangle r = new Rectangle((int)this.x, (int)this.y, this.width, this.height);
                
                if(this.isHovering) g.setColor(this.bgColor.darker());
                else g.setColor(this.bgColor);
                
                // render box
                g.fillRect(r.x, r.y, r.width, r.height);
                
                int txtWidth = g.getFontMetrics().stringWidth(this.txt);
                int txtHeight = g.getFontMetrics().getHeight();
                
                int centerX = (r.x + r.width / 2) - txtWidth / 2;
                int centerY = (r.y + txtHeight + r.height / 2) - txtHeight / 2;
                
                // render text inside the button rectangle
                g.setColor(this.fontColor);
                g.drawString(this.txt, centerX, centerY);
            }
        }
    }

    public void tick() { if(this.isEnabled()) {} }
    
    // ------- GETTERS & SETTERS --------
    public Color getFontColor() { return fontColor; }
    public void setFontColor(Color fontColor) { this.fontColor = fontColor; }
    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }
    public Color getBgColor() { return bgColor; }
    public void setBgColor(Color bgColor) { this.bgColor = bgColor; }
}
