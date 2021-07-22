package com.sirapp.Details;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.Constant.Constant;
import com.sirapp.API.AllSirApi;
import com.sirapp.Adapter.Warehouses_Listing_Adapter;
import com.sirapp.Base.BaseFragment;
import com.sirapp.Model.Warehouse_list;
import com.sirapp.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class Warehouse_Listing_Fragment extends BaseFragment {


    public Warehouse_Listing_Fragment() {
        // Required empty public constructor
    }
    RecyclerView warehouse;
    ArrayList<Warehouse_list> list=new ArrayList<>();


    Warehouses_Listing_Adapter warehouses_listing_adapter;
    EditText search;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    String company_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_warehouse__listing_, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final SharedPreferences pref =getActivity().getSharedPreferences(Constant.PREF_BASE, Context.MODE_PRIVATE);
        company_id = pref.getString(Constant.COMPANY_ID,"");

        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        warehouse = view.findViewById(R.id.warehouse);
        search = view.findViewById(R.id.search);
        search.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Light.otf"));
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                filter(s.toString());
            }
        });




        warehouseget();
        warehouses_listing_adapter = new Warehouses_Listing_Adapter(getContext(),list);
        warehouse.setAdapter(warehouses_listing_adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        warehouse.setLayoutManager(layoutManager);
        warehouse.setHasFixedSize(true);



        return view;
    }
    void filter(String text){
        List<Warehouse_list> temp = new ArrayList();
        for(Warehouse_list  d: list){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getWarehouse_name().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        warehouses_listing_adapter.updateList(temp);
    }


    public void warehouseget()
    {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);

        RequestParams params = new RequestParams();
        params.add("company_id",company_id);

        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL+"warehouse/listing",params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String(responseBody);
                Log.e("responsewarehouse",response);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String company_image_path = data.getString("company_image_path");
                        JSONObject company = data.getJSONObject("company");
                        String logo = company.getString("logo");
                        JSONArray warehouse = data.getJSONArray("warehouse");
                        if (warehouse.length()>0) {
                            for (int i = 0; i < warehouse.length(); i++) {
                                JSONObject item = warehouse.getJSONObject(i);

                                String warehouse_id = item.getString("warehouse_id");
                                String warehouse_name = item.getString("name");
                                String warehouse_address = item.getString("address");

                                Warehouse_list warehouse_list = new Warehouse_list();
                                warehouse_list.setWarehouse_id(warehouse_id);
                                warehouse_list.setWarehouse_name(warehouse_name);
                                warehouse_list.setWarehouse_address(warehouse_address);
                                warehouse_list.setWarehouse_image(company_image_path+logo);

                                list.add(warehouse_list);

                                warehouses_listing_adapter.notifyDataSetChanged();

                            }
                        }
                        else {
                            Constant.ErrorToast(getActivity(), getString(R.string.dialog_TaxNotFound));
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
                    Log.e("responsewarehouseF",response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false"))
                        {
                            //Constant.ErrorToast(getActivity(),jsonObject.getString("message"));
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
