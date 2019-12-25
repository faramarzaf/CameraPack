# CameraPack  



Before starting development on your application with the Camera API, you should make sure your manifest has the appropriate declarations to allow use of camera hardware and other related features.  
- Camera Permission: Your application must request permission to use a device camera.

```xml
 <uses-permission android:name="android.permission.CAMERA" />
```

**Note**: If you are using the camera by invoking an existing camera app, your application does not need to request this permission.  
- Camera Features: Your application must also declare use of camera features, for example:  

```xml
<uses-feature android:name="android.hardware.camera" />
``` 

Adding camera features to your manifest causes Google Play to prevent your application from being installed to devices that do not include a camera or do not support the camera features you specify.  
If your application can use a camera or camera feature for proper operation, but does not require it, you should specify this in the manifest by including the android:required attribute, and setting it to false:  
```xml
<uses-feature android:name="android.hardware.camera" android:required="false" />
``` 
- Storage Permission: If your application saves images or videos to the device's external storage (SD Card), you must also specify this in the manifest.  
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
``` 
- Audio Recording Permission: For recording audio with video capture, your application must request the audio capture permission.  
```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
``` 

- Detecting camera hardware  
```java
/** Check if this device has a camera */
private boolean checkCameraHardware(Context context) {
    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
        // this device has a camera
        return true;
    } else {
        // no camera on this device
        return false;
    }
}
```

To access the primary camera, use the `Camera.open()` method and be sure to catch any exceptions, as shown in the code below:  
 
```java
/** A safe way to get an instance of the Camera object. */
public static Camera getCameraInstance(){
    Camera c = null;
    try {
        c = Camera.open(); // attempt to get a Camera instance
    }
    catch (Exception e){
        // Camera is not available (in use or does not exist)
    }
    return c; // returns null if camera is unavailable
}
```
 
- Creating a preview class  

For users to effectively take pictures or video, they must be able to see what the device camera sees. A camera preview class is a `SurfaceView` that can display the live image data coming from a camera, so users can frame and capture a picture or video.  
The following example code demonstrates how to create a basic camera preview class that can be included in a View layout. This class implements `SurfaceHolder.Callback` in order to capture the callback events for creating and destroying the view, which are needed for assigning the camera preview input.  
```java
/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}
```


The following layout code provides a very basic view that can be used to display a camera preview. In this example, the FrameLayout element is meant to be the container for the camera preview class. This layout type is used so that additional picture information or controls can be overlaid on the live camera preview images.  
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="horizontal"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    >
  <FrameLayout
    android:id="@+id/camera_preview"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:layout_weight="1"
    />

  <Button
    android:id="@+id/button_capture"
    android:text="Capture"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    />
</LinearLayout>
```

In the activity for your camera view, add your preview class to the FrameLayout element shown in the example above. Your camera activity must also ensure that it releases the camera when it is paused or shut down.   
```java
public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }
}
```

- Capturing pictures  
```java
private PictureCallback mPicture = new PictureCallback() {

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null){
            Log.d(TAG, "Error creating media file, check storage permissions");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
};
```java
// Add a listener to the Capture button
Button captureButton = (Button) findViewById(R.id.button_capture);
captureButton.setOnClickListener(
    new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get an image from the camera
            mCamera.takePicture(null, null, picture);
        }
    }
);
```






