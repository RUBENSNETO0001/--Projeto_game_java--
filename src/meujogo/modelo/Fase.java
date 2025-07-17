package meujogo.modelo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Fase extends JPanel {
    private Image background;
    private List<Personagem> personagens;

    public Fase() {
        personagens = new ArrayList<>();
        setBackground(Color.BLACK);
        
        try {
            background = new ImageIcon(getClass().getResource("/res/background/background.jpeg")).getImage();
        } catch (Exception e) {
            System.err.println("Erro ao carregar background: " + e.getMessage());
            background = null;
        }
    }

    public List<Personagem> getPersonagens() {
        return personagens;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D grafico = (Graphics2D) g;
        
        // Desenha background
        if (background != null) {
            grafico.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
        
        // Desenha personagens
        for (Personagem p : personagens) {
            if (p instanceof God) {
                God god = (God) p;
                god.verificarFinalDaFase(this);
            }
            desenharPersonagem(grafico, p);
        }
    }
    
    private void desenharPersonagem(Graphics2D g, Personagem p) {
        if (p instanceof God) {
            God god = (God) p;
            BufferedImage img = god.getImagem();
            if (img != null) {
                g.drawImage(img, p.getX(), p.getY(), 50, 50, null);
            }
        } else {
            g.setColor(Color.RED);
            g.fillRect(p.getX(), p.getY(), 50, 50);
        }
        
        // Barra de vida
        g.setColor(Color.RED);
        g.fillRect(p.getX(), p.getY() - 10, 50, 5);
        g.setColor(Color.GREEN);
        g.fillRect(p.getX(), p.getY() - 10, (int)(50 * ((double)p.getVida()/100)), 5);
        
        // Nome
        g.setColor(Color.WHITE);
        g.drawString(p.getNome(), p.getX(), p.getY() - 15);
    }
}