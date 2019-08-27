package com.example.twstudent;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.OutputStream;

import static com.example.twstudent.MainActivity.TW_SEND;

public class MyService extends Service {
    private static final int NOTIFICATION_ID = 101;
    private static final String CHANNEL_ID = "TW";

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = getNotification();
        startForeground(NOTIFICATION_ID, notification);
        IntentFilter filter = new IntentFilter(TW_SEND);
        registerReceiver(mReceiver, filter);

    }

    private Notification getNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID, "TW Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Tech World")
                .setContentText("Student service")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
        return notification;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        private static final String TAG = "mReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                String str = intent.getStringExtra("gson_string");
                Gson gson = new Gson();
                Lesson lesson = gson.fromJson(str, Lesson.class);
                Log.d(TAG, "lesson.getGrade() =" +  lesson.getGrade());
                Log.d(TAG, "lesson.getCode() =" +  lesson.getCode());
                Log.d(TAG, "lesson.getName() =" +  lesson.getName());
                Log.d(TAG, "lesson.getSubject() =" +  lesson.getSubject());
                Log.d(TAG, "lesson.getTeacher() =" +  lesson.getTeacher());
                Log.d(TAG, "lesson.getUrl() =" +  lesson.getUrl());


                execShellCmd("input keyevent 3");//home5
                execShellCmd("input swipe 200 250 500 250");
            }
        }
    };

    private void execShellCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
