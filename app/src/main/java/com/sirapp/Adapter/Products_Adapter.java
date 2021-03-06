package com.sirapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.sirapp.Model.Product_List;
import com.sirapp.Invoice.SavePref;
import com.sirapp.Model.Product_list;
import com.sirapp.R;
import com.sirapp.Utils.Utility;

import java.util.ArrayList;

/**
 * Created by Fawad on 4/1/2020.
 */


public class Products_Adapter extends RecyclerView.Adapter<Products_Adapter.ViewHolderForCat> {

    private static final String TAG = "Products_Adapter" ;
    private Context mcontext ;
    //Selected List
    ArrayList<Product_list> mnames=new ArrayList<>();
    //Whole Product List
    ArrayList<Product_list> productlist=new ArrayList<>();
    ArrayList<String> tempQuantity=new ArrayList<>();

    ArrayList<String> editpriceval=new ArrayList<>();
    onItemClickListner onItemClickListner;
    String quentity;

    public int numberPostion = 0;

    public Products_Adapter(Context mcontext, ArrayList<Product_list> names,ArrayList<Product_list> tempList, onItemClickListner buttonListener, ArrayList<String> tempQuantity,ArrayList<String> editpriceval){
        this.mcontext = mcontext;
        mnames=tempList;
        productlist = names;
        this.onItemClickListner = buttonListener;
        this.tempQuantity=tempQuantity;
        this.editpriceval=editpriceval;
        SavePref pref = new SavePref();
        pref.SavePref(mcontext);
        numberPostion = pref.getNumberFormatPosition();
    }


    @NonNull
    @Override
    public Products_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final Products_Adapter.ViewHolderForCat viewHolderForCat, final int i) {

        //viewHolderForCat.name.setText(mnames.get(i));
        //viewHolderForCat.price.setText(mprice.get(i));



        Product_list product_service_list = mnames.get(i);
        String product_price = product_service_list.getProduct_price();
       String cruncycode=product_service_list.getCurrency_code();
       String pricsestr=product_service_list.getEditproductproce();
        double valueQuantity = 0;
        try{
            valueQuantity = Double.parseDouble(tempQuantity.get(i));
       }catch (Exception e){

       }

        double valuePrice = 0;
        try{
            valuePrice = Double.parseDouble(editpriceval.get(i));
        }catch (Exception e){

        }

//        viewHolderForCat.quantity.setText(""+editpriceval.get(i) + " "+cruncycode+" x "+valueQuantity);

        //DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");


//        String pQuantity = formatter.format(valueQuantity);
//        String pPrice = formatter.format(valuePrice);

        viewHolderForCat.quantity.setText(Utility.getPatternFormat(""+numberPostion, valuePrice) + " "+cruncycode+" x "+Utility.getPatternFormat(""+numberPostion, valueQuantity));

        String productid=product_service_list.getProduct_id();
        String productprice=product_service_list.getProduct_price();
        String product_quantity=product_service_list.getQuantity();

        Double productresul= Double.parseDouble(tempQuantity.get(i))*Double.parseDouble(editpriceval.get(i));
//        Log.e("productid detailed" ,productid);

       // DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        //String producttotatlps=formatter.format(productresul);
        String product_name = product_service_list.getProduct_name();
        if (product_name.equals("") || product_name.equals("null"))
        {
            viewHolderForCat.name.setText("");
        }
        else
        {
            viewHolderForCat.name.setText(product_name);
        }


        if (product_price.equals("") || product_price.equals("null") || product_price == null)
        {
            viewHolderForCat.price.setText("");
        }
        else
        {
            viewHolderForCat.price.setText(Utility.getPatternFormat(""+numberPostion, productresul)+cruncycode);
        }




        String product_description = product_service_list.getProduct_description();


        Log.e(TAG, "product_description: "+product_description);


        if (product_description.equals("") || product_description.equals("null"))
        {
            viewHolderForCat.productdistxt.setText("");
        }
        else
        {
            viewHolderForCat.productdistxt.setText(String.valueOf(product_description));
        }

        viewHolderForCat.deleteimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListner.onClick(i,"del");
            }
        });

        viewHolderForCat.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListner.onClick(i,"edit");
            }
        });
        viewHolderForCat.quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListner.onClick(i,"edit");
            }
        });
        viewHolderForCat.productdistxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListner.onClick(i,"edit");
            }
        });


    }

    @Override
    public int getItemCount() {
        return mnames.size();
        //return 2;
    }

    public void delete(int position) { //removes the row
        mnames.remove(position);

        notifyItemRemoved(position);

    }

    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        TextView name,price,quantity,productdistxt;
         ImageView deleteimg;

        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            productdistxt = itemView.findViewById(R.id.productdistxt);
            deleteimg=itemView.findViewById(R.id.deleteimg);

            name.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            price.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            quantity.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            productdistxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));

        }

    }
    public void setOnItemClickListner(Products_Adapter.onItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface onItemClickListner{
        void onClick(int str,String type);//pass your object types.
    }



    public void update(ArrayList<Product_list> mnames , ArrayList<Product_list> productlist, ArrayList<String> tempQuantity, ArrayList<String> editpriceval){
        this.mnames=mnames;
        this.productlist=productlist;
        this.tempQuantity=tempQuantity;
        this.editpriceval=editpriceval;
        notifyDataSetChanged();
    }

}
