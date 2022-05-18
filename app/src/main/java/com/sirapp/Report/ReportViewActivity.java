package com.sirapp.Report;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.print.PDFPrint;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.Adapter.Customer_Bottom_Adapter;
import com.sirapp.Adapter.Product_Bottom_Adapter;
import com.sirapp.CN.CreditNotesViewActivityWebView;
import com.sirapp.Constant.Constant;
import com.sirapp.Customer.Customer_Activity;
import com.sirapp.Invoice.InvoiceViewActivityWebView;
import com.sirapp.Invoice.SavePref;
import com.sirapp.PV.EditEditPVActivity;
import com.sirapp.Product.Product_Activity;
import com.sirapp.Receipts.ViewReceipt_Activity;
import com.sirapp.Vendor.Vendor_Activity;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Model.Customer_list;
import com.sirapp.Model.InvoiceData;
import com.sirapp.Model.Product_list;
import com.sirapp.Model.Tax_List;
import com.sirapp.R;
import com.sirapp.Utils.Utility;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ReportViewActivity extends BaseActivity implements Customer_Bottom_Adapter.Callback, Product_Bottom_Adapter.Callback {

    private static final String TAG = "ViewInvoice_Activity";

    String CustomerReportHtml = "CustomerReport.html";
    String SupplierReportHtml = "SupplierReport.html";
    String SalesReportHtml = "SalesReport.html";
    String PurchaseReportHtml = "PurchaseReport.html";
    String CustomerAgingReportHtml = "CustomerAgingReport.html";
    String TaxCollectedReportHtml = "TaxCollectedReport.html";
    String StockReportHtml = "StockReport.html";
    String ProductMovementReportHtml = "ProductMovementReport.html";

    String customer_single_itemHtml = "customer_single_item.html";
    String Supplier_single_itemHtml = "customer_single_item.html";
    String total_sales_single_itemHtml = "total_sales_single_item.html";
    String total_purchase_single_itemHtml = "total_sales_single_item.html";
    String customer_ageing_single_itemHtml = "customer_ageing_single_item.html";
    String tax_collection_single_itemHtml = "tax_collection_single_item.html";
    String stock_single_itemHtml = "stock_single_item.html";
    String product_movement_single_itemHtml = "product_movement_single_item.html";


    BottomSheetDialog bottomSheetDialog, bottomSheetDialog3;

    String filterCustomerSupplier = "";

//    TextView txtcustomer, txtname, txtcontactname;
//    EditText search_customers;
//    RecyclerView recycler_customers;
//    Customer_Bottom_Adapter customer_bottom_adapter;


//    String customer_id = "";
//    String customer_name = "";
    ArrayList<Customer_list> customer_bottom = new ArrayList<>();

//    String supplier_id = "";
//    String supplier_name = "";
    ArrayList<Customer_list> supplier_bottom = new ArrayList<>();

    ArrayList<Product_list> product_bottom = new ArrayList<>();

    ArrayList<Tax_List> tax_list_array = new ArrayList<Tax_List>();

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

    String selectedCompanyId = "";


    CustomerItem customerItemAA = new CustomerItem();
    ArrayList<CustomerReportItem> customerReportItemArrayListAA = new ArrayList<>();

    SupplierItem supplierItemAA = new SupplierItem();
    ArrayList<CustomerReportItem> supplierReportItemArrayListAA = new ArrayList<>();

    CompanyItem totalSalesItemAA = new CompanyItem();
    ArrayList<TotalSalesReportItem> totalSalesReportItemArrayListAA = new ArrayList<>();

    CompanyItem totalPurchaseItemAA = new CompanyItem();
    ArrayList<TotalPurchaseReportItem> totalPurchaseReportItemArrayListAA = new ArrayList<>();

    CompanyItem customerAgeingItemAA = new CompanyItem();
    ArrayList<CustomerAgeingReportItem> customerAgeingReportItemArrayListAA = new ArrayList<>();

    CompanyItem taxCollectedItemAA = new CompanyItem();
    ArrayList<TaxCollectedReportItem> taxCollectedReportItemArrayListAA = new ArrayList<>();

    CompanyItem stockItemAA = new CompanyItem();
    ArrayList<StockReportItem> stockReportItemArrayListAA = new ArrayList<>();

    CompanyItem productMovementItemAA = new CompanyItem();
    ArrayList<ProductMovementReportItem> productMovementItemArrayListAA = new ArrayList<>();

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

        titleView.setText(getString(R.string.preview));

        bottomSheetDialog = new BottomSheetDialog(ReportViewActivity.this);
        bottomSheetDialog3 = new BottomSheetDialog(ReportViewActivity.this);

        if(AllSirApi.ALL_FONT == true){
            checkDevice();
        }

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
        selectedCompanyId = bundle.getString("company_id");



        positionNext = bundle.getInt("positionNext");
        company_image_path = bundle.getString("company_image_path");
        if(positionNext == 0){
            fileName = "CustomerStatementReport";
            customer_id = bundle.getString("customer_id");
            customerReport(customer_id, "", "", "");
        } else if(positionNext == 1){
            fileName = "SupplierStatementReport";
            supplier_id = bundle.getString("supplier_id");
            supplierReport(supplier_id, "", "", "");
        } else if(positionNext == 2){
            customer_list(selectedCompanyId);
            fileName = "TotalSalesReport";
            company_id = bundle.getString("company_id");
            totalSalesReportPaidUnpaid(selectedCompanyId, company_id, "", "", "", "");
        } else if(positionNext == 3){
            supplier_list(selectedCompanyId);
            fileName = "TotalPurchaseReport";
            company_id = bundle.getString("company_id");
            totalPurchaseReport(company_id, "", "", "", "");
        } else if(positionNext == 4){
            customer_list(selectedCompanyId);
            fileName = "CustomerAgeingReport";
            company_id = bundle.getString("company_id");
            customerAgeingReport(company_id,"", "");
        } else if(positionNext == 5){
            companyInformation(selectedCompanyId);
            fileName = "TaxCollecteReport";
            company_id = bundle.getString("company_id");
            taxCollectedReport(company_id, "", "", "", "");
        } else if(positionNext == 6){
            product_list(selectedCompanyId);
            fileName = "StockReport";
            company_id = bundle.getString("company_id");
            stockReport(company_id, "", "");
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
        //invoiceweb.getSettings().setTextSize(WebSettings.TextSize.LARGER);

//        webSettings.setDefaultFontSize(50);

//        invoiceweb.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        invoiceweb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setDefaultFontSize(50);
//        invoiceweb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

       // webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

//        if(AllSirApi.FONT_INVOICE == true){
//            webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE);
//        }else{
//            invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
//        }

//        callForWeb();

        invoiceweb.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed(); // Ignore SSL certificate errors
//            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
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




    private void checkDevice() {
        if(Utility.isTablet(ReportViewActivity.this) == true){
            CustomerReportHtml = "CustomerReport.html";
            SupplierReportHtml = "SupplierReport.html";
            SalesReportHtml = "SalesReport.html";
            PurchaseReportHtml = "PurchaseReport.html";
            CustomerAgingReportHtml = "CustomerAgingReport.html";
            TaxCollectedReportHtml = "TaxCollectedReport.html";
            StockReportHtml = "StockReport.html";
            ProductMovementReportHtml = "ProductMovementReport.html";

            customer_single_itemHtml = "customer_single_item.html";
            Supplier_single_itemHtml = "customer_single_item.html";
            total_sales_single_itemHtml = "total_sales_single_item.html";
            total_purchase_single_itemHtml = "total_sales_single_item.html";
            customer_ageing_single_itemHtml = "customer_ageing_single_item.html";
            tax_collection_single_itemHtml = "tax_collection_single_item.html";
            stock_single_itemHtml = "stock_single_item.html";
            product_movement_single_itemHtml = "product_movement_single_item.html";
        }else {
            String manufacturerModel = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + " " + Build.BRAND + " " + Build.DEVICE;
            if (manufacturerModel.toLowerCase().contains("j7")) {
                CustomerReportHtml = "5/CustomerReport.html";
                SupplierReportHtml = "5/SupplierReport.html";
                SalesReportHtml = "5/SalesReport.html";
                PurchaseReportHtml = "5/PurchaseReport.html";
                CustomerAgingReportHtml = "5/CustomerAgingReport.html";
                TaxCollectedReportHtml = "5/TaxCollectedReport.html";
                StockReportHtml = "5/StockReport.html";
                ProductMovementReportHtml = "5/ProductMovementReport.html";

                customer_single_itemHtml = "5/customer_single_item.html";
                Supplier_single_itemHtml = "5/customer_single_item.html";
                total_sales_single_itemHtml = "5/total_sales_single_item.html";
                total_purchase_single_itemHtml = "5/total_sales_single_item.html";
                customer_ageing_single_itemHtml = "5/customer_ageing_single_item.html";
                tax_collection_single_itemHtml = "5/tax_collection_single_item.html";
                stock_single_itemHtml = "5/stock_single_item.html";
                product_movement_single_itemHtml = "5/product_movement_single_item.html";
            } else {
                CustomerReportHtml = "6/CustomerReport.html";
                SupplierReportHtml = "6/SupplierReport.html";
                SalesReportHtml = "6/SalesReport.html";
                PurchaseReportHtml = "6/PurchaseReport.html";
                CustomerAgingReportHtml = "6/CustomerAgingReport.html";
                TaxCollectedReportHtml = "6/TaxCollectedReport.html";
                StockReportHtml = "6/StockReport.html";
                ProductMovementReportHtml = "6/ProductMovementReport.html";

                customer_single_itemHtml = "6/customer_single_item.html";
                Supplier_single_itemHtml = "6/customer_single_item.html";
                total_sales_single_itemHtml = "6/total_sales_single_item.html";
                total_purchase_single_itemHtml = "6/total_sales_single_item.html";
                customer_ageing_single_itemHtml = "6/customer_ageing_single_item.html";
                tax_collection_single_itemHtml = "6/tax_collection_single_item.html";
                stock_single_itemHtml = "6/stock_single_item.html";
                product_movement_single_itemHtml = "6/product_movement_single_item.html";
            }
        }
    }



    private void addFilterData(int positionNext) {
        arrayListFilter.add(getString(R.string.report_Previewit));
        arrayListFilter.add(getString(R.string.report_Print));
        if(positionNext == 0){
            arrayListFilter.add(getString(R.string.report_ExportShare));
            arrayListFilter.add(getString(R.string.report_FilterByDate));
            arrayListFilter.add(getString(R.string.report_RemoveFilter));
        } else if(positionNext == 1){
            arrayListFilter.add(getString(R.string.report_ExportShare));
            arrayListFilter.add(getString(R.string.report_FilterByDate));
            arrayListFilter.add(getString(R.string.report_RemoveFilter));
        } else if(positionNext == 2){
            arrayListFilter.add(getString(R.string.report_ExportShare));
            arrayListFilter.add(getString(R.string.report_FilterByDate));
            arrayListFilter.add(getString(R.string.reportByCustomerName));
            arrayListFilter.add(getString(R.string.report_Paid));
            arrayListFilter.add(getString(R.string.report_UnPaid));
            arrayListFilter.add(getString(R.string.report_RemoveFilter));
        } else if(positionNext == 3){
            arrayListFilter.add(getString(R.string.report_ExportShare));
            arrayListFilter.add(getString(R.string.report_FilterByDate));
            arrayListFilter.add(getString(R.string.report_BySupplier));
            arrayListFilter.add(getString(R.string.report_RemoveFilter));
        } else if(positionNext == 4){
            arrayListFilter.add(getString(R.string.report_ExportShare));
            arrayListFilter.add(getString(R.string.reportByCustomerName));
            arrayListFilter.add(getString(R.string.report_RemoveFilter));
        } else if(positionNext == 5){
            arrayListFilter.add(getString(R.string.report_ExportShare));
            arrayListFilter.add(getString(R.string.report_FilterByDate));
            arrayListFilter.add(getString(R.string.report_ByTaxName));
            arrayListFilter.add(getString(R.string.report_RemoveFilter));
        } else if(positionNext == 6){
            arrayListFilter.add(getString(R.string.report_ExportShare));
            arrayListFilter.add(getString(R.string.report_InStock));
            arrayListFilter.add(getString(R.string.report_Reorder));
            arrayListFilter.add(getString(R.string.report_FilterByProductName));
            arrayListFilter.add(getString(R.string.report_RemoveFilter));
        } else if(positionNext == 7){
            arrayListFilter.add(getString(R.string.report_ExportShare));
        } else{

        }
    }


    private void customerReport(String customer_id, String filterID, String fDate, String sDate) {
        RequestParams params = new RequestParams();
        params.add("customer_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
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

//                                customerReportItemArrayList.add(customerReportItem);

                                if(filterID.equalsIgnoreCase("date")){
                                    if(!fDate.equalsIgnoreCase("") && !sDate.equalsIgnoreCase("")){
                                        try{
                                            DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = simple.parse(created_date);

                                            Date fDateC = simple.parse(fDate);
                                            Date sDateC = simple.parse(sDate);

                                            if(date.getTime() >= fDateC.getTime() && sDateC.getTime() >= date.getTime()){
                                                Log.e(TAG, "datemillis1 "+simple.format(fDateC));
                                                Log.e(TAG, "datemillis2 "+simple.format(sDateC));
                                                Log.e(TAG, "datemillis3 "+simple.format(date));
                                                customerReportItemArrayList.add(customerReportItem);
                                            }
                                        }catch (Exception e){

                                        }

                                    }else{

                                    }

                                }else{
                                    customerReportItemArrayList.add(customerReportItem);
                                }



                            }
                        }



                       // if(filterID.equalsIgnoreCase("date")){
                            Collections.sort(customerReportItemArrayList, new Comparator<CustomerReportItem>() {
                                public int compare(CustomerReportItem o1, CustomerReportItem o2) {
                                    return o1.getCreated_date().compareTo(o2.getCreated_date());
                                }
                            });
                       // }

//                        Collections.reverse(customerReportItemArrayList);

                        customerItemAA = customerItem;
                        customerReportItemArrayListAA = customerReportItemArrayList;
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

        double balanceAmount = 0.0;

        String name = "report/"+CustomerReportHtml;
        String nameName = "file:///android_asset/report/"+CustomerReportHtml;

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size() ; i++) {
                cruncycode = customerItem.getCurrency_symbol();

//                DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                double getDebit = Double.parseDouble(customerReportItemArrayList.get(i).getDebit());
                double getCredit = Double.parseDouble(customerReportItemArrayList.get(i).getCredit());
               // double getBalance = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());

                String stringDebit = "";
                String stringCredit = "";
                String stringBalance = "";

                balanceAmount = balanceAmount + getDebit;
                Log.e(TAG, "balanceAmountAA "+balanceAmount);

                balanceAmount = balanceAmount - getCredit;
                Log.e(TAG, "balanceAmountBB "+balanceAmount);

                if(getDebit == 0){
                    stringDebit = "";
                }else{
                   // String stringDebit1 = customerReportItemArrayList.get(i).getDebit();
                    stringDebit = Utility.getPatternFormat(""+numberPostion, getDebit) + Utility.getReplaceDollor(cruncycode);
                }

                if(getCredit == 0){
                    stringCredit = "";
                }else{
                    //String stringCredit1 = customerReportItemArrayList.get(i).getCredit();
                    stringCredit = Utility.getPatternFormat(""+numberPostion, getCredit) + Utility.getReplaceDollor(cruncycode);
                }


                if(balanceAmount == 0){
                    stringBalance = Utility.getPatternFormat(""+numberPostion, balanceAmount) + Utility.getReplaceDollor(cruncycode);
                }else{
                    //String stringBalance1 = customerReportItemArrayList.get(i).getBalance();
                    stringBalance = Utility.getPatternFormat(""+numberPostion, balanceAmount) + Utility.getReplaceDollor(cruncycode);
                }






                productitem = IOUtils.toString(getAssets().open("report/"+customer_single_itemHtml))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getCreated_date())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getParticular())
                        .replaceAll("#DebitAmount#", stringDebit)
                        .replaceAll("#CreditAmount#", stringCredit)
                        .replaceAll("#Balance#", stringBalance);
                productitemlist = productitemlist + productitem;


