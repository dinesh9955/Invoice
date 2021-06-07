package com.receipt.invoice.stock.sirproject.Report;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PDFPrint;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Home.Home_Activity;
import com.receipt.invoice.stock.sirproject.Home.Model.CompanyModel;
import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.InvoiceReminder.ViewInvoiceActivity;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.SublimePickerFragment;
import com.receipt.invoice.stock.sirproject.Utils.Utility;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ReportViewActivity extends BaseActivity {

    private static final String TAG = "ViewInvoice_Activity";



    File fileWithinMyDir = null;

    WebView invoiceweb;

    int positionNext = -1;
    String company_image_path = "";

    String fileName = "";

//    String companyName = "";
//    String companyAddress = "";
//    String companyContactNo = "";
//    String companyWebsite = "";
//    String companyEmail = "";
//    String companyLogo = "";

public ArrayList<String> arrayListFilter = new ArrayList<>();

    String customer_id = "";
    String supplier_id = "";
    String company_id = "";
    String product_id = "";

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);

        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);
        ImageView printimg = toolbar.findViewById(R.id.imageViewptint);
        printimg.setImageResource(R.drawable.ic_options);
        printimg.setVisibility(View.VISIBLE);



        invoiceweb = findViewById(R.id.invoiceweb);

        titleView.setText("Preview Report");



//        invoiceweb.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return false;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//
//                //if page loaded successfully then show print button
//                //findViewById(R.id.fab).setVisibility(View.VISIBLE);
//            }
//        });


