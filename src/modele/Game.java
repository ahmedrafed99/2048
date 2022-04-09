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

                tabCells[i][j] = new Cell();
                this.cells.put(tabCells[i][j], new Point(j, i));
            }
        }

        for (int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){
                tabCells[i][j].setGame(this);
            }
        }


        //rnd();
    }

    public void updateCell(int value, Point point){
        if (point.x >= getSize() | point.y >= getSize() | point.x < 0 | point.y < 0 ) {
            throw new IllegalArgumentException("Point must have coordinates inside the game board");
        }
        getCell(point.x, point.y).setValue(value);
        cells.put(getCell(point.x, point.y), point);
    }

    public int getSize() {
        return tabCells.length;
    }

    public Cell getCell(int i, int j) {
        return tabCells[i][j];
    }

    public void move(Direction direction){
        switch (direction) {
            case up:
                for (int y = 0; y<getSize(); y++){
                    for (int x = 0; x<getSize(); x++){
                        Cell cell = getCell(x, y);
                        if(cell.getValue() != NULL) {
                            cell.shift(Direction.up);
                        }

                    }
                }

                break;

            case down:
                for (int y = 0; y<getSize(); y++){
                    for (int x = getSize()-1; x>=0; x--){
                        Cell cell = getCell(x, y);
                        if(cell.getValue() != NULL) {
                            cell.shift(Direction.down);
                        }
                    }
                }

                break;

            case right:
                for (int x = 0; x<getSize(); x++){
                    for (int y = getSize()-1; y>= 0; y--){
                        Cell cell = getCell(x, y);
                        if (cell.getValue() != NULL) {
                            cell.shift(Direction.right);
                        }

                    }
                }

                break;

            case left:
                for (int x = 0; x<getSize(); x++){
                    for (int y = 0; y<getSize(); y++){
                        Cell cell = getCell(x, y);
                        if (cell.getValue() != NULL) {
                            cell.shift(Direction.left);
                        }
                    }
                }

                break;
        }
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
                                updateCell(2, new Point(x, y));
                                break;
                            case 1:
                                updateCell(4, new Point(x, y));
                                break;
                        }
            }

        }.start();


        setChanged();
        notifyObservers();


    }

    public String toString() {
        String boardTitle = "Board" + this.getClass().getName() + "\n"
                + "\t" + "(" + getSize() + "," + getSize() + ")" + "\n";

        StringBuilder stringBuilder = new StringBuilder(boardTitle);
        for (int i=0; i<getSize(); i++) {
            stringBuilder.append("  | ");
            for (int j=0; j<getSize(); j++){
                stringBuilder.append(tabCells[i][j] + " ");
            }
            stringBuilder.append("|" + "\n");
        }
        return stringBuilder.toString();
    }

    public HashMap<Cell, Point> getCells(){
        return this.cells;
    }

    public Cell[][] getTabCells(){
        return tabCells;
    }

}
