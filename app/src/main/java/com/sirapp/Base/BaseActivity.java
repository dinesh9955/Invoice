package com.sirapp.Base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.sirapp.Invoice.SavePref;
import com.sirapp.Utils.LocaleHelper;

public class BaseActivity extends AppCompatActivity {
    String TAG = "BaseActivity";

    public int numberPostion = 0;
    public int languagePosition = 0;

    public FirebaseAnalytics firebaseAnalytics;
    public SavePref pref = new SavePref();

    public Context primaryBaseActivity;//THIS WILL KEEP ORIGINAL INSTANCE

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref.SavePref(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        numberPostion = pref.getNumberFormatPosition();
        languagePosition = pref.getLanguagePosition();

//        float scaleAA = getResources().getConfiguration().fontScale;
//        Log.e(TAG, "manufacturerModelscaleAA :" + scaleAA);
//
//        if(1.0 < scaleAA){
//            Log.e(TAG, "manufacturerModelscaleAA11 :" + scaleAA);
//            showDialogOK(BaseActivity.this, "App font size is not compatible, Please reduce your font size.",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case DialogInterface.BUTTON_POSITIVE:
//                                dialog.dismiss();
//                                startActivity(new Intent(Settings.ACTION_DISPLAY_SETTINGS));
//                                break;
//                            case DialogInterface.BUTTON_NEGATIVE:
//                                dialog.dismiss();
//                                break;
//                        }
//                    }
//                });
//        }else{
//            Log.e(TAG, "manufacturerModelscaleAA22 :" + scaleAA);
//        }




    }


    public String getLanguage(){
        String languageName = "english";
        if(languagePosition == 0){
            languageName = "english";
        }else if(languagePosition == 1){
            languageName = "arabic";
        }else if(languagePosition == 2){
            languageName = "german";
        }else if(languagePosition == 3){
            languageName = "dutch";
        }else if(languagePosition == 4){
            languageName = "french";
        }else if(languagePosition == 5){
            languageName = "italian";
        }else if(languagePosition == 6){
            languageName = "spanish";
        }else if(languagePosition == 7){
            languageName = "portuguese";
        }
        return languageName;
    }

    @Override
    protected void attachBaseContext(Context base) {
        primaryBaseActivity = base;
        super.attachBaseContext(LocaleHelper.onAttach(primaryBaseActivity));
    }


    private static void showDialogOK(Activity splashScreen, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(splashScreen)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("CANCEL", okListener)
                .create()
                .show();
    }
}
