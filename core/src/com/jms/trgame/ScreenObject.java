package com.jms.trgame;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by jmontes on 6/12/16.
 */
public abstract class ScreenObject implements Comparable<ScreenObject> {

    protected Rectangle rect;
    protected ArrayList<TRGame.Direction> allowedDirections;

    public ScreenObject(int startX, int startY, int width, int height) {

        this.rect = new Rectangle();
        this.rect.height = height;
        this.rect.width  = width;
        this.rect.x = startX - rect.width/2;
        this.rect.y = startY - rect.height/2;

        this.allowedDirections = new ArrayList<TRGame.Direction>();
    }

    public int position() {
        return (int)rect.y;
    }

    public int compareTo(ScreenObject o) {
        return this.position() - o.position();
    }

    public double distanceTo(ScreenObject o) {
        return Math.sqrt(Math.pow(this.rect.x - o.rect.x, 2) + Math.pow(this.rect.y - o.rect.y, 2));
    }

    public double getX() {
        return rect.x + rect.height/2;
    }

    public double getY() {
        return rect.y + rect.width/2;
    }

    public double getLeftX() {
        return rect.x;
    }

    public double getRightX() {
        return rect.x + rect.width;
    }

    public double getTopY() {
        return rect.y + rect.height;
    }

    public double getBottomY() {
        return rect.y;
    }

    public double getRadius() {
        return (rect.width + rect.height) / 4;
    }

    public boolean overlaps(ScreenObject o) {
        return this.rect.overlaps(o.rect);
    }

    public boolean overlaps(ArrayList<ScreenObject> objs){

        for (ScreenObject o : objs) {
            if (this.overlaps(o))
                return true;
        }
        return false;
    }

    public void setAllowedDirections(ArrayList<TRGame.Direction> ad) {
        allowedDirections = ad;
    }

    public abstract void draw();
    public abstract TRGame.Direction move(TRGame.Direction direction);
    public abstract TRGame.Direction getDirection();
    public abstract void dispose();
}

