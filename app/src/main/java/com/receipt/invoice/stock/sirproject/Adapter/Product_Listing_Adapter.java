package com.receipt.invoice.stock.sirproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.receipt.invoice.stock.sirproject.Details.Product_Detail_Activity;
import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.Model.Product_list;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.GlideApp;
import com.receipt.invoice.stock.sirproject.Utils.Utility;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Fawad on 4/15/2020.
 */


public class Product_Listing_Adapter extends RecyclerView.Adapter<Product_Listing_Adapter.ViewHolderForCat> {

    private Context mcontext ;
    ArrayList<Product_list> mlist=new ArrayList<>();
    /*List<String> mnames=new ArrayList<>();
    List<String> maddresses=new ArrayList<>();
    List<Integer> mimages=new ArrayList<>();*/

    public Product_Listing_Adapter(Context mcontext , ArrayList<Product_list>list){
        this.mcontext = mcontext;
        mlist=list;

    }


    @NonNull
    @Override
    public Product_Listing_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_listing_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Product_Listing_Adapter.ViewHolderForCat viewHolderForCat, final int i) {

        /*viewHolderForCat.productname.setText(mproductname.get(i));
       *//* viewHolderForCat.name.setText(mnames.get(i));
        viewHolderForCat.address.setText(maddresses.get(i));
        viewHolderForCat.image.setImageResource(mimages.get(i));*//*
       viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(mcontext,Product_Detail_Activity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
               mcontext.startActivity(intent);
           }
       });*/

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
        String mninimum = product_list.getMinimum();

        if (product_name.equals("") || product_name.equals("null"))
        {
            viewHolderForCat.productname.setText("");
        }
        else
        {
            viewHolderForCat.productname.setText(product_name);
        }

        viewHolderForCat.productcurrencyunit.setText(product_list.getCurrency_code());

        double doubleMinimum = 0.0;
        try{
            doubleMinimum = Double.parseDouble(mninimum);
        }catch (Exception e){

        }
        //DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        SavePref pref = new SavePref();
        pref.SavePref(mcontext);

        //double stratingvalue = Double.parseDouble(invoiceModelArrayList.get(position).getTotal());
        int numberPostion = pref.getNumberFormatPosition();
       // Log.e(TAG, "numberPostionAA "+numberPostion);
        String stringFormat = Utility.getPatternFormat(""+numberPostion, doubleMinimum);

        viewHolderForCat.productstock.setText(""+stringFormat);


        //reorder
/*
        viewHolderForCat.productstock.setText(mninimum);

        if (Integer.parseInt(quantity) > Integer.parseInt(mninimum)){
            viewHolderForCat.productstock.setText(" In Stock");
            viewHolderForCat.productstock.setTextColor(mcontext.getResources().getColor(R.color.green));
        }
        else{
            viewHolderForCat.productstock.setText(" Re Order");
            viewHolderForCat.productstock.setTextColor(mcontext.getResources().getColor(R.color.red));
        }
*/



        if (product_price.equals("") || product_price.equals("null"))
        {
            viewHolderForCat.productcurrency.setText("");
        }
        else
        {
            double vc = Double.parseDouble(product_price);
            String stringFormatproductcurrency = Utility.getPatternFormat(""+numberPostion, vc);
            viewHolderForCat.productcurrency.setText(stringFormatproductcurrency);

        }

        if (product_category.equals("") || product_category.equals("null"))
        {
            viewHolderForCat.productcategory.setText("Category:");
        }
        else
        {
            viewHolderForCat.productcategory.setText("Category:"+product_category);
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
                Intent intent = new Intent(mcontext,Product_Detail_Activity.class);
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


        TextView productname,productcategory,status,productstock,productcurrency,productcurrencyunit;
        RoundedImageView image;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.productname);
            productcategory = itemView.findViewById(R.id.productcategory);
            status = itemView.findViewById(R.id.status);
            productstock = itemView.findViewById(R.id.productstock);
            productcurrency = itemView.findViewById(R.id.productcurrency);
            productcurrencyunit = itemView.findViewById(R.id.productcurrencyunit);
            image = itemView.findViewById(R.id.image);

            productname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            productcategory.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            status.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            productstock.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            productcurrency.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Bold.ttf"));
            productcurrencyunit.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Light.ttf"));

        }

    }
    public void updateList(ArrayList<Product_list> list){
        mlist = list;
        notifyDataSetChanged();
    }
}