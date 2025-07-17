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
        
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT: jogador.setX(jogador.getX() - 5); break;
            case KeyEvent.VK_RIGHT: jogador.setX(jogador.getX() + 5); break;
            case KeyEvent.VK_UP: jogador.setY(jogador.getY() - 5); break;
            case KeyEvent.VK_DOWN: jogador.setY(jogador.getY() + 5); break;
        }
        
        fase.repaint();
    }
    
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}