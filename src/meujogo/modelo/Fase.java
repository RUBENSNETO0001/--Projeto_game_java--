package meujogo.modelo;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Fase extends JPanel{

// eu privei o fundo para poder acessa ele depois
private Image background;

//construtor simples
public Fase() {
    // Corrigido para usar / em vez de \\ (funciona em todos os sistemas operacionais)
    ImageIcon referencia = new ImageIcon(getClass().getResource("/res/background.jpg"));
    background = referencia.getImage();
   }


//paint e pinta mais aqui no contexto eu quero printar o fundo

@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g); // Isso limpa o painel antes de desenhar

    Graphics2D grafico = (Graphics2D) g;

    grafico.drawImage(background, 0, 0, null);
}
}