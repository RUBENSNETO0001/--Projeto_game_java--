package meujogo.modelo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.util.TimerTask;

public class God extends Personagem {

    private BufferedImage imagem;
    private BufferedImage imagemOriginal;
    private BufferedImage imagemEfeito;
    private boolean morto = false;
    private Timer timerEfeito;
    private int almasColetadas = 0;

    public God(String nome, int vida, String caminhoImagem) {
        super(nome, vida);
        carregarImagens(caminhoImagem);
    }

    private void carregarImagens(String caminhoImagem) {
        try {
            // Carrega imagem normal
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("res/persona/posseprincipal.png");
            if (inputStream != null) {
                this.imagemOriginal = ImageIO.read(inputStream);
                this.imagem = imagemOriginal;
                inputStream.close();
            }

            // Carrega imagem de efeito
            inputStream = getClass().getClassLoader().getResourceAsStream("res/persona/efeitoalma.png");
            if (inputStream != null) {
                this.imagemEfeito = ImageIO.read(inputStream);
                inputStream.close();
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem: " + e.getMessage());
            criarImagemFallback();
        }
    }

    public void coletarAlma() {
        almasColetadas++;
        ativarEfeitoAlma();
        System.out.println(getNome() + " coletou uma alma! Total: " + almasColetadas);
    }

    public void ativarEfeitoAlma() {
        if (imagemEfeito != null) {
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
            }, 5000);
        }
    }

    private void criarImagemFallback() {
        this.imagem = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        var g = imagem.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 50, 50);
        g.dispose();
    }

    public void verificarBordas(Fase fase) {
        // Se atingiu a borda esquerda
        if (this.getX() <= 0) {
            this.setX(0);
            System.out.println("Borda esquerda atingida!");
        }

        // Se atingiu a borda direita
        if (this.getX() >= fase.getWidth() - 50) {
            this.setX(fase.getWidth() - 50);
            System.out.println("Borda direita atingida!");
        }

        // Se atingiu a borda superior
        if (this.getY() <= 0) {
            this.setY(0);
            System.out.println("Borda superior atingida!");
        }

        // Se atingiu a borda inferior
        if (this.getY() >= fase.getHeight() - 50) {
            this.setY(fase.getHeight() - 50);
            System.out.println("Borda inferior atingida!");
        }
    }
    // Adicionar verificação de bordas no método verificarFinalDaFase

   public void vitoria() {
    // Cria uma imagem verde para representar a vitória
    BufferedImage imgVitoria = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
    var g = imgVitoria.createGraphics();
    g.setColor(Color.GREEN);
    g.fillRect(0, 0, 50, 50);
    g.dispose();
    this.imagem = imgVitoria;
    
    System.out.println(getNome() + " coletou todas as almas! Vitória!");
}

    public void morrer() {
        morto = true;
        // Cria uma imagem branca para representar a morte
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
