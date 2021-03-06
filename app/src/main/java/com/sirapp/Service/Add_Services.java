package com.sirapp.Service;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.Constant.Constant;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseFragment;
import com.sirapp.Home.GoProActivity;
import com.sirapp.R;
import com.sirapp.Settings.SettingsActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

public class Add_Services extends BaseFragment {


    private static final String TAG = "Add_Services";

    public Add_Services() {
        // Required empty public constructor
    }
    String selectedMeasuremetnId = "";
    String otherunitstr="";

    EditText servicename,servicerate,servicedescription,othercategory;
    TextView taxable;
    Button addservice;
    RadioButton ryes,rno;
    RadioGroup radiogroup;
//    AwesomeSpinner selectcompany;

    Button selectcompany1, selectcategory1, measurementunit1;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    EditText weight, mesurementunitedittxt;


    ArrayList<String> companies = new ArrayList<>();

    String selectedCategoryId="";
    String selectedCompanyId="";
    String selectedTaxable="0";
    String selectedMeasurementunit="";

    ArrayList<String> cnames=new ArrayList<>();
    ArrayList<String> cids=new ArrayList<>();

    ArrayList<String> catnames=new ArrayList<>();
    ArrayList<String> catids=new ArrayList<>();

    ArrayList<String> measurementunits = new ArrayList<>();
    ArrayList<String> measurementIds = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = inflater.inflate(R.layout.fragment_add__services, container, false);

        servicename = view.findViewById(R.id.servicename);
        servicerate = view.findViewById(R.id.servicerate);
        servicedescription = view.findViewById(R.id.servicedescription);
        taxable = view.findViewById(R.id.taxable);
        addservice = view.findViewById(R.id.addservice);
        radiogroup = view.findViewById(R.id.radiogroup);
        ryes = view.findViewById(R.id.ryes);
        rno = view.findViewById(R.id.rno);
//        selectcategory = view.findViewById(R.id.selectcategory);
//        measurementunit = view.findViewById(R.id.measurementunit);
        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        selectcompany1 = view.findViewById(R.id.selectcompany2);
        othercategory = view.findViewById(R.id.othercategory);
        weight = view.findViewById(R.id.weight);

        mesurementunitedittxt= view.findViewById(R.id.mesurementunitedittxt);

        setFonts();
        companyget();
        categoriesget();
        MeasurementUnits();

//        selectcategory.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
//        selectcategory.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
//        selectcompany.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
//        selectcompany.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
//        measurementunit.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
//        measurementunit.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));

        selectcategory1 = view.findViewById(R.id.selectcategory1);
        measurementunit1 = view.findViewById(R.id.measurementunit1);

        ryes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    selectedTaxable="1";
                }
            }
        });

        rno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    selectedTaxable="0";
                }
            }
        });


//        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
//            @Override
//            public void onItemSelected(int position, String itemAtPosition) {
//                selectedCompanyId = cids.get(position);
//                Log.e("selectedCompany",selectedCompanyId);
//            }
//        });


        selectcompany1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView mRecyclerView;
                MenuAdapter3 mAdapter;

                final Dialog mybuilder = new Dialog(getActivity());
                mybuilder.setContentView(R.layout.select_company_dialog_3);


                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter3(cnames, mybuilder);
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

