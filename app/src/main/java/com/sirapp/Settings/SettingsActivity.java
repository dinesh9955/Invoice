package com.sirapp.Settings;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.Home.GoProActivity;
import com.sirapp.Home.Home_Activity;
import com.sirapp.Invoice.SavePref;
import com.sirapp.RetrofitApi.ApiInterface;
import com.sirapp.RetrofitApi.RetrofitInstance;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Constant.Constant;
import com.sirapp.R;
import com.sirapp.SignupSignin.Signin_Activity;
import com.sirapp.SignupSignin.SignupSubscriptionActivity;
import com.sirapp.Utils.LocaleHelper;
import com.sirapp.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class SettingsActivity extends BaseActivity implements PurchasesUpdatedListener,
        BillingClientStateListener, SkuDetailsResponseListener {
    private static final String TAG = "SettingsActivity" ;
    ApiInterface apiInterface;
    RecyclerView recycler_invoices;

    int imageClickID = 0;

    String payKey = "";

    SettingsAdapter invoicelistAdapterdt;

    ArrayList<String> arrayListNames = new ArrayList<>();
    ArrayList<Integer> arrayListIcons = new ArrayList<>();

//    private BillingProcessor bp;

    BillingClient billingClient;

    String myPackage = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

//        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Constant.toolbar(SettingsActivity.this, getString(R.string.header_settings));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        recycler_invoices = findViewById(R.id.recycler_invoices);

        Button button_delete = findViewById(R.id.button_delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog mybuilder = new Dialog(SettingsActivity.this);
                mybuilder.setContentView(R.layout.dialog_delete_account);

                TextView softDelete = (TextView) mybuilder.findViewById(R.id.bydateinvoicetxt);
                TextView hardDelete = (TextView) mybuilder.findViewById(R.id.allinvoicetxt);
                TextView txtcancelvalue = (TextView) mybuilder.findViewById(R.id.txtcancelvalue);

                softDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setTitle(getString(R.string.app_name));
                        builder.setMessage("Soft delete will temporarily delete your account and all its data and can be recovered later by contacting support.")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mybuilder.dismiss();
                                        dialog.dismiss();
                                        invoicelistData("1", mybuilder);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mybuilder.dismiss();
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

                hardDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setTitle(getString(R.string.app_name));
                        builder.setMessage("Hard delete will permanently delete your account and all its data and cannot be recovered later.")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mybuilder.dismiss();
                                        dialog.dismiss();
                                        invoicelistData("2", mybuilder);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mybuilder.dismiss();
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

                txtcancelvalue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mybuilder.dismiss();
                    }
                });

                mybuilder.show();
                mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                Window window = mybuilder.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(R.color.transparent);
            }
        });

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


        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().getString("key").equalsIgnoreCase("user")) {
                payKey = "user";
                upgradePackage();
            }else if(getIntent().getExtras().getString("key").equalsIgnoreCase("company")){
                payKey = "company";
                upgradePackage();
            }else if(getIntent().getExtras().getString("key").equalsIgnoreCase("product")){
                payKey = "product";
                upgradePackage();
            }else if(getIntent().getExtras().getString("key").equalsIgnoreCase("item")){
                payKey = "item";
                upgradePackage();
            }
        }


        billingClient = BillingClient.newBuilder(SettingsActivity.this).setListener(
                this).enablePendingPurchases().build();
        billingClient.startConnection(this);

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {

                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.e(TAG, "onBillingSetupFinished");

                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.e(TAG, "onBillingServiceDisconnected");
            }
        });



    }




        private void invoicelistData(String delete_type, Dialog dialog) {
            dialog.dismiss();
            ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            RequestParams params = new RequestParams();


        String token = Constant.GetSharedPreferences(SettingsActivity.this, Constant.ACCESS_TOKEN);
            Log.e("token",token);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("delete_type", delete_type);
        params.add("language", ""+getLanguage());
            Log.e("params",params.toString());
        client.post(AllSirApi.BASE_URL + "user/deleteuser", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.dismiss();
                String response = new String(responseBody);
                Log.e(TAG, "responsecustomers "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    Log.e(TAG, "responsecustomers22 "+ response);

                    if (status.equals("true")) {
//                        JSONObject data = jsonObject.getJSONObject("data");
//                        JSONArray customer = data.getJSONArray("invoice");
                      //  Log.e(TAG, "customerAA "+customer.length());
                        Constant.SuccessToast(SettingsActivity.this, jsonObject.getString("message"));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences preferences = getSharedPreferences(Constant.PREF_BASE, MODE_PRIVATE);
                                preferences.edit().remove(Constant.LOGGED_IN).commit();
                                preferences.edit().remove(Constant.INVOICE).commit();
                                preferences.edit().remove(Constant.ESTIMATE).commit();
                                preferences.edit().remove(Constant.STOCK).commit();
                                preferences.edit().remove(Constant.RECEIPT).commit();
                                preferences.edit().remove(Constant.PURCHASE_ORDER).commit();
                                preferences.edit().remove(Constant.PAYMENT_VOUCHER).commit();
                                preferences.edit().remove(Constant.TAX).commit();
                                preferences.edit().remove(Constant.CUSTOMER).commit();
                                preferences.edit().remove(Constant.SUPPLIER).commit();
                                preferences.edit().remove(Constant.PRODUCT).commit();
                                preferences.edit().remove(Constant.SERVICE).commit();
                                preferences.edit().remove(Constant.DEBIT_NOTE).commit();
                                preferences.edit().remove(Constant.CREDIT_NOTE).commit();
                                preferences.edit().remove(Constant.SUB_ADMIN).commit();
                                preferences.edit().remove(Constant.ACCESS_TOKEN).commit();
                                preferences.edit().remove(Constant.FULLNAME).commit();
                                preferences.edit().remove(Constant.EMAIL).commit();
                                SavePref pref = new SavePref();
                                pref.SavePref(SettingsActivity.this);
                                pref.setSubsType("");
                                pref.setNumberFormatPosition(0);
                                pref.setLanguagePosition(0);

                                Context context = LocaleHelper.setLocale(SettingsActivity.this, "en");
                                Locale myLocale = new Locale("en");
                                Resources res = context.getResources();
                                DisplayMetrics dm = res.getDisplayMetrics();
                                Configuration conf = res.getConfiguration();
                                conf.locale = myLocale;
                                res.updateConfiguration(conf, dm);

                                Intent intent = new Intent(SettingsActivity.this, Signin_Activity.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finishAffinity();
                                finish();
                            }
                        }, 500);


                    }else{
                        Constant.ErrorToast(SettingsActivity.this, jsonObject.getString("message"));
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
                        if (status.equals("false")) {
                             Constant.ErrorToast(SettingsActivity.this, jsonObject.getJSONObject("message").getString("access_token"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // Constant.ErrorToast(getActivity(), "Something went wrong, try again!");
                }

            }
        });
    }






    public void restorePurchase() {
        Log.e(TAG, "restorePurchase ");


      //  bp.loadOwnedPurchasesFromGoogle();
       // callRestore();

        billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, new PurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                Log.e(TAG, "emptyAA "+ list.size());
                if(list.size() > 0){
                    for(int i = 0; i < list.size() ; i++){
                        Purchase purchase = list.get(i);

                        String orderID = purchase.getOrderId();

                         Calendar myCalendar = Calendar.getInstance();
                        String myFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        String dateCurrent = sdf.format(myCalendar.getTime());

                        if(purchase.getOrderId().equalsIgnoreCase("com.sir.oneyear")){
                            callAPIRestore("com.sir.oneyear", orderID, dateCurrent, "1");
                        }else if(purchase.getOrderId().equalsIgnoreCase("com.sirapp.onemonth")){
                            callAPIRestore("com.sirapp.onemonth", orderID, dateCurrent, "1");
                        }else if(purchase.getOrderId().equalsIgnoreCase("com.sir.monthadditional")){
                            callAPIRestore("com.sir.monthadditional", orderID, dateCurrent, "1");
                        }else if(purchase.getOrderId().equalsIgnoreCase("com.sirapp.oneyearadditions")){
                            callAPIRestore("com.sirapp.oneyearadditions", orderID, dateCurrent, "1");
                        }else if(purchase.getOrderId().equalsIgnoreCase("com.sir.one_user")){
                            callAPIUSERRestore("SUBUSER");
                        }else if(purchase.getOrderId().equalsIgnoreCase("com.sir.one_company")){
                            callAPIUSERRestore("COMPANY");
                        }

                    }
                }

            }
        });
    }


    public void upgradePackage() {
        final Dialog mybuilder = new Dialog(SettingsActivity.this);
        mybuilder.setContentView(R.layout.upgrade_dialog_list);

        ImageView imageView = (ImageView) mybuilder.findViewById(R.id.imageView);
        ImageView imageView2 = (ImageView) mybuilder.findViewById(R.id.imageView2);
        ImageView imageView3 = (ImageView) mybuilder.findViewById(R.id.imageView3);
        ImageView imageView4 = (ImageView) mybuilder.findViewById(R.id.imageView4);

        RelativeLayout relativeLayoutUser = mybuilder.findViewById(R.id.relaive111);
        RelativeLayout relativeLayoutCompany = mybuilder.findViewById(R.id.relaive222);

        RelativeLayout relativeLayoutMonthly = mybuilder.findViewById(R.id.relaive333);
        RelativeLayout relativeLayoutYearly = mybuilder.findViewById(R.id.relaive444);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    imageView.setImageResource(R.drawable.radio_check);
                    imageView2.setImageResource(R.drawable.radio_uncheck);
                    imageView3.setImageResource(R.drawable.radio_uncheck);
                    imageView4.setImageResource(R.drawable.radio_uncheck);
                    imageClickID = 1;
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.radio_uncheck);
                imageView2.setImageResource(R.drawable.radio_check);
                imageView3.setImageResource(R.drawable.radio_uncheck);
                imageView4.setImageResource(R.drawable.radio_uncheck);
                imageClickID = 2;
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.radio_uncheck);
                imageView2.setImageResource(R.drawable.radio_uncheck);
                imageView3.setImageResource(R.drawable.radio_check);
                imageView4.setImageResource(R.drawable.radio_uncheck);
                imageClickID = 3;
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.radio_uncheck);
                imageView2.setImageResource(R.drawable.radio_uncheck);
                imageView3.setImageResource(R.drawable.radio_uncheck);
                imageView4.setImageResource(R.drawable.radio_check);
                imageClickID = 4;
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
                if(imageClickID == 1){
                    myPackage = "com.sir.one_user";
//                    bp.purchase(SettingsActivity.this, "com.sir.one_user");
                    callOne();
                }else if(imageClickID == 2){
                    myPackage = "com.sir.one_company";
//                    bp.purchase(SettingsActivity.this, "com.sir.one_company");
                    callTwo();
                }else if(imageClickID == 3){
                    myPackage = "com.sir.monthadditional";
//                    bp.purchase(SettingsActivity.this, "com.sir.monthadditional");
                    callThree();
                }else if(imageClickID == 4){
                    myPackage = "com.sir.oneyearadditions";
//                    bp.purchase(SettingsActivity.this, "com.sir.oneyearadditions");
                    callFour();
                }else{
                    Constant.ErrorToast(SettingsActivity.this , getString(R.string.setting_SelectPlan));
                }
               //bp.purchase(SettingsActivity.this, "android.test.purchased");



                mybuilder.dismiss();
            }
        });


        if(payKey.equalsIgnoreCase("user") || payKey.equalsIgnoreCase("company")){
            relativeLayoutUser.setVisibility(View.VISIBLE);
            relativeLayoutCompany.setVisibility(View.VISIBLE);
            relativeLayoutMonthly.setVisibility(View.GONE);
            relativeLayoutYearly.setVisibility(View.GONE);
        }else if(payKey.equalsIgnoreCase("product") || payKey.equalsIgnoreCase("item")){
            relativeLayoutUser.setVisibility(View.GONE);
            relativeLayoutCompany.setVisibility(View.GONE);
            if(pref.getSubsType().equalsIgnoreCase("onemonth")){
                relativeLayoutMonthly.setVisibility(View.VISIBLE);
                relativeLayoutYearly.setVisibility(View.GONE);
            }
            if(pref.getSubsType().equalsIgnoreCase("oneyear")){
                relativeLayoutMonthly.setVisibility(View.GONE);
                relativeLayoutYearly.setVisibility(View.VISIBLE);
            }
        }else{
            relativeLayoutUser.setVisibility(View.VISIBLE);
            relativeLayoutCompany.setVisibility(View.VISIBLE);
            relativeLayoutMonthly.setVisibility(View.GONE);
            relativeLayoutYearly.setVisibility(View.GONE);
        }


        mybuilder.show();
        mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = mybuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.transparent);

    }

    private void callOne() {
        List<String> skuList = new ArrayList<>();
        skuList.add(myPackage);

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        String ddd = new Gson().toJson(skuDetailsList);
                        Log.e(TAG, "onSkuDetailsResponse "+ddd);
                        if(skuDetailsList != null){
                            for(int i = 0 ; i < skuDetailsList.size() ; i++){
                                if(skuDetailsList.get(i).getSku().equals(myPackage)){
                                    Log.e(TAG, "skuDetailsList "+skuDetailsList.get(i).getSku());
                                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(i))
                                            .build();
                                    int responseCode = billingClient.launchBillingFlow(SettingsActivity.this, billingFlowParams).getResponseCode();
                                }
                            }
                        }

                    }
                });
    }


    private void callTwo() {
        List<String> skuList = new ArrayList<>();
        skuList.add(myPackage);

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        String ddd = new Gson().toJson(skuDetailsList);
                        Log.e(TAG, "onSkuDetailsResponse "+ddd);
                        if(skuDetailsList != null){
                            for(int i = 0 ; i < skuDetailsList.size() ; i++){
                                if(skuDetailsList.get(i).getSku().equals(myPackage)){
                                    Log.e(TAG, "skuDetailsList "+skuDetailsList.get(i).getSku());
                                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(i))
                                            .build();
                                    int responseCode = billingClient.launchBillingFlow(SettingsActivity.this, billingFlowParams).getResponseCode();
                                }
                            }
                        }

                    }
                });
    }


    private void callThree() {
        List<String> skuList = new ArrayList<>();
        skuList.add(myPackage);

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        String ddd = new Gson().toJson(skuDetailsList);
                        Log.e(TAG, "onSkuDetailsResponse "+ddd);
                        if(skuDetailsList != null){
                            for(int i = 0 ; i < skuDetailsList.size() ; i++){
                                if(skuDetailsList.get(i).getSku().equals(myPackage)){
                                    Log.e(TAG, "skuDetailsList "+skuDetailsList.get(i).getSku());
                                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(i))
                                            .build();
                                    int responseCode = billingClient.launchBillingFlow(SettingsActivity.this, billingFlowParams).getResponseCode();
                                }
                            }
                        }

                    }
                });
    }


    private void callFour() {
        List<String> skuList = new ArrayList<>();
        skuList.add(myPackage);

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        String ddd = new Gson().toJson(skuDetailsList);
                        Log.e(TAG, "onSkuDetailsResponse "+ddd);
                        if(skuDetailsList != null){
                            for(int i = 0 ; i < skuDetailsList.size() ; i++){
                                if(skuDetailsList.get(i).getSku().equals(myPackage)){
                                    Log.e(TAG, "skuDetailsList "+skuDetailsList.get(i).getSku());
                                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(i))
                                            .build();
                                    int responseCode = billingClient.launchBillingFlow(SettingsActivity.this, billingFlowParams).getResponseCode();
                                }
                            }
                        }

                    }
                });
    }


    private void callRestore() {
        List<String> skuList = new ArrayList<>();
        skuList.add("com.sir.oneyear");
        skuList.add("com.sirapp.onemonth");
        skuList.add("com.sir.monthadditional");
        skuList.add("com.sirapp.oneyearadditions");

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        String ddd = new Gson().toJson(skuDetailsList);
                        Log.e(TAG, "onSkuDetailsResponse "+ddd);
                        if(skuDetailsList != null){
                            for(int i = 0 ; i < skuDetailsList.size() ; i++){
                                if(skuDetailsList.get(i).getSku().equals(myPackage)){
                                    Log.e(TAG, "skuDetailsList "+skuDetailsList.get(i).getSku());
                                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(i))
                                            .build();
                                    int responseCode = billingClient.launchBillingFlow(SettingsActivity.this, billingFlowParams).getResponseCode();
                                }
                            }
                        }

                    }
                });
    }


    private void callAPI(String productId, String purchaseToken, String date, String witch) {
        final ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();


        if(imageClickID == 3){
            params.add("subscription_type", "onemonth_add");
        } else if(imageClickID == 4){
            params.add("subscription_type", "oneyear_add");
        } else {
            params.add("subscription_type", "restore");
        }


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
        String token = Constant.GetSharedPreferences(SettingsActivity.this, Constant.ACCESS_TOKEN);
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
                        if(witch.equalsIgnoreCase("1")){
                            Constant.SuccessToast(SettingsActivity.this, message);
//                            pref.setSubsType("");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(payKey.equalsIgnoreCase("product")){
                                        pref.setProduct(payKey);
                                    }
                                    if(payKey.equalsIgnoreCase("item")){
                                        pref.setItem(payKey);
                                    }

                                    Intent intent = new Intent();
                                    setResult(RESULT_OK , intent);
                                    finish();
                                }
                            }, 1000);
                        }else if(witch.equalsIgnoreCase("2")){
                            Constant.SuccessToast(SettingsActivity.this, getString(R.string.dialog_Restored_successfully));
                        }





                    }else{
                        Constant.ErrorToast(SettingsActivity.this, message);
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




    private void callAPIUSER(String witch) {
        final ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();

        params.add("key", ""+witch);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        String token = Constant.GetSharedPreferences(SettingsActivity.this, Constant.ACCESS_TOKEN);
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "user/addtionalusercompany", params, new AsyncHttpResponseHandler() {
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

                        Constant.SuccessToast(SettingsActivity.this, message.replace("allow", "allowed"));


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(payKey.equalsIgnoreCase("user")){
                                    pref.setUser(payKey);
                                }
                                if(payKey.equalsIgnoreCase("item")){
                                    pref.setCompany(payKey);
                                }

                                Intent intent = new Intent();
                                setResult(RESULT_OK , intent);
                                finish();
                            }
                        }, 1000);

                    }else{
                        Constant.ErrorToast(SettingsActivity.this, message);
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
                       // if (status.equals("false")) {
                             Constant.ErrorToast(SettingsActivity.this, message);
                       // }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Constant.ErrorToast(PayPalActivity.this, "Something went wrong, try again!");
                }
            }
        });
    }





