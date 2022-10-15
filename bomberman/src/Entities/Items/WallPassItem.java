package Entities.Items;

import Game.Map;
import javafx.scene.image.Image;

public class WallPassItem extends Item {
    public WallPassItem(int xUnit, int yUnit, Image img, Map map) {
        super(xUnit, yUnit, img, map);
        canDead = false;
        isDead = false;
    }

    @Override
    public void update() {
        if (collied(map.getPlayer())) {
            map.getPlayer().setWallPass(true);
            remove();
            sound.play();
        }
    }
}
