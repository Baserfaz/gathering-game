package com.engine;

import java.awt.*;
import java.text.DecimalFormat;

import com.enumerations.HorizontalAlign;
import com.enumerations.GameState;
import com.gameobjects.Actor;
import com.ui.*;
import com.ui.Button;
import com.ui.Panel;
import com.utilities.SpriteCreator;
import com.utilities.Util;

public class GuiRenderer {
    
    private GuiElementManager guiElementManager;
    
    private DecimalFormat df;
    private SpriteCreator sc;
    
    public GuiRenderer() {
    
        this.guiElementManager = Game.instance.getGuiElementManager();
        this.sc = Game.instance.getSpriteCreator();
        
        this.df = new DecimalFormat();
        this.df.setMaximumFractionDigits(2);

        // when constructing the GuiRenderer,
        // it will create and cache the game state specific,
        // "static" gui-elements as well.
        this.createGuiElements();
    }

    // ---- CREATION ----
    private void createGuiElements() {
        this.createMainmenuElements();
        this.createLoadingElements();
        this.createPauseMenuElements();
        this.createGameOverElements();
        this.createIngameElements();
    }

    private void createIngameElements() {

        HPanel panel = GuiFactory.createDefaultHorizontalPanel(
                null, Panel.PanelAlign.SOUTH, false, Colors.GRAY);

        for(int i = 0; i < 5; i++) {
            String btnName = "btn" + i;
            Button btn = new Button(
                    panel, 100, 25, btnName,
                    Colors.BLACK, Colors.WHITE, 16,
                    () -> System.out.println("clicked: " + btnName),
                    null);

            panel.addElement(btn);
        }
        panel.shrink();
        this.guiElementManager.addElementToIngame(panel);
    }

    private void createPauseMenuElements() {

        VPanel panel = GuiFactory.createDefaultCenteredPanel(null, false, Colors.GRAY);
        Button resumeButton = GuiFactory.createDefaultResumeButton(panel);
        Button exitButton = GuiFactory.createDefaultExitToMainMenuButton(panel);
        PlainText pauseHeader = GuiFactory.createDefaultPlainText(panel,
                HorizontalAlign.CENTER, "Pause", Colors.WHITE);
        Separator separator = new Separator(panel, 10);

        panel.addElement(pauseHeader);
        panel.addElement(separator);
        panel.addElement(resumeButton);
        panel.addElement(exitButton);

        panel.shrink();

        this.guiElementManager.addElementToPauseMenu(panel);
    }
    
    private void createGameOverElements() {
        VPanel panel = GuiFactory.createDefaultCenteredPanel(null, false, Colors.GRAY);
        Button exitButton = GuiFactory.createDefaultExitButton(panel);
        panel.addElement(exitButton);
        this.guiElementManager.addElementToGameOver(panel);
    }
    
    private void createLoadingElements() {}
    
    private void createMainmenuElements() {

        VPanel panel = GuiFactory.createDefaultCenteredPanel(
                null, false, Util.changeAlpha(Colors.BLACK, 200)
        );

        panel.addElement(new Separator(panel, 20));
        panel.addElement(GuiFactory.createDefaultPlainText(panel,
                HorizontalAlign.CENTER, "Awesome Dungeons", Colors.BLUE));
        panel.addElement(new Separator(panel, 10));

        panel.addElement(GuiFactory.createDefaultPlayButton(panel));
        panel.addElement(GuiFactory.createDefaultExitButton(panel));

        panel.addElement(GuiFactory.createDefaultPlainText(panel,
                HorizontalAlign.LEFT, "Username", Colors.WHITE));

        panel.addElement(GuiFactory.createDefaultTextField(panel));

        panel.shrink();

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
            int y = r.y + 40;
            
            Rectangle camRect = Game.instance.getCamera().getCameraBounds();
            
            // create info strings
            StringBuilder builder = new StringBuilder();
            builder.append("---- SYSTEM ----\n");
            builder.append("fps: " + Game.FPS + "\n");
            builder.append("version: " + Game.VERSION + "\n");
            builder.append("cam pos: " + camRect.x + ", " + camRect.y + "\n");

            if(Game.instance.getUnitManager().getPlayer() != null) {

                Actor player = Game.instance.getUnitManager().getPlayer();

                Point wpos = player.getWorldPosition();
                Point tilepos = player.getTilePosition();
                builder.append("player world pos: [" + wpos.x + ", " + wpos.y + "]\n");
                builder.append("player tile pos: [" + tilepos.x + ", " + tilepos.y + "]\n");

                builder.append(String.format("Acceleration: %.2f, %.2f\n",
                        player.getAcceleration_x(), player.getAcceleration_y()));

                builder.append(String.format("Velocity: %.2f, %.2f\n",
                        player.getVelocity_x(), player.getVelocity_y()));

            }
            this.renderString(builder.toString(), x, y, Game.debugInfoColor, 24f, HorizontalAlign.RIGHT, g);
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
