package com.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.engine.Game;
import com.enumerations.AnimationType;
import com.enumerations.SpriteType;
import com.utilities.AnimationFactory;
import com.utilities.SpriteCreator;

public class SpriteStorage {

    private Map<SpriteType, BufferedImage> sprites;
    private Map<AnimationType, Animation> animations;

    public SpriteStorage() {
        this.sprites = new HashMap<>();
        this.animations = new HashMap<>();
    }
 
    public void loadSprites() {
        SpriteCreator sc = Game.instance.getSpriteCreator();
        for(SpriteType s : SpriteType.values()) {
            BufferedImage img = sc.CreateSprite(s);
            if(img != null) { this.addSprite(s, img); }
        }
    }

    public void loadAnimations() {
        ArrayList<Animation> anims = AnimationFactory.createAllAnimations();
        for(Animation a : anims) {
            this.animations.put(a.getAnimationType(), a);
        }
    }

    public void addSprite(SpriteType type, BufferedImage img) {
        if(this.sprites.get(type) == null) {
            this.sprites.put(type, img);
        }
    }

    public BufferedImage getSprite(SpriteType type) { 
        BufferedImage img = this.sprites.get(type);
        if(img == null) {
            System.out.println(
                    "SpriteStorage::getSprite: no sprite found for spritetype: " + type);
        }
        return img;
    }

    public void addAnimation(AnimationType type, Animation animation) {
        if(this.animations.get(type) == null) {
            this.animations.put(type, animation);
        }
    }

    public Animation getAnimation(AnimationType type) {
        Animation animation = this.animations.get(type);
        if(animation == null) {
            System.out.println(
                    "SpriteStorage.getAnimation: no animation found for type: " + type);
        }
        return animation;
    }
}
