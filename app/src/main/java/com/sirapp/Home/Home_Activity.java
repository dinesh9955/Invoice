package com.sirapp.Home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplay;
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks;
import com.google.firebase.inappmessaging.model.InAppMessage;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.Adapter.Invoice_OverDue_Adapter;
import com.sirapp.Company.Companies_Activity;
import com.sirapp.Constant.Constant;
import com.sirapp.Customer.Customer_Activity;
import com.sirapp.Details.Company_Details_Activity;
import com.sirapp.FCM.MyMessageDisplayImplementation;
import com.sirapp.Home.Model.CompanyModel;
import com.sirapp.Home.Model.CustomerModel;
import com.sirapp.Home.Model.InvoiceModel;
import com.sirapp.Home.Model.SupplierModel;
import com.sirapp.Home.adapter.HomeCustomerAdapter;
import com.sirapp.Home.adapter.HomeInvoiceAdapter;
import com.sirapp.Home.adapter.HomeSupplierAdapter;
import com.sirapp.Invoice.InvoiceActivity;
import com.sirapp.Product.Product_Activity;
import com.sirapp.Service.Service_Activity;
import com.sirapp.Settings.OnlinePaymentGatewayActivity;
import com.sirapp.SignupSignin.Signin_Activity;
import com.sirapp.Splash_Activity;
import com.sirapp.Utils.Utility;
import com.sirapp.Vendor.Vendor_Activity;
import com.sirapp.Base.BaseActivity;
import com.sirapp.R;
import com.sirapp.API.AllSirApi;
import com.sirapp.API.GetAsyncPost;
import com.sirapp.API.Parameters;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sirapp.Constant.Constant.createDialogOpenClass;
import static project.aamir.sheikh.circletextview.Model.getContext;

public class Home_Activity extends BaseActivity implements MenuDelegate{

    String TAG = "Home_Activity";

    RecyclerView businessactivitiesRV, invoiceoverdueRV, recycleCustomers,recyclerVinder;
   // Invoice_OverDue_Adapter invoice_overDue_adapter;
//    HomeCustomerAdapter homeCustomerAdapter;
//    HomeSupplierAdapter homeSupplierAdapter;
//    HomeInvoiceAdapter homeInvoiceAdapter;

    ImageView menu;
    TextView hello, description, customerstxt, seeall, vendorstxt, vendorsseeall;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    ProgressDialog dialog;

    TextView no_customer;
    TextView txtNo, no_overduetxt;
    TextView txtinvoice;
    String compantId;

    public ArrayList<CompanyModel> companyModelArrayList = new ArrayList<>();

    public ArrayList<InvoiceModel> invoiceModelArrayList = new ArrayList<>();
    public ArrayList<InvoiceModel> invoiceDueDateModelArrayList = new ArrayList<>();

    public ArrayList<CustomerModel> customerModelArrayList = new ArrayList<>();
    public ArrayList<SupplierModel> supplierModelArrayList = new ArrayList<>();

    TextView textViewGoPro;

    RelativeLayout home_AddyourBusiness, home_AddyourCustomer, home_AddyourProducts, home_AddyourWarehouse,
            home_AddyourItems, home_SetupPayment, home_CreateyourInvoice;

    ImageView imageView_AddyourBusiness, imageView_AddyourCustomer, imageView_AddyourProducts, imageView_AddyourWarehouse,
            imageView_AddyourItems, imageView_SetupPayment, imageView_CreateyourInvoice;


    int companyPosition = 0;



    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);
//        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
//        File myDirectory = new File(getExternalFilesDir("FolderName"),"YOUR_DIR");
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "SIR");
        if(!myDirectory.exists()) {
            myDirectory.mkdirs();
        }

        File logoPath = Utility.getFileLogo2(Home_Activity.this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView titleView = toolbar.findViewById(R.id.title1);
        titleView.setText("");
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);
        ImageView ham = toolbar.findViewById(R.id.ham);
        backbtn.setVisibility(View.GONE);
        ham.setVisibility(View.VISIBLE);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();

        FindByIds();

//        Utility.deleteDirectory();

//        File mFile = new File(Environment.getExternalStorageDirectory() + "/Notes");
//        try {
//            deleteFolder(mFile);
//        } catch (IOException e) {
//           // Toast.makeText(Home_Activity.this, "Unable to delete folder", Toast.LENGTH_SHORT).show();
//        }



        SharedPreferences preferences = getSharedPreferences(Constant.PREF_BASE, MODE_PRIVATE);
        boolean LOGGED_IN  = preferences.getBoolean(Constant.LOGGED_IN, false);
        if (!LOGGED_IN) {
            Intent i = new Intent(Home_Activity.this, Signin_Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return;
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "LEFTAAbackbtn "+drawerLayout.getDrawerElevation());
                onBackPressed();
            }
        });

        ham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Log.e(TAG, "LEFTAA "+drawerLayout.getDrawerElevation());
//                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
//                }

            }
        });

        String ddd = Locale.getDefault().getLanguage();
        Log.e(TAG, "dddXX "+ddd);


        setLeftValues();

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(Home_Activity.this, new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                if (pendingDynamicLinkData != null) {
                                    if (pendingDynamicLinkData.getLink().toString().contains("subscribeID")) {
                                        String subscribeID = pendingDynamicLinkData.getLink().getQueryParameter("subscribeID");
                                        if (subscribeID.equalsIgnoreCase("subscribe")) {
                                            Intent intent = new Intent(Home_Activity.this, GoProActivity.class);
                                            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }

                                    if (pendingDynamicLinkData.getLink().toString().contains("invoiceID")) {
                                        String invoiceID = pendingDynamicLinkData.getLink().getQueryParameter("invoiceID");
                                        if (invoiceID.equalsIgnoreCase("invoice")) {
                                            Intent intent = new Intent(Home_Activity.this, InvoiceActivity.class);
                                            intent.putExtra("key" , "home");
                                            startActivity(intent);
                                        }
                                    }

                                }
                            }
                        }
                )
                .addOnFailureListener(Home_Activity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG , "onFailure "+e.getMessage());
                    }
                });

        if (getIntent().hasExtra("login")) {
            //messagebar();
        }

        Constant.bottomNav(Home_Activity.this, -1);
