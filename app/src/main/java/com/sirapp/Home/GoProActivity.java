package com.sirapp.Home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseActivity;
import com.sirapp.BuildConfig;
import com.sirapp.Constant.Constant;
import com.sirapp.R;
import com.sirapp.Utils.Utility;
import com.sirapp.Xyz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class GoProActivity extends BaseActivity {

    private static final String TAG = "GoProActivity";
    LinearLayout linearLayout_12Month, linearLayout_1Month;
    Button buttonUpgrade_now;

    ImageView imageViewPay, imageViewShare;

    private BillingProcessor bp;

    String subscription_typeNet = "";

    String productID = "com.sir.oneyear";
    String subscriptionType = "oneyear";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_pro);
        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);

        linearLayout_12Month = (LinearLayout) findViewById(R.id.linear_12_month);
        linearLayout_1Month = (LinearLayout) findViewById(R.id.linear_1_month);
        buttonUpgrade_now = (Button) findViewById(R.id.button_upgrade_now);

        imageViewPay = findViewById(R.id.imagePay);
        imageViewShare = findViewById(R.id.shareButton);


        ImageView imageView = findViewById(R.id.backbtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                        .setLink(Uri.parse("https://www.sirproject.page.link/"))
                        .setLink(Uri.parse(buildDeepLink()))
                        .setDomainUriPrefix("https://sirproject.page.link/")
//                        .setDomainUriPrefix("https://sirproject.page.link/subscription")
                        .setAndroidParameters(
                                new DynamicLink.AndroidParameters.Builder("com.sirapp")
                                        .setFallbackUrl(Uri.parse("https://www.sirproject.page.link/"))
                                        .setMinimumVersion(1)
                                        .build())
                        .buildDynamicLink();

                Uri dynamicLinkUri = dynamicLink.getUri();

                Log.e(TAG, "dynamicLinkUri "+dynamicLinkUri);

                shareDynamicLink(dynamicLinkUri);
            }
        });


        linearLayout_12Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewPay.setImageResource(R.drawable.pay2);
                productID = "com.sir.oneyear";
                subscriptionType = "oneyear";
              //  bp.purchase(GoProActivity.this, "com.sir.oneyear");
               // bp.purchase(GoProActivity.this, "license-test");
//                bp.loadOwnedPurchasesFromGoogle();
              //  bp.loadOwnedPurchasesFromGoogle();
            }
        });
        linearLayout_1Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewPay.setImageResource(R.drawable.pay1);
                productID = "com.sirapp.onemonth";
                subscriptionType = "onemonth";
              //  bp.purchase(GoProActivity.this, "com.sirapp.onemonth");
            }
        });


        buttonUpgrade_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bp.purchase(GoProActivity.this, productID);

            //    bp.purchase(GoProActivity.this, "android.test.purchased");

                if(subscription_typeNet.equalsIgnoreCase("")) {
                    Log.e(TAG, "emptyAA");
                    bp.purchase(GoProActivity.this, "android.test.purchased");
                }else{
                    if(subscriptionType.equalsIgnoreCase("oneyear")) {
                          if(subscription_typeNet.equalsIgnoreCase(subscriptionType)){
                              Log.e(TAG, "emptyBB");
                              Constant.ErrorToast(GoProActivity.this, "You have already subscribed!");
                          }else{
                              Log.e(TAG, "emptyBB22");
                              bp.purchase(GoProActivity.this, "android.test.purchased");
                          }
                    }else if(subscriptionType.equalsIgnoreCase("onemonth")){
                        if(subscription_typeNet.equalsIgnoreCase(subscriptionType)){
                            Log.e(TAG, "emptyCC");
                            Constant.ErrorToast(GoProActivity.this, "You have already subscribed!");
                        }else{
                            Log.e(TAG, "emptyCC22");
                            Constant.ErrorToast(GoProActivity.this, "You have already subscribed!");
                        }
                    }
                }


            }
        });

