package com.sirapp.Settings;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.sirapp.API.AllSirApi;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Constant.Constant;
import com.sirapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class WebShowActivity extends BaseActivity {

    private static final String TAG = "WebShowActivity";
    WebView webView;

    TextView titleView;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice_);

        Toolbar toolbar = findViewById(R.id.toolbarprint);
        titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);
        ImageView printimg = toolbar.findViewById(R.id.imageViewptint);
        printimg.setVisibility(View.GONE);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        webView = findViewById(R.id.invoiceweb);

        titleView.setText("");


        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);


        webView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);

        Bundle bundle = getIntent().getExtras();

        int key = bundle.getInt("positionNext");

        String url = "";
        if(key == 8){
            url = AllSirApi.BASE_LEVEL+"wp-content/api/about-app.php";
        } else if(key == 10){
            url = AllSirApi.BASE_LEVEL+"wp-content/api/term-of-use.php";
        }else if(key == 11){
            url = AllSirApi.BASE_LEVEL+"wp-content/api/privacy-policy.php";
        }

        companyget(url);

    }






    private void companyget(String url) {

        String token = Constant.GetSharedPreferences(WebShowActivity.this, Constant.ACCESS_TOKEN);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        client.addHeader("Access-Token", token);
        RequestParams params = new RequestParams();
        params.add("language", ""+getLanguage());
        params.add("device_type", "android");

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.e("responsecompany", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("message");
                        String pagetitle = jsonObjectMessage.getString("pagetitle");
                        String pagecontent = jsonObjectMessage.getString("pagecontent");

                        titleView.setText(pagetitle);


                        webView.setWebViewClient(new WebViewClient(){
                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                super.onPageStarted(view, url, favicon);
                               // progressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                return super.shouldOverrideUrlLoading(view, request);
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
//                                webView.loadUrl("document.getElementById('iosVer').style.display = 'none';");
//                                webView.loadUrl("document.getElementById('iosVer').style.display='none';})()");
                                webView.loadUrl("javascript:(function() { " +
                                        "document.getElementsByClassName('iosVer')[0].style.display='none'; })()");
                                webView.loadUrl("javascript:document.body.style.padding= \"2%\"; void 0");

                            }
                        });

                        //webView.setBackgroundColor(0x00000000);
                        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("javascript:document.body.style.backgroundColor ='red';");
       // webView.loadUrl("javascript:document.body.style.fontSize ='20pt'");
//        webView.loadUrl("javascript:document.body.style.color ='yellow';");


                        webView.loadData( pagecontent, "text/html", "UTF-8");
                       // webView.loadData(String.valueOf(Html.fromHtml("pagecontent")));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String response = new String(responseBody);
                    Log.e("responsecompanyF", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("false")) {
                            //Constant.ErrorToast(Home_Activity.this,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
