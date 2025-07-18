package meujogo;

import java.awt.*;
import javax.swing.*;

public class GameEntryScreen extends JFrame {

    public GameEntryScreen() {
        setTitle("Home - Entrada do Jogo");
        setSize(1200, 708);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        BackgroundPanel backgroundPanel = new BackgroundPanel("/res/backgroundHome/backgroundHome.png");
        backgroundPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 30));
        buttonPanel.setOpaque(false);

        JButton startButton = createStyledButton("Começar Jogo");
       startButton.addActionListener(e -> {
    dispose();
    try {
        VLCJVideoPlayer.launchVideo("intro.mp4", () -> {
            SwingUtilities.invokeLater(() -> new Container());
        });
    } catch (Exception ex) {
        ex.printStackTrace();
        // Fallback direto para o jogo se houver erro
        new Container().setVisible(true);
    }
});

        JButton exitButton = createStyledButton("Sair");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(backgroundPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180, 200));
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setFocusPainted(false);
        button.setRequestFocusEnabled(false);
        button.setFocusable(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200, 220));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180, 200));
            }
        });

        return button;
    }

    class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao carregar imagem: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                backgroundImage = null;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameEntryScreen();
        });
    }
}
