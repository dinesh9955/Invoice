package com.sirapp.Stock;


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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.Adapter.Select_Warehouse_Adapter;
import com.sirapp.Constant.Constant;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseFragment;
import com.sirapp.Home.GoProActivity;
import com.sirapp.R;
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
public class Update_Stock extends BaseFragment implements Select_Warehouse_Adapter.Callback {


    private static final String TAG = "Update_Stock";

    public Update_Stock() {
        // Required empty public constructor
    }

    TextView description,selectwarehouse,quantitydescription,pricedescription;
    Button update;
    EditText quantity,price;
    AwesomeSpinner vendorspinner;

    Button selectcompany1;

    Button productcategory1;

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
//        companyspinner.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
//        companyspinner.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
//        productspinner.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
//        productspinner.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
        vendorspinner.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        vendorspinner.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStock();
            }
        });

//        companyspinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
//            @Override
//            public void onItemSelected(int position, String itemAtPosition) {
//                selectedCompanyId = cids.get(position);
//                Log.e("selectedCompany",selectedCompanyId);
//                productget(selectedCompanyId);
//
//            }
//        });


        selectcompany1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView mRecyclerView;
                MenuAdapter2 mAdapter;

                final Dialog mybuilder = new Dialog(getActivity());
                mybuilder.setContentView(R.layout.select_company_dialog_3);


                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter2(cnames, mybuilder);
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



//        productspinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
//            @Override
//            public void onItemSelected(int position, String itemAtPosition) {
//                selectedProductId = pids.get(position);
//                Log.e("selectedProduct",selectedProductId);
//                price.setText(pprice.get(position));
//            }
//        });




        productcategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView mRecyclerView;
                MenuAdapter mAdapter;

                final Dialog mybuilder = new Dialog(getActivity());
                mybuilder.setContentView(R.layout.select_company_dialog_2);


                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter(pnames, mybuilder);
                mRecyclerView.setAdapter(mAdapter);

                mybuilder.show();
                mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                Window window = mybuilder.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(R.color.transparent);
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
        selectcompany1 = v.findViewById(R.id.selectcompany2);
     //   productspinner = v.findViewById(R.id.productspinner);
        productcategory1 = v.findViewById(R.id.productcategory1);
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
            Constant.ErrorToast(getActivity(),getActivity().getString(R.string.select_company));
        }
        else{
            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.add("company_id",id);
            String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.addHeader("Access-Token",token);
            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL + "warehouse/listing", params, new AsyncHttpResponseHandler() {
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
                        //Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
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
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);
        RequestParams params = new RequestParams();
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL+"company/listing", params, new AsyncHttpResponseHandler() {
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

//                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,cnames);
//                                companyspinner.setAdapter(namesadapter);

                            }
                        }



                        if(company.length() == 1){
                            selectedCompanyId = cids.get(0);
                            Log.e("selectedCompany",selectedCompanyId);
                            selectcompany1.setText(cnames.get(0));
                            productget(selectedCompanyId);
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
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);
        RequestParams params = new RequestParams();
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL+"supplier/listing", params, new AsyncHttpResponseHandler() {
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
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL+"product/getOnlyProductsListingByCompany",params, new AsyncHttpResponseHandler() {
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
//
//                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,pnames);
//                                productspinner.setAdapter(namesadapter);

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
                    //Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                }

            }
        });
    }

    private void UpdateStock(){

        String quant = quantity.getText().toString();
        String pricee = price.getText().toString();
        if (quant.isEmpty()){
            quantity.setError(getString(R.string.dialog_Required));
            quantity.requestFocus();
        }
//        else if (pricee.isEmpty()){
//            price.setError(getString(R.string.dialog_Required));
//            price.requestFocus();
//        }
        else if (selectedProductId.equals("")){
            Constant.ErrorToast(getActivity(),getString(R.string.dialog_SelectAProduct));
        }
//       /* else if (selectedVendorId.equals("")){
//            Constant.ErrorToast(getActivity(),"Select a Vendor");
//
//        }*/
        else if (warehouses.equalsIgnoreCase("")){
            Constant.ErrorToast(getActivity(),getString(R.string.stock_Select_Warehouse));
        }
        else{
            Log.e(TAG, "UpdateStock");
            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);

            RequestParams params = new RequestParams();
            params.add("product_id", ""+selectedProductId);
            params.add("quantity", ""+quant);
            params.add("price", ""+pricee);
            params.add("warehouse_id[0]", ""+warehouses);
          //  params.add("supplier_id",selectedVendorId);

            Log.e(TAG, "quantAAA "+quant +" "+params.toString());

            String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.addHeader("Access-Token",token);
            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL + "product/addQuantity", params, new AsyncHttpResponseHandler() {
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

                            Bundle params2 = new Bundle();
                            params2.putString("event_name", "Stocks Update");
                            firebaseAnalytics.logEvent("stocks_update", params2);

                            Constant.SuccessToast(getActivity(), getActivity().getString(R.string.dialog_UpdatedStock));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getContext(),Stock_Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            },1000);
                        }


                        if (status.equals("false")){
                            Constant.ErrorToast(getActivity(),jsonObject.getString("message"));

                            if( jsonObject.has("code")){
                                String code = jsonObject.getString("code");

                                if(code.equalsIgnoreCase("subscription")){
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getActivity(), GoProActivity.class);
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
                       // Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                    }

                }
            });
        }




    }

    @Override
    public void onWarehouseSelected(String warehouseList, String wherehousenamstrSelect) {

        mybuilder.dismiss();

        if(wherehousenamstrSelect.equalsIgnoreCase("")){
            selectwarehouse.setText(getString(R.string.stock_SelectWarehouse));
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
                    selectedProductId = pids.get(i);
                    productcategory1.setText(cnames.get(i));
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



    public class MenuAdapter2 extends RecyclerView.Adapter<MenuAdapter2.ViewHolder> {

        private static final String TAG = "MenuAdapter";

        ArrayList<String> cnames = new ArrayList<>();

        Dialog mybuilder;

        public MenuAdapter2(ArrayList<String> cnames, Dialog mybuilder) {
            super();
            this.cnames = cnames;
            this.mybuilder = mybuilder;
        }



        @Override
        public MenuAdapter2.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.menu_item_2, viewGroup, false);
            return new MenuAdapter2.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final MenuAdapter2.ViewHolder viewHolder, final int i) {

            viewHolder.textViewName.setText(""+cnames.get(i));
            viewHolder.realtive1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mybuilder.dismiss();
                    selectedCompanyId = cids.get(i);
                    selectcompany1.setText(cnames.get(i));
                    productget(selectedCompanyId);
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
