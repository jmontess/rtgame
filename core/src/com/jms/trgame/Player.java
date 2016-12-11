package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by jmontes on 10/12/16.
 */
public class Player extends ScreenObject {

    TextureRegion[] framesUp, framesUpTail;
    TextureRegion[] framesDown, framesDownTail;
    TextureRegion[] framesLeft, framesLeftTail;
    TextureRegion[] framesRight, framesRightTail;

    private Animation walkAnimationUp, walkAnimationUpTail;
    private Animation walkAnimationDown, walkAnimationDownTail;
    private Animation walkAnimationLeft, walkAnimationLeftTail;
    private Animation walkAnimationRight, walkAnimationRightTail;
    private Animation walkAnimation, walkAnimationTail;

    TextureRegion[] framesIdleA, framesIdleB;

    private Animation idleAnimationA, idleAnimationB;
    private Animation idleAnimation;

    private Texture objectTextureTail;

    private float stateTime = 0.0f;
    private float idleChangeTime = 0.0f;

    public Player(TRGame game, Board board, Position pos) {

        super(game, board, pos);

        framesUp = new TextureRegion[2];
        framesUp[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_WALK_UP_1_PATH)));
        framesUp[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_WALK_UP_2_PATH)));
        walkAnimationUp = new Animation(TRGame.PLAYER_ANIMATION_FRAME_DURATION, framesUp);
        framesDown = new TextureRegion[2];
        framesDown[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_WALK_DOWN_1_PATH)));
        framesDown[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_WALK_DOWN_2_PATH)));
        walkAnimationDown = new Animation(TRGame.PLAYER_ANIMATION_FRAME_DURATION, framesDown);
        framesLeft = new TextureRegion[2];
        framesLeft[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_WALK_LEFT_1_PATH)));
        framesLeft[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_WALK_LEFT_2_PATH)));
        walkAnimationLeft = new Animation(TRGame.PLAYER_ANIMATION_FRAME_DURATION, framesLeft);
        framesRight = new TextureRegion[2];
        framesRight[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_WALK_RIGHT_1_PATH)));
        framesRight[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_WALK_RIGHT_2_PATH)));
        walkAnimationRight = new Animation(TRGame.PLAYER_ANIMATION_FRAME_DURATION, framesRight);

        framesUpTail = new TextureRegion[2];
        framesUpTail[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_TAIL_WALK_UP_1_PATH)));
        framesUpTail[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_TAIL_WALK_UP_2_PATH)));
        walkAnimationUpTail = new Animation(TRGame.PLAYER_ANIMATION_FRAME_DURATION, framesUpTail);
        framesDownTail = new TextureRegion[2];
        framesDownTail[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_TAIL_WALK_DOWN_1_PATH)));
        framesDownTail[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_TAIL_WALK_DOWN_2_PATH)));
        walkAnimationDownTail = new Animation(TRGame.PLAYER_ANIMATION_FRAME_DURATION, framesDownTail);
        framesLeftTail = new TextureRegion[2];
        framesLeftTail[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_TAIL_WALK_LEFT_1_PATH)));
        framesLeftTail[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_TAIL_WALK_LEFT_2_PATH)));
        walkAnimationLeftTail = new Animation(TRGame.PLAYER_ANIMATION_FRAME_DURATION, framesLeftTail);
        framesRightTail = new TextureRegion[2];
        framesRightTail[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_TAIL_WALK_RIGHT_1_PATH)));
        framesRightTail[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_TAIL_WALK_RIGHT_2_PATH)));
        walkAnimationRightTail = new Animation(TRGame.PLAYER_ANIMATION_FRAME_DURATION, framesRightTail);

        framesIdleA = new TextureRegion[2];
        framesIdleA[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_IDLE_A1_PATH)));
        framesIdleA[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_IDLE_A2_PATH)));
        idleAnimationA = new Animation(TRGame.PLAYER_ANIMATION_FRAME_DURATION, framesIdleA);

        framesIdleB = new TextureRegion[2];
        framesIdleB[0] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_IDLE_B1_PATH)));
        framesIdleB[1] = new TextureRegion(new Texture(Gdx.files.internal(TRGame.TEXTURE_PLAYER_IDLE_B2_PATH)));
        idleAnimationB = new Animation(TRGame.PLAYER_ANIMATION_FRAME_DURATION*4, framesIdleB);

        idleAnimation = idleAnimationA;
        walkAnimation = idleAnimation;

        direction = Direction.NONE;

        objectTexture = walkAnimation.getKeyFrame(stateTime, true).getTexture();
    }

    @Override
    public void setTexture(String texturePath) {
    }

    @Override
    public void draw() {
        if (!freezed) {
            stateTime += Gdx.graphics.getDeltaTime();
            objectTexture = walkAnimation.getKeyFrame(stateTime, true).getTexture();
        }
        super.draw();
        if (moving) {
            if (!freezed) {
                objectTextureTail = walkAnimationTail.getKeyFrame(stateTime, true).getTexture();
            }
            float tailX = rect.x;
            float tailY = rect.y;
            switch (direction) {
                case UP:
                    tailY -= TRGame.GRID_CELL_SIDE;
                    break;
                case DOWN:
                    tailY += TRGame.GRID_CELL_SIDE;
                    break;
                case LEFT:
                    tailX += TRGame.GRID_CELL_SIDE;
                    break;
                case RIGHT:
                    tailX -= TRGame.GRID_CELL_SIDE;
                    break;
            }
            game.getSpriteBatch().draw(objectTextureTail, tailX, tailY, rect.width, rect.height);
        } else {
            walkAnimation = idleAnimation;
        }
        if (stateTime - idleChangeTime > 2) {
            idleChangeTime = stateTime;
            if (idleAnimation == idleAnimationA) {
                idleAnimation = idleAnimationB;
            } else {
                idleAnimation = idleAnimationA;
            }
        }
    }

    @Override
    public void move(Direction dir) {
        Direction finalDir = dir;
        if (canMove(finalDir) && !moving) {
            switch (finalDir) {
                case UP:
                    walkAnimation = walkAnimationUp;
                    walkAnimationTail = walkAnimationUpTail;
                    break;
                case DOWN:
                    walkAnimation = walkAnimationDown;
                    walkAnimationTail = walkAnimationDownTail;
                    break;
                case LEFT:
                    walkAnimation = walkAnimationLeft;
                    walkAnimationTail = walkAnimationLeftTail;
                    break;
                case RIGHT:
                    walkAnimation = walkAnimationRight;
                    walkAnimationTail = walkAnimationRightTail;
                    break;
                default :
                    walkAnimation = idleAnimation;
            }
        }
        super.move(finalDir);
    }

    @Override
    public void dispose() {

        super.dispose();

        for (int i = 0; i < 2; i++) {
            framesUp[i].getTexture().dispose();
            framesDown[i].getTexture().dispose();
            framesLeft[i].getTexture().dispose();
            framesRight[i].getTexture().dispose();
            framesUpTail[i].getTexture().dispose();
            framesDownTail[i].getTexture().dispose();
            framesLeftTail[i].getTexture().dispose();
            framesRightTail[i].getTexture().dispose();
            framesIdleA[i].getTexture().dispose();
            framesIdleB[i].getTexture().dispose();
        }
    }
}
