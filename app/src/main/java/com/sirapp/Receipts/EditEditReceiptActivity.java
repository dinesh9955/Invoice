package com.sirapp.Receipts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.print.PDFPrint;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.DN.DebitNotesViewActivityWebView;
import com.sirapp.Home.GoProActivity;
import com.sirapp.ImageResource.FileCompressor;
import com.sirapp.PO.ConvertToPVActivity;
import com.sirapp.PV.EditEditPVActivity;
import com.sirapp.RetrofitApi.ApiInterface;
import com.sirapp.RetrofitApi.RetrofitInstance;
import com.sirapp.API.AllSirApi;
import com.sirapp.Adapter.Customer_Bottom_Adapter;
import com.sirapp.Adapter.Product_Bottom_Adapter;
import com.sirapp.Adapter.Products_Adapter;
import com.sirapp.Adapter.Service_bottom_Adapter;
import com.sirapp.Base.BaseActivity;
import com.sirapp.BuildConfig;
import com.sirapp.Company.Companies_Activity;
import com.sirapp.Constant.Constant;
import com.sirapp.Customer.Customer_Activity;
import com.sirapp.Invoice.ChooseTemplate;
import com.sirapp.Invoice.response.InvoiceCompanyDto;
import com.sirapp.Invoice.response.InvoiceCustomerDto;
import com.sirapp.Invoice.response.InvoiceTotalsItemDto;
import com.sirapp.Invoice.response.ProductsItemDto;
import com.sirapp.Model.Customer_list;
import com.sirapp.Model.ItemQuantity;
import com.sirapp.Model.Moving;
import com.sirapp.Model.Product_Service_list;
import com.sirapp.Model.Product_list;
import com.sirapp.Model.SelectedTaxlist;
import com.sirapp.Model.Service_list;
import com.sirapp.Model.Tax_List;
import com.sirapp.Product.Product_Activity;
import com.sirapp.R;
import com.sirapp.Service.Service_Activity;
import com.sirapp.Tax.CustomTaxAdapter;
import com.sirapp.Tax.Tax_Activity;
import com.sirapp.Tax.Taxlistbycompany;
import com.sirapp.Utils.GlideApp;
import com.sirapp.Utils.Utility;
import com.sirapp.Invoice.Fragment_Create_Invoice;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import gun0912.tedbottompicker.TedRxBottomPicker;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;

public class EditEditReceiptActivity extends BaseActivity implements Customer_Bottom_Adapter.Callback, Products_Adapter.onItemClickListner, Product_Bottom_Adapter.Callback, Service_bottom_Adapter.Callback, CustomTaxAdapter.Callback {
    private static final String TAG = "EditReceiptActivity";

    String attachmentHtml = "attchment.html";
    String singleItemHtml = "single_item.html";
    String signatureHtml = "Signatures.html";
    String mainHtml = "receipt.html";


    String companycolor = "#ffffff";
    int selectedTemplate = 0;
//    int defaultClick = 0;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int GALLARY_aCTION_PICK_CODE = 10;
    private static final int CAMERA_ACTION_PICK_CODE = 9;
    private static final String[] PERMISSION_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //Api response data colllection
    String invoiceId = "";
    ApiInterface apiInterface;
    String signature_of_issuerdto = "", signature_of_receiverdto = "", company_stampdto = "";

    double Grossamount_strdto = 0.0,  Discountamountstrdto = 0.0,  Subtotalamountdto = 0.0, Tax_amountdto = 0.0, Shippingamountdto = 0.0, Netamountvaluedto = 0.0, Paidamountstrdto = 0.0, Blanceamountstrdto = 0.0;


    String due_datedto = "", datedto = "", credit_termsdto = "";
    String sltcustonernamedto = "", sltcustomer_emaildto = "", sltcustomer_contactdto = "", sltcustomer_addressdto = "", sltcustomer_websitedto = "", sltcustomer_phone_numberdto = "", invoicetaxvalue = "";
    String invoicenumberdto = "", ref_nodto = "", paid_amount_payment_methoddto = "", strpaid_amountdto = "",   netamountvaluedto = "";
    String shipping_zonedto;
    String company_image_pathdto, customer_image_pathdto, invoiceshre_linkdto, invoice_image_pathdto, customerDtoContactName;
    ArrayList<InvoiceTotalsItemDto> grosamontdto;
    ArrayList<ProductsItemDto> productsItemDtosdto;
    ArrayList<String> invoice_imageDto = new ArrayList<>();

    // Edti invoice With Detail Datata
    InvoiceTotalsItemDto listobj = new InvoiceTotalsItemDto();
    ReceiptDtoReceipt invoiceDtoInvoice;
    String currency_codedto;
    //3
    // String companylogopathdtodt = "";
    TextView itemstxtTemplate, invoicenumtxt, duedatetxt, duedate, invoicetotxt, invoicerecipnt, itemstxt, subtotaltxt, subtotal, discounttxt, discount, txttax, tax, txtcredit, txtdays, txtreferenceno, edreferenceno, txtduedate, edduedate, txtgrossamount, grosstotal, txtfreight, freight, txtnetamount, netamount, txtpaidamount, paidamount, txtbalance, balance, s_invoice, s_receiver, c_stamp, attachmenttxt;
    Button additem, createinvoice, options, addservice;
    RecyclerView productsRecycler;
    EditText ednotes, invoicenum;


    StringBuilder stringBuilderBillTo = new StringBuilder();
    StringBuilder stringBuilderShipTo = new StringBuilder();

    String  tax_type = "", rate1 = "" , value = "" , rate_type = "";

    String taxtypeclusive = "", taxtype = "", taxtrateamt = "", taxID = "";

    ArrayList<Product_list> tempList = new ArrayList<Product_list>();

    ArrayList<String> tempQuantity = new ArrayList<String>();


    ArrayList<String> producprice = new ArrayList<String>();
    // for tax list array listin
    ArrayList<Tax_List> tax_list_array = new ArrayList<Tax_List>();
    ArrayList<SelectedTaxlist> selectedtaxt = new ArrayList<SelectedTaxlist>();
    String invoicetaxamount;


    ImageView imgsigsuccess, imgrecsuccess, imgstampsuccess, attachmenttxtimg;
    BottomSheetDialog bottomSheetDialog, bottomSheetDialog2, bottomSheetDialog3;
    AwesomeSpinner selectcompany, selectwarehouse;

    Products_Adapter products_adapter;
    ArrayList<Product_Service_list> names = new ArrayList<>();
    ArrayList<String> totalpriceproduct = new ArrayList<String>();

    ArrayList<String> quantity = new ArrayList<>();
    //products bottom sheet
    TextView txtproducts;
    EditText search;
    Product_Bottom_Adapter product_bottom_adapter;
    ArrayList<Product_list> product_bottom = new ArrayList<>();
    RecyclerView recycler_services;
    Double ProductTotalprice = 0.0;    //servcies bottom sheet
    TextView txtservices;
    EditText search_service;
    Service_bottom_Adapter service_bottom_adapter;
    ArrayList<Taxlistbycompany> taxtlist = new ArrayList<>();
    CustomTaxAdapter customTaxAdapter;
    RecyclerView taxrecycler;

    //tax bottom sheet
    ArrayList<Service_list> service_bottom = new ArrayList<>();

    ArrayList<Customer_list> customerinfo = new ArrayList<>();

    RecyclerView recycler_products;
    TextView imgincome;


    //paid amount
    Switch taxswitch;
    Button btndone;
    TextView txtpaid, txtamount, txtdate;
    EditText edamount;
    TextView eddate;

    //three dots bottomsheet
    AwesomeSpinner selectpaymentmode;


    //days bottom sheet
    Button btndone2;
    Button btnviewinvoice, btnclear, btndotcancel;
    TextView txtcredit1, txtor;
    RadioGroup radiogroup1, radiogroup2;
    RadioButton rnone, r3days, r14days, r30days, r60days, r120days, r7days, r21days, r45days, r90days, r365days, rimmediately;
    EditText edmanual;

    //datepicker work
    Button btndone1;
    String dayss = "";
    DatePickerDialog.OnDateSetListener mlistener;
    DatePickerDialog.OnDateSetListener mlistener2;
    Calendar myCalendar;

    //signaturepad
    DatePickerDialog datePickerDialog;
    DatePickerDialog datePickerDialog2;
    File fileimage;
    ArrayList<String> cnames = new ArrayList<>();
    ArrayList<String> cids = new ArrayList<>();
    ArrayList<String> wnames = new ArrayList<>();

    //API's
    ArrayList<String> wids = new ArrayList<>();
    ArrayList<String> paymode = new ArrayList<>();
    String selectedCompanyId = "";
    String selectwarehouseId = "";
    int warehousePosition = 0;

    String paimentmodespinerstr = "";
    //customer bottom sheet
    TextView txtcustomer, txtname, txtcontactname;
    EditText search_customers;
    RecyclerView recycler_customers;
    Customer_Bottom_Adapter customer_bottom_adapter;
    ArrayList<Customer_list> customer_bottom = new ArrayList<>();
    ArrayList<Customer_list> selected = new ArrayList<>();

    String invoicecompanyiddto;
    //For Intent
    String company_id = "", company_name = "", company_address = "", company_contact = "", company_email = "", company_website = "", payment_bank_name = "", payment_currency = "", payment_iban = "", payment_swift_bic = "";
    String customer_name = "", customer_id = "", custoner_contact_name = "", customer_email = "", customer_contact = "", customer_address = "", customer_website = "", customer_phone_number = "";
    String invoice_no = "", invoice_due_date = "", invoice_date = "", credit_terms = "", reference_no = "";
    String signature_of_issuer = "", signature_of_receiver = "", company_stamp = "", taxamount, netamountvalue;
    String s_r = "0";
    String  Grossamount_str;


    ArrayList<String> rate = new ArrayList<>();

    String paypal_emailstr = "";
    // pick image from galary and
    Context applicationContext = Companies_Activity.getContextOfApplication();
    FileCompressor mCompressor;
    boolean check = true;
    String strdiscount = "", strdiscountvalue = "0";

    // attchment image patth
    ArrayList<String> attchmentimage = new ArrayList<String>();
    Double total_price = 0.0;
    // Image Data
    File company_stampFileimage, signatureofinvoicemaker, signaturinvoicereceiver, company_logoFileimage;
    ArrayList<String> multimgpath = new ArrayList<>();
    File[] multiple = new File[5];
    String strnotes, ref_no, freight_cost = "", strpaid_amount = "", Blanceamountstr = "";
    String Paymentamountdate, paymentmode, signatureofreceiverst = "", paidamountstr = "";
    // customer detail
    String shippingfirstname, shippinglastname, shippingaddress1, shippingaddress2, shippingcity, shippingpostcode, shippingcountry, paymentbankname, paymentcurrency, paymentiban, paymentswiftbic;
    // Date pikker date Convert To time Millissecund
    long datemillis;
    // Invoice no
    String Invoiceno = "";
    int invoicenovalue;
    // Company logo path
    String companylogopath = "", Subtotalamount = "";
    int selectedItemOfMySpinner;
    // customer information
    String shipping_firstname, shipping_lastname, shipping_address_1, shipping_address_2, shipping_city, shipping_postcode, shipping_country, shipping_zone;
    String Selectedcompanyname, taxrname;
    //    int newinvoice_count;
    private AVLoadingIndicatorView avi;
    ImageView avibackground;
    private SignaturePad signaturePad;
    private Button btnclear1;
    private Button btnsave;
    //Radio Group button Seelect
    private RadioGroup radioGroup;
    // multiple image attachemnt
    private List<Uri> selectedUriList;
    //  ArrayList<String> attchmentimage=new ArrayList<>();
    private Disposable multiImageDisposable;
    private ViewGroup mSelectedImagesContainer;
    private RequestManager requestManager;

    WebView invoiceweb;
//    String templateSelect = "0";
//    String colorCode = "#ffffff";
    TextView textViewNoItems;

    TextView taxvalueText;

    double discountAmountDD = 0.0;

    boolean boolTax = false;

    double grandAmountZZ = 0.0;
    double discountAmountZZ = 0.0;
    double subtotalAmountZZ = 0.0;
    double taxAmountZZ = 0.0;
    double afterTaxAmountZZ = 0.0;
    double shippingAmountZZ = 0.0;
    double netAmountZZ = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.editreceiptactivity);
        Constant.toolbar(EditEditReceiptActivity.this, getString(R.string.header_edit_receipt));

        taxvalueText = findViewById(R.id.taxvalue);

        invoiceweb = findViewById(R.id.invoiceweb);

        textViewNoItems = findViewById(R.id.txtnoitems);

        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        invoiceId = getIntent().getStringExtra("invoiceID");
        if (invoiceId != null) {
            Log.e("invoiceId", invoiceId);
        }
//        invoice_count = getIntent().getStringExtra("invoice_count");
//        if (invoice_count != null) {
//            Log.e("invoice_count", invoice_count);
//        }

//        templateSelect = getIntent().getStringExtra("templateSelect");
//        colorCode = getIntent().getStringExtra("colorCode");

//        Log.e(TAG, "selectedTemplate "+templateSelect);
//        Log.e(TAG, "colorCode "+colorCode);

        //newinvoice_count = Integer.parseInt(invoice_count) + 1;
        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);
        invoicenumtxt = findViewById(R.id.invoicenumtxt);
        invoicenum = findViewById(R.id.invoivenum);
        invoicenum.setEnabled(false);
        duedatetxt = findViewById(R.id.duedatetxt);
        duedate = findViewById(R.id.duedate);
        invoicetotxt = findViewById(R.id.invoicetotxt);
        invoicerecipnt = findViewById(R.id.invoicerecipnt);
        itemstxt = findViewById(R.id.itemstxt);
        subtotaltxt = findViewById(R.id.subtotaltxt);
        subtotal = findViewById(R.id.subtotal);
        discounttxt = findViewById(R.id.discounttxt);
        discount = findViewById(R.id.discount);
        txttax = findViewById(R.id.txttax);
        tax = findViewById(R.id.tax);
        additem = findViewById(R.id.additem);
        createinvoice = findViewById(R.id.createinvoice);
        options = findViewById(R.id.options);
        productsRecycler = findViewById(R.id.productsRecycler);
        txtcredit = findViewById(R.id.txtcredit);
        txtdays = findViewById(R.id.txtdays);
        txtreferenceno = findViewById(R.id.txtreferenceno);
        edreferenceno = findViewById(R.id.edreferenceno);
        txtduedate = findViewById(R.id.txtduedate);
        edduedate = findViewById(R.id.edduedate);
        grosstotal = findViewById(R.id.grosstotal);
        txtgrossamount = findViewById(R.id.txtgrossamount);
        txtfreight = findViewById(R.id.txtfreight);
        freight = findViewById(R.id.freight);
        txtnetamount = findViewById(R.id.txtnetamount);
        netamount = findViewById(R.id.netamount);
        txtpaidamount = findViewById(R.id.txtpaidamount);
        paidamount = findViewById(R.id.paidamount);
        txtbalance = findViewById(R.id.txtbalance);
        balance = findViewById(R.id.balance);
        s_invoice = findViewById(R.id.s_invoice);
        s_receiver = findViewById(R.id.s_receiver);
        c_stamp = findViewById(R.id.c_stamp);
        ednotes = findViewById(R.id.ednotes);
        selectcompany = findViewById(R.id.selectcompany);
        selectwarehouse = findViewById(R.id.selectwarehouse);
        addservice = findViewById(R.id.addservice);
        imgsigsuccess = findViewById(R.id.imgsigsuccess);
        imgrecsuccess = findViewById(R.id.imgrecsuccess);

        imgstampsuccess = findViewById(R.id.imgstampsuccess);

        attachmenttxtimg = findViewById(R.id.attachmenttxtimg);
        attachmenttxt = findViewById(R.id.attachmenttxt);

        itemstxtTemplate = findViewById(R.id.itemstxtTemplate);

        ScrollView scroll = (ScrollView)findViewById(R.id.scrollView);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (invoicenum.hasFocus()) {
                    invoicenum.clearFocus();
                }
                if (edreferenceno.hasFocus()) {
                    edreferenceno.clearFocus();
                }
                if (ednotes.hasFocus()) {
                    ednotes.clearFocus();
                }
                return false;
            }
        });


        myCalendar = Calendar.getInstance();

        Fragment_Create_Invoice.verifyStroagePermissions(this);

        requestManager = GlideApp.with(this);
        mCompressor = new FileCompressor(this);


        s_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_r = "1";
                createbottom_signaturepad();
                bottomSheetDialog.show();

            }
        });

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog2 = new BottomSheetDialog(this);
        bottomSheetDialog3 = new BottomSheetDialog(this);
        setFonts();
        setListeners();

        products_adapter = new Products_Adapter(this, product_bottom, tempList, this::onClick, tempQuantity, producprice);
