package meujogo;

import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import javax.swing.*;
import java.net.URL;

public class VLCJVideoPlayer {
    
    static {
        // Configura caminho do VLC (ajuste para seu sistema)
        System.setProperty("vlcj.nativeLibraryPath", "C:/Program Files/VideoLAN/VLC");
    }

    public static void playVideo(String videoPath, Runnable completionCallback) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                EmbeddedMediaPlayerComponent player = null;
                JFrame frame = null;
                
                try {
                    // 1. Verificar instalação do VLC
                    if (!new NativeDiscovery().discover()) {
                        throw new RuntimeException("VLC não encontrado. Instale o VLC media player.");
                    }

                    // 2. Verificar se o vídeo existe
                    URL videoUrl = VLCJVideoPlayer.class.getResource(videoPath);
                    if (videoUrl == null) {
                        throw new RuntimeException("Vídeo não encontrado: " + videoPath);
                    }

                    // 3. Configurar player
                    player = new EmbeddedMediaPlayerComponent();
                    frame = new JFrame();
                    frame.setUndecorated(true);
                    frame.setContentPane(player);
                    frame.setSize(1200, 708);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    
                    // 4. Configurar listeners
                    player.mediaPlayer().events().addMediaPlayerEventListener(
                        new MediaPlayerEventAdapter() {
                            @Override
                            public void finished(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {
                                cleanup(frame, player, completionCallback);
                            }
                            
                            @Override
                            public void error(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {
                                cleanup(frame, player, completionCallback);
                            }
                        });
                    
                    // 5. Iniciar reprodução
                    player.mediaPlayer().media().play(videoUrl.toString());
                    frame.setVisible(true);
                    
                } catch (Exception e) {
                    if (frame != null) frame.dispose();
                    if (player != null) player.release();
                    handleError(e, completionCallback);
                }
                return null;
            }
        };
        worker.execute();
    }

    private static void cleanup(JFrame frame, 
                              EmbeddedMediaPlayerComponent player, 
                              Runnable completionCallback) {
        try {
            if (player != null) {
                player.mediaPlayer().stop();
                player.mediaPlayer().release();
                player.release();
            }
        } finally {
            if (frame != null) {
                frame.dispose();
            }
            if (completionCallback != null) {
                SwingUtilities.invokeLater(completionCallback);
            }
        }
    }

    private static void handleError(Exception e, Runnable completionCallback) {
        System.err.println("Erro no player de vídeo: " + e.getMessage());
        if (completionCallback != null) {
            SwingUtilities.invokeLater(completionCallback);
        }
    }
}