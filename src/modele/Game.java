package modele;

import java.awt.*;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

import static java.sql.Types.NULL;

public class Game extends Observable {

    private HashMap<Cell, Point> cells;
    private Cell[][] tabCells;
    private static Random rnd = new Random(4);

    public Game(int size) {
        this.tabCells = new Cell[size][size];

        this.cells = new HashMap<>();

        for (int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){
                this.cells.put(tabCells[i][j], new Point(i, j));
                tabCells[i][j] = new Cell();
            }
        }

        for (int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){
                tabCells[i][j].setGame(this);
            }
        }


        rnd();
    }

    public void updateCell(Cell cell, Point point){
        tabCells[point.x][point.y] = cell;
        cells.put(cell, point);
    }

    public int getSize() {
        return tabCells.length;
    }

    public Cell getCell(int i, int j) {
        return tabCells[i][j];
    }

    public void move(Direction direction){

    }

    public void merge(){

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
                                updateCell(new Cell(2), new Point(x, y));
                                break;
                            case 1:
                                updateCell(new Cell(4), new Point(x, y));
                                break;
                        }
            }

        }.start();


        setChanged();
        notifyObservers();


    }

    public HashMap<Cell, Point> getCells(){
        return this.cells;
    }

}
