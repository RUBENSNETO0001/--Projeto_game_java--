package meujogo.fase;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import javax.swing.*;

import meujogo.modelo.Alma;
import meujogo.modelo.Cometa;
import meujogo.modelo.God;
import meujogo.modelo.Personagem;

public class Fase extends JPanel {
    private transient Image background;
    private final List<Personagem> personagens;
    private final List<Alma> almas;
    private final List<Cometa> cometas;
    private final Random random;
    private transient java.util.Timer timerCometas;
    private boolean jogoAtivo;

    public Fase() {
        personagens = new ArrayList<>();
        almas = new ArrayList<>();
        cometas = new ArrayList<>();
        random = new Random();
        jogoAtivo = true;
        setBackground(Color.BLACK);

        try {
            background = new ImageIcon(getClass().getResource("/res/background/background.jpeg")).getImage();
        } catch (Exception e) {
            System.err.println("Erro ao carregar background: " + e.getMessage());
        }

        criarAlmas();
        timerCometas = new java.util.Timer();
        iniciarSpawnCometas();
    }

    public List<Alma> getAlmas() {
        return Collections.unmodifiableList(almas);
    }

    public List<Alma> getAlmasModifiable() {
        return almas;
    }

    public List<Personagem> getPersonagens() {
        return Collections.unmodifiableList(personagens);
    }

    public List<Personagem> getPersonagensModifiable() {
        return personagens;
    }

    private void criarAlmas() {
        String[] imagensAlmas = {"alma.png"};
        // Garante que o painel tenha tamanho antes de criar almas
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < 5; i++) {
                int x = random.nextInt(getWidth() - 200) + 100; // Ajustado para o tamanho do painel
                int y = random.nextInt(getHeight() - 200) + 100; // Ajustado para o tamanho do painel
                almas.add(new Alma(x, y, imagensAlmas[0]));
            }
        });
    }

    public void iniciarSpawnCometas() {
        // Cancela e purga qualquer timer existente para evitar múltiplas instâncias
        if (timerCometas != null) {
            timerCometas.cancel();
            timerCometas.purge();
        }
        timerCometas = new java.util.Timer();
        timerCometas.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (jogoAtivo) {
                    spawnCometa();
                }
            }
        }, 2000, 1000);
    }

    private void spawnCometa() {
        int y = random.nextInt(getHeight() - 100) + 50;
        int velocidade = random.nextInt(15) + 3;
        cometas.add(new Cometa(getWidth(), y, velocidade, "cometa.png"));
    }

    public boolean todasAlmasColetadas() {
        return almas.stream().allMatch(Alma::isColetada);
    }

    public void atualizar() {
        if (!jogoAtivo) return;

        // A condição de vitória é tratada no Container para controle do game loop principal
        // if (todasAlmasColetadas()) {
        //     encerrarJogoComVitoria();
        //     return;
        // }

        atualizarCometas();
        verificarColisoes();
        repaint();
    }

    private void atualizarCometas() {
        Iterator<Cometa> iterator = cometas.iterator();
        while (iterator.hasNext()) {
            Cometa cometa = iterator.next();
            cometa.mover();
            if (!cometa.isAtivo()) {
                iterator.remove();
            } else {
                // Iterar sobre uma cópia da lista de personagens para evitar ConcurrentModificationException
                for (Personagem p : new ArrayList<>(personagens)) {
                    if (p instanceof God) {
                        God god = (God) p;
                        if (!god.isMorto() && cometa.colideCom(god)) {
                            god.morrer();
                            jogoAtivo = false; // Define o jogo como inativo em caso de morte
                            break; // Sai do loop interno, pois o jogador morreu
                        }
                    }
                }
            }
        }
    }


    private void verificarColisoes() {
        personagens.stream()
            .filter(p -> p instanceof God)
            .forEach(p -> verificarColisaoComAlmas((God) p));
    }

    private void encerrarJogoComVitoria() {
        jogoAtivo = false;
        // O tratamento de vitória será feito no Container
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D grafico = (Graphics2D) g;

        if (background != null) {
            grafico.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }

        almas.stream()
            .filter(alma -> !alma.isColetada())
            .forEach(alma -> desenharAlma(grafico, alma));

        cometas.stream()
            .filter(Cometa::isAtivo)
            .forEach(cometa -> desenharCometa(grafico, cometa));

        // Desenha o personagem God apenas se não estiver morto
        personagens.stream()
                .filter(p -> p instanceof God && !((God)p).isMorto())
                .forEach(p -> desenharPersonagem(grafico, p));
    }

    private void desenharAlma(Graphics2D g, Alma alma) {
        BufferedImage img = alma.getImagem();
        if (img != null) {
            g.drawImage(img, alma.getX(), alma.getY(), 30, 30, null);
        }
    }

    private void desenharCometa(Graphics2D g, Cometa cometa) {
        BufferedImage img = cometa.getImagem();
        if (img != null) {
            g.drawImage(img, cometa.getX(), cometa.getY(),
                    cometa.getLargura(), cometa.getAltura(), null);
        }
    }

    private void desenharPersonagem(Graphics2D g, Personagem p) {
        if (p instanceof God) {
            God god = (God) p;
            BufferedImage img = god.getImagem();
            if (img != null) {
                g.drawImage(img, p.getX(), p.getY(), 50, 50, null);
            }
        }
        g.setColor(Color.WHITE);
        g.drawString(p.getNome(), p.getX(), p.getY() - 15);
    }

    private void verificarColisaoComAlmas(God god) {
        // Usar uma cópia para evitar ConcurrentModificationException ao remover
        List<Alma> almasCopy = new ArrayList<>(almas);
        for (Alma alma : almasCopy) {
            if (!alma.isColetada() && colisao(god, alma)) {
                alma.setColetada(true);
                god.coletarAlma();
            }
        }
    }

    private boolean colisao(Personagem a, Alma b) {
        int pX = a.getX();
        int pY = a.getY();
        int pWidth = 50; // Largura do God
        int pHeight = 50; // Altura do God

        int aX = b.getX();
        int aY = b.getY();
        int aWidth = 30; // Largura da Alma
        int aHeight = 30; // Altura da Alma

        return (pX < aX + aWidth &&
                pX + pWidth > aX &&
                pY < aY + aHeight &&
                pY + pHeight > aY);
    }

    public boolean isJogoAtivo() {
        return jogoAtivo;
    }

    public void setJogoAtivo(boolean jogoAtivo) {
        this.jogoAtivo = jogoAtivo;
    }

    public void pararJogo() {
        jogoAtivo = false;
        if (timerCometas != null) {
            timerCometas.cancel();
            timerCometas.purge(); // Limpa as tarefas agendadas
        }
    }
}