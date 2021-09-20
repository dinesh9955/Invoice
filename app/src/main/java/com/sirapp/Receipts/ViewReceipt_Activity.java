package com.sirapp.Receipts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.RequestManager;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseActivity;
import com.sirapp.CN.CreditNotesViewActivityWebView;
import com.sirapp.Model.Customer_list;
import com.sirapp.Model.Product_list;
import com.sirapp.Model.View_invoice;
import com.sirapp.R;
import com.sirapp.Utils.Utility;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewReceipt_Activity extends BaseActivity {


    private static final String TAG = "ViewReceipt_Activity";
    WebView invoiceweb;

    String company_id = "", company_name = "", company_address = "", company_contact = "", company_email = "", company_website = "", payment_bank_name = "", payment_currency, payment_iban, payment_swift_bic;
    String customer_id = "", custoner_contact_name = "", customer_email = "", customer_contact = "", customer_address = "", customer_website = "";
    String  credit_terms = "";
    String signature_of_issuer = "", signature_of_receiver = "", company_stamp = "";
    String due_date = "", date = "", Shipingcosstbyct = "";
//    String invoice_name = "";

    ArrayList<View_invoice> products = new ArrayList<>();
    ArrayList<String> quantity = new ArrayList<>();
    ArrayList<String> unit_of_measurement = new ArrayList<>();
    ArrayList<String> rate = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();
    ArrayList<String> atchemntimg = new ArrayList<>();
    String attchedmentimagepath;
    ArrayList<String> attchmentimgbase64 = new ArrayList<>();
    String encodedImage, signature_of_receiverincode, encodesignatureissure, drableimagebase64, attchmentbase64;
    //product data
    ArrayList<Customer_list> customerselected = new ArrayList<>();
    ArrayList<String> tempQuantity = new ArrayList<String>();
    ArrayList<Product_list> myList;
    ArrayList<String> totalpriceproduct = new ArrayList<String>();

    ArrayList<String> producprice = new ArrayList<String>();

    String selectedTemplate = "";
    String companycolor = "";

    StringBuilder stringBuilderBillTo = new StringBuilder();
    StringBuilder stringBuilderShipTo = new StringBuilder();



    String paypal_emailstr = "", sltcustonername = "", sltcustomer_email = "", sltcustomer_contact = "", sltcustomer_address = "", sltcustomer_website = "", sltcustomer_phone_number = "";
    String strnotes = "",  ref_no ="", paid_amount_payment_method = "", freight_cost = "", strdiscountvalue = "", strpaid_amount = "", companylogopath = "", Grossamount_str = "", Subtotalamount = "", taxamount = "", netamountvalue = "", Blanceamountstr = "";
    String shippingzone = "", Paymentamountdate = "", shippingfirstname = "", shippinglastname = "", shippingaddress1 = "", shippingaddress2 = "", shippingcity = "", shippingpostcode = "", shippingcountry;
    ArrayList<Customer_list> customerinfo;
    String hiddenpaidrow = "";
    String paid_amount_payment;
    String Shipingdetail = "";
    String cruncycode = "", Shiping_tostr = "", companyimagelogopath = "";
    //int invoicenovalue;
    String invoicenobystr="";
    private RequestManager requestManager;

    String taxText = "";

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice_);
        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);
        ImageView printimg = toolbar.findViewById(R.id.imageViewptint);


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        printimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createWebPrintJob(invoiceweb);
            }
        });
        setSupportActionBar(toolbar);
        titleView.setText(getString(R.string.preview));

        drableimagebase64 = "iVBORw0KGgoAAAANSUhEUgAAAC4AAAAnCAYAAABwtnr/AAAAAXNSR0IArs4c6QAAADhlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAAqACAAQAAAABAAAALqADAAQAAAABAAAAJwAAAAB8SmRPAAAAeklEQVRYCe3SQQrAIBTEUPX+d67iCbIIBSGuw/B57fzOGw++9eDN9+QO//vLJZ44FOhXgVBalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYc2r3IESll5TkQAAAAASUVORK5CYII=";

        if (getIntent().hasExtra("company_id")) {


            customerselected = (ArrayList<Customer_list>) getIntent().getSerializableExtra("customerselected");
            if (customerselected.size() > 0) {
                for (int i = 0; i < customerselected.size(); i++) {
                    sltcustonername = customerselected.get(i).getCustomer_name();
                    sltcustomer_address = customerselected.get(i).getCustomer_address();
                    sltcustomer_email = customerselected.get(i).getCustomer_email();
                    sltcustomer_website = customerselected.get(i).getCustomer_website();
                    sltcustomer_phone_number = customerselected.get(i).getCustomer_phone();
                    sltcustomer_contact = customerselected.get(i).getCustomer_contact_person();


                    shippingfirstname = customerselected.get(i).getShipping_firstname();
                    shippinglastname = customerselected.get(i).getShipping_lastname();
                    shippingaddress1 = customerselected.get(i).getShipping_address_1();
                    shippingaddress2 = customerselected.get(i).getShipping_address_2();
                    shippingcity = customerselected.get(i).getShipping_city();
                    shippingcountry = customerselected.get(i).getShipping_country();
                    shippingpostcode = customerselected.get(i).getShipping_postcode();
                    shippingzone = customerselected.get(i).getShipping_zone();

                }



                if(!Utility.isEmptyNull(sltcustonername).equalsIgnoreCase("")){
                    stringBuilderBillTo.append(sltcustonername+"</br>");
                }
                if(!Utility.isEmptyNull(sltcustomer_address).equalsIgnoreCase("")){
                    stringBuilderBillTo.append(sltcustomer_address+"</br>");
                }
                if(!Utility.isEmptyNull(sltcustomer_contact).equalsIgnoreCase("")){
                    stringBuilderBillTo.append(sltcustomer_contact+"</br>");
                }
                if(!Utility.isEmptyNull(sltcustomer_phone_number).equalsIgnoreCase("")){
                    stringBuilderBillTo.append(sltcustomer_phone_number+"</br>");
                }
                if(!Utility.isEmptyNull(sltcustomer_website).equalsIgnoreCase("")){
                    stringBuilderBillTo.append(sltcustomer_website+"</br>");
                }
                if(!Utility.isEmptyNull(sltcustomer_email).equalsIgnoreCase("")){
                    stringBuilderBillTo.append(sltcustomer_email+"");
                }


            }


            if (shippingfirstname.equalsIgnoreCase("")) {
                Shiping_tostr = "";
            } else {
                Shiping_tostr = getString(R.string.html_ShipTo);
                Log.e(TAG, "shippingfirstnameAA "+shippingfirstname);

                if(!shippingfirstname.equalsIgnoreCase("")){
                    stringBuilderShipTo.append(shippingfirstname+" "+shippinglastname+"</br>");
                }
                if(!shippingpostcode.equalsIgnoreCase("")){
                    stringBuilderShipTo.append(shippingpostcode+"</br>");
                }
                if(!shippingcity.equalsIgnoreCase("")){
                    stringBuilderShipTo.append(shippingcity+", "+shippingcountry+"</br>");
                }
                if(!shippingaddress1.equalsIgnoreCase("")){
                    stringBuilderShipTo.append(shippingaddress1+", "+shippingaddress2+"</br>");
                }

                //Shipingdetail = shippingfirstname + "<br>\n" + shippinglastname + "<br>\n" + shippingaddress1 + "<br>\n" + shippingaddress2 + "<br>\n" + shippingcity + "<br>\n" + shippingcountry + "<br>\n" + shippingpostcode;
            }



            tempQuantity = (ArrayList<String>) getIntent().getSerializableExtra("tempQuantity");

            producprice = (ArrayList<String>) getIntent().getSerializableExtra("producprice");
            if (tempQuantity.size() > 0) {
                for (int i = 0; i < tempQuantity.size(); i++) {
                    Log.e("value", tempQuantity.get(i));
                }
            }
            myList = (ArrayList<Product_list>) getIntent().getSerializableExtra("tempList");

            for (int i = 0; i < myList.size(); i++) {
                Log.e("product[" + i + "]" + "[price]", myList.get(i).getProduct_price());
                Log.e("product[" + i + "]" + "[quantity]", tempQuantity.get(i));
                Log.e("product[" + i + "]" + "[product_id]", myList.get(i).getProduct_id());

            }

            Blanceamountstr = getIntent().getStringExtra("blanceamount");
            taxamount = getIntent().getStringExtra("invoicetaxamount");
            Log.e("taxamount", taxamount);

            netamountvalue = getIntent().getStringExtra("netamount");
            company_id = getIntent().getStringExtra("company_id");
            date = getIntent().getStringExtra("invoice_date");
            due_date = getIntent().getStringExtra("due_date");
            customer_id = getIntent().getStringExtra("customer_id");
            invoicenobystr = getIntent().getStringExtra("invoice_no");

//            invoicenovalue = Integer.parseInt(invoicenobystr) + 1;
//            invoice_no = String.valueOf(invoicenovalue);

            strnotes = getIntent().getStringExtra("notes");
            ref_no = getIntent().getStringExtra("ref_no");
            Subtotalamount = getIntent().getStringExtra("subtotalamt");
            paid_amount_payment_method = getIntent().getStringExtra("paid_amount_payment_method");

            if(paid_amount_payment_method != null){
                if (paid_amount_payment_method.equals("")) {
                    paid_amount_payment = "";
                } else {
                    paid_amount_payment = paid_amount_payment_method;

                }
            }

//            Shipingcosstbyct.replace("++", "+").replace("RsRs", "Rs")

            taxText = getIntent().getStringExtra("taxText");
           // companycolor = getIntent().getStringExtra("companycolor");

            selectedTemplate = getIntent().getStringExtra("selectedTemplate");

            paypal_emailstr = getIntent().getStringExtra("paypal_emailstr");
            credit_terms = getIntent().getStringExtra("credit_terms");
            freight_cost = getIntent().getStringExtra("freight_cost");
            strdiscountvalue = getIntent().getStringExtra("discount");
            strpaid_amount = getIntent().getStringExtra("paid_amount");
            Paymentamountdate = getIntent().getStringExtra("paid_amount_date");
            shippingfirstname = getIntent().getStringExtra("shipping_firstname");
            shippinglastname = getIntent().getStringExtra("shipping_lastname");
            shippingaddress1 = getIntent().getStringExtra("shipping_address_1");
            shippingaddress2 = getIntent().getStringExtra("shipping_address_2");
            shippingcity = getIntent().getStringExtra("shipping_city");
            shippingpostcode = getIntent().getStringExtra("shipping_postcode");
            shippingcountry = getIntent().getStringExtra("shipping_country");
            payment_bank_name = getIntent().getStringExtra("payment_bank_name");

            payment_iban = getIntent().getStringExtra("payment_iban");
            payment_swift_bic = getIntent().getStringExtra("payment_swift_bic");
            payment_currency = getIntent().getStringExtra("payment_currency");

            company_name = getIntent().getStringExtra("company_name");
            company_address = getIntent().getStringExtra("company_address");
            company_contact = getIntent().getStringExtra("company_contact");
            company_email = getIntent().getStringExtra("company_email");
            company_website = getIntent().getStringExtra("company_website");
            payment_bank_name = getIntent().getStringExtra("payment_bank_name");
            payment_currency = getIntent().getStringExtra("payment_currency");
            payment_iban = getIntent().getStringExtra("payment_iban");
            payment_swift_bic = getIntent().getStringExtra("payment_swift_bic");
            Grossamount_str = getIntent().getStringExtra("grossamount");
            custoner_contact_name = getIntent().getStringExtra("custoner_contact_name");
            customer_email = getIntent().getStringExtra("customer_email");
            customer_contact = getIntent().getStringExtra("customer_contact");
            customer_address = getIntent().getStringExtra("customer_address");
            customer_website = getIntent().getStringExtra("customer_website");
            //invoice_name = getIntent().getStringExtra("invoice_name");

            companylogopath = getIntent().getStringExtra("company_logo");
            //products = getIntent().getStringArrayListExtra("products_list");
            quantity = getIntent().getStringArrayListExtra("quantity_list");
            rate = getIntent().getStringArrayListExtra("rate_list");
            totalpriceproduct = getIntent().getStringArrayListExtra("totalpriceproduct");

            atchemntimg = getIntent().getStringArrayListExtra("attchemnt");

            signature_of_issuer = getIntent().getStringExtra("signature_issuer");

            signature_of_receiver = getIntent().getStringExtra("signature_of_receiver");
            company_stamp = getIntent().getStringExtra("company_stamp");



            if (signature_of_issuer != null) {
                try {

                    Bitmap bm3 = BitmapFactory.decodeFile(signature_of_issuer);
                    ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
                    bm3.compress(Bitmap.CompressFormat.JPEG, 100, baos3); // bm is the bitmap object
                    byte[] b3 = baos3.toByteArray();
                    encodesignatureissure = Base64.encodeToString(b3, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                encodesignatureissure = drableimagebase64;

            }
            // Log.e("Byte array Image", encodesignatureissure);


            if (company_stamp != null) {
                try {

                    Bitmap bm = BitmapFactory.decodeFile(company_stamp);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                encodedImage = drableimagebase64;

            }
            //  Log.e("Byte array Image", encodedImage);
            if (signature_of_receiver != null) {
                try {
                    Bitmap bm1 = BitmapFactory.decodeFile(signature_of_receiver);
                    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                    bm1.compress(Bitmap.CompressFormat.JPEG, 100, baos1); // bm is the bitmap object
                    byte[] b1 = baos1.toByteArray();
                    signature_of_receiverincode = Base64.encodeToString(b1, Base64.DEFAULT);

                    // Log.e("Byte array Image", encodedImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                signature_of_receiverincode = drableimagebase64;

            }
        }


        for (int i = 0; i < quantity.size(); i++) {

            String qty = String.valueOf(quantity);
            Log.e("logqty", qty);

        }


        view_invoice();

    }

    String convertBitmapToBase64(String path) {


        Bitmap bm3 = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
        bm3.compress(Bitmap.CompressFormat.JPEG, 100, baos3); // bm is the bitmap object
        byte[] b3 = baos3.toByteArray();
        return Base64.encodeToString(b3, Base64.DEFAULT);
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

    private String AttchmentimageConvert(String attchedmentimagepath) {

        Bitmap bm11 = BitmapFactory.decodeFile(attchedmentimagepath);
        ByteArrayOutputStream baos11 = new ByteArrayOutputStream();
        bm11.compress(Bitmap.CompressFormat.JPEG, 100, baos11); // bm is the bitmap object
        byte[] b11 = baos11.toByteArray();
        attchmentbase64 = Base64.encodeToString(b11, Base64.DEFAULT);

        // Log.e("attchmentbase64",attchmentbase64);
        return attchmentbase64;
    }

    public void view_invoice() {
        invoiceweb = findViewById(R.id.invoiceweb);


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

        if(Utility.getDensityName(ViewReceipt_Activity.this).equalsIgnoreCase("hdpi") ||
                Utility.getDensityName(ViewReceipt_Activity.this).equalsIgnoreCase("mdpi") ||
                Utility.getDensityName(ViewReceipt_Activity.this).equalsIgnoreCase("ldpi") ){
            Log.e(TAG, "TTTTTTTTTTTTTTT");
            if(AllSirApi.FONT_INVOICE == true){
                webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE);
            }else{
                invoiceweb.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
            }
        }else{
            Log.e(TAG, "SSSSSSSSSSSSSSS");
            if(AllSirApi.FONT_INVOICE == true){
                webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE);
            }else{
                invoiceweb.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
            }
        }

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

        String multipleimage = "";

        String multipagepath = null;

        if (atchemntimg != null) {
            for (int i = 0; i < atchemntimg.size(); i++) {
                attchedmentimagepath = atchemntimg.get(i);
                try {

                    multipagepath = IOUtils.toString(getAssets().open("attchment.html"))


                            .replaceAll("#ATTACHMENT_1#", atchemntimg.get(i));


                    multipleimage = multipleimage + multipagepath;
                } catch (Exception e) {

                }

            }
        } else {
            multipleimage = "";
        }


        String productitem = null;

        String productitemlist = "";
        try {
            for (int i = 0; i < myList.size(); i++) {
                cruncycode = myList.get(i).getCurrency_code();

             //   DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

                double productQuantity = Double.parseDouble(tempQuantity.get(i));
                double producpriceRate = Double.parseDouble(producprice.get(i));
                double producpriceAmount = producpriceRate * productQuantity;

                String stringFormatQuantity = Utility.getPatternFormat(""+numberPostion, productQuantity);
                String stringFormatRate = Utility.getPatternFormat(""+numberPostion, producpriceRate);
                String stringFormatAmount = Utility.getPatternFormat(""+numberPostion, producpriceAmount);

                productitem = IOUtils.toString(getAssets().open("single_item.html"))

                        .replaceAll("#NAME#", myList.get(i).getProduct_name())
                        .replaceAll("#DESC#", myList.get(i).getProduct_description())
                        .replaceAll("#UNIT#", myList.get(i).getProduct_measurement_unit())
                        .replaceAll("#QUANTITY#", ""+stringFormatQuantity)
                        .replaceAll("#PRICE#", ""+stringFormatRate +"" + Utility.getReplaceDollor(cruncycode))
                        .replaceAll("#TOTAL#", ""+stringFormatAmount +"" + Utility.getReplaceDollor(cruncycode));

                productitemlist = productitemlist + productitem;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        String attachmentimage = "";

        if (atchemntimg.size() < 1) {
            attachmentimage = "";

        } else {
            attachmentimage = getString(R.string.html_Attachments);
        }
        String notestringvalue = "";
        if (strnotes.equals("")) {
            notestringvalue = "";
        } else {
            notestringvalue = getString(R.string.html_Notes);
        }



        String signatureinvoice = null;
        String companyname = "";
        if (company_stamp.equals("") || company_stamp.endsWith("white_img.png")) {
            company_stamp = "/android_res/drawable/white_img.png";
            companyname = "";
        } else {
            companyname = getString(R.string.html_CompanySeal);
        }

        String signature_of_receivername = "";
        if (signature_of_receiver.equals("") || signature_of_receiver.endsWith("white_img.png")) {
            signature_of_receiver = "/android_res/drawable/white_img.png";
            signature_of_receivername = "";
        } else {
            signature_of_receivername = getString(R.string.html_SignatureofReceiver);
        }


        String signature_of_issuername = "";
        if (signature_of_issuer.equals("") || signature_of_issuer.endsWith("white_img.png")) {
            signature_of_issuer = "/android_res/drawable/white_img.png";
            signature_of_issuername = "";
        } else {
            signature_of_issuername = getString(R.string.html_SignatureofIssuer);
        }


        try {
            signatureinvoice = IOUtils.toString(getAssets().open("Signatures.html"))
                    .replaceAll("dataimageCompany_Stamp", "file://" + company_stamp)
                    .replaceAll("CompanyStamp", companyname)
                    .replaceAll("SignatureofReceiver", signature_of_receivername)
                    .replaceAll("SignatureofIssuer", signature_of_issuername)
                    .replaceAll("dataimageRecieverImage", "file://" + signature_of_receiver)
                    .replaceAll("data:imageSige_path", "file://" + signature_of_issuer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String taxtamountstr = "";
        String companywebsitestr = "";
        String taxtamountstrvalue = "";
        if (taxamount.equals("0")) {
            // Do you work here on success
            taxtamountstr = "";
            taxtamountstrvalue = "";

        } else {
            // null response or Exception occur
            taxtamountstr = taxamount;
            taxtamountstrvalue = ""+taxText;
        }

        String discountvalue = "";
        String discounttxtreplace = "";

        if (strdiscountvalue.equals("0")) {
            // Do you work here on success
            discountvalue = "";
            discounttxtreplace = "";

        } else {
            // null response or Exception occur
            discountvalue = strdiscountvalue;
            discounttxtreplace = " Discount ";
        }


        String subTotalTxt = "";
        String subTotalValueTxt = "";

        if(strdiscountvalue.equalsIgnoreCase("0")){
            subTotalTxt = "";
            subTotalValueTxt = "";
        }else{
            subTotalTxt = getString(R.string.html_SubTotal);
            subTotalValueTxt = Utility.getReplaceDollor(Subtotalamount);
        }



        if (company_website != null) {
            // Do you work here on success

            companywebsitestr = company_website;
        } else {
            // null response or Exception occur
            companywebsitestr = "0";
        }
        String Signatureincoicestr = "";

        if (signatureinvoice != null) {
            // Do you work here on success

            Signatureincoicestr = signatureinvoice;
        } else {
            // null response or Exception occur
            Signatureincoicestr = "";
        }
        String shipingvaluetxt = "";
        if (freight_cost.equals("0")) {
            // Do you work here on success
            Shipingcosstbyct = "";
            shipingvaluetxt = "";


        } else {
            // null response or Exception occur

            if (freight_cost.startsWith("+") && freight_cost.endsWith("Af"))
            {
                Shipingcosstbyct = freight_cost;
            }
            else
            {
                Shipingcosstbyct = "" + freight_cost;
            }


            shipingvaluetxt = getString(R.string.html_Shipping);
        }

        if (companylogopath.toLowerCase().endsWith(".jpg") || companylogopath.toLowerCase().endsWith(".jpeg") || companylogopath.toLowerCase().endsWith(".png")){
            companyimagelogopath = companylogopath;
        }else{
            companyimagelogopath = "/android_res/drawable/white_img.png";
        }

        Log.e(TAG, "companyimagelogopathXX "+companyimagelogopath);

        String paidamountstrrepvalue = "";
        String paidamountstrreptxt = "";
        String paidamountstrreplace = "";
        String chektopaidmaount = "";
        String payment_bankstr = "";
        String payment_ibanstr = "";
        String payment_currencystr = "";
        String payment_swiftstr = "";
        String pemailpaidstr = "";
        String paimnetdetailstrtxt="";
        String bycheckstrtxt="";
        String paypalstrtxt="";
        String bankstrtxt="";


        if (strpaid_amount.equals("0")) {
            // Do you work here on success
            paidamountstrrepvalue = "";
            paidamountstrreptxt = "";
            chektopaidmaount = "";
            payment_bankstr = "";
            payment_ibanstr = "";
            payment_currencystr = "";
            payment_swiftstr = "";
            pemailpaidstr = "";
            paimnetdetailstrtxt="";
            bycheckstrtxt="";
            paypalstrtxt="";
            bankstrtxt="";
            hiddenpaidrow="hidden";



        } else {
            // null response or Exception occur
            paidamountstrrepvalue =strpaid_amount;
            paidamountstrreptxt = getString(R.string.html_PaidAmount);


            pemailpaidstr = paypal_emailstr;
            chektopaidmaount = paid_amount_payment;
            payment_bankstr = payment_bank_name;
            payment_ibanstr = payment_iban;
            payment_currencystr = payment_currency;
            payment_swiftstr = payment_swift_bic;

            paimnetdetailstrtxt= getString(R.string.html_PaymentDetails);
            bycheckstrtxt="By cheque :";
            paypalstrtxt="PayPal :";
            bankstrtxt="Bank :";


            hiddenpaidrow="";
        }
        String strreferencenovalue="";
        String strreferencenotxtvalue="";

        Log.e(TAG, "ref_noAA "+ref_no);

        if (ref_no.isEmpty()) {
            // Do you work here on success

            strreferencenovalue="";
            strreferencenotxtvalue="";

        } else {
            // null response or Exception occur

            strreferencenovalue = ref_no;
            strreferencenotxtvalue= getString(R.string.html_ReferenceNo);


        }






        Log.e(TAG, "selectedTemplate "+selectedTemplate);

        String name = "receipt.html";
        String nameName = "file:///android_asset/receipt.html";
//        if(selectedTemplate.equalsIgnoreCase("0")){
//            name = "receipt.html";
//            nameName = "file:///android_asset/receipt.html";
//        }else if(selectedTemplate.equalsIgnoreCase("1")){
//            name = "receipt.html";
//            nameName = "file:///android_asset/receipt.html";
//        }else if(selectedTemplate.equalsIgnoreCase("2")){
//            name = "receipt.html";
//            nameName = "file:///android_asset/receipt.html";
//        }else if(selectedTemplate.equalsIgnoreCase("3")){
//            name = "receipt.html";
//            nameName = "file:///android_asset/receipt.html";
//        }else if(selectedTemplate.equalsIgnoreCase("4")){
//            name = "receipt.html";
//            nameName = "file:///android_asset/receipt.html";
//        }

       Log.e(TAG, "strreferencenovalueAA "+strreferencenovalue);



        StringBuilder stringBuilderCompany = new StringBuilder();

        if(!company_address.equalsIgnoreCase("")){
            stringBuilderCompany.append(company_address+"</br>");
        }
        if(!company_contact.equalsIgnoreCase("")){
            stringBuilderCompany.append(company_contact+"</br>");
        }
        if(!company_website.equalsIgnoreCase("")){
            stringBuilderCompany.append(company_website+"</br>");
        }
        if(!company_email.equalsIgnoreCase("")){
            stringBuilderCompany.append(company_email+"");
        }




            String htmlview_credit_note = getString(R.string.htmlview_receipt);
            String htmlview_Recipient = getString(R.string.htmlview_ReceivedFrom);
            String htmlview_CreditNoteNo = getString(R.string.htmlview_ReceiptNo);
            String htmlview_CreditNoteDate = getString(R.string.htmlview_ReceiptDate);
            String htmlview_ReferenceNo = getString(R.string.htmlview_ReferenceNo);
            String htmlview_SUMMARY = getString(R.string.htmlview_SUMMARY);
            String htmlview_ProductItem = getString(R.string.htmlview_ProductItem);
            String htmlview_UnitofMeasurement = getString(R.string.htmlview_UnitofMeasurement);
            String htmlview_Quantity = getString(R.string.htmlview_Quantity);
            String htmlview_Rate = getString(R.string.htmlview_Rate);
            String htmlview_Amount = getString(R.string.htmlview_Amount);
            String htmlview_GrossAmount = getString(R.string.htmlview_GrossAmount);
            String htmlview_Discount = getString(R.string.htmlview_Discount);
            String htmlview_SubTotal = getString(R.string.htmlview_SubTotal);
            String htmlview_Tax = getString(R.string.htmlview_Tax);
            String htmlview_Shipping = getString(R.string.htmlview_Shipping);
            String htmlview_NetAmount = getString(R.string.htmlview_NetAmount);
            String htmlview_Notes = getString(R.string.htmlview_Notes);
            String htmlview_Attachments = getString(R.string.htmlview_Attachments);
            String htmlview_PaidAmount = getString(R.string.htmlview_PaidAmount);


        String content = null;
        try {
            content = IOUtils.toString(getAssets().open(name))


                    .replaceAll("Title_", htmlview_credit_note)
                    .replaceAll("Received From", htmlview_Recipient)
                    .replaceAll("Receipt No", htmlview_CreditNoteNo)
                    .replaceAll("Receipt Date", htmlview_CreditNoteDate)
//                        .replaceAll("Reference No", htmlview_ReferenceNo)
                    .replaceAll("SUMMARY", htmlview_SUMMARY)
                    .replaceAll("Product/Item", htmlview_ProductItem)
                    .replaceAll("Unit of Measurement", htmlview_UnitofMeasurement)
                    .replaceAll("Quantity", htmlview_Quantity)
                    .replaceAll("Rate", htmlview_Rate)
                    .replaceAll("Amount_", htmlview_Amount)
                    .replaceAll("Gross Amount", htmlview_GrossAmount)
//                        .replaceAll("Discount", htmlview_Discount)
//                        .replaceAll("SubTotal", htmlview_SubTotal)
//                        .replaceAll("Tax", htmlview_Tax)
//                        .replaceAll("Shipping", htmlview_Shipping)
                    .replaceAll("Net Amount", htmlview_NetAmount)
//                        .replaceAll("Paid Amount", htmlview_PaidAmount)
//                        .replaceAll("Notes:", htmlview_Notes)
//                        .replaceAll("Attachments", htmlview_Attachments)


//                    .replaceAll("INVOICE", "RECEIPT")
//                    .replaceAll("Bill To", "Received From")
//                    .replaceAll("Ship To:", "")
//                    .replaceAll("#Shipp", "")
//                    .replaceAll("Invoice No", "Receipt No")
//                    .replaceAll("Invoice Date", "Receipt Date")
//
//                    .replaceAll("Terms:", "")
//                    .replaceAll("crTerms", "")
//                    .replaceAll("Due Date:", "")
//                    .replaceAll("DueDate", "")


                    .replaceAll("Company Name", company_name)
                    .replaceAll("Address", stringBuilderCompany.toString())
//                    .replaceAll("Contact No.", company_contact)
//                    .replaceAll("Website", companywebsitestr)
//                    .replaceAll("Email", company_email)
                    .replaceAll("#LOGO_IMAGE#", companyimagelogopath)
                    .replaceAll("INV-564", invoicenobystr)
                    .replaceAll("invD", date)
                    .replaceAll("DueDate", due_date)
                    .replaceAll("crTerms", credit_terms)
                    .replaceAll("refNo", strreferencenovalue)
                    .replaceAll("GrossAm-", Utility.getReplaceDollor(Grossamount_str))
                    .replaceAll("Discount-", Utility.getReplaceDollor(discountvalue))
                    .replaceAll("SubTotal-", subTotalValueTxt)
                    .replaceAll("Txses-", Utility.getReplaceDollor(taxtamountstr))
                    .replaceAll("Shipping-", Utility.getReplaceDollor(Shipingcosstbyct))
                    .replaceAll("Total Amount-", Utility.getReplaceDollor(netamountvalue))
                    .replaceAll("PaidsAmount", Utility.getReplaceDollor(paidamountstrrepvalue))
                    .replaceAll("Paid Amount", paidamountstrreptxt)
                    .replaceAll("Balance Due-", Utility.getReplaceDollor(Blanceamountstr))

                    .replaceAll("SubTotal", subTotalTxt)
                    .replaceAll("Checkto", chektopaidmaount)
                    .replaceAll("BankName", payment_bankstr)
                    .replaceAll("Pemail", pemailpaidstr)
                    .replaceAll("IBAN", payment_ibanstr)
                    .replaceAll("Currency", payment_currencystr)
                    .replaceAll("Swift/BICCode", payment_swiftstr)
                    .replaceAll("Client N", ""+stringBuilderBillTo.toString())
//                    .replaceAll("Client A", sltcustomer_address)
//                    .replaceAll("Client C P", sltcustomer_contact)
//                    .replaceAll("Client C N", sltcustomer_phone_number)
//                    .replaceAll("Client Web", sltcustomer_website)
//                    .replaceAll("Client E", sltcustomer_email)
                    .replaceAll("Notes-", strnotes)
                    .replaceAll("#SIGNATURES#", Signatureincoicestr)
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("#Shipp", ""+stringBuilderShipTo.toString())
                    .replaceAll("#ATTACHMENTS#", multipleimage)
                    .replaceAll("Attachments", attachmentimage)
                    .replaceAll("Notes:", notestringvalue)
                    .replaceAll("Ship To:", Shiping_tostr)
                    .replaceAll(" Shipping ", shipingvaluetxt)
                    .replaceAll(" Tax ", taxtamountstrvalue)
                    .replaceAll(" Discount ", discounttxtreplace)
                    .replaceAll("Reference No:", strreferencenotxtvalue)
                    .replaceAll("hide", hiddenpaidrow)

                    .replaceAll("#799f6e", companycolor)


                    .replaceAll(" Payment Details ", paimnetdetailstrtxt)
                    .replaceAll("By cheque :", bycheckstrtxt)
                    .replaceAll("PayPal :", paypalstrtxt)
                    .replaceAll("Bank :", bankstrtxt)
                    .replaceAll("#TEMP3#", String.valueOf(R.color.blue));



        } catch (IOException e) {
            e.printStackTrace();

        }

        Log.e("ViewInvoice__",content);

//        final File savedPDFFile = FileManager.getInstance().createTempFile(ViewInvoice_Activity.this, "pdf", false);
//
////                String content22  = " <!DOCTYPE html>\n" +
////                        "<html>\n" +
////                        "<body>\n" +
////                        "\n" +
////                        "<h1>My First Heading</h1>\n" +
////                        "<p>My first paragraph.</p>\n" +
////                        " <a href='https://www.example.com'>This is a link</a>" +
////                        "\n" +
////                        "</body>\n" +
////                        "</html> ";
//
//
//                PDFUtil.generatePDFFromHTML(ViewInvoice_Activity.this, savedPDFFile, content , new PDFPrint.OnPDFPrintListener() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void onSuccess(File file) {
//
//                        Log.e(TAG, "file!!! "+file);
//
//                        // Open Pdf Viewer
//                        // Uri pdfUri = Uri.fromFile(file);
//
//
//                        //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/example.pdf");
////                        Intent intent = new Intent(Intent.ACTION_VIEW);
////                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
////                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
////                        startActivity(intent);
//
//
//
//                        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//                        //File fileWithinMyDir = new File(pdfUri);
//
//                        if(file.exists()) {
//                            Uri photoURI = FileProvider.getUriForFile(ViewInvoice_Activity.this,
//                                    "com.sirapp.provider",
//                                    file);
//                            intentShareFile.setType("application/pdf");
//                            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(""+photoURI));
//
//                            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
//                                    "Share As Pdf");
//                            //  intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
//
//                            startActivity(Intent.createChooser(intentShareFile, "Share File"));
//                        }
//
////                        Intent intentPdfViewer = new Intent(Abc.this, PDFViewerActivity.class);
////                        intentPdfViewer.putExtra(PDFViewerActivity.PDF_FILE_URI, pdfUri);
////
////                        startActivity(intentPdfViewer);
//                    }
//
//                    @Override
//                    public void onError(Exception exception) {
//                        exception.printStackTrace();
//                    }
//                });

        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Create Temp File to save Pdf To
//                final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
//// Generate Pdf From Html
//                PDFUtil.generatePDFFromWebView(savedPDFFile, invoiceweb, new PDFPrint.OnPDFPrintListener() {
//                    @Override
//                    public void onSuccess(File file) {
//                        // Open Pdf Viewer
//                        Uri pdfUri = Uri.fromFile(savedPDFFile);
//
//                        Intent intentPdfViewer = new Intent(ViewInvoice_Activity.this, PDFViewerActivity.class);
//                        intentPdfViewer.putExtra(PDFViewerActivity.PDF_FILE_URI, pdfUri);
//
//                        startActivity(intentPdfViewer);
//                    }
//
//                    @Override
//                    public void onError(Exception exception) {
//                        exception.printStackTrace();
//                    }
//                });
//            }
//        }, 2000);


    }



    @SuppressLint("LongLogTag")
    @Override
    protected void onResume() {
        super.onResume();
        if(invoiceweb != null){
            if(Utility.getDensityName(ViewReceipt_Activity.this).equalsIgnoreCase("hdpi") ||
                    Utility.getDensityName(ViewReceipt_Activity.this).equalsIgnoreCase("mdpi") ||
                    Utility.getDensityName(ViewReceipt_Activity.this).equalsIgnoreCase("ldpi") ){
                Log.e(TAG, "TTTTTTTTTTTTTTT");
                if(AllSirApi.FONT_INVOICE == true){
                    invoiceweb.getSettings().setMinimumFontSize(invoiceweb.getSettings().getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE);
                }else{
                    invoiceweb.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                }
            }else{
                Log.e(TAG, "SSSSSSSSSSSSSSS");
                if(AllSirApi.FONT_INVOICE == true){
                    invoiceweb.getSettings().setMinimumFontSize(invoiceweb.getSettings().getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE);
                }else{
                    invoiceweb.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                }
            }

        }
    }
}