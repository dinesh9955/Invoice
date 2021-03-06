package com.sirapp.Report;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.Adapter.Customer_Bottom_Adapter;
import com.sirapp.Adapter.Product_Bottom_Adapter;
import com.sirapp.Constant.Constant;
import com.sirapp.Customer.Customer_Activity;
import com.sirapp.Product.Product_Activity;
import com.sirapp.RetrofitApi.ApiInterface;
import com.sirapp.RetrofitApi.RetrofitInstance;
import com.sirapp.ThankYouNote.ThankYouNoteActivity;
import com.sirapp.Vendor.Vendor_Activity;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Model.Customer_list;
import com.sirapp.Model.Product_list;
import com.sirapp.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ReportActivity extends BaseActivity implements Customer_Bottom_Adapter.Callback, Product_Bottom_Adapter.Callback {
    private static final String TAG = "ReportActivity" ;
    ApiInterface apiInterface;
    private AVLoadingIndicatorView avi;
    ImageView avibackground;
//    AwesomeSpinner selectcompany;
    Button selectcompany1;

    ArrayList<String> cids = new ArrayList<>();
    ArrayList<String> cnames = new ArrayList<>();
//    ArrayList<String> cAddress = new ArrayList<>();
//    ArrayList<String> ccontactNo = new ArrayList<>();
//    ArrayList<String> cWebsite = new ArrayList<>();
//    ArrayList<String> cEmail = new ArrayList<>();
//    ArrayList<String> cLogo = new ArrayList<>();

//    String companyName = "";
//    String companyAddress = "";
//    String companyContactNo = "";
//    String companyWebsite = "";
//    String companyEmail = "";
//    String companyLogo = "";

    RecyclerView recycler_invoices;
    String selectedCompanyId = "";

    ReportAdapter invoicelistAdapterdt;

    ArrayList<String> arrayListNames = new ArrayList<>();

    String customer_id = "";
    String customer_name = "";
    ArrayList<Customer_list> customer_bottom = new ArrayList<>();

    String supplier_id = "";
    String supplier_name = "";
    ArrayList<Customer_list> supplier_bottom = new ArrayList<>();


//    String supplier_id = "";
//    String supplier_name = "";
    ArrayList<Product_list> product_bottom = new ArrayList<>();

    BottomSheetDialog bottomSheetDialog;

    String titleName = "";

    int positionNext = -1;

    String company_image_path = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

//        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Constant.toolbar(ReportActivity.this, getString(R.string.header_report));


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);

        recycler_invoices = findViewById(R.id.recycler_invoices);
        selectcompany1 = findViewById(R.id.selectcompany2);