//        Constant.toolbar(Home_Activity.this, "");

        setListeners();
//        setFonts();



        ImageView imageViewptint = toolbar.findViewById(R.id.imageViewptint);
        imageViewptint.setImageResource(R.drawable.search_2);
        imageViewptint.setVisibility(View.VISIBLE);
        imageViewptint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecyclerView mRecyclerView;
                MenuAdapter mAdapter;

                final Dialog mybuilder = new Dialog(Home_Activity.this);
                mybuilder.setContentView(R.layout.dialog_list);

                TextView txtcancelvalue = (TextView) mybuilder.findViewById(R.id.txtcancelvalue);
                txtcancelvalue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mybuilder.dismiss();
                    }
                });
                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(Home_Activity.this, LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter(companyModelArrayList, mybuilder);
                mRecyclerView.setAdapter(mAdapter);

                if(companyModelArrayList.size() > 0){
                    mybuilder.show();
                    mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    Window window = mybuilder.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawableResource(R.color.transparent);
                }

            }
        });

        String username = Constant.GetSharedPreferences(getApplicationContext(), Constant.FULLNAME);

        if (username.equals("")) {
            hello.setText(getString(R.string.home_hello));
        } else {
            hello.setText(getString(R.string.home_hello)+" " + username);
        }


        Locale current = getResources().getConfiguration().locale;
        Log.e(TAG,  "currentAAAA "+current.getLanguage());


        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.PARAM_1, "dashboard_view");
        AppsFlyerLib.getInstance().trackEvent(Home_Activity.this, "dashboard_view", eventValue);

        Bundle params2 = new Bundle();
        params2.putString("event_name", "Home");
        firebaseAnalytics.logEvent("dashboard_view", params2);


        String size = Utility.getDensityName(Home_Activity.this);
        Log.e(TAG, "sizeSc "+size);

        Log.e(TAG, "isTablet "+Utility.isTablet(Home_Activity.this));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        Log.e(TAG, "width "+width);
        Log.e(TAG, "height "+height);

        final float scale = getResources().getDisplayMetrics().density;

        Log.e(TAG, "scale "+scale);
        float GESTURE_THRESHOLD_DP = 3.0f;
       int mGestureThreshold = (int) (GESTURE_THRESHOLD_DP * scale + 0.5f);
        Log.e(TAG, "mGestureThreshold "+mGestureThreshold);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width1=dm.widthPixels;
        int height1=dm.heightPixels;
        double wi=(double)width1/(double)dm.xdpi;
        double hi=(double)height1/(double)dm.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);
        //double screenInches = 5.1;
        Log.e(TAG, "screenInches "+screenInches);


        if(screenInches > 4.9 && screenInches < 5.5){
            Log.e(TAG, "screenInches1 "+screenInches);
        }else{
            Log.e(TAG, "screenInches2 "+screenInches);
        }


        final PackageManager pm = getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            String ccc = new Gson().toJson(packageInfo);
            Log.e(TAG, "Source_dir : " + ccc);
//            Log.e(TAG, "Installed package :" + packageInfo.packageName);
//            Log.e(TAG, "Source dir : " + packageInfo.sourceDir);
        //    Log.e(TAG, "Source_dir : " + pm.getLaunchIntentForPackage(packageInfo.packageName).toString());

            //Log.e(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
            if(ccc.toString().toLowerCase().contains("mail")){
                Log.e(TAG, "LaunchActivity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
                Log.e(TAG, "Installedpackage :" + packageInfo.packageName);
                return;
            }
        }



//        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//
//        DatabaseReference mDatabaseReference = mDatabase.getReference();
//        mDatabaseReference = mDatabase.getReference().child("image_upload_error111");
//        mDatabaseReference.setValue("event_name_image_error11");
    }

    private void setLeftValues() {

        String username = Constant.GetSharedPreferences(Home_Activity.this, Constant.FULLNAME);
        String email = Constant.GetSharedPreferences(Home_Activity.this, Constant.EMAIL);

        TextView headerName = (TextView) findViewById(R.id.headerName);
        TextView headerEmail = (TextView) findViewById(R.id.headerEmail);
        TextView headerDesc = (TextView) findViewById(R.id.headerDesc);

        headerName.setText(""+username);
        headerEmail.setText(""+email);
        if(pref.getSubsType().equalsIgnoreCase("onemonth")){
            headerDesc.setText("Monthly Subscription");
        }else if(pref.getSubsType().equalsIgnoreCase("oneyear")){
            headerDesc.setText("Yearly Subscription");
        }else{
            headerDesc.setText("");
        }

        ArrayList<ItemLeftMenu> contacts = new ArrayList<ItemLeftMenu>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Home_Activity.this, LinearLayoutManager.VERTICAL, false));
