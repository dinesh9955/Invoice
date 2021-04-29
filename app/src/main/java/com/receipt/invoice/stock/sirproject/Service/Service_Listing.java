package com.receipt.invoice.stock.sirproject.Service;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.Adapter.Service_Listing_Adapter;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Model.Service_list;
import com.receipt.invoice.stock.sirproject.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Service_Listing extends Fragment {

    public Service_Listing() {
        // Required empty public constructor
    }


    RecyclerView recyclerservice;
    ArrayList<Service_list> list=new ArrayList<>();

    AwesomeSpinner selectcompany;

    Service_Listing_Adapter service_Listing_Adapter;
    EditText search;
    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    ArrayList<String> cnames=new ArrayList<>();
    ArrayList<String> cids=new ArrayList<>();

    String selectedCompanyId="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_service__listing, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        recyclerservice = view.findViewById(R.id.recyclerservice);
        selectcompany = view.findViewById(R.id.selectcompany);
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

                if (list.size()>0) {
                    filter(s.toString());
                }
            }
        });

        selectcompany.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        selectcompany.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));

        service_Listing_Adapter = new Service_Listing_Adapter(getContext(),list);
        recyclerservice.setAdapter(service_Listing_Adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerservice.setLayoutManager(layoutManager);
        recyclerservice.setHasFixedSize(true);

        companyget();

        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                Log.e("selectedCompany",selectedCompanyId);
                serviceget(selectedCompanyId);
            }
        });



        return view;
    }

    void filter(String text){
        ArrayList<Service_list> temp = new ArrayList();
        for(Service_list  d: list){
            if(d.getService_name().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        service_Listing_Adapter.updateList(temp);
    }

    public void companyget()
    {

        cnames.clear();
        cids.clear();
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

    public void serviceget(String c_id)
    {
        list.clear();
        RequestParams params = new RequestParams();
        params.add("company_id",c_id);
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token",token);
        client.post(Constant.BASE_URL+"service/getListingByCompany", params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String(responseBody);
                Log.e("responseservice",response);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray service = data.getJSONArray("service");
                        if (service.length()>0) {
                            for (int i = 0; i < service.length(); i++) {
                                JSONObject item = service.getJSONObject(i);
                                String service_id = item.getString("product_id");
                                String company_id = item.getString("company_id");
                                String service_name = item.getString("name");
                                String service_price = item.getString("price");
                                String service_description = item.getString("description");
                                String service_taxable = item.getString("is_taxable");
                                String currency_code = item.getString("currency_symbol");
                                String category = item.getString("category");
                                String measurement_unit = item.getString("measurement_unit");

                                //String service_category = item.getString("category");
                                //String service_price_unit = item.getString("category");


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

                                list.add(service_list);
                                service_Listing_Adapter.notifyDataSetChanged();

                            }
                        }
                        else {
                            Constant.ErrorToast(getActivity(),jsonObject.getString("Product Not Found"));
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
                    Log.e("responseserviceF",response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false"))
                        {
                            Constant.ErrorToast(getActivity(),jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                }
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

            }
        });
    }

}
