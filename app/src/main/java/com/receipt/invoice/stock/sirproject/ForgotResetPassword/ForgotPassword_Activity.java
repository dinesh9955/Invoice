package com.receipt.invoice.stock.sirproject.ForgotResetPassword;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgotPassword_Activity extends BaseActivity {


    ImageView imgback,imgsucess;
    TextView txtforgot,txtforgotdes,txtresend,txtcode;
    EditText edemail,edv1,edv2,edv3,edv4;
    Button btnverify;
    String verifiction_code="",verify_code="";
    String v1="",v2="",v3="",v4="";
    String user_id="";

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    LinearLayout L1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        L1 = findViewById(R.id.L1);
        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);
        imgback = findViewById(R.id.imgback);
        imgsucess = findViewById(R.id.imgsucess);
        txtforgot = findViewById(R.id.txtforgot);
        txtforgotdes = findViewById(R.id.txtforgotdes);
        txtresend = findViewById(R.id.txtresend);
        txtcode = findViewById(R.id.txtcode);
        edemail = findViewById(R.id.edemail);
        edv1 = findViewById(R.id.edv1);
        edv2 = findViewById(R.id.edv2);
        edv3 = findViewById(R.id.edv3);
        edv4 = findViewById(R.id.edv4);
        btnverify = findViewById(R.id.btnverify);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPassword();
            }
        });

        txtresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPassword();
            }
        });

        edv1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                v1=edv1.getText().toString();
                if(v1.length()>0){
                    edv2.requestFocus();
                    //edv2.setSelection(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edv2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                v2=edv2.getText().toString();
                if(v2.length()>0){
                    edv3.requestFocus();
                }
                else if(v2.length()==0)
                {
                    edv1.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edv3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                v3=edv3.getText().toString();
                if(v3.length()>0){
                    edv4.requestFocus();
                }
                else if(v3.length()==0)
                {
                    edv2.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edv4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String et4=edv4.getText().toString();

                v4=edv4.getText().toString();
                if(v4.length()>0){

                    verify_code = v1+v2+v3+v4;

                    if(verify_code.length()==4){
                        if (verifiction_code.equals(verify_code))
                        {
                            imgsucess.setVisibility(View.VISIBLE);
                            imgsucess.setImageResource(R.drawable.code_success);
                            Intent intent = new Intent(ForgotPassword_Activity.this,ResetPassword_Activity.class);
                            intent.putExtra("usid",user_id);
                            Log.e("intusid",user_id);
                            startActivity(intent);
                        }
                        else
                        {
                            imgsucess.setVisibility(View.VISIBLE);
                            imgsucess.setImageResource(R.drawable.code_error);
                        }
                    }
                }
                else if(v4.length()==0)
                {
                    edv3.requestFocus();
                    imgsucess.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });

        fonts();

    }


    public void fonts()
    {
        txtforgot.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Bold.otf"));
        txtforgotdes.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtresend.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtcode.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        edemail.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        edv1.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        edv2.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        edv3.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        edv4.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        btnverify.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));

    }

    public void ForgotPassword()
    {

        if (TextUtils.isEmpty(edemail.getText())) {
            edemail.setError("Field is required");
            edemail.requestFocus();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(edemail.getText().toString()).matches())
        {
            edemail.setError("Invalid Pattern");
            edemail.requestFocus();
        }
        else
        {

            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);

            String email = edemail.getText().toString();
            RequestParams params = new RequestParams();
            params.add("email",email);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL + "user/forgotPassword", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    String respnose = new String(responseBody);
                    Log.e("forgotresponse",respnose);
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(respnose);
                        String status = jsonObject.getString("status");

                        if (status.equals("true"))
                        {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject profile = data.getJSONObject("profile");
                            user_id = profile.getString("user_id");
                            Log.e("verification_codee",verifiction_code);
                            Log.e("forgotUserid",user_id);
                            Constant.SuccessToast(ForgotPassword_Activity.this,jsonObject.getString("message"));
                            verifiction_code = profile.getString("verification_code");
                            edv1.setText(Character.toString(verifiction_code.charAt(0)));
                            edv2.setText(Character.toString(verifiction_code.charAt(1)));
                            edv3.setText(Character.toString(verifiction_code.charAt(2)));
                            edv4.setText(Character.toString(verifiction_code.charAt(3)));
                            imgsucess.setVisibility(View.VISIBLE);
                            imgsucess.setImageResource(R.drawable.code_success);



                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    String respnose = new String(responseBody);
                    Log.e("forgotresponseF",respnose);
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(respnose);
                        String status = jsonObject.getString("status");

                        if (status.equals("false"))
                        {
                            //Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            Constant.ErrorToast(ForgotPassword_Activity.this,jsonObject.getString("message"));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            });

        }
    }

}
