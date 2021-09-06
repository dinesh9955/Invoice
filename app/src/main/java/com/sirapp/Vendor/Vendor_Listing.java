package com.sirapp.Vendor;


import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.Adapter.Vendro_Listing_Adapter;
import com.sirapp.Constant.Constant;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseFragment;
import com.sirapp.Model.Vendor_list;
import com.sirapp.R;
import com.sirapp.User.User_Listing;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class Vendor_Listing extends BaseFragment {


    public Vendor_Listing() {
        // Required empty public constructor
    }

    RecyclerView vendors;
    ArrayList<Vendor_list> list=new ArrayList<>();
//    AwesomeSpinner selectcompany;
    Button selectcompany1;

    Vendro_Listing_Adapter vendro_listing_adapter;
    EditText search;
    private AVLoadingIndicatorView avi;
    ImageView avibackground;
    ArrayList<String> cnames=new ArrayList<>();
    ArrayList<String> cids=new ArrayList<>();

    String selectedCompanyId="";

    TextView textViewMsg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_vendor__listing, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        vendors = view.findViewById(R.id.vendors);
//        selectcompany = view.findViewById(R.id.selectcompany);
        selectcompany1 = view.findViewById(R.id.selectcompany2);
        search = view.findViewById(R.id.search);

        textViewMsg = view.findViewById(R.id.txtinvoice);
        textViewMsg.setText(getString(R.string.home_NoSupplier));
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

//        selectcompany.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
//        selectcompany.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));

        vendro_listing_adapter = new Vendro_Listing_Adapter(getContext(),list);
        vendors.setAdapter(vendro_listing_adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        vendors.setLayoutManager(layoutManager);
        vendors.setHasFixedSize(true);
        vendro_listing_adapter.notifyDataSetChanged();

        companyget();

//        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
//            @Override
//            public void onItemSelected(int position, String itemAtPosition) {
//                selectedCompanyId = cids.get(position);
//                Log.e("selectedCompany",selectedCompanyId);
//                vendorget(selectedCompanyId);
//            }
//        });



        selectcompany1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView mRecyclerView;
                MenuAdapter mAdapter;

                final Dialog mybuilder = new Dialog(getActivity());
                mybuilder.setContentView(R.layout.select_company_dialog_3);


                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter(cnames, mybuilder);
                mRecyclerView.setAdapter(mAdapter);

                mybuilder.show();
                mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                Window window = mybuilder.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(R.color.transparent);
            }
        });



        return view;
    }

    void filter(String text){
        ArrayList<Vendor_list> temp = new ArrayList();
        for(Vendor_list  d: list){
            if(d.getVendor_name().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        vendro_listing_adapter.updateList(temp);

        if (list.size() == 0){
            textViewMsg.setVisibility(View.VISIBLE);
        }else{
            textViewMsg.setVisibility(View.GONE);
        }
    }

    public void companyget()
    {

        cnames.clear();
        cids.clear();
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
//                                selectcompany.setAdapter(namesadapter);

                            }
                        }


                        if(company.length() == 1){
                            selectedCompanyId = cids.get(0);
                            Log.e("selectedCompany",selectedCompanyId);
                            vendorget(selectedCompanyId);
                            selectcompany1.setText(cnames.get(0));
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

    public void vendorget(String c_id)
    {
        list.clear();
        RequestParams params = new RequestParams();
        params.add("company_id",c_id);
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);

        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL+"supplier/getListingByCompany", params,new AsyncHttpResponseHandler() {
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
                                    String vendor_name = item.getString("supplier_name");
                                    String vendor_contact_person = item.getString("contact_name");
                                    String vendor_address = item.getString("address");
                                    String vendor_image = item.getString("image");
                                    String vendor_email = item.getString("email");
                                    String vendor_phone = item.getString("phone_number");
                                    String vendor_mobile = item.getString("mobile_number");
                                    String vendor_website = item.getString("website");


                                    Vendor_list vendor_list = new Vendor_list();

                                    vendor_list.setVendor_id(vendor_id);
                                    vendor_list.setVendor_name(vendor_name);
                                    vendor_list.setVendor_contact_person(vendor_contact_person);
                                    vendor_list.setVendor_image(vendor_image);
                                    vendor_list.setVendor_email(vendor_email);
                                    vendor_list.setVendor_phone(vendor_phone);
                                    vendor_list.setVendor_website(vendor_website);
                                    vendor_list.setVendor_address(vendor_address);
                                    vendor_list.setVendor_mobile(vendor_mobile);
                                    vendor_list.setVendor_image_path(vendor_image_path);


                                    list.add(vendor_list);
                                    vendro_listing_adapter.notifyDataSetChanged();
                                }
                            }
                            else {
                                //Constant.ErrorToast(getActivity(),jsonObject.getString(getString(R.string.dialog_CustomerNotFound)));
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
                    Log.e("responsevendorF",response);

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
                   // Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                }
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

                textViewMsg.setVisibility(View.VISIBLE);
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
                    selectedCompanyId = cids.get(i);
                    Log.e("selectedCompany",selectedCompanyId);
                    vendorget(selectedCompanyId);
                    selectcompany1.setText(cnames.get(i));
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