//        Calendar myCalendar = Calendar.getInstance();
//        String myFormat = "yyyy-MM-dd";
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        String dateCurrent = sdf.format(myCalendar.getTime());


        //callAPI(productID, "orderIDXYZ", dateCurrent);


        bp = new BillingProcessor(this, AllSirApi.LICENSE_KEY, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                Log.e(TAG, "onProductPurchased"+productId);
//                showToast("onProductPurchased: " + productId);
//                updateTextViews();



                String productID = details.productId;
                String orderID = details.orderId;
                String purchaseToken = details.purchaseToken;
               // Date purchaseTime = details.purchaseTime;

               // Date date = new Date(Long.parseLong(details.purchaseTime));

//                Gson gson = new Gson();
//
//                String json = gson.toJson(details.purchaseInfo.responseData);

//                String json = gson.toJson(bp.consumePurchase(details.productId));

                Log.e(TAG , "productID"+productID);
                Log.e(TAG , "orderID"+orderID);
                Log.e(TAG , "purchaseToken"+purchaseToken);
               // Log.e(TAG , "purchaseTime"+purchaseTime);



//                DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
//                Log.e(TAG, "datemillis22 "+simple.format(purchaseTime));
//                String date = simple.format(purchaseTime);


                Calendar myCalendar = Calendar.getInstance();
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String dateCurrent = sdf.format(myCalendar.getTime());


                String json22 = new Gson().toJson(details);

                Utility.generateNoteOnSD(GoProActivity.this , "go_pro.txt", ""+json22.toString());

                callAPI(productID, orderID, dateCurrent);


//                SkuDetails dd = bp.getPurchaseListingDetails(details.productId);
//
//                String json22 = gson.toJson(dd);
//                Log.e(TAG , "json22 "+json22);

//                SkuDetails dd = bp.getSubscriptionListingDetails("");
//                TransactionDetails dd = bp.getSubscriptionTransactionDetails("");
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                Log.e(TAG, "onBillingError");
//                showToast("onBillingError: " + Integer.toString(errorCode));
                // Log.e(TAG, "onBillingError "+error.getMessage());
            }
            @Override
            public void onBillingInitialized() {
                Log.e(TAG, "onBillingInitialized");
//                showToast("onBillingInitialized");
//                readyToPurchase = true;
//                updateTextViews();
            }
            @Override
            public void onPurchaseHistoryRestored() {
                Log.e(TAG, "onPurchaseHistoryRestored");
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


    public String buildDeepLink() {
        // Get the unique appcode for this app.
        String appCode = getString(R.string.app_name);

        // Get this app's package name.
        String packageName = getPackageName();

        // Build the link with all required parameters
        Uri.Builder builder = new Uri.Builder()
                .scheme("https")
                .authority("sirproject.page.link")
                .path("/subscribtion")
                .appendQueryParameter("subscribeID", "subscribe")
//                .appendQueryParameter("apn", packageName)
                ;

        // Return the completed deep link.
        return builder.build().toString();
    }

    public void shareDynamicLink(Uri dynamicLink)
    {
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink)
                .buildShortDynamicLink()
                .addOnCompleteListener(Objects.requireNonNull(GoProActivity.this), new OnCompleteListener<ShortDynamicLink>()
                {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Short link created
                            Uri shortLink = Objects.requireNonNull(task.getResult()).getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Log.e("DynamicLink", "shortLink: " + shortLink + System.lineSeparator());
                            Log.e("DynamicLink", "flowChartLink: " + flowchartLink + System.lineSeparator());

//                            Intent shareIntent = new Intent();
//                            String msg = "Check this out: " + shortLink;
//                            shareIntent.setAction(Intent.ACTION_SEND);
//                            shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
//                            shareIntent.setType("text/plain");
//                            startActivity(shareIntent);

                            Intent intentShareFile = new Intent(Intent.ACTION_SEND_MULTIPLE);

                            File mFile2 = new File("/sdcard/share.jpg");
                            Uri imageUri2 = FileProvider.getUriForFile(
                                    GoProActivity.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    mFile2);

                            ArrayList<Uri> uriArrayList = new ArrayList<>();
                            if (mFile2.exists()) {
                                uriArrayList.add(imageUri2);
                            }

                            intentShareFile.setType("application/pdf/*|image/*");
                            intentShareFile.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);

                            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sir-app");

                            String allData = "Sir-app" + "\n\n"+shortLink;

                            intentShareFile.putExtra(Intent.EXTRA_TEXT, allData);


                            startActivity(intentShareFile);

                        }
                        else
                        {
                            Toast.makeText(GoProActivity.this, "Failed to share event.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    @Override
    protected void onResume() {
        super.onResume();
        getSubscription();
    }

    private void getSubscription() {
        final ProgressDialog progressDialog = new ProgressDialog(GoProActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();


        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        String token = Constant.GetSharedPreferences(GoProActivity.this, Constant.ACCESS_TOKEN);
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "subscription/get", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                progressDialog.dismiss();

                Log.e(TAG, "CreateInvoicedata "+ response);
                try {



//                    Log.e("Create Invoicedata", response);
//
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
//                    String message = jsonObject.getString("message");


                    if (status.equalsIgnoreCase("true")) {

                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        JSONArray jsonArraySubscription = jsonObjectData.getJSONArray("subscription");

                        if(jsonArraySubscription.length() > 0){
                            JSONObject jsonObjectSubscriptionData = jsonArraySubscription.getJSONObject(0);
                            subscription_typeNet = jsonObjectSubscriptionData.getString("subscription_type");
                            Log.e(TAG, "subscription_typeNet "+subscription_typeNet);
                        }else{
                            subscription_typeNet = "";
                        }

                    }else{
                       // Constant.ErrorToast(GoProActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                subscription_typeNet = "";
//                if (responseBody != null) {
//                    String response = new String(responseBody);
//                    Log.e("responsecustomersF", response);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        String message = jsonObject.getString("message");
//                        if (status.equals("1")) {
//                            // Constant.ErrorToast(SubscribeActivity.this, message);
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    //Constant.ErrorToast(PayPalActivity.this, "Something went wrong, try again!");
//                }
            }
        });
    }




    private void callAPI(String productId, String purchaseToken, String date) {
        final ProgressDialog progressDialog = new ProgressDialog(GoProActivity.this);
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
        String token = Constant.GetSharedPreferences(GoProActivity.this, Constant.ACCESS_TOKEN);
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
                        Constant.SuccessToast(GoProActivity.this, message);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                Intent intent = new Intent();
//                                setResult(RESULT_OK , intent);
                                finish();
                            }
                        }, 500);

                    }else{
                        Constant.ErrorToast(GoProActivity.this, message);
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
        final Dialog mybuilder = new Dialog(GoProActivity.this);
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






//    2021-07-2910: 56: 38.55226461-26461/?E/GoProActivity: productID{
//        "orderId": "transactionId.android.test.purchased",
//                "productId": "android.test.purchased",
//                "purchaseInfo": {
//            "purchaseData": {
//                "autoRenewing": false,
//                        "developerPayload": "inapp:android.test.purchased:d6907708-1f4a-4b51-b375-43e187ff11c9",
//                        "orderId": "transactionId.android.test.purchased",
//                        "packageName": "com.sirapp",
//                        "productId": "android.test.purchased",
//                        "purchaseState": "PurchasedSuccessfully",
//                        "purchaseToken": "inapp:com.sirapp:android.test.purchased"
//            },
//            "responseData": "{\"packageName\":\"com.sirapp\",\"acknowledged\":false,\"orderId\":\"transactionId.android.test.purchased\",\"productId\":\"android.test.purchased\",\"developerPayload\":\"inapp:android.test.purchased:d6907708-1f4a-4b51-b375-43e187ff11c9\",\"purchaseTime\":0,\"purchaseState\":0,\"purchaseToken\":\"inapp:com.sirapp:android.test.purchased\"}",
//                    "signature": ""
//        },
//        "purchaseToken": "inapp:com.sirapp:android.test.purchased"
//    }





//    2021-07-2911: 01: 21.48532642-32642/?E/GoProActivity: productID{
//        "currency": "INR",
//                "description": "Sample description for product: android.test.purchased.",
//                "haveIntroductoryPeriod": false,
//                "haveTrialPeriod": false,
//                "introductoryPriceCycles": 0,
//                "introductoryPriceLong": 0,
//                "introductoryPricePeriod": "",
//                "introductoryPriceText": "",
//                "introductoryPriceValue": 0.0,
//                "isSubscription": false,
//                "priceLong": 73720842,
//                "priceText": "₹73.72",
//                "priceValue": 73.720842,
//                "productId": "android.test.purchased",
//                "subscriptionFreeTrialPeriod": "",
//                "subscriptionPeriod": "",
//                "title": "Sample Title"
//    }



}
