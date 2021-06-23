package com.receipt.invoice.stock.sirproject.CN;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Base.BaseFragment;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Invoice.CheckForSDCard;
import com.receipt.invoice.stock.sirproject.Invoice.ConvertToReceiptsActivity;
//import com.receipt.invoice.stock.sirproject.Invoice.InvoiceViewActivityWebView;
import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.Invoice.SwipeHelper;
import com.receipt.invoice.stock.sirproject.Model.InvoiceData;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.RetrofitApi.ApiInterface;
import com.receipt.invoice.stock.sirproject.RetrofitApi.RetrofitInstance;
import com.receipt.invoice.stock.sirproject.Utils.Utility;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ListOfCreditNotes extends BaseFragment {

    private static final String TAG = "ListOfCreditNotes";
    // List Of Invoice Dtata
    ApiInterface apiInterface;
    ImageView avibackground;
    ProgressDialog dialog;
    AwesomeSpinner selectcompany;
    ArrayList<String> cnames = new ArrayList<>();
    ArrayList<String> cids = new ArrayList<>();
    ArrayList<String> arrayColor = new ArrayList<>();


    String company_id, company_name, company_address, company_contact, company_email, company_website, payment_bank_name, payment_currency, payment_iban, payment_swift_bic;
    ImageView imageViewmenu;
    String selectedCompanyId = "";
    String selectedCompanyName = "";
    String invoice_idstr;
    BottomSheetDialog bottomSheetDialog;
    // Custom Invoice Detail
    RecyclerView recycler_invoices;
    EditText search;
    ArrayList<InvoiceData> list = new ArrayList<>();
    ArrayList<InvoiceData> temp = new ArrayList();

    CreditNoteListAdapterdt invoicelistAdapterdt;
    String invoiceStatus = "";
    int invoice_position = 0;
    String invoice_text = "";
    int invoice_color = 0;
    String invoiceidbypos = "";
    String receipt_count, estimate_count, invoice_useriddt, invoice_count;
    String templatestr = "1";
    String shareInvoicelink = AllSirApi.BASE_URL_INDEX+"view/invoice/";
    //  Shaare invoice lnk
    String BaseurlForShareInvoice = "";
    String invoicelistbyurl = "";
    String parmsvalue = "";

    String templateSelect = "";
    String colorCode = "#ffffff";
    private AVLoadingIndicatorView avi;

    String customerName = "";
    String dataNo = "";

    public ListOfCreditNotes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File logoPath = Utility.getFileLogo(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = inflater.inflate(R.layout.fragment_list_of__invoices, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        recycler_invoices = view.findViewById(R.id.recycler_invoices);
        search = view.findViewById(R.id.search);
        search.setHint(getString(R.string.list_SearchByCreditNoteNumber));
        selectcompany = view.findViewById(R.id.selectcompany);
        imageViewmenu = view.findViewById(R.id.imageViewmenu);

        search.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Light.otf"));
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {


                if (list.size() > 0) {
                    filter(s.toString());
                }
            }
        });


        bottomSheetDialog = new BottomSheetDialog(getActivity());
        companyget();
        // COMPANYListingApi();
        setListeners();
        invoicelistAdapterdt = new CreditNoteListAdapterdt(getContext(), list);
        recycler_invoices.setAdapter(invoicelistAdapterdt);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler_invoices.setLayoutManager(layoutManager);
        recycler_invoices.setHasFixedSize(true);
        invoicelistAdapterdt.notifyDataSetChanged();

        SwipeHelper swipeHelper = new SwipeHelper(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.list_More),
                        0,
                        Color.parseColor("#669933"),

                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                if(temp.size() > 0){
                                    customerName = temp.get(pos).getInvoicustomer_name();
                                    dataNo = temp.get(pos).getInvoice_nobdt();
                                    templateSelect = temp.get(pos).getTemplate_type();
                                    Log.e(TAG, "templateSelect: "+templateSelect);
                                    invoiceidbypos = temp.get(pos).getInvoice_userid();
                                    String ilnvoiceStatus = temp.get(pos).getInvocestatus();
                                    String pdflink = temp.get(pos).getInvoicepdflink();
                                    String sahrelink = temp.get(pos).getInvoice_share_link().replace("13.233.155.0", "13.126.22.0");
                                    createbottomsheet_invoiceop(invoiceidbypos, ilnvoiceStatus, pdflink, sahrelink);
                                    invoicelistAdapterdt.notifyDataSetChanged();
                                    bottomSheetDialog.show();
                                }else{
                                    customerName = list.get(pos).getInvoicustomer_name();
                                    dataNo = list.get(pos).getInvoice_nobdt();
                                    templateSelect = list.get(pos).getTemplate_type();
                                    Log.e(TAG, "templateSelect: "+templateSelect);
                                    invoiceidbypos = list.get(pos).getInvoice_userid();
                                    String ilnvoiceStatus = list.get(pos).getInvocestatus();
                                    String pdflink = list.get(pos).getInvoicepdflink();
                                    String sahrelink = list.get(pos).getInvoice_share_link().replace("13.233.155.0", "13.126.22.0");
                                    createbottomsheet_invoiceop(invoiceidbypos, ilnvoiceStatus, pdflink, sahrelink);
                                    invoicelistAdapterdt.notifyDataSetChanged();
                                    bottomSheetDialog.show();
                                }

                            }
                        }
                ));



