package Entities.Bomb;

import Entities.AnimatedEntity;
import Entities.Brick;
import Entities.Entity;
import Entities.Items.FlameItem;
import Entities.Items.Item;
import Game.Map;
import Graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Flame extends AnimatedEntity {

    private final int xCenter;
    private final int yCenter;
    private int radius;
    private Bomb bomb;
    private ArrayList<FlameSegment> flames = new ArrayList<>();

    public Flame(int xUnit, int yUnit, Image img, int xCenter, int yCenter, Bomb bomb) {
        super(xUnit, yUnit, img, bomb.getMap());
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        radius = map.getPlayer().getRadius();
        this.bomb = bomb;
        createFlameSegment();
    }


    @Override
    public void update() {
        for (FlameSegment flame : flames) {
            flame.update();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        flames.forEach(flameSegment -> flameSegment.render(gc));
    }

    @Override
    public boolean collied(Entity entity) {
        return false;
    }

    public void createFlameSegment() {
        int xx = xCenter/ Sprite.SCALED_SIZE;
        int yy = yCenter/ Sprite.SCALED_SIZE;
        FlameSegment center = new FlameSegment(xx, yy, FlameSegment.flameCenter[0].getFxImage()
                , FlameSegment.flameCenter, this.map);
        flames.add(center);

        Bomb bomb1 = null;
        Bomb bomb2 = null;
        Bomb bomb3 = null;
        Bomb bomb4 = null;


        for (int i = 1; i <= radius; ++i) {
            Entity entity = map.getBarrierAt((xx + i) * Sprite.SCALED_SIZE, yy * Sprite.SCALED_SIZE);
            bomb1 = map.getBombAt((xx + i) * Sprite.SCALED_SIZE, yy * Sprite.SCALED_SIZE);
            if (bomb1 != null && !bomb1.exploded) {
                bomb1.exploded();
                break;
            }
            if (entity == null) {
                if (i == radius) {
                    FlameSegment lastRight = new FlameSegment(xx + i, yy, FlameSegment.flameHorizontalRightLast[0].getFxImage(),
                            FlameSegment.flameHorizontalRightLast, this.map);
                    flames.add(lastRight);
                } else {
                    FlameSegment right = new FlameSegment(xx + i, yy, FlameSegment.flameHorizontal[0].getFxImage()
                            , FlameSegment.flameHorizontal, this.map);
                    flames.add(right);
                }
            } else {
                if (entity instanceof Brick) {
                    entity.remove();
                }
                break;
            }
        }
        for (int i = 1; i <= radius; ++i) {
            Entity entity = map.getBarrierAt((xx - i) * Sprite.SCALED_SIZE, yy * Sprite.SCALED_SIZE);
            bomb2 = map.getBombAt((xx - i) * Sprite.SCALED_SIZE, (yy) * Sprite.SCALED_SIZE);

            if (bomb2 != null && !bomb2.exploded) {
                bomb2.exploded();
                break;
            }
            if (entity == null) {
                if (i == radius) {
                    FlameSegment lastLeft = new FlameSegment(xx - i, yy, FlameSegment.flameHorizontalLeftLast[0].getFxImage(),
                            FlameSegment.flameHorizontalLeftLast, this.map);
                    flames.add(lastLeft);
                } else {
                    FlameSegment left = new FlameSegment(xx - i, yy, FlameSegment.flameHorizontal[0].getFxImage()
                            , FlameSegment.flameHorizontal, this.map);
                    flames.add(left);
                }
            } else {
                if (entity instanceof Brick) {
                    entity.remove();
                }
                break;
            }
        }

        for (int i = 1; i <= radius; ++i) {
            Entity entity = map.getBarrierAt((xx) * Sprite.SCALED_SIZE, (yy + i) * Sprite.SCALED_SIZE);
            bomb3 = map.getBombAt(xx * Sprite.SCALED_SIZE, (yy + i) * Sprite.SCALED_SIZE);

            if (bomb3 != null && !bomb3.exploded) {
                bomb3.exploded();
                break;
            }
            if (entity == null) {
                if (i == radius) {
                    FlameSegment lastBottom = new FlameSegment(xx, yy + i, FlameSegment.flameVerticalBottomLast[0].getFxImage(),
                            FlameSegment.flameVerticalBottomLast, this.map);
                    flames.add(lastBottom);
                } else {
                    FlameSegment bottom = new FlameSegment(xx, yy + i, FlameSegment.flameVertical[0].getFxImage()
                            , FlameSegment.flameVertical, this.map);
                    flames.add(bottom);
                }
            } else {
                if (entity instanceof Brick) {
                    entity.remove();
                }
                break;
            }
        }

        for (int i = 1; i <= radius; ++i) {
            Entity entity = map.getBarrierAt(xx * Sprite.SCALED_SIZE, (yy - i) * Sprite.SCALED_SIZE);
            bomb4 = map.getBombAt(xx * Sprite.SCALED_SIZE, (yy - i) * Sprite.SCALED_SIZE);

            if (bomb4 != null && !bomb4.exploded) {
                bomb4.exploded();
                break;
            }
            if (entity == null) {
                if (i == radius) {
                    FlameSegment lastTop = new FlameSegment(xx, yy - i, FlameSegment.flameVerticalTopLast[0].getFxImage(),
                            FlameSegment.flameVerticalTopLast, this.map);
                    flames.add(lastTop);
                } else {
                    FlameSegment top = new FlameSegment(xx, yy - i, FlameSegment.flameVertical[0].getFxImage()
                            , FlameSegment.flameVertical, this.map);
                    flames.add(top);
                }
            } else {
                if (entity instanceof Brick) {
                    entity.remove();
                }
                break;
            }
        }


    }
}