//                double allAmount = 0.0;
//                if(i == customerReportItemArrayList.size() - 1){
//                    try{
//                        allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());
//                    }catch (Exception e){
//
//                    }
//                }
//                totalAmount =   totalAmount + allAmount;
            }

            totalAmount = balanceAmount;
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

                    .replaceAll("Customer Statement of Account", getString(R.string.report_CustomerStatementofAccount))
                    .replaceAll("Date", getString(R.string.list_Date))
                    .replaceAll("Particulars", getString(R.string.html_Particulars))
                    .replaceAll("Debit Amount", getString(R.string.html_DebitAmount))
                    .replaceAll("Credit Amount", getString(R.string.html_CreditAmount))
                    .replaceAll("Balance", getString(R.string.html_Balance))
                    .replaceAll("Total Amount_", getString(R.string.html_TotalAmount))


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



    private void supplierReport(String customer_id, String filterID, String fDate, String sDate) {
        RequestParams params = new RequestParams();
        params.add("supplier_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
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


                                if(filterID.equalsIgnoreCase("date")){
                                    if(!fDate.equalsIgnoreCase("") && !sDate.equalsIgnoreCase("")){
                                        try{
                                            DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = simple.parse(created_date);

                                            Date fDateC = simple.parse(fDate);
                                            Date sDateC = simple.parse(sDate);

                                            if(date.getTime() >= fDateC.getTime() && sDateC.getTime() >= date.getTime()){
                                                Log.e(TAG, "datemillis1 "+simple.format(fDateC));
                                                Log.e(TAG, "datemillis2 "+simple.format(sDateC));
                                                Log.e(TAG, "datemillis3 "+simple.format(date));
                                              //  if(!code.equalsIgnoreCase("purchase_order")){
                                                    customerReportItemArrayList.add(customerReportItem);
                                               // }
                                            }
                                        }catch (Exception e){

                                        }

                                    }else{

                                    }

                                }else{
                                  //   if(!code.equalsIgnoreCase("purchase_order")){
                                                    customerReportItemArrayList.add(customerReportItem);
                                  //   }
                                }

                            }
                        }

                    //    if(filterID.equalsIgnoreCase("date")){
                            Collections.sort(customerReportItemArrayList, new Comparator<CustomerReportItem>() {
                                public int compare(CustomerReportItem o1, CustomerReportItem o2) {
                                    return o1.getCreated_date().compareTo(o2.getCreated_date());
                                }
                            });
                      //  }


                        supplierItemAA = customerItem;
                        supplierReportItemArrayListAA = customerReportItemArrayList;
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
        double balanceAmount = 0.0;
        String name = "report/"+SupplierReportHtml;
        String nameName = "file:///android_asset/report/"+SupplierReportHtml;

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size(); i++) {
                cruncycode = customerItem.getCurrency_symbol();

//                DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                double getDebit = Double.parseDouble(customerReportItemArrayList.get(i).getDebit());
                double getCredit = Double.parseDouble(customerReportItemArrayList.get(i).getCredit());
                //double getBalance = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());

                String stringDebit = "";
                String stringCredit = "";
                String stringBalance = "";

                balanceAmount = balanceAmount + getDebit;
                Log.e(TAG, "balanceAmountAA "+balanceAmount);

                balanceAmount = balanceAmount - getCredit;
                Log.e(TAG, "balanceAmountBB "+balanceAmount);


                if(getDebit == 0){
                    stringDebit = "";
                }else{
                    // String stringDebit1 = customerReportItemArrayList.get(i).getDebit();
                    stringDebit = Utility.getPatternFormat(""+numberPostion, getDebit) + Utility.getReplaceDollor(cruncycode);
                }

                if(getCredit == 0){
                    stringCredit = "";
                }else{
                    //String stringCredit1 = customerReportItemArrayList.get(i).getCredit();
                    stringCredit = Utility.getPatternFormat(""+numberPostion, getCredit) + Utility.getReplaceDollor(cruncycode);
                }

                if(balanceAmount == 0){
                    stringBalance = Utility.getPatternFormat(""+numberPostion, balanceAmount) + Utility.getReplaceDollor(cruncycode);
                }else{
                    //String stringBalance1 = customerReportItemArrayList.get(i).getBalance();
                    stringBalance = Utility.getPatternFormat(""+numberPostion, balanceAmount) + Utility.getReplaceDollor(cruncycode);
                }


                productitem = IOUtils.toString(getAssets().open("report/"+customer_single_itemHtml))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getCreated_date())
                        .replaceAll("#Particulars#", customerReportItemArrayList.get(i).getParticular())
                        .replaceAll("#DebitAmount#", stringDebit)
                        .replaceAll("#CreditAmount#", stringCredit)
                        .replaceAll("#Balance#", stringBalance);
                productitemlist = productitemlist + productitem;


