package com.spamsoftware.hubertmaciejczyk.caffeteriaapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class QrScannerActivity extends AppCompatActivity {

    TextView textView;
    SurfaceView cameraPreview;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;

    static final String COMMAND_DELETE = "DELETE";
    static final String COMMAND_ADD = "ADD";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        cameraPreview = findViewById(R.id.camerapreview);
        textView = findViewById(R.id.textView);


        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 640)
                .setAutoFocusEnabled(true)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();

                if(qrCode.size() != 0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

                            switch (qrCode.valueAt(0).displayValue){
                                case "kawa":
                                    vibrator.vibrate(1000);
                                    setResultAndClose(COMMAND_ADD);
                                    break;
                                case "collect_coffe_stickers":
                                    vibrator.vibrate(1000);
                                    setResultAndClose(COMMAND_DELETE);
                                    break;
                            }


                        }
                    });
                }
            }
        });
    }

    private void setResultAndClose(String command){
        Intent resultIntent = new Intent();
        cameraSource.stop();
        resultIntent.putExtra("command", command);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
