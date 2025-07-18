package meujogo.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import meujogo.fase.Fase;
import meujogo.modelo.Personagem;

public class Teclado implements KeyListener {
    private Fase fase;
    
    public Teclado(Fase fase) {
        this.fase = fase;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (fase.getPersonagens().isEmpty() || !fase.isJogoAtivo()) return;

        Personagem jogador = fase.getPersonagens().get(0);
        int novaX = jogador.getX();
        int novaY = jogador.getY();
        
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  novaX -= 7; break;
            case KeyEvent.VK_RIGHT: novaX += 7; break;
            case KeyEvent.VK_UP:    novaY -= 7; break;
            case KeyEvent.VK_DOWN:  novaY += 7; break;
        }
        
        // Verifica limites da tela
        if (novaX >= 0 && novaX <= fase.getWidth() - 50) {
            jogador.setX(novaX);
        }
        
        if (novaY >= 0 && novaY <= fase.getHeight() - 50) {
            jogador.setY(novaY);
        }
        
        fase.repaint();
    }
    
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}