//        recyclerView.setNestedScrollingEnabled(false);
        LeftMenuAdapter mAdapter = new LeftMenuAdapter(Home_Activity.this, contacts);
        recyclerView.setAdapter(mAdapter);

        contacts = new Utility().getItemLeftMenuWithoutLoginUser(Home_Activity.this);
        mAdapter.updateData(contacts);


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "company_id"+compantId);
//        if(compantId != null){
//            HomeApi(compantId);
//        }

        COMPANYListingApi();
        getSubscriptionApi();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       // HomeApi(compantId);
    }

    private void FindByIds() {

        textViewGoPro = findViewById(R.id.go_pro);

        home_AddyourBusiness = findViewById(R.id.home_AddyourBusiness);
        home_AddyourCustomer = findViewById(R.id.home_AddyourCustomer);
        home_AddyourProducts = findViewById(R.id.home_AddyourProducts);
        home_AddyourWarehouse = findViewById(R.id.home_AddyourWarehouse);
        home_AddyourItems = findViewById(R.id.home_AddyourItems);
        home_SetupPayment = findViewById(R.id.home_SetupPayment);
        home_CreateyourInvoice = findViewById(R.id.home_CreateyourInvoice);

        imageView_AddyourBusiness = findViewById(R.id.imageView);
        imageView_AddyourCustomer = findViewById(R.id.imageView2);
        imageView_AddyourProducts = findViewById(R.id.imageView4);
        imageView_AddyourWarehouse = findViewById(R.id.imageView5);
        imageView_AddyourItems = findViewById(R.id.imageView7);
        imageView_SetupPayment = findViewById(R.id.imageView8);
        imageView_CreateyourInvoice = findViewById(R.id.imageView9);





        businessactivitiesRV = findViewById(R.id.businessactivitiesRV);
        invoiceoverdueRV = findViewById(R.id.invoiceoverdueRV);
        recycleCustomers = findViewById(R.id.recycleCustomers);
        recyclerVinder = findViewById(R.id.recyclerVinder);
        no_customer = findViewById(R.id.no_customer);
        txtNo = findViewById(R.id.txtNo);
        no_overduetxt = findViewById(R.id.no_overduetxt);

        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);

        menu = findViewById(R.id.menu);
        hello = findViewById(R.id.hello);
        description = findViewById(R.id.description);
        customerstxt = findViewById(R.id.customerstxt);
        seeall = findViewById(R.id.seeall);
        vendorstxt = findViewById(R.id.vendorstxt);
        vendorsseeall = findViewById(R.id.vendorsseeall);
        txtinvoice = findViewById(R.id.txtinvoice);




    }

    private void setListeners() {

        textViewGoPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, GoProActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        SharedPreferences preferences = getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);

        home_AddyourBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getString(Constant.ROLE,"").equalsIgnoreCase("USER")){
                    Intent intent = new Intent(Home_Activity.this, Companies_Activity.class);
                    intent.putExtra("key" , "home");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
                        Intent intent = new Intent(Home_Activity.this, Companies_Activity.class);
                        intent.putExtra("key" , "home");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        createDialogOpenClass(Home_Activity.this);
                    }
                }


            }
        });

        home_AddyourCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getString(Constant.ROLE,"").equalsIgnoreCase("USER")){
                    Intent intent = new Intent(Home_Activity.this, Customer_Activity.class);
                    intent.putExtra("key" , "home");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
                        Intent intent = new Intent(Home_Activity.this, Customer_Activity.class);
                        intent.putExtra("key" , "home");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        if(preferences.getString(Constant.CUSTOMER,"").equalsIgnoreCase("1")){
                            Intent intent = new Intent(Home_Activity.this, Customer_Activity.class);
                            intent.putExtra("key" , "home");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else{
                            createDialogOpenClass(Home_Activity.this);
                        }
                    }

                }

            }
        });

        home_AddyourProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getString(Constant.ROLE,"").equalsIgnoreCase("USER")){
                    Intent intent = new Intent(Home_Activity.this, Product_Activity.class);
                    intent.putExtra("key" , "home");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
                        Intent intent = new Intent(Home_Activity.this, Product_Activity.class);
                        intent.putExtra("key" , "home");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        if(preferences.getString(Constant.PRODUCT,"").equalsIgnoreCase("1")){
                            Intent intent = new Intent(Home_Activity.this, Product_Activity.class);
                            intent.putExtra("key" , "home");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else{
                            createDialogOpenClass(Home_Activity.this);
                        }
                    }


                }


            }
        });

        home_AddyourWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getString(Constant.ROLE,"").equalsIgnoreCase("USER")){
                    Intent intent = new Intent(Home_Activity.this, Company_Details_Activity.class);
                    intent.putExtra("key" , "home");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                    final SharedPreferences pref = getSharedPreferences(Constant.PREF_BASE, Context.MODE_PRIVATE);
                    if(companyModelArrayList.size() > 0){
                        CompanyModel companyModel = companyModelArrayList.get(companyPosition);

                        final String company_id = companyModel.getCompany_id();
                        final String company_email = companyModel.getEmail();
                        final String company_name = companyModel.getName();
                        final String company_phone = companyModel.getPhone_number();
                        final String company_website = companyModel.getWebsite();
                        final String currencyid = companyModel.getCurrency_id();
                        final String address = companyModel.getAddress();
                        final String bankname = companyModel.getPayment_bank_name();
                        final String paypalemail = companyModel.getPaypal_email();
                        final String payment_swiftbic = companyModel.getPayment_swift_bic();
                        final String cheque_payableto = companyModel.getCheque_payable_to();
                        final String ibnnumber = companyModel.getPayment_iban();
                        final String color = companyModel.getColor();
                        final String logo = companyModel.getLogo();


                        pref.edit().putString(Constant.COMPANY_NAME,company_name).commit();
                        pref.edit().putString(Constant.COMPANY_LOGO,logo).commit();
                        pref.edit().putString(Constant.COMPANY_EMAIL,company_email).commit();
                        pref.edit().putString(Constant.COMPANY_PHONE,company_phone).commit();
                        pref.edit().putString(Constant.COMPANY_WEB,company_website).commit();
                        pref.edit().putString(Constant.COMPANY_ID,company_id).commit();
                        pref.edit().putString(Constant.COMPANY_ADDRESS,address).commit();
                        pref.edit().putString(Constant.CURRENCY_ID,currencyid).commit();
                        pref.edit().putString(Constant.Payment_bank_name,bankname).commit();
                        pref.edit().putString(Constant.Paypal_email,paypalemail).commit();
                        pref.edit().putString(Constant.Payment_swift_bic,payment_swiftbic).commit();
                        pref.edit().putString(Constant.Cheque_payable_to,cheque_payableto).commit();
                        pref.edit().putString(Constant.Ibn_number,ibnnumber).commit();
                        pref.edit().putString("color",color).commit();
                        startActivity(intent);
                    }

                }else{
                    if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
                        Intent intent = new Intent(Home_Activity.this, Company_Details_Activity.class);
                        intent.putExtra("key" , "home");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                        final SharedPreferences pref = getSharedPreferences(Constant.PREF_BASE, Context.MODE_PRIVATE);
                        if(companyModelArrayList.size() > 0){
                            CompanyModel companyModel = companyModelArrayList.get(companyPosition);

                            final String company_id = companyModel.getCompany_id();
                            final String company_email = companyModel.getEmail();
                            final String company_name = companyModel.getName();
                            final String company_phone = companyModel.getPhone_number();
                            final String company_website = companyModel.getWebsite();
                            final String currencyid = companyModel.getCurrency_id();
                            final String address = companyModel.getAddress();
                            final String bankname = companyModel.getPayment_bank_name();
                            final String paypalemail = companyModel.getPaypal_email();
                            final String payment_swiftbic = companyModel.getPayment_swift_bic();
                            final String cheque_payableto = companyModel.getCheque_payable_to();
                            final String ibnnumber = companyModel.getPayment_iban();
                            final String color = companyModel.getColor();
                            final String logo = companyModel.getLogo();


                            pref.edit().putString(Constant.COMPANY_NAME,company_name).commit();
                            pref.edit().putString(Constant.COMPANY_LOGO,logo).commit();
                            pref.edit().putString(Constant.COMPANY_EMAIL,company_email).commit();
                            pref.edit().putString(Constant.COMPANY_PHONE,company_phone).commit();
                            pref.edit().putString(Constant.COMPANY_WEB,company_website).commit();
                            pref.edit().putString(Constant.COMPANY_ID,company_id).commit();
                            pref.edit().putString(Constant.COMPANY_ADDRESS,address).commit();
                            pref.edit().putString(Constant.CURRENCY_ID,currencyid).commit();
                            pref.edit().putString(Constant.Payment_bank_name,bankname).commit();
                            pref.edit().putString(Constant.Paypal_email,paypalemail).commit();
                            pref.edit().putString(Constant.Payment_swift_bic,payment_swiftbic).commit();
                            pref.edit().putString(Constant.Cheque_payable_to,cheque_payableto).commit();
                            pref.edit().putString(Constant.Ibn_number,ibnnumber).commit();
                            pref.edit().putString("color",color).commit();
                            startActivity(intent);
                        }

                    }else{
                        createDialogOpenClass(Home_Activity.this);
                    }
                }


            }
        });

        home_AddyourItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getString(Constant.ROLE,"").equalsIgnoreCase("USER")){
                    Intent intent = new Intent(Home_Activity.this, Service_Activity.class);
                    intent.putExtra("key" , "home");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
                        Intent intent = new Intent(Home_Activity.this, Service_Activity.class);
                        intent.putExtra("key" , "home");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        if(preferences.getString(Constant.SERVICE,"").equalsIgnoreCase("1")){
                            Intent intent = new Intent(Home_Activity.this, Service_Activity.class);
                            intent.putExtra("key" , "home");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else{
                            createDialogOpenClass(Home_Activity.this);
                        }
                    }

                }

            }
        });

        home_SetupPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getString(Constant.ROLE,"").equalsIgnoreCase("USER")){
                    Intent intent = new Intent(Home_Activity.this, OnlinePaymentGatewayActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
                        Intent intent = new Intent(Home_Activity.this, OnlinePaymentGatewayActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        createDialogOpenClass(Home_Activity.this);
                    }

                }

            }
        });

        home_CreateyourInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getString(Constant.ROLE,"").equalsIgnoreCase("USER")){
                    Intent intent = new Intent(Home_Activity.this, InvoiceActivity.class);
                    intent.putExtra("key" , "home");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
                        Intent intent = new Intent(Home_Activity.this, InvoiceActivity.class);
                        intent.putExtra("key" , "home");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        if(preferences.getString(Constant.INVOICE,"").equalsIgnoreCase("1")){
                            Intent intent = new Intent(Home_Activity.this, InvoiceActivity.class);
                            intent.putExtra("key" , "home");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else{
                            createDialogOpenClass(Home_Activity.this);
                        }
                    }

                }

            }
        });








        seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home_Activity.this, Customer_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        vendorsseeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home_Activity.this, Vendor_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Comming Soon", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setFonts() {

        hello.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Bold.otf"));
        description.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Light.otf"));
        customerstxt.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Light.otf"));
        seeall.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Light.otf"));
        vendorstxt.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Light.otf"));
        vendorsseeall.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Light.otf"));

    }

    private void HomeApi(String company_id) {

        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.COMPANY_ID, company_id);
        RequestBody formBody = formBuilder.build();
        GetAsyncPost mAsync = new GetAsyncPost(Home_Activity.this, AllSirApi.Home, formBody, dialog, "") {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void getValueParse(Response response) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

                String result = "";
                customerModelArrayList = new ArrayList<>();


                if(response != null){
                    try {

                        result = response.body().string();
                        Log.e(TAG, "result:: "+result);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    // Log.e("HomeResponse", "result " + result);

                    if (response.isSuccessful()) {

                        try {

                            JSONObject jsonObject = new JSONObject(result);

                            JSONObject object_data = jsonObject.getJSONObject("data");
                            String supplier_image_path=object_data.getString("supplier_image_path");
                            String customer_image_path=object_data.getString("customer_image_path");
                            Log.e("customer_image_path",customer_image_path);
                            Log.e("supplier_image_path",supplier_image_path);


//                            JSONArray products_array = object_data.getJSONArray("product");
//                            if(products_array.length() > 0){
//                                imageView_AddyourProducts.setImageResource(R.drawable.radio_check);
//                            }

//                            JSONArray supplier_array = object_data.getJSONArray("supplier");
//                            JSONArray supplier_array = object_data.getJSONArray("supplier");


                            JSONArray customer_array = object_data.getJSONArray("customer");
                            //Log.e("detail Image", "result " + customer_array);

                            customerModelArrayList.clear();
                            for (int j = 0; j <customer_array.length(); j++) {

                                JSONObject obj_customer = customer_array.getJSONObject(j);

                                CustomerModel customerModel = new CustomerModel();

                                customerModel.setCustomerId(obj_customer.getString("customer_id"));
                                customerModel.setCustomerName(obj_customer.getString("customer_name"));
                                customerModel.setImage(obj_customer.getString("image"));
                                customerModel.setCustomer_image_path(customer_image_path);
                                String strresult=obj_customer.getString("customer_name");


                                //Log.e("CompanyList Image", "result " + strresult);
                                customerModelArrayList.add(customerModel);
                            }

                            if(customerModelArrayList.size() > 0){
                                imageView_AddyourCustomer.setImageResource(R.drawable.radio_check);
                            }else{
                                imageView_AddyourCustomer.setImageResource(R.drawable.radio_uncheck);
                            }


                            JSONArray invoice_array = object_data.getJSONArray("invoice");

                            invoiceModelArrayList.clear();
                            for (int i=0; i<invoice_array.length(); i++){

                                JSONObject obj_invoice = invoice_array.getJSONObject(i);

                                InvoiceModel invoiceModel=new InvoiceModel();

                                invoiceModel.setInvoice_id(obj_invoice.getString("invoice_id"));
                                invoiceModel.setInvoice_no(obj_invoice.getString("invoice_no"));
                                invoiceModel.setTotal(obj_invoice.getString("total"));
                                invoiceModel.setPayment_currency(obj_invoice.getString("currency_symbol"));
                                invoiceModelArrayList.add(invoiceModel);

                            }

                            if(invoiceModelArrayList.size() > 0){
                                imageView_CreateyourInvoice.setImageResource(R.drawable.radio_check);
                            }else{
                                imageView_CreateyourInvoice.setImageResource(R.drawable.radio_uncheck);
                            }


                            invoiceDueDateModelArrayList.clear();
                            for (int i=0; i<invoice_array.length(); i++){
                                JSONObject obj_invoice = invoice_array.getJSONObject(i);
                                InvoiceModel invoiceModel=new InvoiceModel();
                                invoiceModel.setInvoice_id(obj_invoice.getString("invoice_id"));
                                invoiceModel.setInvoice_no(obj_invoice.getString("invoice_no"));
                                invoiceModel.setTotal(obj_invoice.getString("total"));
                                invoiceModel.setPayment_currency(obj_invoice.getString("currency_symbol"));
                                String customer_name = obj_invoice.getString("customer_name");
                                invoiceModel.setCustomer_name(customer_name);

                                //  invoiceModel.setCustomer_name(customer_name);
                                String customer_id = obj_invoice.getString("customer_id");
                                String imageSS = getCustomerImageById(customerModelArrayList, customer_id);
                                Log.e(TAG, "imageSS "+imageSS);
                                invoiceModel.setCustomer_logo(customer_image_path+imageSS);

                                String due_date = obj_invoice.getString("due_date");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date date = sdf.parse(due_date);
                                    long millis = date.getTime();
                                    //  Log.e(TAG, "millisAAA "+millis);


                                    String todayCurrent = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                    Date dateCurrent = sdf.parse(todayCurrent);
                                    long millisCurrent = dateCurrent.getTime();
                                    //   Log.e(TAG, "dateCurrentAAA "+millisCurrent);

                                    if(millisCurrent > millis){
                                        String order_status_id = obj_invoice.getString("order_status_id");
                                        if(order_status_id.equalsIgnoreCase("1")){
                                            invoiceDueDateModelArrayList.add(invoiceModel);
                                        }
                                    }

                                }catch (Exception e){

                                }


                            }







                            JSONArray supplier_array = object_data.getJSONArray("supplier");

                            supplierModelArrayList.clear();
                            for (int k=0; k<supplier_array.length(); k++){

                                JSONObject obj_supplier = supplier_array.getJSONObject(k);

                                SupplierModel supplierModel=new SupplierModel();

                                supplierModel.setSupplierId(obj_supplier.getString("supplier_id"));
                                supplierModel.setSupplierName(obj_supplier.getString("supplier_name"));
                                supplierModel.setImage(obj_supplier.getString("image"));
                                supplierModel.setSupplier_image_path(supplier_image_path);

                                String strresult1=obj_supplier.getString("image");
                                //  Log.e("CompanyList Image", "result " + strresult1);


                                supplierModelArrayList.add(supplierModel);

                            }

                            businessactivitiesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                            HomeInvoiceAdapter homeInvoiceAdapter = new HomeInvoiceAdapter(Home_Activity.this);
                            businessactivitiesRV.setAdapter(homeInvoiceAdapter);

//                        LicenseListAdapter licenseListAdapter = new LicenseListAdapter(Home_Activity.this);
//                        businessactivitiesRV.setAdapter(licenseListAdapter);
                            if (invoiceModelArrayList.size()>0) {
                                txtinvoice.setVisibility(View.GONE);
                            } else {
                                txtinvoice.setVisibility(View.VISIBLE);
                            }

                            homeInvoiceAdapter.setData(invoiceModelArrayList);



                            invoiceoverdueRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                            Invoice_OverDue_Adapter invoice_overDue_adapter = new Invoice_OverDue_Adapter(Home_Activity.this);
                            invoiceoverdueRV.setAdapter(invoice_overDue_adapter);

                            if (invoiceDueDateModelArrayList.size() > 0) {
                                no_overduetxt.setVisibility(View.GONE);
                            } else {
                                no_overduetxt.setVisibility(View.VISIBLE);
                            }

                            invoice_overDue_adapter.setData(invoiceDueDateModelArrayList);




                            int numberOfColumns = 3;
                            recyclerVinder.setLayoutManager(new GridLayoutManager(Home_Activity.this, numberOfColumns));
                            HomeSupplierAdapter homeSupplierAdapter = new HomeSupplierAdapter(Home_Activity.this);
                            recyclerVinder.setAdapter(homeSupplierAdapter);

                            if (supplierModelArrayList.size()>0) {
                                txtNo.setVisibility(View.GONE);
                            } else {
                                txtNo.setVisibility(View.VISIBLE);
                            }
                            homeSupplierAdapter.setData(supplierModelArrayList);


                            int numberOfColumns2 = 3;
                            recycleCustomers.setLayoutManager(new GridLayoutManager(Home_Activity.this, numberOfColumns2));
                            HomeCustomerAdapter homeCustomerAdapter = new HomeCustomerAdapter(Home_Activity.this);

                            recycleCustomers.setAdapter(homeCustomerAdapter);

                            if (customerModelArrayList.size()>0) {
                                no_customer.setVisibility(View.GONE);
                            } else {
                                no_customer.setVisibility(View.VISIBLE);
                            }
                            homeCustomerAdapter.setData(customerModelArrayList);


                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    } else {

                        try {

                            JSONObject jsonObject = new JSONObject(result);
                            // util.showToast(Home_Activity.this, jsonObject.getString("message"));

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }
                    }
                }else{
                    if (invoiceModelArrayList.size()>0) {
                        txtinvoice.setVisibility(View.GONE);
                    } else {
                        txtinvoice.setVisibility(View.VISIBLE);
                    }

                    if (invoiceDueDateModelArrayList.size()>0) {
                        no_overduetxt.setVisibility(View.GONE);
                    } else {
                        no_overduetxt.setVisibility(View.VISIBLE);
                    }

                    if (customerModelArrayList.size()>0) {
                        txtNo.setVisibility(View.GONE);
                    } else {
                        txtNo.setVisibility(View.VISIBLE);
                    }

                    if (supplierModelArrayList.size()>0) {
                        no_customer.setVisibility(View.GONE);
                    } else {
                        no_customer.setVisibility(View.VISIBLE);
                    }
                }








            }

            @Override
            public void retry() {

            }
        };

        mAsync.execute();

    }




    @SuppressLint("LongLogTag")
    private void CompanyInformation(String selectedCompanyId) {

        RequestParams params = new RequestParams();
        params.add("company_id", selectedCompanyId);
        params.add("product", "1");
        params.add("service", "1");
        params.add("customer", "1");
        params.add("tax", "1");
        params.add("credit_note", "1");
        //  params.add("invoice", "1");
        params.add("warehouse", "1");


        Log.e(TAG, "paramsa"+params);

        String token = Constant.GetSharedPreferences(Home_Activity.this, Constant.ACCESS_TOKEN);
        Log.e("token", token);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "company/info", params, new AsyncHttpResponseHandler() {
            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "onSuccess11 "+response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        Log.e(TAG, "onSuccessdata11 " + data.toString());

                        JSONArray company = data.getJSONArray("company");
                        JSONArray product = data.getJSONArray("product");
                        JSONArray service = data.getJSONArray("service");
                        JSONArray warehouse = data.getJSONArray("warehouse");

                        if(product.length() > 0){
                            imageView_AddyourProducts.setImageResource(R.drawable.radio_check);
                        }else{
                            imageView_AddyourProducts.setImageResource(R.drawable.radio_uncheck);
                        }

                        if(service.length() > 0){
                            imageView_AddyourItems.setImageResource(R.drawable.radio_check);
                        }else{
                            imageView_AddyourItems.setImageResource(R.drawable.radio_uncheck);
                        }

                        if(warehouse.length() > 0){
                            imageView_AddyourWarehouse.setImageResource(R.drawable.radio_check);
                        }else{
                            imageView_AddyourWarehouse.setImageResource(R.drawable.radio_uncheck);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }




    private String getCustomerImageById(ArrayList<CustomerModel> customerModelArrayList, String customer_id) {
        String path = "";
        if(customerModelArrayList.size() > 0){
            for (int k=0; k<customerModelArrayList.size(); k++){
                if(customerModelArrayList.get(k).getCustomerId().equalsIgnoreCase(customer_id)){
                    path = customerModelArrayList.get(k).getImage().toString();
                }
            }
        }
        return path;
    }

    private void COMPANYListingApi() {

        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);

        FormBody.Builder formBuilder = new FormBody.Builder();
        RequestBody formBody = formBuilder.build();
        GetAsyncPost mAsync = new GetAsyncPost(Home_Activity.this, AllSirApi.COMPANYListing, formBody, dialog, "") {
            @Override
            public void getValueParse(Response response) {
                Log.e(TAG, "getValueParse "+response);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

                String result = "";
                companyModelArrayList = new ArrayList<>();


//                if(response == null){
//
//                }


                if(response != null){
                    try {

                        result = response.body().string();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    Log.e("CompanyListResponse", "result " + result);

                    if (response.isSuccessful()) {

                        try {

                            JSONObject jsonObject = new JSONObject(result);

                            JSONObject object_data = jsonObject.getJSONObject("data");

                            JSONArray invoice_array = object_data.getJSONArray("company");

                            for (int i=0; i<invoice_array.length(); i++){

                                JSONObject obj_invoice = invoice_array.getJSONObject(i);

                                CompanyModel companyModel=new CompanyModel();
                                companyModel.setCompany_id(obj_invoice.getString("company_id"));
                                companyModel.setUser_id(obj_invoice.getString("user_id"));
                                companyModel.setColor(obj_invoice.getString("color"));
                                companyModel.setName(obj_invoice.getString("name"));
                                companyModel.setPhone_number(obj_invoice.getString("phone_number"));
                                companyModel.setEmail(obj_invoice.getString("email"));
                                companyModel.setWebsite(obj_invoice.getString("website"));
                                companyModel.setLogo(obj_invoice.getString("logo"));
                                companyModel.setAddress(obj_invoice.getString("address"));
                                companyModel.setCurrency_id(obj_invoice.getString("currency_id"));
                                companyModel.setPayment_bank_name(obj_invoice.getString("payment_bank_name"));
                                companyModel.setPayment_currency(obj_invoice.getString("payment_currency"));
                                companyModel.setPayment_iban(obj_invoice.getString("payment_iban"));
                                companyModel.setPayment_swift_bic(obj_invoice.getString("payment_swift_bic"));
                                companyModel.setPaypal_email(obj_invoice.getString("paypal_email"));
                                companyModel.setPaypal_email_2(obj_invoice.getString("paypal_email_2"));
                                companyModel.setPaypal_email_2_type(obj_invoice.getString("paypal_email_2_type"));
                                companyModel.setStripe_token(obj_invoice.getString("stripe_token"));
                                companyModel.setPaypal(obj_invoice.getString("paypal"));
                                companyModel.setStripe(obj_invoice.getString("stripe"));
                                companyModel.setCheque_payable_to(obj_invoice.getString("cheque_payable_to"));
                                companyModel.setComma_format(obj_invoice.getString("comma_format"));

                                compantId = obj_invoice.getString("company_id");
                                Log.e("Company Id",compantId);

                                companyModelArrayList.add(companyModel);

                                if(i == 0){
                                    description.setText(""+obj_invoice.getString("name"));
                                    HomeApi(compantId);
                                    CompanyInformation(compantId);
                                    if(obj_invoice.getString("paypal").equalsIgnoreCase("0") &&
                                            obj_invoice.getString("stripe").equalsIgnoreCase("0")){
                                        imageView_SetupPayment.setImageResource(R.drawable.radio_uncheck);
                                    }else{
                                        imageView_SetupPayment.setImageResource(R.drawable.radio_check);
                                    }

                                }



                            }


                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    } else {

                        try {

                            JSONObject jsonObject = new JSONObject(result);

                            Log.e(TAG , "jsonObjectAA "+jsonObject.toString());
//                            if(jsonObject.getString("status").equalsIgnoreCase("false")){
//                                if(jsonObject.getString("message").contains("Unauthorized Access")){
//
//                                }
//                            }
//
//                            Unauthorized
                            // util.showToast(Home_Activity.this, jsonObject.getString("message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    if (invoiceModelArrayList.size()>0) {
                        txtinvoice.setVisibility(View.GONE);
                    } else {
                        txtinvoice.setVisibility(View.VISIBLE);
                    }

                    if (invoiceDueDateModelArrayList.size()>0) {
                        no_overduetxt.setVisibility(View.GONE);
                    } else {
                        no_overduetxt.setVisibility(View.VISIBLE);
                    }

                    if (customerModelArrayList.size()>0) {
                        txtNo.setVisibility(View.GONE);
                    } else {
                        txtNo.setVisibility(View.VISIBLE);
                    }

                    if (supplierModelArrayList.size()>0) {
                        no_customer.setVisibility(View.GONE);
                    } else {
                        no_customer.setVisibility(View.VISIBLE);
                    }
                }else{
                    if (invoiceModelArrayList.size()>0) {
                        txtinvoice.setVisibility(View.GONE);
                    } else {
                        txtinvoice.setVisibility(View.VISIBLE);
                    }

                    if (invoiceDueDateModelArrayList.size()>0) {
                        no_overduetxt.setVisibility(View.GONE);
                    } else {
                        no_overduetxt.setVisibility(View.VISIBLE);
                    }

                    if (customerModelArrayList.size()>0) {
                        txtNo.setVisibility(View.GONE);
                    } else {
                        txtNo.setVisibility(View.VISIBLE);
                    }

                    if (supplierModelArrayList.size()>0) {
                        no_customer.setVisibility(View.GONE);
                    } else {
                        no_customer.setVisibility(View.VISIBLE);
                    }

                }



                if(companyModelArrayList.size() > 0){
                    imageView_AddyourBusiness.setImageResource(R.drawable.radio_check);
                }else{
                    imageView_AddyourBusiness.setImageResource(R.drawable.radio_uncheck);
                }


            }

            @Override
            public void retry() {
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                Log.e(TAG, "retry");
            }
        };

        mAsync.execute();

    }



    private void getSubscriptionApi() {

        Utility.hideKeypad(Home_Activity.this);

            RequestParams params = new RequestParams();
            params.add("language", getLanguage());

            String token = Constant.GetSharedPreferences(Home_Activity.this, Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.addHeader("Access-Token", token);
            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL + "subscription/get", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                      String response111 = new String(responseBody);
                      Log.e(TAG, "addproductResp1 "+response111);

                    try {
                        JSONObject jsonObject = new JSONObject(response111);
                        String status = jsonObject.getString("status");
                        if (status.equals("true")) {
                            JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                            JSONArray jsonArraySubscription = jsonObjectdata.getJSONArray("subscription");

                            if(jsonArraySubscription.length() > 0){
                                for(int i = 0 ; i < jsonArraySubscription.length() ; i++){
                                    JSONObject jsonObjectVal = jsonArraySubscription.getJSONObject(i);
                                    String subscription_type = jsonObjectVal.getString("subscription_type");
                                    if(subscription_type.equalsIgnoreCase("oneyear")){
                                        Log.e(TAG, "subscription_type1 "+subscription_type);
                                        pref.setSubsType(subscription_type);
                                        break;
                                    }else{
                                        if(subscription_type.equalsIgnoreCase("onemonth")){
                                            Log.e(TAG, "subscription_type2 "+subscription_type);
                                            pref.setSubsType(subscription_type);
                                            break;
                                        }
                                    }
                                }
                            }else{
                                pref.setSubsType("");
                            }
                        }
                    }catch (Exception e){

                    }

                   // Constant.toolbar(Home_Activity.this, "");
                    setLeftValues();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    String response111 = new String(responseBody);
//                    Log.e("addproductResp2 ", response111);
                    pref.setSubsType("");
                }
            });


    }




    Boolean twice = false;

    @Override
    public void onBackPressed() {
        if (twice == true) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        twice = true;
        Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        }, 3000);

    }

    public void messagebar() {
        new Flashbar.Builder(Home_Activity.this)
                .gravity(Flashbar.Gravity.BOTTOM)
                .title(getString(R.string.dialog_Success))
                .titleTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Bold.otf"))
                .titleSizeInSp(15)
                .message(getString(R.string.logged_in_msg))
                //.messageTypeface(Typeface.createFromAsset(getAssets(),"fonts/CatamaraBold.ttf"))
                .backgroundDrawable(getResources().getDrawable(R.drawable.success_toast_background))
                .duration(3000)
                .enableSwipeToDismiss()
                .castShadow(false)
                .enterAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate())
                .build().show();
    }

    @Override
    public void onMenuItemClick(String menuName) {

    }





    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

        private static final String TAG = "MenuAdapter";

        ArrayList<CompanyModel> alName = new ArrayList<>();
        Dialog mybuilder;

        public MenuAdapter(ArrayList<CompanyModel> arrayList, Dialog mybuilder) {
            super();
            this.alName = arrayList;
           this.mybuilder = mybuilder;
        }

        @Override
        public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.menu_item, viewGroup, false);
