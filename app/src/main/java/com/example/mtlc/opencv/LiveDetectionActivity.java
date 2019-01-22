// This sample is based on "Camera calibration With OpenCV" tutorial:
// http://docs.opencv.org/doc/tutorials/calib3d/camera_calibration/camera_calibration.html
//
// It uses standard OpenCV asymmetric circles grid pattern 11x4:
// https://github.com/opencv/opencv/blob/2.4/doc/acircles_pattern.png.
// The results are the camera matrix and 5 distortion coefficients.
//
// Tap on highlighted pattern to capture pattern corners for calibration.
// Move pattern along the whole screen and capture data.
//
// When you've captured necessary amount of pattern corners (usually ~20 are enough),
// press "Calibrate" button for performing camera calibration.

package com.example.mtlc.opencv;

import org.json.JSONException;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LiveDetectionActivity extends Activity implements CvCameraViewListener2, OnTouchListener {
    private static final String TAG = "OCVSample::Activity";
    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);







    private CameraBridgeViewBase mOpenCvCameraView;
boolean gaussian;
boolean median;
boolean blur;
boolean protectionDisabled;
    void getSettings(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        blur= prefs.getBoolean("blur",false);
        median=prefs.getBoolean("median",false);
        gaussian=prefs.getBoolean("gaussian",false);
        protectionDisabled=prefs.getBoolean("protection",false);


    }



    HOGDescriptor hog;
    Mat mGray ;
    Mat mRgba ;
    CountDownTimer timer ;
    boolean busy=false;
    Handler h = new Handler();
    Runnable runnable;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
            case LoaderCallbackInterface.SUCCESS:
            {
                Log.i(TAG, "OpenCV loaded successfully");

                hog = new HOGDescriptor();

                hog.setSVMDetector(HOGDescriptor.getDefaultPeopleDetector());

                mOpenCvCameraView.enableView();
                mOpenCvCameraView.setOnTouchListener(LiveDetectionActivity.this);




            } break;
            default:
            {
                super.onManagerConnected(status);
            } break;
            }
        }
    };

    public LiveDetectionActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSettings();
        setContentView(R.layout.camera_calibration_surface_view);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_calibration_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setMaxFrameSize(640, 480);





        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }




    public void onCameraViewStarted(int width, int height) {
Log.e("called finale","sdfs");
        mGray = new Mat();
        mRgba = new Mat();


    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }
    org.opencv.core.Size ss = new Size(3,3);

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

             mRgba =inputFrame.rgba();

        if(gaussian){

            Imgproc.GaussianBlur(inputFrame.rgba(), mRgba, ss, 2);

        }
        if(median){
            Imgproc.medianBlur(inputFrame.rgba(), mRgba, 3);

        }
        if(blur){
            Imgproc.blur(inputFrame.rgba(), mRgba, ss);
        }


        if(protectionDisabled){

            return mRgba;
        }

        mGray=inputFrame.gray();


        //mRgba=inputFrame.rgba();

        MatOfRect bodies = new MatOfRect();
        hog.detectMultiScale(mGray, bodies, new MatOfDouble(0));
        Rect[] bodiesArray = bodies.toArray();
        long s = bodiesArray.length;
        if(s > 0) {
            Log.e("detected", "detected"+s);
            if(!busy){
                //set busy to true;
                busy = true;
                runOnUiThread(new Runnable() {
                    public void run() {
                        // do your work right here

                        timer =new CountDownTimer(3000, 3000) {

                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                busy=false;
                            }
                        }.start();
                    }
                });

                /// run backround task to store image
                new StoringImageTask().execute(mRgba);
            }



        }

        for (int i = 0; i < bodiesArray.length; i++)
            Imgproc.rectangle(mRgba, bodiesArray[i].tl(), bodiesArray[i].br(), FACE_RECT_COLOR, 3);

        return  mRgba;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch invoked");


        return false;
    }




    private class StoringImageTask extends AsyncTask<Mat, Integer, Long> {
        protected Long doInBackground(Mat... mat) {
            long d = 50;


            Mat m = mat[0];
            Bitmap bmp = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(m,bmp);
            try (FileOutputStream out = new FileOutputStream(getCacheDir()+"/intrusions/"+System.currentTimeMillis()+"_profile.jpg")) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                JsonUtils.addActivity("Intrusion Detected", date,"manuser",LiveDetectionActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }






            return d;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

        }
    }
}
