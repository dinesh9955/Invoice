package com.receipt.invoice.stock.sirproject.User;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;

import com.receipt.invoice.stock.sirproject.Abc;
import com.receipt.invoice.stock.sirproject.Base.BaseFragment;
import com.receipt.invoice.stock.sirproject.BuildConfig;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.ImageResource.FileCompressor;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.API.SavePref;
import com.wang.avi.AVLoadingIndicatorView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class Add_User extends BaseFragment {


    public Add_User() {
        // Required empty public constructor
    }


    Context applicationContext = User_Activity.getContextOfApplication();
    private static final int GALLARY_aCTION_PICK_CODE=10;
    private static final int CAMERA_ACTION_PICK_CODE=9;
    FileCompressor mCompressor;

    EditText username,useremail,password;
    AwesomeSpinner userrole;
    TextView accessrights,txtterms,txtdescription,imagedescription;
    CheckBox chkinvoices,chkestimates,chkreceipts,chkpurchase,chkpayment,chkstock,chktax,chksubadmin,chkproducts,chkcustomers,chkcredit,chkservices,chksuppliers,chkdebit;
    Button adduser;
    RoundedImageView uploadimage;
    File fileimage;
    final int PICK_CONTACT=1;
    final int PERMISSION_REQUEST_CONTACT=10;

    ArrayList<String> cnames=new ArrayList<>();
    ArrayList<String> cids=new ArrayList<>();

    String selectedCompanyId="";

    String invoice="0",payments="0",estimates="0",stock="0",receipts="0",taxes="0",purchase="0",sadmin="0",products="0",services="0",customers="0",suppliers="0",credit="0",debit="0";

    String filepath="";

    private AVLoadingIndicatorView avi;
    ImageView avibackground;
    SavePref savePref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = inflater.inflate(R.layout.fragment_add__user, container, false);

        savePref=new SavePref(getContext());
        username = view.findViewById(R.id.username);
        useremail = view.findViewById(R.id.useremail);
        userrole = view.findViewById(R.id.userrole);
        accessrights = view.findViewById(R.id.accessrights);
        imagedescription = view.findViewById(R.id.imagedescription);
        txtterms = view.findViewById(R.id.txtterms);
        txtdescription = view.findViewById(R.id.txtdescription);
        chkinvoices = view.findViewById(R.id.chkinvoices);
        chkestimates = view.findViewById(R.id.chkestimates);
        chkreceipts = view.findViewById(R.id.chkreceipts);
        chkpurchase = view.findViewById(R.id.chkpurchase);
        chkpayment = view.findViewById(R.id.chkpayments);
        chkstock = view.findViewById(R.id.chkstock);
        chktax = view.findViewById(R.id.chktaxes);
        chksubadmin = view.findViewById(R.id.chksubadmin);
        adduser = view.findViewById(R.id.adduser);
        uploadimage = view.findViewById(R.id.uploadimage);
        chkproducts = view.findViewById(R.id.chkproducts);
        chkcustomers = view.findViewById(R.id.chkcustomers);
        chkcredit = view.findViewById(R.id.chkcredit);
        chkservices = view.findViewById(R.id.chkservices);
        chksuppliers = view.findViewById(R.id.chksuppliers);
        chkdebit = view.findViewById(R.id.chkdebit);
        password = view.findViewById(R.id.password);
        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        setFonts();
        mCompressor = new FileCompressor(getActivity());

       uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            SelectImage();
            }
        });


       chkinvoices.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if (chkinvoices.isChecked())
               {
                   invoice ="1";
                   Log.e("loginvoice",invoice);
               }
               else if (!chkinvoices.isChecked())
               {
                   invoice = "0";
                   Log.e("loginvoice",invoice);
               }

           }
       });

        chkpayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkpayment.isChecked())
                {
                    payments ="1";

                }
                else if (!chkpayment.isChecked())
                {
                    payments = "0";

                }

            }
        });
        chkestimates.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkestimates.isChecked())
                {
                    estimates ="1";

                }
                else if (!chkestimates.isChecked())
                {
                    estimates = "0";

                }

            }
        });
        chkstock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkstock.isChecked())
                {
                    stock ="1";

                }
                else if (!chkstock.isChecked())
                {
                    stock = "0";

                }

            }
        });

        chkreceipts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkreceipts.isChecked())
                {
                    receipts ="1";

                }
                else if (!chkreceipts.isChecked())
                {
                    receipts = "0";

                }

            }
        });

        chktax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chktax.isChecked())
                {
                    taxes ="1";

                }
                else if (!chktax.isChecked())
                {
                    taxes = "0";

                }

            }
        });


        chkpurchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkpurchase.isChecked())
                {
                    purchase ="1";

                }
                else if (!chkpurchase.isChecked())
                {
                    purchase = "0";

                }

            }
        });

        chksubadmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chksubadmin.isChecked())
                {
                    sadmin ="1";

                }
                else if (!chksubadmin.isChecked())
                {
                    sadmin = "0";

                }

            }
        });

        chkproducts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkproducts.isChecked())
                {
                    products ="1";

                }
                else if (!chkproducts.isChecked())
                {
                    products = "0";

                }

            }
        });

        chkservices.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkservices.isChecked())
                {
                    services ="1";

                }
                else if (!chkservices.isChecked())
                {
                    services = "0";

                }

            }
        });

        chkcustomers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkcustomers.isChecked())
                {
                    customers ="1";

                }
                else if (!chkcustomers.isChecked())
                {
                    customers = "0";

                }

            }
        });

        chksuppliers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chksuppliers.isChecked())
                {
                    suppliers ="1";

                }
                else if (!chksuppliers.isChecked())
                {
                    suppliers = "0";

                }

            }
        });

        chkcredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkcredit.isChecked())
                {
                    credit ="1";

                }
                else if (!chkcredit.isChecked())
                {
                    credit = "0";

                }

            }
        });

        chkdebit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkdebit.isChecked())
                {
                    debit ="1";


                }
                else if (!chkdebit.isChecked())
                {
                    debit = "0";

                }

            }
        });



        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              add_user();
            }
        });

        companyget();

        userrole.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                Log.e("selectedCompany",selectedCompanyId);

            }
        });



        return view;
    }

    private void SelectImage() {
            final CharSequence[] items={"Camera","Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Image");

            builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (items[i].equals("Camera")) {
                       requestStoragePermission(true);
                    } else if (items[i].equals("Gallery")) {

                        requestStoragePermission(false);
                   } else if (items[i].equals("Cancel")) {
                        dialogInterface.dismiss();
                    }
                }
            });
            builder.show();

        }

        private void requestStoragePermission(boolean isCamera) {
            Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                if (isCamera) {
                                    dispatchTakePictureIntent();
                                } else {
                                    gallaryIntent();
                                }
                            }
                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).withErrorListener(error -> Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show())
                    .onSameThread()
                    .check();
        }

        private void showSettingsDialog() {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Need Permissions");
            builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
            builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
                dialog.cancel();
                openSettings();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();

        }

        private void openSettings() {

            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);

        }

        private void gallaryIntent() {

            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getActivity().startActivityForResult(pickPhoto, GALLARY_aCTION_PICK_CODE);
        }

        @Override
        public  void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == Activity.RESULT_OK) {

                if (requestCode == CAMERA_ACTION_PICK_CODE) {
                    try{
                        fileimage= mCompressor.compressToFile(fileimage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(getActivity()).load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(uploadimage);
                } else if (requestCode == GALLARY_aCTION_PICK_CODE) {
                    Uri selectedImage = data.getData();
                    try {
                        fileimage = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Glide.with(getActivity()).load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(uploadimage);

                }

            }


        }

        private void dispatchTakePictureIntent() {
               Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);

                    fileimage = photoFile;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERA_ACTION_PICK_CODE);

                }
            }

        }
        public String getRealPathFromUri(Uri contentUri) {
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String mFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
            return mFile;
        }

    private void setFonts() {
        imagedescription.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Light.ttf"));
        username.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        useremail.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        chkinvoices.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        chkestimates.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        chkreceipts.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        chkpurchase.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        chkpayment.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        chkstock.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        chktax.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        chksubadmin.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        adduser.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Medium.ttf"));
        accessrights.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Bold.otf"));
    }

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

                                savePref.setCompanyId(company_id);

                                cnames.add(company_name);
                                cids.add(company_id);

                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,cnames);
                                userrole.setAdapter(namesadapter);

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



    public void add_user() {
         if (TextUtils.isEmpty(username.getText())) {
            username.setError("Field is required");
            username.requestFocus();
        }
        else if (TextUtils.isEmpty(useremail.getText())) {
            useremail.setError("Field is required");
            useremail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(useremail.getText().toString()).matches()) {
            useremail.setError("Invalid Pattern");
            useremail.requestFocus();
        } else if (TextUtils.isEmpty(password.getText())) {
            password.setError("Field is required");
            password.requestFocus();
        }else if (!(password.getText().toString().trim().length() >= 6)) {
             password.setError("Password should be min 6 characters");
             password.requestFocus();
         } else {


            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);
            String email = useremail.getText().toString();
            String name = username.getText().toString();
            String pass = password.getText().toString();


            RequestParams params = new RequestParams();
            params.add("email", email);
            params.add("password", pass);
            params.add("fullname", name);
            params.add("company_id", selectedCompanyId);
            params.add("permission[invoice]", invoice);
            params.add("permission[estimate]", estimates);
            params.add("permission[stock]", stock);
            params.add("permission[receipt]", receipts);
            params.add("permission[purchase_order]", purchase);
            params.add("permission[payment_voucher]", payments);
            params.add("permission[tax]", taxes);
            params.add("permission[sub_admin]", sadmin);
            params.add("permission[product]", products);
            params.add("permission[service]", services);
            params.add("permission[customer]", customers);
            params.add("permission[supplier]", suppliers);
            params.add("permission[debit_note]", debit);
            params.add("permission[credit_note]", credit);
             if (fileimage!=null){
                 try {
                     params.put("image",fileimage);
                 }
                 catch (FileNotFoundException e) {
                     e.printStackTrace();
                 }
             }

             Log.e("logparamsuser", String.valueOf(params));

             String token = Constant.GetSharedPreferences(getContext(),Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
             client.addHeader("Access-Token",token);
            client.post(Constant.BASE_URL + "user/addSubUser", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    String response = new String(responseBody);
                    Log.e("responseuser", response);
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");

                        if (status.equals("true")) {
                            Map<String, Object> eventValue = new HashMap<String, Object>();
                            eventValue.put(AFInAppEventParameterName.PARAM_1, "user_addnew");
                            AppsFlyerLib.getInstance().trackEvent(getActivity(), "user_addnew", eventValue);

                            Bundle params2 = new Bundle();
                            params2.putString("event_name", "Add new User");
                            firebaseAnalytics.logEvent("user_addnew", params2);

                            Constant.SuccessToast(getActivity(), "User Added Successfully");
                            Intent intent = new Intent(getContext(), User_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        if (status.equals("false")) {
                            String message = jsonObject.getString("message");
                            Constant.ErrorToast(getActivity(), message);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (responseBody != null) {
                        String response = new String(responseBody);
                        Log.e("responseuserF", response);
                        avi.smoothToHide();
                        avibackground.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("false")) {
                                Constant.ErrorToast(getActivity(), message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });


        }
    }


}
