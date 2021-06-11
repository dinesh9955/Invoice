package com.receipt.invoice.stock.sirproject.Report;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.Utility;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cz.msebera.android.httpclient.Header;

public class PreviewItActivity extends BaseActivity {

    private static final String TAG = "PreviewItActivity";
    WebView invoiceweb;

    int positionNext = -1;
    String company_image_path = "";

//    String companyName = "";
//    String companyAddress = "";
//    String companyContactNo = "";
//    String companyWebsite = "";
//    String companyEmail = "";
//    String companyLogo = "";


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice_);

        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);
        ImageView printimg = toolbar.findViewById(R.id.imageViewptint);
        printimg.setVisibility(View.GONE);

        invoiceweb = findViewById(R.id.invoiceweb);



        titleView.setText("Preview Report");

        WebSettings webSettings = invoiceweb.getSettings();

        webSettings.setJavaScriptEnabled(true);
        invoiceweb.getSettings().setAllowFileAccess(true);
        invoiceweb.setWebChromeClient(new WebChromeClient());
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        invoiceweb.getSettings().setLoadsImagesAutomatically(true);
        invoiceweb.getSettings().setJavaScriptEnabled(true);
        invoiceweb.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        invoiceweb.getSettings().setLoadWithOverviewMode(true);
        invoiceweb.getSettings().setUseWideViewPort(true);
        invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);

        invoiceweb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                //if page loaded successfully then show print button
                //findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });


        printimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createWebPrintJob(invoiceweb);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();


        positionNext = bundle.getInt("positionNext");

        Log.e(TAG, "positionNextDDD "+positionNext);
        company_image_path = bundle.getString("company_image_path");
        if(positionNext == 0){
            String customer_id = bundle.getString("customer_id");
            customerReport(customer_id);
        } else if(positionNext == 1){
            String supplier_id = bundle.getString("supplier_id");
            supplierReport(supplier_id);
        } else if(positionNext == 2){
            String company_id = bundle.getString("company_id");
            totalSalesReport(company_id);
        } else if(positionNext == 3){
            String company_id = bundle.getString("company_id");
            totalPurchaseReport(company_id);
        } else if(positionNext == 4){
            String company_id = bundle.getString("company_id");
            customerAgeingReport(company_id);
        } else if(positionNext == 5){
            String company_id = bundle.getString("company_id");
            taxCollectedReport(company_id);
        } else if(positionNext == 6){
            String company_id = bundle.getString("company_id");
            stockReport(company_id);
        } else if(positionNext == 7){
            String product_id = bundle.getString("product_id");
//            companyName = bundle.getString("companyName");
//            companyAddress = bundle.getString("companyAddress");
//            companyContactNo = bundle.getString("companyContactNo");
//            companyWebsite = bundle.getString("companyWebsite");
//            companyEmail = bundle.getString("companyEmail");
//            companyLogo = bundle.getString("companyLogo");
            productMovementReport(product_id);
        } else{
//            String company_id = bundle.getString("company_id");
//            customerReport(company_id);
        }



        Log.e(TAG, "company_image_pathWW "+company_image_path);



    }


    private void customerReport(String customer_id) {
        RequestParams params = new RequestParams();
        params.add("customer_id", customer_id);

        String token = Constant.GetSharedPreferences(PreviewItActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "report/customerStatement", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanyCSS"+ response);

                //  avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        String company_image_path = data.getString("company_image_path");

                        JSONObject info = data.getJSONObject("info");
                        String customer_name = info.getString("customer_name");
                        String contact_name = info.getString("contact_name");
                        String company_name = info.getString("company_name");
                        String company_address = info.getString("company_address");
                        String company_phone = info.getString("company_phone");
                        String company_website = info.getString("company_website");
                        String company_email = info.getString("company_email");
                        String company_logo = info.getString("company_logo");
                        String currency_title = info.getString("currency_title");
                        String currency_code = info.getString("currency_code");
                        String currency_symbol = info.getString("currency_symbol");


                        CustomerItem customerItem = new CustomerItem();
                        customerItem.setCustomer_name(customer_name);
                        customerItem.setContact_name(contact_name);
                        customerItem.setCompany_name(company_name);
                        customerItem.setCompany_address(company_address);
                        customerItem.setCompany_phone(company_phone);
                        customerItem.setCompany_website(company_website);
                        customerItem.setCompany_email(company_email);
                        if(company_logo.endsWith(".png") || company_logo.endsWith(".jpg") || company_logo.endsWith(".jpeg")){
                            customerItem.setCompany_logo(company_image_path+company_logo);
                        }else{
                            customerItem.setCompany_logo("/android_res/drawable/white_img.png");
                        }

                        customerItem.setCurrency_title(currency_title);
                        customerItem.setCurrency_code(currency_code);
                        customerItem.setCurrency_symbol(currency_symbol);

                        ArrayList<CustomerReportItem> customerReportItemArrayList = new ArrayList<>();

                        JSONArray statement = data.getJSONArray("statement");
                        if (statement.length() > 0) {
                            for (int i = 0; i < statement.length(); i++) {
                                JSONObject item = statement.getJSONObject(i);

                                String created_date = item.getString("created_date");
                                String invoice_id = item.getString("invoice_id");
                                String particular = item.getString("particular");
                                String debit = item.getString("debit");
                                String credit = item.getString("credit");
                                String code = item.getString("code");
                                String customer_id = item.getString("customer_id");
                                String balance = item.getString("balance");

                                Log.e(TAG, "CompanyId "+invoice_id);

                                CustomerReportItem customerReportItem = new CustomerReportItem();
                                customerReportItem.setCreated_date(created_date);
                                customerReportItem.setInvoice_id(invoice_id);
                                customerReportItem.setParticular(particular);
                                customerReportItem.setDebit(debit);
                                customerReportItem.setCredit(credit);
                                customerReportItem.setCode(code);
                                customerReportItem.setCustomer_id(customer_id);
                                customerReportItem.setBalance(balance);

                                customerReportItemArrayList.add(customerReportItem);
                            }
                        }




                        customerReportWeb(customerItem, customerReportItemArrayList);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    //avi.smoothToHide();
                    Log.e(TAG, "responsecompanyF"+ response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("false")) {
//                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }


    private void customerReportWeb(CustomerItem customerItem, ArrayList<CustomerReportItem> customerReportItemArrayList) {



        double totalAmount = 0.0;

        String name = "report/CustomerReport.html";
        String nameName = "file:///android_asset/report/CustomerReport.html";

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size(); i++) {
                cruncycode = customerItem.getCurrency_symbol();

//                DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                double getDebit = Double.parseDouble(customerReportItemArrayList.get(i).getDebit());
                double getCredit = Double.parseDouble(customerReportItemArrayList.get(i).getCredit());
                double getBalance = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());

                String stringDebit = "";
                String stringCredit = "";
                String stringBalance = "";

                if(getDebit == 0){
                    stringDebit = "";
                }else{
                    String stringDebit1 = customerReportItemArrayList.get(i).getDebit();
                    stringDebit = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringDebit1)) + Utility.getReplaceDollor(cruncycode);
                }

                if(getCredit == 0){
                    stringCredit = "";
                }else{
                    String stringCredit1 = customerReportItemArrayList.get(i).getCredit();
                    stringCredit = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringCredit1)) + Utility.getReplaceDollor(cruncycode);
                }

                if(getBalance == 0){
                    stringBalance = "";
                }else{
                    String stringBalance1 = customerReportItemArrayList.get(i).getBalance();
                    stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }



                productitem = IOUtils.toString(getAssets().open("report/customer_single_item.html"))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getCreated_date())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getParticular())
                        .replaceAll("#DebitAmount#", stringDebit)
                        .replaceAll("#CreditAmount#", stringCredit)
                        .replaceAll("#Balance#", stringBalance);
                productitemlist = productitemlist + productitem;


                double allAmount = 0.0;
                if(i == customerReportItemArrayList.size() - 1){
                    try{
                        allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());
                    }catch (Exception e){

                    }
                }

                totalAmount =   totalAmount + allAmount;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        //  DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        //double vc = Double.parseDouble(totalAmount);
        //DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
