package com.sirapp.Tax;

import android.graphics.Typeface;
import android.os.Bundle;

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
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.Adapter.Tax_Listing_Adapter;
import com.sirapp.Base.BaseFragment;
import com.sirapp.Constant.Constant;
import com.sirapp.Model.Tax_List;
import com.sirapp.API.AllSirApi;
import com.sirapp.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Tax_Listing extends BaseFragment {

    public Tax_Listing() {
        // Required empty public constructor
    }

    RecyclerView recyclertax;
    ArrayList<Tax_List> list=new ArrayList<>();


    Tax_Listing_Adapter tax_listing_adapter;
    EditText search;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    TextView textViewMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tax__listing, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        recyclertax = view.findViewById(R.id.recyclertax);
        search = view.findViewById(R.id.search);


        textViewMsg = view.findViewById(R.id.txtinvoice);
        textViewMsg.setText(getString(R.string.home_NoTax));
        textViewMsg.setVisibility(View.VISIBLE);

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

        taxget();
        tax_listing_adapter = new Tax_Listing_Adapter(getContext(),list);
        recyclertax.setAdapter(tax_listing_adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclertax.setLayoutManager(layoutManager);
        recyclertax.setHasFixedSize(true);
        tax_listing_adapter.notifyDataSetChanged();


        return view;
    }
    void filter(String text){
        ArrayList<Tax_List> temp = new ArrayList();
        for(Tax_List d: list){
            if(d.getTax_name().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        tax_listing_adapter.updateList(temp);

        if (list.size() == 0){
            textViewMsg.setVisibility(View.VISIBLE);
        }else{
            textViewMsg.setVisibility(View.GONE);
        }
    }

    public void taxget()
    {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);
        RequestParams params = new RequestParams();
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL+"tax/listing", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String(responseBody);
                Log.e("responsetax",response);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray tax = data.getJSONArray("tax");
                        if (tax.length()>0) {
                            for (int i = 0; i < tax.length(); i++) {
                                JSONObject item = tax.getJSONObject(i);

                                String tax_id = item.getString("tax_id");
                                String tax_name = item.getString("name");
                                String tax_rate = item.getString("rate");
                                String company_id = item.getString("company_id");
                                String company_name = item.getString("company_name");

                                String type = item.getString("type");

                                Tax_List tax_list = new Tax_List();
                                tax_list.setType(type);
                                tax_list.setTax_id(tax_id);
                                tax_list.setCompanyid(company_id);
                                tax_list.setTax_name(tax_name);
                                tax_list.setTax_rate(tax_rate);
                                tax_list.setCompany_name(company_name);

                                list.add(tax_list);
                            }
                        }
                        else {
                           // Constant.ErrorToast(getActivity(),jsonObject.getString("Tax Not Found"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (list.size() == 0){
                    textViewMsg.setVisibility(View.VISIBLE);
                }else{
                    textViewMsg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(responseBody!=null){
                    String response = new String(responseBody);
                    Log.e("responsetaxF",response);

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
                    //Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                }
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

                textViewMsg.setVisibility(View.VISIBLE);
            }
        });
    }

}
