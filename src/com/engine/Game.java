package com.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.io.File;

import com.data.Level;
import com.data.SpriteStorage;
import com.enumerations.GameState;
import com.ui.GuiElementManager;
import com.utilities.SpriteCreator;
import com.utilities.UnitManager;
import com.utilities.Util;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 8116425772004374080L;

    public static Game instance;

    public static final String ENGINENAME = "Java Game Engine";
    public static final String ENGINEVERSION = "0.1";
    public static final String ENGINEAUTHOR = "Heikki Heiskanen";

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    public static final String TITLE = "The Gathering Game";
    public static final String VERSION = "v. 0.001";

    public static final int CAMERA_WIDTH = Game.WIDTH;
    public static final int CAMERA_HEIGHT = Game.HEIGHT;

    public static final double FRAME_CAP = 60.0;

    public static final String SPRITESHEETNAME = "/images/spritesheet.png";
    public static final String FRAMICONPATH = "/images/icon.png";

    public static final String CUSTOMFONTNAME = "coders_crux";
    public static final String CUSTOMFONTEXTENSION = ".ttf";
    public static final String CUSTOMFONTFOLDER = "coders_crux";

    public static final int SPRITEGRIDSIZE = 64;
    public static final int SPRITESIZEMULT = 1;

    public static final int TEXT_LINEHEIGHT = 2;

    // ------------------------------
    // DEBUG

    public static boolean drawDebugInfo = false;
    public static final Color debugInfoColor = Color.white;

    public static boolean drawCameraRect = false;
    public static final Color cameraRectColor = Color.red;

    // -----------------------------

    public static int FPS = 0;

    private boolean isRunning = false;
    public static boolean isPaused = false;
    public static boolean isMuted = false;

    private Thread thread;
    private Window window;

    private SpriteStorage spriteStorage;

    private Font customFont;
    private Camera camera;
    private SpriteCreator spriteCreator;
    private Handler handler;
    private GuiRenderer guiRenderer;
    private GuiElementManager guiElementManager;
    private SoundManager soundManager;
    private Renderer renderer;

    private GameState gamestate;
    private GameState lastGameState;

    private Level level;
    private UnitManager unitManager;
    private Point mousePos;

    private KeyInput keyInput;

    public Game() {

        if (instance != null) return;
        Game.instance = this;
        
        this.printEngineStart();

        System.out.println("-------- Loading Engine Resources --------");

        // create object handler
        this.handler = new Handler();

        // create input listeners
        this.keyInput = new KeyInput();
        this.addKeyListener(this.keyInput);
        MouseInput mouseInput = new MouseInput();
        this.addMouseMotionListener(mouseInput);
        this.addMouseListener(mouseInput);

        // load custom font
        Util.loadCustomFont();

        this.window = new Window(Game.WIDTH, Game.HEIGHT, Game.TITLE, this);
        this.spriteCreator = new SpriteCreator(Game.SPRITESHEETNAME);

        this.guiElementManager = new GuiElementManager();
        this.soundManager = new SoundManager();

        this.guiRenderer = new GuiRenderer();
        this.unitManager = new UnitManager();

        this.camera = new Camera();
        this.renderer = new Renderer();

        this.spriteStorage = new SpriteStorage();

        // load all sprites and animations to memory
        this.spriteStorage.loadSprites();

        this.gamestate = GameState.MAINMENU;
        this.lastGameState = this.gamestate;

        // start game thread
        start();

        System.out.println("Engine resources loaded succesfully.");
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        isRunning = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            isRunning = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.gameloop();
    }

    private void gameloop() {

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        int frames = 0;
        long frameCounter = 0;

        final double frameTime = 1 / FRAME_CAP;
        final long SECOND = 1000000000L;

        boolean render = false;
        long now = 0l, passedTime = 0l;

        while (isRunning) {

            render = false;

            now = System.nanoTime();
            passedTime = now - lastTime;
            lastTime = now;

            // calculate tick in seconds
            unprocessedTime += passedTime / (double) SECOND;
            frameCounter += passedTime;

            while (unprocessedTime > frameTime) {

                render = true;
                unprocessedTime -= frameTime;
                this.tick();

                if (frameCounter >= SECOND) {
                    Game.FPS = frames;
                    frames = 0;
                    frameCounter = 0;
                }
            }

            // render the scene
            if (isRunning && render) {
                this.render();
                frames++;
            }
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();

        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // render pipeline starts here.
        this.renderer.preRender(g);

        g.dispose();
        bs.show();
    }

    private void tick() {

        // handle game state change.
        if (this.lastGameState != this.gamestate)
            this.onGameStateChange();

        if (this.gamestate == GameState.INGAME) {
            if (Game.isPaused == false) {
                handler.tickGameObjects();
            }
        }

        // always tick
        this.guiElementManager.tick(this.gamestate);
        this.camera.tick();

        // cache last frame's state
        this.lastGameState = this.gamestate;
    }

    private void onGameStateChange() {
        this.guiElementManager.activateGuiElementsInGameState(this.gamestate);
    }

    public void startNewGame() {

        System.out.println("-------- Load levels --------");

        this.gamestate = GameState.LOADING;

        // create world
        this.level = new Level(10, 10);

        this.unitManager.createPlayerUnit(this.level);

        Game.instance.setGamestate(GameState.INGAME);

        System.out.println("-------- LOGS --------");

    }

    public static void main(String args[]) {
        new Game();
    }

    private void printEngineStart() {
        System.out.println("Running " + Game.ENGINENAME);
        System.out.println("Version " + Game.ENGINEVERSION);
        System.out.println("Author " + Game.ENGINEAUTHOR);
    }

    // ----- GETTERS & SETTERS ------
    public Window getWindow() {
        return this.window;
    }

    public Font getCustomFont() {
        return customFont;
    }

    public void setCustomFont(Font customFont) {
        this.customFont = customFont;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public SpriteCreator getSpriteCreator() {
        return spriteCreator;
    }

    public void setSpriteCreator(SpriteCreator spriteCreator) {
        this.spriteCreator = spriteCreator;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public UnitManager getUnitManager() {
        return this.unitManager;
    }

    public void setUnitManager(UnitManager unitManager) {
        this.unitManager = unitManager;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setWorld(Level level) {
        this.level = level;
    }

    public GameState getGamestate() {
        return this.gamestate;
    }

    public void setGamestate(GameState gamestate) {
        this.gamestate = gamestate;
    }

    public GuiRenderer getGuiRenderer() {
        return this.guiRenderer;
    }

    public void setGuiRenderer(GuiRenderer guiRenderer) {
        this.guiRenderer = guiRenderer;
    }

    public Point getMousePos() { return this.mousePos; }

    public void setMousePos(Point mousePos) {
        this.mousePos = mousePos;
    }

    public GuiElementManager getGuiElementManager() {
        return guiElementManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    public KeyInput getKeyInput() {
        return this.keyInput;
    }

    public SpriteStorage getSpriteStorage() {
        return spriteStorage;
    }

    public void setSpriteStorage(SpriteStorage spriteStorage) {
        this.spriteStorage = spriteStorage;
    }
}
