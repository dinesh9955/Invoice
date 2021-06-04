package com.receipt.invoice.stock.sirproject.Base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.receipt.invoice.stock.sirproject.Invoice.SavePref;

public class BaseFragment extends Fragment {
    public int numberPostion = 0;
    public int languagePosition = 0;

    public SavePref pref = new SavePref();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref.SavePref(getActivity());
        numberPostion = pref.getNumberFormatPosition();
        languagePosition = pref.getLanguagePosition();
    }
}
