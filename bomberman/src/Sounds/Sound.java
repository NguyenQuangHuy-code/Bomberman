package Sounds;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sound {
    private String[] path = new String[30];
    private Media media;
    private MediaPlayer mediaPlayer;

    public Sound() {
        path[0] = "./res/Sound/theme.mp3";
        path[1] = "./res/Sound/Die.mp3";
        path[2] = "./res/Sound/GameOver.mp3";
        path[3] = "./res/Sound/footstep2.wav";
        path[4] = "./res/Sound/sword.wav";
        path[5] = "./res/Sound/BOM.wav";
        path[6] = "./res/Sound/pickItem.wav";
        path[7] = "./res/Sound/missionpassed.mp3";
        path[8] = "./res/Sound/missionfailed.mp3";
    }

    public void setFile(int i) {
        try {
            media = new Media(new File(path[i]).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
       if (mediaPlayer != null) {
           mediaPlayer.play();
       }
    }

    public void loop() {
        if (mediaPlayer != null) {
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void setVolume(double value) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(value);
        }
    }
}
