package core;

import javax.swing.JFrame;

public class GameWindow {
    public GameWindow() {
        JFrame frame = new JFrame("Meu Jogo");
        GamePanel gamePanel = new GamePanel(frame);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        gamePanel.startGameThread();
    }
}