//                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                        "Delete",
//                        0,
//                        Color.parseColor("#ff0000"),
//
//                        new SwipeHelper.UnderlayButtonClickListener() {
//                            @Override
//                            public void onClick(final int pos) {
//                                String  invoiceidbypos = list.get(pos).getInvoice_userid();
//                                Log.e(TAG, "invoiceidbypos: "+invoiceidbypos);
//                                deleteInvoice(invoiceidbypos);
//                            }
//                        }
//                ));



            }
        };
        swipeHelper.attachToRecyclerView(recycler_invoices);


//        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                try {
//                    invoice_position = viewHolder.getAdapterPosition();
//                    invoiceStatus = list.get(invoice_position).getInvocestatus();
//                    invoice_useriddt = list.get(invoice_position).getInvoice_userid();
//                    Log.e("UinvoiceStatus", invoiceStatus);
//
//                    if (invoiceStatus.equals("1")) {
//                        invoice_text = "Mark as unpaid";
//                        invoice_color = R.color.red;
//                        invoiceStatus = "2";
//
//
//                    } else {
//                        invoice_text = "Mark as Paid";
//                        invoice_color = R.color.green;
//                        invoiceStatus = "1";
//
//                    }
//                    UpdateInvoiceStatusmethodh(invoiceStatus);
//
//
//                } catch (Exception e) {
//                    Log.e("MainActivity", e.getMessage());
//                }
//            }
//
//            // You must use @RecyclerViewSwipeDecorator inside the onChildDraw method
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                        .addSwipeRightBackgroundColor(invoice_color)
//                        .addSwipeRightLabel(invoice_text)
//                        .setSwipeRightLabelColor(Color.WHITE)
//                        .create()
//                        .decorate();
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recycler_invoices);
        return view;


    }

    void filter(String text) {
        temp.clear();

        for (InvoiceData d : list) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getInvoice_nobdt().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        invoicelistAdapterdt.updateList(temp);
    }


    private void setListeners() {

        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                colorCode = arrayColor.get(position);
                selectedCompanyName = cnames.get(position);
                Log.e("colorCode",colorCode);
                Log.e("company_id",selectedCompanyId);
                String parmavalue = "All";
                InvoicelistData(parmavalue);
                CompanyInformation(selectedCompanyId);

            }
        });

        imageViewmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedCompanyId.equals("")) {
                    Constant.ErrorToast(getActivity(), getString(R.string.dialog_SelectACompany));

                } else {

                    createbottomsheet_FilterData();

                }

