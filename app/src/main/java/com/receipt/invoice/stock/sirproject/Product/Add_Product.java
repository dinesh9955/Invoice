package com.receipt.invoice.stock.sirproject.Product;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.isapanah.awesomespinner.AwesomeSpinner;
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
import com.receipt.invoice.stock.sirproject.API.AllSirApi;
import com.receipt.invoice.stock.sirproject.Adapter.Select_Warehouse_Adapter;
import com.receipt.invoice.stock.sirproject.Base.BaseFragment;
import com.receipt.invoice.stock.sirproject.BuildConfig;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.ImageResource.FileCompressor;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.GlideApp;
import com.receipt.invoice.stock.sirproject.Utils.Utility;
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

import static android.content.Context.MODE_PRIVATE;

public class Add_Product extends BaseFragment implements Select_Warehouse_Adapter.Callback {


    private static final int GALLARY_aCTION_PICK_CODE = 10;
    private static final int CAMERA_ACTION_PICK_CODE = 9;
    private static final int pic_id = 123;
    private static final String TAG = "Add_Product";
    final int PICK_CONTACT = 1;
    final int PERMISSION_REQUEST_CONTACT = 10;
    // Select image from galary and camera compress file
    Context applicationContext = Product_Activity.getContextOfApplication();
    FileCompressor mCompressor;
    RoundedImageView uploadimage;
    TextView imagedescription, selectwarehouse, taxable, select;
    EditText productname, productprice, productdescription, reorderlevel, othercategory,mesurementunitedittxt;
    Button addproduct;
    RadioButton ryes, rno;
    RadioGroup radiogroup;
    AwesomeSpinner productcategory, measurementunit, selectcompany;
    ArrayList<String> companies = new ArrayList<>();
    File fileimage;
    RecyclerView recyclerwarehouse;
    ArrayList<String> warehousename = new ArrayList<>();

    String otherunitstr="";

    Select_Warehouse_Adapter select_Warehouse_Adapter;
    Dialog mybuilder;



    ArrayList<String> measurementunits = new ArrayList<>();
    ArrayList<String> measurementIds = new ArrayList<>();
    ImageView avibackground;
    String selectedCategoryId = "";
    String selectedMeasuremetnId = "";
    String selectedCompanyId = "";
    String selectedTaxable = "0";
    ArrayList<String> cnames = new ArrayList<>();
    ArrayList<String> cids = new ArrayList<>();
    ArrayList<String> catnames = new ArrayList<>();
    ArrayList<String> catids = new ArrayList<>();
    String filepath = "";
    Uri image_uri;
    private AVLoadingIndicatorView avi;

    public Add_Product() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
// soft input mode
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = inflater.inflate(R.layout.fragment_add__product, container, false);

        FindByIds(view);
        setFonts();
        setListeners();

        MeasurementUnits();
        companyget();
        categoriesget();

