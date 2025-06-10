package org.example.classes;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/// ///////////////////////////////////////////////////////////////////////////////// KLAWIATURA:

public class InputHandler implements KeyListener, MouseListener{
    private boolean[] keys = new boolean[256];
    private GameEngine gameEngine;
    private Player player;

    public InputHandler(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.player = gameEngine.getPlayer();
    }

    /// RUCH GRACZA
    public void update() {
        /// Ruch gracza
        int moveSpeed = player.getMoveSpeed();

        if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) {
             player.setY(player.getY() - moveSpeed);
        }
        if (keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]) {
             player.setY(player.getY() + moveSpeed);
        }
        if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) {
             player.setX(player.getX() - moveSpeed);
        }
        if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) {
             player.setX(player.getX() + moveSpeed);
        }
    }

    /// //////////////////////////////////////////////////////////////////PRZYCISKI
    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        ///Strzelanie w kierunku kursora myszy
        gameEngine.shoot(e.getX(), e.getY());
    }
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
