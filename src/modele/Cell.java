package modele;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static java.sql.Types.NULL;

public class Cell {
    private int value;
    private Game game;
    private boolean isMerged;
    private Color color;



    public Cell(int value) {
        this.game = null;
        this.value = value;
        this.isMerged = false;
    }

    public Cell(){
        this.value = NULL;
        this.game = null;
        this.isMerged = false;
    }



    public boolean shift(Direction direction){
        switch (direction){
            case up :
                if(getCoord().x != 0) {
                    Cell cellUp = this.getNext(Direction.up);
                    if (cellUp != null && cellUp.getValue() != this.getValue() && getDistance(cellUp, Direction.up) > 1) {
                        Point oldPoint = this.getCoord();
                        game.updateCell(this, new Point(cellUp.getCoord().x+1, cellUp.getCoord().y));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }

                    if(cellUp != null && cellUp.getValue() == this.getValue() ){
                        if (!cellUp.isMerged()) {
                            this.merge(cellUp);
                        }
                        else {
                            Point oldPoint = this.getCoord();
                            game.updateCell(this, new Point(cellUp.getCoord().x+1, cellUp.getCoord().y));
                            game.updateCell(new Cell(), oldPoint);
                        }
                        return true;
                    }

                    if(cellUp == null){
                        Point oldPoint = this.getCoord();
                        game.updateCell(this, new Point(0, getCoord().y));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }

                }

                break;

            case down :
                if(getCoord().x != this.game.getSize()-1) {
                    Cell cellDown = this.getNext(Direction.down);
                    if (cellDown != null && cellDown.getValue() != this.getValue() && getDistance(cellDown, Direction.down) > 1) {
                        Point oldPoint = this.getCoord();
                        game.updateCell(this, new Point(cellDown.getCoord().x-1, cellDown.getCoord().y));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }

                    if(cellDown != null && cellDown.getValue() == this.getValue() ){
                        if (!cellDown.isMerged()) {
                            this.merge(cellDown);
                        }
                        else {
                            Point oldPoint = this.getCoord();
                            game.updateCell(this, new Point(cellDown.getCoord().x-1, cellDown.getCoord().y));
                            game.updateCell(new Cell(), oldPoint);
                        }
                        return true;
                    }

                    if(cellDown == null){
                        Point oldPoint = this.getCoord();
                        game.updateCell(this, new Point(game.getSize()-1, getCoord().y));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }
                }

                break;

            case right :
                if(getCoord().y != this.game.getSize()) {
                    Cell cellRight = this.getNext(Direction.right);
                    if (cellRight != null && cellRight.getValue() != this.getValue() && getDistance(cellRight, Direction.right) > 1) {
                        Point oldPoint = this.getCoord();
                        game.updateCell(this, new Point(cellRight.getCoord().x, cellRight.getCoord().y-1));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }

                    if(cellRight != null && cellRight.getValue() == this.getValue() ){
                        if (!cellRight.isMerged()) {
                            this.merge(cellRight);
                        }
                        else {
                            Point oldPoint = this.getCoord();
                            game.updateCell(this, new Point(cellRight.getCoord().x, cellRight.getCoord().y - 1));
                            game.updateCell(new Cell(), oldPoint);
                        }
                        return true;
                    }

                    if(cellRight == null){
                        Point oldPoint = this.getCoord();
                        game.updateCell(this, new Point(getCoord().x, game.getSize()-1));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }
                }

                break;

            case left :
                if(getCoord().y != 0) {
                    Cell cellLeft = this.getNext(Direction.left);
                    if (cellLeft != null && cellLeft.getValue() != this.getValue() && getDistance(cellLeft, Direction.left) > 1) {
                        Point oldPoint = this.getCoord();
                        game.updateCell(this, new Point(cellLeft.getCoord().x, cellLeft.getCoord().y+1));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }

                    if(cellLeft != null && cellLeft.getValue() == this.getValue() ){
                        if (!cellLeft.isMerged()) {
                            this.merge(cellLeft);
                        }
                        else {
                            Point oldPoint = this.getCoord();
                            game.updateCell(this, new Point(cellLeft.getCoord().x, cellLeft.getCoord().y+1));
                            game.updateCell(new Cell(), oldPoint);
                        }
                        return true;
                    }

                    if(cellLeft == null){
                        Point oldPoint = this.getCoord();
                        game.updateCell(this, new Point(getCoord().x, 0));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }
                }

                break;
        }
        return false;
    }

