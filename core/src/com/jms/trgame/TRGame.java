package com.jms.trgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TRGame extends Game {

    // -----------------------------------------------------------------------------------------------------------------

    public static final String GAME_TITLE = "Rat Adventures";
    //public static final String GAME_TITLE = "Test";

    public static final int SCREEN_SCALE  = 1;
    public static final int SCREEN_WIDTH  = 640*SCREEN_SCALE;
    public static final int SCREEN_HEIGHT = 384*SCREEN_SCALE;

    public static final int GRID_CELL_SIDE = 64*SCREEN_SCALE;

    public static final String TEXTURE_INTRO_FLOOR_PATH   = "intro_floor.png";
    public static final String TEXTURE_FLOOR_PATH         = "floor.png";
    public static final String TEXTURE_VEIL_PATH          = "veil.png";
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

    public static final String TEXTURE_PLAYER_IDLE_A1_PATH = "player/idle/a1.png";
    public static final String TEXTURE_PLAYER_IDLE_A2_PATH = "player/idle/a2.png";
    public static final String TEXTURE_PLAYER_IDLE_B1_PATH = "player/idle/b1.png";
    public static final String TEXTURE_PLAYER_IDLE_B2_PATH = "player/idle/b2.png";

    public static final String TEXTURE_ENEMY_WALK_UP_1_PATH = "enemy/up/1.png";
    public static final String TEXTURE_ENEMY_WALK_UP_2_PATH = "enemy/up/2.png";
    public static final String TEXTURE_ENEMY_WALK_DOWN_1_PATH = "enemy/down/1.png";
    public static final String TEXTURE_ENEMY_WALK_DOWN_2_PATH = "enemy/down/2.png";
    public static final String TEXTURE_ENEMY_WALK_LEFT_1_PATH = "enemy/left/1.png";
    public static final String TEXTURE_ENEMY_WALK_LEFT_2_PATH = "enemy/left/2.png";
    public static final String TEXTURE_ENEMY_WALK_RIGHT_1_PATH = "enemy/right/1.png";
    public static final String TEXTURE_ENEMY_WALK_RIGHT_2_PATH = "enemy/right/2.png";

    public static final String TEXTURE_ENEMY_ALERT_1_PATH = "enemy/alert/1.png";
    public static final String TEXTURE_ENEMY_ALERT_2_PATH = "enemy/alert/2.png";

    public static final String[] TEXTURE_CHEESE_PATHS = {
            "cheese/1.png",
            "cheese/2.png",
            "cheese/3.png",
            "cheese/4.png",
            "cheese/5.png",
            "cheese/6.png"
    };

    public static final String TEXTURE_ROCK_PATH = "rock.png";

    public static final String SOUND_BITE_PATH = "sounds/bite.mp3";
    public static final String SOUND_OUCH_PATH = "sounds/ouch.mp3";

    public static final int OBJECT_SPEED = 150*SCREEN_SCALE;
    public static final int ENEMY_SPEED = 75*SCREEN_SCALE;

    public static final double ENEMY_ALERT_RANGE = 3.5;

    public static final float ENEMY_ANIMATION_FRAME_DURATION = 0.3f;
    public static final float PLAYER_ANIMATION_FRAME_DURATION = 0.15f;

    public static final int CHEESE_SCORE = 10;

    public static final float BASE_OBSTACLE_PROBABILITY = 1/3f;
    public static final int MAX_LEVEL = 10;

    // -----------------------------------------------------------------------------------------------------------------

    private SpriteBatch spriteBatch;
	private BitmapFont font, largeFont, xlargeFont;
	public GameScreen gameScreen;

	public void create() {

        spriteBatch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/mistervampire.regular.fnt"));
        largeFont = new BitmapFont(Gdx.files.internal("fonts/mistervampire.regular.large.fnt"));
        xlargeFont = new BitmapFont(Gdx.files.internal("fonts/mistervampire.regular.xlarge.fnt"));
        this.setScreen(new MainScreen(this));
        //gameScreen = new GameScreen(this);
        //this.setScreen(gameScreen);
	}

	public void render() {
		super.render(); //important!
	}

	public void dispose() {
        spriteBatch.dispose();
		font.dispose();
		largeFont.dispose();
        xlargeFont.dispose();
		if (gameScreen != null) gameScreen.dispose();
	}

    // -----------------------------------------------------------------------------------------------------------------

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public BitmapFont getFont() {
        return font;
    }

    public BitmapFont getLargeFont() {
        return largeFont;
    }

    public BitmapFont getXLargeFont() {
        return xlargeFont;
    }

    // -----------------------------------------------------------------------------------------------------------------

}