package com.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import com.data.Health;
import com.enumerations.InteractAction;
import com.enumerations.ElementAlign;
import com.enumerations.GameState;
import com.gameobjects.Actor;
import com.ui.Button;
import com.ui.GuiElementManager;
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
        int margin = 10;
        int starty = 400;
        int xpos = (Game.CAMERA_WIDTH / 2) - width / 2;
        
        Button resumeButton = new Button(xpos, starty, width, height,
                "Resume", Color.black, Color.white, 40, InteractAction.RESUME, InteractAction.NONE);
        Button exitButton = new Button(xpos, starty + height + margin, width, height,
                "Exit", Color.black, Color.white, 40, InteractAction.EXIT_TO_OS, InteractAction.NONE);
        
        this.guiElementManager.addElementToPauseMenu(resumeButton);
        this.guiElementManager.addElementToPauseMenu(exitButton);
        
    }
    
    private void createGameOverElements() {
        
        int width = 350;
        int height = 75;
        int starty = 400;
        int xpos = (Game.CAMERA_WIDTH / 2) - width / 2;
        
        Button exitButton = new Button(xpos, starty, width, height,
                "Exit", Color.black, Color.white, 40, InteractAction.EXIT_TO_MENU, InteractAction.NONE);
        
        this.guiElementManager.addElementToGameOver(exitButton);
    }
    
    private void createLoadingElements() {}
    
    private void createMainmenuElements() {
        
        int margin = 10;
        int starty = 450;
        int xpos = Game.CAMERA_WIDTH / 2 - 175;
        
        int width = 350;
        int height = 75;
        
        Button playButton = new Button(xpos, starty, width, height, "Play", Color.black, Color.white, 40,
                InteractAction.PLAY, InteractAction.NONE);
        Button exitButton = new Button(xpos, starty + height + margin,
                width, height, "Exit", Color.black, Color.white, 40,
                InteractAction.EXIT_TO_OS, InteractAction.NONE);
        
        // add elements to list
        this.guiElementManager.addElementToMainmenu(playButton);
        this.guiElementManager.addElementToMainmenu(exitButton);
    }
    
    // ----- RENDERING -----
    public void renderLoading(Graphics g) {
        this.guiElementManager.render(g, GameState.LOADING);
        this.renderString("Loading", Game.CAMERA_WIDTH - 20, Game.CAMERA_HEIGHT, Color.white, 50f, ElementAlign.LEFT, g);
    }
    
    public void renderMenu(Graphics g) {
        int centerx = Game.CAMERA_WIDTH / 2;
        
        this.guiElementManager.render(g, GameState.MAINMENU);
        this.renderString(Game.VERSION, centerx, 700, Color.white, 30, ElementAlign.CENTER, g);
    }
    
    public void renderGameOver(Graphics g) {
        int centerx = Game.CAMERA_WIDTH / 2;
        this.renderString("Game over", centerx, 200, Color.white, 60f, ElementAlign.CENTER, g);
        this.guiElementManager.render(g, GameState.GAME_OVER);
    }
    
    public void renderPauseMenu(Graphics g) {
        this.renderString("Paused", Game.CAMERA_WIDTH / 2, 100, Color.white, 60f, ElementAlign.CENTER, g);
        this.guiElementManager.render(g, GameState.PAUSEMENU);
    }
    
    public void renderIngame(Graphics g) {
        
        Camera cam = Game.instance.getCamera();
        Rectangle r = cam.getCameraBounds();
        
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
            
            this.renderString(info, x, y, Game.debugInfoColor, 24f, ElementAlign.RIGHT, g);
        }
    }

    public void renderString(String msg, int x, int y, Color color, float size, ElementAlign align, Graphics g) {
        
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
            System.out.println("GuiRenderer::renderString: ElementAlign not supported!");
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
