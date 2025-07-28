package meujogo.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import meujogo.fase.Fase;
import meujogo.modelo.Personagem;

public class Teclado implements KeyListener {
    private final Fase fase;
    private final boolean[] teclasPressionadas = new boolean[256];

    public Teclado(Fase fase) {
        this.fase = fase;
        fase.setFocusable(true);
        fase.requestFocusInWindow();
        fase.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (fase.getPersonagens().isEmpty() || !fase.isJogoAtivo()) return;

        teclasPressionadas[e.getKeyCode()] = true;
        atualizarMovimento();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        teclasPressionadas[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void atualizarMovimento() {
        Personagem jogador = fase.getPersonagens().get(0);
        int novaX = jogador.getX();
        int novaY = jogador.getY();

        if (teclasPressionadas[KeyEvent.VK_LEFT]) novaX -= 7;
        if (teclasPressionadas[KeyEvent.VK_RIGHT]) novaX += 7;
        if (teclasPressionadas[KeyEvent.VK_UP]) novaY -= 7;
        if (teclasPressionadas[KeyEvent.VK_DOWN]) novaY += 7;

        if (novaX >= 0 && novaX <= fase.getWidth() - 50) {
            jogador.setX(novaX);
        }

        if (novaY >= 0 && novaY <= fase.getHeight() - 50) {
            jogador.setY(novaY);
        }

        fase.repaint();
    }
}