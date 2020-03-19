package com.javarush.task.task34.task3410.model;

import com.javarush.task.task34.task3410.controller.EventListener;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

//модель игры
public class Model {
    //размер ячейки игрового поля
    public static final int FIELD_CELL_SIZE = 20;
    private EventListener eventListener;
    //хранит игровые объекты
    private GameObjects gameObjects;
    //текущий уровень
    private int currentLevel = 1;
    //загрузчик уровней
    private LevelLoader levelLoader = new LevelLoader(Paths.get(Model.class.getPackage().toString().substring(0, Model.class.getPackage().toString().lastIndexOf(".")).replace(".", "/")+"/res/levels.txt"));
    //возвращает все игровые объекты
    public GameObjects getGameObjects() {
        return gameObjects;
    }

    //получает новые игровые объекты для указанного уровня у загрузчика уровня levelLoader и сохраняет их в поле gameObjects
    public void restartLevel(int level){
        try {
            gameObjects = levelLoader.getLevel(level);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //перезапускает текущий уровень, вызывая restartLevel с нужным параметром
    public void restart(){
        restartLevel(currentLevel);
    }
    //увеличивает значение переменной currentLevel, хранящей номер текущего уровня, и выполнять перезапуск нового уровня
    public void startNextLevel(){
        currentLevel++;
        restart();
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }


    //Двигает игрока
    public void move(Direction direction) {
        if (checkWallCollision(gameObjects.getPlayer(), direction)) return;
        if (checkBoxCollisionAndMoveIfAvaliable(direction)) return;
        switch (direction) {
            case UP: gameObjects.getPlayer().move(0, - Model.FIELD_CELL_SIZE);
                break;
            case DOWN: gameObjects.getPlayer().move(0, Model.FIELD_CELL_SIZE);
                break;
            case LEFT: gameObjects.getPlayer().move(- Model.FIELD_CELL_SIZE, 0);
                break;
            case RIGHT: gameObjects.getPlayer().move(Model.FIELD_CELL_SIZE, 0);
                break;
        }
        checkCompletion();
    }

    //проверяет столкновение со стеной
    public boolean checkWallCollision(CollisionObject gameObject, Direction direction) {
        Set<Wall> walls = gameObjects.getWalls();
        for (Wall wall : walls) {
            if (gameObject.isCollision(wall, direction)) return true;
        }
        return false;
    }

    //проверяет столкновение с ящиками
    //Возвращает true, если игрок не может быть сдвинут в направлении direction (там находится: или ящик, за которым стена; или ящик за которым еще один ящик).
    //Возвращает false, если игрок может быть сдвинут в направлении direction (там находится: или свободная ячейка; или дом; или ящик, за которым свободная ячейка или дом).
    //При этом, если на пути есть ящик, который может быть сдвинут, то необходимо переместить этот ящик на новые координаты.
    public boolean checkBoxCollisionAndMoveIfAvaliable(Direction direction) {
        Player player = gameObjects.getPlayer();
        GameObject stopped = null;
        //выбрать НЕ игрока и НЕ дом (т.е. стену или ящик), который при движении игрока в направлении direction будет приводить
        // к пространственной коллизии
        for (GameObject gameObject : gameObjects.getAll()) {
            if (!(gameObject instanceof Player) && !(gameObject instanceof Home) && player.isCollision(gameObject, direction))
                stopped = gameObject;
        }

        if (stopped == null) return false;

        if (stopped instanceof Box) {
            Box stoppedBox = (Box) stopped;
            if (checkWallCollision(stoppedBox, direction)) return true;
            for (Box box : gameObjects.getBoxes()) {
                if (stoppedBox.isCollision(box, direction)) return true;
            }
            switch (direction) {
                case LEFT:
                    stoppedBox.move(-FIELD_CELL_SIZE, 0);
                    break;
                case RIGHT:
                    stoppedBox.move(FIELD_CELL_SIZE, 0);
                    break;
                case UP:
                    stoppedBox.move(0, -FIELD_CELL_SIZE);
                    break;
                case DOWN:
                    stoppedBox.move(0, FIELD_CELL_SIZE);
            }
        }
        return false;
    }

    //проверяет, пройден ли уровень
    //Условие завершения - на всех ли домах стоят ящики, их координаты должны совпадать
    public void checkCompletion(){
        Set<Home> homes = gameObjects.getHomes();
        Set<Box> boxes = gameObjects.getBoxes();
        int n = 0;
        for (Home home : homes) {
            for (Box box : boxes) {
                if (box.getX() == home.getX() && box.getY() == home.getY()) {
                    n++;
                }
            }
        }
        if (n == homes.size()) {
            eventListener.levelCompleted(currentLevel);
        }
    }
}