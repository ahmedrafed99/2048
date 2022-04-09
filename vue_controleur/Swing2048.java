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
import static modele.Direction.*;

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
                tabC[i][j].setFont(new Font("Serif", Font.BOLD, 20));
                tabC[i][j].setOpaque(true);


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
                            tabC[i][j].setBackground(new Color(255, 255, 255));

                        } else {
                            tabC[i][j].setText(cell.getValue() + "");
                            switch (cell.getValue()) {
                                case 2:
                                    tabC[i][j].setBackground(new Color(255, 255, 0));
                                    break;
                                case 4:
                                    tabC[i][j].setBackground(new Color(255, 170, 0 ));
                                    break;
                                case 8:
                                    tabC[i][j].setBackground(new Color(255, 100, 0));
                                    break;
                                case 16:
                                    tabC[i][j].setBackground(new Color(255, 10, 0));
                                    break;
                                case 32:
                                    tabC[i][j].setBackground(new Color(200, 0, 0));
                                    break;
                                case 64:
                                    tabC[i][j].setBackground(new Color(100, 0, 0));
                                    break;
                                case 128:
                                    tabC[i][j].setBackground(new Color(255, 0, 100));
                                    break;
                                case 256:
                                    tabC[i][j].setBackground(new Color(255, 0, 220));
                                    break;
                                case 512:
                                    tabC[i][j].setBackground(new Color(255, 0, 255));
                                    break;
                                case 1024:
                                    tabC[i][j].setBackground(new Color(150, 0, 255));
                                    break;
                                case 2048:
                                    tabC[i][j].setBackground(new Color(50, 0, 255));
                                    break;
                                case 4096:
                                    tabC[i][j].setBackground(new Color(50, 50, 100));
                                    break;
                                case 8192:
                                    tabC[i][j].setBackground(new Color(100, 100, 100));
                                    break;
                                case 16384:
                                    tabC[i][j].setBackground(new Color(160, 160, 160));
                                    break;
                            }
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
                    case KeyEvent.VK_LEFT : game.move(left); break;
                    case KeyEvent.VK_RIGHT : game.move(right); break;
                    case KeyEvent.VK_DOWN : game.move(down); break;
                    case KeyEvent.VK_UP : game.move(up); break;
                }
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}