//        printimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createWebPrintJob(invoiceweb);
//            }
//        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();


        positionNext = bundle.getInt("positionNext");
        company_image_path = bundle.getString("company_image_path");
        if(positionNext == 0){
            fileName = "CustomerStatementReport";
            customer_id = bundle.getString("customer_id");
            customerReport(customer_id, "", null);
        } else if(positionNext == 1){
            fileName = "SupplierStatementReport";
            supplier_id = bundle.getString("supplier_id");
            supplierReport(supplier_id, "", null);
        } else if(positionNext == 2){
            fileName = "TotalSalesReport";
            company_id = bundle.getString("company_id");
            totalSalesReport(company_id, "", null);
        } else if(positionNext == 3){
            fileName = "TotalPurchaseReport";
            company_id = bundle.getString("company_id");
            totalPurchaseReport(company_id, "", null);
        } else if(positionNext == 4){
            fileName = "CustomerAgeingReport";
            company_id = bundle.getString("company_id");
            customerAgeingReport(company_id,"");
        } else if(positionNext == 5){
            fileName = "TaxCollecteReport";
            company_id = bundle.getString("company_id");
            taxCollectedReport(company_id, "", null);
        } else if(positionNext == 6){
            fileName = "StockReport";
            company_id = bundle.getString("company_id");
            stockReport(company_id, "");
        } else if(positionNext == 7){
            fileName = "ProductMovementReport";
            product_id = bundle.getString("product_id");
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

        addFilterData(positionNext);

        printimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView mRecyclerView;
                MenuAdapter mAdapter;

                final Dialog mybuilder = new Dialog(ReportViewActivity.this);
                mybuilder.setContentView(R.layout.report_dialog_list);

                TextView txtcancelvalue = (TextView) mybuilder.findViewById(R.id.txtcancelvalue);
                txtcancelvalue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mybuilder.dismiss();
                    }
                });
                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(ReportViewActivity.this, LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter(arrayListFilter, mybuilder);
                mRecyclerView.setAdapter(mAdapter);

                mybuilder.show();
                mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                Window window = mybuilder.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(R.color.transparent);
            }
        });






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
                final File savedPDFFile = FileManager.getInstance().createTempFile(ReportViewActivity.this, fileName+".pdf", true);
                PDFUtil.generatePDFFromWebView(savedPDFFile, invoiceweb, new PDFPrint.OnPDFPrintListener() {
                    @Override
                    public void onSuccess(File file) {
                        Log.e(TAG, "fileWithinMyDir "+fileWithinMyDir);
                        fileWithinMyDir = file;
                        //Intent intentShareFile = new Intent(Intent.ACTION_SEND);

                    }
                    @Override
                    public void onError(Exception exception) {
                        exception.printStackTrace();
                        Log.e(TAG, "exception::: "+exception.getMessage());
                    }
                });

            }
        });


    }

    private void addFilterData(int positionNext) {
        arrayListFilter.add("Preview it");
        arrayListFilter.add("Print");
        if(positionNext == 0){
            arrayListFilter.add("Export/Share");
            arrayListFilter.add("Filter By Date");
            arrayListFilter.add("Remove Filter");
        } else if(positionNext == 1){
            arrayListFilter.add("Export/Share");
            arrayListFilter.add("Filter By Date");
            arrayListFilter.add("Remove Filter");
        } else if(positionNext == 2){
            arrayListFilter.add("Export/Share");
            arrayListFilter.add("Filter By Date");
            arrayListFilter.add("By Customer Name");
            arrayListFilter.add("Paid");
            arrayListFilter.add("UnPaid");
            arrayListFilter.add("Remove Filter");
        } else if(positionNext == 3){
            arrayListFilter.add("Export/Share");
            arrayListFilter.add("Filter By Date");
            arrayListFilter.add("By Supplier");
            arrayListFilter.add("Remove Filter");
        } else if(positionNext == 4){
            arrayListFilter.add("Export/Share");
            arrayListFilter.add("By Customer Name");
            arrayListFilter.add("Remove Filter");
        } else if(positionNext == 5){
            arrayListFilter.add("Export/Share");
            arrayListFilter.add("Filter By Date");
            arrayListFilter.add("By Tax Name");
            arrayListFilter.add("Remove Filter");
        } else if(positionNext == 6){
            arrayListFilter.add("Export/Share");
            arrayListFilter.add("In Stock");
            arrayListFilter.add("Reorder");
            arrayListFilter.add("Filter By Product Name");
            arrayListFilter.add("Remove Filter");
        } else if(positionNext == 7){
            arrayListFilter.add("Export/Share");
        } else{

        }
    }


    private void customerReport(String customer_id, String filterID, SelectedDate selectedDate) {
        RequestParams params = new RequestParams();
        params.add("customer_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "report/customerStatement", params, new AsyncHttpResponseHandler() {
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

//                                customerReportItemArrayList.add(customerReportItem);

                                if(filterID.equalsIgnoreCase("date")){
                                    if(selectedDate != null){

                                        try{
                                            Date resultSS = new Date(DateFormat.getDateInstance().format(selectedDate.getStartDate().getTimeInMillis()));
                                            Date resultEE = new Date(DateFormat.getDateInstance().format(selectedDate.getEndDate().getTimeInMillis()));

                                            DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");

                                            Date date = simple.parse(created_date);

//                                            if(date.getTime() == resultSS.getTime()){
//                                                customerReportItemArrayList.add(customerReportItem);
//                                            }

                                            if(date.getTime() >= resultSS.getTime() && resultEE.getTime() >= date.getTime()){
                                                Log.e(TAG, "datemillis1 "+simple.format(resultSS));
                                                Log.e(TAG, "datemillis2 "+simple.format(resultEE));
                                                Log.e(TAG, "datemillis3 "+simple.format(date));
                                                customerReportItemArrayList.add(customerReportItem);
                                            }

                                        }catch (Exception e){

                                        }
                                    }
                                }else{
                                    customerReportItemArrayList.add(customerReportItem);
                                }



                            }
                        }



                        if(filterID.equalsIgnoreCase("date")){
                            Collections.sort(customerReportItemArrayList, new Comparator<CustomerReportItem>() {
                                public int compare(CustomerReportItem o1, CustomerReportItem o2) {
                                    return o1.getCreated_date().compareTo(o2.getCreated_date());
                                }
                            });
                        }

                        //Collections.reverse(customerReportItemArrayList);


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

//                if(getDebit == 0){
//                    stringBalance = "";
//                }else{
                String stringBalance1 = customerReportItemArrayList.get(i).getBalance();
                stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
