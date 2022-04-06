package modele;

import static java.sql.Types.NULL;

public class Cell {
    private int value;
    private Game game;

    public Cell(int value) {
        this.value = value;
    }

    public Cell(){
        this.value = NULL;
    }

    public int getValue() {
        return value;
    }

    public void shift(Direction direction){
        if (direction == Direction.up){

        }
    }

}
