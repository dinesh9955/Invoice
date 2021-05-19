package com.receipt.invoice.stock.sirproject.Customer;


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
import com.receipt.invoice.stock.sirproject.Adapter.Customer_Listing_Adapter;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Model.Customer_list;
import com.receipt.invoice.stock.sirproject.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class Customer_Listing extends Fragment {


    public Customer_Listing() {
        // Required empty public constructor
    }

    RecyclerView customers;
    ArrayList<Customer_list> list=new ArrayList<>();

    AwesomeSpinner selectcompany;

    Customer_Listing_Adapter customer_listing_adapter;
    EditText search;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;
    ArrayList<String> cnames=new ArrayList<>();
    ArrayList<String> cids=new ArrayList<>();

    String selectedCompanyId="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customer__listing, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        customers = view.findViewById(R.id.customers);
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

        customer_listing_adapter = new Customer_Listing_Adapter(getContext(),list);
        customers.setAdapter(customer_listing_adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        customers.setLayoutManager(layoutManager);
        customers.setHasFixedSize(true);

        companyget();

        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                Log.e("selectedCompany",selectedCompanyId);
                customerget(selectedCompanyId);
            }
        });


        return view;
    }
    void filter(String text){
        ArrayList<Customer_list> temp = new ArrayList();
        for(Customer_list  d: list){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getCustomer_name().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        customer_listing_adapter.updateList(temp);
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

    public void customerget(String c_id)
    {
        list.clear();
        RequestParams params = new RequestParams();
        params.add("company_id",c_id);
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token",token);
        client.post(Constant.BASE_URL+"customer/getListingByCompany",params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String(responseBody);
                Log.e("responsecustomer",response);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String customer_image_path = data.getString("customer_image_path");

                        JSONArray customer = data.getJSONArray("customer");
                        if (customer.length()>0) {
                            for (int i = 0; i < customer.length(); i++) {
                                JSONObject item = customer.getJSONObject(i);

                                String customer_id = item.getString("customer_id");
                                String customer_name = item.getString("customer_name");
                                String customer_contact_person = item.getString("contact_name");
                                String customer_address = item.getString("address");
                                String customer_image = item.getString("image");
                                String customer_email = item.getString("email");
                                String customer_phone = item.getString("phone_number");
                                String customer_mobile = item.getString("mobile_number");
                                String customer_website = item.getString("website");


                                Customer_list customer_list = new Customer_list();

                                customer_list.setCompany_id(selectedCompanyId);
                                customer_list.setCustomer_id(customer_id);
                                customer_list.setCustomer_name(customer_name);
                                customer_list.setCustomer_contact_person(customer_contact_person);
                                customer_list.setCustomer_image(customer_image);
                                customer_list.setCustomer_email(customer_email);
                                customer_list.setCustomer_phone(customer_phone);
                                customer_list.setCustomer_website(customer_website);
                                customer_list.setCustomer_address(customer_address);
                                customer_list.setCustomer_mobile(customer_mobile);
                                customer_list.setCustomer_image_path(customer_image_path);

                                String fname = item.getString("shipping_firstname");
                                String lname = item.getString("shipping_lastname");
                                String ship1 = item.getString("shipping_address_1");
                                String ship2 = item.getString("shipping_address_2");
                                String city = item.getString("shipping_city");
                                String post = item.getString("shipping_postcode");
                                String country = item.getString("shipping_country");

                                customer_list.setShipping_firstname(fname);
                                customer_list.setShipping_lastname(lname);
                                customer_list.setShipping_address_1(ship1);
                                customer_list.setShipping_address_2(ship2);
                                customer_list.setShipping_city(city);
                                customer_list.setShipping_postcode(post);
                                customer_list.setShipping_country(country);

                                list.add(customer_list);
                                customer_listing_adapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            Constant.ErrorToast(getActivity(),jsonObject.getString("Customer Not Found"));
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
                    Log.e("responsecustomerF",response);

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
