package com.sirapp.Base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.sirapp.Invoice.SavePref;
import com.sirapp.Utils.LocaleHelper;

public class BaseActivity extends AppCompatActivity {
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
}
