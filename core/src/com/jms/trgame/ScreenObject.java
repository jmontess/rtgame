package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by jmontes on 9/12/16.
 */
public class ScreenObject {

    protected TRGame game;
    protected Board board;
    protected Rectangle rect;
    protected Texture objectTexture;
    protected boolean moving = false;
    protected Direction direction = Direction.NONE;
    protected int speed = TRGame.OBJECT_SPEED;
    protected float distanceMoved = 0;

    public ScreenObject(TRGame game, Board board, Position pos) {

        this.game = game;
        this.board = board;

        this.rect = new Rectangle();
        this.rect.width  = TRGame.GRID_CELL_SIDE;
        this.rect.height = TRGame.GRID_CELL_SIDE;
        this.rect.x = pos.x * TRGame.GRID_CELL_SIDE;
        this.rect.y = pos.y * TRGame.GRID_CELL_SIDE;
    }

    public void setTexture(String texturePath) {
        this.objectTexture = new Texture(Gdx.files.internal(texturePath));
    }

    public void draw() {
        updatePosition();
        if (objectTexture != null) {
            game.getSpriteBatch().draw(objectTexture, rect.x, rect.y, rect.width, rect.height);
        }
    }

    public void move(Direction dir) {
        if (!moving && (dir != Direction.NONE)) {
            Position nextPos = getCurrentPosition();
            switch (dir) {
                case UP:
                    nextPos.y++;
                    break;
                case DOWN:
                    nextPos.y--;
                    break;
                case LEFT:
                    nextPos.x--;
                    break;
                case RIGHT:
                    nextPos.x++;
                    break;
            }
            if (board.isEmpty(nextPos)) {
                moving = true;
                direction = dir;
            }
        }
    }

    public Position getCurrentPosition() {
        return new Position((int)Math.round(rect.x/TRGame.GRID_CELL_SIDE), (int)Math.round(rect.y/TRGame.GRID_CELL_SIDE));
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean overlaps(ScreenObject o) {
        return this.rect.overlaps(o.rect);
    }

    public double distanceTo(ScreenObject o) {
        return this.getCurrentPosition().distanceTo(o.getCurrentPosition());
    }

    public boolean canMove(Direction dir) {
        Position currentPos = this.getCurrentPosition();
        boolean result = true;
        switch (dir) {
            case UP:
                result = board.isEmpty(currentPos.x, currentPos.y+1);
                break;
            case DOWN:
                result = board.isEmpty(currentPos.x, currentPos.y-1);
                break;
            case LEFT:
                result = board.isEmpty(currentPos.x-1, currentPos.y);
                break;
            case RIGHT:
                result = board.isEmpty(currentPos.x+1, currentPos.y);
                break;
        }
        return result;
    }

    private void updatePosition() {
        if (moving) {
            float timeDelta = Gdx.graphics.getDeltaTime();
            float step = Math.min(timeDelta * speed, (TRGame.GRID_CELL_SIDE - distanceMoved));
            switch (direction) {
                case UP:
                    rect.y += step;
                    break;
                case DOWN:
                    rect.y -= step;
                    break;
                case LEFT:
                    rect.x -= step;
                    break;
                case RIGHT:
                    rect.x += step;
                    break;
            }
            distanceMoved += step;
            if (distanceMoved >= TRGame.GRID_CELL_SIDE) {
                moving = false;
                distanceMoved = 0;
                //System.out.println(this.getCurrentPosition());
            }
        }
    }
}
