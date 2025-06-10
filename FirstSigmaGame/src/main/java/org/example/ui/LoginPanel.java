package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/// /////////////////////////////////////////////////////////////WYGLĄD OKNA LOGOWANIA
///
///
///
public class LoginPanel extends JPanel implements ActionListener, KeyListener {


    /// /////////////////////////////////////////////////////////ZMIENNE:
    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 600;

    private JTextField nickField;
    private JButton loginButton;
    private JLabel statusLabel;

    /// CZCIONKA I KOLORY
    private Font titleFont;
    private Font labelFont;
    private Font buttonFont;


    /// //////////////////////////////////////////////////////METODY:
    public LoginPanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(new Color(30, 30, 50));
        this.setLayout(null); // Absolute positioning

        initializeFonts();
        createComponents();
        layoutComponents();

        this.setFocusable(true);
        this.requestFocus();
    }
    private void initializeFonts() {
        titleFont = new Font("Arial", Font.BOLD, 36);
        labelFont = new Font("Arial", Font.PLAIN, 18);
        buttonFont = new Font("Arial", Font.BOLD, 16);
    }
    private void createComponents() {

        nickField = new JTextField();
        nickField.setFont(labelFont);
        nickField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        nickField.addKeyListener(this);

        // Przycisk logowania
        loginButton = new JButton("ZALOGUJ");
        loginButton.setFont(buttonFont);
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);

        loginButton.setFocusPainted(false);
        loginButton.addActionListener(this);

        // Label do wyświetlania statusu
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(labelFont);
        statusLabel.setForeground(Color.WHITE);
    }
    private void layoutComponents() {
        // Tytuł będzie rysowany w paintComponent

        // Pole tekstowe
        int fieldWidth = 300;
        int fieldHeight = 40;
        int fieldX = (PANEL_WIDTH - fieldWidth) / 2;
        int fieldY = 280;

        nickField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        this.add(nickField);

        // Przycisk
        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonX = (PANEL_WIDTH - buttonWidth) / 2;
        int buttonY = fieldY + fieldHeight + 30;

        loginButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        this.add(loginButton);

        // Status label
        int statusY = buttonY + buttonHeight + 20;
        statusLabel.setBounds(50, statusY, PANEL_WIDTH - 100, 30);
        this.add(statusLabel);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tytuł
        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        String title = "2D SKELETON SHOOTER";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (PANEL_WIDTH - fm.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 150);

        // Podtytuł
        g2d.setFont(labelFont);
        String subtitle = "Wpisz swój nick, aby rozpocząć grę";
        fm = g2d.getFontMetrics();
        int subtitleX = (PANEL_WIDTH - fm.stringWidth(subtitle)) / 2;
        g2d.drawString(subtitle, subtitleX, 200);

        // Label dla pola tekstowego
        String fieldLabel = "Nick gracza:";
        fm = g2d.getFontMetrics();
        int labelX = (PANEL_WIDTH - fieldLabel.length() * 8) / 2;
        g2d.drawString(fieldLabel, labelX, 270);
    }



    /// //////////////////////////////////////////////////////////AKCJA:
    private void performLogin() {
        String nick = nickField.getText().trim();

        if (nick.isEmpty()) {
            statusLabel.setText("Wpisz nick!");
            statusLabel.setForeground(Color.RED);
            return;
        }

        if (nick.length() < 3) {
            statusLabel.setText("Nick musi mieć przynajmniej 3 znaki!");
            statusLabel.setForeground(Color.RED);
            return;
        }

        if (nick.length() > 20) {
            statusLabel.setText("Nick nie może być dłuższy niż 20 znaków!");
            statusLabel.setForeground(Color.RED);
            return;
        }

        // Sprawdź czy nick zawiera tylko dozwolone znaki
        if (!nick.matches("[a-zA-Z0-9_-]+")) {
            statusLabel.setText("Nick może zawierać tylko litery, cyfry, _ i -");
            statusLabel.setForeground(Color.RED);
            return;
        }

        statusLabel.setText("Logowanie...");
        statusLabel.setForeground(Color.YELLOW);

        // Stworzenie gracza w bazie jeśli nie istnieje
        boolean playerCreated = DatabaseManager.createPlayerIfNotExists(nick);
        if (!playerCreated) {
            statusLabel.setText("Błąd podczas tworzenia/pobierania gracza!");
            statusLabel.setForeground(Color.RED);
            return;
        }

        // Zapisz sesję logowania
        boolean sessionSaved = DatabaseManager.saveLoginSession(nick);
        if (!sessionSaved) {
            statusLabel.setText("Błąd podczas zapisywania sesji logowania!");
            statusLabel.setForeground(Color.RED);
            return;
        }

        statusLabel.setText("Logowanie pomyślne! Witaj " + nick + "!");
        statusLabel.setForeground(Color.GREEN);

        // Poinformuj rodzica o pomyślnym logowaniu
        Timer delay = new Timer(1500, e -> {
            firePropertyChange("loginSuccess", null, nick);
            ((Timer) e.getSource()).stop();
        });
        delay.setRepeats(false);
        delay.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            performLogin();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            performLogin();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    public void resetLoginPanel() {
        nickField.setText("");
        statusLabel.setText("");
        nickField.requestFocus();
    }
}