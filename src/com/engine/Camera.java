package com.engine;

import com.gameobjects.Actor;
import com.utilities.Mathf;

import java.awt.Point;
import java.awt.Rectangle;

public class Camera {
    
    private Rectangle cameraBounds;
    
    public Camera() {
        this.cameraBounds = new Rectangle(0, 0, Game.CAMERA_WIDTH, Game.CAMERA_HEIGHT);
    }

    public void tick() {
        
        Point camCenterPoint = this.getCameraCenterPosition();
        Actor player = Game.instance.getUnitManager().getPlayer();
        
    }
    
    public void nudge(int xmax, int ymax) {
        int x = (int) (this.cameraBounds.x + (Mathf.randomRange(-1, 1) * xmax));
        int y = (int) (this.cameraBounds.y + (Mathf.randomRange(-1, 1) * ymax));
        this.Update(x, y);
    }
   
    public void Update(Point pos, int width, int height) { 
        cameraBounds.setBounds(pos.x, pos.y, width, height); 
    }
    public void Update(int x, int y) {
        cameraBounds.setBounds(x, y, Game.CAMERA_WIDTH, Game.CAMERA_HEIGHT);
    }
    
    // ------ GETTERS & SETTERS -------
    
    public Rectangle getCameraBounds() { return cameraBounds; }
    public Point getCameraCenterPosition() { 
        return new Point(this.cameraBounds.x + this.cameraBounds.width / 2,
            this.cameraBounds.y + this.cameraBounds.height / 2); }
}
