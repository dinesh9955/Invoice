package com.sirapp.Details;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
/*
import android.support.v4.app.Fragment;
*/
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.mikhaellopez.circleview.CircleView;
import com.sirapp.Constant.Constant;
import com.sirapp.ImageResource.FileCompressor;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseFragment;
import com.sirapp.BuildConfig;
import com.sirapp.Company.Companies_Activity;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Company_Details_Fragment extends BaseFragment {


    private static final String TAG = "Company_Details_Fragment";
    String colorCode = "#ffffff";

    public Company_Details_Fragment() {
        // Required empty public constructor
    }

    EditText name,email,phone,website,address,description,Paymentbanknameid,txtPaypalemailid,txtPaymentswiftbicid,chequepayabletoid,Ibnnumbertxtid;
    TextView nametxt,emailtxt,phonetxt,webtxt,addresstxt,txtchequepayableto,txtIbnnumber,txtPaymentswiftbic,txtPaypalemail,txtPaymentbankname ;
    Button itemstxtColor;
    ScrollView sv;
    RoundedImageView image;
    private AVLoadingIndicatorView avi;
    ImageView avibackground,changepic;
    TextView save,cancel, editbtn;
    File fileimage;

    String StrIbnnumber,Strchequepayableto,StrPaymentswiftbic,StrPaymentbankname,StrPaypalemail,company_name,company_logo,company_email,company_phone,comapny_website,comapny_address,company_id,currencyid;

//select image
   private static final int GALLARY_aCTION_PICK_CODE=10;
    private static final int CAMERA_ACTION_PICK_CODE=9;
    Context applicationContext = Company_Details_Activity.getContextOfApplication();
    //FileCompressor mCompressor;
    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final SharedPreferences pref =getActivity().getSharedPreferences(Constant.PREF_BASE, Context.MODE_PRIVATE);
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_company__details_, container, false);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        avi = v.findViewById(R.id.avi);
        avibackground = v.findViewById(R.id.avibackground);

        FindByIds(v);
        setFonts();
        setListeners();

        company_name = pref.getString(Constant.COMPANY_NAME,"");
        company_logo = pref.getString(Constant.COMPANY_LOGO,"");
        company_email = pref.getString(Constant.COMPANY_EMAIL,"");
        company_phone = pref.getString(Constant.COMPANY_PHONE,"");
        comapny_website = pref.getString(Constant.COMPANY_WEB,"");
        comapny_address = pref.getString(Constant.COMPANY_ADDRESS,"");
        company_id = pref.getString(Constant.COMPANY_ID,"");
        currencyid = pref.getString(Constant.CURRENCY_ID,"");
        colorCode = pref.getString("color","#ffffff");


        StrPaypalemail = pref.getString(Constant.Paypal_email,"");
        StrPaymentbankname = pref.getString(Constant.Payment_bank_name,"");
        StrPaymentswiftbic = pref.getString(Constant.Payment_swift_bic,"");
        Strchequepayableto = pref.getString(Constant.Cheque_payable_to,"");
        StrIbnnumber = pref.getString(Constant.Ibn_number,"");

        Log.e(TAG, "StrPaymentbanknameBB "+StrPaymentbankname);
        Log.e(TAG, "StrPaypalemailBB "+StrPaypalemail);

        Paymentbanknameid.setText(StrPaymentbankname);
        txtPaypalemailid.setText(StrPaypalemail);
        txtPaymentswiftbicid.setText(StrPaymentswiftbic);
        chequepayabletoid.setText(Strchequepayableto);
        Ibnnumbertxtid.setText(StrIbnnumber);
        name.setText(company_name);
        email.setText(company_email);
        phone.setText(company_phone);
        website.setText(comapny_website);
        address.setText(comapny_address);
        itemstxtColor.setText(colorCode);
