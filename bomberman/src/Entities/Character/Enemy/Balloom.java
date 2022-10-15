package Entities.Character.Enemy;

import Game.Map;
import Graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Balloom extends Enemy {

    private final Sprite frameLeft1 = Sprite.balloom_left1;
    private final Sprite frameLeft2 = Sprite.balloom_left2;
    private final Sprite frameLeft3 = Sprite.balloom_left3;
    private final Sprite frameRight1 = Sprite.balloom_right1;
    private final Sprite frameRight2 = Sprite.balloom_right2;
    private final Sprite frameRight3 = Sprite.balloom_right3;
    private final Sprite frameDie = Sprite.balloom_dead;
    private static final int SPEED = 1;

    public Balloom(int xUnit, int yUnit, Image img, Map map) {
        super(xUnit, yUnit, img, SPEED, map);
        OPTIMIZATION_DISTANCE = Sprite.SCALED_SIZE / 8;
        dx = speed;
        dy = 0;
        canDead = true;
        isDead = false;
        wallPass = false;
        this.point = 100;
    }

    @Override
    public void update() {
        randomMove();
        move();
        animate();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (dy > 0) {
            img = Sprite.movingSprite( frameRight2, frameLeft3, animate, 20).getFxImage();
        } else if (dy < 0) {
            img = Sprite.movingSprite( frameRight3, frameLeft2, animate, 20).getFxImage();
        } else if (dx > 0) {
            img = Sprite.movingSprite(frameRight1, frameRight2, frameRight3, animate, 20).getFxImage();
        } else if (dx < 0){
            img = Sprite.movingSprite( frameLeft1, frameLeft2, frameLeft3, animate, 20).getFxImage();
        }
        if (dx == 0 && dy == 0) {
            img = Sprite.movingSprite( frameLeft1, frameLeft2, frameLeft3, animate, 20).getFxImage();
        }

        if (isDead) {
            if (timeAfterDie > 0) {
                timeAfterDie--;
                if (timeAfterDie > 30) {
                    img = Sprite.balloom_dead.getFxImage();
                } else {
                    img = Sprite.movingSprite(deadFrame[0], deadFrame[1], deadFrame[2], animate, 30).getFxImage();
                }
            }
        }
        gc.drawImage(img, x, y);
    }

    private void randomMove() {
        randomMoveLow();
    }


    public double pixelToTile(int x) {
        return (double) x / (double) Sprite.SCALED_SIZE;
    }


}
