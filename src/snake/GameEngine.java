/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 *
 * @author Viktória
 */
public class GameEngine extends JPanel {

    private final int FPS = 10;
    private Snake snake;
    private boolean paused = false;
    private boolean isOver = false;
    private Image background;
    private Timer timer;
    private JLabel score;
    private boolean gameOver = false;
    private Connection data;
    private ScoreBoard scoreBoard;

    public GameEngine(JLabel s, ScoreBoard sb) {
        super();
        this.score = s;
        this.scoreBoard = sb;
        this.data = Database.ConnectDB();
        background = new ImageIcon("data/bg.jpg").getImage();
        
        this.getInputMap().put(KeyStroke.getKeyStroke("W"), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!isOver || !paused) {
                    snake.setDir(Snake.Direction.UP);
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "pressed down");
        this.getActionMap().put("pressed down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!isOver || !paused) {
                    snake.setDir(Snake.Direction.DOWN);
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("A"), "pressed left");
        this.getActionMap().put("pressed left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!isOver || !paused) {
                    snake.setDir(Snake.Direction.LEFT);
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("D"), "pressed right");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!isOver || !paused) {
                    snake.setDir(Snake.Direction.RIGHT);
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        this.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!isOver) {
                    paused = !paused;
                    background = new ImageIcon("data/bg.jpg").getImage();

                }
            }
        });
      
        restart();
        this.score.setText("Pont: " + getSorce());
        timer = new Timer(1000 / FPS, new NewFrameListener());
        timer.start();
    }

    /**
     * Visszaadja a kígyó hosszát.
     *
     * @return kígyó hossza
     */
    private int getSorce() {
        return snake.getLenght();
    }

    /**
     * Újraindítja a játékot.
     */
    public void restart() {
        background = new ImageIcon("data/bg.jpg").getImage();
        paused = false;
        isOver = false;
        gameOver = false;
        snake = new Snake();
    }

    /**
     * Megnézi, hogy megkérdeztük-e már a jétékost, hogy mi a neve.
     */
    private void gameOverLogic() {
        if (!gameOver) {

            String nickname = JOptionPane.showInputDialog(null, "Game Over! Elért pontszámod " + getSorce() + ". Kérlek adj meg egy nevet!", "Game Over", JOptionPane.QUESTION_MESSAGE);

            if (nickname != null && nickname.length() > 0) {
                String sql = "INSERT INTO stat (name, point) VALUES ('" + nickname + "', " + getSorce() + ");";
                try {
                    Statement stmt = data.createStatement();
                    stmt.execute(sql);
                    scoreBoard.GetDataFromDB();
                    scoreBoard.setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Hiba történt! ERROR: " + ex.toString());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nem adtál meg Nicknevet, ezért nem kerülhetsz be a toplistára! ");
            }

            gameOver = true;
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(background, 0, 0, 800, 600, null);
        snake.draw(grphcs);
    }
/**
 * Kiírja a játékos ponját felülre.
 */
    class NewFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!paused) {
                snake.move();
                if (!snake.moveIsValid()) {
                    isOver = true;
                    repaint();
                    gameOverLogic();
                    // restart();
                }
            }
            score.setText("Pont: " + getSorce());
            repaint();
        }
    }
}
