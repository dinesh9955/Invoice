package com.receipt.invoice.stock.sirproject.Base;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.receipt.invoice.stock.sirproject.Abc;
import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.Utils.ContextUtils;
import com.receipt.invoice.stock.sirproject.Utils.LocaleHelper;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    public int numberPostion = 0;
    public int languagePosition = 0;

    public FirebaseAnalytics firebaseAnalytics;
    public SavePref pref = new SavePref();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref.SavePref(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        numberPostion = pref.getNumberFormatPosition();
        languagePosition = pref.getLanguagePosition();




    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
