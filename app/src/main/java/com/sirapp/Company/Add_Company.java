package com.sirapp.Company;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
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
import com.sirapp.Home.GoProActivity;
import com.sirapp.ImageResource.FileCompressor;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseFragment;
import com.sirapp.BuildConfig;
import com.sirapp.Model.Company_list;
import com.sirapp.R;
import com.sirapp.Settings.SettingsActivity;
import com.sirapp.Utils.Utility;
import com.sirapp.API.SavePref;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Company extends BaseFragment {


    private static final String TAG = "Add_Company";

    int companyList = 0;

    public Add_Company() {
        // Required empty public constructor
    }

    Context applicationContext = Companies_Activity.getContextOfApplication();
    private static final int GALLARY_aCTION_PICK_CODE=10;
    private static final int CAMERA_ACTION_PICK_CODE=9;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;
    Uri imageurluri;
//    AwesomeSpinner defaultcurrency;
    ArrayList<String> currencies = new ArrayList<>();
    ArrayList<String> currencies_id = new ArrayList<>();
    String selected_currency="";
    RoundedImageView uploadimage;
    File fileimage;
    String filepath="";
    TextView heading, description, imagedescription, haveoneormore, list,txtpayment;
    EditText name, email, phone, website, CompanyAddress,ediban,edbank,edswift,edpaypal,edcheque;
    Button save, addwarehouse;

    String edname="",edemail="",edphone="",edwebsite="",edCompanyAddress="";
    String company_id="";
    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    TextView terms,termsdescprice1,termdescription1;

    Button itemstxtColor;

    String colorCode = "#ffffff";

    SavePref savePref;
//    FileCompressor mCompressor;

    Button defaultcurrency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view =  inflater.inflate(R.layout.fragment_add__company, container, false);


        savePref=new SavePref(getContext());
        FindByIds(view);
        DefaultCurrencies();
        setListeners();
        setFonts();
        companyget();
        //mCompressor = new FileCompressor(getActivity());


        return view;

    }
    private void FindByIds(View view) {

        itemstxtColor = view.findViewById(R.id.itemstxtColor);

        defaultcurrency = view.findViewById(R.id.defaultcurrency1);

     //   defaultcurrency.setSpinnerHint();

        //defaultcurrency.add("Select Category");



        uploadimage = view.findViewById(R.id.uploadimage);
        heading = view.findViewById(R.id.heading);
        description = view.findViewById(R.id.description);
        imagedescription = view.findViewById(R.id.imagedescription);
        haveoneormore = view.findViewById(R.id.haveoneormore);
        list = view.findViewById(R.id.list);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        website = view.findViewById(R.id.website);
        CompanyAddress = view.findViewById(R.id.CompanyAddress);
        save = view.findViewById(R.id.save);
        addwarehouse = view.findViewById(R.id.addwarehouse);
        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        terms = view.findViewById(R.id.terms);
        termsdescprice1 = view.findViewById(R.id.termsdescprice1);
        termdescription1 = view.findViewById(R.id.termdescription1);
        ediban = view.findViewById(R.id.ediban);
        edbank = view.findViewById(R.id.edbank);
        edswift = view.findViewById(R.id.edswift);
        edpaypal = view.findViewById(R.id.edpaypal);
        edcheque = view.findViewById(R.id.edcheque);
        txtpayment = view.findViewById(R.id.txtpayment);
    }
    private void setListeners() {

        itemstxtColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createShortDialog();


//                ColorPicker colorPicker = new ColorPicker(getActivity());
//                colorPicker.show();
//                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
//                    @Override
//                    public void onChooseColor(int position,int color) {
//                        // put code
//                       // Log.e(TAG, "color:"+color);
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

      uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  PickSetup setup = new PickSetup().setSystemDialog(true);

                PickImageDialog.build(setup)
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                if (r.getPath().contains(".jpg") || r.getPath().contains(".jpeg") || r.getPath().contains(".png")){
                                    //gallery
                                    fileimage = new File(r.getPath());
                                    uploadimage.setImageURI(r.getUri());
                                    Log.e("pathGallery",r.getPath());
                                }
                                else if (String.valueOf(r.getUri()).contains(".jpg") || String.valueOf(r.getUri()).contains(".jpeg") ||
                                        String.valueOf(r.getUri()).contains(".png")){
                                    //camera
                                    fileimage = new File(String.valueOf(r.getUri()));
                                    uploadimage.setImageURI(r.getUri());
                                    Log.e("pathCam",String.valueOf(r.getUri()));
                                }
                                uploadimage.setCornerRadius(200);
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                // Toast.makeText(getContext(),r.getError().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }).show(getFragmentManager());
            }*/

                SelectImage();
            }
        });

      /*
      uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage(Add_Company.this, "Select your image:");
                uploadimage.setCornerRadius(200);
            }
        });


       */



