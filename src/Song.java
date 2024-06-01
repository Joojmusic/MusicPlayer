/*
* CLASS USED
* TO
* DESCRIBE
* A SONG - SONG DETAILS
* */

import com.mpatric.mp3agic.Mp3File;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import javax.swing.text.html.HTML;
import java.io.File;

import static java.lang.String.format;

public class Song {

    private String songTitle;
    private String songArtist;
    private String songLength;
    private String filePath;
    private Mp3File mp3File;
    private double frameRatePerMillisecond;

    public Mp3File getMp3File() {
        return mp3File;
    }

    public double getFrameRatePerMillisecond() {
        return frameRatePerMillisecond;
    }

    public Song(String filePath){
        this.filePath = filePath;

        try{

            mp3File = new Mp3File(filePath);
            songLength = convertToSongLengthFormat();
            frameRatePerMillisecond = (double) mp3File.getFrameCount() / mp3File.getLengthInMilliseconds();

            AudioFile audioFile = AudioFileIO.read(new File(filePath));

            Tag tag = audioFile.getTag();
            if(tag != null){
                songTitle = tag.getFirst(FieldKey.TITLE);
                songArtist = tag.getFirst(FieldKey.ARTIST);
            }else {
                songTitle = "N/A";
                songArtist = "N/A";
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String convertToSongLengthFormat(){
        long minutes = mp3File.getLengthInSeconds() / 60;
        long seconds = mp3File.getLengthInSeconds() % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);

        return formattedTime;

    }


    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongLength() {
        return songLength;
    }

    public String getFilePath() {
        return filePath;
    }
}




