package com.sirapp.Customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sirapp.ImageResource.FileCompressor;
import com.sirapp.Settings.SubscribeActivity;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseActivity;
import com.sirapp.BuildConfig;
import com.sirapp.Constant.Constant;
import com.sirapp.R;
import com.sirapp.Utils.GlideApp;
import com.sirapp.Utils.Utility;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Customer_Detail_Activity2 extends BaseActivity {

    private static final String TAG = "Customer_Detail_Activity2";
    Customer_Detail_Activity2 activity;

    private static final int GALLARY_aCTION_PICK_CODE=10;
    private static final int CAMERA_ACTION_PICK_CODE=9;
    FileCompressor mCompressor;
    RoundedImageView uploadimage;
    TextView imagedescription,contacts,type,txtshiiping;
    EditText name,email,contactperson,phone,mobile,website,CompanyAddress,edfirstname,edlastname,edaddress1,edaddress2,edcity,edpostcode,edcountry;
    Button add;
    File fileimage;
    final int PICK_CONTACT=1;
    final int PERMISSION_REQUEST_CONTACT=10;

    String contactname,contactnumber;

    RadioButton individual,company;
    String customertype="company";

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    String selectedCompanyId="";

//    ArrayList<String> cnames=new ArrayList<>();
//    ArrayList<String> cids=new ArrayList<>();
    String filepath="";

    String customer_id,customer_name,customer_contact_person,customer_image,customer_email,customer_phone,customer_mobile,customer_website,customer_address;

    String shipping_firstname, shipping_lastname, shipping_address_1, shipping_address_2, shipping_city, shipping_postcode, shipping_country;


    TextView save,cancel;
    ImageView editbtn;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__detail_2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        activity = this;
        Constant.toolbar(activity, getString(R.string.header_details));

        FindByIds();
        setFonts();
        setListeners();


        mCompressor = new FileCompressor(activity);

        selectedCompanyId = getIntent().getStringExtra("company_id");
        customer_id = getIntent().getStringExtra("customer_id");
        customer_name = getIntent().getStringExtra("customer_name");
        customer_contact_person = getIntent().getStringExtra("customer_contact_person");
        customer_image = getIntent().getStringExtra("customer_image");
        customer_email = getIntent().getStringExtra("customer_email");
        customer_phone = getIntent().getStringExtra("customer_phone");
        customer_mobile = getIntent().getStringExtra("customer_mobile");
        customer_website = getIntent().getStringExtra("customer_website");
        customer_address = getIntent().getStringExtra("customer_address");

        shipping_firstname = getIntent().getStringExtra("shipping_firstname");
        shipping_lastname = getIntent().getStringExtra("shipping_lastname");
        shipping_address_1 = getIntent().getStringExtra("shipping_address_1");
        shipping_address_2 = getIntent().getStringExtra("shipping_address_2");
        shipping_city = getIntent().getStringExtra("shipping_city");
        shipping_postcode = getIntent().getStringExtra("shipping_postcode");
        shipping_country = getIntent().getStringExtra("shipping_country");



        Log.e(TAG, "selectedCompanyId "+selectedCompanyId);
        Log.e(TAG, "customer_id "+customer_id);
        Log.e(TAG, "customer_image "+customer_image);

//        //uploadimage = findViewById(R.id.uploadimage);
//        //imagedescription = findViewById(R.id.imagedescription);
//        contacts = findViewById(R.id.contacts);
//
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.circleCrop();
        options.placeholder(R.drawable.app_icon);
        GlideApp.with(activity)
                .load(customer_image)
                .apply(options)
                .into(uploadimage);

        name.setText(customer_name);
        email.setText(customer_email);
        contactperson.setText(customer_contact_person);
        phone.setText(customer_phone);
        mobile.setText(customer_mobile);
        website.setText(customer_website);
        CompanyAddress.setText(customer_address);
        imagedescription.setVisibility(View.GONE);


        edfirstname.setText(shipping_firstname);
        edlastname.setText(shipping_lastname);
        edaddress1.setText(shipping_address_1);
        edaddress2.setText(shipping_address_2);
        edcity.setText(shipping_city);
        edpostcode.setText(shipping_postcode);
        edcountry.setText(shipping_country);


    }



    private void FindByIds(){
        editbtn = findViewById(R.id.editbtn);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);

        uploadimage = findViewById(R.id.uploadimage);
        imagedescription = findViewById(R.id.imagedescription);
        contacts = findViewById(R.id.contacts);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contactperson = findViewById(R.id.contactperson);
        phone = findViewById(R.id.phone);
        mobile = findViewById(R.id.mobile);
        website = findViewById(R.id.website);
        CompanyAddress = findViewById(R.id.CompanyAddress);
        add = findViewById(R.id.add);
        individual = findViewById(R.id.individual);
        company = findViewById(R.id.company);
        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);
        type = findViewById(R.id.type);
        txtshiiping = findViewById(R.id.txtshiiping);
        edfirstname = findViewById(R.id.edfirstname);
        edlastname = findViewById(R.id.edlastname);
        edaddress1 = findViewById(R.id.edaddress1);
        edaddress2 = findViewById(R.id.edaddress2);
        edcity = findViewById(R.id.edcity);
        edpostcode = findViewById(R.id.edpostcode);
        edcountry = findViewById(R.id.edcountry);


        contacts.setEnabled(false);
        name.setEnabled(false);
        email.setEnabled(false);
        contactperson.setEnabled(false);
        phone.setEnabled(false);
        mobile.setEnabled(false);
        website.setEnabled(false);
        CompanyAddress.setEnabled(false);

        edfirstname.setEnabled(false);
        edlastname.setEnabled(false);
        edaddress1.setEnabled(false);
        edaddress2.setEnabled(false);
        edcity.setEnabled(false);
        edpostcode.setEnabled(false);
        edcountry.setEnabled(false);

    }

    private void setFonts() {

        type.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        individual.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        company.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        imagedescription.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        CompanyAddress.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        website.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        phone.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        email.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        name.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        edfirstname.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        edlastname.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        edaddress1.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        edaddress2.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        edcity.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        edpostcode.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        edcountry.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        contactperson.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        mobile.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        add.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/Ubuntu-Medium.ttf"));
        contacts.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/AzoSans-Regular.otf"));
        txtshiiping.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Fonts/AzoSans-Bold.otf"));
    }





    private void setListeners(){

        company.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //gardenValue="1";
                if (isChecked){
                    name.animate().setDuration(1500).alpha(1.0f);
                    name.setEnabled(true);
                    customertype="company";
                }

            }
        });

        individual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //gardenValue="2";
                // name.setVisibility(View.GONE);
                if (isChecked){
                    name.animate().setDuration(1500).alpha(0.0f);
                    name.setEnabled(false);
                    customertype="individual";

                }
            }
        });


        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForContactPermission();
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCustomer();
            }
        });


        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });




        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                editbtn.setVisibility(View.GONE);
                imagedescription.setVisibility(View.VISIBLE);

                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.circleCrop();
                options.placeholder(R.drawable.app_icon);
                GlideApp.with(activity)
                        .load(R.drawable.upload_company_logo)
                        //.apply(options)
                        .into(uploadimage);

               // uploadimage.setBackgroundResource(R.drawable.upload_company_logo);


                contacts.setEnabled(true);
                name.setEnabled(true);
                email.setEnabled(true);
                contactperson.setEnabled(true);
                phone.setEnabled(true);
                mobile.setEnabled(true);
                website.setEnabled(true);
                CompanyAddress.setEnabled(true);

                edfirstname.setEnabled(true);
                edlastname.setEnabled(true);
                edaddress1.setEnabled(true);
                edaddress2.setEnabled(true);
                edcity.setEnabled(true);
                edpostcode.setEnabled(true);
                edcountry.setEnabled(true);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                editbtn.setVisibility(View.VISIBLE);
                imagedescription.setVisibility(View.GONE);

                contacts.setEnabled(false);
                name.setEnabled(false);
                email.setEnabled(false);
                contactperson.setEnabled(false);
                phone.setEnabled(false);
                mobile.setEnabled(false);
                website.setEnabled(false);
                CompanyAddress.setEnabled(false);

                edfirstname.setEnabled(false);
                edlastname.setEnabled(false);
                edaddress1.setEnabled(false);
                edaddress2.setEnabled(false);
                edcity.setEnabled(false);
                edpostcode.setEnabled(false);
                edcountry.setEnabled(false);

                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.circleCrop();
                options.placeholder(R.drawable.app_icon);
                GlideApp.with(activity)
                        .load(customer_image)
                        .apply(options)
                        .into(uploadimage);

            }
        });

    }



