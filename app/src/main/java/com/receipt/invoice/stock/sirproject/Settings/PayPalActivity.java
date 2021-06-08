package com.receipt.invoice.stock.sirproject.Settings;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PayPalActivity extends BaseActivity {

    private static final String TAG = "PayPalActivity";
    EditText editTextEmail, editTextConfirmEmail;

    Button buttonSave;

    RadioButton radioButton1, radioButton2;

    String optionType = "";

    String companyID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paypal);

     //   overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);

        TextView textViewDone = toolbar.findViewById(R.id.textViewDone);
        textViewDone.setVisibility(View.GONE);

        titleView.setText("PayPal");


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        editTextEmail = findViewById(R.id.login_email);
        editTextConfirmEmail = findViewById(R.id.login_email_confirm);

        radioButton1 = findViewById(R.id.radioButton_1);
        radioButton2 = findViewById(R.id.radioButton_2);

        buttonSave = findViewById(R.id.createinvoice);



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        companyID = getIntent().getExtras().getString("companyID");


        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton1.setChecked(true);
                radioButton2.setChecked(false);
                optionType = "Standard";
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton1.setChecked(false);
                radioButton2.setChecked(true);
                optionType = "Business";
            }
        });




        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(editTextEmail.getText().toString().equalsIgnoreCase("")){
                   Constant.ErrorToast(PayPalActivity.this, "Enter Email");
               } else if(editTextConfirmEmail.getText().toString().equalsIgnoreCase("")){
                   Constant.ErrorToast(PayPalActivity.this, "Enter Confirm Email");
               } else if(!editTextEmail.getText().toString().equals(editTextConfirmEmail.getText().toString())){
                   Constant.ErrorToast(PayPalActivity.this, "Confirm Email does not match");
               } else if(optionType.equalsIgnoreCase("")){
                   Constant.ErrorToast(PayPalActivity.this, "Please select Payment Options");
               }else{
                   callMethod();
               }
            }
        });


    }



    private void callMethod() {
        final ProgressDialog progressDialog = new ProgressDialog(PayPalActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.add("key", "PAYPAL");
        params.add("company_id", companyID);
        params.add("paypal_email", editTextEmail.getText().toString());
        params.add("email_type", optionType);

        AsyncHttpClient client = new AsyncHttpClient();
        String token = Constant.GetSharedPreferences(PayPalActivity.this, Constant.ACCESS_TOKEN);
        client.addHeader("Access-Token", token);

        client.post(Constant.BASE_URL_PAYMENT + "settings/add", params, new AsyncHttpResponseHandler() {
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
                        Constant.SuccessToast(PayPalActivity.this, message);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);

                    }else{
                        Constant.ErrorToast(PayPalActivity.this, message);
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
                            Constant.ErrorToast(PayPalActivity.this, message);

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



}
