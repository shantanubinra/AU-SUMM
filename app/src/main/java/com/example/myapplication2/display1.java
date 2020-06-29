package com.example.myapplication2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public  class display1 extends AppCompatActivity implements View.OnClickListener {
    public static final int RECORD_AUDIO = 0;
    private MediaRecorder myAudioRecorder;
    private File output = null;
    private Button start, stop, play, summary, select_file;
    TextView summaryTV;
    Button transcribee;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    private String[] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};
    LinearLayout main_layout;


    //    String final_path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display1);
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        start = findViewById(R.id.start1);
        stop = findViewById(R.id.stop1);
        play = findViewById(R.id.play1);
        transcribee = findViewById(R.id.transcribe);
        summary = findViewById(R.id.summary);
        select_file = findViewById(R.id.button);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        play.setOnClickListener(this);
        select_file.setOnClickListener(this);
        stop.setEnabled(false);
        play.setEnabled(false);
        summaryTV = findViewById(R.id.summaryTV);
        main_layout = findViewById(R.id.main_layout);
        File dir = new File(Environment.getExternalStorageDirectory() + "/speechtt");

        // output = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myrecording.3gp
        // ";
        try {
            output = File.createTempFile("sound", ".mp3", dir);
        } catch (IOException e) {
            return;
        }


        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.reset();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(output.getAbsolutePath());
        Log.i("message@@", output.getAbsolutePath());

        //TRANSCRIBE ON CLICK
        transcribee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("message@@", "transcribe clicked");
                // Log.d("message@@", output.getAbsolutePath());
                // MessageSender ms = new MessageSender();
                // ms.execute(output.getAbsolutePath());
                // Log.d("message@@", "send completed");
            }
        });
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("message@@", "summary clicked");

                MessageReceiver mr = new MessageReceiver(display1.this);
                mr.execute();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start1:
                Log.d("message@@", "start_pressed");
                start(v);
                break;
            case R.id.stop1:
                stop(v);
                break;
            case R.id.play1:
                try {
                    play(v);
                } catch (IOException e) {
                    Log.i("IOException", "Error in play");
                }
                break;
            case R.id.button:
                Log.d("message@@", "Select_pressed");
                select(v);
                break;
            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 200:
                Log.d("message@@", "permission granted");
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToWriteAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) display1.super.finish();
        if (!permissionToWriteAccepted) display1.super.finish();
    }

    public void start(View view) {
        try {
            Log.d("message@@", "start");
            //myAudioRecorder=new MediaRecorder();
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        start.setEnabled(false);
        stop.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
    }

    public void stop(View view) {
        Log.d("message@@", "Stop_pressed");

        myAudioRecorder.stop();
        Log.d("message@@", "Stop_pressed");
        myAudioRecorder.release();
        myAudioRecorder = null;

        stop.setEnabled(false);
        play.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();
    }

    public void play(View view) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        MediaPlayer m = new MediaPlayer();
        m.setDataSource(output.getAbsolutePath());
        m.prepare();
        m.start();
        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
    }

    private void select(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 10);
        /*Log.d("message@@", "Stop12_pressed");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Log.d("message@@", "Stop13_pressed");
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory()
                + "/speechtt/");
       // Log.d('message@@','selectfilena');
        Log.d("message@@", "Stop11_pressed");
        intent.setDataAndType(uri, "audio/*");
        startActivity(Intent.createChooser(intent, "Open folder"));*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && requestCode == 10){
            Uri uriSound=data.getData();

            try {
                play1(this, uriSound);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
   /* protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //protected void onActivityResult(int requestCode,int resultCode,Intent data){

            if(requestCode == 1){

                if(resultCode == RESULT_OK){

                    //the selected audio.
                    Uri uri = data.getData();
                    play1(this, uri);
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

*/
    private void play1(Context context, Uri uri) throws IOException ,NullPointerException{


            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(context, uri);
            mp.prepare();
            mp.start();

        }
    }

















