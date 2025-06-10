// GamePanel.java - panel do rysowania gry
package org.example.ui;

import org.example.classes.*;
import org.example.ui.MenuPanel.DifficultyLevel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
/// ////////////////////////////////////////////////////////////////WYGLĄD OKNA GRY SAMEJ W SOBIE
public class GamePanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {


    /// ////////////////////////////////////////////////PARAMETRY GRY:
    private GameEngine gameEngine;
    private InputHandler inputHandler;
    private Timer gameTimer;
    private DifficultyLevel difficulty;
    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 600;
    private final int DELAY = 16; // ~60 FPS

    /// /////////////////////////////////////////TEKSTURY POSTACI:
    private BufferedImage playerTexture;
    private BufferedImage skeletonTexture;

    /// //////////////////////////////////////PRZYCISKI PO PRZEGRANEJ:
    private Rectangle restartButton;
    private Rectangle menuButton;
    private boolean showButtons = false;

    //////////////////////////////////////////////////////////////////////KONSTRUKTORY I POZIOM TRUDNOSCI
    public GamePanel() {
        this(DifficultyLevel.EASY);
    }
    public GamePanel(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        loadTextures();
        initializeGame();

        // Dodaj mouse listener dla przycisków
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Uruchom timer gry
        gameTimer = new Timer(DELAY, this);
        gameTimer.start();
    }
    private void initializeGame() {
        // Inicjalizuj game engine z poziomem trudności
        gameEngine = new GameEngine();
        gameEngine.setDifficulty(difficulty); // Ustaw poziom trudności

        inputHandler = new InputHandler(gameEngine);
        gameEngine.setInputHandler(inputHandler);

        // Dodaj listenery
        this.removeKeyListener(inputHandler); // Usuń stary listener jeśli istnieje
        this.addKeyListener(inputHandler);
        this.addMouseListener(inputHandler); // Dodaj mouse listener dla strzelania

        // Ukryj przyciski
        showButtons = false;

        // Inicjalizuj przyciski
        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonX = (PANEL_WIDTH - buttonWidth) / 2;
        int restartY = PANEL_HEIGHT / 2 + 80;
        int menuY = PANEL_HEIGHT / 2 + 140;

        restartButton = new Rectangle(buttonX, restartY, buttonWidth, buttonHeight);
        menuButton = new Rectangle(buttonX, menuY, buttonWidth, buttonHeight);
    }



