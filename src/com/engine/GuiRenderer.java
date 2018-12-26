package com.engine;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

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

        // such as menus etc.
        this.createStaticGuiElements();
    }

    // ---- CREATION ----
    private void createStaticGuiElements() {
        this.createMainmenuElements();
        this.createLoadingElements();
        this.createPauseMenuElements();
        this.createGameOverElements();
        this.createIngameElements();
    }

    private void createIngameElements() {

        // health panel
        this.guiElementManager.addElementToMap(GameState.INGAME,
                GuiFactory.createDefaultHorizontalPanel(
                        null, Panel.PanelAlign.NORTH, Panel.PanelType.HEALTH,
                        true, Colors.YELLOW));
    }

    private void createPauseMenuElements() {

        VPanel panel = GuiFactory.createDefaultCenteredPanel(null, false,
                Colors.GRAY, Panel.PanelType.PAUSE);

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

        this.guiElementManager.addElementToMap(GameState.PAUSEMENU, panel);
    }
    
    private void createGameOverElements() {
        VPanel panel = GuiFactory.createDefaultCenteredPanel(null, false,
                Colors.GRAY, Panel.PanelType.GAMEOVER);
        Button exitButton = GuiFactory.createDefaultExitButton(panel);
        panel.addElement(exitButton);
        this.guiElementManager.addElementToMap(GameState.GAME_OVER, panel);
    }
    
    private void createLoadingElements() {}
    
    private void createMainmenuElements() {

        VPanel panel = GuiFactory.createDefaultCenteredPanel(
                null, false,
                Util.changeAlpha(Colors.BLACK, 200),
                Panel.PanelType.MAINMENU);

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

        this.guiElementManager.addElementToMap(GameState.MAINMENU, panel);
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

        // ---- create dynamic gui-elements ----
        this.createHealthGui();

        // ----
        this.guiElementManager.render(g, GameState.INGAME);
        
        // render debugging information
        this.renderDebugInfo(g);
    }

    private void createHealthGui() {
        Actor player = Game.instance.getUnitManager().getPlayer();
        if(player == null) return;

        ArrayList<Panel> panels = this.guiElementManager.getPanels(GameState.INGAME);

        // TODO: perhaps wrap the panels in a hashmap and give the panel a tag e.g. "HEALTH"
        Panel panel = panels.get(0);

        // only update the panel when hp changes
        if(panel.getElements().size() != player.getHealth().getCurrentHP()) {

            // first remove last images
            panel.getElements().clear();

            for (int i = 0; i < player.getHealth().getCurrentHP(); i++) {
                panel.addElement(GuiFactory.createHealthGuiImage(panel));
            }
        }

        panel.shrink();
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
                builder.append("player world pos: [" + wpos.x + ", " + wpos.y + "]\n");
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