//        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
//            @Override
//            public void onItemSelected(int position, String itemAtPosition) {
//                selectedCompanyId = cids.get(position);
//
//                customer_list(selectedCompanyId);
//                supplier_list(selectedCompanyId);
//                product_list(selectedCompanyId);
//            }
//        });


        selectcompany1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView mRecyclerView;
                MenuAdapter mAdapter;

                final Dialog mybuilder = new Dialog(ReportActivity.this);
                mybuilder.setContentView(R.layout.select_company_dialog_3);


                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(ReportActivity.this, LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter(cnames, mybuilder);
                mRecyclerView.setAdapter(mAdapter);

                if(cnames.size() > 0){
                    mybuilder.show();
                    mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    Window window = mybuilder.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawableResource(R.color.transparent);
                }

            }
        });




        arrayListNames.add(getString(R.string.report_CustomerStatementofAccount));
        arrayListNames.add(getString(R.string.report_SupplierStatementofAccount));
        arrayListNames.add(getString(R.string.report_TotalSalesReport));
        arrayListNames.add(getString(R.string.report_TotalPurchaseReport));
        arrayListNames.add(getString(R.string.report_CustomerAgeingReport));
        arrayListNames.add(getString(R.string.report_TaxCollectedReport));
        arrayListNames.add(getString(R.string.report_StockReport));
        arrayListNames.add(getString(R.string.report_ProductMovementReport));



        invoicelistAdapterdt = new ReportAdapter(ReportActivity.this, arrayListNames);
        recycler_invoices.setAdapter(invoicelistAdapterdt);
        recycler_invoices.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_invoices.setHasFixedSize(true);
        invoicelistAdapterdt.notifyDataSetChanged();


        companyget();

    }







    private void companyget() {

        cnames.clear();
        cids.clear();
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        String token = Constant.GetSharedPreferences(ReportActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        RequestParams params = new RequestParams();
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "company/listing", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("responsecompany", response);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        company_image_path = data.getString("company_image_path");
                        JSONArray company = data.getJSONArray("company");
                        String company_id= "", company_name= "", company_address= "", company_contact= "", company_email= "",
                                company_website= "", payment_bank_name= "", payment_currency= "", payment_iban= "", payment_swift_bic= "", logo = "";
                        if (company.length() > 0) {
                            for (int i = 0; i < company.length(); i++) {
                                JSONObject item = company.getJSONObject(i);
                                company_id = item.getString("company_id");
                                company_name = item.getString("name");
                                company_address = item.getString("address");
                                company_contact = item.getString("phone_number");
                                company_email = item.getString("email");
                                company_website = item.getString("website");
                                logo = item.getString("logo");

                                payment_bank_name = item.getString("payment_bank_name");
                                payment_currency = item.getString("payment_currency");
                                payment_iban = item.getString("payment_iban");
                                payment_swift_bic = item.getString("payment_swift_bic");
                                String colorCode = item.getString("color");
                                Log.e("CompanyId",company_id);

                                cnames.add(company_name);
                                cids.add(company_id);
//                                cAddress.add(company_address);
//                                ccontactNo.add(company_contact);
//                                cWebsite.add(company_website);
//                                cEmail.add(company_email);
//                                cLogo.add(logo);



                            }
                        }


                        if(company.length() == 1){
                            selectcompany1.setText(cnames.get(0));
                            selectedCompanyId = cids.get(0);

                            customer_list(selectedCompanyId);
                            supplier_list(selectedCompanyId);
                            product_list(selectedCompanyId);
                        }

//                        ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(ReportActivity.this, android.R.layout.simple_spinner_item, cnames);
//                        selectcompany.setAdapter(namesadapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
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


    public void customer_list(String selectedCompanyId) {
        customer_bottom.clear();
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);

        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);

        String token = Constant.GetSharedPreferences(ReportActivity.this, Constant.ACCESS_TOKEN);
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

        String token = Constant.GetSharedPreferences(ReportActivity.this, Constant.ACCESS_TOKEN);
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
        supplier_bottom.clear();
        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);

        String token = Constant.GetSharedPreferences(ReportActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "product/getListingByCompany", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

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
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
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



    public void onClickAdapter(int i) {
        Log.e(TAG, "sssss "+i);
        positionNext = i;
        bottomSheetDialog = new BottomSheetDialog(ReportActivity.this);
        if (selectedCompanyId.equals("")) {
            Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_SelectACompany));
        } else {
            //createbottomsheet_FilterData();
            titleName = arrayListNames.get(i);
            if(positionNext == 0){
                createbottomsheet_customers();
                bottomSheetDialog.show();
            } else if(positionNext == 1){
                createbottomsheet_supplier();
                bottomSheetDialog.show();
            }else if(positionNext == 7 ){
                createbottomsheet_product();
                bottomSheetDialog.show();
            }else{
                allReport(positionNext, selectedCompanyId, company_image_path);
            }

        }
    }




    TextView txtcustomer, txtname, txtcontactname;
    EditText search_customers;
    RecyclerView recycler_customers;
    Customer_Bottom_Adapter customer_bottom_adapter;

    public void createbottomsheet_customers() {

        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(ReportActivity.this).inflate(R.layout.customer_bottom_sheet, null);
            txtcustomer = view.findViewById(R.id.txtcustomer);
            search_customers = view.findViewById(R.id.search_customers);
            TextView add_customer = view.findViewById(R.id.add_customer);
            add_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReportActivity.this, Customer_Activity.class);
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

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReportActivity.this, LinearLayoutManager.VERTICAL, false);
            recycler_customers.setLayoutManager(layoutManager);
            recycler_customers.setHasFixedSize(true);

            customer_bottom_adapter = new Customer_Bottom_Adapter(ReportActivity.this, customer_bottom, this);
            recycler_customers.setAdapter(customer_bottom_adapter);
            customer_bottom_adapter.notifyDataSetChanged();


            txtcustomer.setTypeface(Typeface.createFromAsset(ReportActivity.this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            // txtname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));
            // txtcontactname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));

            bottomSheetDialog = new BottomSheetDialog(ReportActivity.this);
            bottomSheetDialog.setContentView(view);

            bottomSheetDialog.show();


        }
    }


    public void createbottomsheet_supplier() {

        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(ReportActivity.this).inflate(R.layout.customer_bottom_sheet, null);
            txtcustomer = view.findViewById(R.id.txtcustomer);
            txtcustomer.setText(getString(R.string.dialog_SelectSupplier));
            search_customers = view.findViewById(R.id.search_customers);
            search_customers.setHint(getString(R.string.dialog_SelectSuppliers));
            TextView add_customer = view.findViewById(R.id.add_customer);
            add_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReportActivity.this, Vendor_Activity.class);
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

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReportActivity.this, LinearLayoutManager.VERTICAL, false);
            recycler_customers.setLayoutManager(layoutManager);
            recycler_customers.setHasFixedSize(true);

            customer_bottom_adapter = new Customer_Bottom_Adapter(ReportActivity.this, supplier_bottom, this);
            recycler_customers.setAdapter(customer_bottom_adapter);
            customer_bottom_adapter.notifyDataSetChanged();


            txtcustomer.setTypeface(Typeface.createFromAsset(ReportActivity.this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            // txtname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));
            // txtcontactname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));

            bottomSheetDialog = new BottomSheetDialog(ReportActivity.this);
            bottomSheetDialog.setContentView(view);

            bottomSheetDialog.show();


        }
    }


    Product_Bottom_Adapter product_bottom_adapter;

    public void createbottomsheet_product() {

        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(ReportActivity.this).inflate(R.layout.customer_bottom_sheet, null);
            txtcustomer = view.findViewById(R.id.txtcustomer);
            txtcustomer.setText(getString(R.string.stock_Select_Product));
            search_customers = view.findViewById(R.id.search_customers);
            search_customers.setHint(getString(R.string.stock_Select_Products));
            TextView add_customer = view.findViewById(R.id.add_customer);
            add_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReportActivity.this, Product_Activity.class);
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

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReportActivity.this, LinearLayoutManager.VERTICAL, false);
            recycler_customers.setLayoutManager(layoutManager);
            recycler_customers.setHasFixedSize(true);

            product_bottom_adapter = new Product_Bottom_Adapter(ReportActivity.this, product_bottom, this, bottomSheetDialog ,"report");
            recycler_customers.setAdapter(product_bottom_adapter);
            product_bottom_adapter.notifyDataSetChanged();


            txtcustomer.setTypeface(Typeface.createFromAsset(ReportActivity.this.getAssets(), "Fonts/AzoSans-Medium.otf"));
            // txtname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));
            // txtcontactname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Medium.otf"));

            bottomSheetDialog = new BottomSheetDialog(ReportActivity.this);
            bottomSheetDialog.setContentView(view);

            bottomSheetDialog.show();


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



    @Override
    public void onPostExecute(Customer_list customer_list) {

        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }

        String customer_id = customer_list.getCustomer_id();
       // String customer_name = customer_list.getCustomer_name();


        Log.e(TAG, "customer_id "+customer_id);
        Log.e(TAG, "customer_name "+customer_name);

        if(positionNext == 0){
            customerReport(customer_id, selectedCompanyId, positionNext);
        } else if(positionNext == 1){
            supplierReport(customer_id, selectedCompanyId, positionNext);
        } else if(positionNext == 2){

        } else if(positionNext == 3){

        } else if(positionNext == 4){

        } else if(positionNext == 5){

        } else if(positionNext == 6){

        }else if(positionNext == 7){

        }





