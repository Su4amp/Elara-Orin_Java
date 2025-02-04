package core;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import world.Map;
import world.TileManager;
import entities.Player;
import input.InputHandler;

public class GamePanel extends JPanel implements Runnable {
    private TileManager tileManager;
    private Map map;
    private Thread gameThread;
    private final double drawInterval = 1_000_000_000 / GameConfig.TARGET_FPS;

    // Instâncias do jogador e da câmera
    private Player player;
    private Camera camera;
    private InputHandler inputHandler;

    public GamePanel(JFrame frame) {

        inputHandler = new InputHandler(frame);
        tileManager = new TileManager(this);
        tileManager.loadTiles("/resources/config/tile_config.txt");
        map = new Map(this, tileManager, "/resources/maps/map01.txt");

        // Configurações iniciais do painel
        setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        setBackground(java.awt.Color.BLACK);
        setDoubleBuffered(true);

        // Criar o jogador
        player = new Player(100, 100); // Posição inicial

        // Inicialização da câmera
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
        player.update(inputHandler); // Atualiza o jogador
        camera.update(player); // Atualiza a câmera
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Aplicar escala global
        g2.scale(GameConfig.SCALE, GameConfig.SCALE);

        // Desenhar o mapa ajustado pela câmera
        map.draw(g2, camera.getOffsetX(), camera.getOffsetY());

        // Desenhar o jogador ajustado pela câmera
        player.draw(g2, camera.getOffsetX(), camera.getOffsetY());

        g2.dispose();
    }
}