//        itemstxtColor.setClickable(false);
        itemstxtColor.setEnabled(false);

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.app_icon);
        GlideApp.with(getActivity())
                .load(company_logo)
                .apply(options)
                .into(image);

        //mCompressor = new FileCompressor(getActivity());

        return v;
    }

    private void FindByIds(View v){
        itemstxtColor = v.findViewById(R.id.itemstxtColor);

        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        phone = v.findViewById(R.id.phone);
        website = v.findViewById(R.id.website);
        address = v.findViewById(R.id.invoicepricetxt);
        description = v.findViewById(R.id.description);
        sv = v.findViewById(R.id.sv);
        image = v.findViewById(R.id.image);
        changepic = v.findViewById(R.id.changepic);
        nametxt = v.findViewById(R.id.nametxt);
        emailtxt = v.findViewById(R.id.emailtxt);
        phonetxt = v.findViewById(R.id.phonetxt);
        webtxt = v.findViewById(R.id.webtxt);
        addresstxt = v.findViewById(R.id.addresstxt);
        editbtn = v.findViewById(R.id.editbtn);
        save = v.findViewById(R.id.save);
        cancel = v.findViewById(R.id.cancel);

        Paymentbanknameid = v.findViewById(R.id.Paymentbanknameid);
        txtPaypalemailid = v.findViewById(R.id.txtPaypalemailid);
        txtPaymentswiftbicid = v.findViewById(R.id.txtPaymentswiftbicid);
        chequepayabletoid = v.findViewById(R.id.chequepayabletoid);
        Ibnnumbertxtid = v.findViewById(R.id.Ibnnumbertxtid);


        txtchequepayableto = v.findViewById(R.id.txtchequepayableto);
        txtIbnnumber = v.findViewById(R.id.txtIbnnumber);
        txtPaymentswiftbic = v.findViewById(R.id.txtPaymentswiftbic);
        txtPaypalemail = v.findViewById(R.id.txtPaypalemail);
        txtPaymentbankname = v.findViewById(R.id.txtPaymentbankname);



    }

    private void setFonts(){

        name.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
        phone.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Regular.otf"));
        email.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Regular.otf"));
        website.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Regular.otf"));
        address.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Regular.otf"));
        description.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Regular.otf"));
        nametxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Medium.ttf"));
        emailtxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Medium.ttf"));
        phonetxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Medium.ttf"));
        webtxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Medium.ttf"));
        addresstxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Medium.ttf"));
        cancel.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Regular.otf"));
        save.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Regular.otf"));




        Paymentbanknameid.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Regular.otf"));
        txtPaypalemailid.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Regular.otf"));
        txtPaymentswiftbicid.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Regular.otf"));
        chequepayabletoid.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Regular.otf"));
        Ibnnumbertxtid.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/AzoSans-Regular.otf"));

        txtchequepayableto.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Medium.ttf"));
        txtIbnnumber.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Medium.ttf"));
        txtPaymentswiftbic.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Medium.ttf"));
        txtPaypalemail.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Medium.ttf"));
        txtPaymentbankname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Medium.ttf"));


    }

    private void setListeners(){

        itemstxtColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createShortDialog();
//                ColorPicker colorPicker = new ColorPicker(getActivity());
//                colorPicker.show();
//                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void onChooseColor(int position,int color) {
//                        // put code
//                        // Log.e(TAG, "color:"+color);
//
//                        if(color != 0){
//                            String strColor = String.format("#%06X", 0xFFFFFF & color);
//                            colorCode = strColor;
//                            itemstxtColor.setText(""+colorCode);
//                        }
//
//                        Log.e(TAG, "strColor:"+colorCode);
//
//                    }
//
//                    @Override
//                    public void onCancel(){
//                        // put code
//                    }
//                });
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditCompany();
            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                changepic.setVisibility(View.VISIBLE);

                name.setEnabled(true);
                email.setEnabled(true);
                phone.setEnabled(true);
                website.setEnabled(true);
                address.setEnabled(true);
                image.setEnabled(true);

                Paymentbanknameid.setEnabled(true);
                txtPaypalemailid.setEnabled(true);
                txtPaymentswiftbicid.setEnabled(true);
                chequepayabletoid.setEnabled(true);
                Ibnnumbertxtid.setEnabled(true);
                editbtn.setVisibility(View.GONE);
                itemstxtColor.setEnabled(true);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                changepic.setVisibility(View.GONE);
                name.setEnabled(false);
                email.setEnabled(false);
                phone.setEnabled(false);
                website.setEnabled(false);
                address.setEnabled(false);
                image.setEnabled(false);
                Paymentbanknameid.setEnabled(false);
                txtPaypalemailid.setEnabled(false);
                txtPaymentswiftbicid.setEnabled(false);
                chequepayabletoid.setEnabled(false);
                Ibnnumbertxtid.setEnabled(false);
                editbtn.setVisibility(View.VISIBLE);
                itemstxtColor.setEnabled(false);

                //if cancel then previous data restore
                name.setText(company_name);
                email.setText(company_email);
                phone.setText(company_phone);
                website.setText(comapny_website);
                address.setText(comapny_address);



                Paymentbanknameid.setText(StrPaymentbankname);
                txtPaypalemailid.setText(StrPaypalemail);
                txtPaymentswiftbicid.setText(StrPaymentswiftbic);
                chequepayabletoid.setText(Strchequepayableto);
                Ibnnumbertxtid.setText(StrIbnnumber);


                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.placeholder(R.drawable.app_icon);
                GlideApp.with(getActivity())
                        .load(company_logo)
                        .apply(options)
                        .into(image);

            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectImage();
            }
        });
    }

    private void SelectImage() {
        Dialog bottomSheetDialog4 = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_image, null);
        bottomSheetDialog4.setContentView(view);
        bottomSheetDialog4.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = bottomSheetDialog4.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog4.show();

        TextView textViewCamera = bottomSheetDialog4.findViewById(R.id.camera);
        TextView textViewGallery = bottomSheetDialog4.findViewById(R.id.gallery);
        TextView textViewCancel = bottomSheetDialog4.findViewById(R.id.cancel);

        textViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog4.dismiss();
                requestStoragePermission(true);
            }
        });

        textViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog4.dismiss();
                requestStoragePermission(false);
            }
        });

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog4.dismiss();
            }
        });


