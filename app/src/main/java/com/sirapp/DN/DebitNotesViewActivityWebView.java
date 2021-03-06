package com.sirapp.DN;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
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
import com.sirapp.Constant.Constant;
import com.sirapp.Invoice.Invoice_image;
import com.sirapp.Invoice.response.InvoiceCompanyDto;
import com.sirapp.Invoice.response.InvoiceDto;
import com.sirapp.Invoice.response.InvoiceTotalsItemDto;
import com.sirapp.Invoice.response.ProductsItemDto;
import com.sirapp.RetrofitApi.ApiInterface;
import com.sirapp.RetrofitApi.RetrofitInstance;
import com.sirapp.Utils.Utility;
import com.sirapp.R;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class DebitNotesViewActivityWebView extends BaseActivity {
    private final String TAG = "DebitNotesViewActivityWebView";

    String attachmentHtml = "attchment.html";
    String singleItemHtml = "single_item.html";
    String signatureHtml = "Signatures.html";
    String mainHtml = "debit.html";

    WebView invoiceweb;
    String invoiceId = "";
    String templateSelect = "";
    String colorCode = "#ffffff";

    int BUTTON_WIDTH = 0;
    int BUTTON_WIDTH_2 = 0;

    ApiInterface apiInterface;
    String company_name = "", company_address = "", company_contact = "", company_email = "", company_website = "", payment_bank_name = "", payment_currency = "", payment_iban = "", payment_swift_bic = "";

    String credit_terms = "";
    String signature_of_issuer = "", signature_of_receiver = "", company_stamp = "";
    String due_date = "", date = "";

    StringBuilder stringBuilderBillTo = new StringBuilder();
    StringBuilder stringBuilderShipTo = new StringBuilder();


    String sltcustonername = "", sltcustomer_email = "", sltcustomer_contact = "", sltcustomer_address = "", sltcustomer_website = "", sltcustomer_phone_number = "";
    String shippingzone = "", Paymentamountdate = "", shippingfirstname = "", shippinglastname = "", shippingaddress1 = "", shippingaddress2 = "", shippingcity = "", shippingpostcode = "", shippingcountry;

    String invoicenumber = "", strnotes = "", ref_no = "", paid_amount_payment_method = "", freight_cost = "0", strdiscountvalue = "0", strpaid_amount = "",
            companylogopath = "", Grossamount_str = "", Subtotalamount = "", netamountvalue = "", Blanceamountstr = "";

    String Shipingdetail = "", shipping_firstname, shipping_lastname, shipping_address_1, shipping_address_2, shipping_city, shipping_country, shipping_zone;

    String shipping_postalcode, company_image_path, customer_image_path, invoiceshre_link, invoice_image_path;
    ArrayList<InvoiceTotalsItemDto> grosamont = new ArrayList<>();

    ArrayList<ProductsItemDto> productsItemDtos = new ArrayList<>();
    ArrayList<Invoice_image> invoice_imageDto = new ArrayList<>();

    InvoiceTotalsItemDto listobj = new InvoiceTotalsItemDto();
    DebitNoteDtoDebitNote invoiceDtoInvoice;

    InvoiceDto invoiceDto;
    String currency_code,templatestr,paypal_emailstr="";
    String hiddenpaidrow="";
    String invoice_image_pathcompanystemp="",invoice_image_pathreceiverpath="",invoice_image_pathissuverpath="",Shiping_tostr="";
    String invoicetaxvalue="",Shipingcosstbyct="";

    String contentAll = "";

    String taxTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice_);
        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);
        ImageView printimg = toolbar.findViewById(R.id.imageViewptint);
        invoiceweb = findViewById(R.id.invoiceweb);


        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        invoiceId = getIntent().getStringExtra("invoiceID");
        templatestr = getIntent().getStringExtra("templatestr");
        templateSelect = getIntent().getStringExtra("templateSelect");
        //   colorCode = getIntent().getStringExtra("colorCode");

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



        if(AllSirApi.ALL_FONT == true){
            checkDevice();
        }

        getinvoicedata();

    }


    private void checkDevice() {
        if(Utility.isTablet(DebitNotesViewActivityWebView.this) == true){
            attachmentHtml = "attchment.html";
            singleItemHtml = "single_item.html";
            signatureHtml = "Signatures.html";
            mainHtml = "debit.html";
        }else {
            String manufacturerModel = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + " " + Build.BRAND + " " + Build.DEVICE;
            if (manufacturerModel.toLowerCase().contains("j7")) {
                attachmentHtml = "5/attchment.html";
                singleItemHtml = "5/single_item.html";
                signatureHtml = "5/Signatures.html";
                mainHtml = "5/debit.html";
            } else {
                attachmentHtml = "6/attchment.html";
                singleItemHtml = "6/single_item.html";
                signatureHtml = "6/Signatures.html";
                mainHtml = "6/debit.html";
            }
        }
    }


    private void getinvoicedata() {

        String token = Constant.GetSharedPreferences(DebitNotesViewActivityWebView.this, Constant.ACCESS_TOKEN);
        Call<DebitNoteResponseDto> resposresult = apiInterface.getDebitNoteDetail(token, invoiceId, ""+getLanguage());
        resposresult.enqueue(new Callback<DebitNoteResponseDto>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<DebitNoteResponseDto> call, retrofit2.Response<DebitNoteResponseDto> response) {

                Log.e("resss ", ""+response.body().toString());
                // image path of all
                company_image_path = response.body().getData().getCompanyImagePath();
                customer_image_path = response.body().getData().getSupplier_image_path();
                invoiceshre_link = response.body().getData().getDebit_note_share_link();
                invoice_image_path = response.body().getData().getDebit_note_image_path();
                // customer data get
                DebitNoteDto data = response.body().getData();

                DebitNoteSupplierDto invoiceCustomerDto = data.getDebit_note().getSupplier();

                if(invoiceCustomerDto != null){
                    sltcustonername = invoiceCustomerDto.getSupplier_name();
                    sltcustomer_address = invoiceCustomerDto.getAddress();
                    sltcustomer_phone_number = invoiceCustomerDto.getPhone_number();
                    sltcustomer_website = invoiceCustomerDto.getWebsite();
                    sltcustomer_email = invoiceCustomerDto.getEmail();
                    sltcustomer_contact = invoiceCustomerDto.getContact_name();

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




//                shippingfirstname = invoiceCustomerDto.getShippingFirstname();
//                shippinglastname = invoiceCustomerDto.getShippingLastname();
//                shippingaddress1 = invoiceCustomerDto.getShippingAddress1();
//                shippingaddress2 = invoiceCustomerDto.getShippingAddress2();
//                shippingcity = invoiceCustomerDto.getShippingCity();
//                shippingcountry = invoiceCustomerDto.getShippingCountry();
//                shippingpostcode = invoiceCustomerDto.getShippingPostcode();
//                shippingzone = (String) invoiceCustomerDto.getShippingZone();

                // Company Detail
                InvoiceCompanyDto companyDto = data.getDebit_note().getCompany();
                company_name = companyDto.getName();
                company_address = companyDto.getAddress();
                company_contact = companyDto.getPhoneNumber();
                company_website = companyDto.getWebsite();
                company_email = companyDto.getEmail();

                if (companyDto.getLogo() != null)
                {
                    companylogopath = companyDto.getLogo();
                }

                paypal_emailstr=companyDto.getPaypalEmail();

                //invoice Data
                invoiceDtoInvoice = data.getDebit_note();
                invoicenumber = invoiceDtoInvoice.getDebit_note_no();
                date = invoiceDtoInvoice.getDebit_note_date();
                due_date = invoiceDtoInvoice.getDueDate();
                credit_terms = invoiceDtoInvoice.getCreditTerms();
                ref_no = invoiceDtoInvoice.getRefNo();
                payment_swift_bic = invoiceDtoInvoice.getPaymentSwiftBic();
                payment_currency = invoiceDtoInvoice.getPaymentCurrency();

                payment_iban = invoiceDtoInvoice.getPaymentIban();
                paid_amount_payment_method = invoiceDtoInvoice.getPaidAmountPaymentMethod();
                paid_amount_payment_method = invoiceDtoInvoice.getPaidAmountPaymentMethod();
                currency_code = invoiceDtoInvoice.getCurrencySymbol();
                Log.e("currency_code",currency_code);
                company_stamp = invoiceDtoInvoice.getCompanyStamp();
                signature_of_receiver = invoiceDtoInvoice.getSignatureOfReceiver();
                signature_of_issuer = invoiceDtoInvoice.getSignatureOfMaker();

                Log.e(TAG, "company_stamp"+company_stamp);
                Log.e(TAG, "signature_of_receiver"+signature_of_receiver);
                Log.e(TAG, "signature_of_issuer"+signature_of_issuer);

//                shipping_firstname = invoiceDtoInvoice.getShippingFirstname();
//                shipping_lastname = invoiceDtoInvoice.getShippingLastname();
//                shipping_address_1 = invoiceDtoInvoice.getShippingAddress1();
//                shipping_address_2 = invoiceDtoInvoice.getShippingAddress2();
//                shipping_city = invoiceDtoInvoice.getShippingCity();
//                shipping_country = invoiceDtoInvoice.getShippingCountry();
//                shipping_postalcode = (String) invoiceDtoInvoice.getShippingPostcode();
//                shipping_zone = (String) invoiceDtoInvoice.getShippingZone();
//                Log.e("shipping_firstname",shipping_firstname);

                strnotes = invoiceDtoInvoice.getNotes();


                //tax



                // calculate amount data


                if(invoiceDtoInvoice.getTotals() != null){
                    grosamont = new ArrayList<InvoiceTotalsItemDto>(invoiceDtoInvoice.getTotals());
                }

                productsItemDtos = new ArrayList<ProductsItemDto>(invoiceDtoInvoice.getProducts());
//                invoice_imageDto = new ArrayList<Invoice_image>(data.getInvoiceImage());

                Log.e(TAG, "invoice_imageDto"+invoice_imageDto.size());

                Log.e("product", productsItemDtos.toString());

                int numsize = grosamont.size();
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
                        // Subtotalamount = listobj.getValue();
                    } else if (code.equals("total")) {
//                        netamountvalue = listobj.getValue();
//                        Blanceamountstr = listobj.getValue();

                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
//                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            netamountvalue = Utility.getPatternFormat(""+numberPostion, vc);
                        }

                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
//                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            Blanceamountstr = Utility.getPatternFormat(""+numberPostion, vc);
                        }
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
                        //Blanceamountstr = listobj.getValue();
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
                        // strdiscountvalue = listobj.getValue();
                    }
                    else if (code.equals("shipping")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
//                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            freight_cost = Utility.getPatternFormat(""+numberPostion, vc);
                        }
                        //freight_cost = listobj.getValue();
                    }

