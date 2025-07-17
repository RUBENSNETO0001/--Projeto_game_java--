package meujogo.modelo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class God extends Personagem {
    private BufferedImage imagem;
    private BufferedImage imagemOriginal;
    private BufferedImage imagemProxima;
    private boolean morto = false;

    public God(String nome, int vida, int ataque, int defesa, String caminhoImagem) {
        super(nome, vida, ataque, defesa);
        carregarImagens(caminhoImagem);
    }

    private void carregarImagens(String caminhoImagem) {
    try {
        // Método alternativo usando ClassLoader
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("res/persona/" + caminhoImagem);
        if (inputStream != null) {
            this.imagemOriginal = ImageIO.read(inputStream);
            this.imagem = imagemOriginal;
            inputStream.close();
        } else {
            throw new IOException("Arquivo não encontrado: res/persona/" + caminhoImagem);
        }
    } catch (Exception e) {
        System.err.println("Erro ao carregar imagem: " + e.getMessage());
        criarImagemFallback();
    }
}

    private void criarImagemFallback() {
        this.imagem = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        var g = imagem.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 50, 50);
        g.dispose();
    }

    public void verificarFinalDaFase(Fase fase) {
        // Se chegou no final direito da tela
        if (this.getX() >= fase.getWidth() - 100) {
            if (!morto) {
                this.setVida(Math.min(100, this.getVida() + 20)); // Recupera 20 de vida
                System.out.println(this.getNome() + " chegou ao final e recuperou vida!");
                
                // Ativa efeito visual de proximidade se existir
                if (imagemProxima != null) {
                    this.imagem = imagemProxima;
                }
            }
        }
        
        // Se morreu
        if (this.getVida() <= 0 && !morto) {
            morrer();
        }
    }

    private void morrer() {
        morto = true;
        // Cria uma imagem branca
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

    @Override
    public void atacar(Personagem alvo) {
        if (morto) return;
        
        super.atacar(alvo);
    }
}