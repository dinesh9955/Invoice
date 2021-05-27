package com.receipt.invoice.stock.sirproject.InvoiceReminder;

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

import com.google.gson.Gson;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Invoice.Invoice_image;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceCompanyDto;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceCustomerDto;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceDto;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceDtoInvoice;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceResponseDto;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceTotalsItemDto;
import com.receipt.invoice.stock.sirproject.Invoice.response.ProductsItemDto;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.RetrofitApi.ApiInterface;
import com.receipt.invoice.stock.sirproject.RetrofitApi.RetrofitInstance;
import com.receipt.invoice.stock.sirproject.Utils.Utility;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ViewInvoiceReminderActivity extends AppCompatActivity {
    private final String TAG = "ViewThankYouNoteActivity";
    WebView invoiceweb;
    String invoiceId = "";
    String templateSelect = "";
    String colorCode = "#ffffff";

    ApiInterface apiInterface;
    String company_name = "", company_address = "", company_contact = "", company_email = "", company_website = "", payment_bank_name = "", payment_currency = "", payment_iban = "", payment_swift_bic = "", cheque_payable_to ="";

    String credit_terms = "";
    String signature_of_issuer = "", signature_of_receiver = "", company_stamp = "";
    String due_date = "", date = "";

    StringBuilder stringBuilderBillTo = new StringBuilder();
    StringBuilder stringBuilderShipTo = new StringBuilder();

    String sltcustonername = "", sltcustomer_email = "", sltcustomer_contact = "", sltcustomer_address = "", sltcustomer_website = "", sltcustomer_phone_number = "";
    String shippingzone = "", Paymentamountdate = "", shippingfirstname = "", shippinglastname = "", shippingaddress1 = "", shippingaddress2 = "", shippingcity = "", shippingpostcode = "", shippingcountry;

    String invoicenumber = "", strnotes = "", ref_no = "", paid_amount_payment_method = "", freight_cost = "0", strdiscountvalue = "0", strpaid_amount = "", companylogopath = "", Grossamount_str = "", Subtotalamount = "", netamountvalue = "", Blanceamountstr = "";

    //    String paid_amount_date = "";
//    String Grossamount_str_real = "0";
    String Shipingdetail = "", shipping_firstname, shipping_lastname, shipping_address_1, shipping_address_2, shipping_city, shipping_country, shipping_zone;

    String shipping_postalcode, company_image_path, customer_image_path, invoiceshre_link, invoice_image_path;
    ArrayList<InvoiceTotalsItemDto> grosamont = new ArrayList<>();

    ArrayList<ProductsItemDto> productsItemDtos = new ArrayList<>();
    ArrayList<Invoice_image> invoice_imageDto;

    InvoiceTotalsItemDto listobj = new InvoiceTotalsItemDto();
    InvoiceDtoInvoice invoiceDtoInvoice;

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
        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        invoiceId = getIntent().getStringExtra("invoiceID");
        templatestr = getIntent().getStringExtra("templatestr");
        templateSelect = getIntent().getStringExtra("templateSelect");
        colorCode = getIntent().getStringExtra("colorCode");

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

//                final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
//
//                String content  = " <!DOCTYPE html>\n" +
//                        "<html>\n" +
//                        "<body>\n" +
//                        "\n" +
//                        "<h1>My First Heading</h1>\n" +
//                        "<p>My first paragraph.</p>\n" +
//                        " <a href='https://www.example.com'>This is a link</a>" +
//                        "\n" +
//                        "</body>\n" +
//                        "</html> ";
//
//
//                PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile, contentAll , new PDFPrint.OnPDFPrintListener() {
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
//                            Uri photoURI = FileProvider.getUriForFile(InvoiceViewActivityWebView.this,
//                                    "com.receipt.invoice.stock.sirproject.provider",
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
//        if (companylogopath.toLowerCase().endsWith(".jpg") || companylogopath.toLowerCase().endsWith(".jpeg") || companylogopath.toLowerCase().endsWith(".png")){
//            companylogopathdto= company_image_path + companylogopath;
//        }else{
//            companylogopathdto = "/android_res/drawable/white_img.png";
//        }
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

                createWebPrintJob(invoiceweb);
            }
        });
        setSupportActionBar(toolbar);
        titleView.setText("Preview Invoice");


        getinvoicedata();

    }

    private void getinvoicedata() {

        String token = Constant.GetSharedPreferences(ViewInvoiceReminderActivity.this, Constant.ACCESS_TOKEN);
        Call<InvoiceResponseDto> resposresult = apiInterface.getInvoiceDetail(token, invoiceId);
        resposresult.enqueue(new Callback<InvoiceResponseDto>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<InvoiceResponseDto> call, retrofit2.Response<InvoiceResponseDto> response) {

                Log.e("resss ", ""+response.body().toString());
                // image path of all
                company_image_path = response.body().getData().getCompanyImagePath();
                customer_image_path = response.body().getData().getCustomerImagePath();
                invoiceshre_link = response.body().getData().getInvoiceShareLink();
                invoice_image_path = response.body().getData().getInvoiceImagePath();
                // customer data get
                InvoiceDto data = response.body().getData();

                InvoiceCustomerDto invoiceCustomerDto = data.getInvoice().getCustomer();



                sltcustonername = invoiceCustomerDto.getCustomerName();
                sltcustomer_address = invoiceCustomerDto.getAddress();
                sltcustomer_phone_number = invoiceCustomerDto.getPhoneNumber();
                sltcustomer_website = invoiceCustomerDto.getWebsite();
                sltcustomer_email = invoiceCustomerDto.getEmail();
                sltcustomer_contact = invoiceCustomerDto.getContactName();


                if(!sltcustonername.equalsIgnoreCase("")){
                    stringBuilderBillTo.append(sltcustonername+"</br>");
                }
                if(!sltcustomer_address.equalsIgnoreCase("")){
                    stringBuilderBillTo.append(sltcustomer_address+"</br>");
                }
                if(!sltcustomer_contact.equalsIgnoreCase("")){
                    stringBuilderBillTo.append(sltcustomer_contact+"</br>");
                }
                if(!sltcustomer_phone_number.equalsIgnoreCase("")){
                    stringBuilderBillTo.append(sltcustomer_phone_number+"</br>");
                }
                if(!sltcustomer_website.equalsIgnoreCase("")){
                    stringBuilderBillTo.append(sltcustomer_website+"</br>");
                }
                if(!sltcustomer_email.equalsIgnoreCase("")){
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
                InvoiceCompanyDto companyDto = data.getInvoice().getCompany();
                company_name = companyDto.getName();
                company_address = companyDto.getAddress();
                company_contact = companyDto.getPhoneNumber();
                company_website = companyDto.getWebsite();
                company_email = companyDto.getEmail();

                if (companyDto.getLogo() != null)
                {
                    companylogopath = companyDto.getLogo();
                }



                //invoice Data
                invoiceDtoInvoice = data.getInvoice();
                invoicenumber = invoiceDtoInvoice.getInvoiceNo();
                date = invoiceDtoInvoice.getInvoiceDate();
                due_date = invoiceDtoInvoice.getDueDate();
                credit_terms = invoiceDtoInvoice.getCreditTerms();
                ref_no = invoiceDtoInvoice.getRefNo();

                payment_swift_bic = invoiceDtoInvoice.getPaymentSwiftBic();
                payment_currency = invoiceDtoInvoice.getPaymentCurrency();
                payment_iban = invoiceDtoInvoice.getPaymentIban();
                paypal_emailstr = companyDto.getPaypalEmail();
                payment_bank_name = companyDto.getPaymentBankName();
                cheque_payable_to = companyDto.getChequePayableTo();

                Log.e(TAG, "paypal_emailstrLL "+paypal_emailstr);

                paid_amount_payment_method = invoiceDtoInvoice.getPaidAmountPaymentMethod();
                Paymentamountdate = invoiceDtoInvoice.getPaidAmountDate();
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
//                Log.e("shipping_firstname",shipping_firstname);

                strnotes = invoiceDtoInvoice.getNotes();


                //tax



                // calculate amount data


                if(invoiceDtoInvoice.getTotals() != null){
                    grosamont = new ArrayList<InvoiceTotalsItemDto>(invoiceDtoInvoice.getTotals());
                }


                Gson gson = new Gson();
                String json2 = gson.toJson(invoiceDtoInvoice);

                Log.e(TAG, "jsonAA "+json2);



                productsItemDtos = new ArrayList<ProductsItemDto>(invoiceDtoInvoice.getProducts());
                invoice_imageDto = new ArrayList<Invoice_image>(data.getInvoiceImage());

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
                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            Grossamount_str = formatter.format(vc);
                            //Grossamount_str_real = dd;
                        }


                        // Grossamount_str = listobj.getValue();
                    } else if (title.equals("Sub Total")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            Subtotalamount = formatter.format(vc);
                        }
                        // Subtotalamount = listobj.getValue();
                    } else if (title.equals("Grand Total")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            netamountvalue = formatter.format(vc);
                        }
                        //netamountvalue = listobj.getValue();
                    } else if (title.equals("Paid Amount")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            strpaid_amount = formatter.format(vc);
                        }
                        //strpaid_amount = listobj.getValue();
                    } else if (title.equals("Remaining Balance")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            Blanceamountstr = formatter.format(vc);
                        }
                        //Blanceamountstr = listobj.getValue();
                    }else if (code.equals("tax")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            invoicetaxvalue = formatter.format(vc);
                            taxTitle = title;
                        }
                        //invoicetaxvalue = listobj.getValue();
                    }
                    else if (title.equals("Discount")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            strdiscountvalue = formatter.format(vc);
                        }
                        // strdiscountvalue = listobj.getValue();
                    }
                    else if (title.equals("Freight Cost")) {
                        if(!listobj.getValue().equalsIgnoreCase("")){
                            String dd = listobj.getValue();
                            double vc = Double.parseDouble(dd);
                            DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                            freight_cost = formatter.format(vc);
                        }
                        // freight_cost = listobj.getValue();
                    }



                }

                view_invoice();


            }

            @Override
            public void onFailure(Call<InvoiceResponseDto> call, Throwable t) {

            }
        });

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


    @SuppressLint("LongLogTag")
    public void view_invoice() {

        invoiceweb = findViewById(R.id.invoiceweb);


        if(shippingfirstname.equals(""))
        {
            Log.e("sltcustonername",sltcustonername);
            Shiping_tostr="";
        }else {
            Shiping_tostr="Ship To:";

            if(!shippingfirstname.equalsIgnoreCase("")){
                stringBuilderShipTo.append(shippingfirstname+"</br>");
            }
            if(!shippinglastname.equalsIgnoreCase("")){
                stringBuilderShipTo.append(shippinglastname+"</br>");
            }
            if(!shippingaddress1.equalsIgnoreCase("")){
                stringBuilderShipTo.append(shippingaddress1+"</br>");
            }
            if(!shippingaddress2.equalsIgnoreCase("")){
                stringBuilderShipTo.append(shippingaddress2+"</br>");
            }
            if(!shippingcity.equalsIgnoreCase("")){
                stringBuilderShipTo.append(shippingcity+"</br>");
            }
            if(!shippingcountry.equalsIgnoreCase("")){
                stringBuilderShipTo.append(shippingcountry+"</br>");
            }
            if(!shippingpostcode.equalsIgnoreCase("")){
                stringBuilderShipTo.append(shippingpostcode+"");
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

        // Uri uri = Uri.parse("android.resource://com.receipt.invoice.stock.sirproject/drawable/white_img.png");

        // String path = uri.toString();
        //  Log.e("path", path);


        String multipleimage = "";

        String multipagepath = null;

        String attachmentimage = "";

        if (invoice_imageDto.size() < 1) {
            attachmentimage = "";

        } else {
            attachmentimage = "Attachments";
        }


        if (invoice_imageDto != null) {
            for (int i = 0; i < invoice_imageDto.size(); i++) {

                Log.e(TAG, "invoice_imageDtoAA "+invoice_imageDto.get(i).getImage());
                // Log.e(TAG, "invoice_imageDtoBB "+invoice_imageDto.get(i).getImage());

                try {

                    multipagepath = IOUtils.toString(getAssets().open("attchment.html"))


                            .replaceAll("#ATTACHMENT_1#", "http://13.126.22.0/saad/app/uploads/invoice/"+invoice_imageDto.get(i).getImage());


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

                DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
                double productQuantity= Double.parseDouble(productsItemDtos.get(i).getQuantity());
                double producpriceRate = Double.parseDouble(productsItemDtos.get(i).getPrice());
                double producpriceAmount = Double.parseDouble(productsItemDtos.get(i).getTotal());

                productitem = IOUtils.toString(getAssets().open("single_item.html"))
                        .replaceAll("#NAME#", ""+productsItemDtos.get(i).getName())
                        .replaceAll("#DESC#", ""+productsItemDtos.get(i).getDescription() == null ? "" : productsItemDtos.get(i).getDescription())
                        .replaceAll("#UNIT#", ""+productsItemDtos.get(i).getMeasurementUnit() == null ? "" : productsItemDtos.get(i).getMeasurementUnit())
                        .replaceAll("#QUANTITY#", ""+formatter.format(productQuantity))
                        .replaceAll("#PRICE#", ""+formatter.format(producpriceRate) +"" + Utility.getReplaceDollor(currency_code))
                        .replaceAll("#TOTAL#", ""+formatter.format(producpriceAmount) +"" + Utility.getReplaceDollor(currency_code));

                productitemlist = productitemlist + productitem;


                //Log.e(TAG, "PRICEAA "+productsItemDtos.get(i).getPrice() + currency_code);
            }


        } catch (Exception e) {
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
            notestringvalue="Notes:";
        }





//        String signatureinvoice = null;
        String companyname="";

        if(company_stamp.equals("") || company_stamp.endsWith("white_img.png"))
        {
            invoice_image_pathcompanystemp="/android_res/drawable/white_img.png";
            companyname="";
        }else {
            invoice_image_pathcompanystemp=invoice_image_path + company_stamp;
            companyname="Company Seal";
        }

        String signature_of_receivername="";
        if(signature_of_receiver.equals("") || signature_of_receiver.endsWith("white_img.png"))
        {
            invoice_image_pathreceiverpath="/android_res/drawable/white_img.png";
            signature_of_receivername="";
        }else {
            invoice_image_pathreceiverpath=invoice_image_path + signature_of_receiver;
            signature_of_receivername="SignatureofReceiver";
        }


        String signature_of_issuername="";
        if(signature_of_issuer.equals("") || signature_of_issuer.endsWith("white_img.png"))
        {
            invoice_image_pathissuverpath="/android_res/drawable/white_img.png";
            signature_of_issuername="";
        }else {
            invoice_image_pathissuverpath=invoice_image_path + signature_of_issuer;
            signature_of_issuername="Signature of Issuer";
        }

        Log.e(TAG, "invoice_image_pathcompanystemp "+invoice_image_pathcompanystemp);
        Log.e(TAG, "invoice_image_pathreceiverpath "+invoice_image_pathreceiverpath);

//        try {
//            signatureinvoice = IOUtils.toString(getAssets().open("Signatures.html"))
//                    .replaceAll("CompanyStamp", companyname)
//                    .replaceAll("SignatureofReceiver", signature_of_receivername)
//                    .replaceAll("SignatureofIssuer", signature_of_issuername)
//                    .replaceAll("dataimageCompany_Stamp",invoice_image_pathcompanystemp)
//                    .replaceAll("dataimageRecieverImage", invoice_image_pathreceiverpath)
//                    .replaceAll("data:imageSige_path", invoice_image_pathissuverpath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        String content = null;










        String shipingvaluetxt="";
        if(freight_cost.equals("0") ){
            // Do you work here on success
            Shipingcosstbyct="";
            shipingvaluetxt="";


        }else{
            // null response or Exception occur

            Shipingcosstbyct=""+freight_cost+currency_code;
            shipingvaluetxt="Shipping";
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
        String cheque_payableTo = "";

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
            cheque_payableTo = "";
            hiddenpaidrow="hidden";


        } else {
            Log.e(TAG, "strpaid_amount2:: "+strpaid_amount);

            // null response or Exception occur
            paidamountstrrepvalue =strpaid_amount+currency_code;
            paidamountstrreptxt = "Paid Amount </br>"+"("+Paymentamountdate+")";


            pemailpaidstr = paypal_emailstr;
            payment_bankstr = payment_bank_name;
            payment_ibanstr = payment_iban;
            payment_currencystr = payment_currency;
            payment_swiftstr = payment_swift_bic;
            cheque_payableTo = cheque_payable_to;


            if ( Utility.isEmptyNull(cheque_payableTo).equalsIgnoreCase("")){
                cheque_payableTo = "";
            }else{
                cheque_payableTo = cheque_payable_to;
            }

            if ( Utility.isEmptyNull(pemailpaidstr).equalsIgnoreCase("")){
                pemailpaidstr = "";
            }else{
                pemailpaidstr = paypal_emailstr;
            }

            if ( Utility.isEmptyNull(payment_bankstr).equalsIgnoreCase("")){
                payment_bankstr = "";
            }else{
                payment_bankstr = payment_bank_name;
            }

            if ( Utility.isEmptyNull(payment_ibanstr).equalsIgnoreCase("")){
                payment_ibanstr = "";
            }else{
                payment_ibanstr = payment_iban;
            }

            if ( Utility.isEmptyNull(payment_swiftstr).equalsIgnoreCase("")){
                payment_swiftstr = "";
            }else{
                payment_swiftstr = payment_swift_bic;
            }

            if ( Utility.isEmptyNull(payment_currencystr).equalsIgnoreCase("")){
                payment_currencystr = "";
            }else{
                payment_currencystr = payment_currency;
            }


            paimnetdetailstrtxt=" Payment Details ";
            bycheckstrtxt="By cheque :";
            paypalstrtxt="Pay Pal :";
            bankstrtxt="Bank :";

            hiddenpaidrow="";
        }


        Log.e(TAG,"paimnetdetailstrtxtCCC "+paimnetdetailstrtxt);
        Log.e(TAG,"bycheckstrtxtCCC "+bycheckstrtxt);
        Log.e(TAG,"paypalstrtxtCCC "+paypalstrtxt);
        Log.e(TAG,"bankstrtxtCCC "+bankstrtxt);

        Log.e(TAG,"cheque_payableToCCC "+cheque_payableTo);
        Log.e(TAG,"payment_bankstrCCC "+payment_bankstr);
        Log.e(TAG,"payment_ibanstrCCC "+payment_ibanstr);
        Log.e(TAG,"payment_currencystrCCC "+payment_currencystr);
        Log.e(TAG,"payment_swiftstrCCC "+payment_swiftstr);
        Log.e(TAG,"pemailpaidstrCCC "+pemailpaidstr);




        String strreferencenovalue="";
        String strreferencenotxtvalue="";

        if (ref_no.isEmpty()) {
            // Do you work here on success

            strreferencenovalue="";
            strreferencenotxtvalue="";

        } else {
            // null response or Exception occur

            strreferencenovalue=ref_no;
            strreferencenotxtvalue=" Reference No:";


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
            discounttxtreplace = " Discount ";
        }



        String subTotalTxt = "";
        String subTotalValueTxt = "";


        if(strdiscountvalue.equalsIgnoreCase("0")){
            subTotalTxt = "";
            subTotalValueTxt = "";
        }else{
            subTotalTxt = "SubTotal";
            subTotalValueTxt = Subtotalamount + ""+ Utility.getReplaceDollor(currency_code);
        }



        String companylogopathdto="";

        if (companylogopath.toLowerCase().endsWith(".jpg") || companylogopath.toLowerCase().endsWith(".jpeg") || companylogopath.toLowerCase().endsWith(".png")){
            companylogopathdto= company_image_path + companylogopath;
        }else{
            companylogopathdto = "/android_res/drawable/white_img.png";
        }

        String name = "notes/duedatereminder.html";
        String nameName = "file:///android_asset/notes/duedatereminder.html";
        if(templatestr.equals("1")) {

//            if(templateSelect.equalsIgnoreCase("0")){
//                name = "invoice.html";
//                nameName = "file:///android_asset/invoice.html";
//            }else if(templateSelect.equalsIgnoreCase("1")){
//                name = "invoice1.html";
//                nameName = "file:///android_asset/invoice1.html";
//            }else if(templateSelect.equalsIgnoreCase("2")){
//                name = "invoice2.html";
//                nameName = "file:///android_asset/invoice2.html";
//            }else if(templateSelect.equalsIgnoreCase("3")){
//                name = "invoice3.html";
//                nameName = "file:///android_asset/invoice3.html";
//            }else if(templateSelect.equalsIgnoreCase("4")){
//                name = "invoice4.html";
//                nameName = "file:///android_asset/invoice4.html";
//            }

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




            try {
                content = IOUtils.toString(getAssets().open(name))
                        .replaceAll("Company Name", company_name)
                        .replaceAll("Address", stringBuilderCompany.toString())
//                        .replaceAll("Contact No.", company_contact)
//                        .replaceAll("Website", company_website)
//                        .replaceAll("Email", company_email)
                        .replaceAll("Name!", company_name)
                        .replaceAll("CusName", date)
                        .replaceAll("INV-564", "" + invoicenumber)
                        .replaceAll("invD", date)
                        .replaceAll("DueDate", due_date)
                        .replaceAll("crTerms", invoiceDtoInvoice.getCreditTerms())
                        .replaceAll("refNo", strreferencenovalue)
                        .replaceAll("GrossAm-", ""+Grossamount_str + ""+ Utility.getReplaceDollor(currency_code))
                        .replaceAll("Discount-", ""+Utility.getReplaceDollor(discountvalue))
                        .replaceAll("SubTotal-", subTotalValueTxt)
                        .replaceAll("Txses-", Utility.getReplaceDollor(taxtamountstr))
                        .replaceAll("Shipping-", Utility.getReplaceDollor(Shipingcosstbyct))
                        .replaceAll("Total Amount-", netamountvalue + Utility.getReplaceDollor(currency_code))
                        .replaceAll("PaidsAmount",  Utility.getReplaceDollor(paidamountstrrepvalue))
                        .replaceAll("Paid Amount", paidamountstrreptxt)
                        .replaceAll("Balance Due-", Blanceamountstr + Utility.getReplaceDollor(currency_code))

                        .replaceAll("SubTotal", subTotalTxt)


                        .replaceAll("Client N", ""+stringBuilderBillTo.toString())
//                        .replaceAll("Client A", sltcustomer_address)
//                        .replaceAll("Client C P", sltcustomer_contact)
//                        .replaceAll("Client C N", sltcustomer_phone_number)
//                        .replaceAll("Client Web", sltcustomer_website)
//                        .replaceAll("Client E", sltcustomer_email)
                        .replaceAll("Notes-", strnotes)
                       // .replaceAll("#SIGNATURES#", signatureinvoice)
//                Log.e(TAG, "invoice_image_pathcompanystemp "+invoice_image_pathcompanystemp);
//                Log.e(TAG, "invoice_image_pathreceiverpath "+invoice_image_pathreceiverpath);

                        .replaceAll("dataimageCompany_Stamp", invoice_image_pathcompanystemp)
                        .replaceAll("dataimageRecieverImage", invoice_image_pathreceiverpath)

                        .replaceAll("Company Seal", companyname)
                        .replaceAll("Authorized Signatory", signature_of_receivername)

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


                        .replaceAll("Checkto", cheque_payableTo)
                        .replaceAll("BankName", payment_bankstr)
                        .replaceAll("Pemail", pemailpaidstr)
                        .replaceAll("IBAN", payment_ibanstr)
                        .replaceAll("Currency", payment_currencystr)
                        .replaceAll("Swift/BICCode", payment_swiftstr)

                        .replaceAll(" Payment Details ", paimnetdetailstrtxt)
                        .replaceAll("By cheque :", bycheckstrtxt)
                        .replaceAll("Pay Pal :", paypalstrtxt)
                        .replaceAll("Bank :", bankstrtxt)


                        .replaceAll("#799f6e", colorCode)
                        .replaceAll("#TEMP3#", String.valueOf(R.color.blue));

                Log.e(TAG, "pemailpaidstrVV "+pemailpaidstr);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (templatestr.equals("2"))
        {

        }

        contentAll = content;

        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);



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
//                final File savedPDFFile = FileManager.getInstance().createTempFile(ViewInvoiceReminderActivity.this, "pdf", false);
//
//                PDFUtil.generatePDFFromWebView(savedPDFFile, invoiceweb, new PDFPrint.OnPDFPrintListener() {
//                    @Override
//                    public void onSuccess(File file) {
//                       // Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//                        if(file.exists()) {
//
//                            Log.e(TAG, "FILENAME" +file);
//                          //  Log.e(TAG, "customer_name" +customer_name);
//
//                           // createinvoicewithdetail(file);
//
//
//
//                            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//                           // File fileWithinMyDir = new File(message);
//                            Uri photoURI = FileProvider.getUriForFile(ViewInvoiceReminderActivity.this,
//                                    "com.receipt.invoice.stock.sirproject.provider",
//                                    file);
//
//                            if(file.exists()) {
//                                intentShareFile.setType("application/pdf");
//                                Uri outputFileUri = Uri.fromFile(file);
//                                intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);
//
//                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
//                                        "Invoice Due Date Reminder");
//                                intentShareFile.putExtra(Intent.EXTRA_TEXT,
//                                        "Your invoice is due for payment. Kindly find below reminder note");
//
//                                if (Utility.isAppAvailable(ViewInvoiceReminderActivity.this, "com.google.android.gm")){
//                                    intentShareFile.setPackage("com.google.android.gm");
//                                }
//                                startActivity(intentShareFile);
//
//                            }
//
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Exception exception) {
//                        exception.printStackTrace();
//                    }
//                });
//
//            }
//        });
//
//

    }


}