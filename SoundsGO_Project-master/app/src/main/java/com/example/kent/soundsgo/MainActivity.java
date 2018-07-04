package com.example.kent.soundsgo;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kent.soundsgo.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button play, stop, begin;
    private MediaRecorder rec;
    private String File;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        begin = (Button) findViewById(R.id.record);
        stop.setEnabled(false);
        play.setEnabled(false);
        File = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        rec = new MediaRecorder();
        rec.setAudioSource(MediaRecorder.AudioSource.MIC);  //to use the mic
        rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// to record in 3gp
        rec.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        rec.setOutputFile(File);

        begin.setOnClickListener(new View.OnClickListener() { //to start to record
            @Override
            public void onClick(View v) {
                try {
                    rec.prepare();
                    rec.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
                begin.setEnabled(false);
                stop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording laucnhed", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() { //to stop and save the audio
            @Override
            public void onClick(View v) {
                rec.stop();
                rec.release();
                rec = null;
                begin.setEnabled(true);
                stop.setEnabled(false);
                play.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Audio Recorded successfully", Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() { // to listen the sound
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(File);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Listening Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // make something
                }
            }
        });
    }
};