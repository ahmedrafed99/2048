package modele;

import java.awt.*;

import static java.sql.Types.NULL;
import static modele.Direction.*;

public class Cell {
    private int value;
    private Game game;

    public Cell(int value) {
        this.game = null;
        this.value = value;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public Cell(){
        this.value = NULL;
        this.game = null;
    }

    public int getValue() {
        return value;
    }

    public void shift(Direction direction){
        switch (direction) {
            case up :
                if (this.getCoord().x!=0) {
                    Cell cellUp = getNext(up);
                    if (cellUp == null) {
                        Point coord = getCoord();
                        game.updateCell(this, game.getPoint(0, coord.y));
                        game.updateCell(new Cell(0), coord);
                    }
                    else {
                        if (cellUp.getValue() == this.getValue()) {
                            cellUp.fusion();
                            game.updateCell(new Cell(0), getCoord());
                        }
                        else if (cellUp.getCoord().x != getCoord().x-1){
                            Point coord = getCoord();
                            game.updateCell(this, game.getPoint(cellUp.getCoord().x+1, coord.y));
                            game.updateCell(new Cell(0), coord);
                        }
                    }
                }
                break;

            case down :
                if (this.getCoord().x!=game.getSize()-1) {
                    Cell cellDown = getNext(down);
                    if (cellDown == null) {
                        Point coord = getCoord();
                        game.updateCell(this, game.getPoint(game.getSize()-1, coord.y));
                        game.updateCell(new Cell(0), coord);
                    }
                    else {
                        if (cellDown.getValue() == this.getValue()) {
                            cellDown.fusion();
                            game.updateCell(new Cell(0), getCoord());
                        }
                        else if (cellDown.getCoord().x != getCoord().x+1){
                            Point coord = getCoord();
                            game.updateCell(this, game.getPoint(cellDown.getCoord().x-1, coord.y));
                            game.updateCell(new Cell(0), coord);
                        }
                    }
                }
                break;

            case left :
                if (getCoord().y != 0) {
                    Cell cellLeft = getNext(left);
                    if (cellLeft == null) {
                        Point coord = getCoord();
                        game.updateCell(this, game.getPoint(coord.x, 0));
                        game.updateCell(new Cell(0), coord);
                    }
                    else {
                        if (cellLeft.getValue() == this.getValue()) {
                            cellLeft.fusion();
                            game.updateCell(new Cell(0), getCoord());
                        }
                        else if (cellLeft.getCoord().y != getCoord().y-1) {
                            Point coord = getCoord();
                            game.updateCell(this, game.getPoint(coord.x, cellLeft.getCoord().y+1));
                            game.updateCell(new Cell(0), coord);
                        }
                    }
                }
                break;

            case right :
                if (getCoord().y != game.getSize()-1) {
                    Cell cellRight = getNext(right);
                    if (cellRight == null) {
                        Point coord = getCoord();
                        game.updateCell(this, game.getPoint(coord.x, game.getSize()-1));
                        game.updateCell(new Cell(0), coord);
                    }
                    else {
                        if (cellRight.getValue() == this.getValue()) {
                            cellRight.fusion();
                            game.updateCell(new Cell(0), getCoord());
                        }
                        else if (cellRight.getCoord().y != getCoord().y+1) {
                            Point coord = getCoord();
                            game.updateCell(this, game.getPoint(coord.x, cellRight.getCoord().y-1));
                            game.updateCell(new Cell(0), coord);
                        }
                    }
                }
                break;

        }
    }
    public void fusion() {
        this.value *= 2;
    }

    public Cell getNext(Direction direction){
        switch (direction) {
            case up:
                for (int x=getCoord().x; x>0; x--){
                    Cell nextUp = game.getCell(x-1, getCoord().y);
                    if(nextUp.getValue() != NULL) {
                        return nextUp;
                    }
                }
                return null;

            case down:
                for (int x=getCoord().x; x<game.getSize()-1; x++){
                    Cell nextDown = game.getCell(x+1, getCoord().y);
                    if(nextDown.getValue() != NULL) {
                        return nextDown;
                    }
                }
                return null;

            case left:
                for (int y=getCoord().y; y>0; y--){
                    Cell nextLeft = game.getCell(getCoord().x, y-1);
                    if(nextLeft.getValue() != NULL) {
                        return nextLeft;
                    }
                }
                return null;

            case right:
                for (int y=getCoord().y; y<game.getSize()-1; y++){
                    Cell nextRight = game.getCell(getCoord().x, y+1);
                    if(nextRight.getValue() != NULL) {
                        return nextRight;
                    }
                }
                return null;
        }
        return null;
    }

    public Point getCoord() {
        return game.getCells().get(this);
    }

}
