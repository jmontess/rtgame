package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by jmontes on 6/12/16.
 */
public class Cheese extends ScreenObject {

    private TRGame game;

    private Texture cheeseTexture;
    //private Rectangle rect;

    public Cheese(TRGame game, int startX, int startY) {


        this.game = game;

        this.cheeseTexture = new Texture(Gdx.files.internal(TRGame.CHEESE_TEXTURE_PATH));

        this.rect = new Rectangle();
        this.rect.height = TRGame.CHEESE_HEIGHT;
        this.rect.width = TRGame.CHEESE_WIDTH;
        this.rect.x = startX - rect.width/2;
        this.rect.y = startY - rect.height/2;

    }

    @Override
    public void draw() {
        game.getSpriteBatch().draw(cheeseTexture, rect.x, rect.y, rect.width, rect.height);
    }

    public boolean checkInteraction(Pacman player) {
        return this.distanceTo(player) < 100;
    }

    public void dispose() {
        cheeseTexture.dispose();
    }
}
