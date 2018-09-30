package com.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import com.enumerations.HorizontalAlign;
import com.enumerations.InteractAction;
import com.enumerations.GameState;
import com.gameobjects.Actor;
import com.ui.*;
import com.utilities.SpriteCreator;

public class GuiRenderer {
    
    private GuiElementManager guiElementManager;
    
    private DecimalFormat df;
    private SpriteCreator sc;
    
    public GuiRenderer() {
    
        this.guiElementManager = Game.instance.getGuiElementManager();
        this.sc = Game.instance.getSpriteCreator();
        
        this.df = new DecimalFormat();
        this.df.setMaximumFractionDigits(2);
        
        this.createGuiElements();
    }

    // ---- CREATION ----
    private void createGuiElements() {
        this.createMainmenuElements();
        this.createLoadingElements();
        this.createPauseMenuElements();
        this.createGameOverElements();
    }
    
    private void createPauseMenuElements() {
        
        int width = 350;
        int height = 75;

        VPanel panel = new VPanel(Game.WIDTH / 2, Game.HEIGHT / 2, 500, 600, Color.red, true, false, HorizontalAlign.CENTER);

        Button resumeButton = new Button(panel, width, height,
                "Resume", Color.black, Color.white, 40, InteractAction.RESUME, InteractAction.NONE);

        Button exitButton = new Button(panel, width, height,
                "Exit", Color.black, Color.white, 40, InteractAction.EXIT_TO_OS, InteractAction.NONE);

        panel.addElement(resumeButton);
        panel.addElement(exitButton);

        this.guiElementManager.addElementToPauseMenu(panel);
        
    }
    
    private void createGameOverElements() {
        
        int width = 350;
        int height = 75;

        VPanel panel = new VPanel(Game.WIDTH / 2, Game.HEIGHT / 2, 500, 600, Color.red, true, false, HorizontalAlign.CENTER);

        Button exitButton = new Button(panel, width, height,
                "Exit", Color.black, Color.white, 40, InteractAction.EXIT_TO_MENU, InteractAction.NONE);

        panel.addElement(exitButton);

        this.guiElementManager.addElementToGameOver(panel);
    }
    
    private void createLoadingElements() {}
    
    private void createMainmenuElements() {

        int panelW = 500;
        int panelH = 600;

        int width = 350;
        int height = 75;

        int fontSize = 60;

        VPanel panel = new VPanel((Game.WIDTH / 2) - (panelW / 2), Game.HEIGHT / 2, panelH, panelW, Color.red, true, false, HorizontalAlign.CENTER);

        Button playButton = new Button(panel, width, height, "Play", Color.black, Color.white, fontSize,
                InteractAction.PLAY, InteractAction.NONE);

        Button exitButton = new Button(panel, width, height, "Exit", Color.black, Color.white, fontSize,
                InteractAction.EXIT_TO_OS, InteractAction.NONE);

        PlainText text = new PlainText(panel,"Alpha version", fontSize, new Color(0, 0, 0,255));

        panel.addElement(playButton);
        panel.addElement(exitButton);
        panel.addElement(text);

        this.guiElementManager.addElementToMainmenu(panel);
    }
    
    // ----- RENDERING -----
    public void renderLoading(Graphics g) {
        this.guiElementManager.render(g, GameState.LOADING);
    }
    
    public void renderMenu(Graphics g) {
        this.guiElementManager.render(g, GameState.MAINMENU);
    }
    
    public void renderGameOver(Graphics g) {
        this.guiElementManager.render(g, GameState.GAME_OVER);
    }
    
    public void renderPauseMenu(Graphics g) {
        this.guiElementManager.render(g, GameState.PAUSEMENU);
    }
    
    public void renderIngame(Graphics g) {
        this.guiElementManager.render(g, GameState.INGAME);
        
        // render debugging information
        this.renderDebugInfo(g);
    }
    
    private void renderDebugInfo(Graphics g) {
        if(Game.drawDebugInfo) {

            Camera cam = Game.instance.getCamera();
            Rectangle r = cam.getCameraBounds();
            
            int x = r.x + 20;
            int y = r.y + 150;
            
            Rectangle camRect = Game.instance.getCamera().getCameraBounds();
            
            // create info strings
            String info = "---- SYSTEM ----\n";
            info += "fps: " + Game.FPS + "\n";
            info += "version: " + Game.VERSION + "\n";
            info += "cam pos: " + camRect.x + ", " + camRect.y + "\n";
            
            Actor player = Game.instance.getUnitManager().getPlayer();
            
            if(player != null) {
                info += "---- PLAYER ----\n";
                
                info += "accel.x: " + player.getAcceleration().x + "\n";
                info += "accel.y: " + player.getAcceleration().y + "\n";
                
                info += "vel.x: " + player.getVelocity().x + "\n";
                info += "vel.y: " + player.getVelocity().y + "\n";
            }
            
            this.renderString(info, x, y, Game.debugInfoColor, 24f, HorizontalAlign.RIGHT, g);
        }
    }

    public void renderString(String msg, int x, int y, Color color, float size, HorizontalAlign align, Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        Font font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, size);
        g2d.setColor(color);
        g2d.setFont(font);

        int offsetx = 0;
        int offsety = -g.getFontMetrics().getHeight();

        switch(align) {
        case CENTER:
            offsetx = -g.getFontMetrics().stringWidth(msg) / 2;
            break;
        case LEFT:
            offsetx = -g.getFontMetrics().stringWidth(msg);
            break;
        case RIGHT:
            // do nothing
            break;
        default:
            System.out.println("GuiRenderer::renderString: HorizontalAlign not supported!");
            break;
        }

        int xx =  x + offsetx;
        int yy = y + offsety;

        for (String line : msg.split("\n")) {
            g2d.drawString(line, xx, yy);
            yy += g.getFontMetrics().getHeight() + Game.TEXT_LINEHEIGHT;
        }
    }
}