//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        if (!bp.handleActivityResult(requestCode, resultCode, data))
////            super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onBillingServiceDisconnected() {

    }

    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        switch (billingResult.getResponseCode()) {
            case BillingClient.BillingResponseCode.OK:
                if (null != list) {
                    Log.d(TAG, "PURCX");
                    String ddd = new Gson().toJson(list);
                    Log.e(TAG, "onSkuDetailsResponseXX "+ddd);

                    Purchase purchase = list.get(0);

                //    String productID = details.productId;
                    String orderID = purchase.getOrderId();
                    String purchaseToken = purchase.getPurchaseToken();


                    Calendar myCalendar = Calendar.getInstance();
                    String myFormat = "yyyy-MM-dd";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    String dateCurrent = sdf.format(myCalendar.getTime());


                    if(payKey.equalsIgnoreCase("user")){
                        callAPIUSER("SUBUSER");
                    }else if(payKey.equalsIgnoreCase("company")){
                        callAPIUSER("COMPANY");
                    }else{
                        callAPI(myPackage, orderID, dateCurrent, "1");
                    }


                    return;
                } else {
                    Log.d(TAG, "Null Purchase List Returned from OK response!");
                }
                break;
            case BillingClient.BillingResponseCode.USER_CANCELED:
                Log.i(TAG, "onPurchasesUpdated: User canceled the purchase");
                break;
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                Log.i(TAG, "onPurchasesUpdated: The user already owns this item");
                break;
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                Log.e(TAG, "onPurchasesUpdated: Developer error means that Google Play " +
                        "does not recognize the configuration. If you are just getting started, " +
                        "make sure you have configured the application correctly in the " +
                        "Google Play Console. The SKU product ID must match and the APK you " +
                        "are using must be signed with release keys."
                );
                break;
            default:
                Log.d(TAG, "BillingResult [" + billingResult.getResponseCode() + "]: "
                        + billingResult.getDebugMessage());
        }
    }

    @Override
    public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