//        productsRecycler.setAdapter(products_adapter);

        if(tempList.size() == 0){
            textViewNoItems.setVisibility(View.VISIBLE);
        }else{
            textViewNoItems.setVisibility(View.GONE);
        }

        mlistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);


                updateLabel();
                Log.e("Dateeee", i + " " + i1 + " " + i2);

            }
        };

        mlistener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);

                updateLabe2();
                Log.e("Dateeee", i + " " + i1 + " " + i2);

            }
        };


        itemstxtTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "companycolor: "+companycolor);
//                defaultClick = 1;
                Intent intent = new Intent(EditEditReceiptActivity.this, ChooseTemplate.class);
                intent.putExtra("companycolor", companycolor);
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 121);
            }
        });


        duedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        edduedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog2.show();

            }
        });

        //for duedate
        datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth, mlistener,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


        //edduedate
        datePickerDialog2 = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth, mlistener2,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //datePickerDialog2.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        productsRecycler.setLayoutManager(layoutManager);
        productsRecycler.setHasFixedSize(true);

        companyget();

        if(AllSirApi.ALL_FONT == true){
            checkDevice();
        }

        getinvoicedata();

    }


    private void checkDevice() {
        if(Utility.isTablet(EditEditReceiptActivity.this) == true){
            attachmentHtml = "attchment.html";
            singleItemHtml = "single_item.html";
            signatureHtml = "Signatures.html";
            mainHtml = "receipt.html";
        }else {
            String manufacturerModel = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + " " + Build.BRAND + " " + Build.DEVICE;
            if (manufacturerModel.toLowerCase().contains("j7")) {
                attachmentHtml = "5/attchment.html";
                singleItemHtml = "5/single_item.html";
                signatureHtml = "5/Signatures.html";
                mainHtml = "5/receipt.html";
            } else {
                attachmentHtml = "6/attchment.html";
                singleItemHtml = "6/single_item.html";
                signatureHtml = "6/Signatures.html";
                mainHtml = "6/receipt.html";
            }
        }
    }


    private void getinvoicedata() {

        Log.e(TAG, "getinvoicedata:: "+invoiceId);

        String token = Constant.GetSharedPreferences(EditEditReceiptActivity.this, Constant.ACCESS_TOKEN);
        Call<ReceiptResponseDto> resposresult = apiInterface.getReceiptDetail(token, invoiceId, ""+getLanguage());
        resposresult.enqueue(new Callback<ReceiptResponseDto>() {
            @Override
            public void onResponse(Call<ReceiptResponseDto> call, retrofit2.Response<ReceiptResponseDto> response) {

                // image path of all

                company_image_pathdto = response.body().getData().getCompany_image_path();
                customer_image_pathdto = response.body().getData().getCustomer_image_path();
                invoiceshre_linkdto = response.body().getData().getReceipt_share_link();
                invoice_image_pathdto = response.body().getData().getReceipt_image_path();
                // customer data get
                ReceiptDto data = response.body().getData();

                InvoiceCustomerDto invoiceCustomerDto = data.getReceipt().getCustomer();


                sltcustonernamedto = invoiceCustomerDto.getCustomerName();

                customer_name = sltcustonernamedto;
                invoicerecipnt.setText(sltcustonernamedto);
                sltcustomer_addressdto = invoiceCustomerDto.getAddress();
                sltcustomer_phone_numberdto = invoiceCustomerDto.getPhoneNumber();
                sltcustomer_websitedto = invoiceCustomerDto.getWebsite();
                sltcustomer_emaildto = invoiceCustomerDto.getEmail();
                sltcustomer_contactdto = invoiceCustomerDto.getCustomerName();
                customerDtoContactName = invoiceCustomerDto.getContactName();
                customer_name = sltcustomer_contactdto;



                shippingfirstname = invoiceCustomerDto.getShippingFirstname();
                shippinglastname = invoiceCustomerDto.getShippingLastname();
                shippingaddress1 = invoiceCustomerDto.getShippingAddress1();
                shippingaddress2 = invoiceCustomerDto.getShippingAddress2();
                shipping_city = invoiceCustomerDto.getShippingCity();
                shippingcountry = invoiceCustomerDto.getShippingCountry();
                shippingpostcode = (String) invoiceCustomerDto.getShippingPostcode();
                shipping_zonedto = (String) invoiceCustomerDto.getShippingZone();


                // Company Detai
                InvoiceCompanyDto companyDto = data.getReceipt().getCompany();
                Selectedcompanyname = companyDto.getName();
                company_address = companyDto.getAddress();
                company_contact = companyDto.getPhoneNumber();
                company_website = companyDto.getWebsite();
                company_email = companyDto.getPaypalEmail();
                companylogopath = companyDto.getLogo();
                // companylogopathdto = companyDto.getLogo();
//                companylogopathdtodt = company_image_pathdto + companylogopathdto;
//
//                Log.e(TAG, "companylogopathdtodtAA "+companylogopathdtodt);

                // new DownloadsImagefromweblogoCom().execute(companylogopathdtodt);

                selectedCompanyId = companyDto.getCompanyId();
                Log.e("selectedCompanyId", selectedCompanyId);

                // all mehodh get information-------
                warehouse_list(selectedCompanyId);
                customer_list(selectedCompanyId);
                CompanyInformation(selectedCompanyId);
                productget(selectedCompanyId);
                serviceget(selectedCompanyId);


                //invoice Data
                invoiceDtoInvoice = data.getReceipt();
                invoicenumberdto = invoiceDtoInvoice.getReceipt_no();
                Log.e(TAG,"invoicenumberdto:: "+invoicenumberdto);

                invoicenum.setText(""+invoicenumberdto);

                // where House id
                selectwarehouseId = invoiceDtoInvoice.getWearhouseId();

                Log.e("Selected_house",selectwarehouseId+"  "+wnames.toString());

                datedto = invoiceDtoInvoice.getReceipt_date();
                due_datedto = invoiceDtoInvoice.getDueDate();
                duedate.setText(datedto);
                invoice_date = datedto;
                invoice_due_date = due_datedto;
                edduedate.setText(due_datedto);


                //  invoicenum.setText("Inv #" + newinvoice_count);



                invoicenum.setClickable(false);

                // selectedTemplate = Integer.parseInt(invoiceDtoInvoice.getTemplate_type());
                if(selectedTemplate != 0){
                    itemstxtTemplate.setText(getString(R.string.header_template)+" "+selectedTemplate);
                }
                strnotes = invoiceDtoInvoice.getNotes();
                ednotes.setText(Html.fromHtml(strnotes));
                //  credit_termsdto = invoiceDtoInvoice.getCreditTerms();
                // credit_terms = credit_termsdto;
                invoicecompanyiddto = invoiceDtoInvoice.getCompanyId();
                Log.e("invoicecompanyiddto", invoicecompanyiddto);
                customer_id = invoiceDtoInvoice.getCustomerId();

                //  paymentmode = invoiceDtoInvoice.getPaidAmountPaymentMethod();

                txtdays.setText(credit_termsdto);
                ref_nodto = invoiceDtoInvoice.getRefNo();
                ref_no = ref_nodto;
                edreferenceno.setText(ref_nodto);

                payment_swift_bic = invoiceDtoInvoice.getPaymentSwiftBic();
                payment_currency = invoiceDtoInvoice.getPaymentCurrency();
                payment_iban = invoiceDtoInvoice.getPaymentIban();
                // paid_amount_payment_methoddto = invoiceDtoInvoice.getPaidAmountPaymentMethod();

                currency_codedto = invoiceDtoInvoice.getCurrencySymbol();
                Log.e("currency_codhjkkjhkje", currency_codedto);
                //data set in calculate amount.


                company_stampdto = invoiceDtoInvoice.getCompanyStamp();
                //  Down load image From Web Data
                String companystempurlpath = invoice_image_pathdto + company_stampdto;
                Log.e(TAG, "invoice_image_pathdto"+ invoice_image_pathdto);
                Log.e(TAG, "company_stampdto"+ company_stampdto);

                if (company_stampdto.equals("")) {
                    Log.e(TAG, "company_stampdto1111"+ company_stampdto);
                } else {
                    Log.e(TAG, "company_stampdto2222"+ company_stampdto);
                    new DownloadCompanystempweb().execute(companystempurlpath);
                    imgstampsuccess.setVisibility(View.VISIBLE);
                }
                signature_of_receiverdto = invoiceDtoInvoice.getSignatureOfReceiver();
                // down load image from web signature image

                if (signature_of_receiverdto.equals("")) {

                } else {
                    s_r = "2";
                    String signatureofreceiverpath = invoice_image_pathdto + signature_of_receiverdto;
                    new Downloadsignaturereceiverweb().execute(signatureofreceiverpath);
                    imgrecsuccess.setVisibility(View.VISIBLE);
                }
                signature_of_issuerdto = invoiceDtoInvoice.getSignatureOfMaker();
                // signature of issueceiver from web data


                if (signature_of_issuerdto.equals("")) {


                } else {
                    s_r = "1";
                    String signatureofreceiverpath = invoice_image_pathdto + signature_of_issuerdto;
                    new Downloadsignatureissueweb().execute(signatureofreceiverpath);
                    imgsigsuccess.setVisibility(View.VISIBLE);
                }

//                shippingfirstname = invoiceDtoInvoice.getShippingFirstname();
//                shippinglastname = invoiceDtoInvoice.getShippingLastname();
//                shippingaddress1 = invoiceDtoInvoice.getShippingAddress1();
//                shippingaddress2 = invoiceDtoInvoice.getShippingAddress2();
//                shipping_city = invoiceDtoInvoice.getShippingCity();
//                shippingaddress2 = invoiceDtoInvoice.getShippingCity();
//                shippingcountry = invoiceDtoInvoice.getShippingCountry();
//                shippingpostcode = (String) invoiceDtoInvoice.getShippingPostcode();
//                shipping_zonedto = (String) invoiceDtoInvoice.getShippingZone();

                Log.e("shippingfirstname", shippingfirstname);

                // Add Custmoer detail
                Customer_list selectcsto = new Customer_list();
                selectcsto.setCustomer_name(sltcustonernamedto);
                selectcsto.setCustomer_address(sltcustomer_addressdto);
                selectcsto.setCustomer_contact_person(customerDtoContactName);
                selectcsto.setCustomer_phone(sltcustomer_phone_numberdto);
                selectcsto.setCustomer_website(sltcustomer_websitedto);
                selectcsto.setCustomer_email(sltcustomer_emaildto);

                selectcsto.setShipping_firstname(shippingfirstname);
                selectcsto.setShipping_lastname(shippinglastname);
                selectcsto.setShipping_address_1(shippingaddress1);
                selectcsto.setShipping_address_2(shippingaddress2);
                Log.e("shippingfirstname", shippingfirstname);
                Log.e("shippinglastname", shippinglastname);
                selectcsto.setShipping_city(shipping_city);
                selectcsto.setShipping_country(shippingcountry);
                selectcsto.setShipping_postcode(shippingpostcode);
                selectcsto.setShipping_zone(shipping_zonedto);


                selected.add(selectcsto);

                // calculate amount data

                grosamontdto = new ArrayList<InvoiceTotalsItemDto>(invoiceDtoInvoice.getTotals());



                //    products_adapter = new Products_Adapter(this, product_bottom, tempList, this::onClick, tempQuantity, producprice);

                //  products_adapter.update(product_bottom, tempList, tempQuantity, producprice);

                if(tempList.size() == 0){
                    textViewNoItems.setVisibility(View.VISIBLE);
                }else{
                    textViewNoItems.setVisibility(View.GONE);
                }

                productsRecycler.setAdapter(products_adapter);

                //DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");


                int numsize = grosamontdto.size();
                for (int i = 0; i < numsize; i++) {
                    listobj = grosamontdto.get(i);
                    String title = listobj.getTitle();
                    String code = listobj.getCode();

                    if (code.equals("gross_amount")) {

                        try {
                            Grossamount_strdto = Double.parseDouble(listobj.getValue());
                        }catch (Exception e){

                        }

                        // Log.e(TAG,"Grossamount_strdtoDDD "+Grossamount_strdto);

                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
                            grosstotal.setText(Utility.getPatternFormat(""+numberPostion, Grossamount_strdto));
                        } else {
                            grosstotal.setText(Utility.getPatternFormat(""+numberPostion, Grossamount_strdto) + currency_codedto);
                        }
                    } else if (code.equals("sub_total")) {

                        try {
                            Subtotalamountdto = Double.parseDouble(listobj.getValue());
                        }catch (Exception e){

                        }

                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
                            subtotal.setText(Utility.getPatternFormat(""+numberPostion, Subtotalamountdto));
                        } else {
                            subtotal.setText(Utility.getPatternFormat(""+numberPostion, Subtotalamountdto) + currency_codedto);
                        }

                    } else if (code.equals("total")) {

                        try {
                            Netamountvaluedto = Double.parseDouble(listobj.getValue());
                        }catch (Exception e){

                        }

                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
                            netamount.setText(Utility.getPatternFormat(""+numberPostion, Netamountvaluedto));
                        } else {
                            netamount.setText(Utility.getPatternFormat(""+numberPostion, Netamountvaluedto) + currency_codedto);
                        }
                    } else if (code.equals("paid_amount")) {

                        try {
                            Paidamountstrdto = Double.parseDouble(listobj.getValue());
                        }catch (Exception e){

                        }

                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
                            paidamount.setText(Utility.getPatternFormat(""+numberPostion, Paidamountstrdto));
                        } else {
                            paidamount.setText(Utility.getPatternFormat(""+numberPostion, Paidamountstrdto) + currency_codedto);
                        }

                    } else if (code.equals("remaining_balance")) {

                        try {
                            Blanceamountstrdto = Double.parseDouble(listobj.getValue());
                        }catch (Exception e){

                        }

                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
                            balance.setText(Utility.getPatternFormat(""+numberPostion, Blanceamountstrdto));
                        } else {
                            balance.setText(Utility.getPatternFormat(""+numberPostion, Blanceamountstrdto) + currency_codedto);
                        }

                    }

                    else if (code.equals("discount")) {

                        try {
                            Discountamountstrdto = Double.parseDouble(listobj.getValue().replace("-" , ""));
                        }catch (Exception e){

                        }

                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
                            discount.setText("-"+Utility.getPatternFormat(""+numberPostion, Discountamountstrdto));
                        } else {
                            discount.setText("-"+Utility.getPatternFormat(""+numberPostion, Discountamountstrdto) + currency_codedto);
                        }

                    }


                    else if (code.equals("shipping")) {

                        try {
                            Shippingamountdto = Double.parseDouble(listobj.getValue());
                        }catch (Exception e){

                        }

                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
                            freight.setText(Utility.getPatternFormat(""+numberPostion, Shippingamountdto));
                        } else {
                            freight.setText(Utility.getPatternFormat(""+numberPostion, Shippingamountdto) + currency_codedto);
                        }

                    }

                    else if (code.equalsIgnoreCase("tax")) {

                        try {
                            Tax_amountdto = Double.parseDouble(listobj.getValue());
                        }catch (Exception e){

                        }

                        tax_type = listobj.getTax_type();
                        taxrname = listobj.getTitle();
                        taxtypeclusive = listobj.getTax_type();
                        taxtrateamt = listobj.getRate();

                        txttax.setText(""+title.replace("(","").replace(")",""));

                        // Double taxVAL = Double.parseDouble(Tax_amountdto);

                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
                            tax.setText(Utility.getPatternFormat(""+numberPostion, Tax_amountdto));
                        } else {
                            tax.setText(Utility.getPatternFormat(""+numberPostion, Tax_amountdto) + currency_codedto);
                        }


                        Log.e(TAG, "taxtypeclusive "+taxtypeclusive);
                        Log.e(TAG, "taxtrateamt "+taxtrateamt);


                        String isTaxRate = taxtrateamt;
                        String isPecent = "%";

                        String subStrinng = taxrname.replace("(", "").replace(")", "");

//                        if(!subStrinng.contains(isTaxRate+isPecent)){
//                            subStrinng = taxrname.replace("(", "").replace(")", "") + " " + taxtrateamt + "%";
//                        }else{
//
//                        }


                        Log.e(TAG, "subStrinngAA "+subStrinng);
                        subStrinng = subStrinng.replaceAll("( )+", " ");

                        taxvalueText.setText("Tax (" + subStrinng + "" + ")");



                        SelectedTaxlist student = new SelectedTaxlist();

                        student.setTaxname(listobj.getTitle());
                        student.setTaxrate(listobj.getRate());
                        student.setTaxtype(tax_type);
                        student.setTaxamount(""+Tax_amountdto);

                        selectedtaxt.add(student);


                    }

                }



                productsItemDtosdto = new ArrayList<ProductsItemDto>(invoiceDtoInvoice.getProducts());