//            SellerFeedbackAdapter.ViewHolder viewHolder = new SellerFeedbackAdapter.ViewHolder(v);
//            return viewHolder;


//            val v = LayoutInflater.from(viewGroup.context)
//                    .inflate(R.layout.mybooking_item, viewGroup, false)
            return new MenuAdapter.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final MenuAdapter.ViewHolder viewHolder, final int i) {

            viewHolder.textViewName.setText(""+alName.get(i).getName());
            viewHolder.textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mybuilder.dismiss();
                    description.setText(""+alName.get(i).getName());
                    HomeApi(alName.get(i).company_id);
                    CompanyInformation(alName.get(i).company_id);
                    companyPosition = i;

                    if(alName.get(i).getPaypal().equalsIgnoreCase("0") &&
                            alName.get(i).getStripe().equalsIgnoreCase("0")){
                        imageView_SetupPayment.setImageResource(R.drawable.radio_uncheck);
                    }else{
                        imageView_SetupPayment.setImageResource(R.drawable.radio_check);
                    }
                }
            });

        }


        @Override
        public int getItemCount() {
            return alName.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            View view11 = null;
            TextView textViewName;
            public ViewHolder(View itemView) {
                super(itemView);
                view11 = itemView;
                //  imageViewImage = (ImageView) itemView.findViewById(R.id.profile_image);
                textViewName = (TextView) itemView.findViewById(R.id.txtList);


            }



        }



        public void updateData(ArrayList<CompanyModel> arrayList2) {
            // TODO Auto-generated method stub
            alName = arrayList2;
            notifyDataSetChanged();
        }


    }




    @Override
    protected void onStart() {
        super.onStart();

        FirebaseInAppMessaging.getInstance().triggerEvent("App Open");
        FirebaseInAppMessaging.getInstance().triggerEvent("first_open");
        FirebaseInAppMessaging.getInstance().triggerEvent("dashboard_view");
        Log.e(TAG, "onStart");

    }


    @Override
    protected void onStop() {
        super.onStop();
//        FirebaseInAppMessaging.getInstance().removeDisplayErrorListener(new FirebaseInAppMessagingDisplay(){
//
//            @Override
//            public void displayMessage(@NonNull InAppMessage inAppMessage, @NonNull FirebaseInAppMessagingDisplayCallbacks firebaseInAppMessagingDisplayCallbacks) {
//
//            }
//        });
    }


    public void closeDrawer() {
        if(drawerLayout != null){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }



}
