package com.receipt.invoice.stock.sirproject.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fawad on 4/1/2020.
 */


public class Invoice_OverDue_Adapter extends RecyclerView.Adapter<Invoice_OverDue_Adapter.ViewHolderForCat> {

    private Context mcontext ;
    List<String> mnames=new ArrayList<>();
    List<String> mquantity=new ArrayList<>();
    List<String> mprice=new ArrayList<>();

    public Invoice_OverDue_Adapter(Context mcontext , ArrayList<String> names,ArrayList<String> quantity){
        this.mcontext = mcontext;
        mnames=names;
        mquantity=quantity;
    }


    @NonNull
    @Override
    public Invoice_OverDue_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoiceoverdue_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Invoice_OverDue_Adapter.ViewHolderForCat viewHolderForCat, final int i) {

        viewHolderForCat.name.setText(mnames.get(i));
        viewHolderForCat.companyname.setText(mquantity.get(i));

        viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext,"Coming Soon",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mnames.size();
        //return 2;
    }


    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        RoundedImageView image;
        TextView invoiceoverduetxt,name,companyname;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            invoiceoverduetxt = itemView.findViewById(R.id.invoiceoverduetxt);
            companyname = itemView.findViewById(R.id.companyname);
            image = itemView.findViewById(R.id.image);

            name.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            invoiceoverduetxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Bold.otf"));
            companyname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));

        }
    }
}
