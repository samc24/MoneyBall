package com.example.moneyball;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    VideoView videoView;
    Button captureVideoButton;
    Button captureWithoutDataVideoButton;
    Button playVideoButton;
    Button button;
    Uri videoFileUri;
    public static int VIDEO_CAPTURED = 1;
    private final int REQUEST_CAMERA = 2;

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

            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(UploadActivity.this, Manifest.permission.CAMERA)) {
                        Toast.makeText(getApplicationContext(), "We need permission to record videos for your challenge!", Toast.LENGTH_LONG).show();
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                    }
                } else {
                    Intent captureVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(captureVideoIntent, VIDEO_CAPTURED);
                }
            }
            else {
                Intent captureVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(captureVideoIntent, VIDEO_CAPTURED);
            }
        }
        else if (view == playVideoButton) {
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

    public void onButtonClick(View view) {
        Intent myIntent = new Intent(UploadActivity.this,  PlaylistActivity.class);
        startActivity(myIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Toast.makeText(getApplicationContext(),  "Thanks! Please click again", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
        }
    }
}

