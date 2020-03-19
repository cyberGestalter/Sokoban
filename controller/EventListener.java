package com.javarush.task.task34.task3410.controller;

import com.javarush.task.task34.task3410.model.Direction;

//интерфейс слушателя событий
//Его должен реализовывать каждый класс, который хочет обрабатывать события.
//А классы, которые будут генерировать события, будут вызывать методы этого интерфейса.
public interface EventListener {
    //передвинуть объект в определенном направлении
    void move(Direction direction);
    //начать заново текущий уровень
    void restart();
    //начать следующий уровень
    void startNextLevel();
    //уровень с номером level завершён
    void levelCompleted(int level);
}
