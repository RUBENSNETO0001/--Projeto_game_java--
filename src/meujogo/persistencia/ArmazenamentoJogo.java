package meujogo.persistencia;

import meujogo.modelo.Alma;
import meujogo.modelo.God;
import java.io.*;
import java.util.List;

public class ArmazenamentoJogo {
    private static final String DIR_SAVES = "saves";
    private static final String ARQUIVO_SAVE = DIR_SAVES + "/savegame.dat";

    public static boolean salvarJogo(God jogador, List<Alma> almas) {
        try {
            File savesDir = new File(DIR_SAVES);
            if (!savesDir.exists()) {
                if (!savesDir.mkdirs()) {
                    System.err.println("Falha ao criar diretório de saves");
                    return false;
                }
            }

            try (ObjectOutputStream out = new ObjectOutputStream(
                 new FileOutputStream(ARQUIVO_SAVE))) {
                DadosJogo dados = new DadosJogo(jogador, almas);
                out.writeObject(dados);
                System.out.println("Jogo salvo com sucesso em: " + 
                                  ARQUIVO_SAVE);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar jogo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static DadosJogo carregarJogo() {
        File arquivo = new File(ARQUIVO_SAVE);
        
        if (!arquivo.exists() || arquivo.length() == 0) {
            System.out.println("Arquivo de save não encontrado ou vazio");
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(
             new BufferedInputStream(new FileInputStream(arquivo)))) {
            
            DadosJogo dados = (DadosJogo) in.readObject();
            System.out.println("Jogo carregado com sucesso!");
            return dados;
            
        } catch (EOFException e) {
            System.err.println("Arquivo corrompido (EOF): " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar jogo: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}