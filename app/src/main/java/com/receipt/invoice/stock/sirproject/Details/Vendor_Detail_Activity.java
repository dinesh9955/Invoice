package com.receipt.invoice.stock.sirproject.Details;

import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.GlideApp;
import com.wang.avi.AVLoadingIndicatorView;

public class Vendor_Detail_Activity extends BaseActivity {
    TextView name,contactperson,email,phone,website,mobile,address;
    RoundedImageView image;
    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    String vendor_id,vendor_name,vendor_contact_person,vendor_image,vendor_email,vendor_phone,vendor_mobile,vendor_website,vendor_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__detail_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Constant.toolbar(Vendor_Detail_Activity.this,"Details");
        Constant.bottomNav(Vendor_Detail_Activity.this,-1);

        FindByIds();
        setFonts();
        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);



        if (getIntent().hasExtra("vendor_id"));
        {
            vendor_id = getIntent().getStringExtra("vendor_id");
            vendor_name = getIntent().getStringExtra("vendor_name");
            vendor_contact_person = getIntent().getStringExtra("vendor_contact_person");
            vendor_image = getIntent().getStringExtra("image_path");
            vendor_email = getIntent().getStringExtra("vendor_email");
            vendor_phone = getIntent().getStringExtra("vendor_phone");
            vendor_mobile = getIntent().getStringExtra("vendor_mobile");
            vendor_website = getIntent().getStringExtra("vendor_website");
            vendor_address = getIntent().getStringExtra("vendor_address");

            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(R.drawable.app_icon);
            GlideApp.with(Vendor_Detail_Activity.this)
                    .load(vendor_image)
                    .apply(options)
                    .into(image);
            if (vendor_name.equals("") || vendor_name.equals("null"))
            {
                name.setText("");
            }
            else
            {
                name.setText(vendor_name);
            }


            if (vendor_contact_person.equals("") || vendor_contact_person.equals("null"))
            {
                contactperson.setText("");
            }
            else
            {
                contactperson.setText(vendor_contact_person);
            }

            if (vendor_email.equals("") || vendor_email.equals("null"))
            {
                email.setText("");
            }
            else
            {
                email.setText(vendor_email);
            }
            if (vendor_phone.equals("") || vendor_phone.equals("null"))
            {
                phone.setText("");
            }
            else
            {
                phone.setText(vendor_phone);
            }
            if (vendor_mobile.equals("") || vendor_mobile.equals("null"))
            {
                mobile.setText("");
            }
            else
            {
                mobile.setText(vendor_mobile);
            }
            if (vendor_website.equals("") || vendor_website.equals("null"))
            {
                website.setText("");
            }
            else
            {
                website.setText(vendor_website);
            }
            if (vendor_address.equals("") || vendor_address.equals("null"))
            {
                address.setText("");
            }
            else
            {
                address.setText(vendor_address);
            }

        }





    }

    private void FindByIds(){
        name = findViewById(R.id.name);
        contactperson = findViewById(R.id.contactperson);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        website = findViewById(R.id.website);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.invoicepricetxt);
        image = findViewById(R.id.image);

    }

    private void setFonts(){

        name.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Medium.otf"));
        contactperson.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Medium.otf"));
        phone.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Regular.otf"));
        email.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Regular.otf"));
        website.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Regular.otf"));
        mobile.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Regular.otf"));
        address.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Regular.otf"));

    }
}
