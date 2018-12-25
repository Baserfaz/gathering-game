package com.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;

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

    public static final String ENGINE_NAME = "Java Game Engine";
    public static final String ENGINE_VERSION = "ALPHA AF";
    public static final String ENGINE_AUTHOR = "Heikki Heiskanen";

    public static final int WIDTH = 1280; //1920;
    public static final int HEIGHT = 720; //1080;

    public static final String TITLE = "The Gathering Game";
    public static final String VERSION = "v. 0.001";

    // camera width & height should be the same as game window.
    public static final int CAMERA_WIDTH = Game.WIDTH;
    public static final int CAMERA_HEIGHT = Game.HEIGHT;

    // this is basically how fast the game is running and not a frame cap.
    public static final double FRAME_CAP = 60.0;

    public static final String SPRITESHEET_NAME = "/images/spritesheet.png";
    public static final String FRAME_ICON_PATH = "/images/icon.png";

    public static final String CUSTOM_FONT_NAME = "coders_crux";
    public static final String CUSTOM_FONT_EXTENSION = ".ttf";
    public static final String CUSTOM_FONT_FOLDER = "coders_crux";

    public static final int MAX_COLLISION_DISTANCE_MULT = 3;

    public static final int SPRITE_GRID_SIZE = 16;
    public static final int SPRITE_SIZE_MULT = 4;
    public static final int SPRITE_UI_SIZE_MULT = 2;

    public static final int TEXT_LINEHEIGHT = 2;

    // ------------------------------
    // DEBUG

    public static boolean drawDebugInfo = false;
    public static final Color debugInfoColor = Color.white;

    public static boolean drawCameraRect = false;
    public static final Color cameraRectColor = Color.red;

    public static boolean drawHitboxes = false;
    public static final Color hitboxColor = Color.red;

    public static boolean drawCollisionDistance = false;
    public static final Color collisionCircleColor = Color.green;

    // -----------------------------

    public static int FPS = 0;

    private boolean isRunning = false;
    public static boolean isPaused = false;
    public static boolean isMuted = false;

    private Thread mainGameThread;
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

    public static int CALCULATED_SPRITE_SIZE;
    public static int CALCULATED_SPRITE_UI_SIZE;
    public static int CALCULATED_MAX_COLLISION_DISTANCE;

    public static void main(String args[]) {
        new Game();
    }

    public Game() {

        if (instance != null) return;
        Game.instance = this;

        // enable hardware acceleration -> use embedded gpu
        System.setProperty("sun.java2d.opengl", "true");

        this.printEngineStart();

        System.out.println("-------- Loading Engine Resources --------");

        // pre-calculate stuff that is otherwise calculated everywhere
        CALCULATED_SPRITE_SIZE = SPRITE_GRID_SIZE * SPRITE_SIZE_MULT;
        CALCULATED_SPRITE_UI_SIZE = SPRITE_GRID_SIZE * SPRITE_UI_SIZE_MULT;
        CALCULATED_MAX_COLLISION_DISTANCE = CALCULATED_SPRITE_SIZE * MAX_COLLISION_DISTANCE_MULT;

        // create object handler
        this.handler = new Handler();

        // create input listeners
        this.keyInput = new KeyInput();
        MouseInput mouseInput = new MouseInput();

        this.addKeyListener(this.keyInput);
        this.addMouseMotionListener(mouseInput);
        this.addMouseListener(mouseInput);

        // load custom font
        Util.loadCustomFont();

        this.window = new Window(Game.WIDTH, Game.HEIGHT, Game.TITLE, this);
        this.spriteCreator = new SpriteCreator(Game.SPRITESHEET_NAME);

        this.guiElementManager = new GuiElementManager();
        this.soundManager = new SoundManager();

        // load all sprites and animations to memory
        this.spriteStorage = new SpriteStorage();
        this.spriteStorage.loadSprites();

        this.guiRenderer = new GuiRenderer();
        this.unitManager = new UnitManager();

        this.camera = new Camera();
        this.renderer = new Renderer();

        this.gamestate = GameState.MAINMENU;
        this.lastGameState = this.gamestate;

        // start game mainGameThread
        start();

        System.out.println("Engine resources loaded succesfully.");
    }

    public synchronized void start() {
        mainGameThread = new Thread(this);
        mainGameThread.start();
        isRunning = true;
    }

    public synchronized void stop() {
        try {
            mainGameThread.join();
            isRunning = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.gameloop();
        this.stop();
    }

    private void gameloop() {

        long lastTime = System.nanoTime();
        double unprocessedTime = 0.0;

        int frames = 0;
        long frameCounter = 0;

        final double frameTime = 1 / FRAME_CAP; // time for each frame
        final long SECOND = 1000000000L;        // second in nano seconds 10^9

        long now, passedTime;

        // this is the heart of the engine,
        // ticks everything and renders the scene
        while (isRunning) {

            // calculate passed time between frames
            now = System.nanoTime();
            passedTime = now - lastTime;
            lastTime = now;

            // calculate tick in seconds, its important to cast second to double here.
            unprocessedTime += passedTime / (double) SECOND;

            // this trips the frame counter
            // how many frames in one second
            frameCounter += passedTime;

            // Consume the left over time of handling the frame.
            // When fps cap is set to 60, one frame has 1/60 seconds (0.016s = 16ms) to draw.
            // Can tick the game multiple times if there is time to do that.
            while (unprocessedTime > frameTime) {
                unprocessedTime -= frameTime;
                this.tick();
            }

            // render the scene
            this.render();
            frames++;

            // this is only for counting the frame rate
            if (frameCounter >= SECOND) {
                Game.FPS = frames;
                frames = 0;
                frameCounter = 0;
            }
        }
    }

    private void render() {

        // "First" buffer has the current frame drawn and
        // the latter buffers the other (second and third and etc.) frames.
        // When BufferStrategy.show() is called, the buffers are changed
        // to show the next frame.

        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(2);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // render pipeline starts here.
        this.renderer.preRender(g);

        g.dispose();
        bs.show();
    }

    /**
     * master tick
     */
    private void tick() {

        // handle game state change.
        if (this.lastGameState != this.gamestate) {
            this.onGameStateChange();
        }

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

    private void printEngineStart() {
        System.out.println("Running " + Game.ENGINE_NAME);
        System.out.println("Version " + Game.ENGINE_VERSION);
        System.out.println("Author " + Game.ENGINE_AUTHOR);
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
