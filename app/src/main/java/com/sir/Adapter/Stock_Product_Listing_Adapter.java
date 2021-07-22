package com.sir.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sir.Details.Stock_Products_Detail;
import com.sir.Invoice.SavePref;
import com.sir.Model.Product_list;
import com.sir.R;
import com.sir.Utils.GlideApp;
import com.sir.Utils.Utility;

import java.util.ArrayList;


public class Stock_Product_Listing_Adapter extends RecyclerView.Adapter<Stock_Product_Listing_Adapter.ViewHolderForCat> {

    private Context mcontext ;
    ArrayList<Product_list> mlist=new ArrayList<>();
    /*List<String> mnames=new ArrayList<>();
    List<String> maddresses=new ArrayList<>();
    List<Integer> mimages=new ArrayList<>();*/

    public Stock_Product_Listing_Adapter(Context mcontext , ArrayList<Product_list>list){
        this.mcontext = mcontext;
        mlist=list;

    }


    @NonNull
    @Override
    public Stock_Product_Listing_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_product_listing_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Stock_Product_Listing_Adapter.ViewHolderForCat viewHolderForCat, final int i) {


        Product_list product_list = mlist.get(i);

        String company_id = product_list.getCompany_id();
        final String product_id = product_list.getProduct_id();
        String product_name = product_list.getProduct_name();
        String product_image = product_list.getProduct_image();
        String product_image_path = product_list.getProduct_image_path();
        String product_price = product_list.getProduct_price();
        String product_status = product_list.getProduct_status();
        String product_description = product_list.getProduct_description();
        String product_taxable = product_list.getProduct_taxable();
        String product_category = product_list.getProduct_category();

        String quantity = product_list.getQuantity();
        String value = product_list.getProduct_value();
        String mninimum = product_list.getMinimum();
        String currency_symbol = product_list.getCurrency_symbol();


        //name
        if (product_name.equals("") || product_name.equals("null"))
        {
            viewHolderForCat.productname.setText("");
        }
        else
        {
            viewHolderForCat.productname.setText(product_name);
        }

        //quantity
        if (quantity.equals("") || quantity.equals("null"))
        {
            viewHolderForCat.productquantity.setText(mcontext.getString(R.string.invoice_Quantity)+"");
        }
        else
        {
            //DecimalFormat formatter = new DecimalFormat("##,##,##,##0");

            double vz = Double.parseDouble(quantity);

            SavePref pref = new SavePref();
            pref.SavePref(mcontext);
            int numberPostion = pref.getNumberFormatPosition();

            String stringFormat = Utility.getPatternFormat(""+numberPostion, vz);

           // grosstotal.setText(formatter.format(stratingvalue) +cruncycode);
            viewHolderForCat.productquantity.setText(mcontext.getString(R.string.invoice_Quantity)+" "+stringFormat);
        }

        //value
        if (value.equals("") || value.equals("null"))
        {
            viewHolderForCat.productvalue.setText(mcontext.getString(R.string.invoice_Value)+"");
        }
        else
        {
           // DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

            double vz = Double.parseDouble(value);

            SavePref pref = new SavePref();
            pref.SavePref(mcontext);
            int numberPostion = pref.getNumberFormatPosition();

            String stringFormat = Utility.getPatternFormat(""+numberPostion, vz);

            if (currency_symbol.equals("") || currency_symbol.equals("null")){
                viewHolderForCat.productvalue.setText(mcontext.getString(R.string.invoice_Value)+" "+stringFormat+" "+product_list.getCurrency_code());
            }
            else {
                viewHolderForCat.productvalue.setText(mcontext.getString(R.string.invoice_Value)+" "+stringFormat+" "+currency_symbol);
            }
        }

        viewHolderForCat.productstock.setText(mninimum);

        //reorder

        if (quantity.equals("") || quantity.equals("null"))
        {
            viewHolderForCat.productquantity.setText(mcontext.getString(R.string.invoice_Quantity)+" ");
            viewHolderForCat.statusvalue.setText(mcontext.getString(R.string.invoice_Reorder)+"");
            viewHolderForCat.statusvalue.setTextColor(mcontext.getResources().getColor(R.color.green));

        }
        else
        {
            if (Integer.parseInt(quantity) > Integer.parseInt(mninimum)){

                viewHolderForCat.statusvalue.setText(mcontext.getString(R.string.invoice_InStock)+"");
                viewHolderForCat.statusvalue.setTextColor(mcontext.getResources().getColor(R.color.green));

            }
            else{


                viewHolderForCat.statusvalue.setText(mcontext.getString(R.string.invoice_Reorder)+"");
                viewHolderForCat.statusvalue.setTextColor(mcontext.getResources().getColor(R.color.red));


            }
        }


        //currency code
        viewHolderForCat.productcurrencyunit.setText(product_list.getCurrency_code());

        //price
        if (product_price.equals("") || product_price.equals("null"))
        {
            viewHolderForCat.productcurrency.setText("");
        }
        else
        {
           // DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
            double vz = Double.parseDouble(product_price);
            SavePref pref = new SavePref();
            pref.SavePref(mcontext);
            int numberPostion = pref.getNumberFormatPosition();

            String stringFormat = Utility.getPatternFormat(""+numberPostion, vz);
            viewHolderForCat.productcurrency.setText(""+stringFormat);

        }

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.app_icon);
        GlideApp.with(mcontext)
                .load(product_list.getProduct_image_path()+product_list.getProduct_image())
                .apply(options)
                .into(viewHolderForCat.image);


        viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, Stock_Products_Detail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("product_id",product_id);
                mcontext.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return mlist.size();
        //return 2;
    }


    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        TextView productname,productquantity,status,productvalue,productstock,productcurrency,productcurrencyunit,statuss,statusvalue;
        RoundedImageView image;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.productname);
            productquantity = itemView.findViewById(R.id.productquantity);
            status = itemView.findViewById(R.id.status);
            productstock = itemView.findViewById(R.id.productstock);
            productcurrency = itemView.findViewById(R.id.productcurrency);
            productcurrencyunit = itemView.findViewById(R.id.productcurrencyunit);
            image = itemView.findViewById(R.id.image);
            productvalue = itemView.findViewById(R.id.productvalue);
            statuss = itemView.findViewById(R.id.statuss);
            statusvalue = itemView.findViewById(R.id.statusvalue);

            productname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            productquantity.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            productvalue.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            status.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            productstock.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            productcurrency.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Bold.ttf"));
            productcurrencyunit.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Medium.ttf"));
            statuss.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            statusvalue.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));


        }

    }

    public void updateList(ArrayList<Product_list> list){
        mlist = list;
        notifyDataSetChanged();
    }
}