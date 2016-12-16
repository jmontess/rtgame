package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmontes on 9/12/16.
 */
public class Board {

    // -----------------------------------------------------------------------------------------------------------------

    TRGame game;

    public boolean[][] boardMap;
    int width;
    int height;

    private boolean allowLeave = false;

    Texture obstacleTexture;

    // -----------------------------------------------------------------------------------------------------------------

    public Board(TRGame game, int width, int height) {

        this.game = game;
        this.width = width;
        this.height = height;
        boardMap = new boolean[width][height];

        // Initialize board
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boardMap[i][j] = true;
            }
        }

        obstacleTexture = new Texture(Gdx.files.internal(TRGame.TEXTURE_ROCK_PATH));
    }

    // -----------------------------------------------------------------------------------------------------------------

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAllowLeave(boolean allow) {
        allowLeave = allow;
    }

    public boolean isEmpty(int x, int y) {
        boolean result = false;
        if (x >= 0 && x < width && y >= 0 && y < height)
            result = boardMap[x][y];
        if (allowLeave && (x < 0 || x >= width || y < 0 || y > height))
            result = true;
        return result;
    }

    public boolean isEmpty(BoardPosition pos) {
        return isEmpty(pos.x, pos.y);
    }

    public void setEmpty(BoardPosition pos, boolean empty) {
        if (pos.x < width && pos.y < height)
            boardMap[pos.x][pos.y] = empty;

        if (pos.x < 0 || pos.x >= width || pos.y < 0 || pos.y > height) {
            System.out.println("Ojo: "+pos);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public List<BoardPosition> getEmptyCells() {
        List<BoardPosition> result = new ArrayList<BoardPosition>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (boardMap[i][j])
                    result.add(new BoardPosition(i,j));
            }
        }
        return result;
    }

    public List<BoardPosition> getOccupiedCells() {
        List<BoardPosition> result = new ArrayList<BoardPosition>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!boardMap[i][j])
                    result.add(new BoardPosition(i,j));
            }
        }
        return result;
    }

    public BoardPosition getRandomEmptyCell() {
        List<BoardPosition> emptyCells = getEmptyCells();
        BoardPosition result = null;
        if (emptyCells.size() > 0)
            result = emptyCells.get(MathUtils.random(0,emptyCells.size()-1));
        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public List<Direction> getAvailableDirections(BoardPosition pos) {
        List<Direction> result = new ArrayList<Direction>();
        if (isEmpty(pos.x,pos.y+1)) result.add(Direction.UP);
        if (isEmpty(pos.x,pos.y-1)) result.add(Direction.DOWN);
        if (isEmpty(pos.x-1,pos.y)) result.add(Direction.LEFT);
        if (isEmpty(pos.x+1,pos.y)) result.add(Direction.RIGHT);
        if (result.size() == 0) result.add(Direction.NONE);
        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void draw() {
        for (BoardPosition pos : getOccupiedCells()) {
            //System.out.println(pos);
            game.getSpriteBatch().draw(obstacleTexture, pos.x*TRGame.GRID_CELL_SIDE, pos.y*TRGame.GRID_CELL_SIDE, TRGame.GRID_CELL_SIDE, TRGame.GRID_CELL_SIDE);
        }
        //System.out.println(getOccupiedCells().size());
        //System.exit(-1);
    }

    public void dispose() {
        obstacleTexture.dispose();
    }
}
