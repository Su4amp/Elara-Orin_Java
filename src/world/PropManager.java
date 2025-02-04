// PropManager.java
package world;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import core.GameConfig;

public class PropManager {
    private final List<Prop> props = new ArrayList<>();
    
    public void addProp(Prop prop) {
        props.add(prop);
        // Ordenar por layer quando adicionar novos props
        props.sort(Comparator.comparingInt(p -> p.layer));
    }
    
    public void draw(Graphics2D g2) {
        for(Prop prop : props) {
            int x = prop.worldX * GameConfig.TILE_SIZE;
            int y = prop.worldY * GameConfig.TILE_SIZE;
            g2.drawImage(prop.image, x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE, null);
        }
    }
}