//                if (CheckForSDCard.isSDCardPresent()) {
//
//                    //check if app has permission to write to the external storage.
//                    if (checkPermission()) {
//                        //Get the URL entered
//                        String url = "http://kskglobalsourcing.com/boost-crm/storage/files/shares/example.pdf";
//                        new DownloadFile(getActivity()).execute(url);
//                    } else {
//
//                    }
//
//
//                } else {
//                    Toast.makeText(getActivity(),
//                            "SD Card not found", Toast.LENGTH_LONG).show();
//
//                }

            }
        });
    }

    private void CompanyInformation(String selectedCompanyId) {

        RequestParams params = new RequestParams();
        params.add("company_id", this.selectedCompanyId);
        params.add("product", "1");
        params.add("service", "1");
        params.add("customer", "1");
        params.add("tax", "1");
       // params.add("receipt", "1");
        params.add("credit_note", "1");
        params.add("warehouse", "1");

        params.add("language", ""+getLanguage());

        String token = Constant.GetSharedPreferences(getActivity(), Constant.ACCESS_TOKEN);
        Log.e("token", token);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "company/info", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("Company Information", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String image_path = data.getString("customer_image_path");
                        Log.e("imagepath customer", image_path);
                        receipt_count = data.getString("receipt_count");
                        estimate_count = data.getString("estimate_count");
                        invoice_count = data.getString("invoice_count");
                        Log.e("estimate_count", estimate_count);
                        //  Log.e("invoice_count",invoice_count);


                   /*    companyreceiptno = Integer.parseInt(receipt_count) + 1;
                        Log.e("companyreceiptno", String.valueOf(companyreceiptno));
*/
                        JSONArray company = data.getJSONArray("company");


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    Log.e("responsecustomersF", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            //Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Constant.ErrorToast(getActivity(), "Something went wrong, try again!");
                }
            }
        });


    }

    private void InvoicelistData(String paramsvalue) {
              /* list.clear();
              String token = Constant.GetSharedPreferences(getActivity(), Constant.ACCESS_TOKEN);
              Call<ResponseListInociceDto> resposresult = apiInterface.getListOfInvoice(token, selectedCompanyId);
                      resposresult.enqueue(new Callback<ResponseListInociceDto>() {
                                 @Override
                                 public void onResponse(Call<ResponseListInociceDto> call, retrofit2.Response<ResponseListInociceDto> response) {


                                     DataInvoicelistDto data = response.body().getDataInvoicelistDto();



                                     List<InvoiceItemlistInvoiceDto> litdatadto = data.getInvoice();

                                    for(int i=0;i<litdatadto.size();i++)
                                    {

                                        InvoiceData company_list = new InvoiceData();
                                        company_list.setInvoice_userid(litdatadto.get(i).getInvoiceId());
                                        company_list.setInvoicustomer_name(litdatadto.get(i).getCustomerName());
                                        company_list.setInvoice_nobdt(litdatadto.get(i).getInvoiceNo());
                                        company_list.setInvoicedue_date(litdatadto.get(i).getDueDate());
                                        company_list.setInvoicetotlaprice(litdatadto.get(i).getTotal());
                                        company_list.setInvocestatus(litdatadto.get(i).getStatus());
                                        company_list.setInvoicepdflink(litdatadto.get(i).getPdf());
                                        company_list.setInvoice_share_link(litdatadto.get(i).getLink());
                                        list.add(company_list);



                                    }

                                     invoicelistAdapterdt.notifyDataSetChanged();

                                 //    Log.e("ustomerimgpath",Customerimgpath);

                                 }

                                 @Override
                                 public void onFailure(Call<ResponseListInociceDto> call, Throwable t) {

                                 }
                             });
    }*/


        list.clear();
        avi.smoothToShow();
        RequestParams params = new RequestParams();


        if (paramsvalue.equals("All")) {
            params.add("company_id", selectedCompanyId);
            Log.e("comany id", selectedCompanyId);
            invoicelistbyurl = "creditnote/getListingByCompany";
        } else if (paramsvalue.equals("PAID")) {
            params.add("company_id", selectedCompanyId);
            params.add("invoice_status", paramsvalue);
            invoicelistbyurl = "creditnote/getListingByCompany";
        } else if (paramsvalue.equals("UNPAID")) {
            params.add("company_id", selectedCompanyId);
            params.add("invoice_status", paramsvalue);
            invoicelistbyurl = "creditnote/getListingByCompany";
        }
        else if (paramsvalue.equals("Bydate")) {
            params.add("company_id", selectedCompanyId);
            params.add("invoice_status", paramsvalue);
            invoicelistbyurl = "creditnote/getListingByDate";
        }


        Log.e("params",params.toString());
        String token = Constant.GetSharedPreferences(getActivity(), Constant.ACCESS_TOKEN);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + invoicelistbyurl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("responsecustomers", response);
                avi.smoothToHide();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray customer = data.getJSONArray("credit_note");
                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject item = customer.getJSONObject(i);
                            invoice_idstr = item.getString("credit_note_id");
                            Log.e("invoice_id company", invoice_idstr);
                            String invoice_no = item.getString("credit_note_no");
                            String due_date = item.getString("credit_note_date");
                            String total = item.getString("total");

                            String statusinvoice = item.getString("order_status_id");
                            //Log.e("customer id", customer);
                            Log.e("invoice_no", invoice_no);
                            //JSONObject customerobj = item.getJSONObject("customer");

                            String customer_name = item.getString("customer_name");

                            Log.e("Customer name",customer_name);
                            String linkpd = item.getString("link");
                            String pdffilename = item.getString("pdf");
                            String payment_currency = item.getString("currency_symbol");

                            InvoiceData company_list = new InvoiceData();
                            Log.e("customer_name id", customer_name);

                            company_list.setInvoice_userid(invoice_idstr);
                            company_list.setInvoicustomer_name(customer_name);
                            company_list.setInvoice_nobdt(invoice_no);
                            company_list.setInvoicedue_date(due_date);
                            company_list.setInvoicetotlaprice(total);
                            company_list.setInvocestatus(statusinvoice);
                            company_list.setInvoicepdflink(pdffilename);
                            company_list.setInvoice_share_link(linkpd);
                            company_list.setPayment_currency(payment_currency);
                         //   company_list.setTemplate_type(item.getString("template_type"));
                            list.add(company_list);
                           // if (list.size() < 20) {
                                invoicelistAdapterdt.updateList(list);
                          //  }

                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    Log.e("responsecustomersF", response);
                    avi.smoothToHide();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Constant.ErrorToast(getActivity(), "Something went wrong, try again!");
                }
            }
        });
    }




    private void deleteInvoice(String s) {

        list.clear();
        avi.smoothToShow();
        RequestParams params = new RequestParams();
//        if (invoice_idstr.equals("") || invoice_idstr.equals("null")) {
//            Constant.ErrorToast(getActivity(), "Invoice not found");
//        } else {

        params.add("credit_note_id", s);
        //  params.add("order_status_id", s);
//
//            Log.e("invoice_idupdate", invoice_idstr);
//            Log.e("s order status", s);
        String token = Constant.GetSharedPreferences(getActivity(), Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "creditnote/delete", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("invoice status", response);
                avi.smoothToHide();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        parmsvalue = "All";
                        InvoicelistData(parmsvalue);
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    Log.e("responsecustomersF", response);
                    avi.smoothToHide();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Constant.ErrorToast(getActivity(), "Something went wrong, try again!");
                }
            }
        });
