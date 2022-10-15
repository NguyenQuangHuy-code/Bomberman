package Entities.Items;

import Entities.Entity;
import Game.Map;
import Graphics.Sprite;
import Sounds.Sound;
import javafx.scene.image.Image;

public abstract class Item extends Entity {
    protected Sound sound = new Sound();
    public Item(int xUnit, int yUnit, Image img, Map map) {
        super(xUnit, yUnit, img, map);
        sound.setFile(6);
    }


    @Override
    public boolean collied(Entity entity) {
        if (map.object[getYTile()][getXTile()] != '\0') {
            return false;
        }
        int x1 = x;
        int y1 = y;
        int x2 = entity.getX();
        int y2 = entity.getY();

        return (x1 + Sprite.SCALED_SIZE - 5 >= x2) && (x2 + Sprite.SCALED_SIZE >= x1 + 5)
                && (y1 + Sprite.SCALED_SIZE - 5 >= y2) && (y2 + Sprite.SCALED_SIZE >= y1 + 5);
    }

}