//                }



                productitem = IOUtils.toString(getAssets().open("report/customer_single_item.html"))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getCreated_date())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getParticular())
                        .replaceAll("#DebitAmount#", stringDebit)
                        .replaceAll("#CreditAmount#", stringCredit)
                        .replaceAll("#Balance#", stringBalance);
                productitemlist = productitemlist + productitem;

                double allAmount = 0.0;

                try{
                    allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());
                }catch (Exception e){

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

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))
                            .replaceAll("Company Name", customerItem.getCompany_name())
                            .replaceAll("Address", customerItem.getCompany_address())
                            .replaceAll("Contact No.", customerItem.getCompany_phone())
                            .replaceAll("Website", customerItem.getCompany_website())
                            .replaceAll("Email", customerItem.getCompany_email())
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



    private void supplierReport(String customer_id, String filterID, SelectedDate selectedDate) {
        RequestParams params = new RequestParams();
        params.add("supplier_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "report/supplierStatement", params, new AsyncHttpResponseHandler() {
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

                                if(filterID.equalsIgnoreCase("date")){
                                    if(selectedDate != null){
                                        try{
                                            Date resultSS = new Date(DateFormat.getDateInstance().format(selectedDate.getStartDate().getTimeInMillis()));
                                            Date resultEE = new Date(DateFormat.getDateInstance().format(selectedDate.getEndDate().getTimeInMillis()));
                                            DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = simple.parse(created_date);
                                            if(date.getTime() >= resultSS.getTime() && resultEE.getTime() >= date.getTime()){
                                                Log.e(TAG, "datemillis1 "+simple.format(resultSS));
                                                Log.e(TAG, "datemillis2 "+simple.format(resultEE));
                                                Log.e(TAG, "datemillis3 "+simple.format(date));
                                                customerReportItemArrayList.add(customerReportItem);
                                            }
                                        }catch (Exception e){
                                        }
                                    }
                                }else{
                                    customerReportItemArrayList.add(customerReportItem);
                                }
                            }
                        }

                        if(filterID.equalsIgnoreCase("date")){
                            Collections.sort(customerReportItemArrayList, new Comparator<CustomerReportItem>() {
                                public int compare(CustomerReportItem o1, CustomerReportItem o2) {
                                    return o1.getCreated_date().compareTo(o2.getCreated_date());
                                }
                            });
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
                    String stringCredit1 = customerReportItemArrayList.get(i).getDebit();
                    stringCredit = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringCredit1)) + Utility.getReplaceDollor(cruncycode);
                }

//                if(getDebit == 0){
//                    stringBalance = "";
//                }else{
                String stringBalance1 = customerReportItemArrayList.get(i).getBalance();
                stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
