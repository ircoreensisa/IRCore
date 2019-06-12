package com.example.ircore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class textReader extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;
    final EmploisDutempsFictif edt = new EmploisDutempsFictif();


    private CameraSource mCameraSource;
    private TextView mTextView;
    private SurfaceView mCameraView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("debug","textReader a été lancé");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_reader_layout);


        Intent intent =getIntent();
        if(intent!=null){
            this.startCameraSource();

        }
    }

    private void startCameraSource() {



        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        final TextView mTextView= (TextView)findViewById(R.id.txt_view);
        final SurfaceView mCameraView= (SurfaceView)findViewById(R.id.surfaceView);

        if (!textRecognizer.isOperational()) {
            Log.w("DEBUG" ,"Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(textReader.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSION_REQUEST_CAMERA);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();

                    if (items.size() != 0 ){

                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i=0;i<items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }

                                //mTextView.setText(stringBuilder.toString());
                                // remplacer le mTextView par le nom de a salle seul , parcourir la liste des salles et reconnaitre les noms , si le strbuilder contient un nom de la
                                //liste alors il est dans cette salle.
                                // mettre un tete " vous etes dans la salle ..."
                                Salles salles = new Salles();
                                String texte = stringBuilder.toString().toLowerCase();
                                //EmploisDutempsFictif edt = new EmploisDutempsFictif();
                                //String cour = edt.getRandomclass();

                                for (Salle s: salles.getList() ){
                                    String salle = s.getName().toLowerCase();
                                    if (texte.contains(salle)){

                                        StringBuilder salledetectee = new StringBuilder();
                                        salledetectee.append("vous êtes devant la salle ");
                                        salledetectee.append(salle);

                                        String cour = edt.getRandomclass(s.getId());

                                        salledetectee.append(cour);
                                        mTextView.setText(salledetectee);
                                    }


                                }

                            }
                        });
                    }
                }
            });
        }
    }

    public void onClick(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

    }
}
