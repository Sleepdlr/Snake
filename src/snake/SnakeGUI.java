/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author Viktória
 */
public class SnakeGUI {

    private JFrame frame;
    private GameEngine gameArea;
    private JPanel header = new JPanel();
    private JLabel scorce;
    private ScoreBoard scoreBoard;

    public SnakeGUI() {
        
        frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.scoreBoard = new ScoreBoard();

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Játék");
        menuBar.add(gameMenu);
        JMenuItem newGame = new JMenuItem("Új játék");
        gameMenu.add(newGame);
        JMenuItem highScore = new JMenuItem("TOP lista");
        gameMenu.add(highScore);

        highScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openScoreBoard();
            }
        });
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameArea.restart();
            }
        });
        JMenuItem exitMenuItem = new JMenuItem("Kilépés");
        gameMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        scorce = new JLabel("Pont: 0");

        header.add(scorce);
        frame.getContentPane().add(BorderLayout.NORTH, header);

        gameArea = new GameEngine(scorce, scoreBoard);
        frame.getContentPane().add(gameArea);

        frame.setPreferredSize(new Dimension(815, 688));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Megnyitja egy új ablakba a rangsorlistát.
     */
    public void openScoreBoard() {
        scoreBoard.GetDataFromDB();
        scoreBoard.setVisible(true);
    }
}
