package meujogo.aplicacao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import meujogo.aplicacao.HomePrincipal;
import meujogo.controller.Teclado;
import meujogo.fase.Fase;
import meujogo.modelo.God;
import meujogo.persistencia.ArmazenamentoJogo;
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

        fase = new Fase();
        deus = new God("─═God═─", "posseprincipal.png");
        deus.setX(100);
        deus.setY(100);
        ((java.util.List<meujogo.modelo.Personagem>) fase.getPersonagensModifiable()).add(deus);

        add(fase);
        // Remova KeyListeners existentes para evitar duplicações
        if (getKeyListeners().length > 0) {
            for (java.awt.event.KeyListener kl : getKeyListeners()) {
                removeKeyListener(kl);
            }
        }
        addKeyListener(new Teclado(fase));


        gameLoop = new Timer(16, this::updateGame);
        gameLoop.start();

        setVisible(true);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Jogo");

        JMenuItem salvarItem = new JMenuItem("Salvar");
        salvarItem.addActionListener(e -> salvarJogo());

        JMenuItem carregarItem = new JMenuItem("Carregar");
        carregarItem.addActionListener(e -> carregarJogo());

        menu.add(salvarItem);
        menu.add(carregarItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void salvarJogo() {
        try {
            if (ArmazenamentoJogo.salvarJogo(deus, fase.getAlmas())) {
                JOptionPane.showMessageDialog(this, "Jogo salvo com sucesso!", "Salvar", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao salvar jogo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar jogo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarJogo() {
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
    }

    private void updateGame(ActionEvent e) {
        if (!fase.isJogoAtivo()) {
            return;
        }

        if (fase.todasAlmasColetadas()) {
            vitoria();
            return;
        }

        fase.atualizar();

        if (deus.isMorto()) {
            gameOver();
        }
    }

    private void vitoria() {
        gameLoop.stop();
        fase.pararJogo();
        JOptionPane.showMessageDialog(this,
            "Parabéns! Você coletou todas as almas!\nVocê venceu o jogo!",
            "Vitória", JOptionPane.INFORMATION_MESSAGE);
        restartApplication();
    }

    private void gameOver() {
        gameLoop.stop();
        fase.pararJogo();
        int resposta = JOptionPane.showConfirmDialog(this,
            "Game Over! O cometa te matou!\nDeseja jogar novamente?",
            "Fim de Jogo", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            restartApplication();
        }
    }

    private void reiniciarJogoComDados(DadosJogo dados) {
        gameLoop.stop();
        fase.pararJogo();
        getContentPane().removeAll();

        this.deus = dados.getJogador();
        fase = new Fase();
        ((java.util.List<meujogo.modelo.Personagem>) fase.getPersonagensModifiable()).add(this.deus);
        fase.getAlmasModifiable().clear();
        fase.getAlmasModifiable().addAll(dados.getAlmas());

        add(fase);
        if (getKeyListeners().length > 0) {
            for (java.awt.event.KeyListener kl : getKeyListeners()) {
                removeKeyListener(kl);
            }
        }
        addKeyListener(new Teclado(fase));
        revalidate();
        repaint();
        fase.iniciarSpawnCometas();
        fase.setJogoAtivo(true);
        gameLoop.start();
    }

    private void restartGame() {
        gameLoop.stop();
        fase.pararJogo();
        getContentPane().removeAll();

        fase = new Fase();
        deus = new God("─═God═─", "posseprincipal.png");
        deus.setX(100);
        deus.setY(100);
        ((java.util.List<meujogo.modelo.Personagem>) fase.getPersonagensModifiable()).add(deus);

        add(fase);
        if (getKeyListeners().length > 0) {
            for (java.awt.event.KeyListener kl : getKeyListeners()) {
                removeKeyListener(kl);
            }
        }
        addKeyListener(new Teclado(fase));
        revalidate();
        repaint();
        fase.iniciarSpawnCometas();
        fase.setJogoAtivo(true);
        gameLoop.start();
    }

    private void restartApplication() {
        dispose();
        new HomePrincipal();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Container::new);
    }
}
