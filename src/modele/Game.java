package modele;

import java.util.Observable;
import java.util.Random;

public class Game extends Observable {

    private Cell[][] tabCells;
    private static Random rnd = new Random(4);

    public Game(int size) {
        tabCells = new Cell[size][size];
        rnd();
    }

    public int getSize() {
        return tabCells.length;
    }

    public Cell getCell(int i, int j) {
        return tabCells[i][j];
    }


    public void rnd() {
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
                int r;

                for (int i = 0; i < tabCells.length; i++) {
                    for (int j = 0; j < tabCells.length; j++) {
                        r = rnd.nextInt(3);

                        switch (r) {
                            case 0:
                                tabCells[i][j] = null;
                                break;
                            case 1:
                                tabCells[i][j] = new Cell(2);
                                break;
                            case 2:
                                tabCells[i][j] = new Cell(4);
                                break;
                        }
                    }
                }
            }

        }.start();


        setChanged();
        notifyObservers();


    }

}
