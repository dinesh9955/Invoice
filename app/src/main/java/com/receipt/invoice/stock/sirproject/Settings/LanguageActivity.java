package com.receipt.invoice.stock.sirproject.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Home.Home_Activity;
import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.LocaleHelper;

import java.util.ArrayList;
import java.util.Locale;

public class LanguageActivity extends BaseActivity implements LanguageCallback{

    private static final String TAG = "LanguageActivity";
    RecyclerView recycler_invoices;

    LanguageAdapter invoicelistAdapterdt;

//    ArrayList<String> arrayListNames = new ArrayList<>();

    int languagePostion = 0;

    //SavePref pref = new SavePref();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

       // Constant.toolbar(LanguageActivity.this, "Select Language");
      //  pref.SavePref(LanguageActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);

        TextView textViewDone = toolbar.findViewById(R.id.textViewDone);

        titleView.setText(getString(R.string.header_select_language));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        languagePostion = pref.getLanguagePosition();

        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setLanguagePosition(languagePostion);
                String languageName = "en";
                if(languagePostion == 0){
                    languageName = "en";
                }else if(languagePostion == 1){
                    languageName = "ar";
                }else if(languagePostion == 2){
                    languageName = "de";
                }else if(languagePostion == 3){
                    languageName = "nl";
                }else if(languagePostion == 4){
                    languageName = "fr";
                }else if(languagePostion == 5){
                    languageName = "it";
                }else if(languagePostion == 6){
                    languageName = "es";
                }else if(languagePostion == 7){
                    languageName = "pt";
                }

                Log.e(TAG, "languagePostion "+languagePostion);
                Log.e(TAG, "languageName "+languageName);

                setLocale(languageName);

            }
        });


        recycler_invoices = findViewById(R.id.recycler_invoices);

        String[] languageArray = getResources().getStringArray(R.array.languageArray);

//        arrayListNames.add("English");
//        arrayListNames.add("Arabic");
//        arrayListNames.add("German");
//        arrayListNames.add("Dutch");
//        arrayListNames.add("French");
//        arrayListNames.add("Italian");
//        arrayListNames.add("Spanish");
//        arrayListNames.add("Portuguese");




        invoicelistAdapterdt = new LanguageAdapter(LanguageActivity.this, languageArray, LanguageActivity.this);
        recycler_invoices.setAdapter(invoicelistAdapterdt);
        invoicelistAdapterdt.updateLanguagePosition(languagePostion);
        recycler_invoices.setLayoutManager(new LinearLayoutManager(LanguageActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_invoices.setHasFixedSize(true);
        invoicelistAdapterdt.notifyDataSetChanged();



        Locale current = getResources().getConfiguration().locale;



//        Gson gson = new Gson();
//        String dd = gson.toJson(current);
//
         Log.e(TAG,  "currentAA "+current.getLanguage());

    }

    @Override
    public void onLanguageClickBack(int position) {
        Log.e(TAG, "positionAA "+position);
        languagePostion = position;
    }


    public void setLocale(String localeName) {
            Context context = LocaleHelper.setLocale(this, localeName);
            Locale myLocale = new Locale(localeName);
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent intent = new Intent(this, Home_Activity.class);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finishAffinity();
            finish();

    }
}
