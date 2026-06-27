package com.example.adhdapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class listenToaudios extends AppCompatActivity {

    public static final String message = "hello";
    private MediaPlayer mediaPlayer;
    private Button playPauseButton;
    private SeekBar audioSeekBar;
    private TextView audioTitleTextView;
    private Handler handler = new Handler();
    private FirebaseStorage storage;
    private String currentAudioFile;
    private boolean wasPlaying = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_toaudios);

        storage = FirebaseStorage.getInstance();

        playPauseButton = findViewById(R.id.playPauseButton);
        audioSeekBar = findViewById(R.id.audioSeekBar);
        audioTitleTextView = findViewById(R.id.audioTitleTextView);

        String receivedMessage = getIntent().getStringExtra(message);
        if (receivedMessage != null) {
            currentAudioFile = "adhd" + receivedMessage + ".mp3";
        } else {
            currentAudioFile = "adhd1.mp3"; // Default to first audio if no message received
        }

        audioTitleTextView.setText(currentAudioFile);
        loadAudio();

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    pauseAudio();
                } else if (mediaPlayer != null) {
                    resumeAudio();
                }
            }
        });

        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading audio...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void loadAudio() {
        playPauseButton.setEnabled(false);
        showProgressDialog();
        StorageReference audioRef = storage.getReference().child(currentAudioFile);
        audioRef.getDownloadUrl().addOnSuccessListener(uri -> {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(uri.toString());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mp -> {
                    SharedPreferences prefs = getSharedPreferences("AudioProgress", MODE_PRIVATE);
                    int savedProgress = prefs.getInt(currentAudioFile, 0);
                    mediaPlayer.seekTo(savedProgress);

                    audioSeekBar.setMax(mediaPlayer.getDuration());
                    updateSeekBar();

                    playPauseButton.setEnabled(true);
                    dismissProgressDialog();
                    resumeAudio(); // Automatically start playing from the saved position
                });
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error preparing audio: " + e.getMessage());
                dismissProgressDialog();
            }
        }).addOnFailureListener(e -> {
            showError("Error loading audio: " + e.getMessage());
            dismissProgressDialog();
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        playPauseButton.setEnabled(false);
    }

    private void updateSeekBar() {
        if (mediaPlayer != null) {
            audioSeekBar.setProgress(mediaPlayer.getCurrentPosition());
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseButton.setText("Play");
            wasPlaying = true;
        }
    }

    private void resumeAudio() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playPauseButton.setText("Pause");
            updateSeekBar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && wasPlaying) {
            resumeAudio();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseAudio();
        saveAudioProgress();
    }

    private void saveAudioProgress() {
        if (mediaPlayer != null) {
            SharedPreferences.Editor editor = getSharedPreferences("AudioProgress", MODE_PRIVATE).edit();
            editor.putInt(currentAudioFile, mediaPlayer.getCurrentPosition());
            editor.apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            saveAudioProgress();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
        dismissProgressDialog();
    }
}