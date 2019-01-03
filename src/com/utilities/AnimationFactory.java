package com.utilities;

import com.data.Animation;
import com.engine.Game;
import com.enumerations.AnimationType;

import java.util.ArrayList;
import java.util.Arrays;

public class AnimationFactory {

    public static Animation createAnimation(AnimationType type) {
        Animation animation = new Animation(type);

        switch (type) {
            case PLAYER_PROJECTILE_DESTROY:
                    animation.addFrames(
                            new ArrayList<>(Arrays.asList(
                                    Game.instance
                                    .getSpriteCreator()
                                    .createMultipleSprites(4, 1, 3)
                            ))
                    );
                break;
        }

        return animation;
    }

    public static ArrayList<Animation> createAllAnimations() {
        ArrayList<Animation> animations = new ArrayList<>();
        for(AnimationType type : AnimationType.values()) {
            animations.add(createAnimation(type));
        }
        return animations;
    }

}