//        Grossamount_str= Utility.getPatternFormat(""+numberPostion, totalAmount);

        StringBuilder stringBuilderBillTo = new StringBuilder();
        if(!customerItem.getCompany_address().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getCompany_address()+"</br>");
        }
        if(!customerItem.getCompany_phone().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getCompany_phone()+"</br>");
        }
        if(!customerItem.getCompany_website().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getCompany_website()+"</br>");
        }
        if(!customerItem.getCompany_email().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getCompany_email()+"");
        }

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))
                    .replaceAll("Company Name", customerItem.getCompany_name())
                    .replaceAll("Address", ""+stringBuilderBillTo.toString())
                    .replaceAll("Customer Name", customerItem.getCustomer_name())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getCompany_logo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Total Amount-", Utility.getPatternFormat(""+numberPostion, totalAmount) + Utility.getReplaceDollor(cruncycode))
            ;

        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }



    private void supplierReport(String customer_id) {
        RequestParams params = new RequestParams();
        params.add("supplier_id", customer_id);

        String token = Constant.GetSharedPreferences(PreviewItActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "report/supplierStatement", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanySS"+ response);

                //  avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        String company_image_path = data.getString("company_image_path");

                        JSONObject info = data.getJSONObject("info");
                        String customer_name = info.getString("supplier_name");
                        String contact_name = info.getString("contact_name");
                        String company_name = info.getString("company_name");
                        String company_address = info.getString("company_address");
                        String company_phone = info.getString("company_phone");
                        String company_website = info.getString("company_website");
                        String company_email = info.getString("company_email");
                        String company_logo = info.getString("company_logo");
                        String currency_title = info.getString("currency_title");
                        String currency_code = info.getString("currency_code");
                        String currency_symbol = info.getString("currency_symbol");


                        SupplierItem customerItem = new SupplierItem();
                        customerItem.setSupplier_name(customer_name);
                        customerItem.setContact_name(contact_name);
                        customerItem.setCompany_name(company_name);
                        customerItem.setCompany_address(company_address);
                        customerItem.setCompany_phone(company_phone);
                        customerItem.setCompany_website(company_website);
                        customerItem.setCompany_email(company_email);
                        if(company_logo.endsWith(".png") || company_logo.endsWith(".jpg") || company_logo.endsWith(".jpeg")){
                            customerItem.setCompany_logo(company_image_path+company_logo);
                        }else{
                            customerItem.setCompany_logo("/android_res/drawable/white_img.png");
                        }

                        customerItem.setCurrency_title(currency_title);
                        customerItem.setCurrency_code(currency_code);
                        customerItem.setCurrency_symbol(currency_symbol);

                        ArrayList<CustomerReportItem> customerReportItemArrayList = new ArrayList<>();

                        JSONArray statement = data.getJSONArray("statement");
                        if (statement.length() > 0) {
                            for (int i = 0; i < statement.length(); i++) {
                                JSONObject item = statement.getJSONObject(i);

                                String created_date = item.getString("created_date");
//                                String invoice_id = item.getString("invoice_id");
                                String particular = item.getString("particular");
                                String debit = item.getString("debit");
                                String credit = item.getString("credit");
                                String code = item.getString("code");
//                                String customer_id = item.getString("customer_id");
                                String balance = item.getString("balance");

                                // Log.e(TAG, "CompanyId "+invoice_id);

                                CustomerReportItem customerReportItem = new CustomerReportItem();
                                customerReportItem.setCreated_date(created_date);
//                                customerReportItem.setInvoice_id(invoice_id);
                                customerReportItem.setParticular(particular);
                                customerReportItem.setDebit(debit);
                                customerReportItem.setCredit(credit);
                                customerReportItem.setCode(code);
//                                customerReportItem.setCustomer_id(customer_id);
                                customerReportItem.setBalance(balance);

                                customerReportItemArrayList.add(customerReportItem);
                            }
                        }


                        supplierReportWeb(customerItem, customerReportItemArrayList);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    //avi.smoothToHide();
                    Log.e(TAG, "responsecompanyF"+ response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("false")) {
//                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }


    private void supplierReportWeb(SupplierItem customerItem, ArrayList<CustomerReportItem> customerReportItemArrayList) {

        double totalAmount = 0.0;

        String name = "report/SupplierReport.html";
        String nameName = "file:///android_asset/report/SupplierReport.html";

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size(); i++) {
                cruncycode = customerItem.getCurrency_symbol();

//                DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                double getDebit = Double.parseDouble(customerReportItemArrayList.get(i).getDebit());
                double getCredit = Double.parseDouble(customerReportItemArrayList.get(i).getCredit());
                double getBalance = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());

                String stringDebit = "";
                String stringCredit = "";
                String stringBalance = "";

                if(getDebit == 0){
                    stringDebit = "";
                }else{
                    String stringDebit1 = customerReportItemArrayList.get(i).getDebit();
                    stringDebit = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringDebit1)) + Utility.getReplaceDollor(cruncycode);
                }

                if(getCredit == 0){
                    stringCredit = "";
                }else{
                    String stringCredit1 = customerReportItemArrayList.get(i).getCredit();
                    stringCredit = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringCredit1)) + Utility.getReplaceDollor(cruncycode);
                }

                if(getBalance == 0){
                    stringBalance = "";
                }else{
                    String stringBalance1 = customerReportItemArrayList.get(i).getBalance();
                    stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }


                productitem = IOUtils.toString(getAssets().open("report/customer_single_item.html"))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getCreated_date())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getParticular())
                        .replaceAll("#DebitAmount#", stringDebit)
                        .replaceAll("#CreditAmount#", stringCredit)
                        .replaceAll("#Balance#", stringBalance);
                productitemlist = productitemlist + productitem;


                double allAmount = 0.0;
                if(i == customerReportItemArrayList.size() - 1){
                    try{
                        allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());
                    }catch (Exception e){

                    }
                }


                totalAmount =   totalAmount + allAmount;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        //    DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        StringBuilder stringBuilderBillTo = new StringBuilder();
        if(!customerItem.getCompany_address().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getCompany_address()+"</br>");
        }
        if(!customerItem.getCompany_phone().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getCompany_phone()+"</br>");
        }
        if(!customerItem.getCompany_website().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getCompany_website()+"</br>");
        }
        if(!customerItem.getCompany_email().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getCompany_email()+"");
        }

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))
                    .replaceAll("Company Name", customerItem.getCompany_name())
                    .replaceAll("Address", ""+stringBuilderBillTo.toString())
                    .replaceAll("Customer Name", customerItem.getSupplier_name())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getCompany_logo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Total Amount-", Utility.getPatternFormat(""+numberPostion, totalAmount) + Utility.getReplaceDollor(cruncycode))
            ;

        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }



    private void totalSalesReport(String customer_id) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(PreviewItActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "report/totalSales", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanySS"+ response);

                //  avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        // String company_image_path = data.getString("company_image_path");

                        JSONObject company = data.getJSONObject("company");

                        String name = company.getString("name");
                        String address = company.getString("address");
                        String phone_number = company.getString("phone_number");
                        String website = company.getString("website");
                        String email = company.getString("email");
                        String logo = company.getString("logo");
                        String currency_symbol = company.getString("currency_symbol");

                        CompanyItem customerItem = new CompanyItem();
                        customerItem.setName(name);
                        customerItem.setAddress(address);
                        customerItem.setPhone_number(phone_number);
                        customerItem.setWebsite(website);
                        customerItem.setEmail(email);
                        if(logo.endsWith(".png") || logo.endsWith(".jpg") || logo.endsWith(".jpeg")){
                            customerItem.setLogo(company_image_path+logo);
                        }else{
                            customerItem.setLogo("/android_res/drawable/white_img.png");
                        }
                        customerItem.setCurrency_symbol(currency_symbol);

                        ArrayList<TotalSalesReportItem> customerReportItemArrayList = new ArrayList<>();

                        JSONArray statement = data.getJSONArray("total_sales");
                        if (statement.length() > 0) {
                            for (int i = 0; i < statement.length(); i++) {
                                JSONObject item = statement.getJSONObject(i);

                                String invoice_id = item.getString("invoice_id");
                                String invoice_no = item.getString("invoice_no");
                                String invoice_date = item.getString("invoice_date");
                                String contact_name = item.getString("contact_name");
                                String customer_name = item.getString("customer_name");
                                String total = item.getString("total");




                                TotalSalesReportItem customerReportItem = new TotalSalesReportItem();
                                customerReportItem.setInvoice_id(invoice_id);
                                customerReportItem.setInvoice_no(invoice_no);
                                customerReportItem.setInvoice_date(invoice_date);
                                customerReportItem.setContact_name(contact_name);
                                customerReportItem.setCustomer_name(customer_name);
                                customerReportItem.setTotal(total);

                                customerReportItemArrayList.add(customerReportItem);
                            }
                        }



                        totalSalesReportWeb(customerItem, customerReportItemArrayList);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    //avi.smoothToHide();
                    Log.e(TAG, "responsecompanyF"+ response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("false")) {
//                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }


    private void totalSalesReportWeb(CompanyItem customerItem, ArrayList<TotalSalesReportItem> customerReportItemArrayList) {

        double totalAmount = 0.0;

        String name = "report/SalesReport.html";
        String nameName = "file:///android_asset/report/SalesReport.html";

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size(); i++) {
                cruncycode = customerItem.getCurrency_symbol();

////                DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                double getTotal = Double.parseDouble(customerReportItemArrayList.get(i).getTotal());
//                double getCredit = Double.parseDouble(customerReportItemArrayList.get(i).getCredit());
//                double getBalance = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());
//
//                String stringDebit = "";
//                String stringCredit = "";
                String stringBalance = "";
//
//                if(getDebit == 0){
//                    stringDebit = "";
//                }else{
//                    stringDebit = customerReportItemArrayList.get(i).getDebit() + Utility.getReplaceDollor(cruncycode);
//                }


                if(getTotal == 0){
                    stringBalance = "";
                }else{
                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                    stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }


                productitem = IOUtils.toString(getAssets().open("report/total_sales_single_item.html"))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getInvoice_date())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getInvoice_no())
                        .replaceAll("#CustomerName#", customerReportItemArrayList.get(i).getCustomer_name())
                        .replaceAll("#Amount#", stringBalance);
                productitemlist = productitemlist + productitem;

                double allAmount = 0.0;

                try{
                    allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getTotal());
                }catch (Exception e){

                }

                totalAmount =   totalAmount + allAmount;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        //    DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");


        StringBuilder stringBuilderBillTo = new StringBuilder();
        if(!customerItem.getAddress().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getAddress()+"</br>");
        }
        if(!customerItem.getPhone_number().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getPhone_number()+"</br>");
        }
        if(!customerItem.getWebsite().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getWebsite()+"</br>");
        }
        if(!customerItem.getEmail().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getEmail()+"");
        }

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))
                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", ""+stringBuilderBillTo.toString())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getLogo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Total Amount-", Utility.getPatternFormat(""+numberPostion, totalAmount)+ Utility.getReplaceDollor(cruncycode))
            ;


        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }



    private void totalPurchaseReport(String customer_id) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(PreviewItActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "report/totalPurchases", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanySS"+ response);

                //  avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        // String company_image_path = data.getString("company_image_path");

                        JSONObject company = data.getJSONObject("company");

                        String name = company.getString("name");
                        String address = company.getString("address");
                        String phone_number = company.getString("phone_number");
                        String website = company.getString("website");
                        String email = company.getString("email");
                        String logo = company.getString("logo");
                        String currency_symbol = company.getString("currency_symbol");

                        CompanyItem customerItem = new CompanyItem();
                        customerItem.setName(name);
                        customerItem.setAddress(address);
                        customerItem.setPhone_number(phone_number);
                        customerItem.setWebsite(website);
                        customerItem.setEmail(email);
                        if(logo.endsWith(".png") || logo.endsWith(".jpg") || logo.endsWith(".jpeg")){
                            customerItem.setLogo(company_image_path+logo);
                        }else{
                            customerItem.setLogo("/android_res/drawable/white_img.png");
                        }
                        customerItem.setCurrency_symbol(currency_symbol);

                        ArrayList<TotalPurchaseReportItem> customerReportItemArrayList = new ArrayList<>();

                        JSONArray statement = data.getJSONArray("total_purchases");
                        if (statement.length() > 0) {
                            for (int i = 0; i < statement.length(); i++) {
                                JSONObject item = statement.getJSONObject(i);

                                String purchase_order_id = item.getString("purchase_order_id");
                                String purchase_order_no = item.getString("purchase_order_no");
                                String order_date = item.getString("order_date");
                                String contact_name = item.getString("contact_name");
                                String supplier_name = item.getString("supplier_name");
                                String total = item.getString("total");




                                TotalPurchaseReportItem customerReportItem = new TotalPurchaseReportItem();
                                customerReportItem.setPurchase_order_id(purchase_order_id);
                                customerReportItem.setPurchase_order_no(purchase_order_no);
                                customerReportItem.setOrder_date(order_date);
                                customerReportItem.setContact_name(contact_name);
                                customerReportItem.setSupplier_name(supplier_name);
                                customerReportItem.setTotal(total);

                                customerReportItemArrayList.add(customerReportItem);
                            }
                        }



                        totalPurchaseReportWeb(customerItem, customerReportItemArrayList);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    //avi.smoothToHide();
                    Log.e(TAG, "responsecompanyF"+ response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("false")) {
//                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }


    private void totalPurchaseReportWeb(CompanyItem customerItem, ArrayList<TotalPurchaseReportItem> customerReportItemArrayList) {

        double totalAmount = 0.0;

        String name = "report/PurchaseReport.html";
        String nameName = "file:///android_asset/report/PurchaseReport.html";

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size(); i++) {
                cruncycode = customerItem.getCurrency_symbol();

////                DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                double getTotal = Double.parseDouble(customerReportItemArrayList.get(i).getTotal());
//                double getCredit = Double.parseDouble(customerReportItemArrayList.get(i).getCredit());
//                double getBalance = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());
//
//                String stringDebit = "";
//                String stringCredit = "";
                String stringBalance = "";
//
//                if(getDebit == 0){
//                    stringDebit = "";
//                }else{
//                    stringDebit = customerReportItemArrayList.get(i).getDebit() + Utility.getReplaceDollor(cruncycode);
//                }


                if(getTotal == 0){
                    stringBalance = "";
                }else{
                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                    stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }


                productitem = IOUtils.toString(getAssets().open("report/total_sales_single_item.html"))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getOrder_date())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getPurchase_order_no())
                        .replaceAll("#CustomerName#", customerReportItemArrayList.get(i).getSupplier_name())
                        .replaceAll("#Amount#", stringBalance);
                productitemlist = productitemlist + productitem;

                double allAmount = 0.0;

                try{
                    allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getTotal());
                }catch (Exception e){

                }

                totalAmount =   totalAmount + allAmount;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        //    DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        StringBuilder stringBuilderBillTo = new StringBuilder();
        if(!customerItem.getAddress().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getAddress()+"</br>");
        }
        if(!customerItem.getPhone_number().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getPhone_number()+"</br>");
        }
        if(!customerItem.getWebsite().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getWebsite()+"</br>");
        }
        if(!customerItem.getEmail().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getEmail()+"");
        }

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))
                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", ""+stringBuilderBillTo.toString())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getLogo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Total Amount-", Utility.getPatternFormat(""+numberPostion, totalAmount)+ Utility.getReplaceDollor(cruncycode))
            ;


        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }



    private void customerAgeingReport(String customer_id) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(PreviewItActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "report/customerAgeing", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanySS"+ response);

                //  avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        // String company_image_path = data.getString("company_image_path");

                        JSONObject company = data.getJSONObject("company");

                        String name = company.getString("name");
                        String address = company.getString("address");
                        String phone_number = company.getString("phone_number");
                        String website = company.getString("website");
                        String email = company.getString("email");
                        String logo = company.getString("logo");
                        String currency_symbol = company.getString("currency_symbol");

                        CompanyItem customerItem = new CompanyItem();
                        customerItem.setName(name);
                        customerItem.setAddress(address);
                        customerItem.setPhone_number(phone_number);
                        customerItem.setWebsite(website);
                        customerItem.setEmail(email);
                        if(logo.endsWith(".png") || logo.endsWith(".jpg") || logo.endsWith(".jpeg")){
                            customerItem.setLogo(company_image_path+logo);
                        }else{
                            customerItem.setLogo("/android_res/drawable/white_img.png");
                        }
                        customerItem.setCurrency_symbol(currency_symbol);

                        ArrayList<CustomerAgeingReportItem> customerReportItemArrayList = new ArrayList<>();

                        JSONArray statement = data.getJSONArray("customer_ageing");
                        if (statement.length() > 0) {
                            for (int i = 0; i < statement.length(); i++) {
                                JSONObject item = statement.getJSONObject(i);



                                String customer_id = item.getString("customer_id");

                                String contact_name = item.getString("contact_name");
                                String customer_name = item.getString("customer_name");

                                String not_due = item.getString("not_due");

                                String slab1 = item.getString("slab1");
                                String slab2 = item.getString("slab2");
                                String slab3 = item.getString("slab3");
                                String slab4 = item.getString("slab4");

                                String total = item.getString("total");




                                CustomerAgeingReportItem customerReportItem = new CustomerAgeingReportItem();
                                customerReportItem.setCustomer_id(customer_id);
                                customerReportItem.setContact_name(contact_name);
                                customerReportItem.setCustomer_name(customer_name);
                                customerReportItem.setNot_due(not_due);
                                customerReportItem.setSlab1(slab1);
                                customerReportItem.setSlab2(slab2);
                                customerReportItem.setSlab3(slab3);
                                customerReportItem.setSlab4(slab4);


                                customerReportItem.setTotal(total);

                                customerReportItemArrayList.add(customerReportItem);
                            }
                        }



                        customerAgeingReportWeb(customerItem, customerReportItemArrayList);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    //avi.smoothToHide();
                    Log.e(TAG, "responsecompanyF"+ response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("false")) {
//                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }


    private void customerAgeingReportWeb(CompanyItem customerItem, ArrayList<CustomerAgeingReportItem> customerReportItemArrayList) {

        double totalAmount = 0.0;

        String name = "report/CustomerAgingReport.html";
        String nameName = "file:///android_asset/report/CustomerAgingReport.html";

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size(); i++) {
                cruncycode = customerItem.getCurrency_symbol();

////                DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

                double slab1 = Double.parseDouble(customerReportItemArrayList.get(i).getSlab1());
                double slab2 = Double.parseDouble(customerReportItemArrayList.get(i).getSlab2());
                double slab3 = Double.parseDouble(customerReportItemArrayList.get(i).getSlab3());
                double slab4 = Double.parseDouble(customerReportItemArrayList.get(i).getSlab4());

                String slab1Txt11 = Utility.getPatternFormat(""+numberPostion, slab1);
                String slab1Txt22 = Utility.getPatternFormat(""+numberPostion, slab2);
                String slab1Txt33 = Utility.getPatternFormat(""+numberPostion, slab3);
                String slab1Txt44 = Utility.getPatternFormat(""+numberPostion, slab4);

                String slab1Txt = "";
                String slab2Txt = "";
                String slab3Txt = "";
                String slab4Txt = "";


                if(slab1 == 0){
                    slab1Txt = "";
                }else{
                    slab1Txt = slab1Txt11 + Utility.getReplaceDollor(cruncycode);
                }

                if(slab2 == 0){
                    slab2Txt = "";
                }else{
                    slab2Txt = slab1Txt22 + Utility.getReplaceDollor(cruncycode);
                }

                if(slab3 == 0){
                    slab3Txt = "";
                }else{
                    slab3Txt = slab1Txt33 + Utility.getReplaceDollor(cruncycode);
                }

                if(slab4 == 0){
                    slab4Txt = "";
                }else{
                    slab4Txt = slab1Txt44 + Utility.getReplaceDollor(cruncycode);
                }


                double doubleBalance = Double.parseDouble(customerReportItemArrayList.get(i).getTotal());
                String stringBalance = "";
                if(doubleBalance == 0){
                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                    stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }else{
                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                    stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }


                double doubleNotDue = Double.parseDouble(customerReportItemArrayList.get(i).getNot_due());
                String stringNotDue = "";
                if(doubleNotDue == 0){
                    stringNotDue = "";
                }else{
                    String stringNotDue1 = customerReportItemArrayList.get(i).getNot_due();
                    stringNotDue = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringNotDue1)) + Utility.getReplaceDollor(cruncycode);
                }




                productitem = IOUtils.toString(getAssets().open("report/customer_ageing_single_item.html"))
                        .replaceAll("#CustomerName#", customerReportItemArrayList.get(i).getContact_name())
                        .replaceAll("#Currentdue#", stringNotDue)
                        .replaceAll("#Slab1#", slab1Txt)
                        .replaceAll("#Slab2#", slab2Txt)
                        .replaceAll("#Slab3#", slab3Txt)
                        .replaceAll("#Slab4#", slab4Txt)
                        .replaceAll("#Amount#", stringBalance);
                productitemlist = productitemlist + productitem;

                double allAmount = 0.0;

                try{
                    allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getTotal());
                }catch (Exception e){

                }

                totalAmount =   totalAmount + allAmount;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        //    DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        StringBuilder stringBuilderBillTo = new StringBuilder();
        if(!customerItem.getAddress().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getAddress()+"</br>");
        }
        if(!customerItem.getPhone_number().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getPhone_number()+"</br>");
        }
        if(!customerItem.getWebsite().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getWebsite()+"</br>");
        }
        if(!customerItem.getEmail().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getEmail()+"");
        }

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))
                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", ""+stringBuilderBillTo.toString())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getLogo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Total Amount-", Utility.getPatternFormat(""+numberPostion, totalAmount)+ Utility.getReplaceDollor(cruncycode))
            ;


        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }



    private void taxCollectedReport(String customer_id) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(PreviewItActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "report/taxation", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanySS"+ response);

                //  avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        // String company_image_path = data.getString("company_image_path");

                        JSONObject company = data.getJSONObject("company");

                        String name = company.getString("name");
                        String address = company.getString("address");
                        String phone_number = company.getString("phone_number");
                        String website = company.getString("website");
                        String email = company.getString("email");
                        String logo = company.getString("logo");
                        String currency_symbol = company.getString("currency_symbol");

                        CompanyItem customerItem = new CompanyItem();
                        customerItem.setName(name);
                        customerItem.setAddress(address);
                        customerItem.setPhone_number(phone_number);
                        customerItem.setWebsite(website);
                        customerItem.setEmail(email);
                        if(logo.endsWith(".png") || logo.endsWith(".jpg") || logo.endsWith(".jpeg")){
                            customerItem.setLogo(company_image_path+logo);
                        }else{
                            customerItem.setLogo("/android_res/drawable/white_img.png");
                        }
                        customerItem.setCurrency_symbol(currency_symbol);

                        ArrayList<TaxCollectedReportItem> customerReportItemArrayList = new ArrayList<>();

                        JSONArray statement = data.getJSONArray("taxation");
                        if (statement.length() > 0) {
                            for (int i = 0; i < statement.length(); i++) {
                                JSONObject item = statement.getJSONObject(i);

                                String date_added = item.getString("date_added");
                                String particulars = item.getString("particulars");
                                String contact_name = item.getString("contact_name");
                                String customer_name = item.getString("customer_name");

                                String tax_name = item.getString("tax_name");
                                String tax_amount = item.getString("tax_amount");
                                String tax_rate = item.getString("tax_rate");


                                TaxCollectedReportItem customerReportItem = new TaxCollectedReportItem();
                                customerReportItem.setDate_added(date_added);
                                customerReportItem.setParticulars(particulars);
                                customerReportItem.setContact_name(contact_name);
                                customerReportItem.setCustomer_name(customer_name);
                                customerReportItem.setTax_name(tax_name);
                                customerReportItem.setTax_amount(tax_amount);
                                customerReportItem.setTax_rate(tax_rate);


                                customerReportItemArrayList.add(customerReportItem);
                            }
                        }


                        taxCollectedReportWeb(customerItem, customerReportItemArrayList);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    //avi.smoothToHide();
                    Log.e(TAG, "responsecompanyF"+ response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("false")) {
//                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }


    private void taxCollectedReportWeb(CompanyItem customerItem, ArrayList<TaxCollectedReportItem> customerReportItemArrayList) {

        double totalAmount = 0.0;

        String name = "report/TaxCollectedReport.html";
        String nameName = "file:///android_asset/report/TaxCollectedReport.html";

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size(); i++) {
                cruncycode = customerItem.getCurrency_symbol();

                double doubleTaxRate = Double.parseDouble(customerReportItemArrayList.get(i).getTax_rate());
                String stringTaxRate = "";
                if(doubleTaxRate == 0){
                    stringTaxRate = "";
                }else{
                    stringTaxRate = Utility.getPatternFormat(""+numberPostion, doubleTaxRate) + "%";
                }


                double doubleAmount = Double.parseDouble(customerReportItemArrayList.get(i).getTax_amount());
                String stringAmount = "";
                if(doubleAmount == 0){
                    stringAmount = "";
                }else{
                    String stringAmount1 = customerReportItemArrayList.get(i).getTax_amount();
                    stringAmount = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringAmount1)) + Utility.getReplaceDollor(cruncycode);
                }



                productitem = IOUtils.toString(getAssets().open("report/tax_collection_single_item.html"))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getDate_added())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getParticulars())
                        .replaceAll("#CustomerName#", customerReportItemArrayList.get(i).getCustomer_name())
                        .replaceAll("#TaxName#", customerReportItemArrayList.get(i).getTax_name())
                        .replaceAll("#TaxRate#", stringTaxRate)
                        .replaceAll("#Amount#", stringAmount +"");
                productitemlist = productitemlist + productitem;

                double allAmount = 0.0;

                try{
                    allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getTax_amount());
                }catch (Exception e){

                }

                totalAmount =   totalAmount + allAmount;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }




        String stringAmount = "";
        if(totalAmount == 0){
            stringAmount = "";
        }else{
            stringAmount = Utility.getPatternFormat(""+numberPostion, totalAmount) + Utility.getReplaceDollor(cruncycode);
        }




        StringBuilder stringBuilderBillTo = new StringBuilder();
        if(!customerItem.getAddress().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getAddress()+"</br>");
        }
        if(!customerItem.getPhone_number().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getPhone_number()+"</br>");
        }
        if(!customerItem.getWebsite().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getWebsite()+"</br>");
        }
        if(!customerItem.getEmail().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getEmail()+"");
        }

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))
                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", ""+stringBuilderBillTo.toString())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getLogo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Total Amount-", stringAmount)
            ;


        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }




    private void stockReport(String customer_id) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(PreviewItActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "report/stock", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanySS"+ response);

                //  avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        // String company_image_path = data.getString("company_image_path");

                        JSONObject company = data.getJSONObject("company");

                        String name = company.getString("name");
                        String address = company.getString("address");
                        String phone_number = company.getString("phone_number");
                        String website = company.getString("website");
                        String email = company.getString("email");
                        String logo = company.getString("logo");
                        String currency_symbol = company.getString("currency_symbol");

                        CompanyItem customerItem = new CompanyItem();
                        customerItem.setName(name);
                        customerItem.setAddress(address);
                        customerItem.setPhone_number(phone_number);
                        customerItem.setWebsite(website);
                        customerItem.setEmail(email);
                        if(logo.endsWith(".png") || logo.endsWith(".jpg") || logo.endsWith(".jpeg")){
                            customerItem.setLogo(company_image_path+logo);
                        }else{
                            customerItem.setLogo("/android_res/drawable/white_img.png");
                        }
                        customerItem.setCurrency_symbol(currency_symbol);

                        ArrayList<StockReportItem> customerReportItemArrayList = new ArrayList<>();

                        JSONArray statement = data.getJSONArray("stock");
                        if (statement.length() > 0) {
                            for (int i = 0; i < statement.length(); i++) {
                                JSONObject item = statement.getJSONObject(i);

                                String product_id = item.getString("product_id");
                                String nameA = item.getString("name");
                                String minimum = item.getString("minimum");
                                String quantity = item.getString("quantity");

                                String quantity_status = item.getString("quantity_status");
                                String price = item.getString("price");
                                String value = item.getString("value");
                                String date_added = item.getString("date_added");

                                StockReportItem customerReportItem = new StockReportItem();
                                customerReportItem.setProduct_id(product_id);
                                customerReportItem.setName(nameA);
                                customerReportItem.setMinimum(minimum);
                                customerReportItem.setQuantity(quantity);
                                customerReportItem.setQuantity_status(quantity_status);
                                customerReportItem.setPrice(price);
                                customerReportItem.setValue(value);
                                customerReportItem.setDate_added(date_added);

                                if(quantity_status.equalsIgnoreCase("In Stock")){
                                    customerReportItemArrayList.add(customerReportItem);
                                }


                            }
                        }


                        stockReportWeb(customerItem, customerReportItemArrayList);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    //avi.smoothToHide();
                    Log.e(TAG, "responsecompanyF"+ response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("false")) {
//                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }


    private void stockReportWeb(CompanyItem customerItem, ArrayList<StockReportItem> customerReportItemArrayList) {

        double totalAmount = 0.0;

        String name = "report/StockReport.html";
        String nameName = "file:///android_asset/report/StockReport.html";

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size(); i++) {
                cruncycode = customerItem.getCurrency_symbol();

////                DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

                double minimum = Double.parseDouble(customerReportItemArrayList.get(i).getMinimum());
                double quantity = Double.parseDouble(customerReportItemArrayList.get(i).getQuantity());
                double pricePerUnit = Double.parseDouble(customerReportItemArrayList.get(i).getPrice());
//                double value = Double.parseDouble(customerReportItemArrayList.get(i).getValue());




                String minimumTxt = "";
                if(minimum == 0){
                    minimumTxt = "";
                }else{
                    String minimumSS = Utility.getPatternFormat(""+numberPostion, minimum);
                    minimumTxt = minimumSS;
                }

                String quantityTxt = "";
                if(quantity == 0){
                    quantityTxt = "";
                }else{
                    String quantitySS = Utility.getPatternFormat(""+numberPostion, quantity);
                    quantityTxt = quantitySS;
                }


                String colorCode = "#ff0000";
                if(customerReportItemArrayList.get(i).getQuantity_status().equalsIgnoreCase("In Stock")){
                    colorCode = "#159f5c";
                }else{
                    colorCode = "#ff0000";
                }



                String pricePerUnitTxt = "";
                if(pricePerUnit == 0){
                    pricePerUnitTxt = "";
                }else{
                    String pricePerUnitSS = Utility.getPatternFormat(""+numberPostion, pricePerUnit) + Utility.getReplaceDollor(cruncycode);
                    pricePerUnitTxt = pricePerUnitSS;
                }


                double quantityPricePerUnit = quantity * pricePerUnit;
                String valueTxt = "";
                if(quantityPricePerUnit == 0){
                    valueTxt = "";
                }else{
                    String valueSS = Utility.getPatternFormat(""+numberPostion, quantityPricePerUnit) + Utility.getReplaceDollor(cruncycode);
                    valueTxt = valueSS;
                }


//                String priceS = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(customerReportItemArrayList.get(i).getPrice()));
//                String valueS = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(customerReportItemArrayList.get(i).getValue()));


                productitem = IOUtils.toString(getAssets().open("report/stock_single_item.html"))
                        .replaceAll("#SNo#", ""+(i+1))
                        .replaceAll("#Product#", customerReportItemArrayList.get(i).getName())
                        .replaceAll("#ReorderLevel#", minimumTxt)
                        .replaceAll("#QuantityAvaliable#", ""+quantityTxt)
                        .replaceAll("#Status#", customerReportItemArrayList.get(i).getQuantity_status())
                        .replaceAll("white1", colorCode)
                        .replaceAll("#PerUnitPrice#", pricePerUnitTxt)
                        .replaceAll("#InventoryValue#", valueTxt);
                productitemlist = productitemlist + productitem;

                double allAmount = 0.0;

                try{
                    allAmount = quantityPricePerUnit;
                }catch (Exception e){

                }

                totalAmount =   totalAmount + allAmount;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        //DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        StringBuilder stringBuilderBillTo = new StringBuilder();
        if(!customerItem.getAddress().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getAddress()+"</br>");
        }
        if(!customerItem.getPhone_number().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getPhone_number()+"</br>");
        }
        if(!customerItem.getWebsite().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getWebsite()+"</br>");
        }
        if(!customerItem.getEmail().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getEmail()+"");
        }

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))
                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", ""+stringBuilderBillTo.toString())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getLogo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Total Amount-", Utility.getPatternFormat(""+numberPostion, totalAmount) + Utility.getReplaceDollor(cruncycode))
            ;


        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }



    private void productMovementReport(String customer_id) {
        RequestParams params = new RequestParams();
        params.add("product_id", customer_id);

        String token = Constant.GetSharedPreferences(PreviewItActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "report/productMovement", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanySS"+ response);

                //  avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        String company_image_path = data.getString("company_image_path");

                        JSONObject product = data.getJSONObject("product");
                        String productName = product.getString("name");
                        String productQuantity = product.getString("quantity");
                        String productPrice = product.getString("price");

                        JSONObject company = data.getJSONObject("company");
                        String name = company.getString("name");
                        String address = company.getString("address");
                        String phone_number = company.getString("phone_number");
                        String website = company.getString("website");
                        String email = company.getString("email");
                        String logo = company.getString("logo");

                        String currency_symbol = product.getString("currency_symbol");

                        CompanyItem customerItem = new CompanyItem();

                        customerItem.setProductName(productName);
                        customerItem.setProductQuantity(productQuantity);
                        customerItem.setProductPrice(productPrice);

                        customerItem.setName(name);
                        customerItem.setAddress(address);
                        customerItem.setPhone_number(phone_number);
                        customerItem.setWebsite(website);
                        customerItem.setEmail(email);


                        if(logo.endsWith(".png") || logo.endsWith(".jpg") || logo.endsWith(".jpeg")){
                            customerItem.setLogo(company_image_path+logo);
                        }else{
                            customerItem.setLogo("/android_res/drawable/white_img.png");
                        }
                        customerItem.setCurrency_symbol(currency_symbol);




                        ArrayList<ProductMovementReportItem> customerReportItemArrayList = new ArrayList<>();

                        JSONArray statement = data.getJSONArray("product_movement");
                        if (statement.length() > 0) {
                            for (int i = 0; i < statement.length(); i++) {
                                JSONObject item = statement.getJSONObject(i);

                                String date_added = item.getString("date_added");
                                String date = item.getString("date");
                                String particulars = item.getString("particulars");
                                String quantity = item.getString("quantity");
                                String code = item.getString("code");
                                String total_quantity = item.getString("total_quantity");

                                ProductMovementReportItem customerReportItem = new ProductMovementReportItem();
                                customerReportItem.setDate_added(date_added);
                                customerReportItem.setDate(date);
                                customerReportItem.setParticulars(particulars);
                                customerReportItem.setQuantity(quantity);
                                customerReportItem.setCode(code);
                                customerReportItem.setTotal_quantity(total_quantity);

                                customerReportItemArrayList.add(customerReportItem);
                            }
                        }

                        productMovementtWeb(customerItem, customerReportItemArrayList);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    //avi.smoothToHide();
                    Log.e(TAG, "responsecompanyF"+ response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("false")) {
//                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }


    private void productMovementtWeb(CompanyItem customerItem, ArrayList<ProductMovementReportItem> customerReportItemArrayList) {

        double totalAmount = 0.0;

        double lastQuantity = 0.0;

        String name = "report/ProductMovementReport.html";
        String nameName = "file:///android_asset/report/ProductMovementReport.html";

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size(); i++) {
                cruncycode = customerItem.getCurrency_symbol();

                //String getQuantity = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(customerReportItemArrayList.get(i).getQuantity()));
                // String getTotal_quantity = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(customerReportItemArrayList.get(i).getTotal_quantity()));


                double getQuantity = Double.parseDouble(customerReportItemArrayList.get(i).getQuantity());
                String openingStockTxt = "";
                String purchasesTxt = "";
                String salesTxt = "";
                String wastageTxt = "";
                if(getQuantity == 0){
                    openingStockTxt = "";
                    purchasesTxt = "";
                    salesTxt = "";
                    wastageTxt = "";
                }else{
                    String valueSS = Utility.getPatternFormat(""+numberPostion, getQuantity);

                    if(customerReportItemArrayList.get(i).getCode().equalsIgnoreCase("product_quantity")){
                        openingStockTxt = valueSS;
                    } else if(customerReportItemArrayList.get(i).getCode().equalsIgnoreCase("purchase_order")){
                        purchasesTxt = valueSS;
                    } else if(customerReportItemArrayList.get(i).getCode().equalsIgnoreCase("invoice")){
                        salesTxt = valueSS;
                    } else if(customerReportItemArrayList.get(i).getCode().equalsIgnoreCase("credit")){
                        salesTxt = valueSS;
                    } else if(customerReportItemArrayList.get(i).getCode().equalsIgnoreCase("debit")){
                        salesTxt = valueSS;
                    }else if(customerReportItemArrayList.get(i).getCode().equalsIgnoreCase("wastage")){
                        wastageTxt = valueSS;
                    }



                }


                double getTotal_quantity = Double.parseDouble(customerReportItemArrayList.get(i).getTotal_quantity());
                String totalQuantityTxt = "";
                if(getTotal_quantity == 0){
                    totalQuantityTxt = "";
                }else{
                    String valueSS = Utility.getPatternFormat(""+numberPostion, getTotal_quantity);
                    totalQuantityTxt = valueSS;
                }


                productitem = IOUtils.toString(getAssets().open("report/product_movement_single_item.html"))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getDate())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getParticulars())
                        .replaceAll("#OpeningStock#", ""+openingStockTxt)
                        .replaceAll("#Purchases#", ""+purchasesTxt)
                        .replaceAll("#Sales#", ""+salesTxt)
                        .replaceAll("#Wastage#", ""+wastageTxt)
                        .replaceAll("#NetQuantity#", totalQuantityTxt);
                productitemlist = productitemlist + productitem;

//                double allAmount = 0.0;
//
//                try{
//                    allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getTotal_quantity());
//                }catch (Exception e){
//
//                }
//
//                totalAmount =   totalAmount + allAmount;

                //  String lastQuantityTxt = "";
                if(i == customerReportItemArrayList.size() - 1){
                    lastQuantity = Double.parseDouble(customerReportItemArrayList.get(i).getTotal_quantity());
//                    if(getLastQuantity == 0){
//                        lastQuantityTxt = "";
//                    }else{
//                        String valueSS = Utility.getPatternFormat(""+numberPostion, getLastQuantity);
//                        lastQuantityTxt = valueSS;
//                    }
                }


            }


            Log.e(TAG,"totalAmountFF "+totalAmount);


        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "customerItem.getLogo()"+customerItem.getLogo());


        // double quantity = Double.parseDouble(customerItem.getProductQuantity());
        double price = Double.parseDouble(customerItem.getProductPrice());

        //   String quantityAA = Utility.getPatternFormat(""+numberPostion, quantity);
        String priceAA = Utility.getPatternFormat(""+numberPostion, price);


        String lastQuantityTxt ="";
        if(lastQuantity == 0){
            lastQuantityTxt = "";
        }else{
            String valueSS = Utility.getPatternFormat(""+numberPostion, lastQuantity);
            lastQuantityTxt = valueSS;
        }

        String priceTxt ="";
        if(price == 0){
            priceTxt = "";
        }else{
            String valueSS = Utility.getPatternFormat(""+numberPostion, price)+Utility.getReplaceDollor(cruncycode);
            priceTxt = valueSS;
        }

        double totalPrice = lastQuantity * price;

        String totalPriceTxt ="";
        if(totalPrice == 0){
            totalPriceTxt = "";
        }else{
            String valueSS = Utility.getPatternFormat(""+numberPostion, totalPrice)+Utility.getReplaceDollor(cruncycode);
            totalPriceTxt = valueSS;
        }


        StringBuilder stringBuilderBillTo = new StringBuilder();
        if(!customerItem.getAddress().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getAddress()+"</br>");
        }
        if(!customerItem.getPhone_number().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getPhone_number()+"</br>");
        }
        if(!customerItem.getWebsite().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getWebsite()+"</br>");
        }
        if(!customerItem.getEmail().equalsIgnoreCase("")){
            stringBuilderBillTo.append(customerItem.getEmail()+"");
        }

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))
                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", ""+stringBuilderBillTo.toString())
                    .replaceAll("Product Name", customerItem.getProductName())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getLogo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Amount1-", ""+lastQuantityTxt)
                    .replaceAll("Amount2-", ""+priceTxt)
                    .replaceAll("Total Amount-", ""+totalPriceTxt)
            ;


        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }


    private void createWebPrintJob(WebView webView) {

        //create object of print manager in your device
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        //create object of print adapter
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        //provide name to your newly generated pdf file
        String jobName = getString(R.string.app_name) + " Print Test";

        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize( PrintAttributes.MediaSize.ISO_A4);
        printManager.print(jobName, printAdapter, builder.build());
    }

}