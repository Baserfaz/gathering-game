package com.ui;

import java.awt.*;

import com.engine.Game;
import com.enumerations.GameState;
import com.enumerations.InteractAction;

public class Button extends InteractableGuiElement {

    private Color fontColor;
    private Color bgColor;
    private int fontSize;
    private String txt;
    private Panel parent;

    public Button(Panel parent, int width, int height,
            String txt, Color fontColor, Color bgColor, int fontSize,
            InteractAction onClickAction, InteractAction onHoverAction) {
        super(parent.x, parent.y, width, height, onClickAction, onHoverAction);

        this.parent = parent;
        this.fontColor = fontColor;
        this.bgColor = bgColor;
        this.fontSize = fontSize;
        this.txt = txt;
    }

    public void render(Graphics g) {
        if(this.isVisible()) {

            Rectangle r = new Rectangle(this.x + this.parent.xrelcam, this.y + this.parent.yrelcam, this.w, this.h);

            if (this.isHovering) g.setColor(this.bgColor.darker());
            else g.setColor(this.bgColor);

            // render box
            g.fillRect(r.x, r.y, r.width, r.height);

            int txtWidth = g.getFontMetrics().stringWidth(this.txt);
            int txtHeight = g.getFontMetrics().getHeight();

            int centerX = (r.x + r.width / 2) - txtWidth / 2;
            int centerY = (r.y + txtHeight + r.height / 2) - txtHeight / 2;

            // render text inside the button rectangle
            Font font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, this.fontSize);
            g.setFont(font);
            g.setColor(this.fontColor);
            g.drawString(this.txt, centerX, centerY);
        }
    }

    public void tick() { if(this.isEnabled()) {} }

    @Override
    public void onHover() {
        if(this.onHoverAction != null) {
            switch (this.onHoverAction) {
                case EXIT_TO_OS:
                    System.exit(0);
                    break;
                case PLAY:
                    Game.instance.startNewGame();
                    break;
                case RESUME:
                    Game.instance.setGamestate(GameState.INGAME);
                    Game.isPaused = false;
                    break;
                default:
                    break;
            }
        } else {
            if(this.getOnHoverRunnable() != null) {
                this.getOnHoverRunnable().run();
            }
        }
    }

    @Override
    public void onClick() {
        if(this.isEnabled) {
            if(this.onClickAction != null) {

                System.out.println("action: " + this.onClickAction.toString());

                switch (this.onClickAction) {
                    case EXIT_TO_OS:
                        System.exit(0);
                        break;
                    case PLAY:
                        Game.instance.startNewGame();
                        break;
                    case RESUME:
                        Game.instance.setGamestate(GameState.INGAME);
                        Game.isPaused = false;
                        break;
                    default:
                        break;
                }
            } else {
                if(this.getOnClickRunnable() != null) {
                    this.getOnClickRunnable().run();
                }
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        Rectangle r = new Rectangle(x + parent.xrelcam, y + parent.yrelcam, w, h);
        return r;
    }

    @Override
    public void onUnfocus() { }
}
