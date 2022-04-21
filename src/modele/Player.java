package modele;

public class Player {
    int id;
    Game game;

    public Player(){
        this.id = 0;
        this.game = null;
    }

    public boolean makeMove(Direction direction){
        this.game.move(direction);
        return true;
    }

    public boolean setGame(Game game){
        this.game = game;
        return true;
    }

}
