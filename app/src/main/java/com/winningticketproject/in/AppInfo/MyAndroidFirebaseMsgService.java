package com.winningticketproject.in.AppInfo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.ChatWithOther.Chat_With_Other;
import com.winningticketproject.in.R;

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat

        System.out.println("-----------"+remoteMessage.getData());

        if (remoteMessage.getData().size()>0){

            createNotification(remoteMessage);
        }
    }

    private void createNotification(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        String messageBody = remoteMessage.getNotification().getBody();
        String notification_type = remoteMessage.getData().get("notification_type");

        if (notification_type.equals("event_chat")) {

            Intent intent = new Intent(this, Chat_With_Other.class);
            intent.putExtra("sender_id", remoteMessage.getData().get("sender_id"));
            intent.putExtra("sender_first_name", remoteMessage.getData().get("sender_first_name"));
            intent.putExtra("receiver_id", remoteMessage.getData().get("receiver_id"));
            intent.putExtra("receiver_first_name", remoteMessage.getData().get("receiver_first_name"));
            intent.putExtra("event_id", remoteMessage.getData().get("event_id"));
            intent.putExtra("notification_type", "chat");

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Default";
            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this,channelId);
            notification.setContentTitle(title);
            notification.setContentText(messageBody);
            notification.setAutoCancel(true);
            notification.setPriority(NotificationCompat.PRIORITY_HIGH);
            notification.setLargeIcon(largeIcon);
//            notification.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));
            notification.setSound(notificationSoundURI);
            notification.setContentIntent(resultIntent);
//            Vibrate
            long[] v = { 1000, 1000, 1000, 1000, 1000};
            notification.setVibrate(v);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notification.setSmallIcon(R.drawable.notification_icon);
                notification.setColor(getResources().getColor(R.color.colorblack));
            } else {
                notification.setSmallIcon(R.drawable.notification_icon);
            }

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, notification.build());

            Intent intent_data = new Intent("Notification_count");
            intent_data.putExtra("count", "1");
            broadcaster.sendBroadcast(intent_data);

        }


        if (notification_type.equals(null) || notification_type.equals("") || notification_type.equals("null")){
            Intent intent = new Intent(this, Splash_screen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
            notification.setContentTitle(title);
            notification.setContentText(messageBody);
            notification.setAutoCancel(true);
            notification.setLargeIcon(largeIcon);
            notification.setSound(notificationSoundURI);
            notification.setContentIntent(resultIntent);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notification.setSmallIcon(R.drawable.notification_icon);
                notification.setColor(getResources().getColor(R.color.colorblack));
            } else {
                notification.setSmallIcon(R.drawable.notification_icon);
            }

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            try {
                nm.notify(0, notification.build());
            } catch (Exception e) {
            }
        }


    }
}