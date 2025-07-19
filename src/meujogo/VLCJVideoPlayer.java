package meujogo;

import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class VLCJVideoPlayer {  // Não estende mais JFrame
    private static Runnable callback;
    
    public static void launchVideo(String videoFileName, Runnable afterVideo) {
        callback = afterVideo;
        
        try {
            // Verifica instalação do VLC
            if (!new NativeDiscovery().discover()) {
                throw new RuntimeException("VLC não encontrado. Instale o VLC Media Player.");
            }

            // Obtém o URL do vídeo
            URL videoUrl = VLCJVideoPlayer.class.getResource("/res/vid/" + videoFileName);
            if (videoUrl == null) {
                throw new RuntimeException("Vídeo não encontrado: /res/vid/" + videoFileName);
            }

            // Cria o frame para o vídeo
            JFrame frame = new JFrame();
            frame.setUndecorated(true);
            frame.setSize(1200, 708);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            
            // Configura o player
            EmbeddedMediaPlayerComponent player = new EmbeddedMediaPlayerComponent();
            frame.setContentPane(player);
            
            // Configura os listeners
            player.mediaPlayer().events().addMediaPlayerEventListener(
                new uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter() {
                    @Override
                    public void finished(uk.co.caprica.vlcj.player.base.MediaPlayer mp) {
                        cleanup(player, frame);
                    }
                    
                    @Override
                    public void error(uk.co.caprica.vlcj.player.base.MediaPlayer mp) {
                        cleanup(player, frame);
                    }
                });
            
            // Inicia a reprodução
            player.mediaPlayer().media().play(videoUrl.toString());
            frame.setVisible(true);
            
        } catch (Exception e) {
            handleError(e);
        }
    }
    
    private static void handleError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
            "Erro no vídeo: " + e.getMessage() + "\nIniciando jogo diretamente...",
            "Erro",
            JOptionPane.ERROR_MESSAGE);
        if (callback != null) {
            SwingUtilities.invokeLater(callback);
        }
    }
    
    private static void cleanup(EmbeddedMediaPlayerComponent player, JFrame frame) {
        try {
            if (player != null) {
                player.mediaPlayer().controls().stop();
                player.release();
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