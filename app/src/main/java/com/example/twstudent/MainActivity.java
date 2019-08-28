package com.example.twstudent;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "channel_id";
    public static final String ACTION_PUSH_LESSON = "com.tab.tw.push";
    public static final String EXTRA_LESSON_INFO = "lesson_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(MainActivity.this, MyService.class));
        } else {
            startService(new Intent(MainActivity.this, MyService.class));
        }
    }

    public void onClickSendLesson(View view) {
        String s = getLessonInfo();
        sendNotification(s);
    }

    private void sendNotification(String info) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "study_channel_id";
            CharSequence channelName = "Study Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(ACTION_PUSH_LESSON);
        intent.putExtra(EXTRA_LESSON_INFO, info);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 22, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getString(R.string.notifiaction_content_title));
        builder.setContentText(getString(R.string.notification_content_text));
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = 201;
        notificationManager.notify(notificationId, notification);
    }

    private String getLessonInfo() {
        Gson gson = new Gson();
        Lesson lesson = new Lesson();
        lesson.setGrade("初一");
        lesson.setCode("c1lsp05");
        lesson.setSubject("历史");
        lesson.setName("千古一帝秦始皇");
        lesson.setTeacher("王宗琦");
        lesson.setUrl("https://static.chinaedu.com/commonplayer/play.html?uid=bf0d46c6-57e9-4729-a04f-0e444a4a3a93&c=1\" frameborder=\"0\" align=\"");
        return gson.toJson(lesson);
    }
}
