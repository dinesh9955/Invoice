package com.receipt.invoice.stock.sirproject.Service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Base.BaseFragment;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Settings.SubscribeActivity;
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
    AwesomeSpinner selectcategory,measurementunit,selectcompany;
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
        selectcategory = view.findViewById(R.id.selectcategory);
        measurementunit = view.findViewById(R.id.measurementunit);
        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        selectcompany = view.findViewById(R.id.selectcompany);
        othercategory = view.findViewById(R.id.othercategory);
        weight = view.findViewById(R.id.weight);

        mesurementunitedittxt= view.findViewById(R.id.mesurementunitedittxt);

        setFonts();
        companyget();
        categoriesget();
        MeasurementUnits();

        selectcategory.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        selectcategory.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
        selectcompany.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        selectcompany.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
        measurementunit.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        measurementunit.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));

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


        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                Log.e("selectedCompany",selectedCompanyId);
            }
        });

        selectcategory.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCategoryId = catids.get(position);
                Log.e("selectedCategory",selectedCategoryId);

            }
        });

        measurementunit.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedMeasurementunit = measurementIds.get(position);
                //weight.animate().setDuration(1000).alpha(1.0f);
                Log.e("selectedunitid",selectedMeasurementunit);

                measurementunit.setSelected(true);
                //weight.animate().setDuration(1000).alpha(1.0f);

                String mesasurmentunitchck= itemAtPosition;

                if( mesasurmentunitchck.equals("Other"))
                {
                    mesurementunitedittxt.setVisibility(View.VISIBLE);
                    selectedMeasuremetnId = measurementIds.get(position);

                    //Log.e("selectedMeasuremetnId", selectedMeasuremetnId);

                }else {

                    selectedMeasuremetnId = measurementIds.get(position);
                    mesurementunitedittxt.setVisibility(View.INVISIBLE);
                    // Log.e("selectedMeasuremetnId", selectedMeasuremetnId);
                }

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
        client.post(AllSirApi.BASE_URL+"company/listing", new AsyncHttpResponseHandler() {
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

    private void MeasurementUnits(){
        RequestParams params = new RequestParams();
        params.add("weight_class","ALL");
        String token = Constant.GetSharedPreferences(getContext(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);
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
                                ArrayAdapter<String> munitsadp = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, measurementunits);
                                measurementunit.setAdapter(munitsadp);
                                measurementunit.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
                                measurementunit.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
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
        client.post(AllSirApi.BASE_URL+"category/listing", new AsyncHttpResponseHandler() {
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

                                ArrayAdapter<String> catadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,catnames);
                                selectcategory.setAdapter(catadapter);

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
            servicename.setError("Required");
            servicename.requestFocus();
        }
        else if (price.isEmpty()){
            servicerate.setError("Required");
            servicerate.requestFocus();
        }
//        else if (pdesc.isEmpty()){
//            servicedescription.setError("Required");
//            servicedescription.requestFocus();
//        }
        else if (selectedMeasurementunit.equals("")){
            Constant.ErrorToast(getActivity(),"Measurement Unit is required");
        }
        else if (selectedCompanyId.equals("")){
            Constant.ErrorToast(getActivity(),"Company is required");
        }
//        else if (selectedTaxable.equals("")){
//            Constant.ErrorToast(getActivity(),"Tax information is required");
//        }
        else if (selectedCategoryId.equals("") && othercat.isEmpty()){
            Constant.ErrorToast(getActivity(),"Item category is required");
        }
        else {

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
            client.post(AllSirApi.BASE_URL + "product/add", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);

                    if(responseBody.length == 0){
                        Constant.ErrorToast(getActivity(), "Something went wrong, try again!");
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

                                Constant.SuccessToast(getActivity(),"Item Added");
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
                                                Intent intent = new Intent(getActivity(), SubscribeActivity.class);
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
                        Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                    }

                }
            });


        }

    }
}
