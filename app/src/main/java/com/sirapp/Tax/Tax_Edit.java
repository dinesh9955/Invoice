package com.sirapp.Tax;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Constant.Constant;
import com.sirapp.Home.GoProActivity;
import com.sirapp.Utils.Utility;
import com.sirapp.API.AllSirApi;
import com.sirapp.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Tax_Edit extends BaseActivity {
    EditText taxname, taxpercent;
    TextView taxcalculation;
    RadioButton rinclusive, rexclusive;
    RadioGroup radiogroup;
    Button add;
    AwesomeSpinner selectcompany;
    String selectedCompanyId="";
    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    ArrayList<String> cnames=new ArrayList<>();
    ArrayList<String> cids=new ArrayList<>();

    String tname="";
    String trate="";
    String taxid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax__edit);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Constant.toolbar(Tax_Edit.this,getString(R.string.header_edit_tax));
//        Constant.bottomNav(Tax_Edit.this,-1);

        FindByIds();
        setFonts();
        setListeners();
        companyget();
        selectcompany.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        selectcompany.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));

        if (getIntent().hasExtra("edit")){
            tname = getIntent().getStringExtra("name");
            trate = getIntent().getStringExtra("rate");
            selectedCompanyId = getIntent().getStringExtra("companyid");
            taxid = getIntent().getStringExtra("taxid");

            taxname.setText(tname);
            taxpercent.setText(trate);

        }

    }
    private void FindByIds() {
        taxcalculation = findViewById(R.id.taxcalculation);
        taxname = findViewById(R.id.taxname);
        taxpercent = findViewById(R.id.taxpercent);
        rinclusive = findViewById(R.id.rinclusive);
        rexclusive = findViewById(R.id.rexclusive);
        radiogroup = findViewById(R.id.radiogroup);
        add = findViewById(R.id.add);
        selectcompany = findViewById(R.id.selectcompany);
        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);
    }

    private void setFonts() {
        taxname.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        taxpercent.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        add.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/Ubuntu-Medium.ttf"));
        taxcalculation.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Bold.otf"));
        rinclusive.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Medium.otf"));
        rexclusive.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Medium.otf"));

    }

    public void companyget()
    {

        final SharedPreferences preferences = getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);
        String token = Constant.GetSharedPreferences(Tax_Edit.this,Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);
        RequestParams params = new RequestParams();
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL+"company/listing", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("responsecompany",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray company = data.getJSONArray("company");
                        if (company.length()>0) {
                            for (int i = 0; i < company.length(); i++) {
                                JSONObject item = company.getJSONObject(i);
                                String company_id = item.getString("company_id");
                                String company_name = item.getString("name");

                                cnames.add(company_name);
                                cids.add(company_id);

                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(Tax_Edit.this, android.R.layout.simple_spinner_item,cnames);
                                selectcompany.setAdapter(namesadapter);


                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(responseBody!=null){
                    String response = new String(responseBody);
                    Log.e("responsecompanyF",response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("false"))
                        {
                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setListeners() {

        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                Log.e("selectedCompany",selectedCompanyId);

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTax();
            }
        });
    }
    private void EditTax(){

        String name=taxname.getText().toString();
        String rate=taxpercent.getText().toString();

        if (name.isEmpty()){
            taxname.setError("Required");
            taxname.requestFocus();
        }
        else if (rate.isEmpty()){
            taxpercent.setError("Required");
            taxpercent.requestFocus();
        }
        else if (selectedCompanyId.equals("")){
            Constant.ErrorToast(Tax_Edit.this,getString(R.string.select_company));
        }
        else{
            Utility.hideKeypad(Tax_Edit.this);
            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.add("tax_id",taxid);
            params.add("name",name);
            params.add("rate",rate.replace(".00","") );
            params.add("company_id",selectedCompanyId);


            String token = Constant.GetSharedPreferences(Tax_Edit.this,Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.addHeader("Access-Token",token);
            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL + "tax/update", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    String response = new String(responseBody);
                    Log.e("addtaxResp",response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("true")){
                            Constant.SuccessToast(Tax_Edit.this, getString(R.string.tax_TaxUpdated));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Tax_Edit.this,Tax_Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            },1000);

                        }

                        if (status.equals("false")){
                            if( jsonObject.has("code")){
                                String code = jsonObject.getString("code");

                                if(code.equalsIgnoreCase("subscription")){
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(Tax_Edit.this, GoProActivity.class);
                                            startActivity(intent);
                                        }
                                    }, 1000);
                                }
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    if(responseBody!=null) {
                        String response = new String(responseBody);
                        Log.e("addtaxRespF",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("false")){

                                Constant.ErrorToast(Tax_Edit.this,jsonObject.getString("message"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                       // Constant.ErrorToast(Tax_Edit.this,"Something went wrong, try again!");
                    }
                }
            });

        }

    }
}
