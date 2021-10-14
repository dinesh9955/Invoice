package com.sirapp.Base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.sirapp.Home.Home_Activity;
import com.sirapp.Invoice.SavePref;
import com.sirapp.R;
import com.sirapp.Utils.LocaleHelper;

public class BaseActivity extends AppCompatActivity {
    String TAG = "BaseActivity";

    int count = 0;

    public int numberPostion = 0;
    public int languagePosition = 0;

    public FirebaseAnalytics firebaseAnalytics;
    public SavePref pref = new SavePref();

    public Context primaryBaseActivity;//THIS WILL KEEP ORIGINAL INSTANCE

//    AlertDialog.Builder dialog = null;

    AlertDialog alertDialog = null;

    public boolean isWorking = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref.SavePref(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        numberPostion = pref.getNumberFormatPosition();
        languagePosition = pref.getLanguagePosition();

        //pref.setFontDialog("");

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

    @Override
    protected void onRestart() {
        super.onRestart();
        if(isWorking == true){
            checkFontSize();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isWorking == true){
            checkFontSize();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        count = 0;
    }

    public void checkFontSize(){
        if(!pref.getFontDialog().equalsIgnoreCase("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this, R.style.MyAlertDialogStyle);
            builder.setTitle(BaseActivity.this.getString(R.string.app_name));
            builder.setMessage("App font size is not compatible, Please reduce your font size.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_DISPLAY_SETTINGS));
                }
            });
            builder.setCancelable(false);
            alertDialog = builder.create();

            float scaleAA = getResources().getConfiguration().fontScale;
            Log.e(TAG, "manufacturerModelscaleAA :" + scaleAA);

            if(1.0 < scaleAA){
                if(count == 0){
                    count = 1;
                    if(alertDialog != null){
                        alertDialog.show();
                    }
                }
            }else{

            }
        }

    }



}
