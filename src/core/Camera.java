package core;

import entities.Player;

public class Camera {
    private int offsetX, offsetY;
    private final int screenWidth, screenHeight;

    public Camera(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Atualiza os offsets da câmera para centralizar o jogador na tela.
     * Aqui você pode adicionar limites caso o mapa possua dimensões máximas.
     */
    public void update(Player player) {
        offsetX = player.getWorldX() - screenWidth / 2;
        offsetY = player.getWorldY() - screenHeight / 2;

        // Impede que a câmera se mova para fora dos limites (ajuste se necessário)
        if (offsetX < 0) offsetX = 0;
        if (offsetY < 0) offsetY = 0;
        // Se houver limites máximos do mapa, adicione as verificações aqui.
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}
