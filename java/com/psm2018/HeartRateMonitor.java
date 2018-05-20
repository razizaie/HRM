package com.psm2018;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;


public class HeartRateMonitor extends Activity {


    TextView mSwitcher;

    Animation in;
    Animation out;

    int fadeCount;

    DatabaseReference reference;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_heart = database.getReference("Patient");

    final DatabaseReference databaseId = FirebaseDatabase.getInstance().getReference("Patient");

    TextView heartbeat;

    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;
    private static View image = null;
    private static TextView text = null;

    private static WakeLock wakeLock = null;
    private static int TIME_OUT = 25000;
    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];

    private int pStatus = 0;
    private TextView txtProgress;
    private ProgressBar progressBar;
    private Handler handler = new Handler();

    public static enum TYPE {
        GREEN, RED
    };

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate);

        preview = (SurfaceView) findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        image = findViewById(R.id.image);
        text = (TextView) findViewById(R.id.text);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

        txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fadeCount = 0;

        handler = new Handler();

        mSwitcher = (TextView) findViewById(R.id.textView12);
        mSwitcher.setText("old text");

        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(3000);

        out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(3000);
        out.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeCount++;
                if (fadeCount == 3){
                    mSwitcher.setText("");
//                    Intent i = new Intent(getApplication() ,  HeartRateMonitor.class);
//                    startActivity(i);
                }
                else {
                    if (fadeCount == 1) {
                        mSwitcher.setText("Calculating for average heart rate");
                    } else {
                        mSwitcher.setText("Please wait..");
                    }

                    mSwitcher.startAnimation(in);
                    handler.postDelayed(mFadeOut, 5000);
                }
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }
        });

        //mSwitcher.startAnimation(out);
        mSwitcher.setText("Please wait...");
        mSwitcher.startAnimation(in);

       /*
        mSwitcher.startAnimation(out);
        mSwitcher.setText("Text 2.");
        mSwitcher.startAnimation(in);
        */

        handler.postDelayed(mFadeOut, 5000);




        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus <= 100) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(pStatus);
//                            txtProgress.setText(pStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus++;
                }
            }
        }).start();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                heartbeat  = (TextView)findViewById(R.id.text);
                String heart = heartbeat.getText().toString().trim();
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                String formattedDate = sdf.format(d);
                SimpleDateFormat fds = new SimpleDateFormat("MM - dd");
                String formattedday = fds.format(d);
                System.out.println(formattedDate);
                int heartrate = Integer.parseInt(heart);
                if (heartrate < 100)
                {

                    Toast toast = Toast.makeText(HeartRateMonitor.this,"You are in a normal resting heart rate range", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    Common.currentUser.setHeartbeat(heart);
//                    table_heart.child(Common.currentUser.getName()).child("heartbeat").setValue(Common.currentUser.getHeartbeat());
                    String key = table_heart.child(Common.currentUser.getName()).child("heartbeat").push().getKey();
                    table_heart.child(Common.currentUser.getName()).child("heartbeat").child(key).child("bpm").setValue(Common.currentUser.getHeartbeat());
//                    String key = table_heart.child(Common.currentUser.getName()).child("heartbeat").;
//                    String key = table_heart.child(Common.currentUser.getName()).child("heartbeat").push().getKey();
                    table_heart.child(Common.currentUser.getName()).child("heartbeat").child(key).child("time").setValue(formattedDate);
                    table_heart.child(Common.currentUser.getName()).child("heartbeat").child(key).child("day").setValue(formattedday);
                    Intent i = new Intent(HeartRateMonitor.this, Result.class);
                    startActivity(i);
                    finish();
                }
                else
                {

//                    Toast toast = Toast.makeText(HeartRateMonitor.this,"You are in a ABNORMAL heart rate range!", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Common.currentUser.setHeartbeat(String.valueOf(heart));
                    String key = table_heart.child(Common.currentUser.getName()).child("heartbeat").push().getKey();
                    table_heart.child(Common.currentUser.getName()).child("heartbeat").child(key).child("bpm").setValue(Common.currentUser.getHeartbeat());
                    table_heart.child(Common.currentUser.getName()).child("heartbeat").child(key).child("time").setValue(formattedDate);
                    table_heart.child(Common.currentUser.getName()).child("heartbeat").child(key).child("day").setValue(formattedday);

                    Intent i = new Intent(HeartRateMonitor.this, ResultAb.class);
                    startActivity(i);
                    finish();

                }
//                heartbeat = (TextView)findViewById(R.id.text);
//                String heart = heartbeat.getText().toString().trim();
//                int heartrate = Integer.parseInt(heart);
//                Common.currentUser.setHeartbeat(heart);
//                System.out.println("Before");
//                table_heart.child(Common.currentUser.getName()).child("heartbeat").setValue(Common.currentUser.getHeartbeat());
//                System.out.println("After");
//                Intent i = new Intent(HeartRateMonitor.this, Result.class);
//                startActivity(i);
//                finish();

            }
        }, TIME_OUT);
    }
    private Runnable mFadeOut =new Runnable(){

        @Override
        public void run() {
            //Speed up the last fade-out so that the Activity opens faster
            if (fadeCount == 2){
                out.setDuration(2000);
            }
            mSwitcher.startAnimation(out);
        }
    };


    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();

        wakeLock.acquire();

        camera = Camera.open();

        startTime = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();

        wakeLock.release();

        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private static PreviewCallback previewCallback = new PreviewCallback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            // Log.i(TAG, "imgAvg="+imgAvg);
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    // Log.d(TAG, "BEAT!! beats="+beats);
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
                image.postInvalidate();
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 10) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                // Log.d(TAG,
                // "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                text.setText(String.valueOf(beatsAvg));
                startTime = System.currentTimeMillis();
                beats = 0;
            }
            processing.set(false);
        }
    };

    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         * {@inheritDoc}
         */
        @SuppressLint("LongLogTag")
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                //camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);

            } catch (Throwable t) {
                Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }
}