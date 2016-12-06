package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by jmontes on 27/11/16.
 */
public class Ghost {

    // -----------------------------------------------------------------------------------------------------------------

    private TRGame game;

    private Texture ghostTexture;
    private Texture ghostTextureUp;
    private Texture ghostTextureDown;
    private Texture ghostTextureLeft;
    private Texture ghostTextureRight;

    private Rectangle rect;

    private int direction;
    private float accDelta;

    // -----------------------------------------------------------------------------------------------------------------

    public Ghost(TRGame game, int startX, int startY) {

        this.game = game;

        this.ghostTextureUp = new Texture(Gdx.files.internal(TRGame.TEXTURE_GHOST_UP_PATH));
        this.ghostTextureDown = new Texture(Gdx.files.internal(TRGame.TEXTURE_GHOST_DOWN_PATH));
        this.ghostTextureLeft = new Texture(Gdx.files.internal(TRGame.TEXTURE_GHOST_LEFT_PATH));
        this.ghostTextureRight = new Texture(Gdx.files.internal(TRGame.TEXTURE_GHOST_RIGHT_PATH));
        this.ghostTexture = ghostTextureRight;

        this.rect = new Rectangle();
        this.rect.height = TRGame.GHOST_HEIGHT;
        this.rect.width = TRGame.GHOST_WIDTH;
        this.rect.x = startX - rect.width/2;
        this.rect.y = startY - rect.height/2;

        changeDirection();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void dispose() {
        ghostTextureUp.dispose();
        ghostTextureDown.dispose();
        ghostTextureLeft.dispose();
        ghostTextureRight.dispose();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void draw() {
        game.getSpriteBatch().draw(ghostTexture, rect.x, rect.y, rect.width, rect.height);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void changeDirection() {
        direction = game.getRandomDirection();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void move() {

        float delta = Gdx.graphics.getDeltaTime();

        float step = TRGame.GHOST_SPEED * delta;

        accDelta += delta;

        if (accDelta > 1) {
            changeDirection();
            accDelta = 0;
        }

        switch (direction) {
            case TRGame.DIRECTION_UP :
                rect.y += step;
                ghostTexture = ghostTextureUp;
                break;
            case TRGame.DIRECTION_DOWN :
                rect.y -= step;
                ghostTexture = ghostTextureDown;
                break;
            case TRGame.DIRECTION_LEFT :
                rect.x -= step;
                ghostTexture = ghostTextureLeft;
                break;
            case TRGame.DIRECTION_RIGHT :
                rect.x += step;
                ghostTexture = ghostTextureRight;
                break;
        }

        if (rect.x > TRGame.SCREEN_WIDTH - TRGame.GHOST_WIDTH) {
            rect.x = TRGame.SCREEN_WIDTH - TRGame.GHOST_WIDTH;
            changeDirection();
        } else if (rect.x < 0) {
            rect.x = 0;
            changeDirection();
        } else if (rect.y > TRGame.SCREEN_HEIGHT - TRGame.GHOST_HEIGHT) {
            rect.y = TRGame.SCREEN_HEIGHT - TRGame.GHOST_HEIGHT;
            changeDirection();
        } else if (rect.y < 0) {
            rect.y = 0;
            changeDirection();
        }
    }
}
