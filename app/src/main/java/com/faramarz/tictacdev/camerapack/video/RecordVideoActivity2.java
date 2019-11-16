package com.faramarz.tictacdev.camerapack.video;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.faramarz.tictacdev.camerapack.R;

public class RecordVideoActivity2 extends AppCompatActivity implements View.OnClickListener {

    VideoView videoView;
    Uri videoFileUri;
    Button captureVideoButton, playVideoButton, captureWithoutDataVideoButton;
    public static int VIDEO_CAPTURED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video2);
        bind();
        clickEvents();
        playVideoButton.setEnabled(false);
    }

    private void bind() {
        videoView = this.findViewById(R.id.VideoView);
        captureVideoButton = this.findViewById(R.id.CaptureVideoButton);
        playVideoButton = this.findViewById(R.id.PlayVideoButton);
        captureWithoutDataVideoButton = this.findViewById(R.id.CaptureVideoWithoutDataButton);

    }

    private void clickEvents() {
        captureVideoButton.setOnClickListener(this);
        playVideoButton.setOnClickListener(this);
        captureWithoutDataVideoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == captureVideoButton) {
            Intent captureVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult(captureVideoIntent, VIDEO_CAPTURED);
        }
        if (v == captureWithoutDataVideoButton) {
            playVideoButton.setEnabled(false);
            Intent captureVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
            startActivity(captureVideoIntent);
        } else if (v == playVideoButton) {
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
}
