package com.receipt.invoice.stock.sirproject.Home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.receipt.invoice.stock.sirproject.Home.Model.CompanyModel;
import com.receipt.invoice.stock.sirproject.Home.Model.InvoiceModel;
import com.receipt.invoice.stock.sirproject.Adapter.Invoice_OverDue_Adapter;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Customer.Customer_Activity;
import com.receipt.invoice.stock.sirproject.Home.Model.CustomerModel;
import com.receipt.invoice.stock.sirproject.Home.Model.SupplierModel;
import com.receipt.invoice.stock.sirproject.Home.adapter.HomeCustomerAdapter;
import com.receipt.invoice.stock.sirproject.Home.adapter.HomeInvoiceAdapter;
import com.receipt.invoice.stock.sirproject.Home.adapter.HomeSupplierAdapter;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Vendor.Vendor_Activity;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.API.GetAsyncPost;
import com.receipt.invoice.stock.sirproject.API.Parameters;
import com.receipt.invoice.stock.sirproject.API.SavePref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home_Activity extends AppCompatActivity implements MenuDelegate{

    String TAG = "Home_Activity";

    RecyclerView businessactivitiesRV, invoiceoverdueRV, recycleCustomers,recyclerVinder;
   // Invoice_OverDue_Adapter invoice_overDue_adapter;
//    HomeCustomerAdapter homeCustomerAdapter;
//    HomeSupplierAdapter homeSupplierAdapter;
//    HomeInvoiceAdapter homeInvoiceAdapter;

    ImageView menu;
    TextView hello, description, customerstxt, seeall, vendorstxt, vendorsseeall;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    ProgressDialog dialog;

    SavePref savePref;
    TextView no_customer;
    TextView txtNo, no_overduetxt;
    TextView txtinvoice;
    String compantId;

    public ArrayList<CompanyModel> companyModelArrayList = new ArrayList<>();

    public ArrayList<InvoiceModel> invoiceModelArrayList = new ArrayList<>();
    public ArrayList<InvoiceModel> invoiceDueDateModelArrayList = new ArrayList<>();

    public ArrayList<CustomerModel> customerModelArrayList = new ArrayList<>();
    public ArrayList<SupplierModel> supplierModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);

        savePref = new SavePref(Home_Activity.this);

        if (getIntent().hasExtra("login")) {
            messagebar();
        }

        Constant.bottomNav(Home_Activity.this, -1);
        Constant.toolbar(Home_Activity.this, "");
        FindByIds();
        setListeners();
        setFonts();


        Toolbar toolbar = findViewById(R.id.toolbar);

        ImageView imageViewptint = toolbar.findViewById(R.id.imageViewptint);
        imageViewptint.setImageResource(R.drawable.search_2);
        imageViewptint.setVisibility(View.VISIBLE);
        imageViewptint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView mRecyclerView;
                MenuAdapter mAdapter;

                final Dialog mybuilder = new Dialog(Home_Activity.this);
                mybuilder.setContentView(R.layout.dialog_list);

                TextView txtcancelvalue = (TextView) mybuilder.findViewById(R.id.txtcancelvalue);
                txtcancelvalue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mybuilder.dismiss();
                    }
                });
                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(Home_Activity.this, LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter(companyModelArrayList, mybuilder);
                mRecyclerView.setAdapter(mAdapter);

                mybuilder.show();
                mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                Window window = mybuilder.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(R.color.transparent);
            }
        });

        String username = Constant.GetSharedPreferences(getApplicationContext(), Constant.FULLNAME);

        if (username.equals("")) {

            hello.setText("Hello");

        } else {

            hello.setText("Hello " + username);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "company_id"+compantId);
