package modele;

import java.awt.*;

import static java.sql.Types.NULL;

public class Cell {
    private int value;
    private Game game;

    public Cell(int value) {
        this.game = null;
        this.value = value;
    }

    public Cell(){
        this.value = NULL;
        this.game = null;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getValue() {
        return value;
    }

    public void shift(Direction direction){
        switch (direction){
            case up :
                if(getCoord().x != 0) {
                    Cell cellUp = this.getNext(Direction.up);
                    if (cellUp != null && cellUp.getValue() != this.getValue()) {
                        game.updateCell(this.getValue(), new Point(cellUp.getCoord().x+1, cellUp.getCoord().y));
                        this.emptyCell();
                    }

                    if(cellUp != null && cellUp.getValue() == this.getValue()){
                        this.merge(cellUp);
                    }

                    if(cellUp == null){
                        game.updateCell(this.getValue(), new Point(0, getCoord().y));
                        this.emptyCell();
                    }

                }

                break;

            case down :
                if(getCoord().x != this.game.getSize()-1) {
                    Cell cellDown = this.getNext(Direction.down);
                    if (cellDown != null && cellDown.getValue() != this.getValue()) {
                        game.updateCell(this.getValue(), new Point(cellDown.getCoord().x-1, cellDown.getCoord().y));
                        this.emptyCell();
                    }

                    if(cellDown != null && cellDown.getValue() == this.getValue()){
                        this.merge(cellDown);
                    }

                    if(cellDown == null){
                        game.updateCell(this.getValue(), new Point(game.getSize()-1, getCoord().y));
                        this.emptyCell();
                    }
                }

                break;

            case right :
                if(getCoord().y != this.game.getSize()) {
                    Cell cellRight = this.getNext(Direction.right);
                    if (cellRight != null && cellRight.getValue() != this.getValue()) {
                        game.updateCell(this.getValue(), new Point(cellRight.getCoord().x, cellRight.getCoord().y-1));
                        this.emptyCell();
                    }

                    if(cellRight != null && cellRight.getValue() == this.getValue()){
                        this.merge(cellRight);
                    }

                    if(cellRight == null){
                        game.updateCell(this.getValue(), new Point(getCoord().x, game.getSize()-1));
                        this.emptyCell();
                    }
                }

                break;

            case left :
                if(getCoord().y != 0) {
                    Cell cellLeft = this.getNext(Direction.left);
                    if (cellLeft != null && cellLeft.getValue() != this.getValue()) {
                        game.updateCell(this.getValue(), new Point(cellLeft.getCoord().x, cellLeft.getCoord().y+1));
                        this.emptyCell();
                    }

                    if(cellLeft != null && cellLeft.getValue() == this.getValue()){
                        this.merge(cellLeft);
                    }

                    if(cellLeft == null){
                        game.updateCell(this.getValue(), new Point(getCoord().x, 0));
                        this.emptyCell();
                    }
                }

                break;
        }
    }

    public void merge(Cell cell){
        game.updateCell(cell.getValue() * 2, new Point(cell.getCoord().x, cell.getCoord().y));
        game.updateCell(NULL, new Point(this.getCoord().x, this.getCoord().y));

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

    public void setValue(int value) {
        this.value = value;
    }

    public Point getCoord() {
        return this.game.getCells().get(this);
    }

    public void emptyCell() {
        setValue(NULL);
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
