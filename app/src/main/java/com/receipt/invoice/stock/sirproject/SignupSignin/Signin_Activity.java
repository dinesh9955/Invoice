package com.receipt.invoice.stock.sirproject.SignupSignin;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.karan.churi.PermissionManager.PermissionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.receipt.invoice.stock.sirproject.Abc;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.ForgotResetPassword.ForgotPassword_Activity;
import com.receipt.invoice.stock.sirproject.Home.Home_Activity;
import com.receipt.invoice.stock.sirproject.Invoice.CheckForSDCard;
import com.receipt.invoice.stock.sirproject.Invoice.List_of_Invoices;
import com.receipt.invoice.stock.sirproject.OnBoardings.OnBoarding_Activity;
import com.receipt.invoice.stock.sirproject.R;
import com.wang.avi.AVLoadingIndicatorView;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class Signin_Activity extends BaseActivity {


    private static final String TAG = "Signin_Activity";
    ImageView imgback;

    TextView txtsignin,txtsignindes,txtforgotpassword,txtaccount,txtsignuphere;
    EditText edemail,edpassword;
    Button btnlogin;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    PermissionManager permissionManager;
    DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

//
//        permissionManager = new PermissionManager() {
//        };
//        permissionManager.checkAndRequestPermissions(this);

        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);

        imgback = findViewById(R.id.imgback);
        txtsignin = findViewById(R.id.txtsignin);
        txtsignindes = findViewById(R.id.txtsignindes);
        txtforgotpassword = findViewById(R.id.txtforgotpassword);
        txtaccount = findViewById(R.id.txtaccount);
        txtsignuphere = findViewById(R.id.txtsignuphere);
        edemail = findViewById(R.id.edemail);
        edpassword = findViewById(R.id.edpassword);
        btnlogin = findViewById(R.id.btnlogin);


        txtsignuphere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signin_Activity.this,OnBoarding_Activity.class);
                intent.putExtra("signup","signup");
                startActivity(intent);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signin_Activity.this,Signup_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Signin();