//                    else if (!title.equals("Remaining Balance")) {
//                        if (title.equals("Grand Total")) {
//                            Blanceamountstr = listobj.getValue();
//                        }
//                    }


                }

                view_invoice();


            }

            @Override
            public void onFailure(Call<DebitNoteResponseDto> call, Throwable t) {

            }
        });

    }


    private void createWebPrintJob(WebView webView) {

        //create object of print manager in your device
        PrintManager printManager = (PrintManager) primaryBaseActivity.getSystemService(Context.PRINT_SERVICE);
      //  webView.getSettings().setMinimumFontSize(webView.getSettings().getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_PRINT);

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

        if(shippingfirstname.equals(""))
        {
            Log.e("sltcustonername",sltcustonername);
            Shiping_tostr="";
        }else {
            Shiping_tostr = getString(R.string.html_ShipTo);

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

                Log.e(TAG, "invoice_imageDtoAA "+invoice_imageDto.get(i).getImage());
                // Log.e(TAG, "invoice_imageDtoBB "+invoice_imageDto.get(i).getImage());

                try {

                    multipagepath = IOUtils.toString(getAssets().open(attachmentHtml))


                            .replaceAll("#ATTACHMENT_1#", invoice_image_path+invoice_imageDto.get(i).getImage());


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
               // double producpriceAmount = Double.parseDouble(productsItemDtos.get(i).getTotal());

                double totalAmount = producpriceRate * productQuantity;

                String stringFormatQuantity = Utility.getPatternFormat(""+numberPostion, productQuantity);
                String stringFormatRate = Utility.getPatternFormat(""+numberPostion, producpriceRate);
                String stringFormatAmount = Utility.getPatternFormat(""+numberPostion, totalAmount);

                productitem = IOUtils.toString(getAssets().open(singleItemHtml))

                        .replaceAll("#NAME#", productsItemDtos.get(i).getName())
                        .replaceAll("#DESC#", productsItemDtos.get(i).getDescription() == null ? "" : productsItemDtos.get(i).getDescription())
                        .replaceAll("#UNIT#", productsItemDtos.get(i).getMeasurementUnit() == null ? "" : productsItemDtos.get(i).getMeasurementUnit())
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
            notestringvalue = getString(R.string.html_Notes);
        }



        String signatureinvoice = null;

        String companyname="";
        if(company_stamp.equals("") || company_stamp.endsWith("white_img.png"))
        {
            invoice_image_pathcompanystemp="/android_res/drawable/white_img.png";
            companyname="";
        }else {
            invoice_image_pathcompanystemp=invoice_image_path + company_stamp;
            companyname = getString(R.string.html_CompanySeal);
        }

        String signature_of_receivername="";
        if(signature_of_receiver.equals("") || signature_of_receiver.endsWith("white_img.png"))
        {
            invoice_image_pathreceiverpath="/android_res/drawable/white_img.png";
            signature_of_receivername="";
        }else {
            invoice_image_pathreceiverpath=invoice_image_path + signature_of_receiver;
            signature_of_receivername = getString(R.string.html_SignatureofReceiver);
        }


        String signature_of_issuername="";
        if(signature_of_issuer.equals("") || signature_of_issuer.endsWith("white_img.png"))
        {
            invoice_image_pathissuverpath="/android_res/drawable/white_img.png";
            signature_of_issuername="";
        }else {
            invoice_image_pathissuverpath=invoice_image_path + signature_of_issuer;
            signature_of_issuername = getString(R.string.html_SignatureofIssuer);
        }

        try {
            signatureinvoice = IOUtils.toString(getAssets().open(signatureHtml))
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
            shipingvaluetxt = getString(R.string.html_Shipping);
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

        if (strpaid_amount.equals("") || strpaid_amount.equals("0") || strpaid_amount.equals("0.00") || strpaid_amount.equals(".00Rs") || strpaid_amount.equals(".00")) {
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

            paimnetdetailstrtxt=" Payment Details ";
            bycheckstrtxt="By cheque :";
            paypalstrtxt="PayPal :";
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
            strreferencenotxtvalue = getString(R.string.html_ReferenceNo);


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



        String name = mainHtml;
        String nameName = "file:///android_asset/"+mainHtml;


        if(templatestr.equals("1")) {


            name = mainHtml;
            nameName = "file:///android_asset/"+mainHtml;


            StringBuilder stringBuilderCompany = new StringBuilder();

            if (!company_address.equalsIgnoreCase("")) {
                stringBuilderCompany.append(company_address + "</br>");
            }
            if (!company_contact.equalsIgnoreCase("")) {
                stringBuilderCompany.append(company_contact + "</br>");
            }
            if (!company_website.equalsIgnoreCase("")) {
                stringBuilderCompany.append(company_website + "</br>");
            }
            if (!company_email.equalsIgnoreCase("")) {
                stringBuilderCompany.append(company_email + "");
            }


            String htmlview_debit_note = getString(R.string.htmlview_debit_note);
            String htmlview_Recipient = getString(R.string.htmlview_Recipient);
            String htmlview_DebitNoteNo = getString(R.string.htmlview_DebitNoteNo);
            String htmlview_DebitNoteDate = getString(R.string.htmlview_DebitNoteDate);
            //  String htmlview_ReferenceNo = getString(R.string.htmlview_ReferenceNo);
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


//        String name = "debit.html";
//        String nameName = "file:///android_asset/debit.html";

//            try {
//                content = IOUtils.toString(getAssets().open(name));
//                invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);
//
//            } catch (Exception e) {
//
//            }
//
//        }


            try {
                 content = IOUtils.toString(getAssets().open(name))

                        .replaceAll("Title_", htmlview_debit_note)
                        .replaceAll("Recipient", htmlview_Recipient)
                        .replaceAll("Debit Note No", htmlview_DebitNoteNo)
                        .replaceAll("Debit Note Date", htmlview_DebitNoteDate)
                       // .replaceAll("Reference No", htmlview_ReferenceNo)
                        .replaceAll("SUMMARY", htmlview_SUMMARY)
                        .replaceAll("Product/Item", htmlview_ProductItem)
                        .replaceAll("Unit of Measurement", htmlview_UnitofMeasurement)
                        .replaceAll("Quantity", htmlview_Quantity)
                        .replaceAll("Rate", htmlview_Rate)
                        .replaceAll("Amount_", htmlview_Amount)
                        .replaceAll("Gross Amount", htmlview_GrossAmount)
////                        .replaceAll("Discount", htmlview_Discount)
////                        .replaceAll("SubTotal", htmlview_SubTotal)
////                        .replaceAll("Tax", htmlview_Tax)
////                        .replaceAll("Shipping", htmlview_Shipping)
                        .replaceAll("Net Amount", htmlview_NetAmount)
////                        .replaceAll("Paid Amount", htmlview_PaidAmount)
////                        .replaceAll("Notes:", htmlview_Notes)
////                        .replaceAll("Attachments", htmlview_Attachments)




                        .replaceAll("Company Name", company_name)
                        .replaceAll("Address", stringBuilderCompany.toString())
//                        .replaceAll("Contact No.", company_contact)
//                        .replaceAll("Website", company_website)
//                        .replaceAll("Email", company_email)
                        .replaceAll("INV-564", "" + invoicenumber)
                        .replaceAll("invD", date)
                        .replaceAll("DueDate", due_date)
                        .replaceAll("crTerms", invoiceDtoInvoice.getCreditTerms())
                        .replaceAll("refNo", strreferencenovalue)
                        .replaceAll("GrossAm-", Grossamount_str + ""+ Utility.getReplaceDollor(currency_code))
                        .replaceAll("Discount-", ""+ Utility.getReplaceDollor(discountvalue))
                        .replaceAll("SubTotal-", subTotalValueTxt)
                        .replaceAll("Txses-", Utility.getReplaceDollor(taxtamountstr))
                        .replaceAll("Shipping-", Utility.getReplaceDollor(Shipingcosstbyct))
                        .replaceAll("Total Amount-", netamountvalue + Utility.getReplaceDollor(currency_code))
                        .replaceAll("PaidsAmount", Utility.getReplaceDollor(paidamountstrrepvalue))
                        .replaceAll("Paid Amount", paidamountstrreptxt)
//                        .replaceAll("Balance Due-", Blanceamountstr + currency_code)
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
//                        .replaceAll("PayPal :", paypalstrtxt)
//                        .replaceAll("Bank :", bankstrtxt)

//                        .replaceAll("14px", BUTTON_WIDTH+"px")
//                        .replaceAll("10.0pt", BUTTON_WIDTH+"px")
//                        .replaceAll("7.0pt", BUTTON_WIDTH_2+"px")
                        .replaceAll(" Payment Details ", "")
                        .replaceAll("By cheque :", "")
                        .replaceAll("PayPal :", "")
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


    @SuppressLint("LongLogTag")
    @Override
    protected void onResume() {
        super.onResume();
      //  callForWeb();
    }

    @SuppressLint("LongLogTag")
    private void callForWeb() {
        if(invoiceweb != null) {
            invoiceweb = findViewById(R.id.invoiceweb);
            WebSettings webSettings = invoiceweb.getSettings();

            BUTTON_WIDTH = (int) getResources().getDimension(R.dimen._2sdp);
            BUTTON_WIDTH_2 = (int) getResources().getDimension(R.dimen._1sdp);
            if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("ldpi")) {
                BUTTON_WIDTH = (int) getResources().getDimension(R.dimen._3sdp);
                BUTTON_WIDTH_2 = (int) getResources().getDimension(R.dimen._2sdp);
            } else if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("mdpi")) {
                BUTTON_WIDTH = (int) getResources().getDimension(R.dimen._4sdp);
                BUTTON_WIDTH_2 = (int) getResources().getDimension(R.dimen._3sdp);
            } else if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("hdpi")) {
                BUTTON_WIDTH = (int) getResources().getDimension(R.dimen._5sdp);
                BUTTON_WIDTH_2 = (int) getResources().getDimension(R.dimen._4sdp);
            } else if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("xhdpi")) {
                BUTTON_WIDTH = (int) getResources().getDimension(R.dimen._5sdp);
                BUTTON_WIDTH_2 = (int) getResources().getDimension(R.dimen._4sdp);
            } else if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("xxhdpi")) {
                BUTTON_WIDTH = (int) getResources().getDimension(R.dimen._4sdp);
                BUTTON_WIDTH_2 = (int) getResources().getDimension(R.dimen._3sdp);
            } else if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("xxxhdpi")) {
                BUTTON_WIDTH = (int) getResources().getDimension(R.dimen._3sdp);
                BUTTON_WIDTH_2 = (int) getResources().getDimension(R.dimen._2sdp);
            } else {
                BUTTON_WIDTH = (int) getResources().getDimension(R.dimen._5sdp);
                BUTTON_WIDTH_2 = (int) getResources().getDimension(R.dimen._4sdp);
            }
            Log.e(TAG, "BUTTON_WIDTH " + BUTTON_WIDTH);
           // webSettings.setTextZoom(BUTTON_WIDTH);

        }

//            Log.e(TAG, "isTablet "+Utility.isTablet(DebitNotesViewActivityWebView.this));
//            if(Utility.isTablet(DebitNotesViewActivityWebView.this) == true){ // tab
//                DisplayMetrics metrics = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                int widthPixels = metrics.widthPixels;
//                int heightPixels = metrics.heightPixels;
//                float scaleFactor = metrics.density;
//                float widthDp = widthPixels / scaleFactor;
//                float heightDp = heightPixels / scaleFactor;
//                float smallestWidth = Math.min(widthDp, heightDp);
//
//                if (smallestWidth > 720) {
//                    //Device is a 10" tablet
//                    webSettings.setMinimumLogicalFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_TAB_10_V);
//                }
//                else if (smallestWidth > 600) {
//                    //Device is a 7" tablet
//                    webSettings.setMinimumLogicalFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_TAB_7_V);
//                }else{
//                    invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
//                }
//            }else{
//                DisplayMetrics dm = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(dm);
//                int width1=dm.widthPixels;
//                int height1=dm.heightPixels;
//                double wi=(double)width1/(double)dm.xdpi;
//                double hi=(double)height1/(double)dm.ydpi;
//                double x = Math.pow(wi,2);
//                double y = Math.pow(hi,2);
//                double screenInches = Math.sqrt(x+y);
//                if(screenInches > 4.9 && screenInches < 5.4){
//                    Log.e(TAG, "screenInches1 "+screenInches);
//                    invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
//                }else{
//                    if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("ldpi")){
//                        webSettings.setMinimumLogicalFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_L_V);
//                    }else if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("mdpi")){
//                        webSettings.setMinimumLogicalFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_M_V);
//                    }else if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("hdpi")){
//                        webSettings.setMinimumLogicalFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_H_V);
//                    }else if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("xhdpi")){
//                        webSettings.setMinimumLogicalFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_X_V);
//                    }else if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("xxhdpi")){
//                        webSettings.setMinimumLogicalFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_XX_V);
//                    }else if (Utility.getDensityName(DebitNotesViewActivityWebView.this).equalsIgnoreCase("xxxhdpi")){
//                        webSettings.setMinimumLogicalFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_XXX_V);
//                    }else{
//                        invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
//                    }
//                }
//
//            }
//
//        }
    }


}