package com.jms.trgame;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by jmontes on 6/12/16.
 */
public abstract class ScreenObject implements Comparable<ScreenObject> {

    public abstract void draw();

    protected Rectangle rect;

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

    public double getRadius() {
        return (rect.width + rect.height) / 4;
    }
}

