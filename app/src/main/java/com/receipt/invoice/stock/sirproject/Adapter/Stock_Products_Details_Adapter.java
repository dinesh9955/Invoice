package com.receipt.invoice.stock.sirproject.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.receipt.invoice.stock.sirproject.Model.Stock_Products;
import com.receipt.invoice.stock.sirproject.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Stock_Products_Details_Adapter extends RecyclerView.Adapter<Stock_Products_Details_Adapter.ViewHolderForCat> {

    private Context mcontext ;
    ArrayList<Stock_Products> mlist = new ArrayList<>();

    public Stock_Products_Details_Adapter(Context mcontext , ArrayList<Stock_Products> list){
        this.mcontext = mcontext;
        mlist=list;
    }

    @NonNull
    @Override
    public Stock_Products_Details_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_product_detail_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Stock_Products_Details_Adapter.ViewHolderForCat viewHolderForCat, final int i) {

        Stock_Products stock_products = mlist.get(i);

        if (stock_products.getWarehouse().equals("") || stock_products.getWarehouse().equals("null")){
            viewHolderForCat.companyname.setText("");
        }
        else {
            viewHolderForCat.companyname.setText(stock_products.getWarehouse());
        }

        if (stock_products.getTotal_quantity().equals("") || stock_products.getTotal_quantity().equals("null")){
            viewHolderForCat.stock.setText("Quantity: ");

        }
        else {
            DecimalFormat formatter = new DecimalFormat("##,##,##,##0");
            double vz = Double.parseDouble(stock_products.getTotal_quantity());
            viewHolderForCat.stock.setText("Quantity: "+formatter.format(vz));
        }

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.app_icon);
        Glide.with(mcontext)
                .load(stock_products.getImagepath()+stock_products.getImage())
                .apply(options)
                .into(viewHolderForCat.image);


    }

    @Override
    public int getItemCount() {
        return mlist.size();
        //return 2;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        TextView companyname,stock;
        RoundedImageView image;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            companyname = itemView.findViewById(R.id.companyname);
            stock = itemView.findViewById(R.id.stock);
            image = itemView.findViewById(R.id.image);

            companyname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));


        }

    }

    public void updateList(ArrayList<Stock_Products> list){
        mlist = list;
        notifyDataSetChanged();
    }
}
