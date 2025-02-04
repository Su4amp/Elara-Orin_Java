package core;

public class GameConfig {
    // Configurações básicas
    public static final int ORIGINAL_TILE_SIZE = 32;
    public static float SCALE = 1.4f; // Tamanho dinâmico (pode ser alterado durante o jogo)
    public static int TILE_SIZE = (int) (ORIGINAL_TILE_SIZE * SCALE); // Tamanho final renderizado
    
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int TARGET_FPS = 60;
    
    // Método para alterar a escala dinamicamente
    public static void setScale(int newScale) {
        SCALE = newScale;
        TILE_SIZE = (int) (ORIGINAL_TILE_SIZE * SCALE);
    }
}