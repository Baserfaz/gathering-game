package com.interfaces;

import java.awt.*;

public interface ICollidable {

    default boolean isColliding(ICollidable other) {
        return getHitbox().intersects(other.getHitbox());
    }

    /**
     * Get distance from center of this hitbox to center of another game object's hitbox.
     * @param hitbox
     * @return
     */
    default double getDistanceFrom(Rectangle hitbox) {

        Rectangle myHitbox = this.getHitbox();

        int center_x1 = hitbox.x + hitbox.width / 2;
        int center_y1 = hitbox.y + hitbox.height / 2;

        int center_x2 = myHitbox.x + myHitbox.width / 2;
        int center_y2 = myHitbox.y + myHitbox.height / 2;

        double xx = (center_x1 - center_x2);
        xx *= xx;

        double yy = (center_y1 - center_y2);
        yy *= yy;

        double result = Math.sqrt(xx + yy);
        return result;
    }

    default void updateHitbox(Point center) {
        Rectangle hitbox = this.getHitbox();
        if(hitbox == null || center == null) return;

        hitbox.x = center.x - hitbox.width / 2;
        hitbox.y = center.y - hitbox.height / 2;

        this.setHitbox(hitbox);
    }

    void onCollision(ICollidable other);
    boolean isActive();
    void disableCollisions();
    void enableCollisions();
    Rectangle getHitbox();
    void setHitbox(Rectangle rectangle);
}
