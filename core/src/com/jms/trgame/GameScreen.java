package com.jms.trgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jmontes on 27/11/16.
 */
public class GameScreen implements Screen {

    // -----------------------------------------------------------------------------------------------------------------

    private TRGame game;

    private OrthographicCamera camera;

    private Texture floorTexture;

    private Pacman pacman;
    private Enemy enemy;

    private ArrayList<Cheese> cheese;

    private ArrayList<ScreenObject> screenObjects;

    private Sound biteSound, ouchSound;
    private Music alarmSound;

    // -----------------------------------------------------------------------------------------------------------------

    public GameScreen(TRGame game) {

        this.game = game;

        // Creating the camera
        camera = new OrthographicCamera(TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);
        camera.setToOrtho(false, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);

        // Loading textures
        floorTexture = new Texture(Gdx.files.internal(TRGame.TEXTURE_FLOOR_PATH));

        // Creating Pacman
        pacman = new Pacman(game, (int)(TRGame.SCREEN_WIDTH*0.5), (int)(TRGame.SCREEN_HEIGHT*0.25));

        // Creating Enemy
        enemy = new Enemy(game, (int)(TRGame.SCREEN_WIDTH*0.75), (int)(TRGame.SCREEN_HEIGHT*0.75));

        // Creating Cheese
        cheese = new ArrayList<Cheese>();
        for (int i = 0; i < 3; i++) {
            cheese.add(
                    new Cheese(
                            game,
                            MathUtils.random(TRGame.CHEESE_WIDTH, TRGame.SCREEN_WIDTH - TRGame.CHEESE_WIDTH),
                            MathUtils.random(TRGame.CHEESE_HEIGHT, TRGame.SCREEN_HEIGHT - TRGame.CHEESE_HEIGHT)));
        }

        screenObjects = generateScreenObjectsList();

        biteSound = Gdx.audio.newSound(Gdx.files.internal("bite.mp3"));
        ouchSound = Gdx.audio.newSound(Gdx.files.internal("ouch.mp3"));
        alarmSound = Gdx.audio.newMusic(Gdx.files.internal("ping.wav"));
        alarmSound.setLooping(true);
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
        Collections.sort(screenObjects);
        Collections.reverse(screenObjects);
        for (ScreenObject o : screenObjects) o.draw();
        game.getSpriteBatch().end();

        // Move enemy
        if (pacman.isAlive()) {
            enemy.move(pacman);
        }

        // Process input
        if (pacman.isAlive()) {
            if (Gdx.input.isTouched()) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                pacman.move(touchPos.x, touchPos.y);
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP))
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

        // Check for interaction events
        boolean objectsChanged = false;
        int cheesePos = 0;
        while (cheesePos < cheese.size()) {
            Cheese c = cheese.get(cheesePos);
            if (c.checkInteraction(pacman)) {
                objectsChanged = true;
                biteSound.play();
                cheese.remove(cheesePos);
            } else {
                cheesePos++;
            }
        }
        if (objectsChanged) {
            screenObjects = generateScreenObjectsList();
        }

        // Check if the enemy has cached the player
        if (pacman.isAlive() && (enemy.distanceTo(pacman) <= enemy.getRadius())) {
            pacman.enemyGotYou();
            enemy.dontMove();
            ouchSound.play();
        }

        if (pacman.isAlive() && enemy.onPursuit())
            alarmSound.play();
        else
            alarmSound.stop();

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
        enemy.dispose();
        for (Cheese c : cheese) {
            c.dispose();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void drawFloor() {
        game.getSpriteBatch().draw(floorTexture, 0, 0, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private ArrayList<ScreenObject> generateScreenObjectsList() {
        ArrayList<ScreenObject> result = new ArrayList<ScreenObject>();
        result.add(pacman);
        result.add(enemy);
        for (Cheese c : cheese) result.add(c);
        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------

}
