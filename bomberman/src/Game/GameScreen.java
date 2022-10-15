package Game;

import Entities.Character.Bomber;
import Entities.Entity;
import Inputs.KeyboardListener;
import Sounds.Sound;
import javafx.animation.AnimationTimer;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import Graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import jdk.internal.util.xml.impl.Input;

public class GameScreen extends AnimationTimer {
    private Map map;
    private Canvas myCanvas;
    private GraphicsContext gc;
    private Group root;
    private Scene scene;
    private KeyboardListener keyboardListener;
    private KeyboardListener inputs;
    private Input input;
    private long delta;
    private double waitTime;
    private long lastFrameTime;
    private static final int FPS = 30;
    private static final double timePerFrame = 1000.0 / FPS;
    private int layoutCanvas = 0;
    private Sound sound = new Sound();
    private int timeInSeconds = 60 * 5;
    private int time = timeInSeconds * FPS;
    private Infomation boxMessage = new Infomation();
    private static final int levelMax = 50;
    private boolean win  = false;
    private int timeAfterWin = 9 * FPS;
    private boolean endGame = false;
    private int score;

    public GameScreen(Map map1)  {
        initGameScreen(map1);

    }

    private void initGameScreen(Map map1) {
        this.map = map1;
        myCanvas = new Canvas(map.getWidth() * Sprite.SCALED_SIZE, map.getHeight() * Sprite.SCALED_SIZE);
        gc = myCanvas.getGraphicsContext2D();
        root = new Group(myCanvas, boxMessage.getHbox());
        scene = new Scene(root, 20 * Sprite.SCALED_SIZE, 15 * Sprite.SCALED_SIZE);
        layoutCanvas = 0;
        myCanvas.setLayoutY(scene.getHeight() - myCanvas.getHeight());
        createPlayer();
    }

    @Override
    public void handle(long now) {
        delta = (now - lastFrameTime) / 1000000;
        lastFrameTime = now;
        waitTime = timePerFrame - delta ;
        if (waitTime > 0) {
            try {
                Thread.sleep((long) waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        update();
        render();
    }


    private void render() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        if (map != null) {
            map.getFixedObjects().forEach(object -> object.render(gc));
            map.getItemObject().forEach(object -> object.render(gc));
            map.getBarrierObjects().forEach(object -> object.render(gc));
            map.getEntities().forEach(object -> object.render(gc));
            map.getBombs().forEach(object -> object.render(gc));
        }
    }

    private void update() {
        boxMessage.update(time(), map.player.getScore(), map.player.getLevel());
        updateSound();
        updateTime();
        updateScene();

        map.getFixedObjects().forEach(Entity::update);
        map.getItemObject().forEach(Entity::update);
        map.getBarrierObjects().forEach(Entity::update);
        map.getEntities().forEach(Entity::update);
        map.getBombs().forEach(Entity::update);
        map.update();
        if (map.getLevel() < map.player.getLevel()) {
            if (map.player.getLevel() <= levelMax) {
                nextLevel(map.player.getLevel());
            } else {
                root.getChildren().clear();
                map.deleteMap();
                win = true;
                root.getChildren().add(SceneEndGame.screenWin());
                endGame = true;
                score = map.player.getScore();
            }
        }
        if ((!map.hasBomber() && !win) || time == 0) {
            map.deleteMap();
            root.getChildren().clear();
            root.getChildren().add(SceneEndGame.screenLose());
            endGame = true;
        }

        if (endGame && timeAfterWin == 0 )  {
            root.getChildren().add(SceneEndGame.screenResult(score));
        }
    }

    private void updateScene() {
        double xx = map.player.getX() + myCanvas.getLayoutX();
        if (xx > 10 * Sprite.SCALED_SIZE) {
            layoutCanvas -= map.player.getSpeed();
        } else if (xx < 9 * Sprite.SCALED_SIZE) {
            layoutCanvas += map.player.getSpeed();
        }
        if (layoutCanvas > 0) {
            layoutCanvas = 0;
        } else if (layoutCanvas < -(myCanvas.getWidth() - 20 * Sprite.SCALED_SIZE)) {
            layoutCanvas = (int) -( myCanvas.getWidth() - 20 * Sprite.SCALED_SIZE);
        }
        if (map.player.getX() == Sprite.SCALED_SIZE && map.player.getY() == Sprite.SCALED_SIZE) {
            layoutCanvas = 0;
        }
        myCanvas.setLayoutX(layoutCanvas);
    }

    public Scene getScreen() {
        return scene;
    }

    public Map getMap() {
        return map;
    }

    private void createPlayer() {
        inputs = new KeyboardListener(this.scene);
        map.player = new Bomber(1, 1, Sprite.player_down.getFxImage(), inputs, this.map);
        map.getEntities().add(map.player);
    }



    public void setMap(Map map) {
        this.map = map;
    }

    public void updateSound() {
        if ((map.player.isDead && map.player.getTimeAfterDie() == 89)) {
            sound.stop();
            score = map.player.getScore();
        }
        if (time == timeInSeconds * FPS) {
            sound.setFile(0);
            sound.setVolume(0.8);
            sound.loop();
        }
        if (map.player.isDead && map.player.getTimeAfterDie() == 0) {
            if (timeAfterWin > 0) {
                timeAfterWin--;
            }
            if (timeAfterWin == 9 * FPS - 1) {
                sound.stop();
                sound.setFile(8);
                sound.play();
            }
        }

        if (win) {
            if (timeAfterWin > 0) {
                timeAfterWin--;
            }
            if (timeAfterWin == 9 * FPS - 1) {
                sound.stop();
                sound.setFile(7);
                sound.play();
            }
        }
    }

    private void nextLevel(int nextLevel) {

        Map newMap = new Map(nextLevel);
        map.getPlayer().nextLevel(newMap);
        setMap(newMap);
        resetTime();
    }

    public int time() {
        return time/(FPS);
    }

    public void updateTime() {
        if (time > 0) {
            time--;
        }
    }

    public void resetTime() {
        time = timeInSeconds * FPS - 1;
    }

}
