package com.sirapp.SignupSignin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.sirapp.Base.BaseActivity;
import com.sirapp.OnBoardings.OnBoarding_Activity;
import com.sirapp.R;

public class WalkThroughActivity extends BaseActivity {

    private static final String TAG = "WalkThroughActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_walk);

        RelativeLayout relativeLayout = findViewById(R.id.relaive11);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WalkThroughActivity.this, OnBoarding_Activity.class));
                finish();
            }
        });


    }
}
