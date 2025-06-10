package org.example;

import org.example.ui.GameWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameWindow();
        });
    }
}