package vue_controleur;

import modele.Cell;
import modele.Game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import static java.sql.Types.NULL;

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 60;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Game game;


    public Swing2048(Game game) {
        this.game = game;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(this.game.getSize() * PIXEL_PER_SQUARE, this.game.getSize() * PIXEL_PER_SQUARE);
        tabC = new JLabel[this.game.getSize()][this.game.getSize()];


        JPanel contentPane = new JPanel(new GridLayout(this.game.getSize(), this.game.getSize()));

        for (int i = 0; i < this.game.getSize(); i++) {
            for (int j = 0; j < this.game.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.darkGray, 5);
                tabC[i][j] = new JLabel();
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);


                contentPane.add(tabC[i][j]);

            }
        }
        setContentPane(contentPane);
        addKeyboardListener();
        refresh();

    }




    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void refresh()  {

        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                for (int i = 0; i < game.getSize(); i++) {
                    for (int j = 0; j < game.getSize(); j++) {
                        Cell cell = game.getCell(i, j);

                        if (cell.getValue() == NULL) {

                            tabC[i][j].setText("");

                        } else {
                            tabC[i][j].setText(cell.getValue() + "");
                        }


                    }
                }
            }
        });


    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void addKeyboardListener() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : game.rnd(); break;
                    case KeyEvent.VK_RIGHT : game.rnd(); break;
                    case KeyEvent.VK_DOWN : game.rnd(); break;
                    case KeyEvent.VK_UP : game.rnd(); break;
                }
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}