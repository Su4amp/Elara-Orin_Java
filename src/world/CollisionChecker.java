package world;

import core.GameConfig;

public class CollisionChecker {
    private final Map map;

    public CollisionChecker(Map map) {
        this.map = map;
    }

    /**
     * Verifica se há colisão em uma determinada posição no mundo.
     *
     * @param worldX Posição X no mundo.
     * @param worldY Posição Y no mundo.
     * @return true se houver colisão, false caso contrário.
     */
    public boolean checkCollision(int worldX, int worldY) {
        // Calcula as coordenadas dos quatro cantos do jogador
        int left = worldX / GameConfig.TILE_SIZE;
        int right = (worldX + GameConfig.TILE_SIZE - 1) / GameConfig.TILE_SIZE;
        int top = worldY / GameConfig.TILE_SIZE;
        int bottom = (worldY + GameConfig.TILE_SIZE - 1) / GameConfig.TILE_SIZE;
    
        // Verifica todos os tiles que o jogador está ocupando
        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                if (row >= 0 && row < map.getRows() && col >= 0 && col < map.getCols()) {
                    // Verificar colisão na camada base
                    int baseTileIndex = map.getBaseLayerValue(row, col);
                    if (baseTileIndex != -1) {
                        Tile tile = map.getTileManager().getTile(0, baseTileIndex);
                        if (tile != null && tile.collision) {
                            return true; // Colisão detectada na camada base
                        }
                    }
    
                    // Verificar colisão na camada de props
                    int propTileIndex = map.getPropLayerValue(row, col);
                    if (propTileIndex != -1) {
                        Tile tile = map.getTileManager().getTile(1, propTileIndex);
                        if (tile != null && tile.collision) {
                            return true; // Colisão detectada na camada de props
                        }
                    }
                }
            }
        }
    
        return false; // Nenhuma colisão detectada
    }
}