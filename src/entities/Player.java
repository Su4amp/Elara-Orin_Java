package entities;

import core.GameConfig;
import input.InputHandler;
import utils.Animation;
import utils.Direction;
import world.CollisionChecker;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

public class Player {
    private int worldX, worldY;
    private final int speed;
    private Direction facingDirection;
    private boolean moving;
    private EnumMap<Direction, Animation> idleAnimations;
    private EnumMap<Direction, Animation> moveAnimations;
    private Animation currentAnimation;
    private CollisionChecker collisionChecker;

    public Player(int startX, int startY) {
        this.worldX = startX;
        this.worldY = startY;
        this.speed = GameConfig.TILE_SIZE / 8;
        this.facingDirection = Direction.DOWN;
        this.moving = false;
        loadAnimations();
        currentAnimation = idleAnimations.get(facingDirection);
    }

    public void setCollisionChecker(CollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
    }

    private void loadAnimations() {
        idleAnimations = new EnumMap<>(Direction.class);
        moveAnimations = new EnumMap<>(Direction.class);

        idleAnimations.put(Direction.UP, new Animation(loadSprites("UP", "Upidle", 4), 200, true));
        idleAnimations.put(Direction.DOWN, new Animation(loadSprites("DOWN", "Downidle", 4), 200, true));
        idleAnimations.put(Direction.LEFT, new Animation(loadSprites("LEFT", "Leftidle", 4), 200, true));
        idleAnimations.put(Direction.RIGHT, new Animation(loadSprites("RIGHT", "Rightidle", 4), 200, true));

        moveAnimations.put(Direction.UP, new Animation(loadSprites("UP", "Up", 4), 250, true));
        moveAnimations.put(Direction.DOWN, new Animation(loadSprites("DOWN", "Down", 4), 250, true));
        moveAnimations.put(Direction.LEFT, new Animation(loadSprites("LEFT", "Left", 4), 250, true));
        moveAnimations.put(Direction.RIGHT, new Animation(loadSprites("RIGHT", "Right", 4), 250, true));
    }

    private BufferedImage[] loadSprites(String direction, String prefix, int count) {
        BufferedImage[] sprites = new BufferedImage[count];
        try {
            for (int i = 0; i < count; i++) {
                String path = String.format("/resources/sprites/Player/%s/%s%d.png", direction, prefix, i + 1);
                sprites[i] = javax.imageio.ImageIO.read(getClass().getResourceAsStream(path));
                if (sprites[i] == null) {
                    System.err.println("Erro ao carregar imagem: " + path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sprites;
    }

    public void update(InputHandler inputHandler) {
        handleMovement(inputHandler);
        updateAnimationState();
        currentAnimation.update();
    }

    private void handleMovement(InputHandler input) {
        int newX = worldX;
        int newY = worldY;
        boolean moved = false;
        Direction newDirection = facingDirection;

        if (input.isKeyDown(InputHandler.KEY_W)) {
            newY -= speed;
            newDirection = Direction.UP;
            moved = true;
        } else if (input.isKeyDown(InputHandler.KEY_S)) {
            newY += speed;
            newDirection = Direction.DOWN;
            moved = true;
        } else if (input.isKeyDown(InputHandler.KEY_A)) {
            newX -= speed;
            newDirection = Direction.LEFT;
            moved = true;
        } else if (input.isKeyDown(InputHandler.KEY_D)) {
            newX += speed;
            newDirection = Direction.RIGHT;
            moved = true;
        }

        if (!collisionChecker.checkCollision(newX, newY)) {
            worldX = newX;
            worldY = newY;
        }

        moving = moved;
        if (moved) facingDirection = newDirection;
    }

    private void updateAnimationState() {
        if (moving) {
            currentAnimation = moveAnimations.get(facingDirection);
        } else {
            currentAnimation = idleAnimations.get(facingDirection);
        }
    }

    public void draw(Graphics2D g2, int cameraX, int cameraY) {
        int screenX = worldX - cameraX;
        int screenY = worldY - cameraY;
    
        // Desenhar o jogador
        g2.drawImage(currentAnimation.getCurrentFrame(), screenX, screenY, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE, null);
    
        // Desenhar o retângulo de colisão do jogador (para depuração)
        g2.setColor(java.awt.Color.GREEN); // Cor do retângulo de colisão do jogador
        g2.drawRect(screenX, screenY, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
    }

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }
}