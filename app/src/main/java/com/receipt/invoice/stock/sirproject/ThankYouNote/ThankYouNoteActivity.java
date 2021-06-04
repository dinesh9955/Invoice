package com.receipt.invoice.stock.sirproject.ThankYouNote;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Invoice.InvoiceViewActivityWebView;
import com.receipt.invoice.stock.sirproject.Invoice.SwipeHelper2;
import com.receipt.invoice.stock.sirproject.InvoiceReminder.InvoiceReminderActivity;
import com.receipt.invoice.stock.sirproject.InvoiceReminder.InvoiceReminderAdapter;
import com.receipt.invoice.stock.sirproject.InvoiceReminder.SendInvoiceReminderActivity;
import com.receipt.invoice.stock.sirproject.Model.InvoiceData;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.RetrofitApi.ApiInterface;
import com.receipt.invoice.stock.sirproject.RetrofitApi.RetrofitInstance;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ThankYouNoteActivity extends BaseActivity {

    private static final String TAG = "ThankYouNoteActivity";
    ArrayList<InvoiceData> list = new ArrayList<>();
    ArrayList<InvoiceData> temp = new ArrayList();

    ApiInterface apiInterface;
    private AVLoadingIndicatorView avi;

    AwesomeSpinner selectcompany;

    ArrayList<String> cids = new ArrayList<>();
    ArrayList<String> cnames = new ArrayList<>();
    ArrayList<String> arrayColor = new ArrayList<>();

    RecyclerView recycler_invoices;
    String selectedCompanyId = "";

    String company_image_path = "";
    EditText search;

    String templateSelect = "";
    String colorCode = "#ffffff";

    String templatestr = "1";

    ThankYouNoteAdapter invoicelistAdapterdt;
    BottomSheetDialog bottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyounote);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Constant.toolbar(ThankYouNoteActivity.this, "Thank You Note");


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        avi = findViewById(R.id.avi);
        recycler_invoices = findViewById(R.id.recycler_invoices);
        selectcompany = findViewById(R.id.selectcompany);
        search = findViewById(R.id.search);


        invoicelistAdapterdt = new ThankYouNoteAdapter(ThankYouNoteActivity.this, list);
        recycler_invoices.setAdapter(invoicelistAdapterdt);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ThankYouNoteActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler_invoices.setLayoutManager(layoutManager);
        recycler_invoices.setHasFixedSize(true);
        invoicelistAdapterdt.notifyDataSetChanged();


        search.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Light.otf"));
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


        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                colorCode = arrayColor.get(position);
                String parmavalue = "PAID";
                InvoicelistData(parmavalue);
            }
        });

        bottomSheetDialog = new BottomSheetDialog(ThankYouNoteActivity.this);
        enableSwipe();

        companyget();
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



    private void companyget() {

        cnames.clear();
        cids.clear();
        avi.smoothToShow();
        String token = Constant.GetSharedPreferences(ThankYouNoteActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + "company/listing", new AsyncHttpResponseHandler() {
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
                        company_image_path = data.getString("company_image_path");
                        JSONArray company = data.getJSONArray("company");
                        String company_id= "", company_name= "", company_address= "", company_contact= "", company_email= "",
                                company_website= "", payment_bank_name= "", payment_currency= "", payment_iban= "", payment_swift_bic= "", logo = "";
                        if (company.length() > 0) {
                            for (int i = 0; i < company.length(); i++) {
                                JSONObject item = company.getJSONObject(i);
                                company_id = item.getString("company_id");
                                company_name = item.getString("name");
                                company_address = item.getString("address");
                                company_contact = item.getString("phone_number");
                                company_email = item.getString("email");
                                company_website = item.getString("website");
                                logo = item.getString("logo");

                                payment_bank_name = item.getString("payment_bank_name");
                                payment_currency = item.getString("payment_currency");
                                payment_iban = item.getString("payment_iban");
                                payment_swift_bic = item.getString("payment_swift_bic");
                                String colorCode = item.getString("color");
                                Log.e("CompanyId",company_id);

                                cnames.add(company_name);
                                cids.add(company_id);
                                arrayColor.add(colorCode);
//                                cAddress.add(company_address);
//                                ccontactNo.add(company_contact);
//                                cWebsite.add(company_website);
//                                cEmail.add(company_email);
//                                cLogo.add(logo);



                            }
                        }

                        ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(ThankYouNoteActivity.this, android.R.layout.simple_spinner_item, cnames);
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



    String invoicelistbyurl = "";
    private void InvoicelistData(String paramsvalue) {

        list.clear();
        avi.smoothToShow();
        RequestParams params = new RequestParams();

        if (paramsvalue.equals("All")) {
            params.add("company_id", selectedCompanyId);
            Log.e("comany id", selectedCompanyId);
            invoicelistbyurl = "invoice/getListingByCompany";
        } else if (paramsvalue.equals("PAID")) {
            params.add("company_id", selectedCompanyId);
            params.add("invoice_status", paramsvalue);
            invoicelistbyurl = "invoice/getListingByCompany";
        } else if (paramsvalue.equals("UNPAID")) {
            params.add("company_id", selectedCompanyId);
            params.add("invoice_status", paramsvalue);
            invoicelistbyurl = "invoice/getListingByCompany";
        }
        else if (paramsvalue.equals("Bydate")) {
            params.add("company_id", selectedCompanyId);
            params.add("invoice_status", paramsvalue);
            invoicelistbyurl = "invoice/getListingByDate";
        }


        Log.e("params",params.toString());
        String token = Constant.GetSharedPreferences(ThankYouNoteActivity.this, Constant.ACCESS_TOKEN);

        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("Access-Token", token);
        client.post(Constant.BASE_URL + invoicelistbyurl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e(TAG, "responsecustomers"+ response);
                avi.smoothToHide();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray customer = data.getJSONArray("invoice");
                        Log.e(TAG, "customerAA "+customer.length());

                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject item = customer.getJSONObject(i);
                            String invoice_idstr = item.getString("invoice_id");
                            Log.e("invoice_id company", invoice_idstr);
                            String invoice_no = item.getString("invoice_no");
                            String due_date = item.getString("due_date");
                            String total = item.getString("total");

                            String void_status = item.getString("void_status");

                            String statusinvoice = item.getString("order_status_id");
                            //Log.e("customer id", customer);
                            Log.e("invoice_no", invoice_no);




                            String customer_name = "";

                            if(item.has("customer_name")){
                                customer_name = item.getString("customer_name");
                            }else{
                                String cc = item.getString("customer");
                                if(!cc.equalsIgnoreCase("null")){
                                    JSONObject customerobj = item.getJSONObject("customer");
                                    Log.e(TAG , "customerobj "+cc);
                                    if(customerobj != null){
                                        customer_name = customerobj.getString("customer_name");
                                    }
                                    Log.e("customer_name_id", customer_name);
                                }
                            }







                            String linkpd = item.getString("link");
                            String pdffilename = item.getString("pdf");

                            String payment_currency = item.getString("currency_symbol");


                            InvoiceData company_list = new InvoiceData();

                            company_list.setInvoice_userid(invoice_idstr);
                            company_list.setPayment_currency(payment_currency);
                            company_list.setInvoicustomer_name(customer_name);
                            company_list.setInvoice_nobdt(invoice_no);
                            company_list.setInvoicedue_date(due_date);
                            company_list.setInvoicetotlaprice(total);
                            company_list.setInvocestatus(statusinvoice);
                            company_list.setInvoicepdflink(pdffilename);
                            company_list.setInvoice_share_link(linkpd);
                            company_list.setTemplate_type(item.getString("template_type"));
                            company_list.setVoid_status(void_status);
                            String is_viewed = item.getString("is_viewed");
                            company_list.setIs_viewed(is_viewed);
                          //  if(!is_viewed.equalsIgnoreCase("0")){
                                list.add(company_list);
                           // }
                        }


                        invoicelistAdapterdt.updateList(list);
                        invoicelistAdapterdt.notifyDataSetChanged();

                        //  enableSwipe();

                    }


                    Log.e(TAG, "customerAAlist "+list.size());

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
                            Constant.ErrorToast(ThankYouNoteActivity.this, jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Constant.ErrorToast(ThankYouNoteActivity.this, "Something went wrong, try again!");
                }
            }
        });
    }





    private void enableSwipe(){
        SwipeHelper2 swipeHelper = new SwipeHelper2(ThankYouNoteActivity.this, recycler_invoices) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                underlayButtons.add(new SwipeHelper2.UnderlayButton(
                        "More",
                        0,
                        Color.parseColor("#669933"),

                        new SwipeHelper2.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                // customerName = list.get(pos).getInvoicustomer_name();
//                                dataNo = list.get(pos).getInvoice_nobdt();

                                if(temp.size() > 0){
                                    templateSelect = temp.get(pos).getTemplate_type();
                                    String invoiceidbypos = temp.get(pos).getInvoice_userid();
                                    String ilnvoiceStatus = temp.get(pos).getInvocestatus();
                                    String pdflink = temp.get(pos).getInvoicepdflink();
                                    String sahrelink = temp.get(pos).getInvoice_share_link().replace("13.233.155.0", "13.126.22.0");
                                    createbottomsheet_invoiceop(invoiceidbypos, ilnvoiceStatus, pdflink, sahrelink);
                                }else{
                                    templateSelect = list.get(pos).getTemplate_type();
                                    String invoiceidbypos = list.get(pos).getInvoice_userid();
                                    String ilnvoiceStatus = list.get(pos).getInvocestatus();
                                    String pdflink = list.get(pos).getInvoicepdflink();
                                    String sahrelink = list.get(pos).getInvoice_share_link().replace("13.233.155.0", "13.126.22.0");
                                    createbottomsheet_invoiceop(invoiceidbypos, ilnvoiceStatus, pdflink, sahrelink);
                                }

                                invoicelistAdapterdt.notifyDataSetChanged();
                                bottomSheetDialog.show();
                            }
                        }
                ));



            }


        };

    }






    private void createbottomsheet_invoiceop(String invoiceidbypos, String ilnvoiceStatus, String pdflink, String sharelink) {
        String urlPDF = Constant.BASE_URL_PDF + pdflink;

        if (bottomSheetDialog != null) {
            View view = LayoutInflater.from(ThankYouNoteActivity.this).inflate(R.layout.bottominvoiceview, null);
            TextView viewinvoicebotom, viewinvoicetemplate, duplicateinvoitxt, shareinvoicetxt, recepitsviewtxt;
            Log.e("ilnvoiceStatus", ilnvoiceStatus);
            LinearLayout viewinvoicerecipts;
            viewinvoicerecipts = view.findViewById(R.id.viewinvoicerecipts);
            viewinvoicerecipts.setVisibility(View.GONE);
            recepitsviewtxt = view.findViewById(R.id.recepitsviewtxt);
//            if (ilnvoiceStatus.equals("2")) {
//                viewinvoicerecipts.setVisibility(View.VISIBLE);
//                recepitsviewtxt.setVisibility(View.VISIBLE);
//            }
//            if (ilnvoiceStatus.equals("1")) {
//                recepitsviewtxt.setVisibility(View.INVISIBLE);
//                viewinvoicerecipts.setVisibility(View.INVISIBLE);
//            }


            viewinvoicebotom = view.findViewById(R.id.viewinvoicebotom);
            viewinvoicetemplate = view.findViewById(R.id.viewinvoicetemplate);
            duplicateinvoitxt = view.findViewById(R.id.duplicateinvoitxt);
            shareinvoicetxt = view.findViewById(R.id.shareinvoicetxt);

            viewinvoicetemplate.setText("Send Thank You Note");
            duplicateinvoitxt.setText("View Thank You Note");
            shareinvoicetxt.setText("Cancel");
            shareinvoicetxt.setVisibility(View.GONE);

            viewinvoicebotom.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Bold.otf"));
            viewinvoicetemplate.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Bold.otf"));
            duplicateinvoitxt.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Bold.otf"));
            shareinvoicetxt.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Bold.otf"));
            recepitsviewtxt.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Bold.otf"));

//            LinearLayout linearLayoutChangeTemp= view.findViewById(R.id.viewicetemplate);
//            linearLayoutChangeTemp.setVisibility(View.GONE);


            viewinvoicebotom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Fragment_Create_Invoice.defaultClick = 1;
                  //  Log.e(TAG, "templateSelectooo "+templateSelect);


                    Intent intent = new Intent(ThankYouNoteActivity.this, ViewInvoiceActivity.class);
                    intent.putExtra("invoiceID", invoiceidbypos);
                    intent.putExtra("templatestr", templatestr);
//                    templateSelect = ""+2;
//                    colorCode = "#ff0000";
                    intent.putExtra("templateSelect", ""+templateSelect);
                    intent.putExtra("colorCode", ""+colorCode);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    bottomSheetDialog.dismiss();
                }
            });

