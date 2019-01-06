package com.interfaces;

import com.sun.javafx.geom.Vec2d;
import com.utilities.Mathf;


public interface IPhysicsObject {

    double getMaxAcceleration();
    double getMaxVelocity();

    double getDeaccelerationValue();

    Vec2d getAcceleration();
    Vec2d getVelocity();

    double getFriction();

    Vec2d getCachedAcceleration();
    Vec2d getCachedVelocity();

    void setCachedAcceleration(Vec2d a);
    void setCachedVelocity(Vec2d a);

    void addAccelerationX(double a);
    void addAccelerationY(double a);

    void addVelocityX(double a);
    void addVelocityY(double a);

    void setVelocityX(double a);
    void setVelocityY(double a);

    void setAccelerationX(double a);
    void setAccelerationY(double a);

    void addWorldPosition(Vec2d a);

    default void move() {

        double deaccelerationValue = getDeaccelerationValue();

        double maxAcceleration = getMaxAcceleration();
        double maxVelocity = getMaxVelocity();

        double acceleration_x = getAcceleration().x;
        double acceleration_y = getAcceleration().y;

        double velocity_x = getVelocity().x;
        double velocity_y = getVelocity().y;

        double lastAccelx = getCachedAcceleration().x;
        double lastAccely = getCachedAcceleration().y;

        double lastVelocityx = getCachedVelocity().x;
        double lastVelocityy = getCachedVelocity().y;

        double friction = getFriction();

        // ----

        // clamp the maximum acceleration to some values
        double axx = Mathf.clamp(-maxAcceleration, maxAcceleration, acceleration_x);
        double ayy = Mathf.clamp(-maxAcceleration, maxAcceleration, acceleration_y);

        // handle velocity in x-axis
        double absVelx = Math.abs(velocity_x);
        if(absVelx < maxVelocity) {
            addVelocityX(axx);
        } else if(absVelx > maxVelocity) {
            if(velocity_x > 0) { setVelocityX(maxVelocity); }
            else if(velocity_x < 0) { setVelocityX(-maxVelocity); }
        }

        // handle velocity in y-axis
        double absVely = Math.abs(velocity_y);
        if(absVely < maxVelocity) {
            addVelocityY(ayy);
        } else if(absVely > maxVelocity) {
            if(velocity_y > 0) { setVelocityY(maxVelocity); }
            else if(velocity_y < 0) { setVelocityY(-maxVelocity); }
        }

        // actually move the character using velocity
        addWorldPosition(new Vec2d(velocity_x, velocity_y));

        // ---- handle acceleration and velocity after applying the forces to world position
        if(acceleration_x > 0) { addAccelerationX(-deaccelerationValue); }
        else if(acceleration_x < 0) { addAccelerationX(deaccelerationValue); }

        if(acceleration_y > 0) { addAccelerationY(-deaccelerationValue); }
        else if(acceleration_y < 0) { addAccelerationY(deaccelerationValue); }

        // when changing polarity -> stop
        if (lastAccelx > 0 && acceleration_x <= 0 || lastAccelx < 0 && acceleration_x >= 0) {
            setAccelerationX(0);
        }

        if (lastAccely > 0 && acceleration_y <= 0 || lastAccely < 0 && acceleration_y >= 0) {
            setAccelerationY(0);
        }

        //  ---- add friction to the velocity, so it decreases over time
        if(velocity_x > 0) { addVelocityX(-friction); }
        else if(velocity_x < 0) { addVelocityX(friction); }

        if(velocity_y > 0) { addVelocityY(-friction); }
        else if(velocity_y < 0) { addVelocityY(friction); }

        // when changing polarity -> stop
        if (lastVelocityx > 0 && velocity_x <= 0 || lastVelocityx < 0 && velocity_x >= 0) {
            setVelocityX(0);
            setAccelerationX(0);
        }

        if (lastVelocityy > 0 && velocity_y <= 0 || lastVelocityy < 0 && velocity_y >= 0) {
            setVelocityY(0);
            setAccelerationY(0);
        }

        // ---- cache
        setCachedVelocity(new Vec2d(velocity_x, velocity_y));
        setCachedAcceleration(new Vec2d(acceleration_x, acceleration_y));
    }

}
