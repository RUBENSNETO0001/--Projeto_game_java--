package meujogo.aplicacao;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File; // Importado para lidar com arquivos
import java.io.IOException; // Importado para lidar com exceções de I/O
import java.awt.Desktop; // Importado para abrir arquivos com o aplicativo padrão

public class HomePrincipal extends JFrame {

    public HomePrincipal() {
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
            try {
                dispose(); // Fecha a tela de entrada
                new Container(); // Inicia o jogo imediatamente ao clicar "Começar Jogo"
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Erro ao iniciar jogo: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // NOVO BOTÃO DEDICADO PARA REPRODUZIR O VÍDEO
        JButton videoButton = createStyledButton("Assistir Introdução");
        videoButton.addActionListener(e -> {
            // Executa a abertura do vídeo em uma nova Thread para não travar a UI
            new Thread(() -> {
                openVideo(); // Chama o método para abrir o vídeo
            }).start();
        });

        JButton exitButton = createStyledButton("Sair");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(videoButton); // Adiciona o botão de vídeo
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

    private void openVideo() {
        File videoFile = new File("src/res/vid/inicio.mp4");

        if (Desktop.isDesktopSupported() && videoFile.exists()) {
            try {
                Desktop.getDesktop().open(videoFile);
                System.out.println("Vídeo de introdução aberto com sucesso!");
            } catch (IOException e) {
                System.err.println("Erro ao abrir o vídeo: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Erro ao abrir o vídeo: " + e.getMessage() +
                        "\nVerifique se o caminho do arquivo está correto e se você tem um reprodutor de vídeo padrão.",
                        "Erro ao Reproduzir Vídeo", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            String mensagemErro = "Não foi possível abrir o vídeo.";
            if (!Desktop.isDesktopSupported()) {
                mensagemErro += "\nRecurso Desktop não suportado pelo seu sistema operacional.";
            }
            if (!videoFile.exists()) {
                mensagemErro += "\nO arquivo de vídeo não foi encontrado: " + videoFile.getAbsolutePath();
            }
            System.out.println(mensagemErro);
            JOptionPane.showMessageDialog(this, mensagemErro, "Erro ao Reproduzir Vídeo", JOptionPane.WARNING_MESSAGE);
        }
    }

    // CLASSE INTERNA CORRIGIDA (removido o 's' extra)
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao carregar imagem de fundo: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                backgroundImage = null; // Garante que não haverá NullPointerException no paintComponent
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(Color.BLACK); // Se a imagem não carregar, preenche com preto
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public static void main(String[] args) {
        // Garante que a UI seja criada na Thread de despacho de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            new HomePrincipal();
        });
    }
}