package Entities.Items;

import Game.Map;
import javafx.scene.image.Image;

public class BombItem extends Item{
    public BombItem(int xUnit, int yUnit, Image img, Map map) {
        super(xUnit, yUnit, img, map);
        canDead = false;
        isDead = false;
    }

    @Override
    public void update() {
        if (collied(map.getPlayer())) {
            map.getPlayer().addBombNumber();
            remove();
            sound.play();
        }
    }

}
