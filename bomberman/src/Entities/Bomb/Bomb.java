package Entities.Bomb;

import Entities.AnimatedEntity;
import Entities.Character.Bomber;
import Entities.Character.Character;
import Entities.Entity;
import Graphics.Sprite;
import Sounds.Sound;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bomb extends AnimatedEntity {

    private final Sprite frameBomb = Sprite.bomb;
    private final Sprite frameBomb2 = Sprite.bomb_2;
    private final Sprite frameBomb1 = Sprite.bomb_1;
    private final Sprite frameBombExploded = Sprite.bomb_exploded;
    private final Sprite frameBombExploded1= Sprite.bomb_exploded1;
    private final Sprite frameBombExploded2 = Sprite.bomb_exploded2;
    public  Bomber bomber_;
    private Sound sound = new Sound();

    private Flame flame;

    private int timeToExplode = 100;
    private int timeFlame = 15;
    public boolean exploded = false;

    public Bomb(int xUnit, int yUnit, Image img, Bomber bomber) {
        super(xUnit, yUnit, img, bomber.getMap());
        canDead = false;
        isDead = false;
        this.bomber_ = bomber;
    }

    @Override
    public void update() {
        if (timeToExplode > 0 && !exploded) {
            if (!bomber_.isDetonator()) {
                timeToExplode--;
            }
            if (!bomber_.isBombActivate()) {
                timeToExplode = 0;
            }
        } else {
            if (!exploded) {
                exploded();
            } else {
                flame.update();
                if (timeFlame > 0) {
                    if (timeFlame == 15) {
                        sound.setFile(5);
                        sound.play();
                    }
                    timeFlame--;
                } else {
                    remove();
                    map.getPlayer().recoveryBomb();
                }
            }
        }
        animate();

    }

    @Override
    public boolean collied(Entity entity) {
        int x1 = x;
        int y1 = y;
        int x2 = entity.getX() + ((Character) entity).getDx();
        int y2 = entity.getY() + ((Character) entity).getDy();

        return (x1 + Sprite.SCALED_SIZE -3 >= x2) && (x2 + Sprite.SCALED_SIZE >= x1 + 3)
                && (y1 + Sprite.SCALED_SIZE - 3 >= y2) && (y2 + Sprite.SCALED_SIZE >= y1 + 3);
    }

    @Override
    public void render(GraphicsContext gc) {
         if (!exploded){
            img = Sprite.movingSprite(frameBomb, frameBomb1, frameBomb2, animate, 30).getFxImage();
             gc.drawImage(img, x, y);
        } else {
             renderFlame(gc);
         }

    }

    public void exploded() {
        exploded = true;
        bomber_.setBombActivate(true);
        createFlame();
    }

    public void createFlame() {
        int xUnit = this.x / Sprite.SCALED_SIZE;
        int yUnit = this.y / Sprite.SCALED_SIZE;
        flame = new Flame(xUnit, yUnit, FlameSegment.flameCenter[2].getFxImage(), this.x, this.y, this);
    }

    public void renderFlame(GraphicsContext gc) {
        flame.render(gc);
    }
}
