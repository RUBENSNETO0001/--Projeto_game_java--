package meujogo;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;

public class VideoPlayer {
    private static Runnable callback;

    public static void launchVideo(Runnable afterVideo) {
        callback = afterVideo;

        // Cria um frame Swing para hospedar o JavaFX
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        final JFXPanel fxPanel = new JFXPanel(); // Garante a inicialização do JavaFX
        frame.add(fxPanel);
        frame.setVisible(true);

        Platform.runLater(() -> initFX(fxPanel, frame));
    }

    private static void initFX(JFXPanel fxPanel, JFrame frame) {
        try {
            // Evite acentos/espaços no nome do arquivo
            String videoPath = VideoPlayer.class.getResource("/res/vid/essa_e_a_tal_da_wav_intro.mp4").toExternalForm();
            Media media = new Media(videoPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            mediaView.setFitWidth(800);
            mediaView.setFitHeight(600);

            StackPane root = new StackPane();
            root.getChildren().add(mediaView);

            Scene scene = new Scene(root);
            fxPanel.setScene(scene);

            mediaPlayer.play();

            mediaPlayer.setOnEndOfMedia(() -> {
                frame.dispose();
                if (callback != null) {
                    Platform.runLater(callback);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            frame.dispose();
            if (callback != null) {
                Platform.runLater(callback);
            }
        }
    }

    public static void main(String[] args) {
        launchVideo(() -> {
            System.out.println("Vídeo finalizado!");
            // Aqui você pode iniciar o jogo ou outro código
        });
    }
}
