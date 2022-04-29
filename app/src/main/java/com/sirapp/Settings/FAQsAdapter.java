package com.sirapp.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.sirapp.R;

public class FAQsAdapter extends RecyclerView.Adapter<FAQsAdapter.ViewHolderForCat> {
    private static final String TAG = "FAQsAdapter";

    //InvoiceCallBack invoiceCallBack;

    private Context mcontext;

   // ArrayList<String> arrayListNames = new ArrayList<>();

    String[] faqQuestionArray;
    String[] faqAnswerArray;

    private int selectedPos = -1;

    LanguageActivity activity;

    public FAQsAdapter(Context mcontext, String[] list) {
        this.mcontext = mcontext;
        faqQuestionArray = list;
        faqAnswerArray = mcontext.getResources().getStringArray(R.array.faqAnswerArray);
    }




    @NonNull
    @Override
    public FAQsAdapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_faqs, viewGroup, false);
        FAQsAdapter.ViewHolderForCat viewHolderForCat = new FAQsAdapter.ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final FAQsAdapter.ViewHolderForCat viewHolderForCat, final int i) {
        viewHolderForCat.textViewHeader.setText(""+faqQuestionArray[i]);

        viewHolderForCat.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolderForCat.nestedScrollView.getVisibility() == View.GONE) {
                    viewHolderForCat.nestedScrollView.setVisibility(View.VISIBLE);
                    viewHolderForCat.imageViewCheck.setImageResource(R.drawable.directional26);
                }else{
                    viewHolderForCat.nestedScrollView.setVisibility(View.GONE);
                    viewHolderForCat.imageViewCheck.setImageResource(R.drawable.directional25);
                }

                notifyDataSetChanged();
                setFoterData(i, viewHolderForCat.textViewName);

            }
        });



    }

    private void setFoterData(int position, TextView textView) {


        String value = faqAnswerArray[position];
        textView.setText(Html.fromHtml(value));
        textView.setMovementMethod(LinkMovementMethod.getInstance());

    }


    @Override
    public int getItemCount() {
        return faqQuestionArray.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateLanguagePosition(int languagePostion) {
        selectedPos = languagePostion;
        notifyDataSetChanged();
    }
    //end for removing redundancy

    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        ImageView imageViewCheck;
        TextView textViewHeader ;
        TextView textViewName ;
        View view;
        NestedScrollView nestedScrollView;

        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            textViewHeader = itemView.findViewById(R.id.text_setting);
            textViewName = itemView.findViewById(R.id.name);
            imageViewCheck = itemView.findViewById(R.id.imageView);
            nestedScrollView = itemView.findViewById(R.id.nestedScroll);

            view = itemView;


        }

    }
//    public void updateList(ArrayList<String> list){
//        arrayListNames = list;
//        notifyDataSetChanged();
//    }
}