package com.sirapp.Report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sirapp.R;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolderForCat> {

    //InvoiceCallBack invoiceCallBack;

    private Context mcontext;
    ArrayList<String> mlist = new ArrayList<>();


    public ReportAdapter(Context mcontext, ArrayList<String> list) {
        this.mcontext = mcontext;
        mlist = list;
    }






    @NonNull
    @Override
    public ReportAdapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_report, viewGroup, false);
        ReportAdapter.ViewHolderForCat viewHolderForCat = new ReportAdapter.ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ReportAdapter.ViewHolderForCat viewHolderForCat, final int i) {

        viewHolderForCat.textViewName.setText(""+mlist.get(i));

        viewHolderForCat.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportActivity)mcontext).onClickAdapter(i);
            }
        });

    }




    @Override
    public int getItemCount() {
        return mlist.size();
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


        TextView textViewName ,invoicenbtxt,invoiceduetxt,invoicepricetxt,statustxt,invoicestatus,invoicesttsuspend;

        TextView textViewLeftPaidUnPaid;

        View view;

        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.customernamtxt);

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
        mlist = list;
        notifyDataSetChanged();
    }
}