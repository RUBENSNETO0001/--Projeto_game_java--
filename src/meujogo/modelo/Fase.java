package meujogo.modelo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Fase extends JPanel {
    private Image background;
    private List<Personagem> personagens;

    public Fase() {
        personagens = new ArrayList<>();
        
        // Carrega background
        java.net.URL imgURL = getClass().getResource("/res/background.jpeg");
        if (imgURL != null) {
            background = new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("Erro: Imagem de fundo não encontrada!");
            background = null;
            setBackground(Color.BLACK);
        }
    }

    public List<Personagem> getPersonagens() {
        return personagens;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D grafico = (Graphics2D) g;
        
        // Desenha o background
        if (background != null) {
            grafico.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
        
        // Desenha os personagens
        for (Personagem p : personagens) {
            desenharPersonagem(grafico, p);
        }
    }
    
    private void desenharPersonagem(Graphics2D g, Personagem p) {
        // Implementação básica de desenho
        g.setColor(Color.RED);
        g.fillRect(100, 100, 50, 50); // Exemplo - substitua por sprite real
        g.setColor(Color.WHITE);
        g.drawString(p.getNome(), 100, 90);
        
        // Barra de vida
        g.setColor(Color.RED);
        g.fillRect(100, 80, 50, 5);
        g.setColor(Color.GREEN);
        g.fillRect(100, 80, (int)(50 * ((double)p.getVida()/100)), 5);
    }
}