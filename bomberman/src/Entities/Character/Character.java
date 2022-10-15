package Entities.Character;

import Entities.AnimatedEntity;
import Entities.Brick;
import Entities.Character.Enemy.Enemy;
import Entities.Entity;
import Entities.Wall;
import Game.Map;
import Graphics.Sprite;
import com.sun.javafx.iio.gif.GIFImageLoaderFactory;
import javafx.scene.image.Image;

public abstract class Character extends AnimatedEntity {

    protected int dx;
    protected int dy;
    protected int speed;
    protected int OPTIMIZATION_DISTANCE;
    protected boolean moving;
    protected int timeAfterDie = 90;
    protected boolean wallPass;
    protected boolean bombPass = false;
    protected boolean detonator;
    protected boolean bombActivate = true;
    protected boolean flamePass = false;
    protected boolean mystery = false;

    public Character(int xUnit, int yUnit, Image img, int speed, Map map) {
        super(xUnit, yUnit, img, map);
        this.speed = speed;
    }

    public boolean isCollision() {

        int xx = x + dx;
        int yy = y + dy;
        int barrierX1;
        int barrierX2;
        int barrierY1;
        int barrierY2;
        Entity barrier1 = null;
        Entity barrier2 = null;
        if (dx > 0) {
            barrierX1 = (xx + (int) img.getWidth()) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierX2 = barrierX1;
            barrierY1 = (yy + OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierY2 = (yy+ (int) img.getHeight() - OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrier1 = map.getBarrierAt(barrierX1, barrierY1);
            barrier2 = map.getBarrierAt(barrierX2, barrierY2);
        } else if (dx < 0) {
            barrierX1 = (this.x + dx) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierX2 = barrierX1;
            barrierY1 = (this.y + OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierY2 = (this.y + (int) img.getHeight() - OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrier1 = map.getBarrierAt(barrierX1, barrierY1);
            barrier2 = map.getBarrierAt(barrierX2, barrierY2);
        }
        if (dy > 0) {
            barrierY1 = (this.y + (int) img.getHeight() + dy) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierY2 = barrierY1;
            barrierX1 = (this.x + OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierX2 = (this.x + (int) img.getWidth() - OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrier1 = map.getBarrierAt(barrierX1, barrierY1);
            barrier2 = map.getBarrierAt(barrierX2, barrierY2);
        } else if (dy < 0) {
            barrierY1 = (this.y + dy) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierY2 = barrierY1;
            barrierX1 = (this.x + OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierX2 = (this.x + (int) img.getWidth() - OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrier1 = map.getBarrierAt(barrierX1, barrierY1);
            barrier2 = map.getBarrierAt(barrierX2, barrierY2);
        }

        if (barrier1 != null) {
            if (barrier1.collied(this) && ((barrier1 instanceof Wall) || (barrier1 instanceof Brick && !wallPass))) {
                return true;
            }
        }
        if (barrier2 != null) {
            return barrier2.collied(this) && ((barrier2 instanceof Wall) || (barrier2 instanceof Brick && !wallPass));
        }
        return false;
    }

    public void optimizationX() {
        int x1 = x - (x / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE;
        if (x1 <= OPTIMIZATION_DISTANCE) {
            x = (x / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE;
        } else if (x1 >= Sprite.SCALED_SIZE - OPTIMIZATION_DISTANCE) {
            x = (x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
        }
    }

    public void optimizationY() {
        int y1 = y - (y / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE;
        if (y1 <= OPTIMIZATION_DISTANCE) {
            y = (y / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE;
        } else if (y1 >= Sprite.SCALED_SIZE - OPTIMIZATION_DISTANCE) {
            y = (y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
        }
    }

    public void optimization() {
        optimizationX();
        optimizationY();
    }

    public boolean isFlamePass() {
        return flamePass;
    }

    public boolean isBombActivate() {
        return bombActivate;
    }

    public boolean isDetonator() {
        return detonator;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public abstract boolean canMove();

    public int getSpeed() {
        return speed;
    }

    public void stopMove() {
        speed = 0;
    }

    public void kill() {
        isDead = true;
    }

    public boolean removeFromMap() {
        return isDead && timeAfterDie == 0;
    }

    public int getTimeAfterDie() {
        return timeAfterDie;
    }
}
