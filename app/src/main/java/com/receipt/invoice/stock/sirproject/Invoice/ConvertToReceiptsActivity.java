package com.receipt.invoice.stock.sirproject.Invoice;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.Adapter.Customer_Bottom_Adapter;
import com.receipt.invoice.stock.sirproject.Adapter.Product_Bottom_Adapter;
import com.receipt.invoice.stock.sirproject.Adapter.Products_Adapter;
import com.receipt.invoice.stock.sirproject.Adapter.Service_bottom_Adapter;
import com.receipt.invoice.stock.sirproject.BuildConfig;
import com.receipt.invoice.stock.sirproject.Company.Companies_Activity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Customer.Customer_Activity;
import com.receipt.invoice.stock.sirproject.ImageResource.FileCompressor;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceCompanyDto;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceCustomerDto;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceDto;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceDtoInvoice;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceResponseDto;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceTotalsItemDto;
import com.receipt.invoice.stock.sirproject.Invoice.response.ProductsItemDto;
import com.receipt.invoice.stock.sirproject.Model.Customer_list;
import com.receipt.invoice.stock.sirproject.Model.GlobalVariabal;
import com.receipt.invoice.stock.sirproject.Model.Moving;
import com.receipt.invoice.stock.sirproject.Model.Product_list;
import com.receipt.invoice.stock.sirproject.Model.SelectedTaxlist;
import com.receipt.invoice.stock.sirproject.Model.Service_list;
import com.receipt.invoice.stock.sirproject.Model.Tax_List;
import com.receipt.invoice.stock.sirproject.Model.View_invoice;
import com.receipt.invoice.stock.sirproject.Product.Product_Activity;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Receipts.EditReceiptActivity;
import com.receipt.invoice.stock.sirproject.RetrofitApi.ApiInterface;
import com.receipt.invoice.stock.sirproject.RetrofitApi.RetrofitInstance;
import com.receipt.invoice.stock.sirproject.Service.Service_Activity;
import com.receipt.invoice.stock.sirproject.Tax.CustomTaxAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

public class ConvertToReceiptsActivity extends AppCompatActivity implements Customer_Bottom_Adapter.Callback, Products_Adapter.onItemClickListner, Product_Bottom_Adapter.Callback, Service_bottom_Adapter.Callback, CustomTaxAdapter.Callback {


    private static final String TAG = "ConvertToReceiptsActivity";
    //Api response data colllection
    String invoiceId = "";
    ApiInterface apiInterface;
    String company_namedto = "", company_addressdto = "", company_contactdto = "", company_emaildto = "", company_websitedto = "", payment_currencydto = "", payment_ibandto = "", payment_swift_bicdto = "";
    String signature_of_issuerdto = "", signature_of_receiverdto = "", company_stampdto = "";
    String due_datedto = "", datedto = "";
    String sltcustonernamedto = "", sltcustomer_emaildto = "", sltcustomer_contactdto = "", sltcustomer_addressdto = "", sltcustomer_websitedto = "", sltcustomer_phone_numberdto = "";
    String invoicenumberdto = "", ref_nodto = "", paid_amount_payment_methoddto = "", strpaid_amountdto = "", companylogopathdto = "", Grossamount_strdto = "", Subtotalamountdto = "",
            netamountvaluedto = "", Blanceamountstrdto = "" , Discountamountstrdto="" , Shippingamountdto = "";
    String shipping_firstnamedto, shipping_lastnamedto, shipping_address_1dto, shipping_address_2dto, shipping_citydto, shipping_countrydto, shipping_postalcodedto, shipping_zonedto;
    String company_image_pathdto, customer_image_pathdto, invoiceshre_linkdto, invoice_image_pathdto;
    ArrayList<InvoiceTotalsItemDto> grosamontdto = new ArrayList<>();
    ArrayList<ProductsItemDto> productsItemDtosdto;
    ArrayList<Invoice_image> invoice_imageDto;
    InvoiceTotalsItemDto listobj = new InvoiceTotalsItemDto();
    InvoiceDtoInvoice invoiceDtoInvoice;
    String currency_codedto="";

    String invoicecompanyiddto, companyreceiptno;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int GALLARY_aCTION_PICK_CODE = 10;
    private static final int CAMERA_ACTION_PICK_CODE = 9;
    private static final String[] PERMISSION_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    TextView invoicenumtxt, invoicenum, duedatetxt, duedate, invoicetotxt, invoicerecipnt, itemstxt, subtotaltxt, subtotal, discounttxt, discount, txttax, tax, txtcredit, txtreferenceno, edreferenceno, txtgrossamount, grosstotal, txtfreight, freight, txtnetamount, netamount, s_receiver, c_stamp;
    Button additem, createinvoice, options, addservice;
    RecyclerView productsRecycler;
    EditText ednotes;
    ArrayList<Product_list> tempList = new ArrayList<Product_list>();
    ArrayList<String> tempQuantity = new ArrayList<String>();

    ArrayList<Service_list> Servicetem = new ArrayList<Service_list>();
    ArrayList<String> producprice = new ArrayList<String>();
    // for tax list array listin
    ArrayList<Tax_List> tax_list_array = new ArrayList<Tax_List>();
    ArrayList<SelectedTaxlist> selectedtaxt = new ArrayList<SelectedTaxlist>();
    String invoicetaxamount="";
    ImageView imgsigsuccess, imgrecsuccess, imgstampsuccess;
    BottomSheetDialog bottomSheetDialog, bottomSheetDialog2, bottomSheetDialog3;
    AwesomeSpinner selectcompany;

    Products_Adapter products_adapter;


    ArrayList<String> quantity = new ArrayList<>();
    ArrayList<String> totalpriceproduct = new ArrayList<String>();
    //products bottom sheet
    TextView txtproducts;
    EditText search;
    Product_Bottom_Adapter product_bottom_adapter;
    ArrayList<Product_list> product_bottom = new ArrayList<>();
    RecyclerView recycler_services;

    TextView txtservices;
    EditText search_service;
    Service_bottom_Adapter service_bottom_adapter;

    CustomTaxAdapter customTaxAdapter;
    RecyclerView taxrecycler;

    //tax bottom sheet
    ArrayList<Service_list> service_bottom = new ArrayList<>();
    RecyclerView recycler_products;
    TextView imgincome;


    //paid amount
    Switch taxswitch;
    Button btndone;


    Button btnviewinvoice, btnclear, btndotcancel;

    DatePickerDialog.OnDateSetListener mlistener;

    Calendar myCalendar;

    //signaturepad
    DatePickerDialog datePickerDialog;

    File fileimage;
    ArrayList<String> cnames = new ArrayList<>();
    ArrayList<String> cids = new ArrayList<>();


    String selectedCompanyId = "";

    //customer bottom sheet
    TextView txtcustomer, txtname, txtcontactname;
    EditText search_customers;
    RecyclerView recycler_customers;
    Customer_Bottom_Adapter customer_bottom_adapter;
    ArrayList<Customer_list> customer_bottom = new ArrayList<>();
    ArrayList<Customer_list> selected = new ArrayList<>();
    String customer_name = "", taxrname = "";

