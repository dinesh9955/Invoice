package com.receipt.invoice.stock.sirproject.User;

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
import com.receipt.invoice.stock.sirproject.Adapter.User_Listing_Adapter;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Model.User_list;
import com.receipt.invoice.stock.sirproject.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class User_Listing extends Fragment {



    public User_Listing() {
        // Required empty public constructor
    }

    RecyclerView recycleruser;
    ArrayList<User_list> username=new ArrayList<>();

    User_Listing_Adapter user_Listing_Adapter;
    EditText search;
    AwesomeSpinner selectcompany;
    String selectedCompanyId="";
    ArrayList<String> cnames=new ArrayList<>();
    ArrayList<String> cids=new ArrayList<>();
    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user__listing, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        recycleruser = view.findViewById(R.id.recycleruser);
        search = view.findViewById(R.id.search);
        selectcompany = view.findViewById(R.id.selectcompany);
        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
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

                if (username.size()>0){
                   // filter(s.toString());
                }
            }
        });




        user_Listing_Adapter = new User_Listing_Adapter(getContext(),username);
        recycleruser.setAdapter(user_Listing_Adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycleruser.setLayoutManager(layoutManager);
        recycleruser.setHasFixedSize(true);
        //user_Listing_Adapter.notifyDataSetChanged();
        companyget();

        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                Log.e("selectedCompany",selectedCompanyId);
                user_list(selectedCompanyId);

            }
        });


        return view;
    }
    /*void filter(String text){
        List<String> temp = new ArrayList();
        for(String d: username){
            if(d.toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        user_Listing_Adapter.updateList(temp);
    }
*/
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

    public void user_list(String c_id)
    {

        username.clear();
        RequestParams params = new RequestParams();
        params.add("company_id",c_id);
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token",token);
        client.post(Constant.BASE_URL + "user/getSubUserListing",params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String(responseBody);
                Log.e("responseuserget",response);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");


                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String user_image_path = data.getString("user_image_path");
                        JSONArray user = data.getJSONArray("user");
                        if (user.length()>0) {
                            for (int i = 0; i < user.length(); i++) {
                                JSONObject item = user.getJSONObject(i);
                                String user_id = item.getString("user_id");
                                String email_id = item.getString("email");
                                String full_name = item.getString("fullname");
                                String phone_number = item.getString("phone_number");
                                String role = item.getString("role");
                                String image = item.getString("image");


                                JSONObject permission= item.getJSONObject("permission");
                                String invoice = permission.getString("invoice");
                                String estimate = permission.getString("estimate");
                                String stock = permission.getString("stock");
                                String receipt = permission.getString("receipt");
                                String purchase_order = permission.getString("purchase_order");
                                String payment_voucher = permission.getString("payment_voucher");
                                String tax = permission.getString("tax");
                                String customer = permission.getString("customer");
                                String supplier = permission.getString("supplier");
                                String product = permission.getString("product");
                                String service = permission.getString("service");
                                String debit_note = permission.getString("debit_note");
                                String credit_note = permission.getString("credit_note");
                                String sub_admin = permission.getString("sub_admin");

                                User_list user_list = new User_list();
                                user_list.setUser_id(user_id);
                                user_list.setEmail_id(email_id);
                                user_list.setFull_name(full_name);
                                user_list.setPhone_number(phone_number);
                                user_list.setRole(role);
                                user_list.setInvoice(invoice);
                                user_list.setEstimate(estimate);
                                user_list.setStock(stock);
                                user_list.setReceipt(receipt);
                                user_list.setPurchase_order(purchase_order);
                                user_list.setPayment_voucher(payment_voucher);
                                user_list.setTax(tax);
                                user_list.setCustomer(customer);
                                user_list.setSupplier(supplier);
                                user_list.setProduct(product);
                                user_list.setService(service);
                                user_list.setDebit_note(debit_note);
                                user_list.setCredit_note(credit_note);
                                user_list.setSub_admin(sub_admin);
                                user_list.setImage(image);
                                user_list.setImage_path(user_image_path);

                                username.add(user_list);
                                user_Listing_Adapter.notifyDataSetChanged();


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
                    Log.e("responseusergetF",response);

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
