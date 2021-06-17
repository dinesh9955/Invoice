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

public class Customer_Detail_Activity extends BaseActivity {
    TextView name,contactperson,email,phone,website,mobile,address;
    RoundedImageView image;
    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    String customer_id,customer_name,customer_contact_person,customer_image,customer_email,customer_phone,customer_mobile,customer_website,customer_address;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__detail_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Constant.toolbar(Customer_Detail_Activity.this,getString(R.string.header_details));
        Constant.bottomNav(Customer_Detail_Activity.this,-1);

        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);
        FindByIds();
        setFonts();

        if (getIntent().hasExtra("customer_id"));
        {
            customer_id = getIntent().getStringExtra("customer_id");
            customer_name = getIntent().getStringExtra("customer_name");
            customer_contact_person = getIntent().getStringExtra("customer_contact_person");
            customer_image = getIntent().getStringExtra("customer_image");
            customer_email = getIntent().getStringExtra("customer_email");
            customer_phone = getIntent().getStringExtra("customer_phone");
            customer_mobile = getIntent().getStringExtra("customer_mobile");
            customer_website = getIntent().getStringExtra("customer_website");
            customer_address = getIntent().getStringExtra("customer_address");

            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(R.drawable.app_icon);
            GlideApp.with(Customer_Detail_Activity.this)
                    .load(customer_image)
                    .apply(options)
                    .into(image);
            if (customer_name.equals("") || customer_name.equals("null"))
            {
                name.setText("");
            }
            else
            {
                name.setText(customer_name);
            }


            if (customer_contact_person.equals("") || customer_contact_person.equals("null"))
            {
                contactperson.setText("");
            }
            else
            {
                contactperson.setText(customer_contact_person);
            }

            if (customer_email.equals("") || customer_email.equals("null"))
            {
                email.setText("");
            }
            else
            {
                email.setText(customer_email);
            }
            if (customer_phone.equals("") || customer_phone.equals("null"))
            {
                phone.setText("");
            }
            else
            {
                phone.setText(customer_phone);
            }
            if (customer_mobile.equals("") || customer_mobile.equals("null"))
            {
                mobile.setText("");
            }
            else
            {
                mobile.setText(customer_mobile);
            }
            if (customer_website.equals("") || customer_website.equals("null"))
            {
                website.setText("");
            }
            else
            {
                website.setText(customer_website);
            }
            if (customer_address.equals("") || customer_address.equals("null"))
            {
                address.setText("");
            }
            else
            {
                address.setText(customer_address);
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
