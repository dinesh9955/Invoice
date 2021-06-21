package com.receipt.invoice.stock.sirproject.Settings;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Report.ReportActivity;
import com.receipt.invoice.stock.sirproject.Report.ReportAdapter;
import com.receipt.invoice.stock.sirproject.RetrofitApi.ApiInterface;
import com.receipt.invoice.stock.sirproject.RetrofitApi.RetrofitInstance;

import java.util.ArrayList;

public class SettingsActivity extends BaseActivity {
    private static final String TAG = "SettingsActivity" ;
    ApiInterface apiInterface;
    RecyclerView recycler_invoices;


    SettingsAdapter invoicelistAdapterdt;

    ArrayList<String> arrayListNames = new ArrayList<>();
    ArrayList<Integer> arrayListIcons = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Constant.toolbar(SettingsActivity.this, getString(R.string.header_settings));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        recycler_invoices = findViewById(R.id.recycler_invoices);

        arrayListIcons.add(R.drawable.comma_st);
        arrayListIcons.add(R.drawable.online_payment_st);
        arrayListIcons.add(R.drawable.subscribe_st);
        arrayListIcons.add(R.drawable.upgrade_st);
        arrayListIcons.add(R.drawable.restore_purchase_st);
        arrayListIcons.add(R.drawable.invite_friend_st);
        arrayListIcons.add(R.drawable.faq_st);
        arrayListIcons.add(R.drawable.language_st);
        arrayListIcons.add(R.drawable.about_st);
        arrayListIcons.add(R.drawable.rate_st);
        arrayListIcons.add(R.drawable.terms_st);
        arrayListIcons.add(R.drawable.privacy_st);
        arrayListIcons.add(R.drawable.support_st);


        arrayListNames.add(getString(R.string.setting_CommaFormatSelection));
        arrayListNames.add(getString(R.string.setting_OnlinePaymentGateway));
        arrayListNames.add(getString(R.string.setting_Subscribe));
        arrayListNames.add(getString(R.string.setting_Upgrade));
        arrayListNames.add(getString(R.string.setting_RestorePurchases));
        arrayListNames.add(getString(R.string.setting_InviteFriends));
        arrayListNames.add(getString(R.string.setting_FAQs));
        arrayListNames.add(getString(R.string.setting_Language));
        arrayListNames.add(getString(R.string.setting_Abouttheapp));
        arrayListNames.add(getString(R.string.setting_Ratetheapp));
        arrayListNames.add(getString(R.string.setting_Termsofuse));
        arrayListNames.add(getString(R.string.setting_PrivacyPolicy));
        arrayListNames.add(getString(R.string.setting_Support));


        invoicelistAdapterdt = new SettingsAdapter(SettingsActivity.this, arrayListIcons, arrayListNames);
        recycler_invoices.setAdapter(invoicelistAdapterdt);
        recycler_invoices.setLayoutManager(new LinearLayoutManager(SettingsActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_invoices.setHasFixedSize(true);
        invoicelistAdapterdt.notifyDataSetChanged();
    }
}
