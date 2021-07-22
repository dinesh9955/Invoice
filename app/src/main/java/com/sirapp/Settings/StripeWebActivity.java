package com.sirapp.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sirapp.Base.BaseActivity;
import com.sirapp.R;

public class StripeWebActivity extends BaseActivity {

    private static final String TAG = "StripeWebActivity";
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stripe_web);

        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);

        TextView textViewDone = toolbar.findViewById(R.id.textViewDone);
        textViewDone.setVisibility(View.GONE);

        titleView.setText(getString(R.string.header_stripe));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        webView = findViewById(R.id.invoiceweb);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);


        webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        ca_FkyHCg7X8mlvCUdMDao4mMxagUfhIwXb
//        http://localhost:3000/stripe/callback?scope=read_only&code=ac_JdHKVQ65I3LYxfsZ6nnAQdp6tVPjres7
//        live id "ca_JDMy6eMrZX8v9mxheSVPTo9VasKhK7bS"
        webView.loadUrl("https://connect.stripe.com/oauth/v2/authorize?response_type=code&client_id=ca_JDMy6eMrZX8v9mxheSVPTo9VasKhK7bS");

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                Log.e(TAG, "onPageFinished "+url);

                String responseCode = "";
                if(url.contains("code=")){
                    responseCode = url.split("code=")[1];
                    Intent intent = new Intent();
                    intent.putExtra("responseCode" , ""+responseCode);
                    setResult(RESULT_OK , intent);
                    finish();
                }else{
                    responseCode = url;
                }



            }
        });
    }
}
