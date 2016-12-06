package com.jms.trgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class TRGame extends Game {

    // -----------------------------------------------------------------------------------------------------------------

    //public static final String GAME_TITLE = "Rat Adventures";
    public static final String GAME_TITLE = "Test";

    public static final int SCREEN_SCALE  = 4;
    public static final int SCREEN_WIDTH  = 640*SCREEN_SCALE;
    public static final int SCREEN_HEIGHT = 360*SCREEN_SCALE;

    public static final int PACMAN_WIDTH  = 64*SCREEN_SCALE;
    public static final int PACMAN_HEIGHT = 64*SCREEN_SCALE;

    public static final int GHOST_WIDTH   = 64*SCREEN_SCALE;
    public static final int GHOST_HEIGHT  = 64*SCREEN_SCALE;

    public static final int CHEESE_WIDTH   = 48*SCREEN_SCALE;
    public static final int CHEESE_HEIGHT  = 48*SCREEN_SCALE;

    public static final int MAN_SPEED = 300*SCREEN_SCALE;
    public static final int ENEMY_WALK_SPEED = 100*SCREEN_SCALE;
    public static final int ENEMY_RUN_SPEED  = 150*SCREEN_SCALE;

    public static final int ENEMY_RANGE = 200*SCREEN_SCALE;

    public static final String TEXTURE_FLOOR_PATH = "floor.png";

    public static final int FRAME_COLS = 3;
    public static final int FRAME_ROWS = 4;
    public static final float ANIMATION_DURATION = 0.25f;

    public static final String MAN_SPRITES_PATH = "man.png";
    public static final String ENEMY_SPRITES_PATH = "dragon.png";
    public static final String CHEESE_TEXTURE_PATH = "cheese.png";

    public static final int DIRECTION_UP    = 1;
    public static final int DIRECTION_DOWN  = 2;
    public static final int DIRECTION_LEFT  = 3;
    public static final int DIRECTION_RIGHT = 4;

    public static final float ENEMY_WALK_DIR_CHANGE_TIME = 1.5f;
    public static final float ENEMY_RUN_DIR_CHANGE_TIME = 0.5f;

    // -----------------------------------------------------------------------------------------------------------------

    private SpriteBatch spriteBatch;
	private BitmapFont font;
	private GameScreen gameScreen;

	public void create() {

        spriteBatch = new SpriteBatch();
		//Use LibGDX's default Arial font.
		font = new BitmapFont();
		//this.setScreen(new MainMenuScreen(this));
        gameScreen = new GameScreen(this);
        this.setScreen(gameScreen);
	}

	public void render() {
		super.render(); //important!
	}

	public void dispose() {
        spriteBatch.dispose();
		//font.dispose();
		gameScreen.dispose();
	}

    // -----------------------------------------------------------------------------------------------------------------

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public int getRandomDirection() {
        return MathUtils.random(1,4);
    }
}