    //For Intent
    String company_id = "", company_name = "", company_address = "", company_contact = "", company_email = "", company_website = "", payment_bank_name = "", payment_currency = "", payment_iban = "", payment_swift_bic = "";
    String customer_id = "", custoner_contact_name = "", customer_email = "", customer_contact = "", customer_address = "", customer_website = "", customer_phone_number = "";

    String shippingfirstname, shippinglastname, shippingaddress1, shippingaddress2, shippingcity, shippingpostcode, shippingcountry;


    String invoice_no = "", invoice_date = "", credit_terms = "", reference_no = "";
    String signature_of_issuer="", signature_of_receiver="", company_stamp="",netamountvalue="" , company_logo="";
    String s_r = "0";
    String invoice_nocompany, Grossamount_str;
   String paypal_emailstr="";

    ArrayList<String> rate = new ArrayList<>();

    ArrayList<Customer_list> customerinfo = new ArrayList<>();
    // pick image from galary and
    Context applicationContext = Companies_Activity.getContextOfApplication();
    FileCompressor mCompressor;
    boolean check = true;
    String strdiscount = "", strdiscountvalue = "0";
    // attchment image patth
    ArrayList<String> attchmentimage = new ArrayList<String>();
    Double total_price = 0.0;
    // Image Data
    File company_stampFileimage, signatureofinvoicemaker, signaturinvoicereceiver,company_logoFileimage;


    String strnotes, ref_no, freight_cost = "";
    String Paymentamountdate, paymentmode, signatureofreceiverst="";
    // customer detail
    String shipping_firstname, shipping_lastname, shipping_address_1, shipping_address_2, shipping_city, shipping_postcode, shipping_country, shipping_zone;
    // Date pikker date Convert To time Millissecund
    long datemillis;
    // Invoice no

    int invoicenovalue;
    // Company logo path
    String companylogopath = "", Subtotalamount = "";
    String taxtypeclusive = "", taxtype = "", taxtrateamt = "";
    int selectedItemOfMySpinner;
    String selectwarehouseId = "12";
    private AVLoadingIndicatorView avi;
    private SignaturePad signaturePad;
    private Button btnclear1;
    private Button btnsave;
    //Radio Group button Seelect
    int invocerecepts_no, invoiceestimatetno;


    String compnayestimate_count, companylogopathdtodt;

    private List<Uri> selectedUriList;
    private RequestManager requestManager;

    public ConvertToReceiptsActivity() {
        // Required empty public constructor
    }

    private static void hideKeyboard(FragmentActivity activity, View view) {

        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void verifyStroagePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.fragment_convert_to_receipts);
        Constant.toolbar(ConvertToReceiptsActivity.this, "Edit Receipt");
        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        invoiceId = getIntent().getStringExtra("invoiceID");
        companyreceiptno = getIntent().getStringExtra("companyreceiptno");
        compnayestimate_count = getIntent().getStringExtra("compnayestimate_count");
        invocerecepts_no = Integer.parseInt(companyreceiptno) + 1;
        invoiceestimatetno = Integer.parseInt(companyreceiptno) + 1;

        if (invoiceId != null) {
            Log.e("invoiceId", invoiceId);
        }
        avi = findViewById(R.id.avi);
        invoicenumtxt = findViewById(R.id.invoicenumtxt);
        invoicenum = findViewById(R.id.invoivenum);
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
        txtreferenceno = findViewById(R.id.txtreferenceno);
        edreferenceno = findViewById(R.id.edreferenceno);
        grosstotal = findViewById(R.id.grosstotal);
        txtgrossamount = findViewById(R.id.txtgrossamount);
        txtfreight = findViewById(R.id.txtfreight);
        freight = findViewById(R.id.freight);
        txtnetamount = findViewById(R.id.txtnetamount);
        netamount = findViewById(R.id.netamount);
        s_receiver = findViewById(R.id.s_receiver);
        c_stamp = findViewById(R.id.c_stamp);
        ednotes = findViewById(R.id.ednotes);
        selectcompany = findViewById(R.id.selectcompany);
        addservice = findViewById(R.id.addservice);
        imgsigsuccess = findViewById(R.id.imgsigsuccess);
        imgrecsuccess = findViewById(R.id.imgrecsuccess);
        imgstampsuccess = findViewById(R.id.imgstampsuccess);
        myCalendar = Calendar.getInstance();
        verifyStroagePermissions(this);
        requestManager = Glide.with(this);
        mCompressor = new FileCompressor(this);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog2 = new BottomSheetDialog(this);
        bottomSheetDialog3 = new BottomSheetDialog(this);
        setFonts();

        products_adapter = new Products_Adapter(this, product_bottom, tempList, this::onClick, tempQuantity, producprice);
        //productsRecycler.setAdapter(products_adapter);
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
        duedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        //for duedate
        datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth, mlistener,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        companyget();
        getinvoicedata();

