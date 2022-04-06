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


        rnd();
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
                                tabCells[x][y] = new Cell(2);
                                break;
                            case 1:
                                tabCells[x][y] = new Cell(4);
                                break;
                        }
            }

        }.start();


        setChanged();
        notifyObservers();


    }

}
