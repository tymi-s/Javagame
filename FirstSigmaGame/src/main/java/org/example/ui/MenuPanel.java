package org.example.ui;

import org.example.classes.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
/// //////////////////////////////////////////////////////////////////////////// WYGLĄD OKNA MENU:
public class MenuPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

    /// /////////////////////////////////////////////////////////////////ZMIENNE:
    private GameEngine gameEngine;
    private InputHandler inputHandler;
    private Timer timer;

    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 600;
    private final int DELAY = 16;

    /// ////// PRZYCISKI POZIOMÓW TRÓDNOŚCI
    private Rectangle easyDiffButton;
    private Rectangle hardDiffButton;
    private Rectangle veryHardDiffButton;
    private Rectangle insaneDiffButton;


    private Font titleFont;
    private Font buttonFont;
    private Color buttonColor;
    private Color buttonHoverColor;
    private Color textColor;

    private Rectangle hoveredButton;
    private boolean gameStarted = false;
    private DifficultyLevel selectedDifficulty = DifficultyLevel.EASY;//// DOMYŚLNY POZIOM TRÓDNOSCI





    /// //////////////////////////////////////////////////////////////////METODY DO RYOSWANIA ITP:
    public MenuPanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(new Color(20, 20, 40)); // Ciemne tło
        this.setFocusable(true);

        initializeMenu();
        this.addMouseListener(this);
        this.addMouseMotionListener(this); // Dodane MouseMotionListener

        // Timer do odświeżania ekranu
        timer = new Timer(DELAY, this);
        timer.start();
    }
    private void initializeMenu() {

        titleFont = new Font("Arial", Font.BOLD, 48);
        buttonFont = new Font("Arial", Font.BOLD, 20);
        buttonColor = new Color(60, 60, 100);
        //buttonHoverColor = new Color(80, 80, 120);
        textColor = Color.WHITE;


        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonX = (PANEL_WIDTH - buttonWidth) / 2;
        int startY = 250;
        int buttonSpacing = 70;

        // Tworzenie przycisków (prostokątów reprezentujących przyciski)
        easyDiffButton = new Rectangle(buttonX, startY, buttonWidth, buttonHeight);
        hardDiffButton = new Rectangle(buttonX, startY + buttonSpacing, buttonWidth, buttonHeight);
        veryHardDiffButton = new Rectangle(buttonX, startY + buttonSpacing * 2, buttonWidth, buttonHeight);
        insaneDiffButton = new Rectangle(buttonX, startY + buttonSpacing * 3, buttonWidth, buttonHeight);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!gameStarted) {
            drawMenu(g2d);
        }
    }
    private void drawMenu(Graphics2D g2d) {
        // Rysowanie tytułu
        g2d.setFont(titleFont);
        g2d.setColor(textColor);
        String title = "WYBIERZ POZIOM TRUDNOŚCI";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (PANEL_WIDTH - fm.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 150);

        // Rysowanie przycisków
        drawButton(g2d, easyDiffButton, "ŁATWY", Color.GREEN);
        drawButton(g2d, hardDiffButton, "TRUDNY", Color.YELLOW);
        drawButton(g2d, veryHardDiffButton, "BARDZO TRUDNY", Color.ORANGE);
        drawButton(g2d, insaneDiffButton, "SZALONY", Color.RED);
    }
    private void drawButton(Graphics2D g2d, Rectangle button, String text, Color difficultyColor) {
        // Sprawdzenie czy przycisk jest najechany myszą
        Color currentButtonColor = (button.equals(hoveredButton)) ? buttonHoverColor : buttonColor;

        // Rysowanie przycisku
        g2d.setColor(currentButtonColor);
        g2d.fillRoundRect(button.x, button.y, button.width, button.height, 10, 10);

        // Ramka przycisku
        g2d.setColor(difficultyColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(button.x, button.y, button.width, button.height, 10, 10);

        // Tekst na przycisku
        g2d.setFont(buttonFont);
        g2d.setColor(textColor);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = button.x + (button.width - fm.stringWidth(text)) / 2;
        int textY = button.y + (button.height + fm.getAscent()) / 2;
        g2d.drawString(text, textX, textY);
    }
    private void startGame(DifficultyLevel difficulty) {
        selectedDifficulty = difficulty;
        gameStarted = true;

        // Informuj rodzica o rozpoczęciu gry
        firePropertyChange("startGame", false, true);
    }

    public DifficultyLevel getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public void resetMenu() {
        gameStarted = false;
        hoveredButton = null;
        repaint();
    }


    /// //////////////////////////////////////////////////////////////////////AKCJA:
    @Override
    public void actionPerformed(ActionEvent e) {
        // Timer - odświeżanie ekranu
        repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameStarted) return;

        Point clickPoint = e.getPoint();

        // Sprawdzenie który przycisk został kliknięty
        if (easyDiffButton.contains(clickPoint)) {
            startGame(DifficultyLevel.EASY);
        } else if (hardDiffButton.contains(clickPoint)) {
            startGame(DifficultyLevel.HARD);
        } else if (veryHardDiffButton.contains(clickPoint)) {
            startGame(DifficultyLevel.VERY_HARD);
        } else if (insaneDiffButton.contains(clickPoint)) {
            startGame(DifficultyLevel.INSANE);
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        // Można dodać efekt naciśnięcia przycisku
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        // Można dodać efekt zwolnienia przycisku
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        // Mysz weszła na panel
    }
    @Override
    public void mouseExited(MouseEvent e) {
        hoveredButton = null;
        repaint();
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        // Nie używane
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameStarted) return;

        Point mousePoint = e.getPoint();
        Rectangle previousHovered = hoveredButton;

        // Sprawdzenie nad którym przyciskiem jest mysz
        if (easyDiffButton.contains(mousePoint)) {
            hoveredButton = easyDiffButton;
        } else if (hardDiffButton.contains(mousePoint)) {
            hoveredButton = hardDiffButton;
        } else if (veryHardDiffButton.contains(mousePoint)) {
            hoveredButton = veryHardDiffButton;
        } else if (insaneDiffButton.contains(mousePoint)) {
            hoveredButton = insaneDiffButton;
        } else {
            hoveredButton = null;
        }

        // Odśwież tylko gdy stan się zmienił
        if (hoveredButton != previousHovered) {
            repaint();
        }
    }
    public enum DifficultyLevel {
        EASY, HARD, VERY_HARD, INSANE
    }
}