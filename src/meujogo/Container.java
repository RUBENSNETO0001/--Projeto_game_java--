package meujogo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import meujogo.controller.Teclado;
import meujogo.fase.Fase;
import meujogo.modelo.God;

public class Container extends JFrame {
    private Fase fase;
    private Timer gameLoop;

    public Container() {
        initGame();
    }

    private void initGame() {
        setTitle("God Game");
        setSize(1200, 708);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        fase = new Fase();
        God deus = new God("─═God═─", "posseprincipal.png");
        deus.setX(100);
        deus.setY(100);
        fase.getPersonagens().add(deus);

        add(fase);
        addKeyListener(new Teclado(fase));
        
        fase.setFocusable(true);
        fase.requestFocusInWindow();

        gameLoop = new Timer(16, this::updateGame);
        gameLoop.start();
        
        setVisible(true);
    }

    private void updateGame(ActionEvent e) {
        if (fase.todasAlmasColetadas()) {
            vitoria();
            return;
        }
        
        fase.atualizar();
        
        for (var p : fase.getPersonagens()) {
            if (p instanceof God && ((God)p).isMorto()) {
                gameOver();
                break;
            }
        }
    }

    private void vitoria() {
        gameLoop.stop();
        JOptionPane.showMessageDialog(this, 
            "Parabéns! Você coletou todas as almas!\nVocê venceu o jogo!", 
            "Vitória", 
            JOptionPane.INFORMATION_MESSAGE);
        restartApplication();
    }

    private void gameOver() {
        gameLoop.stop();
        int resposta = JOptionPane.showConfirmDialog(this, 
            "Game Over! O cometa te matou!\nDeseja jogar novamente?", 
            "Fim de Jogo", 
            JOptionPane.YES_NO_OPTION);
        
        if (resposta == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            restartApplication();
        }
    }

    private void restartGame() {
        getContentPane().removeAll();
        fase = new Fase();
        God deus = new God("─═God═─", "posseprincipal.png");
        deus.setX(100);
        deus.setY(100);
        fase.getPersonagens().add(deus);

        add(fase);
        addKeyListener(new Teclado(fase));
        fase.setFocusable(true);
        fase.requestFocusInWindow();
        
        revalidate();
        repaint();
        gameLoop.start();
    }

    private void restartApplication() {
        dispose();
        new HomePrincipal();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Container());
    }
}