//        boolean controlnumber = bp.loadOwnedPurchasesFromGoogle();
//        if(controlnumber) {
//            for(String sku : bp.listOwnedSubscriptions()){
//                TransactionDetails details = bp.getSubscriptionTransactionDetails(sku);
//
//                if (details != null) {
//                    Log.d("TAG", "onBillingInitialized: active");
//                    String productID = details.productId;
//                    String orderID = details.orderId;
//                    String purchaseToken = details.purchaseToken;
//
//                    Calendar myCalendar = Calendar.getInstance();
//                    String myFormat = "yyyy-MM-dd";
//                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//                    String dateCurrent = sdf.format(myCalendar.getTime());
//
//                    callAPI(productID, orderID, dateCurrent, "2");
//                }
//
//            }
//
//        }
    }




    private void callAPIRestore(String productId, String purchaseToken, String date, String witch) {
        final ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();


//        if(imageClickID == 3){
//            params.add("subscription_type", "onemonth_add");
//        } else if(imageClickID == 4){
//            params.add("subscription_type", "oneyear_add");
//        } else {
//            params.add("subscription_type", "restore");
//        }

        params.add("subscription_type", "restore");
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
        String token = Constant.GetSharedPreferences(SettingsActivity.this, Constant.ACCESS_TOKEN);
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
                        if(witch.equalsIgnoreCase("1")){
                            Constant.SuccessToast(SettingsActivity.this, message);
//                            pref.setSubsType("");

//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if(payKey.equalsIgnoreCase("product")){
//                                        pref.setProduct(payKey);
//                                    }
//                                    if(payKey.equalsIgnoreCase("item")){
//                                        pref.setItem(payKey);
//                                    }
//
//                                    Intent intent = new Intent();
//                                    setResult(RESULT_OK , intent);
//                                    finish();
//                                }
//                            }, 1000);
                        }else if(witch.equalsIgnoreCase("2")){
                            Constant.SuccessToast(SettingsActivity.this, getString(R.string.dialog_Restored_successfully));
                        }





                    }else{
                        Constant.ErrorToast(SettingsActivity.this, message);
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


    private void callAPIUSERRestore(String witch) {
        final ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();

        params.add("key", ""+witch);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        String token = Constant.GetSharedPreferences(SettingsActivity.this, Constant.ACCESS_TOKEN);
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "user/addtionalusercompany", params, new AsyncHttpResponseHandler() {
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

                        Constant.SuccessToast(SettingsActivity.this, message.replace("allow", "allowed"));


//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(payKey.equalsIgnoreCase("user")){
//                                    pref.setUser(payKey);
//                                }
//                                if(payKey.equalsIgnoreCase("item")){
//                                    pref.setCompany(payKey);
//                                }
//
//                                Intent intent = new Intent();
//                                setResult(RESULT_OK , intent);
//                                finish();
//                            }
//                        }, 1000);

                    }else{
                        Constant.ErrorToast(SettingsActivity.this, message);
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
                        // if (status.equals("false")) {
                        Constant.ErrorToast(SettingsActivity.this, message);
                        // }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Constant.ErrorToast(PayPalActivity.this, "Something went wrong, try again!");
                }
            }
        });
    }
}
