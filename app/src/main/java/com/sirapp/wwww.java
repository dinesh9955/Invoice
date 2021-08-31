package com.sirapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

public class wwww extends AppCompatActivity {

    private String TAG = "wwww";
    private String DEEP_LINK_URL = "https://sirproject.page.link";

    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abc);

        button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri newDeepLink = buildDeepLink(Uri.parse(DEEP_LINK_URL));

                Log.e(TAG, "newDeepLink "+newDeepLink);

            }
        });






    }




   public Uri buildDeepLink(Uri deepLink) {
       DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
               .setLink(Uri.parse(deepLink.toString()))
               .setDomainUriPrefix("https://rastogi.page.link")
               // Open links with this app on Android
               .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.sirapp").build())
               // Open links with com.example.ios on iOS
               .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())

               .buildDynamicLink();

       Uri dynamicLinkUri = dynamicLink.getUri();

        return dynamicLinkUri;
    }
}
