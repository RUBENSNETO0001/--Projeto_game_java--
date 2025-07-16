package meujogo;

import javax.swing.JFrame;
import meujogo.modelo.Fase;

public class Container extends JFrame {
    public Container() {
        // Adiciona o painel do jogo
        add(new Fase());
        // Título da janela
        setTitle("Jogo Beta Em Java");
        // Tamanho da janela
        setSize(1200, 708);
        // Fecha o programa ao fechar a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Centraliza a janela na tela
        setLocationRelativeTo(null);
        // Impede redimensionamento
        setResizable(false);
        // Torna a janela visível
        setVisible(true);
    }

    public static void main(String[] args) {
        // Garante que a interface gráfica seja criada na thread correta
        javax.swing.SwingUtilities.invokeLater(() -> new Container());
    }
}