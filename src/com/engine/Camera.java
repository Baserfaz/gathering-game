package com.engine;

import com.enumerations.GameState;
import com.gameobjects.Actor;
import com.utilities.Mathf;

import java.awt.Point;
import java.awt.Rectangle;

public class Camera {
    
    private Rectangle cameraBounds;
    private Actor player = null;

    public Camera() {
        this.cameraBounds = new Rectangle(0, 0, Game.CAMERA_WIDTH, Game.CAMERA_HEIGHT);
    }

    public void tick() {
        if(Game.instance.getGamestate().equals(GameState.INGAME)) {

            // get player and cache it
            if(player == null) {
                if(Game.instance.getUnitManager().getPlayer() != null) {
                    this.player = Game.instance.getUnitManager().getPlayer();
                } else {
                    return;
                }
            }

            // set the player to be the center object of the camera
            Point pos = new Point(player.getWorldPosition().x - Game.CAMERA_WIDTH / 2,
                    player.getWorldPosition().y - Game.CAMERA_HEIGHT / 2);

            this.update(pos);
        }
    }

    public void update(Point pos) {
        cameraBounds.setBounds(pos.x, pos.y, (int) cameraBounds.getWidth(), (int) cameraBounds.getHeight());
    }
    
    public Rectangle getCameraBounds() { return cameraBounds; }
    public Point getCameraCenterPosition() { 
        return new Point(this.cameraBounds.x + this.cameraBounds.width / 2,
            this.cameraBounds.y + this.cameraBounds.height / 2); }
}