//        selectcategory.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
//            @Override
//            public void onItemSelected(int position, String itemAtPosition) {
//                selectedCategoryId = catids.get(position);
//                Log.e("selectedCategory",selectedCategoryId);
//
//            }
//        });
//
//        measurementunit.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
//            @Override
//            public void onItemSelected(int position, String itemAtPosition) {
//                selectedMeasurementunit = measurementIds.get(position);
//                //weight.animate().setDuration(1000).alpha(1.0f);
//                Log.e("selectedunitid",selectedMeasurementunit);
//
//                measurementunit.setSelected(true);
//                //weight.animate().setDuration(1000).alpha(1.0f);
//
//                String mesasurmentunitchck= itemAtPosition;
//
//                if( mesasurmentunitchck.equals(getString(R.string.item_Other)))
//                {
//                    mesurementunitedittxt.setVisibility(View.VISIBLE);
//                    selectedMeasuremetnId = measurementIds.get(position);
//
//                    //Log.e("selectedMeasuremetnId", selectedMeasuremetnId);
//
//                }else {
//
//                    selectedMeasuremetnId = measurementIds.get(position);
//                    mesurementunitedittxt.setVisibility(View.INVISIBLE);
//                    // Log.e("selectedMeasuremetnId", selectedMeasuremetnId);
//                }
//
//            }
//        });




        selectcategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView mRecyclerView;
                MenuAdapter mAdapter;

                final Dialog mybuilder = new Dialog(getActivity());
                mybuilder.setContentView(R.layout.select_company_dialog_2);


                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter(catnames, mybuilder);
                mRecyclerView.setAdapter(mAdapter);

                mybuilder.show();
                mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                Window window = mybuilder.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(R.color.transparent);
            }
        });

        measurementunit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView mRecyclerView;
                MenuAdapter2 mAdapter;

                final Dialog mybuilder = new Dialog(getActivity());
                mybuilder.setContentView(R.layout.select_company_dialog_2);


                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter2(measurementunits, mybuilder);
                mRecyclerView.setAdapter(mAdapter);

                mybuilder.show();
                mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                Window window = mybuilder.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(R.color.transparent);
            }
        });






        addservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddService();
            }
        });


        return view;
    }

    private void setFonts() {

        servicename.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        servicerate.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        servicedescription.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        taxable.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Bold.otf"));
        ryes.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
        rno.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
        addservice.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Medium.ttf"));

    }

    public void companyget()
    {

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
//                                selectcompany.setAdapter(namesadapter);

                            }
                        }


                        if(company.length() == 1){
                            selectedCompanyId = cids.get(0);
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

    private void MeasurementUnits(){
        RequestParams params = new RequestParams();
        params.add("weight_class","ALL");
        String token = Constant.GetSharedPreferences(getContext(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "product/getMeasurementUnit",params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("munitsResp",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray unit = data.getJSONArray("unit");
                        if (unit.length()>0){
                            for (int i=0; i<unit.length(); i++){
                                JSONObject item = unit.getJSONObject(i);
                                String weight_class_id = item.getString("weight_class_id");
                                String units = item.getString("title");
                                measurementIds.add(weight_class_id);
                                measurementunits.add(units);
//                                ArrayAdapter<String> munitsadp = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, measurementunits);
//                                measurementunit.setAdapter(munitsadp);
//                                measurementunit.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
//                                measurementunit.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
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
                    Log.e("munitsRespF",response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("false")){
                            //Constant.ErrorToast(Company_Setup_Activity.this,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void categoriesget()
    {
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);
        RequestParams params = new RequestParams();
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL+"category/listing", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("categoriesResp",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray category = data.getJSONArray("category");
                        if (category.length()>0) {
                            for (int i = 0; i < category.length(); i++) {
                                JSONObject item = category.getJSONObject(i);
                                String category_id = item.getString("category_id");
                                String name = item.getString("name");

                                if (!name.equals("qqq")){
                                    catids.add(category_id);
                                    catnames.add(name);
                                }
                            }

                            catids.add("");
                            catnames.add("Other");
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
                    Log.e("categoriesRespF",response);
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

    private void AddService(){

        String pname = servicename.getText().toString();
        String price = servicerate.getText().toString();
        String pdesc = servicedescription.getText().toString();
        String othercat = othercategory.getText().toString();
        otherunitstr=mesurementunitedittxt.getText().toString();
       // String pweight = weight.getText().toString();

        if (pname.isEmpty()){
            servicename.setError(getString(R.string.dialog_Required));
            servicename.requestFocus();
        }
        else if (price.isEmpty()){
            servicerate.setError(getString(R.string.dialog_Required));
            servicerate.requestFocus();
        }
//        else if (pdesc.isEmpty()){
//            servicedescription.setError("Required");
//            servicedescription.requestFocus();
//        }
        else if (selectedMeasurementunit.equals("")){
            Constant.ErrorToast(getActivity(),getString(R.string.dialog_Measurement_Unit_required));
        }
        else if (selectedCompanyId.equals("")){
            Constant.ErrorToast(getActivity(),getString(R.string.dialog_Company_required));
        }
//        else if (selectedTaxable.equals("")){
//            Constant.ErrorToast(getActivity(),"Tax information is required");
//        }
        else if (selectedCategoryId.equals("") && othercat.isEmpty()){
            Constant.ErrorToast(getActivity(),getString(R.string.dialog_Item_category_required));
        }
        else {
//            if(pref.getItem().equalsIgnoreCase("item")){
                avi.smoothToShow();
                avibackground.setVisibility(View.VISIBLE);
                RequestParams params = new RequestParams();
                params.add("name",pname);
                params.add("price",price);
                params.add("description",pdesc);
                params.add("company_id",selectedCompanyId);
                params.add("is_taxable",selectedTaxable);
                params.add("product_type","SERVICE");
                // params.add("weight_class_id",selectedMeasurementunit);

                if (selectedMeasuremetnId.equals("19")) {
                    params.add("weight_class_id", selectedMeasuremetnId);
                    params.add("other_weight_unit", otherunitstr);
                }else {
                    params.add("weight_class_id", selectedMeasuremetnId);
                }

//            //params.add("weight",pweight);
//            if (othercat.isEmpty()){
//                params.add("category_id",selectedCategoryId);
//            }
//            else if (selectedCategoryId.equals("")){
//                params.add("category_name",othercat);
//            }
//            else {
//                params.add("category_id",selectedCategoryId);
//            }

                if (!selectedCategoryId.equals("")) {
                    params.add("category_id", selectedCategoryId);
                }

                if (selectedCategoryId.equals("")) {
                    if (!othercat.isEmpty()) {
                        params.add("custom_category", othercat);
                    }
                }

                Gson gson = new Gson();
                String json = gson.toJson(params);

                Log.e(TAG, "jsonAA "+json);

                Log.e(TAG, "paramss"+params);

                String token = Constant.GetSharedPreferences(getContext(),Constant.ACCESS_TOKEN);
                AsyncHttpClient client = new AsyncHttpClient();
                client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
                client.addHeader("Access-Token",token);
                params.add("language", ""+getLanguage());
                client.post(AllSirApi.BASE_URL + "product/add", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        avi.smoothToHide();
                        avibackground.setVisibility(View.GONE);

                        if(responseBody.length == 0){
                            //Constant.ErrorToast(getActivity(), "Something went wrong, try again!");
                        }else{
                            String response = new String(responseBody);
                            Log.e("addserviceResp",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("true")){

                                    Map<String, Object> eventValue = new HashMap<String, Object>();
                                    eventValue.put(AFInAppEventParameterName.PARAM_1, "service_addnew");
                                    AppsFlyerLib.getInstance().trackEvent(getActivity(), "service_addnew", eventValue);

                                    Bundle params2 = new Bundle();
                                    params2.putString("event_name", "My Services");
                                    firebaseAnalytics.logEvent("service_addnew", params2);
                                    pref.setItem("");
                                    Constant.SuccessToast(getActivity(), getString(R.string.dialog_ItemAdded));
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getContext(),Service_Activity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    },1000);

                                }

                                if (status.equals("false")){

                                    if(jsonObject.has("error")){
                                        Constant.ErrorToast(getActivity(), jsonObject.getString("error"));
                                    }

                                    if(jsonObject.has("message")){
                                        Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                                    }


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

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        avi.smoothToHide();
                        avibackground.setVisibility(View.GONE);
                        if(responseBody!=null) {
                            String response = new String(responseBody);
                            Log.e("addserviceRespF",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("false")){

                                    Constant.ErrorToast(getActivity(),jsonObject.getString("message"));

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

//            }else{
//                Intent intent = new Intent(getActivity(), SettingsActivity.class);
//                intent.putExtra("key", "item");
//                startActivityForResult(intent, 44);
//            }



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
                    selectedCategoryId = catids.get(i);
                    selectcategory1.setText(cnames.get(i));

                    if(selectedCategoryId.equals("")){
                        othercategory.setVisibility(View.VISIBLE);
                    }else{
                        othercategory.setVisibility(View.GONE);
                    }
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

        private static final String TAG = "MenuAdapter2";

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
                    measurementunit1.setText(cnames.get(i));
                    selectedMeasurementunit = measurementIds.get(i);
                    if(cnames.get(i).equalsIgnoreCase(getString(R.string.item_Other)))
                    {
                        mesurementunitedittxt.setVisibility(View.VISIBLE);
                        selectedMeasuremetnId = measurementIds.get(i);
                        //Log.e("selectedMeasuremetnId", selectedMeasuremetnId);
                    }else {

                        selectedMeasuremetnId = measurementIds.get(i);
                        mesurementunitedittxt.setVisibility(View.INVISIBLE);
                        // Log.e("selectedMeasuremetnId", selectedMeasuremetnId);
                    }

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








    public class MenuAdapter3 extends RecyclerView.Adapter<MenuAdapter3.ViewHolder> {

        private static final String TAG = "MenuAdapter3";

        ArrayList<String> cnames = new ArrayList<>();

        Dialog mybuilder;

        public MenuAdapter3(ArrayList<String> cnames, Dialog mybuilder) {
            super();
            this.cnames = cnames;
            this.mybuilder = mybuilder;
        }



        @Override
        public MenuAdapter3.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.menu_item_2, viewGroup, false);
            return new MenuAdapter3.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final MenuAdapter3.ViewHolder viewHolder, final int i) {

            viewHolder.textViewName.setText(""+cnames.get(i));
            viewHolder.realtive1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mybuilder.dismiss();
                    selectcompany1.setText(cnames.get(i));
                    selectedCompanyId = cids.get(i);
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