//                invoice_imageDto = new ArrayList<String>(data.getInvoiceImage());
                Log.e("product", productsItemDtosdto.toString());

                if (productsItemDtosdto.size() > 0) {
                    for (int i = 0; i < productsItemDtosdto.size(); i++) {
                        Product_list product_list = new Product_list();
                        product_list.setProduct_name(productsItemDtosdto.get(i).getName());
                        product_list.setProduct_id(productsItemDtosdto.get(i).getProductId());
                        product_list.setCurrency_code(currency_codedto);

                        product_list.setProduct_description(productsItemDtosdto.get(i).getDescription());

                        product_list.setProduct_measurement_unit(productsItemDtosdto.get(i).getMeasurementUnit());

                        product_list.setProduct_price(productsItemDtosdto.get(i).getTotal());
                        Log.e("price", productsItemDtosdto.get(i).getPrice());
                        Log.e("Quentity", productsItemDtosdto.get(i).getQuantity());
                        producprice.add(productsItemDtosdto.get(i).getPrice());
                        tempList.add(product_list);

                        tempQuantity.add(productsItemDtosdto.get(i).getQuantity());
                        total_price = total_price + (Double.parseDouble(productsItemDtosdto.get(i).getPrice()) * Double.parseDouble(productsItemDtosdto.get(i).getQuantity()));
                        totalpriceproduct.add(String.valueOf(total_price));

                        calculateTotalAmount(total_price);

                        Log.e("total_price", ""+String.valueOf(total_price));

                    }

                }

            }

            @Override
            public void onFailure(Call<ReceiptResponseDto> call, Throwable t) {

            }
        });

    }


    private void setFonts() {
        invoicenumtxt.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Light.otf"));
        invoicenum.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        duedatetxt.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Light.otf"));
        duedate.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        invoicetotxt.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Light.otf"));
        invoicerecipnt.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        itemstxt.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
        subtotaltxt.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        subtotal.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        discounttxt.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        discount.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        txttax.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        tax.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        additem.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        addservice.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        createinvoice.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/Ubuntu-Medium.ttf"));
        txtgrossamount.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        grosstotal.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        txtfreight.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        freight.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        txtnetamount.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        netamount.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        txtpaidamount.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        paidamount.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        txtbalance.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        balance.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        s_invoice.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Bold.otf"));
        s_receiver.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Bold.otf"));
        c_stamp.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Bold.otf"));
        attachmenttxt.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Bold.otf"));
        ednotes.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        txtcredit.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Light.otf"));
        txtdays.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        txtreferenceno.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Light.otf"));
        edreferenceno.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
        txtduedate.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Light.otf"));
        edduedate.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));


    }

    private void setListeners() {

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (selectwarehouseId.equals(""))
//                {
//                    Constant.ErrorToast(EditReceiptActivity.this, "Select A Where House");
//                } else {
                createbottomsheet_products();
                bottomSheetDialog.show();
                bottomSheetDialog2.dismiss();
//
//                }

            }
        });

        addservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createbottomsheet_services();
                bottomSheetDialog2.show();

            }
        });

        tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createbottomsheet_tax();
                bottomSheetDialog3.show();
            }
        });

        paidamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createbottomsheet_paid();
                bottomSheetDialog2.show();
            }
        });

        options.setEnabled(false);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createbottomsheet_dots();
                bottomSheetDialog2.show();
            }
        });
        txtdays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createbottomsheet_days();
                bottomSheetDialog.show();

            }
        });


        createinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                invoice_no = invoicenumtxt.getText().toString();
                //strnotes = ednotes.getText().toString();
                SpannableStringBuilder textNotes = (SpannableStringBuilder) ednotes.getText();
                strnotes = Html.toHtml(textNotes);

                ref_no = edreferenceno.getText().toString();

                strdiscountvalue = discount.getText().toString();
                strpaid_amount = paidamount.getText().toString();

                invoice_date = duedate.getText().toString();
                invoice_due_date = edduedate.getText().toString();
                invoicetaxamount = tax.getText().toString();
//                SelectedTaxlist student = new SelectedTaxlist();
//                student.setTaxamount(invoicetaxamount);
//                selectedtaxt.add(student);
                if (multimgpath != null) {
                    for (int i = 0; i < multimgpath.size(); i++) {
                        File imgFile = new File(multimgpath.get(i));
                        // company_stampFileimage=imgFile;
                        multiple[i] = imgFile;
                    }
                }

                if (attchmentimage != null) {
                    for (int i = 0; i < attchmentimage.size(); i++) {
                        File imgFile = new File(attchmentimage.get(i));
                        // company_stampFileimage=imgFile;
                        multiple[i] = imgFile;
                    }
                }

                viewPDF();
            }
        });


        freight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freightdialog();
            }
        });

        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discount_dialog();
            }
        });

        s_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_r = "2";
                createbottom_signaturepad();
                bottomSheetDialog.show();

            }
        });

        c_stamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission(false);

            }
        });

        attachmenttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                multiimagepicker();

            }
        });


        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);


                selectedItemOfMySpinner = selectcompany.getSelectedItemPosition();

                Log.e("selectedCompany", selectedCompanyId);
                warehouse_list(selectedCompanyId);
                productget(selectedCompanyId);
                serviceget(selectedCompanyId);
                customer_list(selectedCompanyId);
                CompanyInformation(selectedCompanyId);

            }
        });

        selectwarehouse.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectwarehouseId = cids.get(position);
                Log.e("selectwarehouseId", selectwarehouseId);

                warehousePosition = position;


            }
        });



        invoicerecipnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createbottomsheet_customers(); // ye method kia kr rha hai?//bottom sheet open kar rha
                //bottomSheetDialog.show();
                // final SharedPreferences pref = getContext().getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);
                //String customer_name = Constant.GetSharedPreferences(getActivity(),Constant.CUSTOMER_NAME);
                Moving moving = new Moving();
                customer_name = moving.getCus_name();

                //   Log.e("reccustomer",customer_name);
            }
        });


    }

    private void multiimagepicker() {
        multiImageDisposable = TedRxBottomPicker.with(this)
                //.setPeekHeight(getResources().getDisplayMetrics().heightPixels/2)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText(getString(R.string.done))
                .setEmptySelectionText(getString(R.string.noSelect))
                .setSelectMaxCount(5)
                .setSelectMinCount(1)

                .setSelectedUriList(selectedUriList)
                .showMultiImage()
                .subscribe(uris -> {
                    selectedUriList = uris;
                    showUriList(uris);
                }, Throwable::printStackTrace);

    }

    private void showUriList(List<Uri> uriList) {
        attchmentimage.clear();

        for (Uri uri : uriList) {
            attchmentimage.add(uri.toString());
            attachmenttxtimg.setVisibility(View.VISIBLE);
        }
        int sizen = attchmentimage.size();

        attachmenttxtimg.setVisibility(View.VISIBLE);
        String attchedmentimagepath;
        if (attchmentimage != null) {
            for (int i = 0; i < attchmentimage.size(); i++) {
                attchedmentimagepath = attchmentimage.get(i);
                try {

                    String decoded = URLDecoder.decode(attchedmentimagepath, "UTF-8");
                    String replaceString = decoded.replaceAll("file://", "");
                    multimgpath.add(replaceString);

                } catch (Exception e) {

                }

            }
        } else {

        }

        Log.e("uri Image ", String.valueOf(attchmentimage));

    }

    private void createinvoicewithdetail(File file) {
        avi.smoothToShow();
        if (customer_name.equals("")) {
            Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.dialog_SelectACustomer));
        } else if (Utility.getRealValue(invoicenum.getText().toString(), Utility.DEFAULT_RECEIPT).equalsIgnoreCase("")) {
            Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.dialog_ReceiptDigits));

        }else if (invoice_date.equals("")) {
            Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.dialog_SelectReceiptDate));

        } else if (selectedCompanyId.equals("")) {
            Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.dialog_SelectACompany));

