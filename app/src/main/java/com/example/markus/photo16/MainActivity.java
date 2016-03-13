package com.example.markus.photo16;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.util.Log;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import junit.runner.Version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Camera myCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Camera.Parameters parameters;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File directory = new File(Environment.getExternalStorageDirectory() + "/pictures/");

        if (!directory.exists()) {
            directory.mkdir();
        }

        myCamera = null;

        int numCameras;
        numCameras = Camera.getNumberOfCameras();
        try {
            myCamera = Camera.open(1);
        }catch (Exception e){
            Log.e("", "open camera failed", e);
        }
        try {
            myCamera.setPreviewTexture(new SurfaceTexture(10));
        } catch (IOException e1) {
            Log.e("", e1.getMessage());
        }
        //SurfaceView mview = new SurfaceView(getBaseContext());
        //try {
        //    myCamera.setPreviewDisplay(mview.getHolder());
        //} catch (IOException e) {
        //   e.printStackTrace();
        //}
        //try {
        //    myCamera.setPreviewDisplay(null);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}

        parameters = myCamera.getParameters();

        List<Camera.Size> supported = parameters.getSupportedPreviewSizes();
        Camera.Size size1 = parameters.getPreviewSize();
        parameters.setPreviewSize(640, 480);
        Camera.Size size2 = parameters.getPreviewSize();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        parameters.setPictureFormat(ImageFormat.JPEG);
        myCamera.setParameters(parameters);
        try {
            myCamera.startPreview();
        } catch( Exception e) {
            Log.i("Hello World ", "Exception @ startPreview");
            myCamera.release();
            return;
        }
        Log.i("Hello World ", "before takePicture");
        try {
            myCamera.takePicture(null, photoCallback, photoCallback, photoCallback);
        } catch( Exception e) {
            Log.i("Hello World ", "Exception @ takePicture");
            myCamera.stopPreview();
            myCamera.release();
            return;
        }
        Log.i("Hello World ", "after takePicture");
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    Camera.PictureCallback photoCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera myCameraCallback) {
            Log.i("Hello World ", "picture-taken (in photoCallback)");

            if (data == null) {
                Log.i("Hello World ", "picture-taken (in photoCallback); data is null");
            }
            else {
                Log.i("Hello World ", "picture-taken (in photoCallback); data is not null continuing with write");
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/pictures");
                File file = new File(myDir, "pic1.jpeg");
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(data);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                myCamera.stopPreview();
                myCamera.release();
                finish();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.markus.photo16/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.markus.photo16/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}