package com.sirapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplay;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Constant.Constant;
import com.sirapp.FCM.MyMessageDisplayImplementation;
import com.sirapp.Home.Home_Activity;
import com.sirapp.Invoice.InvoiceActivity;
import com.sirapp.SignupSignin.Signin_Activity;
import com.sirapp.SignupSignin.WalkThroughActivity;
import com.sirapp.Utils.Utility;
import com.sirapp.R;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

public class Splash_Activity extends BaseActivity {
    private static final String TAG = "Splash_Activity";
    boolean LOGGED_IN = false;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isWorking = false;

        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_);

        pref.setCustomerName("");
//        Map<String, Object> eventValue = new HashMap<String, Object>();
//        eventValue.put("App_open", "App Open");
//
//        Gson gson = new Gson();
//        String stringConvert = gson.toJson(eventValue);
//
//        AppsFlyerLib.getInstance().start(getApplicationContext(), ""+"stringConvert");




       /* FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
*/
//        SavePref pref  = new SavePref();
//        pref.SavePref(Splash_Activity.this);
        pref.setTemplate(0);

        FacebookSdk.setAdvertiserIDCollectionEnabled(true);
        FacebookSdk.setAdvertiserIDCollectionEnabled(true);
        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ACTIVATED_APP);
// OPTIONALLY the following two lines if AutoInitEnabled is set to false in manifest:
        FacebookSdk.getAutoLogAppEventsEnabled();
        FacebookSdk.setAutoLogAppEventsEnabled(true);
        FacebookSdk.setAutoInitEnabled(true);
        setAutoLogAppEventsEnabled(true);
        FacebookSdk.getAutoLogAppEventsEnabled();
        FacebookSdk.fullyInitialize();

        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.PARAM_1, "App_Open");
        AppsFlyerLib.getInstance().trackEvent(Splash_Activity.this, "app_open", eventValue);


        Bundle params2 = new Bundle();
        params2.putString("event_name", "App Open");
        firebaseAnalytics.logEvent("app_open", params2);


//
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>(){
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String deviceToken = instanceIdResult.getToken();
//                Log.e("newToken", deviceToken);
//
//            }
//        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Utility.checkAndRequestPermissions(Splash_Activity.this, REQUEST_ID_MULTIPLE_PERMISSIONS)) {
                    getLogin();
                }else{
                }
            }
        }, 500);


     //   sendNotification(this);

//        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
//        String token = instanceID.getToken(Config.GOOGLE_PROJECT_ID,
//                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);


//        GoogleSignInOptions gso = new GoogleSignInOptions
//                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.d("cloudMessage", "getInstanceId failed", task.getException());
//                        }else if(task.isSuccessful ()) {
//                            try {
//                                // Get new Instance ID token
//                                String token = task.getResult ().getToken ();
//                                // Log and toast
//                                Log.d ( "cloudMessage", "Token: " + token );
//                            }catch (Exception ex){
//
//                            }
//                        }
//                    }
//                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                if(Utility.checkAdditionPermissionsCheck(Splash_Activity.this, permissions, grantResults, REQUEST_ID_MULTIPLE_PERMISSIONS)) {
                    getLogin();
                }
            }
        }

    }


    private void getLogin() {

        requestPermission();
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(Splash_Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 234);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    SharedPreferences preferences = getSharedPreferences(Constant.PREF_BASE, MODE_PRIVATE);
                    LOGGED_IN = preferences.getBoolean(Constant.LOGGED_IN, false);
                    if (LOGGED_IN) {
                        Intent i = new Intent(Splash_Activity.this, Home_Activity.class);
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(Splash_Activity.this, WalkThroughActivity.class);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseInAppMessaging.getInstance().setMessageDisplayComponent(new MyMessageDisplayImplementation());
        FirebaseInAppMessaging.getInstance().setMessagesSuppressed(false);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        isWorking = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isWorking = false;
    }



    String NOTIFICATION_CHANNEL_ID = "1000";
    String NOTIFICATION_CHANNEL_ID2 = "2000";

    String NOTIFICATION_CHANNEL_NAME2 = "FullBatteryAlarm2";

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
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(context11.getString(R.string.noti_invoice_seen)+" "+  pref.getCustomerName()))
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
