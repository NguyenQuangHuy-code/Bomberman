package Entities.Character.Enemy;

import Entities.Bomb.Bomb;
import Entities.Brick;
import Entities.Character.Character;
import Entities.Entity;
import Entities.Wall;
import Game.Map;
import Graphics.Sprite;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

public abstract class Enemy extends Character {

    protected Sprite[] deadFrame = {Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3};
    protected static int time = 0;
    protected boolean canMove;
    private final Random rand = new Random();
    protected int point;

    ArrayList[][] cellI, cellJ;

    public Enemy(int xUnit, int yUnit, Image img, int speed, Map map) {
        super(xUnit, yUnit, img, speed, map);
    }

    public int getPoint() {
        return point;
    }

    public boolean canMove() {
        return !isCollision();
    }

    protected void move() {
        if (!canMove) {
            dx = 0;
            dy = 0;
        }

        if (moving) {
            if (dx != 0) {
                optimizationY();
            }
            if (dy != 0) {
                optimizationX();
            }
        }
        if (map.player.isDead || isDead) {
            dx = 0;
            dy = 0;
        }
        x += dx;
        y += dy;
    }

    public int getTimeRemove() {
        return timeAfterDie;
    }

    @Override
    public boolean collied(Entity entity) {
        int x1 = x + dx;
        int y1 = y + dy;
        int x2 = entity.getX();
        int y2 = entity.getY();

        return (x1 + Sprite.SCALED_SIZE - 2 > x2) && (x2 + Sprite.SCALED_SIZE > x1 + 2)
                && (y1 + Sprite.SCALED_SIZE - 2 > y2) && (y2 + Sprite.SCALED_SIZE > y1 + 2);
    }

