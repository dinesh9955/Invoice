package com.receipt.invoice.stock.sirproject.Report;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Adapter.Customer_Bottom_Adapter;
import com.receipt.invoice.stock.sirproject.Adapter.Product_Bottom_Adapter;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Customer.Customer_Activity;
import com.receipt.invoice.stock.sirproject.Model.Customer_list;
import com.receipt.invoice.stock.sirproject.Model.Product_list;
import com.receipt.invoice.stock.sirproject.Product.Product_Activity;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.RetrofitApi.ApiInterface;
import com.receipt.invoice.stock.sirproject.RetrofitApi.RetrofitInstance;
import com.receipt.invoice.stock.sirproject.Vendor.Vendor_Activity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ReportActivity extends BaseActivity implements Customer_Bottom_Adapter.Callback, Product_Bottom_Adapter.Callback{
    private static final String TAG = "ReportActivity" ;
    ApiInterface apiInterface;
    private AVLoadingIndicatorView avi;

    AwesomeSpinner selectcompany;

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

        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Constant.toolbar(ReportActivity.this, "Report");


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        avi = findViewById(R.id.avi);
        recycler_invoices = findViewById(R.id.recycler_invoices);
        selectcompany = findViewById(R.id.selectcompany);



        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
//                companyName = cids.get(position);
//                companyAddress = cAddress.get(position);
//                companyContactNo = ccontactNo.get(position);
//                companyWebsite = cWebsite.get(position);
//                companyEmail = cEmail.get(position);
//                companyLogo = cLogo.get(position);

//                Log.e(TAG, "companyLogo"+companyLogo);

                customer_list(selectedCompanyId);
                supplier_list(selectedCompanyId);
                product_list(selectedCompanyId);
            }
        });


        arrayListNames.add("Customer Statement of Account");
        arrayListNames.add("Supplier Statement of Account");
        arrayListNames.add("Total Sales Report");
        arrayListNames.add("Total Purchase Report");
        arrayListNames.add("Customer Ageing Report");
        arrayListNames.add("Tax Collected Report");
        arrayListNames.add("Stock Report");
        arrayListNames.add("Product Movement Report");

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
        String token = Constant.GetSharedPreferences(ReportActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "company/listing", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("responsecompany", response);
                avi.smoothToHide();
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

                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(ReportActivity.this, android.R.layout.simple_spinner_item, cnames);
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
                    avi.smoothToHide();
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
        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);

        String token = Constant.GetSharedPreferences(ReportActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
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



    public void onClickAdapter(int i) {
        Log.e(TAG, "sssss "+i);
        positionNext = i;
        bottomSheetDialog = new BottomSheetDialog(ReportActivity.this);
        if (selectedCompanyId.equals("")) {
            Constant.ErrorToast(ReportActivity.this, "Select A Company");
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
                Intent intent = new Intent(ReportActivity.this, ReportViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("positionNext", positionNext);
                intent.putExtra("company_id", selectedCompanyId);
                intent.putExtra("company_image_path", company_image_path);
                startActivity(intent);
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
            txtcustomer.setText("Select Supplier");
            search_customers = view.findViewById(R.id.search_customers);
            search_customers.setHint("Select Suppliers");
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
            txtcustomer.setText("Select Product");
            search_customers = view.findViewById(R.id.search_customers);
            search_customers.setHint("Select Products");
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
        for (Customer_list d : customer_bottom) {
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

        Intent intent = new Intent(ReportActivity.this, ReportViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("titleName", titleName);

        intent.putExtra("positionNext", positionNext);

        if(positionNext == 0){
            intent.putExtra("customer_id", customer_id);
        } else if(positionNext == 1){
            intent.putExtra("supplier_id", customer_id);
        } else if(positionNext == 7){
            intent.putExtra("product_id", customer_id);
        } else{
            intent.putExtra("company_id", selectedCompanyId);
        }

        startActivity(intent);
    }



    @Override
    public void onPostExecutecall(Product_list selected_item, String s, String price) {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }

        String customer_id = selected_item.getProduct_id();

        Log.e(TAG, "customer_id "+customer_id);
        //Log.e(TAG, "customer_name "+customer_name);

        Intent intent = new Intent(ReportActivity.this, ReportViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("titleName", titleName);

        intent.putExtra("positionNext", positionNext);

        if(positionNext == 7){
            intent.putExtra("product_id", customer_id);
//            intent.putExtra("companyName", companyName);
//            intent.putExtra("companyAddress", companyAddress);
//            intent.putExtra("companyContactNo", companyContactNo);
//            intent.putExtra("companyWebsite", companyWebsite);
//            intent.putExtra("companyEmail", companyEmail);
//            intent.putExtra("companyLogo", companyLogo);
        }

        startActivity(intent);
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
}
