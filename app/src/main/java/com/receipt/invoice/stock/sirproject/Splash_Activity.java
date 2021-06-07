package com.receipt.invoice.stock.sirproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Home.Home_Activity;
import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.SignupSignin.Signin_Activity;
import com.receipt.invoice.stock.sirproject.Utils.Utility;

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

        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_);


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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Utility.checkAndRequestPermissions(Splash_Activity.this, REQUEST_ID_MULTIPLE_PERMISSIONS)) {
                    getLogin();
                }else{
                }
            }
        }, 500);




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
        SharedPreferences preferences = getSharedPreferences(Constant.PREF_BASE, MODE_PRIVATE);
        //if (preferences.contains(Constant.FIRST_TIME)) {

        LOGGED_IN = preferences.getBoolean(Constant.LOGGED_IN, false);
        if (LOGGED_IN) {

            Intent i = new Intent(Splash_Activity.this, Home_Activity.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(Splash_Activity.this, Signin_Activity.class);
            startActivity(i);
        }
    }
}
