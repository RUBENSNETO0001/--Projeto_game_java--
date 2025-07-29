package meujogo.modelo;

import java.io.Serializable;

public abstract class Personagem implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int x, y;
    protected String nome;

    public Personagem(String nome, int x, int y) {
        this.nome = nome;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getNome() {
        return nome;
    }
}
