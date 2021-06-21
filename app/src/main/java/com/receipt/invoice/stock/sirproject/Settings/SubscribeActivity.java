package com.receipt.invoice.stock.sirproject.Settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

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
        titleView.setText(getString(R.string.header_GO_PREMIUM));

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

                String productID = details.productId;
                String orderID = details.orderId;
                String purchaseToken = details.purchaseToken;
                Date purchaseTime = details.purchaseTime;
                DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                Log.e(TAG, "datemillis22 "+simple.format(purchaseTime));
                String date = simple.format(purchaseTime);

                callAPI(productId, purchaseToken, date);


//                SkuDetails dd = bp.getPurchaseListingDetails("");
//                SkuDetails dd = bp.getSubscriptionListingDetails("");
//                TransactionDetails dd = bp.getSubscriptionTransactionDetails("");
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
//                showToast("onBillingError: " + Integer.toString(errorCode));
               // Log.e(TAG, "onBillingError "+error.getMessage());
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
        itemSubscribe1.setSubscription_type("oneyear");
        itemSubscribe1.setProductId("com.sirapp.oneyear");
        itemSubscribe1.setPlan("35.99");
        itemSubscribe1.setPlanName(getString(R.string.settings_paln_title_1));
        itemSubscribe1.setDescription(getString(R.string.settings_paln_desc_1));
        itemSubscribeArrayList.add(itemSubscribe1);

        ItemSubscribe itemSubscribe2 = new ItemSubscribe();
        itemSubscribe2.setSubscription_type("onemonth");
        itemSubscribe2.setProductId("com.sirapp.onemonth");
        itemSubscribe2.setPlan("3.99");
        itemSubscribe2.setPlanName(getString(R.string.settings_paln_title_2));
        itemSubscribe2.setDescription(getString(R.string.settings_paln_desc_2));
        itemSubscribeArrayList.add(itemSubscribe2);

        ItemSubscribe itemSubscribe3 = new ItemSubscribe();
        itemSubscribe3.setSubscription_type("onemonth_add");
        itemSubscribe3.setProductId("com.sirapp.onemonth_add");
        itemSubscribe3.setPlan("3.99");
        itemSubscribe3.setPlanName(getString(R.string.settings_paln_title_3));
        itemSubscribe3.setDescription(getString(R.string.settings_paln_desc_3));
        itemSubscribeArrayList.add(itemSubscribe3);

        ItemSubscribe itemSubscribe4 = new ItemSubscribe();
        itemSubscribe4.setSubscription_type("oneyear_add");
        itemSubscribe4.setProductId("com.sirapp.oneyear_add");
        itemSubscribe4.setPlan("11.99");
        itemSubscribe4.setPlanName(getString(R.string.settings_paln_title_4));
        itemSubscribe4.setDescription(getString(R.string.settings_paln_desc_4));
        itemSubscribeArrayList.add(itemSubscribe4);

        return itemSubscribeArrayList;
    }

    ItemSubscribe itemSubscribe = null;
    public void onClickBack(ItemSubscribe itemSubscribe2) {
        Log.e(TAG, "positionAA "+itemSubscribe2);
        //languagePostion = position;
        itemSubscribe = itemSubscribe2;
        bp.purchase(this, itemSubscribe2.getProductId());
    }



    private void callAPI(String productId, String purchaseToken, String date) {
        final ProgressDialog progressDialog = new ProgressDialog(SubscribeActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.add("subscription_type", itemSubscribe.subscription_type);
        params.add("productId", productId);
        params.add("quantity", "1");
        params.add("transactionId", purchaseToken);
        params.add("originalTransactionId", purchaseToken);
        params.add("purchaseDate", date);
        params.add("originalPurchaseDate", date);
        params.add("webOrderLineItemId", "");
        params.add("subscriptionExpirationDate", "");
        params.add("cancellationDate", "");
        params.add("isTrialPeriod", "false");
        params.add("isInIntroOfferPeriod", "false");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        String token = Constant.GetSharedPreferences(SubscribeActivity.this, Constant.ACCESS_TOKEN);
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "subscription/add", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                progressDialog.dismiss();

                Log.e(TAG, "CreateInvoicedata "+ response);
                try {
                    Log.e("Create Invoicedata", response);

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("true")) {
                        Constant.SuccessToast(SubscribeActivity.this, message);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                Intent intent = new Intent();
//                                setResult(RESULT_OK , intent);
                                finish();
                            }
                        }, 500);

                    }else{
                        Constant.ErrorToast(SubscribeActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();

                if (responseBody != null) {
                    String response = new String(responseBody);
                    Log.e("responsecustomersF", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equals("1")) {
                           // Constant.ErrorToast(SubscribeActivity.this, message);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Constant.ErrorToast(PayPalActivity.this, "Something went wrong, try again!");
                }
            }
        });
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
