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
        if (direction == Direction.up){

        }
    }

    public Cell getNext(Direction direction){
        switch (direction) {
            case up:
                if (getCoord().y == 0) {
                    return null;
                }
                else {
                    for (int y=getCoord().y; y>0; y--){
                        Cell nextUp = game.getCell(getCoord().x, y-1);
                        if(nextUp.getValue() != NULL) {
                            return nextUp;
                        }

                    }
                    return null;
                }

            case down:
                if (getCoord().y == game.getSize()-1) {
                    return null;
                }
                else {
                    for (int y=getCoord().y; y<game.getSize(); y++){
                        Cell nextDown = game.getCell(getCoord().x, y+1);
                        if(nextDown.getValue() != NULL) {
                            return nextDown;
                        }

                    }
                    return null;
                }

            case left:
                if (getCoord().x == 0) {
                    return null;
                }
                else {
                    for (int x=getCoord().x; x>0; x--){
                        Cell nextLeft = game.getCell(x-1, getCoord().y);
                        if(nextLeft.getValue() != NULL) {
                            return nextLeft;
                        }

                    }
                    return null;
                }

            case right:
                if (getCoord().x == game.getSize()-1) {
                    return null;
                }
                else {
                    for (int x=getCoord().x; x<game.getSize(); x++){
                        Cell nextRight = game.getCell(x+1, getCoord().y);
                        if(nextRight.getValue() != NULL) {
                            return nextRight;
                        }

                    }
                    return null;
                }
        }
        return null;
    }

    public Point getCoord() {
        return game.getCells().get(this);
    }

}
