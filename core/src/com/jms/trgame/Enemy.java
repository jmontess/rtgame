package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by jmontes on 9/12/16.
 */
public class Enemy extends ScreenObject {

    private Texture textureBase;
    private Texture textureAlert;

    TextureRegion[] framesUp;
    TextureRegion[] framesDown;
    TextureRegion[] framesLeft;
    TextureRegion[] framesRight;

    private Animation walkAnimationUp;
    private Animation walkAnimationDown;
    private Animation walkAnimationLeft;
    private Animation walkAnimationRight;
    private Animation walkAnimation;

    private float stateTime = 0.0f;

    private boolean alert = false;
    private Direction playerDirection = Direction.NONE;

    public Enemy(TRGame game, Board board, Position pos) {

        super(game, board, pos);

        textureBase = new Texture(Gdx.files.internal(TRGame.TEXTURE_RED_BOX_PATH));
        textureAlert = new Texture(Gdx.files.internal(TRGame.TEXTURE_RED_BOX_ALERT_PATH));

        objectTexture = textureBase;

        speed = TRGame.ENEMY_SPEED;

        framesUp = new TextureRegion[2];
        framesUp[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_ENEMY_WALK_UP_1_PATH)));
        framesUp[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_ENEMY_WALK_UP_2_PATH)));
        walkAnimationUp = new Animation(TRGame.ENEMY_ANIMATION_FRAME_DURATION, framesUp);

        framesDown = new TextureRegion[2];
        framesDown[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_ENEMY_WALK_DOWN_1_PATH)));
        framesDown[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_ENEMY_WALK_DOWN_2_PATH)));
        walkAnimationDown = new Animation(TRGame.ENEMY_ANIMATION_FRAME_DURATION, framesDown);

        framesLeft = new TextureRegion[2];
        framesLeft[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_ENEMY_WALK_LEFT_1_PATH)));
        framesLeft[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_ENEMY_WALK_LEFT_2_PATH)));
        walkAnimationLeft = new Animation(TRGame.ENEMY_ANIMATION_FRAME_DURATION, framesLeft);

        framesRight = new TextureRegion[2];
        framesRight[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_ENEMY_WALK_RIGHT_1_PATH)));
        framesRight[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_ENEMY_WALK_RIGHT_2_PATH)));
        walkAnimationRight = new Animation(TRGame.ENEMY_ANIMATION_FRAME_DURATION, framesRight);

        walkAnimation = walkAnimationDown;
        direction = Direction.DOWN;
    }

    @Override
    public void setTexture(String texturePath) {
    }

    @Override
    public void draw() {
        if (alert) {
            objectTexture = textureAlert;
        } else {
            objectTexture = textureBase;
        }
        stateTime += Gdx.graphics.getDeltaTime();
        objectTexture = walkAnimation.getKeyFrame(stateTime, true).getTexture();
        super.draw();
    }

    @Override
    public void move(Direction dir) {
        Direction finalDir = dir;
        if (alert) {
            finalDir = playerDirection;
        }
        if (!moving) {
            switch (finalDir) {
                case UP:
                    walkAnimation = walkAnimationUp;
                    break;
                case DOWN:
                    walkAnimation = walkAnimationDown;
                    break;
                case LEFT:
                    walkAnimation = walkAnimationLeft;
                    break;
                case RIGHT:
                    walkAnimation = walkAnimationRight;
                    break;
            }
        }
        super.move(finalDir);
    }

    public void setPlayerPosition(Position playerPos) {

        Position pos = getCurrentPosition();

        if (pos.distanceTo(playerPos) <= TRGame.ENEMY_ALERT_RANGE) {

            alert = true;

            int xDif = pos.x - playerPos.x;
            int yDif = pos.y - playerPos.y;

            Direction mainDir = Direction.NONE;
            Direction secondaryDir = Direction.NONE;

            if (xDif == 0 && yDif == 0) {
                playerDirection = Direction.NONE;
            } else if (Math.abs(xDif) > Math.abs(yDif)) {
                if (xDif > 0) {
                    mainDir = Direction.LEFT;
                } else if (xDif < 0) {
                    mainDir = Direction.RIGHT;
                }
                if (yDif > 0) {
                    secondaryDir = Direction.DOWN;
                } else if (yDif < 0) {
                    secondaryDir = Direction.UP;
                }

                if (!canMove(mainDir) && secondaryDir == Direction.NONE) {
                    if (canMove(Direction.DOWN)) secondaryDir = Direction.DOWN;
                    else if (canMove(Direction.UP)) secondaryDir = Direction.UP;
                }

            } else {
                if (yDif > 0) {
                    mainDir = Direction.DOWN;
                } else if (yDif < 0) {
                    mainDir = Direction.UP;
                }
                if (xDif > 0) {
                    secondaryDir = Direction.LEFT;
                } else if (xDif < 0) {
                    secondaryDir = Direction.RIGHT;
                }

                if (!canMove(mainDir) && secondaryDir == Direction.NONE) {
                    if (canMove(Direction.LEFT)) secondaryDir = Direction.LEFT;
                    else if (canMove(Direction.RIGHT)) secondaryDir = Direction.RIGHT;
                }
            }

            if (!this.canMove(mainDir)) mainDir = Direction.NONE;
            if (!this.canMove(secondaryDir)) secondaryDir = Direction.NONE;

            playerDirection = mainDir;
            if (mainDir == Direction.NONE) {
                playerDirection = secondaryDir;
            }
        } else {
            alert = false;
        }
    }
}
