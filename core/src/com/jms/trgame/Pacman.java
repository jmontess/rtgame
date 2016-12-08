package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by jmontes on 27/11/16.
 */
public class Pacman extends ScreenObject {

    // -----------------------------------------------------------------------------------------------------------------

    private TRGame game;

    private TextureRegion manTexture;
    private TextureRegion manTextureUp;
    private TextureRegion manTextureDown;
    private TextureRegion manTextureLeft;
    private TextureRegion manTextureRight;

    private Texture         walkSheet;
    private TextureRegion[] walkFramesUp;
    private TextureRegion[] walkFramesDown;
    private TextureRegion[] walkFramesLeft;
    private TextureRegion[] walkFramesRight;
    private Animation       walkAnimationUp;
    private Animation       walkAnimationDown;
    private Animation       walkAnimationLeft;
    private Animation       walkAnimationRight;
    private Animation       walkAnimation;

    private float stateTime = 0.0f;

    private Boolean moving = false;
    private Boolean alive  = true;

    private TRGame.Direction currentDirection = TRGame.Direction.LEFT;
    private float accDelta = 0;

    //private Rectangle rect;

    // -----------------------------------------------------------------------------------------------------------------

    public Pacman(TRGame game, int startX, int startY) {

        super(startX, startY, TRGame.PACMAN_WIDTH, TRGame.PACMAN_HEIGHT);

        this.game = game;

        walkSheet = new Texture(Gdx.files.internal(TRGame.MAN_SPRITES_PATH));
        TextureRegion[][] tmp = TextureRegion.split(
                walkSheet, walkSheet.getWidth()/TRGame.FRAME_COLS,
                walkSheet.getHeight()/TRGame.FRAME_ROWS);

        walkFramesUp    = new TextureRegion[TRGame.FRAME_COLS];
        walkFramesDown  = new TextureRegion[TRGame.FRAME_COLS];
        walkFramesLeft  = new TextureRegion[TRGame.FRAME_COLS];
        walkFramesRight = new TextureRegion[TRGame.FRAME_COLS];

        for (int i = 0; i < TRGame.FRAME_COLS; i++) {
            walkFramesDown[i]  = tmp[0][i];
            walkFramesLeft[i]  = tmp[1][i];
            walkFramesRight[i] = tmp[2][i];
            walkFramesUp[i]    = tmp[3][i];
        }
        walkAnimationUp    = new Animation(TRGame.ANIMATION_DURATION/TRGame.FRAME_COLS, walkFramesUp);
        walkAnimationDown  = new Animation(TRGame.ANIMATION_DURATION/TRGame.FRAME_COLS, walkFramesDown);
        walkAnimationLeft  = new Animation(TRGame.ANIMATION_DURATION/TRGame.FRAME_COLS, walkFramesLeft);
        walkAnimationRight = new Animation(TRGame.ANIMATION_DURATION/TRGame.FRAME_COLS, walkFramesRight);
        walkAnimation = walkAnimationRight;

        this.manTextureUp    = walkFramesUp[TRGame.FRAME_COLS/2];
        this.manTextureDown  = walkFramesDown[TRGame.FRAME_COLS/2];
        this.manTextureLeft  = walkFramesLeft[TRGame.FRAME_COLS/2];
        this.manTextureRight = walkFramesRight[TRGame.FRAME_COLS/2];
        this.manTexture = manTextureRight;

    }

    // -----------------------------------------------------------------------------------------------------------------

    public void dispose() {
        walkSheet.dispose();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public void draw() {
        TextureRegion currentFrame = manTexture;

        if (alive && moving) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        }
        game.getSpriteBatch().draw(currentFrame, rect.x, rect.y, rect.width, rect.height);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void dontMove() {
        moving = false;
        accDelta = 0;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public TRGame.Direction getDirection() {
        return currentDirection;
    }

    @Override
    public TRGame.Direction move(TRGame.Direction direction) {

        if (allowedDirections.contains(direction)) {

            float step = TRGame.MAN_SPEED * Gdx.graphics.getDeltaTime();

            switch (direction) {
                case UP:
                    rect.y += step;
                    manTexture = manTextureUp;
                    walkAnimation = walkAnimationUp;
                    break;
                case DOWN:
                    rect.y -= step;
                    manTexture = manTextureDown;
                    walkAnimation = walkAnimationDown;
                    break;
                case LEFT:
                    rect.x -= step;
                    manTexture = manTextureLeft;
                    walkAnimation = walkAnimationLeft;
                    break;
                case RIGHT:
                    rect.x += step;
                    manTexture = manTextureRight;
                    walkAnimation = walkAnimationRight;
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
            currentDirection = direction;

        } else {
            moving = false;
        }
        return currentDirection;
    }

    public TRGame.Direction move(float x, float y) {

        double difX = this.getX() - x;
        double difY = this.getY() - y;

        TRGame.Direction newDirection = currentDirection;

        if ((Math.abs(difX) > TRGame.PACMAN_WIDTH || Math.abs(difY) > TRGame.PACMAN_WIDTH) &&
                (Math.max(Math.abs(difX),Math.abs(difY))/Math.min(Math.abs(difX),Math.abs(difY)) > 0)) {

            float delta = Gdx.graphics.getDeltaTime();
            accDelta -= delta;

            if (accDelta <= 0) {
                if (Math.abs(difX) > Math.abs(difY)) {
                    if (difX > 0)
                        newDirection = TRGame.Direction.LEFT;
                    else
                        newDirection = TRGame.Direction.RIGHT;
                } else {
                    if (difY > 0)
                        newDirection = TRGame.Direction.DOWN;
                    else
                        newDirection = TRGame.Direction.UP;

                }
                accDelta = 0.25f;
            }

            currentDirection = move(newDirection);
        } else {
            moving = false;
        }

        return currentDirection;
    }

    public boolean isAlive() {
        return alive;
    }

    public void enemyGotYou() {
        alive = false;
    }
}