//                if (CheckForSDCard.isSDCardPresent()) {
//
//                    //check if app has permission to write to the external storage.
//                    if (checkPermission()) {
//                        //Get the URL entered
//                        String url = "http://kskglobalsourcing.com/boost-crm/storage/files/shares/example.pdf";
//                        new DownloadFile(Signin_Activity.this).execute(url);
//                    } else {
//
//                    }
//
//
//                } else {
//                    Toast.makeText(Signin_Activity.this,
//                            "SD Card not found", Toast.LENGTH_LONG).show();
//
//                }
            }
        });

        txtforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signin_Activity.this, ForgotPassword_Activity.class);
                startActivity(intent);
            }
        });

        fonts();

    }
    public void fonts()
    {
        txtsignin.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Bold.otf"));
        txtsignindes.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtforgotpassword.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtaccount.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtsignuphere.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));
        edemail.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Regular.ttf"));
        edpassword.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/Ubuntu-Regular.ttf"));
        btnlogin.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Medium.otf"));

    }

    public void Signin()
    {
        final SharedPreferences pref = getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);
        if (TextUtils.isEmpty(edemail.getText())) {
            edemail.setError("Field is required");
            edemail.requestFocus();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(edemail.getText().toString()).matches())
        {
            edemail.setError("Invalid Pattern");
            edemail.requestFocus();
        }

        else if (TextUtils.isEmpty(edpassword.getText())) {
            edpassword.setError("Field is required");
            edpassword.requestFocus();
        }
        else if (!(edpassword.getText().toString().trim().length() >= 6)) {
            edpassword.setError("Password should be min 6 characters");
            edpassword.requestFocus();
        }else
            {

                avi.smoothToShow();
                avibackground.setVisibility(View.VISIBLE);


                String email = edemail.getText().toString();
                String password = edpassword.getText().toString();

                RequestParams params = new RequestParams();

                params.add("email",email);
                params.add("password",password);

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(Constant.BASE_URL+"user/login",params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        Log.e(TAG, "signinresponse# "+responseBody.length);

                        String response = new String(responseBody);

                        Log.e("signinresponse",response);

                        avi.smoothToHide();
                        avibackground.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            if (status.equals("true"))
                            {
                                JSONObject data = jsonObject.getJSONObject("data");
                                String access_token = data.getString("access_token");
                                JSONObject profile = data.getJSONObject("profile");
                                String fullname = profile.getString("fullname");
                                String email = profile.getString("email");
                                pref.edit().putString(Constant.ACCESS_TOKEN,access_token).commit();
                                pref.edit().putString(Constant.FULLNAME,fullname).commit();
                                pref.edit().putString(Constant.EMAIL,email).commit();
                                pref.edit().putBoolean(Constant.LOGGED_IN,true).commit();

                                JSONObject permissions = profile.getJSONObject("permission");
                                String invoice = permissions.getString("invoice");
                                String estimate = permissions.getString("estimate");
                                String stock = permissions.getString("stock");
                                String receipt = permissions.getString("receipt");
                                String purchase_order = permissions.getString("purchase_order");
                                String payment_voucher = permissions.getString("payment_voucher");
                                String tax = permissions.getString("tax");
                                String customer = permissions.getString("customer");
                                String supplier = permissions.getString("supplier");
                                String product = permissions.getString("product");
                                String service = permissions.getString("service");
                                String credit_note = permissions.getString("credit_note");
                                String sub_admin = permissions.getString("sub_admin");


                                Log.e("sub_admin",sub_admin);
                                Log.e("service",service);

                                pref.edit().putString(Constant.ACCESS_TOKEN,access_token).commit();
                                pref.edit().putString(Constant.FULLNAME,fullname).commit();
                                pref.edit().putString(Constant.EMAIL,email).commit();
                                pref.edit().putBoolean(Constant.LOGGED_IN,true).commit();
                                pref.edit().putString(Constant.SUB_ADMIN,sub_admin).commit();
                                pref.edit().putString(Constant.INVOICE,invoice).commit();
                                pref.edit().putString(Constant.ESTIMATE,estimate).commit();
                                pref.edit().putString(Constant.STOCK,stock).commit();
                                pref.edit().putString(Constant.RECEIPT,receipt).commit();
                                pref.edit().putString(Constant.PURCHASE_ORDER,purchase_order).commit();
                                pref.edit().putString(Constant.PAYMENT_VOUCHER,payment_voucher).commit();
                                pref.edit().putString(Constant.TAX,tax).commit();
                                pref.edit().putString(Constant.CUSTOMER,customer).commit();
                                pref.edit().putString(Constant.SUPPLIER,supplier).commit();
                                pref.edit().putString(Constant.PRODUCT,product).commit();
                                pref.edit().putString(Constant.SERVICE,service).commit();
                                pref.edit().putString(Constant.CREDIT_NOTE,credit_note).commit();

                                Map<String, Object> eventValue = new HashMap<String, Object>();
                                eventValue.put(AFInAppEventParameterName.PARAM_1, "signin_click");
                                AppsFlyerLib.getInstance().trackEvent(Signin_Activity.this, "signin_click", eventValue);

                                Bundle params2 = new Bundle();
                                params2.putString("event_name", "signin_click");
                                firebaseAnalytics.logEvent("signin_click", params2);

                                Constant.SuccessToast(Signin_Activity.this,jsonObject.getString("message"));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(Signin_Activity.this, Home_Activity.class);
                                        intent.putExtra("login","login");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                },1500);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(responseBody!=null){
                            String response = new String(responseBody);
                            Log.e("signinresponseF",response);


                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString("status");
                                if (status.equals("false"))
                                {
                                    //JSONObject message = jsonObject.getJSONObject("message");
                                    String message = jsonObject.getString("message");
                                    Log.e("signinresponseF",response);

                                    //Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                    Constant.ErrorToast(Signin_Activity.this, message);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            Constant.ErrorToast(Signin_Activity.this, "Something went wrong!");
                        }

                        avi.smoothToHide();
                        avibackground.setVisibility(View.GONE);

                    }
                });


        }
    }

    Boolean twice=false;
    @Override
    public void onBackPressed() {
        if (twice==true){
            Intent intent=new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        twice=true;
        Toast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        },3000);

    }




    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Signin_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        permissionManager.checkResult(requestCode, permissions, grantResults);
//    }

    private static class DownloadFile extends AsyncTask<String, String, String> {

        private static final String TAG = "DownloadFile";
        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;
        Context context;

        DownloadFile(Context c) {
            context = c;
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
                folder = Environment.getExternalStorageDirectory() + File.separator + "abir_download/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

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
                return "Downloaded at: " + folder + fileName;

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

            // Display File path after downloading
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();


//                                    Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
//                                    shareIntent.setType("text/plain");
//                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
//                                    //String app_url = "file:///home/apptunix/Desktop/invoice.html";
//                                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
//                                    context.startActivity(Intent.createChooser(shareIntent, "Share via"));

        }
    }



}
