package com.interfaces;

import java.awt.*;

public interface ICollidable {

    default boolean isColliding(ICollidable a, ICollidable b) {
        return a.getHitbox().intersects(b.getHitbox());
    }

    boolean isActive();
    void disableCollisions();
    void enableCollisions();
    Rectangle getHitbox();
}
