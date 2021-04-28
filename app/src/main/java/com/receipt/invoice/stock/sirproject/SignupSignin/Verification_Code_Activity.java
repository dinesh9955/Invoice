package com.receipt.invoice.stock.sirproject.SignupSignin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Home.Go_Premium_Activity;
import com.receipt.invoice.stock.sirproject.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Verification_Code_Activity extends AppCompatActivity {


    ImageView imgback,imgsucess;

    TextView txtverification,txtverificationdes,txtcode,txtresendcode;

    Button btnverify;

    EditText edv1,edv2,edv3,edv4;

   // String verify_code="";
    String v1="",v2="",v3="",v4="";
    String mail="",verification_code="",pwd="";

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification__code_);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);
        imgback = findViewById(R.id.imgback);
        imgsucess = findViewById(R.id.imgsucess);
        txtverification = findViewById(R.id.txtverification);
        txtverificationdes = findViewById(R.id.txtverificationdes);
        txtcode = findViewById(R.id.txtcode);
        txtresendcode = findViewById(R.id.txtresendcode);
        btnverify = findViewById(R.id.btnverify);
        edv1 = findViewById(R.id.edv1);
        edv2 = findViewById(R.id.edv2);
        edv3 = findViewById(R.id.edv3);
        edv4 = findViewById(R.id.edv4);

        fonts();

        if (getIntent().hasExtra("mail") &&getIntent().hasExtra("verification_code") )
        {
            mail = getIntent().getStringExtra("mail");
            verification_code = getIntent().getStringExtra("verification_code");
            pwd = getIntent().getStringExtra("pwd");

            edv1.setText(Character.toString(verification_code.charAt(0)));
            edv2.setText(Character.toString(verification_code.charAt(1)));
            edv3.setText(Character.toString(verification_code.charAt(2)));
            edv4.setText(Character.toString(verification_code.charAt(3)));
            imgsucess.setVisibility(View.VISIBLE);
            imgsucess.setImageResource(R.drawable.code_success);
            edv4.requestFocus();

        }


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edv1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edv1.getText().toString().length() == 1)     //size as per your requirement
                {
                    edv2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });

        edv2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edv2.getText().toString().length() == 1)     //size as per your requirement
                {
                    edv3.requestFocus();
                } else if (edv2.getText().toString().length() == 0) {
                    edv1.requestFocus();

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });

        edv3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edv3.getText().toString().length() == 1)     //size as per your requirement
                {
                    edv4.requestFocus();
                } else if (edv3.getText().toString().length() == 0) {
                    edv2.requestFocus();

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }


        });

        edv4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(edv4.getText().toString().length()==1){

                    v1=edv1.getText().toString();
                    v2=edv2.getText().toString();
                    v3=edv3.getText().toString();
                    v4=edv4.getText().toString();
                    verification_code = v1+v2+v3+v4;

                    Log.e("codelength", String.valueOf(verification_code.length()));
                        if(verification_code.length()==4){
                            if (verification_code.equals(verification_code))
                            {
                                imgsucess.setVisibility(View.VISIBLE);
                                imgsucess.setImageResource(R.drawable.code_success);
                            }
                            else
                            {
                                imgsucess.setVisibility(View.VISIBLE);
                                imgsucess.setImageResource(R.drawable.code_error);
                            }
                        }
                }

                else if (edv4.getText().toString().length() == 0) {
                    edv3.requestFocus();
                    imgsucess.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Verification_code();

            }
        });

        txtresendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ResendCode();
            }
        });

    }


    public void fonts()
    {
        txtverification.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Bold.otf"));
        txtverificationdes.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtresendcode.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtcode.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        edv1.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Regular.ttf"));
        edv2.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Regular.ttf"));
        edv3.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Regular.ttf"));
        edv4.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Regular.ttf"));
        btnverify.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));

    }

    public void Verification_code()
    {
        String et1=edv1.getText().toString();
        String et2=edv2.getText().toString();
        String et3=edv3.getText().toString();
        String et4=edv4.getText().toString();
        if(et1.isEmpty()){
            edv1.setError("Required");
            edv1.requestFocus();
        }
        else if(et2.isEmpty()){
            edv2.setError("Required");
            edv2.requestFocus();

        }
        else if(et3.isEmpty()){
            edv3.setError("Required");
            edv3.requestFocus();

        }
        else if(et4.isEmpty()){
            edv4.setError("Required");
            edv4.requestFocus();

        }
        else if (mail.isEmpty()){
            Toast.makeText(getApplicationContext(),"Email Is Required",Toast.LENGTH_SHORT).show();
        }
        else{
            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);

            final SharedPreferences pref = getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);
            RequestParams params = new RequestParams();
            params.add("email",mail);
            params.add("code",verification_code);

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(Constant.BASE_URL + "user/verification", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    String response = new String(responseBody);
                    Log.e("verificationresponse",response);
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("true")){

                            JSONObject data = jsonObject.getJSONObject("data");

                            String access_token = data.getString("access_token");
                            pref.edit().putString(Constant.ACCESS_TOKEN,access_token).commit();

                            Constant.SuccessToast(Verification_Code_Activity.this,jsonObject.getString("message"));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Verification_Code_Activity.this, Go_Premium_Activity.class);
                                    intent.putExtra("signup","signup");
                                    intent.putExtra("mail",mail);
                                    intent.putExtra("pwd",pwd);
                                    startActivity(intent);
                                }
                            },1500);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);

                    String response = new String(responseBody);
                    Log.e("verificationresponseF",response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false")){

                            Constant.ErrorToast(Verification_Code_Activity.this,jsonObject.getString("message"));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

    }

    private void ResendCode(){

        if (mail.isEmpty()){
            Constant.ErrorToast(Verification_Code_Activity.this,"Email Is Required");
        }
        else{

            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);

            RequestParams params = new RequestParams();
            params.add("email",mail);

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(Constant.BASE_URL + "user/resendCode", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    String response = new String(responseBody);
                    Log.e("resendcode",response);
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("true")){

                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject profile = data.getJSONObject("profile");

                            verification_code = profile.getString("verification_code");
                            edv1.setText(Character.toString(verification_code.charAt(0)));
                            edv2.setText(Character.toString(verification_code.charAt(1)));
                            edv3.setText(Character.toString(verification_code.charAt(2)));
                            edv4.setText(Character.toString(verification_code.charAt(3)));
                            Constant.SuccessToast(Verification_Code_Activity.this,jsonObject.getString("message"));


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);

                    String response = new String(responseBody);
                    Log.e("resendcodeF",response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false")){

                            //Toast.makeText(getApplicationContext(),jsonObject.getString("Something went wrong"),Toast.LENGTH_SHORT).show();
                            Constant.ErrorToast(Verification_Code_Activity.this,jsonObject.getString("message"));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

}
