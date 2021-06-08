package com.receipt.invoice.stock.sirproject.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.Utility;

import java.util.ArrayList;

public class SubscribeActivity extends BaseActivity{

    private static final String TAG = "SubscribeActivity";
    RecyclerView recycler_invoices;

    SubscribeAdapter invoicelistAdapterdt;

    ArrayList<ItemSubscribe> arrayListNames = new ArrayList<>();

//    int languagePostion = 0;

    //SavePref pref = new SavePref();

    private BillingProcessor bp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subscribe);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Constant.toolbar(LanguageActivity.this, "Select Language");
        //  pref.SavePref(LanguageActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);

        TextView textViewDone = toolbar.findViewById(R.id.textViewDone);
        textViewDone.setVisibility(View.GONE);
        titleView.setText("GO PREMIUM");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        languagePostion = pref.getLanguagePosition();
//
//        textViewDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pref.setLanguagePosition(languagePostion);
//                onBackPressed();
//            }
//        });


        recycler_invoices = findViewById(R.id.recycler_invoices);


        arrayListNames = getPlans();






        invoicelistAdapterdt = new SubscribeAdapter(SubscribeActivity.this, arrayListNames);
        recycler_invoices.setAdapter(invoicelistAdapterdt);
       // invoicelistAdapterdt.updateLanguagePosition(languagePostion);
        recycler_invoices.setLayoutManager(new LinearLayoutManager(SubscribeActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_invoices.setHasFixedSize(true);
        invoicelistAdapterdt.notifyDataSetChanged();




        bp = new BillingProcessor(this, AllSirApi.LICENSE_KEY, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//                showToast("onProductPurchased: " + productId);
//                updateTextViews();
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
//                showToast("onBillingError: " + Integer.toString(errorCode));
            }
            @Override
            public void onBillingInitialized() {
//                showToast("onBillingInitialized");
//                readyToPurchase = true;
//                updateTextViews();
            }
            @Override
            public void onPurchaseHistoryRestored() {
                //showToast("onPurchaseHistoryRestored");
                for(String sku : bp.listOwnedProducts())
                    Log.e(TAG, "Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    Log.e(TAG, "Owned Subscription: " + sku);
                //updateTextViews();
            }
        });

    }

    private ArrayList<ItemSubscribe> getPlans() {

        ArrayList<ItemSubscribe> itemSubscribeArrayList = new ArrayList<>();

        ItemSubscribe itemSubscribe1 = new ItemSubscribe();
        itemSubscribe1.setId("com.sir.oneyear");
        itemSubscribe1.setPlan("35.99");
        itemSubscribe1.setPlanName("Annual Plan");
        itemSubscribe1.setDescription("Subscribe to Annual Package and get upto 25% off. USD 35.99 only annually with unlimited access to all features except Stock Tracking.");
        itemSubscribeArrayList.add(itemSubscribe1);

        ItemSubscribe itemSubscribe2 = new ItemSubscribe();
        itemSubscribe2.setId("com.sir.onemonth");
        itemSubscribe2.setPlan("3.99");
        itemSubscribe2.setPlanName("Value Plan");
        itemSubscribe2.setDescription("Monthly package starts at USD 3.99 only with unlimited access to all features except Stock Tracking.");
        itemSubscribeArrayList.add(itemSubscribe2);

        ItemSubscribe itemSubscribe3 = new ItemSubscribe();
        itemSubscribe3.setId("com.sir.onemonth_add");
        itemSubscribe3.setPlan("3.99");
        itemSubscribe3.setPlanName("Extra Users");
        itemSubscribe3.setDescription("Assign additional users with limited or unlimited access for one time payment of USD 3.99 only per user.");
        itemSubscribeArrayList.add(itemSubscribe3);

        ItemSubscribe itemSubscribe4 = new ItemSubscribe();
        itemSubscribe4.setId("com.sir.oneyear_add");
        itemSubscribe4.setPlan("11.99");
        itemSubscribe4.setPlanName("Extra Companies");
        itemSubscribe4.setDescription("Create additional companies under the same credentials for one time payment of USD 11.99 only per additional company.");
        itemSubscribeArrayList.add(itemSubscribe4);

        return itemSubscribeArrayList;
    }


    public void onClickBack(ItemSubscribe itemSubscribe) {
        Log.e(TAG, "positionAA "+itemSubscribe);
        //languagePostion = position;
        bp.purchase(this, itemSubscribe.getId());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

}