//                double allAmount = 0.0;
//                if(i == customerReportItemArrayList.size() - 1){
//                    try{
//                        allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getBalance());
//                    }catch (Exception e){
//
//                    }
//                }
//
//                totalAmount =   totalAmount + allAmount;
            }

            totalAmount = balanceAmount;
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

                    .replaceAll("Supplier Statement of Account", getString(R.string.report_SupplierStatementofAccount))
                    .replaceAll("Date", getString(R.string.list_Date))
                    .replaceAll("Particulars", getString(R.string.html_Particulars))
                    .replaceAll("Debit Amount", getString(R.string.html_DebitAmount))
                    .replaceAll("Credit Amount", getString(R.string.html_CreditAmount))
                    .replaceAll("Balance", getString(R.string.html_Balance))
                    .replaceAll("Total Amount_", getString(R.string.html_TotalAmount))


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



    private void totalSalesReportPaidUnpaid(String company_id, String customer_id, String filterID, String fDate, String sDate, String customerName) {
        ArrayList<InvoiceData> invoiceDataArrayList = new ArrayList<>();

        RequestParams params = new RequestParams();
        params.add("company_id", company_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "invoice/getListingByCompany", params, new AsyncHttpResponseHandler() {
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
                        JSONArray invoice = data.getJSONArray("invoice");

                        if (invoice.length() > 0) {
                            for (int i = 0; i < invoice.length(); i++) {
                                JSONObject item = invoice.getJSONObject(i);

                                String invoice_idstr = item.getString("invoice_id");
//                                String invoice_no = item.getString("invoice_no");
//                                String due_date = item.getString("due_date");
//                                String total = item.getString("total");
//
//                                String void_status = item.getString("void_status");

                                String statusinvoice = item.getString("order_status_id");

                                InvoiceData company_list = new InvoiceData();

                                company_list.setInvoice_userid(invoice_idstr);

//                                company_list.setInvoice_nobdt(invoice_no);
//                                company_list.setInvoicedue_date(due_date);
//                                company_list.setInvoicetotlaprice(total);
                                company_list.setInvocestatus(statusinvoice);
//                                company_list.setVoid_status(void_status);
                                invoiceDataArrayList.add(company_list);

                                Log.e(TAG , invoice_idstr+ " invoice_no "+statusinvoice);
                            }
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                    totalSalesReport(invoiceDataArrayList, company_id, filterID, fDate, sDate, customerName);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    //avi.smoothToHide();
                    Log.e(TAG, "responsecompanyF"+ response);
                    totalSalesReport(invoiceDataArrayList, company_id, filterID, fDate, sDate, customerName);
                }
            }
        });
    }

    private void totalSalesReport( ArrayList<InvoiceData> list, String company_id, String filterID, String fDate, String sDate, String customerName) {
        RequestParams params = new RequestParams();
        params.add("company_id", company_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
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




                                String paidUnpaid = getPaidUnpaid(list, invoice_id);
                                customerReportItem.setPaidUnpaid(paidUnpaid);

                                Log.e(TAG, "filterIDPaid "+filterID);
                                Log.e(TAG, "paidUnpaid "+paidUnpaid);
                                Log.e(TAG, "customerName "+customerName);

                                if(filterID.equalsIgnoreCase("date")){
                                    if(!fDate.equalsIgnoreCase("") && !sDate.equalsIgnoreCase("")){
                                        try{
                                            DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = simple.parse(invoice_date);

                                            Date fDateC = simple.parse(fDate);
                                            Date sDateC = simple.parse(sDate);

                                            if(date.getTime() >= fDateC.getTime() && sDateC.getTime() >= date.getTime()){
                                                Log.e(TAG, "datemillis1 "+simple.format(fDateC));
                                                Log.e(TAG, "datemillis2 "+simple.format(sDateC));
                                                Log.e(TAG, "datemillis3 "+simple.format(date));
                                                customerReportItemArrayList.add(customerReportItem);
                                            }
                                        }catch (Exception e){

                                        }

                                    }else{

                                    }

                                }
                                else if(filterID.equalsIgnoreCase("paid")){

                                    if(paidUnpaid.equalsIgnoreCase("2")){
                                        customerReportItemArrayList.add(customerReportItem);
                                    }
                                }

                                else if(filterID.equalsIgnoreCase("unpaid")){
                                    if(paidUnpaid.equalsIgnoreCase("1")){
                                        customerReportItemArrayList.add(customerReportItem);
                                    }
                                }

                                else if(filterID.equalsIgnoreCase("customer")){
                                    if(customer_name.toLowerCase().equalsIgnoreCase(customerName.toLowerCase())){
                                        customerReportItemArrayList.add(customerReportItem);
                                    }
                                }

                                else{
                                    customerReportItemArrayList.add(customerReportItem);
                                }
                            }
                        }

//                        if(filterID.equalsIgnoreCase("date")){
//                            Collections.sort(customerReportItemArrayList, new Comparator<TotalSalesReportItem>() {
//                                public int compare(TotalSalesReportItem o1, TotalSalesReportItem o2) {
//                                    return o1.getInvoice_date().compareTo(o2.getInvoice_date());
//                                }
//                            });
//                        } else

//                        if(filterID.equalsIgnoreCase("customer")){
//                            Collections.sort(customerReportItemArrayList, new Comparator<TotalSalesReportItem>() {
//                                public int compare(TotalSalesReportItem o1, TotalSalesReportItem o2) {
//                                    return o1.getCustomer_name().compareTo(o2.getCustomer_name());
//                                }
//                            });
//                        } else
//
//                            if(filterID.equalsIgnoreCase("paid")){
////                            Collections.sort(customerReportItemArrayList, new Comparator<TotalSalesReportItem>() {
////                                public int compare(TotalSalesReportItem o1, TotalSalesReportItem o2) {
////                                    return o1.getCustomer_name().compareTo(o2.getCustomer_name());
////                                }
////                            });
//                        } else if(filterID.equalsIgnoreCase("unpaid")){
//
//                        }

                        totalSalesItemAA = customerItem;
                        totalSalesReportItemArrayListAA = customerReportItemArrayList;
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

    private String getPaidUnpaid(ArrayList<InvoiceData> list, String invoice_id) {
        String paidUnpaid = "";
        if(list != null){
            for(int i = 0 ; i < list.size() ; i++){
                Log.e(TAG, list.get(i).getInvoice_userid()+" getPaidUnpaid "+invoice_id);
                if(list.get(i).getInvoice_userid().equalsIgnoreCase(invoice_id)){
                    if(list.get(i).getInvocestatus().equalsIgnoreCase("2")){
                        paidUnpaid = "2";
                    }else{
                        paidUnpaid = "1";
                    }
                }
            }
        }
        return paidUnpaid;
    }


    private void totalSalesReportWeb(CompanyItem customerItem, ArrayList<TotalSalesReportItem> customerReportItemArrayList) {

        double totalAmount = 0.0;

        String name = "report/"+SalesReportHtml;
        String nameName = "file:///android_asset/report/"+SalesReportHtml;

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


                productitem = IOUtils.toString(getAssets().open("report/"+total_sales_single_itemHtml))
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

                    .replaceAll("Total Sales Report", getString(R.string.report_TotalSalesReport))
                    .replaceAll("Date", getString(R.string.list_Date))
                    .replaceAll("Particulars", getString(R.string.html_Particulars))
                    .replaceAll("Customer Name", getString(R.string.html_CustomerName))
                    .replaceAll("Amount_", getString(R.string.service_Amount))
                    .replaceAll("Total Amount_", getString(R.string.html_TotalAmount))


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



    private void totalPurchaseReport(String customer_id, String filterID, String fDate, String sDate, String supplierName) {

        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
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

                                if(filterID.equalsIgnoreCase("date")){
                                    if(!fDate.equalsIgnoreCase("") && !sDate.equalsIgnoreCase("")){
                                        try{
                                            DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = simple.parse(order_date);

                                            Date fDateC = simple.parse(fDate);
                                            Date sDateC = simple.parse(sDate);

                                            if(date.getTime() >= fDateC.getTime() && sDateC.getTime() >= date.getTime()){
                                                Log.e(TAG, "datemillis1 "+simple.format(fDateC));
                                                Log.e(TAG, "datemillis2 "+simple.format(sDateC));
                                                Log.e(TAG, "datemillis3 "+simple.format(date));
                                                customerReportItemArrayList.add(customerReportItem);
                                            }
                                        }catch (Exception e){

                                        }

                                    }else{

                                    }

                                    Log.e(TAG, "filterID111");
                                } else if(filterID.equalsIgnoreCase("supplier")){
                                    if(supplier_name.toLowerCase().equalsIgnoreCase(supplierName.toLowerCase())){
                                        customerReportItemArrayList.add(customerReportItem);
                                    }

                                    Log.e(TAG, "filterID222");
                                } else{
                                    customerReportItemArrayList.add(customerReportItem);
                                    Log.e(TAG, "filterID333");
                                }
                            }
                        }





