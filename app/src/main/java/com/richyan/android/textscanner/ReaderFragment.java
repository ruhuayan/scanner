package com.richyan.android.textscanner;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class ReaderFragment extends DialogFragment {
    private SurfaceView cameraView;
    private TextView textView;
    private CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    private DatabaseReference mDatabase;
    private String measurement;
    private String mark;
    private String unit;

    public ReaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RequestCameraPermissionID: {
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        return;
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_reader,null);

        cameraView = (SurfaceView) view.findViewById(R.id.surface_view);
        textView = (TextView) view.findViewById(R.id.text_view);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(view.getContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector not available");
        } else {
            /*initFirebase();
            mDatabase.child("marks").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        Mark mark = snapshot.getValue(Mark.class);
                        System.out.println(mark.getName());
                        //Log.d("item id ",item_snapshot.child("item_id").getValue().toString());
                        //Log.d("item desc",item_snapshot.child("item_desc").getValue().toString());
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });*/

            cameraSource = new CameraSource.Builder(view.getContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID );
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>(){

                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    final String pattern = "^\\d+(\\.\\d+)?";
                    if(items.size()!=0){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();

                                for(int i=0; i<items.size(); i++){
                                    TextBlock item = items.valueAt(i);


                                    String words[] = item.getValue().split("\\s+");

                                    for(int n=0; n<words.length; n++){
                                        if(words[n].matches(pattern)){
                                            measurement = words[n];
                                            //Toast.makeText(getContext(), words[n], Toast.LENGTH_LONG).show();
                                            textView.setText(words[n]);
                                            break;
                                        }

                                    }
                                    //stringBuilder.append(i +":" + item.getValue());
                                    //stringBuilder.append("\n");
                                }
                                //textView.setText(measurement);
                            }
                        });
                    }
                }
            });
        }


        builder.setView(view)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ReaderFragment.this.getDialog().cancel();
                    }
                });
        Dialog dialog = builder.create();
        return dialog;
    }

    private void initFirebase(){
        FirebaseApp.initializeApp(getContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    /*private String searchMark(String word){
        final String[] units = {"kg", "g", "lb", "ml", "m", "g", "l"};
        for(int i=0; i<units.length; i++){
            word.matches(units[i]); return units[i];
        }
        return "";
    }
    private String searchMeasurement(String word){
        final String[] units = {"kg","KG","Kg","kilo","Kilo", "g", "G", "lb", "Lb", "LB", "ml","ML", "Ml", "l", "L", "m","M", "cm","CM", "mm", "MM"};
        String pattern = "^\\d+(\\.\\d+)?";
        if(word.matches(pattern)) return word;
        else return "";
    }*/
}
