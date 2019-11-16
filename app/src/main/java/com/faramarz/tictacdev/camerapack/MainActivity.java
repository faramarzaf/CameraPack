package com.faramarz.tictacdev.camerapack;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.faramarz.tictacdev.camerapack.advance.AdvanceActivity;
import com.faramarz.tictacdev.camerapack.advance2.Advance2Activity;
import com.faramarz.tictacdev.camerapack.simple.SimpleActivity;
import com.faramarz.tictacdev.camerapack.video.RecordVideoActivity;
import com.faramarz.tictacdev.camerapack.video.RecordVideoActivity2;
import com.faramarz.tictacdev.camerapack.video.Sample3.RecordVideoActivity3;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSimple, btnAdvance, btnAdvance2, btnRecordVideo, btnRecordVideo2, btnRecordVideo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();
        clickEvents();
        getPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSimple:
                startActivity(new Intent(this, SimpleActivity.class));
                break;
            case R.id.btnAdvance:
                startActivity(new Intent(this, AdvanceActivity.class));
                break;
            case R.id.btnAdvance2:
                startActivity(new Intent(this, Advance2Activity.class));
                break;

            case R.id.btnRecordVideo:
                startActivity(new Intent(this, RecordVideoActivity.class));
                break;

            case R.id.btnRecordVideo2:
                startActivity(new Intent(this, RecordVideoActivity2.class));
                break;

            case R.id.btnRecordVideo3:
                startActivity(new Intent(this, RecordVideoActivity3.class));
                break;
        }
    }

    private void clickEvents() {

        btnAdvance.setOnClickListener(this);
        btnSimple.setOnClickListener(this);
        btnAdvance2.setOnClickListener(this);
        btnRecordVideo.setOnClickListener(this);
        btnRecordVideo2.setOnClickListener(this);
        btnRecordVideo3.setOnClickListener(this);

    }

    private void bind() {
        btnAdvance = findViewById(R.id.btnAdvance);
        btnSimple = findViewById(R.id.btnSimple);
        btnAdvance2 = findViewById(R.id.btnAdvance2);
        btnRecordVideo = findViewById(R.id.btnRecordVideo);
        btnRecordVideo2 = findViewById(R.id.btnRecordVideo2);
        btnRecordVideo3 = findViewById(R.id.btnRecordVideo3);
    }

    void getPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }


}
