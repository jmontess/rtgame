package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmontes on 9/12/16.
 */
public class GameScreen implements Screen {

    private TRGame game;

    private OrthographicCamera camera;

    private Texture floorTexture;

    private Board board;

    private Player player;
    private Enemy enemy;
    private List<ScreenObject> cheese;

    private Sound biteSound;

    private int score = 0;

    // -----------------------------------------------------------------------------------------------------------------


    public GameScreen(TRGame game) {

        this.game = game;

        // Creating the board
        board = new Board(game, TRGame.SCREEN_WIDTH/TRGame.GRID_CELL_SIDE, TRGame.SCREEN_HEIGHT/TRGame.GRID_CELL_SIDE);

        // Creating the camera
        camera = new OrthographicCamera(TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);
        camera.setToOrtho(false, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);

        // Loading floor texture
        floorTexture = new Texture(Gdx.files.internal(TRGame.TEXTURE_FLOOR_PATH));

        // Creating obstacles
        for (int i = 0; i < 10; i++) {
            board.setEmpty(board.getRandomEmptyCell(), false);
        }

        // Creating player
        Position playerPos = board.getRandomEmptyCell();
        player = new Player(game, board, playerPos);
        //player.setTexture(TRGame.TEXTURE_GREEN_BOX_PATH);
        board.setEmpty(playerPos, false);

        // Creating enemy
        Position enemyPos = board.getRandomEmptyCell();
        enemy = new Enemy(game, board, enemyPos);
        //enemy.setTexture(TRGame.TEXTURE_RED_BOX_PATH);
        board.setEmpty(enemyPos, false);

        // Creating cheese
        cheese = new ArrayList<ScreenObject>();
        for (int i = 0; i < 3; i++) {
            Position cheesePos = board.getRandomEmptyCell();
            ScreenObject cheeseObj = new ScreenObject(game, board, cheesePos);
            cheeseObj.setTexture(TRGame.TEXTURE_CHEESE_PATH);
            cheese.add(cheeseObj);
            board.setEmpty(cheesePos, false);
        }

        // Freeing cells
        board.setEmpty(playerPos, true);
        board.setEmpty(enemyPos, true);
        for (ScreenObject c : cheese) {
            board.setEmpty(c.getCurrentPosition(), true);
        }

        // Creating sounds
        biteSound = Gdx.audio.newSound(Gdx.files.internal(TRGame.SOUND_BITE_PATH));
    }

    // -----------------------------------------------------------------------------------------------------------------

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
        board.draw();
        for (ScreenObject c : cheese) { c.draw(); }
        player.draw();
        enemy.draw();

        game.getFont().draw(game.getSpriteBatch(), ""+score, 8, TRGame.SCREEN_HEIGHT-8);

        game.getSpriteBatch().end();

        // Process input
        processInput();

        // Inform enemy of player's position
        enemy.setPlayerPosition(player.getCurrentPosition());

        // Move enemy
        enemy.move(getRandomDirectionFrom(enemy.getCurrentPosition()));

        // Check if some cheese is taken
        int i = 0;
        while (i < cheese.size()) {
            ScreenObject c = cheese.get(i);
            if (player.distanceTo(c) < 1) {
                cheese.remove(i);
                biteSound.play();
                score += TRGame.CHEESE_SCORE;
            } else {
                i++;
            }
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

    }

    // -----------------------------------------------------------------------------------------------------------------

    private void drawFloor() {
        game.getSpriteBatch().draw(floorTexture, 0, 0, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void processInput() {

        if (Gdx.input.isTouched()) {

            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            Position touch = new Position(Math.round(touchPos.x/TRGame.GRID_CELL_SIDE), Math.round(touchPos.y/TRGame.GRID_CELL_SIDE));
            Position playerPos = player.getCurrentPosition();
            Direction dir;

            int xDif = touch.x - playerPos.x;
            int yDif = touch.y - playerPos.y;

            if (xDif == 0 && yDif == 0) {
                dir = Direction.NONE;
            } else if (Math.abs(xDif) > Math.abs(yDif)) {
                if (xDif < 0)
                    dir = Direction.LEFT;
                else
                    dir = Direction.RIGHT;
            } else {
                if (yDif < 0)
                    dir = Direction.DOWN;
                else
                    dir = Direction.UP;
            }
            player.move(dir);

        } else if (Gdx.input.isKeyPressed(Input.Keys.UP))
            player.move(Direction.UP);
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            player.move(Direction.DOWN);
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            player.move(Direction.LEFT);
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            player.move(Direction.RIGHT);

    }

    // -----------------------------------------------------------------------------------------------------------------

    public Direction getRandomDirectionFrom(Position pos) {
        List<Direction> availiableDirs = board.getAvailableDirections(pos);
        return availiableDirs.get(MathUtils.random(0, availiableDirs.size()-1));
    }
}