    public void merge(Cell cell){
        Cell mergedCell = new Cell(cell.getValue()*2);
        Point cellPoint = new Point(cell.getCoord().x, cell.getCoord().y);

        this.deleteCell();
        cell.deleteCell();
        game.updateCell(mergedCell, cellPoint);

        mergedCell.setMerged(true);
        game.getCell(mergedCell.getCoord().x, mergedCell.getCoord().y).updateFile(game.getFile());
    }

    public int getDistance(Cell cell, Direction direction) {
        switch (direction){
            case up:
                return this.getCoord().x - cell.getCoord().x;

            case down:
                return cell.getCoord().x - this.getCoord().x;

            case right:
                return cell.getCoord().y - this.getCoord().y;

            case left:
                return this.getCoord().y - cell.getCoord().y;
        }
        return -1;
    }

    public Cell getNext(Direction direction){
        switch (direction) {
            case up:
                if (getCoord().x == 0) {
                    return null;
                }
                else {
                    for (int x=getCoord().x; x>0; x--){
                        Cell nextUp = game.getCell(x-1, getCoord().y);
                        if(nextUp.getValue() != NULL) {
                            return nextUp;
                        }

                    }
                    return null;
                }

            case down:
                if (getCoord().x == game.getSize()-1) {
                    return null;
                }
                else {
                    for (int x=getCoord().x; x<game.getSize()-1; x++){
                        Cell nextDown = game.getCell(x+1, getCoord().y);
                        if(nextDown.getValue() != NULL) {
                            return nextDown;
                        }

                    }
                    return null;
                }

            case left:
                if (getCoord().y == 0) {
                    return null;
                }
                else {
                    for (int y=getCoord().y; y>0; y--){
                        Cell nextLeft = game.getCell(getCoord().x, y-1);
                        if(nextLeft.getValue() != NULL) {
                            return nextLeft;
                        }

                    }
                    return null;
                }

            case right:
                if (getCoord().y == game.getSize()-1) {
                    return null;
                }
                else {
                    for (int y = getCoord().y; y<game.getSize()-1; y++){
                        Cell nextRight = game.getCell(getCoord().x, y+1);
                        if(nextRight.getValue() != NULL) {
                            return nextRight;
                        }

                    }
                    return null;
                }
        }
        return null;
    }

    public boolean hasNext(){
        Cell nextUp = getNext(Direction.up);
        Cell nextDown = getNext(Direction.down);
        Cell nextRight = getNext(Direction.right);
        Cell nextLeft = getNext(Direction.left);

        return nextUp != null | nextDown != null | nextRight != null | nextLeft != null;

    }
    public Point getCoord() {
        return this.game.getCells().get(this);
    }

    public void deleteCell() {
        game.updateCell(new Cell(), this.getCoord());
        game.getCells().remove(this);
    }


    public boolean isMerged() {
        return isMerged;
    }

    public void setMerged(boolean merged) {
        isMerged = merged;
    }

    public Color getColor() {
        return color;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void updateColor() {
        switch (value) {
            case 2:
                color = new Color(238,228,218);
                break;
            case 4:
                color = new Color(238,225,201);
                break;
            case 8:
                color = new Color(243,178,122);
                break;
            case 16:
                color = new Color(246,150,100);
                break;
            case 32:
                color = new Color(236, 229, 29);
                break;
            case 64:
                color = new Color(236, 59, 0);
                break;
            case 128:
                color = new Color(243, 17, 17);
                break;
            case 256:
                color = new Color(236, 218, 8);
                break;
            case 512:
                color = new Color(220, 166, 11);
                break;
            case 1024:
                color = new Color(236, 252, 4);
                break;
            case 2048:
                color = new Color(138, 250, 37);
                break;
            case 4096:
                color = new Color(50, 50, 100);
                break;
            case 8192:
                color = new Color(100, 100, 100);
                break;
            case 16384:
                color = new Color(160, 160, 160);
                break;
        }
    }

    public int getValue() {
        return value;
    }

    public void updateFile(File file) {
        int score = game.getBestScore();
        double time = game.getBestTime();

        if (score < value || value == 2048) {
            try {
                PrintWriter writer1 = new PrintWriter(file);
                if (score > value) {
                    writer1.println(score);
                }
                else {
                    writer1.println(value);
                }
                if (value == 2048 && (game.getTimeElapsedMillis() < time || time == 0.0) ) {
                    writer1.println(game.getTimeElapsedMillis());
                }
                else {
                    writer1.println(time);
                }
                writer1.flush();
                writer1.close();
            } catch (FileNotFoundException ex) {
                System.err.println("error");
            }
        }

    }

    @Override
    public String toString() {
        if (value != 0) {
            return "{" +
                    "" + value +
                    "}" ;
        } else {
            return "{ }";
        }

    }
}
