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
import static vue_controleur.Swing2048.PIXEL_PER_SQUARE;

public class Game extends Observable {

    private HashMap<Cell, Point> cells;
    private Cell[][] tabCells;
    private static Random rnd = new Random(4);
    private File data = new File("score.txt");
    private Instant instantStart;
    private double timeElapsed;
    private boolean gameOver;
    private int unlock;
    private boolean unlockRunning;
    private Point unlockedPosition;


    /**
     * Constructeur du jeu en fonction de la taille de la grille passée en paramètre
     * @param size entier représentant la taille de la grille du jeu
     */
    public Game(int size) {
        unlock = 100;
        unlockRunning = false;
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

    /**
     * Methode get pour obtenir l'attribut gameOver (private)
     * @return l'attribut gameOver
     */
    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * Met à jour la cellule passée en paramètre à la position passée en paramètre.
     * Si la cellule existe déjà dans le jeu, elle est simplement remplacée dans la hashmap, sinon, elle est ajoutée dans la hashmap
     * On appelle également updateColor, et on lui affecte le jeu courant en attribut
     * @param cell la cellule que l'on veut mettre ou modifier
     * @param point le point auquel on veut ajouter ou déplacer la dite cellule
     */
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

    /**
     * Cette méthode analyse pour chaque cellule, si son voisin direct dans toutes les directions, a une valeur égale à elle même ou non, pour déterminer s'il reste des mouvements possibles.
     * @return false si toutes les cases adjacentes sont de valeurs différentes, true sinon.
     */
    private boolean hasNextMove() {
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

    /**
     * Méthode principale qui est appelée lorsque l'on appuie sur une flèche dans l'interface du jeu, elle bouge les cases en appelant successivement la méthode shift sur chaque cellule, de la plus proche du bord
     * à la plus éloignée. Elle initialise un booléen hasMoved à false, qui ne passe à true que si au moins une case a pu bouger. Si aucune case n'a bougé, rien ne se passe, sinon, on peut appeler la fonction rnd()
     * qui va placer une nouvelle case au hasard après avoir fait le déplacement de toutes les cases déjà présentes sur la grille. On verifie ensuite qu'un mouvement est possible avec la nouvelle grille, en faisant
     * appel, si le nombre de cellules présentes est égal à la taille de la grille, à la méthode hasNextMove(), le cas échéant, on passe le booléen gameOver à false.
     * Elle n'effectue ces actions que si le processus d'échange entre deux cases n'est pas en cours (ie. on empêche d'appuyer sur les flèches si le bouton de souris est enfoncé sur une case de la grille)
     * @param direction La direction vers laquelle on veut envoyer les cases (qui correspond à la direction de la flèche sur laquelle on a appuyé)
     */
    public void move(Direction direction) {
                boolean hasMoved = false;
                if (!unlockRunning) {
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
                }
                if (hasMoved == true) {
                    for (Cell cell : getCells().keySet()) {
                        cell.setMerged(false);
                    }
                    rnd();
                    System.out.println("hm size :" + cells.size());
                }

                if (getCells().keySet().size() == getSize() * getSize() && !hasNextMove()) {
                    System.out.println("game is over");
                    gameOver = true;
                    setChanged();
                    notifyObservers();
                }



        System.out.println(getCells().size());
    }


    /**
     * Fonction qui tire au hasard une case dans le tableau (jusqu'à trouver une case libre), puis tire au hasard une valeur pour y ajouter une nouvelle cellule, de la valeur en question.
     * Elle notifie l'observer que le modèle a changé pour que l'interface se mette à jour.
     */
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
                updateCell(new Cell(4), new Point(x, y));
                break;
        }
        getCell(x, y).updateFile(data);


        setChanged();
        notifyObservers();


    }

    /**
     * Methode qui va placer dans le tableau de cellules, la cellule passé en paramètre au point passé en paramètre
     * @param cell la cellule que l'on veut ajouter dans le tableau
     * @param point le point auquel on veut ajouter la cellule dans le tableau
     */
    private void setCell(Cell cell, Point point) {
        tabCells[point.x][point.y] = cell;
    }

    /**
     * @return une chaine de caractères représantant l'état courant du jeu
     */
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

    /**
     * @return l'attribut hashMap qui contient les cellules du jeu
     */
    public HashMap<Cell, Point> getCells() {
        return this.cells;
    }

    /**
     * @return la taille du tableau de jeu, autrement dit, la dimension du jeu (ex: retourne 4 si le jeu est un 4x4)
     */
    public int getSize() {
        return tabCells.length;
    }

    /**
     * @param i l'indice de la ligne du tableau
     * @param j l'indice de la colonne du tableau
     * @return la cellule stockée dans le tableau à l'indice (i, j)
     */
    public Cell getCell(int i, int j) {
        return tabCells[i][j];
    }

    /**
     * Methode qui supprime le fichier data passé en attribut de la classe Game, et qui va implicitement écraser les données sauvegardées pour le meilleur temps et le meilleur score
     */
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

    /**
     * @return le fichier data passé en argument de la classe
     */
    public File getFile() {
        return this.data;
    }

    /**
     * Cette méthode regarde d'abord si le fichier data (argument de la classe) existe, et si c'est le cas, elle va regarder la première ligne de ce fichier qui
     * correspond à l'endroit où est stocké le meilleur score sous forme d'entier
     * @return la première ligne du fichier data s'il existe, 0 sinon.
     */
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

    /**
     * Cette methode regarde, si le fichier data existe, la deuxième ligne correspondant à l'endroit où est stocké le meilleur temps pour atteindre 2048.
     * @return le double correspondant au meilleur temps pour gagner le jeu (en secondes), 0 si ce score n'a jamais été atteint.
     */
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

