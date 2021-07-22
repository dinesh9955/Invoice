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
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.sirapp.Invoice.InvoiceActivity;
import com.sirapp.Invoice.SavePref;
import com.sirapp.R;

import org.json.JSONObject;

public class MyReciever extends WakefulBroadcastReceiver
{

    SavePref pref = new SavePref();
    private static final String TAG = "MyReciever";

    Context context = null;

    private static final String ACTION_REGISTRATION
            = "com.google.android.c2dm.intent.REGISTRATION";
    private static final String ACTION_RECEIVE
            = "com.google.android.c2dm.intent.RECEIVE";


   // SavePref pref = new SavePref();


    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    String NOTIFICATION_CHANNEL_ID = "1000";
    String NOTIFICATION_CHANNEL_ID2 = "2000";

    String NOTIFICATION_CHANNEL_NAME2 = "FullBatteryAlarm2";

    int badgeCount = 0;

    @Override
    public void onReceive(Context context11, Intent intent)
    {
        context = context11;

        pref.SavePref(context11);

        Log.e(TAG, "onReceive: " );

      // pref.SavePref(context11);




        String action = intent.getAction();
        for (String key : intent.getExtras().keySet()) {
            Log.d(
                    "TAG",
                    "{UniversalFCM}->onReceive: key->"
                            + key + ", value->" + intent.getExtras().get(key)
            );
        }


        String data = intent.getStringExtra("data");
                Log.e(
                "TAGGGString ",
                "{UniversalFCMQQQ}->"+data
        );


                try{
                    JSONObject jsonObject = new JSONObject(data);
                    if(jsonObject.has("notificationtype")){
                        String notificationtype = jsonObject.getString("notificationtype");
                        if(notificationtype.equalsIgnoreCase("invoice seen")){
                            if(!pref.getCustomerName().equalsIgnoreCase("")){
                                sendNotification(context11);
                            }
                        }
                    }
                }catch (Exception e){

                }




//            try
//            {
//                JSONObject json = new JSONObject(data);
//
//                String title=json.getString("title").toString();
//                String body=json.getString("body").toString();
//
//                Log.d(
//                        "TAGGGString ",
//                        "{title}->"+title +" XXX "+body);
//
//
//                Log.e(TAG, "title Message========123456: " + title);
//                Log.e(TAG, "body  Message========1234567: " + body);
//
//
//                JSONObject jsonObjectBody = new JSONObject(body);
//                if(!pref.getId().equalsIgnoreCase("")){
//                    if(Utility.isRunning(context) == false){     // if app is close
//                            String message = jsonObjectBody.getString("message");
//                            sendNotificationNotification(title, message);
//                    }else{    // if app is open
//                        String code = jsonObjectBody.getString("code");
//                        if(code.equalsIgnoreCase("cancelride")){
//                            Intent intent1 = new Intent("OPEN_NEW_ACTIVITY3");
//                            Bundle bundleObject = new Bundle();
//                            bundleObject.putString("key" , "3");
//                            bundleObject.putString("body" , body);
//                            bundleObject.putString("code" , code);
//                            intent1.putExtras(bundleObject);
//                            context11.sendBroadcast(intent1);
//                        }else if(code.equalsIgnoreCase("acceptride")){
//                            Intent intent1 = new Intent("OPEN_NEW_ACTIVITY3");
//                            Bundle bundleObject = new Bundle();
//                            bundleObject.putString("key" , "3");
//                            bundleObject.putString("body" , body);
//                            bundleObject.putString("code" , code);
//                            intent1.putExtras(bundleObject);
//                            context11.sendBroadcast(intent1);
//                        }else if(code.equalsIgnoreCase("ridestart")){
//                            Intent intent1 = new Intent("OPEN_NEW_ACTIVITY3");
//                            Bundle bundleObject = new Bundle();
//                            bundleObject.putString("key" , "3");
//                            bundleObject.putString("body" , body);
//                            bundleObject.putString("code" , code);
//                            intent1.putExtras(bundleObject);
//                            context11.sendBroadcast(intent1);
//                        }else if(code.equalsIgnoreCase("ridecomplete")){
//                            Intent intent1 = new Intent("OPEN_NEW_ACTIVITY3");
//                            Bundle bundleObject = new Bundle();
//                            bundleObject.putString("key" , "3");
//                            bundleObject.putString("body" , body);
//                            bundleObject.putString("code" , code);
//                            intent1.putExtras(bundleObject);
//                            context11.sendBroadcast(intent1);
//                        }
//
//                    }
//
//
//                }
//
//
//               // sendNotification(title,body);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }




        abortBroadcast();




    }

    private void sendNotification(Context context11) {

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
                .setContentText(context11.getString(R.string.noti_invoice_seen)+" "+  pref.getCustomerName())
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