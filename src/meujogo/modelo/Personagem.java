package meujogo.modelo;

import java.io.Serializable;

public class Personagem implements Serializable {
    private String nome;
    private int x, y;

    public Personagem(String nome) {
        this.nome = nome;
        this.x = 0;
        this.y = 0;
    }

    public Personagem(String nome, int x, int y) {
        this.nome = nome;
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public String getNome() { return nome; }



    @Override
    public String toString() {
        return String.format("Personagem[nome=%s, vida=%d]", nome);
    }
}