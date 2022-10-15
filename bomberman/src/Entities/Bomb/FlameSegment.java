package Entities.Bomb;

import Entities.AnimatedEntity;
import Entities.Character.Character;
import Entities.Entity;
import Game.Map;
import Graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import Entities.Bomb.Bomb;

public class FlameSegment extends AnimatedEntity {

    public Sprite[] flameFrame;

    public static final Sprite[] flameCenter = {Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2};
    public static final Sprite[] flameHorizontal = {Sprite.explosion_horizontal, Sprite.explosion_horizontal1, Sprite.explosion_horizontal2};
    public static final Sprite[] flameVertical = {Sprite.explosion_vertical, Sprite.explosion_vertical1, Sprite.explosion_vertical2};
    public static final Sprite[] flameHorizontalLeftLast = {Sprite.explosion_horizontal_left_last, Sprite.explosion_horizontal_left_last1, Sprite.explosion_horizontal_left_last2};
    public static final Sprite[] flameHorizontalRightLast = {Sprite.explosion_horizontal_right_last, Sprite.explosion_horizontal_right_last1, Sprite.explosion_horizontal_right_last2};
    public static final Sprite[] flameVerticalTopLast = {Sprite.explosion_vertical_top_last, Sprite.explosion_vertical_top_last1, Sprite.explosion_vertical_top_last2};
    public static final Sprite[] flameVerticalBottomLast = {Sprite.explosion_vertical_down_last,Sprite.explosion_vertical_down_last1, Sprite.explosion_vertical_down_last2};
    public FlameSegment(int xUnit, int yUnit, Image img, Sprite[] flameFrame, Map map) {
        super(xUnit, yUnit, img, map);
        this.flameFrame = flameFrame;
    }

    @Override
    public void update() {
        animate();
        for (Character entity : map.getEntities()) {
            if (entity != null) {
                if (collied(entity) && !entity.isFlamePass()) {
                    entity.kill();
                }
            }
        }

    }

    @Override
    public boolean collied(Entity other) {
        int x1 = x- 2;
        int y1 = y - 2;
        int x2 = other.getX();
        int y2 = other.getY();

        return (x1 + Sprite.SCALED_SIZE - 8 >= x2) && (x2 + Sprite.SCALED_SIZE >= x1 + 8)
                && (y1 + Sprite.SCALED_SIZE - 8 >= y2) && (y2 + Sprite.SCALED_SIZE >= y1 + 8);
    }

    @Override
    public void render(GraphicsContext gc) {
        img = Sprite.movingSprite(flameFrame[0], flameFrame[1], flameFrame[2], animate, 15).getFxImage();
        gc.drawImage(img, x, y);
    }

}
