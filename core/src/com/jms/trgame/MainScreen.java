package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

/**
 * Created by jmontes on 14/12/16.
 */
public class MainScreen implements Screen {

    private TRGame game;

    private OrthographicCamera camera;

    private Texture floorTexture;

    private Board board;
    private Player player;
    private Enemy enemy;

    private float timer = 0;

    public MainScreen(TRGame game) {

        this.game = game;
        // Creating the camera
        camera = new OrthographicCamera(TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);
        camera.setToOrtho(false, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);

        floorTexture = new Texture(Gdx.files.internal(TRGame.TEXTURE_INTRO_FLOOR_PATH));

        board = new Board(game, TRGame.SCREEN_WIDTH/TRGame.GRID_CELL_SIDE+1, TRGame.SCREEN_HEIGHT/TRGame.GRID_CELL_SIDE);
        board.setAllowLeave(true);
        player = new Player(game, board, new BoardPosition(TRGame.SCREEN_WIDTH/TRGame.GRID_CELL_SIDE+1, 1));
        enemy = new Enemy(game, board, new BoardPosition(-1,1));

        player.rect.y -= TRGame.GRID_CELL_SIDE/2;
        enemy.rect.y -= TRGame.GRID_CELL_SIDE/2;

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        //Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        // Draw the screen
        game.getSpriteBatch().begin();

        drawBackground();

        player.draw();
        enemy.draw();

        drawText();

        game.getSpriteBatch().end();

        // Follow the animation script

        if (timer > 1 && timer <= 2) {
            // The rat appears, running from the right side of the screen
            player.move(Direction.LEFT);
        } else if (timer > 2 && timer <= 4) {
            // Te rats pauses
            player.move(Direction.NONE);
        } else if (timer > 4 && timer <= 5.5) {
            // The tar resumes running
            player.move(Direction.LEFT);
        } else if (timer > 5.5 && timer <= 8) {
            // The rat stops again
            player.move(Direction.NONE);
        } else if (timer > 8 && timer <= 9) {
            // Laura appears from the left
            enemy.setAlert(true);
            enemy.move(Direction.RIGHT);
        } else if (timer > 9 && timer <= 11) {
            // The rat runs from Laura
            enemy.move(Direction.RIGHT);
            player.move(Direction.RIGHT);
        } else if (timer > 11 && timer <= 13) {
            // The rat stops. Laura continues her approach.
            enemy.move(Direction.RIGHT);
            player.move(Direction.NONE);
        }  else if (timer > 13 && timer <= 14.5) {
            // The rat flees. Laura continues.
            enemy.move(Direction.RIGHT);
            player.move(Direction.RIGHT);
        } else if (timer > 14.5 && timer <= 16) {
            // Laura stops
            enemy.setAlert(false);
            enemy.move(Direction.NONE);
        } else if (timer > 16 && timer <= 19) {
            // Laura returns
            enemy.move(Direction.LEFT);
        } else if (timer > 19 && timer <= 20) {
            // Laura stops
            enemy.move(Direction.NONE);
        } else if (timer > 20 && timer <= 20.5) {
            // Laura moves right again
            enemy.move(Direction.RIGHT);
        } else if (timer > 20.5 && timer <= 21.5) {
            // Laura stops again
            enemy.move(Direction.NONE);
        } else if (timer > 21.5 && timer <= 28) {
            // Laura returns
            enemy.move(Direction.LEFT);
        } else if (timer > 28 && timer <= 30) {
            // Resetting player position
            player.rect.x = TRGame.SCREEN_WIDTH;
            enemy.rect.x = TRGame.SCREEN_WIDTH;
        } else if (timer > 30 && timer <= 30.5) {
            // The rat appears from the right
            player.move(Direction.LEFT);
        } else if (timer > 30.5 && timer <= 33) {
            // The rat stops
            player.move(Direction.NONE);
        } else if (timer > 33 && timer <= 35) {
            // The rat leaves again
            player.move(Direction.RIGHT);
        } else if (timer > 34 && timer <= 36) {
            // The enemy appears from the right
            enemy.move(Direction.LEFT);
        } else if (timer > 36 && timer <= 37) {
            // The enemy stops
            enemy.move(Direction.NONE);
        } else if (timer > 37 && timer <= 40) {
            // The enemy leaves again
            enemy.move(Direction.RIGHT);
        } else if (timer > 40 && timer <= 40.1) {
            // Resetting player and enemy position
            player.rect.x = TRGame.SCREEN_WIDTH;
            enemy.rect.x = TRGame.SCREEN_WIDTH+TRGame.GRID_CELL_SIDE*0.1f;
            enemy.setAlert(true);
        } else if (timer > 40.1 && timer <= 53) {
            // Final chase11
            player.move(Direction.LEFT);
            enemy.move(Direction.LEFT);
        } else if (timer > 53) {
            // resetting everything
            player.setPosition(new BoardPosition(TRGame.SCREEN_WIDTH/TRGame.GRID_CELL_SIDE, 1));
            player.rect.x += TRGame.GRID_CELL_SIDE/3;
            enemy.setPosition(new BoardPosition(-1,1));
            player.rect.y -= TRGame.GRID_CELL_SIDE/2;
            enemy.rect.y -= TRGame.GRID_CELL_SIDE/2;
            enemy.setAlert(false);
            timer = 0;
        }

        timer += delta;

        // check if screen clicked
        if (timer > 0.5 && Gdx.input.isTouched()) {
            game.gameScreen = new GameScreen(game);
            game.setScreen(game.gameScreen);
            dispose();
        }
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
        board.dispose();
        player.dispose();
        enemy.dispose();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void drawBackground() {
        game.getSpriteBatch().draw(floorTexture, 0, 0, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT/3);
    }

    private void drawText() {
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(game.getLargeFont(), TRGame.GAME_TITLE, Color.WHITE, TRGame.SCREEN_WIDTH*0.8f, Align.center, true);
        game.getLargeFont().draw(
                game.getSpriteBatch(),
                glyphLayout,
                (int) ((TRGame.SCREEN_WIDTH) * 0.1),
                (int) (TRGame.SCREEN_HEIGHT * 0.85));

        glyphLayout = new GlyphLayout();
        glyphLayout.setText(game.getFont(), "Tap screen to continue", Color.WHITE, TRGame.SCREEN_WIDTH*0.8f, Align.center, true);
        game.getFont().draw(
                game.getSpriteBatch(),
                glyphLayout,
                (int) ((TRGame.SCREEN_WIDTH) * 0.1),
                (int) (TRGame.SCREEN_HEIGHT * 0.375));
    }

    // -----------------------------------------------------------------------------------------------------------------

}
