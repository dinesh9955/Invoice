package com.sirapp.Invoice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sirapp.Invoice.adapter.ChooseTemplateAdapter;
import com.sirapp.Base.BaseActivity;
import com.sirapp.R;

import java.util.ArrayList;

public class ChooseTemplate extends BaseActivity {

    private static final String TAG = "ChooseTemplate";
    int selectedTemplate = 0;

    RecyclerView mRecyclerView;
//    RecyclerView.LayoutManager mLayoutManager;
    ChooseTemplateAdapter mAdapter;

    ArrayList<String> arrayList = new ArrayList<>();
    //SavePref pref = new SavePref();
     String colorCode = "#ffffff";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.choose_template);

//        pref.SavePref(ChooseTemplate.this);

        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);
        ImageView printimg = toolbar.findViewById(R.id.imageViewptint);
        printimg.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SimpleItemDecoration(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        colorCode = getIntent().getExtras().getString("companycolor");



        arrayList.add("file:///android_asset/template1.html");
        arrayList.add("file:///android_asset/template2.html");
        arrayList.add("file:///android_asset/template3.html");
        arrayList.add("file:///android_asset/template4.html");

        mAdapter = new ChooseTemplateAdapter(ChooseTemplate.this, arrayList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.updateColor(colorCode);

        titleView.setText(getString(R.string.header_choose_template));

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//
//        WebView webView = (WebView) findViewById(R.id.invoiceweb);
//
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
//
//        webView.getSettings().setBuiltInZoomControls(false);
//        webView.getSettings().setDisplayZoomControls(false);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webView.getSettings().setSupportZoom(false);
//
//
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                //  progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//
////                progressBar.setVisibility(View.INVISIBLE);
////                webView.loadUrl("javascript:document.body.style.padding= \"2%\"; void 0");
////                webView.setBackgroundColor(0x00000000);
////                webView.loadUrl(
////                        "javascript:document.body.style.setProperty(\"color\", \"white\");"
////                );
////                webview.loadUrl("javascript:document.body.style.color=\"white\";");
//            }
//        });
//
//        webView.loadUrl("file:///android_asset/invoice.html");

    }

    public void setSelected(int i) {
        selectedTemplate = i;
        Log.e(TAG, "selectedTemplate "+selectedTemplate);
    }

    @Override
    public void onBackPressed() {
        pref.setTemplate(selectedTemplate);
        super.onBackPressed();
    }
}
