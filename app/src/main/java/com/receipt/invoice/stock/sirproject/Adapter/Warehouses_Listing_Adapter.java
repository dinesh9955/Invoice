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
import com.receipt.invoice.stock.sirproject.Model.Warehouse_list;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

public class Warehouses_Listing_Adapter extends RecyclerView.Adapter<Warehouses_Listing_Adapter.ViewHolderForCat> {

    private Context mcontext ;
    ArrayList<Warehouse_list> mlist=new ArrayList<>();


    public Warehouses_Listing_Adapter(Context mcontext , ArrayList<Warehouse_list> list){
        this.mcontext = mcontext;
        mlist=list;

    }


    @NonNull
    @Override
    public Warehouses_Listing_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.warehouse_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Warehouses_Listing_Adapter.ViewHolderForCat viewHolderForCat, final int i) {


        Warehouse_list warehouse_list = mlist.get(i);


        String warehouse_id = warehouse_list.getWarehouse_id();
        String warehouse_name = warehouse_list.getWarehouse_name();
        String warehouse_address = warehouse_list.getWarehouse_address();
        String warehouse_image = warehouse_list.getWarehouse_image();

        if (warehouse_name.equals("") || warehouse_name.equals("null"))
        {
            viewHolderForCat.companyname.setText("");
        }
        else
        {
            viewHolderForCat.companyname.setText(warehouse_name);
        }
        if (warehouse_address.equals("") || warehouse_address.equals("null"))
        {
            viewHolderForCat.address.setText("");
        }
        else
        {
            viewHolderForCat.address.setText(warehouse_address);
        }

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.app_icon);
        GlideApp.with(mcontext)
                .load(warehouse_image)
                .apply(options)
                .into(viewHolderForCat.image);

        viewHolderForCat.stock.setVisibility(View.GONE);



    }

    @Override
    public int getItemCount() {
        return mlist.size();
        //return 2;
    }


    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        TextView companyname,stock,address,details;
        RoundedImageView image;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            companyname = itemView.findViewById(R.id.companyname);
            stock = itemView.findViewById(R.id.stock);
            address = itemView.findViewById(R.id.invoicepricetxt);
            details = itemView.findViewById(R.id.details);
            image = itemView.findViewById(R.id.image);

            companyname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            stock.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            address.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            details.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Medium.ttf"));

        }

    }
    public void updateList(List<Warehouse_list> list){
        mlist = (ArrayList<Warehouse_list>) list;
        notifyDataSetChanged();
    }
}