package meujogo;

import javax.swing.JFrame;
import meujogo.modelo.Fase;
import meujogo.modelo.Personagem;

public class Container extends JFrame {
    public Container() {
        Fase fase = new Fase(); // Cria uma nova fase
        // Adiciona personagens à fase
        fase.getPersonagens().add(new Personagem("god", 100, 15, 5));
        fase.getPersonagens().add(new Personagem("EstelaMorta", 80, 10, 8));
        
        add(fase); // Adiciona o painel do jogo
        
        setTitle("Jogo Beta Em Java");
        setSize(1200, 708);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new Container());
    }
}