        setListeners();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        productsRecycler.setLayoutManager(layoutManager);
        productsRecycler.setHasFixedSize(true);


    }


    private void getinvoicedata() {

        String token = Constant.GetSharedPreferences(ConvertToReceiptsActivity.this, Constant.ACCESS_TOKEN);
        Call<InvoiceResponseDto> resposresult = apiInterface.getInvoiceDetail(token, invoiceId);
        resposresult.enqueue(new Callback<InvoiceResponseDto>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<InvoiceResponseDto> call, retrofit2.Response<InvoiceResponseDto> response) {

                // image path of all

                company_image_pathdto = response.body().getData().getCompanyImagePath();
                customer_image_pathdto = response.body().getData().getCustomerImagePath();
                invoiceshre_linkdto = response.body().getData().getInvoiceShareLink();
                invoice_image_pathdto = response.body().getData().getInvoiceImagePath();
                // customer data get
                InvoiceDto data = response.body().getData();

                InvoiceCustomerDto invoiceCustomerDto = data.getInvoice().getCustomer();
                sltcustonernamedto = invoiceCustomerDto.getCustomerName();

                invoicerecipnt.setText(sltcustonernamedto);


                sltcustomer_addressdto = invoiceCustomerDto.getAddress();

                sltcustomer_phone_numberdto = invoiceCustomerDto.getPhoneNumber();
                sltcustomer_websitedto = invoiceCustomerDto.getWebsite();
                sltcustomer_emaildto = invoiceCustomerDto.getEmail();
                sltcustomer_contactdto = invoiceCustomerDto.getCustomerName();
                // Company Detail
                InvoiceCompanyDto companyDto = data.getInvoice().getCompany();

                company_namedto = companyDto.getName();
                company_addressdto = companyDto.getAddress();
                company_contactdto = companyDto.getPhoneNumber();
                company_websitedto = companyDto.getWebsite();
                company_emaildto = companyDto.getPaypalEmail();
                companylogopathdto = companyDto.getLogo();
                companylogopathdtodt = company_image_pathdto + companylogopathdto;

                paypal_emailstr=companyDto.getPaypalEmail();
                new DownloadsImagefromweblogoCom().execute(companylogopathdtodt);
                // Set Data To arraylist
                String customerDtoContactName = invoiceCustomerDto.getContactName();

                Customer_list selectcsto = new Customer_list();
                selectcsto.setCustomer_name(sltcustonernamedto);
                selectcsto.setCustomer_address(sltcustomer_addressdto);
                selectcsto.setCustomer_contact_person(customerDtoContactName);
                selectcsto.setCustomer_phone(sltcustomer_phone_numberdto);
                selectcsto.setCustomer_website(sltcustomer_websitedto);
                selectcsto.setCustomer_email(sltcustomer_emaildto);
                selected.add(selectcsto);


                selectedCompanyId = companyDto.getCompanyId();
                Log.e("selectedCompanyIddto", selectedCompanyId);
                customer_list(selectedCompanyId);
                CompanyInformation(selectedCompanyId);
                productget(selectedCompanyId);
                serviceget(selectedCompanyId);


                //invoice Data
                invoiceDtoInvoice = data.getInvoice();

                Gson gson = new Gson();
                String json = gson.toJson(invoiceDtoInvoice);

                Log.e(TAG, "jsonAA "+json);

                invoicenumberdto = invoiceDtoInvoice.getInvoiceNo();
                datedto = invoiceDtoInvoice.getInvoiceDate();
                due_datedto = invoiceDtoInvoice.getDueDate();
                duedate.setText(datedto);
                strnotes = invoiceDtoInvoice.getNotes();
                ednotes.setText(strnotes);
//                int recepts = Integer.parseInt(companyreceiptno) + 1;
//                invoicenum.setText("Recno #" + String.valueOf(recepts));


                String sss = getRealValue(invoicenumberdto);
                invoicenum.setText(sss);

//                invoicenum.setText("" + invoicenumberdto);
                invoicenum.setClickable(false);


                invoicecompanyiddto = invoiceDtoInvoice.getCompanyId();
                Log.e("invoicecompanyiddto", invoicecompanyiddto);
                String url = "";

                selectcompany.setSelection(1);
                ref_nodto = invoiceDtoInvoice.getRefNo();
                edreferenceno.setText(ref_nodto);

                payment_swift_bicdto = invoiceDtoInvoice.getPaymentSwiftBic();
                payment_currencydto = invoiceDtoInvoice.getPaymentCurrency();
                payment_ibandto = invoiceDtoInvoice.getPaymentIban();
                paid_amount_payment_methoddto = invoiceDtoInvoice.getPaidAmountPaymentMethod();
                currency_codedto = invoiceDtoInvoice.getCurrencySymbol();

                //data set in calculate amount.
                company_stampdto = invoiceDtoInvoice.getCompanyStamp();
                String companystemppath = invoice_image_pathdto + company_stampdto;
                Log.e("companystemppathAA ", companystemppath);
                if(companystemppath.toLowerCase().endsWith(".png") || companystemppath.toLowerCase().endsWith(".jpg") || companystemppath.toLowerCase().endsWith(".jpeg")){
                    new DownloadsImagefromwebCompanystem().execute(companystemppath);
                }

                if (company_stampdto.equals("")) {

                } else {
                    imgstampsuccess.setVisibility(View.VISIBLE);
                }
                signature_of_receiverdto = invoiceDtoInvoice.getSignatureOfReceiver();
                if (signature_of_receiverdto.equals("")) {

                } else {
                    imgrecsuccess.setVisibility(View.VISIBLE);
                }
                String signatureofreceverpath = invoice_image_pathdto + signature_of_receiverdto;
                Log.e("pathtureofreceverpath", signatureofreceverpath);
                Log.e("path receiver", signature_of_receiverdto);
                if(signatureofreceverpath.toLowerCase().endsWith(".png") || signatureofreceverpath.toLowerCase().endsWith(".jpg") || signatureofreceverpath.toLowerCase().endsWith(".jpeg")) {
                    new DownloadsImagefromweb().execute(signatureofreceverpath);
                }

                shipping_firstnamedto = invoiceDtoInvoice.getShippingFirstname();
                shipping_lastnamedto = invoiceDtoInvoice.getShippingLastname();
                shipping_address_1dto = invoiceDtoInvoice.getShippingAddress1();
                shipping_address_2dto = invoiceDtoInvoice.getShippingAddress2();
                shipping_citydto = invoiceDtoInvoice.getShippingCity();
                shipping_countrydto = invoiceDtoInvoice.getShippingCountry();
                shipping_postalcodedto = (String) invoiceDtoInvoice.getShippingPostcode();
                shipping_zonedto = (String) invoiceDtoInvoice.getShippingZone();
                // calculate amount data



                if(invoiceDtoInvoice.getTotals() != null){
                    grosamontdto = new ArrayList<InvoiceTotalsItemDto>(invoiceDtoInvoice.getTotals());
                }

                Log.e(TAG, "invoiceDtoInvoice.getTotals() "+invoiceDtoInvoice.getTotals());

                productsItemDtosdto = new ArrayList<ProductsItemDto>(invoiceDtoInvoice.getProducts());
                invoice_imageDto = new ArrayList<Invoice_image>(data.getInvoiceImage());
              //  Log.e("product", productsItemDtosdto.toString());

                //Gson gson = new Gson();
                String json2 = gson.toJson(productsItemDtosdto);

                Log.e(TAG, "jsonAA "+json2);

                if (productsItemDtosdto.size() > 0) {
                    for (int i = 0; i < productsItemDtosdto.size(); i++) {
                        Product_list product_list = new Product_list();
                        product_list.setProduct_name(productsItemDtosdto.get(i).getName());

                        product_list.setCurrency_code(currency_codedto);
                        product_list.setProduct_id(productsItemDtosdto.get(i).getProductId());


                        product_list.setProduct_measurement_unit(productsItemDtosdto.get(i).getMeasurementUnit());

                        product_list.setProduct_description(productsItemDtosdto.get(i).getDescription());
                        product_list.setProduct_price(productsItemDtosdto.get(i).getPrice());
//                        Log.e("price", productsItemDtosdto.get(i).getPrice());
//                        Log.e("Quentity", productsItemDtosdto.get(i).getQuantity());
                        producprice.add(productsItemDtosdto.get(i).getPrice());
                        tempList.add(product_list);
                        tempQuantity.add(productsItemDtosdto.get(i).getQuantity());
                        total_price = total_price + (Double.parseDouble(productsItemDtosdto.get(i).getPrice()) * Double.parseDouble(productsItemDtosdto.get(i).getQuantity()));
                        totalpriceproduct.add(String.valueOf(total_price));
                        calculateTotalAmount(total_price);
                    }

                }





                productsRecycler.setAdapter(products_adapter);

                int numsize = grosamontdto.size();
                for (int i = 0; i < numsize; i++) {
                    listobj = grosamontdto.get(i);
                    String title = listobj.getTitle();

                    Log.e(TAG, "titleSS "+title + " "+listobj.getValue());

//                    if (title.equals("Gross Amount")) {
//                        Grossamount_strdto = listobj.getValue();
//                        Double Grossamoundb=Double.parseDouble(Grossamount_strdto);
//                        int Grossamoint = new BigDecimal(Grossamoundb).setScale(0, RoundingMode.HALF_UP).intValueExact();
//
//                        grosstotal.setText(Grossamoint + currency_codedto);
//
//                    } else if (title.equals("Sub Total")) {
//
//                        Subtotalamountdto = listobj.getValue();
//                        Double Subtotalamoundbl=Double.parseDouble(Subtotalamountdto);
//                        int Subtotalamountint = new BigDecimal(Subtotalamoundbl).setScale(0, RoundingMode.HALF_UP).intValueExact();
//
//                        subtotal.setText(Subtotalamountint + currency_codedto);
//                    } else if (title.equals("Grand Total")) {
//                        netamountvaluedto = listobj.getValue();
//                        Double netamountvaluedbl=Double.parseDouble(netamountvaluedto);
//                        int netamountvaluedblint = new BigDecimal(netamountvaluedbl).setScale(0, RoundingMode.HALF_UP).intValueExact();
//
//
//                        netamount.setText(netamountvaluedblint + currency_codedto);
//                    }



                     if (title.equals("Gross Amount")) {
                        Grossamount_strdto = listobj.getValue();

                        Double Grossamount_strdtodbl = Double.parseDouble(Grossamount_strdto);


                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
                            grosstotal.setText(""+Grossamount_strdtodbl);
                        } else {
                            grosstotal.setText(""+Grossamount_strdtodbl + currency_codedto);
                        }
                    } else if (title.equals("Sub Total")) {

                        Subtotalamountdto = listobj.getValue();

                        Double Subtotalamountdbl = Double.parseDouble(Subtotalamountdto);
                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
                            subtotal.setText(""+Subtotalamountdbl);
                        } else {
                            subtotal.setText(""+Subtotalamountdbl + currency_codedto);
                        }

                    } else if (title.equals("Grand Total")) {

                        netamountvaluedto = listobj.getValue();
                        Double netamountvaludbl = Double.parseDouble(netamountvaluedto);


                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
                            netamount.setText(""+netamountvaludbl);
                        } else {
                            netamount.setText(""+netamountvaludbl+ currency_codedto);
                        }
                    }

                    else if (title.equals("Discount")) {

                        Discountamountstrdto = listobj.getValue();
                        discount.setText(""+Discountamountstrdto+currency_codedto);
//                        Double Discountamountstdbl = Double.parseDouble(Discountamountstrdto);
//
//                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
//                            balance.setText(formatter.format(Blanceamountstdbl));
//                        } else {
//                            balance.setText(formatter.format(Blanceamountstdbl) + currency_codedto);
//                        }

                    }


                    else if (title.equals("Freight Cost")) {
                        Shippingamountdto = listobj.getValue();
                        freight.setText(""+Shippingamountdto+currency_codedto);
//                        Double Discountamountstdbl = Double.parseDouble(Discountamountstrdto);
//
//                        if (currency_codedto.equals("null") || currency_codedto.equals("")) {
//                            balance.setText(formatter.format(Blanceamountstdbl));
//                        } else {
//                            balance.setText(formatter.format(Blanceamountstdbl) + currency_codedto);
//                        }

                    }

                }





            }

            @Override
            public void onFailure(Call<InvoiceResponseDto> call, Throwable t) {

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
        s_receiver.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Bold.otf"));
        c_stamp.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Bold.otf"));
        ednotes.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        txtreferenceno.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Light.otf"));
        edreferenceno.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Regular.otf"));
    }

    private void setListeners() {


        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectwarehouseId.equals("")) {
                    Constant.ErrorToast(ConvertToReceiptsActivity.this, "Select Warehouse");

                } else {
                    createbottomsheet_products();
                    bottomSheetDialog.show();
                    bottomSheetDialog2.dismiss();

                }

            }
        });

        addservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectwarehouseId.equals("")) {
                    Constant.ErrorToast(ConvertToReceiptsActivity.this, "Select Warehouse");
                    bottomSheetDialog2.dismiss();
                } else {
                    createbottomsheet_services();
                    bottomSheetDialog2.show();
                }

            }
        });

        tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createbottomsheet_tax();
                bottomSheetDialog3.show();
            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createbottomsheet_dots();
                bottomSheetDialog2.show();
            }
        });
        createinvoice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                invoice_no = invoicenumtxt.getText().toString();
                strnotes = ednotes.getText().toString();
                ref_no = edreferenceno.getText().toString();

                strdiscountvalue = discount.getText().toString();

                invoice_date = duedate.getText().toString();

                invoicetaxamount = tax.getText().toString();
                SelectedTaxlist student = new SelectedTaxlist();
                student.setTaxamount(invoicetaxamount);
                selectedtaxt.add(student);
                createinvoicewithdetail();

                        //Log.e(TAG, "invocerecepts_no "+invoicenum.getText());
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


    private void createinvoicewithdetail() {
        avi.smoothToShow();


        if (customer_name.equals("")) {
            Constant.ErrorToast(ConvertToReceiptsActivity.this, "Select A  Customer");
        } else if (invoice_date.equals("")) {
            Constant.ErrorToast(ConvertToReceiptsActivity.this, "Select Date");

        } else if (selectedCompanyId.equals("")) {
            Constant.ErrorToast(ConvertToReceiptsActivity.this, "Select a Company");

//        } else if (ref_no.equals("")) {
//            Constant.ErrorToast(ConvertToReceiptsActivity.this, "Select Ref number");

        } else {

            final ProgressDialog progressDialog=new ProgressDialog(ConvertToReceiptsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            RequestParams params = new RequestParams();
            params.add("company_id", selectedCompanyId);

            params.add("receipt_no", invoicenum.getText().toString());
            params.add("receipt_date", invoice_date);
            params.add("customer_id", customer_id);
            params.add("invoice_no", String.valueOf(invoicenovalue));
            params.add("notes", strnotes);
            params.add("ref_no", ref_no);
            params.add("paid_amount_payment_method", paymentmode);

            params.add("freight_cost", freight_cost);
            params.add("discount", strdiscountvalue);

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
            params.add("invoice_id", invoiceId);
            params.add("estimate_no", String.valueOf(invoiceestimatetno));
                      if(company_logoFileimage !=null)

            {
                try {
                    params.put("logo", company_logoFileimage);
                    //  Log.e("company stamp", company_stamp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

            if (company_stampFileimage != null) {
                try {
                    params.put("company_stamp", company_stampFileimage);
                    //  Log.e("company stamp", company_stamp);
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
            for (int i = 0; i < tempList.size(); i++) {
                params.add("product[" + i + "]" + "[price]", tempList.get(i).getProduct_price());
                params.add("product[" + i + "]" + "[quantity]", tempQuantity.get(i));
                params.add("product[" + i + "]" + "[product_id]", tempList.get(i).getProduct_id());


            }
            if (selectedtaxt.size() > 0) {
                for (int i = 0; i < selectedtaxt.size() - 1; i++) {
                    params.add("tax[" + i + "]" + "[type]", selectedtaxt.get(i).getTaxtype());
                    params.add("tax[" + i + "]" + "[type]", selectedtaxt.get(i).getTaxtype());
                          /* if (selectedtaxt.get(i).getTaxtype().equals("p"))
                      params.add("tax[" + i + "]" + "[rate]", selectedtaxt.get(i).getTaxrate());
                     else
                            params.add("tax[" + i + "[amount]", selectedtaxt.get(i).getTaxrate());
                                    */
                    params.add("tax[" + i + "]" + "[title]", selectedtaxt.get(i).getTaxname());
                }
            }

            String token = Constant.GetSharedPreferences(ConvertToReceiptsActivity.this, Constant.ACCESS_TOKEN);
            Log.e("token", token);
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Access-Token", token);
            client.post(Constant.BASE_URL + "receipt/add", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    progressDialog.dismiss();
                    Log.e("Create Invoicedata", response);
                    try {
                        Log.e("Create Invoicedata", response);

                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("true")) {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ConvertToReceiptsActivity.this, Create_Invoice_Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }, 1500);

                            JSONObject data = jsonObject.getJSONObject("data");


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
                                Constant.ErrorToast(ConvertToReceiptsActivity.this, jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Constant.ErrorToast(ConvertToReceiptsActivity.this, "Something went wrongng, try again!");
                    }
                }
            });
        }

    }

    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
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

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, GALLARY_aCTION_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_ACTION_PICK_CODE) {
                try {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri

                    imgstampsuccess.setVisibility(View.VISIBLE);
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
                    company_stamp = getRealPathFromUri(selectedImage);

                    try {
                        company_stampFileimage = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                        Log.e("company_stamp Path", company_stamp);
                        imgstampsuccess.setVisibility(View.VISIBLE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.e("company_stamp Path", company_stamp);
                } catch (Exception e) {
                    Log.e("FileSelectorActivity", "File select error", e);
                }
            }


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
            search = view.findViewById(R.id.search);
            TextView add_product = view.findViewById(R.id.add_product_new);
            add_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ConvertToReceiptsActivity.this, Product_Activity.class);
                    startActivityForResult(intent, 124);
                    bottomSheetDialog.dismiss();
                }
            });
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

            product_bottom_adapter = new Product_Bottom_Adapter(this, product_bottom, this, bottomSheetDialog);
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
                    Intent intent = new Intent(ConvertToReceiptsActivity.this, Service_Activity.class);
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


            taxrecycler = view.findViewById(R.id.taxrecycler);
            taxswitch = view.findViewById(R.id.taxswitch);
            btndone = view.findViewById(R.id.taxbtndone);

            imgincome = view.findViewById(R.id.txttax);


        /*    txtincomepercent.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Light.otf"));
            txtincometax.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
    */
            taxswitch.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            imgincome.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));

            btndone.setTypeface(Typeface.createFromAsset(this.getAssets(), "Fonts/AzoSans-Medium.otf"));


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            taxrecycler.setLayoutManager(layoutManager);
            taxrecycler.setHasFixedSize(true);

            customTaxAdapter = new CustomTaxAdapter(this, tax_list_array, this);
            taxrecycler.setAdapter(customTaxAdapter);
            customTaxAdapter.notifyDataSetChanged();


            btndone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String statusSwitch1;
                    if (taxswitch.isChecked()) {

                        statusSwitch1 = taxswitch.getTextOn().toString();
                        taxtypeclusive = "Exclisive";
                    } else {
                        statusSwitch1 = taxswitch.getTextOn().toString();
                        taxtypeclusive = "Inclusive";
                    }
                    calculateTotalAmount(total_price);
                    bottomSheetDialog3.dismiss();

                }
            });
            bottomSheetDialog3 = new BottomSheetDialog(this);
            bottomSheetDialog3.setContentView(view);
        }
    }


    public void createbottomsheet_dots() {
        if (bottomSheetDialog2 != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dots_bottomsheet, null);
            btnviewinvoice = view.findViewById(R.id.btnviewinvoice);
            btnviewinvoice.setText("View Receipt");
            btnclear = view.findViewById(R.id.btnclear);
            btndotcancel = view.findViewById(R.id.btndotcancel);

            btnviewinvoice.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onClick(View view) {
                    Subtotalamount = subtotal.getText().toString();
                    Grossamount_str = grosstotal.getText().toString();
                    invoice_date = duedate.getText().toString();
                    netamountvalue = netamount.getText().toString();
                    invoice_no = invoicenumtxt.getText().toString();
                    strnotes = ednotes.getText().toString();
                    ref_no = edreferenceno.getText().toString();
                    strdiscountvalue = discount.getText().toString();
                    invoice_date = duedate.getText().toString();
                    invoicetaxamount = tax.getText().toString();
                    if (selectedCompanyId.equals("")) {
                        Constant.ErrorToast(ConvertToReceiptsActivity.this, "Select a Company");
                        bottomSheetDialog2.dismiss();
                    } else if (customer_name.equals("")) {
                        Constant.ErrorToast(ConvertToReceiptsActivity.this, "Select A  Customer");
                        bottomSheetDialog2.dismiss();
                    } else if (invoice_date.equals("")) {
                        Constant.ErrorToast(ConvertToReceiptsActivity.this, "Select Date");
                        bottomSheetDialog2.dismiss();
                    } else {

                        Log.e(TAG , "taxamount11 "+ invoicetaxamount);

                        Intent intent = new Intent(ConvertToReceiptsActivity.this, InvoiceToReceiptsWebview.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("company_id", selectedCompanyId);
                        intent.putExtra("invoice_date", duedate.getText().toString());
                        intent.putExtra("netamount", netamountvalue);
                        intent.putExtra("taxamount", ""+invoicetaxamount);
                        intent.putExtra("subtotalamt", Subtotalamount);
                        Log.e(TAG, "Subtotalamount "+Subtotalamount);
                        intent.putExtra("paypal_emailstr", paypal_emailstr);

                        intent.putExtra("customer_id", customer_id);
                        intent.putExtra("grossamount", Grossamount_str);
                        intent.putExtra("invoice_no", invoicenum.getText().toString());
                         Log.e(TAG, "invoice_no "+invoicenum.getText().toString());
                        intent.putExtra("notes", strnotes);

                        intent.putExtra("ref_no", ref_no);
                        intent.putExtra("paid_amount_payment_method", paymentmode);
                        intent.putExtra("credit_terms", credit_terms);
                        intent.putExtra("freight_cost", freight.getText().toString());
                       // Log.e(TAG, "freight_cost "+freight_cost);
                        intent.putExtra("discount", strdiscountvalue);
                        Log.e(TAG, "discountvalue "+strdiscountvalue);
                        intent.putExtra("paid_amount_date", Paymentamountdate);
                        intent.putExtra("shipping_firstname", shippingfirstname);
                        intent.putExtra("shipping_lastname", shippinglastname);
                        intent.putExtra("shipping_address_1", shippingaddress1);
                        intent.putExtra("shipping_address_2", shippingaddress2);
                        intent.putExtra("shipping_city", shippingcity);
                        intent.putExtra("shipping_postcode", shippingpostcode);
                        intent.putExtra("shipping_country", shippingcountry);
                        intent.putExtra("payment_bank_name", payment_bank_name);
                        intent.putExtra("payment_currency", payment_currency);
                        intent.putExtra("payment_iban", payment_iban);
                        intent.putExtra("payment_swift_bic", payment_swift_bic);

                        intent.putExtra("company_logo", companylogopath);
                        intent.putExtra("company_name", company_name);
                        intent.putExtra("company_address", company_address);
                        intent.putExtra("company_contact", company_contact);
                        intent.putExtra("company_email", company_email);
                        intent.putExtra("company_website", company_website);
                        intent.putExtra("payment_bank_name", payment_bank_name);
                        intent.putExtra("payment_currency", payment_currency);
                        intent.putExtra("payment_iban", payment_iban);
                        intent.putExtra("payment_swift_bic", payment_swift_bic);

                        intent.putExtra("custoner_contact_name", custoner_contact_name);
                        intent.putExtra("customer_email", customer_email);
                        intent.putExtra("customer_contact", customer_contact);
                        intent.putExtra("customer_address", customer_address);
                        intent.putExtra("customer_website", customer_website);


                        intent.putExtra("signature_of_receiver", signatureofreceiverst);
                        Log.e(TAG, "signatureofreceiverst "+signatureofreceiverst);
                        intent.putExtra("company_stamp", company_stamp);
                        Log.e(TAG, "company_stamp "+company_stamp);

                        intent.putExtra("signature_issuer", signature_of_issuer);
                        intent.putExtra("reference_no", reference_no);

                        // intent.putStringArrayListExtra("products_list",products);
                        intent.putExtra("tempQuantity", tempQuantity);

                        intent.putExtra("tempList", tempList);
                        intent.putExtra("customerselected", selected);
                        intent.putExtra("producprice", producprice);

                        intent.putExtra("quantity_list", quantity);
                        intent.putExtra("rate_list", rate);
                        intent.putExtra("attchemnt", attchmentimage);
                        intent.putExtra("totalpriceproduct", totalpriceproduct);


                        startActivity(intent);
                        bottomSheetDialog2.dismiss();
                    }

                }
            });
            btnclear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    duedate.setText("");
                    invoicenum.setText("");
                    invoicerecipnt.setText("");

                    edreferenceno.setText("");

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


                    ednotes.setText("");
                    imgsigsuccess.setVisibility(View.INVISIBLE);
                    imgrecsuccess.setVisibility(View.INVISIBLE);
                    imgstampsuccess.setVisibility(View.INVISIBLE);


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

            bottomSheetDialog2 = new BottomSheetDialog(this);
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


                }

            }
        });

        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                strdiscountvalue = eddisount.getText().toString();
                if (strdiscountvalue.matches("")) {
                   // Toast.makeText(ConvertToReceiptsActivity.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                    mybuilder.dismiss();
                    return;
                } else {
                    calculateTotalAmount(total_price);
                    mybuilder.dismiss();
                }

            }
        });

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
                    Toast.makeText(ConvertToReceiptsActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
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
            }
            if (s_r.equals("2")) {
                signaturinvoicereceiver = photo;
                signatureofreceiverst = photo.getAbsolutePath();
            }


            //    Log.e("Signature Of Issuer", signature_of_issuer);
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


    public void companyget() {
        cnames.clear();
        cids.clear();
        String token = Constant.GetSharedPreferences(this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "company/listing", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
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
                                company_address = item.getString("address");
                                company_contact = item.getString("phone_number");
                                company_email = item.getString("email");
                                company_website = item.getString("website");
                                payment_bank_name = item.getString("payment_bank_name");
                                payment_currency = item.getString("payment_currency");
                                payment_iban = item.getString("payment_iban");
                                payment_swift_bic = item.getString("payment_swift_bic");


                                cnames.add(company_name);
                                cids.add(company_id);

                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(ConvertToReceiptsActivity.this, android.R.layout.simple_spinner_item, cnames);
                                selectcompany.setAdapter(namesadapter);

                            }
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


    public void productget(String selectedCompanyId) {
        product_bottom.clear();
        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);

        String token = Constant.GetSharedPreferences(this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "product/getListingByCompany", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String(responseBody);
                // Log.e("response product", response);

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
                                String minimum = item.getString("minimum");
                                String measurement_unit = item.getString("measurement_unit");



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

                                product_bottom.add(product_list);


                            }
                        } else {
                            Constant.ErrorToast(ConvertToReceiptsActivity.this, jsonObject.getString("Product Not Found"));
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
                    Constant.ErrorToast(ConvertToReceiptsActivity.this, "Something went wrongng, try again!");
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
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "service/getListingByCompany", params, new AsyncHttpResponseHandler() {
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
                            Constant.ErrorToast(ConvertToReceiptsActivity.this, jsonObject.getString("Product Not Found"));
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
                    Constant.ErrorToast(ConvertToReceiptsActivity.this, "Something went wrongng, try again!");
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
                    Intent intent = new Intent(ConvertToReceiptsActivity.this, Customer_Activity.class);
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
        params.add("invoice", "1");
        params.add("warehouse", "1");


        String token = Constant.GetSharedPreferences(this, Constant.ACCESS_TOKEN);
        Log.e("token", token);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "company/info", params, new AsyncHttpResponseHandler() {
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


                        String company_image_path = data.getString("company_image_path");
                        Log.e("company_image_path", company_image_path);

                        JSONArray company = data.getJSONArray("company");

                        for (int i = 0; i < company.length(); i++) {
                            JSONObject item = company.getJSONObject(i);

                            String logo = item.getString("logo");
                            companylogopath = company_image_path + logo;
                            Log.e("companylogopath", companylogopath);
                        }

                        JSONArray customer = data.getJSONArray("customer");

                        JSONArray invoice = data.getJSONArray("invoice");
                        for (int i = 0; i < invoice.length(); i++) {
                            JSONObject item = invoice.getJSONObject(i);
                            invoice_nocompany = item.getString("invoice_no");

                            /* invoicenum.setText(invoice_nocompany);*/
                            if (invoice_nocompany != null) {
                                Log.e("invoice no", invoice_nocompany);
                            }

                        }


                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject item = customer.getJSONObject(i);

                            customer_id = item.getString("customer_id");
                            customer_name = item.getString("customer_name");
                            custoner_contact_name = item.getString("contact_name");
                            String image = item.getString("image");
                            customer_email = item.getString("email");
                            customer_contact = item.getString("phone_number");
                            customer_address = item.getString("address");
                            customer_website = item.getString("website");
                            customer_phone_number = item.getString("phone_number");

                            Customer_list customer_list = new Customer_list();

                            customer_list.setCustomer_email(customer_email);
                            customer_list.setCustomer_address(customer_address);
                            customer_list.setCustomer_website(customer_website);
                            customer_list.setCustomer_website(customer_website);
                            customer_list.setCustomer_phone(customer_phone_number);

                            customer_list.setCustomer_id(customer_id);
                            customer_list.setCustomer_name(customer_name);
                            customer_list.setCustomer_contact_person(custoner_contact_name);
                            customer_list.setCustomer_image_path(image_path);
                            customer_list.setCustomer_image(image);
                            customer_bottom.add(customer_list);
                            tax_list_array.clear();

                            tax_list_array = new ArrayList<Tax_List>();

                            JSONArray tax_list = data.getJSONArray("tax");

                            for (int j = 0; j < tax_list.length(); j++) {
                               // Tax_List student = new Gson().fromJson(tax_list.get(j).toString(), Tax_List.class);
                                JSONObject jsonObject1 = tax_list.getJSONObject(i);
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
                    Constant.ErrorToast(ConvertToReceiptsActivity.this, "Something went wrongng, try again!");
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
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "customer/getListingByCompany", params, new AsyncHttpResponseHandler() {
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

                            customer_id = item.getString("customer_id");
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
                    Constant.ErrorToast(ConvertToReceiptsActivity.this, "Something went wrongng, try again!");
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
    public void onPostExecutecall(Product_list selected_item, String s, String price) {

        Double propice=Double.parseDouble(price);
        int propriceint = new BigDecimal(propice).setScale(0, RoundingMode.HALF_UP).intValueExact();
        producprice.add(String.valueOf(propriceint));
        tempList.add(selected_item);
        tempQuantity.add(s);
        total_price = total_price + (Double.parseDouble(price) * Double.parseDouble(s));

        int subtotalint = new BigDecimal(total_price).setScale(0, RoundingMode.HALF_UP).intValueExact();
        totalpriceproduct.add(String.valueOf(subtotalint));
        calculateTotalAmount(total_price);

        products_adapter.notifyDataSetChanged();

        bottomSheetDialog.dismiss();


    }

    private void calculateTotalAmount(Double total_price) {

        double balanceamount = 0.0;
        Double netamountvalue = 0.0;
        Double Totatlvalue = 0.0;
        Double subtotalvalue = 0.0;


        if (tempList.size() > 0) {

            String cruncycode = tempList.get(0).getCurrency_code();
            Log.e("cruncycode", cruncycode);
            Log.e("total_price", String.valueOf(this.total_price));

            Double stratingvalue = this.total_price;
            grosstotal.setText(stratingvalue + cruncycode);
            netamount.setText(stratingvalue + cruncycode);
            subtotalvalue = total_price;
            netamountvalue = total_price;
            balanceamount = total_price;
            if (strdiscount.equals("Percentage")) {
                subtotalvalue = 0.0;
                netamountvalue = 0.0;
                balanceamount = 0.0;
                Totatlvalue = total_price * Double.parseDouble(strdiscountvalue) / 100;

                discount.setText(Totatlvalue + cruncycode);
                subtotalvalue = total_price - Totatlvalue;


                netamountvalue = subtotalvalue;
                subtotal.setText(subtotalvalue + cruncycode);
                netamount.setText(subtotalvalue + cruncycode);

                //  Log.e("DissCount value", String.valueOf(Totatlvalue)+ cruncycode);
            } else if (strdiscount.equals("Amount")) {
                subtotalvalue = 0.0;
                netamountvalue = 0.0;
                balanceamount = 0.0;
                subtotalvalue = total_price - Double.parseDouble(strdiscountvalue.replace("Rs" , ""));
                netamountvalue = subtotalvalue;


                discount.setText(strdiscountvalue + cruncycode);
                subtotal.setText(subtotalvalue + cruncycode);
                netamount.setText(subtotalvalue + cruncycode);

            } else {

                discount.setText("0");
                subtotal.setText(stratingvalue + cruncycode);
                netamount.setText(stratingvalue + cruncycode);

            }
            if (selectedtaxt.size() > 0) {
                if (taxtypeclusive.equals("Inclusive")) {
                    if (taxtype.equals("P")) {
                        netamountvalue = 0.0;
                        Double Totatlvalue1 = subtotalvalue * Double.parseDouble(taxtrateamt) / 100;

                        tax.setText(Totatlvalue1 + cruncycode);

                        String taxtpercentage = taxtrateamt.substring(0, 2);
//                        String tatxnamesub = taxrname.substring(0, 3);
//                        String subStrinng = tatxnamesub + taxtpercentage + "%";
//
//
//                        txttax.setText("(" + subStrinng + "Incl" + ")"); //Dont do any change

                        String subStrinng = "VAT" + taxtpercentage + "%";

                        txttax.setText("(" + subStrinng + "Incl" + ")"); //Dont do any change

                        netamountvalue = subtotalvalue;
                        netamount.setText(subtotalvalue + cruncycode);


                    } else {
                        netamountvalue = 0.0;
                        String taxtpercentage1 = taxtrateamt.substring(0, 4);
//                        String tatxnamesub1 = taxrname.substring(0, 3);
//                        String subStrinng = tatxnamesub1 + taxtpercentage1;
//
//
//                        txttax.setText("(" + subStrinng + "Incl" + ")");


                        String subStrinng = "VAT" + taxtpercentage1 + "";

                        txttax.setText("(" + subStrinng + "Incl" + ")");


                        Double amomnt = Double.parseDouble(taxtrateamt);

                        netamountvalue = subtotalvalue;

                        tax.setText(amomnt + cruncycode);
                        netamount.setText(subtotalvalue + cruncycode);

                    }
                } else {
                    if (taxtype.equals("P")) {
                        netamountvalue = 0.0;
                        Double Totatlvalue1 = subtotalvalue * Double.parseDouble(taxtrateamt) / 100;

                        tax.setText(Totatlvalue1 + cruncycode);

                        String taxtpercentage = taxtrateamt.substring(0, 2);
//                        String tatxnamesub = taxrname.substring(0, 3);
//                        String subStrinng = tatxnamesub + taxtpercentage + "%";
//
//
//                        txttax.setText("(" + subStrinng + ")"); //Dont do any change

                        String subStrinng = "VAT" + taxtpercentage + "%";

                        txttax.setText("(" + subStrinng + ")"); //Dont do any change

                        netamountvalue = subtotalvalue + Totatlvalue1;

                        netamount.setText(netamountvalue + cruncycode);



                    } else {

                        netamountvalue = 0.0;
                        String taxtpercentage1 = taxtrateamt.substring(0, 4);
//                        String tatxnamesub1 = taxrname.substring(0, 3);
//                        String subStrinng = tatxnamesub1 + taxtpercentage1;
//
//
//                        txttax.setText("(" + subStrinng + ")");


                        String subStrinng = "VAT" + taxtpercentage1 + "";


                        txttax.setText("(" + subStrinng + ")");

                        Double amomnt = Double.parseDouble(taxtrateamt);
                        Double taxamountvalue = subtotalvalue + amomnt;

                        netamountvalue = taxamountvalue;
                        tax.setText(amomnt + cruncycode);
                        netamount.setText(taxamountvalue + cruncycode);

                    }
                }

            }
            if (freight_cost.equals("")) {


            } else {
                balanceamount = netamountvalue + Double.parseDouble(freight_cost);

                Double shipingvalue = Double.parseDouble(freight_cost);

                freight.setText("+" + shipingvalue + cruncycode);
                netamount.setText(balanceamount + cruncycode);
            }



        } else {
            grosstotal.setText("0");
            subtotal.setText("0");
            discount.setText("0");
            freight.setText("0");
            netamount.setText("0");
            tax.setText("0");


        }

    }

    @Override
    public void onPostExecutecall2(Service_list selected_item, String s, String price) {
        Double propice=Double.parseDouble(price);
        int propriceint = new BigDecimal(propice).setScale(0, RoundingMode.HALF_UP).intValueExact();
        producprice.add(String.valueOf(propriceint));

        Product_list product_list = new Product_list();
        product_list.setProduct_name(selected_item.getService_name());
        product_list.setProduct_id(selected_item.getService_id());
        product_list.setCurrency_code(selected_item.getCuurency_code());

        product_list.setProduct_description(selected_item.getService_description());

        product_list.setProduct_measurement_unit(selected_item.getMeasurement_unit());

        product_list.setProduct_price(selected_item.getService_price());

        producprice.add(selected_item.getService_price());
        tempList.add(product_list);


        tempQuantity.add(s);

        total_price = total_price + (Double.parseDouble(price) * Double.parseDouble(s));

        int subtotalint = new BigDecimal(total_price).setScale(0, RoundingMode.HALF_UP).intValueExact();
        totalpriceproduct.add(String.valueOf(subtotalint));
        calculateTotalAmount(total_price);

        products_adapter.notifyDataSetChanged();

        bottomSheetDialog.dismiss();


    }

    @Override
    public void onClick(int str,String type) {
        //  Log.e("Total price",String.valueOf(total_price));

        if(tempList.size() > 0) {
            total_price = total_price - (Double.parseDouble(tempList.get(str).getProduct_price()) * Double.parseDouble(tempQuantity.get(str)));
        }

        calculateTotalAmount(total_price);

        tempList.remove(str);
        totalpriceproduct.remove(str);
        producprice.remove(str);
        tempQuantity.remove(str);

        products_adapter.notifyDataSetChanged();

        Log.e("tempList", ""+String.valueOf(tempList.size()));



    }

    @Override
    public void onPostExecutecall3(String taxnamst, String taxnamss, String type) {
        selectedtaxt.clear();
        Log.e("income rate", taxnamst);
        Log.e("taxnamss_rate", taxnamss);
        Log.e("type rate", type);
        taxtype = type;
        taxtrateamt = taxnamss;
        SelectedTaxlist student = new SelectedTaxlist();
        student.setTaxname(taxnamst);
        student.setTaxrate(taxnamss);
        student.setTaxtype(type);
        student.setTaxamount(taxnamss);
        selectedtaxt.add(student);


    }


    @Override
    public void onPostExecute(Customer_list customer_list) {
        customerinfo.add(customer_list);
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();

            // createbottomsheet_customers();
            invoicerecipnt.setText(customer_list.getCustomer_name());
            customer_name = customer_list.getCustomer_name();

        } else {
            // createbottomsheet_customers();
            invoicerecipnt.setText(customer_list.getCustomer_name());
            customer_name = customer_list.getCustomer_name();
        }
        selected.add(customer_list);
/*

        for (int i = 0; i < customerinfo.size(); i++) {
            Log.e("getShipping_firstname", customerinfo.get(i).getShipping_firstname());
            Log.e("getCustomer_email", customerinfo.get(i).getCustomer_email());
            Log.e("getCustomer_email", customerinfo.get(i).getCustomer_address());

        }*/
    }

    private class DownloadsImagefromweb extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
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

            File imageFile = new File(path, String.valueOf(System.currentTimeMillis()) + ".png"); // Imagename.png

            signaturinvoicereceiver = imageFile;
            signatureofreceiverst = imageFile.getAbsolutePath();

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
                MediaScannerConnection.scanFile(ConvertToReceiptsActivity.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
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

    private class DownloadsImagefromwebCompanystem extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
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


            File imageFile = new File(path, String.valueOf(System.currentTimeMillis()) + ".png"); // Imagename.png

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
                MediaScannerConnection.scanFile(ConvertToReceiptsActivity.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
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

    private class DownloadsImagefromweblogoCom extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
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


            File imageFile = new File(path, String.valueOf(System.currentTimeMillis()) + ".png"); // Imagename.png

            company_logoFileimage = imageFile;
            company_logo = imageFile.getAbsolutePath();
            String imagepath = imageFile.getAbsolutePath();
         //   Log.e("companystemp", imagepath);

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
                MediaScannerConnection.scanFile(ConvertToReceiptsActivity.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
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




    @SuppressLint("LongLogTag")
    private String getRealValue(String sss) {
        String valueIs = "";
        if(sss.toString().length() > 0){

            // char cc = invoicenum.getText().toString().charAt(invoicenum.getText().toString().length() - 1);

            boolean gg = isNumeric(sss);
            Log.e(TAG, "gggggg "+gg);

            boolean dd = isChar(sss);
            Log.e(TAG, "dddddd "+dd);

            if(gg == false && dd == false){
                Log.e(TAG, "truee ");
                Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
                Log.e(TAG, "cccccc "+flag);
                if(flag == true){
                    String str = sss;
                    String cc = extractInt(str);
                    if(cc.contains(" ")){
                        String vv[] = cc.split(" ");
                        String ii =  vv[vv.length - 1];
                        Log.e(TAG , "extractInt "+ii);
                        String vvvvv = sss.substring(0, sss.length() - ii.length());

                        Log.e(TAG , "vvvvv "+vvvvv);

                        int myValue = Integer.parseInt(ii);
                        valueIs = vvvvv+myValue;
                    }
                    if(!cc.contains(" ")){
                        Log.e(TAG , "extractInt2 "+cc);
                        int myValue = Integer.parseInt(cc);
                        String vvvvv = sss.substring(0, sss.length() - cc.length());

                        Log.e(TAG , "bbbbbb "+vvvvv);
                        valueIs = vvvvv+myValue;
                    }
                }
            }else{
                boolean ddd = isChar(sss);
                if(ddd == false){
                    int myValue = Integer.parseInt(sss);
                    valueIs = "Inv # "+myValue;
                }
            }
        }
        return valueIs;
    }



    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }


    public static boolean isChar(String str)
    {
        for (char c : str.toCharArray())
        {
            if (Character.isDigit(c)) return false;
        }
        return true;
    }



    static String extractInt(String str)
    {
        // Replacing every non-digit number
        // with a space(" ")
        str = str.replaceAll("[^\\d]", " ");

        // Remove extra spaces from the beginning
        // and the ending of the string
        str = str.trim();

        // Replace all the consecutive white
        // spaces with a single space
        str = str.replaceAll(" +", " ");

        if (str.equals(""))
            return "-1";

        return str;
    }



    @Override
    public void closeDialog() {
        if(bottomSheetDialog != null){
            bottomSheetDialog.dismiss();
        }
    }
}