    /// ////////////////////////////////////////////////////////////TEKSTURY I WYGLĄD W GRZE
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (gameEngine.isGameRunning()) {
            drawPlayer(g2d);
            drawSkeletons(g2d);
            drawBullets(g2d);
            drawUI(g2d);
        } else {
            drawGameOver(g2d);
            if (showButtons) {
                drawRestartButton(g2d);
                drawMenuButton(g2d);
            }
        }
    }

    private void drawPlayer(Graphics2D g2d) {
        Player player = gameEngine.getPlayer();

        // Narysuj gracza jako koło
        int playerSize = 20;
        int x = (int) player.getX() - playerSize/2;
        int y = (int) player.getY() - playerSize/2;

        if (playerTexture != null) {
            g2d.drawImage(playerTexture, x, y, playerSize*2, playerSize*2, null);
        } else {
            g2d.setColor(Color.BLUE);
            g2d.fillOval(x, y, playerSize, playerSize);
        }

        // Narysuj HP gracza
        g2d.setColor(Color.GREEN);
        int hpBarWidth = (int) ((player.getCurrentHP() / (float) player.getMaxHP()) * 30);
        g2d.fillRect(x - 5, y - 10, hpBarWidth, 5);
    }

    private void drawSkeletons(Graphics2D g2d) {
        for (Skeleton skeleton : gameEngine.getSkeletons()) {
            int skeletonSize = 15;
            int x = (int) skeleton.getX() - skeletonSize/2;
            int y = (int) skeleton.getY() - skeletonSize/2;

            if (skeletonTexture != null) {
                g2d.drawImage(skeletonTexture, x, y, skeletonSize*2, skeletonSize*2, null);
            } else {
                g2d.setColor(Color.RED);
                g2d.fillRect(x, y, skeletonSize, skeletonSize);
            }

            // HP bar dla szkieleta
            g2d.setColor(Color.WHITE);
            int hpBarWidth = (int) ((skeleton.getHP() / 20.0f) * 20); // Zmienione z 50.0f na 20.0f
            g2d.fillRect(x, y - 8, hpBarWidth, 3);
        }
    }

    private void loadTextures() {
        try {
            // Załaduj tekstury z folderu resources
            playerTexture = ImageIO.read(getClass().getResourceAsStream("/gracz.png"));
            skeletonTexture = ImageIO.read(getClass().getResourceAsStream("/szkieletDoGry.png"));
            System.out.println("Tekstury załadowane pomyślnie!");
        } catch (IOException e) {
            System.err.println("Nie można załadować tekstur: " + e.getMessage());
            e.printStackTrace();
            // Jeśli nie można załadować, tekstury będą null i użyjemy prostych kształtów
        } catch (Exception e) {
            System.err.println("Błąd podczas ładowania tekstur: " + e.getMessage());
            // Tekstury pozostaną null
        }
    }

    private void drawBullets(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);

        for (Bullet bullet : gameEngine.getBullets()) {
            if (bullet.isActive()) {
                int bulletSize = 4;
                int x = (int) bullet.getX() - bulletSize/2;
                int y = (int) bullet.getY() - bulletSize/2;

                g2d.fillOval(x, y, bulletSize, bulletSize);
            }
        }
    }

    private void drawUI(Graphics2D g2d) {
        // Wyświetl wynik
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Score: " + gameEngine.getScore(), 10, 25);

        // Wyświetl poziom trudności
        g2d.drawString("Difficulty: " + difficulty.toString(), 10, 45);

        // Wyświetl instrukcje
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("WASD - Move, Mouse - Shoot", 10, PANEL_HEIGHT - 10);
    }

    private void drawGameOver(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g2d.getFontMetrics();
        String gameOverText = "GAME OVER";
        int x = (PANEL_WIDTH - fm.stringWidth(gameOverText)) / 2;
        int y = PANEL_HEIGHT / 2;
        g2d.drawString(gameOverText, x, y);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        fm = g2d.getFontMetrics();
        String scoreText = "Final Score: " + gameEngine.getScore();
        x = (PANEL_WIDTH - fm.stringWidth(scoreText)) / 2;
        g2d.drawString(scoreText, x, y + 40);

        // Pokaż przyciski po 2 sekundach
        if (!showButtons) {
            Timer buttonTimer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showButtons = true;
                    ((Timer) e.getSource()).stop();
                }
            });
            buttonTimer.setRepeats(false);
            buttonTimer.start();
        }
    }

    private void drawRestartButton(Graphics2D g2d) {
        // Narysuj przycisk restart
        g2d.setColor(new Color(0, 150, 0));
        g2d.fillRect(restartButton.x, restartButton.y, restartButton.width, restartButton.height);

        // Narysuj ramkę
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(restartButton.x, restartButton.y, restartButton.width, restartButton.height);

        // Narysuj tekst
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2d.getFontMetrics();
        String buttonText = "ZAGRAJ PONOWNIE";
        int textX = restartButton.x + (restartButton.width - fm.stringWidth(buttonText)) / 2;
        int textY = restartButton.y + (restartButton.height + fm.getAscent()) / 2;
        g2d.drawString(buttonText, textX, textY);
    }

    private void drawMenuButton(Graphics2D g2d) {
        // Narysuj przycisk menu
        g2d.setColor(new Color(150, 150, 0));
        g2d.fillRect(menuButton.x, menuButton.y, menuButton.width, menuButton.height);

        // Narysuj ramkę
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(menuButton.x, menuButton.y, menuButton.width, menuButton.height);

        // Narysuj tekst
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2d.getFontMetrics();
        String buttonText = "POWRÓT DO MENU";
        int textX = menuButton.x + (menuButton.width - fm.stringWidth(buttonText)) / 2;
        int textY = menuButton.y + (menuButton.height + fm.getAscent()) / 2;
        g2d.drawString(buttonText, textX, textY);
    }


    /// ///////////////////////////////////////////////////////////AKCJA
    @Override
    public void actionPerformed(ActionEvent e) {
        // Aktualizuj logikę gry
        if (gameEngine.isGameRunning()) {
            gameEngine.update();
        }
        // Zawsze odświeżaj ekran
        repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameEngine.isGameRunning() && showButtons) {
            Point clickPoint = e.getPoint();

            if (restartButton.contains(clickPoint)) {
                // Restart gry
                gameEngine.resetGame();
                showButtons = false;
                this.requestFocus(); // Przywróć focus do panelu gry
            } else if (menuButton.contains(clickPoint)) {
                // Powrót do menu
                firePropertyChange("returnToMenu", false, true);
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mouseDragged(MouseEvent e) {

    }
    @Override
    public void mouseMoved(MouseEvent e) {
        // Można dodać efekty hover dla przycisków w przyszłości
    }
}