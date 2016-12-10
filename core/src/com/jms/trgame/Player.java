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

    private Texture objectTextureTail;

    private float stateTime = 0.0f;

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

        walkAnimation = walkAnimationRight;
        walkAnimationTail = walkAnimationRightTail;
        direction = Direction.RIGHT;

        objectTexture = walkAnimation.getKeyFrame(stateTime, true).getTexture();
        objectTextureTail = walkAnimationTail.getKeyFrame(stateTime, true).getTexture();
    }

    @Override
    public void setTexture(String texturePath) {
    }

    @Override
    public void draw() {
        if (moving) {
            stateTime += Gdx.graphics.getDeltaTime();
            objectTexture = walkAnimation.getKeyFrame(stateTime, true).getTexture();
            objectTextureTail = walkAnimationTail.getKeyFrame(stateTime, true).getTexture();
        }
        super.draw();
        float tailX = rect.x;
        float tailY = rect.y;
        switch(direction) {
            case UP :
                tailY -= TRGame.GRID_CELL_SIDE;
                break;
            case DOWN :
                tailY += TRGame.GRID_CELL_SIDE;
                break;
            case LEFT :
                tailX += TRGame.GRID_CELL_SIDE;
                break;
            case RIGHT :
                tailX -= TRGame.GRID_CELL_SIDE;
                break;
        }
        game.getSpriteBatch().draw(objectTextureTail, tailX, tailY, rect.width, rect.height);
    }

    @Override
    public void move(Direction dir) {
        Direction finalDir = dir;
        if (!moving) {
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
            }
        }
        super.move(finalDir);
    }
}
