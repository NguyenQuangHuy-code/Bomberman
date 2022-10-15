package Entities;

import Game.Map;
import javafx.scene.image.Image;

public abstract class AnimatedEntity extends Entity {
    protected int animate = 0;
    private static final int MAX_ANIMATE = 1000;

    public AnimatedEntity(int xUnit, int yUnit, Image img, Map map) {
        super(xUnit, yUnit, img, map);
    }

    protected void animate() {
        if (animate < MAX_ANIMATE) {
            animate++;
        } else {
            animate = 0;
        }
    }

}
