package meujogo.fase;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;
import javax.swing.*;

import meujogo.modelo.Alma;
import meujogo.modelo.Cometa;
import meujogo.modelo.God;
import meujogo.modelo.Personagem;

public class Fase extends JPanel {

    private Image background;
    private List<Personagem> personagens;
    private List<Alma> almas;
    private List<Cometa> cometas;
    private Random random;
    private Timer timerCometas;
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
            background = null;
        }

        criarAlmas();
        iniciarSpawnCometas();
    }

    private void criarAlmas() {
        String[] imagensAlmas = {"alma.png"};
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(1000) + 100;
            int y = random.nextInt(500) + 100;
            String imgAleatoria = imagensAlmas[random.nextInt(imagensAlmas.length)];
            almas.add(new Alma(x, y, imgAleatoria));
        }
    }

    private void iniciarSpawnCometas() {
        timerCometas = new Timer();
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
        String[] imagens = {"cometa.png"};
        String img = imagens[random.nextInt(imagens.length)];
        cometas.add(new Cometa(getWidth(), y, velocidade, img));
    }

    public boolean todasAlmasColetadas() {
        for (Alma alma : almas) {
            if (!alma.isColetada()) {
                return false;
            }
        }
        return true;
    }

    public void atualizar() {
        if (!jogoAtivo) {
            return;
        }

        if (todasAlmasColetadas()) {
            jogoAtivo = false;
            for (Personagem p : personagens) {
                if (p instanceof God) {
                    ((God) p).vitoria();
                    break;
                }
            }
            return;
        }

        for (Iterator<Cometa> it = cometas.iterator(); it.hasNext();) {
            Cometa cometa = it.next();
            cometa.mover();

            for (Personagem p : personagens) {
                if (p instanceof God && cometa.colideCom((God) p)) {
                    ((God) p).morrer();
                    jogoAtivo = false;
                    break;
                }
            }

            if (!cometa.isAtivo()) {
                it.remove();
            }
        }

        for (Personagem p : personagens) {
            if (p instanceof God) {
                verificarColisaoComAlmas((God) p);
                ((God) p).verificarBordas(this);
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D grafico = (Graphics2D) g;

        if (background != null) {
            grafico.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }

        for (Alma alma : almas) {
            if (!alma.isColetada()) {
                BufferedImage img = alma.getImagem();
                if (img != null) {
                    grafico.drawImage(img, alma.getX(), alma.getY(), 30, 30, null);
                }
            }
        }

        for (Cometa cometa : cometas) {
            if (cometa.isAtivo()) {
                BufferedImage img = cometa.getImagem();
                if (img != null) {
                    grafico.drawImage(img, cometa.getX(), cometa.getY(),
                            cometa.getLargura(), cometa.getAltura(), null);
                }
            }
        }

        for (Personagem p : personagens) {
            desenharPersonagem(grafico, p);
        }
    }

    public boolean isJogoAtivo() {
        return jogoAtivo;
    }

    private void verificarColisaoComAlmas(God god) {
        for (Alma alma : almas) {
            if (!alma.isColetada() && colisao(god, alma)) {
                alma.setColetada(true);
                god.coletarAlma();
                break;
            }
        }
    }

    private boolean colisao(Personagem a, Alma b) {
        return Math.abs(a.getX() - b.getX()) < 40 && Math.abs(a.getY() - b.getY()) < 40;
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

    public List<Personagem> getPersonagens() {
        return personagens;
    }
}
