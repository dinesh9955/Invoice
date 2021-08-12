package com.sirapp.Receipts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
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

import androidx.appcompat.widget.Toolbar;

import com.sirapp.API.AllSirApi;
import com.sirapp.RetrofitApi.ApiInterface;
import com.sirapp.RetrofitApi.RetrofitInstance;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Constant.Constant;
import com.sirapp.Invoice.response.InvoiceCompanyDto;
import com.sirapp.Invoice.response.InvoiceCustomerDto;
import com.sirapp.Invoice.response.InvoiceDto;
import com.sirapp.Invoice.response.InvoiceTotalsItemDto;
import com.sirapp.Invoice.response.ProductsItemDto;
import com.sirapp.R;
import com.sirapp.Utils.Utility;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ReceiptViewActivityWebView extends BaseActivity {
    private final String TAG = "ReceiptViewActivityWebView";
    WebView invoiceweb;
    String invoiceId = "";
    String templateSelect = "";
    String colorCode = "#ffffff";

    ApiInterface apiInterface;
    String company_name = "", company_address = "", company_contact = "", company_email = "", company_website = "", payment_bank_name = "", payment_currency = "", payment_iban = "", payment_swift_bic = "";

    String credit_terms = "";
    String signature_of_issuer = "", signature_of_receiver = "", company_stamp = "";
    String due_date = "", date = "";

    StringBuilder stringBuilderBillTo = new StringBuilder();
    StringBuilder stringBuilderShipTo = new StringBuilder();


    String sltcustonername = "", sltcustomer_email = "", sltcustomer_contact = "", sltcustomer_address = "", sltcustomer_website = "", sltcustomer_phone_number = "";
    String shippingzone = "", Paymentamountdate = "", shippingfirstname = "", shippinglastname = "", shippingaddress1 = "", shippingaddress2 = "", shippingcity = "", shippingpostcode = "", shippingcountry;

    String invoicenumber = "", strnotes = "", ref_no = "", paid_amount_payment_method = "", freight_cost = "0", strdiscountvalue = "0", strpaid_amount = "", companylogopath = "", Grossamount_str = "", Subtotalamount = "", netamountvalue = "", Blanceamountstr = "";

    String Shipingdetail, shipping_firstname, shipping_lastname, shipping_address_1, shipping_address_2, shipping_city, shipping_country, shipping_zone;

    String shipping_postalcode, company_image_path, customer_image_path, invoiceshre_link, invoice_image_path;
    ArrayList<InvoiceTotalsItemDto> grosamont;

    ArrayList<ProductsItemDto> productsItemDtos;
    ArrayList<String> invoice_imageDto = new ArrayList<>();

    InvoiceTotalsItemDto listobj = new InvoiceTotalsItemDto();
    ReceiptDtoReceipt invoiceDtoInvoice;

    InvoiceDto invoiceDto;
    String currency_code,templatestr,paypal_emailstr="";
    String hiddenpaidrow="";
    String invoice_image_pathcompanystemp="",invoice_image_pathreceiverpath="",invoice_image_pathissuverpath="",Shiping_tostr="";
    String invoicetaxvalue="",Shipingcosstbyct="";

    String taxTitle = "";

    String contentAll = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice_);
        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);
        ImageView printimg = toolbar.findViewById(R.id.imageViewptint);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);

