package com.jms.trgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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

    private Texture floorTexture, veilTexture, cheeseTexture;

    private Board board;

    private Player player;
    private Enemy enemy;
    private List<ScreenObject> cheese;

    private Sound biteSound, ouchSound;

    private int previousScore;
    private int score;
    private int numCheese;
    private float timer;
    private int gameLevel;

    private enum GameStatus {
        PLAYING, FINISHED, OVER
    }

    GameStatus gameStatus;

    // -----------------------------------------------------------------------------------------------------------------


    public GameScreen(TRGame game) {

        this.game = game;

        // Creating the camera
        camera = new OrthographicCamera(TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);
        camera.setToOrtho(false, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);

        // Initializing game;
        init(0, 0);
    }

    private void init(int initialScore, int level) {

        // Creating the board
        //board = new Board(game, TRGame.SCREEN_WIDTH/TRGame.GRID_CELL_SIDE, TRGame.SCREEN_HEIGHT/TRGame.GRID_CELL_SIDE);
        MazeGenerator mg = new MazeGenerator(TRGame.SCREEN_WIDTH/TRGame.GRID_CELL_SIDE/2, TRGame.SCREEN_HEIGHT/TRGame.GRID_CELL_SIDE/2);
        board = mg.generateBoard(game, level);

        // Loading textures
        floorTexture = new Texture(Gdx.files.internal(TRGame.TEXTURE_FLOOR_PATH));
        veilTexture = new Texture(Gdx.files.internal(TRGame.TEXTURE_VEIL_PATH));
        cheeseTexture = new Texture(Gdx.files.internal(TRGame.TEXTURE_CHEESE_PATHS[0]));

        /*
        // Creating obstacles
        for (int i = 0; i < 10; i++) {
            board.setEmpty(board.getRandomEmptyCell(), false);
        }
        */

        // Creating player
        Position playerPos = board.getRandomEmptyCell();
        player = new Player(game, board, playerPos);
        //player.setTexture(TRGame.TEXTURE_GREEN_BOX_PATH);
        board.setEmpty(playerPos, false);

        // Creating enemy
        Position enemyPos = board.getRandomEmptyCell();
        enemy = new Enemy(game, board, enemyPos);
        while (player.distanceTo(enemy) <= TRGame.ENEMY_ALERT_RANGE) {
            enemyPos = board.getRandomEmptyCell();
            enemy.setPosition(enemyPos);
        }

        //enemy.setTexture(TRGame.TEXTURE_RED_BOX_PATH);
        board.setEmpty(enemyPos, false);

        // Creating cheese
        cheese = new ArrayList<ScreenObject>();
        for (int i = 0; i < 3; i++) {
            Position cheesePos = board.getRandomEmptyCell();
            ScreenObject cheeseObj = new ScreenObject(game, board, cheesePos);
            cheeseObj.setTexture(getRandomCheeseTexturePath());
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
        ouchSound = Gdx.audio.newSound(Gdx.files.internal(TRGame.SOUND_OUCH_PATH));

        // Setting state
        previousScore = initialScore;
        score = initialScore;
        numCheese = 0;
        timer = 0;
        gameStatus = GameStatus.PLAYING;
        gameLevel = level;

        //System.out.println("--->"+board.getOccupiedCells().size());
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

        // Check if some cheese is taken
        checkCheese();

        // Check if the player is caught
        chechCaught();;

        // Draw the screen
        game.getSpriteBatch().begin();

        drawFloor();
        board.draw();
        for (ScreenObject c : cheese) { c.draw(); }
        player.draw();
        enemy.draw();
        game.getFont().draw(game.getSpriteBatch(), ""+score, 8, TRGame.SCREEN_HEIGHT-8);
        drawOvertext();

        game.getSpriteBatch().end();

        // Process input
        processInput();

        // Inform enemy of player's position
        enemy.setPlayerPosition(player.getCurrentPosition());

        // Move enemy
        enemy.move(getRandomDirectionFrom(enemy.getCurrentPosition()));
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
        veilTexture.dispose();
        cheeseTexture.dispose();
        board.dispose();
        player.dispose();
        enemy.dispose();
        for (ScreenObject c : cheese) c.dispose();
        biteSound.dispose();
        ouchSound.dispose();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void drawFloor() {
        game.getSpriteBatch().draw(floorTexture, 0, 0, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);
    }

    private void drawVeil() {
        game.getSpriteBatch().draw(veilTexture, 0, 0, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void drawOvertext() {

        if (gameStatus != GameStatus.PLAYING) {
            timer -= Gdx.graphics.getDeltaTime();
            if (timer <= 0) {
                timer = 0;
                String header = "";
                switch (gameStatus) {
                    case OVER:
                        header = "GAME OVER";
                        break;
                    case FINISHED:
                        header = "STAGE CLEAR";
                        break;
                }
                drawVeil();
                // Game over
                GlyphLayout glyphLayout = new GlyphLayout();
                glyphLayout.setText(game.getLargeFont(), header);
                game.getLargeFont().draw(
                        game.getSpriteBatch(),
                        glyphLayout,
                        (int) ((TRGame.SCREEN_WIDTH - glyphLayout.width) * 0.5),
                        (int) (TRGame.SCREEN_HEIGHT * 0.85));
                // Previous score line
                game.getFont().draw(
                        game.getSpriteBatch(),
                        "Previous",
                        (int) (TRGame.SCREEN_WIDTH * 0.25),
                        (int) (TRGame.SCREEN_HEIGHT * 0.6));
                game.getFont().draw(
                        game.getSpriteBatch(),
                        "" + previousScore,
                        (int) (TRGame.SCREEN_WIDTH * 0.55) + TRGame.GRID_CELL_SIDE,
                        (int) (TRGame.SCREEN_HEIGHT * 0.6));
                // Cheese count line
                game.getSpriteBatch().draw(
                        cheeseTexture,
                        (int) (TRGame.SCREEN_WIDTH * 0.25),
                        (int) (TRGame.SCREEN_HEIGHT * 0.35),
                        TRGame.GRID_CELL_SIDE, TRGame.GRID_CELL_SIDE);
                game.getFont().draw(
                        game.getSpriteBatch(),
                        " x " + numCheese,
                        (int) (TRGame.SCREEN_WIDTH * 0.25) + TRGame.GRID_CELL_SIDE,
                        (int) (TRGame.SCREEN_HEIGHT * 0.35 + TRGame.GRID_CELL_SIDE * 0.65));
                game.getFont().draw(
                        game.getSpriteBatch(),
                        "" + (numCheese * TRGame.CHEESE_SCORE),
                        (int) (TRGame.SCREEN_WIDTH * 0.55) + TRGame.GRID_CELL_SIDE,
                        (int) (TRGame.SCREEN_HEIGHT * 0.35 + TRGame.GRID_CELL_SIDE * 0.65));
                // Total score line
                game.getFont().draw(
                        game.getSpriteBatch(),
                        "Total",
                        (int) (TRGame.SCREEN_WIDTH * 0.25),
                        (int) (TRGame.SCREEN_HEIGHT * 0.3));
                game.getFont().draw(
                        game.getSpriteBatch(),
                        "" + score,
                        (int) (TRGame.SCREEN_WIDTH * 0.55) + TRGame.GRID_CELL_SIDE,
                        (int) (TRGame.SCREEN_HEIGHT * 0.3));

            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void checkCheese() {
        int i = 0;
        while (i < cheese.size()) {
            ScreenObject c = cheese.get(i);
            if (player.distanceTo(c) < 1) {
                cheese.remove(i);
                biteSound.play();
                score += TRGame.CHEESE_SCORE;
                numCheese++;
            } else {
                i++;
            }
        }

        // Check if the game has finished
        if (gameStatus == GameStatus.PLAYING && cheese.size() == 0) {
            enemy.freeze(true);
            player.freeze(true);
            gameStatus = GameStatus.FINISHED;
            timer = 2;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void chechCaught() {
        if (gameStatus == GameStatus.PLAYING && player.distanceTo(enemy) < 1) {
            ouchSound.play();
            enemy.freeze(true);
            player.freeze(true);
            gameStatus = GameStatus.OVER;
            timer = 2;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void processInput() {

        if (gameStatus == GameStatus.PLAYING) {

            if (Gdx.input.isTouched()) {

                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);

                Position touch = new Position(Math.round(touchPos.x / TRGame.GRID_CELL_SIDE), Math.round(touchPos.y / TRGame.GRID_CELL_SIDE));
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

        } else if (gameStatus == GameStatus.OVER || gameStatus == GameStatus.FINISHED) {

            if (timer <= 0 && Gdx.input.isTouched()) {

                if (gameStatus == GameStatus.OVER) {
                    game.setScreen(new MainScreen(game));
                    game.gameScreen = null;
                    this.dispose();
                } else {
                    this.init(score, gameLevel+1);
                }
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Direction getRandomDirectionFrom(Position pos) {
        List<Direction> availiableDirs = board.getAvailableDirections(pos);
        return availiableDirs.get(MathUtils.random(0, availiableDirs.size()-1));
    }

    public String getRandomCheeseTexturePath() {
        return TRGame.TEXTURE_CHEESE_PATHS[MathUtils.random(0,TRGame.TEXTURE_CHEESE_PATHS.length-1)];
    }
}