//                        if(filterID.equalsIgnoreCase("date")){
//                            Collections.sort(customerReportItemArrayList, new Comparator<TotalPurchaseReportItem>() {
//                                public int compare(TotalPurchaseReportItem o1, TotalPurchaseReportItem o2) {
//                                    return o1.getOrder_date().compareTo(o2.getOrder_date());
//                                }
//                            });
//                        } else if(filterID.equalsIgnoreCase("supplier")){
//                            Collections.sort(customerReportItemArrayList, new Comparator<TotalPurchaseReportItem>() {
//                                public int compare(TotalPurchaseReportItem o1, TotalPurchaseReportItem o2) {
//                                    return o1.getSupplier_name().compareTo(o2.getSupplier_name());
//                                }
//                            });
//                        }

                        totalPurchaseItemAA = customerItem;
                        totalPurchaseReportItemArrayListAA = customerReportItemArrayList;
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

        String name = "report/"+PurchaseReportHtml;
        String nameName = "file:///android_asset/report/"+PurchaseReportHtml;

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


                productitem = IOUtils.toString(getAssets().open("report/"+total_sales_single_itemHtml))
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

                    .replaceAll("Total Purchase Report", getString(R.string.report_TotalPurchaseReport))
                    .replaceAll("Date", getString(R.string.list_Date))
                    .replaceAll("Particulars", getString(R.string.html_Particulars))
                    .replaceAll("Supplier Name", getString(R.string.html_SupplierName))
                    .replaceAll("Amount_", getString(R.string.service_Amount))
                    .replaceAll("Total Amount_", getString(R.string.html_TotalAmount))


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



    private void customerAgeingReport(String customer_id, String filterID, String customerName) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
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


                                if(filterID.equalsIgnoreCase("ageing")){
                                    if(customer_name.toLowerCase().equalsIgnoreCase(customerName.toLowerCase())){
                                        customerReportItemArrayList.add(customerReportItem);
                                    }
                                }else{
                                     customerReportItemArrayList.add(customerReportItem);
                                }

                            }
                        }

//                        if(filterID.equalsIgnoreCase("customer")){
////                            Collections.sort(customerReportItemArrayList, new Comparator<CustomerAgeingReportItem>() {
////                                public int compare(CustomerAgeingReportItem o1, CustomerAgeingReportItem o2) {
////                                    return o1.getCustomer_name().compareTo(o2.getCustomer_name());
////                                }
////                            });
//
//                           // if(customer_name. customerName)
//
//                        }

                        customerAgeingItemAA = customerItem;
                        customerAgeingReportItemArrayListAA = customerReportItemArrayList;
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

        String name = "report/"+CustomerAgingReportHtml;
        String nameName = "file:///android_asset/report/"+CustomerAgingReportHtml;

        String productitem = null;
        String productitemlist = "";

        String cruncycode = "";
        try {
            for (int i = 0; i < customerReportItemArrayList.size(); i++) {
                cruncycode = customerItem.getCurrency_symbol();

////                DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");


                double slab1 = Double.parseDouble(customerReportItemArrayList.get(i).getSlab1()) ;
                double slab2 = Double.parseDouble(customerReportItemArrayList.get(i).getSlab2()) ;
                double slab3 = Double.parseDouble(customerReportItemArrayList.get(i).getSlab3()) ;
                double slab4 = Double.parseDouble(customerReportItemArrayList.get(i).getSlab4()) ;





                String slab1Txt11 = Utility.getPatternFormat(""+numberPostion, slab1);
                String slab1Txt22 = Utility.getPatternFormat(""+numberPostion, slab2);
                String slab1Txt33 = Utility.getPatternFormat(""+numberPostion, slab3);
                String slab1Txt44 = Utility.getPatternFormat(""+numberPostion, slab4);

                String slab1Txt = "";
                String slab2Txt = "";
                String slab3Txt = "";
                String slab4Txt = "";


                if(slab1 == 0){
                    slab1Txt = slab1Txt11 + Utility.getReplaceDollor(cruncycode);
                }else{
                    slab1Txt = slab1Txt11 + Utility.getReplaceDollor(cruncycode);
                }

                if(slab2 == 0){
                    slab2Txt = slab1Txt22 + Utility.getReplaceDollor(cruncycode);
                }else{
                    slab2Txt = slab1Txt22 + Utility.getReplaceDollor(cruncycode);
                }

                if(slab3 == 0){
                    slab3Txt = slab1Txt33 + Utility.getReplaceDollor(cruncycode);
                }else{
                    slab3Txt = slab1Txt33 + Utility.getReplaceDollor(cruncycode);
                }

                if(slab4 == 0){
                    slab4Txt = slab1Txt44 + Utility.getReplaceDollor(cruncycode);
                }else{
                    slab4Txt = slab1Txt44 + Utility.getReplaceDollor(cruncycode);
                }


                double doubleBalance = Double.parseDouble(customerReportItemArrayList.get(i).getTotal());
                String stringBalance = "";

                String stringNotDue = "";

                if(doubleBalance == 0){
                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                    stringNotDue = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }else{
                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                    stringNotDue = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }


                double totalDD1 = 0.0;
                double totalDD2 = 0.0;
                double totalDD3 = 0.0;
                double totalDD4 = 0.0;
                if(slab1 != 0){
                    totalDD1 = slab1;
                    stringBalance = Utility.getPatternFormat(""+numberPostion, totalDD1) + Utility.getReplaceDollor(cruncycode);
                } if(slab2 != 0){
                    totalDD2 = slab2 ;
                    stringBalance = Utility.getPatternFormat(""+numberPostion, totalDD2) + Utility.getReplaceDollor(cruncycode);
                } if(slab3 != 0){
                    totalDD3 = slab3 ;
                    stringBalance = Utility.getPatternFormat(""+numberPostion, totalDD3) + Utility.getReplaceDollor(cruncycode);
                } if(slab4 != 0){
                    totalDD4 = slab4 ;
                    stringBalance = Utility.getPatternFormat(""+numberPostion, totalDD4) + Utility.getReplaceDollor(cruncycode);
//                } else{
//                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
//                    totalDD = doubleBalance;
//                    stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }

                double totalDDXX = doubleBalance + totalDD1 + totalDD2 + totalDD3 + totalDD4;

                String stringTotalDDXX = "";

                if(totalDDXX != 0){
                    stringTotalDDXX = Utility.getPatternFormat(""+numberPostion, totalDDXX) + Utility.getReplaceDollor(cruncycode);
                }

                productitem = IOUtils.toString(getAssets().open("report/"+customer_ageing_single_itemHtml))
                        .replaceAll("#CustomerName#", customerReportItemArrayList.get(i).getCustomer_name())
                        .replaceAll("#Currentdue#", stringNotDue)
                        .replaceAll("#Slab1#", slab1Txt)
                        .replaceAll("#Slab2#", slab2Txt)
                        .replaceAll("#Slab3#", slab3Txt)
                        .replaceAll("#Slab4#", slab4Txt)
                        .replaceAll("#Amount#", stringTotalDDXX);
                productitemlist = productitemlist + productitem;



                totalAmount =   totalAmount + totalDDXX;
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

                    .replaceAll("Customer Ageing Report", getString(R.string.report_CustomerAgeingReport))
                    .replaceAll("Customer Name", getString(R.string.html_CustomerName))
                    .replaceAll("Current due", getString(R.string.html_Currentdue))
                    .replaceAll("days overdue", getString(R.string.html_daysoverdue))
                    .replaceAll("Total_", getString(R.string.html_total))
                    .replaceAll("Total Amount_", getString(R.string.html_TotalAmount))


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



    private void taxCollectedReport(String customer_id, String filterID, String fDate, String sDate, String taxName) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
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


                                if(filterID.equalsIgnoreCase("date")){
                                    if(!fDate.equalsIgnoreCase("") && !sDate.equalsIgnoreCase("")){
                                        try{
                                            DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = simple.parse(date_added);

                                            Date fDateC = simple.parse(fDate);
                                            Date sDateC = simple.parse(sDate);

                                            if(date.getTime() >= fDateC.getTime() && sDateC.getTime() >= date.getTime()){
                                                Log.e(TAG, "datemillis1 "+simple.format(fDateC));
                                                Log.e(TAG, "datemillis2 "+simple.format(sDateC));
                                                Log.e(TAG, "datemillis3 "+simple.format(date));
                                                customerReportItemArrayList.add(customerReportItem);
                                            }
                                        }catch (Exception e){

                                        }

                                    }else{

                                    }

                                }

                                else if(filterID.equalsIgnoreCase("tax")){

                                    Log.e(TAG, tax_name.toLowerCase()+" ::: "+taxName.toLowerCase());
                                    if(tax_name.toLowerCase().trim().equalsIgnoreCase(taxName.toLowerCase().trim())){
                                        Log.e(TAG, tax_name.toLowerCase()+" equalsIgnoreCase "+taxName.toLowerCase());
                                        customerReportItemArrayList.add(customerReportItem);
                                    }
                                }

                                else{
                                    customerReportItemArrayList.add(customerReportItem);
                                }
                            }
                        }




//                        if(filterID.equalsIgnoreCase("date")){
                            Collections.sort(customerReportItemArrayList, new Comparator<TaxCollectedReportItem>() {
                                public int compare(TaxCollectedReportItem o1, TaxCollectedReportItem o2) {
                                    return o1.getDate_added().compareTo(o2.getDate_added());
                                }
                            });
                           // Collections.reverse(customerReportItemArrayList);
//                        }else if(filterID.equalsIgnoreCase("tax")){
//                            Collections.sort(customerReportItemArrayList, new Comparator<TaxCollectedReportItem>() {
//                                public int compare(TaxCollectedReportItem o1, TaxCollectedReportItem o2) {
//                                    return o1.getTax_name().compareTo(o2.getTax_name());
//                                }
//                            });
//                        }

                        taxCollectedItemAA = customerItem;
                        taxCollectedReportItemArrayListAA = customerReportItemArrayList;
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

        String name = "report/"+TaxCollectedReportHtml;
        String nameName = "file:///android_asset/report/"+TaxCollectedReportHtml;

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



                productitem = IOUtils.toString(getAssets().open("report/"+tax_collection_single_itemHtml))
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

                    .replaceAll("Tax Collected Report", getString(R.string.report_TaxCollectedReport))
                    .replaceAll("Date", getString(R.string.list_Date))
                    .replaceAll("Particulars", getString(R.string.html_Particulars))
                    .replaceAll("Customer Name", getString(R.string.html_CustomerName))
                    .replaceAll("Tax Name", getString(R.string.html_TaxName))
                    .replaceAll("Tax Rate", getString(R.string.html_TaxRate))
                    .replaceAll("Amount_", getString(R.string.html_Amount))
                    .replaceAll("Total Amount_", getString(R.string.html_TotalAmount))

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



    private void stockReport(String customer_id, String filterID, String productName) {
        RequestParams params = new RequestParams();
        params.add("company_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
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

                                Log.e(TAG, "filterIDZZZZ "+filterID);

                                if(filterID.toLowerCase().equalsIgnoreCase("instock".toLowerCase())){
                                    if(quantity_status.toLowerCase().equalsIgnoreCase("in stock".toLowerCase())){
                                        customerReportItemArrayList.add(customerReportItem);
                                    }
                                }else if(filterID.toLowerCase().equalsIgnoreCase("reorder".toLowerCase())){
                                    if(quantity_status.toLowerCase().equalsIgnoreCase("Re-Order".toLowerCase())){
                                        customerReportItemArrayList.add(customerReportItem);
                                    }
                                }else if(filterID.toLowerCase().equalsIgnoreCase("product".toLowerCase())){
                                    if(nameA.toLowerCase().equalsIgnoreCase(productName.toLowerCase())){
                                        customerReportItemArrayList.add(customerReportItem);
                                    }
                                }else{
                                    customerReportItemArrayList.add(customerReportItem);
                                }


                            }
                        }

//                        if(filterID.equalsIgnoreCase("reorder")){
//                            Collections.sort(customerReportItemArrayList, new Comparator<StockReportItem>() {
//                                public int compare(StockReportItem o1, StockReportItem o2) {
//                                    return o1.getMinimum().compareTo(o2.getMinimum());
//                                }
//                            });
//                        }else if(filterID.equalsIgnoreCase("product")){
//                            Collections.sort(customerReportItemArrayList, new Comparator<StockReportItem>() {
//                                public int compare(StockReportItem o1, StockReportItem o2) {
//                                    return o1.getName().compareTo(o2.getName());
//                                }
//                            });
//                        }
                        stockItemAA = customerItem;
                        stockReportItemArrayListAA = customerReportItemArrayList;
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

        String name = "report/"+StockReportHtml;
        String nameName = "file:///android_asset/report/"+StockReportHtml;

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
                    minimumTxt = Utility.getPatternFormat(""+numberPostion, 0.00);
                }else{
                    String minimumSS = Utility.getPatternFormat(""+numberPostion, minimum);
                    minimumTxt = minimumSS;
                }

                String quantityTxt = "";
                if(quantity == 0){
                    quantityTxt = Utility.getPatternFormat(""+numberPostion, 0.00);
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
                    valueTxt =  Utility.getPatternFormat(""+numberPostion, 0.00) + Utility.getReplaceDollor(cruncycode);
                }else{
                    String valueSS = Utility.getPatternFormat(""+numberPostion, quantityPricePerUnit) + Utility.getReplaceDollor(cruncycode);
                    valueTxt = valueSS;
                }

