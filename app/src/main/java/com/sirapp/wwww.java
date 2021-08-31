package com.sirapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

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


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.e(TAG, "getDynamicLinkYES"+deepLink);
                        }else{
                            Log.e(TAG, "getDynamicLinkNO");
                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });



    }




   public Uri buildDeepLink(Uri deepLink) {
       DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
               .setLink(Uri.parse(deepLink.toString()))
               .setDomainUriPrefix("https://rastogi.page.link")
               // Open links with this app on Android
           //    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.sirapp").build())
               // Open links with com.example.ios on iOS
             //  .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
               .buildDynamicLink();

       Uri dynamicLinkUri = dynamicLink.getUri();

        return dynamicLinkUri;
    }
}
