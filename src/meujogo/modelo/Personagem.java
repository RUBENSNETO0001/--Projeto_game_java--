package meujogo.modelo;

import java.io.Serializable;

public class Personagem implements Serializable {

    private String nome;
    private int vida;
    private int ataque;
    private int defesa;
    private int nivel;
    private int experiencia;
    private int x, y;

    public Personagem(String nome, int vida, int ataque, int defesa) {
        this(nome, vida, ataque, defesa, 0, 0);
    }

    public Personagem(String nome, int vida, int ataque, int defesa, int x, int y) {
        this.nome = nome;
        this.vida = vida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = 1;
        this.experiencia = 0;
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

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }


    public void atacar(Personagem alvo) {
        int dano = this.ataque - (alvo.defesa / 2);
        if (dano > 0) {
            alvo.vida -= dano;
            if (alvo.vida < 0) {
                alvo.vida = 0;
            }
            System.out.println(this.nome + " atacou " + alvo.nome + " causando " + dano + " de dano!");
        } else {
            System.out.println(this.nome + " atacou, mas " + alvo.nome + " defendeu com sucesso!");
        }
    }

    public void ganharExperiencia(int xp) {
        this.experiencia += xp;
        System.out.println(this.nome + " ganhou " + xp + " pontos de experiência!");

        int xpParaProximoNivel = this.nivel * 100;
        if (this.experiencia >= xpParaProximoNivel) {
            this.subirNivel();
        }
    }

    private void subirNivel() {
        this.nivel++;
        this.ataque += 5;
        this.defesa += 3;
        this.vida += 10;
        this.experiencia = 0;
        System.out.println(this.nome + " subiu para o nível " + this.nivel + "!");
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getNivel() {
        return nivel;
    }

    public int getExperiencia() {
        return experiencia;
    }

    @Override
    public String toString() {
        return String.format(
                "Personagem[nome=%s, vida=%d, ataque=%d, defesa=%d, nivel=%d, experiencia=%d]",
                nome, vida, ataque, defesa, nivel, experiencia
        );
    }
}