//        if(compantId != null){
//            HomeApi(compantId);
//        }

        COMPANYListingApi();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       // HomeApi(compantId);
    }

    private void FindByIds() {

        businessactivitiesRV = findViewById(R.id.businessactivitiesRV);
        invoiceoverdueRV = findViewById(R.id.invoiceoverdueRV);
        recycleCustomers = findViewById(R.id.recycleCustomers);
        recyclerVinder = findViewById(R.id.recyclerVinder);
        no_customer = findViewById(R.id.no_customer);
        txtNo = findViewById(R.id.txtNo);
        no_overduetxt = findViewById(R.id.no_overduetxt);

        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);

        menu = findViewById(R.id.menu);
        hello = findViewById(R.id.hello);
        description = findViewById(R.id.description);
        customerstxt = findViewById(R.id.customerstxt);
        seeall = findViewById(R.id.seeall);
        vendorstxt = findViewById(R.id.vendorstxt);
        vendorsseeall = findViewById(R.id.vendorsseeall);
        txtinvoice = findViewById(R.id.txtinvoice);


        COMPANYListingApi();



    }

    private void setListeners() {

        seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home_Activity.this, Customer_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        vendorsseeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home_Activity.this, Vendor_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Comming Soon", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setFonts() {

        hello.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Bold.otf"));
        description.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Light.otf"));
        customerstxt.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Light.otf"));
        seeall.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Light.otf"));
        vendorstxt.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Light.otf"));
        vendorsseeall.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Light.otf"));

    }

    private void HomeApi(String company_id) {

        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.COMPANY_ID, company_id);
        RequestBody formBody = formBuilder.build();
        GetAsyncPost mAsync = new GetAsyncPost(Home_Activity.this, AllSirApi.Home, formBody, dialog, "") {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void getValueParse(Response response) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

                String result = "";
                customerModelArrayList = new ArrayList<>();
                try {

                    result = response.body().string();
                    Log.e(TAG, "result:: "+result);

                } catch (Exception e) {

                    e.printStackTrace();

                }

              // Log.e("HomeResponse", "result " + result);

                if (response.isSuccessful()) {

                    try {

                        JSONObject jsonObject = new JSONObject(result);

                        JSONObject object_data = jsonObject.getJSONObject("data");
                     String supplier_image_path=object_data.getString("supplier_image_path");
                        String customer_image_path=object_data.getString("customer_image_path");
                       Log.e("customer_image_path",customer_image_path);
                        Log.e("supplier_image_path",supplier_image_path);



                        JSONArray customer_array = object_data.getJSONArray("customer");
                        //Log.e("detail Image", "result " + customer_array);

                        customerModelArrayList.clear();
                        for (int j = 0; j <customer_array.length(); j++) {

                            JSONObject obj_customer = customer_array.getJSONObject(j);

                            CustomerModel customerModel = new CustomerModel();

                            customerModel.setCustomerId(obj_customer.getString("customer_id"));
                            customerModel.setCustomerName(obj_customer.getString("customer_name"));
                            customerModel.setImage(obj_customer.getString("image"));
                            customerModel.setCustomer_image_path(customer_image_path);
                            String strresult=obj_customer.getString("customer_name");


                            //Log.e("CompanyList Image", "result " + strresult);
                            customerModelArrayList.add(customerModel);
                        }


                        JSONArray invoice_array = object_data.getJSONArray("invoice");

                        invoiceModelArrayList.clear();
                        for (int i=0; i<invoice_array.length(); i++){

                            JSONObject obj_invoice = invoice_array.getJSONObject(i);

                            InvoiceModel invoiceModel=new InvoiceModel();

                            invoiceModel.setInvoice_id(obj_invoice.getString("invoice_id"));
                            invoiceModel.setInvoice_no(obj_invoice.getString("invoice_no"));
                            invoiceModel.setTotal(obj_invoice.getString("total"));
                            invoiceModel.setPayment_currency(obj_invoice.getString("currency_symbol"));
                            invoiceModelArrayList.add(invoiceModel);

                        }



                        invoiceDueDateModelArrayList.clear();
                        for (int i=0; i<invoice_array.length(); i++){
                            JSONObject obj_invoice = invoice_array.getJSONObject(i);
                            InvoiceModel invoiceModel=new InvoiceModel();
                            invoiceModel.setInvoice_id(obj_invoice.getString("invoice_id"));
                            invoiceModel.setInvoice_no(obj_invoice.getString("invoice_no"));
                            invoiceModel.setTotal(obj_invoice.getString("total"));
                            invoiceModel.setPayment_currency(obj_invoice.getString("currency_symbol"));
                            String customer_name = obj_invoice.getString("customer_name");
                            invoiceModel.setCustomer_name(customer_name);

                          //  invoiceModel.setCustomer_name(customer_name);
                            String customer_id = obj_invoice.getString("customer_id");
                            String imageSS = getCustomerImageById(customerModelArrayList, customer_id);
                            Log.e(TAG, "imageSS "+imageSS);
                            invoiceModel.setCustomer_logo(customer_image_path+imageSS);

                            String due_date = obj_invoice.getString("due_date");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date date = sdf.parse(due_date);
                                long millis = date.getTime();
                              //  Log.e(TAG, "millisAAA "+millis);


                                String todayCurrent = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                Date dateCurrent = sdf.parse(todayCurrent);
                                long millisCurrent = dateCurrent.getTime();
                             //   Log.e(TAG, "dateCurrentAAA "+millisCurrent);

                                if(millisCurrent > millis){
                                    String order_status_id = obj_invoice.getString("order_status_id");
                                    if(order_status_id.equalsIgnoreCase("1")){
                                        invoiceDueDateModelArrayList.add(invoiceModel);
                                    }
                                }

                            }catch (Exception e){

                            }


                        }







                        JSONArray supplier_array = object_data.getJSONArray("supplier");

                        supplierModelArrayList.clear();
                        for (int k=0; k<supplier_array.length(); k++){

                            JSONObject obj_supplier = supplier_array.getJSONObject(k);

                            SupplierModel supplierModel=new SupplierModel();

                            supplierModel.setSupplierId(obj_supplier.getString("supplier_id"));
                            supplierModel.setSupplierName(obj_supplier.getString("supplier_name"));
                            supplierModel.setImage(obj_supplier.getString("image"));
                            supplierModel.setSupplier_image_path(supplier_image_path);

                            String strresult1=obj_supplier.getString("image");
                          //  Log.e("CompanyList Image", "result " + strresult1);


                            supplierModelArrayList.add(supplierModel);

                        }

                        businessactivitiesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        HomeInvoiceAdapter homeInvoiceAdapter = new HomeInvoiceAdapter(Home_Activity.this);
                        businessactivitiesRV.setAdapter(homeInvoiceAdapter);

                        if (invoiceModelArrayList.size()>0) {
                            txtinvoice.setVisibility(View.GONE);
                        } else {
                            txtinvoice.setVisibility(View.VISIBLE);
                        }

                        homeInvoiceAdapter.setData(invoiceModelArrayList);



                        invoiceoverdueRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        Invoice_OverDue_Adapter invoice_overDue_adapter = new Invoice_OverDue_Adapter(Home_Activity.this);
                        invoiceoverdueRV.setAdapter(invoice_overDue_adapter);

                        if (invoiceDueDateModelArrayList.size()>0) {
                            no_overduetxt.setVisibility(View.GONE);
                        } else {
                            no_overduetxt.setVisibility(View.VISIBLE);
                        }

                        invoice_overDue_adapter.setData(invoiceDueDateModelArrayList);




                        int numberOfColumns = 3;
                        recyclerVinder.setLayoutManager(new GridLayoutManager(Home_Activity.this, numberOfColumns));
                        HomeSupplierAdapter homeSupplierAdapter = new HomeSupplierAdapter(Home_Activity.this);
                        recyclerVinder.setAdapter(homeSupplierAdapter);

                        if (supplierModelArrayList.size()>0) {
                            txtNo.setVisibility(View.GONE);
                        } else {
                            txtNo.setVisibility(View.VISIBLE);
                        }
                        homeSupplierAdapter.setData(supplierModelArrayList);


                        int numberOfColumns2 = 3;
                        recycleCustomers.setLayoutManager(new GridLayoutManager(Home_Activity.this, numberOfColumns2));
                        HomeCustomerAdapter homeCustomerAdapter = new HomeCustomerAdapter(Home_Activity.this);

                        recycleCustomers.setAdapter(homeCustomerAdapter);

                        if (customerModelArrayList.size()>0) {
                            no_customer.setVisibility(View.GONE);
                         } else {
                            no_customer.setVisibility(View.VISIBLE);
                        }
                        homeCustomerAdapter.setData(customerModelArrayList);


                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                } else {

                    try {

                        JSONObject jsonObject = new JSONObject(result);
                       // util.showToast(Home_Activity.this, jsonObject.getString("message"));

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void retry() {

            }
        };

        mAsync.execute();

    }

    private String getCustomerImageById(ArrayList<CustomerModel> customerModelArrayList, String customer_id) {
        String path = "";
        if(customerModelArrayList.size() > 0){
            for (int k=0; k<customerModelArrayList.size(); k++){
                if(customerModelArrayList.get(k).getCustomerId().equalsIgnoreCase(customer_id)){
                    path = customerModelArrayList.get(k).getImage().toString();
                }
            }
        }
        return path;
    }

    private void COMPANYListingApi() {

        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        FormBody.Builder formBuilder = new FormBody.Builder();
        RequestBody formBody = formBuilder.build();
        GetAsyncPost mAsync = new GetAsyncPost(Home_Activity.this, AllSirApi.COMPANYListing, formBody, dialog, "") {
            @Override
            public void getValueParse(Response response) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                String result = "";
                companyModelArrayList = new ArrayList<>();

                try {

                    result = response.body().string();

                } catch (Exception e) {

                    e.printStackTrace();

                }

                Log.e("CompanyListResponse", "result " + result);

                if (response.isSuccessful()) {

                    try {

                        JSONObject jsonObject = new JSONObject(result);

                        JSONObject object_data = jsonObject.getJSONObject("data");

                        JSONArray invoice_array = object_data.getJSONArray("company");

                        for (int i=0; i<invoice_array.length(); i++){

                            JSONObject obj_invoice = invoice_array.getJSONObject(i);

                            CompanyModel companyModel=new CompanyModel();
                            companyModel.setCompany_name(obj_invoice.getString("name"));
                            companyModel.setCompany_id(obj_invoice.getString("company_id"));

                            compantId=obj_invoice.getString("company_id");
                            Log.e("Company Id",compantId);

                            companyModelArrayList.add(companyModel);

                            if(i == 0){
                                description.setText(""+obj_invoice.getString("name"));
                                HomeApi(compantId);
                            }



                        }


                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                } else {

                    try {

                        JSONObject jsonObject = new JSONObject(result);
                       // util.showToast(Home_Activity.this, jsonObject.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                if (invoiceModelArrayList.size()>0) {
                    txtinvoice.setVisibility(View.GONE);
                } else {
                    txtinvoice.setVisibility(View.VISIBLE);
                }

                if (invoiceDueDateModelArrayList.size()>0) {
                    no_overduetxt.setVisibility(View.GONE);
                } else {
                    no_overduetxt.setVisibility(View.VISIBLE);
                }

                if (customerModelArrayList.size()>0) {
                    txtNo.setVisibility(View.GONE);
                } else {
                    txtNo.setVisibility(View.VISIBLE);
                }

                if (supplierModelArrayList.size()>0) {
                    no_customer.setVisibility(View.GONE);
                } else {
                    no_customer.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void retry() {
            }
        };

        mAsync.execute();

    }


    Boolean twice = false;

    @Override
    public void onBackPressed() {
        if (twice == true) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        twice = true;
        Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        }, 3000);

    }

    public void messagebar() {
        new Flashbar.Builder(Home_Activity.this)
                .gravity(Flashbar.Gravity.BOTTOM)
                .title("\nSuccess")
                .titleTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Bold.otf"))
                .titleSizeInSp(15)
                .message("Logged in successfully!")
                //.messageTypeface(Typeface.createFromAsset(getAssets(),"fonts/CatamaraBold.ttf"))
                .backgroundDrawable(getResources().getDrawable(R.drawable.success_toast_background))
                .duration(3000)
                .enableSwipeToDismiss()
                .castShadow(false)
                .enterAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate())
                .build().show();
    }

    @Override
    public void onMenuItemClick(String menuName) {

    }





    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

        private static final String TAG = "MenuAdapter";

        ArrayList<CompanyModel> alName = new ArrayList<>();
        Dialog mybuilder;

        public MenuAdapter(ArrayList<CompanyModel> arrayList, Dialog mybuilder) {
            super();
            this.alName = arrayList;
           this.mybuilder = mybuilder;
        }

        @Override
        public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.menu_item, viewGroup, false);
//            SellerFeedbackAdapter.ViewHolder viewHolder = new SellerFeedbackAdapter.ViewHolder(v);
//            return viewHolder;


//            val v = LayoutInflater.from(viewGroup.context)
//                    .inflate(R.layout.mybooking_item, viewGroup, false)
            return new MenuAdapter.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final MenuAdapter.ViewHolder viewHolder, final int i) {

            viewHolder.textViewName.setText(""+alName.get(i).getCompany_name());
            viewHolder.textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mybuilder.dismiss();
                    description.setText(""+alName.get(i).getCompany_name());
                    HomeApi(alName.get(i).company_id);
                }
            });

        }


        @Override
        public int getItemCount() {
            return alName.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            View view11 = null;
            TextView textViewName;
            public ViewHolder(View itemView) {
                super(itemView);
                view11 = itemView;
                //  imageViewImage = (ImageView) itemView.findViewById(R.id.profile_image);
                textViewName = (TextView) itemView.findViewById(R.id.txtList);


            }



        }



        public void updateData(ArrayList<CompanyModel> arrayList2) {
            // TODO Auto-generated method stub
            alName = arrayList2;
            notifyDataSetChanged();
        }


    }




}
