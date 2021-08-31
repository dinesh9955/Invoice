package com.sirapp;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.sirapp.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import project.aamir.sheikh.circletextview.CircleTextView;
import org.apache.commons.lang3.text.WordUtils;
public class Xyz extends AppCompatActivity {


    private static final String TAG = "Xyz";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abc);

        Button button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseDynamicLinks.getInstance()
//                        .getDynamicLink(getIntent())
//                        .addOnSuccessListener(Xyz.this, new OnSuccessListener<PendingDynamicLinkData>() {
//                                    @Override
//                                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                                        if (pendingDynamicLinkData != null) {
//                                            pendingDynamicLinkData.getLink();
//
//                                        }
//                                    }
//                                }
//                        );


                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                        .setLink(Uri.parse("https://www.sirproject.page.link/"))
                        .setLink(Uri.parse(buildDeepLink()))
                        .setDomainUriPrefix("https://sirproject.page.link/")
//                        .setDomainUriPrefix("https://sirproject.page.link/subscription")
                        .setAndroidParameters(
                                new DynamicLink.AndroidParameters.Builder("com.sirapp")
                                        .setFallbackUrl(Uri.parse("https://www.sirproject.page.link/"))
                                        .setMinimumVersion(1)
                                        .build())
                        .buildDynamicLink();

                Uri dynamicLinkUri = dynamicLink.getUri();

                Log.e(TAG, "dynamicLinkUri "+dynamicLinkUri);

                shareDynamicLink(dynamicLinkUri);

              /*  Intent shareIntent = new Intent();
                String msg = "Check this out: " + dynamicLinkUri;
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);*/



//                FirebaseDynamicLinks.getInstance()
//                        .getDynamicLink(getIntent())
//                        .addOnSuccessListener(Xyz.this, new OnSuccessListener<PendingDynamicLinkData>() {
//                                    @Override
//                                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                                        if (pendingDynamicLinkData != null) {
////                                            String uri = pendingDynamicLinkData.getExtensions().getQueryParameter("dynamic_link_link_name");
////
////
////                                            Gson gson = new Gson();
////                                            String json = gson.toJson(pendingDynamicLinkData);
////                                            Log.e(TAG , "onSuccess "+uri.toString());
//
//
//                                            Uri deepLink = null;
//                                            if(pendingDynamicLinkData != null){
//                                                deepLink = pendingDynamicLinkData.getLink();
//                                            }
//
//                                            // trying to get from the intent
//                                            if(deepLink != null){
//
//                                                Uri intentData = getIntent().getData();
//                                                if(intentData != null){
//
//
//
//                                                    String code =  getIntent().getStringExtra("subscribeID"); // always returns null
//                                                    Log.e(TAG , "onSuccess "+code.toString());
//                                                }
//                                            }
//
//                                        }
//                                    }
//                                }
//                        )
//                .addOnFailureListener(Xyz.this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG , "onFailure "+e.getMessage());
//                    }
//                })
//                ;

            }
        });


    }

    public String buildDeepLink() {
        // Get the unique appcode for this app.
        String appCode = getString(R.string.app_name);

        // Get this app's package name.
        String packageName = getPackageName();

        // Build the link with all required parameters
        Uri.Builder builder = new Uri.Builder()
                .scheme("https")
                .authority(appCode)
                .path("/")
                .appendQueryParameter("link", "abcsfd")
                .appendQueryParameter("apn", packageName);

        // Return the completed deep link.
        return builder.build().toString();
    }


    public void shareDynamicLink(Uri dynamicLink)
    {
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink)
                .buildShortDynamicLink()
                .addOnCompleteListener(Objects.requireNonNull(Xyz.this), new OnCompleteListener<ShortDynamicLink>()
                {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Short link created
                            Uri shortLink = Objects.requireNonNull(task.getResult()).getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Log.e("DynamicLink", "shortLink: " + shortLink + System.lineSeparator());
                            Log.e("DynamicLink", "flowChartLink: " + flowchartLink + System.lineSeparator());

                            Intent shareIntent = new Intent();
                            String msg = "Check this out: " + shortLink;
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
                            shareIntent.setType("text/plain");
                            startActivity(shareIntent);
                        }
                        else
                        {
                            Toast.makeText(Xyz.this, "Failed to share event.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    private void createDynamicLinkForPostShare(String eventId, String _id) {
//
//        Task<PendingDynamicLinkData> dynamicLink = FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent());
//        {
//            builder =
//                    Uri.Builder().scheme("https").authority(BuildConfig.APPLICATION_ID)
//                            .appendPath("params")
//                            .appendQueryParameter("eventId", eventId)
//                            .appendQueryParameter("_id", _id)
//            link = builder.build()
//            domainUriPrefix = "https://mmmtapp2.page.link/"
//            androidParameters(BuildConfig.APPLICATION_ID) {}
////            com.moments.com
//            iosParameters("com.MMNT-EditingVideo") {
//                appStoreId = "1493006990"
//            }
//        }
//
//
////        createShortLink(dynamicLink, true)
////        val dynamicLinkUri = dynamicLink.uri
////        Log.e("dynamicLinkUri", dynamicLinkUri.toString())
//    }
//
//    private fun createShortLink(dynamicLink: DynamicLink, isPost: Boolean) {
//        Firebase.dynamicLinks.shortLinkAsync {
//            longLink = Uri.parse(dynamicLink.uri.toString())
//        }.addOnSuccessListener { result ->
//                val shortLink = result.shortLink                 // Short link created
//            val flowchartLink = result.previewLink
//            viewModel.data.shareLink = shortLink
//
//        }.addOnFailureListener {
//            // Error
//            it.printStackTrace()
//            // ...
//        }
//    }
//



}
