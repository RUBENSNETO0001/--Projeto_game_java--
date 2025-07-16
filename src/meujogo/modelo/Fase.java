package meujogo.modelo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Fase extends JPanel {

    private Image background;

    public Fase() {
        // Tenta carregar a imagem de fundo
        java.net.URL imgURL = getClass().getResource("/res/background.jpg");
        if (imgURL != null) {
            background = new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("Erro: Imagem de fundo não encontrada!");
            background = null;
            setBackground(Color.BLACK);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            Graphics2D grafico = (Graphics2D) g;
            grafico.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
        // Se background for null, o fundo será preto (setBackground)
    }
}