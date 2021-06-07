package com.receipt.invoice.stock.sirproject.Stock;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.TextView;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.Adapter.Select_Warehouse_Adapter;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Home.Home_Activity;
import com.receipt.invoice.stock.sirproject.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Update_Stock extends Fragment implements Select_Warehouse_Adapter.Callback{


    private static final String TAG = "Update_Stock";

    public Update_Stock() {
        // Required empty public constructor
    }

    TextView description,selectwarehouse,quantitydescription,pricedescription;
    Button update;
    EditText quantity,price;
    AwesomeSpinner companyspinner,productspinner,vendorspinner;
    RecyclerView recyclerwarehouse;
    ArrayList<String> warehousename=new ArrayList<>();
    ArrayList<String> warehouseids=new ArrayList<>();

    Select_Warehouse_Adapter select_Warehouse_Adapter;
    Dialog mybuilder;
    TextView select;

    String selectedCompanyId="";
    String selectedProductId="";
    String selectedVendorId="";

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    ArrayList<String> cnames=new ArrayList<>();
    ArrayList<String> cids=new ArrayList<>();

    ArrayList<String> pnames=new ArrayList<>();
    ArrayList<String> pids=new ArrayList<>();
    ArrayList<String> pprice=new ArrayList<>();

    ArrayList<String> vnames=new ArrayList<>();
    ArrayList<String> vids=new ArrayList<>();

//    ArrayList<String> warehouses=new ArrayList<>();

    String warehouses =  "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View v =  inflater.inflate(R.layout.fragment_update__stock, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FindByIds(v);
        setFonts();

        companyget();
        //();

        //spinners
        companyspinner.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        companyspinner.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
        productspinner.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        productspinner.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
        vendorspinner.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        vendorspinner.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStock();
            }
        });

        companyspinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                Log.e("selectedCompany",selectedCompanyId);
                productget(selectedCompanyId);

            }
        });

        productspinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedProductId = pids.get(position);
                Log.e("selectedProduct",selectedProductId);
                price.setText(pprice.get(position));
            }
        });

        vendorspinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedVendorId = vids.get(position);
                Log.e("selectedvendor",selectedVendorId);
            }
        });

        selectwarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WareHouseDialog(selectedCompanyId);
            }
        });

        return v;
    }

    private void FindByIds(View v){
        description = v.findViewById(R.id.description);
        selectwarehouse = v.findViewById(R.id.selectwarehouse);
        quantitydescription = v.findViewById(R.id.quantitydescription);
        pricedescription = v.findViewById(R.id.pricedescription);
        update = v.findViewById(R.id.update);
        quantity = v.findViewById(R.id.quantity);
        price = v.findViewById(R.id.price);
        companyspinner = v.findViewById(R.id.companyspinner);
        productspinner = v.findViewById(R.id.productspinner);
        vendorspinner = v.findViewById(R.id.vendorspinner);
        avi = v.findViewById(R.id.avi);
        avibackground = v.findViewById(R.id.avibackground);
    }

    private void setFonts(){
        description.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Regular.otf"));

        selectwarehouse.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Medium.ttf"));

        quantitydescription.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        pricedescription.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));

        update.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Medium.ttf"));
        quantity.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        price.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
    }

    public void WareHouseDialog(String id) {
        warehousename.clear();

        mybuilder = new Dialog(getActivity());
        mybuilder.setContentView(R.layout.warehouse_dailog);
        mybuilder.setCancelable(false);
        recyclerwarehouse = mybuilder.findViewById(R.id.recycler_warehouse);
        select = mybuilder.findViewById(R.id.select);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerwarehouse.setLayoutManager(layoutManager);
        recyclerwarehouse.setHasFixedSize(true);

        if (id.equals("")){
            Constant.ErrorToast(getActivity(),"Select Company");
        }
        else{
            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.add("company_id",id);
            String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Access-Token",token);
            client.post(Constant.BASE_URL + "warehouse/listing", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.e("warehouseResp",response);
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("true")){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray warehouse = data.getJSONArray("warehouse");
                            if (warehouse.length()>0){
                                for (int i=0; i<warehouse.length(); i++){
                                    JSONObject item = warehouse.getJSONObject(i);
                                    String warehouse_id = item.getString("warehouse_id");
                                    String name = item.getString("name");
                                    warehousename.add(name);
                                    warehouseids.add(warehouse_id);

                                    select_Warehouse_Adapter = new Select_Warehouse_Adapter(getContext(),warehousename,warehouseids,select,Update_Stock.this);
                                    recyclerwarehouse.setAdapter(select_Warehouse_Adapter);
                                    select_Warehouse_Adapter.notifyDataSetChanged();
                                }

                                mybuilder.show();
                                mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                Window window = mybuilder.getWindow();
                                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                window.setBackgroundDrawableResource(R.color.transparent);
                            }
                        }
                    } catch (JSONException e){
                       e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    if(responseBody!=null) {
                        String response = new String(responseBody);
                        Log.e("warehouseRespF",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("false")){

                                Constant.ErrorToast(getActivity(),jsonObject.getString("message"));
                                //warehouses.clear();
                                warehouses = "";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                    }

                }
            });

        }






    }

    public void companyget()
    {
        cnames.clear();
        cids.clear();
        final SharedPreferences preferences = getActivity().getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token",token);
        client.post(Constant.BASE_URL+"company/listing", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("responsecompany",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray company = data.getJSONArray("company");
                        if (company.length()>0) {
                            for (int i = 0; i < company.length(); i++) {
                                JSONObject item = company.getJSONObject(i);
                                String company_id = item.getString("company_id");
                                String company_name = item.getString("name");

                                cnames.add(company_name);
                                cids.add(company_id);

                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,cnames);
                                companyspinner.setAdapter(namesadapter);

                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(responseBody!=null){
                    String response = new String(responseBody);
                    Log.e("responsecompanyF",response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("false"))
                        {
                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void vendorget()
    {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token",token);
        client.post(Constant.BASE_URL+"supplier/listing", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("responsevendor",response);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String vendor_image_path = data.getString("supplier_image_path");
                        JSONArray vendor = data.getJSONArray("supplier");
                        if (vendor.length()>0) {
                            for (int i = 0; i < vendor.length(); i++) {
                                JSONObject item = vendor.getJSONObject(i);
                                String vendor_id = item.getString("supplier_id");
                                String vendor_name = item.getString("contact_name");

                                vnames.add(vendor_name);
                                vids.add(vendor_id);

                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,vnames);
                                vendorspinner.setAdapter(namesadapter);


                            }
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
                if(responseBody!=null){
                    String response = new String(responseBody);
                    Log.e("responsevendorF",response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("false"))
                        {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void productget(String companyid)
    {
        pnames.clear();
        pids.clear();
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.add("company_id",companyid);
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token",token);
        client.post(Constant.BASE_URL+"product/getOnlyProductsListingByCompany",params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("responseproduct",response);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray product = data.getJSONArray("product");
                        if (product.length()>0) {
                            for (int i = 0; i < product.length(); i++) {
                                JSONObject item = product.getJSONObject(i);
                                String product_id = item.getString("product_id");
                                String product_name = item.getString("name");
                                String price = item.getString("price");

                                pnames.add(product_name);
                                pids.add(product_id);
                                pprice.add(price);

                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,pnames);
                                productspinner.setAdapter(namesadapter);

                            }
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
                if(responseBody!=null){
                    String response = new String(responseBody);
                    Log.e("responseproductF",response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("false"))
                        {
                            Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                            selectedProductId="";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                }

            }
        });
    }

    private void UpdateStock(){

        String quant = quantity.getText().toString();
        String pricee = price.getText().toString();
        if (quant.isEmpty()){
            quantity.setError("Required");
            quantity.requestFocus();
        }
        else if (pricee.isEmpty()){
            price.setError("Required");
            price.requestFocus();
        }
        else if (selectedProductId.equals("")){
            Constant.ErrorToast(getActivity(),"Select A Product");

        }
       /* else if (selectedVendorId.equals("")){
            Constant.ErrorToast(getActivity(),"Select a Vendor");

        }*/
        else if (warehouses.equalsIgnoreCase("")){
            Constant.ErrorToast(getActivity(),"Select Warehouse");
        }
        else{
            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);

            RequestParams params = new RequestParams();
            params.add("product_id",selectedProductId);
            params.add("quantity",quant);
            params.add("price",pricee);
            params.add("warehouse_id[0]", ""+warehouses);
            params.add("supplier_id",selectedVendorId);

            Log.e(TAG, "quantAAA "+quant +" "+params.toString());

            String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Access-Token",token);
            client.post(Constant.BASE_URL + "product/addQuantity", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.e("updatestockResp",response);
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("true"))
                        {

                            Map<String, Object> eventValue = new HashMap<String, Object>();
                            eventValue.put(AFInAppEventParameterName.PARAM_1, "stocks_update");
                            AppsFlyerLib.getInstance().trackEvent(getActivity(), "stocks_update", eventValue);

                            Constant.SuccessToast(getActivity(),"Updated Stock");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getContext(),Stock_Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            },1500);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    if(responseBody!=null) {
                        String response = new String(responseBody);
                        Log.e("updatestockRespF",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("false"))
                            {
                                Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                    }

                }
            });
        }




    }

    @Override
    public void onWarehouseSelected(String warehouseList, String wherehousenamstrSelect) {

        mybuilder.dismiss();

        if(wherehousenamstrSelect.equalsIgnoreCase("")){
            selectwarehouse.setText("Select Warehouse (s)");
        }else {
            selectwarehouse.setText(warehouseList);
        }

        Log.e(TAG, "warehouseList "+wherehousenamstrSelect);

        warehouses = wherehousenamstrSelect;

    }

//
//        @Override
//    public void onWarehouseSelected(int count, ArrayList<String> warehouseList, String wherehousenamstr) {
//
//            mybuilder.dismiss();
//            if (count!=0){
//                selectwarehouse.setText(wherehousenamstr);
//            }
//            else {
//                selectwarehouse.setText("Select Warehouse (s)");
//            }
//            warehouses.clear();
//            warehouses = warehouseList;
//
//
//        }


}