//        } else if (selectwarehouseId.equals("")) {
//            Constant.ErrorToast(EditReceiptActivity.this, "Select A Where House");
//
//        } else if (credit_terms.equals("")) {
//            Constant.ErrorToast(EditReceiptActivity.this, "Select Credit Tearm");

        }

        else if (tempList.size() == 0) {
            Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.done));
        }

        else {

            final ProgressDialog progressDialog = new ProgressDialog(EditEditReceiptActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            RequestParams params = new RequestParams();
            params.add("receipt_id", invoiceId);
            params.add("receipt_no", invoicenumberdto);
            params.add("new_receipt_no", invoicenum.getText().toString());

            params.add("company_id", selectedCompanyId);
            params.add("wearhouse_id", selectwarehouseId);
            params.add("receipt_date", invoice_date);
            params.add("due_date", invoice_due_date);
            params.add("customer_id", customer_id);
            Log.e(TAG, "customer_idBB"+customer_id);

            Log.e(TAG, "strdiscountvalue "+strdiscountvalue);
            Log.e(TAG, "strpaid_amount "+strpaid_amount);

            params.add("notes", strnotes);
            params.add("ref_no", ref_no);
            params.add("paid_amount_payment_method", paymentmode);
            params.add("credit_terms", credit_terms);
            params.add("freight_cost", ""+shippingAmountZZ);
            params.add("discount", ""+discountAmountZZ);
            params.add("paid_amount", strpaid_amount);
            params.add("paid_amount_date", Paymentamountdate);
            params.add("shipping_firstname", shippingfirstname);
            params.add("shipping_lastname", shippinglastname);
            params.add("shipping_address_1", shippingaddress1);
            params.add("shipping_address_2", shippingaddress2);
            params.add("shipping_city", shippingcity);
            params.add("shipping_postcode", shippingpostcode);
            params.add("shipping_country", shippingcountry);
            params.add("payment_bank_name", payment_bank_name);
            params.add("payment_currency", payment_currency);
            params.add("payment_iban", payment_iban);
            params.add("payment_swift_bic", payment_swift_bic);


            params.add("template_type", ""+selectedTemplate);

            if (file!=null){
                try {
                    params.put("pdf",file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (company_logoFileimage != null) {
                try {
                    params.put("logo", company_logoFileimage);
                    //  Log.e("company stamp", company_stamp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (company_stampFileimage != null) {
                try {
                   // company_stampFileimage = Utility.getJPEGtoPNGCompanySeal(company_stampFileimage);
                    params.put("company_stamp", company_stampFileimage);
                    //  Log.e("company stamp", company_stamp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (signatureofinvoicemaker != null) {
                try {
                    params.put("signature_of_maker", signatureofinvoicemaker);
                    //  Log.e("signature_of_issuer", signatureofinvoicemaker.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (signaturinvoicereceiver != null) {
                try {
                    params.put("signature_of_receiver", signaturinvoicereceiver);
                    // Log.e("signature_of_issuer", signaturinvoicereceiver.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (company_logoFileimage != null) {
                try {
                    params.put("logo", company_logoFileimage);
                    // Log.e("signature_of_issuer", signaturinvoicereceiver.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < tempList.size(); i++) {

                Log.e("product id", tempList.get(i).getProduct_id());
                params.add("product[" + i + "]" + "[product_id]", tempList.get(i).getProduct_id());
                params.add("product[" + i + "]" + "[price]", producprice.get(i));
                params.add("product[" + i + "]" + "[quantity]", tempQuantity.get(i));


            }




            Log.e(TAG, "selectedtaxtAAA "+selectedtaxt.size());

            if (selectedtaxt.size() > 0) {
                for (int i = 0; i < selectedtaxt.size(); i++) {

                    //taxtypeclusive = selectedtaxt.get(i).getTaxtype();

                    Log.e(TAG, "selectedtaxtAAA1 "+selectedtaxt.get(i).getTaxtype());
                    Log.e(TAG, "selectedtaxtAAA2 "+selectedtaxt.get(i).getTaxrate());
                    Log.e(TAG, "selectedtaxtAAA3 "+selectedtaxt.get(i).getTaxname());
                    Log.e(TAG, "selectedtaxtAAA4 "+invoicetaxamount);
                    Log.e(TAG, "selectedtaxtAAA5 "+taxtypeclusive);

                    if(selectedtaxt.get(i).getRateType().equalsIgnoreCase("p")){
                        Log.e(TAG, "QQQQQQQQQQQ");
                        params.add("tax[" + i + "]" + "[type]", taxtypeclusive.toLowerCase());
                        // params.add("tax[" + i + "]" + "[amount]", Utility.getReplaceCurrency(invoicetaxamount, cruncycode));
                        params.add("tax[" + i + "]" + "[rate]", selectedtaxt.get(i).getTaxrate());
////                        params.add("tax[" + i + "]" + "[title]", "zz");
//
////                        if(selectedtaxt.get(i).getTaxname().length() > 0){
////                            if(selectedtaxt.get(i).getTaxname().contains(" ")){
////                                String firstTax = selectedtaxt.get(i).getTaxname().split(" ")[0].replace("(", "");
////                                Log.e(TAG, "firstTaxAAA5 "+firstTax);
////                                params.add("tax[" + i + "]" + "[title]", firstTax);
////                            }else{
//                                String isTaxRate = selectedtaxt.get(i).getTaxrate();
//                                String isPecent = "%";
//
//                                String subStrinng = selectedtaxt.get(i).getTaxname().replace("(", "").replace(")", "");
//
//                                if(!subStrinng.contains(isTaxRate+isPecent)){
//                                    subStrinng = selectedtaxt.get(i).getTaxname().replace("(", "").replace(")", "") + " " + selectedtaxt.get(i).getTaxrate() + "%";
//                                }else if(subStrinng.contains(isTaxRate+isPecent)){
//                                    subStrinng = selectedtaxt.get(i).getTaxname().replace("(", "").replace(")", "").replace(isTaxRate+isPecent, "");
//                                }
//
//
//        //                                txttax.setText(subStrinng+ " Incl"); //Dont do any change
//        //                                taxvalueText.setText("Tax (" + subStrinng + " Incl" + ")"); //Dont do any change
//
//                                    subStrinng = subStrinng.replace("incl." , "");
//                                    Log.e(TAG, "subStrinngAA "+subStrinng);
//                                    params.add("tax[" + i + "]" + "[title]", subStrinng);
////                            }
////                        }

                        String isTaxRate = selectedtaxt.get(i).getTaxrate();
                        String isPecent = "%";
                        String taxT1 = selectedtaxt.get(i).getTaxname().replace("(", "").replace(")", "");
                        taxT1 = taxT1.replace(isTaxRate, "");
                        taxT1 = taxT1.replace(isPecent, "");
                        taxT1 = taxT1.replace("incl." , "");
                        params.add("tax[" + i + "]" + "[title]", taxT1);
                        params.add("tax[" + i + "]" + "[amount]", ""+taxAmountZZ);
                    }else{
                        Log.e(TAG, "WWWWWWWWWWWWW");
                        params.add("tax[" + i + "]" + "[type]", taxtypeclusive.toLowerCase());
                        params.add("tax[" + i + "]" + "[amount]", ""+taxAmountZZ);
                        params.add("tax[" + i + "]" + "[rate]", selectedtaxt.get(i).getTaxrate());
////                        params.add("tax[" + i + "]" + "[title]", "xx");
//
////                        if(selectedtaxt.get(i).getTaxname().length() > 0){
////                            if(selectedtaxt.get(i).getTaxname().contains(" ")){
////                                String firstTax = selectedtaxt.get(i).getTaxname().split(" ")[0].replace("(", "");
////                                Log.e(TAG, "firstTaxAAA6 "+firstTax);
////                                params.add("tax[" + i + "]" + "[title]", firstTax);
////                            }else{
//                                String isTaxRate = selectedtaxt.get(i).getTaxrate();
//                                String isPecent = "%";
//
//                                String subStrinng = selectedtaxt.get(i).getTaxname().replace("(", "").replace(")", "");
//
//                                if(!subStrinng.contains(isTaxRate+isPecent)){
//                                    subStrinng = selectedtaxt.get(i).getTaxname().replace("(", "").replace(")", "") + " " + selectedtaxt.get(i).getTaxrate() + "%";
//                                }else if(subStrinng.contains(isTaxRate+isPecent)){
//                                    subStrinng = selectedtaxt.get(i).getTaxname().replace("(", "").replace(")", "").replace(isTaxRate+isPecent, "");
//                                }
//
//                                    subStrinng = subStrinng.replace("incl." , "");
//                                    Log.e(TAG, "subStrinngAA "+subStrinng);
//                                    params.add("tax[" + i + "]" + "[title]", subStrinng);
////                            }
////                        }
                        String isTaxRate = selectedtaxt.get(i).getTaxrate();
                        String isPecent = "%";
                        String taxT1 = selectedtaxt.get(i).getTaxname().replace("(", "").replace(")", "");
                        taxT1 = taxT1.replace(isTaxRate, "");
                        taxT1 = taxT1.replace(isPecent, "");
                        taxT1 = taxT1.replace("incl." , "");
                        params.add("tax[" + i + "]" + "[title]", taxT1);
                    }

                }
            } else {

            }




            if (multiple.length > 0) {
                for (int k = 0; k < multiple.length; k++) {
                    try {
                        params.add("invoice_image", "[" + k + "]");
                        params.put("fileName:", "invoice_image" + multiple[k] + ".jpg");
                        params.add("mimeType:", "image/jpeg");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            Log.e(TAG, "paramsSS "+params.toString());

            String token = Constant.GetSharedPreferences(this, Constant.ACCESS_TOKEN);
            Log.e("token", token);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.addHeader("Access-Token", token);
            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL + "receipt/update", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    avi.smoothToHide();
                    progressDialog.dismiss();
                    Log.e("Create Invoicedata", response);
                    try {
                        Log.e("Create Invoicedata", response);

                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("true")) {
                            Constant.SuccessToast(EditEditReceiptActivity.this, getString(R.string.receipt_updated_msg));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(EditEditReceiptActivity.this, ReceiptsActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }, 500);

                            JSONObject data = jsonObject.getJSONObject("data");
//
                        }

                        if (status.equals("false")) {
                            Constant.ErrorToast(EditEditReceiptActivity.this, jsonObject.getString("message"));

                            if( jsonObject.has("code")){
                                String code = jsonObject.getString("code");

                                if(code.equalsIgnoreCase("subscription")){
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(EditEditReceiptActivity.this, GoProActivity.class);
                                            startActivity(intent);
                                        }
                                    }, 1000);
                                }
                            }
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
                            if (status.equals("false")) {
                                Constant.ErrorToast(EditEditReceiptActivity.this, jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                       // Constant.ErrorToast(EditEditReceiptActivity.this, "Something went wrong, try again!");
                    }
                }
            });
        }

    }

    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(EditEditReceiptActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                gallaryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);

    }

    private void gallaryIntent() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.startActivityForResult(pickPhoto, GALLARY_aCTION_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_ACTION_PICK_CODE) {
                try {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri


                    fileimage = mCompressor.compressToFile(fileimage);
                    signature_of_receiver = getRealPathFromUri(selectedImageUri);
                    Log.e("Image Path", signature_of_receiver);
                } catch (Exception e) {
                    Log.e("FileSelectorActivity", "File select error", e);
                }
            } else if (requestCode == GALLARY_aCTION_PICK_CODE) {
                Uri selectedImage = data.getData();
                try {
                    // Get the path from the Uri


                    imgstampsuccess.setVisibility(View.VISIBLE);
                    company_stamp = getRealPathFromUri(selectedImage);

                    try {
                       // company_stampFileimage = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                        company_stampFileimage = Utility.getJPEGtoPNGCompanySeal(new File(company_stamp));
                        Log.e("company_stamp Path", company_stamp);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.e("company_stamp Path", company_stamp);
                } catch (Exception e) {
                    Log.e("FileSelectorActivity", "File select error", e);
                }
            }


        }


        if(requestCode == 121){

            selectedTemplate = pref.getTemplate();
            Log.e(TAG, "onResume selectedTemplate"+selectedTemplate);

            if(selectedTemplate != 0){
                itemstxtTemplate.setText(getString(R.string.header_template)+" "+selectedTemplate);
            }
        }




        if(requestCode == 123){
            Log.e(TAG, "requestCode "+requestCode);
            customer_list(selectedCompanyId);
        }

        if(requestCode == 124){
            Log.e(TAG, "requestCode "+requestCode);
            productget(selectedCompanyId);
        }

        if(requestCode == 125){
            Log.e(TAG, "requestCode "+requestCode);
            serviceget(selectedCompanyId);
        }

        if(requestCode == 126){
            Log.e(TAG, "requestCode "+requestCode);
            CompanyInformation(selectedCompanyId);
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                fileimage = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_ACTION_PICK_CODE);

            }
        }

    }

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    public void createbottomsheet_products() {
        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.products_bottom_sheet, null);
            txtproducts = view.findViewById(R.id.txtproduct);
            TextView add_product = view.findViewById(R.id.add_product_new);
            add_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditEditReceiptActivity.this, Product_Activity.class);
                    startActivityForResult(intent, 124);
                    bottomSheetDialog.dismiss();
                }
            });
            search = view.findViewById(R.id.search);
            recycler_products = view.findViewById(R.id.recycler_products);

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (product_bottom.size() > 0) {
                        filter(s.toString());
                    }
                }
            });


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recycler_products.setLayoutManager(layoutManager);
            recycler_products.setHasFixedSize(true);

            product_bottom_adapter = new Product_Bottom_Adapter(this, product_bottom, this, bottomSheetDialog, "receipt");
            recycler_products.setAdapter(product_bottom_adapter);
            product_bottom_adapter.notifyDataSetChanged();
            txtproducts.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
        }
    }

    public void createbottomsheet_services() {
        if (bottomSheetDialog2 != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.service_bottom_sheet, null);
            txtservices = view.findViewById(R.id.txtservices);
            search_service = view.findViewById(R.id.search_service);
            TextView add_service = view.findViewById(R.id.add_service_new);
            add_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditEditReceiptActivity.this, Service_Activity.class);
                    startActivityForResult(intent, 125);
                    bottomSheetDialog2.dismiss();
                }
            });
            recycler_services = view.findViewById(R.id.recycler_services);

            search_service.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {


                    if (service_bottom.size() > 0) {
                        filter2(s.toString());
                    }
                }
            });


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recycler_services.setLayoutManager(layoutManager);
            recycler_services.setHasFixedSize(true);

            service_bottom_adapter = new Service_bottom_Adapter(this, service_bottom, this);
            recycler_services.setAdapter(service_bottom_adapter);
            service_bottom_adapter.notifyDataSetChanged();


            txtservices.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            bottomSheetDialog2 = new BottomSheetDialog(this);
            bottomSheetDialog2.setContentView(view);
        }
    }



    public void createbottomsheet_tax() {
        if (bottomSheetDialog3 != null) {

            View view = LayoutInflater.from(this).inflate(R.layout.tax_bottom_itemlayout, null);

            TextView add_service_new = view.findViewById(R.id.add_service_new);
            add_service_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditEditReceiptActivity.this, Tax_Activity.class);
                    startActivityForResult(intent, 126);
                    bottomSheetDialog3.dismiss();
                }
            });

            taxrecycler = view.findViewById(R.id.taxrecycler);
            taxswitch = view.findViewById(R.id.taxswitch);
            btndone = view.findViewById(R.id.taxbtndone);

            if(taxtypeclusive.equalsIgnoreCase("Inclusive")){
                taxswitch.setChecked(true);
            }else{
                taxswitch.setChecked(false);
            }


            imgincome = view.findViewById(R.id.txttax);
            taxswitch.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            imgincome.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            btndone.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            taxrecycler.setLayoutManager(layoutManager);
            taxrecycler.setHasFixedSize(true);

            customTaxAdapter = new CustomTaxAdapter(this, tax_list_array, this);
            taxrecycler.setAdapter(customTaxAdapter);
            customTaxAdapter.notifyDataSetChanged();

            customTaxAdapter.updateTaxSelect(taxID);

            btndone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String statusSwitch1;
                    if (taxswitch.isChecked()) {
                        statusSwitch1 = taxswitch.getTextOn().toString();
                        taxtypeclusive = "Inclusive";
                    } else {
                        statusSwitch1 = taxswitch.getTextOn().toString();
                        taxtypeclusive = "Exclusive";
                    }
                    //selectedtaxt.clear();
                    boolTax = true;
                    calculateTotalAmount(total_price);
                    bottomSheetDialog3.dismiss();

                }
            });
            bottomSheetDialog3 = new BottomSheetDialog(this);
            bottomSheetDialog3.setContentView(view);
        }
    }




    public void createbottomsheet_paid() {
        if (bottomSheetDialog2 != null) {

            View view = LayoutInflater.from(this).inflate(R.layout.paidamount_bottom_sheet, null);
            txtpaid = view.findViewById(R.id.txtpaid);
            txtamount = view.findViewById(R.id.txtamount);
            txtdate = view.findViewById(R.id.txtdate);
            edamount = view.findViewById(R.id.edamount);
            eddate = view.findViewById(R.id.eddate);
            btndone2 = view.findViewById(R.id.btndone2);
            selectpaymentmode = view.findViewById(R.id.selectpaymentmode);
            paymode.clear();
            paymode.add(getString(R.string.dropCash));
            paymode.add(getString(R.string.dropCheque));
            paymode.add(getString(R.string.dropBank));
            paymode.add(getString(R.string.dropCreditCard));
            paymode.add(getString(R.string.dropPaypal));
            paymode.add(getString(R.string.dropOthers));
            ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymode);
            selectpaymentmode.setAdapter(namesadapter);


            selectpaymentmode.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
                @Override
                public void onItemSelected(int position, String itemAtPosition) {
                    paimentmodespinerstr = paymode.get(position);
                    paymentmode = paimentmodespinerstr;

                }
            });
            txtpaid.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            txtamount.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            txtdate.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            edamount.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Light.otf"));
            eddate.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Light.otf"));
            btndone2.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            eddate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mYear, mMonth, mDay;
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditEditReceiptActivity.this,
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

                                    eddate.setText(year + "-" + realMonth + "-" + realDay);

                                }
                            }, mYear, mMonth, mDay);
                    //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                }
            });

            btndone2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paidamountstr = edamount.getText().toString();
                    String paiddate = eddate.getText().toString();

                    Paymentamountdate = paiddate;

                    if (paidamountstr.isEmpty()) {
                        edamount.setError(getString(R.string.dialog_Required));
                        edamount.requestFocus();
                    } else if (paiddate.isEmpty()) {
                        Constant.ErrorToastTop(EditEditReceiptActivity.this, getString(R.string.dialog_DateRequired));
                    } else if (paimentmodespinerstr.equals("")) {
                        Constant.ErrorToastTop(EditEditReceiptActivity.this, getString(R.string.dialog_PaymentModeRequired));
                    } else {
                        if (paidamountstr != null) {
                            paidamount.setText(paidamountstr);
                            calculateTotalAmount(total_price);
                            bottomSheetDialog2.dismiss();
                        }
                    }


                }
            });
            bottomSheetDialog2 = new BottomSheetDialog(EditEditReceiptActivity.this);
            bottomSheetDialog2.setContentView(view);
        }
    }

    public void createbottomsheet_dots() {
        if (bottomSheetDialog2 != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dots_bottomsheet, null);
            btnviewinvoice = view.findViewById(R.id.btnviewinvoice);
            btnviewinvoice.setText(getString(R.string.dialog_ViewRecepit));
            btnclear = view.findViewById(R.id.btnclear);
            btndotcancel = view.findViewById(R.id.btndotcancel);

            btnviewinvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    avi.smoothToShow();

                    // fright cost
                    String shipingcoast = freight.getText().toString();
                    Subtotalamount = subtotal.getText().toString();
                    Grossamount_str = grosstotal.getText().toString();
                    invoice_date = duedate.getText().toString();
                    invoice_due_date = edduedate.getText().toString();
                    reference_no = edreferenceno.getText().toString();
                    netamountvalue = netamount.getText().toString();
                    Blanceamountstr = balance.getText().toString();

                    ref_no = edreferenceno.getText().toString();

                    strdiscountvalue = discount.getText().toString();
                    strpaid_amount = paidamount.getText().toString();

                    invoice_date = duedate.getText().toString();
                    invoice_due_date = edduedate.getText().toString();
                    invoicetaxamount = tax.getText().toString();

                    //strnotes = ednotes.getText().toString();
                    SpannableStringBuilder textNotes = (SpannableStringBuilder) ednotes.getText();
                    strnotes = Html.toHtml(textNotes);

                    if (selectedCompanyId.equals("")) {
                        Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.dialog_SelectACompany));
                        bottomSheetDialog2.dismiss();
                    } else if (invoice_date.equals("")) {
                        Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.dialog_SelectReceiptDate));
                        bottomSheetDialog2.dismiss();
                    } else if (customer_name.equals("")) {
                        Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.dialog_SelectACustomer));
                        bottomSheetDialog2.dismiss();
