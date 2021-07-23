package com.sirapp.Settings;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Constant.Constant;
import com.sirapp.InvoiceReminder.InvoiceReminderActivity;
import com.sirapp.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class OnlinePaymentGatewayActivity extends BaseActivity {

    private static final String TAG = "OnlinePaymentGateway";

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    ImageView imageViewStripeUp, imageViewPaypalUp;

    TextView textViewStripe, textViewPaypal;

    Button buttonStripe, buttonPaypal;

    int payPalCode = 0;
    int stripeCode = 0;

    Button selectcompany1;

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


        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);

        selectcompany1 = findViewById(R.id.selectcompany2);

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



//        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
//            @Override
//            public void onItemSelected(int position, String itemAtPosition) {
//                selectedCompanyId = cids.get(position);
//                selectedCompanyName = cnames.get(position);
//                selectedCompanyImage = cidsLogo.get(position);
//
//                selectedStringPaypal = stringPaypal.get(position);
//                selectedStringStripe = stringStripe.get(position);
//
//                if(selectedStringPaypal.equalsIgnoreCase("1")){
//                    buttonPaypal.setText(getString(R.string.tax_edit));
//                }else{
//                    buttonPaypal.setText(getString(R.string.setting_Setup));
//                }
//
//                if(selectedStringStripe.equalsIgnoreCase("1")){
//                    buttonStripe.setText(getString(R.string.tax_edit));
//                }else{
//                    buttonStripe.setText(getString(R.string.setting_Setup));
//                }
//
//            }
//        });





        selectcompany1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView mRecyclerView;
                MenuAdapter mAdapter;

                final Dialog mybuilder = new Dialog(OnlinePaymentGatewayActivity.this);
                mybuilder.setContentView(R.layout.select_company_dialog_3);


                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(OnlinePaymentGatewayActivity.this, LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter(cnames, mybuilder);
                mRecyclerView.setAdapter(mAdapter);

                mybuilder.show();
                mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                Window window = mybuilder.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(R.color.transparent);
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





    }



    private void companyget() {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);

        cnames.clear();
        cids.clear();
        cidsLogo.clear();
        stringPaypal.clear();
        stringStripe.clear();

        String token = Constant.GetSharedPreferences(OnlinePaymentGatewayActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        RequestParams params = new RequestParams();
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "company/listing", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

                String response = new String(responseBody);
                Log.e(TAG, "responsecompanyQQ "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray company = data.getJSONArray("company");
                        String company_image_path = data.getString("company_image_path");


                        if (company.length() > 0) {
                            for (int i = 0; i < company.length(); i++) {
                                JSONObject item = company.getJSONObject(i);
                                String company_id = item.getString("company_id");
                                String company_name = item.getString("name");
                                String logo = item.getString("logo");
                                String paypal = item.getString("paypal");
                                String stripe = item.getString("stripe");

                                Log.e(TAG, "paypalQQ "+ paypal);
                                Log.e(TAG, "stripeQQ "+ stripe);



                                stringPaypal.add(paypal);
                                stringStripe.add(stripe);
                                cnames.add(company_name);
                                cids.add(company_id);
                                cidsLogo.add(company_image_path+logo);

                                Log.e(TAG, "payPalCodeQQ "+ payPalCode);

//                                if(payPalCode == 122){
//                                    if(paypal.equalsIgnoreCase("1")){
//                                        buttonPaypal.setText(getString(R.string.tax_edit));
//                                    }else if(paypal.equalsIgnoreCase("0")){
//                                        buttonPaypal.setText(getString(R.string.setting_Setup));
//                                    }
//
////                                    if(stripe.equalsIgnoreCase("1")){
////                                        buttonStripe.setText(getString(R.string.tax_edit));
////                                    }else if(stripe.equalsIgnoreCase("0")){
////                                        buttonStripe.setText(getString(R.string.setting_Setup));
////                                    }
//                                }


//                                if(stripeCode == 123){
//                                    if(paypal.equalsIgnoreCase("1")){
//                                        buttonPaypal.setText(getString(R.string.tax_edit));
//                                    }else{
//                                        buttonPaypal.setText(getString(R.string.setting_Setup));
//                                    }
//
//                                    if(stripe.equalsIgnoreCase("1")){
//                                        buttonStripe.setText(getString(R.string.tax_edit));
//                                    }else{
//                                        buttonStripe.setText(getString(R.string.setting_Setup));
//                                    }
//                                }


                            }
                        }

//                        ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(OnlinePaymentGatewayActivity.this, android.R.layout.simple_spinner_item, cnames);
//                        selectcompany.setAdapter(namesadapter);


                        if(company.length() == 1){
                            selectcompany1.setText(cnames.get(0));
                            selectedCompanyId = cids.get(0);
                            selectedCompanyName = cnames.get(0);
                            selectedCompanyImage = cidsLogo.get(0);

                            selectedStringPaypal = stringPaypal.get(0);
                            selectedStringStripe = stringStripe.get(0);

                            if(selectedStringPaypal.equalsIgnoreCase("1")){
                                buttonPaypal.setText(getString(R.string.tax_edit));
                            }else{
                                buttonPaypal.setText(getString(R.string.setting_Setup));
                            }

                            if(selectedStringStripe.equalsIgnoreCase("1")){
                                buttonStripe.setText(getString(R.string.tax_edit));
                            }else{
                                buttonStripe.setText(getString(R.string.setting_Setup));
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


    private void CompanyInformation(String selectedCompanyId) {

//        cnames.clear();
//        cids.clear();
//        cidsLogo.clear();
//        stringPaypal.clear();
//        stringStripe.clear();


        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);
//        params.add("product", "1");
//        params.add("service", "1");
//        params.add("customer", "1");
//        params.add("tax", "1");
//        params.add("estimate", "1");
//        params.add("warehouse", "1");


        String token = Constant.GetSharedPreferences(this, Constant.ACCESS_TOKEN);
        Log.e("token", token);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "company/info", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("Company_Information", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String company_image_path = data.getString("company_image_path");

                        JSONArray company = data.getJSONArray("company");

                        for (int i = 0; i < company.length(); i++) {
                            JSONObject item = company.getJSONObject(i);

                            String company_id = item.getString("company_id");
                            String company_name = item.getString("name");
                            String logo = item.getString("logo");
                            String paypal = item.getString("paypal");
                            String stripe = item.getString("stripe");

                            Log.e(TAG, "paypalQQ "+ paypal);
                            Log.e(TAG, "stripeQQ "+ stripe);


//                            stringPaypal.add(paypal);
//                            stringStripe.add(stripe);
//                            cnames.add(company_name);
//                            cids.add(company_id);
//                            cidsLogo.add(company_image_path+logo);
//
//
//                            Log.e(TAG, "paypalQQ "+ paypal);
//                            Log.e(TAG, "stripeQQ "+ stripe);


                            if(payPalCode == 122){
                                if(paypal.equalsIgnoreCase("1")){
                                    buttonPaypal.setText(getString(R.string.tax_edit));
                                }else if(paypal.equalsIgnoreCase("0")){
                                    buttonPaypal.setText(getString(R.string.setting_Setup));
                                }

                                    if(stripe.equalsIgnoreCase("1")){
                                        buttonStripe.setText(getString(R.string.tax_edit));
                                    }else if(stripe.equalsIgnoreCase("0")){
                                        buttonStripe.setText(getString(R.string.setting_Setup));
                                    }
                            }


                            if(stripeCode == 123){
                                if(paypal.equalsIgnoreCase("1")){
                                    buttonPaypal.setText(getString(R.string.tax_edit));
                                }else if(paypal.equalsIgnoreCase("0")){
                                    buttonPaypal.setText(getString(R.string.setting_Setup));
                                }

                                if(stripe.equalsIgnoreCase("1")){
                                    buttonStripe.setText(getString(R.string.tax_edit));
                                }else if(stripe.equalsIgnoreCase("0")){
                                    buttonStripe.setText(getString(R.string.setting_Setup));
                                }
                            }

                        }


                    }
                    if (status.equals("false")) {
                        //Constant.ErrorToast(OnlinePaymentGatewayActivity.this, jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    Log.e("responsecustomersF", response);


                } else {
                    //Constant.ErrorToast(EditEstimateActivity.this, "Something went wrong, try again!");
                }
            }
        });


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "requestCode:: "+requestCode);
        Log.e(TAG, "resultCode:: "+resultCode);

        if(requestCode == 122){
            payPalCode = requestCode;
            CompanyInformation(selectedCompanyId);
        }

        if(requestCode == 123){
            stripeCode = requestCode;
            if(data != null){
                String responseCode = data.getStringExtra("responseCode");
                Log.e(TAG, "responseCode:: "+responseCode);
                callMethod(responseCode);
            }

        }
    }




    private void callMethod(String responseCode) {
        final ProgressDialog progressDialog = new ProgressDialog(OnlinePaymentGatewayActivity.this);
        progressDialog.setMessage(getString(R.string.dialog_Please_wait));
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
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL+ "settings/add", params, new AsyncHttpResponseHandler() {
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

                        CompanyInformation(selectedCompanyId);


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
        companyget();
    }









    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

        private static final String TAG = "MenuAdapter";

        ArrayList<String> cnames = new ArrayList<>();

        Dialog mybuilder;

        public MenuAdapter(ArrayList<String> cnames, Dialog mybuilder) {
            super();
            this.cnames = cnames;
            this.mybuilder = mybuilder;
        }



        @Override
        public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.menu_item_2, viewGroup, false);
            return new MenuAdapter.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final MenuAdapter.ViewHolder viewHolder, final int i) {

            viewHolder.textViewName.setText(""+cnames.get(i));
            viewHolder.realtive1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mybuilder.dismiss();
//                    selectedProductId = pids.get(i);
//                    productcategory1.setText(cnames.get(i));

//                    selectedCompanyId = cids.get(i);
//                    colorCode = arrayColor.get(i);
//                    String parmavalue = "UNPAID";
//                    InvoicelistData(parmavalue);
//
                    selectcompany1.setText(cnames.get(i));

                    selectedCompanyId = cids.get(i);
                    selectedCompanyName = cnames.get(i);
                    selectedCompanyImage = cidsLogo.get(i);

                    selectedStringPaypal = stringPaypal.get(i);
                    selectedStringStripe = stringStripe.get(i);

                    if(selectedStringPaypal.equalsIgnoreCase("1")){
                        buttonPaypal.setText(getString(R.string.tax_edit));
                    }else{
                        buttonPaypal.setText(getString(R.string.setting_Setup));
                    }

                    if(selectedStringStripe.equalsIgnoreCase("1")){
                        buttonStripe.setText(getString(R.string.tax_edit));
                    }else{
                        buttonStripe.setText(getString(R.string.setting_Setup));
                    }


                }
            });

        }


        @Override
        public int getItemCount() {
            return cnames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            View view11 = null;
            TextView textViewName;
            RelativeLayout realtive1;
            public ViewHolder(View itemView) {
                super(itemView);
                view11 = itemView;
                realtive1 = (RelativeLayout) itemView.findViewById(R.id.realtive1);
                textViewName = (TextView) itemView.findViewById(R.id.txtList);
            }

        }



        public void updateData(ArrayList<String> cnames) {
            // TODO Auto-generated method stub
            this.cnames = cnames;
            notifyDataSetChanged();
        }


    }


}
