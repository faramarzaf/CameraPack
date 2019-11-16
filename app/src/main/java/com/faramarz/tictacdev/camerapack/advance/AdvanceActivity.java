package com.faramarz.tictacdev.camerapack.advance;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.faramarz.tictacdev.camerapack.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;

public class AdvanceActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {

    SurfaceView surfaceView;
    ImageView imageView;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback pictureCallback;
    Camera camera;
    Button btnTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance);
        getPermissions();

        imageView = findViewById(R.id.iv);
        surfaceView = findViewById(R.id.sv);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);

        btnTakePhoto.setOnClickListener(this);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                imageView.setImageBitmap(bitmap);

            }
        };


    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {



           camera = Camera.open();
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
          /*  android.hardware.Camera.Parameters parameters;
            parameters = camera.getParameters();
            camera.startFaceDetection();
         parameters.setPreviewFrameRate(20);
            List<Camera.Size> customSizes = parameters.getSupportedPreviewSizes();
            Camera.Size customSize = customSizes.get(0); //Added size
            parameters.setPreviewSize(customSize.width, customSize.height);
         parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.setParameters(parameters);
           camera.setDisplayOrientation(90);
*/
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }


    @Override
    public void onClick(View v) {

        camera.takePicture(null, null, pictureCallback);
    }


    void getPermissions() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();


    }


}
