package modele;

import java.awt.*;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

import static java.sql.Types.NULL;
import static modele.Direction.*;

public class Game extends Observable {

    private HashMap<Cell, Point> cells;
    private Cell[][] tabCells;
    private static Random rnd = new Random(4);
    private Point[][] tabPoints;

    public Game(int size) {
        this.tabCells = new Cell[size][size];
        this.cells = new HashMap<>();
        this.tabPoints = new Point[size][size];

        for (int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){
                tabPoints[i][j]= new Point(i, j);
            }
        }

        for (int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){
                this.cells.put(tabCells[i][j], tabPoints[i][j]);
                tabCells[i][j] = new Cell();
            }
        }

        for (int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){
                tabCells[i][j].setGame(this);
            }
        }

        rnd();
        rnd();
    }

    public void updateCell(Cell cell, Point point){
        tabCells[point.x][point.y] = cell;
        cells.put(cell, point);
        tabCells[point.x][point.y].setGame(this);
    }

    public int getSize() {
        return tabCells.length;
    }

    public Cell getCell(int i, int j) {
        return tabCells[i][j];
    }

    public void move(Direction direction) {
        switch (direction) {
            case up:
                for (int j = 0; j < getSize(); j++) {
                    for (int i = 1; i < getSize(); i++) {
                        if (tabCells[i][j].getValue() != NULL) {
                            tabCells[i][j].shift(up);
                        }
                    }
                }
                rnd();
                break;
            case down:
                for (int j = 0; j < getSize(); j++) {
                    for (int i = getSize() - 2; i >= 0; i--) {
                        if (tabCells[i][j].getValue() != NULL) {
                            tabCells[i][j].shift(down);
                        }
                    }
                }
                rnd();
                break;
            case left:
                for (int i = 0; i < getSize(); i++) {
                    for (int j = 1; j < getSize(); j++) {
                        if (tabCells[i][j].getValue() != NULL) {
                            tabCells[i][j].shift(left);
                        }
                    }
                }
                rnd();
                break;
            case right:
                for (int i = 0; i < getSize(); i++) {
                    for (int j = getSize() - 2; j >= 0; j--) {
                        if (tabCells[i][j].getValue() != NULL) {
                            tabCells[i][j].shift(right);
                        }
                    }
                }
                rnd();
                break;
        }
        setChanged();
        notifyObservers();
    }



    public void rnd() {
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
                int r, x, y;


                x = rnd.nextInt(4);
                y = rnd.nextInt(4);
                while(tabCells[x][y].getValue() != NULL) {
                    x = rnd.nextInt(4);
                    y = rnd.nextInt(4);
                }
                r = rnd.nextInt(2);


                        switch (r) {
                            case 0:
                                updateCell(new Cell(2), tabPoints[x][y]);
                                break;
                            case 1:
                                updateCell(new Cell(4), tabPoints[x][y]);
                                break;
                        }
            }

        }.start();


        setChanged();
        notifyObservers();


    }

    public Point getPoint(int x, int y) {
        return tabPoints[x][y];
    }

    public HashMap<Cell, Point> getCells(){
        return this.cells;
    }

}
