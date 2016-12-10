package com.jms.trgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TRGame extends Game {

    // -----------------------------------------------------------------------------------------------------------------

    //public static final String GAME_TITLE = "Rat Adventures";
    public static final String GAME_TITLE = "Test";

    public static final int SCREEN_SCALE  = 4;
    public static final int SCREEN_WIDTH  = 640*SCREEN_SCALE;
    public static final int SCREEN_HEIGHT = 384*SCREEN_SCALE;

    public static final int GRID_CELL_SIDE = 64*SCREEN_SCALE;

    public static final String TEXTURE_FLOOR_PATH         = "floor_plain.png";
    public static final String TEXTURE_GREEN_BOX_PATH     = "green_box.png";
    public static final String TEXTURE_RED_BOX_PATH       = "red_box.png";
    public static final String TEXTURE_RED_BOX_ALERT_PATH = "red_box_alert.png";
    public static final String TEXTURE_YELLOW_BOX_PATH    = "yellow_box.png";
    public static final String TEXTURE_GREY_BOX_PATH      = "grey_box.png";

    public static final String TEXTURE_PLAYER_WALK_UP_1_PATH = "player/up/1.png";
    public static final String TEXTURE_PLAYER_WALK_UP_2_PATH = "player/up/2.png";
    public static final String TEXTURE_PLAYER_WALK_DOWN_1_PATH = "player/down/1.png";
    public static final String TEXTURE_PLAYER_WALK_DOWN_2_PATH = "player/down/2.png";
    public static final String TEXTURE_PLAYER_WALK_LEFT_1_PATH = "player/left/1.png";
    public static final String TEXTURE_PLAYER_WALK_LEFT_2_PATH = "player/left/2.png";
    public static final String TEXTURE_PLAYER_WALK_RIGHT_1_PATH = "player/right/1.png";
    public static final String TEXTURE_PLAYER_WALK_RIGHT_2_PATH = "player/right/2.png";

    public static final String TEXTURE_PLAYER_TAIL_WALK_UP_1_PATH = "player/up/1t.png";
    public static final String TEXTURE_PLAYER_TAIL_WALK_UP_2_PATH = "player/up/2t.png";
    public static final String TEXTURE_PLAYER_TAIL_WALK_DOWN_1_PATH = "player/down/1t.png";
    public static final String TEXTURE_PLAYER_TAIL_WALK_DOWN_2_PATH = "player/down/2t.png";
    public static final String TEXTURE_PLAYER_TAIL_WALK_LEFT_1_PATH = "player/left/1t.png";
    public static final String TEXTURE_PLAYER_TAIL_WALK_LEFT_2_PATH = "player/left/2t.png";
    public static final String TEXTURE_PLAYER_TAIL_WALK_RIGHT_1_PATH = "player/right/1t.png";
    public static final String TEXTURE_PLAYER_TAIL_WALK_RIGHT_2_PATH = "player/right/2t.png";

    public static final String TEXTURE_ENEMY_WALK_UP_1_PATH = "enemy/up/1.png";
    public static final String TEXTURE_ENEMY_WALK_UP_2_PATH = "enemy/up/2.png";
    public static final String TEXTURE_ENEMY_WALK_DOWN_1_PATH = "enemy/down/1.png";
    public static final String TEXTURE_ENEMY_WALK_DOWN_2_PATH = "enemy/down/2.png";
    public static final String TEXTURE_ENEMY_WALK_LEFT_1_PATH = "enemy/left/1.png";
    public static final String TEXTURE_ENEMY_WALK_LEFT_2_PATH = "enemy/left/2.png";
    public static final String TEXTURE_ENEMY_WALK_RIGHT_1_PATH = "enemy/right/1.png";
    public static final String TEXTURE_ENEMY_WALK_RIGHT_2_PATH = "enemy/right/2.png";

    public static final String TEXTURE_CHEESE_PATH = "cheese.png";


    public static final int OBJECT_SPEED = 150*SCREEN_SCALE;
    public static final int ENEMY_SPEED = 75*SCREEN_SCALE;

    public static final double ENEMY_ALERT_RANGE = 3.5;

    public static final float ENEMY_ANIMATION_FRAME_DURATION = 0.3f;
    public static final float PLAYER_ANIMATION_FRAME_DURATION = 0.15f;

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

    // -----------------------------------------------------------------------------------------------------------------

}