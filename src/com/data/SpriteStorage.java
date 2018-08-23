package com.data;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.engine.Game;
import com.enumerations.SpriteType;
import com.utilities.SpriteCreator;

public class SpriteStorage {

    private Map<SpriteType, BufferedImage> sprites;
    
    public SpriteStorage() {
        this.sprites = new HashMap<SpriteType, BufferedImage>();
    }
 
    public void loadSprites() {
        
        SpriteCreator sc = Game.instance.getSpriteCreator();
        for(SpriteType s : SpriteType.values()) {
            
            BufferedImage img = sc.CreateSprite(s);
            
            if(img != null) {
                this.addSprite(s, img);
            }

        }
    }
    
    // ---- SETTERS & GETTERS ----
    public void addSprite(SpriteType type, BufferedImage img) { this.sprites.put(type, img); }
    public BufferedImage getSprite(SpriteType type) { 
        BufferedImage img = this.sprites.get(type);
        if(img == null) System.out.println("SpriteStorage::getSprite: no sprite found for spritetype: " + type);
        return img;
    }
}