    public boolean colliedBomb() {
        for (Entity entity : map.getBombs()) {
            if (collied(entity)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCollision2() {
        int xx = x + dx;
        int yy = y + dy;
        int barrierX1;
        int barrierX2;
        int barrierY1;
        int barrierY2;
        Entity barrier1 = null;
        Entity barrier2 = null;
        if (dx > 0) {
            barrierX1 = (int) ((xx + (int) img.getWidth() - 0.5) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE;
            barrierX2 = barrierX1;
            barrierY1 = (yy + OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierY2 = (yy+ (int) img.getHeight() - OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrier1 = map.getBarrierAt(barrierX1, barrierY1);
            barrier2 = map.getBarrierAt(barrierX2, barrierY2);
        } else if (dx < 0) {
            barrierX1 = (int) ((this.x + dx + 0.5) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE;
            barrierX2 = barrierX1;
            barrierY1 = (this.y + OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierY2 = (this.y + (int) img.getHeight() - OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrier1 = map.getBarrierAt(barrierX1, barrierY1);
            barrier2 = map.getBarrierAt(barrierX2, barrierY2);
        }
        if (dy > 0) {
            barrierY1 = (int) ((this.y + (int) img.getHeight() + dy - 0.5) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE;
            barrierY2 = barrierY1;
            barrierX1 = (this.x + OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierX2 = (this.x + (int) img.getWidth() - OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrier1 = map.getBarrierAt(barrierX1, barrierY1);
            barrier2 = map.getBarrierAt(barrierX2, barrierY2);
        } else if (dy < 0) {
            barrierY1 = (int) ((this.y + dy + 0.5) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE;
            barrierY2 = barrierY1;
            barrierX1 = (this.x + OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrierX2 = (this.x + (int) img.getWidth() - OPTIMIZATION_DISTANCE) / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
            barrier1 = map.getBarrierAt(barrierX1, barrierY1);
            barrier2 = map.getBarrierAt(barrierX2, barrierY2);
        }

        if (barrier1 != null) {
            if (barrier1.collied(this) && ((barrier1 instanceof Wall) || (barrier1 instanceof Brick && !wallPass))) {
                return true;
            }
        }
        if (barrier2 != null) {
            return barrier2.collied(this) && ((barrier2 instanceof Wall) || (barrier2 instanceof Brick && !wallPass));
        }
        return false;
    }

    public boolean isCollision() {
//        return super.isCollision() || colliedBomb();
        return isCollision2() || colliedBomb();
    }

    protected void randomMoveLow() {
        moving = true;
        if ((isCollision() || time == 0) && speed !=0 ) {
            time = 3 * rand.nextInt(Sprite.SCALED_SIZE / speed) + 1;
            canMove = false;
            int i = 0;
            while (!canMove) {
                i++;
                calculateDirectionLow();
                canMove = canMove();
                if (i == 8) {
                    break;
                }
            }
        }
        time --;
    }

    protected void randomMoveNormal() {
        moving = true;
        if ((isCollision() || time == 0 || testNormal()) && speed !=0 ) {
            time = rand.nextInt(Sprite.SCALED_SIZE / speed) + 1;
            canMove = false;
            int i = 0;
            while (!canMove) {
                i++;
                calculateDirectionNormal();
                if (i == 8) {
                    calculateDirectionLow();
                    canMove = canMove();
                    break;
                }
                canMove = canMove();
            }
        }
        time --;
    }

    protected void randomMoveHigh() {
        int n = calculateDirectionHigh();
        if (n == 0) {
            dx = 0;
            dy = -speed;
        } else if (n == 1) {
            dx = speed;
            dy = 0;
        } else if (n == 2) {
            dx = 0;
            dy = speed;
        } else if (n == 3) {
            dx = -speed;
            dy = 0;
        } else if (n == -1) {
            randomMoveLow();
        }
        canMove = canMove();
    }

    private void calculateDirectionLow() {
        int n =  rand.nextInt(4);
        if (n == 0) {
            dx = 0;
            dy = -speed;
        } else if (n == 1) {
            dx = speed;
            dy = 0;
        } else if (n == 2) {
            dx = 0;
            dy = speed;
        } else {
            dx = -speed;
            dy = 0;
        }
    }

    private boolean testNormal() {
        int xx = dx;
        int yy = dy;
        if (map.getPlayer().getX() == getX()) {
            if (map.getPlayer().getY() < getY()) {
                dx = 0;
                dy = -speed;
                if (isCollision()) {
                    dx = xx;
                    dy = yy;
                    return false;
                }
                return true;
            } else {
                dx = 0;
                dy = speed;
                if (isCollision()) {
                    dx = xx;
                    dy = yy;
                    return false;
                }
                return true;
            }
        }
        if (map.getPlayer().getY() == getY()) {
            if (map.getPlayer().getX() < getX()) {
                dx = -speed;
            } else {
                dx = speed;
            }
            dy = 0;
            if (isCollision()) {
                dx = xx;
                dy = yy;
                return false;
            }
            return true;
        }
        return false;
    }

    private void calculateDirectionNormal() {
        if (map.getPlayer().getX() == getX()) {
            if (map.getPlayer().getY() < getY()) {
                dx = 0;
                dy = -speed;
                if (isCollision()) {
                    calculateDirectionLow();
                }
            } else {
                dx = 0;
                dy = speed;
                if (isCollision()) {
                    calculateDirectionLow();
                }
            }
            return;
        }
        if (map.getPlayer().getY() == getY()) {
            if (map.getPlayer().getX() < getX()) {
                dx = -speed;
            } else {
                dx = speed;
            }
            dy = 0;
            if (isCollision()) {
                calculateDirectionLow();
            }
            return;
        }
        calculateDirectionLow();
    }

    private boolean cellPlayer() {
        if (cellI[map.getPlayer().getYTile()][map.getPlayer().getXTile()] == null
                && cellJ[map.getPlayer().getYTile()][map.getPlayer().getXTile()] == null) {
            return false;
        }
        return !cellI[map.getPlayer().getYTile()][map.getPlayer().getXTile()].isEmpty()
                && !cellJ[map.getPlayer().getYTile()][map.getPlayer().getXTile()].isEmpty();
    }


    private int calculateDirectionHigh() {
        cellI = new ArrayList[map.getHeight()][map.getWidth()];
        cellJ = new ArrayList[map.getHeight()][map.getWidth()];
        boolean[][] kt = new boolean[map.getHeight()][map.getWidth()];
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                if (map.object[i][j] == '\0' || (map.object[i][j] == '*' && wallPass)) {
                    kt[i][j] = true;
                }
            }
        }
        for (Bomb bomb : map.getBombs()) {
            kt[bomb.getYTile()][bomb.getXTile()] = false;
        }

        ArrayList<Integer> aI = new ArrayList<>();
        ArrayList<Integer> aJ = new ArrayList<>();
        int aa = getYTile();
        int bb = getXTile();
        kt[aa][bb] = false;
        aI.add(aa);
        aJ.add(bb);
        cellI[aa][bb] = new ArrayList<Integer>();
        cellJ[aa][bb] = new ArrayList<Integer>();
        cellI[aa][bb].add(aa);
        cellJ[aa][bb].add(bb);
        do {
            ArrayList<Integer> bI = new ArrayList<>();
            ArrayList<Integer> bJ = new ArrayList<>();
            for (int k = 0; k < aI.size(); k++) {
                int i = aI.get(k);
                int j = aJ.get(k);
                if (kt[i - 1][j]) {
                    cellI[i - 1][j] = new ArrayList<Integer>();
                    cellJ[i - 1][j] = new ArrayList<Integer>();
                    cellI[i - 1][j].addAll(cellI[i][j]);
                    cellJ[i - 1][j].addAll(cellJ[i][j]);

                    cellI[i - 1][j].add(i - 1);
                    cellJ[i - 1][j].add(j);
                    kt[i - 1][j] = false;
                    bI.add(i - 1);
                    bJ.add(j);
                }
                if (kt[i][j + 1]) {
                    cellI[i][j + 1] = new ArrayList<Integer>();
                    cellJ[i][j + 1] = new ArrayList<Integer>();
                    cellI[i][j + 1].addAll(cellI[i][j]);
                    cellJ[i][j + 1].addAll(cellJ[i][j]);

                    cellI[i][j + 1].add(i);
                    cellJ[i][j + 1].add(j + 1);
                    kt[i][j + 1] = false;
                    bI.add(i);
                    bJ.add(j + 1);
                }
                if (kt[i + 1][j]) {
                    cellI[i + 1][j] = new ArrayList<Integer>();
                    cellJ[i + 1][j] = new ArrayList<Integer>();
                    cellI[i + 1][j].addAll(cellI[i][j]);
                    cellJ[i + 1][j].addAll(cellJ[i][j]);

                    cellI[i + 1][j].add(i + 1);
                    cellJ[i + 1][j].add(j);
                    kt[i + 1][j] = false;
                    bI.add(i + 1);
                    bJ.add(j);
                }
                if (kt[i][j - 1]) {
                    cellI[i][j - 1] = new ArrayList<Integer>();
                    cellJ[i][j - 1] = new ArrayList<Integer>();
                    cellI[i][j - 1].addAll(cellI[i][j]);
                    cellJ[i][j - 1].addAll(cellJ[i][j]);

                    cellI[i][j - 1].add(i);
                    cellJ[i][j - 1].add(j - 1);
                    kt[i][j - 1] = false;
                    bI.add(i);
                    bJ.add(j - 1);
                }

            }
            if (cellPlayer()) {
                break;
            }
            aI = bI;
            aJ = bJ;
        } while (!aI.isEmpty());

        if (cellPlayer()) {
            if (cellI[map.getPlayer().getYTile()][map.getPlayer().getXTile()].size() == 1
                    || cellI[map.getPlayer().getYTile()][map.getPlayer().getXTile()].size() == 2) {
                if (map.getPlayer().getX() < getX()) {
                    dx = -speed;
                    dy = 0;
                    if (canMove())
                    return 3;
                }
                if (map.getPlayer().getX() > getX()) {
                    dx = speed;
                    dy = 0;
                    if (canMove())
                    return 1;
                }
                if (map.getPlayer().getY() < getY()) {
                    dx = 0;
                    dy = -speed;
                    if (canMove())
                    return 0;
                }
                if (map.getPlayer().getY() > getY()) {
                    dx = 0;
                    dy = speed;
                    if (canMove())
                    return 2;
                }
            } else for (int k = 0; k < cellI[map.getPlayer().getYTile()][map.getPlayer().getXTile()].size() - 1; k++) {
                int x1 = (Integer) cellJ[map.getPlayer().getYTile()][map.getPlayer().getXTile()].get(k)
                        * Sprite.SCALED_SIZE;
                int y1 = (Integer) cellI[map.getPlayer().getYTile()][map.getPlayer().getXTile()].get(k)
                        * Sprite.SCALED_SIZE;

                int x2 = (Integer) cellJ[map.getPlayer().getYTile()][map.getPlayer().getXTile()].get(k + 1)
                        * Sprite.SCALED_SIZE;
                int y2 = (Integer) cellI[map.getPlayer().getYTile()][map.getPlayer().getXTile()].get(k + 1)
                        * Sprite.SCALED_SIZE;
                if (x1 < getX() && !(x1 < x2)) {
                    return 3;
                }
                if (x1 > getX() && !(x1 > x2)) {
                    return 1;
                }
                if (y1 < getY() && !(y1 < y2)) {
                    return 0;
                }
                if (y1 > getY() && !(y1 > y2)) {
                    return 2;
                }
            }
        }

        return -1;
    }
}
