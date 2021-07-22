package com.sirapp.OnBoardings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sirapp.R;


public class OnBooarding_Fragment4 extends Fragment {


    public OnBooarding_Fragment4() {
        // Required empty public constructor
    }

    TextView txtonboardheading4,txtonboardingdes4;
    ImageView imgonboard4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_on_booarding__fragment4, container, false);
        txtonboardheading4 = view.findViewById(R.id.txtonboardheading4);
        txtonboardingdes4 = view.findViewById(R.id.txtonboardingdes4);
        imgonboard4 = view.findViewById(R.id.imgonboard4);

        fonts();

        return view;
    }


    public void fonts()
    {
        txtonboardheading4.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/AzoSans-Bold.otf"));
        txtonboardingdes4.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"Fonts/Ubuntu-Light.ttf"));
    }

}
