package Entities.Character.Enemy;

import Game.Map;
import Graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Kondoria extends Enemy {
    private final Sprite frameLeft1 = Sprite.kondoria_left1;
    private final Sprite frameLeft2 = Sprite.kondoria_left2;
    private final Sprite frameLeft3 = Sprite.kondoria_left3;
    private final Sprite frameRight1 = Sprite.kondoria_right1;
    private final Sprite frameRight2 = Sprite.kondoria_right2;
    private final Sprite frameRight3 = Sprite.kondoria_right3;
    private final Sprite frameDie = Sprite.kondoria_dead;
    private static final int SPEED = 1;

    public Kondoria(int xUnit, int yUnit, Image img, Map map) {
        super(xUnit, yUnit, img, SPEED, map);
        OPTIMIZATION_DISTANCE = Sprite.SCALED_SIZE / 8 + 1;
        dx = speed;
        dy = 0;
        canDead = true;
        isDead = false;
        wallPass = true;
        this.point = 1000;
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
            img = Sprite.movingSprite(frameRight1, frameRight2, frameRight3, animate, 20).getFxImage();
        } else if (dy < 0) {
            img = Sprite.movingSprite( frameLeft1, frameLeft2, frameLeft3, animate, 20).getFxImage();
        } else if (dx > 0) {
            img = Sprite.movingSprite(frameRight1, frameRight2, frameRight3, animate, 20).getFxImage();
        } else if (dx < 0){
            img = Sprite.movingSprite( frameLeft1, frameLeft2, frameLeft3, animate, 20).getFxImage();
        } else {
            img = Sprite.movingSprite( frameLeft1, frameLeft2, frameLeft3, animate, 20).getFxImage();
        }

        if (isDead) {
            if (timeAfterDie > 0) {
                timeAfterDie--;
                if (timeAfterDie > 30) {
                    img = Sprite.kondoria_dead.getFxImage();
                } else {
                    img = Sprite.movingSprite(deadFrame[0], deadFrame[1], deadFrame[2], animate, 30).getFxImage();
                }
            }
        }
        gc.drawImage(img, x, y);
    }

    private void randomMove() {
        randomMoveHigh();
    }
}
