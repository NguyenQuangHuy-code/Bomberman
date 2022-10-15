package Entities.Character;

import Entities.Bomb.Bomb;
import Entities.Character.Enemy.Enemy;
import Entities.Entity;
import Entities.Items.Item;
import Graphics.Sprite;
import Inputs.KeyboardListener;
import Sounds.Sound;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import Game.Map;

public class Bomber extends Character {

    private final ArrayList<Bomb> bombs = new ArrayList<>();
    private final ArrayList<Item> items = new ArrayList<>();
    private static final int SPEED = 2;
    private int bombNumber = 1;
    private int currentBombNumber = bombNumber;
    private static int radius = 1;
    private final KeyboardListener input;
    private boolean moving;
    private final Sprite frameLeft = Sprite.player_left;
    private final Sprite frameLeft1 = Sprite.player_left_1;
    private final Sprite frameLeft2 = Sprite.player_left_2;
    private final Sprite frameRight = Sprite.player_right;
    private final Sprite frameRight1 = Sprite.player_right_1;
    private final Sprite frameRight2 = Sprite.player_right_2;
    private final Sprite frameDown = Sprite.player_down;
    private final Sprite frameDown1 = Sprite.player_down_1;
    private final Sprite frameDown2 = Sprite.player_down_2;
    private final Sprite frameUp = Sprite.player_up;
    private final Sprite frameUp1 = Sprite.player_up_1;
    private final Sprite frameUp2 = Sprite.player_up_2;
    private final Sprite frameDead1 = Sprite.player_dead1;
    private final Sprite frameDead2 = Sprite.player_dead2;
    private final Sprite frameDead3 = Sprite.player_dead3;
    private static final int timePerFramePlayer = 20;
    private int timePerDropBomb = 0;
    public boolean win = false;
    private final Sound sound = new Sound();
    private int stepTime = 15;
    private int level = 0;
    private int score = 0;
    private boolean bombPass;

    
    public Bomber(int xUnit, int yUnit, Image img, KeyboardListener input, Map map) {
        super(xUnit, yUnit, img, SPEED, map);
        this.input = input;
        canDead = true;
        OPTIMIZATION_DISTANCE =  Sprite.SCALED_SIZE/2 -2;
        isDead = false;
        wallPass = false;
        bombPass = super.bombPass;
        flamePass = false;
        detonator = false;
    }

    @Override
    public void update() {
        input.addListeners();

        if (!super.bombPass) {
            int kt = 0;
            for (Entity entity : map.getBombs()) {
                if (collied(entity)) {
                    bombPass = true;
                    kt = 1;
                    break;
                }
            }
            if (kt == 0) {
                bombPass = super.bombPass;
            }
        }

        move();
        if (timePerDropBomb <= -1000) {
            timePerDropBomb = 0;
        } else {
            timePerDropBomb--;
        }
        animate();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (dx > 0 ) {
            img = Sprite.movingSprite(frameRight, frameRight1, frameRight2, animate, timePerFramePlayer).getFxImage();
        } else if (dx < 0){
            img = Sprite.movingSprite(frameLeft, frameLeft1, frameLeft2, animate, timePerFramePlayer).getFxImage();
        }
        if (dy < 0) {
            img = Sprite.movingSprite(frameUp, frameUp1, frameUp2, animate, timePerFramePlayer).getFxImage();
        } else if (dy > 0) {
            img = Sprite.movingSprite(frameDown, frameDown1, frameDown2, animate, timePerFramePlayer).getFxImage();
        }

        if (dx == 0 && dy == 0) {
            img = frameDown.getFxImage();
        } else {
            if (stepTime == 0 && !isDead) {
                sound.setFile(3);
                sound.play();
                stepTime = 15;
            } else {
                stepTime--;
            }
        }
        if (isDead) {
            if (timeAfterDie == 90) {
                sound.setFile(1);
                sound.play();
            }
            if (timeAfterDie > 0) {
                timeAfterDie--;
                img = Sprite.movingSprite(frameDead1, frameDead2, frameDead3, animate, 120).getFxImage();
            }
        }
        gc.drawImage(img, x, y);
    }

