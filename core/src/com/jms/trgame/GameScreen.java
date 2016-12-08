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

    private ArrayList<ScreenObject> cheese;
    private ArrayList<ScreenObject> obstacles;

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

        // Creating Obstacles
        generateObstacles(15);

        // Creating Cheese
        generateCheese(3);

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
            enemy.setAllowedDirections(allowedDirections(enemy));
            enemy.move(pacman);
            fixPosition(enemy, enemy.getDirection());
        }

        // Process input
        if (pacman.isAlive()) {
            pacman.setAllowedDirections(allowedDirections(pacman));
            if (Gdx.input.isTouched()) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                pacman.move(touchPos.x, touchPos.y);
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP))
                pacman.move(TRGame.Direction.UP);
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                pacman.move(TRGame.Direction.DOWN);
            else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                pacman.move(TRGame.Direction.LEFT);
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                pacman.move(TRGame.Direction.RIGHT);
            else
                pacman.dontMove();
        }

        fixPosition(pacman, pacman.getDirection());

        // Check for interaction events
        boolean objectsChanged = false;
        int cheesePos = 0;
        while (cheesePos < cheese.size()) {
            Cheese c = (Cheese) cheese.get(cheesePos);
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
        for (ScreenObject o : obstacles) {
            o.dispose();
        }
        for (ScreenObject c : cheese) {
            c.dispose();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void drawFloor() {
        game.getSpriteBatch().draw(floorTexture, 0, 0, TRGame.SCREEN_WIDTH, TRGame.SCREEN_HEIGHT);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void generateObstacles(int numObstacles) {
        obstacles = new ArrayList<ScreenObject>();
        while (obstacles.size() < numObstacles) {
            int obstacleX = MathUtils.random(TRGame.OBSTACLE_WIDTH, TRGame.SCREEN_WIDTH - TRGame.OBSTACLE_WIDTH);
            int obstacleY = MathUtils.random(TRGame.OBSTACLE_HEIGHT, TRGame.SCREEN_HEIGHT - TRGame.OBSTACLE_HEIGHT);
            Obstacle newObstacle = new Obstacle(game, obstacleX, obstacleY);
            Obstacle newObstacle2 = null;
            if (MathUtils.random(0,1) == 0)
                newObstacle2 = new Obstacle(game, obstacleX + TRGame.OBSTACLE_WIDTH, obstacleY);
            else
                newObstacle2 = new Obstacle(game, obstacleX, obstacleY + TRGame.OBSTACLE_HEIGHT);
            if (!newObstacle.overlaps(obstacles) && !newObstacle2.overlaps(obstacles)) {
                obstacles.add(newObstacle);
                obstacles.add(newObstacle2);
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void generateCheese(int numCheese) {
        cheese = new ArrayList<ScreenObject>();
        while (cheese.size() < numCheese) {
            Cheese newCheeseObj = new Cheese(
                    game,
                    MathUtils.random(TRGame.CHEESE_WIDTH, TRGame.SCREEN_WIDTH - TRGame.CHEESE_WIDTH),
                    MathUtils.random(TRGame.CHEESE_HEIGHT, TRGame.SCREEN_HEIGHT - TRGame.CHEESE_HEIGHT));
            if (!newCheeseObj.overlaps(cheese) && !newCheeseObj.overlaps(obstacles))
                cheese.add(newCheeseObj);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private ArrayList<ScreenObject> generateScreenObjectsList() {
        ArrayList<ScreenObject> result = new ArrayList<ScreenObject>();
        result.add(pacman);
        result.add(enemy);
        result.addAll(cheese);
        result.addAll(obstacles);
        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private ArrayList<TRGame.Direction> allowedDirections(ScreenObject target) {

        boolean allowedUp    = true;
        boolean allowedDown  = true;
        boolean allowedLeft  = true;
        boolean allowedRight = true;

        for (ScreenObject o : obstacles) {
            //if (o.overlaps(target)) System.out.println("Overlaps! "+o.getLeftX()+" "+target.getRightX()+" "+(o.getLeftX() < target.getRightX())+" "+allowedRight);

            if (allowedUp && o.overlaps(target) && (o.getBottomY() > target.getBottomY())) {
                allowedUp = false;
            }
            if (allowedDown && o.overlaps(target) && (o.getBottomY() < target.getBottomY())) {
                allowedDown = false;
            }
            if (allowedRight && o.overlaps(target) && (o.getX() > target.getX())  && (o.getLeftX() < target.getRightX())) {
                //System.out.println("yeah!");
                allowedRight = false;
            }
            if (allowedLeft && o.overlaps(target) && (o.getX() < target.getX()) && (o.getRightX() > target.getLeftX())) {
                allowedLeft = false;
            }
        }

        ArrayList<TRGame.Direction> result = new ArrayList<TRGame.Direction>();
        if (allowedUp)    result.add(TRGame.Direction.UP);
        if (allowedDown)  result.add(TRGame.Direction.DOWN);
        if (allowedLeft)  result.add(TRGame.Direction.LEFT);
        if (allowedRight) result.add(TRGame.Direction.RIGHT);

        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void fixPosition(ScreenObject target, TRGame.Direction lastMove) {
        for (ScreenObject o : obstacles) {
            //if (o.overlaps(target)) System.out.println("Overlaps! "+o.getLeftX()+" "+target.getRightX()+" "+(o.getLeftX() < target.getRightX())+" "+allowedRight);

            if ((lastMove == TRGame.Direction.UP) && o.overlaps(target) && (o.getBottomY() > target.getBottomY())) {
                target.rect.y = (float)(o.getBottomY() - target.rect.height);
            }
            if ((lastMove == TRGame.Direction.DOWN) && o.overlaps(target) && (o.getBottomY() < target.getBottomY())) {
                target.rect.y = (float)o.getTopY();
            }
            if ((lastMove == TRGame.Direction.RIGHT) && o.overlaps(target) && (o.getX() > target.getX())  && (o.getLeftX() < target.getRightX())) {
                //allowedRight = false;
                target.rect.x = (float)o.getLeftX() - target.rect.width;
            }
            if ((lastMove == TRGame.Direction.LEFT) && o.overlaps(target) && (o.getX() < target.getX()) && (o.getRightX() > target.getLeftX())) {
                //allowedLeft = false;
                target.rect.x = (float)o.getRightX();
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
}
