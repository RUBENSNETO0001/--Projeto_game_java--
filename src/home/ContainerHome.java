package home;

import java.awt.Color;
import javax.swing.JFrame;

public class ContainerHome extends JFrame {
    public ContainerHome() {
        setTitle("Home");
        setSize(1200, 708);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Adicione seus componentes aqui
        getContentPane().setBackground(Color.BLACK); // Exemplo
        
        setVisible(true);
    }
}