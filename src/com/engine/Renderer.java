package com.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.List;

import com.enumerations.GameState;
import com.gameobjects.GameObject;
import com.gameobjects.Actor;
import com.gameobjects.Item;
import com.interfaces.ICollidable;
import com.ui.Colors;

public class Renderer {

    private Handler handler;
    private GuiRenderer guirenderer;
    private Camera cam;
    
    public Renderer() {
        this.handler = Game.instance.getHandler();
        this.guirenderer = Game.instance.getGuiRenderer();
        this.cam = Game.instance.getCamera();
    }
    
    public void preRender(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        GameState gamestate = Game.instance.getGamestate();
        
        this.setRenderingHints(g2d);
        
        if(gamestate == GameState.INGAME) this.renderIngame(g);
        else if(gamestate== GameState.MAINMENU) this.renderMainMenu(g);
        else if(gamestate == GameState.LOADING) this.renderLoading(g);
        else if(gamestate == GameState.PAUSEMENU) this.renderPauseMenu(g);
        else if(gamestate == GameState.GAME_OVER) this.renderGameOver(g);
    }
    
    private void setRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
    
    private void renderGameOver(Graphics g) {
        this.guirenderer.renderGameOver(g);
    }
    
    private void renderPauseMenu(Graphics g) {
        this.guirenderer.renderPauseMenu(g);
    }
    
    private void renderLoading(Graphics g) {
        this.fillScreen(g, Color.white);
        this.guirenderer.renderLoading(g);        
    }
    
    private void renderMainMenu(Graphics g) {
        this.fillScreen(g, Colors.WHITE);
        this.guirenderer.renderMenu(g);
    }
    
    private void renderIngame(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        Rectangle r = cam.getCameraBounds();
        
        // set background
        this.fillScreen(g, Colors.GAME_BACKGROUND);
        
        // set zoom level
        g2d.scale(1, 1);

        // move the "camera"
        g2d.translate(-r.x, -r.y);
        
        handler.renderGameObjects(g2d);
        
        this.renderDebug(g2d);
        this.guirenderer.renderIngame(g2d);
    }
    
    private void renderDebug(Graphics g) {
        
        // render camera debug 
        if(Game.drawCameraRect) {
            Camera cam = Game.instance.getCamera();
            Rectangle camRect = cam.getCameraBounds();
            g.setColor(Game.cameraRectColor);
            g.drawRect(camRect.x, camRect.y, camRect.width, camRect.height);
        }

        // draw hitboxes
        if(Game.drawHitboxes) {

            List<GameObject> gos = Game.instance.getHandler().getObjects();

            // blocks
            g.setColor(Game.hitboxColor);
            for (GameObject go : gos) {
                if(go instanceof Actor || go instanceof Item || go.isDeleted()) continue;

                Rectangle hitbox = ((ICollidable)go).getHitbox();
                if(hitbox != null) {
                    g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
                }
            }

            // actors
            g.setColor(Color.BLUE);
            for(Actor a : Game.instance.getUnitManager().getUnitInstances()) {
                if(a.isDeleted()) return;

                Rectangle hitbox = ((ICollidable)a).getHitbox();
                if(hitbox != null) {
                    g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
                }
            }

            // items
            g.setColor(Color.CYAN);
            for(GameObject go : gos) {
                if(!(go instanceof Item) || go.isDeleted()) continue;

                Rectangle hitbox = ((ICollidable)go).getHitbox();
                if(hitbox != null) {
                    g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
                }
            }
        }

        if(Game.drawCollisionDistance) {
            Actor player = Game.instance.getUnitManager().getPlayer();
            if(player == null) return;
            Point pos = player.getCenterPosition();
            int r = Game.CALCULATED_MAX_COLLISION_DISTANCE;
            int r2 = r / 2;

            g.setColor(Game.collisionCircleColor);
            g.drawOval(pos.x - r2, pos.y - r2, r, r);
        }

        // DRAW CENTER POINT OF THE CAMERA
        //Point camCent = Game.instance.getCamera().getCameraCenterPosition();
        //g.setColor(Game.cameraRectColor);
        //g.drawRect(camCent.x, camCent.y, 1, 1);
        
    }
    
    private void fillScreen(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(0, 0, Game.WIDTH + 10, Game.HEIGHT + 10);
    }

}
