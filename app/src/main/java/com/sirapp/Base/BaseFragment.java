package com.sirapp.Base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.sirapp.Invoice.SavePref;

public class BaseFragment extends Fragment {
    public int numberPostion = 0;
    public int languagePosition = 0;
    public FirebaseAnalytics firebaseAnalytics;
    public SavePref pref = new SavePref();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref.SavePref(getActivity());
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
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
    public void onResume() {
        super.onResume();
        callCheckFontSize();
    }

    public void callCheckFontSize(){
        BaseActivity activity = (BaseActivity)getActivity();
        activity.checkFontSize();
    }

}