//
//    private void SelectImage() {
//        final CharSequence[] items={"Camera","Gallery", "Cancel"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle("Add Image");
//
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (items[i].equals("Camera")) {
//
//                    // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    //  startActivityForResult(intent, REQUEST_CAMERA);
//                    requestStoragePermission(true);
//                } else if (items[i].equals("Gallery")) {
//
//                    requestStoragePermission(false);
//
////                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                    intent.setType("image/*");
////                    startActivityForResult(intent, SELECT_FILE);
//
//                } else if (items[i].equals("Cancel")) {
//                    dialogInterface.dismiss();
//                }
//            }
//        });
//        builder.show();
//
//    }



    private void SelectImage() {
        final CharSequence[] items={getString(R.string.dialog_Camera),getString(R.string.dialog_Gallery),getString(R.string.dialog_Cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(getString(R.string.dialog_AddImage));

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals(getString(R.string.dialog_Camera))) {

                    // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //  startActivityForResult(intent, REQUEST_CAMERA);
                    requestStoragePermission(true);
                } else if (items[i].equals(getString(R.string.dialog_Gallery))) {

                    requestStoragePermission(false);

//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals(getString(R.string.dialog_Cancel))) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }




    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(activity).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
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
                }).withErrorListener(error -> Toast.makeText(activity, "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);

    }

    private void gallaryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, GALLARY_aCTION_PICK_CODE);
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
                Utility.glideSet(Customer_Detail_Activity2.this , fileimage , uploadimage);
                //GlideApp.with(activity).load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(uploadimage);
            } else if (requestCode == GALLARY_aCTION_PICK_CODE) {
                Uri selectedImage = data.getData();
                try {
                    fileimage = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Utility.glideSet(Customer_Detail_Activity2.this , fileimage , uploadimage);
                //GlideApp.with(activity).load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(uploadimage);

            }else if(requestCode== PICK_CONTACT)
            {

                Uri contactData = data.getData();
                Cursor c =  managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst()) {


                    String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                null, null);
                        phones.moveToFirst();
                        contactnumber = phones.getString(phones.getColumnIndex("data1"));
                    }
                    contactname = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    name.setText(contactname);
                    contactperson.setText(contactname);
                    mobile.setText(contactnumber);
                    phone.setText(contactnumber);

                }
            }

        }




    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
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
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
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
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }






    @SuppressLint("LongLogTag")
    private void AddCustomer(){

        String companyname = name.getText().toString();
        String cemail = email.getText().toString();
        String cperson = contactperson.getText().toString();
        String cphone = phone.getText().toString();
        String cmobile= mobile.getText().toString();
        String cwebsite = website.getText().toString();
        String caddress = CompanyAddress.getText().toString();

        String f_name = edfirstname.getText().toString();
        String l_name = edlastname.getText().toString();
        String address1 = edaddress1.getText().toString();
        String address2 = edaddress2.getText().toString();
        String city = edcity.getText().toString();
        String postcode= edpostcode.getText().toString();
        String country = edcountry.getText().toString();

        if (customertype.equals("company")){
            if (companyname.isEmpty()){
                name.setError(getString(R.string.dialog_Required));
                name.requestFocus();
            }

            else if (!cemail.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(cemail).matches()){
                email.setError(getString(R.string.dialog_InvalidEmail));
                email.requestFocus();
            }
//            else if (selectedCompanyId.equals("")){
//                Constant.ErrorToast(activity,"Select Company");
//            }
            else{
                boolean isEnter = isEnter();

                Log.e(TAG, "isEnterAA "+isEnter);

                if(isEnter == false){
                    return;
                }

                Utility.hideKeypad(activity);
                avi.smoothToShow();
                avibackground.setVisibility(View.VISIBLE);

                RequestParams params = new RequestParams();

                params.add("customer_id",customer_id);
                params.add("customer_name",companyname);
                params.add("company_id",selectedCompanyId);
                params.add("contact_name",cperson);
                params.add("email",cemail);
                params.add("address",caddress);
                params.add("phone_number",cphone);
                params.add("mobile_number",cmobile);
                params.add("website",cwebsite);
                params.add("shipping_firstname",f_name);
                params.add("shipping_lastname",l_name);
                params.add("shipping_address_1",address1);
                params.add("shipping_address_2",address2);
                params.add("shipping_city",city);
                params.add("shipping_postcode",postcode);
                params.add("shipping_country",country);
                if (fileimage!=null){
                    try {
                        params.put("image",fileimage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                String token = Constant.GetSharedPreferences(activity,Constant.ACCESS_TOKEN);
                AsyncHttpClient client = new AsyncHttpClient();
                client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
                client.addHeader("Access-Token",token);
                params.add("language", ""+getLanguage());
                client.post(AllSirApi.BASE_URL + "customer/update", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        avi.smoothToHide();
                        avibackground.setVisibility(View.GONE);
                        String response = new String(responseBody);
                        Log.e("addcustomerResp",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("true")){
                                Constant.SuccessToast(activity,getString(R.string.dialog_CustomerUpdated));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(activity,Customer_Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                },1000);

                            }

                            if (status.equals("false")){
                                Constant.ErrorToast(activity,jsonObject.getString("message"));

                                if( jsonObject.has("code")){
                                    String code = jsonObject.getString("code");

                                    if(code.equalsIgnoreCase("subscription")){
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(Customer_Detail_Activity2.this, SubscribeActivity.class);
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
                            Log.e("addcustomerRespF",response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("false")){
                                    Constant.ErrorToast(activity,jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            //Constant.ErrorToast(activity,"Something went wrong, try again!");
                        }

                    }
                });
            }
        }
        else {

            if (cemail.isEmpty()){
                email.setError(getString(R.string.dialog_Required));
                email.requestFocus();
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(cemail).matches()){
                email.setError(getString(R.string.dialog_InvalidEmail));
                email.requestFocus();
            }
            else if (cperson.isEmpty()){
                contactperson.setError(getString(R.string.dialog_Required));
                contactperson.requestFocus();
            }
            else if (cphone.isEmpty()){
                phone.setError(getString(R.string.dialog_Required));
                phone.requestFocus();
            }
            else if (cmobile.isEmpty()){
                mobile.setError(getString(R.string.dialog_Required));
                mobile.requestFocus();
            }
            else if (cwebsite.isEmpty()){
                website.setError(getString(R.string.dialog_Required));
                website.requestFocus();
            }
            else if (caddress.isEmpty()){
                CompanyAddress.setError(getString(R.string.dialog_Required));
                CompanyAddress.requestFocus();
            }
//            else if (selectedCompanyId.equals("")){
//                Constant.ErrorToast(getActivity(),"Select Company");
//            }
            else{
                avi.smoothToShow();
                avibackground.setVisibility(View.VISIBLE);

                RequestParams params = new RequestParams();
                params.add("company_id",selectedCompanyId);
                params.add("contact_name",cperson);
                params.add("email",cemail);
                params.add("address",caddress);
                params.add("phone_number",cphone);
                params.add("mobile_number",cmobile);
                params.add("website",cwebsite);
                if (fileimage!=null){
                    try {
                        params.put("image",fileimage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                String token = Constant.GetSharedPreferences(activity,Constant.ACCESS_TOKEN);
                AsyncHttpClient client = new AsyncHttpClient();
                client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
                client.addHeader("Access-Token",token);
                params.add("language", ""+getLanguage());
                client.post(AllSirApi.BASE_URL + "customer/add", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        avi.smoothToHide();
                        avibackground.setVisibility(View.GONE);
                        String response = new String(responseBody);
                        Log.e("addcustomerResp",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("true")){
                                Constant.SuccessToast(activity, getString(R.string.dialog_CustomerAdded));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(activity,Customer_Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                },1000);

                            }

                            if (status.equals("false")){
                                if( jsonObject.has("code")){
                                    String code = jsonObject.getString("code");

                                    if(code.equalsIgnoreCase("subscription")){
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(Customer_Detail_Activity2.this, SubscribeActivity.class);
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
                            Log.e("addcustomerRespF",response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("false")){

                                    Constant.ErrorToast(activity,jsonObject.getString("message"));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            //Constant.ErrorToast(activity,"Something went wrong, try again!");
                        }

                    }
                });
            }



        }

    }






    @SuppressLint("LongLogTag")
    private boolean isEnter() {

        boolean isEntered = false;

        String f_name = edfirstname.getText().toString();
        String l_name = edlastname.getText().toString();
        String address1 = edaddress1.getText().toString();
        String address2 = edaddress2.getText().toString();
        String city = edcity.getText().toString();
        String postcode= edpostcode.getText().toString();
        String country = edcountry.getText().toString();


        if (f_name.equals("") && l_name.equals("") && address1.equals("") && address2.equals("") && city.equals("") && postcode.equals("") && country.equals("")) {
            isEntered = true;
            Log.e(TAG, "AAAAAAAAAAA");
        }else{
            if (f_name.equals("")){
                Constant.ErrorToast(Customer_Detail_Activity2.this,getString(R.string.dialog_PleaseEnterFirstName));
                isEntered = false;
            }else if(l_name.equals("")){
                Constant.ErrorToast(Customer_Detail_Activity2.this,getString(R.string.dialog_PleaseEnterLastName));
                isEntered = false;
            }else if(address1.equals("")){
                Constant.ErrorToast(Customer_Detail_Activity2.this,getString(R.string.dialog_ShippingAddress1));
                isEntered = false;
            }else if(address2.equals("")){
                Constant.ErrorToast(Customer_Detail_Activity2.this,getString(R.string.dialog_ShippingAddress2));
                isEntered = false;
            }else if(city.equals("")){
                Constant.ErrorToast(Customer_Detail_Activity2.this,getString(R.string.dialog_PleaseEnterCity));
                isEntered = false;
            }else if(postcode.equals("")){
                Constant.ErrorToast(Customer_Detail_Activity2.this,getString(R.string.dialog_SPleaseEnterPostcode));
                isEntered = false;
            }else if(country.equals("")){
                Constant.ErrorToast(Customer_Detail_Activity2.this,getString(R.string.dialog_PleaseEnterCountry));
                isEntered = false;
            }else{
                isEntered = true;
            }
        }
        return isEntered;
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }
                else {
                    Constant.ErrorToast(activity,"Permission Denied for contacts");
                }
                return;
            }
        }
    }

    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();


                } else {

                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                }
            }else{
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        }
        else{
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }



}
