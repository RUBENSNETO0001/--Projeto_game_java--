package meujogo.persistencia;

import meujogo.modelo.Alma;
import meujogo.modelo.God;
import meujogo.persistencia.DadosJogo;

import java.io.*;
import java.util.List;

public class ArmazenamentoJogo {

    private static final String DIR_SAVES = "saves";
    private static final String ARQUIVO_SAVE = DIR_SAVES + "/savegame.dat";

    public static boolean salvarJogo(God jogador, List<Alma> almas) {
        try {
            // Garante que o diretório de saves existe
            File savesDir = new File(DIR_SAVES);
            if (!savesDir.exists()) {
                savesDir.mkdirs();
            }

            File arquivoSave = new File(ARQUIVO_SAVE);

            try (ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(arquivoSave))) {
                DadosJogo dados = new DadosJogo(jogador, almas);
                out.writeObject(dados);
                System.out.println("Jogo salvo com sucesso em: "
                        + arquivoSave.getAbsolutePath());
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

        System.out.println("Tentando carregar de: " + arquivo.getAbsolutePath());

        if (!arquivo.exists()) {
            System.out.println("Arquivo de save não encontrado.");
            return null;
        }

        if (arquivo.length() == 0) {
            System.out.println("Arquivo de save está vazio.");
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(arquivo)))) {

            DadosJogo dados = (DadosJogo) in.readObject();
            System.out.println("Jogo carregado com sucesso!");
            return dados;

        } catch (EOFException e) {
            System.err.println("Arquivo corrompido (EOF): " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de IO ao carregar jogo: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Classe não encontrada ao carregar jogo");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao carregar jogo: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