    @Override
    public boolean collied(Entity other) {
        int x1 = x ;
        int y1 = y ;
        int x2 = other.getX();
        int y2 = other.getY();

        return (x1 + Sprite.SCALED_SIZE - 5 >= x2) && (x2 + Sprite.SCALED_SIZE >= x1 + 5)
                && (y1 + Sprite.SCALED_SIZE - 5 >= y2) && (y2 + Sprite.SCALED_SIZE >= y1 + 5);
    }

    public boolean colliedBomb() {
        if (bombPass) {
            return false;
        }
        for (Entity entity : map.getBombs()) {
            if (entity.collied(this)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCollision() {
        return super.isCollision() || colliedBomb();
    }

    public void move() {
        processInput();
        if (canMove()) {
            if (dx != 0) {
                optimizationY();
            } else if (dy != 0){
                optimizationX();
            }
            x += dx;
            y += dy;
        }
    }
    public void stopMove() {
        dx = 0;
        dy = 0;
    }


    public void processInput() {
        if (input.isMoveDown()) {
            dy = speed;
        } else if (input.isMoveUp()) {
            dy = -speed;
        } else {
            dy = 0;
        }
        if (input.isMoveLeft()) {
            dx = -speed;
        } else if (input.isMoveRight()) {
            dx = speed;
        } else {
            dx  = 0;
        }

        dropBomb();

        if (input.isDetonated() && detonator) {
            bombActivate = false;
        }
    }

    private void dropBomb() {
        if (currentBombNumber > 0 && input.isDropBomb() && timePerDropBomb < 0) {
            int bombX = (this.x + (int) img.getWidth()/2) / Sprite.SCALED_SIZE;
            int bombY = (this.y + (int) img.getHeight()/2) / Sprite.SCALED_SIZE;
            Bomb bomb = new Bomb(bombX, bombY, Sprite.bomb.getFxImage(), this);

            if (map.object[bombY][bombX] != '\0') {
                return;
            }
            for (Character character : map.getEntities()) {
                if (character instanceof Enemy) {
                    if (character.collied(bomb)) {
                        return;
                    }
                }
            }

            map.addBomb(bomb);
            sound.setFile(4);
            sound.play();
            timePerDropBomb = 20;
            currentBombNumber--;
            setBombActivate(true);
        }
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public boolean canMove() {
        return !isCollision() && !isDead;
    }

    public void addBombNumber() {
        bombNumber++;
        currentBombNumber++;
    }

    public int getRadius() {
        return radius;
    }

    public void increaseRadius() {
        radius++;
    }

    public void speedUP() {
        speed++;
    }

    public void setWallPass(boolean wallPass) {
        this.wallPass = wallPass;
    }

    public void setFlamePass(boolean flamePass) {
        this.flamePass = flamePass;
    }

    public void setBombPass(boolean bombPass) {
        super.bombPass = bombPass;
        this.bombPass = bombPass;
    }

    public void flamePass() {
        flamePass = true;
    }

    public void setDetonator(boolean detonator) {
        this.detonator = detonator;
    }

    public void setBombActivate(boolean bombActivate) {
        this.bombActivate = bombActivate;
    }



    public void recoveryBomb() {
        currentBombNumber++;
    }

    private void respawn() {
        this.x = Sprite.SCALED_SIZE;
        this.y = Sprite.SCALED_SIZE;
    }

    public void nextLevel(Map newMap) {
        this.map = newMap;
        respawn();
        map.player = this;
        map.getEntities().add(this);
        currentBombNumber = bombNumber;
    }

    public void increaseLevel() {
        this.level++;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public void addScore(Enemy enemy) {
        this.score += enemy.getPoint();
    }
}
