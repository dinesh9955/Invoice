package com.sirapp.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sirapp.Invoice.InvoiceActivity;
import com.sirapp.Invoice.SavePref;
import com.sirapp.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    String NOTIFICATION_CHANNEL_ID2 = "2000";

    String NOTIFICATION_CHANNEL_NAME2 = "FullBatteryAlarm2";

    SavePref pref = new SavePref();

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);

        pref.SavePref(getApplicationContext());
        pref.setDeviceToken(""+s);

    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Log.e(TAG, "handleIntent: " + intent);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            //handleNotification(remoteMessage.getNotification().getBody());
            sendNotification(getApplicationContext(), remoteMessage.getNotification().getBody());
        }

//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
//
//            try {
//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                handleDataMessage(json);
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
//        }
    }



    private void sendNotification(Context context11, String msg) {

        NotificationManager notificationManager = (NotificationManager) context11.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID2, NOTIFICATION_CHANNEL_NAME2, importance);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(context11, notificationChannel.getId());

        } else {
            builder = new NotificationCompat.Builder(context11);
        }



        Intent notificationIntent = new Intent(context11, InvoiceActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context11, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        builder = builder
                .setSmallIcon(R.mipmap.noti_icon)
                // .setColor(ContextCompat.getColor(context, R.color.color))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(context11.getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setContentText(msg)
                // .setContentText("Level: "+level+"% | Remaining Time: "+(hour)+"h "+(min)+"min")
                .setPriority(Notification.PRIORITY_MAX)
//                .setCategory(Notification.CATEGORY_SERVICE)
//                .setTicker("VINlocity")
//                .setContentText("VINlocity Driver is tracking.")
//                .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(true)
                .setSound(null)
                .setAutoCancel(true)
                .setOngoing(true);
        notificationManager.notify(11, builder.build());
        // startForeground(11, builder.build());



    }


}