//                    } else if (credit_terms.equals("")) {
//                        Constant.ErrorToast(EditReceiptActivity.this, "Select Credit Tearm");
//                        bottomSheetDialog2.dismiss();
//                    } else if (selectwarehouseId.equals("")) {
//                        Constant.ErrorToast(EditReceiptActivity.this, "Select A Where House");
//                        bottomSheetDialog2.dismiss();
                    }

                    else if (tempList.size() == 0) {
                        Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.dialog_SelectProductFirst));
                        bottomSheetDialog2.dismiss();
                    }


                    else {
                        Customer_list customer_lists = selected.get(0);
                        Log.e(TAG, "shippingfirstnameAA "+customer_lists.getShipping_firstname());

                        Intent intent = new Intent(EditEditReceiptActivity.this, ViewReceipt_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("company_id", selectedCompanyId);
                        intent.putExtra("taxText", txttax.getText().toString());
                        intent.putExtra("companycolor", companycolor);
                        intent.putExtra("selectedTemplate", ""+selectedTemplate);

                        intent.putExtra("invoice_date", invoice_date);
                        intent.putExtra("netamount", netamountvalue);

                        intent.putExtra("invoicetaxamount", invoicetaxamount);
                        intent.putExtra("blanceamount", Blanceamountstr);

                        intent.putExtra("subtotalamt", Subtotalamount);
                        intent.putExtra("due_date", invoice_due_date);
                        intent.putExtra("customer_id", customer_id);
                        intent.putExtra("grossamount", Grossamount_str);
//                        intent.putExtra("invoice_no", invoicenumberdto);
                        intent.putExtra("invoice_no", invoicenum.getText().toString());
                        intent.putExtra("notes", strnotes);
                        intent.putExtra("ref_no", ref_no);
                        intent.putExtra("paid_amount_payment_method", paymentmode);
                        intent.putExtra("credit_terms", credit_terms);
                        intent.putExtra("freight_cost", shipingcoast);
                        intent.putExtra("paypal_emailstr", paypal_emailstr);
                        intent.putExtra("company_name", Selectedcompanyname);
                        intent.putExtra("company_logo", companylogopath);
                        intent.putExtra("company_name", Selectedcompanyname);
                        intent.putExtra("company_address", company_address);
                        intent.putExtra("company_contact", company_contact);
                        intent.putExtra("company_email", company_email);
                        intent.putExtra("company_website", company_website);
                        intent.putExtra("discount", strdiscountvalue);
                        intent.putExtra("paid_amount", strpaid_amount);
                        intent.putExtra("paid_amount_date", Paymentamountdate);
                        intent.putExtra("payment_bank_name", payment_bank_name);
                        intent.putExtra("payment_currency", payment_currency);
                        intent.putExtra("payment_iban", payment_iban);
                        intent.putExtra("payment_swift_bic", payment_swift_bic);
                        intent.putExtra("producprice", producprice);
                        intent.putExtra("totalpriceproduct", totalpriceproduct);

                        intent.putExtra("signature_of_receiver", signatureofreceiverst);
                        Log.e(TAG, "signatureofreceiverst::: "+signatureofreceiverst);
                        //Log.e(TAG, "signature_of_receiver::: "+signatureofreceiverst);

                        Log.e(TAG, "company_stamp::: "+company_stamp);
                        intent.putExtra("company_stamp", company_stamp);
                        intent.putExtra("signature_issuer", signature_of_issuer);
                        intent.putExtra("reference_no", reference_no);
                        // intent.putStringArrayListExtra("products_list",products);
                        intent.putExtra("tempQuantity", tempQuantity);
                        intent.putExtra("tempList", tempList);
                        intent.putExtra("customerselected", selected);
                        intent.putExtra("customerinfo", customerinfo);
                        intent.putExtra("quantity_list", quantity);
                        intent.putExtra("rate_list", rate);
                        intent.putExtra("attchemnt", attchmentimage);


                        startActivity(intent);
                        bottomSheetDialog2.dismiss();
                        avi.hide();
                    }

                }
            });
            btnclear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    duedate.setText("");
                    invoicenum.setText("");
                    invoicerecipnt.setText("");
                    txtdays.setText("");
                    edreferenceno.setText("");
                    edduedate.setText("");
                    tempList.clear();
                    quantity.clear();
                    producprice.clear();
                    cnames.clear();
                    cnames.clear();
                    grosstotal.setText("");
                    discount.setText("");
                    subtotal.setText("");
                    tax.setText("");
                    freight.setText("");
                    netamount.setText("");
                    paidamount.setText("");
                    balance.setText("");
                    ednotes.setText("");
                    imgsigsuccess.setVisibility(View.INVISIBLE);
                    imgrecsuccess.setVisibility(View.INVISIBLE);
                    imgstampsuccess.setVisibility(View.INVISIBLE);
                    attachmenttxtimg.setVisibility(View.INVISIBLE);

                    if(tempList.size() == 0){
                        textViewNoItems.setVisibility(View.VISIBLE);
                    }else{
                        textViewNoItems.setVisibility(View.GONE);
                    }

                    products_adapter.notifyDataSetChanged();

                    bottomSheetDialog2.dismiss();
                }
            });
            btndotcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog2.dismiss();
                }
            });


            btnviewinvoice.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            btnclear.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            btndotcancel.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));

            bottomSheetDialog2 = new BottomSheetDialog(EditEditReceiptActivity.this);
            bottomSheetDialog2.setContentView(view);
        }
    }

    public void freightdialog() {
        final Dialog mybuilder = new Dialog(this);
        mybuilder.setContentView(R.layout.freight_amount_dialog);

        TextView txtfreight, txtfreightdes;
        EditText edfreight;
        Button btnok, btncancel;

        txtfreight = mybuilder.findViewById(R.id.txtfreight);
        edfreight = mybuilder.findViewById(R.id.edfreight);
        txtfreightdes = mybuilder.findViewById(R.id.txtfreightdes);
        txtfreightdes.setText("Do you want to add Shipping amount for\nthis Receipt?");
        btnok = mybuilder.findViewById(R.id.btnok);
        btncancel = mybuilder.findViewById(R.id.btncancel);


        edfreight.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
        btnok.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
        btncancel.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
        txtfreight.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
        txtfreightdes.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));


        mybuilder.show();
        mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = mybuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.transparent);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sipingvalue;
                freight_cost = edfreight.getText().toString();
                try{
                    Shippingamountdto = Double.parseDouble(freight_cost);
                }catch (Exception e){

                }
                calculateTotalAmount(total_price);
                mybuilder.dismiss();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mybuilder.dismiss();
            }
        });

    }


    public void discount_dialog() {
        final Dialog mybuilder = new Dialog(this);
        mybuilder.setContentView(R.layout.discount_dialog);

        TextView txtdiscount;
        RadioGroup radiogroup;
        RadioButton radiopercent, radioamount;
        EditText eddisount;
        Button btndone;

        txtdiscount = mybuilder.findViewById(R.id.txtdiscount);
        radiogroup = mybuilder.findViewById(R.id.radiogroup);
        radiopercent = mybuilder.findViewById(R.id.radiopercent);
        radioamount = mybuilder.findViewById(R.id.radioamount);
        eddisount = mybuilder.findViewById(R.id.eddisount);
        btndone = mybuilder.findViewById(R.id.btndone);

        txtdiscount.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
        radiopercent.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
        radioamount.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
        eddisount.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
        btndone.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));

        mybuilder.show();
        mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = mybuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.transparent);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    strdiscount = rb.getText().toString();
                    Log.e("Radio Button value", strdiscount);
                    if(strdiscount.equalsIgnoreCase(getString(R.string.dialog_Percentage))){
                        eddisount.setHint(getString(R.string.dialog_EnterDiscountinPercent));
                    }
                    if(strdiscount.equalsIgnoreCase(getString(R.string.html_Amount))){
                        eddisount.setHint(getString(R.string.dialog_EnterDiscountinAmount));
                    }

                }

            }
        });

        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strdiscountvalue = eddisount.getText().toString();
                try {
                    discountAmountDD = Double.parseDouble(eddisount.getText().toString());
                }catch (Exception e){

                }
                if (strdiscountvalue.matches("")) {
                    // Toast.makeText(EditReceiptActivity.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                    mybuilder.dismiss();
                    return;
                } else {
                    calculateTotalAmount(total_price);
                    mybuilder.dismiss();
                }

            }
        });

    }

    public void createbottomsheet_days() {
        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.days_itemlayout, null);
            txtcredit1 = view.findViewById(R.id.txtcredit);
            radiogroup1 = view.findViewById(R.id.radiogroup1);
            radiogroup2 = view.findViewById(R.id.radiogroup2);
            rnone = view.findViewById(R.id.rnone);
            r3days = view.findViewById(R.id.r3days);
            r14days = view.findViewById(R.id.r14days);
            r30days = view.findViewById(R.id.r30days);
            r60days = view.findViewById(R.id.r60days);
            r120days = view.findViewById(R.id.r120days);
            rimmediately = view.findViewById(R.id.rimmediately);
            r7days = view.findViewById(R.id.r7days);
            r21days = view.findViewById(R.id.r21days);
            r45days = view.findViewById(R.id.r45days);
            r90days = view.findViewById(R.id.r90days);
            r365days = view.findViewById(R.id.r365days);
            txtor = view.findViewById(R.id.txtor);
            btndone1 = view.findViewById(R.id.btndone);
            edmanual = view.findViewById(R.id.edmanual);

            radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {

                        credit_terms = rb.getText().toString();

                    }

                }
            });
            radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {

                        credit_terms = rb.getText().toString();

                    }

                }
            });

            edmanual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edmanual.setClickable(radiogroup1.getCheckedRadioButtonId() != -1 || radiogroup2.getCheckedRadioButtonId() != -1);
                    credit_terms = "";
                    radiogroup2.clearCheck();
                    radiogroup1.clearCheck();
                }
            });

            btndone1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dayss = edmanual.getText().toString();

                    if (dayss.equals("") && credit_terms.equals("")) {
                        Toast.makeText(EditEditReceiptActivity.this, getString(R.string.dialog_PleaseSelectAtleastOne), Toast.LENGTH_LONG).show();
                    } else if (dayss != null && credit_terms.equals("")) {

                        String dayswith = dayss.trim();

                        Double daysvalue = Double.parseDouble(dayswith);

                        Double resultday = toMilliSeconds(daysvalue);
                        long sum = (long) (resultday + datemillis);
                        // Creating date format
                        DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");

                        // Creating date from milliseconds
                        // using Date() constructor
                        Date result = new Date(sum);
                        Log.e("Date Long", simple.format(result));
                        edduedate.setText(simple.format(result));
                        edduedate.setClickable(true);
                        txtdays.setText(dayss + " " + getString(R.string.dialog_days));
                        bottomSheetDialog.dismiss();
                        edduedate.setClickable(false);
                    } else if (credit_terms != null && dayss.equals("")) {
                        if (credit_terms.equals(getString(R.string.dialog_DateNone))) {
                            txtdays.setText(credit_terms);
                            edduedate.setClickable(true);
                            bottomSheetDialog.dismiss();

                            edduedate.setText(duedate.getText().toString());

                        } else if (credit_terms.equals(getString(R.string.dialog_immediately))) {
                            String myFormat = "yyyy-MM-dd"; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                            edduedate.setText(sdf.format(myCalendar.getTime()));
                            edduedate.setClickable(false);
                            txtdays.setText(credit_terms);

                            bottomSheetDialog.dismiss();
                        } else {


                            String replaceString = credit_terms.replaceAll(getString(R.string.dialog_days), "");
                            String dayswith = replaceString.trim();
                            Double daysvalue = Double.parseDouble(dayswith);

                            Double result = toMilliSeconds(daysvalue);

                            long sumresult = (long) (result + datemillis);
                            // Creating date format
                            DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                            Date sumresultdate = new Date(sumresult);

                            // Formatting Date according to the
                            // given format

                            Log.e("Date Long", simple.format(sumresultdate));
                            edduedate.setText(simple.format(sumresultdate));
                            edduedate.setClickable(false);
                            txtdays.setText(credit_terms);
                            bottomSheetDialog.dismiss();
                        }


                    } else if (dayss != null && credit_terms != null) {
                        Toast.makeText(EditEditReceiptActivity.this, getString(R.string.dialog_PleaseSelectOneValue), Toast.LENGTH_LONG).show();

                    }

                    credit_terms = txtdays.getText().toString();
                }

                private Double toMilliSeconds(Double days) {
                    return days * 24 * 60 * 60 * 1000;
                }
            });


            txtcredit1.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Bold.otf"));
            txtor.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Bold.otf"));
            rnone.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            r3days.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            r14days.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            r30days.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            r60days.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            r120days.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            rimmediately.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            r7days.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            r21days.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            r45days.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            r90days.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            r365days.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            edmanual.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            btndone1.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
        }
    }

    private void updateLabe21() {

        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edduedate.setText(sdf.format(myCalendar.getTime()));
    }

    public void createbottom_signaturepad() {
        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.signaturebottom_itemlayout, null);
            signaturePad = view.findViewById(R.id.signaturepad);
            btnclear1 = view.findViewById(R.id.btnclear);
            btnsave = view.findViewById(R.id.btnsave);

            signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
                @Override
                public void onStartSigning() {
                    // Toast.makeText(getContext(), "OnStartSigning", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSigned() {

                    btnsave.setEnabled(true);
                    btnclear1.setEnabled(true);
                }

                @Override
                public void onClear() {
                    btnsave.setEnabled(false);
                    btnclear1.setEnabled(false);
                }
            });

            btnclear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signaturePad.clear();
                    bottomSheetDialog.dismiss();
                }
            });

            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                    if (addSignatureToGallery(signatureBitmap)) {
                        //  Toast.makeText(getContext(), "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        imgrecsuccess.setVisibility(View.VISIBLE);
                    } else {
                        //Toast.makeText(getContext(), "Unable to store the signature", Toast.LENGTH_SHORT).show();
                    }
                    if (addSvgSignatureToGallery(signaturePad.getSignatureSvg())) {
                        //  Toast.makeText(getContext(), "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        imgrecsuccess.setVisibility(View.VISIBLE);
                    } else {
                        //Toast.makeText(getContext(), "Unable to store the signature", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditEditReceiptActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    public File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not Created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newbitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newbitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newbitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addSignatureToGallery(Bitmap signature) {
        boolean result = false;


        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
            if (s_r.equals("1")) {
                signatureofinvoicemaker = photo;
                signature_of_issuer = photo.getAbsolutePath();
                imgsigsuccess.setVisibility(View.VISIBLE);

            }
            if (s_r.equals("2")) {
                signaturinvoicereceiver = photo;
                signatureofreceiverst = photo.getAbsolutePath();
                imgrecsuccess.setVisibility(View.VISIBLE);
            }

//            Log.e(TAG, "signature_of_issuer"+signatureofreceiverst);

            Log.e("Signature Of Issuer", signature_of_issuer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    private void scanMediaFile(File photo) {
        Intent MediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contenturi = Uri.fromFile(photo);

        MediaScanIntent.setData(contenturi);
        this.sendBroadcast(MediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = true;
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        duedate.setText(sdf.format(myCalendar.getTime()));
        try {
            String myFormat1 = ("yyyy/MM/dd HH:mm:ss"); //In which you need put here
            SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat1, Locale.US);

            String myDate = sdf2.format(myCalendar.getTime());
            Log.e("Date Long", myDate);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = sdf1.parse(myDate);
            datemillis = date.getTime();

        } catch (Exception e) {

        }

    }

    private void updateLabe2() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edduedate.setText(sdf.format(myCalendar.getTime()));
    }

    public void companyget() {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        cnames.clear();

        cids.clear();
        String token = Constant.GetSharedPreferences(this, Constant.ACCESS_TOKEN);
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
                Log.e("responsecompany", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray company = data.getJSONArray("company");
                        if (company.length() > 0) {
                            for (int i = 0; i < company.length(); i++) {
                                JSONObject item = company.getJSONObject(i);
                                company_id = item.getString("company_id");
                                company_name = item.getString("name");

                                companycolor = item.getString("color");

                                Log.e(TAG , "companycolor:: "+companycolor);

                                cnames.add(company_name);
                                cids.add(company_id);



                            }
                        }

                        warehousePosition = wids.indexOf(selectwarehouseId);

                        ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(EditEditReceiptActivity.this, android.R.layout.simple_spinner_item, cnames);
                        selectcompany.setAdapter(namesadapter);

                        selectwarehouse.setSelection(warehousePosition);
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

    public void warehouse_list(String selectedCompanyId) {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        wids.clear();
        wnames.clear();

        RequestParams params = new RequestParams();
        if (this.selectedCompanyId.equals("") || this.selectedCompanyId.equals("null")) {
            Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.select_company));
        } else {
            params.add("company_id", this.selectedCompanyId);
            String token = Constant.GetSharedPreferences(EditEditReceiptActivity.this, Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.addHeader("Access-Token", token);
            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL + "warehouse/listing", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    String response = new String(responseBody);
                    //Log.e("warehouseResp", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("true")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray warehouse = data.getJSONArray("warehouse");
                            for (int i = 0; i < warehouse.length(); i++) {
                                JSONObject item = warehouse.getJSONObject(i);
                                String warehouse_id = item.getString("warehouse_id");
                                String company_id = item.getString("company_id");
                                String warehouse_name = item.getString("name");

                                wids.add(warehouse_id);
                                wnames.add(warehouse_name);

                                Log.e("warehouseNames",wnames.toString());

                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(EditEditReceiptActivity.this, android.R.layout.simple_spinner_item, wnames);
                                selectwarehouse.setAdapter(namesadapter);


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
                        //  Log.e("responsevendorF", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("false")) {
                               //Constant.ErrorToast(EditEditReceiptActivity.this, "No Warehouse Found");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }


    public void productget(String selectedCompanyId) {
        product_bottom.clear();
        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);

        String token = Constant.GetSharedPreferences(EditEditReceiptActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "product/getListingByCompany", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String(responseBody);
                Log.e("response product", response);

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
                                String currency_code = item.getString("currency_symbol");
                                String quantity = item.getString("quantity");

                                String measurement_unit = item.getString("measurement_unit");

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
                                product_list.setCurrency_code(currency_code);
                                product_list.setQuantity(quantity);
                                product_list.setMinimum(minimum);
                                String product_type = item.getString("product_type");
                                product_list.setProduct_type(product_type);

                                product_bottom.add(product_list);


                            }
                        } else {
                            Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.dialog_ProductNotFound));
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
                    //  Log.e("responseproductF", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            // Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Constant.ErrorToast(EditEditReceiptActivity.this, "Something went wrong, try again!");
                }


            }
        });
    }

    void filter(String text) {
        ArrayList<Product_list> temp = new ArrayList();
        for (Product_list d : product_bottom) {
            if (d.getProduct_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        product_bottom_adapter.updateList(temp);
    }

    public void serviceget(String selectedCompanyId) {
        service_bottom.clear();
        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);

        String token = Constant.GetSharedPreferences(this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "service/getListingByCompany", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String(responseBody);
                Log.e("responseservice", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray service = data.getJSONArray("service");
                        if (service.length() > 0) {
                            for (int i = 0; i < service.length(); i++) {
                                JSONObject item = service.getJSONObject(i);
                                String service_id = item.getString("product_id");
                                String company_id = item.getString("company_id");
                                String service_name = item.getString("name");
                                String service_price = item.getString("price");
                                String service_description = item.getString("description");
                                String service_taxable = item.getString("is_taxable");
                                String currency_code = item.getString("currency_code");
                                String category = item.getString("category");
                                String measurement_unit = item.getString("measurement_unit");
                                String service_quantity = item.getString("quantity");

                                //String service_category = item.getString("category");
                                String service_price_unit = item.getString("currency_symbol");


                                Service_list service_list = new Service_list();

                                service_list.setService_id(service_id);
                                service_list.setCompany_id(company_id);
                                service_list.setService_name(service_name);
                                service_list.setService_price(service_price);
                                service_list.setService_description(service_description);
                                service_list.setService_taxable(service_taxable);
                                service_list.setCuurency_code(currency_code);
                                service_list.setService_category(category);
                                service_list.setMeasurement_unit(measurement_unit);
                                service_list.setService_quantity(service_quantity);
                                service_list.setService_price_unit(service_price_unit);

                                service_bottom.add(service_list);


                            }
                        } else {
                            Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.dialog_ProductNotFound));
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
                    Log.e("responseserviceF", response);

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
                    //Constant.ErrorToast(EditEditReceiptActivity.this, "Something went wrong, try again!");
                }


            }
        });
    }

    void filter2(String text) {
        ArrayList<Service_list> temp = new ArrayList();
        for (Service_list d : service_bottom) {
            if (d.getService_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        service_bottom_adapter.updateList(temp);
    }

    public void createbottomsheet_customers() {

        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.customer_bottom_sheet, null);
            txtcustomer = view.findViewById(R.id.txtcustomer);
            search_customers = view.findViewById(R.id.search_customers);
            TextView add_customer = view.findViewById(R.id.add_customer);
            add_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditEditReceiptActivity.this, Customer_Activity.class);
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
                        filter3(s.toString());
                    }
                }
            });

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recycler_customers.setLayoutManager(layoutManager);
            recycler_customers.setHasFixedSize(true);

            customer_bottom_adapter = new Customer_Bottom_Adapter(this, customer_bottom, this);
            recycler_customers.setAdapter(customer_bottom_adapter);
            customer_bottom_adapter.notifyDataSetChanged();


            txtcustomer.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            // txtname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));
            // txtcontactname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));

            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);

            bottomSheetDialog.show();


        }
    }

    private void CompanyInformation(String selectedCompanyId) {
        tax_list_array.clear();
        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);
        params.add("product", "1");
        params.add("service", "1");
        params.add("customer", "1");
        params.add("tax", "1");
        params.add("receipt", "1");
        params.add("warehouse", "1");


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
                Log.e("Company Information", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String image_path = data.getString("customer_image_path");
                        Log.e("imagepath customer", image_path);
                        Invoiceno = data.getString("invoice_count");

                        invoicenovalue = Integer.parseInt(Invoiceno) + 1;
                  /*      if (Invoiceno != null) {

                            invoicenum.setText("Inv # " + invoicenovalue);

                            Log.e("imagepath customer", String.valueOf(invoicenovalue));

                        }*/
                        String company_image_path = data.getString("company_image_path");
                        Log.e("company_image_path", company_image_path);

                        JSONArray company = data.getJSONArray("company");

                        for (int i = 0; i < company.length(); i++) {
                            JSONObject item = company.getJSONObject(i);

                            String logo = item.getString("logo");
                            Selectedcompanyname = item.getString("name");
                            company_address = item.getString("address");
                            company_contact = item.getString("phone_number");
                            company_email = item.getString("email");
                            company_website = item.getString("website");
                            payment_bank_name = item.getString("payment_bank_name");
                            payment_currency = item.getString("payment_currency");
                            payment_iban = item.getString("payment_iban");
                            payment_swift_bic = item.getString("payment_swift_bic");
                            paypal_emailstr = item.getString("paypal_email");
                            companylogopath = company_image_path + logo;
                            Log.e("companylogopath", companylogopath);

                            companycolor = item.getString("color");

                            options.setEnabled(true);

                        }

                        JSONArray customer = data.getJSONArray("customer");

                        JSONArray invoice = data.getJSONArray("receipt");
                        for (int i = 0; i < invoice.length(); i++) {
                            JSONObject item = invoice.getJSONObject(i);
                            String invoice_nocompany = item.getString("receipt_no");

                            /* invoicenum.setText(invoice_nocompany);*/
//                            if (invoice_nocompany != null) {
//                                Log.e("invoice no", invoice_nocompany);
//                            }

//                            if(i == invoice.length() - 1){
//                                Log.e(TAG, "zzzz0 "+invoice_nocompany);
//                                Log.e(TAG, "zzzz1 "+i);
//                                Log.e(TAG, "zzzz2 "+invoice.length());
//
//                                String sss = getRealValue(invoice_nocompany);
//                                invoicenum.setText(sss);
//
//                                // invoicenum.setEnabled(true);
//
//                            }

                        }


//                        for (int i = 0; i < customer.length(); i++) {
//                            JSONObject item = customer.getJSONObject(i);

                        tax_list_array.clear();

                        tax_list_array = new ArrayList<Tax_List>();

                        JSONArray tax_list = data.getJSONArray("tax");

                        for (int j = 0; j < tax_list.length(); j++) {
                            JSONObject jsonObject1 = tax_list.getJSONObject(j);
//                                String name = jsonObject1.getString("name");
                            Tax_List student = new Tax_List();
                            student.setTax_id(jsonObject1.getString("tax_id"));
                            student.setTax_name(jsonObject1.getString("name"));
                            student.setCompany_name(jsonObject1.getString("company_name"));
                            student.setTax_rate(jsonObject1.getString("rate"));
                            student.setCompanyid(jsonObject1.getString("company_id"));
                            student.setType(jsonObject1.getString("type"));
                            student.setTax_name(jsonObject1.getString("name"));
                            tax_list_array.add(student);


                        }
                        Log.e("Taxt array", tax_list_array.toString());
                    }


