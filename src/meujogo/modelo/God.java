package meujogo.modelo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;

public class God extends Personagem implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient BufferedImage imagem;
    private transient BufferedImage imagemOriginal;
    private transient BufferedImage imagemEfeito;
    private boolean morto = false;
    private transient Timer timerEfeito;
    private int almasColetadas = 0;

    public God(String nome, String caminhoImagem) {
        super(nome, 100, 100);
        carregarImagens(caminhoImagem);
    }

    private void carregarImagens(String caminhoImagem) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("res/persona/" + caminhoImagem)) {
            if (inputStream != null) {
                this.imagemOriginal = ImageIO.read(inputStream);
                this.imagem = imagemOriginal;
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem: " + e.getMessage());
            criarImagemFallback();
        }

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("res/persona/efeitoalma.png")) {
            if (inputStream != null) {
                this.imagemEfeito = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem de efeito: " + e.getMessage());
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        carregarImagens("posseprincipal.png");
    }

    public void coletarAlma() {
        almasColetadas++;
        ativarEfeitoAlma();
    }

    public void ativarEfeitoAlma() {
        if (imagemEfeito == null) return;

        this.imagem = imagemEfeito;
        if (timerEfeito != null) {
            timerEfeito.cancel();
        }

        timerEfeito = new Timer();
        timerEfeito.schedule(new TimerTask() {
            @Override
            public void run() {
                imagem = imagemOriginal;
            }
        }, 500);
    }

    private void criarImagemFallback() {
        this.imagem = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        var g = imagem.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 50, 50);
        g.dispose();
    }

    public void verificarBordas(Fase fase) {
        if (getX() <= 0) setX(0);
        if (getX() >= fase.getWidth() - 50) setX(fase.getWidth() - 50);
        if (getY() <= 0) setY(0);
        if (getY() >= fase.getHeight() - 50) setY(fase.getHeight() - 50);
    }

    public void vitoria() {
        BufferedImage imgVitoria = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        var g = imgVitoria.createGraphics();
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 50, 50);
        g.dispose();
        this.imagem = imgVitoria;
    }

    public void morrer() {
        morto = true;
        BufferedImage imgBranca = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        var g = imgBranca.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 50, 50);
        g.dispose();
        this.imagem = imgBranca;
    }

    public BufferedImage getImagem() {
        return imagem;
    }

    public boolean isMorto() {
        return morto;
    }

    public int getAlmasColetadas() {
        return almasColetadas;
    }
}