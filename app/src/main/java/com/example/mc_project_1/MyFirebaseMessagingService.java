package com.example.mc_project_1;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.mc_project_1.*;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String CHANNEL_ID = "senderchannel";
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public  void onMessageReceived(@NonNull RemoteMessage remoteMessage){

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        super.onMessageReceived(remoteMessage);
        Intent intent = new Intent(this, HomeActivity.class); //it can send notification to activity
        NotificationManager  manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(manager);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent intent1 =PendingIntent.getActivities(this,0,new Intent[]{intent},PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);


        Notification notification = null;
//        Notification.notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_android_black_24dp)
                    .setAutoCancel(true)
                    .setContentIntent(intent1)
                    .setContentIntent((intent1))
                    .build();

        }

        manager.notify(notificationId,notification);



    }

    private void createNotificationChannel(NotificationManager manager) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"channelName",NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My Description");
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);

            manager.createNotificationChannel(channel);
        }



    }


}
