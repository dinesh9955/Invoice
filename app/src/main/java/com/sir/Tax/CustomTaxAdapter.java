package com.sir.Tax;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sir.Invoice.SavePref;
import com.sir.Model.Tax_List;
import com.sir.Utils.Utility;
import com.sir.R;

import java.util.ArrayList;

public class CustomTaxAdapter  extends RecyclerView.Adapter<CustomTaxAdapter.ViewHolderForCat> {

    private static final String TAG = "CustomTaxAdapter" ;
    private Context mcontext ;
    ArrayList<Tax_List> mlist=new ArrayList<>();


    CustomTaxAdapter.Callback callback;

    String taxname,taxvalue;

    String taxID= "";

    private int selectedPos = -100;
    public CustomTaxAdapter(Context mcontext , ArrayList<Tax_List> list, CustomTaxAdapter.Callback callback){
        this.mcontext = mcontext;
        mlist=list;
        this.callback = callback;

    }


    @NonNull
    @Override
    public CustomTaxAdapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragmenttaxtcustom , viewGroup , false);
        CustomTaxAdapter.ViewHolderForCat viewHolderForCat =new CustomTaxAdapter.ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomTaxAdapter.ViewHolderForCat viewHolderForCat, final int i) {


        final Tax_List service_list = mlist.get(i);
        taxname = service_list.getTax_name();
        taxvalue = service_list.getTax_rate();
        String typestr=service_list.getType();
        Log.e("type",typestr);

        Log.e(TAG, "AAAAA00 "+service_list.getTax_id()+"  "+taxID);

        if(taxID == service_list.getTax_id()){
            //viewHolderForCat.imgincome.setVisibility(View.VISIBLE);
            selectedPos = viewHolderForCat.getAdapterPosition();
        }else{
            selectedPos = -100;
          //  viewHolderForCat.imgincome.setVisibility(View.INVISIBLE);
        }

        if (taxname != null)
        {
            viewHolderForCat.txtincome.setText(taxname);
        }

        SavePref pref = new SavePref();
        pref.SavePref(mcontext);
        int numberPostion = pref.getNumberFormatPosition();
        double vc = 0.0;
        try {
            vc = Double.parseDouble(taxvalue);
        }catch (Exception e){

        }
        String stringFormat = Utility.getPatternFormat(""+numberPostion, vc);

        if (typestr.equals("P"))
        {
                viewHolderForCat.txtincomepercent.setText(stringFormat + " %");
        }
        else
            {
                viewHolderForCat.txtincomepercent.setText(stringFormat );
            }

        if(i == selectedPos)
        {
            viewHolderForCat.imgincome.setVisibility(View.VISIBLE);
        } else {
            viewHolderForCat.imgincome.setVisibility(View.INVISIBLE);
        }

        viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                taxID = service_list.getTax_id();
               String taxnamsts = service_list.getTax_name();
                String taxvlstr = service_list.getTax_rate();
                String typestr = service_list.getType();

                if(viewHolderForCat.getAdapterPosition() == selectedPos)
                {
                    selectedPos = -100;
                } else {
                    selectedPos = viewHolderForCat.getAdapterPosition();
                }


                Log.e("possition", String.valueOf(viewHolderForCat.getAdapterPosition()));
                notifyDataSetChanged();



                callback.onPostExecutecall3(taxID, taxnamsts,taxvlstr,typestr);



            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
        //return 2;
    }

    public void updateTaxSelect(String taxID) {
        this.taxID = taxID;
        Log.e(TAG, "taxID" + taxID);
        notifyDataSetChanged();
    }


    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        TextView txtincome,txtincomepercent;
        ImageView imgincome;

        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            imgincome = itemView.findViewById(R.id.imgincome);
            txtincome = itemView.findViewById(R.id.txtincome);
            txtincomepercent = itemView.findViewById(R.id.txtincomepercent);
            txtincomepercent.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            txtincome.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Bold.ttf"));


        }

    }
    public void updateList(ArrayList<Tax_List> list){
        mlist = list;
        notifyDataSetChanged();
    }



    public interface Callback{

        void onPostExecutecall3(String taxID, String taxnamst,String taxnamss,String type);
    }

}