//        defaultcurrency.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
//            @Override
//            public void onItemSelected(int position, String itemAtPosition) {
//                defaultcurrency.setSelected(true);
//                selected_currency = currencies_id.get(position);
//                Log.e("selected_currecy",selected_currency);
//            }
//        });



        addwarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(getActivity(),Add_Warehouse_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("company_id",company_id);
                startActivity(intent);*/
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(companyList == 0){
                    addcompany();
//                }else{
//                    if(pref.getCompany().equalsIgnoreCase("company")){
//                        addcompany();
//                    }else{
//                        Intent intent = new Intent(getActivity(), SettingsActivity.class);
//                        intent.putExtra("key", "company");
//                        startActivityForResult(intent, 42);
//                    }
//                }
            }
        });




        defaultcurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView mRecyclerView;
                MenuAdapter mAdapter;

                final Dialog mybuilder = new Dialog(getActivity());
                mybuilder.setContentView(R.layout.select_company_dialog_2);


                mRecyclerView = (RecyclerView) mybuilder.findViewById(R.id.recycler_list);
//                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                mAdapter = new MenuAdapter(currencies, mybuilder);
                mRecyclerView.setAdapter(mAdapter);

                mybuilder.show();
                mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                Window window = mybuilder.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(R.color.transparent);

            }
        });


    }
    private void setFonts() {
        list.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        haveoneormore.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Light.ttf"));
        imagedescription.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Light.ttf"));
        description.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Light.ttf"));
        heading.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Bold.otf"));
        CompanyAddress.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        website.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        phone.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        email.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        name.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        ediban.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        edbank.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        edcheque.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        edpaypal.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        edswift.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        addwarehouse.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Medium.ttf"));
        save.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Medium.ttf"));
        termsdescprice1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Bold.ttf"));
        termdescription1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Light.ttf"));
        terms.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Light.ttf"));
        txtpayment.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Ubuntu-Light.ttf"));

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


//        final CharSequence[] items={getString(R.string.dialog_Camera),getString(R.string.dialog_Gallery),getString(R.string.dialog_Cancel)};
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
//
//
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



    public void companyget()
    {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);
        String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);

        Log.e("access token",token);
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
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray company = data.getJSONArray("company");
                        companyList = company.length();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(responseBody!=null){
                    String response = new String(responseBody);
                }
                else {
                }
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);

            }
        });
    }



    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_ACTION_PICK_CODE) {
            try{
           // fileimage = mCompressor.compressToFile(fileimage);
                fileimage = fileimage;
            } catch (Exception e) {
                e.printStackTrace();
            }
                Utility.glideSet(getActivity() , fileimage , uploadimage);
//            GlideApp.with(getActivity()).load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(uploadimage);
        } else if (requestCode == GALLARY_aCTION_PICK_CODE) {

                    Uri selectedImage = data.getData();
                try {
                    //fileimage = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    fileimage = new File(getRealPathFromUri(selectedImage));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Utility.glideSet(getActivity() , fileimage , uploadimage);
               // GlideApp.with(getActivity()).load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(uploadimage);

            }

        }


        if (requestCode == 42) {
            if (resultCode == Activity.RESULT_OK) {
               // addcompany();
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
    /* @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data);
         uploadimage.setImageBitmap(bitmap);
         //Log.e("PathPicker",ImagePicker.getImagePathFromResult(this, requestCode, resultCode, data));
         filepath = ImagePicker.getImagePathFromResult(getActivity(), requestCode, resultCode, data);
         fileimage = new File(filepath);
     }
 */
    private void DefaultCurrencies(){
        String token = Constant.GetSharedPreferences(getContext(),Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);

        RequestParams params = new RequestParams();
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "currency/all", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("currencyResp",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray currency = data.getJSONArray("currencies");
                        if (currency.length()>0){
                            for (int i=0; i<currency.length(); i++){
                                JSONObject item = currency.getJSONObject(i);
                                String currency_id = item.getString("currency_id");
                                String symbol_left = item.getString("symbol_left");
                                String title = item.getString("title");

                                currencies_id.add(currency_id);
                                currencies.add(symbol_left+" "+title);


                            }

//                            ArrayAdapter<String> defaultcurrencyadap = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, currencies);
//                            defaultcurrency.setAdapter(defaultcurrencyadap);
//                            defaultcurrency.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
//                            defaultcurrency.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));

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
                    Log.e("currencyRespF",response);
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
    public void addcompany()
    {
        edname = name.getText().toString();
        edemail = email.getText().toString();
        edphone = phone.getText().toString();
        edwebsite = website.getText().toString();
        edCompanyAddress = CompanyAddress.getText().toString();

        String iban = ediban.getText().toString();
        String bank = edbank.getText().toString();
        String swift = edswift.getText().toString();
        String paypal = edpaypal.getText().toString();
        String cheque = edcheque.getText().toString();


        if (TextUtils.isEmpty(name.getText())) {
            name.setError(getActivity().getString(R.string.dialog_Fieldisrequired));
            name.requestFocus();
        } else if (selected_currency.equals(""))
        {
            Constant.ErrorToast(getActivity(),getString(R.string.dialog_Please_select_currency));
        }


        else {

            boolean isEnter = isEnter();

            Log.e(TAG, "isEnterAA "+isEnter);

            if(isEnter == false){
                return;
            }



            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.add("name",edname);
            params.add("currency_id",selected_currency);
            params.add("phone_number",edphone);
            params.add("email",edemail);
            params.add("website",edwebsite);
            params.add("address",edCompanyAddress);
            params.add("payment_iban",iban);
            params.add("payment_bank_name",bank);
            params.add("payment_swift_bic",swift);
            params.add("paypal_email",paypal);
            params.add("cheque_payable_to",cheque);
            params.add("color",colorCode);

            if (fileimage!=null){
                try {
                    params.put("image",fileimage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


            Log.e(TAG, "paramsAA "+params.toString());

            String token = Constant.GetSharedPreferences(getActivity(),Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.addHeader("Access-Token",token);

            params.add("language", ""+getLanguage());
            client.post(AllSirApi.BASE_URL + "company/add", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    String response = new String(responseBody);
                    Log.e("responsecompany",response);
                    Utility.hideKeypad(getActivity());
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        if (status.equals("true"))
                        {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject company = data.getJSONObject("company");
                            company_id = company.getString("company_id");
                            savePref.setCompanyId(company_id);

                            Map<String, Object> eventValue = new HashMap<String, Object>();
                            eventValue.put(AFInAppEventParameterName.PARAM_1, "companies_addnew");
                            AppsFlyerLib.getInstance().trackEvent(getActivity(), "companies_addnew", eventValue);

                            Bundle params2 = new Bundle();
                            params2.putString("event_name", "My Companies");
                            firebaseAnalytics.logEvent("companies_addnew", params2);

                            pref.setCompany("");

                            Constant.SuccessToast(getActivity(), getString(R.string.dialog_CompanyCreatedSuccessfully));



                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getContext(), Companies_Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            },1000);
                        }

                        if (status.equals("false"))
                        {
                            Constant.ErrorToast(getActivity(), jsonObject.getString("message").replace("allow","allowed"));


                            if( jsonObject.has("code")){
                                String code = jsonObject.getString("code");

                                if(code.equalsIgnoreCase("subscription")){
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if(jsonObject.getString("message").contains(" Extra Companies")){
                                                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                                                    intent.putExtra("key", "company");
                                                    startActivityForResult(intent, 42);
                                                }else{
                                                    Intent intent = new Intent(getActivity(), GoProActivity.class);
                                                    startActivity(intent);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 3000);
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
                        Utility.hideKeypad(getActivity());

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
//                    else {
//                        Constant.ErrorToast(getActivity(),"Something went wrong, try again!");
//                    }
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);

                }
            });
        }
    }



    private boolean isEnter() {

        boolean isEntered = false;
        String iban = ediban.getText().toString();
        String bank = edbank.getText().toString();
        String swift = edswift.getText().toString();

        if (iban.equals("") && bank.equals("") && swift.equals("")) {
            isEntered = true;
            Log.e(TAG, "AAAAAAAAAAA");
        }else{
            if (iban.equals("")){
                Constant.ErrorToast(getActivity(), getString(R.string.dialog_PleaseEnterIBAN));
                isEntered = false;
            }else if(bank.equals("")){
                Constant.ErrorToast(getActivity(), getString(R.string.dialog_PleaseEnterBankName));
                isEntered = false;
            }else if(swift.equals("")){
                Constant.ErrorToast(getActivity(), getString(R.string.dialog_PleaseEnterSwiftBIC));
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
                    selected_currency = cnames.get(i);
                    defaultcurrency.setText(selected_currency);
                    selected_currency = currencies_id.get(i);
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
