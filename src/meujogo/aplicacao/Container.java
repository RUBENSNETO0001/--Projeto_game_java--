package meujogo.aplicacao;

import javax.swing.*;
import java.awt.event.ActionEvent;

import meujogo.controller.Teclado;
import meujogo.fase.Fase;
import meujogo.modelo.Alma;
import meujogo.modelo.God;
import meujogo.persistencia.ArmazenamentoJogo;  // Já existe
import meujogo.persistencia.DadosJogo;

public class Container extends JFrame {

    private Fase fase;
    private Timer gameLoop;
    private God deus; 

    public Container() {
        initGame();
        initMenu();
    }

    private void initGame() {
    setTitle("God Game");
    setSize(1200, 708);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    // Primeiro cria a fase
    fase = new Fase();
    
    // Depois cria o personagem
    deus = new God("─═God═─", "posseprincipal.png");
    deus.setX(100);
    deus.setY(100);
    fase.getPersonagens().add(deus);

    // Adiciona a fase ao container
    add(fase);

    // Configura o teclado UMA ÚNICA VEZ
    Teclado teclado = new Teclado(fase);
    addKeyListener(teclado);

    fase.setFocusable(true);
    fase.requestFocusInWindow();

    gameLoop = new Timer(16, this::updateGame);
    gameLoop.start();

    setVisible(true);
}

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Jogo");
        JMenuItem salvarItem = new JMenuItem("Salvar");
        JMenuItem carregarItem = new JMenuItem("Carregar");

        salvarItem.addActionListener(e -> {
            try {
                ArmazenamentoJogo.salvarJogo(deus, fase.getAlmas());
                JOptionPane.showMessageDialog(this, "Jogo salvo com sucesso!", "Salvar", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar jogo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        carregarItem.addActionListener(e -> {
            try {
                DadosJogo dados = ArmazenamentoJogo.carregarJogo();
                if (dados != null) {
                    reiniciarJogoComDados(dados);
                    JOptionPane.showMessageDialog(this, "Jogo carregado com sucesso!", "Carregar", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhum jogo salvo encontrado", "Carregar", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar jogo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        menu.add(salvarItem);
        menu.add(carregarItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void updateGame(ActionEvent e) {
        if (fase.todasAlmasColetadas()) {
            vitoria();
            return;
        }
        fase.atualizar();
        for (var p : fase.getPersonagens()) {
            if (p instanceof God && ((God) p).isMorto()) { 
                gameOver();
                break;
            }
        }
    }

    private void vitoria() {
        gameLoop.stop();
        JOptionPane.showMessageDialog(this, "Parabéns! Você coletou todas as almas!\nVocê venceu o jogo!", "Vitória", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(this, "Finalmente completo\nObrigado por jogar!!", "Vitória", JOptionPane.INFORMATION_MESSAGE);
        restartApplication();
    }

    private void gameOver() {
        gameLoop.stop();
        int resposta = JOptionPane.showConfirmDialog(this, "Game Over! O cometa te matou!\nDeseja jogar novamente?", "Fim de Jogo", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            restartApplication();
        }
    }

    private void reiniciarJogoComDados(DadosJogo dados) {
        gameLoop.stop(); 
        getContentPane().removeAll(); 

        this.deus = dados.getJogador(); 
        
        fase = new Fase();
        fase.getPersonagens().add(this.deus); 

        fase.getAlmas().clear(); 
        for (Alma alma : dados.getAlmas()) {
            fase.getAlmas().add(alma);
        }

        addKeyListener(new Teclado(fase));
        add(fase);
        fase.setFocusable(true); 
        fase.requestFocusInWindow();

        revalidate();
        repaint();
        gameLoop.start();
    }

    private void restartGame() {
        gameLoop.stop(); 
        getContentPane().removeAll(); 

        fase = new Fase();
        deus = new God("─═God═─", "posseprincipal.png");
        deus.setX(100);
        deus.setY(100);
        fase.getPersonagens().add(deus);
        
        addKeyListener(new Teclado(fase));
        add(fase);
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