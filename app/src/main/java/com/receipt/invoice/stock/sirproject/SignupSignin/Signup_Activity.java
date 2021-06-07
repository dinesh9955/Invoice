package com.receipt.invoice.stock.sirproject.SignupSignin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Home.Home_Activity;
import com.receipt.invoice.stock.sirproject.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class Signup_Activity extends BaseActivity {

    String TAG = "Signup_Activity";
    TextView txtsignup,txtsignupdes,txtregistered,txtsigninhere;
    EditText edfullname,edemail,edpassword;
    Button btnregister;

    String mail="";

    private AVLoadingIndicatorView avi;
    ImageView avibackground;
    ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);
        txtsignup = findViewById(R.id.txtsignup);
        txtsignupdes = findViewById(R.id.txtsignupdes);
        txtregistered = findViewById(R.id.txtregistered);
        txtsigninhere = findViewById(R.id.txtsigninhere);
        edfullname = findViewById(R.id.edfullname);
        edemail = findViewById(R.id.edemail);
        edpassword = findViewById(R.id.edpassword);
        btnregister = findViewById(R.id.btnregister);
        imgback = findViewById(R.id.imgback);

        fonts();

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup_Activity.this.onBackPressed();
            }
        });

        txtsigninhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup_Activity.this,Signin_Activity.class);
                startActivity(intent);
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                userregistration();

               // messagebar();
            }
        });


    }


    public void fonts()
    {
        txtsignup.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Bold.otf"));
        txtsignupdes.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtregistered.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtsigninhere.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));
        edfullname.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Regular.ttf"));
        edemail.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Regular.ttf"));
        edpassword.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Regular.ttf"));
        btnregister.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));

    }




 public void userregistration()
 {
     if (TextUtils.isEmpty(edfullname.getText())) {
         edfullname.setError("Field is required");
         edfullname.requestFocus();
     }
     else if (TextUtils.isEmpty(edemail.getText())) {
         edemail.setError("Field is required");
         edemail.requestFocus();
     }else if (!Patterns.EMAIL_ADDRESS.matcher(edemail.getText().toString()).matches())
     {
         edemail.setError("Invalid Pattern");
         edemail.requestFocus();
     }

     else if (TextUtils.isEmpty(edpassword.getText())) {
         edpassword.setError("Field is required");
         edpassword.requestFocus();
     }
     else if (!(edpassword.getText().toString().trim().length() >= 6)) {
         edpassword.setError("Password should be min 6 characters");
         edpassword.requestFocus();
     }
     else {

         int value = -1;
         try{
             value = Integer.parseInt(edfullname.getText().toString().replaceAll("[^0-9]", ""));
         }catch (Exception e){

         }


         Log.e(TAG, "value "+value);

         if(value == -1){
             avi.smoothToShow();
             avibackground.setVisibility(View.VISIBLE);


             String fullname = edfullname.getText().toString();
             String email = edemail.getText().toString();
             final String password = edpassword.getText().toString();
             RequestParams requestParams = new RequestParams();
             requestParams.add("email",email);
             requestParams.add("password",password);
             requestParams.add("fullname",fullname);

             requestParams.add("device_type", "Andorid");

             AsyncHttpClient client = new AsyncHttpClient();
             client.post(Constant.BASE_URL+"user/registration", requestParams, new AsyncHttpResponseHandler() {
                 @Override
                 public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                     String response = new String(responseBody);
                     Log.e("signupresponse",response);

                     avi.smoothToHide();
                     avibackground.setVisibility(View.GONE);
                     try {

                         final SharedPreferences pref = getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);
                         JSONObject jsonObject = new JSONObject(response);

                         String status = jsonObject.getString("status");
                         if (status.equals("true"))
                         {
                             //JSONObject data = jsonObject.getJSONObject("data");

                             Map<String, Object> eventValue = new HashMap<String, Object>();
                             eventValue.put(AFInAppEventParameterName.PARAM_1, "Signup_complete");
                             AppsFlyerLib.getInstance().trackEvent(Signup_Activity.this, "Signup_complete", eventValue);

                             Bundle params2 = new Bundle();
                             params2.putString("event_name", "Complete Sign-up");
                             firebaseAnalytics.logEvent("signup_complete", params2);

                             Constant.SuccessToast(Signup_Activity.this,jsonObject.getString("message"));


                             Signin();


                         }

                         if (status.equals("false")){
                             Constant.ErrorToast(Signup_Activity.this,jsonObject.getString("message"));
                         }


                     } catch (JSONException e) {
                         e.printStackTrace();
                     }


                     // messagebar();

                 }

                 @Override
                 public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                     if(responseBody!=null){
                         String response = new String(responseBody);
                         Log.e("signupresponseFBB",response);

                         try {
                             JSONObject jsonObject = new JSONObject(response);
                             Log.e(TAG, "jsonObjectBB"+jsonObject);

                             String status = jsonObject.getString("status");
                             if (status.equals("false"))
                             {
                                 Log.e(TAG, "statusBB"+status);

                                 JSONObject message = jsonObject.getJSONObject("message");

                                 Log.e(TAG, "messageBB"+message);

                                 String email = message.getString("email");
                                 //String fullname = message.getString("fullname");
                                 //Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                 Constant.ErrorToast(Signup_Activity.this,email);
                             }
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }

                     }
                     avi.smoothToHide();
                     avibackground.setVisibility(View.GONE);
                 }
             });
         }else{
             edfullname.setError("Digit is not required");
             edfullname.requestFocus();
         }


 }

 }







    public void Signin()
    {
        final SharedPreferences pref = getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);
        if (TextUtils.isEmpty(edemail.getText())) {
            edemail.setError("Field is required");
            edemail.requestFocus();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(edemail.getText().toString()).matches())
        {
            edemail.setError("Invalid Pattern");
            edemail.requestFocus();
        }

        else if (TextUtils.isEmpty(edpassword.getText())) {
            edpassword.setError("Field is required");
            edpassword.requestFocus();
        }
        else if (!(edpassword.getText().toString().trim().length() >= 6)) {
            edpassword.setError("Password should be min 6 characters");
            edpassword.requestFocus();
        }else
        {

            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);


            String email = edemail.getText().toString();
            String password = edpassword.getText().toString();

            RequestParams params = new RequestParams();

            params.add("email",email);
            params.add("password",password);

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(Constant.BASE_URL+"user/login",params, new AsyncHttpResponseHandler() {
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

                            pref.edit().putString(Constant.ACCESS_TOKEN,access_token).commit();
                            pref.edit().putString(Constant.FULLNAME,fullname).commit();
                            pref.edit().putString(Constant.EMAIL,email).commit();
                            pref.edit().putBoolean(Constant.LOGGED_IN,true).commit();
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


                            Constant.SuccessToast(Signup_Activity.this,jsonObject.getString("message"));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Signup_Activity.this, Home_Activity.class);
                                    intent.putExtra("login","login");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            },500);

                        }

                        if (status.equals("false"))
                        {
                            JSONObject message = jsonObject.getJSONObject("message");
                            String messageMsg = message.getString("email");
                            Log.e("signinresponseF",response);

                            //Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            Constant.ErrorToast(Signup_Activity.this, messageMsg);


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if(responseBody!=null){
                        String response = new String(responseBody);
                        Log.e("signinresponseFAA ",response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            Log.e(TAG, "signinresponseF"+status);

                            if (status.equals("false"))
                            {
                                JSONObject message = jsonObject.getJSONObject("message");
                                String err = jsonObject.getString("message");
                                Log.e("signinresponseF",response);

                                //Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            //    Constant.ErrorToast(Signup_Activity.this, message);


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


}
