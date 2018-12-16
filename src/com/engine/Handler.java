package com.engine;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.enumerations.UnitType;
import com.gameobjects.Actor;
import com.gameobjects.Block;
import com.gameobjects.GameObject;
import com.gameobjects.Item;

public class Handler {

    private List<GameObject> objects = new ArrayList<>();

    public void tickGameObjects() {
        for(int i = 0; i < objects.size(); i++) {
            GameObject current = objects.get(i);
            if(current != null) current.tick();
        }
    }
    
    public void renderGameObjects(Graphics g) {
        
        // references
        List<Block> solidBlocks = new ArrayList<>();
        List<Actor> actors = new ArrayList<>();
        List<Item> items = new ArrayList<>();
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
            if(go == null) continue;
            if(camView.contains(go.getWorldPosition())) {
                objInView.add(go);
                go.activate();
            } else { go.deactivate(); }
        }
        
        // ---------------------- RENDER ---------------------------------

        for(int i = 0; i < objInView.size(); i++) {
            GameObject current = objInView.get(i);
            
            if(current instanceof Actor) {
                if(((Actor) current).getUnitType().equals(UnitType.PLAYER_UNIT)) {
                    player = (Actor) current;
                } else {
                    actors.add((Actor) current);
                }
            } else if(current instanceof Block) {
                Block block = (Block) current;
                solidBlocks.add(block);
            } else if(current instanceof Item) {
                Item item = (Item) current;
                items.add(item);
            }
        }
        
        // render queue: back to front
        for(Block block : solidBlocks) block.render(g);
        for(Actor actor : actors) actor.render(g);
        for(Item item : items) item.render(g);

        if(player != null) player.render(g);
    }

    // ---- GETTERS & SETTERS ----
    public void AddObject(GameObject go) { this.objects.add(go); }	
    public void RemoveObject(GameObject go) { this.objects.remove(go); }
    public List<GameObject> getObjects() { return objects; }
    public void setObjects(List<GameObject> objects) { this.objects = objects; }
}
