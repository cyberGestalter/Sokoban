package com.javarush.task.task34.task3410.model;

//Объекты игры, которые могут сталкиваться с другими объектами игры
public abstract class CollisionObject extends GameObject{
    public CollisionObject(int x, int y) {
        super(x, y);
    }

    //Столкнется ли текущий объект с gameObject, если отправится в направлении direction на FIELD_CELL_SIZE
    public boolean isCollision(GameObject gameObject, Direction direction) {
        if (gameObject == null || direction == null) return false;
        switch (direction) {
            case UP: if ((this.getY() - Model.FIELD_CELL_SIZE) == gameObject.getY() && this.getX() == gameObject.getX()) return true;
                break;
            case DOWN: if ((this.getY() + Model.FIELD_CELL_SIZE) == gameObject.getY() && this.getX() == gameObject.getX()) return true;
                break;
            case LEFT: if ((this.getX() - Model.FIELD_CELL_SIZE) == gameObject.getX() && this.getY() == gameObject.getY()) return true;
                break;
            case RIGHT: if ((this.getX() + Model.FIELD_CELL_SIZE) == gameObject.getX() && this.getY() == gameObject.getY()) return true;
                break;
        }
        return false;
    }
}
