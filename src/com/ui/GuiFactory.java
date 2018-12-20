package com.ui;

import com.engine.Game;
import com.enumerations.GameState;
import com.enumerations.HorizontalAlign;

import java.awt.*;

public class GuiFactory {

    private static final int DEFAULT_PANEL_WIDTH = 500;
    private static final int DEFAULT_PANEL_HEIGHT = 300;

    private static final int DEFAULT_BUTTON_WIDTH = 150;
    private static final int DEFAULT_BUTTON_HEIGHT = 35;
    private static final int DEFAULT_FONTSIZE = 40;

    private static final int DEFAULT_TEXTFIELD_WIDTH = 350;
    private static final int DEFAULT_TEXTFIELD_HEIGHT = 35;

    private static final int DEFAULT_TEXTFIELD_MAXLEN = 15;

    public static PlainText createDefaultPlainText(Panel panel, HorizontalAlign align, String text, Color color) {
        return new PlainText(panel, align, text, 40, color);
    }

    public static TextField createDefaultTextField(Panel panel) {
        return new TextField(panel,
                DEFAULT_TEXTFIELD_WIDTH, DEFAULT_TEXTFIELD_HEIGHT,
                15, DEFAULT_FONTSIZE, DEFAULT_TEXTFIELD_MAXLEN,
                true);
    }

    public static HPanel createDefaultHorizontalPanel(Panel parent, Panel.PanelAlign panelAlign,
                                                      boolean isTransparent, Color bgcolor) {

        return new HPanel(panelAlign, Game.WIDTH + 10, 50, parent, bgcolor,
                isTransparent, false, 15, true);
    }

    public static VPanel createDefaultCenteredPanel(Panel parent, boolean isTransparent, Color bgcolor) {
        return new VPanel(Panel.PanelAlign.MIDDLE, DEFAULT_PANEL_WIDTH, DEFAULT_PANEL_HEIGHT, parent,
                bgcolor, isTransparent, false, 15, HorizontalAlign.CENTER);
    }

    public static Button createDefaultPlayButton(Panel panel) {
        return new Button(panel, 600, 50,
                "Play", Color.black, Color.white, DEFAULT_FONTSIZE,
                () -> Game.instance.startNewGame(), null);
    }

    public static Button createDefaultExitToMainMenuButton(Panel panel) {
        return new Button(panel, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT,
                "Exit to mainmenu", Color.black, Color.white, DEFAULT_FONTSIZE,
                null, null);
    }

    public static Button createDefaultExitButton(Panel panel) {
        return new Button(panel, 600, 50,
                "Exit", Color.black, Color.white, DEFAULT_FONTSIZE,
                () -> System.exit(0), null);
    }

    public static Button createDefaultResumeButton(Panel panel) {
        return new Button(panel, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT,
                "Resume", Color.black, Color.white, DEFAULT_FONTSIZE,
                () -> {
                    Game.instance.setGamestate(GameState.INGAME);
                    Game.isPaused = false;
                }, null);
    }

}
