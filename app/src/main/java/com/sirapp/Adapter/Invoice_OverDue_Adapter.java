package com.sirapp.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.sirapp.Constant.Constant.createDialogOpenClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sirapp.Constant.Constant;
import com.sirapp.Home.Home_Activity;
import com.sirapp.Home.Model.InvoiceModel;
import com.sirapp.Invoice.InvoiceActivity;
import com.sirapp.R;
import com.sirapp.Utils.GlideApp;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fawad on 4/1/2020.
 */


public class Invoice_OverDue_Adapter extends RecyclerView.Adapter<Invoice_OverDue_Adapter.ViewHolderForCat> {

    private Activity mcontext ;

    public ArrayList<InvoiceModel> invoiceModelArrayList = new ArrayList<>() ;


    public Invoice_OverDue_Adapter(Activity mcontext){
        this.mcontext = mcontext;
    }

    public void setData(ArrayList<InvoiceModel> newList) {
        this.invoiceModelArrayList.clear();
        this.invoiceModelArrayList.addAll(newList);
        notifyDataSetChanged();
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
        GlideApp.with(mcontext).load(invoiceModelArrayList.get(i).customer_logo).placeholder(R.drawable.app_icon).into(viewHolderForCat.image);
        viewHolderForCat.name.setText(invoiceModelArrayList.get(i).getInvoice_no());
        viewHolderForCat.companyname.setText(invoiceModelArrayList.get(i).getCustomer_name());

        viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(mcontext,"Coming Soon",Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = mcontext.getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);

                if(preferences.getString(Constant.ROLE,"").equalsIgnoreCase("USER")){
                    Intent intent = new Intent(mcontext, InvoiceActivity.class);
                    intent.putExtra("keyList" , "keyList");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);
                }else{
                    if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
                        Intent intent = new Intent(mcontext, InvoiceActivity.class);
                        intent.putExtra("keyList" , "keyList");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mcontext.startActivity(intent);
                    }else{
                        if(preferences.getString(Constant.INVOICE,"").equalsIgnoreCase("1")){
                            Intent intent = new Intent(mcontext, InvoiceActivity.class);
                            intent.putExtra("keyList" , "keyList");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            mcontext.startActivity(intent);
                        }else{
                            createDialogOpenClass(mcontext);
                        }
                    }

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return invoiceModelArrayList.size();
        //return 2;
    }


    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        CircleImageView image;
        TextView invoiceoverduetxt,name,companyname;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            invoiceoverduetxt = itemView.findViewById(R.id.invoiceoverduetxt);
            companyname = itemView.findViewById(R.id.companyname);
            image = itemView.findViewById(R.id.image);

            name.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            invoiceoverduetxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Bold.otf"));
            companyname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));

        }
    }
}
