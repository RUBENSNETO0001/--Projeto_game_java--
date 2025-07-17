package meujogo.modelo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Teclado implements KeyListener {
    private Fase fase;
    
    public Teclado(Fase fase) {
        this.fase = fase;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (fase.getPersonagens().isEmpty()) return;

        Personagem jogador = fase.getPersonagens().get(0);
        int novaX = jogador.getX();
        int novaY = jogador.getY();
        
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  novaX -= 5; break;
            case KeyEvent.VK_RIGHT: novaX += 5; break;
            case KeyEvent.VK_UP:    novaY -= 5; break;
            case KeyEvent.VK_DOWN:  novaY += 5; break;
        }
        
        // Verifica limites da tela
        if (novaX >= 0 && novaX <= fase.getWidth() - 50) {  // 50 é a largura do personagem
            jogador.setX(novaX);
        }
        
        if (novaY >= 0 && novaY <= fase.getHeight() - 50) {  // 50 é a altura do personagem
            jogador.setY(novaY);
        }
        
        fase.repaint();
    }
    
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}