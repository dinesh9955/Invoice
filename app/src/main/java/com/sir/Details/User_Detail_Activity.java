package com.sir.Details;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import androidx.core.content.FileProvider;

import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sir.Constant.Constant;
import com.sir.ImageResource.FileCompressor;
import com.sir.API.AllSirApi;
import com.sir.Base.BaseActivity;
import com.sir.BuildConfig;
import com.sir.Home.Home_Activity;
import com.sir.R;
import com.sir.User.User_Activity;
import com.sir.Utils.GlideApp;
import com.sir.Utils.Utility;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class User_Detail_Activity extends BaseActivity {
    TextView nametxt,save,cancel,emailtxt,phonetxt;
    EditText name,email,password;
    CircleImageView image;
    ImageView editbtn,changepic;
    CheckBox chkinvoices,chkestimates,chkreceipts,chkpurchase,chkpayment,chkstock,chktax,chksubadmin,chkproducts,chkcustomers,chkcredit,chkservices,chksuppliers,chkdebit;

    String fullname,mail,pass,invoices,estimates,receipts,purchase,products,customers,payments,stock,taxes,sadmin,services,suppliers,debit,credit,user_id;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;
    File fileimage;
    // fetch image from camera and Gallary
    Context applicationContext = User_Activity.getContextOfApplication();
    private static final int GALLARY_aCTION_PICK_CODE=10;
    private static final int CAMERA_ACTION_PICK_CODE=9;
    FileCompressor mCompressor;
// Image Url
    String image_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__detail_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        Constant.toolbar(User_Detail_Activity.this,getString(R.string.header_details));
        Constant.bottomNav(User_Detail_Activity.this,-1);
        FindByIds();
        setFonts();
        mCompressor = new FileCompressor(this);
        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);

        image_path = getIntent().getStringExtra("userpath");


        if (getIntent().hasExtra("namee"))
        {
           fullname = getIntent().getStringExtra("namee");
            mail = getIntent().getStringExtra("email");
            invoices = getIntent().getStringExtra("invoice");
            estimates = getIntent().getStringExtra("estimate");
            stock = getIntent().getStringExtra("stock");
            receipts = getIntent().getStringExtra("receipt");
            purchase = getIntent().getStringExtra("purchase_order");
            payments = getIntent().getStringExtra("payment");
            taxes = getIntent().getStringExtra("tax");
            customers = getIntent().getStringExtra("customer");
            suppliers = getIntent().getStringExtra("supplier");
            products = getIntent().getStringExtra("product");
            services = getIntent().getStringExtra("service");
            debit = getIntent().getStringExtra("debit");
            credit = getIntent().getStringExtra("credit");
            sadmin = getIntent().getStringExtra("subadmin");
            user_id = getIntent().getStringExtra("user_id");
    }

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.app_icon);
        GlideApp.with(this)
                .load(image_path)
                .apply(options)
                .into(image);
        chkinvoices.setChecked(true);

        name.setText(fullname);
        email.setText(mail);
        password.setText("51202332");
        if (invoices.equals("1"))
        {
            chkinvoices.setChecked(true);
            chkinvoices.setEnabled(false);
        }
        else if (invoices.equals("0"))
        {
            chkinvoices.setChecked(false);
            chkinvoices.setEnabled(false);
        }

        if (estimates.equals("1"))
        {
            chkestimates.setChecked(true);
            chkestimates.setEnabled(false);
        }
        else if (estimates.equals("0"))
        {
            chkestimates.setChecked(false);
            chkestimates.setEnabled(false);
        }


        if (stock.equals("1"))
        {
            chkstock.setChecked(true);
            chkstock.setEnabled(false);
        }
        else if (stock.equals("0"))
        {
            chkstock.setChecked(false);
            chkstock.setEnabled(false);
        }


        if (receipts.equals("1"))
        {
            chkreceipts.setChecked(true);
            chkreceipts.setEnabled(false);
        }
        else if (receipts.equals("0"))
        {
            chkreceipts.setChecked(false);
            chkreceipts.setEnabled(false);
        }

        if (purchase.equals("1"))
        {
            chkpurchase.setChecked(true);
            chkpurchase.setEnabled(false);
        }
        else if (purchase.equals("0"))
        {
            chkpurchase.setChecked(false);
            chkpurchase.setEnabled(false);
        }

        if (payments.equals("1"))
        {
            chkpayment.setChecked(true);
            chkpayment.setEnabled(false);
        }
        else if (payments.equals("0"))
        {
            chkpayment.setChecked(false);
            chkpayment.setEnabled(false);
        }

        if (taxes.equals("1"))
        {
            chktax.setChecked(true);
            chktax.setEnabled(false);
        }
        else if (taxes.equals("0"))
        {
            chktax.setChecked(false);
            chktax.setEnabled(false);
        }

        if (customers.equals("1"))
        {
            chkcustomers.setChecked(true);
            chkcustomers.setEnabled(false);
        }
        else if (customers.equals("0"))
        {
            chkcustomers.setChecked(false);
            chkcustomers.setEnabled(false);
        }


        if (suppliers.equals("1"))
        {
            chksuppliers.setChecked(true);
            chksuppliers.setEnabled(false);
        }
        else if (suppliers.equals("0"))
        {
            chksuppliers.setChecked(false);
            chksuppliers.setEnabled(false);
        }


        if (products.equals("1"))
        {
            chkproducts.setChecked(true);
            chkproducts.setEnabled(false);
        }
        else if (products.equals("0"))
        {
            chkproducts.setChecked(false);
            chkproducts.setEnabled(false);
        }


        if (services.equals("1"))
        {
            chkservices.setChecked(true);
            chkservices.setEnabled(false);
        }
        else if (services.equals("0"))
        {
            chkservices.setChecked(false);
            chkservices.setEnabled(false);
        }

        if (debit.equals("1"))
        {
            chkdebit.setChecked(true);
            chkdebit.setEnabled(false);
        }
        else if (debit.equals("0"))
        {
            chkdebit.setChecked(false);
            chkdebit.setEnabled(false);
        }


        if (credit.equals("1"))
        {
            chkcredit.setChecked(true);
            chkcredit.setEnabled(false);
        }
        else if (credit.equals("0"))
        {
            chkcredit.setChecked(false);
            chkcredit.setEnabled(false);
        }

        if (sadmin.equals("1"))
        {
            chksubadmin.setChecked(true);
            chksubadmin.setEnabled(false);
        }
        else if (sadmin.equals("0"))
        {
            chksubadmin.setChecked(false);
            chksubadmin.setEnabled(false);
        }

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editbtn.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);

                changepic.setVisibility(View.VISIBLE);
                name.setEnabled(true);
                email.setEnabled(true);
                chkinvoices.setEnabled(true);
                chkestimates.setEnabled(true);
                chkreceipts.setEnabled(true);
                chkpurchase.setEnabled(true);
                chkpayment.setEnabled(true);
                chkstock.setEnabled(true);
                chktax.setEnabled(true);
                chksubadmin.setEnabled(true);
                chkcustomers.setEnabled(true);
                chkcredit.setEnabled(true);
                chkservices.setEnabled(true);
                chksuppliers.setEnabled(true);
                chkdebit.setEnabled(true);
                chkproducts.setEnabled(true);


            }
        });

        chkinvoices.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkinvoices.isChecked())
                {
                    invoices ="1";
                    Log.e("loginvoice",invoices);
                }
                else if (!chkinvoices.isChecked())
                {
                    invoices = "0";
                    Log.e("loginvoice",invoices);
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



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Edit_subuser();

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changepic.setVisibility(View.GONE);
                name.setEnabled(false);
                email.setEnabled(false);
                chkinvoices.setEnabled(false);
                chkestimates.setEnabled(false);
                chkreceipts.setEnabled(false);
                chkpurchase.setEnabled(false);
                chkpayment.setEnabled(false);
                chkstock.setEnabled(false);
                chktax.setEnabled(false);
                chksubadmin.setEnabled(false);
                chkcustomers.setEnabled(false);
                chkcredit.setEnabled(false);
                chkservices.setEnabled(false);
                chksuppliers.setEnabled(false);
                chkdebit.setEnabled(false);
                chkproducts.setEnabled(false);
                image.setEnabled(false);

                cancel.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                editbtn.setVisibility(View.VISIBLE);


            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });




    }
    private void FindByIds(){
        nametxt = findViewById(R.id.nametxt);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        emailtxt = findViewById(R.id.emailtxt);
        phonetxt = findViewById(R.id.phonetxt);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        image = findViewById(R.id.image);
        editbtn = findViewById(R.id.editbtn);
        changepic = findViewById(R.id.changepic);
        chkinvoices = findViewById(R.id.chkinvoices);
        chkestimates = findViewById(R.id.chkestimates);
        chkreceipts = findViewById(R.id.chkreceipts);
        chkpurchase = findViewById(R.id.chkpurchase);
        chkpayment = findViewById(R.id.chkpayments);
        chkstock = findViewById(R.id.chkstock);
        chktax = findViewById(R.id.chktaxes);
        chksubadmin = findViewById(R.id.chksubadmin);
        chkproducts = findViewById(R.id.chkproducts);
        chkcustomers = findViewById(R.id.chkcustomers);
        chkcredit = findViewById(R.id.chkcredit);
        chkservices = findViewById(R.id.chkservices);
        chksuppliers = findViewById(R.id.chksuppliers);
        chkdebit = findViewById(R.id.chkdebit);

    }

    private void setFonts(){

/*
        name.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Medium.otf"));
        email.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Regular.otf"));
        roles.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Regular.otf"));
*/
    }
    private void SelectImage() {
        final CharSequence[] items={getString(R.string.dialog_Camera), getString(R.string.dialog_Gallery), getString(R.string.dialog_Cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_AddImage));

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals(getString(R.string.dialog_Camera))) {
                    requestStoragePermission(true);
                } else if (items[i].equals(getString(R.string.dialog_Gallery))) {

                    requestStoragePermission(false);
                } else if (items[i].equals(getString(R.string.dialog_Cancel))) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        Uri uri = Uri.fromParts("package", getPackageName(), null);
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
                Utility.glideSet(this, fileimage, image);
              //  GlideApp.with(this).load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(image);
            } else if (requestCode == GALLARY_aCTION_PICK_CODE) {
                Uri selectedImage = data.getData();
                try {
                    fileimage = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Utility.glideSet(this, fileimage, image);
               // GlideApp.with(this).load(fileimage).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon)).into(image);

            }

        }


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
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
        File storageDir =getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    public void Edit_subuser()
    {
        avi.smoothToShow();
        avibackground.setVisibility(View.VISIBLE);

        String mail = email.getText().toString();
        String fullname = name.getText().toString();

        RequestParams params = new RequestParams();
        params.add("subuser_id",user_id);
        params.add("email",mail);
        params.add("fullname",fullname);
        params.add("permission[invoice]",invoices);
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        String token = Constant.GetSharedPreferences(User_Detail_Activity.this,Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token",token);
        params.add("language", ""+getLanguage());
        client.post(AllSirApi.BASE_URL + "user/editSubUser", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String(responseBody);
                Log.e("responseuser",response);
                avi.smoothToHide();
                avibackground.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true"))
                    {
                        String message = jsonObject.getString("message");

                        Constant.SuccessToast(User_Detail_Activity.this,message);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(User_Detail_Activity.this,Home_Activity.class);
                                startActivity(intent);
                            }
                        },1000);




                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    Log.e("responseuser", response);
                    avi.smoothToHide();
                    avibackground.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equals("false")) {
                            Constant.ErrorToast(User_Detail_Activity.this, message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });




    }





}
