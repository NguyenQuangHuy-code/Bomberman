package Game;

import Entities.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import Entities.Bomb.Bomb;
import Entities.Character.Enemy.*;
import Entities.Character.Bomber;
import Entities.Character.Character;
import Entities.Items.*;
import Graphics.Sprite;
import Sounds.Sound;
import jdk.internal.util.xml.impl.Input;

public class Map {
    private int level;
    private int width;
    private int height;
    private String _path;
    public Bomber player;
    public char[][] object;
    private ArrayList<Entity> fixedObjects = new ArrayList<>();
    private ArrayList<Entity> itemObject = new ArrayList<>();
    private ArrayList<Entity> barrierObjects = new ArrayList<>();
    private ArrayList<Character> entities = new ArrayList<>();
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private Sound sound = new Sound();
    private int timeInSeconds = 120;
    private int time = 30 * timeInSeconds;


    public int MAX_MAP_X;
    public int MAX_MAP_Y;

    public Map() {

    }

    public Map(int level) {
        this.level = level;
        _path = String.format("./res/levels/Level%d.txt", level);
        load();
    }

    private void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(_path));
            String[] info = reader.readLine().split(" ");
            height = Integer.parseInt(info[1]);
            width = Integer.parseInt(info[2]);
            object = new char[height][width];
            MAX_MAP_X = width * Sprite.SCALED_SIZE;
            MAX_MAP_Y = height * Sprite.SCALED_SIZE;
            char[] sign;
            for (int i = 0; i < height; ++i) {
                sign = reader.readLine().toCharArray();
                for (int y = 0; y < width; ++y) {
                    Entity object;
                    if (sign[y] == '#') {
                        object = new Wall(y, i, Sprite.wall.getFxImage(), this);
                        fixedObjects.add(object);
                        barrierObjects.add(object);
                        this.object[i][y] = '#';
                    } else {
                        object =  new Grass(y, i, Sprite.grass.getFxImage(), this);
                        fixedObjects.add(object);
                    }

                    if (sign[y] == '*') {
                        object = new Brick(y, i, Sprite.brick.getFxImage(), this);
                        barrierObjects.add(object);
                        this.object[i][y] = '*';
                    } else if (sign[y] == '1') {
                        entities.add(new Balloom(y, i, Sprite.balloom_left1.getFxImage(), this));
                    } else if (sign[y] == '2') {
                        entities.add(new Oneal(y, i, Sprite.oneal_left1.getFxImage(), this));
                    } else if (sign[y] == '3') {
                        entities.add((new Doll(y, i, Sprite.doll_left1.getFxImage(), this)));
                    } else if (sign[y] == '4') {
                        entities.add(new Minvo(y, i, Sprite.minvo_left1.getFxImage(), this));
                    } else if (sign[y] == '5') {
                        entities.add(new Kondoria(y, i, Sprite.kondoria_left1.getFxImage(), this));
                    } else if (sign[y] == '6') {
                        entities.add(new Ovapi(y, i, Sprite.ovapi_left1.getFxImage(), this));
                    } else if (sign[y] == '7') {
                        entities.add(new Pass(y, i, Sprite.pass_left1.getFxImage(), this));
                    } else if (sign[y] == '8') {
                        entities.add(new Pontan(y, i, Sprite.pontan_left1.getFxImage(), this));
                    } else if (sign[y] == 'x') {
                        itemObject.add(new Portal(y, i, Sprite.portal.getFxImage(), this));
                        barrierObjects.add(new Brick(y, i, Sprite.brick.getFxImage(), this));
                        this.object[i][y] = '*';
                    } else if (sign[y] == 'b') {
                        itemObject.add(new BombItem(y, i, Sprite.powerup_bombs.getFxImage(), this));
                        this.object[i][y] = '*';
                        barrierObjects.add(new Brick(y, i, Sprite.brick.getFxImage(), this));
                    } else if (sign[y] == 'f') {
                        itemObject.add(new FlameItem(y, i, Sprite.powerup_flames.getFxImage(), this));
                        barrierObjects.add(new Brick(y, i, Sprite.brick.getFxImage(), this));
                        this.object[i][y] = '*';
                    } else if (sign[y] == 's') {
                        itemObject.add(new SpeedItem(y, i, Sprite.powerup_speed.getFxImage(), this));
                        barrierObjects.add(new Brick(y, i, Sprite.brick.getFxImage(), this));
                        this.object[i][y] = '*';
                    } else if (sign[y] == 'w') {
                        itemObject.add(new WallPassItem(y, i, Sprite.powerup_wallpass.getFxImage(), this));
                        barrierObjects.add(new Brick(y, i, Sprite.brick.getFxImage(), this));
                        this.object[i][y] = '*';
                    } else if (sign[y] == 'e') {
                        itemObject.add(new FlamePassItem(y, i, Sprite.powerup_flamepass.getFxImage(), this));
                        barrierObjects.add(new Brick(y, i, Sprite.brick.getFxImage(), this));
                        this.object[i][y] = '*';
                    } else if (sign[y] == 'm') {
                        itemObject.add(new BombPassItem(y, i, Sprite.powerup_bombpass.getFxImage(), this));
                        barrierObjects.add(new Brick(y, i, Sprite.brick.getFxImage(), this));
                        this.object[i][y] = '*';
                    } else if (sign[y] == 'd') {
                        itemObject.add(new DetonatorItem(y, i, Sprite.powerup_detonator.getFxImage(), this));
                        barrierObjects.add(new Brick(y, i, Sprite.brick.getFxImage(), this));
                        this.object[i][y] = '*';
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**get Level Map.*/
    public int getLevel() {
        return level;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<Character> getEntities() {
        return entities;
    }

    public ArrayList<Entity> getFixedObjects() {
        return fixedObjects;
    }

    public ArrayList<Entity> getItemObject() {
        return itemObject;
    }

    public ArrayList<Entity> getBarrierObjects() {
        return barrierObjects;
    }

    public Entity getBarrierAt(int x, int y) {
        for (Entity entity : barrierObjects) {
            if (x == entity.getX() && y == entity.getY()) {
                return entity;
            }
        }

        return null;
    }

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public Bomb getBombAt(int x, int y) {
        for (Bomb bomb : bombs) {
            if (bomb.getX() == x && bomb.getY() == y) {
                return bomb;
            }
        }
        return null;
    }

    public Bomber getPlayer() {
        return player;
    }

    public Entity getCharacterAt(int x, int y) {
        for (Entity entity : entities) {
            if (entity instanceof Character) {
                if (entity.getX() == x && entity.getY() == y) {
                    return entity;
                }
            }
        }
        return null;
    }

    public boolean hasEnemy() {
        for (Character entity : entities) {
            if (entity instanceof Enemy) {
                return true;
            }
        }
        return false;
    }

    public boolean hasBomber() {
        for (Character entity : entities) {
            if (entity instanceof Bomber) {
                return true;
            }
        }
        return false;
    }

    public void deleteMap() {
        fixedObjects.clear();
        barrierObjects.clear();
        entities.clear();
        bombs.clear();
        itemObject.clear();
    }

    public void update() {
        itemObject.removeIf(Entity::getRemove);
        bombs.removeIf(Entity::getRemove);

        for (int i = 0; i < entities.size(); ++i) {
            if (entities.get(i).removeFromMap()) {
                if (entities.get(i) instanceof Enemy) {
                    player.addScore((Enemy) entities.get(i));
                }
                entities.remove(i);
            }
        }

        for (int i = 0; i < barrierObjects.size(); ++i) {
            if (barrierObjects.get(i) instanceof Brick) {
                if (((Brick) barrierObjects.get(i)).removeFromMap()) {
                    Entity entity = barrierObjects.get(i);
                    object[entity.getYTile()][entity.getXTile()] = '\0';
                    barrierObjects.remove(i);
                }
            }
        }

        if (!player.isDead) {
            for (Entity entity : getEntities()) {
                if (entity instanceof Enemy) {
                    if (player.collied(entity) && ((Enemy) entity).collied(player) && ((Enemy) entity).getTimeRemove() == 90) {
                        player.isDead = true;
                        break;
                    }
                }
            }
        }
    }

}
