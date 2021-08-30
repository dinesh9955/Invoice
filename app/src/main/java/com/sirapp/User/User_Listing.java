package com.sirapp.User;

import android.app.Dialog;
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
import com.sirapp.Adapter.User_Listing_Adapter;
import com.sirapp.Constant.Constant;
import com.sirapp.Model.InvoiceData;
import com.sirapp.Model.User_list;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseFragment;
import com.sirapp.R;
import com.sirapp.Report.ReportActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class User_Listing extends BaseFragment {


    private static final String TAG = "User_Listing";

    public User_Listing() {
        // Required empty public constructor
    }

    RecyclerView recycleruser;
    ArrayList<User_list> username=new ArrayList<>();
    ArrayList<User_list> temp = new ArrayList();

    User_Listing_Adapter user_Listing_Adapter;
    EditText search;
//    AwesomeSpinner selectcompany;
    Button selectcompany1;
    String selectedCompanyId="";
    ArrayList<String> cnames=new ArrayList<>();
    ArrayList<String> cids=new ArrayList<>();
    private AVLoadingIndicatorView avi;
    TextView textViewMsg;
    ImageView avibackground;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user__listing, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        recycleruser = view.findViewById(R.id.recycleruser);
        search = view.findViewById(R.id.search);
        selectcompany1 = view.findViewById(R.id.selectcompany2);
        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        search.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Light.otf"));
        textViewMsg = view.findViewById(R.id.txtinvoice);
        textViewMsg.setText(getString(R.string.home_NoUsers));
        textViewMsg.setVisibility(View.VISIBLE);



        user_Listing_Adapter = new User_Listing_Adapter(getContext(),username);
        recycleruser.setAdapter(user_Listing_Adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycleruser.setLayoutManager(layoutManager);
        recycleruser.setHasFixedSize(true);

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
                    filter(s.toString());
                }
            }
        });


        //user_Listing_Adapter.notifyDataSetChanged();
        companyget();

//        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
//            @Override
//            public void onItemSelected(int position, String itemAtPosition) {
//                selectedCompanyId = cids.get(position);
//                Log.e("selectedCompany",selectedCompanyId);
//                user_list(selectedCompanyId);
//
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



    void filter(String text) {
        temp.clear();

        Log.e(TAG,  "usernameAAA "+username.size());


        for (User_list d : username) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getFull_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        user_Listing_Adapter.updateList(temp);

        if (temp.size() == 0){
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
                            user_list(selectedCompanyId);
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

    public void user_list(String c_id)
    {

        username.clear();
        RequestParams params = new RequestParams();
        params.add("company_id",c_id);
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "user/getSubUserListing",params, new AsyncHttpResponseHandler() {
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

                        if (username.size() == 0){
                            textViewMsg.setVisibility(View.VISIBLE);
                        }else{
                            textViewMsg.setVisibility(View.GONE);
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
                    user_list(selectedCompanyId);
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
