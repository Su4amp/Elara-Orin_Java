package core;

import entities.Player;
import input.InputHandler;
import world.CollisionChecker;
import world.Map;
import world.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    private TileManager tileManager;
    private Map map;
    private Thread gameThread;
    private final double drawInterval = 1_000_000_000 / GameConfig.TARGET_FPS;
    private Player player;
    private Camera camera;
    private InputHandler inputHandler;

    public GamePanel(JFrame frame) {
        inputHandler = new InputHandler(frame);
        tileManager = new TileManager(this);
        tileManager.loadTiles("/resources/config/tile_config.txt");
        map = new Map(this, tileManager, "/resources/maps/map01.txt");

        setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        setBackground(java.awt.Color.BLACK);
        setDoubleBuffered(true);

        player = new Player(100, 100); // Posição inicial
        CollisionChecker collisionChecker = new CollisionChecker(map);
        player.setCollisionChecker(collisionChecker);

        camera = new Camera(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
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
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        player.update(inputHandler);
        camera.update(player);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(GameConfig.SCALE, GameConfig.SCALE);
        map.draw(g2, camera.getOffsetX(), camera.getOffsetY());
        player.draw(g2, camera.getOffsetX(), camera.getOffsetY());
        g2.dispose();
    }
}