//        }
    }





    private void UpdateInvoiceStatusmethodh(String s) {

        list.clear();
        avi.smoothToShow();
        RequestParams params = new RequestParams();
        if (invoice_idstr.equals("") || invoice_idstr.equals("null")) {
            Constant.ErrorToast(getActivity(), getString(R.string.list_Invoicenotfound));
        } else {

            params.add("invoice_id", invoice_useriddt);
            params.add("order_status_id", s);

            Log.e("invoice_idupdate", invoice_idstr);
            Log.e("s order status", s);
            String token = Constant.GetSharedPreferences(getActivity(), Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.addHeader("Access-Token", token);
            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL + "invoice/updateStatus", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.e("invoice status", response);
                    avi.smoothToHide();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("true")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONObject data = jsonObject1.getJSONObject("invoice");
                            //String customer = data.getString("invoice_id");
                            // String invoice_no = data.getString("invoice_no");
                            String due_date = data.getString("due_date");
                            String total = data.getString("total");
                            String statusinvoice = data.getString("status");
                            // Log.e("customer id", customer);
                            // Log.e("invoice_no id", invoice_no);
                            Log.e("due_date id", due_date);
                            Log.e("total id", total);
                            Log.e("statusinvoice id", statusinvoice);
                            parmsvalue = "All";
                            InvoicelistData(parmsvalue);
                            Log.e("invoice id afterup", invoice_idstr);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (responseBody != null) {
                        String response = new String(responseBody);
                        Log.e("responsecustomersF", response);
                        avi.smoothToHide();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            if (status.equals("false")) {
                                Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Constant.ErrorToast(getActivity(), "Something went wrong, try again!");
                    }
                }
            });
        }
    }

    private void createbottomsheet_invoiceop(String invoiceidbypos, String ilnvoiceStatus, String pdflink, String sharelink) {

        if (bottomSheetDialog != null) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.bottominvoiceview, null);
            TextView viewinvoicebotom, viewinvoicetemplate, duplicateinvoitxt, shareinvoicetxt, recepitsviewtxt;
            Log.e("ilnvoiceStatus", ilnvoiceStatus);
            LinearLayout viewinvoicerecipts;
            viewinvoicerecipts = view.findViewById(R.id.viewinvoicerecipts);
            recepitsviewtxt = view.findViewById(R.id.recepitsviewtxt);
            if (ilnvoiceStatus.equals("2")) {
                viewinvoicerecipts.setVisibility(View.VISIBLE);
                recepitsviewtxt.setVisibility(View.VISIBLE);
            }
            if (ilnvoiceStatus.equals("1")) {
                recepitsviewtxt.setVisibility(View.INVISIBLE);
                viewinvoicerecipts.setVisibility(View.INVISIBLE);
            }
            viewinvoicebotom = view.findViewById(R.id.viewinvoicebotom);
            viewinvoicetemplate = view.findViewById(R.id.viewinvoicetemplate);
            duplicateinvoitxt = view.findViewById(R.id.duplicateinvoitxt);
            shareinvoicetxt = view.findViewById(R.id.shareinvoicetxt);
            viewinvoicebotom.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Bold.otf"));
            viewinvoicetemplate.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Bold.otf"));
            duplicateinvoitxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Bold.otf"));
            shareinvoicetxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Bold.otf"));
            recepitsviewtxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Bold.otf"));

            LinearLayout linearLayoutChangeTemp= view.findViewById(R.id.viewicetemplate);
            linearLayoutChangeTemp.setVisibility(View.GONE);

            LinearLayout linearLayoutviewiceduplicate = view.findViewById(R.id.viewiceduplicate);
            linearLayoutviewiceduplicate.setVisibility(View.GONE);


            viewinvoicebotom.setText(getString(R.string.list_ViewCreditNote));
            viewinvoicetemplate.setText(getString(R.string.list_EditCreditNote));
            duplicateinvoitxt.setText(getString(R.string.list_DuplicateCreditNote));
            shareinvoicetxt.setText(getString(R.string.list_ShareCreditNote));

            viewinvoicebotom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Fragment_Create_Invoice.defaultClick = 1;
                    Log.e(TAG, "templateSelectooo "+templateSelect);
                    Intent intent = new Intent(getContext(), CreditNotesViewActivityWebView.class);
                    intent.putExtra("invoiceID", invoiceidbypos);
                    intent.putExtra("templatestr", templatestr);
                    intent.putExtra("templateSelect", ""+templateSelect);
                    intent.putExtra("colorCode", ""+colorCode);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    bottomSheetDialog.dismiss();
                }
            });


            viewinvoicetemplate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (selectedCompanyId.equals("")) {
                        Constant.ErrorToast(getActivity(), getString(R.string.dialog_SelectACompany));
                        bottomSheetDialog.show();
                    } else {

                        ViewTamlatemethodh();
                        bottomSheetDialog.show();
                    }
                }
            });

            duplicateinvoitxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
