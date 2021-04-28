package com.receipt.invoice.stock.sirproject.Invoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.receipt.invoice.stock.sirproject.Model.Customer_list;
import com.receipt.invoice.stock.sirproject.Model.Product_list;
import com.receipt.invoice.stock.sirproject.Model.View_invoice;
import com.receipt.invoice.stock.sirproject.R;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class InvoiceToReceiptsWebview extends AppCompatActivity {


    private static final String TAG = "InvoiceToReceiptsWebview";
    WebView invoiceweb;

    String company_id = "", company_name = "", company_address = "", company_contact = "", company_email = "", company_website = "", payment_bank_name = "", payment_currency,
            payment_iban, payment_swift_bic;
    String customer_id = "", custoner_contact_name = "", customer_email = "", customer_contact = "", customer_address = "", customer_website = "";
    String invoice_name = "", credit_terms = "";
    String signature_of_receiver = "", company_stamp = "";
    String due_date = "", date = "";
  String Shiping_tostr="";

    ArrayList<String> quantity = new ArrayList<>();

    ArrayList<String> rate = new ArrayList<>();

    ArrayList<String> atchemntimg = new ArrayList<>();
    ArrayList<String> totalpriceproduct = new ArrayList<String>();

  String crunancycode="",paypal_emailstr="";
    String encodedImage="", signature_of_receiverincode="", drableimagebase64;
    //product data
    ArrayList<Customer_list> customerselected;
    ArrayList<String> tempQuantity;
    ArrayList<Product_list> myList = new ArrayList<>();
    String sltcustonername = "", sltcustomer_email = "", sltcustomer_contact = "", sltcustomer_address = "", sltcustomer_website = "", sltcustomer_phone_number = "";
    String strnotes = "", invoice_no = "", ref_no = "", paid_amount_payment_method = "", freight_cost = "", strdiscountvalue = "",
            strpaid_amount = "", companylogopath = "", Grossamount_str = "", Subtotalamount = "", taxamount="", netamountvalue = "", Blanceamountstr = "";
    String shippingzone="",Paymentamountdate = "", shippingfirstname, shippinglastname = "", shippingaddress1 = "", shippingaddress2 = "", shippingcity = "", shippingpostcode = "", shippingcountry;
    private RequestManager requestManager;

    String Shipingcosstbyct="";

    String Shipingdetail;
    @SuppressLint("LongLogTag")
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
        titleView.setText("Preview Receipt");

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
            }


            Shipingdetail= shippingfirstname+"<br>\n"+shippinglastname+"<br>\n"+shippingaddress1+"<br>\n"+shippingaddress2+"<br>\n"+shippingcity+"<br>\n"+shippingcountry+"<br>\n"+shippingpostcode+"<br>\n"+shippingzone;

            tempQuantity = (ArrayList<String>) getIntent().getSerializableExtra("tempQuantity");
            if (tempQuantity.size() > 0) {
                for (int i = 0; i < tempQuantity.size(); i++) {
                    Log.e("value", tempQuantity.get(i));
                }
            }
            myList = (ArrayList<Product_list>) getIntent().getSerializableExtra("tempList");


            Gson gson = new Gson();
            String json = gson.toJson(myList);

            Log.e(TAG, "jsonAA "+json);


            for (int i = 0; i < myList.size(); i++) {
                Log.e(TAG, "getProduct_price "+myList.get(i).getProduct_price());
                Log.e(TAG, "getProduct_id "+myList.get(i).getProduct_id());
                Log.e(TAG, "tempQuantity "+tempQuantity.get(i));



//                Log.e("product[" + i + "]" + "[price]", myList.get(i).getProduct_price());
//                Log.e("product[" + i + "]" + "[quantity]", tempQuantity.get(i));
//                Log.e("product[" + i + "]" + "[product_id]", myList.get(i).getProduct_id());
                crunancycode=myList.get(i).getCurrency_code();
            }

            Blanceamountstr = getIntent().getStringExtra("blanceamount");
            taxamount = getIntent().getStringExtra("taxamount");

            Log.e(TAG , "taxamount22 "+ taxamount);

            netamountvalue = getIntent().getStringExtra("netamount");
            company_id = getIntent().getStringExtra("company_id");
            date = getIntent().getStringExtra("invoice_date");
            due_date = getIntent().getStringExtra("due_date");
            customer_id = getIntent().getStringExtra("customer_id");
            invoice_no = getIntent().getStringExtra("invoice_no");
            strnotes = getIntent().getStringExtra("notes");
            ref_no = getIntent().getStringExtra("ref_no");
            Subtotalamount = getIntent().getStringExtra("subtotalamt");

            Log.e(TAG, "Subtotalamount"+Subtotalamount);
          //  Log.e(TAG, "Subtotalamount"+Subtotalamount);
            
            paid_amount_payment_method = getIntent().getStringExtra("paid_amount_payment_method");
            credit_terms = getIntent().getStringExtra("credit_terms");
            freight_cost = getIntent().getStringExtra("freight_cost");
            Log.e(TAG, "freight_cost"+freight_cost);

            strdiscountvalue = getIntent().getStringExtra("discount");

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
          //  payment_iban = getIntent().getStringExtra("payment_iban");
            payment_swift_bic = getIntent().getStringExtra("payment_swift_bic");
            Grossamount_str = getIntent().getStringExtra("grossamount");
            custoner_contact_name = getIntent().getStringExtra("custoner_contact_name");
            customer_email = getIntent().getStringExtra("customer_email");
            customer_contact = getIntent().getStringExtra("customer_contact");
            customer_address = getIntent().getStringExtra("customer_address");
            customer_website = getIntent().getStringExtra("customer_website");
            invoice_name = getIntent().getStringExtra("invoice_name");

            paypal_emailstr=getIntent().getStringExtra("paypal_emailstr");

            companylogopath = getIntent().getStringExtra("company_logo");
            //products = getIntent().getStringArrayListExtra("products_list");
            quantity = getIntent().getStringArrayListExtra("quantity_list");
            rate = getIntent().getStringArrayListExtra("rate_list");

            atchemntimg = getIntent().getStringArrayListExtra("attchemnt");



            totalpriceproduct = getIntent().getStringArrayListExtra("totalpriceproduct");

            signature_of_receiver = getIntent().getStringExtra("signature_of_receiver");
            company_stamp = getIntent().getStringExtra("company_stamp");

            Log.e(TAG, "signature_of_receiver "+signature_of_receiver);
            Log.e(TAG, "company_stamp "+company_stamp);


        }


        for (int i = 0; i < quantity.size(); i++) {

            String qty = String.valueOf(quantity);
            Log.e("logqty", qty);

        }


        view_invoice();

    }


    private void createWebPrintJob(WebView webView) {

        //create object of print manager in your device
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        //create object of print adapter
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        //provide name to your newly generated pdf file
        String jobName = getString(R.string.app_name) + " Print Test";

        //open print dialog
        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }


    @SuppressLint("LongLogTag")
    public void view_invoice() {
        invoiceweb = findViewById(R.id.invoiceweb);
        invoiceweb.getSettings().setAllowFileAccess(true);
        WebSettings webSettings = invoiceweb.getSettings();
        webSettings.setJavaScriptEnabled(true);
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


        String productitem = null;

        String productitemlist ="";
        try {
            for (int i = 0; i < myList.size(); i++) {
             //   Log.e(TAG , "myList.get(i).getProduct_description()" +myList.get(i).getProduct_description());



                productitem = IOUtils.toString(getAssets().open("single_item.html"))
                        .replaceAll("#NAME#", myList.get(i).getProduct_name())
                        .replaceAll("#DESC#", myList.get(i).getProduct_description() )
                        .replaceAll("#UNIT#", myList.get(i).getProduct_measurement_unit())
                        .replaceAll("#QUANTITY#", tempQuantity.get(i))
                        .replaceAll("#PRICE#",  myList.get(i).getProduct_price())
                        .replaceAll("#TOTAL#", totalpriceproduct.get(i)+myList.get(i).getCurrency_code());

                productitemlist = productitemlist + productitem;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }





        String companyname;
        if(company_stamp.equals(""))
        {
            company_stamp="file:///android_res/drawable/white_img.png";
            companyname="";
        }else {
            companyname="Company Seal";
        }


        String signature_of_receivername;
        if(signature_of_receiver.equals(""))
        {
            signature_of_receiver="file:///android_res/drawable/white_img.png";
            signature_of_receivername="";
        }else {
            signature_of_receivername="Signature of Receiver";
        }


        String signatureinvoice = null;

        try {
            signatureinvoice = IOUtils.toString(getAssets().open("Signatures.html"))
                    .replaceAll("SignatureofIssuer", "")
                    .replaceAll("CompanyStamp", companyname)
                    .replaceAll("SignatureofReceiver", signature_of_receivername)
                    .replaceAll("data:imageSige_path", "/android_res/drawable/white_img.png")
                    .replaceAll("dataimageCompany_Stamp", company_stamp)
                    .replaceAll("dataimageRecieverImage", signature_of_receiver);

        } catch (IOException e) {
            e.printStackTrace();
        }



        String notestringvalue="";
        if(strnotes.equals(""))
        {
            notestringvalue="";
        }else
        {
            notestringvalue="Notes:";
        }



        String companywebsitestr="";

        if(company_website !=null ){
            // Do you work here on success

            companywebsitestr=company_website;
        }else{
            // null response or Exception occur
            companywebsitestr="0";
        }




        if(sltcustonername !=null)
        {
            Shiping_tostr="Ship To:";

        }else {
            Shiping_tostr="";

        }

        String shipingvaluetxt="";
        if(freight_cost.equals("0") ){
            // Do you work here on success
            Shipingcosstbyct="";
            shipingvaluetxt="";


        }else{
            // null response or Exception occur

            Shipingcosstbyct=""+freight_cost;
            shipingvaluetxt="Shipping";
        }






        String paidamountstrreplace = "";
        String chektopaidmaount = "";
        String payment_bankstr = "";
        String payment_ibanstr = "";
        String payment_currencystr = "";
        String payment_swiftstr = "";
        String pemailpaidstr = "";
        String hiddenpaidrow="";

        String paimnetdetailstrtxt="";
        String bycheckstrtxt="";
        String paypalstrtxt="";
        String bankstrtxt="";

        if (strpaid_amount.equals("0")) {
            // Do you work here on success
            paidamountstrreplace = "";
            chektopaidmaount = "";
            payment_bankstr = "";
            payment_ibanstr = "";
            payment_currencystr = "";
            payment_swiftstr = "";
            pemailpaidstr = "";
            hiddenpaidrow="hidden";

        } else {
            // null response or Exception occur

            paidamountstrreplace = "Paid Amount";
            paidamountstrreplace = strpaid_amount + crunancycode;
            pemailpaidstr = paypal_emailstr;
            chektopaidmaount = paid_amount_payment_method;
            payment_bankstr = payment_bank_name;
            payment_ibanstr = payment_iban;
            payment_currencystr = payment_currency;
            payment_swiftstr = payment_swift_bic;
            hiddenpaidrow="";



        }


        //Log.e(TAG , "")

//        if (strpaid_amount.equals("0")) {
//            paimnetdetailstrtxt="";
//            bycheckstrtxt="By cheque :";
//            paypalstrtxt="Pay Pal :";
//            bankstrtxt="Bank :";
//        }else{
//            paimnetdetailstrtxt=" Payment Details ";
//            bycheckstrtxt="By cheque :";
//            paypalstrtxt="Pay Pal :";
//            bankstrtxt="Bank :";
//        }



        if (strpaid_amount.equals("0")) {
            // Do you work here on success

            strpaid_amount = "";

        } else {
            // null response or Exception occur

        }
        String taxtamountstrvaluedt = "";

        String taxtamountstrvalue = "";
        if (!taxamount.equalsIgnoreCase("0")) {
            // Do you work here on success
            taxtamountstrvaluedt = taxamount;
//            taxtamountstrvaluedt = taxamount + crunancycode;
            taxtamountstrvalue = " Tax ";

        } else {
            taxtamountstrvaluedt = "";
            taxtamountstrvalue = "";

            // null response or Exception occur

        }


        Log.e(TAG, "taxtamountstrvaluedt "+taxtamountstrvaluedt);

        String discountvalue = "";
        String discounttxtreplace = "";

        if (strdiscountvalue.equals("0")) {
            // Do you work here on success
            discountvalue = "";
            discounttxtreplace = "";

        } else {
            // null response or Exception occur
            discountvalue = strdiscountvalue + crunancycode;
            discounttxtreplace = " Discount ";
        }
        String companylogopathdto="";
        if(companylogopath.equals(""))
        {
            companylogopathdto= "/android_res/drawable/white_img.png";
        }else {
            companylogopathdto=companylogopath;
        }
        if(shippingfirstname !=null)
        {

            Shiping_tostr="Ship To:";

        }else {
            Log.e("sltcustonername",sltcustonername);
            Shiping_tostr="";

        }

        hiddenpaidrow = "hidden";

        Log.e(TAG , "invoice_no "+invoice_no);

        Log.e(TAG, "signature_of_receiver "+signature_of_receiver);
        Log.e(TAG, "company_stamp "+company_stamp);

        String content = null;
        try {
            content = IOUtils.toString(getAssets().open("receipt1.html"))

                    .replaceAll("Company Name", company_name)
                    .replaceAll("Address", company_address)
                    .replaceAll("Contact No.", company_contact)
                    .replaceAll("Website", company_website)
                    .replaceAll("Email", company_email)
                    .replaceAll("#LOGO_IMAGE#", companylogopathdto)
                    .replaceAll("invD", date)
                    .replaceAll("INV-564", "" + invoice_no)
//                    .replaceAll("Receipt-No.", "Rec#"+invoice_no)
                    .replaceAll("Receipt-Date", date)
                    .replaceAll("refNo", ref_no)
                    .replaceAll("GrossAm-", Grossamount_str)
                    .replaceAll("Discount-",discountvalue)
                    .replaceAll("SubTotal-", Subtotalamount)
//                    .replaceAll("Txses-",taxtamountstrvaluedt)
                    .replaceAll("Txses-", taxtamountstrvaluedt)
                    .replaceAll("Shipping-",Shipingcosstbyct)
                    .replaceAll(" Shipping ",shipingvaluetxt)

                    .replaceAll("Total Amount-", netamountvalue)
                    .replaceAll("IBAN", payment_iban)
//                    .replaceAll("Currency", "payment_currency")
                    .replaceAll("Currency", "")

                    .replaceAll("Swift/BICCode", payment_swift_bic)
                    .replaceAll("Client N", sltcustonername)
                    .replaceAll("Client A", sltcustomer_address)
                    .replaceAll("Client C P", sltcustomer_contact)
                    .replaceAll("Client C N", sltcustomer_phone_number)
                    .replaceAll("Client Web", sltcustomer_website)
                    .replaceAll("Client E", sltcustomer_email)
//                    .replaceAll("SignatureReciever_Image", "file://storage/emulated/0/Pictures/AndroidDvlpr/1619155120171.png")
//                    .replaceAll("signatureCompany_Stamp", company_stamp)
                    .replaceAll("#SIGNATURES#", signatureinvoice)
                    .replaceAll("#ITEMS#", productitemlist)
                    .replaceAll("#Shipp",Shipingdetail)
                    .replaceAll("Ship To:",Shiping_tostr)

                    .replaceAll("Notes-", strnotes)
                    .replaceAll("Notes:",notestringvalue)

                    .replaceAll(" Tax ", taxtamountstrvalue)
                    .replaceAll(" Discount ", discounttxtreplace)

                    .replaceAll("#ATTACHMENTS#", "")
                    .replaceAll("Attachments", "")

                    .replaceAll("#799f6e", "#ffffff")

                    .replaceAll(" Payment Details ", "")
                        .replaceAll("By cheque :", "")
                        .replaceAll("Pay Pal :", "")
                        .replaceAll("Bank :", "")

                    .replaceAll(" Payment Details ", "")
                    .replaceAll("By cheque :", "")
                    .replaceAll("Pay Pal :", "")
                    .replaceAll("Bank :", "")

                    .replaceAll("Checkto", "")
                    .replaceAll("Pemail", "")
                    .replaceAll("BankName", "")
                    .replaceAll("Checkto", "")

                    .replaceAll("hide", hiddenpaidrow)
                    .replaceAll("Company Seal",companyname)
                    .replaceAll("Signature of Receiver",signature_of_receivername);




        } catch (IOException e) {
            e.printStackTrace();
        }

        invoiceweb.loadDataWithBaseURL("file:///android_asset/receipt1.html", content, "text/html", "UTF-8", null);
    }
}