//        invoiceId = "581";
//        templatestr = "1";
//        templateSelect = "1";
//        colorCode = "#ffffff";

        invoiceId = getIntent().getStringExtra("invoiceID");
        templatestr = getIntent().getStringExtra("templatestr");
        templateSelect = getIntent().getStringExtra("templateSelect");
        //colorCode = getIntent().getStringExtra("colorCode");

        if (invoiceId != null) {
            Log.e("invoiceId", invoiceId);
        }
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


        getinvoicedata();

    }

    private void getinvoicedata() {

        String token = Constant.GetSharedPreferences(ReceiptViewActivityWebView.this, Constant.ACCESS_TOKEN);
        Call<ReceiptResponseDto> resposresult = apiInterface.getReceiptDetail(token, invoiceId, ""+getLanguage());
        resposresult.enqueue(new Callback<ReceiptResponseDto>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ReceiptResponseDto> call, retrofit2.Response<ReceiptResponseDto> response) {


                // image path of all
                company_image_path = response.body().getData().getCompany_image_path();
                customer_image_path = response.body().getData().getCustomer_image_path();
                invoiceshre_link = response.body().getData().getReceipt_share_link();
                invoice_image_path = response.body().getData().getReceipt_image_path();

              //  Log.e(TAG, "invoice_image_path"+company_image_path);

                // customer data get
                ReceiptDto data = response.body().getData();

                InvoiceCustomerDto invoiceCustomerDto = data.getReceipt().getCustomer();
                sltcustonername = invoiceCustomerDto.getCustomerName();
                sltcustomer_address = invoiceCustomerDto.getAddress();
                sltcustomer_phone_number = invoiceCustomerDto.getPhoneNumber();
                sltcustomer_website = invoiceCustomerDto.getWebsite();
                sltcustomer_email = invoiceCustomerDto.getEmail();
                sltcustomer_contact = invoiceCustomerDto.getContactName();



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


                shippingfirstname = invoiceCustomerDto.getShippingFirstname();
                shippinglastname = invoiceCustomerDto.getShippingLastname();
                shippingaddress1 = invoiceCustomerDto.getShippingAddress1();
                shippingaddress2 = invoiceCustomerDto.getShippingAddress2();
                shippingcity = invoiceCustomerDto.getShippingCity();
                shippingcountry = invoiceCustomerDto.getShippingCountry();
                shippingpostcode = invoiceCustomerDto.getShippingPostcode();
                shippingzone = (String) invoiceCustomerDto.getShippingZone();



                // Company Detail
                InvoiceCompanyDto companyDto = data.getReceipt().getCompany();
                company_name = companyDto.getName();
                company_address = companyDto.getAddress();
                company_contact = companyDto.getPhoneNumber();
                company_website = companyDto.getWebsite();
                company_email = companyDto.getEmail();

                Log.e(TAG, "websiteL "+company_website);



                if (companyDto.getLogo() != null)
                {
                    companylogopath = companyDto.getLogo();
                }


                paypal_emailstr=companyDto.getPaypalEmail();




                //invoice Data
                invoiceDtoInvoice = data.getReceipt();
                invoicenumber = invoiceDtoInvoice.getReceipt_no();
                date = invoiceDtoInvoice.getReceipt_date();
                due_date = invoiceDtoInvoice.getDueDate();
              //  credit_terms = invoiceDtoInvoice.getCreditTerms();
                ref_no = invoiceDtoInvoice.getRefNo();
                payment_swift_bic = invoiceDtoInvoice.getPaymentSwiftBic();
                payment_currency = invoiceDtoInvoice.getPaymentCurrency();
                strnotes = invoiceDtoInvoice.getNotes();



                payment_iban = invoiceDtoInvoice.getPaymentIban();
//                paid_amount_payment_method = invoiceDtoInvoice.getPaidAmountPaymentMethod();
//                paid_amount_payment_method = invoiceDtoInvoice.getPaidAmountPaymentMethod();
                currency_code = invoiceDtoInvoice.getCurrencySymbol();
                Log.e("currency_code",currency_code);
                company_stamp = invoiceDtoInvoice.getCompanyStamp();
                signature_of_receiver = invoiceDtoInvoice.getSignatureOfReceiver();
                signature_of_issuer = invoiceDtoInvoice.getSignatureOfMaker();


//                shipping_firstname = invoiceDtoInvoice.getShippingFirstname();
//                shipping_lastname = invoiceDtoInvoice.getShippingLastname();
//                shipping_address_1 = invoiceDtoInvoice.getShippingAddress1();
//                shipping_address_2 = invoiceDtoInvoice.getShippingAddress2();
//                shipping_city = invoiceDtoInvoice.getShippingCity();
//                shipping_country = invoiceDtoInvoice.getShippingCountry();
//                shipping_postalcode = (String) invoiceDtoInvoice.getShippingPostcode();
//                shipping_zone = (String) invoiceDtoInvoice.getShippingZone();




                //tax





                // calculate amount data

                grosamont = new ArrayList<InvoiceTotalsItemDto>(invoiceDtoInvoice.getTotals());

                productsItemDtos = new ArrayList<ProductsItemDto>(invoiceDtoInvoice.getProducts());
                //invoice_imageDto = new ArrayList<String>(data.getInvoiceImage());
                Log.e("product", productsItemDtos.toString());

                int numsize = grosamont.size();

                Log.e(TAG, "numsize "+numsize);


                for (int i = 0; i < numsize; i++) {
                    listobj = grosamont.get(i);
                    String title = listobj.getTitle();
                    String code = listobj.getCode();
                    if (code.equals("gross_amount")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
//                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            Grossamount_str = Utility.getPatternFormat(""+numberPostion, vc);
                        }
                        //Grossamount_str = listobj.getValue();
                    } else if (code.equals("sub_total")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
//                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            Subtotalamount = Utility.getPatternFormat(""+numberPostion, vc);
                        }
                        //Subtotalamount = listobj.getValue();
                    } else if (code.equals("total")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
//                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            netamountvalue = Utility.getPatternFormat(""+numberPostion, vc);
                        }
                        //netamountvalue = listobj.getValue();
                    } else if (code.equals("paid_amount")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
//                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            strpaid_amount = Utility.getPatternFormat(""+numberPostion, vc);
                        }
                        //strpaid_amount = listobj.getValue();
                    } else if (code.equals("remaining_balance")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
//                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            Blanceamountstr = Utility.getPatternFormat(""+numberPostion, vc);
                        }
                        Blanceamountstr = listobj.getValue();
                    }

                    else if (code.equals("tax")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
//                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            invoicetaxvalue = Utility.getPatternFormat(""+numberPostion, vc);
                            taxTitle = title;
                        }
                    }

                    else if (code.equals("discount")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
//                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            strdiscountvalue = Utility.getPatternFormat(""+numberPostion, vc);
                        }
                        //strdiscountvalue = listobj.getValue();
                    }
                    else if (code.equals("shipping")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
//                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            freight_cost = Utility.getPatternFormat(""+numberPostion, vc);
                        }
                       // freight_cost = listobj.getValue();
                    }



                }

                view_invoice();


            }

            @Override
            public void onFailure(Call<ReceiptResponseDto> call, Throwable t) {

            }
        });

    }


    private void createWebPrintJob(WebView webView) {

        //create object of print manager in your device
        PrintManager printManager = (PrintManager) primaryBaseActivity.getSystemService(Context.PRINT_SERVICE);

        //create object of print adapter
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        //provide name to your newly generated pdf file
        String jobName = getString(R.string.app_name) + " Print Test";

        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize( PrintAttributes.MediaSize.ISO_A4);
        printManager.print(jobName, printAdapter, builder.build());
    }


    @SuppressLint("LongLogTag")
    public void view_invoice() {
        invoiceweb = findViewById(R.id.invoiceweb);

        if (shippingfirstname.equalsIgnoreCase("")) {
            Log.e("sltcustonername",sltcustonername);
            Shiping_tostr="";
        }else {
            Shiping_tostr= getString(R.string.html_ShipTo);

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

           // Shipingdetail = shippingfirstname + "<br>\n" + shippinglastname + "<br>\n" + shippingaddress1 + "<br>\n" + shippingaddress2 + "<br>\n" + shippingcity + "<br>\n" + shippingcountry + "<br>\n" + shippingpostcode;
        }

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
        if(AllSirApi.FONT_INVOICE == true){
            webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE);
        }else{
            invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
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

        Uri uri = Uri.parse("android.resource://com.sirapp/drawable/white_img.png");

        String path = uri.toString();
        Log.e("path", path);


        String multipleimage = "";

        String multipagepath = null;

        String attachmentimage = "";

        if (invoice_imageDto.size() < 1) {
            attachmentimage = "";

        } else {
            attachmentimage = getString(R.string.html_Attachments);
        }


        if (invoice_imageDto != null) {
            for (int i = 0; i < invoice_imageDto.size(); i++) {

                try {

                    multipagepath = IOUtils.toString(getAssets().open("attchment.html"))


                            .replaceAll("#ATTACHMENT_1#", invoice_imageDto.get(i));


                    multipleimage = multipleimage + multipagepath;
                } catch (Exception e) {

                }

            }
        } else {
            multipleimage = "";
        }


        String productitem = null;

        String productitemlist ="";
        try {
            for (int i = 0; i < productsItemDtos.size(); i++) {
              //  DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

                double productQuantity = Double.parseDouble(productsItemDtos.get(i).getQuantity());
                double producpriceRate = Double.parseDouble(productsItemDtos.get(i).getPrice());
//                double producpriceAmount = Double.parseDouble(productsItemDtos.get(i).getTotal());

                double totalAmount = producpriceRate * productQuantity;

                String stringFormatQuantity = Utility.getPatternFormat(""+numberPostion, productQuantity);
                String stringFormatRate = Utility.getPatternFormat(""+numberPostion, producpriceRate);
                String stringFormatAmount = Utility.getPatternFormat(""+numberPostion, totalAmount);

                productitem = IOUtils.toString(getAssets().open("single_item.html"))
                        .replaceAll("#NAME#", productsItemDtos.get(i).getName())
                        .replaceAll("#DESC#", productsItemDtos.get(i).getDescription())
                        .replaceAll("#UNIT#", productsItemDtos.get(i).getMeasurementUnit())
                        .replaceAll("#QUANTITY#", ""+stringFormatQuantity)
                        .replaceAll("#PRICE#", ""+stringFormatRate +"" + Utility.getReplaceDollor(currency_code))
                        .replaceAll("#TOTAL#", ""+stringFormatAmount +"" + Utility.getReplaceDollor(currency_code));

                productitemlist = productitemlist + productitem;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        String drableimagebase64 = "iVBORw0KGgoAAAANSUhEUgAAAC4AAAAnCAYAAABwtnr/AAAAAXNSR0IArs4c6QAAADhlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAAqACAAQAAAABAAAALqADAAQAAAABAAAAJwAAAAB8SmRPAAAAeklEQVRYCe3SQQrAIBTEUPX+d67iCbIIBSGuw/B57fzOGw++9eDN9+QO//vLJZ44FOhXgVBalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYc2r3IESll5TkQAAAAASUVORK5CYII=";


        // Edit Note Data

        String notestringvalue="";
        if(strnotes.equals(""))
        {
            notestringvalue="";
        }else
        {
            notestringvalue= getString(R.string.html_Notes);
        }


        String signatureinvoice = null;

        String companyname="";
        if(company_stamp.equals("") || company_stamp.endsWith("white_img.png"))
        {
            invoice_image_pathcompanystemp="/android_res/drawable/white_img.png";
            companyname="";
        }else {
            invoice_image_pathcompanystemp=invoice_image_path + company_stamp;
            companyname= getString(R.string.html_CompanySeal);
        }

        String signature_of_receivername="";
        if(signature_of_receiver.equals("") || signature_of_receiver.endsWith("white_img.png"))
        {
            invoice_image_pathreceiverpath="/android_res/drawable/white_img.png";
            signature_of_receivername="";
        }else {
            invoice_image_pathreceiverpath=invoice_image_path + signature_of_receiver;
            signature_of_receivername= getString(R.string.html_SignatureofReceiver);
        }


        String signature_of_issuername="";
        if(signature_of_issuer.equals("") || signature_of_issuer.endsWith("white_img.png"))
        {
            invoice_image_pathissuverpath="/android_res/drawable/white_img.png";
            signature_of_issuername="";
        }else {
            invoice_image_pathissuverpath=invoice_image_path + signature_of_issuer;
            signature_of_issuername= getString(R.string.html_SignatureofIssuer);
        }


        try {
            signatureinvoice = IOUtils.toString(getAssets().open("Signatures.html"))
                    .replaceAll("CompanyStamp", companyname)
                    .replaceAll("SignatureofReceiver", signature_of_receivername)
                    .replaceAll("SignatureofIssuer", signature_of_issuername)
                    .replaceAll("dataimageCompany_Stamp",invoice_image_pathcompanystemp)
                    .replaceAll("dataimageRecieverImage", invoice_image_pathreceiverpath)
                    .replaceAll("data:imageSige_path", invoice_image_pathissuverpath);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String content = null;




        String shipingvaluetxt="";
        if(freight_cost.equals("0") ){
            // Do you work here on success
            Shipingcosstbyct="";
            shipingvaluetxt="";


        }else{
            // null response or Exception occur

            Shipingcosstbyct=""+freight_cost+currency_code;
            shipingvaluetxt= getString(R.string.html_Shipping);
        }

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

        Log.e(TAG, "strpaid_amount:: "+strpaid_amount);

        if (strpaid_amount.equals("0") || strpaid_amount.equals("0.00") || strpaid_amount.equals(".00Rs") || strpaid_amount.equals(".00")) {
            Log.e(TAG, "strpaid_amount1:: "+strpaid_amount);
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
            Log.e(TAG, "strpaid_amount2:: "+strpaid_amount);

            // null response or Exception occur
            paidamountstrrepvalue =strpaid_amount+currency_code;
            paidamountstrreptxt = getString(R.string.html_PaidAmount);


            pemailpaidstr = paypal_emailstr;
            chektopaidmaount = strpaid_amount;
            payment_bankstr = payment_bank_name;
            payment_ibanstr = payment_iban;
            payment_currencystr = payment_currency;
            payment_swiftstr = payment_swift_bic;

            paimnetdetailstrtxt= getString(R.string.html_PaymentDetails);
            bycheckstrtxt="By cheque :";
            paypalstrtxt="Pay Pal :";
            bankstrtxt="Bank :";


            hiddenpaidrow="";
        }
        String strreferencenovalue="";
        String strreferencenotxtvalue="";

        if (ref_no.isEmpty()) {
            // Do you work here on success

            strreferencenovalue="";
            strreferencenotxtvalue="";

        } else {
            // null response or Exception occur

            strreferencenovalue=ref_no;
            strreferencenotxtvalue= getString(R.string.html_ReferenceNo);


        }



        if (strpaid_amount.equals("0")) {
            // Do you work here on success

            strpaid_amount = "";

        } else {
            // null response or Exception occur

        }
        String taxtamountstr = "";
        String companywebsitestr = "";
        String taxtamountstrvalue = "";
        if (invoicetaxvalue.equals("")) {
            // Do you work here on success
            taxtamountstr = "";
            taxtamountstrvalue = "";

        } else {
            // null response or Exception occur
            taxtamountstr = invoicetaxvalue + currency_code;
            taxtamountstrvalue = ""+taxTitle.replace("(", "").replace(")", "");
        }

        String discountvalue = "";
        String discounttxtreplace = "";

        if (strdiscountvalue.equals("0")) {
            // Do you work here on success
            discountvalue = "";
            discounttxtreplace = "";

        } else {
            // null response or Exception occur
            discountvalue = strdiscountvalue + currency_code;
            discounttxtreplace = getString(R.string.html_Discount);
        }


        String subTotalTxt = "";
        String subTotalValueTxt = "";


        if(strdiscountvalue.equalsIgnoreCase("0")){
            subTotalTxt = "";
            subTotalValueTxt = "";
        }else{
            subTotalTxt = getString(R.string.html_SubTotal);
            subTotalValueTxt = Subtotalamount + ""+ Utility.getReplaceDollor(currency_code);
        }


        String companylogopathdto="";

        if (companylogopath.toLowerCase().endsWith(".jpg") || companylogopath.toLowerCase().endsWith(".jpeg") || companylogopath.toLowerCase().endsWith(".png")){
            companylogopathdto= company_image_path + companylogopath;
        }else{
            companylogopathdto = "/android_res/drawable/white_img.png";
        }


        //Log.e(TAG , "templateSelect"+templateSelect);
        String name = "receipt.html";
        String nameName = "file:///android_asset/receipt.html";
        if(templatestr.equals("1")) {

                name = "receipt.html";
                nameName = "file:///android_asset/receipt.html";



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



                        .replaceAll("Company Name", company_name)
                        .replaceAll("Address", stringBuilderCompany.toString())
//                        .replaceAll("Contact No.", company_contact)
//                        .replaceAll("Website", company_website)
//                        .replaceAll("Email", company_email)
                        .replaceAll("INV-564", "" + invoicenumber)
                        .replaceAll("invD", date)
                     //   .replaceAll("DueDate", due_date)
//                        .replaceAll("crTerms", invoiceDtoInvoice.getCreditTerms())
                        .replaceAll("refNo", strreferencenovalue)
                        .replaceAll("GrossAm-", Grossamount_str + ""+ Utility.getReplaceDollor(currency_code))
                        .replaceAll("Discount-", ""+ Utility.getReplaceDollor(discountvalue))
                        .replaceAll("SubTotal-", subTotalValueTxt)
                        .replaceAll("Txses-", Utility.getReplaceDollor(taxtamountstr))
                        .replaceAll("Shipping-", Utility.getReplaceDollor(Shipingcosstbyct))
                        .replaceAll("Total Amount-", netamountvalue + Utility.getReplaceDollor(currency_code))
                        .replaceAll("PaidsAmount", Utility.getReplaceDollor(paidamountstrrepvalue))
                        .replaceAll("Paid Amount", paidamountstrreptxt)
                        .replaceAll("Balance Due-", Blanceamountstr + Utility.getReplaceDollor(currency_code))

                        .replaceAll("SubTotal", subTotalTxt)
//                        .replaceAll("Checkto", chektopaidmaount)
                        .replaceAll("Checkto", "")

                        .replaceAll("BankName", payment_bankstr)
                        .replaceAll("Pemail", pemailpaidstr)
                        .replaceAll("IBAN", payment_ibanstr)
//                        .replaceAll("Currency", payment_currencystr)
                        .replaceAll("Currency", "")
                        .replaceAll("Swift/BICCode", payment_swiftstr)
                        .replaceAll("Client N", ""+stringBuilderBillTo.toString())
//                        .replaceAll("Client A", sltcustomer_address)
//                        .replaceAll("Client C P", sltcustomer_contact)
//                        .replaceAll("Client C N", sltcustomer_phone_number)
//                        .replaceAll("Client Web", sltcustomer_website)
//                        .replaceAll("Client E", sltcustomer_email)
                        .replaceAll("Notes-", strnotes)
                        .replaceAll("#SIGNATURES#", signatureinvoice)
                        .replaceAll("#ITEMS#", productitemlist)
                        .replaceAll("#LOGO_IMAGE#",companylogopathdto)
                        .replaceAll("#Shipp", ""+stringBuilderShipTo.toString())
                        .replaceAll("#ATTACHMENTS#", multipleimage)
                        .replaceAll("Attachments", attachmentimage)
                        .replaceAll("Notes:", notestringvalue)
                        .replaceAll("Ship To:",Shiping_tostr)
                        .replaceAll(" Shipping ",shipingvaluetxt)
                        .replaceAll(" Tax ", taxtamountstrvalue)
                        .replaceAll(" Discount ", discounttxtreplace)
                        .replaceAll("Reference No:", strreferencenotxtvalue)
                        .replaceAll("hide", hiddenpaidrow)
//                        .replaceAll(" Payment Details ", paimnetdetailstrtxt)
//                        .replaceAll("By cheque :", bycheckstrtxt)
//                        .replaceAll("Pay Pal :", paypalstrtxt)
//                        .replaceAll("Bank :", bankstrtxt)
                        .replaceAll(" Payment Details ", "")
                        .replaceAll("By cheque :", "")
                        .replaceAll("Pay Pal :", "")
                        .replaceAll("Bank :", "")
                        .replaceAll("#799f6e", colorCode)
                        .replaceAll("#TEMP3#", String.valueOf(R.color.blue));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (templatestr.equals("2"))
        {

        }

        contentAll = content;

        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);
    }


}