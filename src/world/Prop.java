// Prop.java
package world;

import java.awt.image.BufferedImage;

public class Prop {
    public BufferedImage image;
    public int worldX;
    public int worldY;
    public int layer;
    
    public Prop(BufferedImage image, int x, int y, int layer) {
        this.image = image;
        this.worldX = x;
        this.worldY = y;
        this.layer = layer;
    }
}