//                    }
                    if (status.equals("false")) {
                        Constant.ErrorToast(EditEditReceiptActivity.this, jsonObject.getString("message"));
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
                            Constant.ErrorToast(EditEditReceiptActivity.this, jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Constant.ErrorToast(EditEditReceiptActivity.this, "Something went wrong, try again!");
                }
            }
        });


    }

    public void customer_list(String selectedCompanyId) {
        customer_bottom.clear();
        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);


        String token = Constant.GetSharedPreferences(this, Constant.ACCESS_TOKEN);
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

                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject item = customer.getJSONObject(i);
                            String customer_idBB = item.getString("customer_id");
                            Log.e(TAG, "customer_idBB "+customer_idBB);
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

                            customer_list.setCustomer_id(customer_idBB);
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
                   // Constant.ErrorToast(EditEditReceiptActivity.this, "Something went wrong, try again!");
                }
            }
        });
    }

    void filter3(String text) {
        ArrayList<Customer_list> temp = new ArrayList();
        for (Customer_list d : customer_bottom) {
            if (d.getCustomer_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        customer_bottom_adapter.updateList(temp);
    }

    @Override
    public void onPostExecute(Customer_list customer_list) {
        customerinfo.add(customer_list);
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();

            // createbottomsheet_customers();
            invoicerecipnt.setText(customer_list.getCustomer_name());
            customer_name = customer_list.getCustomer_name();
            customer_id = customer_list.getCustomer_id();
        } else {
            // createbottomsheet_customers();
            invoicerecipnt.setText(customer_list.getCustomer_name());
            customer_name = customer_list.getCustomer_name();
            customer_id = customer_list.getCustomer_id();
        }
        selected.add(customer_list);

    }


    @Override
    public void onPostExecutecall(Product_list selected_item, String s, String price) {

//        Double propice = Double.parseDouble(price);
//        int propriceint = new BigDecimal(propice).setScale(0, RoundingMode.HALF_UP).intValueExact();
//        producprice.add(String.valueOf(propriceint));
//        tempList.add(selected_item);
//        tempQuantity.add(s);
//        total_price = total_price + (Double.parseDouble(price) * Double.parseDouble(s));
//
//
//        totalpriceproduct.add(String.valueOf(total_price));
//        calculateTotalAmount(total_price);
//
//        products_adapter.notifyDataSetChanged();
//
//        bottomSheetDialog.dismiss();


        bottomSheetDialog.dismiss();

        producprice.add(price);
        tempList.add(selected_item);
        tempQuantity.add(s);

        Log.e(TAG, "tempQuantityAA "+s);
        total_price = total_price + (Double.parseDouble(price) * Double.parseDouble(s));

        double newPrice = Double.parseDouble(price) * Double.parseDouble(s);

        totalpriceproduct.add(String.valueOf(newPrice));
        calculateTotalAmount(total_price);

        products_adapter.notifyDataSetChanged();


    }






    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("SetTextI18n")
    private void calculateTotalAmount(Double total_price) {
        Log.e(TAG,  "total_priceSSSS "+total_price);

        if(tempList.size() == 0){
            textViewNoItems.setVisibility(View.VISIBLE);
        }else{
            textViewNoItems.setVisibility(View.GONE);
        }

       // DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        //  double Grossamount_strdto = 0.0,  Discountamountstrdto = 0.0,  Subtotalamountdto = 0.0, Tax_amountdto = 0.0,
        //  Shippingamountdto = 0.0, Netamountvaluedto = 0.0, Paidamountstrdto = 0.0, Blanceamountstrdto = 0.0;


//        double balanceamount = 0.0;
//        Double netamountvalue = 0.0;
//        Double Totatlvalue = 0.0;
//        Double subtotalvalue = 0.0;
//

        double grandAmount = 0.0;
        double discountAmount = 0.0;
        double subtotalAmount = 0.0;
        double taxAmount = 0.0;
        double afterTaxAmount = 0.0;
        double shippingAmount = 0.0;
        double netAmount = 0.0;

        if (tempList.size() > 0) {

            Log.e(TAG, "strdiscount "+strdiscount);
            Log.e(TAG, "strdiscountvalue "+strdiscountvalue);
            Log.e(TAG, "Grossamount_strdtoCCC "+Grossamount_strdto);
            Log.e(TAG, "DiscountamountstrdtoCCC "+Discountamountstrdto);
            Log.e(TAG, "SubtotalamountdtoCCC "+Subtotalamountdto);
            Log.e(TAG, "Tax_amountdtoCCC "+Tax_amountdto);
            Log.e(TAG, "ShippingamountdtoCCC "+Shippingamountdto);
            Log.e(TAG, "NetamountvaluedtoCCC "+Netamountvaluedto);
            Log.e(TAG, "PaidamountstrdtoCCC "+Paidamountstrdto);
            Log.e(TAG, "BlanceamountstrdtoCCC "+Blanceamountstrdto);

            Log.e(TAG, "taxtypeclusive "+taxtypeclusive);
            Log.e(TAG, "BlanceamountstrdtoCCC "+Blanceamountstrdto);

            String cruncycode = tempList.get(0).getCurrency_code();

            grandAmount = total_price;


            if (strdiscount.equalsIgnoreCase(getString(R.string.dialog_Percentage))) {
                double value = grandAmount * discountAmountDD / 100;
                discountAmount = value;
            } else if (strdiscount.equalsIgnoreCase(getString(R.string.dialog_Amount))) {
                double value = discountAmountDD;
                discountAmount = value;
            }else{
                double value = Discountamountstrdto;
                discountAmount = value;
            }



            subtotalAmount = grandAmount - discountAmount;

            afterTaxAmount = subtotalAmount;

            Log.e(TAG, "afterTaxAmountAAA "+afterTaxAmount);

            Log.e(TAG, "selectedtaxt "+selectedtaxt.size());
            Log.e(TAG, "taxtrateamt "+taxtrateamt);


            Log.e(TAG, "Tax_amountdto "+Tax_amountdto);
            //Log.e(TAG, "Tax_amountdto "+Tax_amountdto);



            if(boolTax == false){
                if (selectedtaxt.size() > 0) {
                    // taxAmount = Tax_amountdto;
                    if (taxtypeclusive.equalsIgnoreCase("Inclusive")) { // exclude on
                        double taxAm = 0.0;
                        try{
                            taxAm = Double.parseDouble(taxtrateamt);
                        }catch (Exception e){

                        }

                        if(taxAm != 0){
                            taxAmount = taxAm * subtotalAmount / ( 100 + taxAm);
                        }


                        afterTaxAmount = subtotalAmount;
//                        afterTaxAmount = subtotalAmount;
//                        taxAmount = Tax_amountdto;
//                    String subStrinng = taxrname + " " + taxtrateamt + "%";
//                    txttax.setText(  subStrinng + " incl." );
//                    taxvalueText.setText("Tax (" + subStrinng + " incl." + ")"); //Dont do any change
                    } else { // include off
                        double taxAm = 0.0;
                        try{
                            taxAm = Double.parseDouble(taxtrateamt);
                        }catch (Exception e){

                        }

                        taxAmount = subtotalAmount * taxAm / 100;
                        afterTaxAmount = subtotalAmount + taxAmount;
//                        afterTaxAmount = subtotalAmount + taxAmount;
//                        taxAmount = Tax_amountdto;
//                    String subStrinng = taxrname + " " + taxtrateamt + "%";
//                    txttax.setText(  subStrinng + "" );
//                    taxvalueText.setText("Tax (" + subStrinng + " " + ")"); //Dont do any change
                    }
                }
            }else{
                if (taxtypeclusive.equalsIgnoreCase("Inclusive")) { // exclude on
                    if(!taxtrateamt.equalsIgnoreCase("")){
                        taxAmount = Double.parseDouble(taxtrateamt) * subtotalAmount / (100+ Double.parseDouble(taxtrateamt));
                        afterTaxAmount = subtotalAmount;
//                    String subStrinng = taxrname + " " + taxtrateamt + "%";
                        String subStrinng = taxrname.replace("(", "").replace(")", "");
                        subStrinng = subStrinng.replace(taxtrateamt, "");
                        subStrinng = subStrinng.replace("%", "");
                        subStrinng = subStrinng.replace("incl." , "");
                        subStrinng = subStrinng + " " + taxtrateamt + "%";
                        subStrinng = subStrinng.replaceAll("( )+", " ");
                        txttax.setText(  subStrinng + " incl." );

                        taxvalueText.setText("Tax (" + subStrinng + " incl." + ")");

                        Log.e(TAG, "taxrnameQQQQ1 "+taxrname);
                    }
                } else {
                    if(!taxtrateamt.equalsIgnoreCase("")){
                        taxAmount = subtotalAmount * Double.parseDouble(taxtrateamt) / 100;
                        afterTaxAmount = subtotalAmount + taxAmount;

                        String subStrinng = taxrname.replace("(", "").replace(")", "");
                        subStrinng = subStrinng.replace(taxtrateamt, "");
                        subStrinng = subStrinng.replace("%", "");
                        subStrinng = subStrinng.replace("incl." , "");
                        subStrinng = subStrinng + " " + taxtrateamt + "%";
                        subStrinng = subStrinng.replaceAll("( )+", " ");
                        txttax.setText(  subStrinng + "" );

                        taxvalueText.setText("Tax (" + subStrinng + "" + ")");

                        Log.e(TAG, "taxrnameQQQQ2 "+taxrname);
                    }

                }
            }





            double value = Shippingamountdto;
            shippingAmount = value;


            Log.e(TAG, "afterTaxAmount "+afterTaxAmount);
            Log.e(TAG, "taxAmount "+taxAmount);
            Log.e(TAG, "shippingAmount "+shippingAmount);

            netAmount  = afterTaxAmount + shippingAmount;




            Log.e(TAG, "grandAmount "+grandAmount);
            Log.e(TAG, "discountAmount "+discountAmount);
            Log.e(TAG, "subtotalAmount "+subtotalAmount);
            Log.e(TAG, "taxAmount "+taxAmount);
            Log.e(TAG, "shippingAmount "+shippingAmount);
            Log.e(TAG, "netAmount "+netAmount);


            if(grandAmount == 0){
                grosstotal.setText("0");
            }else{
                grosstotal.setText(Utility.getPatternFormat(""+numberPostion, grandAmount)+""+cruncycode);
            }

            if(discountAmount == 0){
                discount.setText("0");
            }else{
                discount.setText("-"+Utility.getPatternFormat(""+numberPostion, discountAmount)+""+cruncycode);
            }

            if(subtotalAmount == 0){
                subtotal.setText("0");
            }else{
                subtotal.setText(Utility.getPatternFormat(""+numberPostion, subtotalAmount)+""+cruncycode);
            }

            if(taxAmount == 0){
                tax.setText("0");
            }else{
                tax.setText(Utility.getPatternFormat(""+numberPostion, taxAmount)+""+cruncycode);
            }

            if(shippingAmount == 0){
                freight.setText("0");
            }else{
                freight.setText(Utility.getPatternFormat(""+numberPostion, shippingAmount)+""+cruncycode);
            }

            if(netAmount == 0){
                netamount.setText("0");
            }else{
                netamount.setText(Utility.getPatternFormat(""+numberPostion, netAmount)+""+cruncycode);
            }


//            grandAmountZZ = grandAmount;
//            discountAmountZZ = discountAmount;
//            subtotalAmountZZ = subtotalAmount;
//            taxAmountZZ = taxAmount;
//            afterTaxAmountZZ = afterTaxAmount;
//            shippingAmountZZ = shippingAmount;
//            netAmountZZ = netAmount;

            DecimalFormat formatter = new DecimalFormat("#0.00");
            Log.e(TAG , "subtotalAmountZZ "+formatter.format(subtotalAmount));

            grandAmountZZ = Double.parseDouble(formatter.format(grandAmount));
            discountAmountZZ = Double.parseDouble(formatter.format(discountAmount));
            subtotalAmountZZ = Double.parseDouble(formatter.format(subtotalAmount));
            taxAmountZZ = Double.parseDouble(formatter.format(taxAmount));
            afterTaxAmountZZ = Double.parseDouble(formatter.format(afterTaxAmount));
            shippingAmountZZ = Double.parseDouble(formatter.format(shippingAmount));
            netAmountZZ = Double.parseDouble(formatter.format(netAmount));
//            paidAmountZZ = Double.parseDouble(formatter.format(paidAmount));
//            balanceAmountZZ = Double.parseDouble(formatter.format(balanceAmount));

        } else {

            grandAmount = 0.0;
            discountAmount = 0.0;
            subtotalAmount = 0.0;
            taxAmount = 0.0;
            afterTaxAmount = 0.0;
            shippingAmount = 0.0;
            netAmount = 0.0;

            grandAmountZZ = 0.0;
            discountAmountZZ = 0.0;
            subtotalAmountZZ = 0.0;
            taxAmountZZ = 0.0;
            afterTaxAmountZZ = 0.0;
            shippingAmountZZ = 0.0;
            netAmountZZ = 0.0;

            grosstotal.setText("0");
            subtotal.setText("0");
            discount.setText("0");
            freight.setText("0");
            paidamount.setText("0");
            netamount.setText("0");
            tax.setText("0");
            balance.setText("0");

        }

    }





    @Override
    public void onPostExecutecall2(Service_list selected_item, String s, String price) {
        //producprice.add(String.valueOf(price));

        Product_list product_list = new Product_list();
        product_list.setProduct_name(selected_item.getService_name());
        product_list.setProduct_id(selected_item.getService_id());
        product_list.setCurrency_code(selected_item.getService_price_unit());

        product_list.setProduct_description(selected_item.getService_description());

        product_list.setProduct_measurement_unit(selected_item.getMeasurement_unit());

        product_list.setProduct_price(selected_item.getService_price());

//        producprice.add(selected_item.getService_price());
//        tempList.add(product_list);
//        tempQuantity.add(s);
//
//        total_price = total_price + (Double.parseDouble(price) * Double.parseDouble(s));
//
//
//        totalpriceproduct.add(String.valueOf(total_price));
//        calculateTotalAmount(total_price);
//
//        products_adapter.notifyDataSetChanged();
//
//        bottomSheetDialog2.dismiss();

        producprice.add(price);
        tempList.add(product_list);
        tempQuantity.add(s);

        Log.e(TAG, "tempQuantityAA "+s);
        total_price = total_price + (Double.parseDouble(price) * Double.parseDouble(s));

        double newPrice = Double.parseDouble(price) * Double.parseDouble(s);

        totalpriceproduct.add(String.valueOf(newPrice));
        calculateTotalAmount(total_price);

        products_adapter.notifyDataSetChanged();

        bottomSheetDialog2.dismiss();
    }

    @Override
    public void onClick(int str,String type) {

        if (type.equalsIgnoreCase("del"))
        {
            producprice.remove(str);
            tempList.remove(str);
            tempQuantity.remove(str);
            totalpriceproduct.remove(str);

            Log.e(TAG, "producprice.size() "+producprice.size());
            Log.e(TAG, "tempList.size() "+tempList.size());
            Log.e(TAG, "tempQuantity.size() "+tempQuantity.size());
            Log.e(TAG, "totalpriceproduct.size() "+totalpriceproduct.size());

            products_adapter.notifyDataSetChanged();

            double total_price2 = 0;

            if(tempList.size() > 0){
                if(tempQuantity.size() > 0){
                    for(int i = 0 ; i < tempList.size() ; i++){
                        Log.e(TAG, "ccDDD "+producprice.get(i) + " DDDD "+ tempQuantity.get(i));
                        total_price2 = total_price2 + Double.parseDouble(producprice.get(i)) * Double.parseDouble(tempQuantity.get(i));
                    }
                }
            }

            Log.e(TAG, "productCal "+total_price2);

            if(tempList.size() == 0) {
                total_price2 = 0.0;
            }

            total_price = total_price2;

            calculateTotalAmount(total_price);

        }
        else
        {
            final Dialog mybuilder=new Dialog(this);
            mybuilder.setContentView(R.layout.product_itemlayout);

            TextView txtprice;
            final EditText edquantity,edprice;
            Button btnok,btncancel;

            txtprice=mybuilder.findViewById(R.id.txtprice);
            edquantity=mybuilder.findViewById(R.id.edquantity);
            edprice=mybuilder.findViewById(R.id.edprice);
            btnok=mybuilder.findViewById(R.id.btnok);
            btncancel=mybuilder.findViewById(R.id.btncancel);


            edprice.setText(producprice.get(str));
            edquantity.setText(tempQuantity.get(str));


            edquantity.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));
            btnok.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));
            btncancel.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));
            txtprice.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));
            edprice.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));


            mybuilder.show();
            mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            Window window = mybuilder.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(R.color.transparent);

            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mybuilder.dismiss();


                    if(edprice.getText().toString().length() == 0){
                        Constant.ErrorToast(EditEditReceiptActivity.this,getString(R.string.select_Please_enter_amount));
                    }else if(edquantity.getText().toString().length() == 0){
                        Constant.ErrorToast(EditEditReceiptActivity.this,getString(R.string.select_Please_enter_quantity));
                    }else{
                        double en_quantity = Double.parseDouble(edquantity.getText().toString());

                        double sh_quantity = 0;
                        double sh_price = 0.0;



                        ItemQuantity itemQuantity = Utility.getQuantityByProductId(product_bottom, tempList.get(str).getProduct_id());
                        Log.e(TAG, "itemQuantityAA "+itemQuantity.getEn_quantity());
                        Log.e(TAG, "itemQuantityBB "+itemQuantity.getProduct_type());

                        if(itemQuantity.getProduct_type().equalsIgnoreCase("PRODUCT")) {
//                            if (itemQuantity.getEn_quantity() <= en_quantity) {
//                                mybuilder.show();
//                                Constant.ErrorToast(EditEditReceiptActivity.this, getString(R.string.invoice_InsufficientQuantityAvailable));
//                                mybuilder.dismiss();
//                            } else {
                                sh_price = Double.parseDouble(edprice.getText().toString());
                                double multiply = en_quantity * sh_price;
                                String s_multiply = String.valueOf(multiply);



                                producprice.remove(str);
                                totalpriceproduct.remove(str);
                                tempQuantity.remove(str);

                                producprice.add(str, String.valueOf(sh_price));
                                totalpriceproduct.add(str, String.valueOf(sh_price));
                                tempQuantity.add(str, edquantity.getText().toString());


                                double dd = 0.0;
                                for (int i = 0; i < producprice.size(); i++){
                                    double aa = Double.parseDouble(producprice.get(i));
                                    double bb = Double.parseDouble(tempQuantity.get(i));

                                    double cc = aa * bb;
                                    dd = dd + cc;
                                }
                                total_price = dd;


                                calculateTotalAmount(total_price);
                                products_adapter.notifyDataSetChanged();

                                mybuilder.dismiss();
                            }
//                        }

                        else
                        {
                            sh_price = Double.parseDouble(edprice.getText().toString());
                            double multiply = en_quantity * sh_price;
                            String s_multiply = String.valueOf(multiply);



                            producprice.remove(str);
                            totalpriceproduct.remove(str);
                            tempQuantity.remove(str);

                            producprice.add(str, String.valueOf(sh_price));
                            totalpriceproduct.add(str, String.valueOf(sh_price));
                            tempQuantity.add(str, edquantity.getText().toString());


                            double dd = 0.0;
                            for (int i = 0; i < producprice.size(); i++){
                                double aa = Double.parseDouble(producprice.get(i));
                                double bb = Double.parseDouble(tempQuantity.get(i));

                                double cc = aa * bb;
                                dd = dd + cc;
                            }
                            total_price = dd;


                            calculateTotalAmount(total_price);
                            products_adapter.notifyDataSetChanged();

                            mybuilder.dismiss();
                        }

                    }

                }
            });



            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mybuilder.dismiss();
                }
            });
        }

    }

    @Override
    public void onPostExecutecall3(String taxID, String taxnamst, String taxnamss, String type) {
//        selectedtaxt.clear();
//        Log.e("income rate", taxnamst);
//        Log.e("taxnamss_rate", taxnamss);
//        Log.e("type rate", type);
//        taxtype = type;
//        taxrname = taxnamst;
//        taxtrateamt = taxnamss;
//        SelectedTaxlist student = new SelectedTaxlist();
//        student.setTaxname(taxnamst);
//        student.setTaxrate(taxnamss);
//        student.setTaxtype(type);
//        student.setTaxamount(taxnamss);
//        selectedtaxt.add(student);

        selectedtaxt.clear();
        Log.e(TAG, "income rate" + taxnamst);
        Log.e("taxnamss_rate", taxnamss);
        Log.e("type rate", type);
        this.taxID = taxID;
        Log.e(TAG, "taxID" + taxID);

        taxtype = type;
        taxrname = taxnamst;
        taxtrateamt = taxnamss;
        SelectedTaxlist student = new SelectedTaxlist();

        //student.setTaxID(taxID);
        student.setTaxname(taxnamst);
        student.setTaxrate(taxnamss);
        student.setTaxtype(type);
        student.setTaxamount(taxnamss);
        selectedtaxt.add(student);


    }

    private class DownloadCompanystempweb extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(EditEditReceiptActivity.this);
        @Override
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0].replace("https", "http"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Create Path to save Image
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/AndroidDvlpr"); //Creates app specific folder

            if (!path.exists()) {
                path.mkdirs();
            }


            File imageFile = new File(path, System.currentTimeMillis() + ".png"); // Imagename.png

            company_stampFileimage = imageFile;
            company_stamp = imageFile.getAbsolutePath();
            String imagepath = imageFile.getAbsolutePath();
            Log.e("companystemp", imagepath);

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
                out.flush();
                out.close();
                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(EditEditReceiptActivity.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        // Log.i("ExternalStorage", "Scanned " + path + ":");
                        //    Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.dialog_Please_wait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            //  Toast.makeText(ConvertToReceiptsActivity.this,"Image Is save",Toast.LENGTH_LONG).show();
        }
    }

    private class Downloadsignatureissueweb extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(EditEditReceiptActivity.this);
        @Override
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0].replace("https", "http"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Create Path to save Image
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/AndroidDvlpr"); //Creates app specific folder

            if (!path.exists()) {
                path.mkdirs();
            }

            File imageFile = new File(path, System.currentTimeMillis() + ".png"); // Imagename.png

//            if (s_r.equals("1")) {
            signatureofinvoicemaker = imageFile;
            signature_of_issuer = imageFile.getAbsolutePath();
//            }
//            if (s_r.equals("2")) {
//                signaturinvoicereceiver = imageFile;
//                signatureofreceiverst = imageFile.getAbsolutePath();
//            }

            String imagepath = imageFile.getAbsolutePath();
            Log.e("save image", imagepath);

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
                out.flush();
                out.close();
                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(EditEditReceiptActivity.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        // Log.i("ExternalStorage", "Scanned " + path + ":");
                        //    Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.dialog_Please_wait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            //  Toast.makeText(ConvertToReceiptsActivity.this,"Image Is save",Toast.LENGTH_LONG).show();
        }
    }


    private class Downloadsignaturereceiverweb extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(EditEditReceiptActivity.this);
        @Override
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0].replace("https", "http"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Create Path to save Image
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/AndroidDvlpr"); //Creates app specific folder

            if (!path.exists()) {
                path.mkdirs();
            }

            File imageFile = new File(path, System.currentTimeMillis() + ".png"); // Imagename.png

//            if (s_r.equals("1")) {
//                signaturinvoicereceiver = imageFile;
//                signature_of_issuer = imageFile.getAbsolutePath();
//            }
//            if (s_r.equals("2")) {
            signaturinvoicereceiver = imageFile;
            signatureofreceiverst = imageFile.getAbsolutePath();
//            }

            String imagepath = imageFile.getAbsolutePath();
            Log.e("save image", imagepath);

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
                out.flush();
                out.close();
                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(EditEditReceiptActivity.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        // Log.i("ExternalStorage", "Scanned " + path + ":");
                        //    Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.dialog_Please_wait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            //  Toast.makeText(ConvertToReceiptsActivity.this,"Image Is save",Toast.LENGTH_LONG).show();
        }
    }




    private class DownloadsImagefromweblogoCom extends AsyncTask<String, Void, Void> {

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0].replace("https", "http"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Create Path to save Image
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/AndroidDvlpr"); //Creates app specific folder

            if (!path.exists()) {
                path.mkdirs();
            }


            File imageFile = new File(path, System.currentTimeMillis() + ".png"); // Imagename.png

            company_logoFileimage = imageFile;
            company_stamp = imageFile.getAbsolutePath();
            String imagepath = imageFile.getAbsolutePath();
            Log.e("DownloadsImagefromweblogoCom", "DownloadsImagefromweblogoCom"+imagepath);

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
                out.flush();
                out.close();
                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(EditEditReceiptActivity.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        // Log.i("ExternalStorage", "Scanned " + path + ":");
                        //    Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //  Toast.makeText(ConvertToReceiptsActivity.this,"Image Is save",Toast.LENGTH_LONG).show();

        }
    }


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//        Log.e(TAG, "onRestart selectedTemplate"+defaultClick);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.e(TAG, "onResume selectedTemplate"+defaultClick);
//
//        if(defaultClick == 1){
//                SavePref pref = new SavePref();
//                pref.SavePref(editInvoiceActivity.this);
//
//                selectedTemplate = pref.getTemplate();
//                Log.e(TAG, "onResume selectedTemplate"+selectedTemplate);
//
//                if(selectedTemplate != 0){
//                    itemstxtTemplate.setText("Template "+selectedTemplate);
//                }
//            }
//
//    }



