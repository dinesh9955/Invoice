package com.receipt.invoice.stock.sirproject.ForgotResetPassword;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.SignupSignin.Signin_Activity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ResetPassword_Activity extends BaseActivity {




    ImageView imgback;
    TextView txtreset,txtresestdes,txtsuccess,txtstart,txtsigninhere;
    EditText edpassword,edreentertpassword;
    Button btnresestpassword;

    String v1,v2;
    String user_id;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);
        imgback = findViewById(R.id.imgback);
        txtreset = findViewById(R.id.txtreset);
        txtresestdes = findViewById(R.id.txtresestdes);
        txtsuccess = findViewById(R.id.txtsuccess);
        txtstart = findViewById(R.id.txtstart);
        txtsigninhere = findViewById(R.id.txtsigninhere);
        edpassword = findViewById(R.id.edpassword);
        edreentertpassword = findViewById(R.id.edreentertpassword);
        btnresestpassword = findViewById(R.id.btnresestpassword);

        fonts();

       // txtsuccess.setVisibility(View.GONE);

        btnresestpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                v1=edpassword.getText().toString();
                v2=edreentertpassword.getText().toString();

                if (!v2.equals(v1))
                {
                   /* edreentertpassword.setError("Password not matched");
                    edreentertpassword.requestFocus();*/
                    txtsuccess.setVisibility(View.VISIBLE);
                    txtsuccess.setTextColor(Color.RED);
                   txtsuccess.setText("Password not matched");

                }
                else {
                    txtsuccess.setVisibility(View.VISIBLE);
                    txtsuccess.setTextColor(Color.GREEN);
                    txtsuccess.setText("You have reset your password sucessfully.");
                    ResetPassword();
                }
            }
        });

        txtsigninhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPassword_Activity.this, Signin_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent().hasExtra("usid"))
        {
            user_id = getIntent().getStringExtra("usid");
            Log.e("rec_user_id",user_id);
        }

    }

    public void fonts()
    {
        txtreset.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Bold.otf"));
        txtresestdes.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtsuccess.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtstart.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtsigninhere.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));
        edpassword.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        edreentertpassword.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        btnresestpassword.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));

    }

    public void ResetPassword()
    {
        String password1 = edpassword.getText().toString();
        String password2= edreentertpassword.getText().toString();

        if(TextUtils.isEmpty(edpassword.getText()))
        {
            edpassword.setError("Field is required");
            edpassword.requestFocus();
        }
        else if(TextUtils.isEmpty(edreentertpassword.getText()))
        {
            edreentertpassword.setError("Field is required");
            edreentertpassword.requestFocus();
        }
        else if (password1.equals(password2)){

            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);

            RequestParams params = new RequestParams();
            params.add("user_id",user_id);
            params.add("password",password2);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.post(AllSirApi.BASE_URL + "user/resetPassword", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    String response = new String(responseBody);
                    Log.e("resetresponse",response);
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("true")){

                            Constant.SuccessToast(ResetPassword_Activity.this,jsonObject.getString("message"));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ResetPassword_Activity.this,Signin_Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    Log.e("resetresponseF",response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("false")){

                            //Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            Constant.ErrorToast(ResetPassword_Activity.this,jsonObject.getString("message"));

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }



}
