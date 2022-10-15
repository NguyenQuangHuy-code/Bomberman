package Entities;

import Game.Map;
import javafx.scene.image.Image;

public class Grass extends Entity {

    public Grass(int xUnit, int yUnit, Image img, Map map) {
        super(xUnit, yUnit, img, map);
        canDead = false;
        isDead = false;
    }

    @Override
    public void update() {

    }

    @Override
    public boolean collied(Entity entity) {
        return true;
    }
}
