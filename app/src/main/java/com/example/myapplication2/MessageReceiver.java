package com.example.myapplication2;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageReceiver<MessageReciever> extends AsyncTask<Void, Void, String> {

    private WeakReference<display1> activityWeakReference;
    MessageReceiver(display1 activity){
        activityWeakReference=new WeakReference<display1>(activity);
    }
    final StackTraceElement se =Thread.currentThread().getStackTrace()[2];
    private String data=null;



    @Override
    protected String doInBackground(Void... params) {
        Log.d(se.getClassName() + "." + se.getMethodName(), "start");
        String line="";
        StringBuilder buffer = new StringBuilder();
        try {
            Log.d("message@@","message sender try");
            ServerSocket serverSocket = new ServerSocket(2291);
            serverSocket.setSoTimeout(50000);
            Socket socket = serverSocket.accept();
            socket.setKeepAlive(true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int readed = in.read();
            Log.d("message@@","readed bytes : "+readed);

            while ((line = in.readLine()) != null){
                Log.i("message@@","line : "+ line);
                Log.i("message@@","line : "+ line.length());
                buffer.append(line);
            }

            socket.close();
            serverSocket.close();
//                return line;
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("message@@","onpost : "+result);
        display1 activity=activityWeakReference.get();
        activity.summaryTV.setVisibility(View.VISIBLE);
        activity.main_layout.setVisibility(View.GONE);
        MessageReceiver.this.data = result;
        activity.summaryTV.setText(result);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("message@@","onpre : ");
        display1 activity=activityWeakReference.get();

    }

    public String getData() {
        return data;
    }


}
/*

ublic class MessageReciever extends AsyncTask<Void, Void, String> {

    private WeakReference<MainActivity> activityWeakReference;
    MessageReciever(MainActivity activity){
        activityWeakReference=new WeakReference<MainActivity>(activity);
    }
    final StackTraceElement se = Thread.currentThread().getStackTrace()[2];
    private String data = null;

    @Override
    protected String doInBackground(Void... params) {
        Log.d(se.getClassName() + "." + se.getMethodName(), "start");
        String line="";
        StringBuilder buffer = new StringBuilder();
        try {
            Log.d("message@@","message sender try");
            ServerSocket serverSocket = new ServerSocket(2291);
            serverSocket.setSoTimeout(50000);
            Socket socket = serverSocket.accept();
            socket.setKeepAlive(true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int readed = in.read();
            Log.d("message@@","readed bytes : "+readed);

            while ((line = in.readLine()) != null){
                Log.i("message@@","line : "+ line);
                Log.i("message@@","line : "+ line.length());
                buffer.append(line);
            }

            socket.close();
            serverSocket.close();
//                return line;
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("message@@","onpost : "+result);
        MainActivity activity=activityWeakReference.get();
        activity.summaryTV.setVisibility(View.VISIBLE);
        activity.main_layout.setVisibility(View.GONE);
        MessageReciever.this.data = result;
        activity.summaryTV.setText(result);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("message@@","onpre : ");
        MainActivity activity=activityWeakReference.get();

    }

    public String getData() {
        return data;
    }
}
*/
