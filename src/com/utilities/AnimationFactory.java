package com.utilities;

import com.data.Animation;
import com.engine.Game;
import com.enumerations.AnimationType;
import com.enumerations.SpriteType;

import java.util.ArrayList;

public class AnimationFactory {

    public static Animation createAnimation(String name, AnimationType type) {
        Animation animation = new Animation(name, type);

        switch (type) {
            case PROJECTILE_DESTROY:
                    animation.addFrame(
                            Game.instance.getSpriteStorage().getSprite(
                                    SpriteType.PROJECTILE_PLAYER_2));
                break;
        }

        return animation;
    }

    public static ArrayList<Animation> createAllAnimations() {
        ArrayList<Animation> animations = new ArrayList<>();
        for(AnimationType type : AnimationType.values()) {
            animations.add(createAnimation("", type));
        }
        return animations;
    }

}
