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
        int row = worldY / GameConfig.TILE_SIZE;
        int col = worldX / GameConfig.TILE_SIZE;

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

        return false; // Nenhuma colisão detectada
    }
}