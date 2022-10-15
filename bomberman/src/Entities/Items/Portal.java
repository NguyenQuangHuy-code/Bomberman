package Entities.Items;

import Entities.Entity;
import Game.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Portal extends Item {
    public Portal(int xUnit, int yUnit, Image img, Map map) {
        super(xUnit, yUnit, img, map);
        canDead = false;
        isDead = false;
    }

    @Override
    public void update() {
        if (collied(map.getPlayer())) {
            if (!map.hasEnemy()) {
                map.getPlayer().increaseLevel();
            }
        }
    }


}
