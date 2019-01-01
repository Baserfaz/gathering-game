package com.engine;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.gameobjects.*;

public class Handler {

    private List<GameObject> objects = new ArrayList<>();
    private List<GameObject> toBeDeleted = new ArrayList<>();
    private List<GameObject> lastFrameObjectsInView = new ArrayList<>();
    private Camera mainCam;

    public void tickGameObjects() {

        for(int i = 0; i < objects.size(); i++) {
            GameObject current = objects.get(i);
            if(current == null) continue;
            if(current.isDeleted()) {
                toBeDeleted.add(current);
                continue;
            }
            if(current.isEnabled()) current.tick();
        }

        // remove delete objects from objects array
        objects.removeAll(toBeDeleted);
        toBeDeleted.clear();
    }
    
    public void renderGameObjects(Graphics g) {
        
        // references
        List<Block> solidBlocks = new ArrayList<>();
        List<Actor> enemies = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        List<Gold> valuables = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        Actor player = null;

        if(mainCam == null) {
            mainCam = Game.instance.getCamera();
            if (mainCam == null) return;
        }
        
        // --------------------- calculate objs that camera sees ------------------------
        
        Rectangle camView = (Rectangle) mainCam.getCameraBounds().clone();
        
        int size = Game.CALCULATED_SPRITE_SIZE;

        // widen the camera view
        camView.x -= size;
        camView.width += 2 * size;
        
        camView.y -= size;
        camView.height += 2 * size;

        // calculate what objects are in view
        List<GameObject> objInView = new ArrayList<>();
        for(GameObject go : this.objects) {

            // we dont want to touch to-be-deleted game objects
            if(go == null || go.isDeleted()) continue;

            if(camView.contains(go.getCenterPosition())) {

                // if the object was not enabled in the last frame
                // then we need to activate it.
                if(!lastFrameObjectsInView.contains(go)) {
                    go.activate();

                    // TODO: there is a problem where the items that are out of
                    // TODO: the view of the camera are activated again when camera
                    // TODO: sees them again.

                }

                objInView.add(go);

            } else {
                go.deactivate();
            }
        }

        // cache last frame's objects in view
        lastFrameObjectsInView = objInView;

        // ---------------------- RENDER ---------------------------------

        // order objects
        for(GameObject current : objInView) {
            if(current instanceof Player) {
                player = (Actor) current;
            } else if(current instanceof Block) {
                solidBlocks.add((Block) current);
            } else if(current instanceof Item) {
                if(current instanceof Gold) valuables.add((Gold) current);
                else items.add((Item) current);
            } else if(current instanceof Projectile) {
                projectiles.add((Projectile) current);
            }
        }
        
        // render queue: back to front
        for(Block block : solidBlocks) {
            if(block.isVisible()) block.render(g);
        }

        for(Item item : items) {
            if(item.isVisible()) item.render(g);
        }

        for(Item valuable : valuables) {
            if(valuable.isVisible()) valuable.render(g);
        }

        for(Actor actor : enemies) {
            if(actor.isVisible()) actor.render(g);
        }

        if(player != null) {
            if(player.isVisible()) player.render(g);
        }

        for(Projectile p : projectiles) {
            if(p.isVisible()) p.render(g);
        }
    }

    public void AddObject(GameObject go) { this.objects.add(go); }
    public List<GameObject> getObjects() { return objects; }
}
