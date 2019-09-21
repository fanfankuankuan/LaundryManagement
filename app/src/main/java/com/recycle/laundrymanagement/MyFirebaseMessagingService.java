package com.recycle.laundrymanagement;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseService";
    private Context mContext;

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (mContext == null) {
            mContext = getBaseContext();
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        sendNotification(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */
    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(RemoteMessage remoteMessage) {
        String status_id = remoteMessage.getData().get("status_id");
        String status_type  = remoteMessage.getData().get("status_type");
        String machine_id = remoteMessage.getData().get("machine_id");
        String user_email  = remoteMessage.getData().get("user_email");

        String noticeTitle = " ";
        String noticeText = " ";

        Log.d(TAG, "\nstatus_id = " + status_id);

        switch (status_id) {
            case "3": noticeTitle = "Laundry Finished";
                noticeText = remoteMessage.getData().get("body");
                break;
            case "4": noticeTitle = "Laundry Over Timed";
                noticeText = remoteMessage.getData().get("body");
                break;
            default: break;
        }

//        Bitmap icon =  BitmapFactory.decodeResource(mContext.getResources(),
//                Config.trafficMap.get(type));

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getApplicationContext(), WashingMachineOverview.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_HIGH);

            //Configure Notification Channel
            notificationChannel.setDescription("Laundry Notification");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.washing_machine)
                .setContentTitle(noticeTitle)
                .setAutoCancel(true)
                .setSound(defaultSound)
//                .setLargeIcon(icon)
                .setContentText(noticeText)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);

        notificationManager.notify(1, notificationBuilder.build());
    }



}
