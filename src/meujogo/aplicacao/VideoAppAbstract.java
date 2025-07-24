package meujogo.aplicacao;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class VideoAppAbstract {

    public static void main(String[] args) {

        File videoFile = new File("src/res/vid/inicio.mp4");

        // Verifique se o Desktop é suportado e se o arquivo existe
        if (Desktop.isDesktopSupported() && videoFile.exists()) {
            try {
                // Abra o arquivo com o aplicativo padrão do sistema
                Desktop.getDesktop().open(videoFile);
                System.out.println("Vídeo aberto com sucesso!");
            } catch (IOException e) {
                System.err.println("Erro ao abrir o vídeo: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Não foi possível abrir o vídeo.");
            if (!Desktop.isDesktopSupported()) {
                System.out.println("Recurso Desktop não suportado pelo sistema.");
            }
            if (!videoFile.exists()) {
                System.out.println("O arquivo de vídeo não foi encontrado: " + videoFile.getAbsolutePath());
            }
        }
    }
}