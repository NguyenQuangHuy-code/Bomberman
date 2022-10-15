package Entities;

import Game.Map;
import Graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Entity {
    protected int x;
    protected int y;
    protected Image img;
    protected Map map;

    public boolean canDead;
    public boolean isDead;

    protected boolean remove = false;

    public Entity(int xUnit, int yUnit, Image img, Map map) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
        this.map = map;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, x, y);
    }

    public abstract void update();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getXTile() {
        return x / Sprite.SCALED_SIZE;
    }

    public int getYTile() {
        return y / Sprite.SCALED_SIZE;
    }

    public abstract boolean collied(Entity entity);

    public void remove() {
        remove = true;
    }

    public boolean getRemove() {
        return this.remove;
    }

    public Map getMap() {
        if (map != null) {
            return map;
        } else {
            return null;
        }
    }
}
