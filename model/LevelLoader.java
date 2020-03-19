package com.javarush.task.task34.task3410.model;

import java.io.*;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

//загрузчик уровней
public class LevelLoader {
    private Path levels;

    public LevelLoader(Path levels) {
        //Параметр levels - это путь к тестовому файлу, содержащему описание уровней
        this.levels = levels;
    }

    //возвращает все игровые объекты
    public GameObjects getLevel(int level) throws IOException {
        if (level > 60) level -= 60;
        Set<Wall> walls = new HashSet<>();
        Set<Box> boxes = new HashSet<>();
        Set<Home> homes = new HashSet<>();
        Player player = null;
        FileInputStream fis = new FileInputStream(new File(String.valueOf(levels)));
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        //Выбор уровня
        //Чтение останавливается на строке с "Maze : ..."
        while (true) {
            String maze = br.readLine();
            if (maze.contains("Maze")) {
                int levelMaze = Integer.parseInt(maze.substring(maze.lastIndexOf(":")+2));
                if (levelMaze == level) break;
            }
        }
        String gameFieldStrings = "";
        //Перевод курсора чтения на строку перед игровым полем
        for (int i = 0; i < 6; i++) br.readLine();
        int j = -1; //указатель номера считываемой строки для расчета вертикальных координат объектов
        while (true) {
            gameFieldStrings = br.readLine();
            if (gameFieldStrings.contains("*************************************")) break;
            j++;
            for (int i = 0; i < gameFieldStrings.length(); i++) {
                char fieldSymbol = gameFieldStrings.charAt(i);
                switch (fieldSymbol) {
                    case 'X' :  Wall wall = new Wall(Model.FIELD_CELL_SIZE/2 + i*Model.FIELD_CELL_SIZE, Model.FIELD_CELL_SIZE/2 + j*Model.FIELD_CELL_SIZE);
                                walls.add(wall);
                                break;
                    case '*' :  Box box = new Box(Model.FIELD_CELL_SIZE/2 + i*Model.FIELD_CELL_SIZE, Model.FIELD_CELL_SIZE/2 + j*Model.FIELD_CELL_SIZE);
                                boxes.add(box);
                                break;
                    case '.' :  Home home = new Home(Model.FIELD_CELL_SIZE/2 + i*Model.FIELD_CELL_SIZE, Model.FIELD_CELL_SIZE/2 + j*Model.FIELD_CELL_SIZE);
                                homes.add(home);
                                break;
                    case '&' :  Box boxInHome = new Box(Model.FIELD_CELL_SIZE/2 + i*Model.FIELD_CELL_SIZE, Model.FIELD_CELL_SIZE/2 + j*Model.FIELD_CELL_SIZE);
                                boxes.add(boxInHome);
                                Home homeWithBox = new Home(Model.FIELD_CELL_SIZE/2 + i*Model.FIELD_CELL_SIZE, Model.FIELD_CELL_SIZE/2 + j*Model.FIELD_CELL_SIZE);
                                homes.add(homeWithBox);
                                break;
                    case '@' :  player = new Player(Model.FIELD_CELL_SIZE/2 + i*Model.FIELD_CELL_SIZE, Model.FIELD_CELL_SIZE/2 + j*Model.FIELD_CELL_SIZE);
                                break;
                }
            }
        }
        br.close();
        return new GameObjects(walls, boxes, homes, player);
    }
}