    /**
     * @return double représentant le temps passé depuis l'initialisation du jeu, en secondes avec 3 flottants (pour le stockage du meilleur temps)
     */
    public double getTimeElapsedMillis() {
        return timeElapsed/1000.0;
    }

    /**
     * @return entier représentant le temps passé depuis l'initialisation du jeu, en secondes (pour l'affichage dans l'interface)
     */
    public int getTimeElapsed() {
        return (int)timeElapsed/1000;
    }

    /**
     * Met à jour l'attribut timeElapsed (synchronized pour qu'un seul thread n'y accède à la fois) en calculant la durée entre l'instant où le jeu a commencé (initialisé au debut du jeu),
     * et l'instant actuel, puis notifie l'observer pour qu'il mette à jour sur la fenêtre le temps écoulé.
     */
    private synchronized void setTimeElapsed() {
        Instant instantStop = Instant.now();
        //System.out.println(Duration.between(instantStart, instantStop).toMillis());
        timeElapsed = Duration.between(instantStart, instantStop).toMillis();
        setChanged();
        notifyObservers();
    }

    /**
     * Utilise un thread qui, tant que la partie n'est pas terminée (gameOver à true), va appeler setTimeElapsed pour mettre à jour le temps écoulé depuis le début du jeu
     */
    private void ThreadGetActualTime() {
        new Thread() {
            public synchronized void run() {
                while (!gameOver) {
                    //System.out.println(gameOver);
                    setTimeElapsed();
                    //System.out.println(activeCount());

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * Cette méthode remet le jeu à 0 afin de recommencer une nouvelle partie, on vide la grille, on remet deux cases aléatoires, et on recommence le timer à 0
     */
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
        instantStart = Instant.now();
        ThreadGetActualTime();
    }

    /**
     * Cette méthode vient stocker les coordonnées de la case sur laquelle on a cliqué (à l'enfoncement) dans l'attribut unlockedPosition, aux coordonnées sur la fenêtre passées en paramètres.
     * Elle passe l'attribut booleen unlockRunning à false, qui signifie que l'on est en train de déclencher un déblocage (et ce jusqu'à ce que le clic soit relaché sur une autre position)
     * @param mouseX les coordonnées en X du clic sur la fenêtre
     * @param mouseY les coordonnées en Y du clic sur la fenêtre
     */
    public void setUnlocked(int mouseX, int mouseY) {
        int tabX = (mouseY-35)/PIXEL_PER_SQUARE;
        int tabY = mouseX/PIXEL_PER_SQUARE;
        if (tabX<getSize() && tabY<getSize()) {
            if (unlock > 0 && !unlockRunning) {
                System.out.println(unlock + ", " + unlockRunning);
                unlockRunning = true;
                unlockedPosition = new Point(tabX, tabY);
                System.out.println(unlock + ", " + unlockRunning + ", " + tabCells[tabX][tabY].getValue() + ", " + unlockedPosition);
            }
        }
    }

    /**
     * Modifie le nombre de déblocage(s) disponible(s) dans la partie (par défaut il est à 1 dans le constructeur) et redémarre le jeu
     * @param a le nouveau nombre de déblocage(s) que l'on souhaite pour notre partie
     */
    public void setUnlock(int a) {
        unlock = a;
        restart();
    }

    /**
     * Cette méthode, appelée au relachement d'un clic, va échanger la case aux coordonnées stockées dans l'attribut unlockedPosition, affecté au moment de la pression du clic, avec la case aux coordonnées
     * passées en paramètres, en vérifiant que l'on est actuellement dans la démarche de déblocage (unlockRunning à true), puis si le bouton à été relaché dans une case du tableau, et enfin si cette case est différente
     * de celle sur laquelle le clic a été enclenché. Une fois tout cela vérifié, on peut décrémenter de 1 le compteur de déblocage "unlock", puis on échange les positions des deux cellules avec la méthode updateCell.
     * On regarde ensuite, dans le cas où la grille est pleine, si nous étions en gameOver et que l'on s'est débloqué, alors la partie peut reprendre son cours en repassant l'attribut gameOver à false, et on notifie l'observer
     * @param mouseX les coordonnées en X du clic sur la fenêtre
     * @param mouseY les coordonnées en Y du clic sur la fenêtre
     */
    public void switchPosition(int mouseX, int mouseY) {
        int tabX = (mouseY-35)/PIXEL_PER_SQUARE;
        int tabY = mouseX/PIXEL_PER_SQUARE;
        if (unlockRunning) {
            if (tabX < getSize() && tabY < getSize()) {
                if (tabX != unlockedPosition.x || tabY != unlockedPosition.y) {
                    unlock -= 1;
                    Cell unlocked =null;
                    unlocked = getCell(unlockedPosition.x, unlockedPosition.y);
                    updateCell(getCell(tabX, tabY), new Point(unlockedPosition.x, unlockedPosition.y));
                    updateCell(unlocked, new Point(tabX, tabY));
                    System.out.println(unlock + ", " + unlockRunning + ", " + tabCells[tabX][tabY].getValue());
                }
            }
        }
        unlockedPosition = null;
        unlockRunning = false;

        if (this.getCells().keySet().size() == getSize() * getSize() ) {
            if (!gameOver && !hasNextMove()) {
                System.out.println("game is over");
                gameOver = true;
            }
            else if (gameOver && hasNextMove()) {
                gameOver = false;
            }
        }

        setChanged();
        notifyObservers();
    }

}