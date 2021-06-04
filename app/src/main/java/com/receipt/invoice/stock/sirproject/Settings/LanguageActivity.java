package com.receipt.invoice.stock.sirproject.Settings;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;

public class LanguageActivity extends BaseActivity implements LanguageCallback{

    private static final String TAG = "LanguageActivity";
    RecyclerView recycler_invoices;

    LanguageAdapter invoicelistAdapterdt;

    ArrayList<String> arrayListNames = new ArrayList<>();

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

        titleView.setText("Select Language");

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
                onBackPressed();
            }
        });


        recycler_invoices = findViewById(R.id.recycler_invoices);

        arrayListNames.add("English");
        arrayListNames.add("Arabic");
        arrayListNames.add("German");
        arrayListNames.add("Dutch");
        arrayListNames.add("French");
        arrayListNames.add("Italian");
        arrayListNames.add("Spanish");
        arrayListNames.add("Portuguese");




        invoicelistAdapterdt = new LanguageAdapter(LanguageActivity.this, arrayListNames, LanguageActivity.this);
        recycler_invoices.setAdapter(invoicelistAdapterdt);
        invoicelistAdapterdt.updateLanguagePosition(languagePostion);
        recycler_invoices.setLayoutManager(new LinearLayoutManager(LanguageActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_invoices.setHasFixedSize(true);
        invoicelistAdapterdt.notifyDataSetChanged();

    }

    @Override
    public void onLanguageClickBack(int position) {
        Log.e(TAG, "positionAA "+position);
        languagePostion = position;
    }
}
