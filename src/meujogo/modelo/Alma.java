package meujogo.modelo;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Alma {
    private int x, y;
    private BufferedImage imagem;
    private boolean coletada = false;

    public Alma(int x, int y, String caminhoImagem) {
        this.x = x;
        this.y = y;
        carregarImagem(caminhoImagem);
    }

    private void carregarImagem(String caminhoImagem) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("res/almas/" + caminhoImagem);
            if (inputStream != null) {
                this.imagem = ImageIO.read(inputStream);
                inputStream.close();
            } else {
                System.err.println("Arquivo não encontrado: res/almas/" + caminhoImagem);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem da alma: " + e.getMessage());
            this.imagem = null;
        }
    }

    public BufferedImage getImagem() {
        return imagem;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isColetada() {
        return coletada;
    }

    public void setColetada(boolean coletada) {
        getClass().getClassLoader().getResourceAsStream("res/persona/posse_pegandoaAlma.png");
        this.coletada = coletada;
    }
}