//                }


                productitem = IOUtils.toString(getAssets().open("report/customer_single_item.html"))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getCreated_date())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getParticular())
                        .replaceAll("#DebitAmount#", stringDebit)
                        .replaceAll("#CreditAmount#", stringCredit)
                        .replaceAll("#Balance#", stringBalance);
                productitemlist = productitemlist + productitem;

                double allAmount = 0.0;

                try{
                    allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());
                }catch (Exception e){

                }

                totalAmount =   totalAmount + allAmount;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    //    DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))

                    .replaceAll("Company Name", customerItem.getCompany_name())
                    .replaceAll("Address", customerItem.getCompany_address())
                    .replaceAll("Contact No.", customerItem.getCompany_phone())
                    .replaceAll("Website", customerItem.getCompany_website())
                    .replaceAll("Email", customerItem.getCompany_email())
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



    private void totalSalesReport(String customer_id, String filterID, SelectedDate selectedDate) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "report/totalSales", params, new AsyncHttpResponseHandler() {
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

                                if(filterID.equalsIgnoreCase("date")){
                                    if(selectedDate != null){
                                        try{
                                            Date resultSS = new Date(DateFormat.getDateInstance().format(selectedDate.getStartDate().getTimeInMillis()));
                                            Date resultEE = new Date(DateFormat.getDateInstance().format(selectedDate.getEndDate().getTimeInMillis()));
                                            DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = simple.parse(invoice_date);
                                            if(date.getTime() >= resultSS.getTime() && resultEE.getTime() >= date.getTime()){
                                                Log.e(TAG, "datemillis1 "+simple.format(resultSS));
                                                Log.e(TAG, "datemillis2 "+simple.format(resultEE));
                                                Log.e(TAG, "datemillis3 "+simple.format(date));
                                                customerReportItemArrayList.add(customerReportItem);
                                            }
                                        }catch (Exception e){
                                        }
                                    }
                                }else{
                                    customerReportItemArrayList.add(customerReportItem);
                                }
                            }
                        }

                        if(filterID.equalsIgnoreCase("date")){
                            Collections.sort(customerReportItemArrayList, new Comparator<TotalSalesReportItem>() {
                                public int compare(TotalSalesReportItem o1, TotalSalesReportItem o2) {
                                    return o1.getInvoice_date().compareTo(o2.getInvoice_date());
                                }
                            });
                        } else if(filterID.equalsIgnoreCase("customer")){
                            Collections.sort(customerReportItemArrayList, new Comparator<TotalSalesReportItem>() {
                                public int compare(TotalSalesReportItem o1, TotalSalesReportItem o2) {
                                    return o1.getCustomer_name().compareTo(o2.getCustomer_name());
                                }
                            });
                        } else if(filterID.equalsIgnoreCase("paid")){
//                            Collections.sort(customerReportItemArrayList, new Comparator<TotalSalesReportItem>() {
//                                public int compare(TotalSalesReportItem o1, TotalSalesReportItem o2) {
//                                    return o1.getCustomer_name().compareTo(o2.getCustomer_name());
//                                }
//                            });
                        } else if(filterID.equalsIgnoreCase("unpaid")){

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
//                double getDebit = Double.parseDouble(customerReportItemArrayList.get(i).getDebit());
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


//                if(getDebit == 0){
//                    stringBalance = "";
//                }else{
                String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
//                }


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


        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))

                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", customerItem.getAddress())
                    .replaceAll("Contact No.", customerItem.getPhone_number())
                    .replaceAll("Website", customerItem.getWebsite())
                    .replaceAll("Email", customerItem.getEmail())
                   // .replaceAll("Customer Name", customerItem.getSupplier_name())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getLogo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Total Amount-", Utility.getPatternFormat(""+numberPostion, totalAmount)+ Utility.getReplaceDollor(cruncycode))
            ;


        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }



    private void totalPurchaseReport(String customer_id, String filterID, SelectedDate selectedDate) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "report/totalPurchases", params, new AsyncHttpResponseHandler() {
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

                                if(filterID.equalsIgnoreCase("date")){
                                    if(selectedDate != null){
                                        try{
                                            Date resultSS = new Date(DateFormat.getDateInstance().format(selectedDate.getStartDate().getTimeInMillis()));
                                            Date resultEE = new Date(DateFormat.getDateInstance().format(selectedDate.getEndDate().getTimeInMillis()));
                                            DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = simple.parse(order_date);
                                            if(date.getTime() >= resultSS.getTime() && resultEE.getTime() >= date.getTime()){
                                                Log.e(TAG, "datemillis1 "+simple.format(resultSS));
                                                Log.e(TAG, "datemillis2 "+simple.format(resultEE));
                                                Log.e(TAG, "datemillis3 "+simple.format(date));
                                                customerReportItemArrayList.add(customerReportItem);
                                            }
                                        }catch (Exception e){
                                        }
                                    }
                                }else{
                                    customerReportItemArrayList.add(customerReportItem);
                                }                            }
                        }

                        if(filterID.equalsIgnoreCase("date")){
                            Collections.sort(customerReportItemArrayList, new Comparator<TotalPurchaseReportItem>() {
                                public int compare(TotalPurchaseReportItem o1, TotalPurchaseReportItem o2) {
                                    return o1.getOrder_date().compareTo(o2.getOrder_date());
                                }
                            });
                        } else if(filterID.equalsIgnoreCase("supplier")){
                            Collections.sort(customerReportItemArrayList, new Comparator<TotalPurchaseReportItem>() {
                                public int compare(TotalPurchaseReportItem o1, TotalPurchaseReportItem o2) {
                                    return o1.getSupplier_name().compareTo(o2.getSupplier_name());
                                }
                            });
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
//                double getDebit = Double.parseDouble(customerReportItemArrayList.get(i).getDebit());
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


//                if(getDebit == 0){
//                    stringBalance = "";
//                }else{
                String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
//                }


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

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))

                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", customerItem.getAddress())
                    .replaceAll("Contact No.", customerItem.getPhone_number())
                    .replaceAll("Website", customerItem.getWebsite())
                    .replaceAll("Email", customerItem.getEmail())
                    // .replaceAll("Customer Name", customerItem.getSupplier_name())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getLogo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Total Amount-", Utility.getPatternFormat(""+numberPostion, totalAmount)+ Utility.getReplaceDollor(cruncycode))
            ;


        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }



    private void customerAgeingReport(String customer_id, String filterID) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "report/customerAgeing", params, new AsyncHttpResponseHandler() {
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

                        if(filterID.equalsIgnoreCase("customer")){
                            Collections.sort(customerReportItemArrayList, new Comparator<CustomerAgeingReportItem>() {
                                public int compare(CustomerAgeingReportItem o1, CustomerAgeingReportItem o2) {
                                    return o1.getCustomer_name().compareTo(o2.getCustomer_name());
                                }
                            });
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
                    stringBalance = "";
                }else{
                String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                    stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }

                String stringNotDue1 = customerReportItemArrayList.get(i).getNot_due();
                String stringNotDue11 = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringNotDue1)) + Utility.getReplaceDollor(cruncycode);


                productitem = IOUtils.toString(getAssets().open("report/customer_ageing_single_item.html"))
                        .replaceAll("#CustomerName#", customerReportItemArrayList.get(i).getContact_name())
                        .replaceAll("#Currentdue#", stringNotDue11)
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

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))

                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", customerItem.getAddress())
                    .replaceAll("Contact No.", customerItem.getPhone_number())
                    .replaceAll("Website", customerItem.getWebsite())
                    .replaceAll("Email", customerItem.getEmail())
                    // .replaceAll("Customer Name", customerItem.getSupplier_name())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getLogo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Total Amount-", Utility.getPatternFormat(""+numberPostion, totalAmount)+ Utility.getReplaceDollor(cruncycode))
            ;


        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }



    private void taxCollectedReport(String customer_id, String filterID, SelectedDate selectedDate) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "report/taxation", params, new AsyncHttpResponseHandler() {
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


                                if(filterID.equalsIgnoreCase("date")){
                                    if(selectedDate != null){
                                        try{
                                            Date resultSS = new Date(DateFormat.getDateInstance().format(selectedDate.getStartDate().getTimeInMillis()));
                                            Date resultEE = new Date(DateFormat.getDateInstance().format(selectedDate.getEndDate().getTimeInMillis()));
                                            DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = simple.parse(date_added);
                                            if(date.getTime() >= resultSS.getTime() && resultEE.getTime() >= date.getTime()){
                                                Log.e(TAG, "datemillis1 "+simple.format(resultSS));
                                                Log.e(TAG, "datemillis2 "+simple.format(resultEE));
                                                Log.e(TAG, "datemillis3 "+simple.format(date));
                                                customerReportItemArrayList.add(customerReportItem);
                                            }
                                        }catch (Exception e){
                                        }
                                    }
                                }else{
                                    customerReportItemArrayList.add(customerReportItem);
                                }                            }
                        }

                        if(filterID.equalsIgnoreCase("date")){
                            Collections.sort(customerReportItemArrayList, new Comparator<TaxCollectedReportItem>() {
                                public int compare(TaxCollectedReportItem o1, TaxCollectedReportItem o2) {
                                    return o1.getDate_added().compareTo(o2.getDate_added());
                                }
                            });
                        }else if(filterID.equalsIgnoreCase("tax")){
                            Collections.sort(customerReportItemArrayList, new Comparator<TaxCollectedReportItem>() {
                                public int compare(TaxCollectedReportItem o1, TaxCollectedReportItem o2) {
                                    return o1.getTax_name().compareTo(o2.getTax_name());
                                }
                            });
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

                String stringTAX1 = customerReportItemArrayList.get(i).getTax_rate();
                String stringTAX = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringTAX1));

                String stringAmount1 = customerReportItemArrayList.get(i).getTax_amount();
                String stringAmount = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringAmount1));

                productitem = IOUtils.toString(getAssets().open("report/tax_collection_single_item.html"))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getDate_added())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getParticulars())
                        .replaceAll("#CustomerName#", customerReportItemArrayList.get(i).getCustomer_name())
                        .replaceAll("#TaxName#", customerReportItemArrayList.get(i).getTax_name())
                        .replaceAll("#TaxRate#", stringTAX +"%")
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

      //  DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))

                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", customerItem.getAddress())
                    .replaceAll("Contact No.", customerItem.getPhone_number())
                    .replaceAll("Website", customerItem.getWebsite())
                    .replaceAll("Email", customerItem.getEmail())
                    // .replaceAll("Customer Name", customerItem.getSupplier_name())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getLogo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Total Amount-", Utility.getPatternFormat(""+numberPostion, totalAmount) + Utility.getReplaceDollor(cruncycode) + "%")
            ;


        } catch (IOException e) {
            e.printStackTrace();

        }



        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

    }



    private void stockReport(String customer_id, String filterID) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "report/stock", params, new AsyncHttpResponseHandler() {
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

                        if(filterID.equalsIgnoreCase("reorder")){
                            Collections.sort(customerReportItemArrayList, new Comparator<StockReportItem>() {
                                public int compare(StockReportItem o1, StockReportItem o2) {
                                    return o1.getMinimum().compareTo(o2.getMinimum());
                                }
                            });
                        }else if(filterID.equalsIgnoreCase("product")){
                            Collections.sort(customerReportItemArrayList, new Comparator<StockReportItem>() {
                                public int compare(StockReportItem o1, StockReportItem o2) {
                                    return o1.getName().compareTo(o2.getName());
                                }
                            });
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
//                double slab3 = Double.parseDouble(customerReportItemArrayList.get(i).getSlab3());
//                double slab4 = Double.parseDouble(customerReportItemArrayList.get(i).getSlab4());

                String minimumSS = Utility.getPatternFormat(""+numberPostion, minimum);
                String quantitySS = Utility.getPatternFormat(""+numberPostion, quantity);


                String minimumTxt = "";

                String stringBalance = "";

                if(minimum == 0){
                    minimumTxt = "";
                }else{
                    minimumTxt = minimumSS + Utility.getReplaceDollor(cruncycode);
                }


                String colorCode = "#ff0000";
                if(customerReportItemArrayList.get(i).getQuantity_status().equalsIgnoreCase("In Stock")){
                    colorCode = "#159f5c";
                }else{
                    colorCode = "#ff0000";
                }
//
//                if(slab4 == 0){
//                    slab4Txt = "";
//                }else{
//                    slab4Txt = slab4 + Utility.getReplaceDollor(cruncycode);
//                }


//                if(getDebit == 0){
//                    stringBalance = "";
//                }else{
                //stringBalance = customerReportItemArrayList.get(i).getTax_amount() + Utility.getReplaceDollor(cruncycode);
//                }

                String priceS = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(customerReportItemArrayList.get(i).getPrice()));
                String valueS = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(customerReportItemArrayList.get(i).getValue()));


                productitem = IOUtils.toString(getAssets().open("report/stock_single_item.html"))
                        .replaceAll("#SNo#", ""+(i+1))
                        .replaceAll("#Product#", customerReportItemArrayList.get(i).getName())
                        .replaceAll("#ReorderLevel#", minimumTxt)
                        .replaceAll("#QuantityAvaliable#", ""+quantitySS)
                        .replaceAll("#Status#", customerReportItemArrayList.get(i).getQuantity_status())
                        .replaceAll("white1", colorCode)
                        .replaceAll("#PerUnitPrice#", priceS + Utility.getReplaceDollor(cruncycode))
                        .replaceAll("#InventoryValue#", valueS + Utility.getReplaceDollor(cruncycode));
                productitemlist = productitemlist + productitem;

                double allAmount = 0.0;

                try{
                    allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getValue());
                }catch (Exception e){

                }

                totalAmount =   totalAmount + allAmount;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        //DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))

                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", customerItem.getAddress())
                    .replaceAll("Contact No.", customerItem.getPhone_number())
                    .replaceAll("Website", customerItem.getWebsite())
                    .replaceAll("Email", customerItem.getEmail())
                    // .replaceAll("Customer Name", customerItem.getSupplier_name())
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

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "report/productMovement", params, new AsyncHttpResponseHandler() {
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

        String name = "report/ProductMovementReport.html";
        String nameName = "file:///android_asset/report/ProductMovementReport.html";

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size(); i++) {
                cruncycode = customerItem.getCurrency_symbol();

                String getQuantity = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(customerReportItemArrayList.get(i).getQuantity()));
                String getTotal_quantity = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(customerReportItemArrayList.get(i).getTotal_quantity()));

                productitem = IOUtils.toString(getAssets().open("report/product_movement_single_item.html"))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getDate())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getParticulars())
                        .replaceAll("#OpeningStock#", getQuantity)
                        .replaceAll("#Purchases#", "")
                        .replaceAll("#Sales#", "")
                        .replaceAll("#Wastage#", "")
                        .replaceAll("#NetQuantity#", getTotal_quantity);
                productitemlist = productitemlist + productitem;

                double allAmount = 0.0;

                try{
                    allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getTotal_quantity());
                }catch (Exception e){

                }

                totalAmount =   totalAmount + allAmount;
            }


            Log.e(TAG,"totalAmountFF "+totalAmount);


        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "customerItem.getLogo()"+customerItem.getLogo());


        double quantity = Double.parseDouble(customerItem.getProductQuantity());
        double price = Double.parseDouble(customerItem.getProductPrice());

        String quantityAA = Utility.getPatternFormat(""+numberPostion, quantity);
        String priceAA = Utility.getPatternFormat(""+numberPostion, price);

       // DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))

                    .replaceAll("Company Name", customerItem.getName())
                    .replaceAll("Address", customerItem.getAddress())
                    .replaceAll("Contact No.", customerItem.getPhone_number())
                    .replaceAll("Website", customerItem.getWebsite())
                    .replaceAll("Email", customerItem.getEmail())
                     .replaceAll("Product Name", customerItem.getProductName())
                    .replaceAll("#LOGO_IMAGE#", customerItem.getLogo())
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("Amount1-", ""+quantityAA)
                    .replaceAll("Amount2-", priceAA+""+Utility.getReplaceDollor(cruncycode))
                    .replaceAll("Total Amount-", Utility.getPatternFormat(""+numberPostion, totalAmount) + Utility.getReplaceDollor(cruncycode))
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












    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

        private static final String TAG = "MenuAdapter";

        ArrayList<String> alName = new ArrayList<>();
        Dialog mybuilder;

        public MenuAdapter(ArrayList<String> arrayList, Dialog mybuilder) {
            super();
            this.alName = arrayList;
            this.mybuilder = mybuilder;
        }

        @Override
        public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.menu_item, viewGroup, false);
