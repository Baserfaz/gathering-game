package com.ui;

import com.engine.Game;
import com.enumerations.HorizontalAlign;
import com.enumerations.InteractAction;

import java.awt.*;

public class GuiFactory {

    private static final int DEFAULT_PANEL_WIDTH = 500;
    private static final int DEFAULT_PANEL_HEIGHT = 300;

    private static final int DEFAULT_BUTTON_WIDTH = 350;
    private static final int DEFAULT_BUTTON_HEIGHT = 75;
    private static final int DEFAULT_FONTSIZE = 40;

    public static VPanel createDefaultCenteredPanel() {
        return new VPanel((Game.WIDTH / 2) - (DEFAULT_PANEL_WIDTH / 2),
                Game.HEIGHT / 2, DEFAULT_PANEL_WIDTH, DEFAULT_PANEL_HEIGHT,
                Colors.BLUE, false, false, HorizontalAlign.CENTER);
    }

    public static Button createDefaultPlayButton(Panel panel) {
        return new Button(panel, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT,
                "Play", Color.black, Color.white, DEFAULT_FONTSIZE,
                InteractAction.PLAY, InteractAction.NONE);
    }

    public static Button createDefaultExitButton(Panel panel) {
        return new Button(panel, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT,
                "Exit", Color.black, Color.white, DEFAULT_FONTSIZE,
                InteractAction.EXIT_TO_OS, InteractAction.NONE);
    }

    public static Button createDefaultResumeButton(Panel panel) {
        return new Button(panel, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT,
                "Resume", Color.black, Color.white, DEFAULT_FONTSIZE,
                InteractAction.RESUME, InteractAction.NONE);
    }

}