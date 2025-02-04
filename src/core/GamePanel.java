package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import world.Map;
import world.TileManager;

public class GamePanel extends JPanel implements Runnable {
    private TileManager tileManager;
    private Map map;
    private Thread gameThread;
    private final double drawInterval = 1_000_000_000 / GameConfig.TARGET_FPS;

    public GamePanel(JFrame frame) {
        tileManager = new TileManager(this);
        tileManager.loadTiles("/resources/config/tile_config.txt");
        map = new Map(this, tileManager, "/resources/maps/map01.txt");
        
        setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        
        // Exemplo: Alterar escala durante o jogo
        // GameConfig.setScale(3);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        // Lógica de atualização (exemplo: alterar escala com teclado)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Aplicar escala global
        g2.scale(GameConfig.SCALE, GameConfig.SCALE);
        map.draw(g2);
        
        g2.dispose();
    }
}