//
//                totalAmount = totalAmount + pricePerUnitValue;


//                String priceS = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(customerReportItemArrayList.get(i).getPrice()));
//                String valueS = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(customerReportItemArrayList.get(i).getValue()));


                productitem = IOUtils.toString(getAssets().open("report/"+stock_single_itemHtml))
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

                    .replaceAll("Stock Report", getString(R.string.report_StockReport))
                    .replaceAll("S.No.", getString(R.string.html_SNo))
                    .replaceAll("Product", getString(R.string.html_Product))
                    .replaceAll("Re-order Level", getString(R.string.html_ReorderLevel))
                    .replaceAll("Quantity Avaliable", getString(R.string.html_QuantityAvaliable))
                    .replaceAll("Status", getString(R.string.html_Status))
                    .replaceAll("Per Unit Price", getString(R.string.html_PerUnitPrice))
                    .replaceAll("Inventory Value", getString(R.string.html_InventoryValue))
                    .replaceAll("Total Amount_", getString(R.string.html_TotalAmount))

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

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
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

                                String date_added = item.getString("date_added").split(" ")[0];
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

                        productMovementItemAA = customerItem;
                        productMovementItemArrayListAA = customerReportItemArrayList;
                        productMovementWeb(customerItem, customerReportItemArrayList);

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


    private void productMovementWeb(CompanyItem customerItem, ArrayList<ProductMovementReportItem> customerReportItemArrayList) {

        double totalAmount = 0.0;

        double lastQuantity = 0.0;

        String name = "report/"+ProductMovementReportHtml;
        String nameName = "file:///android_asset/report/"+ProductMovementReportHtml;

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
                        purchasesTxt = valueSS;
                    } else if(customerReportItemArrayList.get(i).getCode().equalsIgnoreCase("debit")){
                        salesTxt = valueSS;
                    }else if(customerReportItemArrayList.get(i).getCode().equalsIgnoreCase("wastage")){
                        wastageTxt = valueSS;
                    }



                }


                double getTotal_quantity = Double.parseDouble(customerReportItemArrayList.get(i).getTotal_quantity());
                String totalQuantityTxt = "";
                if(getTotal_quantity == 0){
//                    String valueSS = Utility.getPatternFormat(""+numberPostion, getTotal_quantity);
                    totalQuantityTxt = Utility.getPatternFormat(""+numberPostion, 0.00);
                }else{
                    String valueSS = Utility.getPatternFormat(""+numberPostion, getTotal_quantity);
                    totalQuantityTxt = valueSS;
                }


                productitem = IOUtils.toString(getAssets().open("report/"+product_movement_single_itemHtml))
                        .replaceAll("#DATE#", customerReportItemArrayList.get(i).getDate_added())
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
            lastQuantityTxt = Utility.getPatternFormat(""+numberPostion, 0.00);
         }else{
             String valueSS = Utility.getPatternFormat(""+numberPostion, lastQuantity);
             lastQuantityTxt = valueSS;
         }

        String priceTxt ="";
        if(price == 0){
            priceTxt = Utility.getPatternFormat(""+numberPostion, 0.00) + Utility.getReplaceDollor(cruncycode);
        }else{
            String valueSS = Utility.getPatternFormat(""+numberPostion, price)+Utility.getReplaceDollor(cruncycode);
            priceTxt = valueSS;
        }

        double totalPrice = lastQuantity * price;

        String totalPriceTxt ="";
        if(totalPrice == 0){
            totalPriceTxt = Utility.getPatternFormat(""+numberPostion, 0.00) + Utility.getReplaceDollor(cruncycode);
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
                    .replaceAll("Product Movement Report", getString(R.string.report_ProductMovementReport))
                    .replaceAll("Date", getString(R.string.list_Date))
                    .replaceAll("Particulars", getString(R.string.html_Particulars))
                    .replaceAll("Opening Stock", getString(R.string.html_OpeningStock))
                    .replaceAll("Purchases", getString(R.string.html_Purchases))
                    .replaceAll("Sales", getString(R.string.html_Sales))
                    .replaceAll("Wastages/Damages", getString(R.string.html_WastagesDamages))
                    .replaceAll("Net Quantity_", getString(R.string.html_NetQuantity))
                    .replaceAll("Net Quantity Available", getString(R.string.html_NetQuantityAvailable))
                    .replaceAll("Price Per Unit", getString(R.string.html_PricePerUnit))
                    .replaceAll("Total Amount_", getString(R.string.html_TotalAmount))

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
        PrintManager printManager = (PrintManager) primaryBaseActivity.getSystemService(Context.PRINT_SERVICE);
       // webView.getSettings().setMinimumFontSize(webView.getSettings().getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_PRINT);
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
        Bundle bundle = new Bundle();
        bundle.putInt("positionNext", positionNext);
        bundle.putString("company_image_path", company_image_path);
        if(positionNext == 0){
            if(i == 0){
//                intent.putExtra("customer_id", customer_id);
                bundle.putSerializable("customerItemAA", customerItemAA);
                bundle.putSerializable("customerReportItemArrayListAA", customerReportItemArrayListAA);
                intent.putExtras(bundle);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb(getString(R.string.report_CustomerStatementReport));
            }else if(i == 3){
                //customerReport(customer_id, "date");
//                customerFilterDate(customer_id, positionNext);

                filterDate(customer_id, positionNext);

            }else if(i == 4){
                customerReport(customer_id, "", "", "");
            }
        } else if(positionNext == 1){
            if(i == 0){
                bundle.putSerializable("supplierItemAA", supplierItemAA);
                bundle.putSerializable("supplierReportItemArrayListAA", supplierReportItemArrayListAA);
                intent.putExtras(bundle);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb(getString(R.string.report_SupplierStatementReport));
            }else if(i == 3){
                filterDate(supplier_id, positionNext);
            }else if(i == 4){
                supplierReport(supplier_id, "", "", "");
            }
        } else if(positionNext == 2){
            if(i == 0){
                bundle.putSerializable("totalSalesItemAA", totalSalesItemAA);
                bundle.putSerializable("totalSalesReportItemArrayListAA", totalSalesReportItemArrayListAA);
                intent.putExtras(bundle);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb(getString(R.string.report_TotalSalesReport));
            }else if(i == 3){
                filterDate(company_id, positionNext);
            }else if(i == 4){
                //totalSalesReport(company_id, "customer", "", "");
                filterCustomerSupplier = "customer";
                createbottomsheet_customers();
            }else if(i == 5){
                totalSalesReportPaidUnpaid(selectedCompanyId, company_id, "paid", "", "" , "");
                //totalSalesReport(null, company_id, "paid", "", "");
            }else if(i == 6){
                totalSalesReportPaidUnpaid(selectedCompanyId, company_id, "unpaid", "", "", "");
//                totalSalesReport(null, company_id, "unpaid", "", "");
            }else if(i == 7){
                totalSalesReportPaidUnpaid(selectedCompanyId, company_id, "", "", "", "");
            }
        } else if(positionNext == 3){
            if(i == 0){
                bundle.putSerializable("totalPurchaseItemAA", totalPurchaseItemAA);
                bundle.putSerializable("totalPurchaseReportItemArrayListAA", totalPurchaseReportItemArrayListAA);
                intent.putExtras(bundle);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb(getString(R.string.report_TotalPurchaseReport));
            }else if(i == 3){
//                totalPurchaseReport(company_id, "date");
                filterDate(company_id, positionNext);
            }else if(i == 4){
                filterCustomerSupplier = "supplier";
                createbottomsheet_supplier();
                //totalPurchaseReport(company_id, "supplier", "", "");
            }else if(i == 5){
                totalPurchaseReport(company_id, "", "", "", "");
            }
        } else if(positionNext == 4){
            if(i == 0){
                bundle.putSerializable("customerAgeingItemAA", customerAgeingItemAA);
                bundle.putSerializable("customerAgeingReportItemArrayListAA", customerAgeingReportItemArrayListAA);
                intent.putExtras(bundle);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb(getString(R.string.report_CustomerAgeingReport));
            }else if(i == 3){
                filterCustomerSupplier = "ageing";
                createbottomsheet_customers();
              //  customerAgeingReport(company_id, "customer");
            }else if(i == 4){
                filterCustomerSupplier = "ageing";
               // createbottomsheet_customers();
                customerAgeingReport(company_id, "", "");
            }
        } else if(positionNext == 5){
            if(i == 0){
                bundle.putSerializable("taxCollectedItemAA", taxCollectedItemAA);
                bundle.putSerializable("taxCollectedReportItemArrayListAA", taxCollectedReportItemArrayListAA);
                intent.putExtras(bundle);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb(getString(R.string.report_TaxCollectedReport));
            }else if(i == 3){
                //taxCollectedReport(company_id, "date");
                filterDate(company_id, positionNext);
            }else if(i == 4){
                createbottomsheet_tax();
                //taxCollectedReport(company_id, "tax", "", "");
            }else if(i == 5){
                taxCollectedReport(company_id, "", "", "", "tax");
            }
        } else if(positionNext == 6){
            if(i == 0){
                bundle.putSerializable("stockItemAA", stockItemAA);
                bundle.putSerializable("stockReportItemArrayListAA", stockReportItemArrayListAA);
                intent.putExtras(bundle);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb(getString(R.string.report_StockReport));
            }else if(i == 3){
                stockReport(company_id, "instock", "");
            }else if(i == 4){
                stockReport(company_id, "reorder", "");
            }else if(i == 5){
                createbottomsheet_product();
               // stockReport(company_id, "product", "");
            }else if(i == 6){
                stockReport(company_id, "", "");
            }
        } else if(positionNext == 7){
            if(i == 0){
                bundle.putSerializable("productMovementItemAA", productMovementItemAA);
                bundle.putSerializable("productMovementItemArrayListAA", productMovementItemArrayListAA);
                intent.putExtras(bundle);
                startActivity(intent);
            }else if(i == 1){
                createWebPrintJob(invoiceweb);
            }else if(i == 2){
                shareWeb(getString(R.string.report_ProductMovementReport));
            }
        } else{

        }


    }



    private void filterDate(String customer_id, int positionNext) {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ReportViewActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int month = monthOfYear + 1;
                        String realMonth = ""+month;
                        if(realMonth.length() == 1){
                            realMonth = "0"+month;
                        }


                        int day = dayOfMonth;
                        String realDay = ""+day;
                        if(realDay.length() == 1){
                            realDay = "0"+day;
                        }

                        //eddate.setText(year + "-" + realMonth + "-" + realDay);
                        String fDate = year + "-" + realMonth + "-" + realDay;
                        filterDate2(customer_id, positionNext, fDate);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.setMessage(getString(R.string.report_SelectDateFrom));
        datePickerDialog.show();
    }


    private void filterDate2(String customer_id, int position, String fDate) {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ReportViewActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int month = monthOfYear + 1;
                        String realMonth = ""+month;
                        if(realMonth.length() == 1){
                            realMonth = "0"+month;
                        }


                        int day = dayOfMonth;
                        String realDay = ""+day;
                        if(realDay.length() == 1){
                            realDay = "0"+day;
                        }

                        String sDate = year + "-" + realMonth + "-" + realDay;

//                        customerFilterDate();
                        //eddate.setText(year + "-" + realMonth + "-" + realDay);

                        if(position == 0){
                            customerReport(customer_id, "date", fDate, sDate);
                        } else if(position == 1){
                            supplierReport(customer_id, "date", fDate, sDate);
                        } else if(position == 2){
                            //totalSalesReport(null, customer_id, "date", fDate, sDate, "");
                            totalSalesReportPaidUnpaid(selectedCompanyId, customer_id, "date", fDate, sDate, "");
                        }else if(position == 3){
                            totalPurchaseReport(customer_id, "date", fDate, sDate, "");
                        }else if(position == 4){
                        }else if(position == 5){
                            taxCollectedReport(customer_id, "date", fDate, sDate, "");
                        }else if(position == 6){
                        }else if(position == 7){
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.setMessage(getString(R.string.report_SelectDateTo));
        datePickerDialog.show();
    }


    private void shareWeb(String title) {
        Log.e(TAG, "title "+title);

        if(fileWithinMyDir.exists()) {
            Log.e(TAG, "FILENAME" +fileWithinMyDir);
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            // File fileWithinMyDir = new File(message);
            Uri photoURI = FileProvider.getUriForFile(ReportViewActivity.this, "com.sirapp.provider", fileWithinMyDir);
            if(fileWithinMyDir.exists()) {
                intentShareFile.setType("application/pdf");
                //Uri outputFileUri = Uri.fromFile(fileWithinMyDir);
                intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, title);
                startActivity(Intent.createChooser(intentShareFile, getString(R.string.list_ShareFile)));
            }
        }

    }








    TextView txtcustomer, txtname, txtcontactname;
    EditText search_customers;
    RecyclerView recycler_customers;
    Customer_Bottom_Adapter customer_bottom_adapter;

    public void createbottomsheet_customers() {

        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(ReportViewActivity.this).inflate(R.layout.customer_bottom_sheet, null);
            txtcustomer = view.findViewById(R.id.txtcustomer);
            search_customers = view.findViewById(R.id.search_customers);
            TextView add_customer = view.findViewById(R.id.add_customer);
            add_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReportViewActivity.this, Customer_Activity.class);
                    startActivityForResult(intent, 123);
                    bottomSheetDialog.dismiss();
                }
            });

            recycler_customers = view.findViewById(R.id.recycler_customers);
            txtname = view.findViewById(R.id.name);
            txtcontactname = view.findViewById(R.id.contactname);
            search_customers.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {


                    if (customer_bottom.size() > 0) {
                        filterCustomers(s.toString());
                    }
                }
            });

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReportViewActivity.this, LinearLayoutManager.VERTICAL, false);
            recycler_customers.setLayoutManager(layoutManager);
            recycler_customers.setHasFixedSize(true);

            customer_bottom_adapter = new Customer_Bottom_Adapter(ReportViewActivity.this, customer_bottom, this);
            recycler_customers.setAdapter(customer_bottom_adapter);
            customer_bottom_adapter.notifyDataSetChanged();


            txtcustomer.setTypeface(Typeface.createFromAsset(ReportViewActivity.this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            // txtname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));
            // txtcontactname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));

            bottomSheetDialog = new BottomSheetDialog(ReportViewActivity.this);
            bottomSheetDialog.setContentView(view);

            bottomSheetDialog.show();


        }
    }


    public void createbottomsheet_supplier() {

        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(ReportViewActivity.this).inflate(R.layout.customer_bottom_sheet, null);
            txtcustomer = view.findViewById(R.id.txtcustomer);
            txtcustomer.setText(getString(R.string.dialog_SelectSupplier));
            search_customers = view.findViewById(R.id.search_customers);
            search_customers.setHint(getString(R.string.dialog_SelectSuppliers));
            TextView add_customer = view.findViewById(R.id.add_customer);
            add_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReportViewActivity.this, Vendor_Activity.class);
                    startActivityForResult(intent, 124);
                    bottomSheetDialog.dismiss();
                }
            });

            recycler_customers = view.findViewById(R.id.recycler_customers);
            txtname = view.findViewById(R.id.name);
            txtcontactname = view.findViewById(R.id.contactname);
            search_customers.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {


                    if (supplier_bottom.size() > 0) {
                        filterSupplier(s.toString());
                    }
                }
            });

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReportViewActivity.this, LinearLayoutManager.VERTICAL, false);
            recycler_customers.setLayoutManager(layoutManager);
            recycler_customers.setHasFixedSize(true);

            customer_bottom_adapter = new Customer_Bottom_Adapter(ReportViewActivity.this, supplier_bottom, this);
            recycler_customers.setAdapter(customer_bottom_adapter);
            customer_bottom_adapter.notifyDataSetChanged();


            txtcustomer.setTypeface(Typeface.createFromAsset(ReportViewActivity.this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            // txtname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));
            // txtcontactname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));

            bottomSheetDialog = new BottomSheetDialog(ReportViewActivity.this);
            bottomSheetDialog.setContentView(view);

            bottomSheetDialog.show();


        }
    }


    Product_Bottom_Adapter product_bottom_adapter;

    public void createbottomsheet_product() {

        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(ReportViewActivity.this).inflate(R.layout.customer_bottom_sheet, null);
            txtcustomer = view.findViewById(R.id.txtcustomer);
            txtcustomer.setText(getString(R.string.stock_Select_Product));
            search_customers = view.findViewById(R.id.search_customers);
            search_customers.setHint(getString(R.string.stock_Select_Products));
            TextView add_customer = view.findViewById(R.id.add_customer);
            add_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReportViewActivity.this, Product_Activity.class);
                    startActivityForResult(intent, 125);
                    bottomSheetDialog.dismiss();
                }
            });

            recycler_customers = view.findViewById(R.id.recycler_customers);
            txtname = view.findViewById(R.id.name);
            txtcontactname = view.findViewById(R.id.contactname);
            search_customers.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {


                    if (product_bottom.size() > 0) {
                        filterProduct(s.toString());
                    }
                }
            });

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReportViewActivity.this, LinearLayoutManager.VERTICAL, false);
            recycler_customers.setLayoutManager(layoutManager);
            recycler_customers.setHasFixedSize(true);

            product_bottom_adapter = new Product_Bottom_Adapter(ReportViewActivity.this, product_bottom, this, bottomSheetDialog ,"report");
            recycler_customers.setAdapter(product_bottom_adapter);
            product_bottom_adapter.notifyDataSetChanged();


            txtcustomer.setTypeface(Typeface.createFromAsset(ReportViewActivity.this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            // txtname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));
            // txtcontactname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));

            bottomSheetDialog = new BottomSheetDialog(ReportViewActivity.this);
            bottomSheetDialog.setContentView(view);

            bottomSheetDialog.show();


        }
    }




    CustomTaxAdapter customTaxAdapter;

    public void createbottomsheet_tax() {

        if (bottomSheetDialog3 != null) {
            View view = LayoutInflater.from(ReportViewActivity.this).inflate(R.layout.tax_bottom_item_report_layout, null);

            search_customers = view.findViewById(R.id.search_customers);
            search_customers.setHint("Search");
            search_customers.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (tax_list_array.size() > 0) {
                        filterTax(s.toString());
                    }
                }
            });

            RecyclerView taxrecycler = view.findViewById(R.id.taxrecycler);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReportViewActivity.this, LinearLayoutManager.VERTICAL, false);
            taxrecycler.setLayoutManager(layoutManager);
            taxrecycler.setHasFixedSize(true);

            Log.e(TAG,  "tax_list_array "+tax_list_array.size());

            customTaxAdapter = new CustomTaxAdapter(ReportViewActivity.this, tax_list_array);
            taxrecycler.setAdapter(customTaxAdapter);
            customTaxAdapter.notifyDataSetChanged();


            bottomSheetDialog3 = new BottomSheetDialog(ReportViewActivity.this);
            bottomSheetDialog3.setContentView(view);
            bottomSheetDialog3.show();
        }
    }


    void filterCustomers(String text) {
        ArrayList<Customer_list> temp = new ArrayList();
        for (Customer_list d : customer_bottom) {
            if (d.getCustomer_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        customer_bottom_adapter.updateList(temp);
    }


    void filterSupplier(String text) {
        ArrayList<Customer_list> temp = new ArrayList();
        for (Customer_list d : supplier_bottom) {
            if (d.getCustomer_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        customer_bottom_adapter.updateList(temp);
    }


    void filterProduct(String text) {
        ArrayList<Product_list> temp = new ArrayList();
        for (Product_list d : product_bottom) {
            if (d.getProduct_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        product_bottom_adapter.updateList(temp);
    }



    void filterTax(String text) {
        ArrayList<Tax_List> temp = new ArrayList();
        for (Tax_List d : tax_list_array) {
            if (d.getTax_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        customTaxAdapter.updateList(temp);
    }




    @Override
    public void onPostExecute(Customer_list customer_list) {

        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }

        String customer_id = customer_list.getCustomer_id();
        String customer_name = customer_list.getCustomer_name();

        if(filterCustomerSupplier.equalsIgnoreCase("customer")){
            totalSalesReportPaidUnpaid(selectedCompanyId, company_id, "customer", "", "", customer_name);
        } else if(filterCustomerSupplier.equalsIgnoreCase("supplier")){
            totalPurchaseReport(company_id, "supplier", "", "", customer_name);
        } else if(filterCustomerSupplier.equalsIgnoreCase("ageing")){
            customerAgeingReport(company_id, "ageing",  customer_name);
        }


        Log.e(TAG, "filterCustomerSupplierAAAAAAA "+filterCustomerSupplier);
        Log.e(TAG, "customer_idAAAAAAA "+customer_id);
        Log.e(TAG, "customer_nameAAAAAA "+customer_name);

//        Intent intent = new Intent(ReportViewActivity.this, ReportViewActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        //intent.putExtra("titleName", titleName);
//
//        intent.putExtra("positionNext", positionNext);
//
//        if(positionNext == 0){
//            intent.putExtra("customer_id", customer_id);
//        } else if(positionNext == 1){
//            intent.putExtra("supplier_id", customer_id);
//        } else if(positionNext == 7){
//            intent.putExtra("product_id", customer_id);
//        } else{
//            intent.putExtra("company_id", selectedCompanyId);
//        }
//
//        startActivity(intent);
    }



    @Override
    public void onPostExecutecall(Product_list selected_item, String s, String price) {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }

        String customer_id = selected_item.getProduct_id();
        String productName = selected_item.getProduct_name();

        Log.e(TAG, "customer_idBBBBBBB "+customer_id);

        stockReport(company_id, "product", productName);


        //Log.e(TAG, "customer_name "+customer_name);

//        Intent intent = new Intent(ReportViewActivity.this, ReportViewActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        //intent.putExtra("titleName", titleName);
//
//        intent.putExtra("positionNext", positionNext);
//
//        if(positionNext == 7){
//            intent.putExtra("product_id", customer_id);
////            intent.putExtra("companyName", companyName);
////            intent.putExtra("companyAddress", companyAddress);
////            intent.putExtra("companyContactNo", companyContactNo);
////            intent.putExtra("companyWebsite", companyWebsite);
////            intent.putExtra("companyEmail", companyEmail);
////            intent.putExtra("companyLogo", companyLogo);
//        }
//
//        startActivity(intent);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult requestCode:: "+requestCode);

        if(requestCode == 123){
            Log.e(TAG, "requestCode "+requestCode);
            customer_list(selectedCompanyId);
        }

        if(requestCode == 124){
            Log.e(TAG, "requestCode "+requestCode);
            supplier_list(selectedCompanyId);
        }

        if(requestCode == 125){
            Log.e(TAG, "requestCode "+requestCode);
            product_list(selectedCompanyId);
        }


    }



    @Override
    public void closeDialog() {
        if(bottomSheetDialog != null){
            bottomSheetDialog.dismiss();
        }
    }






    public void customer_list(String selectedCompanyId) {
        customer_bottom.clear();
        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "customer/getListingByCompany", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("response customers", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray customer = data.getJSONArray("customer");
                        String image_path = data.getString("customer_image_path");

                        String customer_name = "", customer_id = "", custoner_contact_name = "", customer_email = "", customer_contact = "", customer_address = "", customer_website = "", customer_phone_number = "";

                        String shipping_firstname, shipping_lastname, shipping_address_1, shipping_address_2, shipping_city, shipping_postcode, shipping_country, shipping_zone;


                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject item = customer.getJSONObject(i);

                            customer_id = item.getString("customer_id");

                            Log.e("Customer Id", customer_id);
                            customer_name = item.getString("customer_name");
                            custoner_contact_name = item.getString("contact_name");
                            String image = item.getString("image");
                            customer_email = item.getString("email");
                            customer_contact = item.getString("phone_number");
                            customer_address = item.getString("address");
                            customer_website = item.getString("website");
                            shipping_firstname = item.getString("shipping_firstname");
                            shipping_lastname = item.getString("shipping_lastname");
                            shipping_address_1 = item.getString("shipping_address_1");

                            shipping_address_2 = item.getString("shipping_address_2");
                            shipping_city = item.getString("shipping_city");
                            shipping_postcode = item.getString("shipping_postcode");
                            shipping_country = item.getString("shipping_country");
                            shipping_zone = item.getString("shipping_zone");

                            Customer_list customer_list = new Customer_list();

                            customer_list.setCustomer_email(customer_email);
                            customer_list.setCustomer_address(customer_address);
                            customer_list.setCustomer_website(customer_website);
                            customer_list.setCustomer_phone(customer_contact);

                            customer_list.setCustomer_id(customer_id);
                            customer_list.setCustomer_name(customer_name);
                            customer_list.setCustomer_contact_person(custoner_contact_name);
                            customer_list.setCustomer_image_path(image_path);
                            customer_list.setCustomer_image(image);

                            customer_list.setShipping_firstname(shipping_firstname);
                            customer_list.setShipping_lastname(shipping_lastname);
                            customer_list.setShipping_address_1(shipping_address_1);
                            customer_list.setShipping_address_2(shipping_address_2);
                            customer_list.setShipping_city(shipping_city);
                            customer_list.setShipping_postcode(shipping_postcode);
                            customer_list.setShipping_country(shipping_country);
                            customer_list.setShipping_zone(shipping_zone);

                            customer_bottom.add(customer_list);


                        }


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

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            //Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Constant.ErrorToast(ReportActivity.this, "Something went wrong, try again!");
                }
            }
        });
    }


    public void supplier_list(String selectedCompanyId) {
        supplier_bottom.clear();
        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "supplier/getListingByCompany", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("response customers", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray customer = data.getJSONArray("supplier");
                        String image_path = data.getString("supplier_image_path");

                        String customer_name = "", customer_id = "", custoner_contact_name = "", customer_email = "", customer_contact = "", customer_address = "", customer_website = "", customer_phone_number = "";

                        String shipping_firstname, shipping_lastname, shipping_address_1, shipping_address_2, shipping_city, shipping_postcode, shipping_country, shipping_zone;


                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject item = customer.getJSONObject(i);

                            customer_id = item.getString("supplier_id");

                            Log.e("supplier_id", customer_id);
                            customer_name = item.getString("supplier_name");
                            custoner_contact_name = item.getString("contact_name");
                            String image = item.getString("image");
                            customer_email = item.getString("email");
                            customer_contact = item.getString("phone_number");
                            customer_address = item.getString("address");
                            customer_website = item.getString("website");


//                            shipping_firstname = item.getString("shipping_firstname");
//                            shipping_lastname = item.getString("shipping_lastname");
//                            shipping_address_1 = item.getString("shipping_address_1");
//
//                            shipping_address_2 = item.getString("shipping_address_2");
//                            shipping_city = item.getString("shipping_city");
//                            shipping_postcode = item.getString("shipping_postcode");
//                            shipping_country = item.getString("shipping_country");
//                            shipping_zone = item.getString("shipping_zone");

                            Customer_list customer_list = new Customer_list();

                            customer_list.setCustomer_email(customer_email);
                            customer_list.setCustomer_address(customer_address);
                            customer_list.setCustomer_website(customer_website);
                            customer_list.setCustomer_phone(customer_contact);

                            customer_list.setCustomer_id(customer_id);
                            customer_list.setCustomer_name(customer_name);
                            customer_list.setCustomer_contact_person(custoner_contact_name);
                            customer_list.setCustomer_image_path(image_path);
                            customer_list.setCustomer_image(image);

//                            customer_list.setShipping_firstname(shipping_firstname);
//                            customer_list.setShipping_lastname(shipping_lastname);
//                            customer_list.setShipping_address_1(shipping_address_1);
//                            customer_list.setShipping_address_2(shipping_address_2);
//                            customer_list.setShipping_city(shipping_city);
//                            customer_list.setShipping_postcode(shipping_postcode);
//                            customer_list.setShipping_country(shipping_country);
//                            customer_list.setShipping_zone(shipping_zone);

                            supplier_bottom.add(customer_list);


                        }


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

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            //Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Constant.ErrorToast(ReportActivity.this, "Something went wrong, try again!");
                }
            }
        });
    }


    public void product_list(String selectedCompanyId) {
        product_bottom.clear();
        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);

        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "product/getListingByCompany", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "responseproductAAA"+ response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        String product_image_path = data.getString("product_image_path");

                        JSONArray product = data.getJSONArray("product");
                        if (product.length() > 0) {
                            for (int i = 0; i < product.length(); i++) {
                                JSONObject item = product.getJSONObject(i);
                                String product_id = item.getString("product_id");
                                String company_id = item.getString("company_id");
                                String product_name = item.getString("name");
                                String product_description = item.getString("description");
                                String product_status = item.getString("status");
                                String product_image = item.getString("image");
                                String product_price = item.getString("price");
                                String product_taxable = item.getString("is_taxable");
                                String product_category = item.getString("category");
                                String currency_symbol = item.getString("currency_symbol");
                                String quantity = item.getString("quantity");

                                String measurement_unit = item.getString("measurement_unit");
                                Log.e(TAG, "measurement_unit: "+measurement_unit);



                                String minimum = item.getString("minimum");

                                Product_list product_list = new Product_list();

                                product_list.setProduct_measurement_unit(measurement_unit);
                                product_list.setCompany_id(company_id);
                                product_list.setProduct_id(product_id);
                                product_list.setProduct_name(product_name);
                                product_list.setProduct_description(product_description);
                                product_list.setProduct_status(product_status);
                                product_list.setProduct_image(product_image);
                                product_list.setProduct_image_path(product_image_path);
                                product_list.setProduct_price(product_price);
                                product_list.setProduct_taxable(product_taxable);
                                product_list.setProduct_category(product_category);
                                product_list.setCurrency_code(currency_symbol);
                                product_list.setQuantity(quantity);
                                product_list.setMinimum(minimum);

//                                Product_list product_list = new Customer_list();
//                                product_list.setCompany_id(company_id);
//                                product_list.setCustomer_id(product_id);
//                                product_list.setCustomer_name(product_name);
//                                product_list.setCustomer_image(product_image_path+product_image);
//                                product_list.setPrice(product_price);
//                                product_list.setCurrency_symbol(currency_symbol);
//                                product_list.setType("product");
                                product_bottom.add(product_list);

                            }
                        } else {
                            //Constant.ErrorToast(ReportActivity.this, jsonObject.getString("Product Not Found"));
                        }

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

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            //Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Constant.ErrorToast(ReportActivity.this, "Something went wrong, try again!");
                }
            }
        });
    }


    private void companyInformation(String selectedCompanyId) {
        tax_list_array.clear();
        RequestParams params = new RequestParams();
        params.add("company_id", selectedCompanyId);
//        params.add("product", "1");
//        params.add("service", "1");
//        params.add("customer", "1");
        params.add("tax", "1");
//        //   params.add("receipt", "1");
//        params.add("invoice", "1");
//        params.add("warehouse", "1");


        String token = Constant.GetSharedPreferences(ReportViewActivity.this, Constant.ACCESS_TOKEN);
        Log.e("token", token);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "company/info", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "Company_Information"+ response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");


                       // JSONArray company = data.getJSONArray("company");

                        JSONArray tax_list = data.getJSONArray("tax");

                        for (int j = 0; j < tax_list.length(); j++) {
//                                Tax_List student = new Gson().fromJson(tax_list.get(j).toString(), Tax_List.class);
                            JSONObject jsonObject1 = tax_list.getJSONObject(j);
//                                String name = jsonObject1.getString("name");
                            Tax_List student = new Tax_List();
                            student.setTax_id(jsonObject1.getString("tax_id"));
                            student.setTax_name(jsonObject1.getString("name"));
                            student.setCompany_name(jsonObject1.getString("company_name"));
                            student.setTax_rate(jsonObject1.getString("rate"));
                            student.setCompanyid(jsonObject1.getString("company_id"));
                            student.setType(jsonObject1.getString("type"));
                            tax_list_array.add(student);


                        }


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

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            //Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });


    }






    public class CustomTaxAdapter  extends RecyclerView.Adapter<CustomTaxAdapter.ViewHolderForCat> {

        private static final String TAG = "CustomTaxAdapter";
        private Context mcontext;
        ArrayList<Tax_List> mlist = new ArrayList<>();




        // private int selectedPos = -100;
        public CustomTaxAdapter(Context mcontext, ArrayList<Tax_List> list) {
            this.mcontext = mcontext;
            mlist = list;
            //   this.callback = callback;

        }


        @NonNull
        @Override
        public CustomTaxAdapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragmenttaxtcustom_tax, viewGroup, false);
            CustomTaxAdapter.ViewHolderForCat viewHolderForCat = new CustomTaxAdapter.ViewHolderForCat(myitem);
            return viewHolderForCat;
        }

        @Override
        public void onBindViewHolder(@NonNull final CustomTaxAdapter.ViewHolderForCat viewHolderForCat, final int i) {


            final Tax_List service_list = mlist.get(i);
            String taxname = service_list.getTax_name();
            String taxvalue = service_list.getTax_rate();
            String typestr = service_list.getType();
            String company_name = service_list.getCompany_name();

            Log.e("type", typestr);



//            if(taxID == service_list.getTax_id()){
//                //viewHolderForCat.imgincome.setVisibility(View.VISIBLE);
//                selectedPos = viewHolderForCat.getAdapterPosition();
//            }else{
//                selectedPos = -100;
//                //  viewHolderForCat.imgincome.setVisibility(View.INVISIBLE);
//            }

            if (taxname != null) {
                viewHolderForCat.txtincome.setText(taxname);
            }

            SavePref pref = new SavePref();
            pref.SavePref(mcontext);
            int numberPostion = pref.getNumberFormatPosition();
            double vc = 0.0;
            try {
                vc = Double.parseDouble(taxvalue);
            } catch (Exception e) {

            }
            String stringFormat = Utility.getPatternFormat("" + numberPostion, vc);

            if (typestr.equals("P")) {
                viewHolderForCat.txtincomepercent.setText(stringFormat + " %");
            } else {
                viewHolderForCat.txtincomepercent.setText(stringFormat);
            }


            viewHolderForCat.txtincomepercent_name.setText(company_name);

//            if(i == selectedPos)
//            {
//                viewHolderForCat.imgincome.setVisibility(View.VISIBLE);
//            } else {
//                viewHolderForCat.imgincome.setVisibility(View.INVISIBLE);
//            }

            viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bottomSheetDialog3.dismiss();

                    String tax_name = service_list.getTax_name();

                    taxCollectedReport(company_id, "tax", "", "", tax_name);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mlist.size();
            //return 2;
        }

