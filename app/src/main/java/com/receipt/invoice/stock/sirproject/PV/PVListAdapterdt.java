package com.receipt.invoice.stock.sirproject.PV;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.Model.InvoiceData;
import com.receipt.invoice.stock.sirproject.PO.POListAdapterdt;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.Utility;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PVListAdapterdt extends RecyclerView.Adapter<PVListAdapterdt.ViewHolderForCat> {

    //InvoiceCallBack invoiceCallBack;

    private Context mcontext;
    ArrayList<InvoiceData> mlist = new ArrayList<>();


    public PVListAdapterdt(Context mcontext, ArrayList<InvoiceData> list) {
        this.mcontext = mcontext;
        mlist = list;

    }






    @NonNull
    @Override
    public PVListAdapterdt.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoicedetail, viewGroup, false);
        PVListAdapterdt.ViewHolderForCat viewHolderForCat = new PVListAdapterdt.ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PVListAdapterdt.ViewHolderForCat viewHolderForCat, final int i) {


        InvoiceData company_list = mlist.get(i);

        final String customer_name = company_list.getInvoicustomer_name();
        final String invoice_no = company_list.getInvoice_nobdt();
        final String invoicedue_date = company_list.getInvoicedue_date();
        final String invoicetotlaprice = company_list.getInvoicetotlaprice();
        final String strstatus = company_list.getInvocestatus();
        final String is_viewed = company_list.getIs_viewed();



        if (customer_name.equals("") && customer_name.equals("null")) {
            viewHolderForCat.customernamtxt.setText("");
        } else {
            viewHolderForCat.customernamtxt.setText(customer_name);
        }
        if (invoice_no.equals("") && invoice_no.equals("null")) {
            viewHolderForCat.invoicenbtxt.setText("");
        } else {
            viewHolderForCat.invoicenbtxt.setText(""+invoice_no);
        }

        if (invoicedue_date.equals("") && invoicedue_date.equals("null")) {
            viewHolderForCat.invoiceduetxt.setText("");
        } else {
            viewHolderForCat.invoiceduetxt.setText("| Date: "+invoicedue_date);
        }

        if (invoicetotlaprice.equals("") && invoicetotlaprice.equals("null")) {
            viewHolderForCat.invoicepricetxt.setText("");
        } else {
           // DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
            double stratingvalue = Double.parseDouble(invoicetotlaprice);
            SavePref pref = new SavePref();
            pref.SavePref(mcontext);
            int numberPostion = pref.getNumberFormatPosition();
            viewHolderForCat.invoicepricetxt.setText(Utility.getPatternFormat(""+numberPostion, stratingvalue)+" "+company_list.getPayment_currency());
        }

        if (strstatus.equals("") && strstatus.equals("null")) {
            viewHolderForCat.statustxt.setText("");
        } else {
            if (strstatus.equals("2")) {
                viewHolderForCat.statustxt.setText("Paid");
                viewHolderForCat.statustxt.setTextColor(Color.parseColor("#008000"));

            }else {
                viewHolderForCat.statustxt.setText("Unpaid");
                viewHolderForCat.statustxt.setTextColor(Color.parseColor("#FF0000"));
            }
        }

        if (is_viewed.equalsIgnoreCase("0")) {
            viewHolderForCat.invoicesttsuspend.setText("Pending");
            viewHolderForCat.invoicesttsuspend.setTextColor(Color.parseColor("#FF0000"));
        }else{
            viewHolderForCat.invoicesttsuspend.setText("Seen");
            viewHolderForCat.invoicesttsuspend.setTextColor(Color.parseColor("#008000"));
        }

        viewHolderForCat.invoicestatus.setVisibility(View.GONE);
        viewHolderForCat.invoicesttsuspend.setVisibility(View.GONE);

        viewHolderForCat.statustxt.setVisibility(View.GONE);


//
//                String invoiceStatus = "0";
//                String invoice_useriddt = "";
//
//                String invoice_text = "0";
//                int invoice_color = 0;
//
//                try {
//                    invoiceStatus = mlist.get(i).getInvocestatus();
//                    invoice_useriddt = mlist.get(i).getInvoice_userid();
//                    Log.e("UinvoiceStatus", invoiceStatus);
//
//                    if (invoiceStatus.equals("1")) {
//                        invoice_text = "Mark as unpaid";
//                        invoice_color = R.color.red;
//                        invoiceStatus = "2";
//
//
//                    } else {
//                        invoice_text = "Mark as Paid";
//                        invoice_color = R.color.green;
//                        invoiceStatus = "1";
//
//                    }
//
//                    //UpdateInvoiceStatusmethodh(invoiceStatus);
//                } catch (Exception e) {
//                    Log.e("MainActivity", e.getMessage());
//                }
//
//
//        String finalInvoiceStatus = invoiceStatus;
//        viewHolderForCat.textViewLeftPaidUnPaid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                notifyDataSetChanged();
//                invoiceCallBack.callBack(finalInvoiceStatus);
//
//            }
//        });

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


        TextView customernamtxt,invoicenbtxt,invoiceduetxt,invoicepricetxt,statustxt,invoicestatus,invoicesttsuspend;

        TextView textViewLeftPaidUnPaid;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

//            SwipeLayout swipeLayout = (SwipeLayout)itemView.findViewById(R.id.swipe);
//            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
//            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.left_view));
//            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewById(R.id.right_view));
//
//            textViewLeftPaidUnPaid = itemView.findViewById(R.id.textdelete1);

            customernamtxt = itemView.findViewById(R.id.customernamtxt);
            invoicenbtxt = itemView.findViewById(R.id.invoicenbtxt);
            invoiceduetxt = itemView.findViewById(R.id.invoiceduetxt);
            invoicepricetxt = itemView.findViewById(R.id.invoicepricetxt);
            statustxt = itemView.findViewById(R.id.statustxt);
            invoicestatus = itemView.findViewById(R.id.invoicestatus);
            invoicesttsuspend = itemView.findViewById(R.id.invoicesttsuspend);

            invoicestatus.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            invoicesttsuspend.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            invoicenbtxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            invoiceduetxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            invoicepricetxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Bold.otf"));
            statustxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Bold.otf"));

        }

    }
    public void updateList(ArrayList<InvoiceData> list){
        mlist = list;
        notifyDataSetChanged();
    }
}