//
//            viewinvoicetemplate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    if (selectedCompanyId.equals("")) {
//                        Constant.ErrorToast(getActivity(), "Select A Company");
//                        bottomSheetDialog.show();
//                    } else {
//
//                        ViewTamlatemethodh();
//                        bottomSheetDialog.show();
//                    }
//                }
//            });
//
            duplicateinvoitxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Fragment_Create_Invoice.defaultClick = 1;
//                    SavePref pref = new SavePref();
//                    pref.SavePref(getActivity());
//                    pref.setTemplate(0);

                    Intent intent = new Intent(ThankYouNoteActivity.this, ViewThankYouNoteActivity.class);
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
                    Intent intent = new Intent(ThankYouNoteActivity.this, SendThankYouNoteActivity.class);
                    intent.putExtra("invoiceID", invoiceidbypos);
                    intent.putExtra("templatestr", templatestr);
                    intent.putExtra("templateSelect", ""+templateSelect);
                    intent.putExtra("colorCode", ""+colorCode);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    bottomSheetDialog.dismiss();
                }
            });


            shareinvoicetxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });

//
//
//
//
//            shareinvoicetxt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
////                    Fragment_Create_Invoice.defaultClick = 1;
//                    final Dialog mybuilder = new Dialog(getContext());
//                    mybuilder.setContentView(R.layout.dialogsharefile);
//
//                    TextView txtSHAREvalue, txtcancelvalue, txtPDFfvalue, txtheadvalue;
//
//
//                    txtSHAREvalue = mybuilder.findViewById(R.id.txtpdfvalue);
//                    txtPDFfvalue = mybuilder.findViewById(R.id.txtsharefvalue);
//                    txtcancelvalue = mybuilder.findViewById(R.id.txtcancelvalue);
//                    txtheadvalue = mybuilder.findViewById(R.id.txtheadvalue);
//
//
//                    txtheadvalue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Bold.otf"));
//                    txtPDFfvalue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
//                    txtPDFfvalue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
//                    txtSHAREvalue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
//
//
//                    mybuilder.show();
//                    mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//                    Window window = mybuilder.getWindow();
//                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    window.setBackgroundDrawableResource(R.color.transparent);
//
//
//
//
//
//
//
//
//                    txtSHAREvalue.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            Log.e(TAG, "sharelink:: "+urlPDF);
//
////                            Log.e(TAG, "pdflink: "+pdflink);
//                            Log.e(TAG, "dataNo: "+dataNo);
//
//
//
//                            String subject = Utility.getRealValueInvoiceWithoutPlus(dataNo)+" from "+customerName;
//                            String txt = "Your Invoice can be viewed, printed and downloaded from below link." +
//                                    "\n\n" +urlPDF ;
//
//                            try {
//
//                                if (!urlPDF.endsWith(".pdf")) {
//                                    Toast.makeText(getActivity(), "No File Found", Toast.LENGTH_LONG).show();
//                                } else {
//                                    BaseurlForShareInvoice = shareInvoicelink + sharelink;
//                                    //String finalurl =BaseurlForShareInvoice;
//
////                                    String[] TO = {"email@server.com"};
////                                    Uri uri = Uri.parse("mailto:email@server.com")
////                                            .buildUpon()
////                                            .appendQueryParameter("subject", subject)
////                                            .appendQueryParameter("body", txt)
////                                            .build();
////                                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
////
////                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
////                                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//
//
//
//
//
////                                    String to = "";
////                                    //  String subject= "Hi I am subject";
////                                    //  String body="Hi I am test body";
////                                    String mailTo = "mailto:" + to +
////                                            "?&subject=" + Uri.encode(subject) +
////                                            "&body=" + Uri.encode(txt);
////
////                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
////                                    // intent.setType("text/plain");
////                                    String message="File to be shared is " + "file_name" + ".";
//////                                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
////                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
//////                                intent.putExtra(Intent.EXTRA_TEXT, message);
////                                    //intent.setData(Uri.parse("mailto:xyz@gmail.com"));
////                                    intent.setData(Uri.parse(mailTo));
////                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                                    startActivity(intent);
//
//                                    Intent share = new Intent(Intent.ACTION_SEND);
//                                    share.setType("image/jpeg");
//                                    share.putExtra(Intent.EXTRA_SUBJECT, subject);
//                                    share.putExtra(Intent.EXTRA_TEXT, txt);
//
//                                    share.putExtra(Intent.EXTRA_STREAM,
//                                            Uri.parse("file:///sdcard/share.jpg"));
//
//                                    if (Utility.isAppAvailable(getActivity(), "com.google.android.gm")){
//                                        share.setPackage("com.google.android.gm");
//                                    }
//                                    startActivity(share);
//                                }
//
//
//
//                            } catch (Exception e) {
//                                //e.toString();
//                            }
//                            bottomSheetDialog.dismiss();
//                            mybuilder.dismiss();
//                        }
//                    });
//
//
//                    txtPDFfvalue.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Log.e(TAG, "pdflink:: "+urlPDF);
//                            try {
//                                if (!urlPDF.endsWith(".pdf")) {
//                                    Toast.makeText(getActivity(), "No File Found", Toast.LENGTH_LONG).show();
//                                } else {
////                    /*              String pdfurl = "http://13.126.22.0/saad/app/uploads/invoice/pdf/" + pdflink;
////                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
////                                    shareIntent.setType("text/plain");
////                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "PDF File");
////                                    String shareMessage = pdfurl;
////                                    shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(shareMessage));
////                                    getActivity().startActivity(Intent.createChooser(shareIntent, "choose one"));
////*/                                  Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
////                                    shareIntent.setType("text/plain");
////                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
////                                    //String app_url = "file:///home/apptunix/Desktop/invoice.html";
////                                    //shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
////                                    startActivity(Intent.createChooser(shareIntent, "Share via"));
//
//
//                                    if (CheckForSDCard.isSDCardPresent()) {
//
//                                        //check if app has permission to write to the external storage.
//                                        if (checkPermission()) {
//                                            //Get the URL entered
//                                            String url = urlPDF;
//                                            String subject = Utility.getRealValueInvoiceWithoutPlus(dataNo)+" from "+customerName;
//                                            new List_of_Invoices.DownloadFile(getActivity(), subject).execute(url);
//                                        } else {
//
//                                        }
//
//
//                                    } else {
//                                        Toast.makeText(getActivity(),
//                                                "SD Card not found", Toast.LENGTH_LONG).show();
//
//                                    }
//
//                                }
//
//                            } catch (Exception e) {
//                                //e.toString();
//                            }
//                            bottomSheetDialog.dismiss();
//                            mybuilder.dismiss();
//                        }
//                    });
//
//
//
//                    txtcancelvalue.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            mybuilder.dismiss();
//                            bottomSheetDialog.dismiss();
//                        }
//                    });
//
//
//                }
//
//            });
            bottomSheetDialog = new BottomSheetDialog(ThankYouNoteActivity.this);
            bottomSheetDialog.setContentView(view);
//
//
//
//            recepitsviewtxt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Fragment_Create_Invoice.defaultClick = 1;
//                    Intent intent = new Intent(getContext(), ConvertToReceiptsActivity.class);
//                    intent.putExtra("invoiceID", invoiceidbypos);
//                    intent.putExtra("companyreceiptno", receipt_count);
//                    intent.putExtra("compnayestimate_count", estimate_count);
//
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    getActivity().startActivity(intent);
//                    bottomSheetDialog.dismiss();
//                }
//            });
        }
    }






}
