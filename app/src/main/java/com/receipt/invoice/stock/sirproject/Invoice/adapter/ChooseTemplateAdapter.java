package com.receipt.invoice.stock.sirproject.Invoice.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.receipt.invoice.stock.sirproject.Invoice.ChooseTemplate;
import com.receipt.invoice.stock.sirproject.R;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ChooseTemplateAdapter extends RecyclerView.Adapter<ChooseTemplateAdapter.ViewHolder>  {

    private static final String TAG = "MyBookingAdapter";

    ArrayList<String> alName = new ArrayList<String>();
    Activity context;

    //private SparseBooleanArray selectedItems = new SparseBooleanArray();
    int selected_position;

    private final ArrayList<Integer> selected = new ArrayList<>();

    String colorCode = "#ffffff";


    public ChooseTemplateAdapter(Activity context11, ArrayList<String> arrayList) {
        super();
        this.context = context11;
        this.alName = arrayList;

        //selected.add(0);

    }

    @Override
    public ChooseTemplateAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        final View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.choose_template_item, viewGroup, false);
        return new ChooseTemplateAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ChooseTemplateAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.webView.getSettings().setAllowFileAccess(true);
        viewHolder.webView.setWebChromeClient(new WebChromeClient());
        viewHolder.webView.getSettings().setSupportZoom(false);
        viewHolder.webView.getSettings().setBuiltInZoomControls(false);
        viewHolder.webView.getSettings().setDisplayZoomControls(false);

        viewHolder.webView.getSettings().setLoadsImagesAutomatically(true);
        viewHolder.webView.getSettings().setJavaScriptEnabled(true);
        viewHolder.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        viewHolder.webView.getSettings().setLoadWithOverviewMode(true);
        viewHolder.webView.getSettings().setUseWideViewPort(true);
        viewHolder.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        viewHolder.webView.setHorizontalScrollBarEnabled(false);
        viewHolder.webView.setVerticalScrollBarEnabled(false);

//        viewHolder.webView.getSettings().setTextZoom(200);
        viewHolder.webView.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);

        viewHolder.webView.getSettings().setUserAgentString("viewport");

        viewHolder.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                //if page loaded successfully then show print button
                //findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });

        //viewHolder.invoiceweb.loadDataWithBaseURL("file:///android_asset/invoice.html", "", "text/html", "UTF-8", null);
        //viewHolder.invoiceweb.loadUrl("http://www.google.com");
//        viewHolder.webView.loadUrl("file:///android_asset/invoice.html");

        File file = new File(alName.get(i));

        String content = null;
        try {
            content = IOUtils.toString(context.getAssets().open(file.getName().toString()))

                    .replaceAll("#799f6e", colorCode);

        } catch (IOException e) {
            e.printStackTrace();

        }

        Log.e("ViewInvoice__",content);

        viewHolder.webView.loadDataWithBaseURL(alName.get(i), content, "text/html", "UTF-8", null);




//        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


//        viewHolder.webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction()==MotionEvent.ACTION_UP){
//                    viewHolder.conit.setBackgroundColor(Color.CYAN);
//                    if (selected.isEmpty()){
//                        selected.add(i); // (done - see note)
//                    }else {
//                        int oldSelected = selected.get(0);
//                        selected.clear(); // (*1)... and here.
//                        selected.add(i);
//                        notifyItemChanged(oldSelected);
//                    }
//                }
//
//                return false;
//            }
//        });

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.conit.setBackgroundColor(Color.CYAN);
                ((ChooseTemplate)context).setSelected(i+1);
                if (selected.isEmpty()){
                    selected.add(i); // (done - see note)
                }else {
                    int oldSelected = selected.get(0);
                    selected.clear(); // (*1)... and here.
                    selected.add(i);
                    notifyItemChanged(oldSelected);
                }

               ((ChooseTemplate) context).onBackPressed();

            }

        });

        if (!selected.contains(i)){
            viewHolder.conit.setBackgroundColor(Color.LTGRAY);
        }
        else{
            viewHolder.conit.setBackgroundColor(Color.CYAN);
        }
    }


    @Override
    public int getItemCount() {
        return alName.size();
    }

    public void updateColor(String colorCode) {
        this.colorCode = colorCode;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View view11 = null;
        RelativeLayout conit, relativeLayout;
        WebView webView;

        public ViewHolder(View itemView) {
            super(itemView);
            view11 = itemView;
            conit = (RelativeLayout) itemView.findViewById(R.id.conit);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relt);
            webView = (WebView) itemView.findViewById(R.id.invoiceweb);

        }




    }



    public void updateData(ArrayList<String> arrayList2) {
        // TODO Auto-generated method stub
//            alName.clear();
//            alName.addAll(arrayList2);
        alName = arrayList2;
        notifyDataSetChanged();
    }


}