package world;

import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;
    public boolean collision = false;
    public int layer = 0; // 0 = base, 1 = props, 2 = overlay
}