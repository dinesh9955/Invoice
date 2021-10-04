package com.sirapp.Report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseActivity;
import com.sirapp.CN.CreditNotesViewActivityWebView;
import com.sirapp.Invoice.InvoiceViewActivityWebView;
import com.sirapp.R;
import com.sirapp.Receipts.ViewReceipt_Activity;
import com.sirapp.Utils.Utility;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;

public class PreviewItActivity extends BaseActivity {

    private static final String TAG = "PreviewItActivity";
    WebView invoiceweb;

    int positionNext = -1;
    String company_image_path = "";


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
        setContentView(R.layout.activity_view_invoice_);

        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);
        ImageView printimg = toolbar.findViewById(R.id.imageViewptint);
        printimg.setVisibility(View.GONE);

        invoiceweb = findViewById(R.id.invoiceweb);



        titleView.setText(getString(R.string.preview));

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
//        invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
        // webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        if(AllSirApi.FONT_INVOICE == true){
//            webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE);
//        }else{
//            invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
//        }

        callForWeb();


        invoiceweb.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }
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
        Log.e(TAG, "company_image_pathWW "+company_image_path);



        if(positionNext == 0){
            customerItemAA = (CustomerItem) bundle.getSerializable("customerItemAA");
            customerReportItemArrayListAA = (ArrayList<CustomerReportItem>) bundle.getSerializable("customerReportItemArrayListAA");
            customerReportWeb(customerItemAA, customerReportItemArrayListAA);
        } else if(positionNext == 1){
            supplierItemAA = (SupplierItem) bundle.getSerializable("supplierItemAA");
            supplierReportItemArrayListAA = (ArrayList<CustomerReportItem>) bundle.getSerializable("supplierReportItemArrayListAA");
            supplierReportWeb(supplierItemAA, supplierReportItemArrayListAA);
        } else if(positionNext == 2){
            totalSalesItemAA = (CompanyItem) bundle.getSerializable("totalSalesItemAA");
            totalSalesReportItemArrayListAA = (ArrayList<TotalSalesReportItem>) bundle.getSerializable("totalSalesReportItemArrayListAA");
            totalSalesReportWeb(totalSalesItemAA, totalSalesReportItemArrayListAA);
        } else if(positionNext == 3){
            totalPurchaseItemAA = (CompanyItem) bundle.getSerializable("totalPurchaseItemAA");
            totalPurchaseReportItemArrayListAA = (ArrayList<TotalPurchaseReportItem>) bundle.getSerializable("totalPurchaseReportItemArrayListAA");
            totalPurchaseReportWeb(totalPurchaseItemAA, totalPurchaseReportItemArrayListAA);
        } else if(positionNext == 4){
            customerAgeingItemAA = (CompanyItem) bundle.getSerializable("customerAgeingItemAA");
            customerAgeingReportItemArrayListAA = (ArrayList<CustomerAgeingReportItem>) bundle.getSerializable("customerAgeingReportItemArrayListAA");
            customerAgeingReportWeb(customerAgeingItemAA, customerAgeingReportItemArrayListAA);
        } else if(positionNext == 5){
            taxCollectedItemAA = (CompanyItem) bundle.getSerializable("taxCollectedItemAA");
            taxCollectedReportItemArrayListAA = (ArrayList<TaxCollectedReportItem>) bundle.getSerializable("taxCollectedReportItemArrayListAA");
            taxCollectedReportWeb(taxCollectedItemAA, taxCollectedReportItemArrayListAA);
        } else if(positionNext == 6){
            stockItemAA = (CompanyItem) bundle.getSerializable("stockItemAA");
            stockReportItemArrayListAA = (ArrayList<StockReportItem>) bundle.getSerializable("stockReportItemArrayListAA");
            stockReportWeb(stockItemAA, stockReportItemArrayListAA);
        } else if(positionNext == 7){
            productMovementItemAA = (CompanyItem) bundle.getSerializable("productMovementItemAA");
            productMovementItemArrayListAA = (ArrayList<ProductMovementReportItem>) bundle.getSerializable("productMovementItemArrayListAA");
            productMovementWeb(productMovementItemAA, productMovementItemArrayListAA);
        } else{

        }



    }






    private void customerReportWeb(CustomerItem customerItem, ArrayList<CustomerReportItem> customerReportItemArrayList) {

        double totalAmount = 0.0;

        double balanceAmount = 0.0;

        String name = "report/CustomerReport.html";
        String nameName = "file:///android_asset/report/CustomerReport.html";

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






                productitem = IOUtils.toString(getAssets().open("report/customer_single_item.html"))
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


    private void supplierReportWeb(SupplierItem customerItem, ArrayList<CustomerReportItem> customerReportItemArrayList) {

        double totalAmount = 0.0;
        double balanceAmount = 0.0;
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


                productitem = IOUtils.toString(getAssets().open("report/customer_single_item.html"))
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
//                if(doubleBalance == 0){
//                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
//                    stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
//                }else{
//                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
//                    stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
//                }

//                double doubleNotDue = Double.parseDouble(customerReportItemArrayList.get(i).getNot_due());
                String stringNotDue = "";
//                if(doubleNotDue == 0){
//                    stringNotDue = "";
//                }else{
//                    String stringNotDue1 = customerReportItemArrayList.get(i).getNot_due();
//                    stringNotDue = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringNotDue1)) + Utility.getReplaceDollor(cruncycode);
//                }

//                if(slab1 != 0 || slab2 != 0 || slab3 != 0 || slab4 != 0){
//                    stringNotDue = "0.0"+ Utility.getReplaceDollor(cruncycode);
//                }else{
//                    String stringNotDue1 = customerReportItemArrayList.get(i).getNot_due();
//                    stringNotDue = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringNotDue1)) + Utility.getReplaceDollor(cruncycode);
//                }

                if(doubleBalance == 0){
                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                    stringNotDue = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }else{
                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                    stringNotDue = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }


                double totalDD = 0.0;
                if(slab1 != 0){
                    totalDD = slab1 + doubleBalance;
                    stringBalance = Utility.getPatternFormat(""+numberPostion, totalDD) + Utility.getReplaceDollor(cruncycode);
                } else if(slab2 != 0){
                    totalDD = slab2 + doubleBalance;
                    stringBalance = Utility.getPatternFormat(""+numberPostion, totalDD) + Utility.getReplaceDollor(cruncycode);
                } else if(slab3 != 0){
                    totalDD = slab3 + doubleBalance;
                    stringBalance = Utility.getPatternFormat(""+numberPostion, totalDD) + Utility.getReplaceDollor(cruncycode);
                } else if(slab4 != 0){
                    totalDD = slab4 + doubleBalance;
                    stringBalance = Utility.getPatternFormat(""+numberPostion, totalDD) + Utility.getReplaceDollor(cruncycode);
                } else{
                    String stringBalance1 = customerReportItemArrayList.get(i).getTotal();
                    totalDD = doubleBalance;
                    stringBalance = Utility.getPatternFormat(""+numberPostion, Double.parseDouble(stringBalance1)) + Utility.getReplaceDollor(cruncycode);
                }


                productitem = IOUtils.toString(getAssets().open("report/customer_ageing_single_item.html"))
                        .replaceAll("#CustomerName#", customerReportItemArrayList.get(i).getCustomer_name())
                        .replaceAll("#Currentdue#", stringNotDue)
                        .replaceAll("#Slab1#", slab1Txt)
                        .replaceAll("#Slab2#", slab2Txt)
                        .replaceAll("#Slab3#", slab3Txt)
                        .replaceAll("#Slab4#", slab4Txt)
                        .replaceAll("#Amount#", stringBalance);
                productitemlist = productitemlist + productitem;

//                double allAmount = 0.0;
//
//                try{
//                    allAmount = Double.parseDouble(customerReportItemArrayList.get(i).getTotal());
//                }catch (Exception e){
//
//                }

                totalAmount =   totalAmount + totalDD;
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
                    minimumTxt = Utility.getPatternFormat(""+numberPostion, 0.00);
                }else{
                    String minimumSS = Utility.getPatternFormat(""+numberPostion, minimum);
                    minimumTxt = minimumSS;
                }

                String quantityTxt = "";
                if(quantity == 0){
                    quantityTxt =  Utility.getPatternFormat(""+numberPostion, 0.00);
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

//
//                totalAmount = totalAmount + pricePerUnitValue;


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


    private void productMovementWeb(CompanyItem customerItem, ArrayList<ProductMovementReportItem> customerReportItemArrayList) {

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


                productitem = IOUtils.toString(getAssets().open("report/product_movement_single_item.html"))
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
        webView.getSettings().setMinimumFontSize(webView.getSettings().getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_PRINT);
        //create object of print adapter
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        //provide name to your newly generated pdf file
        String jobName = getString(R.string.app_name) + " Print Test";

        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize( PrintAttributes.MediaSize.ISO_A4);
        printManager.print(jobName, printAdapter, builder.build());
    }



    @SuppressLint("LongLogTag")
    @Override
    protected void onResume() {
        super.onResume();
        callForWeb();
    }




    @SuppressLint("LongLogTag")
    private void callForWeb() {
        if(invoiceweb != null){

            WebSettings webSettings = invoiceweb.getSettings();

            Log.e(TAG, "isTablet "+Utility.isTablet(PreviewItActivity.this));
            if(Utility.isTablet(PreviewItActivity.this) == true){ // tab
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
                    if (Utility.getDensityName(PreviewItActivity.this).equalsIgnoreCase("ldpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_L_V);
                    }else if (Utility.getDensityName(PreviewItActivity.this).equalsIgnoreCase("mdpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_M_V);
                    }else if (Utility.getDensityName(PreviewItActivity.this).equalsIgnoreCase("hdpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_H_V);
                    }else if (Utility.getDensityName(PreviewItActivity.this).equalsIgnoreCase("xhdpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_X_V);
                    }else if (Utility.getDensityName(PreviewItActivity.this).equalsIgnoreCase("xxhdpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_XX_V);
                    }else if (Utility.getDensityName(PreviewItActivity.this).equalsIgnoreCase("xxxhdpi")){
                        webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_XXX_V);
                    }else{
                        invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                    }
                }

            }

        }
    }



}