//            SellerFeedbackAdapter.ViewHolder viewHolder = new SellerFeedbackAdapter.ViewHolder(v);
//            return viewHolder;


//            val v = LayoutInflater.from(viewGroup.context)
//                    .inflate(R.layout.mybooking_item, viewGroup, false)
            return new MenuAdapter.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final MenuAdapter.ViewHolder viewHolder, final int i) {

            viewHolder.textViewName.setText(""+alName.get(i));
            viewHolder.textViewName.setTextColor(getColor(R.color.light_blue));

            viewHolder.textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mybuilder.dismiss();
                    menuClickByPosition(positionNext, i);
                }
            });

        }


        @Override
        public int getItemCount() {
            return alName.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            View view11 = null;
            TextView textViewName;
            public ViewHolder(View itemView) {
                super(itemView);
                view11 = itemView;
                //  imageViewImage = (ImageView) itemView.findViewById(R.id.profile_image);
                textViewName = (TextView) itemView.findViewById(R.id.txtList);

            }


       }



        public void updateData(ArrayList<String> arrayList2) {
            // TODO Auto-generated method stub
            alName = arrayList2;
            notifyDataSetChanged();
        }
    }


    private void menuClickByPosition(int positionNext, int i) {
        Intent intent = new Intent(ReportViewActivity.this, PreviewItActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("positionNext", positionNext);
        intent.putExtra("company_image_path", company_image_path);
        if(positionNext == 0){
            if(i == 0){
                intent.putExtra("customer_id", customer_id);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb("Customer Statement Report");
            }else if(i == 3){
                //customerReport(customer_id, "date");
                customerFilterDate(customer_id, positionNext);
            }else if(i == 4){
                customerReport(customer_id, "", null);
            }
        } else if(positionNext == 1){
            if(i == 0){
                intent.putExtra("supplier_id", supplier_id);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb("Supplier Statement Report");
            }else if(i == 3){
                customerFilterDate(supplier_id, positionNext);
            }else if(i == 4){
                supplierReport(supplier_id, "", null);
            }
        } else if(positionNext == 2){
            if(i == 0){
                intent.putExtra("company_id", company_id);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb("Total Sales Report");
            }else if(i == 3){
                customerFilterDate(company_id, positionNext);
            }else if(i == 4){
                totalSalesReport(company_id, "customer", null);
            }else if(i == 5){
                totalSalesReport(company_id, "paid", null);
            }else if(i == 6){
                totalSalesReport(company_id, "unpaid", null);
            }else if(i == 7){
                totalSalesReport(company_id, "", null);
            }
        } else if(positionNext == 3){
            if(i == 0){
                intent.putExtra("company_id", company_id);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb("Total Purchase Report");
            }else if(i == 3){
//                totalPurchaseReport(company_id, "date");
                customerFilterDate(company_id, positionNext);
            }else if(i == 4){
                totalPurchaseReport(company_id, "supplier", null);
            }else if(i == 5){
                totalPurchaseReport(company_id, "", null);
            }
        } else if(positionNext == 4){
            if(i == 0){
                intent.putExtra("company_id", company_id);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb("Customer Ageing Report");
            }else if(i == 3){
                customerAgeingReport(company_id, "customer");
            }else if(i == 4){
                customerAgeingReport(company_id, "");
            }
        } else if(positionNext == 5){
            if(i == 0){
                intent.putExtra("company_id", company_id);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb("Tax Collected Report");
            }else if(i == 3){
                //taxCollectedReport(company_id, "date");
                customerFilterDate(company_id, positionNext);
            }else if(i == 4){
                taxCollectedReport(company_id, "tax", null);
            }else if(i == 5){
                taxCollectedReport(company_id, "", null);
            }
        } else if(positionNext == 6){
            if(i == 0){
                intent.putExtra("company_id", company_id);
                startActivity(intent); startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb("Stock Report");
            }else if(i == 3){
                stockReport(company_id, "instock");
            }else if(i == 4){
                stockReport(company_id, "reorder");
            }else if(i == 5){
                stockReport(company_id, "product");
            }else if(i == 6){
                stockReport(company_id, "");
            }
        } else if(positionNext == 7){
            if(i == 0){
                intent.putExtra("product_id", customer_id);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb("Product Movement Report");
            }
        } else{

        }


    }


    private void shareWeb(String title) {
        Log.e(TAG, "title "+title);

        if(fileWithinMyDir.exists()) {
            Log.e(TAG, "FILENAME" +fileWithinMyDir);
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            // File fileWithinMyDir = new File(message);
            Uri photoURI = FileProvider.getUriForFile(ReportViewActivity.this, "com.receipt.invoice.stock.sirproject.provider", fileWithinMyDir);
            if(fileWithinMyDir.exists()) {
                intentShareFile.setType("application/pdf");
                //Uri outputFileUri = Uri.fromFile(fileWithinMyDir);
                intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, title);
                startActivity(Intent.createChooser(intentShareFile, "Share File"));
            }
        }

    }



    private void customerFilterDate(String customer_id, int position) {
        SublimePickerFragment pickerFrag = new SublimePickerFragment();
        pickerFrag.setCallback(new SublimePickerFragment.Callback() {
            @Override
            public void onCancelled() {

            }

            @Override
            public void onDateTimeRecurrenceSet(SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
                if (selectedDate != null) {
                    if (selectedDate.getType() == SelectedDate.Type.SINGLE) {
    //                    Log.e(TAG, "YEARSS "+String.valueOf(selectedDate.getStartDate().get(Calendar.YEAR)));
    //                    Log.e(TAG, "MONTHSS "+String.valueOf(selectedDate.getStartDate().get(Calendar.MONTH)));
    //                    Log.e(TAG, "DAYSS "+String.valueOf(selectedDate.getStartDate().get(Calendar.DAY_OF_MONTH)));
    //                    String ddd = selectedDate.getStartDate().get(Calendar.YEAR) + "-" + (selectedDate.getStartDate().get(Calendar.MONTH)+1) + "-" + selectedDate.getStartDate().get(Calendar.DAY_OF_MONTH);

                    } else if (selectedDate.getType() == SelectedDate.Type.RANGE) {

//                        DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
//
//    //                    String fff = DateFormat.getDateInstance().format(selectedDate.getStartDate().getTime());
//    //                    Log.e(TAG, "fff "+fff);
//                        Date resultSS = new Date(DateFormat.getDateInstance().format(selectedDate.getStartDate().getTimeInMillis()));
//                        Log.e(TAG, "datemillis22 "+simple.format(resultSS));
//
//    //                    String eee = DateFormat.getDateInstance().format(selectedDate.getEndDate().getTime());
//    //                    Log.e(TAG, "eee "+eee);
//                        Date resultEE = new Date(DateFormat.getDateInstance().format(selectedDate.getEndDate().getTimeInMillis()));
//                        Log.e(TAG, "datemillis22 "+simple.format(resultEE));



                    }

                    Log.e(TAG, "positionCCCCC "+position);

                    if(position == 0){
                        customerReport(customer_id, "date", selectedDate);
                    } else if(position == 1){
                        supplierReport(customer_id, "date", selectedDate);
                    } else if(position == 2){
                        totalSalesReport(customer_id, "date", selectedDate);
                    }else if(position == 3){
                        totalPurchaseReport(customer_id, "date", selectedDate);
                    }else if(position == 4){
                        //customerReport(customer_id, "date", selectedDate);
                    }else if(position == 5){
                        taxCollectedReport(customer_id, "date", selectedDate);
                    }else if(position == 6){
                        //customerReport(customer_id, "date", selectedDate);
                    }else if(position == 7){
                        //customerReport(customer_id, "date", selectedDate);
                    }
                }
            }
        });
        android.util.Pair<Boolean, SublimeOptions> optionsPair = getOptions();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
        pickerFrag.setArguments(bundle);

        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
    }

    android.util.Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;

        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);

        options.setDisplayOptions(displayOptions);

        options.setCanPickDateRange(true);

        return new android.util.Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }



