package com.receipt.invoice.stock.sirproject.Settings;

import android.os.Bundle;
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
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.ThankYouNote.ThankYouNoteActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class OnlinePaymentGatewayActivity extends AppCompatActivity {

    private static final String TAG = "OnlinePaymentGatewayy";

    ImageView imageViewStripeUp, imageViewPaypalUp;

    TextView textViewStripe, textViewPaypal;

    Button buttonStripe, buttonPaypal;

    AwesomeSpinner selectcompany;

    ArrayList<String> cids = new ArrayList<>();
    ArrayList<String> cnames = new ArrayList<>();

    String selectedCompanyId = "";

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

        titleView.setText("Online Payment Gateway");

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
            }
        });


        companyget();

    }



    private void companyget() {

        cnames.clear();
        cids.clear();
        String token = Constant.GetSharedPreferences(OnlinePaymentGatewayActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "company/listing", new AsyncHttpResponseHandler() {
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

                                cnames.add(company_name);
                                cids.add(company_id);



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



}
