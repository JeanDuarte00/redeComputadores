package com.codai.utils;

import sun.audio.AudioStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class AudioPlayer implements Runnable {

    private AudioPlayer audioPlayer;
    private String backgroundMusic;
    private Map<String, String>effects  = new HashMap<String, String>();
    Thread th;


    public AudioPlayer AudioPlayer() {

        if ( this.audioPlayer == null ) {
            this.audioPlayer = new AudioPlayer();
        }
        return this.audioPlayer;

    }

    public void setTrackSound (String backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
    }

    public void addSoundEffect (String key, String musicFile) {
        this.effects.put(key, musicFile);
    }

    public String getMusic (String musicKey) {
        return this.effects.get(musicKey);
    }

    public void play(String musicKey) {
        InputStream music;
        try {
            String name = this.getMusic(musicKey);
            music = new FileInputStream(new File(name));
            AudioStream audioStream = new AudioStream(music);
            sun.audio.AudioPlayer.player.start(audioStream);

        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    public void stopTrack() {
        th.interrupt();
    }

    public void playTrack() {
        th = new Thread(this);
        th.start();
    }

    public void run() {

        while (true){
            playTrack(backgroundMusic);
        }

    }

    private void playTrack(String fileName) {

        File soundFile = new File(fileName);
        AudioInputStream audioInputStream = null;

        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        AudioFormat audioFormat = audioInputStream.getFormat();
        SourceDataLine line = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try
        {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
        }
        catch (LineUnavailableException err)
        {
            err.printStackTrace();
        }
        catch (Exception err)
        {
            err.printStackTrace();
        }
        line.start();
        int nBytesRead = 0;
        byte[] abData = new byte[128000];
        while (nBytesRead != -1) {

            try {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                int nBytesWritten = line.write(abData, 0, nBytesRead);
            }
        }
        line.drain();
        line.close();
    }
}