package com.receipt.invoice.stock.sirproject.Invoice;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Adapter.Product_Selection_Adapter;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Model.Product_list;
import com.receipt.invoice.stock.sirproject.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Add_Item_Activity extends BaseActivity {

    RecyclerView recyclerproduct;
    ArrayList<Product_list> list=new ArrayList<>();


    Product_Selection_Adapter product_Listing_Adapter;
    EditText search;
    AwesomeSpinner selectcompany;

    ArrayList<String> cnames=new ArrayList<>();
    ArrayList<String> cids=new ArrayList<>();

    String selectedCompanyId="";

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__item_);





        Constant.toolbar(Add_Item_Activity.this, getString(R.string.header_add_item));

        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);
        recyclerproduct = findViewById(R.id.recyclerproduct);
        search = findViewById(R.id.search);
        selectcompany = findViewById(R.id.selectcompany);

        search.setTypeface(Typeface.createFromAsset(Add_Item_Activity.this.getAssets(),"Fonts/AzoSans-Light.otf"));
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


        product_Listing_Adapter = new Product_Selection_Adapter(getApplication(),list);
        recyclerproduct.setAdapter(product_Listing_Adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplication(), LinearLayoutManager.VERTICAL, false);
        recyclerproduct.setLayoutManager(layoutManager);
        recyclerproduct.setHasFixedSize(true);

        companyget();

        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                Log.e("selectedCompany",selectedCompanyId);
                productget(selectedCompanyId);
            }
        });




    }
    public void productget(String c_id)
    {
        list.clear();
        RequestParams params = new RequestParams();
        params.add("company_id",c_id);
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        String token = Constant.GetSharedPreferences(getApplication(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        params.add("language", ""+getLanguage());
        client.addHeader("Access-Token",token);
        client.post(AllSirApi.BASE_URL+"product/getListingByCompany", params, new AsyncHttpResponseHandler() {
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
                        String product_image_path = data.getString("product_image_path");
                        JSONArray product = data.getJSONArray("product");
                        if (product.length()>0) {
                            for (int i = 0; i < product.length(); i++) {
                                JSONObject item = product.getJSONObject(i);
                                String product_id = item.getString("product_id");
                                String company_id = item.getString("company_id");
                                String product_name = item.getString("name");
                                String product_description = item.getString("description");
                                String product_status = item.getString("status");
                                String product_image = item.getString("image");
                                String product_price = item.getString("price");
                                String product_taxable = item.getString("is_taxable");
                                String product_category = item.getString("category");
                                String currency_code = item.getString("currency_code");
                                String quantity = item.getString("quantity");
                                String minimum = item.getString("minimum");

                                Product_list product_list = new Product_list();
                                product_list.setCompany_id(company_id);
                                product_list.setProduct_id(product_id);
                                product_list.setProduct_name(product_name);
                                product_list.setProduct_description(product_description);
                                product_list.setProduct_status(product_status);
                                product_list.setProduct_image(product_image);
                                product_list.setProduct_image_path(product_image_path);
                                product_list.setProduct_price(product_price);
                                product_list.setProduct_taxable(product_taxable);
                                product_list.setProduct_category(product_category);
                                product_list.setCurrency_code(currency_code);
                                product_list.setQuantity(quantity);
                                product_list.setMinimum(minimum);

                                list.add(product_list);
                                product_Listing_Adapter.notifyDataSetChanged();

                            }
                        }
                        else {
                            Constant.ErrorToast(Add_Item_Activity.this,jsonObject.getString("Product Not Found"));
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
                    Log.e("responseproductF",response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false"))
                        {
                            Constant.ErrorToast(Add_Item_Activity.this,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                   // Constant.ErrorToast(Add_Item_Activity.this,"Something went wrong, try again!");
                }

                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

            }
        });
    }

    public void companyget()
    {

        cnames.clear();
        cids.clear();
        String token = Constant.GetSharedPreferences(getApplication(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        RequestParams params = new RequestParams();
        params.add("language", ""+getLanguage());
        client.addHeader("Access-Token",token);
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

                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,cnames);
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

    void filter(String text){
        ArrayList<Product_list> temp = new ArrayList();
        for(Product_list d: list){
            if(d.getProduct_name().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        product_Listing_Adapter.updateList(temp);
    }
}