//        final CharSequence[] items={getString(R.string.dialog_Camera), getString(R.string.dialog_Gallery), getString(R.string.dialog_Cancel)};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(getString(R.string.dialog_AddImage));
//
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (items[i].equals(getString(R.string.dialog_Camera))) {
//
//                    // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    //  startActivityForResult(intent, REQUEST_CAMERA);
//                    requestStoragePermission(true);
//                } else if (items[i].equals(getString(R.string.dialog_Gallery))) {
//
//                    requestStoragePermission(false);
//
////                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                    intent.setType("image/*");
////                    startActivityForResult(intent, SELECT_FILE);
//
//                } else if (items[i].equals(getString(R.string.dialog_Cancel))) {
//                    dialogInterface.dismiss();
//                }
//            }
//        });
//        builder.show();

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

       /* Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent,GALLARY_aCTION_PICK_CODE );
*/

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getActivity().startActivityForResult(pickPhoto, GALLARY_aCTION_PICK_CODE);
    }

    @SuppressLint("LongLogTag")
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {


            /* if (requestCode == GALLARY_aCTION_PICK_CODE) {

                Uri selectedImageUri = data.getData();
                uploadimage.setImageURI(selectedImageUri);

            } else if (requestCode == CAMERA_ACTION_PICK_CODE) {
                dispatchTakePictureIntent();

                //Uri selectedImageUri = data.getData();
                //uploadimage.setImageURI(selectedImageUri);
            }*/

            if (requestCode == CAMERA_ACTION_PICK_CODE) {
                try{
                    fileimage= fileimage;
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                GlideApp.with(getActivity()).load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(image);

                Utility.glideSet(getActivity() , fileimage , image);

            } else if (requestCode == GALLARY_aCTION_PICK_CODE) {

              /*  Uri selectedImageUri = data.getData();
                uploadimage.setImageURI(selectedImageUri);
*/

                Uri selectedImage = data.getData();
                try {
                    fileimage = new File(getRealPathFromUri(selectedImage));

                    //fileimage = Utility.getJPEGtoPNG(fileimage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Utility.glideSet(getActivity() , fileimage , image);

                Log.e(TAG, "fileimageAAA "+fileimage);
//                GlideApp.with(getActivity())
//                        .load(fileimage)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon))
//                        .placeholder(R.drawable.app_icon)
//                        .into(image);

//                GlideApp.with(getActivity())
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(image);

            }

        }


    }

    private void dispatchTakePictureIntent() {
      /*  Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go

            ContentValues contentvale=new ContentValues();
            contentvale.put(MediaStore.Images.Media.TITLE,"Temp title:");
            contentvale.put(MediaStore.Images.Media.TITLE,"Temp descrc:");
            imageurluri=getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentvale);
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, CAMERA_ACTION_PICK_CODE);


        }*/
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





    @SuppressLint("LongLogTag")
    private void EditCompany(){

        String edname = name.getText().toString();
        String edemail = email.getText().toString();
        String edphone = phone.getText().toString();
        String edwebsite = website.getText().toString();
        String edCompanyAddress = address.getText().toString();




        String endPaymentbanknameid = Paymentbanknameid.getText().toString();
        String endtxtPaypalemailid = txtPaypalemailid.getText().toString();
        String endtxtPaymentswiftbicid = txtPaymentswiftbicid.getText().toString();
        String endchequepayabletoid = chequepayabletoid.getText().toString();
        String endIbnnumbertxtid = Ibnnumbertxtid.getText().toString();


        if (TextUtils.isEmpty(name.getText())) {
            name.setError(getString(R.string.dialog_Fieldisrequired));
            name.requestFocus();
        } else if (currencyid.equals(""))
        {
            Constant.ErrorToast(getActivity(),getString(R.string.dialog_Please_select_currency));
        }else{

            boolean isEnter = isEnter();

            Log.e(TAG, "isEnterAA "+isEnter);

            if(isEnter == false){
                return;
            }


            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);

            RequestParams params = new RequestParams();
            params.add("company_id",company_id);
            params.add("name",edname);
            params.add("currency_id",currencyid);
            params.add("phone_number",edphone);
            params.add("email",edemail);
            params.add("website",edwebsite);
            params.add("address",edCompanyAddress);
            params.add("color",colorCode);

            Log.e(TAG, "endPaymentbanknameid "+endPaymentbanknameid);
            Log.e(TAG, "endtxtPaypalemailid "+endtxtPaypalemailid);

            params.add("payment_iban",endIbnnumbertxtid);
            params.add("payment_bank_name",endPaymentbanknameid);
            params.add("paypal_email",endtxtPaypalemailid);
            params.add("payment_swift_bic",endtxtPaymentswiftbicid);
            params.add("cheque_payable_to",endchequepayabletoid);




            if (fileimage!=null){
                try {
                    params.put("image",fileimage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            Log.e(TAG, "paramsBB "+params.toString());



            String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.addHeader("Access-Token",token);
            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL + "company/update", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    String response = new String(responseBody);
                    Log.e("responsecompanyedit",response);
                    Utility.hideKeypad(getActivity());

                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("true"))
                        {
                            Constant.SuccessToast(getActivity(),getString(R.string.dialog_Companyupdated));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getActivity(),Companies_Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                }
                            },1000);
                        }

                        if (status.equals("false"))
                        {
                            Constant.ErrorToast(getActivity(),jsonObject.getString("message"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Utility.hideKeypad(getActivity());

                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    if(responseBody!=null){
                        String response = new String(responseBody);
                        Log.e("responsecompanyeditF",response);

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
                       // Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
                    }


                }
            });

        }






     }




    @SuppressLint("LongLogTag")
    private boolean isEnter() {

        boolean isEntered = false;


        String iban = Ibnnumbertxtid.getText().toString();
        String bank = Paymentbanknameid.getText().toString();
        String swift = txtPaymentswiftbicid.getText().toString();

        if (iban.equals("") && bank.equals("") && swift.equals("")) {
            isEntered = true;
            Log.e(TAG, "AAAAAAAAAAA");
        }else{
            if (iban.equals("")){
                Constant.ErrorToast(getActivity(),getString(R.string.dialog_PleaseEnterIBAN));
                isEntered = false;
            }else if(bank.equals("")){
                Constant.ErrorToast(getActivity(),getString(R.string.dialog_PleaseEnterBankName));
                isEntered = false;
            }else if(swift.equals("")){
                Constant.ErrorToast(getActivity(),getString(R.string.dialog_PleaseEnterSwiftBIC));
                isEntered = false;
            }else{
                isEntered = true;
            }
        }
        return isEntered;
    }





    public void createShortDialog() {
        // TODO Auto-generated method stub
//				final Builder dialog = new Builder(splash, AlertDialog.THEME_HOLO_LIGHT);
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_choose_color);

        RecyclerView mRecyclerView = (RecyclerView) dialog.findViewById(R.id.color_picker);
        //                mRecyclerView.setHasFixedSize(true);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("#fcd578");
        arrayList.add("#999999");
        arrayList.add("#599d29");
        arrayList.add("#cdcdcd");
        arrayList.add("#d086f2");

        arrayList.add("#737ef9");
        arrayList.add("#d4fb7c");
        arrayList.add("#8d8d8d");
        arrayList.add("#fd42ff");
        arrayList.add("#f2ec08");

        arrayList.add("#fe319a");
        arrayList.add("#f5827a");
        arrayList.add("#919000");
        arrayList.add("#49beee");
        arrayList.add("#874fee");

        arrayList.add("#f3a78c");
        arrayList.add("#f07a5c");
        arrayList.add("#bb771a");
        arrayList.add("#fc89fb");
        arrayList.add("#88fe00");

        arrayList.add("#da417a");
        arrayList.add("#ee5931");
        arrayList.add("#f5b432");
        arrayList.add("#73c449");
        arrayList.add("#38aff8");

        arrayList.add("#e57ca4");
        arrayList.add("#f4a884");
        arrayList.add("#fbd986");
        arrayList.add("#b7e19d");
        arrayList.add("#76d9ee");

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));

        // mRecyclerView.setLayoutManager(new LinearLayoutManager(Xyz.this, LinearLayoutManager.VERTICAL, false));
        ColorAdapter mAdapter = new ColorAdapter(arrayList, dialog);
        mRecyclerView.setAdapter(mAdapter);

        dialog.show();




    }




    public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

        private static final String TAG = "ColorAdapter";

        ArrayList<String> cnames = new ArrayList<>();

        Dialog mybuilder;

        public ColorAdapter(ArrayList<String> cnames, Dialog mybuilder) {
            super();
            this.cnames = cnames;
            this.mybuilder = mybuilder;
        }




        @Override
        public ColorAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.color_item, viewGroup, false);
            return new ColorAdapter.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final ColorAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.textViewName.setCircleColor(Color.parseColor(cnames.get(i)));

//            viewHolder.textViewName.setBackgroundColor(Color.RED);
            viewHolder.textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mybuilder.dismiss();
                    colorCode = cnames.get(i);
                    itemstxtColor.setText(""+colorCode.toUpperCase());
                }
            });

        }


        @Override
        public int getItemCount() {
            return cnames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            View view11 = null;
            CircleView textViewName;
            RelativeLayout realtive1;
            public ViewHolder(View itemView) {
                super(itemView);
                view11 = itemView;
                //  realtive1 = (RelativeLayout) itemView.findViewById(R.id.realtive1);
                textViewName = (CircleView) itemView.findViewById(R.id.textView);

                //  CircleTextView  mCircleTextView = (CircleTextView) findViewById(R.id.textView); //change with your id

            }

        }



        public void updateData(ArrayList<String> cnames) {
            // TODO Auto-generated method stub
            this.cnames = cnames;
            notifyDataSetChanged();
        }


    }



}
