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
public class Stock_Movement extends BaseFragment {


    public Stock_Movement() {
        // Required empty public constructor
    }

    TextView description,selectfromtext,sendtotext,quantitydescription;
    Button move;
    EditText quantity;
    AwesomeSpinner fromwarehousespinner,sendtospinner;

    Button selectcompany1;

    Button productcategory1;
    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    String selectedCompanyId="";
    String selectedProductId="";
    String selectedfromId="";
    String selectedtoId="";

    ArrayList<String> cnames=new ArrayList<>();
    ArrayList<String> cids=new ArrayList<>();

    ArrayList<String> pnames=new ArrayList<>();
    ArrayList<String> pids=new ArrayList<>();

    ArrayList<String> warehousename=new ArrayList<>();
    ArrayList<String> warehouseids=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View v =  inflater.inflate(R.layout.fragment_stock__movement, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FindByIds(v);
        setFonts();

        companyget();

        //spinners
//        companyspinner.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
//        companyspinner.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
        fromwarehousespinner.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        fromwarehousespinner.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
        sendtospinner.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        sendtospinner.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
//        productspinner.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
//        productspinner.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
        //productspinner.setSpinnerHint(String.valueOf(Html.fromHtml("Select Produc<font color='#FF0000'>*</font>")));

        //((AwesomeSpinner) productspinner).setSpinnerHint(String.valueOf(Html.fromHtml("Select Produc<font color='#FF0000'>*</font>")));


        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockMovement();
            }
        });


//        companyspinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
//            @Override
//            public void onItemSelected(int position, String itemAtPosition) {
//                selectedCompanyId = cids.get(position);
//                Log.e("selectedCompany",selectedCompanyId);
//                productget(selectedCompanyId);
//                warehouses(selectedCompanyId);
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




        fromwarehousespinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedfromId = warehouseids.get(position);
                Log.e("selectedfrom",selectedfromId);
            }
        });

        sendtospinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedtoId = warehouseids.get(position);
                Log.e("selectedto",selectedtoId);
            }
        });

        return v;
    }

    private void FindByIds(View v){
        description = v.findViewById(R.id.description);
        selectfromtext = v.findViewById(R.id.selectfromtext);
        quantitydescription = v.findViewById(R.id.quantitydescription);
        sendtotext = v.findViewById(R.id.sendtotext);
        move = v.findViewById(R.id.move);
        quantity = v.findViewById(R.id.quantity);
        selectcompany1 = v.findViewById(R.id.selectcompany2);
        fromwarehousespinner = v.findViewById(R.id.fromwarehousespinner);
        sendtospinner = v.findViewById(R.id.sendtospinner);
        productcategory1 = v.findViewById(R.id.productcategory1);

        avi = v.findViewById(R.id.avi);
        avibackground = v.findViewById(R.id.avibackground);
    }

    private void setFonts(){
        description.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Regular.otf"));
        selectfromtext.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Medium.ttf"));
        quantitydescription.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        sendtotext.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        move.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Medium.ttf"));
        quantity.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
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
                            warehouses(selectedCompanyId);
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
        client.post(AllSirApi.BASE_URL+"product/getListingByCompany",params, new AsyncHttpResponseHandler() {
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

                                pnames.add(product_name);
                                pids.add(product_id);

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
                   // Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                }
            }
        });
    }

    private void warehouses(String id){
        warehousename.clear();
        warehouseids.clear();
        if (id.equals("")){
            Constant.ErrorToast(getActivity(),getString(R.string.select_company));
        }
        else{
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


                                    ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,warehousename);
                                    fromwarehousespinner.setAdapter(namesadapter);
                                    sendtospinner.setAdapter(namesadapter);

                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if(responseBody!=null) {
                        String response = new String(responseBody);
                        Log.e("warehouseRespF",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("false")){

                                //Constant.ErrorToast(getActivity(),jsonObject.getString("message"));
                                selectedfromId="";
                                selectedtoId="";

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

        }
    }


    private void StockMovement(){

        String quant = quantity.getText().toString();

        if (quant.isEmpty()){
            quantity.setError(getString(R.string.dialog_Required));
            quantity.requestFocus();
        }
        else if (selectedProductId.equals("")){
            Constant.ErrorToast(getActivity(),getString(R.string.dialog_SelectAProduct));
        }
        else if (selectedfromId.equals("")){
            Constant.ErrorToast(getActivity(),getString(R.string.dialog_SelectSenderWarehouse));
        }
        else if (selectedtoId.equals("")){
            Constant.ErrorToast(getActivity(),getString(R.string.dialog_SelectReceiverWarehouse));
        }
        else {

            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);

            RequestParams params = new RequestParams();
            params.add("product_id",selectedProductId);
            params.add("warehouse_id_sender",selectedfromId);
            params.add("warehouse_id_receiver",selectedtoId);
            params.add("quantity",quant);

            String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.addHeader("Access-Token",token);
            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL + "product/movement", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.e("stockmoveResp",response);
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("true"))
                        {

                            Map<String, Object> eventValue = new HashMap<String, Object>();
                            eventValue.put(AFInAppEventParameterName.PARAM_1, "stocks_move");
                            AppsFlyerLib.getInstance().trackEvent(getActivity(), "stocks_move", eventValue);

                            Bundle params2 = new Bundle();
                            params2.putString("event_name", "Stocks Move");
                            firebaseAnalytics.logEvent("stocks_move", params2);

                            Constant.SuccessToast(getActivity(),getString(R.string.dialog_StockMoved));
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
                        Log.e("stockmoveRespF",response);

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
                    productcategory1.setText(cnames.get(i));
                    productget(selectedCompanyId);
                    warehouses(selectedCompanyId);
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
