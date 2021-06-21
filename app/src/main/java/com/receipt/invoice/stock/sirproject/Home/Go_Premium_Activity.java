package com.receipt.invoice.stock.sirproject.Home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.SignupSignin.Signup_Activity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Go_Premium_Activity extends BaseActivity implements BillingProcessor.IBillingHandler {

    ImageView back;
    TextView heading;

    RelativeLayout r1,r2,r3;
    TextView annualtxt,annualprice,monthtxt,monthprice,restoretxt,purchasestxt;
    ImageView selection1,selection2,selection3;

    TextView termstxt,termsdescprice1,termdescription1;
    TextView termsdescprice2,termdescription2;

    TextView skiptxt,skipdescription;
    Button startfree;

    String mail="",fromsignup="",pwd="";

    //Billing
    BillingProcessor bp;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go__premium_);

        //Billing
       // bp = new BillingProcessor(this,"",this);
        bp = BillingProcessor.newBillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1N1+ZLMuFAppmlqxGTxvEHuNj2FhZnk1f0kYDwjSJ/oj9Yam2AiJJutZNussNwSDGKsSCN4srSGeYR6crH81iw7lXnsbUohdCw0mLNjVKn5N7eCZlpcqgjyjcyjX/vckpXz+IMmWJK8eodwkuAs56ikltXOrV03Jl19SuORlvzBbmq0Ff2iEKxP8HOxyi9rfPcrhk6e2+wquLA5UL5/K/ppj7YkTphamDpJA5z+iKUnKT5HAuFh+JMrw7B5k7a1VKT8VH1uHJ1zozoDDhxQXnNXw+Nu+kgj4mWmBHuNKTjRhAy6zQfpW8divo1Ckn1ic6wUTAsNmpd1OyFcVYDQ+NwIDAQAB", this); // doesn't bind

        bp.initialize();



        FindByIds();
        setFonts();
        setListeners();

        if (getIntent().hasExtra("signup")){
            mail = getIntent().getStringExtra("mail");
            pwd = getIntent().getStringExtra("pwd");
            fromsignup="yes";
        }

    }

    private void FindByIds(){
        back = findViewById(R.id.back);
        heading = findViewById(R.id.heading);
        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);

        annualtxt = findViewById(R.id.annualtxt);
        annualprice = findViewById(R.id.annualprice);
        monthtxt = findViewById(R.id.monthtxt);
        monthprice = findViewById(R.id.monthprice);
        restoretxt = findViewById(R.id.restoretxt);
        purchasestxt = findViewById(R.id.purchasestxt);
        selection1 = findViewById(R.id.selection1);
        selection2 = findViewById(R.id.selection2);
        selection3 = findViewById(R.id.selection3);

        termstxt = findViewById(R.id.termstxt);
        termsdescprice1 = findViewById(R.id.termsdescprice1);
        termdescription1 = findViewById(R.id.termdescription1);
        termsdescprice2 = findViewById(R.id.termsdescprice2);
        termdescription2 = findViewById(R.id.termdescription2);

        skiptxt = findViewById(R.id.skiptxt);
        skipdescription = findViewById(R.id.skipdescription);
        startfree = findViewById(R.id.startfree);
    }

    private void setFonts(){
        heading.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Bold.otf"));

        annualtxt.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        annualprice.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Bold.ttf"));
        monthtxt.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        monthprice.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Bold.ttf"));
        restoretxt.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        purchasestxt.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Bold.ttf"));

        termstxt.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        termsdescprice1.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Bold.ttf"));
        termdescription1.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        termsdescprice2.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Bold.ttf"));
        termdescription2.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));

        skiptxt.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        skipdescription.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Regular.ttf"));
        startfree.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Medium.ttf"));

    }

    private void setListeners(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Go_Premium_Activity.this,Signup_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);            }
        });

        startfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signin();


            }
        });

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(r1.getBackground().getConstantState()== getResources().getDrawable(R.drawable.pkg_unselect_curve).getConstantState()){

                    r1.setBackground(getResources().getDrawable(R.drawable.premium_pkg_curve));
                    selection1.setImageResource(R.drawable.plan_checked);
                    r2.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection2.setImageResource(R.drawable.checked);
                    r3.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection3.setColorFilter(ContextCompat.getColor(Go_Premium_Activity.this, R.color.lightwhite), android.graphics.PorterDuff.Mode.MULTIPLY);

                    annualtxt.setTextColor(getResources().getColor(R.color.darkbluefont));
                    annualprice.setTextColor(getResources().getColor(R.color.darkbluefont));

                    monthprice.setTextColor(getResources().getColor(R.color.lightwhite));
                    monthtxt.setTextColor(getResources().getColor(R.color.lightwhite));

                    restoretxt.setTextColor(getResources().getColor(R.color.lightwhite));
                    purchasestxt.setTextColor(getResources().getColor(R.color.lightwhite));

                    //perform action after 2 secs
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           Toast.makeText(getApplicationContext(),"Payment coming soon",Toast.LENGTH_SHORT).show();
                        }
                    },500);
                }
                else{
                    r1.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection1.setImageResource(R.drawable.checked);
                    r2.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection2.setImageResource(R.drawable.checked);
                    r3.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection3.setColorFilter(ContextCompat.getColor(Go_Premium_Activity.this, R.color.lightwhite), android.graphics.PorterDuff.Mode.MULTIPLY);

                    annualtxt.setTextColor(getResources().getColor(R.color.lightwhite));
                    annualprice.setTextColor(getResources().getColor(R.color.lightwhite));
                    monthprice.setTextColor(getResources().getColor(R.color.lightwhite));
                    monthtxt.setTextColor(getResources().getColor(R.color.lightwhite));
                    restoretxt.setTextColor(getResources().getColor(R.color.lightwhite));
                    purchasestxt.setTextColor(getResources().getColor(R.color.lightwhite));
                }


            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(r2.getBackground().getConstantState()== getResources().getDrawable(R.drawable.pkg_unselect_curve).getConstantState()){
                    r2.setBackground(getResources().getDrawable(R.drawable.premium_pkg_curve));
                    selection2.setImageResource(R.drawable.plan_checked);
                    r1.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection1.setImageResource(R.drawable.checked);
                    r3.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection3.setColorFilter(ContextCompat.getColor(Go_Premium_Activity.this, R.color.lightwhite), android.graphics.PorterDuff.Mode.MULTIPLY);

                    annualtxt.setTextColor(getResources().getColor(R.color.lightwhite));
                    annualprice.setTextColor(getResources().getColor(R.color.lightwhite));

                    monthprice.setTextColor(getResources().getColor(R.color.darkbluefont));
                    monthtxt.setTextColor(getResources().getColor(R.color.darkbluefont));

                    restoretxt.setTextColor(getResources().getColor(R.color.lightwhite));
                    purchasestxt.setTextColor(getResources().getColor(R.color.lightwhite));

                    //perform action after 2 secs
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //bp.subscribe(Go_Premium_Activity.this,"monthly_3.99");
                            //bp.subscribe(Go_Premium_Activity.this,"android.test.purchased");
                            Toast.makeText(getApplicationContext(),"Payment coming soon",Toast.LENGTH_SHORT).show();
                        }
                    },500);
                }
                else{
                    r1.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection1.setImageResource(R.drawable.checked);
                    r2.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection2.setImageResource(R.drawable.checked);
                    r3.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection3.setColorFilter(ContextCompat.getColor(Go_Premium_Activity.this, R.color.lightwhite), android.graphics.PorterDuff.Mode.MULTIPLY);

                    annualtxt.setTextColor(getResources().getColor(R.color.lightwhite));
                    annualprice.setTextColor(getResources().getColor(R.color.lightwhite));

                    monthprice.setTextColor(getResources().getColor(R.color.lightwhite));
                    monthtxt.setTextColor(getResources().getColor(R.color.lightwhite));

                    restoretxt.setTextColor(getResources().getColor(R.color.lightwhite));
                    purchasestxt.setTextColor(getResources().getColor(R.color.lightwhite));
                }

            }
        });

        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(r3.getBackground().getConstantState()== getResources().getDrawable(R.drawable.pkg_unselect_curve).getConstantState()){

                    r3.setBackground(getResources().getDrawable(R.drawable.premium_pkg_curve));
                    selection3.setColorFilter(ContextCompat.getColor(Go_Premium_Activity.this, R.color.selectiongreen), android.graphics.PorterDuff.Mode.MULTIPLY);

                    r1.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection1.setImageResource(R.drawable.checked);
                    r2.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection2.setImageResource(R.drawable.checked);

                    annualtxt.setTextColor(getResources().getColor(R.color.lightwhite));
                    annualprice.setTextColor(getResources().getColor(R.color.lightwhite));

                    monthprice.setTextColor(getResources().getColor(R.color.lightwhite));
                    monthtxt.setTextColor(getResources().getColor(R.color.lightwhite));

                    restoretxt.setTextColor(getResources().getColor(R.color.darkbluefont));
                    purchasestxt.setTextColor(getResources().getColor(R.color.darkbluefont));


                    //perform action after 2 secs
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Payment coming soon",Toast.LENGTH_SHORT).show();
                        }
                    },500);
                }
                else{
                    r1.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection1.setImageResource(R.drawable.checked);
                    r2.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection2.setImageResource(R.drawable.checked);
                    r3.setBackground(getResources().getDrawable(R.drawable.pkg_unselect_curve));
                    selection3.setColorFilter(ContextCompat.getColor(Go_Premium_Activity.this, R.color.lightwhite), android.graphics.PorterDuff.Mode.MULTIPLY);

                    annualtxt.setTextColor(getResources().getColor(R.color.lightwhite));
                    annualprice.setTextColor(getResources().getColor(R.color.lightwhite));

                    monthprice.setTextColor(getResources().getColor(R.color.lightwhite));
                    monthtxt.setTextColor(getResources().getColor(R.color.lightwhite));

                    restoretxt.setTextColor(getResources().getColor(R.color.lightwhite));
                    purchasestxt.setTextColor(getResources().getColor(R.color.lightwhite));
                }

            }
        });
    }

    public void Signin()
    {
        final SharedPreferences pref = getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);
        if (TextUtils.isEmpty(mail)) {
            Constant.ErrorToast(Go_Premium_Activity.this,"Email Required");

        }else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            Constant.ErrorToast(Go_Premium_Activity.this,"Invalid Email");

        }

        else if (TextUtils.isEmpty(pwd)) {
           Constant.ErrorToast(Go_Premium_Activity.this,"Password Required");
        }
        else
        {

            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);



            RequestParams params = new RequestParams();
            params.add("email",mail);
            params.add("password",pwd);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());

            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL+"user/login",params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    String response = new String(responseBody);

                    Log.e("signinresponse",response);

                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("true"))
                        {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String access_token = data.getString("access_token");
                            JSONObject profile = data.getJSONObject("profile");
                            String fullname = profile.getString("fullname");
                            String email = profile.getString("email");
                            pref.edit().putString(Constant.ACCESS_TOKEN,access_token).commit();
                            pref.edit().putString(Constant.FULLNAME,fullname).commit();
                            pref.edit().putString(Constant.EMAIL,email).commit();
                            pref.edit().putBoolean(Constant.LOGGED_IN,true).commit();
                            JSONObject permissions = profile.getJSONObject("permission");
                            String invoice = permissions.getString("invoice");
                            String estimate = permissions.getString("estimate");
                            String stock = permissions.getString("stock");
                            String receipt = permissions.getString("receipt");
                            String purchase_order = permissions.getString("purchase_order");
                            String payment_voucher = permissions.getString("payment_voucher");
                            String tax = permissions.getString("tax");
                            String customer = permissions.getString("customer");
                            String supplier = permissions.getString("supplier");
                            String product = permissions.getString("product");
                            String service = permissions.getString("service");
                            String credit_note = permissions.getString("credit_note");
                            String sub_admin = permissions.getString("sub_admin");

                            Log.e("sub_admin",sub_admin);
                            Log.e("service",service);

                            pref.edit().putString(Constant.SUB_ADMIN,sub_admin).commit();
                            pref.edit().putString(Constant.INVOICE,invoice).commit();
                            pref.edit().putString(Constant.ESTIMATE,estimate).commit();
                            pref.edit().putString(Constant.STOCK,stock).commit();
                            pref.edit().putString(Constant.RECEIPT,receipt).commit();
                            pref.edit().putString(Constant.PURCHASE_ORDER,purchase_order).commit();
                            pref.edit().putString(Constant.PAYMENT_VOUCHER,payment_voucher).commit();
                            pref.edit().putString(Constant.TAX,tax).commit();
                            pref.edit().putString(Constant.CUSTOMER,customer).commit();
                            pref.edit().putString(Constant.SUPPLIER,supplier).commit();
                            pref.edit().putString(Constant.PRODUCT,product).commit();
                            pref.edit().putString(Constant.SERVICE,service).commit();
                            pref.edit().putString(Constant.CREDIT_NOTE,credit_note).commit();


                            Constant.SuccessToast(Go_Premium_Activity.this,jsonObject.getString("message"));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Go_Premium_Activity.this, Home_Activity.class);
                                    intent.putExtra("login","login");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            },1000);



                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if(responseBody!=null){
                        String response = new String(responseBody);
                        Log.e("signinresponseF",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            if (status.equals("false"))
                            {

                                //Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                Constant.ErrorToast(Go_Premium_Activity.this,jsonObject.getString("message"));


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);

                }
            });


        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(Go_Premium_Activity.this,Signup_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Billing
    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        //Toast.makeText(getApplicationContext(),"Purchased: "+productId,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPurchaseHistoryRestored() {
        //Toast.makeText(getApplicationContext(),"Purchase History",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        //Toast.makeText(getApplicationContext(),"Purchase Error",Toast.LENGTH_SHORT).show();
        //Log.e("errcode", String.valueOf(errorCode));
        //Log.e("errcodemsg", error.toString());


    }

    @Override
    public void onBillingInitialized() {
       // Toast.makeText(getApplicationContext(),"initialized",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}

