package com.interfaces;

import java.awt.*;

public interface ICollidable {
    default boolean isColliding(ICollidable a, ICollidable b) {
        return a.getHitbox().intersects(b.getHitbox());
    }
    default double getDistanceFrom(Rectangle hitbox) {

        double xx = (hitbox.x - this.getHitbox().x);
        xx *= xx;

        double yy = (hitbox.y - this.getHitbox().y);
        yy *= yy;

        double result = Math.sqrt(xx + yy);
        return result;
    }

    boolean isActive();
    void disableCollisions();
    void enableCollisions();
    Rectangle getHitbox();
}
