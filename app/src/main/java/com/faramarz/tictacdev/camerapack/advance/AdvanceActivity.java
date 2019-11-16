package com.faramarz.tictacdev.camerapack.advance;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
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

    /*List<Camera.Size> sizes;
    Camera.Size optimalSize;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance);
        getPermissions();
        bind();

        btnTakePhoto.setOnClickListener(this);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);








        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                imageView.setImageBitmap(bitmap);
                imageView.setRotation(90);
                camera.startPreview();
            }
        };

    }

    private void setRatioFix(){
        Camera.Size size = camera.getParameters().getPreviewSize();
        //landscape
        float ratio = (float)size.width/size.height;
        surfaceView =  findViewById(R.id.sv);
        int new_width=0, new_height=0;
        if(surfaceView.getWidth()/surfaceView.getHeight()<ratio){
            new_width = Math.round(surfaceView.getHeight()*ratio);
            new_height = surfaceView.getHeight();
        }else{
            new_width = surfaceView.getWidth();
            new_height = Math.round(surfaceView.getWidth()/ratio);
        }
        surfaceView.setLayoutParams(new FrameLayout.LayoutParams(new_width, new_height));



    }

    private void bind() {
        imageView = findViewById(R.id.iv);
        surfaceView = findViewById(R.id.sv);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);

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
      /*  if (holder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // make any resize, rotate or reformatting changes here
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            camera.setDisplayOrientation(90);
        } else {
            camera.setDisplayOrientation(0);
        }
        // start preview with new settings
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            Log.d("TAG", "Error starting camera preview: " + e.getMessage());
        }*/

        fixCameraRotationPreview();
     //   setRatioPreviewCorrectly();
    }


   /* private void setRatioPreviewCorrectly() {
        sizes = camera.getParameters().getSupportedPreviewSizes();
        optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        camera.getParameters().setPreviewSize(optimalSize.width, optimalSize.height);

    }*/

    private void fixCameraRotationPreview() {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(5); //480h x 720w
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        ///parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(parameters);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (display.getRotation() == Surface.ROTATION_0) {
            camera.setDisplayOrientation(90);
        } else if (display.getRotation() == Surface.ROTATION_270) {
            camera.setDisplayOrientation(180);
        }

        camera.startPreview();
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

/*    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }
    }*/


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;

        // Find size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