////                    Fragment_Create_Invoice.defaultClick = 1;
//                    SavePref pref = new SavePref();
//                    pref.SavePref(getActivity());
//                    pref.setTemplate(0);
//
//                    Intent intent = new Intent(getContext(), EditCreditNotesActivity.class);
//                    intent.putExtra("invoiceID", invoiceidbypos);
//                    intent.putExtra("invoice_count", invoice_count);
//
//
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                    bottomSheetDialog.dismiss();
                }
            });




            shareinvoicetxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Fragment_Create_Invoice.defaultClick = 1;
                    final Dialog mybuilder = new Dialog(getContext());
                    mybuilder.setContentView(R.layout.dialogsharefile);

                    TextView txtSHAREvalue, txtcancelvalue, txtPDFfvalue, txtheadvalue;


                    txtSHAREvalue = mybuilder.findViewById(R.id.txtpdfvalue);
                    txtPDFfvalue = mybuilder.findViewById(R.id.txtsharefvalue);
                    txtcancelvalue = mybuilder.findViewById(R.id.txtcancelvalue);
                    txtheadvalue = mybuilder.findViewById(R.id.txtheadvalue);


                    txtheadvalue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Bold.otf"));
                    txtPDFfvalue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
                    txtPDFfvalue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
                    txtSHAREvalue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));


                    mybuilder.show();
                    mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    Window window = mybuilder.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawableResource(R.color.transparent);








                    txtSHAREvalue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.e(TAG, "sharelink:: "+sharelink);

                            Log.e(TAG, "sharelink:: "+sharelink);
                            Log.e(TAG, "customerName:: "+customerName);
                            Log.e(TAG, "dataNo:: "+dataNo);

                            String subject = Utility.getRealValueCreditNoteWithoutPlus(dataNo)+" "+getString(R.string.list_From)+" "+selectedCompanyName;
                            String txt = getString(R.string.list_YourCreditNoteviewed) +
                                    "\n\n" +sharelink ;

                            try {
                                if (!sharelink.endsWith(".pdf")) {
                                    Toast.makeText(getActivity(), getString(R.string.list_NoFileFound), Toast.LENGTH_LONG).show();
                                } else {
                                    BaseurlForShareInvoice = shareInvoicelink + sharelink;
                                }






                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/jpeg");
                                share.putExtra(Intent.EXTRA_SUBJECT, subject);
                                share.putExtra(Intent.EXTRA_TEXT, txt);

                                share.putExtra(Intent.EXTRA_STREAM,
                                        Uri.parse("file:///sdcard/share.jpg"));

                                if (Utility.isAppAvailable(getActivity(), "com.google.android.gm")){
                                    share.setPackage("com.google.android.gm");
                                }
                                startActivity(share);


//                                String to = "";
//                              //  String subject= "Hi I am subject";
//                              //  String body="Hi I am test body";
//                                String mailTo = "mailto:" + to +
//                                        "?&subject=" + Uri.encode(subject) +
//                                        "&body=" + Uri.encode(txt);
//
//                                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                                // intent.setType("text/plain");
//                                String message="File to be shared is " + "file_name" + ".";
////                                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
////                                intent.putExtra(Intent.EXTRA_TEXT, message);
//                                //intent.setData(Uri.parse("mailto:xyz@gmail.com"));
//                                intent.setData(Uri.parse(mailTo));
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);




