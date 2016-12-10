package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by jmontes on 6/12/16.
 */
public class Cheese extends OldScreenObject {

    private TRGame game;

    private Texture cheeseTexture;
    //private Rectangle rect;

    public Cheese(TRGame game, int startX, int startY) {

        super(startX, startY, TRGame.CHEESE_WIDTH, TRGame.CHEESE_HEIGHT);

        this.game = game;

        this.cheeseTexture = new Texture(Gdx.files.internal(TRGame.CHEESE_TEXTURE_PATH));

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
        return this.distanceTo(player) < this.getRadius()*1.5;
    }

    public void dispose() {
        cheeseTexture.dispose();
    }
}
