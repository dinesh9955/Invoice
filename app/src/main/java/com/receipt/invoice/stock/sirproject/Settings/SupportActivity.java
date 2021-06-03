package com.receipt.invoice.stock.sirproject.Settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Invoice.InvoiceActivity;
import com.receipt.invoice.stock.sirproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SupportActivity extends AppCompatActivity {
    private static final String TAG = "SupportActivity" ;

    EditText editTextEmail, editTextMessage;
    Button buttonPress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_support);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Constant.toolbar(SupportActivity.this, "Support");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        editTextEmail = findViewById(R.id.login_email);
        editTextMessage = findViewById(R.id.login_message);
        buttonPress = findViewById(R.id.createinvoice);


        buttonPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextEmail.getText().toString().equalsIgnoreCase("")){
                    Constant.ErrorToast(SupportActivity.this, "Enter Email");
                }else if(editTextMessage.getText().toString().equalsIgnoreCase("")){
                    Constant.ErrorToast(SupportActivity.this, "Enter Message");
                }else{

                    callMethod();
                }
            }
        });

    }



    private void callMethod() {
        final ProgressDialog progressDialog = new ProgressDialog(SupportActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.add("user_email", editTextEmail.getText().toString());
        params.add("user_message", editTextMessage.getText().toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constant.BASE_URL_SUPPORT + "support.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                progressDialog.dismiss();

                Log.e("Create Invoicedata", response);
                try {
                    Log.e("Create Invoicedata", response);

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("1")) {
                        Constant.SuccessToast(SupportActivity.this, message);
                        editTextEmail.setText("");
                        editTextMessage.setText("");
                    }else{
                        Constant.ErrorToast(SupportActivity.this, message);
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
                            Constant.ErrorToast(SupportActivity.this, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Constant.ErrorToast(SupportActivity.this, "Something went wrong, try again!");
                }
            }
        });
    }
}
