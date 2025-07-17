package meujogo;

import javax.swing.JFrame;
import meujogo.modelo.Fase;
import meujogo.modelo.God;
import meujogo.modelo.Teclado;

public class Container extends JFrame {
    public Container() {
        Fase fase = new Fase();
        
        God deus = new God("─═  𝔤𝔬𝔡 ═─", 100, 30, 20, "god.png");
        deus.setX(100);
        deus.setY(100);
        fase.getPersonagens().add(deus);

        addKeyListener(new Teclado(fase));
        setFocusable(true);
        add(fase);

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