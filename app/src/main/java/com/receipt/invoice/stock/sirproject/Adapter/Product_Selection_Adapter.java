package com.receipt.invoice.stock.sirproject.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.receipt.invoice.stock.sirproject.Model.Product_list;
import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;

/**
 * Created by Fawad on 5/6/2020.
 */
//Product_Selection_Adapter

public class Product_Selection_Adapter extends RecyclerView.Adapter<Product_Selection_Adapter.ViewHolderForCat> {

    private Context mcontext ;
    ArrayList<Product_list> mlist=new ArrayList<>();
    Dialog mybuilder;
    TextView select;
    EditText edquantity;
    /*List<String> mnames=new ArrayList<>();
    List<String> maddresses=new ArrayList<>();
    List<Integer> mimages=new ArrayList<>();*/

    public Product_Selection_Adapter(Context mcontext , ArrayList<Product_list>list){
        this.mcontext = mcontext;
        mlist=list;

    }


    @NonNull
    @Override
    public Product_Selection_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_listing_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Product_Selection_Adapter.ViewHolderForCat viewHolderForCat, final int i) {

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
        final String quantity = product_list.getQuantity();
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

        viewHolderForCat.productstock.setText(mninimum);


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
            viewHolderForCat.productcurrency.setText(product_price.replace(".00",""));

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
        Glide.with(mcontext)
                .load(product_list.getProduct_image_path()+product_list.getProduct_image())
                .apply(options)
                .into(viewHolderForCat.image);


       /* viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,Product_Detail_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("product_id",product_id);
                mcontext.startActivity(intent);
            }
        });*/

  viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuantityDialog();
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

    public void QuantityDialog() {


        mybuilder = new Dialog(mcontext);
        mybuilder.setContentView(R.layout.quantity_dailog);
        mybuilder.setCancelable(false);
        select = mybuilder.findViewById(R.id.select);
        edquantity = mybuilder.findViewById(R.id.edquantity);
        mybuilder.show();
        mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = mybuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.transparent);

    }
}
