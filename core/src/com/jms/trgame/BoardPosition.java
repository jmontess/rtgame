package com.jms.trgame;

/**
 * Created by jmontes on 9/12/16.
 */
public class BoardPosition {

    public int x;
    public int y;

    public BoardPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(BoardPosition pos) {
        return Math.sqrt(Math.pow(this.x - pos.x, 2) + Math.pow(this.y - pos.y, 2));
    }

    @Override
    public String toString() {
        return "BoardPosition{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