//        public void updateTaxSelect(String taxID) {
//            this.taxID = taxID;
//            Log.e(TAG, "taxID" + taxID);
//            notifyDataSetChanged();
//        }


        public class ViewHolderForCat extends RecyclerView.ViewHolder {


            TextView txtincome, txtincomepercent, txtincomepercent_name;
            // ImageView imgincome;

            public ViewHolderForCat(@NonNull View itemView) {
                super(itemView);

                // imgincome = itemView.findViewById(R.id.imgincome);
                txtincome = itemView.findViewById(R.id.txtincome);
                txtincomepercent = itemView.findViewById(R.id.txtincomepercent);
                txtincomepercent_name = itemView.findViewById(R.id.txtincomepercent_name);
                txtincomepercent.setTypeface(Typeface.createFromAsset(mcontext.getAssets(), "Fonts/AzoSans-Light.otf"));
                txtincome.setTypeface(Typeface.createFromAsset(mcontext.getAssets(), "Fonts/Ubuntu-Bold.ttf"));


            }

        }

        public void updateList(ArrayList<Tax_List> list) {
            mlist = list;
            notifyDataSetChanged();
        }

    }



    @SuppressLint("LongLogTag")
    @Override
    protected void onResume() {
        super.onResume();
        //callForWeb();
    }



    @SuppressLint("LongLogTag")
    private void callForWeb() {
        if(invoiceweb != null){

            WebSettings webSettings = invoiceweb.getSettings();

            Log.e(TAG, "isTablet "+Utility.isTablet(ReportViewActivity.this));
            if(Utility.isTablet(ReportViewActivity.this) == true){ // tab
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int widthPixels = metrics.widthPixels;
                int heightPixels = metrics.heightPixels;
                float scaleFactor = metrics.density;
                float widthDp = widthPixels / scaleFactor;
                float heightDp = heightPixels / scaleFactor;
                float smallestWidth = Math.min(widthDp, heightDp);

                if (smallestWidth > 720) {
                    //Device is a 10" tablet
                    webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_TAB_10_V);
                }
                else if (smallestWidth > 600) {
                    //Device is a 7" tablet
                    webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_TAB_7_V);
                }else{
                    invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                }
            }else{
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width1=dm.widthPixels;
                int height1=dm.heightPixels;
                double wi=(double)width1/(double)dm.xdpi;
                double hi=(double)height1/(double)dm.ydpi;
                double x = Math.pow(wi,2);
                double y = Math.pow(hi,2);
                double screenInches = Math.sqrt(x+y);
                if(screenInches > 4.9 && screenInches < 5.4){
                    Log.e(TAG, "screenInches1 "+screenInches);
                    invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                }else{
                    if (Utility.getDensityName(ReportViewActivity.this).equalsIgnoreCase("ldpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_L_V);
                    }else if (Utility.getDensityName(ReportViewActivity.this).equalsIgnoreCase("mdpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_M_V);
                    }else if (Utility.getDensityName(ReportViewActivity.this).equalsIgnoreCase("hdpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_H_V);
                    }else if (Utility.getDensityName(ReportViewActivity.this).equalsIgnoreCase("xhdpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_X_V);
                    }else if (Utility.getDensityName(ReportViewActivity.this).equalsIgnoreCase("xxhdpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_XX_V);
                    }else if (Utility.getDensityName(ReportViewActivity.this).equalsIgnoreCase("xxxhdpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_XXX_V);
                    }else{
                        invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                    }
                }

            }

        }
    }




}
