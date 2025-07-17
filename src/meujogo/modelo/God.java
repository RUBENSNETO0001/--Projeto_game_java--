package meujogo.modelo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.IOException;

public class God extends Personagem {
    private BufferedImage imagem;

    public God(String nome, int vida, int ataque, int defesa, String caminhoImagem) {
        super(nome, vida, ataque, defesa);
        carregarImagem(caminhoImagem);
    }

    private void carregarImagem(String caminhoImagem) {
        try {
            this.imagem = ImageIO.read(getClass().getResource("/res/persona/posseParado.jpeg" + caminhoImagem));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erro ao carregar a imagem: " + e.getMessage());
            this.imagem = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
            var g = imagem.createGraphics();
            g.setColor(Color.RED);
            g.fillRect(0, 0, 50, 50);
            g.dispose();
        }
    }

    public BufferedImage getImagem() {
        return imagem;
    }

    @Override
public void atacar(Personagem alvo) {
    // Garante que o alvo está vivo
    if (!alvo.estaVivo()) {
        System.out.println(alvo.getNome() + " já está derrotado!");
        return;
    }

    int dano = this.getAtaque() - (alvo.getDefesa() / 2);
    dano = Math.max(dano, 1); // Garante pelo menos 1 de dano
    
    alvo.setVida(alvo.getVida() - dano);
    System.out.println(this.getNome() + " atacou " + alvo.getNome() + " causando " + dano + " de dano.");

    // Verifica se derrotou o alvo
    if (!alvo.estaVivo()) {
        System.out.println(alvo.getNome() + " foi derrotado!");
        this.ganharExperiencia(30); // Ganha XP por derrotar inimigo
    }
}

public void defender(int dano) {
    int danoReduzido = Math.max(dano - (this.getDefesa() / 2), 0);
    this.setVida(this.getVida() - danoReduzido);
    
    if (danoReduzido > 0) {
        System.out.println(this.getNome() + " recebeu " + danoReduzido + " de dano.");
    } else {
        System.out.println(this.getNome() + " se defendeu completamente do ataque.");
    }
}

public void ganharExperiencia(int pontos) {
    this.experiencia += pontos;
    System.out.println(this.getNome() + " ganhou " + pontos + " pontos de experiência!");
    
    int xpNecessario = this.getNivel() * 100;
    if (this.getExperiencia() >= xpNecessario) {
        this.nivel++;
        this.ataque += 5;
        this.defesa += 3;
        this.vida += 20;
        this.experiencia -= xpNecessario;
        System.out.println("⭐ " + this.getNome() + " subiu para o nível " + this.getNivel() + "!");
        System.out.println("   Ataque: +5 | Defesa: +3 | Vida: +20");
    }
}
}