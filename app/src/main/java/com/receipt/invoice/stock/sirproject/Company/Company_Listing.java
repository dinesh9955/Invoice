package com.receipt.invoice.stock.sirproject.Company;


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
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Adapter.Company_Listing_Adapter;
import com.receipt.invoice.stock.sirproject.Base.BaseFragment;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Model.Company_list;
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
public class Company_Listing extends BaseFragment {


    public Company_Listing() {
        // Required empty public constructor
    }

    RecyclerView company;
   /* ArrayList<String> Cnames=new ArrayList<>();
    ArrayList<String> addresses=new ArrayList<>();
    ArrayList<Integer> images=new ArrayList<>();*/

    ArrayList<Company_list> list=new ArrayList<>();

    Company_Listing_Adapter company_listing_adapter;
    EditText search;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = inflater.inflate(R.layout.fragment_company__listing, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        company = view.findViewById(R.id.company);
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



       /* Bundle bundle = null;
        if (bundle != null)
        {

            String company_id = getArguments().getString("company_id");
            Log.e("get_id_c",company_id);

        }*/

        companyget();

        company_listing_adapter = new Company_Listing_Adapter(getContext(),list);
        company.setAdapter(company_listing_adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        company.setLayoutManager(layoutManager);
        company.setHasFixedSize(true);
       company_listing_adapter.notifyDataSetChanged();

        return view;
    }
    void filter(String text){
        ArrayList<Company_list> temp = new ArrayList();
        for(Company_list  d: list){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getCompany_name().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        company_listing_adapter.updateList(temp);
    }

    public void companyget()
    {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);

        Log.e("access token",token);
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
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String company_image_path = data.getString("company_image_path");
                        JSONArray company = data.getJSONArray("company");
                        if (company.length()>0) {
                            for (int i = 0; i < company.length(); i++) {
                                JSONObject item = company.getJSONObject(i);

                                String company_id = item.getString("company_id");
                                String company_name = item.getString("name");
                                String company_phone = item.getString("phone_number");
                                String company_email = item.getString("email");
                                String company_website = item.getString("website");
                                String company_logo = item.getString("logo");
                                String company_address = item.getString("address");
                                String currency_id = item.getString("currency_id");


                                String ibn_number1 = item.getString("payment_iban");
                                String payment_bank_name1 = item.getString("payment_bank_name");

                                String paypal_email1 = item.getString("paypal_email");

                                String payment_swift_bic1 = item.getString("payment_swift_bic");
                                String cheque_payable_to1 = item.getString("cheque_payable_to");

                                Company_list company_list = new Company_list();
                                company_list.setCompany_id(company_id);
                                company_list.setCompany_name(company_name);
                                company_list.setCompany_phone(company_phone);
                                company_list.setCompany_email(company_email);
                                company_list.setCompany_website(company_website);
                                company_list.setCompany_logo(company_logo);
                                company_list.setCompany_address(company_address);
                                company_list.setCompany_image_path(company_image_path);
                                company_list.setCurrencyid(currency_id);

                                company_list.setIbn_number(ibn_number1);
                                company_list.setPayment_bank_name(payment_bank_name1);
                                company_list.setPaypal_email(paypal_email1);
                                company_list.setPayment_swift_bic(payment_swift_bic1);
                                company_list.setCheque_payable_to(cheque_payable_to1);
                                company_list.setColor(item.getString("color"));

                                list.add(company_list);


                            }
                        }
                        else {
                            //Constant.ErrorToast(getActivity(),jsonObject.getString("Company Not Found"));
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
                  //  Log.e("responsecompanyF",response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false"))
                        {
                           // Constant.ErrorToast(getActivity(),jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                  //  Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                }
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

            }
        });
    }
}
