package com.receipt.invoice.stock.sirproject.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Report.ReportActivity;
import com.receipt.invoice.stock.sirproject.Report.ReportAdapter;
import com.receipt.invoice.stock.sirproject.Report.ReportViewActivity;

import java.util.ArrayList;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolderForCat> {

    //InvoiceCallBack invoiceCallBack;

    private Context mcontext;

    ArrayList<Integer> arrayListIcons = new ArrayList<>();
    ArrayList<String> arrayListNames = new ArrayList<>();


    public SettingsAdapter(Context mcontext, ArrayList<Integer> arrayListIcons2, ArrayList<String> list) {
        this.mcontext = mcontext;
        arrayListNames = list;
        arrayListIcons = arrayListIcons2;
    }






    @NonNull
    @Override
    public SettingsAdapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_settings, viewGroup, false);
        SettingsAdapter.ViewHolderForCat viewHolderForCat = new SettingsAdapter.ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final SettingsAdapter.ViewHolderForCat viewHolderForCat, final int i) {


        viewHolderForCat.imageViewIcon.setImageResource(arrayListIcons.get(i));
        viewHolderForCat.textViewName.setText(""+arrayListNames.get(i));


        viewHolderForCat.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, WebShowActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                if(i == 8){
                    intent.putExtra("positionNext", i);
                    mcontext.startActivity(intent);
                } else if(i == 10){
                    intent.putExtra("positionNext", i);
                    mcontext.startActivity(intent);
                }else if(i == 11){
                    intent.putExtra("positionNext", i);
                    mcontext.startActivity(intent);
                }
            }
        });



    }




    @Override
    public int getItemCount() {
        return arrayListNames.size();
        //return 2;
    }
    //for removing redundancy
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    //end for removing redundancy

    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        ImageView imageViewIcon;
        TextView textViewName ;
        View view;

        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            imageViewIcon = itemView.findViewById(R.id.icon_setting);
            textViewName = itemView.findViewById(R.id.text_setting);

            view = itemView;

//            SwipeLayout swipeLayout = (SwipeLayout)itemView.findViewById(R.id.swipe);
//            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
//            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.left_view));
//            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewById(R.id.right_view));
//
//            textViewLeftPaidUnPaid = itemView.findViewById(R.id.textdelete1);

//            customernamtxt = itemView.findViewById(R.id.customernamtxt);
//            invoicenbtxt = itemView.findViewById(R.id.invoicenbtxt);
//            invoiceduetxt = itemView.findViewById(R.id.invoiceduetxt);
//            invoicepricetxt = itemView.findViewById(R.id.invoicepricetxt);
//            statustxt = itemView.findViewById(R.id.statustxt);
//            invoicestatus = itemView.findViewById(R.id.invoicestatus);
//            invoicesttsuspend = itemView.findViewById(R.id.invoicesttsuspend);
//
//            invoicestatus.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
//            invoicesttsuspend.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
//            invoicenbtxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
//            invoiceduetxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
//            invoicepricetxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Bold.otf"));
//            statustxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Bold.otf"));

        }

    }
    public void updateList(ArrayList<String> list){
        arrayListNames = list;
        notifyDataSetChanged();
    }
}