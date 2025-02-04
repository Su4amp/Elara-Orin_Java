package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class InputHandler implements KeyListener {
    
    private final boolean[] keys = new boolean[256];
    private final boolean[] prevKeys = new boolean[256];
    
    // Códigos das teclas mapeados (melhor que usar KeyEvent diretamente)
    public static final int KEY_W = KeyEvent.VK_W;
    public static final int KEY_A = KeyEvent.VK_A;
    public static final int KEY_S = KeyEvent.VK_S;
    public static final int KEY_D = KeyEvent.VK_D;
    public static final int KEY_ESPACO = KeyEvent.VK_SPACE;
    public static final int KEY_ESC = KeyEvent.VK_ESCAPE;
    
    public InputHandler(JFrame frame) {
        frame.addKeyListener(this);
        // Garante que o JFrame vai receber os eventos de teclado
        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }
    
    // Atualiza o estado anterior das teclas (chamar no início de cada frame)
    public void update() {
        System.arraycopy(keys, 0, prevKeys, 0, keys.length);
    }
    
    // Verifica se a tecla está pressionada
    public boolean isKeyDown(int keyCode) {
        if(keyCode >= 0 && keyCode < keys.length) {
            return keys[keyCode];
        }
        return false;
    }
    
    // Verifica se a tecla foi pressionada neste exato momento
    public boolean isKeyPressed(int keyCode) {
        if(keyCode >= 0 && keyCode < keys.length) {
            return keys[keyCode] && !prevKeys[keyCode];
        }
        return false;
    }
    
    // Verifica se a tecla foi solta
    public boolean isKeyReleased(int keyCode) {
        if(keyCode >= 0 && keyCode < keys.length) {
            return !keys[keyCode] && prevKeys[keyCode];
        }
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Não necessário para jogos, mas mantido pela interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code >= 0 && code < keys.length) {
            keys[code] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code >= 0 && code < keys.length) {
            keys[code] = false;
        }
    }
}