//
//    static String extractInt(String str)
//    {
//        // Replacing every non-digit number
//        // with a space(" ")
//        str = str.replaceAll("[^\\d]", " ");
//
//        // Remove extra spaces from the beginning
//        // and the ending of the string
//        str = str.trim();
//
//        // Replace all the consecutive white
//        // spaces with a single space
//        str = str.replaceAll(" +", " ");
//
//        if (str.equals(""))
//            return "-1";
//
//        return str;
//    }
//
//
//    public static boolean isNumeric(String str)
//    {
//        for (char c : str.toCharArray())
//        {
//            if (!Character.isDigit(c)) return false;
//        }
//        return true;
//    }
//
//
//    public static boolean isChar(String str)
//    {
//        for (char c : str.toCharArray())
//        {
//            if (Character.isDigit(c)) return false;
//        }
//        return true;
//    }
//
//
//
//
//    private String getRealValue(String sss) {
//        String valueIs = "";
//        if(sss.toString().length() > 0){
//
//            // char cc = invoicenum.getText().toString().charAt(invoicenum.getText().toString().length() - 1);
//
//            boolean gg = isNumeric(sss);
//            Log.e(TAG, "gggggg "+gg);
//
//            boolean dd = isChar(sss);
//            Log.e(TAG, "dddddd "+dd);
//
//            if(gg == false && dd == false){
//                Log.e(TAG, "truee ");
//                Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
//                Log.e(TAG, "cccccc "+flag);
//                if(flag == true){
//                    String str = sss;
//                    String cc = extractInt(str);
//                    if(cc.contains(" ")){
//                        String vv[] = cc.split(" ");
//                        String ii =  vv[vv.length - 1];
//                        Log.e(TAG , "extractInt "+ii);
//                        String vvvvv = sss.substring(0, sss.length() - ii.length());
//
//                        Log.e(TAG , "vvvvv "+vvvvv);
//
//                        int myValue = Integer.parseInt(ii)+1;
//                        valueIs = vvvvv+myValue;
//                    }
//                    if(!cc.contains(" ")){
//                        Log.e(TAG , "extractInt2 "+cc);
//                        int myValue = Integer.parseInt(cc)+1;
//                        String vvvvv = sss.substring(0, sss.length() - cc.length());
//
//                        Log.e(TAG , "bbbbbb "+vvvvv);
//                        valueIs = vvvvv+myValue;
//                    }
//                }
//            }else{
//                boolean ddd = isChar(sss);
//                if(ddd == false){
//                    int myValue = Integer.parseInt(sss)+1;
//                    valueIs = "Rec # "+myValue;
//                }
//            }
//        }
//        return valueIs;
//    }
//
//
//
//
//
//    private boolean getTrueValue(String toString) {
//        boolean b = false;
//        if(toString.length() > 0){
//            boolean gg = isNumeric(toString.toString());
//            Log.e(TAG, "gggggg "+gg);
//
//            boolean dd = isChar(toString.toString());
//            Log.e(TAG, "dddddd "+dd);
//
//            if(gg == false && dd == false){
//                Log.e(TAG, "truee ");
//                Boolean flag = Character.isDigit(toString.toString().charAt(toString.toString().length() - 1));
//                Log.e(TAG, "cccccc "+flag);
//                if(flag == true){
//                    String str = toString.toString();
//                    String cc = extractInt(str);
//                    if(cc.contains(" ")){
//                        String vv[] = cc.split(" ");
//                        Log.e(TAG , "extractInt "+vv[vv.length - 1]);
//                        b = true;
//                    }
//                    if(!cc.contains(" ")){
//                        Log.e(TAG , "extractInt2 "+cc);
//                        b = true;
//                    }
//                }
//            }else{
//                Log.e(TAG, "falsee ");
//                b = false;
//            }
//        }
//        return b;
//    }
//
//
//
//




    String  sltcustonername = "", sltcustomer_email = "", sltcustomer_contact = "", sltcustomer_address = "", sltcustomer_website = "", sltcustomer_phone_number = "";
    // String  taxamount = "";
    String shippingzone = "";

    String cruncycode = "", Shiping_tostr = "", companyimagelogopath = "";

    String Shipingdetail = "";

    String encodedImage, signature_of_receiverincode, encodesignatureissure, drableimagebase64, attchmentbase64;
    String paid_amount_payment;



    //////////////
    private void viewPDF() {

        drableimagebase64 = "iVBORw0KGgoAAAANSUhEUgAAAC4AAAAnCAYAAABwtnr/AAAAAXNSR0IArs4c6QAAADhlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAAqACAAQAAAABAAAALqADAAQAAAABAAAAJwAAAAB8SmRPAAAAeklEQVRYCe3SQQrAIBTEUPX+d67iCbIIBSGuw/B57fzOGw++9eDN9+QO//vLJZ44FOhXgVBalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYcSh1BalrhGCYc2r3IESll5TkQAAAAASUVORK5CYII=";

        stringBuilderBillTo.delete(0,stringBuilderBillTo.length());
        stringBuilderShipTo.delete(0,stringBuilderShipTo.length());

        if (selected.size() > 0) {
            for (int i = 0; i < selected.size(); i++) {
                sltcustonername = selected.get(i).getCustomer_name();
                sltcustomer_address = selected.get(i).getCustomer_address();
                sltcustomer_email = selected.get(i).getCustomer_email();
                sltcustomer_website = selected.get(i).getCustomer_website();
                sltcustomer_phone_number = selected.get(i).getCustomer_phone();
                sltcustomer_contact = selected.get(i).getCustomer_contact_person();


                shippingfirstname = selected.get(i).getShipping_firstname();
                shippinglastname = selected.get(i).getShipping_lastname();
                shippingaddress1 = selected.get(i).getShipping_address_1();
                shippingaddress2 = selected.get(i).getShipping_address_2();
                shippingcity = selected.get(i).getShipping_city();
                shippingcountry = selected.get(i).getShipping_country();
                shippingpostcode = selected.get(i).getShipping_postcode();
                shippingzone = selected.get(i).getShipping_zone();

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



        if (tempQuantity.size() > 0) {
            for (int i = 0; i < tempQuantity.size(); i++) {
                Log.e("value", tempQuantity.get(i));
            }
        }

        for (int i = 0; i < tempList.size(); i++) {
            Log.e("product[" + i + "]" + "[price]", producprice.get(i));
            Log.e("product[" + i + "]" + "[quantity]", tempQuantity.get(i));
            Log.e("product[" + i + "]" + "[product_id]", tempList.get(i).getProduct_id());

        }


        if(paymentmode != null){
            if (paymentmode.equals("")) {
                paid_amount_payment = "";
            } else {
                paid_amount_payment = paymentmode;

            }
        }


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

                Log.e("Byte array Image", encodedImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            signature_of_receiverincode = drableimagebase64;

        }




        for (int i = 0; i < quantity.size(); i++) {
            String qty = String.valueOf(quantity);
            Log.e("logqty", qty);
        }




        view_invoice();
    }






    String attchedmentimagepath;

    String Shipingcosstbyct = "";

    String hiddenpaidrow = "";

//    String paid_amount_payment;

    public void view_invoice() {

        Grossamount_str = grosstotal.getText().toString();

        String shipingcoast = freight.getText().toString();
        Subtotalamount = subtotal.getText().toString();
        Grossamount_str = grosstotal.getText().toString();
        invoice_date = duedate.getText().toString();
        invoice_due_date = edduedate.getText().toString();
        reference_no = edreferenceno.getText().toString();
        netamountvalue = netamount.getText().toString();
        Blanceamountstr = balance.getText().toString();
        invoice_no = invoicenumtxt.getText().toString();
        //strnotes = ednotes.getText().toString();

        SpannableStringBuilder textNotes = (SpannableStringBuilder) ednotes.getText();
        strnotes = Html.toHtml(textNotes);

        ref_no = edreferenceno.getText().toString();

        strdiscountvalue = discount.getText().toString();
        strpaid_amount = paidamount.getText().toString();

        invoice_date = duedate.getText().toString();
        invoice_due_date = edduedate.getText().toString();
        invoicetaxamount = tax.getText().toString();

        String multipleimage = "";

        String multipagepath = null;


        if (attchmentimage != null) {
            for (int i = 0; i < attchmentimage.size(); i++) {
                attchedmentimagepath = attchmentimage.get(i);
                try {

                    multipagepath = IOUtils.toString(getAssets().open(attachmentHtml))


                            .replaceAll("#ATTACHMENT_1#", attchmentimage.get(i));


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
            for (int i = 0; i < tempList.size(); i++) {
                cruncycode = tempList.get(i).getCurrency_code();

               // DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

                double productQuantity = Double.parseDouble(tempQuantity.get(i));
                double producpriceRate = Double.parseDouble(producprice.get(i));
                //double producpriceAmount = Double.parseDouble(totalpriceproduct.get(i));

                double totalAmount = producpriceRate * productQuantity;

                String stringFormatQuantity = Utility.getPatternFormat(""+numberPostion, productQuantity);
                String stringFormatRate = Utility.getPatternFormat(""+numberPostion, producpriceRate);
                String stringFormatAmount = Utility.getPatternFormat(""+numberPostion, totalAmount);

                productitem = IOUtils.toString(getAssets().open(singleItemHtml))

                        .replaceAll("#NAME#", tempList.get(i).getProduct_name())
                        .replaceAll("#DESC#", tempList.get(i).getProduct_description())
                        .replaceAll("#UNIT#", tempList.get(i).getProduct_measurement_unit())
                        .replaceAll("#QUANTITY#", ""+stringFormatQuantity)
                        .replaceAll("#PRICE#", ""+stringFormatRate + Utility.getReplaceDollor(cruncycode))
                        .replaceAll("#TOTAL#", ""+stringFormatAmount + Utility.getReplaceDollor(cruncycode));

                productitemlist = productitemlist + productitem;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        String attachmentimage = "";

        if (attchmentimage.size() < 1) {
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


        String companyname = "";
        if (company_stamp.toLowerCase().endsWith(".jpg") || company_stamp.toLowerCase().endsWith(".jpeg") || company_stamp.toLowerCase().endsWith(".png")){
            if(company_stamp.toLowerCase().endsWith("white_img.png")){
                companyname = "";
            }else{
                companyname = getString(R.string.html_CompanySeal);
            }
        }else{
            company_stamp = "/android_res/drawable/white_img.png";
            companyname = "";
        }


        String signature_of_receivername = "";
        if (signatureofreceiverst.toLowerCase().endsWith(".jpg") || signatureofreceiverst.toLowerCase().endsWith(".jpeg") || signatureofreceiverst.toLowerCase().endsWith(".png")){
            if(signatureofreceiverst.toLowerCase().endsWith("white_img.png")){
                signature_of_receivername = "";
            }else{
                signature_of_receivername = getString(R.string.html_SignatureofReceiver);
            }
        }else{
            signatureofreceiverst = "/android_res/drawable/white_img.png";
            signature_of_receivername = "";
        }


        String signature_of_issuername = "";
        if (signature_of_issuer.toLowerCase().endsWith(".jpg") || signature_of_issuer.toLowerCase().endsWith(".jpeg") || signature_of_issuer.toLowerCase().endsWith(".png")){
            if(signature_of_issuer.toLowerCase().endsWith("white_img.png")){
                signature_of_issuername = "";
            }else{
                signature_of_issuername = getString(R.string.html_SignatureofIssuer);
            }
        }else{
            signature_of_issuer = "/android_res/drawable/white_img.png";
            signature_of_issuername = "";
        }


        String signatureinvoice = null;
         try {
            signatureinvoice = IOUtils.toString(getAssets().open(signatureHtml))
                    .replaceAll("dataimageCompany_Stamp", "file://" + company_stamp)
                    .replaceAll("CompanyStamp", companyname)
                    .replaceAll("SignatureofReceiver", signature_of_receivername)
                    .replaceAll("SignatureofIssuer", signature_of_issuername)
                    .replaceAll("dataimageRecieverImage", "file://" + signatureofreceiverst)
                    .replaceAll("data:imageSige_path", "file://" + signature_of_issuer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String taxtamountstr = "";
        String companywebsitestr = "";
        String taxtamountstrvalue = "";
        if (invoicetaxamount.equals("0")) {
            // Do you work here on success
            taxtamountstr = "";
            taxtamountstrvalue = "";

        } else {
            // null response or Exception occur
            taxtamountstr = invoicetaxamount;
            taxtamountstrvalue = ""+txttax.getText().toString();
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
            discounttxtreplace = getString(R.string.html_Discount);
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
        if (shipingcoast.equals("0")) {
            // Do you work here on success
            Shipingcosstbyct = "";
            shipingvaluetxt = "";


        } else {
            // null response or Exception occur

            if (shipingcoast.startsWith("+") && shipingcoast.endsWith("Af"))
            {
                Shipingcosstbyct = shipingcoast;
            }
            else
            {
                Shipingcosstbyct = "" + shipingcoast + cruncycode;
            }


            shipingvaluetxt = getString(R.string.html_Shipping);
        }

        if (companylogopath.toLowerCase().endsWith(".jpg") || companylogopath.toLowerCase().endsWith(".jpeg") || companylogopath.toLowerCase().endsWith(".png")){
            companyimagelogopath = companylogopath;
        }else{
            companyimagelogopath = "/android_res/drawable/white_img.png";
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

        if (ref_no.isEmpty()) {
            // Do you work here on success

            strreferencenovalue="";
            strreferencenotxtvalue="";

        } else {
            // null response or Exception occur

            strreferencenovalue=ref_no;
            strreferencenotxtvalue= getString(R.string.done);


        }

        companycolor = "#ffffff";

        String selectedTemplate = ""+this.selectedTemplate;

        String name = mainHtml;
        String nameName = "file:///android_asset/"+mainHtml;
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

        StringBuilder stringBuilderCompany = new StringBuilder();

        if(!company_address.equalsIgnoreCase("")){
            stringBuilderCompany.append(company_address+"</br>");
        }
        if(!company_contact.equalsIgnoreCase("")){
            stringBuilderCompany.append(company_contact+"</br>");
        }
        if(!companywebsitestr.equalsIgnoreCase("")){
            stringBuilderCompany.append(companywebsitestr+"</br>");
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

            Log.e(TAG , "ShipingdetailAAA "+Shipingdetail);

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
//                    .replaceAll("Contact No.", company_contact)
//                    .replaceAll("Website", companywebsitestr)
//                    .replaceAll("Email", company_email)
                    .replaceAll("#LOGO_IMAGE#", companyimagelogopath)
                    .replaceAll("INV-564", invoicenum.getText().toString())
                    .replaceAll("invD", invoice_date)
                    .replaceAll("DueDate", invoice_due_date)
                    .replaceAll("crTerms", credit_terms)
                    .replaceAll("refNo", strreferencenovalue)

                    .replaceAll("GrossAm-", ""+Utility.getContainsReplaceCurrency(Grossamount_str, cruncycode))
                    .replaceAll("Discount-", ""+Utility.getContainsReplaceCurrency(discountvalue, cruncycode))
                    .replaceAll("SubTotal-", subTotalValueTxt)
                    .replaceAll("Txses-", ""+Utility.getContainsReplaceCurrency(taxtamountstr, cruncycode))
                    .replaceAll("Shipping-", ""+Utility.getRemoveSinglePlus(Utility.getContainsReplaceCurrency(Shipingcosstbyct, cruncycode)))
                    .replaceAll("Total Amount-", ""+Utility.getContainsReplaceCurrency(netamountvalue, cruncycode))
                    .replaceAll("PaidsAmount", ""+Utility.getContainsReplaceCurrency(paidamountstrrepvalue, cruncycode))
                    .replaceAll("Paid Amount", paidamountstrreptxt)
                    .replaceAll("Balance Due-", ""+Utility.getContainsReplaceCurrency(Blanceamountstr, cruncycode))

                    .replaceAll("SubTotal", subTotalTxt)
//                    .replaceAll("Checkto", chektopaidmaount)
                    .replaceAll("Checkto", "")
                    .replaceAll("BankName", payment_bankstr)
                    .replaceAll("Pemail", pemailpaidstr)
                    .replaceAll("IBAN", payment_ibanstr)
//                    .replaceAll("Currency", payment_currencystr)
                    .replaceAll("Currency", "")
                    .replaceAll("Swift/BICCode", payment_swiftstr)
                    
                    .replaceAll("Client N", ""+stringBuilderBillTo)
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

//
//                    .replaceAll(" Payment Details ", paimnetdetailstrtxt)
//                    .replaceAll("By cheque :", bycheckstrtxt)
//                    .replaceAll("PayPal :", paypalstrtxt)
//                    .replaceAll("Bank :", bankstrtxt)

                    .replaceAll(" Payment Details ", "")
                    .replaceAll("By cheque :", "")
                    .replaceAll("PayPal :", "")
                    .replaceAll("Bank :", "")

                    .replaceAll("#TEMP3#", String.valueOf(R.color.blue));



        } catch (IOException e) {
            e.printStackTrace();

        }

        Log.e("ViewInvoice__",content);

        // invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);




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

        Log.e(TAG, "isTablet "+Utility.isTablet(EditEditReceiptActivity.this));
        if(Utility.isTablet(EditEditReceiptActivity.this) == true){ // tab
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
                webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_TAB_10);
            }
            else if (smallestWidth > 600) {
                //Device is a 7" tablet
                webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_TAB_7);
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
                Log.e(TAG, "screenInches2 "+screenInches);
                if (Utility.getDensityName(EditEditReceiptActivity.this).equalsIgnoreCase("ldpi")){
//                webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_L);
                    invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                }else if (Utility.getDensityName(EditEditReceiptActivity.this).equalsIgnoreCase("mdpi")){
//                webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_M);
                    invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                }else if (Utility.getDensityName(EditEditReceiptActivity.this).equalsIgnoreCase("hdpi")){
//                webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_H);
                    invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                }else if (Utility.getDensityName(EditEditReceiptActivity.this).equalsIgnoreCase("xhdpi")){
                    webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_X);
                }else if (Utility.getDensityName(EditEditReceiptActivity.this).equalsIgnoreCase("xxhdpi")){
                    webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_XX);
                }else if (Utility.getDensityName(EditEditReceiptActivity.this).equalsIgnoreCase("xxxhdpi")){
                    webSettings.setMinimumFontSize(webSettings.getMinimumLogicalFontSize() + AllSirApi.FONT_SIZE_CREATE_XXX);
                }else{
                    invoiceweb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                }
            }

        }


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
                final File savedPDFFile = FileManager.getInstance().createTempFile(EditEditReceiptActivity.this, "pdf", false);

                PDFUtil.generatePDFFromWebView(savedPDFFile, invoiceweb, new PDFPrint.OnPDFPrintListener() {
                    @Override
                    public void onSuccess(File file) {
                        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                        if(file.exists()) {
//                            Uri photoURI = FileProvider.getUriForFile(getActivity(),
//                                    "com.sirapp.provider",
//                                    file);
//                            intentShareFile.setType("application/pdf");
//                            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(""+photoURI));
//
//                            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
//                                    "Share As Pdf");
//
//                            startActivity(Intent.createChooser(intentShareFile, "Share File"));

                            Log.e(TAG, "FILENAME" +file);
                            createinvoicewithdetail(file);
                        }

                    }

                    @Override
                    public void onError(Exception exception) {
                        exception.printStackTrace();
                    }
                });

            }
        });

        invoiceweb.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        }, 1000);

    }


    @Override
    public void closeDialog() {
        if(bottomSheetDialog != null){
            bottomSheetDialog.dismiss();
        }
    }

}