package meujogo;

import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import javax.swing.*;
import java.net.URL;

public class VLCJVideoPlayer {
    private static Runnable onCompletion;

    public VLCJVideoPlayer() {
        super();
    }

    public static void playVideo(String videoPath, Runnable completionCallback) {
        onCompletion = completionCallback;
        
        try {
            // 1. Descobrir bibliotecas nativas do VLC
            boolean vlcFound = new NativeDiscovery().discover();
            if (!vlcFound) {
                throw new RuntimeException("VLC not found. Please install VLC media player.");
            }

            // 2. Verificar se o vídeo existe
            URL videoUrl = VLCJVideoPlayer.class.getResource(videoPath);
            if (videoUrl == null) {
                throw new RuntimeException("Video file not found: " + videoPath);
            }

            // 3. Configurar player
            EmbeddedMediaPlayerComponent player = new EmbeddedMediaPlayerComponent();
            
            JFrame frame = new JFrame();
            frame.setUndecorated(true);
            frame.setContentPane(player);
            frame.setSize(1200, 708);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            
            // 4. Configurar listeners
            player.mediaPlayer().events().addMediaPlayerEventListener(
                new uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter() {
                    @Override
                    public void finished(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {
                        cleanup(frame, player);
                    }
                    
                    @Override
                    public void error(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {
                        cleanup(frame, player);
                    }
                });
            
            // 5. Iniciar reprodução
            player.mediaPlayer().media().play(videoUrl.toString());
            frame.setVisible(true);
            
        } catch (Exception e) {
            handleError(e);
        }
    }

    private static void cleanup(JFrame frame, EmbeddedMediaPlayerComponent player) {
        try {
            if (player != null) {
                player.mediaPlayer().release();
                player.release();
            }
            if (frame != null) {
                frame.dispose();
            }
            if (onCompletion != null) {
                SwingUtilities.invokeLater(onCompletion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
            "Video error: " + e.getMessage() + "\nStarting game directly...",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        if (onCompletion != null) {
            SwingUtilities.invokeLater(onCompletion);
        }
    }
}