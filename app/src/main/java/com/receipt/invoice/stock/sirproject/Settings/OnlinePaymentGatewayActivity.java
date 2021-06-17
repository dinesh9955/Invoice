package com.receipt.invoice.stock.sirproject.Settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.ThankYouNote.ThankYouNoteActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class OnlinePaymentGatewayActivity extends BaseActivity {

    private static final String TAG = "OnlinePaymentGateway";

    ImageView imageViewStripeUp, imageViewPaypalUp;

    TextView textViewStripe, textViewPaypal;

    Button buttonStripe, buttonPaypal;

    AwesomeSpinner selectcompany;

    ArrayList<String> cids = new ArrayList<>();
    ArrayList<String> cnames = new ArrayList<>();
    ArrayList<String> cidsLogo = new ArrayList<>();

    ArrayList<String> stringPaypal = new ArrayList<>();
    ArrayList<String> stringStripe = new ArrayList<>();



    String selectedCompanyId = "";
    String selectedCompanyName = "";
    String selectedCompanyImage = "";

    String selectedStringPaypal = "";
    String selectedStringStripe = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_online_payment_gateway);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);

        TextView textViewDone = toolbar.findViewById(R.id.textViewDone);
        textViewDone.setVisibility(View.GONE);

        titleView.setText(getString(R.string.header_online_payment_gateway));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

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

        selectcompany = findViewById(R.id.selectcompany);

        imageViewStripeUp = findViewById(R.id.imageViewUp1);
        imageViewPaypalUp = findViewById(R.id.imageViewUp2);

        textViewStripe = findViewById(R.id.text_setting);
        textViewPaypal = findViewById(R.id.text_setting2);

        buttonStripe = findViewById(R.id.buttonRight1);
        buttonPaypal = findViewById(R.id.buttonRight2);

        imageViewStripeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewStripe.getVisibility() == View.GONE){
                    textViewStripe.setVisibility(View.VISIBLE);
                    imageViewStripeUp.setImageResource(R.drawable.arrow_down);
                }else{
                    textViewStripe.setVisibility(View.GONE);
                    imageViewStripeUp.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        imageViewPaypalUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewPaypal.getVisibility() == View.GONE){
                    textViewPaypal.setVisibility(View.VISIBLE);
                    imageViewPaypalUp.setImageResource(R.drawable.arrow_down);
                }else{
                    textViewPaypal.setVisibility(View.GONE);
                    imageViewPaypalUp.setImageResource(R.drawable.arrow_up);
                }
            }
        });



        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                selectedCompanyName = cnames.get(position);
                selectedCompanyImage = cidsLogo.get(position);

                selectedStringPaypal = stringPaypal.get(position);
                selectedStringStripe = stringStripe.get(position);

                if(selectedStringPaypal.equalsIgnoreCase("1")){
                    buttonPaypal.setText("Edit");
                }else{
                    buttonPaypal.setText("Setup");
                }

                if(selectedStringStripe.equalsIgnoreCase("1")){
                    buttonStripe.setText("Edit");
                }else{
                    buttonStripe.setText("Setup");
                }

            }
        });



        buttonPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectedCompanyId.equalsIgnoreCase("")){
                    Intent intent = new Intent(OnlinePaymentGatewayActivity.this, PayPalActivity.class);
                    intent.putExtra("companyID", selectedCompanyId);
                    startActivityForResult(intent, 122);
                }
            }
        });


        buttonStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectedCompanyId.equalsIgnoreCase("")){
                    Intent intent = new Intent(OnlinePaymentGatewayActivity.this, StripeWebActivity.class);
//                    intent.putExtra("companyID", selectedCompanyId);
//                    intent.putExtra("companyName", selectedCompanyName);
//                    intent.putExtra("companyImage", selectedCompanyImage);
                    startActivityForResult(intent, 123);
                }
            }
        });



        companyget();

    }



    private void companyget() {

        cnames.clear();
        cids.clear();
        String token = Constant.GetSharedPreferences(OnlinePaymentGatewayActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "company/listing", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("responsecompany", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray company = data.getJSONArray("company");
                        String company_image_path = data.getString("company_image_path");

                        String company_id= "", company_name= "", company_address= "", company_contact= "", company_email= "",
                                company_website= "", payment_bank_name= "", payment_currency= "", payment_iban= "", payment_swift_bic= "", logo = "";
                        if (company.length() > 0) {
                            for (int i = 0; i < company.length(); i++) {
                                JSONObject item = company.getJSONObject(i);
                                company_id = item.getString("company_id");
                                company_name = item.getString("name");
                                company_address = item.getString("address");
                                company_contact = item.getString("phone_number");
                                company_email = item.getString("email");
                                company_website = item.getString("website");
                                logo = item.getString("logo");

                                payment_bank_name = item.getString("payment_bank_name");
                                payment_currency = item.getString("payment_currency");
                                payment_iban = item.getString("payment_iban");
                                payment_swift_bic = item.getString("payment_swift_bic");
                                String colorCode = item.getString("color");
                                Log.e("CompanyId",company_id);

                                String paypal = item.getString("paypal");
                                String stripe = item.getString("stripe");

                                stringPaypal.add(paypal);
                                stringStripe.add(stripe);
                                cnames.add(company_name);
                                cids.add(company_id);
                                cidsLogo.add(company_image_path+logo);


                            }
                        }

                        ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(OnlinePaymentGatewayActivity.this, android.R.layout.simple_spinner_item, cnames);
                        selectcompany.setAdapter(namesadapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    Log.e("responsecompanyF", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 122){
            if(resultCode == RESULT_OK){
                companyget();
            }
        }

        if(requestCode == 123){
            if(resultCode == RESULT_OK){
                String responseCode = data.getStringExtra("responseCode");
                Log.e(TAG, "responseCode:: "+responseCode);
                callMethod(responseCode);

            }
        }
    }




    private void callMethod(String responseCode) {
        final ProgressDialog progressDialog = new ProgressDialog(OnlinePaymentGatewayActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.add("key", "STRIPE");
        params.add("company_id", selectedCompanyId);
        params.add("token", responseCode);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        String token = Constant.GetSharedPreferences(OnlinePaymentGatewayActivity.this, Constant.ACCESS_TOKEN);
        client.addHeader("Access-Token", token);

        client.post(AllSirApi.BASE_URL_PAYMENT+ "settings/add", params, new AsyncHttpResponseHandler() {
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
                        Constant.SuccessToast(OnlinePaymentGatewayActivity.this, message);

                        companyget();

//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                finish();
//                            }
//                        }, 500);

                    }else{
                        Constant.ErrorToast(OnlinePaymentGatewayActivity.this, message);
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
                            Constant.ErrorToast(OnlinePaymentGatewayActivity.this, message);

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


    @Override
    protected void onResume() {
        super.onResume();
       // companyget();
    }
}
