package com.jms.trgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class TRGame extends Game {

    // -----------------------------------------------------------------------------------------------------------------

    //public static final String GAME_TITLE = "Rat Adventures";
    public static final String GAME_TITLE = "Test";

    public static final int SCREEN_WIDTH = 640;
    public static final int SCREEN_HEIGHT = 360;

    public static final int DIRECTION_UP = 1;
    public static final int DIRECTION_DOWN = 2;
    public static final int DIRECTION_LEFT = 3;
    public static final int DIRECTION_RIGHT = 4;

    public static final int PACMAN_WIDTH = 64;
    public static final int PACMAN_HEIGHT = 64;
    public static final int GHOST_WIDTH = 64;
    public static final int GHOST_HEIGHT = 64;

    public static final String TEXTURE_FLOOR_PATH = "floor.jpg";
    public static final String TEXTURE_PACMAN_UP_PATH = "pacman_up.png";
    public static final String TEXTURE_PACMAN_DOWN_PATH = "pacman_down.png";
    public static final String TEXTURE_PACMAN_LEFT_PATH = "pacman_left.png";
    public static final String TEXTURE_PACMAN_RIGHT_PATH = "pacman_right.png";

    public static final String TEXTURE_GHOST_UP_PATH = "red_ghost_left.png";
    public static final String TEXTURE_GHOST_DOWN_PATH = "red_ghost_right.png";
    public static final String TEXTURE_GHOST_LEFT_PATH = "red_ghost_left.png";
    public static final String TEXTURE_GHOST_RIGHT_PATH = "red_ghost_right.png";

    public static final int PACMAN_SPEED = 300;
    public static final int GHOST_SPEED = 100;

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