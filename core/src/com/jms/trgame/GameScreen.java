package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by jmontes on 27/11/16.
 */
public class GameScreen implements Screen {

    // -----------------------------------------------------------------------------------------------------------------

    private TRGame game;

    private OrthographicCamera camera;

    private Texture floorTexture;

    private Pacman pacman;
    private Ghost ghost;

    // -----------------------------------------------------------------------------------------------------------------

    public GameScreen(TRGame game) {

        this.game = game;

        // Creating the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);

        // Loading textures
        floorTexture = new Texture(Gdx.files.internal(TRGame.TEXTURE_FLOOR_PATH));

        // Creating Pacman
        pacman = new Pacman(game, (int)(TRGame.SCREEN_WIDTH*0.5), (int)(TRGame.SCREEN_HEIGHT*0.25));

        // Creating Ghost
        ghost = new Ghost(game, (int)(TRGame.SCREEN_WIDTH*0.75), (int)(TRGame.SCREEN_HEIGHT*0.75));

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        // Draw the screen
        game.getSpriteBatch().begin();
        drawFloor();
        ghost.draw();
        pacman.draw();
        game.getSpriteBatch().end();

        // Move ghost
        ghost.move();

        // Process input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            pacman.move(touchPos.x, touchPos.y);
        } else
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            pacman.move(TRGame.DIRECTION_UP);
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            pacman.move(TRGame.DIRECTION_DOWN);
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            pacman.move(TRGame.DIRECTION_LEFT);
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            pacman.move(TRGame.DIRECTION_RIGHT);
        else
            pacman.dontMove();


    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        floorTexture.dispose();
        pacman.dispose();
        ghost.dispose();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void drawFloor() {
        game.getSpriteBatch().draw(floorTexture, 0, 0, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
