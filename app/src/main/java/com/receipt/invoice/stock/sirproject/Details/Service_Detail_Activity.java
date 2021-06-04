package com.receipt.invoice.stock.sirproject.Details;

import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
//import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.Utility;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DecimalFormat;

public class Service_Detail_Activity extends BaseActivity {


    TextView service_id,servicename,serviceprice,servicedepart,servicedescription,servicetaxable,measurement;

    private AVLoadingIndicatorView avi;
    ImageView avibackground;

    String service_name,service_price,service_description,service_taxable,service_category_service_price_unit;
    String currencycode="";
    String service_category="";
    String measurement_unit="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__detail_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Constant.toolbar(Service_Detail_Activity.this,"Details");
        Constant.bottomNav(Service_Detail_Activity.this,-1);

        avi = findViewById(R.id.avi);
        avibackground = findViewById(R.id.avibackground);
        FindByIds();
        setFonts();

        if (getIntent().hasExtra("service_id"))
        {
            service_name = getIntent().getStringExtra("service_name");
            service_price = getIntent().getStringExtra("service_price");
            service_description = getIntent().getStringExtra("service_description");
            service_taxable = getIntent().getStringExtra("service_taxable");
            currencycode = getIntent().getStringExtra("currencycode");
            service_category = getIntent().getStringExtra("service_category");
            measurement_unit = getIntent().getStringExtra("measurement_unit");


            if (service_name.equals("") || service_name.equals("null"))
            {
                servicename.setText("");
            }
            else
            {
                servicename.setText(service_name);
            }


            if (measurement_unit.equals("") || measurement_unit.equals("null"))
            {
                measurement.setText("");
            }
            else
            {
                measurement.setText("Measurement Unit: " +measurement_unit);
            }


            if (service_category.equals("") || service_category.equals("null"))
            {
                servicedepart.setText("");
            }
            else
            {
                servicedepart.setText(service_category);
            }



            if (service_price.equals("") || service_price.equals("null"))
            {
                serviceprice.setText("");
            }
            else
            {
                double vc = Double.parseDouble(service_price);
              //  DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

                String stringFormat = Utility.getPatternFormat(""+numberPostion, vc);

                serviceprice.setText(stringFormat+" "+currencycode);
            }

            if (service_description.equals("") || service_description.equals("null"))
            {
                servicedescription.setText("");
            }
            else
            {
                servicedescription.setText(service_description);
            }

            if (service_taxable.equals("") || service_taxable.equals("null"))
            {
                servicetaxable.setText("");
            }
            else if (service_taxable.equals("0"))
            {
                servicetaxable.setText("Taxable: No");
            }
            else if (service_taxable.equals("1"))
            {
                servicetaxable.setText("Taxable: Yes");
            }


        }


    }
    private void FindByIds(){
        servicename = findViewById(R.id.servicename);
        serviceprice = findViewById(R.id.serviceprice);
        servicedepart = findViewById(R.id.servicedepart);
        servicedescription = findViewById(R.id.servicedescription);
        servicetaxable = findViewById(R.id.servicetaxable);
        measurement = findViewById(R.id.measurement);

    }
    private void setFonts(){

        servicename.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Medium.otf"));
        serviceprice.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Medium.otf"));
        servicedepart.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Regular.otf"));
        servicetaxable.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Regular.otf"));
        servicedescription.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Regular.otf"));
        measurement.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/AzoSans-Regular.otf"));

    }
}
