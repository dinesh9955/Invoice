package com.sir.Settings;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sir.API.AllSirApi;
import com.sir.Base.BaseActivity;
import com.sir.Constant.Constant;
import com.sir.R;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SupportActivity extends BaseActivity {
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

        Constant.toolbar(SupportActivity.this, getString(R.string.header_support));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        editTextEmail = findViewById(R.id.login_email);
        editTextMessage = findViewById(R.id.login_message);
        buttonPress = findViewById(R.id.createinvoice);

        String email = Constant.GetSharedPreferences(SupportActivity.this, Constant.EMAIL);
        editTextEmail.setText(""+email);

        buttonPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextEmail.getText().toString().equalsIgnoreCase("")){
                    Constant.ErrorToast(SupportActivity.this, getString(R.string.setting_enter_email));
                }else if(editTextMessage.getText().toString().equalsIgnoreCase("")){
                    Constant.ErrorToast(SupportActivity.this, getString(R.string.setting_enter_message));
                }else{

                //    callMethod();
                    callMethod2();
                }
            }
        });

    }



    private void callMethod() {
        final ProgressDialog progressDialog = new ProgressDialog(SupportActivity.this);
        progressDialog.setMessage(getString(R.string.dialog_Please_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.add("user_email", editTextEmail.getText().toString());
        params.add("user_message", editTextMessage.getText().toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL_SUPPORT + "support.php", params, new AsyncHttpResponseHandler() {
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
//                        editTextEmail.setText("");
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
                    ///Constant.ErrorToast(SupportActivity.this, "Something went wrong, try again!");
                }
            }
        });
    }




    private void callMethod2() {
        final ProgressDialog progressDialog = new ProgressDialog(SupportActivity.this);
        progressDialog.setMessage(getString(R.string.dialog_Please_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.add("user_email", editTextEmail.getText().toString());
        params.add("user_message", editTextMessage.getText().toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        String token = Constant.GetSharedPreferences(SupportActivity.this, Constant.ACCESS_TOKEN);
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "support/add", params, new AsyncHttpResponseHandler() {
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
                    if (status.equals("true")) {
                        Constant.SuccessToast(SupportActivity.this, message);
//                        editTextEmail.setText("");
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
                        if (status.equals("false")) {
                            Constant.ErrorToast(SupportActivity.this, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ///Constant.ErrorToast(SupportActivity.this, "Something went wrong, try again!");
                }
            }
        });
    }


}
