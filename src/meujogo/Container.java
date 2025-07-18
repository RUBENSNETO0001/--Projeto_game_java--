package meujogo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import meujogo.controller.Teclado;
import meujogo.fase.Fase;
import meujogo.modelo.God;
import meujogo.modelo.Personagem;

public class Container extends JFrame {
    private Fase fase;
    private Timer gameLoop;

    public Container() {
        fase = new Fase();

        God deus = new God("─═  𝔤𝔬𝔡 ═─", "posseprincipal.png");
        deus.setX(100);
        deus.setY(100);
        fase.getPersonagens().add(deus);

        addKeyListener(new Teclado(fase));
        setFocusable(true);
        add(fase);

        setTitle("God Game");
        setSize(1200, 708);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        // Game loop
        gameLoop = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fase.todasAlmasColetadas()) {
                    vitoria();
                    return;
                }
                fase.atualizar();
                
                for (Personagem p : fase.getPersonagens()) {
                    if (p instanceof God && ((God)p).isMorto()) {
                        gameOver();
                        break;
                    }
                }
            }
        });
        gameLoop.start();
        fase.setFocusable(true);
        fase.requestFocusInWindow();
    }

    private void vitoria() {
        gameLoop.stop();
        JOptionPane.showMessageDialog(this, 
            "Parabéns! Você coletou todas as almas!\nVocê venceu o jogo!", 
            "Vitória", 
            JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void gameOver() {
        gameLoop.stop();
        JOptionPane.showMessageDialog(this, "Game Over! O cometa te matou!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja jogar novamente?", "Reiniciar", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            reiniciarJogo();
        } else {
            System.exit(0);
        }
    }

    private void reiniciarJogo() {
        getContentPane().removeAll();
        fase = new Fase();
        God deus = new God("─═𝔤𝔬𝔡═─", "posseprincipal.png");
        deus.setX(100);
        deus.setY(100);
        fase.getPersonagens().add(deus);

        addKeyListener(new Teclado(fase));
        add(fase);
        revalidate();
        repaint();
        gameLoop.start();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new Container());
    }
}