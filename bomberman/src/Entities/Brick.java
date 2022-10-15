package Entities;

import Game.Map;
import Graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Iterator;


public class Brick extends AnimatedEntity {

    private Sprite brickExploded= Sprite.brick_exploded;
    private Sprite brickExploded1 = Sprite.brick_exploded1;
    private Sprite brickExploded2 = Sprite.brick_exploded2;
    public int timeRemove = 15;

    public Brick(int xUnit, int yUnit, Image img, Map map) {
        super(xUnit, yUnit, img, map);
        canDead = true;
        isDead = false;
    }

    @Override
    public void update() {
        animate();
        if (getRemove()) {
            if (timeRemove > 0) {
                timeRemove--;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (getRemove()) {
            if (timeRemove == 15) {
                img = Sprite.brick_exploded.getFxImage();
            } else if (timeRemove == 10) {
                img = Sprite.brick_exploded1.getFxImage();
            } else if (timeRemove == 5) {
                img = Sprite.brick_exploded2.getFxImage();
            }
        }

            gc.drawImage(img, x, y);


    }

    @Override
    public boolean collied(Entity entity) {
        return true;
    }

    public boolean removeFromMap() {
        return getRemove() && timeRemove == 0;
    }


}
