package meujogo.modelo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;

public class Alma implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int x, y;
    private transient BufferedImage imagem;
    private boolean coletada = false;

    public Alma(int x, int y, String caminhoImagem) {
        this.x = x;
        this.y = y;
        carregarImagem(caminhoImagem);
    }

    private void carregarImagem(String caminhoImagem) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("res/almas/" + caminhoImagem)) {
            if (inputStream != null) {
                this.imagem = ImageIO.read(inputStream);
            } else {
                System.err.println("Arquivo não encontrado: res/almas/" + caminhoImagem);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem da alma: " + e.getMessage());
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        carregarImagem("alma.png");
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
        this.coletada = coletada;
    }
}