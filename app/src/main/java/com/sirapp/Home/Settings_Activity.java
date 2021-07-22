package com.sirapp.Home;

import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sirapp.Constant.Constant;
import com.sirapp.R;

public class Settings_Activity extends AppCompatActivity {

    TextView description;
    TextView invite,customize,faqs,language,about,rate,terms,privacy,support;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_);


        Constant.toolbar(Settings_Activity.this,"Settings");
        FindByIds();
        setFonts();
        setListeners();




    }
    private void FindByIds(){
        description= findViewById(R.id.description);
        invite = findViewById(R.id.invite);
        customize = findViewById(R.id.customize);
        faqs = findViewById(R.id.faqs);
        language = findViewById(R.id.language);
        about = findViewById(R.id.about);
        rate = findViewById(R.id.rate);
        terms = findViewById(R.id.terms);
        privacy = findViewById(R.id.privacy);
        support = findViewById(R.id.support);
    }

    private void setFonts(){
        description.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Light.otf"));
        invite.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Light.otf"));
        customize.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Light.otf"));
        faqs.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Light.otf"));
        language.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Light.otf"));
        about.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Light.otf"));
        rate.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Light.otf"));
        terms.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Light.otf"));
        privacy.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Light.otf"));
        support.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Light.otf"));

    }

    private void setListeners(){

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
