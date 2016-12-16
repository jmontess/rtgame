package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by jmontes on 9/12/16.
 */
public class Enemy extends ScreenObject {

    TextureRegion[] framesUp;
    TextureRegion[] framesDown;
    TextureRegion[] framesLeft;
    TextureRegion[] framesRight;
    TextureRegion[] framesAlert;

    private Animation walkAnimationUp;
    private Animation walkAnimationDown;
    private Animation walkAnimationLeft;
    private Animation walkAnimationRight;
    private Animation walkAnimation;
    private Animation alertAnimation;

    private Texture alertTexture;

    private float stateTime = 0.0f;

    private boolean alert = false;
    private Direction playerDirection = Direction.NONE;

    public Enemy(TRGame game, Board board, BoardPosition pos) {

        super(game, board, pos);

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

        framesAlert = new TextureRegion[2];
        framesAlert[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_ENEMY_ALERT_1_PATH)));
        framesAlert[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_ENEMY_ALERT_2_PATH)));
        alertAnimation = new Animation(TRGame.ENEMY_ANIMATION_FRAME_DURATION, framesAlert);

        walkAnimation = walkAnimationDown;
        direction = Direction.DOWN;

        alertTexture = alertAnimation.getKeyFrame(0, true).getTexture();
    }

    @Override
    public void setTexture(String texturePath) {
    }

    @Override
    public void draw() {
        if (!freezed && direction != Direction.NONE) {
            stateTime += Gdx.graphics.getDeltaTime();
            objectTexture = walkAnimation.getKeyFrame(stateTime, true).getTexture();
        }
        super.draw();
        if (alert) {
            alertTexture = alertAnimation.getKeyFrame(stateTime, true).getTexture();
            game.getSpriteBatch().draw(alertTexture, rect.x, rect.y+TRGame.GRID_CELL_SIDE/3, TRGame.GRID_CELL_SIDE, TRGame.GRID_CELL_SIDE);
        }
    }

    @Override
    public void move(Direction dir) {
        Direction finalDir = dir;
        if (alert && playerDirection != Direction.NONE) {
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
            direction = finalDir;
        }
        super.move(finalDir);
    }

    public void setPlayerPosition(BoardPosition playerPos) {

        BoardPosition pos = getCurrentPosition();

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

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    @Override
    public void dispose() {

        super.dispose();

        for (int i = 0; i < 2; i++) {
            framesUp[i].getTexture().dispose();
            framesDown[i].getTexture().dispose();
            framesLeft[i].getTexture().dispose();
            framesRight[i].getTexture().dispose();
        }
    }
}
