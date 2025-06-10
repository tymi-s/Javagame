package org.example.ui;
import org.example.ui.MenuPanel.DifficultyLevel;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/// /////////////////////////////////////////////////////////////////////////PRZEŁĄCZANIE MIEDZY OKNAMI PROGRAMU
public class GameWindow extends JFrame implements PropertyChangeListener {
    private LoginPanel loginPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private String currentPlayerNick; // Przechowuje nick zalogowanego gracza

    private static final String LOGIN_CARD = "LOGIN";
    private static final String MENU_CARD = "MENU";
    private static final String GAME_CARD = "GAME";

    public GameWindow() {

        ///  INICJALIZACJA BAZY
        DatabaseManager.initialize();

        initializeWindow();
        createComponents();
        setupCardLayout();
        finalizeWindow();


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseManager.close();
        }));
    }
    private void initializeWindow() {
        this.setTitle("SKELETON INVAZION - TIME OF JUSTICE");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }
    private void createComponents() {

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        loginPanel = new LoginPanel();
        loginPanel.addPropertyChangeListener(this);
        menuPanel = new MenuPanel();
        menuPanel.addPropertyChangeListener(this);
        mainPanel.add(loginPanel, LOGIN_CARD);
        mainPanel.add(menuPanel, MENU_CARD);


        this.add(mainPanel);
    }
    private void setupCardLayout() {
        cardLayout.show(mainPanel, LOGIN_CARD);
    }
    private void showMenu(String playerNick) {
        this.currentPlayerNick = playerNick;


        this.setTitle("2D Skeleton Shooter - Zalogowany: " + playerNick);
        cardLayout.show(mainPanel, MENU_CARD);
        menuPanel.requestFocus();
        System.out.println("Przejście do menu dla gracza: " + playerNick);
    }
    private void startGame(DifficultyLevel difficulty) {
        if (gamePanel != null) {
            mainPanel.remove(gamePanel);
        }


        gamePanel = new GamePanel(difficulty);
        gamePanel.addPropertyChangeListener(this);


        mainPanel.add(gamePanel, GAME_CARD);


        cardLayout.show(mainPanel, GAME_CARD);


        gamePanel.requestFocus();

        System.out.println("Gra rozpoczęta na poziomie: " + difficulty + " dla gracza: " + currentPlayerNick);
    }
    private void returnToMenu() {

        cardLayout.show(mainPanel, MENU_CARD);


        menuPanel.requestFocus();


        if (gamePanel != null) {
            mainPanel.remove(gamePanel);
            gamePanel = null;
            System.gc();
        }

        System.out.println("Powrót do menu");
    }
    private void returnToLogin() {

        this.currentPlayerNick = null;
        this.setTitle("SKELETON INVASION - TIME OF JUSTICE");

        loginPanel.resetLoginPanel();


        cardLayout.show(mainPanel, LOGIN_CARD);


        loginPanel.requestFocus();


        if (gamePanel != null) {
            mainPanel.remove(gamePanel);
            gamePanel = null;
            System.gc();
        }

        System.out.println("Powrót do ekranu logowania");
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("loginSuccess".equals(evt.getPropertyName())) {

            String playerNick = (String) evt.getNewValue();
            showMenu(playerNick);
        } else if ("startGame".equals(evt.getPropertyName())) {

            DifficultyLevel difficulty = menuPanel.getSelectedDifficulty();
            startGame(difficulty);
        } else if ("returnToMenu".equals(evt.getPropertyName())) {

            returnToMenu();
        } else if ("logout".equals(evt.getPropertyName())) {

            returnToLogin();
        }
    }
    private void finalizeWindow() {
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


        SwingUtilities.invokeLater(() -> {
            loginPanel.requestFocus();
        });
    }
    public String getCurrentPlayerNick() {
        return currentPlayerNick;
    }
}