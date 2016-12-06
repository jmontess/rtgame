package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by jmontes on 27/11/16.
 */
public class Pacman {

    // -----------------------------------------------------------------------------------------------------------------

    private TRGame game;

    private Texture pacmanTexture;
    private Texture pacmanTextureUp;
    private Texture pacmanTextureDown;
    private Texture pacmanTextureLeft;
    private Texture pacmanTextureRight;

    private static final int FRAME_COLS = 6;
    private static final int FRAME_ROWS = 5;
    Animation                walkAnimation;
    Texture                  walkSheet;
    TextureRegion[]          walkFrames;
    float stateTime = 0.0f;

    private Boolean moving = false;

    private Rectangle rect;

    // -----------------------------------------------------------------------------------------------------------------

    public Pacman(TRGame game, int startX, int startY) {

        this.game = game;

        this.pacmanTextureUp = new Texture(Gdx.files.internal(TRGame.TEXTURE_PACMAN_UP_PATH));
        this.pacmanTextureDown = new Texture(Gdx.files.internal(TRGame.TEXTURE_PACMAN_DOWN_PATH));
        this.pacmanTextureLeft = new Texture(Gdx.files.internal(TRGame.TEXTURE_PACMAN_LEFT_PATH));
        this.pacmanTextureRight = new Texture(Gdx.files.internal(TRGame.TEXTURE_PACMAN_RIGHT_PATH));
        this.pacmanTexture = pacmanTextureRight;

        walkSheet = new Texture(Gdx.files.internal("sprite-animation.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.025f, walkFrames);

        this.rect = new Rectangle();
        this.rect.height = TRGame.PACMAN_HEIGHT;
        this.rect.width = TRGame.PACMAN_WIDTH;
        this.rect.x = startX - rect.width/2;
        this.rect.y = startY - rect.height/2;

    }

    // -----------------------------------------------------------------------------------------------------------------

    public void dispose() {
        pacmanTextureUp.dispose();
        pacmanTextureDown.dispose();
        pacmanTextureLeft.dispose();
        pacmanTextureRight.dispose();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void draw() {
        TextureRegion currentFrame = null;

        if (moving) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = new TextureRegion(pacmanTexture);
        }
        game.getSpriteBatch().draw(currentFrame, rect.x, rect.y, rect.width, rect.height);
        //game.getSpriteBatch().draw(pacmanTexture, rect.x, rect.y, rect.width, rect.height);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void dontMove() {
        moving = false;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void move(int direction) {

        float step = TRGame.PACMAN_SPEED * Gdx.graphics.getDeltaTime();

        switch (direction) {
            case TRGame.DIRECTION_UP :
                rect.y += step;
                pacmanTexture = pacmanTextureUp;
                break;
            case TRGame.DIRECTION_DOWN :
                rect.y -= step;
                pacmanTexture = pacmanTextureDown;
                break;
            case TRGame.DIRECTION_LEFT :
                rect.x -= step;
                pacmanTexture = pacmanTextureLeft;
                break;
            case TRGame.DIRECTION_RIGHT :
                rect.x += step;
                pacmanTexture = pacmanTextureRight;
                break;
        }

        if (rect.x > TRGame.SCREEN_WIDTH - TRGame.PACMAN_WIDTH)
            rect.x = TRGame.SCREEN_WIDTH - TRGame.PACMAN_WIDTH;
        else if (rect.x < 0)
            rect.x = 0;
        else if (rect.y > TRGame.SCREEN_HEIGHT - TRGame.PACMAN_HEIGHT)
            rect.y = TRGame.SCREEN_HEIGHT - TRGame.PACMAN_HEIGHT;
        else if (rect.y < 0)
        rect.y = 0;

        moving = true;
    }

    public void move(float x, float y) {

        float difX = rect.x - x + TRGame.PACMAN_WIDTH/2;
        float difY = rect.y - y + TRGame.PACMAN_HEIGHT/2;

        int newDirection = 0;

        if (Math.abs(difX) > Math.abs(difY)) {
            if (difX > 0)
                newDirection = TRGame.DIRECTION_LEFT;
            else
                newDirection = TRGame.DIRECTION_RIGHT;
        } else {
            if (difY > 0)
                newDirection = TRGame.DIRECTION_DOWN;
            else
                newDirection = TRGame.DIRECTION_UP;

        }

        move(newDirection);

        /*
        double vectorL = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
        double ratio = TRGame.PACMAN_SPEED/vectorL;

        float delta = Gdx.graphics.getDeltaTime();

        if (Math.abs(difX) >= TRGame.PACMAN_WIDTH)
            rect.x -= difX * ratio * delta;
        if (Math.abs(difY) >= TRGame.PACMAN_HEIGHT)
            rect.y -= difY * ratio * delta;

        if (Math.abs(difX) > Math.abs(difY)) {
            if (difX > 0)
                pacmanTexture = pacmanTextureLeft;
            else
                pacmanTexture = pacmanTextureRight;
        } else {
            if (difY > 0)
                pacmanTexture = pacmanTextureDown;
            else
                pacmanTexture = pacmanTextureUp;
        }
        */
    }
}
