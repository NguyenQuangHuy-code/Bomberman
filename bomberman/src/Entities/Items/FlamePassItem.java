package Entities.Items;

import Entities.Entity;
import Game.Map;
import javafx.scene.image.Image;

public class FlamePassItem extends Item {
    public FlamePassItem(int xUnit, int yUnit, Image img, Map map) {
        super(xUnit, yUnit, img, map);
        canDead = false;
        isDead = false;
    }

    @Override
    public void update() {
        if (collied(map.getPlayer())) {
            map.getPlayer().setFlamePass(true);
            remove();
            sound.play();
        }
    }
}
