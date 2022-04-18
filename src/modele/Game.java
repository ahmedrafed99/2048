package modele;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;

import static java.sql.Types.NULL;

public class Game extends Observable {

    private HashMap<Cell, Point> cells;
    private Cell[][] tabCells;
    private static Random rnd = new Random(4);
    private File data = new File("score.txt");
    private Instant instantStart;
    private double timeElapsed;
    private boolean gameOver;

    public Game(int size) {
        gameOver = false;
        this.tabCells = new Cell[size][size];

        this.cells = new HashMap<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                tabCells[i][j] = new Cell();
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tabCells[i][j].setGame(this);
            }
        }

        rnd();
        rnd();
        instantStart = Instant.now();
        ThreadGetActualTime();
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public void updateCell(Cell cell, Point point) {
        if (point.x >= getSize() | point.y >= getSize() | point.x < 0 | point.y < 0) {
            throw new IllegalArgumentException("Point must have coordinates inside the game board");
        }

        setCell(cell, new Point(point.x, point.y));
        cell.setGame(this);
        cell.updateColor();

        if (!cells.containsKey(cell) && cell.getValue() != 0) {
            cells.put(getCell(point.x, point.y), point);
        } else {
            cells.replace(cell, cell.getCoord(), point);
        }
    }

    public boolean hasNextMove() {
        for (Cell cell : getCells().keySet()) {
            if (cell.getNext(Direction.up) != null) {
                if (cell.getNext(Direction.up).getValue() == cell.getValue()) return true;
            }
            if (cell.getNext(Direction.down) != null) {
                if (cell.getNext(Direction.down).getValue() == cell.getValue()) return true;
            }
            if (cell.getNext(Direction.left) != null) {
                if (cell.getNext(Direction.left).getValue() == cell.getValue()) return true;
            }
            if (cell.getNext(Direction.right) != null) {
                if (cell.getNext(Direction.right).getValue() == cell.getValue()) return true;
            }
        }
        return false;
    }

    public void move(Direction direction) {
        boolean hasMoved = false;

        switch (direction) {
            case up:
                for (int y = 0; y < getSize(); y++) {
                    for (int x = 1; x < getSize(); x++) {
                        Cell cell = getCell(x, y);
                        if (cell.getValue() != NULL) {
                            if (cell.shift(Direction.up)) {
                                hasMoved = true;
                            }
                        }

                    }
                }

                break;

            case down:
                for (int y = 0; y < getSize(); y++) {
                    for (int x = getSize() - 2; x >= 0; x--) {
                        Cell cell = getCell(x, y);
                        if (cell.getValue() != NULL) {
                            if (cell.shift(Direction.down)) {
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;

            case right:
                for (int x = 0; x < getSize(); x++) {
                    for (int y = getSize() - 2; y >= 0; y--) {
                        Cell cell = getCell(x, y);
                        if (cell.getValue() != NULL) {
                            if (cell.shift(Direction.right)) {
                                hasMoved = true;
                            }
                        }

                    }
                }

                break;

            case left:
                for (int x = 0; x < getSize(); x++) {
                    for (int y = 1; y < getSize(); y++) {
                        Cell cell = getCell(x, y);
                        if (cell.getValue() != NULL) {
                            if (cell.shift(Direction.left)) {
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
        }

        if (hasMoved == true) {
            for (Cell cell : getCells().keySet()) {
                cell.setMerged(false);
            }
            rnd();
        }

        if (this.getCells().keySet().size() == getSize() * getSize() && !hasNextMove()) {
            System.out.println("game is over");
            gameOver = true;
        }

        System.out.println(getCells().size());
        setChanged();
        notifyObservers();
    }


    public void rnd() {

        int r, x, y;

        x = rnd.nextInt(getSize());
        y = rnd.nextInt(getSize());
        while (tabCells[x][y].getValue() != NULL) {
            x = rnd.nextInt(getSize());
            y = rnd.nextInt(getSize());
        }
        r = rnd.nextInt(2);


        switch (r) {
            case 0:
                updateCell(new Cell(2), new Point(x, y));
                break;
            case 1:
                updateCell(new Cell(512), new Point(x, y));
                break;
        }
        getCell(x, y).updateFile(data);


        setChanged();
        notifyObservers();


    }

    public void setCell(Cell cell, Point point) {
        tabCells[point.x][point.y] = cell;
    }

    public String toString() {
        String boardTitle = "Board" + this.getClass().getName() + "\n"
                + "\t" + "(" + getSize() + "," + getSize() + ")" + "\n";

        StringBuilder stringBuilder = new StringBuilder(boardTitle);
        for (int i = 0; i < getSize(); i++) {
            stringBuilder.append("  | ");
            for (int j = 0; j < getSize(); j++) {
                stringBuilder.append(tabCells[i][j] + " ");
            }
            stringBuilder.append("|" + "\n");
        }
        return stringBuilder.toString();
    }

    public HashMap<Cell, Point> getCells() {
        return this.cells;
    }

    public int getSize() {
        return tabCells.length;
    }

    public Cell getCell(int i, int j) {
        return tabCells[i][j];
    }


    public void resetBestScore() {
        try {

            if (data.delete()) {
                System.out.println(data.getName() + " est supprimé.");
            } else {
                System.out.println("Opération de suppression echouée");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public File getFile() {
        return this.data;
    }

    public int getBestScore() {
        if (data.exists()) {
            try {
                Scanner scanner = new Scanner(data);
                if (scanner.hasNextLine())
                    return Integer.parseInt(scanner.nextLine());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public double getBestTime() {

        if (data.exists()) {
            try {
                Scanner scanner = new Scanner(data);
                if (scanner.hasNextLine())
                    scanner.nextLine();
                    if (scanner.hasNextLine()) return Double.parseDouble(scanner.nextLine());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    public double getTimeElapsedMillis() {
        return timeElapsed/1000.0;
    }
    public int getTimeElapsed() {
        return (int)timeElapsed/1000;
    }

    public synchronized void setTimeElapsed() {
        Instant instantStop = Instant.now();
        //System.out.println(Duration.between(instantStart, instantStop).toMillis());
        timeElapsed = Duration.between(instantStart, instantStop).toMillis();
        setChanged();
        notifyObservers();
    }

    public void ThreadGetActualTime() {
        new Thread() {
            public synchronized void run() {
                while (!gameOver) {
                    //System.out.println(gameOver);
                    setTimeElapsed();
                    //System.out.println(activeCount());

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void restart() {
        gameOver = false;
        int size=this.getSize();
        this.cells.clear();

        for (int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){;
                tabCells[i][j] = new Cell(NULL);
            }
        }

        for (int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){
                tabCells[i][j].setGame(this);
            }
        }

        rnd();
        rnd();
        ThreadGetActualTime();
        instantStart = Instant.now();
    }

}