package com.sirapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
//                Uri newDeepLink = buildDeepLink(Uri.parse(DEEP_LINK_URL));
//
//                Log.e(TAG, "newDeepLink "+newDeepLink);

                String content1 = "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "  <head>\n" +
                        "    <title>Title of the document</title>\n" +
                        "    <style>\n" +
                        "      .button {\n" +
                        "        background-color: #1c87c9;\n" +
                        "        border: none;\n" +
                        "        color: white;\n" +
                        "        padding: 20px 34px;\n" +
                        "        text-align: center;\n" +
                        "        text-decoration: none;\n" +
                        "        display: inline-block;\n" +
                        "        font-size: 20px;\n" +
                        "        margin: 4px 2px;\n" +
                        "        cursor: pointer;\n" +
                        "      }\n" +
                        "    </style>\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <a href=\"" + "urlPaypal" + "\" class=\"button\">" + "linkWitch" + "</a>\n" +
                        "  </body>\n" +
                        "</html>";

                generateNoteOnSD(wwww.this , "track.html", ""+content1.toString());

            }
        });






    }



    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            Log.e(TAG, "gpxfile "+gpxfile.toString());
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
