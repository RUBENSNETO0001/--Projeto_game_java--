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
    private final Timer timerCometas;
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
        timerCometas = new Timer();
        iniciarSpawnCometas();
    }

    public List<Alma> getAlmas() {
        return Collections.unmodifiableList(almas);
    }

    private void criarAlmas() {
        String[] imagensAlmas = {"alma.png"};
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(1000) + 100;
            int y = random.nextInt(500) + 100;
            almas.add(new Alma(x, y, imagensAlmas[0]));
        }
    }

    private void iniciarSpawnCometas() {
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

        if (todasAlmasColetadas()) {
            encerrarJogoComVitoria();
            return;
        }

        atualizarCometas();
        verificarColisoes();
        repaint();
    }

    private void atualizarCometas() {
        cometas.removeIf(cometa -> {
            cometa.mover();
            if (!cometa.isAtivo()) return true;
            
            for (Personagem p : personagens) {
                if (p instanceof God && cometa.colideCom((God) p)) {
                    ((God) p).morrer();
                    jogoAtivo = false;
                    break;
                }
            }
            return false;
        });
    }

    private void verificarColisoes() {
        personagens.stream()
            .filter(p -> p instanceof God)
            .forEach(p -> verificarColisaoComAlmas((God) p));
    }

    private void encerrarJogoComVitoria() {
        jogoAtivo = false;
        personagens.stream()
            .filter(p -> p instanceof God)
            .findFirst()
            .ifPresent(p -> ((God) p).vitoria());
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

        personagens.forEach(p -> desenharPersonagem(grafico, p));
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
        almas.stream()
            .filter(alma -> !alma.isColetada() && colisao(god, alma))
            .findFirst()
            .ifPresent(alma -> {
                alma.setColetada(true);
                god.coletarAlma();
            });
    }

    private boolean colisao(Personagem a, Alma b) {
        return Math.abs(a.getX() - b.getX()) < 40 && Math.abs(a.getY() - b.getY()) < 40;
    }

    public boolean isJogoAtivo() {
        return jogoAtivo;
    }

    public List<Personagem> getPersonagens() {
        return Collections.unmodifiableList(personagens);
    }

    public void pararJogo() {
        jogoAtivo = false;
        timerCometas.cancel();
    }
}