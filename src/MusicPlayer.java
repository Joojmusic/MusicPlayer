import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.swing.*;
import java.io.*;
import java.security.PrivateKey;
import java.util.ArrayList;

public class MusicPlayer extends PlaybackListener {

    private static final Object playSignal = new Object();

    private MusicPlayerGUI musicPlayerGUI;

    private Song currentSong;
    private AdvancedPlayer advancedPlayer;
    private boolean isPlaying;
    private boolean songFinished;
    private int currentFrame;
    private int currentTimeInMillisecond;
    private boolean pressedNext, pressedPrev, songPlaying;

    public void setCurrentFrame(int frame){
        currentFrame = frame;
    }

    public void setCurrentTimeInMilliseconds(int timeInMilliseconds){
        currentTimeInMillisecond = timeInMilliseconds;
    }

    public Song getCurrentSong(){
        return currentSong;
    }

    //PRIVATE
    private ArrayList<Song> playlist;
    private int currentPlaylistIndex;

    public MusicPlayer(MusicPlayerGUI musicPlayerGUI){

        this.musicPlayerGUI = musicPlayerGUI;

    }

    public void loadSong(Song song){
        currentSong = song;
        playlist = null;

        if(!songFinished){
            stopSong();
        }

        if(currentSong != null){
            currentFrame = 0;

            currentTimeInMillisecond = 0;

            musicPlayerGUI.setPlaybackSliderValue(0);

            playCurrentSong();
        }
    }

    public void loadPlaylist(File playlistFile){
        playlist = new ArrayList<>();

        if(songPlaying) return;

        try {
            FileReader fileReader = new FileReader(playlistFile);
            BufferedReader br = new BufferedReader(fileReader);

            String songPath;
            while ((songPath = br.readLine()) != null){
                Song song = new Song(songPath);

                playlist.add(song);

                if(!playlist.isEmpty()){
                    musicPlayerGUI.setPlaybackSliderValue(0);
                    currentTimeInMillisecond = 0;

                    currentSong = playlist.getFirst();

                    currentFrame = 0;

                    musicPlayerGUI.disablePlayButton();
                    musicPlayerGUI.updateSongDetails(currentSong);
                    musicPlayerGUI.updatePlaybackSlider(currentSong);

                    playCurrentSong();
                }

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void playCurrentSong(){
        System.out.println("Song Playing");
        if(currentSong == null){
            return;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(currentSong.getFilePath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            advancedPlayer = new AdvancedPlayer(bufferedInputStream);
            advancedPlayer.setPlayBackListener(this);


            startMusicThread();
            songPlaying = true;

            startPlaybackSliderThread();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseSong(){

        if(advancedPlayer != null){
            isPlaying = false;
            songPlaying = false;
            stopSong();
        }
    }

    public void stopSong(){
        if(advancedPlayer != null){
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer = null;
        }
    }

    public void nextSong(){
        if(playlist == null) return;

        if(currentPlaylistIndex + 1 > playlist.size() - 1) {
            JOptionPane.showMessageDialog(musicPlayerGUI, "No next song to go to!!", "Next Song Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pressedNext = true;

        if(!songFinished)

            stopSong();

        currentPlaylistIndex++;
        currentSong = playlist.get(currentPlaylistIndex);

        currentTimeInMillisecond = 0;

        currentFrame = 0;

        musicPlayerGUI.disablePlayButton();
        musicPlayerGUI.updateSongDetails(currentSong);
        musicPlayerGUI.updatePlaybackSlider(currentSong);

        playCurrentSong();

    }

    public void prevSong(){
        if(playlist == null) return;

        if(currentPlaylistIndex - 1 < 0) {
            JOptionPane.showMessageDialog(musicPlayerGUI, "No more previous songs!!", "Previous Song Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pressedPrev = true;

        if(!songFinished)

            stopSong();

        currentPlaylistIndex--;
        currentSong = playlist.get(currentPlaylistIndex);

        currentTimeInMillisecond = 0;

        currentFrame = 0;

        musicPlayerGUI.disablePlayButton();
        musicPlayerGUI.updateSongDetails(currentSong);
        musicPlayerGUI.updatePlaybackSlider(currentSong);

        playCurrentSong();

    }

    public void startMusicThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!isPlaying){

                        synchronized (playSignal){
                            isPlaying = true;

                            playSignal.notify();

                        }

                        //play from last position
                        advancedPlayer.play(currentFrame, Integer.MAX_VALUE);
                    }else {

                        //play from beginning
                        advancedPlayer.play();
                    }
                } catch (JavaLayerException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void startPlaybackSliderThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                if(!isPlaying){
                    try{

                        synchronized (playSignal){
                            playSignal.wait();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


                while(isPlaying && !songFinished && !pressedNext && !pressedPrev){

                    try{
                        currentTimeInMillisecond++;

                        int calculatedFrame = (int) (currentTimeInMillisecond * 2.02 * currentSong.getFrameRatePerMillisecond());

                        musicPlayerGUI.setPlaybackSliderValue(calculatedFrame);

                        Thread.sleep(1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void playbackStarted(PlaybackEvent evt) {
        songFinished = false;
        pressedPrev = false;
        pressedNext = false;
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        if(!isPlaying){
            currentFrame = (int) (currentFrame + (int) (double) evt.getFrame() * currentSong.getFrameRatePerMillisecond());
        }else{

            //prevent calling "nextSong" or "prevSong" twice
            if(pressedNext || pressedPrev) return;

            songFinished = true;

            if(playlist == null){
                musicPlayerGUI.enablePlayButton();
            }else{
                if(currentPlaylistIndex == playlist.size() - 1){
                    musicPlayerGUI.disablePlayButton();
                }else{
                    nextSong();
                }
            }
        }
    }
}













