package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by jmontes on 27/11/16.
 */
public class Enemy extends ScreenObject {

    // -----------------------------------------------------------------------------------------------------------------

    private TRGame game;

    private TextureRegion enemyTexture;
    private TextureRegion enemyTextureUp;
    private TextureRegion enemyTextureDown;
    private TextureRegion enemyTextureLeft;
    private TextureRegion enemyTextureRight;

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

    private Boolean moving = true;
    private Boolean pursuing = false;

    //private Rectangle rect;

    private TRGame.Direction direction;
    private float accDelta;

    private int speed = TRGame.ENEMY_WALK_SPEED;
    private TRGame.Direction currentDirection = TRGame.Direction.LEFT;

    // -----------------------------------------------------------------------------------------------------------------

    public Enemy(TRGame game, int startX, int startY) {

        super(startX, startY, TRGame.GHOST_WIDTH, TRGame.GHOST_HEIGHT);

        this.game = game;

        walkSheet = new Texture(Gdx.files.internal(TRGame.ENEMY_SPRITES_PATH));
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

        this.enemyTextureUp    = walkFramesUp[TRGame.FRAME_COLS/2];
        this.enemyTextureDown  = walkFramesDown[TRGame.FRAME_COLS/2];
        this.enemyTextureLeft  = walkFramesLeft[TRGame.FRAME_COLS/2];
        this.enemyTextureRight = walkFramesRight[TRGame.FRAME_COLS/2];
        this.enemyTexture = enemyTextureRight;

        changeDirection();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void dispose() {
        walkSheet.dispose();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public void draw() {
        TextureRegion currentFrame = null;

        if (moving) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = new TextureRegion(enemyTexture);
        }
        game.getSpriteBatch().draw(currentFrame, rect.x, rect.y, rect.width, rect.height);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void changeDirection() {
        direction = game.getRandomDirection();
    }

    // -----------------------------------------------------------------------------------------------------------------


    @Override
    public TRGame.Direction getDirection() {
        return currentDirection;
    }

    @Override
    public TRGame.Direction move(TRGame.Direction direction) {

        if (allowedDirections.contains(direction)) {

            float delta = Gdx.graphics.getDeltaTime();
            float step = speed * delta;

            switch (direction) {
                case UP:
                    rect.y += step;
                    enemyTexture = enemyTextureUp;
                    walkAnimation = walkAnimationUp;
                    break;
                case DOWN:
                    rect.y -= step;
                    enemyTexture = enemyTextureDown;
                    walkAnimation = walkAnimationDown;
                    break;
                case LEFT:
                    rect.x -= step;
                    enemyTexture = enemyTextureLeft;
                    walkAnimation = walkAnimationLeft;
                    break;
                case RIGHT:
                    rect.x += step;
                    enemyTexture = enemyTextureRight;
                    walkAnimation = walkAnimationRight;
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

            moving = true;
            currentDirection = direction;

        } else {
            moving = false;
        }

        return currentDirection;
    }

    public TRGame.Direction move(Pacman player) {

        float delta = Gdx.graphics.getDeltaTime();
        //float step = speed * delta;
        accDelta += delta;

        if (player.isAlive() && (distanceTo(player) <= TRGame.ENEMY_RANGE)) {

            pursuing = true;
            speed = TRGame.ENEMY_RUN_SPEED;

            double difX = this.getX() - player.getX();
            double difY = this.getY() - player.getY();

            if ((accDelta > TRGame.ENEMY_RUN_DIR_CHANGE_TIME) &&
                    (Math.abs(difX) > TRGame.GHOST_WIDTH/2 || Math.abs(difY) > TRGame.GHOST_WIDTH/2) &&
                    (Math.max(Math.abs(difX),Math.abs(difY))/Math.min(Math.abs(difX),Math.abs(difY)) > 1.05)) {

                if (Math.abs(difX) > Math.abs(difY)) {
                    if (difX > 0)
                        direction = TRGame.Direction.LEFT;
                    else
                        direction = TRGame.Direction.RIGHT;
                } else {
                    if (difY > 0)
                        direction = TRGame.Direction.DOWN;
                    else
                        direction = TRGame.Direction.UP;
                }

                accDelta = 0;
            }
        } else {

            pursuing = false;
            speed = TRGame.ENEMY_WALK_SPEED;

            if (accDelta > TRGame.ENEMY_WALK_DIR_CHANGE_TIME) {
                changeDirection();
                accDelta = 0;
            }
        }

        return move(direction);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public boolean onPursuit() {
        return pursuing;
    }

    public void dontMove() {
        moving = false;
    }
}
