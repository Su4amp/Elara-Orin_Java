package world;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.GamePanel;

public class TileManager {
    private final GamePanel gp;
    private final Map<Integer, List<Tile>> layers = new HashMap<>();
    
    public TileManager(GamePanel gp) {
        this.gp = gp;
        layers.put(0, new ArrayList<>()); // Camada base
        layers.put(1, new ArrayList<>()); // Camada de props
    }

    public void loadTiles(String configPath) {
        try (InputStream is = TileManager.class.getResourceAsStream(configPath)) {
            if(is == null) throw new IOException("Arquivo não encontrado: " + configPath);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            
            while ((line = br.readLine()) != null) {
                line = line.split("#")[0].trim();
                if(line.isEmpty()) continue;
                
                String[] data = line.split(",");
                if(data.length < 4) continue;

                int layer = Integer.parseInt(data[0].trim());
                int index = Integer.parseInt(data[1].trim());
                
                // Verificar se a camada existe
                if(!layers.containsKey(layer)) {
                    layers.put(layer, new ArrayList<>());
                }
                
                // Garantir que o índice seja válido
                while(layers.get(layer).size() <= index) {
                    layers.get(layer).add(null);
                }
                
                Tile tile = new Tile();
                tile.layer = layer;
                
                String imagePath = data[2].trim();
                InputStream imgStream = TileManager.class.getResourceAsStream(imagePath);
                if(imgStream == null) {
                    System.err.println("Imagem não encontrada: " + imagePath);
                    continue;
                }
                tile.image = ImageIO.read(imgStream);
                tile.collision = Boolean.parseBoolean(data[3].trim());
                
                layers.get(layer).set(index, tile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tile getTile(int layer, int index) {
        if(index < 0 || index >= layers.get(layer).size()) return null;
        return layers.get(layer).get(index);
    }
    
    public List<Tile> getLayerTiles(int layer) {
        return layers.get(layer);
    }
}