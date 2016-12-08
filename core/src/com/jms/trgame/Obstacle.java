package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by jmontes on 6/12/16.
 */
public class Obstacle extends ScreenObject {

    private TRGame game;

    private Texture cheeseTexture;
    //private Rectangle rect;

    public Obstacle(TRGame game, int startX, int startY) {

        super(startX, startY, TRGame.OBSTACLE_WIDTH, TRGame.OBSTACLE_HEIGHT);

        this.game = game;

        this.cheeseTexture = new Texture(Gdx.files.internal(TRGame.OBSTACLE_TEXTURE_PATH));
    }

    @Override
    public void draw() {
        game.getSpriteBatch().draw(cheeseTexture, rect.x, rect.y, rect.width, rect.height);
    }

    @Override
    public TRGame.Direction getDirection() {
        return TRGame.Direction.NONE;
    }
    @Override
    public TRGame.Direction move(TRGame.Direction direction) {
        return TRGame.Direction.NONE;
    }


    public boolean checkInteraction(Pacman player) {
        return this.distanceTo(player) < this.getRadius()*2;
    }

    public void dispose() {
        cheeseTexture.dispose();
    }
}
