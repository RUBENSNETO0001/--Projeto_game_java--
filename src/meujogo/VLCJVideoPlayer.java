package meujogo;

import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import javax.swing.*;
import java.io.File;

public class VLCJVideoPlayer {
    private static Runnable callback;
    
    public static void launchVideo(String videoPath, Runnable afterVideo) {
    // Obter o caminho do recurso dentro do JAR
    String resourcePath = "/vid/" + videoPath;
    URL videoUrl = VLCJVideoPlayer.class.getResource(resourcePath);
    
    if (videoUrl == null) {
        JOptionPane.showMessageDialog(null,
            "Vídeo não encontrado: " + resourcePath,
            "Erro",
            JOptionPane.ERROR_MESSAGE);
        afterVideo.run();
        return;
    }
    
    // Restante do código permanece igual
    mediaPlayerComponent.mediaPlayer().media().play(videoUrl.toString());
}
    
    private static void cleanup(EmbeddedMediaPlayerComponent mediaPlayerComponent, JFrame frame) {
        try {
            if (mediaPlayerComponent != null) {
                mediaPlayerComponent.mediaPlayer().controls().stop();
                mediaPlayerComponent.release();
            }
            if (frame != null) {
                frame.dispose();
            }
            if (callback != null) {
                SwingUtilities.invokeLater(callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}