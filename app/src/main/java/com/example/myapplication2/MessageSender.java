package com.example.myapplication2;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageSender extends AsyncTask<String,Void,Void> {
        Socket sock;
        DataOutputStream dos;
        PrintWriter pw;
        @Override
        protected Void doInBackground(String... voids) {
            Log.d("message@@","background");
            String selectedImagePath=voids[0];
            Log.d("message@@","path "+selectedImagePath);
            try{
                Log.d("message@@","insidde try");
                sock = new Socket("192.168.29.222", 2290);
//                    System.out.println("Connecting...");
                Log.d("message@@","socket completed");
                // sendfile
                File myFile = new File (selectedImagePath);
                Log.d("message@@",(int)myFile.length()+"");
                byte [] mybytearray  = new byte [(int)myFile.length()];
                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(mybytearray,0,mybytearray.length);
                OutputStream os = sock.getOutputStream();
                Log.d("message@@","sending2");
                os.write(mybytearray,0,mybytearray.length);

                os.flush();
                Log.d("message@@","finished");

                InputStream input = sock.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = reader.readLine();    // reads a line of text

                Log.d("message@@", String.valueOf(line));

                sock.close();

            }
            catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
                Log.d("message@@",e.toString());
            }

            return null;
        }
    }


