package meujogo.modelo;

import java.io.Serializable;

public class Personagem implements Serializable {
    private String nome;
    private int vida;
    private int x, y;

    public Personagem(String nome, int vida) {
        this.nome = nome;
        this.vida = vida;
        this.x = 0;
        this.y = 0;
    }

    public Personagem(String nome, int vida, int x, int y) {
        this.nome = nome;
        this.vida = vida;
        this.x = x;
        this.y = y;
    }

    // Getters e Setters
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public String getNome() { return nome; }
    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    @Override
    public String toString() {
        return String.format("Personagem[nome=%s, vida=%d]", nome, vida);
    }
}