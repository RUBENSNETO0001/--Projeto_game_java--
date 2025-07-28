package meujogo.aplicacao;

import javax.swing.*;
import meujogo.ui.Container;
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
            SwingUtilities.invokeLater(Container::new);
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
        File videoFile = new File("res/vid/inicio.mp4");

        if (!Desktop.isDesktopSupported()) {
            JOptionPane.showMessageDialog(this, "Recurso Desktop não suportado pelo sistema.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!videoFile.exists()) {
            JOptionPane.showMessageDialog(this, "O arquivo de vídeo não foi encontrado: " + videoFile.getAbsolutePath(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Desktop.getDesktop().open(videoFile);
            System.out.println("Vídeo aberto com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao abrir vídeo: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            Image tempImage = null;
            try {
                tempImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
            } catch (Exception e) {
                System.err.println("Erro ao carregar imagem de fundo: " + e.getMessage());
            }
            this.backgroundImage = tempImage;
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