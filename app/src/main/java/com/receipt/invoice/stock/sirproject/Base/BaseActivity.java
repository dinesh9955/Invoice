package com.receipt.invoice.stock.sirproject.Base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.receipt.invoice.stock.sirproject.Invoice.SavePref;

public class BaseActivity extends AppCompatActivity {
    public int numberPostion = 0;
    public int languagePosition = 0;

    public SavePref pref = new SavePref();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref.SavePref(this);
        numberPostion = pref.getNumberFormatPosition();
        languagePosition = pref.getLanguagePosition();
    }
}