        productcategory.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        productcategory.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
        measurementunit.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        measurementunit.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
        selectcompany.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
        selectcompany.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));

        mCompressor = new FileCompressor(getActivity());


        return view;
    }

    private void FindByIds(View view) {
        uploadimage = view.findViewById(R.id.uploadimage);
        imagedescription = view.findViewById(R.id.imagedescription);
        selectwarehouse = view.findViewById(R.id.selectwarehouse);
        taxable = view.findViewById(R.id.taxable);
        productname = view.findViewById(R.id.productname);
        productprice = view.findViewById(R.id.productprice);
        productdescription = view.findViewById(R.id.productdescription);
        addproduct = view.findViewById(R.id.addprodduct);
        ryes = view.findViewById(R.id.ryes);
        rno = view.findViewById(R.id.rno);
        radiogroup = view.findViewById(R.id.radiogroup);
        productcategory = view.findViewById(R.id.productcategory);
        measurementunit = view.findViewById(R.id.measurementunit);

        mesurementunitedittxt= view.findViewById(R.id.mesurementunitedittxt);
        selectcompany = view.findViewById(R.id.selectcompany);

        avi = view.findViewById(R.id.avi);
        avibackground = view.findViewById(R.id.avibackground);
        reorderlevel = view.findViewById(R.id.reorderlevel);
        othercategory = view.findViewById(R.id.othercategory);
    }

    private void setFonts() {

        imagedescription.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Light.ttf"));
        productname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        reorderlevel.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        othercategory.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        mesurementunitedittxt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        productprice.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        productdescription.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Regular.ttf"));
        selectwarehouse.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Light.otf"));
        taxable.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Bold.otf"));
        ryes.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
        rno.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/AzoSans-Medium.otf"));
        addproduct.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Ubuntu-Medium.ttf"));

    }

    private void setListeners() {

        selectwarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WareHouseDialog();
            }
        });

        selectcompany.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCompanyId = cids.get(position);
                Log.e("selectedCompany", selectedCompanyId);
            }
        });

        productcategory.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                selectedCategoryId = catids.get(position);
                Log.e("selectedCategory", selectedCategoryId);

            }
        });

        measurementunit.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                measurementunit.setSelected(true);
                //weight.animate().setDuration(1000).alpha(1.0f);

                String mesasurmentunitchck= itemAtPosition;

                if( mesasurmentunitchck.equals("Other"))
                {
                    mesurementunitedittxt.setVisibility(View.VISIBLE);
                    selectedMeasuremetnId = measurementIds.get(position);

                    //Log.e("selectedMeasuremetnId", selectedMeasuremetnId);


                }else {

                    selectedMeasuremetnId = measurementIds.get(position);
                    mesurementunitedittxt.setVisibility(View.INVISIBLE);
                   // Log.e("selectedMeasuremetnId", selectedMeasuremetnId);
                }

            }
        });

        ryes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    selectedTaxable = "1";
                }
            }
        });

        rno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    selectedTaxable = "0";
                }
            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProduct();
            }
        });


        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();

            }
        });
    }

    private void SelectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CAMERA_ACTION_PICK_CODE) {
                try {
                    fileimage = mCompressor.compressToFile(fileimage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GlideApp.with(getActivity()).load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(uploadimage);
            } else if (requestCode == GALLARY_aCTION_PICK_CODE) {
                Uri selectedImage = data.getData();
                try {
                    fileimage = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                GlideApp.with(getActivity()).load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(uploadimage);

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

    private void MeasurementUnits() {
        RequestParams params = new RequestParams();
        params.add("weight_class", "PRODUCT");
        String token = Constant.GetSharedPreferences(getContext(), Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "product/getMeasurementUnit", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("munitsResp", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray unit = data.getJSONArray("unit");
                        if (unit.length() > 0) {
                            for (int i = 0; i < unit.length(); i++) {
                                JSONObject item = unit.getJSONObject(i);
                                String weight_class_id = item.getString("weight_class_id");
                                String units = item.getString("title");
                                measurementIds.add(weight_class_id);
                                measurementunits.add(units);
                                ArrayAdapter<String> munitsadp = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, measurementunits);
                                measurementunit.setAdapter(munitsadp);
                                measurementunit.setDownArrowTintColor(getResources().getColor(R.color.lightpurple));
                                measurementunit.setSelectedItemHintColor(getResources().getColor(R.color.lightpurple));
                            }
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
                    Log.e("munitsRespF", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            //Constant.ErrorToast(Company_Setup_Activity.this,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void companyget() {

        final SharedPreferences preferences = getActivity().getSharedPreferences(Constant.PREF_BASE, MODE_PRIVATE);
        String token = Constant.GetSharedPreferences(getActivity(), Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "company/listing", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("responsecompany", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray company = data.getJSONArray("company");
                        if (company.length() > 0) {
                            for (int i = 0; i < company.length(); i++) {
                                JSONObject item = company.getJSONObject(i);
                                String company_id = item.getString("company_id");
                                String company_name = item.getString("name");

                                cnames.add(company_name);
                                cids.add(company_id);

                                ArrayAdapter<String> namesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cnames);
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
                if (responseBody != null) {
                    String response = new String(responseBody);
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

    public void categoriesget() {

        String token = Constant.GetSharedPreferences(getActivity(), Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        client.post(AllSirApi.BASE_URL + "category/listing", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("categoriesResp", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray category = data.getJSONArray("category");
                        if (category.length() > 0) {
                            for (int i = 0; i < category.length(); i++) {
                                JSONObject item = category.getJSONObject(i);
                                String category_id = item.getString("category_id");
                                String name = item.getString("name");

                                if (!name.equals("qqq")) {

                                    catids.add(category_id);
                                    catnames.add(name);

                                }

                                ArrayAdapter<String> catadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, catnames);
                                productcategory.setAdapter(catadapter);


                            }
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
                    Log.e("categoriesRespF", response);
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

    private void AddProduct() {

        Utility.hideKeypad(getActivity());

        String pname = productname.getText().toString();
        String price = productprice.getText().toString();
        //String pweight = weight.getText().toString();
        String pdesc = productdescription.getText().toString();
        String reorder = reorderlevel.getText().toString();
        String othercat = othercategory.getText().toString();
        otherunitstr=mesurementunitedittxt.getText().toString();
        Log.e("selectedMeasuremetnId", selectedMeasuremetnId);
        if (pname.isEmpty()) {
            productname.setError("Required");
            productname.requestFocus();
        } else if (price.isEmpty()) {
            productprice.setError("Required");
            productprice.requestFocus();
        }  else if (selectedMeasuremetnId.equals("")) {
            Constant.ErrorToast(getActivity(), "Measurement unit is required");
        } else if (selectedCompanyId.equals("")) {
            Constant.ErrorToast(getActivity(), "Company is required");
//        } else if (selectedTaxable.equals("")) {
//            Constant.ErrorToast(getActivity(), "Tax information is required");
        } else if (selectedCategoryId.equals("") && othercat.isEmpty()) {
            Constant.ErrorToast(getActivity(), "Product category is required");
        } else {


            avi.smoothToShow();
            avibackground.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.add("name", pname);
            params.add("price", price);
            params.add("minimum", reorder);

            // params.add("weight",pweight);
            params.add("description", pdesc);
            params.add("company_id", selectedCompanyId);
            params.add("is_taxable", selectedTaxable);
            params.add("product_type", "PRODUCT");
            if (selectedMeasuremetnId.equals("19")) {
                params.add("weight_class_id", selectedMeasuremetnId);
                params.add("other_weight_unit", otherunitstr);
            }else {
                params.add("weight_class_id", selectedMeasuremetnId);
            }


//            if (othercat.isEmpty()) {
//                params.add("category_id", selectedCategoryId);
//            } else if (selectedCategoryId.equals("")) {
//                params.add("category_name", othercat);
//            } else {
//                params.add("category_id", selectedCategoryId);
//            }


            if (!selectedCategoryId.equals("")) {
                params.add("category_id", selectedCategoryId);
            }

            if (selectedCategoryId.equals("")) {
                if (!othercat.isEmpty()) {
                    params.add("custom_category", othercat);
                }
            }

            if (fileimage != null) {
                try {
                    params.put("image", fileimage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            Gson gson = new Gson();
            String json = gson.toJson(params);

            Log.e(TAG, "jsonAA "+json);

            Log.e("paramss", String.valueOf(params));

            String token = Constant.GetSharedPreferences(getContext(), Constant.ACCESS_TOKEN);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.addHeader("Access-Token", token);
            client.post(AllSirApi.BASE_URL + "product/add", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                  //  String response111 = new String(responseBody);
                    Log.e(TAG, "addproductResp1 "+responseBody.length);
                    Utility.hideKeypad(getActivity());
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);

                    if(responseBody.length == 0){
                        Constant.ErrorToast(getActivity(), "Something went wrong, try again!");
                    }else{
                        String response = new String(responseBody);
                        Log.e("addproductResp", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("true")) {

                                Map<String, Object> eventValue = new HashMap<String, Object>();
                                eventValue.put(AFInAppEventParameterName.PARAM_1, "product_addnew");
                                AppsFlyerLib.getInstance().trackEvent(getActivity(), "product_addnew", eventValue);

                                Bundle params2 = new Bundle();
                                params2.putString("event_name", "My Products");
                                firebaseAnalytics.logEvent("product_addnew", params2);

                                Constant.SuccessToast(getActivity(), "Product Added");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getContext(), Product_Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }, 1500);

                            }

                            if (status.equals("false")) {
                                if(jsonObject.has("error")){
                                    Constant.ErrorToast(getActivity(), jsonObject.getString("error"));
                                }

                                if(jsonObject.has("message")){
                                    Constant.ErrorToast(getActivity(), jsonObject.getString("message"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response111 = new String(responseBody);
                    Log.e("addproductResp2 ", response111);
                    Utility.hideKeypad(getActivity());
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    if (responseBody != null) {
                        String response = new String(responseBody);
                        Log.e("addproductRespF", response);
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



//    @Override
//    public void onWarehouseSelected(int count, ArrayList<String> warehouseList, String wherehousenamstr) {
//        mybuilder.dismiss();
//        if (count!=0){
//            selectwarehouse.setText(wherehousenamstr);
//        }
//        else {
//            selectwarehouse.setText("Select Warehouse (s)");
//        }
//
//
//
//    }


    @Override
    public void onWarehouseSelected(String warehouseList, String wherehousenamstrSelect) {

        mybuilder.dismiss();

        if(wherehousenamstrSelect.equalsIgnoreCase("")){
            selectwarehouse.setText("Select Warehouse (s)");
        }else {
            selectwarehouse.setText(warehouseList);
        }

        Log.e(TAG, "warehouseList "+wherehousenamstrSelect);

        //warehouses = wherehousenamstrSelect;

    }


}