//
//    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
//        @Override
//        public void onCancelled() {
//            // rlDateTimeRecurrenceInfo.setVisibility(View.GONE);
//        }
//
//        @Override
//        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
//                                            int hourOfDay, int minute,
//                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
//                                            String recurrenceRule) {
//            if (selectedDate != null) {
//                if (selectedDate.getType() == SelectedDate.Type.SINGLE) {
////                tvYear.setText(applyBoldStyle("YEAR: ")
////                        .append(String.valueOf(mSelectedDate.getStartDate().get(Calendar.YEAR))));
////                tvMonth.setText(applyBoldStyle("MONTH: ")
////                        .append(String.valueOf(mSelectedDate.getStartDate().get(Calendar.MONTH))));
////                tvDay.setText(applyBoldStyle("DAY: ")
////                        .append(String.valueOf(mSelectedDate.getStartDate().get(Calendar.DAY_OF_MONTH))));
//                } else if (selectedDate.getType() == SelectedDate.Type.RANGE) {
////                llDateHolder.setVisibility(View.GONE);
////                llDateRangeHolder.setVisibility(View.VISIBLE);
////
////                tvStartDate.setText(applyBoldStyle("START: ")
////                        .append(DateFormat.getDateInstance().format(mSelectedDate.getStartDate().getTime())));
////                tvEndDate.setText(applyBoldStyle("END: ")
////                        .append(DateFormat.getDateInstance().format(mSelectedDate.getEndDate().getTime())));
//                }
//            }
//        }
//    };
//



}
