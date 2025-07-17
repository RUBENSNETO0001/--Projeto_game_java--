package meujogo.modelo;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Cometa {
    private int x, y;
    private int velocidade;
    private BufferedImage imagem;
    private boolean ativo;
    private int largura = 60;
    private int altura = 60;

    public Cometa(int x, int y, int velocidade, String caminhoImagem) {
        this.x = x;
        this.y = y;
        this.velocidade = velocidade;
        this.ativo = true;
        carregarImagem(caminhoImagem);
    }

    private void carregarImagem(String caminhoImagem) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("res/cometas/" + caminhoImagem);
            if (inputStream != null) {
                this.imagem = ImageIO.read(inputStream);
                inputStream.close();
            } else {
                System.err.println("Arquivo não encontrado: res/cometas/" + caminhoImagem);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem do cometa: " + e.getMessage());
            this.imagem = null;
        }
    }

    public void mover() {
        x -= velocidade;
        if (x < -largura) {
            ativo = false;
        }
    }

    public boolean colideCom(God god) {
        return ativo && 
               x < god.getX() + 50 && 
               x + largura > god.getX() && 
               y < god.getY() + 50 && 
               y + altura > god.getY();
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public BufferedImage getImagem() { return imagem; }
    public boolean isAtivo() { return ativo; }
    public int getLargura() { return largura; }
    public int getAltura() { return altura; }
}