package meujogo;

import javax.swing.JFrame;
import meujogo.modelo.Fase;

public class Container extends JFrame{
	public Container() {
		// se de error aqui e bem provavel que esqueceu de extender a class
		add(new Fase());
		//titulo do game la em cima
		setTitle("Jogo Beta Em Java");
		//dimensão do game
		setSize(1200,708); 
		//isso aqui vai fechar a tela do game
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//onde o game vai abrir, o ponto onde ele vai abrir
		setLocationRelativeTo(null);
		//impedi que user vai remidificar a tela esse bosta
		this.setResizable(false);
		//torna a tela visivel ou fazer ela aparecer ainda não entendi ainda
		setVisible(true);
	}
	
	public static void main(String[] args) {
		// novo conteiner
		new Container();
	}
	
}