package com.engine;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.enumerations.UnitType;
import com.gameobjects.*;

public class Handler {

    private List<GameObject> objects = new ArrayList<>();
    private List<GameObject> lastFrameObjectsInView = new ArrayList<>();

    public void tickGameObjects() {
        for(int i = 0; i < objects.size(); i++) {
            GameObject current = objects.get(i);
            if(current != null) current.tick();
        }
    }
    
    public void renderGameObjects(Graphics g) {
        
        // references
        List<Block> solidBlocks = new ArrayList<>();
        List<Actor> enemies = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        List<Gold> valuables = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        Actor player = null;
        
        Camera cam = Game.instance.getCamera();
        if(cam == null) return;
        
        // --------------------- calculate objs that camera sees ------------------------
        
        Rectangle camView = (Rectangle) cam.getCameraBounds().clone();
        
        int size = Game.CALCULATED_SPRITE_SIZE;
        
        camView.x -= size;
        camView.width += 2 * size;
        
        camView.y -= size;
        camView.height += 2 * size;
        
        List<GameObject> objInView = new ArrayList<>();
        for(int i = 0; i < this.objects.size(); i++) {
            GameObject go = this.objects.get(i);

            // we dont want to touch to-be-deleted game objects
            if(go == null || go.isDeleted()) continue;

            if(camView.contains(go.getWorldPosition())) {

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

        for(int i = 0; i < objInView.size(); i++) {
            GameObject current = objInView.get(i);
            
            if(current instanceof Actor) {
                if(((Actor) current).getUnitType().equals(UnitType.PLAYER_UNIT)) {
                    player = (Actor) current;
                } else {
                    enemies.add((Actor) current);
                }
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
        for(Block block : solidBlocks) block.render(g);
        for(Item item : items) item.render(g);
        for(Item valuable : valuables) valuable.render(g);

        for(Actor actor : enemies) actor.render(g);
        if(player != null) player.render(g);

        for(Projectile p : projectiles) p.render(g);
    }

    // ---- GETTERS & SETTERS ----
    public void AddObject(GameObject go) { this.objects.add(go); }	
    public void RemoveObject(GameObject go) { this.objects.remove(go); }
    public List<GameObject> getObjects() { return objects; }
    public void setObjects(List<GameObject> objects) { this.objects = objects; }
}
