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

    private static final int DEFAULT_TEXTFIELD_WIDTH = 350;
    private static final int DEFAULT_TEXTFIELD_HEIGHT = 35;

    private static final int DEFAULT_TEXTFIELD_MAXLEN = 15;

    public static PlainText createDefaultPlainText(Panel panel, String text) {
        return new PlainText(panel,text, 40, new Color(0, 0, 0,255));
    }

    public static TextField createDefaultTextField(Panel panel) {
        return new TextField(panel,
                DEFAULT_TEXTFIELD_WIDTH, DEFAULT_TEXTFIELD_HEIGHT,
                15, DEFAULT_FONTSIZE, DEFAULT_TEXTFIELD_MAXLEN,
                true, "Name");
    }

    public static VPanel createDefaultCenteredPanel(boolean isTransparent) {
        return new VPanel((Game.WIDTH / 2) - (DEFAULT_PANEL_WIDTH / 2),
                Game.HEIGHT / 2, DEFAULT_PANEL_WIDTH, DEFAULT_PANEL_HEIGHT,
                Colors.BLUE, isTransparent, false, HorizontalAlign.CENTER);
    }

    public static Button createDefaultPlayButton(Panel panel) {
        return new Button(panel, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT,
                "Play", Color.black, Color.white, DEFAULT_FONTSIZE,
                InteractAction.PLAY, InteractAction.NONE);
    }

    public static Button createDefaultExitButton(Panel panel) {
        return new Button(panel, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT - 15,
                "Exit", Color.black, Color.white, DEFAULT_FONTSIZE,
                InteractAction.EXIT_TO_OS, InteractAction.NONE);
    }

    public static Button createDefaultResumeButton(Panel panel) {
        return new Button(panel, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT,
                "Resume", Color.black, Color.white, DEFAULT_FONTSIZE,
                InteractAction.RESUME, InteractAction.NONE);
    }

}