//        Intent intent = new Intent(ReportActivity.this, ReportViewActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        intent.putExtra("positionNext", positionNext);
//        intent.putExtra("company_id", selectedCompanyId);
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

        Log.e(TAG, "customer_id "+customer_id);

//        Intent intent = new Intent(ReportActivity.this, ReportViewActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("positionNext", positionNext);
//        intent.putExtra("company_id", selectedCompanyId);
//
//        if(positionNext == 7){
//            intent.putExtra("product_id", customer_id);
//        }
//
//        startActivity(intent);


        productMovementReport(customer_id, selectedCompanyId, positionNext);
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









    private void customerReport(String customer_id, String companyID, int positionNext) {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.add("customer_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "report/customerStatement", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanyCSS"+ response);

                //  avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray statement = data.getJSONArray("statement");

                        if(statement.length() == 0){
                            Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_customer_statement_report));
                        }else{
                            Intent intent = new Intent(ReportActivity.this, ReportViewActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("positionNext", positionNext);
                            intent.putExtra("company_id", companyID);
                            intent.putExtra("customer_id", customer_id);
                            startActivity(intent);
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
                    Log.e(TAG, "responsecompanyF"+ response);
                }

                Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_customer_statement_report));
            }
        });
    }



    private void supplierReport(String customer_id, String companyID, int positionNext) {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.add("supplier_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "report/supplierStatement", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanySS"+ response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray statement = data.getJSONArray("statement");
                        if(statement.length() == 0){
                            Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_supplier_statement_report));
                        }else{
                            Intent intent = new Intent(ReportActivity.this, ReportViewActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("positionNext", positionNext);
                            intent.putExtra("company_id", companyID);
                            intent.putExtra("supplier_id", customer_id);
                            startActivity(intent);
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
                    Log.e(TAG, "responsecompanyF"+ response);

                }
                Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_supplier_statement_report));
            }
        });
    }



    private void productMovementReport(String customer_id, String companyID, int positionNext) {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.add("product_id", customer_id);

        String token = Constant.GetSharedPreferences(ReportActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "report/productMovement", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanySS"+ response);

                //  avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray statement = data.getJSONArray("product_movement");
                        if(statement.length() == 0){
                            Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_product_movement_report));
                        }else{
                            Intent intent = new Intent(ReportActivity.this, ReportViewActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("positionNext", positionNext);
                            intent.putExtra("company_id", companyID);
                            intent.putExtra("product_id", customer_id);
                            startActivity(intent);
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
                }
                Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_product_movement_report));

            }
        });
    }



    private void allReport(int positionNext, String companyID, String company_image_path) {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.add("company_id", companyID);

        String token = Constant.GetSharedPreferences(ReportActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());

        String url_ = "";
        if(positionNext == 2){
            url_ = "report/totalSales";
        }else if(positionNext == 3){
            url_ = "report/totalPurchases";
        }else if(positionNext == 4){
            url_ = "report/customerAgeing";
        }else if(positionNext == 5){
            url_ = "report/taxation";
        }else if(positionNext == 6){
            url_ = "report/stock";
        }

        client.post(AllSirApi.BASE_URL + url_, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                String response = new String(responseBody);
                Log.e(TAG, "responsecompanySS"+ response);

                //  avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray statement = null;
                        if(positionNext == 2){
                            statement = data.getJSONArray("total_sales");
                            if(statement.length() == 0){
                                Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_total_sales_report));
                            }
                        }else if(positionNext == 3){
                            statement = data.getJSONArray("total_purchases");
                            if(statement.length() == 0){
                                Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_total_purchase_report));
                            }
                        }else if(positionNext == 4){
                            statement = data.getJSONArray("customer_ageing");
                            if(statement.length() == 0){
                                Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_customer_ageing_report));
                            }
                        }else if(positionNext == 5){
                            statement = data.getJSONArray("taxation");
                            if(statement.length() == 0){
                                Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_tax_collected_report));
                            }
                        }else if(positionNext == 6){
                            statement = data.getJSONArray("stock");
                            if(statement.length() == 0){
                                Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_stock_report));
                            }
                        }




                        if(statement != null){
                            if(statement.length() != 0){
                                Intent intent = new Intent(ReportActivity.this, ReportViewActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("positionNext", positionNext);
                                intent.putExtra("company_id", companyID);
                                intent.putExtra("company_image_path", company_image_path);
                                startActivity(intent);
                            }
                        }else{

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
                }
                Constant.ErrorToast(ReportActivity.this, getString(R.string.dialog_no_product_movement_report));

            }
        });
    }






    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

        private static final String TAG = "MenuAdapter";

        ArrayList<String> cnames = new ArrayList<>();

        Dialog mybuilder;

        public MenuAdapter(ArrayList<String> cnames, Dialog mybuilder) {
            super();
            this.cnames = cnames;
            this.mybuilder = mybuilder;
        }



        @Override
        public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.menu_item_2, viewGroup, false);
            return new MenuAdapter.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final MenuAdapter.ViewHolder viewHolder, final int i) {

            viewHolder.textViewName.setText(""+cnames.get(i));
            viewHolder.realtive1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mybuilder.dismiss();
                    selectcompany1.setText(cnames.get(i));
                    selectedCompanyId = cids.get(i);

                    customer_list(selectedCompanyId);
                    supplier_list(selectedCompanyId);
                    product_list(selectedCompanyId);

                }
            });

        }


        @Override
        public int getItemCount() {
            return cnames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            View view11 = null;
            TextView textViewName;
            RelativeLayout realtive1;
            public ViewHolder(View itemView) {
                super(itemView);
                view11 = itemView;
                realtive1 = (RelativeLayout) itemView.findViewById(R.id.realtive1);
                textViewName = (TextView) itemView.findViewById(R.id.txtList);
            }

        }



        public void updateData(ArrayList<String> cnames) {
            // TODO Auto-generated method stub
            this.cnames = cnames;
            notifyDataSetChanged();
        }


    }





}
