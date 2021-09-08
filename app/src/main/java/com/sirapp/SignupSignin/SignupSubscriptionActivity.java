package com.sirapp.SignupSignin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.Constant.Constant;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Home.GoProActivity;
import com.sirapp.Home.Home_Activity;
import com.sirapp.R;
import com.sirapp.Utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class SignupSubscriptionActivity extends BaseActivity {

    private static final String TAG = "SignupSubscriptionActivity";
    LinearLayout linearLayout_12Month, linearLayout_1Month;
    Button buttonUpgrade_now, buttonSkip;

    private BillingProcessor bp;

    String productID = "com.sir.oneyear";
    String subscriptionType = "oneyear";
    ImageView imageViewPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_subscription);
        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);

        linearLayout_12Month = (LinearLayout) findViewById(R.id.linear_12_month);
        linearLayout_1Month = (LinearLayout) findViewById(R.id.linear_1_month);
        buttonUpgrade_now = (Button) findViewById(R.id.button_upgrade_now);
        buttonSkip = (Button) findViewById(R.id.button_skip);

        imageViewPay = findViewById(R.id.imagePay);

        ImageView imageView = findViewById(R.id.backbtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupSubscriptionActivity.this, Home_Activity.class);
                intent.putExtra("login","login");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        linearLayout_12Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewPay.setImageResource(R.drawable.pay2);
//                bp.purchase(SignupSubscriptionActivity.this, "com.sir.oneyear");
                productID = "com.sir.oneyear";
                subscriptionType = "oneyear";
            }
        });
        linearLayout_1Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewPay.setImageResource(R.drawable.pay1);
//                bp.purchase(SignupSubscriptionActivity.this, "com.sirapp.onemonth");
                productID = "com.sirapp.onemonth";
                subscriptionType = "onemonth";
            }
        });

        buttonUpgrade_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.purchase(SignupSubscriptionActivity.this, productID);
              // upgradePackage();
            }
        });


        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupSubscriptionActivity.this, Home_Activity.class);
                intent.putExtra("login","login");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });




        bp = new BillingProcessor(this, AllSirApi.LICENSE_KEY, new BillingProcessor.IBillingHandler() {
            @SuppressLint("LongLogTag")
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//                showToast("onProductPurchased: " + productId);
//                updateTextViews();

                String productID = details.productId;
                String orderID = details.orderId;
                String purchaseToken = details.purchaseToken;
//                Date purchaseTime = details.purchaseTime;
//                DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
//                Log.e(TAG, "datemillis22 "+simple.format(purchaseTime));
//                String date = simple.format(purchaseTime);

                Calendar myCalendar = Calendar.getInstance();
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String dateCurrent = sdf.format(myCalendar.getTime());

                String json22 = new Gson().toJson(details);

                Utility.generateNoteOnSD(SignupSubscriptionActivity.this , "signup_pro.txt", ""+json22.toString());

                callAPI(productId, orderID, dateCurrent);


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
            @SuppressLint("LongLogTag")
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
        bp.initialize();


    }





    private void callAPI(String productId, String purchaseToken, String date) {
        final ProgressDialog progressDialog = new ProgressDialog(SignupSubscriptionActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.add("subscription_type", subscriptionType);
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
        String token = Constant.GetSharedPreferences(SignupSubscriptionActivity.this, Constant.ACCESS_TOKEN);
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "subscription/add", params, new AsyncHttpResponseHandler() {
            @SuppressLint("LongLogTag")
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
                        Constant.SuccessToast(SignupSubscriptionActivity.this, message);

                        Intent intent = new Intent(SignupSubscriptionActivity.this, Home_Activity.class);
                        intent.putExtra("login","login");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }else{
                        Constant.ErrorToast(SignupSubscriptionActivity.this, message);
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




    int imageClickID = 0;
    public void upgradePackage() {
        final Dialog mybuilder = new Dialog(SignupSubscriptionActivity.this);
        mybuilder.setContentView(R.layout.upgrade_dialog_list);

        ImageView imageView = (ImageView) mybuilder.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(imageClickID == 0){
//                    imageClickID = 1;
//                    imageView.setImageResource(R.drawable.radio_uncheck);
//                }else{
                imageView.setImageResource(R.drawable.radio_check);
                imageClickID = 1;
//                }
            }
        });

        TextView txtBack = (TextView) mybuilder.findViewById(R.id.txtBack);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mybuilder.dismiss();
            }
        });

        TextView txtSave = (TextView) mybuilder.findViewById(R.id.txtSave);
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pref.setNumberFormatPosition(languagePostion);
                mybuilder.dismiss();
//                Intent intent = new Intent(mcontext, Home_Activity.class);
//                mcontext.startActivity(intent);
//                // mcontext.finishAffinity();
//                mcontext.finish();
            }
        });

//        RecyclerView recycler_invoices = mybuilder.findViewById(R.id.recycler_list);
//
//        ArrayList<String> arrayListNames = new ArrayList<>();
//        arrayListNames.add("1,000,000.00");
//        arrayListNames.add("1,00,00,000.00");
//        arrayListNames.add("1.000.000,00");
//        arrayListNames.add("1 000 000,00");
//
//
//        languagePostion = pref.getNumberFormatPosition();
//
//        NumberFormatAdapter invoicelistAdapterdt = new NumberFormatAdapter(mcontext, arrayListNames, settingsAdapter);
//        recycler_invoices.setAdapter(invoicelistAdapterdt);
//        invoicelistAdapterdt.updateLanguagePosition(languagePostion);
//        recycler_invoices.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
//        recycler_invoices.setHasFixedSize(true);
//        invoicelistAdapterdt.notifyDataSetChanged();



        mybuilder.show();
        mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = mybuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.transparent);

    }

}
