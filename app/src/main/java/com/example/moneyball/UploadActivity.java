package com.example.moneyball;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    VideoView videoView;
    Button captureVideoButton;
    Button captureWithoutDataVideoButton;
    Button playVideoButton;
    Button button;
    Uri videoFileUri;
    public static int VIDEO_CAPTURED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        captureVideoButton = findViewById(R.id.CaptureVideoButton);
        playVideoButton = findViewById(R.id.PlayVideoButton);
        videoView = findViewById(R.id.VideoView);
        button = findViewById(R.id.button);

        captureVideoButton.setOnClickListener(this);
        playVideoButton.setOnClickListener(this);
        playVideoButton.setEnabled(false);

    }

    public void onClick(View view) {
        if (view == captureVideoButton) {
            Intent captureVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult(captureVideoIntent, VIDEO_CAPTURED);
        } else if (view == playVideoButton) {
            videoView.setVideoURI(videoFileUri);
            videoView.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == VIDEO_CAPTURED) {
            videoFileUri = data.getData();
            playVideoButton.setEnabled(true);
        }

    }

    public void OnButtonClick(View view) {
        Intent myIntent = new Intent(UploadActivity.this,  PlaylistActivity.class);
        startActivity(myIntent);
    }
}

