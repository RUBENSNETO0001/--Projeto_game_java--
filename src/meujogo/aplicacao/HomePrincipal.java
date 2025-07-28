package meujogo.aplicacao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.awt.Desktop;

public class HomePrincipal extends JFrame {

    public HomePrincipal() {
        initUI();
    }

    private void initUI() {
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
            new Container();
        });

        JButton videoButton = createStyledButton("Assistir Introdução");
        videoButton.addActionListener(e -> new Thread(this::openVideo).start());

        JButton exitButton = createStyledButton("Sair");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(videoButton);
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

    private void openVideo() {
        File videoFile = new File("src/res/vid/inicio.mp4");

        if (!Desktop.isDesktopSupported() || !videoFile.exists()) {
            String errorMsg = "Não foi possível abrir o vídeo.";
            if (!Desktop.isDesktopSupported()) {
                errorMsg += "\nRecurso Desktop não suportado.";
            }
            if (!videoFile.exists()) {
                errorMsg += "\nArquivo não encontrado: " + videoFile.getAbsolutePath();
            }
            JOptionPane.showMessageDialog(this, errorMsg, "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Desktop.getDesktop().open(videoFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao abrir vídeo: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao carregar imagem de fundo: " + e.getMessage(), e);
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
        SwingUtilities.invokeLater(HomePrincipal::new);
    }
}