//                                String finalurl = BaseurlForShareInvoice;
//
//                                String cc = "android.resource://com.receipt.invoice.stock.sirproject/"+R.drawable.a;
//
//                                Uri imgUri=Uri.parse("android.resource://com.receipt.invoice.stock.sirproject/"+R.drawable.a);
//
//                                String company_stamp = "/android_res/drawable/white_img.png";
//
//
//                                String company_stamp22 = "/sdcard/thanksimg.png";
//
//
//
//                                String[] TO = {"email@server.com"};
//                                Uri uri = Uri.parse("mailto:email@server.com")
//                                        .buildUpon()
//                                        .appendQueryParameter("subject", subject)
//                                        .appendQueryParameter("body", txt)
//                                        // .appendQueryParameter("image", cc)
//                                        .build();
//                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
//                                // emailIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
//                                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" +company_stamp22));
//                                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//                                startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                            } catch (Exception e) {
                                //e.toString();
                            }
                            bottomSheetDialog.dismiss();
                            mybuilder.dismiss();
                        }
                    });


                    txtPDFfvalue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e(TAG, "pdflink:: "+sharelink);
                            try {
                                if (!sharelink.endsWith(".pdf")) {
                                    Toast.makeText(getActivity(), getString(R.string.list_NoFileFound), Toast.LENGTH_LONG).show();
                                } else {
//                    /*              String pdfurl = "http://13.126.22.0/saad/app/uploads/invoice/pdf/" + pdflink;
//                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                                    shareIntent.setType("text/plain");
//                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "PDF File");
//                                    String shareMessage = pdfurl;
//                                    shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(shareMessage));
//                                    getActivity().startActivity(Intent.createChooser(shareIntent, "choose one"));
//*/                                  Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
//                                    shareIntent.setType("text/plain");
//                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
//                                    //String app_url = "file:///home/apptunix/Desktop/invoice.html";
//                                    //shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
//                                    startActivity(Intent.createChooser(shareIntent, "Share via"));


                                    if (CheckForSDCard.isSDCardPresent()) {

                                        //check if app has permission to write to the external storage.
                                        if (checkPermission()) {
                                            //Get the URL entered
                                            String url = sharelink;
                                            String subject = Utility.getRealValueCreditNoteWithoutPlus(dataNo)+" "+getString(R.string.list_From)+" "+selectedCompanyName;
                                            new DownloadFile(getActivity(), subject).execute(url.replace("https", "http"));
                                        } else {

                                        }


                                    } else {
                                        Toast.makeText(getActivity(),
                                                "SD Card not found", Toast.LENGTH_LONG).show();

                                    }

                                }

                            } catch (Exception e) {
                                //e.toString();
                            }
                            bottomSheetDialog.dismiss();
                            mybuilder.dismiss();
                        }
                    });



                    txtcancelvalue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mybuilder.dismiss();
                            bottomSheetDialog.dismiss();
                        }
                    });


                }

            });
            bottomSheetDialog = new BottomSheetDialog(getActivity());
            bottomSheetDialog.setContentView(view);



            recepitsviewtxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Fragment_Create_Invoice.defaultClick = 1;
                    Intent intent = new Intent(getContext(), ConvertToReceiptsActivity.class);
                    intent.putExtra("invoiceID", invoiceidbypos);
                    intent.putExtra("companyreceiptno", receipt_count);
                    intent.putExtra("compnayestimate_count", estimate_count);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                    bottomSheetDialog.dismiss();
                }
            });
        }
    }

    private void ViewTamlatemethodh() {

        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_template_invoice, null);

            WebView selectionimg1, selectionimg2, selectionimg3, selectionimg4;

            selectionimg1 = view.findViewById(R.id.selectionimg1);
            selectionimg2 = view.findViewById(R.id.selectionimg2);
            selectionimg3 = view.findViewById(R.id.selectionimg3);
            selectionimg4 = view.findViewById(R.id.selectionimg4);


          /*  String signature_of_issuername="/android_res/drawable/f_cc_visa.png";

            String Signatureincoicestr="";



//
             String productitem = null;

        String productitemlist = null;
        try {
            for (int i = 0; i <2; i++) {


                productitem = IOUtils.toString(getActivity().getAssets().open("customer_single_item.html"))


                        .replaceAll("#NAME#", "Dev")
                        .replaceAll("#DESC#", "Very nfjlsajfsajfdk")
                        .replaceAll("#UNIT#", "Pieces")
                        .replaceAll("#QUANTITY#", "12")
                        .replaceAll("#PRICE#", "25000")
                        .replaceAll("#TOTAL#", "270000");

                productitemlist = productitemlist + productitem;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

            try {
                Signatureincoicestr = IOUtils.toString(getActivity().getAssets().open("Signatures.html"))
                        .replaceAll("dataimageCompany_Stamp", "file://" +signature_of_issuername)
                        .replaceAll("dataimageRecieverImage", "file://" + signature_of_issuername)
                        .replaceAll("data:imageSige_path", "file://" + signature_of_issuername);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String companylogo="/android_res/drawable/confectionary.png";
            String content = null;
            try {
                content = IOUtils.toString(getActivity().getAssets().open("template.html"))

                        .replaceAll("Company Name", "Tata Power")
                        .replaceAll("Address", "MOhali 8 B")
                        .replaceAll("Contact No.", "999999999")
                        .replaceAll("Website", "companywebsitestr")
                        .replaceAll("Email", "dev@gmail.com")
                        .replaceAll("#LOGO_IMAGE#", companylogo)
                        .replaceAll("INV-564", "Inv#201")
                        .replaceAll("invD", "22-2-21")
                        .replaceAll("DueDate", "22-2-21")
                        .replaceAll("crTerms", "7 dyas")
                        .replaceAll("refNo", "230")
                        .replaceAll("GrossAm-", "24444")
                        .replaceAll("Discount-", "1000")
                        .replaceAll("SubTotal-", "25000")
                        .replaceAll("Txses-", "230")
                        .replaceAll("Shipping-", "+"+"1000")
                        .replaceAll("Total Amount-", "25000")
                        .replaceAll("PaidsAmount", "1000")
                        .replaceAll("PaidsAmount", "1000")
                        .replaceAll("Balance Due-", "2000")
                        .replaceAll("Checkto", "211212")
                        .replaceAll("BankName", "Punjab nationalbank")
                        .replaceAll("IBAN", "Asdb")
                        .replaceAll("Currency", "RS")
                        .replaceAll("Swift/BICCode", "ASPF")
                        .replaceAll("Client N", "Dev")
                        .replaceAll("Client A", "Raj")
                        .replaceAll("Client C P", "Ajay")
                        .replaceAll("Client C N", "8888888888")
                        .replaceAll("Client Web", "wwww.tatapower.com")
                        .replaceAll("Client E", "dev@gmail.com")
                        .replaceAll("Notes-", "Very Usefull eamail all document related issue")
                        .replaceAll("#SIGNATURES#", Signatureincoicestr)
                        .replaceAll("#ITEMS#", productitemlist)


                                .replaceAll("#TEMP3#", String.valueOf(R.color.blue));

            } catch (IOException e) {
                e.printStackTrace();
            }
            selectionimg1.loadDataWithBaseURL("file:///android_asset/template.html", content, "text/html", "UTF-8", null);
            selectionimg2.loadDataWithBaseURL("file:///android_asset/template.html", content, "text/html", "UTF-8", null);
            selectionimg3.loadDataWithBaseURL("file:///android_asset/template.html", content, "text/html", "UTF-8", null);
            selectionimg4.loadDataWithBaseURL("file:///android_asset/template.html", content, "text/html", "UTF-8", null);

*/
            selectionimg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    templatestr = "1";
                    selectionimg1.setBackgroundResource(R.drawable.selectortem);
                    selectionimg3.setBackgroundResource(R.drawable.signaturebgwhite);
                    selectionimg4.setBackgroundResource(R.drawable.signaturebgwhite);
                    selectionimg2.setBackgroundResource(R.drawable.signaturebgwhite);

                }
            });
            selectionimg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    templatestr = "2";
                    selectionimg2.setBackgroundResource(R.drawable.selectortem);
                    selectionimg1.setBackgroundResource(R.drawable.signaturebgwhite);
                    selectionimg4.setBackgroundResource(R.drawable.signaturebgwhite);
                    selectionimg3.setBackgroundResource(R.drawable.signaturebgwhite);

                }
            });
            selectionimg3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    templatestr = "3";
                    selectionimg3.setBackgroundResource(R.drawable.selectortem);
                    selectionimg1.setBackgroundResource(R.drawable.signaturebgwhite);
                    selectionimg2.setBackgroundResource(R.drawable.signaturebgwhite);
                    selectionimg4.setBackgroundResource(R.drawable.signaturebgwhite);

                }
            });
            selectionimg4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    templatestr = "4";

                    selectionimg4.setBackgroundResource(R.drawable.selectortem);
                    selectionimg1.setBackgroundResource(R.drawable.signaturebgwhite);
                    selectionimg2.setBackgroundResource(R.drawable.signaturebgwhite);
                    selectionimg3.setBackgroundResource(R.drawable.signaturebgwhite);

                }
            });

            bottomSheetDialog = new BottomSheetDialog(getActivity());
            bottomSheetDialog.setContentView(view);

        }
    }

    private void createbottomsheet_FilterData() {

        final Dialog mybuilder = new Dialog(getActivity());
        mybuilder.setContentView(R.layout.filtered_incoicedata);
        TextView unpaidinvoicetxt, paidinvoicetxt, bydateinvoicetxt, allinvoicetxt, cancelinvoicetxt;
        unpaidinvoicetxt = mybuilder.findViewById(R.id.unpaidinvoicetxt);
        paidinvoicetxt = mybuilder.findViewById(R.id.paidinvoicetxt);
        bydateinvoicetxt = mybuilder.findViewById(R.id.bydateinvoicetxt);
        allinvoicetxt = mybuilder.findViewById(R.id.allinvoicetxt);
        cancelinvoicetxt = mybuilder.findViewById(R.id.cancelinvoicetxt);

        unpaidinvoicetxt.setVisibility(View.GONE);
        paidinvoicetxt.setVisibility(View.GONE);
        allinvoicetxt.setVisibility(View.GONE);

        View view = mybuilder.findViewById(R.id.view);
        View view1 = mybuilder.findViewById(R.id.view1);
        View viewdfr1 = mybuilder.findViewById(R.id.viewdfr1);
        view.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        viewdfr1.setVisibility(View.GONE);


        unpaidinvoicetxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
        paidinvoicetxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
        bydateinvoicetxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
        allinvoicetxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
        cancelinvoicetxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));



        paidinvoicetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parmsvalue = "PAID";
                InvoicelistData(parmsvalue);
                mybuilder.dismiss();
            }
        });
        unpaidinvoicetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parmsvalue = "UNPAID";
                InvoicelistData(parmsvalue);
                mybuilder.dismiss();
            }
        });

        allinvoicetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parmsvalue = "All";
                InvoicelistData(parmsvalue);
                mybuilder.dismiss();
            }
        });
        bydateinvoicetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parmsvalue = "Bydate";
                InvoicelistData(parmsvalue);
                mybuilder.dismiss();
            }
        });

        cancelinvoicetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mybuilder.dismiss();
            }
        });

        mybuilder.show();
        mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = mybuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.transparent);


    }

    private void companyget() {


        cnames.clear();
        cids.clear();
        avi.smoothToShow();
        String token = Constant.GetSharedPreferences(getActivity(), Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        RequestParams params = new RequestParams();
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "company/listing", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("responsecompany", response);
                avi.smoothToHide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray company = data.getJSONArray("company");
                        if (company.length() > 0) {
                            for (int i = 0; i < company.length(); i++) {
                                JSONObject item = company.getJSONObject(i);
                                company_id = item.getString("company_id");
                                company_name = item.getString("name");
                                company_address = item.getString("address");
                                company_contact = item.getString("phone_number");
                                company_email = item.getString("email");
                                company_website = item.getString("website");
                                payment_bank_name = item.getString("payment_bank_name");
                                payment_currency = item.getString("payment_currency");
                                payment_iban = item.getString("payment_iban");
                                payment_swift_bic = item.getString("payment_swift_bic");
                                String colorCode = item.getString("color");
                                Log.e("CompanyId",company_id);

                                cnames.add(company_name);
                                cids.add(company_id);
                                arrayColor.add(colorCode);



                            }
                        }

                        ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cnames);
                        selectcompany.setAdapter(namesadapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    avi.smoothToHide();
                    Log.e("responsecompanyF", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }




    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }




    private static class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;
        Context context;
        String subject;

        DownloadFile(Context c, String sub) {
            context = c;
            subject = sub;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1);

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "SAAR/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String newFileName = "CreditNote.pdf";
                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + newFileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "" + folder + newFileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();


            Log.e(TAG, "onPostExecuteFile "+message);

            // Display File path after downloading
            //  Toast.makeText(context, message, Toast.LENGTH_LONG).show();


//                                    Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
//                                    shareIntent.setType("text/plain");
//                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
//                                    //String app_url = "file:///home/apptunix/Desktop/invoice.html";
//                                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
//                                    context.startActivity(Intent.createChooser(shareIntent, "Share via"));
///storage/emulated/0/SAAR/CreditNote.pdf

            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File fileWithinMyDir = new File(message);
            Uri photoURI = FileProvider.getUriForFile(context,
                    "com.receipt.invoice.stock.sirproject.provider",
                    fileWithinMyDir);

            if(fileWithinMyDir.exists()) {
                intentShareFile.setType("application/pdf");
                Uri outputFileUri = Uri.fromFile(fileWithinMyDir);
                intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                        subject);

//                if (isAppAvailable(context, "com.google.android.gm")){
//                    intentShareFile.setPackage("com.google.android.gm");
//                }
//                context.startActivity(intentShareFile);

                context.startActivity(Intent.createChooser(intentShareFile, context.getString(R.string.list_ShareFile)));
            }

        }
    }


    public static Boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        boolean isInstalled;
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

}