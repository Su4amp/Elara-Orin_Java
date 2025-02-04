package world;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.GameConfig;
import core.GamePanel;

public class Map {
    
    private final TileManager tileManager;
    private int[][] baseLayer;
    private int[][] propLayer;
    private int rows;
    private int cols;

    public Map(GamePanel gp, TileManager tileManager, String mapPath) {
        
        this.tileManager = tileManager;
        // Remover inicialização prematura
        loadMap(mapPath);
        validateMap();
    }

    private void loadMap(String mapPath) {
        try {
            // Carregar todo o conteúdo do arquivo primeiro
            InputStream is = Map.class.getResourceAsStream(mapPath);
            if(is == null) throw new RuntimeException("Arquivo de mapa não encontrado: " + mapPath);
            
            List<String> allLines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    allLines.add(line);
                }
            }
    
            // Primeira passada: processar header
            for(String line : allLines) {
                line = line.trim();
                if(line.startsWith("Rows=")) {
                    rows = Integer.parseInt(line.split("=")[1].trim());
                }
                if(line.startsWith("Cols=")) {
                    cols = Integer.parseInt(line.split("=")[1].trim());
                }
                if(rows > 0 && cols > 0) break; // Para de processar header quando tiver ambos
            }
    
            // Inicializar camadas
            baseLayer = new int[rows][cols];
            propLayer = new int[rows][cols];
            for(int i = 0; i < rows; i++) {
                Arrays.fill(baseLayer[i], -1);
                Arrays.fill(propLayer[i], -1);
            }
    
            // Segunda passada: processar camadas
            String currentSection = "";
            int baseRow = 0;
            int propRow = 0;
            
            for(String line : allLines) {
                line = line.trim();
                if(line.isEmpty() || line.startsWith("#")) continue;
    
                if(line.startsWith("[Header]")) {
                    currentSection = "Header";
                    continue;
                }
                if(line.startsWith("[BaseLayer]")) {
                    currentSection = "BaseLayer";
                    continue;
                }
                if(line.startsWith("[PropsLayer]")) {
                    currentSection = "PropsLayer";
                    continue;
                }
    
                switch(currentSection) {
                    case "BaseLayer":
                        if(baseRow < rows) processLayerLine(line, baseLayer[baseRow++]);
                        break;
                    case "PropsLayer":
                        if(propRow < rows) processLayerLine(line, propLayer[propRow++]);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void processLayerLine(String line, int[] layerRow) {
        String[] tokens = line.split("\\s+");
        for(int col = 0; col < cols; col++) {
            if(col < tokens.length && !tokens[col].isEmpty()) {
                try {
                    layerRow[col] = Integer.parseInt(tokens[col]);
                } catch (NumberFormatException e) {
                    layerRow[col] = -1;
                }
            } else {
                layerRow[col] = -1;
            }
        }
    }
    

    public void draw(Graphics2D g2, int cameraX, int cameraY) {
        // Desenhar camada base
        drawLayer(g2, baseLayer, 0, cameraX, cameraY);
        // Desenhar camada de props
        drawLayer(g2, propLayer, 1, cameraX, cameraY);
    }
    
    private void drawLayer(Graphics2D g2, int[][] layer, int layerType, int cameraX, int cameraY) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int tileIndex = layer[row][col];
                if (tileIndex == -1 || tileIndex == Integer.MIN_VALUE) continue;
    
                // Validação crítica!
                if (tileIndex < 0 || tileIndex >= tileManager.getLayerTiles(layerType).size()) {
                    System.err.println("Índice inválido: " + tileIndex + " na camada " + layerType);
                    continue;
                }
    
                Tile tile = tileManager.getTile(layerType, tileIndex);
                if (tile != null && tile.image != null) {
                    int x = col * GameConfig.TILE_SIZE - cameraX; // Ajuste pela câmera
                    int y = row * GameConfig.TILE_SIZE - cameraY; // Ajuste pela câmera
                    g2.drawImage(tile.image, x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE, null);
                }
            }
        }
    }

    private void validateMap() {
        validateLayer(baseLayer, 0);
        validateLayer(propLayer, 1);
    }
    
    private void validateLayer(int[][] layer, int layerType) {
        int maxIndex = tileManager.getLayerTiles(layerType).size() - 1;
        
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                int index = layer[row][col];
                if(index != -1 && (index < 0 || index > maxIndex)) {
                    throw new RuntimeException("Tile inválido na posição ["+row+"]["+col+"]: " + index + 
                                             " (Camada " + layerType + " aceita 0-" + maxIndex + ")");
                }
            }
        }
    }

    // Getters
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}